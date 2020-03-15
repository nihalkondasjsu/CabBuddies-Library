package com.cabbuddieslib.aop.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import com.cabbuddieslib.data.exception.CustomException;

public class ArgsFinder {

	public static <T extends Object> T findArg(Object[] args, Class<T> cls) throws CustomException {
		System.out.println("Looking for "+cls.toString());
		for (Object object : args) {
			if(object == null)
				continue;
			System.out.println(object.getClass().toString());
			if(cls.isInstance(object))
				return cls.cast(object);
		}
		throw new CustomException("No Argument of "+cls.getName()+" type");
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
