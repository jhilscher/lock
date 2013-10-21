package com.tao.lock.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.ClientIdentifier;
import com.tao.lock.entities.CloudUser;
import com.tao.lock.qrservice.QRUtils;
import com.tao.lock.security.AuthentificationHandler;
import com.tao.lock.security.RegistrationHandler;
import com.tao.lock.security.SecurityUtils;
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
	private RegistrationHandler registrationHandler;
	
	@EJB
	private AuthentificationHandler authentificationHandler;
	

	
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
		
				String url = null;
				QRUtils qrUtils = new QRUtils();
				qrUtils.setContext(context);	
				
				try {
					

					// attach session to this user
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
					
					// tell gc to remove secret
					clientIdKey = null;
					
				} catch (NoSuchAlgorithmException e) {
					LOGGER.error("Could not find algorithm", e.getMessage());
				}  catch (InvalidKeySpecException e) {
					LOGGER.error("Exception while hashing", e.getMessage());
				} 

				return url;
	}
	
	@RolesAllowed(Roles.MANAGER)
	public String authentificateUser(HttpServletRequest request, ServletContext context, CloudUser cloudUser) {

		if (request == null || context == null || cloudUser == null)
			return null;
		
		QRUtils qrUtils = new QRUtils();
		qrUtils.setContext(context);
		
		String url = null;
		
		try {
			

			if (cloudUser.getIdentifier() == null || cloudUser.getIdentifier().getSecret() == null)	{
				return null;
			}
			
			
			// if authed
			if((String)request.getSession(false).getAttribute("auth") == "true") {
				return null;
			}
			
			cloudUser.setSession(request.getSession(false));
			
			// generate a token
			String token = SecurityUtils.generateKey();
			
			// get x_1 of user
			byte[] x_1 = SecurityUtils.fromHex(cloudUser.getIdentifier().getSecret());
			
			// XOR it
			byte[] alpha = SecurityUtils.xor(SecurityUtils.fromHex(token), x_1);
			
			
			url = qrUtils.renderQR(SecurityUtils.toHex(alpha) + "#" + (new Date()).hashCode());
			
			// send to authHandler
			authentificationHandler.addToWaitList(cloudUser, token, qrUtils);

			// FIXME: remove 
			// errorMsg = "Token: " + token;
			// errorMsg += "<br> Re Xored: " + SecurityUtils.toHex(SecurityUtils.xor(alpha, x_1));
			// errorMsg += " --";
			
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Could not find algorithm", e.getMessage());
		}  

		return url;
	}
	
}
