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
	
	private static final int ITERATIONS = 1000;
	
	private static final int BYTE_SIZE = 64;
	
	@EJB
	private RegistrationHandler registrationHandler;
	
	@EJB
	private AuthentificationHandler authentificationHandler;
	
	@EJB
	private ConnectionService connectionService;
	
	/**
	 * 
	 * @param request	HttpServletRequest
	 * @return URL of the QR-Code, or null
	 */
	@SuppressWarnings("static-access")
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
					String hashedValue = SecurityUtils.pbkdf2(clientIdKey.toCharArray(), salt, ITERATIONS, BYTE_SIZE);
					
					// add to pojo
					ClientIdentifierPojo id1 = new ClientIdentifierPojo();
					id1.setSalt(SecurityUtils.toHex(salt));
					id1.setHashedClientId(hashedValue);
					id1.setCreated(new Date());
					id1.setUserName(cloudUser.getUserName());
					
					// set registered flag
					cloudUser.setIsRegistered(false);

					// TODO: Test later
					registrationHandler.addToWaitList(id1, clientIdKey, qrUtils);
					
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
			

			if (!cloudUser.getIsRegistered())	{
				return null;
			}
			
			ClientIdentifierPojo clientIdentifierPojo = connectionService.getClientIdentifier(cloudUser.getUserName());
			
			if (clientIdentifierPojo == null || clientIdentifierPojo.getSecret() == null)
				return null;
			
			// if authed
			if((String)request.getSession(false).getAttribute("auth") == "true") {
				return null;
			}
			
			cloudUser.setSession(request.getSession(false));
			
			// generate a token
			String token = SecurityUtils.generateKey();
			
			// get x_1 of user
			byte[] x_1 = SecurityUtils.fromHex(clientIdentifierPojo.getSecret());
			
			// XOR it
			byte[] alpha = SecurityUtils.xor(SecurityUtils.fromHex(token), x_1);
			
			// new Date
			Date t1 = new Date();
			
			//                                                        v Time in milliseconds
			url = qrUtils.renderQR(SecurityUtils.toHex(alpha) + "#" + t1.getTime());
			
			// hashed token
			String hashedToeken =  SecurityUtils.pbkdf2(token.toCharArray(), (String.valueOf(t1.getTime())).getBytes(), ITERATIONS, BYTE_SIZE);
	
			
			// send to authHandler
			authentificationHandler.addToWaitList(cloudUser, hashedToeken, qrUtils);

			
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("Could not find algorithm", e.getMessage());
		} catch (InvalidKeySpecException e) {
			LOGGER.error("InvalidKeySpecException", e.getMessage());
		}  

		return url;
	}
	
}
