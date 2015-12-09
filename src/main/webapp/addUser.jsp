<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add User</title>
</head>

<body>
	<form action="<%=path%>/user/addUser.do" method="POST">
		<table>
			<tr>
				<td>name:</td>
				<td><input type="text" name="name"></td>
			</tr>
			<tr>
				<td>age:</td>
				<td><input type="text" name="age"></td>
			</tr>
			<tr>
				<td>address:</td>
				<td><input type="text" name="address"></td>
			</tr>
			<tr>
				<td><input type="submit" value="Save"></td>
				<td><input type="reset" value="Reset"></td>
			</tr>
		</table>
	</form>
</body>
</html>
