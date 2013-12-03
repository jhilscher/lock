package com.tao.lock.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.Index;

import com.google.gson.annotations.Expose;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@Entity
@Table(name = "T_CLOUDUSER")
@NamedQueries({
	@NamedQuery(name = CloudUser.QUERY_BYALLUSERS, query = "SELECT p FROM CloudUser p"),
	@NamedQuery(name = CloudUser.QUERY_BYUSERNAME, query = "SELECT u FROM CloudUser u WHERE lower(u.userName) = lower(:userName)"),
	@NamedQuery(name = CloudUser.QUERY_BYUSERID, query = "SELECT u FROM CloudUser u WHERE u.id = :id")
	})
public class CloudUser {
	
	// Queries
	public static final String QUERY_BYALLUSERS = "getAllUsers";
	public static final String QUERY_BYUSERNAME = "getUserByUserName";
	public static final String QUERY_BYUSERNAMEWITHIDENTIFIER = "getUserByUserNameWithIdentifier";
	public static final String QUERY_BYUSERID = "getUserById";
	
	//@Expose
    @Id
    @GeneratedValue
    private Long id;
    
	@Expose
    @Column(unique = true, nullable = false)
    @Index
    @Size(min = 4, max = 256)
    private String userName;
    
	@Expose
	@Column
	private String firstName;
	
	@Expose
	@Column
	private String lastName;
	
	@Expose
    @Column(unique = true, nullable = false)
    @Index
    @Size(min = 4, max = 256)
    private String email;
    
	@Expose
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
	@Expose
    @Transient
    private Date lastLogIn;
	
	@Expose
    @Column
	private Boolean isRegistered;
	    
	@Expose
	@Transient
	private int status;
	
	/*
	 * 0: Restricted -> Default
	 * 1: Always
	 * 2: on risk
	 */
	@Expose
	@Column()
	private int securityLevel;
	
    @Transient
    private HttpSession session;
        
    public CloudUser() {}
    
    // workaround for timestamp value
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        
        securityLevel = 0;
        
        // set default value
        if (isRegistered == null )  
        {  
        	isRegistered = false;
        }
    }
    
    public long getId() {
        return id;
    }

    public void setId(long newId) {
        this.id = newId;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		CloudUser other = (CloudUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CloudUser [id=" + id + ", userName=" + userName + ", email="
				+ email + ", createdAt=" + createdAt + ", session=" + session
				+ "]";
	}

	public Boolean getIsRegistered() {
		return isRegistered;
	}

	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getLastLogIn() {
		return lastLogIn;
	}

	public void setLastLogIn(Date lastLogIn) {
		this.lastLogIn = lastLogIn;
	}

	public int getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


}
