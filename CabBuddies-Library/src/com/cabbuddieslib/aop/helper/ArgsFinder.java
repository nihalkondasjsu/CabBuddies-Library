package com.cabbuddieslib.aop.helper;

import javax.servlet.http.HttpServletRequest;

import com.cabbuddieslib.data.exception.CustomException;

public class ArgsFinder {
	
	public static <T extends Object> T findArg(Object[] args, Class<T> cls) throws CustomException {
		System.out.println("Looking for "+cls.toString());
		int found = findArgIndex(args, cls);
		if(found == -1 || args[found]==null)
		throw new CustomException("No Argument of "+cls.getName()+" type");
		return cls.cast(args[found]);
	}
	
	public static int findArgIndex(Object[] args, Class<?> cls) throws CustomException {
		System.out.println("Looking for "+cls.toString());
		int i=0;
		for (Object object : args) {
			if(object != null)
				System.out.println(object.getClass().toString());
			if(cls.isInstance(object))
				return i;
			i++;
		}
		return -1;
	}
	
	public static HttpServletRequest getHttpServletRequest(Object[] args) {
		HttpServletRequest httpServletRequest=null;
		try {
			try {
				httpServletRequest = findArg(args, HttpServletRequest.class);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(httpServletRequest != null) {
				System.out.println("httpServletRequest found");
				return httpServletRequest;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpServletRequest;
	}
	
}
