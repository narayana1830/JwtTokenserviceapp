package com.learn.jwt.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learn.jwt.exception.AppException;
import com.learn.jwt.model.JwtRequest;
import com.learn.jwt.model.JwtResponse;
import com.learn.jwt.model.User;
import com.learn.jwt.service.UserService;
import com.learn.jwt.utility.JwtUtility;




@RestController

public class HomeController {
	
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtility jwtUtility;
	
	@GetMapping("/")
	public String getHomePage() {
		logger.info("Home Page");
		return "hello";
	}
	
	@PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
		logger.info("authenticate");
		String jwt = "";
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(), jwtRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		jwt = jwtUtility.generateToken();
		User user = (User) authentication.getPrincipal();
        List<String> roles = authentication.getAuthorities().stream().map(item -> item.getAuthority())
		        .collect(Collectors.toList());
        final String token = jwtUtility.generateToken();
        String refreshToken = jwtUtility.createUserToken(user,jwt);
		if(refreshToken == null || refreshToken.equals("")) {
			throw new AppException("Please logout and make new signin request for user", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		//return ResponseEntity.ok(jwt);

		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken,user.getUserId(),user.getUserName(), user.getEmailId(), roles));
    }
	
	private void authenticate(String username, String password) throws Exception {
		try {
			Objects.requireNonNull(username);
			Objects.requireNonNull(password);
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

}
