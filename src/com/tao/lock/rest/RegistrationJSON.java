package com.tao.lock.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO used as JSON container.
 * @author Joerg Hilscher
 *
 */
@XmlRootElement
public class RegistrationJSON {

	private String x1;
	private String clientIdKey;
	
	public String getX1() {
		return x1;
	}
	public void setX1(String x_1) {
		this.x1 = x_1;
	}
	public String getClientIdKey() {
		return clientIdKey;
	}
	public void setClientIdKey(String clientIdKey) {
		this.clientIdKey = clientIdKey;
	}
	
}
