package com.tao.lock.security;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.qrservice.QRUtils;

/**
 * Filter to restrict access to QR-Code Image Files.
 * @author Joerg Hilscher
 *
 */
@WebFilter("/qrcodes/*")
public class QRCodeFilter implements Filter {
	
	
		private static final Logger LOGGER = LoggerFactory.getLogger(QRCodeFilter.class);
		
		private final String QRCODE_KEY = "qrcode";
	
		@Override
		public void destroy() {

			
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			
			// get session
			HttpSession session = req.getSession();
			
			// get url
			String url = req.getRequestURI();
			
			LOGGER.info("Someone is trying to access a resouce. URL: " + url);
			
			String filename = QRUtils.getFilenameFromUrl(url);
			

			if (session.getAttribute(QRCODE_KEY) == null  
					|| session.getAttribute(QRCODE_KEY).toString() == null)
				res.sendError(401);
			else if (session.getAttribute(QRCODE_KEY).toString().equals(filename))
				chain.doFilter(req, res);
			else {
				res.sendError(401);
			}
			
			
		}

	    @Override
	    public void init(FilterConfig filterConfig) {   }

}

