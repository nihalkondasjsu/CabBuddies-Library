package com.cabbuddieslib.managers.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cabbuddieslib.dao.auth.JWTJPA;
import com.cabbuddieslib.data.auth.JWT;
import com.cabbuddieslib.discover.MicroServicesDiscovery;
import com.cabbuddieslib.utils.AESCrypto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Component
public class JWTManager {

	@Autowired(required=true)
	JWTJPA jwtJpa;
	
	public static boolean isJWTAuthority=false;
	
	public JWT createJWT(String ip,Long userId,String name) {
		JWT jwt=new JWT();
		
		jwt.setIp(ip);
		jwt.setUserId(userId);
		jwt.setName(name);
		
		jwt.setValidTill(System.currentTimeMillis()+JWT.STANDARD_VALIDITY);
		
		jwt.setPwd(UUID.randomUUID().toString());
		
		jwtJpa.deleteAllJWTByUserId(userId);
		
		return jwtJpa.save(jwt);
	}
	
	public JWT renewJWT(JWT jwt,String ip) {
		if(jwt.getIp().equals(ip)==false)
			return null;
		JWT jwtRes = null;
		
		jwtRes = jwtJpa.findJWTByIdAndPwd(jwt.getId(), jwt.getPwd());
		
		if(jwtRes==null)
			return null;
		
		if(jwtRes.isUntampered(jwt) && jwtRes.isRenewable())
			return createJWT(ip,jwtRes.getUserId(),jwtRes.getName());
		
		return null;
	}

	public void flushNonRenewableJWT() {
		jwtJpa.deleteAllJWTExpiringBy(System.currentTimeMillis()-JWT.STANDARD_RENEWAL_VALIDITY);
	}
	
	public void flushInValidJWT() {
		jwtJpa.deleteAllJWTExpiringBy(System.currentTimeMillis());
	}
	
	public void saveJWT(JWT jwt) {
		jwtJpa.deleteAllJWTByUserId(jwt.getUserId());
		jwtJpa.save(jwt);
	}
	
	public JWT validateJWT(JWT jwtInp) {
		JWT jwtRes = null;
		jwtRes = jwtJpa.findJWTByIdAndPwd(jwtInp.getId(), jwtInp.getPwd());
		if(jwtRes==null) {
			if(isJWTAuthority)
				return null;
			jwtRes = fetchJWT(jwtInp);
			if(jwtRes == null)
				return null;
			saveJWT(jwtRes);
		}
		if(jwtRes.isValid(jwtInp))
			return jwtRes;
		
		return null;
	}
	
	private JWT fetchJWT(JWT jwtInp) {
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
					.header("authorization", "Bearer "+transferVersion(jwtInp))
			      .url(MicroServicesDiscovery.USER_MANAGEMENT_SERVICE+"/validateJWT")
			      .build();

			  try {
				String response = client.newCall(request).execute().body().string();
				System.out.println(response);
			    return new Gson().fromJson(response, JWT.class);
			  }catch (Exception e) {
				  e.printStackTrace();
			}
		return null;
	}

	public String clientVersion(JWT jwt) {
		jwt.setUserId(-1l);
		return transferVersion(jwt);
	}
	
	
	public String transferVersion(JWT jwt) {
		ObjectMapper mapper = new ObjectMapper();
	    String jsonString="";
		try {
			jsonString = mapper.writeValueAsString(jwt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(jsonString);
		return AESCrypto.encrypt(jsonString, true);
	}
	
	public JWT parseJWT(String text) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readValue(AESCrypto.decrypt(text, true), JWT.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
