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
	<title>演示中心管理列表页</title>
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
    <input type="hidden" id="year" value="<%=request.getParameter("year")==null?"":request.getParameter("year")%>">
    <input type="hidden" id="month" value="<%=request.getParameter("month")==null?"":request.getParameter("month")%>">
	<div class="main_div"></div>

	<!-- start    查询条件 -->
	<div class="sheach">
		<div class='content_top'>参观设定</div>	 
		<form id="queryForm" style="margin-bottom: 10px;">
			<label>申请单号：</label>
			<input type = "text" id = "applyId" name = "applyId" style="width: 10%" class = "inputQuery changeQuery" >
			
			<label  for="year" class="yearTitle">年度：</label>
			<select id = "year" name = "year"   class = "changeQuery changeYear">
				<option value = ""></option>
				<option value = "${year}"  selected>  ${year}</option>
			</select>
			<label  for="month">月度：</label>
			<select id = "month" name = "month"   class = "changeQuery changeMonth">
				<option value="" ></option>
				<option value="01"   ${month == '01' ?"selected='selected'":''}>1月</option>
				<option value="02"   ${month == '02' ?"selected='selected'":''}>2月</option>
				<option value="03"   ${month == '03' ?"selected='selected'":''}>3月</option>
				<option value="04"   ${month == '04' ?"selected='selected'":''}>4月</option>
				<option value="05"   ${month == '05' ?"selected='selected'":''}>5月</option>
				<option value="06"   ${month == '06' ?"selected='selected'":''}>6月</option>
				<option value="07"   ${month == '07' ?"selected='selected'":''}>7月</option>
				<option value="08"   ${month == '08' ?"selected='selected'":''}>8月</option>
				<option value="09"   ${month == '09' ?"selected='selected'":''}>9月</option>
				<option value="10"   ${month == '10' ?"selected='selected'":''}>10月</option>
				<option value="11"   ${month == '11' ?"selected='selected'":''}>11月</option>
				<option value="12"   ${month == '12' ?"selected='selected'":''}>12月</option>
			</select>
			<!-- 查询按钮  " -->
			<div id = "queryButton" class = "btn query" onclick = "roomList.query()">搜索</div> 
			 

		</form>
	</div>
	<!-- end    查询条件 -->
	
	<!-- start   新增  修改  删除按钮 -->
    
	<div id="funcBtn" style="width:100%;height: 35px;">
   	    <div class='btn right deleteButton' onclick="roomList.delEvent()" >删除</div>
		<div class='btn right repealButton' onclick="roomList.repealEvent()">撤销</div>
		<div class='btn right submitButton' onclick="roomList.submitEvent()">提交</div>
	   	<div class='btn right updateButton' onclick="roomList.updateEvent()">修改</div>
	   	<div class='btn right addButton' onclick="roomList.addEvent()">新增</div> 
    </div>
	<!-- end   新增  修改  删除按钮 -->
	
	

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
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/idea/roomList.js"></script>
</body>

</html>