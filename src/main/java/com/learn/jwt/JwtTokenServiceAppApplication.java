package com.learn.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.learn.jwt.controller.HomeController;

@SpringBootApplication
public class JwtTokenServiceAppApplication {

	Logger logger = LoggerFactory.getLogger(JwtTokenServiceAppApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(JwtTokenServiceAppApplication.class, args);
	}
	
	/*
	 * @Bean public PasswordEncoder passwordEncoder() {
	 * logger.info("Password Endcoder"); return NoOpPasswordEncoder.getInstance(); }
	 */

}
