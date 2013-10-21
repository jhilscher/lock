package com.tao.lock.security;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.services.UserService;

/**
 *
 * @author Joerg Hilscher
 * 
 * This filter is for general calls to the website.
 * This should map every user with the role "Everyone"
 * to a local user.
 *
 */
@WebFilter("/general/*")
public class GeneralFilter implements Filter {

	@EJB
	private UserService userService;

	@Override
	public void destroy() {

		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		// FIXME
		CloudUser cloudUser = userService.getCloudUser(req);
		req.getSession().setAttribute("user", cloudUser);
		
		
		chain.doFilter(req, res);
		
	}

    @Override
    public void init(FilterConfig filterConfig) {   }

}

