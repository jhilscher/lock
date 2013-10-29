package com.tao.lock.security;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;






import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.utils.QRUtils;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@Singleton
public class RegistrationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationHandler.class);
	
	private static HashMap<String, CloudUser> userMap = new HashMap<String, CloudUser>();

	private static final int TIMEOUT = 1000 * 60 * 2; // 2mins
	
	public static void addToWaitList(final CloudUser user, final String clientIdKey, final QRUtils qrUtils) {
		
		userMap.put(clientIdKey, user);
		
		// kills the user after 2mins
		 TimerTask timerTask = new TimerTask() {

	            @Override
	            public void run() {
	            	// remove Identifier
	            	user.setIdentifier(null);
	            	userMap.remove(clientIdKey);
	            	qrUtils.deleteFile();
	            }
	        };

	        Timer timer = new Timer(user.getUserName());

	        timer.schedule(timerTask, TIMEOUT);
		
	}
	
	public static CloudUser tryToGetUserToRegister(String clientIdKey) {
		
		CloudUser user = userMap.get(clientIdKey);
		userMap.remove(clientIdKey);
		return user;
	}
	
}
