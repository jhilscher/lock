package com.tao.lock.rest.json;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
public class UserLogJSON {

	@Expose
	private String userAgent;
	
	@Expose
	private String ipAdress;
	
	@Expose
	private Date timeStamp;
	
	@Expose
	private int success;

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}
	
}
