package com.learn.jwt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.learn.jwt.dao.UserDao;
import com.learn.jwt.exception.UserNotFoundException;
import com.learn.jwt.model.JwtResponse;
import com.learn.jwt.model.User;
import com.learn.jwt.service.UserService;

@Service
public class UserServiceImpl implements UserService {
Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
   @Autowired
   private UserDao userDao;
   
	@Override
	public com.learn.jwt.model.User loadUserByUsername(String username) throws UserNotFoundException {
		return userDao.loadUserByUsername(username);
		
	}

	@Override
	public List<com.learn.jwt.model.User> getUserDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public com.learn.jwt.model.User getUserDetailsByUserId(com.learn.jwt.model.User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addUserDetails(com.learn.jwt.model.User user) {
		
		return userDao.addUserDetails(user);
	}

	@Override
	public int updateUserDetailsByUserId(com.learn.jwt.model.User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateTokenByUserId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User validateUserLogin(User user) {
		// TODO Auto-generated method stub
		return userDao.validateUserLogin(user);
	}

	@Override
	public Long generateUserTokenId() {
		// TODO Auto-generated method stub
		return userDao.generateUserTokenId();
	}

	@Override
	public String addUserAccessToken(JwtResponse jwtModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User validateUserToken(JwtResponse jwtModel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
