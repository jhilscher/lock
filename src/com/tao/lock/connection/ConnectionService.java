package com.tao.lock.connection;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




//import com.sap.core.connectivity.api.DestinationException;
//import com.sap.core.connectivity.api.http.HttpDestination;
import com.sap.core.connectivity.httpdestination.api.HttpDestinationException;
import com.sap.core.connectivity.api.DestinationFactory;
import com.sap.core.connectivity.api.DestinationNotFoundException;
import com.sap.core.connectivity.api.HttpDestination;
//import com.sap.core.connectivity.api.http.;
//import com.sap.core.connectivity.api.HttpDestination;

/**
 * 
 * @author Joerg Hilscher
 *
 */

@Stateless
public class ConnectionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);
	
	@Resource(name="connect")
	private HttpDestination destination;
	
	private static HttpClient httpClient;
	
	// default constructor
	public ConnectionService() {}
	
	@PostConstruct
	public void init() {
		
		//com.sap.core.connectivity.api.DestinationFactory
		
		HttpDestination httpDestination = null;
		
		try {
			httpDestination = com.sap.core.connectivity.httpdestination.api.HttpDestinationFactory.getHttpDestination("connect");
			httpClient = httpDestination.createHttpClient();
		
		} catch (HttpDestinationException e1) {
			LOGGER.error(e1.getMessage());
			e1.printStackTrace();
		}
		
		
	}
	
	
	public String sendGetRequest() {
		// access the HttpDestination for the resource "pingdest" specified in the web.xml
		
		HttpDestination destination3 = null;
		
		InitialContext ctx = null;
		
		try {
			ctx = new InitialContext();
			
			//working
			destination3 = com.sap.core.connectivity.httpdestination.api.HttpDestinationFactory.getHttpDestination("connect");
			

			Context xmlContext = (Context) ctx.lookup("comp/env/connect");
			
		} catch (HttpDestinationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			destination3 = (HttpDestination) com.sap.core.connectivity.httpdestination.api.HttpDestinationExtendedFactory.getHttpDestination("connect");
		} catch (HttpDestinationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		  		
		
		try {

		
		Context xmlContext = (Context) ctx.lookup("java:comp/env"); // thats everything from the context.xml and from the global configuration
		//DataSource myDatasource = (DataSource) xmlContext.lookup("jdbc/MyDatasource");

		//javax.naming.Context ctx = new InitialContext();
		HttpDestination destination2 = (HttpDestination) xmlContext.lookup("connect");
		
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
		
			Context initialContext = new InitialContext();
			DestinationFactory factory  = (DestinationFactory)initialContext.lookup("comp/env/connectivity/DestinationFactory");
			
			HttpDestination destination2 = factory.getDestination("connect");
			
			//HttpDestination destination2 = (HttpDestination) envContext.lookup("destination/connect");
	
			HttpGet get = new HttpGet("getall");
			HttpResponse resp;
		
			resp = httpClient.execute(get);

			HttpEntity entity = resp.getEntity();
			String respToString = EntityUtils.toString(entity);
			//int statusCode = resp.getStatusLine().getStatusCode();
			return respToString;
			
		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DestinationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
}
