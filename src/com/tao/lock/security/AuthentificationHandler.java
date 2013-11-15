package com.tao.lock.security;

import java.util.HashMap;
import java.util.Map;
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
public class AuthentificationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthentificationHandler.class);
	
	private static HashMap<String, CloudUser> userMap = new HashMap<String, CloudUser>();

	private static final int TIMEOUT = 1000 * 60 * 2; // 2mins
	
	/**
	 * Adds a user to the waitlist to authentification.
	 * The waitlist entry is valid for 2mins.
	 * @param user
	 * @param token		Hashed Token
	 * @param qrUtils
	 */
	public static void addToWaitList(final CloudUser user, final String userName, final QRUtils qrUtils) {
		
		userMap.put(userName, user);
		
		// kills the user after 2mins
	 	TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
            	user.setSession(null);
            	userMap.remove(userName);
            	qrUtils.deleteFile();
            }
        };

        Timer timer = new Timer(user.getUserName());

        timer.schedule(timerTask, TIMEOUT);
		
	}
	
	public static CloudUser tryToGetUserToAuth(String userName) {
		
		CloudUser user = userMap.get(userName);
		userMap.remove(userName);
		return user;
	}
	

	
}
