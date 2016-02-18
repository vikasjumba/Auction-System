package org.sandeep.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHandler {
	
	public static HttpSession createSession(HttpServletRequest request, UserDTO userInfo){
		HttpSession session = request.getSession();
    	session.setMaxInactiveInterval(15*60);
    	session.setAttribute("userinfo", userInfo);
    	return session;
	}
	public static boolean checkSessionValidity(HttpServletRequest request){
		boolean status = true;
		HttpSession session = request.getSession(false);
		//System.out.println(session);
		if(session == null){
			status = false;
		}
		return status;
	}
	public static void terminateSession(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("userinfo",null);
    	session.invalidate();
	}
	public static UserDTO getUserDetailsInSession(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		UserDTO userInfo = null;
		userInfo = (UserDTO) session.getAttribute("userinfo");
		//System.out.println(userInfo.getUserName());
    	return userInfo;
	}
	public static void setParameterInSession(HttpServletRequest request, String key, String value){
		HttpSession session = request.getSession(false);
		if(session != null){
			session.setAttribute(key,value);
		}
	}
	public static String getParameterInSession(HttpServletRequest request, String key){
		String value = null;
		HttpSession session = request.getSession(false);
		if(session != null){
			session.getAttribute(key);
		}
		return value;
	}
}
