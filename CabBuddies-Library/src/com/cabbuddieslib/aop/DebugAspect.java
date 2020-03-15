package com.cabbuddieslib.aop;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.cabbuddieslib.aop.helper.ArgsFinder;

@Aspect
@Component
public class DebugAspect {
	@Around("@annotation(com.cabbuddieslib.aop.helper.annotation.Debug)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("starting "+proceedingJoinPoint.toShortString()+" with "+Arrays.toString(proceedingJoinPoint.getArgs()));
		try {
			HttpServletRequest hsr = ArgsFinder.getHttpServletRequest(proceedingJoinPoint.getArgs());
			Enumeration<?> names = hsr.getHeaderNames();
			
			while(names.hasMoreElements()) {
				String t = names.nextElement().toString();
				System.out.println(t + " => " + hsr.getHeader(t));
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		Object obj = proceedingJoinPoint.proceed();
		System.out.println("finished "+proceedingJoinPoint.toShortString()+" with "+obj);
		return obj;
	}
	
}
