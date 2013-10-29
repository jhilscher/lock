package com.tao.lock.security;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;




/**
 * 
 * @author Joerg Hilscher
 * Service for authorizing the SSO-User. 	
 *
 */
@Stateless
@LocalBean
public class AuthorizationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);
	
	// geht nicht
	//@Resource
	//private UserProvider userProvider;

	
	public AuthorizationService() {};
	
	/**
	 * 
	 * @param role
	 * @param request
	 * @return true if user has role, false if user is null or does not have the role
	 */
	public Boolean isUserInRole(String role, HttpServletRequest request) {
		User user = null;
		
		try {
			user = getUser(request);
		} catch (NamingException e) {
			LOGGER.error(e.getMessage(), e);
		}
	
		if (user != null) {
			return user.hasRole(role);
		}
		
		return false;
	}

	/**
	 * Gets a logged in (SSO) user from the request.
	 * @param request
	 * @return User or Null.
	 * @throws NamingException
	 */
	public User getUser(HttpServletRequest request) throws NamingException {
		User user = null;
		
		// check if the user is logged in
		if (request.getUserPrincipal() != null) {
		
			try {
				UserProvider userProvider = UserManagementAccessor.getUserProvider();
				user = userProvider.getUser(request.getUserPrincipal().getName());
				
			} catch (PersistenceException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		
		return user;
	}
	

}
