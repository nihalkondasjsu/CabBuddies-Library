package com.cabbuddieslib.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(8)
public class StripSensitiveDataAspect {

	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
		System.out.println(">"+proceedingJoinPoint.toLongString());
		Object o = proceedingJoinPoint.proceed();
		System.out.println("<"+proceedingJoinPoint.toLongString());
		return o;
	}
	
}
