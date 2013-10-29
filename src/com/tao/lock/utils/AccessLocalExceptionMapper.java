package com.tao.lock.utils;

import javax.ejb.AccessLocalException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * FROM: http://www.oraclejavamagazine-digital.com/javamagazine_open/20130102#pg64
 * @author Joerg Hilscher
 *
 */
@Provider
public class AccessLocalExceptionMapper implements ExceptionMapper<AccessLocalException> {

	@Context 
	HttpServletRequest request;
    
    @Override
    public Response toResponse(AccessLocalException exception) {
        try {
            request.logout();
        } catch (ServletException ex) {
        }
       return Response.status(Response.Status.UNAUTHORIZED).build();
    }


}
