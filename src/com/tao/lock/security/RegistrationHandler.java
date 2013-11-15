package com.tao.lock.security;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.rest.json.ClientIdentifierPojo;
import com.tao.lock.utils.QRUtils;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@Singleton
public class RegistrationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationHandler.class);
	
	private static HashMap<String, ClientIdentifierPojo> userMap = new HashMap<String, ClientIdentifierPojo>();

	private static final int TIMEOUT = 1000 * 60 * 2; // 2mins
	
	public static void addToWaitList(final ClientIdentifierPojo clientIdentifierPojo, final String clientIdKey, final QRUtils qrUtils) {
		
		userMap.put(clientIdKey, clientIdentifierPojo);
		
		// kills the user after 2mins
		TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
            	// remove Identifier
            	userMap.remove(clientIdKey);
            	qrUtils.deleteFile();
            	
            	LOGGER.info(clientIdentifierPojo.getUserName() + " removed from registration waitlist.");
            }
        };

        Timer timer = new Timer(clientIdentifierPojo.getUserName());

        timer.schedule(timerTask, TIMEOUT);
	}
	
	public static ClientIdentifierPojo tryToGetUserToRegister(String clientIdKey) {
		
		ClientIdentifierPojo user = userMap.get(clientIdKey);
		userMap.remove(clientIdKey);
		return user;
		
	}
	
}
