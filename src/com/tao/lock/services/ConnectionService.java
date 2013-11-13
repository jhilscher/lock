package com.tao.lock.services;

import java.io.IOException;
import java.util.Date;

import javax.ejb.Stateless;

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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sap.core.connectivity.api.HttpDestination;
//import com.sap.core.connectivity.api.DestinationException;
//import com.sap.core.connectivity.api.http.HttpDestination;
import com.sap.core.connectivity.httpdestination.api.HttpDestinationException;
import com.tao.lock.rest.WebService;
import com.tao.lock.rest.json.ClientIdentifierPojo;
import com.tao.lock.utils.JsonDateDeserializer;


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
	
	/**
	 * Gets a HttpClient from the platform api.
	 * @return HttpClient
	 * @throws HttpDestinationException
	 */
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
		
    	post.addHeader("Accept", "text/plain");
    	post.addHeader("Content-Type", "application/json");

    	post.setEntity(new StringEntity(json));
		
		HttpResponse resp = httpClient.execute(post);
		HttpEntity entity = resp.getEntity();
		
		int statusCode = resp.getStatusLine().getStatusCode();
		
		LOGGER.debug("registerUser Statuscode: " + statusCode);
		
		if(statusCode != 200 && statusCode != 201)
			return null;
		
		String respToString = EntityUtils.toString(entity);
		
		respToString = respToString.replace("\"", "");
		
		LOGGER.info("Response Connection Call: " + respToString);
		
		
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
		//HttpEntity entity = resp.getEntity();
		
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
		
		return false;
	}
	

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public ClientIdentifierPojo getClientIdentifierByUserName(String userName) {
		try {
			
			httpClient = getHttpClient();
				
			HttpGet get = new HttpGet("getuserdata/" + userName);
	    	get.addHeader("Accept", "application/json");
			
			HttpResponse resp = httpClient.execute(get);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);

			LOGGER.info(respToString);
			
			int statusCode = resp.getStatusLine().getStatusCode();
			
			if(statusCode != 200 && statusCode != 201)
				return null;
			
			// Build up from JSON.
			// Build Gson with custom TypeAdapter for parsing c#-date format
			Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
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
	
	/**
	 * Validates a token for authentification.
	 * @param clientID		Not hashed clientId
	 * @param token			Token response from mobile
	 * @return UserName 	UserName if valid, null if not
	 */
	public String validateToken(String clientID, String token) {
		try {

			httpClient = getHttpClient();
				
			LOGGER.info("validateToken Request: " + clientID + "/" + token);
			
			HttpPost post = new HttpPost("validateToken");
			
			// build up a json object
	    	JsonObject jsonObject = new JsonObject();
	    	jsonObject.addProperty("hashedClientId", clientID);
	    	jsonObject.addProperty("token", token);
	    	
			
			LOGGER.info("JSON Connection Call: " + jsonObject.toString());
			
	    	//post.addHeader("Accept", "application/json");
	    	post.addHeader("Content-Type", "application/json");

	    	post.setEntity(new StringEntity(jsonObject.toString()));
			
			HttpResponse resp = httpClient.execute(post);
			HttpEntity entity = resp.getEntity();
			
			String respToString = EntityUtils.toString(entity);
			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.info("validateToken Statuscode: " + statusCode);
			
			// check statuscode
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

//	public String updateClientIdentifier(ClientIdentifierPojo cId) {
//		try {
//			
//			httpClient = getHttpClient();
//			
//			HttpPost post = new HttpPost("update");
//			
//			// Build up JSON.
//			Gson gson = WebService.getGson();
//			String json = gson.toJson(cId);
//			
//			LOGGER.info("JSON Connection Call: " + json);
//			
//	    	post.addHeader("Accept", "application/json");
//	    	post.addHeader("Content-Type", "application/json");
//	
//	    	post.setEntity(new StringEntity(json));
//			
//			HttpResponse resp = httpClient.execute(post);
//			HttpEntity entity = resp.getEntity();
//			
//			String respToString = EntityUtils.toString(entity);
//			
//			LOGGER.info("Response Connection Call: " + respToString);
//			int statusCode = resp.getStatusLine().getStatusCode();
//			
//			LOGGER.debug("updateClientIdentifier Statuscode: " + statusCode);
//			
//			if(statusCode != 200 && statusCode != 201)
//				return null;
//			
//			return respToString;
//		
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (HttpDestinationException e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
	
	/**
	 * Deletes the local data of a client.
	 * @param userName
	 * @return Boolean if the command was successful.
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
			
			//HttpEntity entity = resp.getEntity();
			//String respToString = EntityUtils.toString(entity);

			int statusCode = resp.getStatusLine().getStatusCode();
			
			LOGGER.debug("DeleteClientIdentifier Statuscode: " + statusCode);
			
			if(statusCode != 200 && statusCode != 201)
				return false;
			
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
