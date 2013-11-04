package com.tao.lock.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ejb.Stateless;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.sap.core.connectivity.api.HttpDestination;
//import com.sap.core.connectivity.api.DestinationException;
//import com.sap.core.connectivity.api.http.HttpDestination;
import com.sap.core.connectivity.httpdestination.api.HttpDestinationException;
import com.tao.lock.rest.WebService;
import com.tao.lock.rest.json.ClientIdentifierPojo;


/**
 * 
 * @author Joerg Hilscher
 * 
 * Builds a connection to SCC.
 * Exchanges user-data.
 *
 */
@SuppressWarnings("deprecation")
@Stateless
public class ConnectionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);
	
	private static HttpClient httpClient;
	
	// default constructor
	public ConnectionService() {}
	
	private HttpClient getHttpClient() throws HttpDestinationException {
		HttpDestination httpDestination = com.sap.core.connectivity.httpdestination.api.HttpDestinationFactory.getHttpDestination("connect");
		return httpDestination.createHttpClient();
	}
	
	/**
	 * 
	 * @param cId
	 * @return
	 */
	public Boolean registerUser(ClientIdentifierPojo cId) {
		try {
			
		
		httpClient = getHttpClient();
			
		HttpPost post = new HttpPost("register");
		
		// Build up JSON.
		Gson gson = WebService.getGson();
		String json = gson.toJson(cId);
		
		LOGGER.info("JSON Connection Call: " + json);
		
    	post.addHeader("Accept", "application/json");
    	post.addHeader("Content-Type", "application/json");

    	post.setEntity(new StringEntity(json));
		
		HttpResponse resp = httpClient.execute(post);
		HttpEntity entity = resp.getEntity();
		
		int statusCode = resp.getStatusLine().getStatusCode();
		
		LOGGER.debug("registerUser Statuscode: " + statusCode);
		
		if(statusCode != 200 && statusCode != 201)
			return false;
		
		String respToString = EntityUtils.toString(entity);
		
		LOGGER.info("Response Connection Call: " + respToString);
		//int statusCode = resp.getStatusLine().getStatusCode();
		
		return true;
		
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HttpDestinationException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public ClientIdentifierPojo getClientIdentifier(String userName) {
		try {
			
			httpClient = getHttpClient();
				
			HttpGet get = new HttpGet("getuser/" + userName);
	    	get.addHeader("Accept", "application/json");
			
			HttpResponse resp = httpClient.execute(get);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);

			
			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.debug("getClientIdentifier Statuscode: " + statusCode);
			LOGGER.debug("getClientIdentifier Rsp: " + respToString);
			
			if(statusCode != 200 && statusCode != 201)
				return null;
			
			// Build up from JSON.
			Gson gson = WebService.getGson();
			ClientIdentifierPojo json = gson.fromJson(respToString, ClientIdentifierPojo.class);
			
			return json;
			
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (HttpDestinationException e) {
				e.printStackTrace();
			}
			
			return null;
	}
	

	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public ClientIdentifierPojo getClientIdentifierByToken(String token) {
		try {
			
			httpClient = getHttpClient();
				
			HttpGet get = new HttpGet("getUserToken/" + token);
	    	get.addHeader("Accept", "application/json");
			
			HttpResponse resp = httpClient.execute(get);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);

			
			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.debug("getClientIdentifier Statuscode: " + statusCode);
			LOGGER.debug("getClientIdentifier Rsp: " + respToString);
			
			if(statusCode != 200 && statusCode != 201)
				return null;
			
			// Build up from JSON.
			Gson gson = WebService.getGson();
			ClientIdentifierPojo json = gson.fromJson(respToString, ClientIdentifierPojo.class);
			
			return json;
			
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (HttpDestinationException e) {
				e.printStackTrace();
			}
			
			return null;
	}
	
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public String requestToken(String userName) {
		try {
			
			httpClient = getHttpClient();
				
			HttpGet get = new HttpGet("getToken/" + userName);

			HttpResponse resp = httpClient.execute(get);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);
			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.debug("getClientIdentifier Statuscode: " + statusCode);
			
			if(statusCode != 200 && statusCode != 201)
				return null;

			return respToString;
			
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (HttpDestinationException e) {
				e.printStackTrace();
			}
			
			return null;
	}
	
	// "validateToken/{userName}/{token}/{timestamp}"
	public String validateToken(String userName, String token, String timestamp) {
		try {
			
			httpClient = getHttpClient();
				
			HttpGet get = new HttpGet("validateToken/" + userName + "/" + token + "/" + timestamp);

			HttpResponse resp = httpClient.execute(get);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);
			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.debug("validateToken Statuscode: " + statusCode);
			
			if(statusCode != 200 && statusCode != 201)
				return null;

			return respToString;
			
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (HttpDestinationException e) {
				e.printStackTrace();
			}
			
			return null;
	}

	public String updateClientIdentifier(ClientIdentifierPojo cId) {
		try {
			
			httpClient = getHttpClient();
			
			HttpPost post = new HttpPost("update");
			
			// Build up JSON.
			Gson gson = WebService.getGson();
			String json = gson.toJson(cId);
			
			LOGGER.info("JSON Connection Call: " + json);
			
	    	post.addHeader("Accept", "application/json");
	    	post.addHeader("Content-Type", "application/json");
	
	    	post.setEntity(new StringEntity(json));
			
			HttpResponse resp = httpClient.execute(post);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);
			
			LOGGER.info("Response Connection Call: " + respToString);
			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.debug("updateClientIdentifier Statuscode: " + statusCode);
			
			if(statusCode != 200 && statusCode != 201)
				return null;
			
			return respToString;
		
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HttpDestinationException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public Boolean deleteClientIdentifier(String userName) {
		
		try {
			
			
			httpClient = getHttpClient();
				
			HttpPost post = new HttpPost("delete");
			
			// Build up JSON.
			Gson gson = WebService.getGson();
			String json = gson.toJson(userName);
						
	    	post.addHeader("Accept", "application/json");
	    	post.addHeader("Content-Type", "application/json");

	    	post.setEntity(new StringEntity(json));
			
			HttpResponse resp = httpClient.execute(post);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);

			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.debug("DeleteClientIdentifier Statuscode: " + statusCode);
			
			if(statusCode != 200 && statusCode != 201)
				return null;
			
			return true;
			
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (HttpDestinationException e) {
				e.printStackTrace();
			}
			
			return false;
		
	}
	
	
	
//	@RolesAllowed(Roles.MANAGER)
//	public String authentificateUser(HttpServletRequest request, ServletContext context, CloudUser cloudUser) { 
//		
//	}
	
	
	
	
	
	
	/** TESTING **/
	public String sendGetRequest() {
		// access the HttpDestination for the resource "pingdest" specified in the web.xml
		
		HttpDestination destination3 = null;
		
		InitialContext ctx = null;
		
		try {
			ctx = new InitialContext();
			
			
			//ctx.addToEnvironment("connect", com.sap.core.connectivity.api.http.HttpDestination.class);
			//working
			//destination3 = com.sap.core.connectivity.httpdestination.api.HttpDestinationFactory.getHttpDestination("connect");
			Context xmlCon = (Context) ctx.lookup("java:comp/env");
	
			HttpDestination destination1 = (HttpDestination) xmlCon.lookup("lock/connect");
			
		} catch (NamingException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		
		/*try {
			destination3 = (HttpDestination) com.sap.core.connectivity.httpdestination.api.HttpDestinationExtendedFactory.getHttpDestination("connect");
		} catch (HttpDestinationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		  		
		
		try {

		
		//Context xmlContext = (Context) ctx.lookup("java:comp/env"); // thats everything from the context.xml and from the global configuration
		//DataSource myDatasource = (DataSource) xmlContext.lookup("jdbc/MyDatasource");

		//javax.naming.Context ctx = new InitialContext();
		HttpDestination destination2 = (HttpDestination) ctx.lookup("java:comp/env/lock/connect");
		
		HttpClient createHttpClient = destination3.createHttpClient();

		// make a GET-request to the backend;
		// for basic authentication use HttpGet get = new HttpGet("pingbasic");
		HttpGet get = new HttpGet("getall");
		HttpResponse resp = createHttpClient.execute(get);
		HttpEntity entity = resp.getEntity();
		String respToString = EntityUtils.toString(entity);
		
		//int statusCode = resp.getStatusLine().getStatusCode();

		return respToString;
		
		}  catch (ParseException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}
	
	public String sendGetRequestWithoutLookup() {
		// access the HttpDestination for the resource "pingdest" specified in the web.xml
		try {
		
			Context ctx = (Context)new InitialContext().lookup("java:comp/env");
			listContext(ctx, "");
			

			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;

	}
	


	/**
	* Recursively exhaust the JNDI tree
	*/
	private static final void listContext(Context ctx, String indent) {
	try {
	   NamingEnumeration list = ctx.listBindings("");
	   while (list.hasMore()) {
	       Binding item = (Binding) list.next();
	       String className = item.getClassName();
	       String name = item.getName();
	       
	       LOGGER.info(indent + className + " " + name);
	      
	       Object o = item.getObject();
	       if (o instanceof javax.naming.Context) {
	    	   listContext((Context) o, indent + " ");
	       }
	   }
	} catch (NamingException ex) {
		LOGGER.error("JNDI failure: ", ex);
	}

	}

}
