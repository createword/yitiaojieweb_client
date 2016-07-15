<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'form.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<table border="1">
		<thead>
			<tr>
				<th>序列</th>
				<th>标题</th>
				<th>发布时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<c:forEach items="${blogs}" var="b" varStatus="s">
			<tr>
				<th>${s.index+1}</th>
				<th>${b.title}</th>
				<th>${b.content}</th>
				<th>${b.publicTime}</th>
				<th><a href="/blog/form?id=${b.id }">编辑</a> <a
					href="/blog/delete?id=${b.id }">删除</a></th>
			</tr>

		</c:forEach>
	</table>
	<a href="/blog/form">添加</a>
</body>
</html>
