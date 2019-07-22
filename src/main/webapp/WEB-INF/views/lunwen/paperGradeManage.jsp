<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>论文打分</title>
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
	<div class='content_top'>论文打分</div>
	<form id="queryForm" style="margin-bottom: 10px;margin-left: 20px">
		<label>论文题目：</label>
		<input type = "text" id = "paperName" name = "paperName" style="width: 200px" class = "inputQuery changeQuery" >
		<label  for="scoreStatus" class="yearTitle">打分状态：</label>
		<select id = "scoreStatus" name = "scoreStatus"   class = "changeQuery changeYear" style="width: 100px">
			<option value = "" selected>请选择</option>
			<c:forEach  var="scoreStatus"  items="${scoreStatus}">
				<option value ="${scoreStatus.K}"}> ${scoreStatus.V}</option>
			</c:forEach>
		</select>
		<input type="text" id="paperType" name="paperType" style="display: none" value="1">
		<!-- 查询按钮  " -->
		<div style="float:right" id = "queryButton" class = "btn query" onclick = "grade.query()">搜索</div>
	</form>
</div>
<!-- end    查询条件 -->

<hr/>
<!-- start 列表展示 -->
<div>
	<!--  新增  修改  删除 功能按钮 -->
	<div id="funcBtn" style="height: 35px;float:right">
		<div class='btn right deleteButton' onclick="grade.scoreSubmit()">提交</div>
	</div>
	<!-- 学术技术综述分类按钮 -->
	<div class="grid-title">
		<h3 style="float: left">
			<a href="#" style="color:black" onclick="grade.updatePaperType(1)">学术类</a>
		</h3>
		<h3 style="float: left">
			<a href="#" style="color:black" onclick="grade.updatePaperType(2)">技术类</a>
		</h3>
		<h3 style="float: left">
			<a href="#" style="color:black" onclick="grade.updatePaperType(3)">综述类</a>
		</h3>
		<h3></h3>
	</div>
	<hr/>
</div>
<div class="tabbable active" >
	<div id="datagrid" style=""></div>
	<div class="tablepage"></div>
</div>
<!-- end 列表展示 -->

<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->

<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>

<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>  	<!-- 弹框.js  -->
<!-- 引入datagrid -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
<!-- 验证校验公共方法，提示框公共方法 -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
<!-- 本页面所需的js -->
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/lunwen/paperGrade.js"></script>
</body>

</html>