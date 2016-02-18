<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="org.sandeep.helpers.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Auction App</title>
</head>

<body style="background-color:CornSilk">
<a id = 'noback'></a>
<a id = 'noback again'></a>
<script>
window.location.hash="";
window.location.hash="";//again because google chrome don't insert first hash into history
window.onhashchange=function(){window.location.hash="";}
</script> 
<h2> <font color="White"><marquee direction="left" style="background:SaddleBrown">Welcome to Auction and Bidding.</marquee></font> </h2>
        <form method="post" action="login">
            <center>
            <table border="2" width="30%" cellpadding="5">
                <thead>
                    <tr>
                        <th colspan="2">Login Here</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>User Name</td>
                        <td><input type="text" name="username" value="" required/></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="password" value="" required/></td>
                    </tr>
                    <tr>
                        <td><input type="submit" name="tag" value="Login" /></td>
                        <td><input type="reset" value="Reset" /></td>
                    </tr>
                    
                    <%
                   	String err = request.getParameter("loginError");
                    //System.out.println(msg);
                    String msg;
                    if (err == null){
                    	msg = "Yet not registered!!!";
                    }
                    
                    else{
                    	msg = "Login failed. Please retry.";
                    }
               //     SessionHandler.terminateSession(request);
                    %>
                    <tr>
                        <td colspan="2"><%=msg %> <a href="Reg.jsp">Register Here</a></td>
                    </tr>
                </tbody>
            </table>
            </center>
        </form>
<%
//HttpSession httpSession = request.getSession(false);
//httpSession.setAttribute("userinfo", null);
//httpSession.setAttribute("flow", "-1"); 
//httpSession.invalidate();
//System.out.println("session invalidated");
%>
    </body>
</html>