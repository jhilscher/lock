package com.tao.lock.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.security.CsrfListener;


/**
 * Filter to prevent CSRF.
 * @author Joerg Hilscher
 *
 */
public class CsrfFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfFilter.class);
	
	private FilterConfig filterConfig = null;
	
	private static final String PARAM_EXCLUDE = "exclude";
	
	private static final String PARAM_ENTRY_POINTS = "entryPoints"; // currently not used
	
	
	@Override
	public void destroy() { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		/**
		 * check for excludes
		 */
		String allExcludes = filterConfig.getInitParameter(PARAM_EXCLUDE);
		String[] exludeEntries = allExcludes.split(",");
		String currentUrl = req.getRequestURI();
		
		LOGGER.info("Trying to access " + currentUrl);
		
		for (int i = 0; i < exludeEntries.length; i++) {
			
			// FIXME: make this better
			if (currentUrl.contains(exludeEntries[i])) {
				chain.doFilter(req, res); 
				return;
			}	
		}
		
		// get session
		HttpSession session = req.getSession();
		
		// check if session has token
		if (session.getAttribute(CsrfListener.CSRFTOKEN) == null  
				|| session.getAttribute(CsrfListener.CSRFTOKEN).toString() == null) {
			
			LOGGER.info("CSRF Filter: no attribute set.");
			
			res.sendError(400); 
			return;
		}
		
		// get the parameter
		String parameter = req.getParameter(CsrfListener.CSRFTOKEN);
		
		
		if (session.getAttribute(CsrfListener.CSRFTOKEN).toString().equals(parameter)) {
			chain.doFilter(req, res); 
		} else {
			res.sendError(400); // send statuscode 400
		}
	

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}
