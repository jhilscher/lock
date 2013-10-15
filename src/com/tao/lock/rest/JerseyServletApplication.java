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
public class JerseyServletApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        
        // Register Class here.
        s.add(WebService.class);
        
        return s;
    }

    

}
