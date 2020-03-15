package com.cabbuddieslib.data.managers.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.cabbuddieslib.dao.auth.JWTJPA;
import com.cabbuddieslib.data.auth.JWT;
import com.cabbuddieslib.utils.AESCrypto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JWTManager {

	@Autowired(required=true)
	JWTJPA jwtJpa;
	
	public JWT createJWT(String ip,Long userId) {
		JWT jwt=new JWT();
		
		jwt.setIp(ip);
		jwt.setUserId(userId);
		
		jwt.setValidTill(System.currentTimeMillis()+JWT.STANDARD_VALIDITY);
		
		jwt.setPwd(UUID.randomUUID().toString());
		
		jwtJpa.deleteAllJWTByUserId(userId);
		
		return jwtJpa.save(jwt);
	}
	
	public JWT renewJWT(JWT jwt,String ip) {
		JWT jwtRes = null;
		
		jwtRes = jwtJpa.findJWTByIdAndPwd(jwt.getId(), jwt.getPwd());
		
		if(jwtRes==null)
			return null;
		
		if(jwtRes.isUntampered(jwt) && jwtRes.isRenewable())
			return createJWT(ip,jwtRes.getUserId());
		
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
		if(jwtRes==null)
			return null;
		if(jwtRes.isValid(jwtInp))
			return jwtRes;
		
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
