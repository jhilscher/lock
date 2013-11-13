package com.tao.lock.common;

import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.lock.security.CsrfListener;

@Named
@SessionScoped
public class TokenMapper {


	@PostConstruct
	public void init() {
		
		
	}


	public String getCsrfToken() {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		return (String) request.getSession().getAttribute(CsrfListener.CSRFTOKEN);
	}
	

}
