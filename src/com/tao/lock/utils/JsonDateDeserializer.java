package com.tao.lock.utils;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Helper to deserialize c#-date format in JSON
 * FROM: http://stackoverflow.com/questions/12878693/gson-jsonsyntaxexception-on-date
 * 
 * c#-Format example: "\/Date(1384297200000+0100)\/"
 * 
 * @author Joerg Hilscher
 *
 */
public class JsonDateDeserializer implements JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	      String s = json.getAsJsonPrimitive().getAsString();
	      
	      int plusChar = s.indexOf("+");
	      int end = 0;
	      
	      if (plusChar > 0)
	    	  end = plusChar;
	      else
	    	  end = s.length() - 2;
	      
	      long l = Long.parseLong(s.substring(6, end));
	      Date d = new Date(l);
	      
	      return d; 
	   }

		
}
