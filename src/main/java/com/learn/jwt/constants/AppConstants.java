package com.learn.jwt.constants;

public interface AppConstants {

	public static final String JWT_APPLICATION_SUB="app";
	public static final Long TOKEN_EXPIRY_DAYS=100000L;
	public static final String JWT_OPAQUE_STRING = "Bearer ";
	public static final String JWT_AUTHORIZATION_HEADER="Authorization";
	public static final String JWT_USER_SUBJECT_KEY = "sub";
	public static final String JWT_USER_REFRESH = "refresh";
	public static final String JWT_USER_ROLE_KEY = "role";
	public static final String JWT_USER_KEY = "username";
	public static final String JWT_IAT_KEY = "iat";
	public static final String JWT_ID_KEY = "jti";
	public static final String JWT_USER_APPLICATION_KEY = "app";
	public static final String APP_LEARN= "learn";
}
