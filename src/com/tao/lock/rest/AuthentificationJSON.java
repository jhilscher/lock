package com.tao.lock.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO used as JSON container.
 * @author Joerg Hilscher
 *
 */
@XmlRootElement
public class AuthentificationJSON {

	private String x1;
	private String clientIdKey;
	private String timeStamp;

	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getClientIdKey() {
		return clientIdKey;
	}
	public void setClientIdKey(String clientIdKey) {
		this.clientIdKey = clientIdKey;
	}
	public String getX1() {
		return x1;
	}
	public void setX1(String x1) {
		this.x1 = x1;
	}
	
	

}
