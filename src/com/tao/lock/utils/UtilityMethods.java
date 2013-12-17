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
	 * Gets the IP Adress of a user.
	 * More difficult since the service is behind a gateway.
	 * 
	 * @param request
	 * @return IPAdress as String.
	 */
	public static String getIpAdess(HttpServletRequest request) {
		String ipAddress=null;
		String getWay = request.getHeader("VIA");   
		ipAddress = request.getHeader("X-FORWARDED-FOR");   
		if(ipAddress==null)
		{
		    ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}
	
	/**
	 * Gets a short UserAgent String with Browser-Name and OS-Name.
	 * @param request
	 * @return
	 */
	public static String getShortUserAgentString(HttpServletRequest request) {
		String userAgentString = request.getHeader("User-Agent");
		
		if (userAgentString == null || userAgentString == "")
			return ">> User-Agent not set";
		
				
		UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
		return userAgent.getBrowser().getName() + " on " + userAgent.getOperatingSystem().getName();
	}
}
