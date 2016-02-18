package org.sandeep.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
//import java.sql.SQLException;


import com.mysql.jdbc.PreparedStatement;

public class MySqlDriver {
	final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final static String DB_URL = "jdbc:mysql://localhost:3306/auctionserver";
	final static String USER = "root";
	final static String PASS = "test";
	//Connect to database
	public static Connection createConnection(){
			Connection sqlConnect;
			try{
				//Get a connection to DB
				Class.forName(JDBC_DRIVER);
				sqlConnect = DriverManager.getConnection(DB_URL, USER,PASS);
			}catch(Exception exc){
				exc.printStackTrace();
				sqlConnect = null;
			}
			return sqlConnect;
		}
			//Close connection to database
 	public static void closeDb(Connection sqlConnect){
			//boolean isClosed = false;
			try{
				sqlConnect.close();	
				//isClosed = true;
			}catch(Exception exc){
 				System.out.println("Exception: Mysql Db closing.");
				exc.printStackTrace();
				
				//isClosed = false;
			}
			//return isClosed;
		}
 	public static Statement getCreatedStatement(Connection sqlConnect){
 		Statement sqlStatement = null;
 		try{
 			sqlStatement = sqlConnect.createStatement();
 		}catch(Exception exc){
 				System.out.println("Exception: Mysql statement creation.");
 				exc.printStackTrace();
 			}
 		return sqlStatement;
 	}
 	public static PreparedStatement getPreparedStatement(Connection sqlConnect,String sqlQuery){
 		PreparedStatement  sqlStatement = null;
 		try{
 			sqlStatement = (PreparedStatement) sqlConnect.prepareStatement(sqlQuery);
 		}catch(Exception exc){
 				System.out.println("Exception: Mysql statement preparation.");
 				exc.printStackTrace();
 			}
 		return sqlStatement;
 	}
 	public static boolean updateDatabaseWithStatement(Connection sqlConnect,String sqlQuery){
 		boolean status = false;
 		try{
 			Statement statement = getCreatedStatement(sqlConnect);
 			statement.executeUpdate(sqlQuery);
 			status = true;
 		}catch(Exception exc){
 			status = false;
				System.out.println("Exception: Mysql query update.");
				exc.printStackTrace();
			}
 		return status;
 	}
 	public static boolean updateDatabaseWithPrepStatement(PreparedStatement prepStatement){
 		boolean status = false;
 		try{
 			prepStatement.executeUpdate();
 			status = true;
 		}catch(Exception exc){
				System.out.println("Exception: Mysql prepared statement query update.");
				exc.printStackTrace();
			}
 		return status;
 	}
 	public static ResultSet runSqlQuery(Connection sqlConnect,String sqlQuery){
 		//System.out.println(sqlQuery);
 		ResultSet resultSet = null;
 		try{
 			Statement statement = getCreatedStatement(sqlConnect);
 			resultSet = statement.executeQuery(sqlQuery);
 		}catch(Exception exc){
				System.out.println("Exception: Mysql query execution.");
				exc.printStackTrace();
			}
 		return resultSet;
 	}
}
