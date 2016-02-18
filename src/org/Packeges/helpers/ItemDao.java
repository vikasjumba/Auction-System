package org.sandeep.helpers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class ItemDao {
	public boolean deleteUserWithId(String userId){
		Connection sqlConnect = MySqlDriver.createConnection();
		String sqlQuery = String.format("DELETE from items where user_id=%s", userId);
		boolean status = MySqlDriver.updateDatabaseWithStatement(sqlConnect,sqlQuery);
		MySqlDriver.closeDb(sqlConnect);
		return status;
	}
	public  String isNewItemAvailableAfter(String timeStampSoFar) throws SQLException{
		int counter = 0;
			 String sqlQuery = String.format("Select COUNT(*) from items where timestamp > %s", timeStampSoFar);
			 Connection sqlConnect = MySqlDriver.createConnection();
			 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,sqlQuery);
			 
			 while(resultSet.next()){
				 counter = resultSet.getInt(1);
			 }
			 MySqlDriver.closeDb(sqlConnect);
		return String.valueOf(counter);
	}
	public boolean isUserWinningAnyItem(String userId) throws SQLException{
		boolean res = false;
		String itemQuery = String.format("Select timestamp, is_bid_done "
				+ "( SELECT max(bid_value) from bids where user_id=%s)", userId);
		 Connection sqlConnect = MySqlDriver.createConnection();
		 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,itemQuery);
		 while(resultSet.next()){
			 String timestamp = String.valueOf(resultSet.getInt("timestamp"));
			 String isBidDone = resultSet.getString("is_bid_done");
			 long duration = BidTimeHandler.getRemainingBidDuration(isBidDone, timestamp);
			 if(duration > 0){
			 res = true;
			 break;
			 }
		 }
		 MySqlDriver.closeDb(sqlConnect);
   return res;
	}
    public boolean  isUnsoldItemsFromSeller(String userId) throws SQLException{
    	boolean res = false;
    	String itemQuery = String.format("Select timestamp, is_bid_done from items where user_id=%s", userId);
		 Connection sqlConnect = MySqlDriver.createConnection();
		 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,itemQuery);
		 while(resultSet.next()){
			 String timestamp = String.valueOf(resultSet.getInt("timestamp"));
			 String isBidDone = resultSet.getString("is_bid_done");
			 long duration = BidTimeHandler.getRemainingBidDuration(isBidDone, timestamp);
			 if(duration > 0){
			 res = true;
			 break;
			 }
		 }
		 MySqlDriver.closeDb(sqlConnect);
    return res;
    }
	public  ItemDTO getItemWithId(String itemId) throws SQLException{
		ItemDTO item = new ItemDTO();		
			 String itemQuery = String.format("Select * from items where item_id=%s", itemId);
			 Connection sqlConnect = MySqlDriver.createConnection();
			 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,itemQuery);
			while(resultSet.next()){
				
				item = itemFromSqlResult(sqlConnect,resultSet);

			 /*System.out.println(item.item_id); 
			 System.out.println(item.uploadTimestamp); 
			 System.out.println(item.isBidDone); 
			 System.out.println(item.duration); */
		}
			 MySqlDriver.closeDb(sqlConnect);
		return item;
		
	}
	public  byte[] getImageBytes(String itemid, String itemname) throws SQLException
	{
		byte[] imageBytes = null;
			 String imgQuery = String.format("Select image from items where item_id='%s' and item_name='%s'", itemid,itemname);
			 Connection sqlConnect = MySqlDriver.createConnection();
			 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,imgQuery);
			 while(resultSet.next()){
				 imageBytes = resultSet.getBytes(1);
				// System.out.println(imageBytes);
				}
			 MySqlDriver.closeDb(sqlConnect);
		return imageBytes;
	}
	
	private boolean checkIfItemExist(ItemDTO item) throws SQLException{
		boolean res = false;
		String itmCheckQuery = String.format("SELECT COUNT(*) from items where  "
				+ " item_name='%s' "
				+ " AND description='%s' AND base_price=%s "
				+ " AND user_id=%s",item.getItemName(),item.getDescription(),item.getBasePrice(),item.getUserId());
		Connection sqlConnect = MySqlDriver.createConnection();	
		 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,itmCheckQuery);
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
	public  boolean saveItem2Db(ItemDTO item) throws SQLException
	{
		    boolean status = false;
		    boolean res = checkIfItemExist(item);
		    if(!res){
			String sqlQuery = "INSERT INTO items (item_name, description, base_price, image, timestamp, is_bid_done, user_id)"
					+ " values (?, ?, ?, ?, ?, ?, ?)";
			Connection sqlConnect = MySqlDriver.createConnection();
			PreparedStatement prepStmt = MySqlDriver.getPreparedStatement( sqlConnect, sqlQuery);
			int itemId = -1;
			float basePrice = Float.parseFloat(item.getBasePrice());
			//myStmt.setInt(1, itemId);
			prepStmt.setString(1, item.getItemName());
			prepStmt.setString(2, item.getDescription());
			prepStmt.setFloat(3, basePrice);
			if(item.getImageStream() != null){
				prepStmt.setBlob(4, item.getImageStream());
			}
			prepStmt.setDouble(5, Long.parseLong(item.getUploadTimestamp()));
			prepStmt.setString(6, item.getIsBidDone());
			prepStmt.setInt(7, Integer.parseInt(item.getUserId()));

			MySqlDriver.updateDatabaseWithPrepStatement(prepStmt);
		    String itemIdQuery = "Select @@IDENTITY";
			ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,itemIdQuery);
			if(resultSet != null){
				resultSet.next();
				itemId = resultSet.getInt(1);
			}
			
			String imgUrl = item.getImageUrl() + String.format("imagereq=1&itemname=%s&itemid=%d",item.getItemName(),itemId);
			item.setImageUrl(imgUrl) ;
			sqlQuery = String.format("UPDATE items SET image_url ='%s' WHERE item_id=%d", item.getImageUrl(),itemId);
			MySqlDriver.updateDatabaseWithStatement(sqlConnect,sqlQuery);
			status  = true;
			 MySqlDriver.closeDb(sqlConnect);
		    }
		    else{
		    	status  = true;
		    }
		return status;
	}
  private ItemDTO itemFromSqlResult(Connection sqlConnect, ResultSet resultSet) throws SQLException{
		 ItemDTO item = new ItemDTO();
		 item.setBasePrice(String.valueOf(resultSet.getDouble("base_price")));
		 item.setDescription(resultSet.getString("description"));
		 item.setItemId(String.valueOf(resultSet.getInt("item_id")));
		 item.setItemName(resultSet.getString("item_name"));
		 item.setImageUrl(resultSet.getString("image_url"));
		 item.setIsBidDone(resultSet.getString("is_bid_done"));
		 item.setUploadTimestamp(String.valueOf(resultSet.getInt("timestamp")));
		 long duration = BidTimeHandler.getRemainingBidDuration(item.getIsBidDone(), item.getUploadTimestamp());
		 item.setDuration(String.valueOf(duration));
		 String sellerId = String.valueOf(resultSet.getInt("user_id"));
		 item.setUserId(sellerId);
		 String sellerSqlQuery = String.format("SELECT username from userinfo where user_id=%s",sellerId);
		 ResultSet rs = MySqlDriver.runSqlQuery(sqlConnect, sellerSqlQuery);
		 while(rs.next()){
			 String sellerName = rs.getString("username");
			 item.setSellerName(sellerName);
		 }
		 return item;
  }
  public  List<ItemDTO> getAllItemsFromDb() throws SQLException{
		List<ItemDTO> itemList = new ArrayList<ItemDTO>();	
		String itemQuery = "Select * from items order by timestamp desc";
		Connection sqlConnect = MySqlDriver.createConnection();
		 ResultSet resultSet = MySqlDriver.runSqlQuery(sqlConnect,itemQuery);
			 while(resultSet.next()){
				 //System.out.println("In side item");
				 ItemDTO item = itemFromSqlResult(sqlConnect, resultSet);
				 itemList.add(item);
			 }
			 MySqlDriver.closeDb(sqlConnect);
		return itemList;
	}
public List<ItemDTO> getUnsoldItemsWithIds(List<String> itemIds) throws SQLException {
	List<ItemDTO> itemList = new ArrayList<ItemDTO>();	
	for(String itemId : itemIds){
		ItemDTO item = this.getItemWithId(itemId);
		String timestamp = item.getUploadTimestamp();
		 String isBidDone = item.getIsBidDone();
		 //System.out.println("timestamp " + timestamp + " isBidDone " + isBidDone);
		 long duration = 0;
		 if(isBidDone != null){
		 duration = BidTimeHandler.getRemainingBidDuration(isBidDone, timestamp);
		 }
		 //System.out.println("Item id " + itemId);
		 //System.out.println("duration " + String.valueOf(duration));
		 if(duration > 0)
		 {
			 itemList.add(item);
		 }
	}
	return itemList;
}
}
