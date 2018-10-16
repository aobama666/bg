<!DOCTYPE>
<!-- authentication_update.jsp -->
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>

<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>工时管理修改</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->
</head>
<body>
<div class="page-header-sl">
	<h5>工时管理修改</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="forSave()"> 保存</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workCommit()"> 提交</button>
		<!-- <button type="button" class="btn btn-warning btn-xs" onclick="forClose()"> 关闭</button> -->
	</div>
</div>
<hr>
<div class="form-box">
	<div class="form-group col-xs-12">
		<label for="empCode">人员姓名</label>
		<div class="controls">
			<input type="text" name="userName" property=""userName"" disabled="disabled">
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="empCode">人员编号</label>
		<div class="controls">
			<input type="text" name="userCode" property="userCode" disabled="disabled">
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="documentName">日期</label>
		<div class="controls">
			<input type="text" name="time" property="time"  disabled="disabled">
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="documentCode">项目名称</label>
		<div class="controls">
			<input type="text" name="proName" property="proName"   >
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="empCode">项目类型</label>
		<div class="controls">
			<input type="text" name="proType" property="proType" disabled="disabled">
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="classification">工作内容</label><!--<font class="glyphicon glyphicon-asterisk required"></font>-->
		<div class="controls">
			<textarea style="height:60px;" name="workContent"></textarea>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="documentType"><font class="glyphicon glyphicon-asterisk required"></font>投入工时</label>
		<div class="controls">
			<input type="text" name="hours" property="hours">
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
var id=common.getQueryString("id");

function forSave(){
	var userName = $("input[name=userName]").val();
	var userCode = $("input[name=userCode]").val();
	var time = $("input[name=time]").val();
	var proName = $("input[name=proName]").val();
	var proType = $("input[name=proType]").val();
	var workContent = $("textarea[name=workContent]").val();
	var hours = $("input[name=hours]").val();
	var ran = Math.random()*10000;
	var validator=[
	           	      //{name:'workContent',vali:'required'},暂不校验工作内容必填
	           	      {name:'hours',vali:'required'}
	          	];
	var checkResult = $(".form-box").sotoValidate(validator);
	if(checkResult){
		var result = checkNumberFormat(hours);
		if(!result.result){
			layer.msg(result.info);
			return false;
		}
		var param = {
				userName:userName,
				userCode:userCode,
				time:time,
				proName:proName,
				proType:proType,
				workContent:workContent,
				hours:hours,
				id:id
		}
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath() %>/BgWorkinghourInfo/updatabgWorkinghourInfo?ran="+ran,
			data:param,
			success:function(data){
				if(data.success == "true"){
					 layer.msg(data.msg);
				}else{
					 layer.msg(data.msg);
				}
			}
		});
		
	}
	
}
function workCommit(){
	var userName = $("input[name=userName]").val();
	var userCode = $("input[name=userCode]").val();
	var time = $("input[name=time]").val();
	var proName = $("input[name=proName]").val();
	var proType = $("input[name=proType]").val();
	var workContent = $("textarea[name=workContent]").val();
	var hours = $("input[name=hours]").val();
	var ran = Math.random()*10000;
	var validator=[
	           	      //{name:'workContent',vali:'required'},暂不校验工作内容必填
	           	      {name:'hours',vali:'required'}
	          	];
	var checkResult = $(".form-box").sotoValidate(validator);
	if(checkResult){
		var result = checkNumberFormat(hours);
		if(!result.result){
			layer.msg(result.info);
			return false;
		}
		
		var param = {
				userName:userName,
				userCode:userCode,
				time:time,
				proName:proName,
				proType:proType,
				workContent:workContent,
				hours:hours,
				id:id
		}
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath() %>/BgWorkinghourInfo/commitbgWorkinghourInfo?ran="+ran,
			data:param,
			success:function(data){
				if(data.success == "true"){
					 layer.msg(data.msg);
					 parent.layer.close(parent.layer.getFrameIndex(window.name));
				}else{
					 layer.msg(data.msg);
				}
			}
		});
		 
	}
	
	 
	

}

function checkNumberFormat(workHour){
	var result = {};
	var reg=/^([0-9]+|[0-9]*\.[05])$/;
	if($.trim(workHour)!="" && !reg.test(workHour)){
		result.result = false;
		result.info = "必须为数字且最小时间单位为0.5h；";
	}else{
		result.result = true;
		result.info = "";
	}
	return result;
}
function checkUniqueness(){
	var ran = Math.random()*10000;
	$.ajax({
		type: 'POST',
		url:'<%=request.getContextPath()%>/BgWorkinghourInfo/selectForbgWorkinghourInfo?ran='+ran,
		async: false,
		data: {id:id,page:1,limit:1000},
		success:function(data){
			if(data.items[0].CATEGORY=="非项目工作"){
				$("input[name=proName]").attr("readOnly",false);
			}else{
				$("input[name=proName]").attr("disabled","disabled");
			}
			$("input[name=userName]").val(data.items[0].WORKERS);
			$("input[name=userCode]").val(data.items[0].EMPLOYEENUMBER);
			$("input[name=time]").val(data.items[0].WORK_TIME);
			$("input[name=proName]").val(data.items[0].PROJECT_NAME);
			$("input[name=proType]").val(data.items[0].CATEGORY);
			$("textarea[name=workContent]").val(data.items[0].JOB_CONTENT);
			$("input[name=hours]").val(data.items[0].WORKING_HOUR);
		}
	});
}
checkUniqueness();
function forClose(){
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	