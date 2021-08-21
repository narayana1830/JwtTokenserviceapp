package com.learn.jwt.service;

import java.util.List;

import com.learn.jwt.model.JwtResponse;
import com.learn.jwt.model.User;

public interface UserService  {

	public List<User> getUserDetails();
	
	public User getUserDetailsByUserId(User user);
	
	public int addUserDetails(User user);
	
	public int updateUserDetailsByUserId(User user);
	
	public int updateTokenByUserId();
	
	public User loadUserByUsername(String userId);

	public User validateUserLogin(User user);
	
	public Long generateUserTokenId();
	
	public String addUserAccessToken(JwtResponse jwtModel);
	
	public User validateUserToken(JwtResponse jwtModel);
}
