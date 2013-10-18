package com.tao.lock.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "T_CLIENTIDENTIFIER")
@NamedQueries({
	@NamedQuery(name = ClientIdentifier.QUERY_ALLIDENTIFIERS, query = "SELECT i FROM ClientIdentifier i")
})
public class ClientIdentifier {

	// Queries
	public static final String QUERY_ALLIDENTIFIERS = "getAllIdentifiers";
	
    @Id
    @GeneratedValue
    private Long identifierId;
    
    /**
     * x1.
     * Must be store in plain text.
     */
    @Column
    private String secret;
    
    /**
     * random salt.
     */
    @Column
    private String salt;
    
    /**
     * hashed and salted client id.
     * No need to store this in plain text.
     */
    @Column
    private String hashedClientId;

    @Expose
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Expose
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginAttempt;
    
	public Long getIdentifierId() {
		return identifierId;
	}

	public void setIdentifierId(Long identifierId) {
		this.identifierId = identifierId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((hashedClientId == null) ? 0 : hashedClientId.hashCode());
		result = prime * result + ((salt == null) ? 0 : salt.hashCode());
		result = prime * result + ((secret == null) ? 0 : secret.hashCode());
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
		ClientIdentifier other = (ClientIdentifier) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (hashedClientId == null) {
			if (other.hashedClientId != null)
				return false;
		} else if (!hashedClientId.equals(other.hashedClientId))
			return false;
		if (salt == null) {
			if (other.salt != null)
				return false;
		} else if (!salt.equals(other.salt))
			return false;
		if (secret == null) {
			if (other.secret != null)
				return false;
		} else if (!secret.equals(other.secret))
			return false;
		return true;
	}

	public Date getLoginAttempt() {
		return loginAttempt;
	}

	public void setLoginAttempt(Date loginAttempt) {
		this.loginAttempt = loginAttempt;
	}


    
	
    
}
