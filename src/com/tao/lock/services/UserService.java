package com.tao.lock.services;

import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import com.tao.lock.utils.Roles;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@DenyAll
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
	@RolesAllowed(Roles.ADMIN)
	public List<CloudUser> getAllUsers() {
		return userDao.getAllUsers();
	}
	
	// WebService#register needs this -> PermitAll
	@PermitAll
	public CloudUser getUserByName(String userName) {
		return userDao.getUserByUserName(userName);
	}
	
	/**
	 * Adds a User.
	 * @param user	User to be added.
	 */
	@RolesAllowed(Roles.EVERYONE)
	public CloudUser addUser(CloudUser user) {
		
		if (user == null)
			return null;
		
		return userDao.addUser(user);
	}
	
	// WebService#register needs this -> PermitAll
	@PermitAll
	public CloudUser update(CloudUser user) {
		
		if (user == null)
			return null;
		
		return userDao.updateUser(user);
	}
	
	/**
	 * Gets the local user from the sso-user.
	 * If there is no local user yet, it will create one.
	 * @param request
	 * @return CloudUser or Null if there is no user from SSO.
	 */
	@RolesAllowed(Roles.EVERYONE)
	public CloudUser getCloudUser(HttpServletRequest request) {
		
		if (request == null)
			return null;
		
		User ssoUser = null;
		CloudUser cloudUser = null;
		
		try {
			cloudUser = (CloudUser)request.getSession().getAttribute("user");
		} catch (Exception e) {
			LOGGER.error("User not in session!", e.getMessage());
		}
		
		if (cloudUser != null)
			return cloudUser;
		
		try {
			ssoUser = authorizationService.getUser(request);
		} catch (NamingException e) {
			LOGGER.error("User from SSO not found!", e.getMessage());
		}
		
		if (ssoUser == null)
			return null;
		
		// try finding the corresponding local user
		cloudUser = getUserByName(ssoUser.getName());
		
		if (cloudUser != null) {
			return cloudUser;
		}
		
		cloudUser = new CloudUser();
		cloudUser.setUserName(ssoUser.getName());
		
		// fill cloudUser with data from SSO
		try {
			cloudUser.setEmail(ssoUser.getAttribute("email"));
			cloudUser.setLastName(ssoUser.getAttribute("lastname"));
			cloudUser.setFirstName(ssoUser.getAttribute("firstname"));
		} catch (UnsupportedUserAttributeException e) {
			LOGGER.error("Attribute email missing!", e.getMessage());
		}
		
		return addUser(cloudUser);
	}

	@RolesAllowed(Roles.ADMIN)
	public CloudUser getUserById(long id) {
		return userDao.getUserById(id);
	}
}
