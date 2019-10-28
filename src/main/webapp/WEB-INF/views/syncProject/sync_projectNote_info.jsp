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
	<div class='content_top'>项目同步记录</div>
	<form id="queryForm" style="margin-bottom: 10px;">
		<label  for="beginDate" class="yearTitle">同步日期：</label>
		<input id="beginDate" name="beginDate"
			   onclick=" WdatePicker({dateFmt:'yyyy-MM-dd',enableInputMask:false})" readonly="true"
			   type="text"
			   class="Wdate validNull  "
			   title="必填项 ,同步开始时间（格式：yyyy-MM-dd）"
			   content="同步开始时间"
			   style="width:150px;"
		/>
		<label >至 </label>
		<input id="endDate" name="endDate"
			   onclick=" WdatePicker({dateFmt:'yyyy-MM-dd',enableInputMask:false})" readonly="true"
			   type="text"
			   class="Wdate validNull  "
			   title="必填项  ,同步开始时间（格式：yyyy-MM-dd）"
			   content="同步开始时间"
			   style="width:150px;"
		/>
		<label  for="projectType" class="yearTitle"> 项目类型：</label>
		<select id = "projectType" name = "projectType"   class = "changeQuery userlevel" style="width: 240px;margin-left: -2px"  onchange="changeDeptCode()">
			<option value = "">   </option>
			<c:forEach  var="dataDictionaryList"  items="${dataDictionaryList}">
				<option value ="${dataDictionaryList.CODE}" title=" ${dataDictionaryList.NAME}" > ${dataDictionaryList.NAME}</option>
			</c:forEach>
		</select>
		<label  for="deptCode" class="yearTitle">部门名称：</label>
		<select id = "deptCode" name = "deptCode"   class = "changeQuery userlevel" style="width: 240px;margin-left: -2px">
			<option value = "">   </option>
		</select>
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
<script src="<%=request.getContextPath()%>/js/syncProject/projectNote.js"></script>
</body>
</html>