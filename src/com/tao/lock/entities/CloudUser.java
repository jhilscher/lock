package com.tao.lock.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Size;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.annotations.Index;

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
	@NamedQuery(name = CloudUser.QUERY_BYUSERNAMEWITHIDENTIFIER, 
				query = "SELECT u FROM CloudUser u "
						+ "LEFT JOIN FETCH u.identifier WHERE lower(u.userName) = lower(:userName)")
	})
public class CloudUser {
	
	// Queries
	public static final String QUERY_BYALLUSERS = "getAllUsers";
	public static final String QUERY_BYUSERNAME = "getUserByUserName";
	public static final String QUERY_BYUSERNAMEWITHIDENTIFIER = "getUserByUserNameWithIdentifier";
	
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(unique = true, nullable = false)
    @Index
    @Size(min = 4, max = 256)
    private String userName;
    
    @Column(unique = true, nullable = false)
    @Index
    @Size(min = 4, max = 256)
    private String email;
    
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="identifierId")
    private ClientIdentifier identifier;
    
    // not mapped to the db
    // TODO: not working... remove?
    @Transient
    private HttpSession session;
    
    public CloudUser() {}
    
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

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
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
}
