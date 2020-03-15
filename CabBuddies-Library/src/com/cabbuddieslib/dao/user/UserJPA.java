package com.cabbuddieslib.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cabbuddieslib.data.user.User;

public interface UserJPA extends JpaRepository<User,Long> {

	@Query("select u from User u left join u.userDetails ud where ud.email = :email")
	public User findUserByEmail(@Param("email")String email);
	
}
