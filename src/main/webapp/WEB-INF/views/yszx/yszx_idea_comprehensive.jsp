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
	<title>演示中心综合查询表页</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/js/jquery-tool.datagrid.js"></script> 
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/idea/roomComprehensive.js"></script>
  
</head>
<body>
	<div class="main_div"></div>
	<input type = "hidden" value = ${filter} id = "filter" >  
	<!-- start    查询条件 -->
	<div class="sheach">
		<div class='content_top'>综合查询</div>	 
		<form id="queryForm" style="margin-bottom: 10px;">
			<label>申请单号：</label>
			<input type = "text" id = "applyNumber" name = "applyNumber" style="width: 10%" class = "inputQuery changeQuery" >
			
			<label  for="year" class="yearTitle">年度：</label>
			<select id = "year" name = "year"   class = "changeQuery changeYear">
				<option value = "">  </option>
				<option value = "${year}" selected>  ${year}   </option>
			</select>
			<label  for="month">月度：</label>
			<select id = "month" name = "month"   class = "changeQuery changeMonth">
				<option value="" > </option>
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
			<label  for="year" class="yearTitle">申请部门（单位）：</label>
			<select id = "applyDept" name = "applyDept"   class = "changeQuery userlevel">
				<option value = "">   </option>
				<c:forEach  var="deptInfo"  items="${deptInfo}">
					        <option value ="${deptInfo.applyDeptID}"  > ${deptInfo.applyDeptName}</option>
					     </c:forEach>
			     </select>
			 
			<label>参观领导姓名：</label>
			<input type = "text" id = "visitUserName" name = "visitUserName" style="width: 10%" class = "inputQuery changeQuery" >
			<label>参观领导级别：</label>
			<select id="visitLevel"  name = "visitLevel"  class = "changeQuery userlevel"  >
						<option value=""  ></option>
						<c:forEach  var="visitUnitLevleInfo"  items="${visitUnitLevleInfo}">
					        <option value ="${visitUnitLevleInfo.V}"        > ${visitUnitLevleInfo.V}</option>
					     </c:forEach>
			        	</select>
			<!-- 查询按钮  -->
			<div id = "queryButton" class = "btn query" onclick = "roomList.query()">搜索</div> <!-- 原来引用的函数onclick = "roomList.query()" -->
		</form>
	</div>
	<!-- end    查询条件 -->
	
	<!-- start   新增  修改  删除按钮 -->
	<div id="funcBtn" style="width:100%;height: 35px;margin-bottom:-35px;">
		<div class='btn right deleteButton' onclick="roomList.expEvent()" >导出</div>
		 
	</div>
	<!-- end   新增  修改  删除按钮 -->
	
	<!-- start 列表标题 -->
	<div class="grid-title">
		<h3>综合查询列表</h3>
	</div>
	<!-- end 列表标题 -->
	
	<!-- start 列表展示 -->
	<div class="tabbable" >
		<div id="datagrid"></div>
		<div class="tablepage"></div>
	</div>
	<!-- end 列表展示 -->

</body>
</html>