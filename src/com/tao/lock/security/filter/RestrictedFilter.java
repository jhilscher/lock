package com.tao.lock.security.filter;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.rest.json.ClientIdentifierPojo;
import com.tao.lock.services.ConnectionService;
import com.tao.lock.services.UserService;

/**
 * FROM: http://www.oraclejavamagazine-digital.com/javamagazine_open/20130102#pg64
 * @author Joerg Hilscher
 * 
 * This Filter intercepts any request to the path.
 * It checks the session for the attribute "auth".
 * If not, then redirects to the index-page.
 */
@WebFilter("/restricted/*")
public class RestrictedFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestrictedFilter.class);
	
	@EJB
	private UserService userService;

	@EJB
	private ConnectionService connectionService;
	
	@Override
	public void destroy() { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		
		CloudUser cloudUser = userService.getCloudUser(req);
		
		// cancel if user is unknown or not registered
		if (cloudUser == null || !cloudUser.getIsRegistered()) {
			res.sendRedirect("/lock/unauthorized.xhtml");
		}
		
		// TODO: put in utitlity class
		if (cloudUser.getSecurityLevel() == 2) {
			
			ClientIdentifierPojo clientIdentifierPojo = new ClientIdentifierPojo();
			clientIdentifierPojo.setUserName(cloudUser.getUserName());
			clientIdentifierPojo.setIpAdress(req.getRemoteAddr());
			
			Double lvl = connectionService.getRiskLevel(clientIdentifierPojo);
			
			LOGGER.info("Risklevel of " + cloudUser.getUserName() + " :" + lvl);
			
			if (lvl > 0.0) {
				chain.doFilter(req, res);
				return;
			}
		}
		
		
		// check
		if ((String) session.getAttribute("auth") == "true")
			chain.doFilter(req, res);
		else
			res.sendRedirect("/lock/#locklogin");
		
	}

    @Override
    public void init(FilterConfig filterConfig) {   }

}

