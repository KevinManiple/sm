<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>sm</title>
</head>
<body>
	Spring core + Spring MVC + MyBatis
	<br>
	<a href="<%=path%>/user/listUser.do">User List</a>
</body>
</html>
