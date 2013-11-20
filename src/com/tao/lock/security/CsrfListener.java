package com.tao.lock.security;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Class to counter CSRF.
 * @author Joerg Hilscher
 *
 */
public class CsrfListener implements HttpSessionListener {

	public static final String CSRFTOKEN = "csrf";
	
	
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
	public void sessionDestroyed(HttpSessionEvent arg0) {}

}
