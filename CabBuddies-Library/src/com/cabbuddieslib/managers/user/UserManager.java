package com.cabbuddieslib.managers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.cabbuddieslib.dao.user.UserJPA;
import com.cabbuddieslib.data.exception.CustomException;
import com.cabbuddieslib.data.user.User;
import com.cabbuddieslib.data.user.UserDetails;
import com.cabbuddieslib.reference.Messages;
import com.cabbuddieslib.utils.Serialize;

@Component
public class UserManager {


	@Autowired(required=true)
	UserJPA userJpa;
	
	public User registerUser(UserDetails userDetails) throws CustomException {
		
		userDetails.prepareForRegistration();
		
		User user = userJpa.findUserByEmail(userDetails.getEmail());
		
		if(user!=null)
			throw new CustomException(Messages.Registration.EMAIL_EXISTS);
		
		user = new User();
		
		user.setUserDetails(userDetails);
		
		return userJpa.save(user);
		
	}
	
	public User loginUser(UserDetails userDetails) throws CustomException {
		userDetails.prepareForLogin();
		User user = userJpa.findUserByEmail(userDetails.getEmail());
		
		if(user==null)
			throw new CustomException(Messages.Login.EMAIL_NOT_FOUND);
		
		System.out.println(Serialize.toString(user));
		
		if(user.getUserDetails().getPassword().equals(userDetails.getPassword()))
			return user;
		throw new CustomException(Messages.Login.INCORRECT_PASSWORD);
	}
	
	public User getUser(Long id) {
		return userJpa.findById(id).get();
	}
	
}
