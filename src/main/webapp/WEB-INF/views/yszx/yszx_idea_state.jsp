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
	<title>演示中心管理预定状态页</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
<%-- 	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css"> --%>
	<link href="<%=request.getContextPath()%>/yszx/css/laboratory/roomList.css" rel="stylesheet" type="text/css">
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/laboratory/easyui.css" rel="stylesheet"/>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<%-- <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/js/jquery-tool.datagrid.js"></script>  --%>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/roomList.js"></script>
</head>
<body>
	<div class="main_div"></div>
	<input type = "hidden" value = ${filter} id = "filter" >  
	<!-- start  头部 -->
	<div style="margin-right: 1%;">
		<button class="btn right nextweekBtn" type="button">下一周</button>
		<button class="btn right nowweekBtn" type="button">本周</button>
		<button class="btn right prevweekBtn" type="button">上一周</button>
	</div>
	<div class="grid-title approveMessage">
		<h3>预定状态查询</h3>
	</div>
	
	<!-- end  头部 -->
	
	<!-- start预定状态信息 -->
	<table class="reserveState tableStyle thTableStyle">
		<tr>
			<th>时间段：2-11至2-15</th>
			<th>上午8:30-11:30至下午14:00-16:30</th>
			<th>预定情况</th>
			<th>预定情况</th>
			<th>预定情况</th>
		</tr>
		<tr>
			<td rowspan="2">星期一</td>
			<td>上午</td>
			<td class="redColor">已预订（08:30-09:10）</td>
			<td class="redColor">已预订（09:40-10:20）</td>
			<td class="redColor">已预订（10:50-11:30）</td>
		</tr>
		<tr>
			<td>下午</td>
			<td class="redColor">已预订（14:00-14:30）</td>
			<td class="redColor">已预订（15:00-15:30）</td>
			<td class="redColor">已预订（16:00-16:30）</td>
		</tr>
		<tr>
			<td rowspan="2">星期二</td>
			<td>上午</td>
			<td class="redColor">已预订（08:30-09:10）</td>
			<td></td>
			<td class="redColor">已预订（10:50-11:30）</td>
		</tr>
		<tr>
			<td>下午</td>
			<td class="redColor">已预订（14:00-14:30）</td>
			<td class="redColor">已预订（15:00-15:30）</td>
			<td></td>
		</tr>
	</table>
	
	<!-- end预定状态信息-->
	
</body>
</html>