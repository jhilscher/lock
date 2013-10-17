package com.tao.lock.common;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.services.UserService;

@SessionScoped
@Named
public class AdminBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	private static final Logger LOGGER = LoggerFactory.getLogger(AdminBean.class);

	

}
