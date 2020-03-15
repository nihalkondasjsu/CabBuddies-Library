package com.cabbuddieslib.dao.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cabbuddieslib.data.auth.JWT;

public interface JWTJPA extends JpaRepository<JWT, String>{

	@Query("select jwt from JWT jwt where id = :id and pwd = :pwd")
	public JWT findJWTByIdAndPwd(@Param("id")String id,@Param("pwd")String pwd);

	
	@Transactional
	@Modifying
	@Query("delete from JWT where validTill < :expiringBy")
	public void deleteAllJWTExpiringBy(@Param("expiringBy")Long expiringBy);
	
	@Transactional
	@Modifying
	@Query("delete from JWT where userId = :userId")
	public void deleteAllJWTByUserId(@Param("userId")Long userId);
	
}
