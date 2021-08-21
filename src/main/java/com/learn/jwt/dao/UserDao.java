package com.learn.jwt.dao;

import java.util.List;

import com.learn.jwt.model.JwtResponse;
import com.learn.jwt.model.User;

public interface UserDao {

	public List<User> getUserDetails();
	
	public User getUserDetailsByUserId(User user);
	
	public int addUserDetails(User user);
	
	public int updateUserDetailsByUserId(User user);
	
	public int updateTokenByUserId();
	
	public User loadUserByUsername(String userId);
	
	public Long generateUserTokenId();
	
	public User validateUserToken(JwtResponse jwtModel);
	
	public User validateUserLogin(User user);
	
}
