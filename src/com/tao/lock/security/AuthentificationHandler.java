package com.tao.lock.security;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;





import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tao.lock.entities.CloudUser;
import com.tao.lock.qrservice.QRUtils;

/**
 * 
 * @author Joerg Hilscher
 *
 */
@Singleton
public class AuthentificationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationHandler.class);
	
	private static HashMap<String, CloudUser> userMap = new HashMap<String, CloudUser>();

	private static final int TIMEOUT = 1000 * 60 * 2; // 2mins
	
	public static void addToWaitList(final CloudUser user, final String token, final QRUtils qrUtils) {
		
		userMap.put(token, user);
		
		// kills the user after 2mins
		 TimerTask timerTask = new TimerTask() {

	            @Override
	            public void run() {
	            	user.setSession(null);
	            	userMap.remove(token);
	            	qrUtils.deleteFile();
	            }
	        };

	        Timer timer = new Timer(user.getUserName());

	        timer.schedule(timerTask, TIMEOUT);
		
	}
	
	public static CloudUser tryToGetUserToAuth(String token) {
		
		CloudUser user = userMap.get(token);
		userMap.remove(token);
		return user;
	}
	
}
