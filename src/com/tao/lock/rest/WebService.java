package com.tao.lock.rest;

import javax.annotation.ManagedBean;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tao.lock.entities.CloudUser;
import com.tao.lock.rest.json.AuthentificationJSON;
import com.tao.lock.rest.json.ClientIdentifierPojo;
import com.tao.lock.rest.json.RegistrationJSON;
import com.tao.lock.security.AuthentificationHandler;
import com.tao.lock.security.RegistrationHandler;
import com.tao.lock.services.AuthentificationService;
import com.tao.lock.services.ConnectionService;
import com.tao.lock.services.UserService;
import com.tao.lock.utils.QRUtils;
import com.tao.lock.utils.Roles;


	/**
	 * 
	 * @author Joerg Hilscher
	 *
	 */
	@ManagedBean
	@Path("/service")
	public class WebService {

		private static final Logger LOGGER = LoggerFactory.getLogger(WebService.class);

		@Context
		private HttpServletRequest request;

		@Context
	    private HttpServletResponse response;

		@Context
	    private ServletContext context;
		
		@EJB
		private UserService userService;
				
		@EJB
		private AuthentificationService authentificationService;
   
		@EJB
		private ConnectionService connectionService;
		
		/**
		 * This builder will only include fields with @Expose annotation.
		 * @return Gson
		 */
		public static Gson getGson () {														
			return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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
	    	
	    	// FIXME: shitty way to do this.
	    	if (request.getSession().getAttribute("auth") == "true")
	    				return Response.ok().build();

	    	return Response.status(Response.Status.UNAUTHORIZED).build();
	    }
	    
	    /**
	     * Interface for polling.
	     * @return
	     */
	    @POST
	    @Path("/regpolling")
	    @RolesAllowed(Roles.MANAGER)
	    public Response regPolling() {
	    	
	    	if (userService.getCloudUser(request).getIsRegistered())
	    				return Response.ok().build();

	    	return Response.status(Response.Status.NO_CONTENT).build();
	    }
	    
	    /**
	     * **** REQUESTS FROM MOBILE CLIENT ****
	     */
	    
	    /**
	     * Request coming from mobile.
	     * @param authJSON
	     * @return 	StatusCode.
	     */
	    @POST
	    @Path("/auth")
	    @Consumes(MediaType.APPLICATION_JSON)
	    @PermitAll 
	    public Response auth(AuthentificationJSON authJSON) {

    		// resp should be the user name
    		String resp = connectionService.validateToken(authJSON.getClientIdKey(), authJSON.getX1());
    		
    		if (resp == null) {
    			LOGGER.info("401: user not found: ");
    			return Response.status(Response.Status.UNAUTHORIZED).entity("key wrong?").build();	
    		}
    		
    		// remove potential " from the string
    		resp = resp.replace("\"", "");
    		
    		CloudUser user = AuthentificationHandler.tryToGetUserToAuth(resp);
    		

    		if (user == null) {
    			LOGGER.info("401: user is not auted.");
    			return Response.status(Response.Status.UNAUTHORIZED).entity("key wrong?").build();	
    		}
    		
	    	HttpSession session = user.getSession();
    		if(session == null) {
    			LOGGER.info("401: session not found");
    			return Response.status(Response.Status.UNAUTHORIZED).build();
    		}
    		// FIXME: Give User Auth?! 
    		// Add to session
    		user.getSession().setAttribute("auth", "true");

    		return Response.status(Response.Status.CREATED).entity("success").build();
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
	    @PermitAll 
	    public Response register(RegistrationJSON registrationJSON) {
	    	
	    	String clientIdKey = registrationJSON.getClientIdKey();	
	    	
			ClientIdentifierPojo clientIdentifierPojo = RegistrationHandler.tryToGetUserToRegister(clientIdKey);
	    	
	    	CloudUser user = userService.getUserByName(clientIdentifierPojo.getUserName());
	    	
	    	if (clientIdentifierPojo == null || user == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error").build();

	    	// add x_1 to the user
	    	clientIdentifierPojo.setSecret(registrationJSON.getX1());
	    	clientIdentifierPojo.setHashedClientId(clientIdKey);
	    	
	    	if(!connectionService.registerConfirm(clientIdentifierPojo))
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error").build();
	    	
	    	// save user with identifier to db
	    	user.setIsRegistered(true);
	    	userService.update(user);
	    	

	    	clientIdKey = null;
	    	registrationJSON = null;
	    	
	    	return Response.status(Response.Status.CREATED).entity("success").build();
	    }
	    
	    /**
	     * **** SERVICES FOR WEB-CLIENT ****
	     */
	    @GET
	    @Path("/getallusers")
	    @Produces(MediaType.APPLICATION_JSON)
	    @RolesAllowed(Roles.ADMIN)
	    public String getAllUsers() {
	    	return getGson().toJson(userService.getAllUsers());
	    }
	    
	    /**
	     * 
	     * @param id
	     * @return
	     */
	    @POST
	    @Path("/removeclientidfromuser")
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    @RolesAllowed(Roles.ADMIN)
	    public Response removeClientIdFromUser(@FormParam("id") long id) {
	    	
	    	CloudUser user = userService.getUserById(id);
	    	
	    	if (user == null)
	    		Response.status(Response.Status.NO_CONTENT).entity("user not found").build();
	    	
	    	user.setIsRegistered(false);
	    	connectionService.deleteClientIdentifier(user.getUserName());
	    	userService.update(user);
	    	
	    	return Response.status(Response.Status.CREATED).entity("success").build();
	    	
	    }
	    
	    /**
	     * Gets the current user.
	     * @return
	     */
	    @GET
	    @Path("/getcurrentuser")
	    @RolesAllowed(Roles.EVERYONE)
	    @Produces(MediaType.APPLICATION_JSON)
	    public String getCurrentUser() {
	    	CloudUser user = userService.getCloudUser(request);
	    	JsonObject jsonObject = new JsonObject();
	    	jsonObject.addProperty("userName", user.getUserName());
	    	
	    	HttpSession session = request.getSession();
	    	String auth = (String) session.getAttribute("auth");
	    	jsonObject.addProperty("isLoggedIn", auth);
	    	return jsonObject.toString();
	    }
	    
	    @GET
	    @Path("/getregisterqr")
	    @RolesAllowed(Roles.MANAGER)
	    public Response getRegisterQr() {
	    	
	    	CloudUser user = userService.getCloudUser(request);
	    	
	    	if (user == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error").build();
	    	
	    	String url = authentificationService.registerUser(request, context, user);
	    	if (url == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error").build();
	    	
	    	// add filename to session
	    	request.getSession().setAttribute("qrcode", QRUtils.getFilenameFromUrl(url));
	    	
	    	LOGGER.info("Generated QR-Code with filename: . ", QRUtils.getFilenameFromUrl(url));
	    	
	    	return Response.ok().entity(url).build();
	    	
	    }
	    
	    @GET
	    @Path("/getauthqr")
	    @RolesAllowed(Roles.MANAGER)
	    public Response getAuthQr() {
	    	
	    	CloudUser user = userService.getCloudUser(request);
	    	if (user == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error on user").build();
	    	
	    	String url = authentificationService.authentificateUser(request, context, user);
	    	if (url == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error on auth").build();
	    	
	    	String filename = QRUtils.getFilenameFromUrl(url);
	    	
	    	// add filename to session
	    	request.getSession().setAttribute("qrcode", filename);
	    	
	    	return Response.ok().entity(url).build();
	    	
	    }


	}

	

