package com.cabbuddieslib.data.auth;

import javax.persistence.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class JWT {

	@Transient
	public static final Long STANDARD_VALIDITY=10*60*1000l;
	@Transient
	public static final Long STANDARD_RENEWAL_VALIDITY=5*60*1000l;
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "CHAR(32)")
	private String id;
	

	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(columnDefinition = "CHAR(32)")
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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
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
	
	public boolean isValid(JWT jwt) {
		return 	isUntampered(jwt) && jwt.isValid();
	}
	
	
	public boolean isValid() {
		return (this.validTill>=System.currentTimeMillis());
	}
	
	public boolean isUntampered(JWT jwt) {
		return 	(this.id.equals(jwt.id)) &&
				(this.pwd.equals(jwt.pwd)) &&
				(this.ip.equals(jwt.ip)) &&
				(this.validTill.equals(jwt.validTill)) ;
	}
	
	public boolean isRenewable() {
		return System.currentTimeMillis()-this.validTill<=STANDARD_RENEWAL_VALIDITY;
	}
	
}
