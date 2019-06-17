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
	<title>论文评审临时测试</title>
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
		<div class='content_top'>论文管理</div>
		<form id="queryForm" style="margin-bottom: 10px;">
				<label  for="year" class="yearTitle">年度：</label>
				<select id = "year" name = "year"   class = "changeQuery changeYear" style="width: 100px">
					<option value = "2019"  selected> 2019</option>
					<%--<option value = "${year}"  selected>  ${year}</option>--%>
				</select>
				<label>论文题目：</label>
				<input type = "text" id = "paper_name" name = "applyId" style="width: 200px" class = "inputQuery changeQuery" >
				<label>编号：</label>
				<input type = "text" id = "paper_code" name = "applyId" style="width: 100px" class = "inputQuery changeQuery" >
				<label>单位：</label>
				<input type = "text" id = "unit" name = "applyId" style="width: 100px" class = "inputQuery changeQuery" >
				<label>作者：</label>
				<input type = "text" id = "author" name = "applyId" style="width: 100px" class = "inputQuery changeQuery" >
				<label>领域：</label>
				<input type = "text" id = "field" name = "applyId" style="width: 100px" class = "inputQuery changeQuery" >
				<label  for="score_status" class="yearTitle">打分状态：</label>
				<select id = "score_status" name = "year"   class = "changeQuery changeYear" style="width: 100px">
					<option value = "1">未打分</option>
					<option value = "0"  selected>  已打分</option>
				</select>
				<!-- 查询按钮  " -->
				<div style="float:right" id = "queryButton" class = "btn query" onclick = "roomList.query()">搜索</div>
		</form>
	</div>
	<!-- end    查询条件 -->
	
	<!-- start   新增  修改  删除按钮 -->
	<div id="funcBtn" style="width:100%;height: 35px;">
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >删除</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >下载模板</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >附件批量导入</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >导入</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >修改</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >新增</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >自动匹配</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >生成打分表</div>
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >撤回打分表</div>
    </div>
	<!-- end   新增  修改  删除按钮 -->
	
	

	<!-- start 列表展示 -->
	<div class="tab" role="tabplanel">
		<ul class="nav nav-tabs" role="tablist" style="margin-top: 0px;" id="typeTabs">
			<li role="presentation"><a href="#xueshu" aria-controls="home" role="tab" data-toggle="tab">学术类</a> </li>
			<li role="presentation"><a href="#jishu" aria-controls="profile"  role="tab" data-toggle="tab">技术类</a> </li>
			<li role="presentation"><a href="#zongshu" aria-controls="messages" role="tab" data-toggle="tab">综述类</a> </li>
		</ul>
	</div>

	<div class="tab-content">

		<div role="tabpanel" class="tab-pane fade" id="xueshu">
			<div class="tab-content">
				<div class="tab-pane active" >
					<div id="datagrid"></div>
					<div class="tablepage"></div>
				</div>
			</div>
		</div>

		<div role="tabpanel" class="tab-pane fade" id="jishu">
			<div class="tab-content">
				<div class="tab-pane active" >
					<div id="datagrid2"></div>
					<div class="tablepage"></div>
				</div>
			</div>
		</div>

		<div role="tabpanel" class="tab-pane fade" id="zongshu">
			<div class="tab-content">
				<div class="tab-pane active" >
					<div id="datagrid3"></div>
					<div class="tablepage"></div>
				</div>
			</div>
		</div>

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
 	<script src="<%=request.getContextPath()%>/js/lunwen/lunwen.js"></script>
</body>

</html>