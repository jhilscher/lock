package com.tao.lock.rest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.annotation.ManagedBean;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tao.lock.entities.CloudUser;
import com.tao.lock.security.AuthentificationHandler;
import com.tao.lock.security.RegistrationHandler;
import com.tao.lock.security.SecurityUtils;
import com.tao.lock.services.UserService;
import com.tao.lock.utils.Roles;


	@ManagedBean
	@Path("/service")
	public class WebService {

		private static final Logger LOGGER = LoggerFactory.getLogger(WebService.class);

		// geht
		@Context
		private HttpServletRequest request;

		@Context
	    private HttpServletResponse response;

	    // not working
	    //@Resource
	    //private UserProvider userProvider;
	    
	    @Context
	    private ServletContext context;
		
		@EJB
		private UserService userService;
		
		@EJB
		private RegistrationHandler registrationHandler;

		@EJB
		private AuthentificationHandler authentificationHandler;
		
	     
	    @GET
	    @Path("/echo/{input}")
	    @Produces("text/plain")
	    public String ping(@PathParam("input") String input) { 	
	        return input;
	    }
	    
	    @GET
	    @Path("/test")
	    @Produces("text/plain")
	    @RolesAllowed(Roles.ADMIN) // <-- is working
	    public String test() {

			CloudUser user = new CloudUser();
			user.setUserName("vorname" + " " + "Nachname");
			user.setEmail("test@ewlnfw.de");

			userService.addUser(user);
	        return "geht";
	    }
	    

	    @GET
	    @Path("/getall")
	    @Produces("application/json")
	    @RolesAllowed(Roles.ADMIN)
	    public String getAll() {
	    		    	
	    	Gson gson = new Gson();
	        return gson.toJson(userService.getAllUsers());
	    }
	    
	    /**
	     * Poll on this, to see if a user is authed.
	     * Warning: this is expensive!
	     * @return Statuscode.
	     */
	    @POST
	    @Path("/authpolling")
	    @RolesAllowed(Roles.MANAGER)
	    public Response authPolling() {
	    	
	    	CloudUser user = userService.getCloudUser(request);
	    	
	    	if(user != null) {
	    		if (user.getSession() != null) {
	    			if (user.getSession().getAttribute("auth") == "true")
	    				return Response.ok().build();
	    		}
	    	}
	    	
	    	return Response.status(Response.Status.UNAUTHORIZED).build();
	    }
	    
	    @POST
	    @Path("/auth")
	    @Consumes(MediaType.APPLICATION_JSON)
	    public Response auth(AuthentificationJSON authJSON) {
	    	String r1 = authJSON.getX1();
	    	
	    	//lookup
	    	CloudUser user = authentificationHandler.tryToGetUserToAuth(r1);
	    	
	    	if (user == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
	    	
	    	
    		boolean authed = false;
	    	
    		try {
				authed = SecurityUtils.validateKey(authJSON.getClientIdKey().toCharArray(), user.getIdentifier().getHashedClientId(),  user.getIdentifier().getSalt());
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage());
			} catch (InvalidKeySpecException e) {
				LOGGER.error(e.getMessage());
			}
    		
    		if (!authed)
    			return Response.status(Response.Status.UNAUTHORIZED).entity("key wrong?").build();	
    		
    		// TODO: Give User Auth?! But how?
	    	HttpSession session = user.getSession();
    		if(session == null)
    			return Response.status(Response.Status.UNAUTHORIZED).build();
    		
    		// Add to session
    		user.getSession().setAttribute("auth", "true");
    		
	    	return Response.ok().build();
	    }
	    
	    	
	    /**
	     * Registers the client to the server.
	     * Checks the clientIdKey.
	     * @param registrationJSON
	     * @return StatusCode
	     */
	    @POST
	    @Path("/register")
	    @Consumes(MediaType.APPLICATION_JSON)
	    public Response register(RegistrationJSON registrationJSON) {
	    	
	    	String clientIdKey = registrationJSON.getClientIdKey();	
	    	
	    	@SuppressWarnings("static-access")
			CloudUser user = registrationHandler.tryToGetUserToRegister(clientIdKey);
	    	
	    	if (user == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("code timed out").build();
	    	
	    	// Check for correct client key?
	    	boolean authed = false;
	    	
	    	try {
	    		authed = SecurityUtils.validateKey(clientIdKey.toCharArray(), user.getIdentifier().getHashedClientId(),  user.getIdentifier().getSalt());
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			} 
	    	
	    	if(!authed)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("key is wrong").build();
	    	
	    	// add x_1 to the user
	    	user.getIdentifier().setSecret(registrationJSON.getX1());
	    	
	    	// save user with identifier to db
	    	userService.update(user);
	    	
	    	
	    	clientIdKey = null;
	    	registrationJSON = null;
	    	

	    	return Response.status(Response.Status.CREATED).entity("success").build();
	    }
	    
   
	}


	

