package com.cabbuddieslib.aop.aspect;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.cabbuddieslib.aop.helper.ArgsFinder;
import com.cabbuddieslib.data.auth.JWT;
import com.cabbuddieslib.managers.auth.JWTManager;

@Aspect
@Component
@DependsOn({"JWTManager"})
public class JWTValidateAspect {

	@Autowired
	JWTManager jwtManager;
	
	@Around("@annotation(com.cabbuddieslib.aop.helper.annotation.JWTValidate)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("starting "+proceedingJoinPoint.toShortString()+" with "+Arrays.toString(proceedingJoinPoint.getArgs()));
		Object[] args = proceedingJoinPoint.getArgs();
		try {
			
			int found = ArgsFinder.findArgIndex(args, JWT.class);
			
			args[found] = jwtManager.validateJWT(
							jwtManager.parseJWT(
									ArgsFinder.findArg(
											args,
											HttpServletRequest.class).getHeader("authorization").substring("Bearer ".length())
									)
							);
			

			Object obj = proceedingJoinPoint.proceed(args);
			System.out.println("finished "+proceedingJoinPoint.toShortString()+" with "+obj);
			return obj;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
}
