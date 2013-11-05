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
	 * @return String ID_A
	 */
	public String registerRequest(ClientIdentifierPojo cId) {
		try {
			
		
		httpClient = getHttpClient();
			
		HttpPost post = new HttpPost("registerRequest");
		
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
			return null;
		
		String respToString = EntityUtils.toString(entity);
		
		LOGGER.info("Response Connection Call: " + respToString);
		//int statusCode = resp.getStatusLine().getStatusCode();
		
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
	 * @param cId
	 * @return String ID_A
	 */
	public Boolean registerConfirm(ClientIdentifierPojo cId) {
		try {
			
		
		httpClient = getHttpClient();
			
		HttpPost post = new HttpPost("confirmRegister");
		
		// Build up JSON.
		Gson gson = WebService.getGson();
		String json = gson.toJson(cId);
		
		LOGGER.info("JSON Connection Call: " + json);
		
    	//post.addHeader("Accept", "application/json");
    	post.addHeader("Content-Type", "application/json");

    	post.setEntity(new StringEntity(json));
		
		HttpResponse resp = httpClient.execute(post);
		HttpEntity entity = resp.getEntity();
		
		int statusCode = resp.getStatusLine().getStatusCode();
		
		LOGGER.debug("registerUser Statuscode: " + statusCode);
		
		if(statusCode != 200 && statusCode != 201)
			return false;
		else
			return true;
		
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HttpDestinationException e) {
			e.printStackTrace();
		}
		
		return true;
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
	

}
