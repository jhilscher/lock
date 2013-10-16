package com.tao.lock.common;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.services.UserService;

@SessionScoped
@Named
public class AdminBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminBean.class);

	@EJB
	UserService userService;
	
	/**
	 * Members for displaying in jfs.
	 */
	private String output = "initial";
	
	private List<CloudUser> users;
	
	private String userName;
	
	private String email;
	
	/**
	 * Triggered after initialization.
	 */
	@PostConstruct
	public void init() {
		listAllUsers();
	}
	
	/**
	 * Default Constructor.
	 */
	public AdminBean() { }
		
	/**
	 * Adds a local user.
	 * TODO: remove.
	 * @return
	 */
	public String addUser() {
		
		if (userName != null && !userName.isEmpty() && email != null && !email.isEmpty()) {
		
			CloudUser user = new CloudUser();
			user.setUserName(userName);
			user.setEmail(email);
	
			LOGGER.info("Added Person " + user.toString());
			
			userService.addUser(user);
			
			return "true";
		}
		
		LOGGER.info("Could not add user. username or email null or empty.");
		return "false";
	}
	
	public String removeId(String arg) {
		long id = Long.parseLong(arg);
		
		userService.removeIdentifierFromUser(id);
		
		return "true";
	}
	
	public void listAllUsers() {
		users = userService.getAllUsers();
	}
	
	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public List<CloudUser> getPersons() {
		return users;
	}

	public void setPersons(List<CloudUser> persons) {
		this.users = persons;
	}

	public List<CloudUser> getUsers() {
		return users;
	}

	public void setUsers(List<CloudUser> users) {
		this.users = users;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
