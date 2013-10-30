package com.tao.lock.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Required to register the JAX-RS Webservice.
 * Alternate way is to do it via web.xml.
 * @author Joerg Hilscher.
 *
 */
@ApplicationPath("api")
public class JerseyServletApplication extends Application   {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        
        // Register Class here.
        s.add(WebService.class);
        
        return s;
    }

//	public JerseyServletApplication() {
//		this(true);
//	}
//	
//	public JerseyServletApplication(boolean b) {
//		Set<Class<?>> s = new HashSet<Class<?>>();
//		s.add(WebService.class);
//		registerClasses(s);
//		
//		register(new Binder());
//	}
//    
//	
//	public class Binder extends AbstractBinder {
//		                   @Override
//		                   protected void configure() {
//		                           bind(UserDao.class).to(UserDao.class);
//		                           bind(UserService.class).to(UserService.class);
//		                           bind(ConnectionService.class).to(ConnectionService.class);
//		                           bind(AuthentificationService.class).to(AuthentificationService.class);
//		                           bind(AuthorizationService.class).to(AuthorizationService.class);
//		                           bind(AuthentificationHandler.class).to(AuthentificationHandler.class);
//		                           bind(RegistrationHandler.class).to(RegistrationHandler.class);
//		                   }
//		          }

}
