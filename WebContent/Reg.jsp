<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registration</title>
</head>
<body style="background-color:CornSilk">
<a id = 'noback'></a>
<a id = 'noback again'></a>
<script>
window.location.hash="";
window.location.hash="";//again because google chrome don't insert first hash into history
window.onhashchange=function(){window.location.hash="";}
</script> 
<h2><font color="White"><marquee direction="left" style="background:SaddleBrown">Welcome to Auction and Bidding.</marquee></font> </h2>
           <form method="post" action="registration">  
    <br>
                <center>
            <table border="2" width="30%" cellpadding="6">
                <thead>
                    <tr>
                        <th colspan="2">Enter Information Here</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>First Name</td>
                        <td><input type="text" name="firstname" value="" required/></td>
                    </tr>
                    <tr>
                        <td>Last Name</td>
                        <td><input type="text" name="lastname" value="" required/></td>
                    </tr>
                    <tr>
                        <td>Email</td>
                        <td><input type="email" name="email" value="" required/></td>
                    </tr>
                    <tr>
                        <td>Username</td>
                        <td><input type="text" name="username" value="" required/></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="password" value="" required/></td>
                    </tr>
                    <tr>
                        <td><input type="submit" name="tag" value="Register" /></td>
                        <td><input type="reset" value="Reset" /></td>
                    </tr>
                    <tr>
                        <td colspan="2">Already registered!! <a href="Login.jsp">Login Here</a></td>
                    </tr>
                </tbody>
            </table>
            </center>
        </form>
    </body>
</html>