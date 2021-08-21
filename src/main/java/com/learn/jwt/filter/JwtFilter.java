package com.learn.jwt.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learn.jwt.constants.AppConstants;
import com.learn.jwt.exception.ExceptionResponse;
import com.learn.jwt.service.UserService;
import com.learn.jwt.utility.JwtUtility;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtUtility jwtUtility;
	
	 Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader(AppConstants.JWT_AUTHORIZATION_HEADER);
		  String userId = null;
	        String token = null;
	        String application = null;
	        String subject = null;
	        try {
			        if(null != authorization && authorization.startsWith(AppConstants.JWT_OPAQUE_STRING)) {
			            token = authorization.substring(7);
			        }
			        if (token != null  && jwtUtility.validateJwtToken(token)) {
			        	userId = jwtUtility.isTokenValid(token);
			        	 application = jwtUtility.extractKeyClaim(token,AppConstants.JWT_USER_APPLICATION_KEY).toString();
			             subject = jwtUtility.extractKeyClaim(token,AppConstants.JWT_USER_SUBJECT_KEY).toString();
			             if(subject != null && !subject.equalsIgnoreCase(AppConstants.APP_LEARN)) {
			             	throw new UnsupportedJwtException("Invalid Token "+token);
			     		}
			             if(userId == null || userId.trim().equals("")) {
			             	throw new UnsupportedJwtException("Invalid Token "+token);
			             }
			        }
			        if (userId != null && !userId.trim().equals("") && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			            if (jwtUtility.validateAccessToken(token)) {
			            	
			            	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtility.getAuthentication(userId);   //3
			            	usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			           	 	SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			           	 	request.setAttribute("userId", userId);
			           	 	request.setAttribute("claims", jwtUtility.extractAllClaims(token));
			            }else {
			            	throw new UnsupportedJwtException("Invalid Token "+token);
			            }
			        }

	        }catch (ExpiredJwtException ex) {        	
	        	throw new ExceptionResponse(new Date(), ex.getMessage(), "Access token expired!.Please make a new sign-in request");
	        } catch (BadCredentialsException ex) {
	        	throw new ExceptionResponse(new Date(), ex.getMessage(), "Bad Creditional exception");
	        } catch (Exception ex) {
	        	throw new ExceptionResponse(new Date(), ex.getMessage(), "Application not working");
	        }

	        filterChain.doFilter(request, response);
	}
}
