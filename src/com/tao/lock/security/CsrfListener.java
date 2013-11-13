package com.tao.lock.security;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.rest.WebService;

/**
 * Class to counter CSRF.
 * @author Joerg Hilscher
 *
 */
public class CsrfListener implements HttpSessionListener {

	public static final String CSRFTOKEN = "csrf";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CsrfListener.class);
	
	/**
	 * Generate token on sessionCreated and add it to the session.
	 */
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		
		HttpSession session = arg0.getSession();
		
		String randomId = "";
		
		try {
			randomId = SecurityUtils.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		session.setAttribute(CSRFTOKEN, randomId);
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
