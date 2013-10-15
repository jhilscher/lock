package com.tao.lock.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.tao.lock.entities.CloudUser;

/**
 * DAO for managing CloudUsers.
 */
@Stateless
@LocalBean
public class UserDao extends Dao {

	private static final long serialVersionUID = 1762473855221682526L;

	/**
     * Default constructor. 
     */
    public UserDao() {}

    public CloudUser getUserByUserName(String userName) {
        try {
            return (CloudUser) em.createNamedQuery(CloudUser.QUERY_BYUSERNAME)
                    .setParameter("userName", userName.toLowerCase()).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            LOGGER.error("More than one result! Fatal Error and possibly a security leak!", e);
            throw e;
        }
    }
    
	@SuppressWarnings("unchecked")
	public List<CloudUser> getAllUsers() {
		return em.createNamedQuery(CloudUser.QUERY_BYALLUSERS).getResultList();
	}

	public CloudUser addUser(CloudUser user) {
		return create(user);
	}

	public CloudUser updateUser(CloudUser user) {
		return update(user);
	}
}
