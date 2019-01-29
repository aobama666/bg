<!DOCTYPE>
<!-- authentication_import_excel_page.jsp -->
<%@ page language="java" contentType="text/html;charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>批量录入</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css"
	media="screen">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/css/style.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath()%>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->
<style type="text/css">
	label{
		width: 110px;
    	text-align: right;
	}
</style>
</head>
<body >
	<div class="page-header-sl">
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs"
				onclick="downLoadTemp('个人工时填报模板.xls')">模板下载</button>
		</div>
	</div>
	<hr>
	<form class="form-inline bg-white" sytle="width:500px" method="post" target="hidden_frame">
		<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期： -->
		<label>日期：</label>
		<div class="form-group" style="display: inline-block">
			<input type="text" readonly style="display: inline-block;width:196px" name="startDate" class="form-control form_date" id="startDate" placeholder="">
		</div>
		<div class="form-group" style="display: inline-block">
			-- 
			<input type="text" readonly style="display: inline-block;width:196px" name="endDate" class="form-control form_date" id="endDate" placeholder="">
		</div>
		<input type="hidden" name="proIds" id="selectProIds">
	</form>
	<form class="form-inline">
		<div class="form-group">
			<label>选择工作任务：</label>
			<div class="input-group bg-white" id="selectProject" style="width: 415px; display: inline-table; vertical-align: middle">
				<input type="text" name="selectProject" id="selectNames" class="form-control" readonly> 
				<span class="input-group-addon"><span class="glyphicon glyphicon-briefcase"></span></span>
			</div>
		</div>
	</form>
	<iframe name='hidden_frame' id="hidden_frame" width="0" height="0" frameborder="0"></iframe>
</body>
<script type="text/javascript" charset="utf-8">

$(function(){
	$("#selectProject").click(function(){
		var startDate=$("#startDate").val();
		var endDate=$("#endDate").val();
		if(startDate=="" || endDate==""){
			parent.layer.msg("请指定开始日期和结束日期");
			return;
		}else if(getDate(startDate)>getDate(endDate)){
			parent.layer.msg("结束日期小于开始日期");
			return;
		}
		var ran = Math.random()*1000;
		parent.layer.open({
			type:2,
			title:"工作任务选择框",
			area:['730px', '60%'],
			scrollbar:true,
			skin:'query-box',
			content:['<%=request.getContextPath()%>/staffWorkbench/DIYProJobSelector?ran='+ran]
		}); 
	});
	
	$(".form_date").datepicker({
		autoclose:true,
		orientation:'auto',
		language: 'cn',
		format: 'yyyy-mm-dd',
		todayHighlight:true
	});
});

function downLoadTemp(){
	var ran = Math.random()*1000;
	var proIds=$("#selectProIds").val();
	var startDate=$("#startDate").val();
	var endDate=$("#endDate").val();
	if(startDate=="" || endDate==""){
		parent.layer.msg("请指定开始日期和结束日期");
		return;
	}else if(getDate(startDate)>getDate(endDate)){
		parent.layer.msg("结束日期小于开始日期");
		return;
	}
	if(proIds==""){
		parent.layer.msg("请选择项目");
		return;
	}
	document.forms[0].action ="<%=request.getContextPath()%>/staffWorkbench/downLoadDIYTemp?ran="+ran;
	document.forms[0].submit();
}

function forClose() {
	parent.layer.close(parent.layer.getFrameIndex(window.name));
} 

function getDate(dateStr){
	var reg=new RegExp("\\-","gi");
	dateStr=dateStr.replace(reg,"/");
	var millisSeconds=Date.parse(dateStr);
	var date=new Date();
	date.setTime(millisSeconds);
	return date;
}
</script>
</html>
