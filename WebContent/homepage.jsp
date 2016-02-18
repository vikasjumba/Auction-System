<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.sandeep.helpers.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to Auction</title>
</head>

<%
//HttpSession httpSession = request.getSession(false);
//String flow = (String)session.getAttribute("flow");
//if(flow == null || flow.equals("-1")){ <jsp:forward page = "Login.jsp" /> System.out.println("Not valid Session");%>
<%//} %>
 <%
 System.out.println("Home JSP loaded");
 HttpSession httpSession = request.getSession(false);
	UserDTO userInfo = (UserDTO) httpSession.getAttribute("userinfo");
	List<ItemDTO> itemList = (List<ItemDTO>) request.getAttribute("items");
	HashMap<String,String> userbids = (HashMap<String,String>) request.getAttribute("userbids");
	String timerId = "";
	String bidTextId = "";
	String bidBtnId = "";
	String bidMsgId = "";
	String bidHighestId = "";
	long duration = 0;	
	long latestItemsTimestamp = 0;
	String newItemsLinkId = "newItems"; 	
	String userId = userInfo.getUserId();	
//	String regFlag = request.getParameter("regFlag");
%>
<h2> <font color="White"><marquee direction="left" style="background:SaddleBrown">Hey <%=userInfo.getFirstName() %>, welcome to Auction. </marquee></font> </h2>
<body style="background-color:CornSilk">
<a id = 'noback'></a>
<a id = 'noback again'></a>
<script>
window.location.hash="";
window.location.hash="";//again because google chrome don't insert first hash into history
window.onhashchange=function(){window.location.hash="";}
</script> 
<form align ="right" width="30%" method="post" action="login">
   <input type="submit" name="tag" value="Logout" />  
 </form>
 <br>
 <form align ="right" width="30%" method="post" action="registration">
   <button type="button"  name="tag" onclick="javascript:leaveRequestToServer();">Leave Auction App</button> 
   <input type="hidden" name="userid"  value=<%=userId %>>
 </form>
 
 <form method="post" action="items" enctype="multipart/form-data">

  <center>
            <table border="2" width="30%" cellpadding="5">
                <thead>
                    <tr>
                        <th colspan="2">Submit New Item for Auction</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td> <span style="color:SaddleBrown;font-size:105%; white-space:nowrap"><strong>Item Name</strong></span></td>
                        <td><input  type="text" name="itemname" value="" required/></td>
                    </tr>
                    <tr>
                        <td> <span style="color:SaddleBrown;font-size:105%; white-space:nowrap"><strong>Description</strong></span></td>
                        <td><input type="text" name="itemdescription" value="" required/></td>
                    </tr>
                    <tr>
                        <td> <span style="color:SaddleBrown;font-size:105%; white-space:nowrap"><strong>Base Price</strong></span></td>
                        <td><input type="number" step="any" name="baseprice" value="" min="0" required/></td>
                    </tr>
                    <tr>
                     <td><input type="file" name="photo" required></td>
 					<td><input type="submit" name="tag" value="upload" ></td>
                    </tr>
                </tbody>
            </table>
            </center>

 </form>
 <br>
 <br>
<script>


