package org.sandeep.servlets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sandeep.helpers.SessionHandler;
import org.sandeep.helpers.UserDTO;
import org.sandeep.services.DaoFactory;

import com.google.gson.Gson;
/**
 * Servlet implementation class RegServlet
 */
@WebServlet("/Registration")
public class RegServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;       

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Register the user
		String tag = request.getParameter("tag");
		System.out.println(tag);

	if(tag.equalsIgnoreCase("Register")){
		UserDTO user = new UserDTO();
		user.setFirstName(request.getParameter("firstname"));
		user.setLastName(request.getParameter("lastname"));
		user.setEmail(request.getParameter("email"));
		user.setUserName(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));		
		int status = -1;
		try {
			status = DaoFactory.performRegistration(user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		switch (status){
		case 0:
			System.out.println("Already Registered");
			response.sendRedirect("Login.jsp");
			break;
		case 1:
			response.sendRedirect("Login.jsp");
			break;
		case -1:
			System.out.println("Error - Registration unsuccessful");
        	response.sendRedirect("Reg.jsp");
			break;
			}
		
	}
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// leave auction server
			//	System.out.println("deregister");
				UserDTO user = SessionHandler.getUserDetailsInSession(request);
				String userId = user.getUserId();
				boolean res = false;
				try 
				{
					res = DaoFactory.userLeaveCheck(userId);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			    HashMap<String,String> respMap = new HashMap<String,String>();
				if(!res){
				//	System.out.println("Not able to deregister");
					respMap.put("isAlert", "1");
					respMap.put("url", "");
					//response.sendRedirect("login?regFlag=-1");
				}else{
					//System.out.println("Able to deregister");
					DaoFactory.deRegisterUser(userId);
					// perform logout
					SessionHandler.terminateSession(request);
					respMap.put("isAlert", "0");
					String urlStr = ((HttpServletRequest)request).getRequestURL().toString();
					//url += "/Reg.jsp";
					String url = urlStr.replace("/Registration","/Reg.jsp");
					//respMap.put("url", "http://localhost:8085/RegisterAndLogin/Reg.jsp");
					respMap.put("url", url);
					//response.sendRedirect("Login.jsp");
				}
				String json = new Gson().toJson(respMap);
				response.getWriter().write(json);
	}


}
