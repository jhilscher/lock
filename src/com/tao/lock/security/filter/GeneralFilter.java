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
import com.tao.lock.rest.json.ClientIdentifierPojo;
import com.tao.lock.services.ConnectionService;
import com.tao.lock.services.UserService;
import com.tao.lock.utils.UtilityMethods;

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

	@EJB
	private ConnectionService connectionService;
	
	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		
		
		try {
			
			// check path
			String path = req.getRequestURI();
			
			// if on login-path, or on api for mobile access
			if (path.endsWith("login.xhtml") || path.endsWith("/auth") 
					|| path.endsWith("/getauthqr") || path.contains("/qrcodes/") || path.endsWith("/authpolling")) {
				chain.doFilter(req, res);
				return;
			} 
			
			CloudUser cloudUser = userService.getCloudUser(req);
			
			if (userService.isUserAuthed(req)) {
				chain.doFilter(req, res);
				return;
			}
			
			// block, if sec-level is on "always".
			if (cloudUser.getSecurityLevel() == 1) {
					res.sendRedirect("/lock/login.xhtml");
					return;			
			// check if risk based auth	
			} else if (cloudUser.getSecurityLevel() == 2) {
				
				ClientIdentifierPojo clientIdentifierPojo = new ClientIdentifierPojo();
				clientIdentifierPojo.setUserName(cloudUser.getUserName());
				clientIdentifierPojo.setIpAdress(UtilityMethods.getIpAdess(req));
				
				Double lvl = connectionService.getRiskLevel(clientIdentifierPojo);
				
				LOGGER.info("Risklevel of " + cloudUser.getUserName() + " :" + lvl);
				
				if (lvl > 0.0001) { // risk ok, let it pass
		    		// Auth user to session
		    		req.getSession().setAttribute("auth", true);
					chain.doFilter(req, res);
					
				} else { // you shall not pass
					res.sendRedirect("/lock/login.xhtml");
				}
				
				return;
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

