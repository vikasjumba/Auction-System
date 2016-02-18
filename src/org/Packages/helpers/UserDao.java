package org.sandeep.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;



//import java.sql.Statement;
import org.sandeep.helpers.MySqlDriver;

public class UserDao {
	public  boolean deleteUserWithId(String userId)
	{
		Connection sqlConnect = MySqlDriver.createConnection();
		String sqlQuery = String.format("DELETE from userinfo where user_id=%s", userId);
		boolean status = MySqlDriver.updateDatabaseWithStatement(sqlConnect,sqlQuery);
		MySqlDriver.closeDb(sqlConnect);
		return status;
	}
	private static UserDTO getUserFromSqlResult(ResultSet resultSet) throws SQLException{
		UserDTO user = new UserDTO();
		user.setUserName(resultSet.getString("username"));
		user.setEmail(resultSet.getString("email"));
		user.setFirstName(resultSet.getString("first_name"));
		user.setLastName(resultSet.getString("last_name"));
		user.setPassword("");
		user.setUserId(resultSet.getString("user_id"));
		return user;
	}
	//Authenticate the user login.
	public  UserDTO authenticateLogin(String userName, String password) throws SQLException{
		UserDTO userInfo = null;	
		 String sqlQuery = "Select password from userinfo where username='" + userName + "'";
		 Connection sqlConnect = MySqlDriver.createConnection();
		 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
		 while(resultSet.next())
		 {
				String passwdInDb = resultSet.getString("password");
				if(passwdInDb.equals(password)){
					userInfo = getUserInfo(sqlConnect,userName,password);
				}
			}
		 MySqlDriver.closeDb(sqlConnect);
		
		return userInfo;
	}
	public UserDTO getBidWinnerForItem(String itemId) throws SQLException{
		UserDTO user = new UserDTO();
		String	 sqlQuery = String.format("Select user_id " 
				 +" from bids "
				 +" where item_id=%s" 
				 +" and bid_value = (select max(bid_value) "
				 		 		    +" from bids "
				 		 		    +" where item_id=%s) " 
				 +" and bid_timestamp = ( select min(bid_timestamp) "
				 		 				+" from bids " 
				 		 				+" where item_id=%s "
				 		 				+" and bid_value = (select max(bid_value)" 
				 		 				+" from bids " 
				 		 				+" where item_id=%s)); ", itemId,itemId,itemId,itemId);
		Connection sqlConnect = MySqlDriver.createConnection();
		ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
		 
     	 //String winnerName = null;
     	 String winnerId = null;
		 while(resultSet.next()){
			 winnerId = String.valueOf(resultSet.getInt("user_id"));
		 }
		 sqlQuery = String.format("Select * "
		 		+ " from userinfo "
		 		+ " where user_id=%s;", winnerId);
		 resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
		 while(resultSet.next()){
				user = getUserFromSqlResult(resultSet);
		 }
		 MySqlDriver.closeDb(sqlConnect);
		return user;
	}
	private static UserDTO getUserInfo(Connection sqlConnect, String userName, String password) throws SQLException {			
				String sqlQuery = String.format("Select * from userinfo where userName='%s' AND password='%s';", userName,password);
				ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
				UserDTO user = null;
				while(resultSet.next())
				{
				user = getUserFromSqlResult(resultSet);
				}	
			return user;
		}
		//Register the user
	public  int registerUserDetails(UserDTO user) throws SQLException{
		/*
		 * status = 1 Registration successful
		 * status = -1 Registration Error or Exception
		 * status = 0 Already Registered
		 * */
			String firstname = user.getFirstName();
			String lastname = user.getLastName();
			String email = user.getEmail();
			String username = user.getUserName();
			String password = user.getPassword();
			int status = -1;
			 String sqlQuery = "Select username from userinfo where email='" + email +"'";
			 Connection sqlConnect = MySqlDriver.createConnection();
			 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
			 while(resultSet.next()){
				 String userNameDb = resultSet.getString("username");
				 if(null != userNameDb){
				  status = 0;
				 }
			 }
			 if(status == -1)
			 {
	         String insertQuery = String.format("INSERT INTO userinfo (Email, First_name, Last_name, Username, Password) VALUES ('%s', '%s', '%s', '%s', '%s')",email,firstname,lastname,username,password);
	         boolean res = MySqlDriver.updateDatabaseWithStatement(sqlConnect,insertQuery);	 
			  if(res) {
				  status = 1;
			  }
			 }
			 MySqlDriver.closeDb(sqlConnect);
		return status;
	

}
}