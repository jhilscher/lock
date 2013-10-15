package com.tao.lock.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 
 * @author Joerg Hilscher.
 *
 */
public class SecurityUtils {

	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	
	public static final int ITERATIONS = 1000;
	
	public static final int BYTE_SIZE = 64;
	
	/**
	 * http://android-developers.blogspot.de/2013/02/using-cryptography-to-store-credentials.html
	 */
	public static String generateKey() throws NoSuchAlgorithmException {

		final int outputKeyLength = 256;
		
	    SecureRandom secureRandom = new SecureRandom();
	    // Do *not* seed secureRandom! Automatically seeded from system entropy.
	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	    keyGenerator.init(outputKeyLength, secureRandom);
	    SecretKey key = keyGenerator.generateKey();
	    
	    return toHex(key.getEncoded());
	}
	
	
	/**
     * Taken From:
     * https://crackstation.net/hashing-security.htm
     * Computes the PBKDF2 hash of a password.
     *
     * @param   password    the password to hash.
     * @param   salt        the salt
     * @param   iterations  the iteration count (slowness factor)
     * @param   bytes       the length of the hash to compute in bytes
     * @return              the PBDKF2 hash of the password
     */
    public static String pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return toHex(skf.generateSecret(spec).getEncoded());
    }
    
    /**
     * https://crackstation.net/hashing-security.htm
     * @param bytesize
     * @return
     */
    public static byte[] generateSalt(int bytesize) {
    	SecureRandom random = new SecureRandom();
        byte[] salt = new byte[bytesize];
        random.nextBytes(salt);
        return salt;
    }
    
    /**
     * https://crackstation.net/hashing-security.htm
     * Converts a byte array into a hexadecimal string.
     *
     * @param   array       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    public static String toHex(byte[] array)
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
    
    /**
     * https://crackstation.net/hashing-security.htm
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param   hex         the hex string
     * @return              the hex string decoded into a byte array
     */
    public static byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }
    
    /**
     * Validates a password using a hash.
     *
     * @param   password        the password to check
     * @param   correctHash     the hash of the valid password
     * @return                  true if the password is correct, false if not
     */
    public static boolean validateKey(char[] key, String hashString, String saltString)
        throws NoSuchAlgorithmException, InvalidKeySpecException
    {

        byte[] salt = fromHex(saltString);
        byte[] hash = fromHex(hashString);
        // Compute the hash of the provided password, using the same salt, 
        // iteration count, and hash length
        String testHash = pbkdf2(key, salt, ITERATIONS, hash.length);
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, fromHex(testHash));
    }
    
    /**
     * https://crackstation.net/hashing-security.htm
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line 
     * system using a timing attack and then attacked off-line.
     * 
     * @param   a       the first byte array
     * @param   b       the second byte array 
     * @return          true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }

    /**
     * 
     * @param a
     * @param b
     * @return
     */
    public static byte[] xor(byte[] a, byte[] b)
    {
    	final byte[] output = new byte[Math.min(a.length, b.length)];
    	
        for(int i = 0; i < a.length && i < b.length; i++)
        	output[i] = (byte) (a[i] ^ b[i]);
        return output;
    }
}
