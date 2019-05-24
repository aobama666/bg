<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>演示中心待办表页</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<style>
		.window-mask,.window-shadow{z-index: 99999999!important;}
		.messager-window{z-index: 999999999!important;}
	</style>
	
</head>
<body>
	<div class="main_div"></div>
	<!-- start    查询条件 -->
	<div class="sheach">
		<div class='content_top'>待办事项</div>	 
		<form id="queryForm" style="margin-bottom: 10px;">
			<label>申请单号：</label>
			<input type = "text" id = "appltNumber" name = "appltNumber" class = "inputQuery changeQuery" style="width: 200px;">
			
			<label  for="applyDept" class="yearTitle">申请部门：</label>
		    <select id = "applyDept" name = "applyDept"   class = "changeQuery userlevel" style="width:240px;margin-right: 20px;">
				<option value = "">   </option>
				<c:forEach  var="deptInfo"  items="${deptInfo}">
					        <option value ="${deptInfo.applyDeptID}" title="${deptInfo.applyDeptName}"  > ${deptInfo.applyDeptName}</option>
			    </c:forEach>
			</select>
			
			<label  for="contactUser">联系人：</label>
			 <input type = "text" id = "contactUser" name = "contactUser"  class = "inputQuery changeQuery" style="width: 200px;">
			<!-- 查询按钮  -->
			<div id = "queryButton" class = "btn query" onclick = "roomList.query()">搜索</div> <!-- 原来引用的函数onclick = "roomList.query()" -->
		</form>
	</div>
	<!-- end    查询条件 -->
	
	<!-- start   新增  修改  删除按钮 -->
	<div id="funcBtn" style="width:100%;height: 35px;">
	    <div class='btn right repealButton' onclick="roomList.returnEvent()">退回</div>
		<div class='btn right deleteButton' onclick="roomList.agreeEvent()" >同意</div>
	</div>
	<!-- end   新增  修改  删除按钮 -->
	
	<!-- start 列表标题 -->
	<!--
	<div class="grid-title">
		<h3>待办列表</h3>
	</div>
	-->
	<!-- end 列表标题 -->
	
	<!-- start 列表展示 -->
	<div class="tabbable" >
	    <div class="tab-content">
	        <!-- 表格 -->
	        <div class="tab-pane active" >
	            <div id="datagrid"></div>
	            <div class="tablepage"></div>
	        </div>
	    </div>
    </div>
	<!-- end 列表展示 -->
	
    <script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script> 
     
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  
 	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>  	<!-- 弹框.js  --> 
	<!-- 引入datagrid -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
    <!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/idea/roomDealtList.js"></script>
</body>
</html>