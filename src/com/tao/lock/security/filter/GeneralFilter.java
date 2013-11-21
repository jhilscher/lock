package com.tao.lock.security.filter;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class GeneralFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralFilter.class);
	
	@EJB
	private UserService userService;

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		try {
			
			CloudUser cloudUser = userService.getCloudUser(req);
			
			if (cloudUser != null) 
				req.getSession().setAttribute("user", cloudUser);
			
			// block, if sec-level is on "always".
			if (cloudUser.getSecurityLevel() == 1) {
				
				// check path
				String path = req.getRequestURI();
				
				// if on login-path
				if (path.endsWith("login.xhtml") || path.endsWith("/auth") || path.endsWith("/getauthqr") ) {
					chain.doFilter(req, res);
					return;
				} 
				
				
//				if ((String) req.getSession().getAttribute("auth") != "true") {
//					res.sendRedirect("/lock/login.xhtml");
//					return;
//				}
			}
			
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		
		chain.doFilter(req, res);
		
	}

    @Override
    public void init(FilterConfig filterConfig) {   }

}

