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
	<title>项目前期工作维护</title>
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/bg2/css/laboratory/roomList.css" rel="stylesheet" type="text/css">
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/bg2/css/laboratory/easyui.css" rel="stylesheet"/>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datagrid/js/jquery-tool.datagrid.js"></script> 
	<script src="<%=request.getContextPath()%>/bg2/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/bg2/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/bg2/js/laboratory/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/bg2/js/laboratory/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/bg2/js/laboratory/roomList.js"></script>
</head>
<body>
	<div class="main_div"></div>
	<input type = "hidden" value = ${filter} id = "filter" >  
	<!-- start    查询条件 -->
	<div class="sheach">
		<div class='content_top'>项目前期工作维护</div>	 
		<form id="queryForm" style="margin-bottom: 10px;">
			<label>名称：</label>
			<input type = "text" id = "proName" name = "proName" style="width: 10%" class = "inputQuery changeQuery" >
			
			<label  for="querySex">类型：</label>
			<select id = "proStatus" name = "proStatus"   class = "changeQuery">
				<option></option>
				<c:forEach var ="dict" items="${dictMap}">
					<option value=${dict.key}>${dict.value}</option>
				</c:forEach>
			</select>
			<!-- 查询按钮  -->
			<div id = "queryButton" class = "btn query" onclick = "roomList.query()">搜索</div>
		</form>
	</div>
	<!-- end    查询条件 -->
	
	<!-- start   新增  修改  删除按钮 -->
	<div id="funcBtn" style="width:100%;height: 35px;">
	   	<div class='btn right addButton' onclick="roomList.expEvent()">导出</div> 
	   	<div class='btn right importButton' onclick="roomList.importEvent()">导入</div> 
		<div class='btn right deleteButton' onclick="roomList.delEvent()" >删除</div>
	   	<div class='btn right updateButton' onclick="roomList.updateEvent()">修改</div>
	   	<div class='btn right addButton' onclick="roomList.addEvent()">新增</div> 
	</div>
	<!-- end   新增  修改  删除按钮 -->
	
	<!-- start 列表展示 -->
	<div class="tabbable" >
		<div id="datagrid"></div>
		<div class="tablepage"></div>
	</div>
	<!-- end 列表展示 -->

</body>
</html>