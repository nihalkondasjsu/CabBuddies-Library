package com.cabbuddieslib.utils;

import com.cabbuddieslib.data.auth.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Serialize {

	public static String toString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
	    String jsonString="{}";
		try {
			jsonString = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public static <T extends Object> T toPOJO(String json,Class<T> type) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return type.cast(mapper.readValue(json, type.getClass()));
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
