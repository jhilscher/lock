package com.tao.lock.security.filter;

import java.io.IOException;

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


	@Override
	public void destroy() { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession session = req.getSession();
		
		// check
		if ((String) session.getAttribute("auth") != "true")
			res.sendRedirect("/lock/unauthorized.xhtml");
		else
			chain.doFilter(req, res);
		
	}

    @Override
    public void init(FilterConfig filterConfig) {   }

}

