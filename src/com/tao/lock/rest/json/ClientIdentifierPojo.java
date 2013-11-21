package com.tao.lock.rest.json;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 * Pojo / JSON Container
 * @author Joerg Hilscher
 *
 */
@XmlRootElement
public class ClientIdentifierPojo {

	
	@Expose
    private String userName;
        
    /**
     * hashed and salted client id.
     * No need to store this in plain text.
     */
    @Expose
    private String clientId;

    @Expose
    private Date created;

    @Expose
    private Date loginAttempt;

    @Expose
    private String ipAdress;
    
    @Expose
    private String userAgent;
    
    @Expose
    private String secret;
    
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientIdentifierPojo other = (ClientIdentifierPojo) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLoginAttempt() {
		return loginAttempt;
	}

	public void setLoginAttempt(Date loginAttempt) {
		this.loginAttempt = loginAttempt;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "ClientIdentifierPojo [userName=" + userName
				+ ", hashedClientId=" + clientId + ", created=" + created
				+ ", loginAttempt=" + loginAttempt + ", ipAdress=" + ipAdress
				+ ", userAgent=" + userAgent + ", secret=" + secret + "]";
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
