package com.learn.jwt.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.learn.jwt.dao.UserDao;
import com.learn.jwt.exception.AppException;
import com.learn.jwt.exception.ExceptionResponse;
import com.learn.jwt.model.JwtResponse;
import com.learn.jwt.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Repository
public class UserDaoImpl extends JdbcDaoSupport implements UserDao{
	Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	@Autowired
	DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	@Override
	public List<User> getUserDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserDetailsByUserId(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int addUserDetails(User user) {
		String userDetailssql=" INSERT INTO app_users(user_id, user_name, password, created_by, created_on, modified_by,  modified_on) VALUES(?,?,?,?,SYSDATE(),?,SYSDATE())";
		
	 boolean useAdded = getJdbcTemplate().execute(userDetailssql, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean  doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, user.getUserId());
				ps.setString(2, user.getUserName());
				ps.setString(3, user.getPassword());
				ps.setString(4, user.getUserId());
				ps.setString(5, user.getUserId());
				return ps.execute();
			}
		}); 
	 if(useAdded){
	  String tokenSql = "INSERT INTO app_user_token(user_id) VALUES(?,?)";
	   useAdded = getJdbcTemplate().execute(tokenSql, new PreparedStatementCallback<Boolean >() {

			@Override
			public Boolean  doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, user.getUserId());
				return ps.execute();
			}
		}); 
	   if(useAdded) {
		   return 1;
	   }
	   else {
		   return -1;
	   }
	}
		return -1;
	}

	@Override
	public int updateUserDetailsByUserId(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateTokenByUserId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User loadUserByUsername(String userId) {
		String sql ="SELECT user_id, user_name FROM  cmn_app_user WHERE user_id = ? ";
		User user = (User) getJdbcTemplate().execute(sql,  new PreparedStatementCallback<Object>() {

			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				User u= new User();
				ResultSet rs = ps.getResultSet();
				if(rs != null && rs.next()) {
				u.setUserId(rs.getString(1));
				u.setUserName(rs.getString(2));
				}
				return u;
			}
			
		});
		return user;
	}

	@Override
	public Long generateUserTokenId() {
		final SqlRowSet sqlRowSet = getJdbcTemplate().queryForRowSet("SELECT next.val FROM app_token_seq");
		sqlRowSet.next();// mandatory to move the cursor 
		 return  sqlRowSet.getLong(1);
	}

	@Override
	public User validateUserToken(JwtResponse jwtModel) {
		User user=null;
		String sql = "SELECT cut.user_id FROM app_user_token cut " + 
				"JOIN app_users usr on usr.user_id = cut.user_id " + 
				"where cut.token_id = ? and cut.token_type = ? ";
		 user	=	getJdbcTemplate().execute(sql, new PreparedStatementCallback<User>(){

			@Override
			public User doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ResultSet   rs = ps.getResultSet();
				User user1 = new User();
				if(rs != null && rs.next()) {
				        
				user1.setUserId(sql);
				}
				return user1;
			}
			
		});
		if (user == null) {
			logger.error("Invalid JWT signature");
			throw new ExceptionResponse(new Date(), "Invalid JWT signature", "JWT SIGNATURE");
		}

		return user;
	}

	@Override
	public User validateUserLogin(User user) {
		String sql="SELECT user_id, user_name FROM app_users Where lower(user_id) = ?";
		return getJdbcTemplate().query(sql, new Object[] {user.getUserId()}, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				User user = null;
				if(rs != null && rs.next()) {
				user= new User();
				user.setUserId(rs.getString(1));
				user.setUserName(rs.getString(2));
				return user;
				}
				return user;
				
			}
			
		});
	}

}
