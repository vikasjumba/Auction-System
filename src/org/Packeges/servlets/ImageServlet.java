package org.sandeep.servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sandeep.helpers.SessionHandler;
import org.sandeep.services.DaoFactory;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet("/itemsimage")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Do get Image");

		if(!SessionHandler.checkSessionValidity(request)){
			System.out.println("Session expired");
			response.sendRedirect("Login.jsp");
			}
		else{
		String itemId = request.getParameter("itemid");
		String itemName = request.getParameter("itemname");
		byte[] imageBytes = null;
		try {
			imageBytes = DaoFactory.getImage(itemId,itemName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.setContentType("image/jpeg");
		response.setContentLength(imageBytes.length);
		response.getOutputStream().write(imageBytes);
		//try{
		request.getRequestDispatcher("homepage.jsp").forward(request, response);
		//}catch(Exception e)
		//{
		//	System.out.println("Image Exception");
		//}
		}
		//response.sendRedirect("homepage.jsp");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println("Hello");
//		if(request.getParameter("imagereq")!= null)
//		{
//		System.out.println("Hello");
//		String itemid = request.getParameter("itemid");
//		String itemname = request.getParameter("itemname");
//		byte[] imageBytes =  Service.getImageBytes(itemid,itemname);
//		response.setContentType("image/jpeg");
//		response.setContentLength(imageBytes.length);
//		response.getOutputStream().write(imageBytes);
//		request.getRequestDispatcher("homepage.jsp").forward(request, response);
//	}
		

}
	
}
