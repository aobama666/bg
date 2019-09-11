<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>用印事项</title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/css/ztree/demo.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/ztree/jquery-1.4.4.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/ztree/jquery.ztree.core.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>
	<script src="<%=request.getContextPath()%>/js/yygl/comprehensive/item.js"></script>
</head>
<body>
     <input type='hidden' name='itemList' id='itemList' value='${itemList}'>
	<div  style="margin-left: 74px;margin-top: 13px">
		<div id="treeDemo" class="ztree"></div>
	</div>
</body>
</html>