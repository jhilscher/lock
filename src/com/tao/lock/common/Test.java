package com.tao.lock.common;

import java.io.IOException;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.sap.core.connectivity.api.http.HttpDestination;
import com.sap.core.connectivity.api.*;
import com.sap.core.connectivity.httpdestination.api.HttpDestinationException;
import com.tao.lock.connection.ConnectionService;

/**
 * Servlet implementation class Test
 */

@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

    	

    	String param = request.getParameter("url");
    	//String paramUser = request.getParameter("user");
    	
		try {
			// access the HttpDestination for the resource "pingdest" specified in the web.xml
			Context ctx = new InitialContext();
//			HttpDestination destination = (HttpDestination) ctx.lookup("java:comp/env/connect");
//			HttpClient createHttpClient = destination.createHttpClient();

			Context xmlCon = (Context) ctx.lookup("java:comp/env");
			//HttpDestination destination1 = (HttpDestination) xmlCon.lookup("context/connect");
			
			com.sap.core.connectivity.api.HttpDestination httpDestination = com.sap.core.connectivity.httpdestination.api.HttpDestinationFactory.getHttpDestination("connect");
			HttpClient createHttpClient = httpDestination.createHttpClient();
			
			// make a GET-request to the backend;
			// for basic authentication use HttpGet get = new HttpGet("pingbasic");
			//HttpGet get = new HttpGet(param + ((paramUser != null)? "/"+paramUser : ""));
			HttpPost post = new HttpPost(param);
		
			StringEntity se = new StringEntity("{\"clientId\":12,\"clientIdHash\":\"fwef\",\"salt\":\"saltwert\",\"secret\":\"meinsecret\"}");
			
        	post.addHeader("Accept", "application/json");
        	post.addHeader("Content-Type", "application/json");
            post.setEntity(se);
			
			
			HttpResponse resp = createHttpClient.execute(post);
			HttpEntity entity = resp.getEntity();
			String respToString = EntityUtils.toString(entity);
			int statusCode = resp.getStatusLine().getStatusCode();

			//response.getWriter().println("Status code: " + statusCode);
			response.getWriter().println(respToString);

//		} catch (NamingException e) {
//			throw new RuntimeException(e);
//		} catch (DestinationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (HttpDestinationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
