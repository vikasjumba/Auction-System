package org.sandeep.servlets;

import java.io.IOException;
//import java.sql.ResultSet;
//import java.sql.SQLException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sandeep.helpers.*;
import org.sandeep.services.DaoFactory;
/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @throws SQLException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	private void loginTheUser(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException{
		//System.out.println("loginTheUser");
		String userName = request.getParameter("username");
	    String password = request.getParameter("password");
        UserDTO userInfo = DaoFactory.validateLogin(userName, password);
        if (userInfo != null)
        {   
        	//SessionHandler.createSession(request, userInfo);
        	List<ItemDTO> itemList =  DaoFactory.getListOfItemsFromDb();
        	HashMap<String, String> userBids = DaoFactory.getRecentBidFromUser(userInfo, itemList);
        	request.setAttribute("items", itemList);  
        	request.setAttribute("userbids", userBids);        	
    		//System.out.println("loginTheUser1");
        	
        	HttpSession session = request.getSession();
        	session.setMaxInactiveInterval(15*60);
        	session.setAttribute("userinfo", userInfo);
        	session.setAttribute("flow", "1");
			//SessionHandler.setParameterInSession(request,"flow","1");
        	request.getRequestDispatcher("homepage.jsp").forward(request, response);
        	//response.sendRedirect("homepage.jsp");
        }else{
        	response.sendRedirect("Login.jsp?loginError=-1");
        }
	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String tag = request.getParameter("tag");
//		System.out.println(tag);
		if(tag.equalsIgnoreCase("login")){
	// perform login
		 try {
			loginTheUser(request,response);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			//System.out.println("loginTheUser2");

		}else{
		// Perform logout
			System.out.println("Perform Logout");
			//SessionHandler.terminateSession(request);
			HttpSession session = request.getSession();
			session.setAttribute("userinfo",null);
	    	session.invalidate();
			response.sendRedirect("Login.jsp");
		}
	    
}
}