package com.tao.lock.dao;

import java.io.Serializable;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO Superclass.
 * @author Joerg Hilscher
 *
 */
@Stateless
@LocalBean
public class Dao implements Serializable{

	private static final long serialVersionUID = 1677389526664199824L;

	protected static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
	
    @PersistenceContext
	protected transient EntityManager em;
    
    public <T> T create(T t) {
        em.persist(t);
        return t;
    }
    
    public <T> T update(T t) {
        T merge = em.merge(t);
        return merge;
    }
    
    public <T> void remove(T t) {
        em.remove(t);
        em.flush();
    }
    
}
