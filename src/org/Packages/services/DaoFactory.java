package org.sandeep.services;
//import java.sql.Connection;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sandeep.helpers.*;
//import com.mysql.jdbc.PreparedStatement;
	public class DaoFactory {
	//Registration
	public static int performRegistration(UserDTO user) throws SQLException{
		UserDao userDao = new UserDao();
		return userDao.registerUserDetails(user);
	}
	//Login
	public static UserDTO validateLogin(String userName,String password) throws SQLException{
		UserDao userDao = new UserDao();
		UserDTO user = userDao.authenticateLogin(userName, password);
		return user;
	}
	//Insert Item
	public static int insertItem(ItemDTO item) throws SQLException{
		int status = -1;
		ItemDao itemDao = new ItemDao();
		if(!itemDao.saveItem2Db(item)){
        	System.out.println("Error: Item save");
        }else{
		status = 1;
        }
		return status;
	}
	
	//Get list of all items from DB
	public static List<ItemDTO> getListOfItemsFromDb() throws SQLException{
		ItemDao itemDao = new ItemDao();
		List<ItemDTO> items = itemDao.getAllItemsFromDb();
		return items;
	}
	//Get recent bid from user
	public static HashMap<String, String> getRecentBidFromUser(UserDTO user, List<ItemDTO> itemList) throws SQLException{
		
		BidsDao bidsDao = new BidsDao();
		HashMap<String, String>  bids = bidsDao.getLatestBidsForUser(user.getUserId(), itemList);
		return bids;
	}
	//Get image
	public static byte[] getImage(String itemId, String itemName) throws SQLException{
		ItemDao itemDao = new ItemDao();
		return itemDao.getImageBytes(itemId,itemName);
	}
	//Insert bid
	public static int insertBid(BidsDTO bid) throws SQLException{
		int status = -2;
		BidsDao BidsDao = new BidsDao();
		if(!BidsDao.saveBid2Db(bid)){
            System.out.println("Error: Item save");	            	
            }else{
            status = 2;
            }
		return status;
	}
	//Get items from DB
	public static ItemDTO getItemFromDBWithItemId(String itemId) throws SQLException{
		ItemDao itemDao = new ItemDao();
		return itemDao.getItemWithId(itemId);	
	}
	public static String checkNewItemsAfter(String timeStampSoFar) throws SQLException{
		ItemDao itemDao = new ItemDao();
		return itemDao.isNewItemAvailableAfter(timeStampSoFar);
	}
	//Whether user is winning any item
	private static boolean isUserWinningAnyItem(String userId) throws SQLException{
				boolean res = false;
				ItemDao itemDao = new ItemDao();
				UserDao userDao = new UserDao();
				BidsDao bidsDao = new BidsDao();
				List<String> itemIds = bidsDao.getItemIdsForWhichUserBids(userId);
				if(!itemIds.isEmpty()){
				List<ItemDTO> items = itemDao.getUnsoldItemsWithIds(itemIds);	
				for(ItemDTO item : items){
					UserDTO user = userDao.getBidWinnerForItem(item.getItemId());
					String winnerId = user.getUserId();
					if(userId.equalsIgnoreCase(winnerId)){
						res = true;
						break;
					}
				}
				}
				return res;
			}
	//Whether user can leave AS
	public static boolean userLeaveCheck(String userId) throws SQLException{
				boolean res = true;
				ItemDao itemDao = new ItemDao();
				// user should not be selling an item right now
				res = itemDao.isUnsoldItemsFromSeller(userId);
				if(!res){
				// user should not be winner in any item
				res = isUserWinningAnyItem(userId);
				}
				return !res;
			}
	public static String fetchHighestBid(String itemId) throws SQLException{
		BidsDao bidsDao = new BidsDao();
		return bidsDao.getHighestBidSoFar(itemId);
	}
	public static String getBidWinMessageForUser(String userId, String itemId) throws SQLException{
		String msg = "";
		String sellerId = "";
		String winnerId = "";
		ItemDao itemDao = new ItemDao();
		ItemDTO item = itemDao.getItemWithId(itemId);
		sellerId = item.getUserId();
		UserDao userDao = new UserDao();
		UserDTO user = userDao.getBidWinnerForItem(itemId);
		String winnerName = user.getFirstName();
		if(winnerName ==  null){
			winnerName = "";
		}else{
		 winnerName = winnerName + " " + user.getLastName();
		}
		 winnerId = user.getUserId();
		// System.out.println("msg for " + userId + ": winner is " + winnerId);
			 if(userId.equals(sellerId)){
				 if(winnerName == ""){
					 msg = "Buyer left the auction System";
				 }else{
				 msg = "Sold to " + winnerName;
				 }
			 }
			 else if(userId.equals(winnerId)){
				 msg = "Congratulaton! You win the item";
			 }
			 else{
				 msg = "Sold Out";
			 }
			 return msg;
	}
	//De-register User
	public static boolean deRegisterUser(String userId){
		UserDao userDao = new UserDao();
		ItemDao itemDao = new ItemDao();
		//BidsDao bidsDao = new BidsDao();
		itemDao.deleteUserWithId(userId);
		return userDao.deleteUserWithId(userId);
	}

}
