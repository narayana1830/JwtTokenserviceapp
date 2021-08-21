package com.learn.jwt.securityutil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.learn.jwt.model.User;
import com.learn.jwt.service.UserService;

@Component
public class AppAuthenticationProvider implements AuthenticationProvider {

	@Autowired
   private  UserService userService;
	
	 public AppAuthenticationProvider() {
	       
	    }
	
	@Override
    public Authentication authenticate(Authentication authentication)  throws AuthenticationException {
		String username = authentication.getName();
        Object credentials = authentication.getCredentials();
        if (!(credentials instanceof String)) {
            return null;
        }
        
        String password = credentials.toString();
        User userModel = new User();
		try {
			userModel.setUserId(username);
			userModel.setUserName(username);
			userModel.setPassword(password);
			userModel.setCreatedBy(username);
			userModel.setModifiedStr(username);
			if(userService.addUserDetails(userModel) > 0) {
			userModel = userService.validateUserLogin(userModel);
			}
			else {
				userModel = null;
				throw new BadCredentialsException("Authentication failed for " + username);
			}
		} catch (Exception e) {
			userModel = null;
			throw new BadCredentialsException("Authentication failed for " + username);
			
		}
		if (userModel == null) {
            throw new BadCredentialsException("Authentication failed for " + username);
        }
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
       // Authentication auth = new UsernamePasswordAuthenticationToken(userModel.getUserId(), userModel.getPassword(), grantedAuthorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(userModel, null, grantedAuthorities);
        return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}
