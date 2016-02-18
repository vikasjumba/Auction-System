package org.sandeep.helpers;

import java.io.Serializable;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 7719479936252868216L;
	private String _email;
	private String _firstName;
	private String _lastName;
	private String _userName;
	private String _password;
	private String _userId;
	public String getEmail() {
		return _email;
	}
	public void setEmail(String email) {
		this._email = email;
	}
	public String getFirstName() {
		return _firstName;
	}
	public void setFirstName(String firstName) {
		this._firstName = firstName;
	}
	public String getLastName() {
		return _lastName;
	}
	public void setLastName(String lastName) {
		this._lastName = lastName;
	}
	public String getUserName() {
		return _userName;
	}
	public void setUserName(String userName) {
		this._userName = userName;
	}
	public String getPassword() {
		return _password;
	}
	public void setPassword(String password) {
		this._password = password;
	}
	public String getUserId() {
		return _userId;
	}
	public void setUserId(String userId) {
		this._userId = userId;
	}
}
