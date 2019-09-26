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
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/zTree/css/bootstrapStyle.css">
	<link href="<%=request.getContextPath()%>/css/ztree/demo.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/ztree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
	<script src="<%=request.getContextPath()%>/js/ztree/jquery-1.4.4.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/ztree/jquery.ztree.core.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>
	<script src="<%=request.getContextPath()%>/js/yygl/comprehensive/item.js"></script>

	<style type="text/css">
		.tree-box {
			border: 1px solid #1b9974;
			background: #ffffff;
			width: 100%;
			border-radius: 5px;
			position: relative;
			float: left;
			margin-top: -10px;
			height: 407px !important;
			overflow-y:auto;
		}
		.tree-box li{
			padding: 3px 4px;
		}
		._box {
			border: 1px solid #1b9974;
			background: #ffffff;
			padding: 7px 10px;
			width: 100%;
			border-top-left-radius: 5px;
			border-top-right-radius: 5px;
			position: relative;
			margin-top: -10px;
			height: 38px;
		}
		._box input {
			height: 22px;
			padding: 1px 5px;
		}
		._box label {
			font-size: 12px;
			font-weight: normal;
			margin-top: 4px;
			float: left;
			width: 68px;
			text-align: right;
		}
		._box .controls {
			margin-left: 71px;
		}

		body {
			background-color:#D5E7E7;
			padding:15px;
		}
		h5 {
			margin: 2px 0;
			color:#0a433a;
			font-weight:bold;
		}
		hr {
			color:#ffffff;
			background-color:#ffffff;
			border-color:#ffffff;
		}
		.page-header-sl {
			margin: 5px 0 7px 0;
			height: 10px;
		}

		.page-header-sl h5{
			float: left;
		}
		.button-box {
			float: right;
		}
		.btn{
			background-color:#2D9592;
			border-color:#2D9592;
		}
		.btn:hover{
			background-color:#00828a;
			border-color:#00828a;
		}


	</style>
</head>
<body>
		<div class="page-header-sl" style="height: 32px;">
			<h5> 用印事项</h5>
			<div class="button-box">
				 <button type="button" class="btn btn-warning btn-xs" onclick="clearChecked()"> 清空</button>
			</div>
		</div>

     <input type='hidden' name='itemList' id='itemList' value='${itemList}'>
	 <div class="tree-box" style="width: 270px;height: 241px !important">
		<div id="treeDemo" class="ztree"></div>
	</div>
</body>
</html>