package com.tao.lock.rest.json;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 * POJO used as JSON container.
 * @author Joerg Hilscher
 *
 */
@XmlRootElement
public class RegistrationJSON {

	@Expose
	private String x1;
	
	@Expose
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
