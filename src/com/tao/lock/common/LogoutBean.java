package com.tao.lock.common;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import com.sap.security.auth.login.LoginContextFactory;

/**
 * Bean to simply log a user out.
 * Logs the user out from SSO and Lock.
 * @author Joerg Hilscher
 *
 */
@Named
@SessionScoped
public class LogoutBean {

	private String message;
	
	@PostConstruct
	public void init() {
		
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		//Call logout if the user is logged in
		  LoginContext loginContext = null;
		  if (request.getRemoteUser() != null) { 

		    try { 
		      loginContext = LoginContextFactory.createLoginContext(); 
		      loginContext.logout();
		     
		    } catch (LoginException e) { 
		      // Servlet container handles the login exception
		      // It throws it to the application for its information
		    	message = "Logout failed. Reason: " + e.getMessage(); 
		    }
		   } else {
			   	message = "You have successfully logged out."; 
		   }
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