function leaveRequestToServer(){
	var xmlhttp;
	if (window.XMLHttpRequest)
    {
        // Supported in IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    }
    else {
        // making it IE6, IE5 compatible
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function()
    {
      if (xmlhttp.readyState == XMLHttpRequest.DONE)
      {
       if(xmlhttp.status == 200)
       {
    	   var msgJSON = xmlhttp.responseText;
           //console.log("msgJSON");
           //console.log(msgJSON);
           var jsonObj = JSON.parse(msgJSON);
           //console.log("jsonObj");
          // console.log(jsonObj);
          var isAlert = Number(jsonObj.isAlert);
          if(isAlert == 1)
          {
          	alert('You cannot leave Auction app while you are selling or winning an item.');	
          }
          else{
        	  var regUrl = jsonObj.url;
        	  //console.log(regUrl);
        	  window.open(regUrl,"_self");
          }
       }else{
    	   alert('Error in processing leave request. Please try again.');
       }
      }
     }
    xmlhttp.open("GET", "Registration", true);
    xmlhttp.send();
}

function frequentBidQuery(itemId,maxTimestamp,newItmLnkId,bdHighId,bdTxtId,itmBasePrice,userHighBid,validTxt){
	//console.log("itemId " + itemId);
	var highBidInterval = setInterval(
			 function(){
				 highestBidSoFarAndNewItems(itemId,maxTimestamp, newItmLnkId, bdHighId,bdTxtId,itmBasePrice,userHighBid,validTxt);
				 },1000);
}
function highestBidSoFarAndNewItems(itemId,latestItem,newItemsLinkId,highestBidId,bdTxtId,itmBasePrice,userHighBid,validTxt){
	var xmlhttp;
	if (window.XMLHttpRequest)
    {
        // Supported in IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    }
    else {
        // making it IE6, IE5 compatible
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function()
    {
      if (xmlhttp.readyState == XMLHttpRequest.DONE )
      {
       if(xmlhttp.status == 200)
       {	
                var msgJSON = xmlhttp.responseText;
                var jsonObj = JSON.parse(msgJSON);
                var highestBid = jsonObj.highestbid;
                var newItemCount = jsonObj.newItemCount;
                var bidSoFar = "";
                var regUrl = jsonObj.url;
                if(regUrl != ""){
                	//  window.open(regUrl);
                }
                if(highestBid != -1){
          //      	
                	bidSoFar = highestBid;
                }
               
                var newItemBtn = document.getElementById(newItemsLinkId);
                if(newItemCount != "0"){
                	            	 
                	 var singPluStr = " New Items";
                	 if(newItemCount == "1"){
                		 singPluStr = " New Item";
                	 }
                	
                 	newItemBtn.value = "Click for " + newItemCount + singPluStr;
               	 	newItemBtn.style.display = "block";
                }else{
                	
                	newItemBtn.style.display = "none";
                }
                document.getElementById(highestBidId).innerHTML = bidSoFar;

                var newMinVal = 0;
                var bdTxt = document.getElementById(bdTxtId);
                if(bdTxt != null){
                	var highBid = 0;
                	if(bidSoFar != ""){
                		highBid = Number(bidSoFar);
                	}
                	if(userHighBid != "No bid"){
                		newMinVal = Math.max(highBid,Number(userHighBid));
                	}
                	newMinVal = Math.max(newMinVal,Number(itmBasePrice));
                	bdTxt.min = newMinVal;
                	var valdTxt = document.getElementById(validTxt);
                	var vldStrMsg = "";
                	if(bdTxt.disabled == false) {
                		vldStrMsg = "Enter you bid higher than " + newMinVal;
                	}
                	if(valdTxt != null){
                		valdTxt.innerHTML = vldStrMsg;
               		}
                	//console.log("highBid " + highBid);
                	//console.log("newMin " + newMinVal);
                }
        }
        else 
        {
              
        }
      }
    };
	    xmlhttp.open("GET", "items?reqtag=2&latestItem=" + latestItem + "&itemid=" + itemId, true);
	    xmlhttp.send();
};
function bidInfoAjax(timeId,itemId,bdTxtId,bdBtnId,bdMsgId)
{
	
	 var xmlhttp;

    if (window.XMLHttpRequest)
    {
       
        xmlhttp = new XMLHttpRequest();
    }
    else {
       
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function()
    {
      if (xmlhttp.readyState == XMLHttpRequest.DONE )
      {
       if(xmlhttp.status == 200)
       {	
                var msgJSON = xmlhttp.responseText;
                
                var jsonObj = JSON.parse(msgJSON);
              
               var newDuration = Number(jsonObj.duration);
               var regUrl = jsonObj.url;
               console.log("url " + regUrl);
               if(regUrl != ""){
            	   
               	
               }
               if(newDuration == 0)
               {
            	   // disable bid button and show msgs 
            	   var bidMsg = jsonObj.message;
            	   var bdBtn = document.getElementById(bdBtnId);
            	   var bdTxt = document.getElementById(bdTxtId);
            	   var bdMsg = document.getElementById(bdMsgId);
            	   if(bdBtn != null){
            		   bdBtn.disabled = true;  
            	   }
            	   if(bdTxt != null){
            		   bdTxt.disabled = true;
            	   } 
            	   if(bdMsg != null){
            		   bdMsg.innerHTML = bidMsg;
               }
               }
               else
               {
            	   //endtime = newDuration;
            	   //console.log(newDuration);
            	   initializeClock(timeId, newDuration,itemId,bdTxtId,bdBtnId,bdMsgId);

               }
        }
        else 
        {
              // alert('An error occured while processing bid timing.');
        }
      }
    };
	    xmlhttp.open("GET", "items?reqtag=1&itemid=" + itemId, true);
	    xmlhttp.send();
};

function initializeClock(timeId, endtime, itemId,bdTxtId,bdBtnId,bdMsgId){	
	  var clock = document.getElementById(timeId);
	  var timeInterval = setInterval(function(){
		  var min = Math.floor(endtime/60);
		  var sec = endtime%60;
		  var minStr = min.toString();	
		  var secStr = sec.toString();
		  if(min < 10){
			  minStr = "0" + minStr;  
		  }
		  if(sec < 10){
			  secStr = "0" + secStr;  
		  }
	    clock.innerHTML = minStr + ":" + secStr;
		//console.log("Hello timer");
	    if(endtime > 0)
		  {
	     	--endtime;
	      }
	    else{
	    	// duration is 0 now check from servlet if bidding is allowed or over  
	    	//clock.innerHTML = "hello";
	    	clearInterval(timeInterval);
	        bidInfoAjax(timeId,itemId,bdTxtId,bdBtnId,bdMsgId);
	    }
	    },1000);
	};
	function validateBid(formName){
		console.log(formName);
		if( document.myForm.bidvalue.value == "" )
        {
           alert( "Please provide a bid!" );
           return false;
        }
	}
	
 </script>
<input type="button" id = <%=newItemsLinkId%> value="" onClick="document.location.reload(true)"> 
<%
for(int j=0;j<itemList.size();j++)
{
	ItemDTO itemDetails = itemList.get(j);
	 long uploadTime = Long.parseLong(itemDetails.getUploadTimestamp());
	if(latestItemsTimestamp < uploadTime){
		 latestItemsTimestamp = uploadTime;
	 }	
}
%>
	
<Table align="center" width="80%" BORDER="3" CELLSPACING="1" CELLPADDING="1">
<%
 for(int i=0;i<itemList.size();i++)
 {
 ItemDTO itemDetails = itemList.get(i);
 timerId =    	"timer_" + i;
 bidTextId =  	"bidtext_" + i;
 bidBtnId =   	"bidbutton_" + i;
 bidMsgId =   	"bidwinmsg_" + i; 
 bidHighestId = "bidHighest_" + i;
 String validStr = "validStr_" + i;
 duration = Long.parseLong(itemDetails.getDuration());
 //long uploadTime = Long.parseLong(itemDetails.uploadTimestamp);

 String bidAttributes = "uid_" + userInfo.getUserId() + "_itemid_" + itemDetails.getItemId();
 %>
 <tr>
 <td align="center"><span style="color:SaddleBrown;font-size:110%"><strong>Seller</strong></span><br><br><%=itemDetails.getSellerName()%></td>
 <td align="center"><span style="color:SaddleBrown;font-size:110%; white-space:nowrap"><strong>Item name</strong></span><br><br><%=itemDetails.getItemName()%></td>
 <td align="center"><span style="color:SaddleBrown;font-size:110%; white-space:nowrap"><strong>Description</strong></span><br><br><%=itemDetails.getDescription()%></td>
 <td align="center"><span style="color:SaddleBrown;font-size:110%; white-space:nowrap"><strong>Baseprice($)</strong></span><br><br><%=itemDetails.getBasePrice()%></td>
 <td align="center"><img src=<%=itemDetails.getImageUrl()%>></td>
 <td align="center"><span style="color:SaddleBrown;font-size:110%; white-space:nowrap"><strong>Time Left to Bid</strong></span><br><br><div id=<%=timerId%> ></div></td>
 <td align="center"><span style="color:SaddleBrown;font-size:110%; white-space:nowrap"><strong>Your Bid($)</strong></span><br><br> <%=userbids.get(itemDetails.getItemId())%> </td>
 <td align="center"><span style="color:SaddleBrown;font-size:110%; white-space:nowrap"><strong>Highest Bid($)</strong></span><br><br><div  id=<%=bidHighestId  %>></div></td>
 <%
 String sellerId = itemDetails.getUserId();
 if(!sellerId.equals(userInfo.getUserId()))
 {
 %>
 <td>  
 <form name="bidForm" method="post" action="items" enctype="multipart/form-data">
<input width="25" type="number" step="any" name="bidvalue" id=<%=bidTextId%> value="" min="<%=itemDetails.getBasePrice()%>" required>
<input type="hidden" name="bidattributes"  value=<%=bidAttributes%> >
<input type="submit" name="bid" id=<%=bidBtnId%> value="Bid">
<div id=<%=validStr%> ></div>
 </form>
 </td>
 <%}else{
%>
<td>  </td>  
 <%}
 %>
  <td align="center"><span style="color:SaddleBrown;font-size:110%; white-space:nowrap"><strong>Bid Result</strong></span><br><br><div id=<%=bidMsgId%> ></div></td>
 </tr>
<script language="javascript" type="text/javascript">
 var idd = "<%=timerId%>";
 var dur = <%=duration%>;
 var itemId = "<%=itemDetails.getItemId()%>";
 var bdTxtId = "<%=bidTextId%>";
 var bdBtnId = "<%=bidBtnId%>";
 var bdMsgId = "<%=bidMsgId%>";
 var newItmLnkId = "<%=newItemsLinkId%>";
 var latestItem = <%=latestItemsTimestamp%>;
 initializeClock(idd, dur,itemId,bdTxtId,bdBtnId,bdMsgId);
 var bdHighId = "<%=bidHighestId%>"; 
 var itmBasePrice = <%=itemDetails.getBasePrice()%>;
 var userHighBid = "<%=userbids.get(itemDetails.getItemId())%>";
 var validTxt = "<%=validStr%>"
 frequentBidQuery(itemId,latestItem,newItmLnkId,bdHighId,bdTxtId,itmBasePrice,userHighBid,validTxt);
</script>
 <%
 }
 %>
</Table>
</body>
</html>