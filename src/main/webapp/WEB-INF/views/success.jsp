<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
<style>
.loginout{
	 background: #20a29e;
	 cursor:pointer;
}
</style>
</head>
<body>
	欢迎登陆统一管理平台<br/><br/><span class="loginout">退出系统</span>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.7.2.min.js"></script>
<script>
	$(".loginout").on("click",function(){
		location.href="/bg/index/loginout"; 
	})
</script>
</body>
</html>