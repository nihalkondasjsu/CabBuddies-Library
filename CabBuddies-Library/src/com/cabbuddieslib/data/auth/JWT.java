package com.cabbuddieslib.data.auth;

import javax.persistence.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class JWT {

	@Transient
	public static final Long STANDARD_VALIDITY=10*60*1000l;
	@Transient
	public static final Long STANDARD_RENEWAL_VALIDITY=5*60*1000l;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(50)",nullable=false, unique = true)
	private String id;
	
	private String name;

	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(columnDefinition = "varchar(50)",nullable=false, unique = true)
	private String pwd;
	
	private String ip;
	
	private Long validTill;
	
	private Long userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd.length()<50 ? pwd : pwd.substring(0, 50);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getValidTill() {
		return validTill;
	}

	public void setValidTill(Long validTill) {
		this.validTill = validTill;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@JsonIgnore
	public boolean isValid(JWT jwt) {
		return 	isUntampered(jwt) && jwt.isValid();
	}
	
	
	@JsonIgnore
	public boolean isValid() {
		return (this.validTill>=System.currentTimeMillis());
	}
	
	@JsonIgnore
	public boolean isUntampered(JWT jwt) {
		return 	(this.id.equals(jwt.id)) &&
				(this.pwd.equals(jwt.pwd)) &&
				(this.ip.equals(jwt.ip)) &&
				(this.validTill.equals(jwt.validTill)) ;
	}
	
	@JsonIgnore
	public boolean isRenewable() {
		return System.currentTimeMillis()-this.validTill<=STANDARD_RENEWAL_VALIDITY;
	}
	
}

/*
 * 
 * 
 * c -> um: Login
um -> umdb: validate credentials
umdb --> um: valid credentials ( user details )
um -> um: Create JWT
um -> umdb: Save JWT
um -> um: Remove UserId from JWT
um -> um: Excrypt JWT
um --> c: Encrypted JWT

 * 
 * 
 * */

/*
 * 
 * 
 * @startuml
participant "Client App" as c

participant "Chatroom" as cr
participant "Chatroom DB" as crdb

participant "User Management" as um
participant "User Management DB" as umdb

title Using JWT for Authentication

== First Interaction of Chatroom and Client App with this JWT ==

c -> cr: Request with Encrypted JWT in AuthHeader

cr -> crdb: validate JWT

crdb --> cr: JWT not found

cr -> um: validate JWT

um -> umdb: validate JWT

umdb --> um: Full JWT with userId

um --> cr: Full JWT with userId

cr --> c: Response (if authorized) 

== Consequent Interactions of Chatroom and Client App with this JWT ==

c -> cr: Request with Encrypted JWT in AuthHeader
cr -> crdb: validate JWT
crdb --> cr: Full JWT with userId
cr --> c: Response (if authorized) 

@enduml
 * 
 * */