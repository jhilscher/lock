package com.tao.lock.rest.json;

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
     * x1.
     * Must be store in plain text.
     */
    @Expose
    private String secret;
    
    /**
     * random salt.
     */
    @Expose
    private String salt;
    
    /**
     * hashed and salted client id.
     * No need to store this in plain text.
     */
    @Expose
    private String hashedClientId;

//    @Expose
//    private Date created;
//
//    @Expose
//    private Date loginAttempt;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHashedClientId() {
		return hashedClientId;
	}

	public void setHashedClientId(String hashedClientId) {
		this.hashedClientId = hashedClientId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hashedClientId == null) ? 0 : hashedClientId.hashCode());
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
		if (hashedClientId == null) {
			if (other.hashedClientId != null)
				return false;
		} else if (!hashedClientId.equals(other.hashedClientId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
