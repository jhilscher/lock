package com.tao.lock.services;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.rest.json.ClientIdentifierPojo;
import com.tao.lock.security.AuthentificationHandler;
import com.tao.lock.security.RegistrationHandler;
import com.tao.lock.security.SecurityUtils;
import com.tao.lock.utils.QRUtils;
import com.tao.lock.utils.Roles;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@RolesAllowed(Roles.MANAGER)
@Stateless
public class AuthentificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationService.class);
	

	@EJB
	private ConnectionService connectionService;
	
	/**
	 * 
	 * @param request	HttpServletRequest
	 * @return URL of the QR-Code, or null
	 */
	@RolesAllowed(Roles.MANAGER)
	public String registerUser(HttpServletRequest request, ServletContext context, CloudUser cloudUser) {

				if (cloudUser == null || request == null) {
					return null;
				}
		
				// QR-Code
				String url = null;
				QRUtils qrUtils = new QRUtils();
				qrUtils.setContext(context);	
				
				
				// Build Pojo
				ClientIdentifierPojo id1 = new ClientIdentifierPojo();
				id1.setUserName(cloudUser.getUserName());

				// Call to SCC
				String ID_A = connectionService.registerRequest(id1);
				
				// attach session to this user
				cloudUser.setSession(request.getSession());
				
				// set registered flag
				cloudUser.setIsRegistered(false);
				
				// TODO: Test later
				RegistrationHandler.addToWaitList(id1, ID_A, qrUtils);
				
				// Get url
				url = qrUtils.renderQR(ID_A);

				return url;
	}
	
	/**
	 * Request Auth for a user.
	 * Requests Token from SCC.
	 * 
	 * @param request
	 * @param context
	 * @param cloudUser
	 * @return URL of the image with the qr-code.
	 */
	@RolesAllowed(Roles.MANAGER)
	public String authentificateUser(HttpServletRequest request, ServletContext context, CloudUser cloudUser) {

		if (request == null || context == null || cloudUser == null)
			return null;
		
		QRUtils qrUtils = new QRUtils();
		qrUtils.setContext(context);
		
		String url = null;
		
		if (!cloudUser.getIsRegistered())	{
			return null;
		}
		
		
		// if authed
		if((String)request.getSession(false).getAttribute("auth") == "true") {
			return null;
		}
		
		cloudUser.setSession(request.getSession(false));
		
		String token = connectionService.requestToken(cloudUser.getUserName());

		url = qrUtils.renderQR(token);


		LOGGER.info("TestToken: " + token);

		AuthentificationHandler.addToWaitList(cloudUser, cloudUser.getUserName(), qrUtils);
			

		return url;
	}
	
}
