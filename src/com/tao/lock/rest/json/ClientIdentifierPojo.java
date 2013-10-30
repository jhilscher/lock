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

    @Expose
    private Date created;

    @Expose
    private Date loginAttempt;

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
    
	
	
    
}
