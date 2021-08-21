package com.learn.jwt.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.learn.jwt.constants.AppConstants;
import com.learn.jwt.exception.AppException;
import com.learn.jwt.exception.ExceptionResponse;
import com.learn.jwt.model.JwtResponse;
import com.learn.jwt.model.User;
import com.learn.jwt.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtility implements Serializable {
	
	private static final long serialVersionUID = 234234523523L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    Logger logger = LoggerFactory.getLogger(JwtUtility.class);

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Autowired
	@Lazy
    private UserService userService;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
    	logger.info("retrieve username from jwt token");
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
    	logger.info("retrieve expiration date from jwt token");
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
    	logger.info("for retrieving any information from token we will need the secret key");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        logger.info("check if the token has expired");
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    //generate token for user
    public String generateToken(UserDetails userDetails) {
    	logger.info("generate token for user");
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }


    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
    	logger.info("while creating the token -\n 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID\n"
    			+ "2. Sign the JWT using the HS512 algorithm and secret key.");
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }


    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
    	logger.info("validate token");
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
     
    private String createToken(Map<String, Object> claims) {

        return Jwts.builder()
        		.setClaims(claims)
        		.setId(String.valueOf(userService.generateUserTokenId()))
        		.setSubject(AppConstants.JWT_APPLICATION_SUB)
        		.setIssuedAt(new Date())
                .setExpiration(getExpirationTime(AppConstants.TOKEN_EXPIRY_DAYS))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    
    public String generateToken() {
        Map<String, Object> claims = new HashMap<>(); // <role,list<authorities>
        claims.put("application", AppConstants.JWT_APPLICATION_SUB);
        return createToken(claims);
    }
    
    protected Date getExpirationTime(Long expiryTimeDuration)
    {
    	Date now = new Date();
        Long expireInMilis = TimeUnit.MINUTES.toMillis(expiryTimeDuration);
        return new Date(expireInMilis + now.getTime());
    }
    
    public String createUserToken(User userModel,String jwt) {
        JwtResponse jwtModel = new  JwtResponse();
        jwtModel.setToken(jwt);
        jwtModel.setType(AppConstants.JWT_OPAQUE_STRING);
        jwtModel.setUserId(userModel.getUserId());
    	String refreshToken = userService.addUserAccessToken(jwtModel);
    	//String refreshToken = this.generateTokenWithClaims(userModel.getUserId(),null,null,"refresh");
    	return refreshToken;
    	
    }
    public boolean validateJwtToken(String token) {
        try {
          Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
          return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}
      }
    
    public String isTokenValid(String token) {
    	JwtResponse jwtModel = new  JwtResponse();
        jwtModel.setToken((String) extractKeyClaim(token,AppConstants.JWT_ID_KEY).toString());
        jwtModel.setType(AppConstants.JWT_OPAQUE_STRING);
       // jwtModel.setUserId(extractKeyClaim(token,HitsLimsConstants.JWT_USER_KEY).toString());
        User userModel = userService.validateUserToken(jwtModel);
        if (userModel == null) {
            throw new BadCredentialsException("Authentication failed ");
        }
		return userModel.getUserId();
	}
    
    public Object extractKeyClaim(String token, String keyClaim) {
        Claims claims = extractAllClaims(token);
        if(claims.get(keyClaim) != null) {
        	return claims.get(keyClaim);
        }else {
        	throw new AppException("Invalid jwt token", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    
    public Claims extractAllClaims(String token) {
        try {
        	return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}

    }
    
    public UsernamePasswordAuthenticationToken getAuthentication(String username) {
    	String message = "";
    	User userModel = new User();
    	List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		try {
			message = "Invalid username/password supplied";
			userModel.setUserId(username);
			userModel = userService.getUserDetailsByUserId(userModel);
			if (userModel == null) {
	            throw new BadCredentialsException("Authentication failed for " + username);
	        }
	        grantedAuthorities.add(new SimpleGrantedAuthority(userModel.getRoleName().toUpperCase()));
	        
	        
		} catch (Exception e) {
			throw new ExceptionResponse(new Date(), "Exption", "Invalid username/password supplied");
		}
        
    	return new UsernamePasswordAuthenticationToken(username, null,grantedAuthorities);
      }
       
    public boolean validateAccessToken(String token) {
		try { 
		     boolean tokenFlag = !isTokenExpired(token);
		     if(!tokenFlag) {
		    	 throw new BadCredentialsException("Refresh token invalid.Please make new signin request" );
		     }
		     return true;
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
		} catch (ExpiredJwtException ex) {
			throw ex;
		}
	}
}
