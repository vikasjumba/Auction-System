package org.sandeep.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.sandeep.helpers.*;
import org.sandeep.services.DaoFactory;

import com.google.gson.Gson;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/HomeServlet")
@MultipartConfig
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private int saveItemAndRefreshPage(HttpServletRequest request) throws IOException, SQLException{
    	int status = -1;
    	/*
    	 * status -1 failure item add
    	 * status -2 failure bid upload
    	 * 1	success item add
    	 * 2 	success bid add
    	 **/
    	
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory()); 
		List<FileItem> items;
		ItemDTO itemDTO = new ItemDTO();
		String bidValue = null;
		String uid = null;
		String itemId = null;
		try {
			items = upload.parseRequest(new ServletRequestContext(request));
			 for (FileItem item : items) {
			        if (item.isFormField()) {
			                 //get form fields here 
			        	String fieldname = item.getFieldName();
		                String fieldvalue = item.getString();
//		                System.out.println(fieldname);
//		                System.out.println(fieldvalue);
		                if(fieldname.equalsIgnoreCase("bidvalue")){
		                	status = -2;
		                	bidValue = fieldvalue;
		                }
		                else if (fieldname.equalsIgnoreCase("bidattributes")){
		                	String[] attribs = fieldvalue.split("_");
		                	//System.out.println("attribs");
		                	//System.out.println(attribs);
		                	uid = attribs[1];
		                	itemId = attribs[3].split("/")[0];
		                }
		                else{
		                	if(fieldname.equalsIgnoreCase("itemname")){
		                		itemDTO.setItemName(fieldvalue);			            		
		                	}else if(fieldname.equalsIgnoreCase("itemdescription"))
		                	{
		                		itemDTO.setDescription(fieldvalue);
		                	}else if(fieldname.equalsIgnoreCase("baseprice"))
		                	{
		                		status = -1;
		                		itemDTO.setBasePrice(fieldvalue);
		                	}
		                }
		                }
			         else {
			                  //process file upload here
//			        	 System.out.println(item.getName());
//			             System.out.println(item.getSize());
//			             System.out.println(item.getContentType());
			        	 status = -1;
			             InputStream imageStream = item.getInputStream();
			             itemDTO.setImageStream(imageStream);
			          }
			        }
			 if(bidValue == null){
			 String url = ((HttpServletRequest)request).getRequestURL().toString();
				itemDTO.setImageUrl(url + "_image?");
				itemDTO.setIsBidDone("0"); // already a default value in mysql;
				UserDTO user = (UserDTO) SessionHandler.getUserDetailsInSession(request);
				itemDTO.setUserId(user.getUserId());
				long nowSeconds = System.currentTimeMillis() / 1000l;
				System.out.println(nowSeconds);
				itemDTO.setUploadTimestamp(String.valueOf(nowSeconds));
				status = DaoFactory.insertItem(itemDTO);
			 }else{
				 // save bid in db
				 BidsDTO bid = new BidsDTO();
				 bid.setUserId(uid);
				 bid.setItemId(itemId);
				// System.out.println("Itemid" + bid.itemId);
				 bid.setBidValue(bidValue);
				 bid.setBidTimestamp(String.valueOf(System.currentTimeMillis() / 1000l));
				 status = DaoFactory.insertBid(bid);
			 }
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		return status;
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url = "";
		 response.setContentType("text/plain");  
		    response.setCharacterEncoding("UTF-8"); 
		    HashMap<String,String> timeOverResponse = new HashMap<String,String>();
		    HttpSession session = request.getSession(false); 
		if(session == null){
			System.out.println("Session expired doGet");
			//request.getRequestDispatcher("/Login.jsp").forward(request, response);
			String reqUrl = ((HttpServletRequest)request).getRequestURL().toString();
			 url = reqUrl.replaceAll("/items", "/Login.jsp");
			//url += "/Login.jsp";
			timeOverResponse.put("url", url);
			String json = new Gson().toJson(timeOverResponse);
	    	response.getWriter().write(String.valueOf(json));
			//response.sendRedirect("Login.jsp");
			}
		else{
		String reqTag = request.getParameter("reqtag");
		//System.out.println("doGet");
		//System.out.println(itemId);	
		
	    if(reqTag.equals("1")){
	    	ItemDTO item = null;
			try {
				String itemId = request.getParameter("itemid");
				item = DaoFactory.getItemFromDBWithItemId(itemId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	   
	    	timeOverResponse.put("duration", item.getDuration());
	    	UserDTO userInfo = (UserDTO) SessionHandler.getUserDetailsInSession(request);
	    	String msg = null;
			try {
				String itemId = request.getParameter("itemid");
				msg = DaoFactory.getBidWinMessageForUser(userInfo.getUserId(), itemId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	timeOverResponse.put("message", msg);
	    	timeOverResponse.put("url", url);
	    	String json = new Gson().toJson(timeOverResponse);
	    	response.getWriter().write(String.valueOf(json));
	    }else if(reqTag.equals("2")){
	    	String timeStampSoFar = request.getParameter("latestItem");
			String itemId = request.getParameter("itemid");
	    	//System.out.println("time stamp " + timeStampSoFar);
	    	String newItemCount = null;
	    	String highestBid = null;
			try {
				newItemCount = DaoFactory.checkNewItemsAfter(timeStampSoFar);
				 highestBid = DaoFactory.fetchHighestBid(itemId);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	timeOverResponse.put("highestbid",highestBid);
	    	timeOverResponse.put("newItemCount",newItemCount);
	    	timeOverResponse.put("url", url);
	    	String json = new Gson().toJson(timeOverResponse);
	    	response.getWriter().write(String.valueOf(json));
	    }
	    }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("Do post home");
		System.out.println("Inside do post");
		 HttpSession session = request.getSession(false); 
			if(session == null){
			System.out.println("Session expired");
			//request.getRequestDispatcher("/Login.jsp").forward(request, response);
			//response.sendRedirect("Login.jsp");
			}
		else{
//			String lastReqCount = (String) session.getAttribute("lastReqCounter");
//	    	String currReqCount = (String) session.getAttribute("currReqCounter");
//	    	System.out.println("Old values -> lastReqCount " + lastReqCount + " currReqCount " + currReqCount);
			int status = -1;
			try {
				status = saveItemAndRefreshPage(request);
				// update request counters in session
//		    	if(lastReqCount == null){lastReqCount = "0";}
//		    	else{
//		    		int temp = Integer.parseInt(lastReqCount) + 1;
//		    		lastReqCount = String.valueOf(temp);
//		    	}
//		    	if(currReqCount == null){currReqCount = "1";}
//		    	else{
//		    		int temp = Integer.parseInt(currReqCount) + 1;
//		    		currReqCount = String.valueOf(temp);
//		    	}
//		    	session.setAttribute("lastReqCounter",lastReqCount);
//		    	session.setAttribute("currReqCounter",currReqCount);
//		    	System.out.println("New values -> lastReqCount " + lastReqCount + " currReqCount " + currReqCount);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			switch (status){
			case -1:
				System.out.println("Item failure add");
				break;
			case -2:
				System.out.println("Bid failure");
				break;
			case 1:				
			case 2:
				System.out.println("Do post home");
				
				UserDTO userInfo = SessionHandler.getUserDetailsInSession(request);
				HashMap<String, String> userBids = null;
				List<ItemDTO> itemList = null;
				try {
					itemList =  DaoFactory.getListOfItemsFromDb();
					userBids = DaoFactory.getRecentBidFromUser(userInfo, itemList);
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        	request.setAttribute("items", itemList);  
	        	request.setAttribute("userbids", userBids);  
	        	//response.sendRedirect("homepage.jsp");
	        	request.getRequestDispatcher("homepage.jsp").forward(request, response);
				break;
			}
			
		}	
		}		
}
