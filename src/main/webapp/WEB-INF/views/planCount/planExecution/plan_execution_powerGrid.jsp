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
	<title>计划统计-电网信息化执行数据维护</title>
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
<div class="sheach">
	<%--<div class='content_top'>执行数据综合维护</div>--%>
	<form id="queryForm" style="margin-bottom: 10px;">
		<input type="hidden"   id="specialType"  name="specialType" value="${sprcialType}">
		<label >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年度：</label>
		<select id="year"  name = "year"  title="年度" class = "changeQuery userlevel" style="width: 200px;margin-left: 0px"   >
			<c:forEach  var="yearInfo"  items="${yearList}">
				<option value ="${yearInfo.year}" title=" ${yearInfo.year}" > ${yearInfo.year}</option>
			</c:forEach>
		</select>

		<label  for="commitmentUnit" class="yearTitle"> 承担单位：</label>
		<select id = "commitmentUnit" name = "commitmentUnit" title="承担单位"    class = "changeQuery userlevel" style="width: 240px;margin-left: -2px">
			<option value = "">   </option>
			<c:forEach  var="commitmentUnitInfo"  items="${commitmentUnitList}">
				<option value ="${commitmentUnitInfo.PROFIT_CENTER_CODE}" title=" ${commitmentUnitInfo.PROFIT_CENTER_DEATIL}" > ${commitmentUnitInfo.PROFIT_CENTER_DEATIL}</option>
			</c:forEach>
		</select>
		<!-- 查询按钮  -->
		<div id = "queryButton" class = "btn query" onclick = "roomList.query()" style="margin-left: 20px;">搜索</div> <!-- 原来引用的函数onclick = "roomList.query()" -->
	</form>
</div>

<!-- end    查询条件 -->

<div class='btn right deleteButton' onclick="roomList.expEvent()" style="white-space: nowrap">导出</div>
<div  style="line-height: 37px">单位：万元</div>
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
<!-- end参观详情信息-->
<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>

<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->

<!-- 引入日期选择框 -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
<!-- 验证校验公共方法，提示框公共方法 -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
<!-- 本页面所需的js-->

<script src="<%=request.getContextPath()%>/js/planCount/planExecute/powerGrid.js"></script>


</body>
</html>