package com.learn.jwt.model;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private String refreshToken;
  private String userId;
  private String userName;
  private String email;
  private List<String> roles;
  
  public JwtResponse() {
	}
  public JwtResponse(String token, String refreshToken, String userId, String userName, String email,
		List<String> roles) {
	this.token =token;
	this.refreshToken=refreshToken;
	this.userId = userId;
	this.userName = userName;
	this.email=email;
	this.roles = roles;
}

public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	  public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getRefreshToken() {
			return refreshToken;
		}

		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

}
