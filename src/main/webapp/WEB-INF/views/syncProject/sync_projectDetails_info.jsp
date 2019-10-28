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
	<title>项目同步记录</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">

</head>
<body>
<div class="main_div"></div>
<!-- start    查询条件 -->
<div class="sheach">

	<form id="queryForm" style="margin-bottom: 10px;">
		<input type = "hidden"   id = "noteId" name="noteId" value="${noteId}">
		<label  for="year" class="yearTitle">考核年度：</label>
		<select id = "year" name = "year"   class = "changeQuery userlevel" style="width: 240px;margin-left: -2px"  onchange="changeMonth()">
			<option value = "">   </option>
			<c:forEach  var="yearInfo"  items="${yearInfo}">
				<option value ="${yearInfo.YEAR}" title=" ${yearInfo.YEAR}" > ${yearInfo.YEAR}</option>
			</c:forEach>
		</select>

		<label  for="month" class="yearTitle">考核月度：</label>
		<select id = "month" name = "month"   class = "changeQuery userlevel" style="width: 240px;margin-left: -2px"   >
			<option value = "">   </option>

		</select>
		<label  for="projectName" class="yearTitle"> 项目名称：</label>
		<input type = "text" id = "projectName" name = "projectName" style="width: 240px" class = "inputQuery changeQuery" >
		<br>
		<label  for="projectNumber" class="yearTitle">项目编码：</label>
		<input type = "text" id = "projectNumber" name = "projectNumber" style="width: 240px" class = "inputQuery changeQuery" >

		<label  for="wbsNumber" class="yearTitle">WBS编号：</label>

		<input type = "text" id = "wbsNumber" name = "wbsNumber" style="width: 240px" class = "inputQuery changeQuery" >

		<div id = "queryButton" class = "btn query" onclick = "roomList.query()" style="margin-left: 20px;">搜索</div> <!-- 原来引用的函数onclick = "roomList.query()" -->

	</form>
</div>
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
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->

<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>

<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>  	<!-- 弹框.js  -->
<!-- 引入datagrid -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
<!-- 验证校验公共方法，提示框公共方法 -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
<!-- 本页面所需的js -->
<script src="<%=request.getContextPath()%>/js/syncProject/projectDetails.js"></script>
</body>
</html>