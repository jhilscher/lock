package com.tao.lock.common;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.security.AuthentificationHandler;
import com.tao.lock.security.SecurityUtils;
import com.tao.lock.services.UserService;
import com.tao.lock.utils.QRUtils;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@SessionScoped
@Named
public class AuthBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthBean.class);


	@EJB
	UserService userService;

	@EJB
	private AuthentificationHandler authentificationHandler;
	
	private String errorMsg;
	
	private String url;
	
	/**
	 * Triggered after initialization.
	 */
	@PostConstruct
	public void init() {
		generateAuthQRCode();
	}
	
	@SuppressWarnings("static-access")
	public String generateAuthQRCode() {
			
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		
		
		QRUtils qrUtils = new QRUtils();

		try {

			// get the current user
			CloudUser cloudUser = userService.getCloudUser(request);
			
			// check, if the user is there
			if (cloudUser == null) {
				errorMsg = "Unknown User!";
				return "false";
			}
			

			if (cloudUser.getIdentifier() == null || cloudUser.getIdentifier().getSecret() == null)	{
				errorMsg = "User not registered!";
				return "false";
			}
			
			
			// if authed
			if((String)request.getSession(false).getAttribute("auth") == "true") {
				try {
					// redirect to restricted area
					response.sendRedirect("/lock/restricted");
				} catch (IOException e) {

				}
				
				return "false";
			}
			
			cloudUser.setSession(request.getSession(false));
			
			// generate a token
			String token = SecurityUtils.generateKey();
			
			// get x_1 of user
			byte[] x_1 = SecurityUtils.fromHex(cloudUser.getIdentifier().getSecret());
			
			// XOR it
			byte[] alpha = SecurityUtils.xor(SecurityUtils.fromHex(token), x_1);
			
			// t1
			Date t1 = new Date();
			
			url = qrUtils.renderQR(SecurityUtils.toHex(alpha) + "#" + (new Date()).hashCode());

			cloudUser.getIdentifier().setLoginAttempt(t1);
			
			// send to authHandler
			authentificationHandler.addToWaitList(cloudUser, token, qrUtils);

			// FIXME: remove 
			// errorMsg = "Token: " + token;
			// errorMsg += "<br> Re Xored: " + SecurityUtils.toHex(SecurityUtils.xor(alpha, x_1));
			// errorMsg += " --";
			
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Could not find algorithm", e.getMessage());
			errorMsg += e.getMessage();
		}  

		return "true";
	}

	
	/**
	 * Default Constructor.
	 */
	public AuthBean() { }

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
		
	
}
