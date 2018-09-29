<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>login</title>
<link href="<%=request.getContextPath()%>/css/login/login.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/login/main.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="loginbox">
        <h1>登录页面</h1>
        <label>账号：</label><input type="text" class="loginame" name='nope' id='nope' placeholder='用户姓名' maxlength='40' value=""><br>
        <label>密码：</label><input type="password" class="loginpas" value=""><br>
        <input type="hidden" class="redirect" value="<%=request.getAttribute("redirect")%>"/>
        <span class="loginbnt">登录</span>
</div>
<script type="text/javascript" src="../js/jquery/jquery-1.7.2.min.js"></script>
<script src="<%=request.getContextPath()%>/js/stylePage/inputall/js/jquery.autocompleter.js"></script> 
<script src="<%=request.getContextPath()%>/js/stylePage/layer/layer.js"></script>
<script src="<%=request.getContextPath()%>/js/login/login.js"></script>
</body>
</html>