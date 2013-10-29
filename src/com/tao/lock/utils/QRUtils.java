package com.tao.lock.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Hashtable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.tao.lock.security.SecurityUtils;

/**
 * Utility to generate a QR-Code image file.
 * 
 * @author Joerg Hilscher
 * 
 * Not Using SecureRandom
 * http://android-developers.blogspot.de/2013/02/using-cryptography-to-store-credentials.html
 */
public class QRUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QRUtils.class);
	
	private static final String DESC = "/";
	private static final String FOLDER = "qrcodes";
	
	
	private File file;
	
	private SecureRandom random = new SecureRandom();
	
	private String filePath;
	
	private ServletContext context;
	
	/**
	 * pixel values for the size of the image.
	 */
	private final int width = 450, height = 450;
	
	/**
	 * File type.
	 */
	private final String FILE_TYPE = ".png";
	
	/**
	 * 
	 */
	public String renderQR(String qrText) {
		
		
			// try to remove old img
			deleteFile();
			
		    Charset charset = Charset.forName("UTF-8");
		    CharsetEncoder encoder = charset.newEncoder();
		    byte[] b = null;
		    try {
		        // Convert a string to UTF-8 bytes in a ByteBuffer
		        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(qrText));
		        b = bbuf.array();
		    } catch (CharacterCodingException e) {
		        LOGGER.error(e.getMessage());
		    }
	
		    try {
		        String data = new String(b, "UTF-8");
		        // get a byte matrix for the data
		        BitMatrix matrix = null;
	
		        com.google.zxing.Writer writer = new MultiFormatWriter();
		        
		        try {
		        	
		            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
		            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		            matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints);
		            
		        } catch (com.google.zxing.WriterException e) {
		        	
		        	LOGGER.error(e.getMessage());
		        	
		        }
		        

		        /**
		         * Write File to WebContent Folder
		         */
		        
		        // Generate random filename
		        if (filePath == null) {
					try {
						filePath = FOLDER + DESC + nextPictureId() + FILE_TYPE;
					} catch (NoSuchAlgorithmException e1) {
						e1.printStackTrace();
					}
		        }
		        
		        ServletContext servletContext;
		        
		        if (this.context == null) {
		        	FacesContext facesContext = FacesContext.getCurrentInstance();
		        	servletContext = (ServletContext)facesContext.getExternalContext().getContext();
		        } else {
		        	servletContext = context;
		        }
		        
				String path = servletContext.getRealPath(DESC + filePath);
				
				LOGGER.info("PATH: " + path);
				
				file = new File(path);
		      
		        
		        try {
		        	
		            MatrixToImageWriter.writeToFile(matrix, "PNG", file);
		            
		        } catch (IOException e) {
		        	
		        	LOGGER.error(e.getMessage());
		        }
		        
		   
		    } catch (UnsupportedEncodingException e) {
	        		LOGGER.error(e.getMessage());
		    }
			
		    return filePath;
	}
	
	
	/**
	 * deletes the qr-code image.
	 * @return
	 */
	public Boolean deleteFile() {
		if (file != null && file.delete()) {
			if (!file.exists())
				return true;
		} 
		return false;
	} 
	
	/**
	 * 	
	 * @param url
	 * @return
	 */
	public static String getFilenameFromUrl(String url) {
		String[] temp = url.split("/");
		
		String filename;
		
		if (temp.length >= 2)
			filename = temp[temp.length - 1];
		else 
			filename = url;
		
		return filename;
	}
	
	/**
	 * Creates a secure random String.
	 * @return random 32 char String
	 * @throws NoSuchAlgorithmException 
	 */
	public String nextPictureId() throws NoSuchAlgorithmException {
	    return new BigInteger(130, random).toString(32);
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public ServletContext getContext() {
		return context;
	}


	public void setContext(ServletContext context) {
		this.context = context;
	}
	
}
