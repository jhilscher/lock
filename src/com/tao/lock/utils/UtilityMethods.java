package com.tao.lock.utils;

import javax.servlet.http.HttpServletRequest;

import nl.bitwalker.useragentutils.UserAgent;

/**
 * General Methods.
 * @author Joerg Hilscher
 *
 */
public class UtilityMethods {

	
	public static String getUserBrowser(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		return userAgent.getBrowser().getName();
	}
	
	public static String getUserOS(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		return userAgent.getOperatingSystem().getName();
	}
	
	/**
	 * Gets a short UserAgent String with Browser-Name and OS-Name.
	 * @param request
	 * @return
	 */
	public static String getShortUserAgentString(HttpServletRequest request) {
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		return userAgent.getBrowser().getName() + " on " + userAgent.getOperatingSystem().getName();
	}
}
