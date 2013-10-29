package com.tao.lock.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.CascadeOnDelete;
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
	
	@Expose
    @Id
    @GeneratedValue
    private Long id;
    
	@Expose
    @Column(unique = true, nullable = false)
    @Index
    @Size(min = 4, max = 256)
    private String userName;
    
	@Expose
    @Column(unique = true, nullable = false)
    @Index
    @Size(min = 4, max = 256)
    private String email;
    
	@Expose
    @Column()
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
	@Expose
    @OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="identifierId")
    //@Transient
	private ClientIdentifier identifier;
    
    // not mapped to the db
    // TODO: currently working
    @Transient
    private HttpSession session;
    
    public CloudUser() {}
    
    // workaround for timestamp value
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
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

	public ClientIdentifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(ClientIdentifier identifier) {
		this.identifier = identifier;
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


}
