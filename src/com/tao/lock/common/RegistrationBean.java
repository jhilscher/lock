package com.tao.lock.common;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.tao.lock.entities.CloudUser;
import com.tao.lock.entities.ClientIdentifier;
import com.tao.lock.security.AuthorizationService;
import com.tao.lock.security.RegistrationHandler;
import com.tao.lock.security.SecurityUtils;
import com.tao.lock.services.UserService;
import com.tao.lock.utils.QRUtils;

@Named
@SessionScoped
public class RegistrationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	 
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationBean.class);
	
	private String url;
	
	private String errorMsg;
	
	private QRUtils qrUtils = new QRUtils();
	
	@EJB
	private UserService userService;
	
	@EJB
	private RegistrationHandler registrationHandler;
	
	@PostConstruct
	public void init() {
		generateRegistrationQRCode();
	}

	
	@SuppressWarnings("static-access")
	public String generateRegistrationQRCode() {
		
		// get the request
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	
		
		try {

			// get the current user
			CloudUser cloudUser = userService.getCloudUser(request);
			
			if (cloudUser == null) {
				errorMsg = "Unknown User!";
				return "false";
			}

			cloudUser.setSession(request.getSession());
			
			// generate a clientIdKey (ID_A)
			String clientIdKey = SecurityUtils.generateKey();
			
			url = qrUtils.renderQR(clientIdKey);


			byte[] salt = SecurityUtils.generateSalt(64);
			
			// securely hash the clientidkey
			String hashedValue = SecurityUtils.pbkdf2(clientIdKey.toCharArray(), salt, 1000, 64);
			
			// TODO: ??? Save this stuff here, or after confirm, and keep it in memory till then
			// save to db
			ClientIdentifier id1 = new ClientIdentifier();
			id1.setSalt(SecurityUtils.toHex(salt));
			id1.setHashedClientId(hashedValue);
			id1.setCreated(new Date());

			cloudUser.setIdentifier(id1);
			
			// TODO: Test later
			registrationHandler.addToWaitList(cloudUser, clientIdKey, qrUtils);
			
			// TODO: remove
			errorMsg = clientIdKey;
			
			// tell gc to remove secret
			clientIdKey = null;
			
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Could not find algorithm", e.getMessage());
			//errorMsg += e.getMessage();
		}  catch (InvalidKeySpecException e) {
			LOGGER.error("Exception while hashing", e.getMessage());
			//errorMsg += e.getMessage();
		} 

		return "true";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
