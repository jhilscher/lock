package com.tao.lock.rest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.annotation.ManagedBean;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.naming.NamingException;
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

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sap.cloud.security.oauth2.OAuthAuthorization;
import com.sap.cloud.security.oauth2.OAuthSystemException;
import com.tao.lock.entities.CloudUser;
import com.tao.lock.rest.json.AuthentificationJSON;
import com.tao.lock.rest.json.ClientIdentifierPojo;
import com.tao.lock.rest.json.RegistrationJSON;
import com.tao.lock.security.AuthentificationHandler;
import com.tao.lock.security.RegistrationHandler;
import com.tao.lock.security.SecurityUtils;
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
		private RegistrationHandler registrationHandler;

		@EJB
		private AuthentificationHandler authentificationHandler;
		
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
	     * 
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
	    	String r1 = authJSON.getX1();
	    	
	    	//lookup
	    	CloudUser user = authentificationHandler.tryToGetUserToAuth(r1);
	    	
	    	
	    	if (user == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
	    	
	    	
	    	ClientIdentifierPojo cId = connectionService.getClientIdentifier(user.getUserName());
	    	if (cId == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).build();
	    	
	    	
    		boolean authed = false;
	    	
    		try {
				authed = SecurityUtils.validateKey(authJSON.getClientIdKey().toCharArray(), cId.getHashedClientId(),  cId.getSalt());
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage());
			} catch (InvalidKeySpecException e) {
				LOGGER.error(e.getMessage());
			}
    		
    		if (!authed)
    			return Response.status(Response.Status.UNAUTHORIZED).entity("key wrong?").build();	
    		
    		// FIXME: Give User Auth?! 
	    	HttpSession session = user.getSession();
    		if(session == null)
    			return Response.status(Response.Status.UNAUTHORIZED).build();
    		
    		// Add to session
    		user.getSession().setAttribute("auth", "true");
    		
    		// Update login attempt
    		cId.setLoginAttempt(new Date());
			connectionService.updateClientIdentifier(cId);
    		
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
	    	
	    	@SuppressWarnings("static-access")
			ClientIdentifierPojo clientIdentifierPojo = registrationHandler.tryToGetUserToRegister(clientIdKey);
	    	
	    	CloudUser user = userService.getUserByName(clientIdentifierPojo.getUserName());
	    	
	    	if (clientIdentifierPojo == null || user == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error").build();
	    	
	    	// Check for correct client key?
	    	boolean authed = false;
	    	
	    	try {
	    		authed = SecurityUtils.validateKey(clientIdKey.toCharArray(), clientIdentifierPojo.getHashedClientId(),  clientIdentifierPojo.getSalt());
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
	    	clientIdentifierPojo.setSecret(registrationJSON.getX1());
	    	connectionService.registerUser(clientIdentifierPojo);
	    	
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
	    
	    @POST
	    @Path("/removeclientidfromuser")
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    @RolesAllowed(Roles.ADMIN)
	    public Response removeClientIdFromUser(@FormParam("id") long id) {
	    	
	    	CloudUser user = userService.getUserById(id);
	    	
	    	if (user == null)
	    		Response.status(Response.Status.NO_CONTENT).entity("user not found").build();
	    	
	    	user.setIsRegistered(false);
	    	connectionService.DeleteClientIdentifier(user.getUserName());
	    	userService.update(user);
	    	
	    	return Response.status(Response.Status.CREATED).entity("success").build();
	    	
	    }
	    
	    @GET
	    @Path("/getcurrentuser")
	    @RolesAllowed(Roles.EVERYONE)
	    @Produces(MediaType.APPLICATION_JSON)
	    public String getCurrentUser() {
	    	return getGson().toJson(userService.getCloudUser(request));
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
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error").build();
	    	
	    	String url = authentificationService.authentificateUser(request, context, user);
	    	if (url == null)
	    		return Response.status(Response.Status.UNAUTHORIZED).entity("error").build();
	    	
	    	String filename = QRUtils.getFilenameFromUrl(url);
	    	
	    	// add filename to session
	    	request.getSession().setAttribute("qrcode", filename);
	    	
	    	LOGGER.info("Generated QR-Code with filename: + filename");
	    	
	    	return Response.ok().entity(url).build();
	    	
	    }
	    
	    
	    // FIXME: remove
	    
	    @GET
	    @Path("/oauth")
	    public Response oAuth() throws OAuthSystemException {
	    	OAuthAuthorization authAuthorization = OAuthAuthorization.getOAuthAuthorizationService();
		    if(!authAuthorization.isAuthorized(request, "access")){
		    	 return Response.ok().entity("not authorized").build();
		    } 
		    

		    return Response.ok().entity("fail").build();
		    
	    }
	    
	    
	    // FIXME: remove
	    @GET
	    @Path("/ping")
	    public Response ping() throws ClientProtocolException, IOException  {
	   
	    	String response = connectionService.sendGetRequest();

	    	
	    	
	    	if (response != null)
	    		return Response.ok().entity(response).build();
	    	else 
	    		return Response.serverError().build();

	    }
	    
	    // FIXME: remove
	    @GET
	    @Path("/ping2")
	    public Response ping2() throws ClientProtocolException, IOException  {
	   
	    	String response = connectionService.sendGetRequestWithoutLookup();

	    	//context.getResource("java:comp/env/connect");
	    		
	    	
	    	if (response != null)
	    		return Response.ok().entity(response).build();
	    	else 
	    		return Response.serverError().build();

	    }
	    
	    // FIXME: remove
	    @GET
	    @Path("/ping3")
	    public Response ping3() throws NamingException {
	    	
	    	CloudUser user = userService.getCloudUser(request);
	    	ClientIdentifierPojo response = connectionService.getClientIdentifier(user.getUserName());

	    	return Response.ok().entity(response.toString()).build();

	    	
	    }

	}

	

