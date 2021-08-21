package com.learn.jwt.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	private String password;
	private String emailId;
	private Long contanctNo;
	private String token;
	private String tokenExprired;
	private String tokenModifiedDate;
	private String passwordModifiedOn;
	private String roleName;
	private String createdBy;
	private Date createdOn;
	private String modifiedStr;
	private Date modifiedOn;
	private String tokenType;
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Long getContanctNo() {
		return contanctNo;
	}
	public void setContanctNo(Long contanctNo) {
		this.contanctNo = contanctNo;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenExprired() {
		return tokenExprired;
	}
	public void setTokenExprired(String tokenExprired) {
		this.tokenExprired = tokenExprired;
	}
	public String getTokenModifiedDate() {
		return tokenModifiedDate;
	}
	public void setTokenModifiedDate(String tokenModifiedDate) {
		this.tokenModifiedDate = tokenModifiedDate;
	}
	public String getPasswordModifiedOn() {
		return passwordModifiedOn;
	}
	public void setPasswordModifiedOn(String passwordModifiedOn) {
		this.passwordModifiedOn = passwordModifiedOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedStr() {
		return modifiedStr;
	}
	public void setModifiedStr(String modifiedStr) {
		this.modifiedStr = modifiedStr;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
