package com.cabbuddieslib.utils;

import javax.servlet.http.HttpServletRequest;

public class RemoteIP {

	public static String getIP(HttpServletRequest hsr) {
		String ipAddress = hsr.getHeader("X-FORWARDED-FOR");  
		if (ipAddress == null || ipAddress.equals("0:0:0:0:0:0:0:1")) {  
		    ipAddress = hsr.getRemoteAddr();  
		}
		return ipAddress;
	}
	
}
