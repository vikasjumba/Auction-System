package org.sandeep.helpers;

import java.io.InputStream;
import java.io.Serializable;	
public class ItemDTO implements Serializable{
	private static final long serialVersionUID = -7199358275789753059L;
	private String _itemId;
	private String _itemName;
	private String _description;
	private String _basePrice;
	private String _imageUrl;
	private InputStream _imageStream;
	private String _uploadTimestamp;
	private String _isBidDone;
	private String _userId;
	private String _duration;
	private String _sellerName;
	public String getItemId() {
		return _itemId;
	}
	public void setItemId(String itemId) {
		this._itemId = itemId;
	}
	public String getItemName() {
		return _itemName;
	}
	public void setItemName(String itemName) {
		this._itemName = itemName;
	}
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	public String getBasePrice() {
		return _basePrice;
	}
	public void setBasePrice(String basePrice) {
		this._basePrice = basePrice;
	}
	public String getImageUrl() {
		return _imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this._imageUrl = imageUrl;
	}
	public InputStream getImageStream() {
		return _imageStream;
	}
	public void setImageStream(InputStream imageStream) {
		this._imageStream = imageStream;
	}
	public String getUploadTimestamp() {
		return _uploadTimestamp;
	}
	public void setUploadTimestamp(String uploadTimestamp) {
		this._uploadTimestamp = uploadTimestamp;
	}
	public String getIsBidDone() {
		return _isBidDone;
	}
	public void setIsBidDone(String isBidDone) {
		this._isBidDone = isBidDone;
	}
	public String getUserId() {
		return _userId;
	}
	public void setUserId(String userId) {
		this._userId = userId;
	}
	public String getDuration() {
		return _duration;
	}
	public void setDuration(String duration) {
		this._duration = duration;
	}
	public String getSellerName(){
		return _sellerName;
	}
	public void setSellerName(String username){
		this._sellerName = username;
	}
};
