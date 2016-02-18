package org.sandeep.helpers;

import java.io.Serializable;

public class BidsDTO implements Serializable{
	private static final long serialVersionUID = 5978111992792674956L;
	private String _userId;
	private String _itemId;
	private String _bidValue;
	private String _bidTimeStamp;

public BidsDTO(){}
public BidsDTO(String userId,String itemId, String bidValue,String bidTimeStamp) {
	this._userId = userId;
	this._itemId = itemId;
	this._bidValue = bidValue;
	this._bidTimeStamp = bidTimeStamp;
}
public String getUserId(){
	return _userId;
}
public void setUserId(String userId){
	this._userId = userId;
}

public String getItemId(){
	return _itemId;
}
public void setItemId(String itemId){
	this._itemId = itemId;
}
public String getBidValue(){
	return _bidValue;
}
public void setBidValue(String bidValue){
	this._bidValue = bidValue;
}
public String getBidTimestamp(){
	return _bidTimeStamp;
}
public void setBidTimestamp(String bidTimeStamp){
	this._bidTimeStamp = bidTimeStamp;
}

}
