package com.tao.lock.common;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.tao.lock.security.CsrfListener;
import com.tao.lock.services.AuthorizationService;
import com.tao.lock.utils.Roles;

/**
 * 
 * @author Joerg Hilscher
 * 
 * Maps the CSRF-Token to JavaScript.
 *
 */
@Named
@SessionScoped
public class TokenMapper {

	@EJB
	AuthorizationService authService;

	@PostConstruct
	public void init() {
		
		
	}

	public String getCsrfToken() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return (String) request.getSession().getAttribute(CsrfListener.CSRFTOKEN);
	}
	
	public Boolean isUserAdmin() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		return authService.isUserInRole(Roles.ADMIN, request);
	}

}
