package com.tao.lock.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.tao.lock.dao.UserDao;
import com.tao.lock.entities.CloudUser;
import com.tao.lock.security.AuthorizationService;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@EJB
	private UserDao userDao;
	
	@EJB
	private AuthorizationService authorizationService;
	
	/**
	 * GetAllUsers.
	 * @return list of all cloud-users.
	 */
	public List<CloudUser> getAllUsers() {
		return userDao.getAllUsers();
	}
	
	public CloudUser getUserByName(String userName) {
		return userDao.getUserByUserName(userName);
	}
	
	/**
	 * Adds a User.
	 * @param user	User to be added.
	 */
	public CloudUser addUser(CloudUser user) {
		
		return userDao.addUser(user);
	}
	
	public CloudUser update(CloudUser user) {
		return userDao.updateUser(user);
	}
	
	/**
	 * Gets the local user from the sso-user.
	 * If there is no local user yet, it will create one.
	 * @param request
	 * @return CloudUser or Null if there is no user from SSO.
	 */
	public CloudUser getCloudUser(HttpServletRequest request) {
		
		User ssoUser = null;
		
		try {
			ssoUser = authorizationService.getUser(request);
		} catch (NamingException e) {
			LOGGER.error("User from SSO not found!", e.getMessage());
		}
		
		if (ssoUser == null)
			return null;
		
		// try finding the coresponding local user
		CloudUser cloudUser = getUserByName(ssoUser.getName());
		
		if (cloudUser != null)
			return cloudUser;
		
		cloudUser = new CloudUser();
		cloudUser.setUserName(ssoUser.getName());
		
		
		try {
			cloudUser.setEmail(ssoUser.getAttribute("email"));
		} catch (UnsupportedUserAttributeException e) {
			LOGGER.error("Attribute email missing!", e.getMessage());
		}
		
		return addUser(cloudUser);
	}
}
