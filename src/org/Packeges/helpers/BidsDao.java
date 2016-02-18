package org.sandeep.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class BidsDao {

	public  String getHighestBidSoFar(String itemId) throws SQLException{
		String res = "-1";
			 String sqlQuery = String.format("Select bid_value "
			 							   + " from bids where item_id = %s "
			 							    + " and bid_timestamp = "
			 										+ "(select max(bid_timestamp) from bids where item_id=%s)", itemId,itemId);
			 Connection sqlConnect = MySqlDriver.createConnection();
			 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
			 while(resultSet.next()){	
				 res = String.valueOf(resultSet.getDouble("bid_value"));
			 }
			 MySqlDriver.closeDb(sqlConnect);
		return res;
	}
	
	public  HashMap<String, String> getLatestBidsForUser(String userId, List<ItemDTO> items) throws SQLException{
		String noBidMsg = "No bid";
		HashMap<String, String> userBids = new HashMap<String, String>();
		 Connection sqlConnect = MySqlDriver.createConnection();
			for(ItemDTO item : items){
				String itemId = item.getItemId();
				String sqlQuery = String.format("Select bid_value from bids where item_id=%s AND user_id=%s "
						+ " AND bid_timestamp =(select max(bid_timestamp) from bids where item_id=%s AND user_id=%s)",itemId,userId,itemId,userId);
				 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
				//boolean hasValue = false;
				String bidValue = noBidMsg;
				while(resultSet.next()){
					//hasValue = true;
					bidValue = resultSet.getString("bid_value");
					if(bidValue == null){
						bidValue = noBidMsg;
					}
				}				
				userBids.put(itemId, bidValue);
			}
			MySqlDriver.closeDb(sqlConnect);
		return userBids;
	}
	private boolean checkIfBidExist(BidsDTO bid) throws SQLException{
		boolean res = false;
		String bidCheckQuery = String.format("SELECT COUNT(*) from bids where item_id=%s AND "
				+ " user_id=%s AND bid_value=%s",bid.getItemId(),bid.getUserId(),bid.getBidValue());
		Connection sqlConnect = MySqlDriver.createConnection();	
		 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,bidCheckQuery);
		 int count = 0;
		 while(resultSet.next()){
			 count = resultSet.getInt(1);
		 }
		 MySqlDriver.closeDb(sqlConnect);
         if(count != 0){
        	 res = true;
         }
         return res;
	}
	public  boolean saveBid2Db(BidsDTO bid) throws SQLException
	{
		boolean status = false;
		    boolean res = checkIfBidExist(bid);
		    if(!res){
			String sqlQuery = "INSERT INTO bids (item_id, user_id, bid_value, bid_timestamp)"
					+ " values (?, ?, ?, ?)";
			Connection sqlConnect = MySqlDriver.createConnection();
			PreparedStatement prepStmt = MySqlDriver.getPreparedStatement( sqlConnect, sqlQuery);
			float bidValue = Float.parseFloat(bid.getBidValue());
			int uid =  Integer.parseInt(bid.getUserId());
			int itemId =  Integer.parseInt(bid.getItemId());
			long timestamp = Long.parseLong(bid.getBidTimestamp());
			prepStmt.setInt(1, itemId);
			prepStmt.setInt(2, uid);
			prepStmt.setFloat(3, bidValue);		
			prepStmt.setLong(4, timestamp);			
			MySqlDriver.updateDatabaseWithPrepStatement(prepStmt);
			
			sqlQuery = String.format("Select is_bid_done, timestamp from items where item_id='%s'", bid.getItemId());
			 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
			String isBidDonePrev = null;
			String uploadTime = null;
			 //System.out.println("Itemid" + bid.getItemId());
			while(resultSet.next()){
				isBidDonePrev = resultSet.getString("is_bid_done");
				uploadTime = resultSet.getString("timestamp");
			}
			if(isBidDonePrev.equals("0")){
				isBidDonePrev = "1";
				String newUploadTime = BidTimeHandler.getUploadTimeIntervalPeriodForFirstTimeBidItem(uploadTime);
				sqlQuery = String.format("UPDATE items set is_bid_done='%s', timestamp='%s' where item_id='%s'",isBidDonePrev,newUploadTime,bid.getItemId());
				MySqlDriver.updateDatabaseWithStatement(sqlConnect,sqlQuery);
			}
			status  = true;
			MySqlDriver.closeDb(sqlConnect);
		    }else{
				status  = true;
		    }
		return status;
		
	}

	public List<String> getItemIdsForWhichUserBids(String userId) throws SQLException {
		List<String> itemIds = new ArrayList<String>();
		Connection sqlConnect = MySqlDriver.createConnection();
		String sqlQuery = String.format("Select item_id from bids where user_id = %s GROUP by item_id", userId);
		 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
		 while(resultSet.next()){	
			 String itemId = String.valueOf(resultSet.getInt("item_id"));
			 itemIds.add(itemId);
		 }
		 MySqlDriver.closeDb(sqlConnect);	 
		return itemIds;
	}	
	
	
}
