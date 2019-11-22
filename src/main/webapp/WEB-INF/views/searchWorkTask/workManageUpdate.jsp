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
			<input type="text" name="userName" property="userName" disabled="disabled">
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
		<label for="empCode">类型</label>
		<div class="controls">
			<input type="text" name="proType" property="proType" disabled="disabled">
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="documentCode">任务名称</label>
		<div class="controls">
			<input type="text" name="proName" property="proName"   >
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="classification">工作内容简述</label><!--<font class="glyphicon glyphicon-asterisk required"></font>-->
		<div class="controls">
			<textarea style="height:60px;" name="workContent"></textarea>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<div style="float: left">
			<label for="documentType"><!-- <font class="glyphicon glyphicon-asterisk required"></font> -->
				<span>投入工时(h)</span>
			</label>
			<div class="controls">
				<input type="text" style="width: 150px" name="hours" property="hours"  >
				<input type="hidden" name="hoursOld" property="hoursOld">
			</div>
		</div>
		<div style="padding-left: 290px">
			<span style="line-height: 26px; color: red">月度工时/已填报工时（h）：</span>
			<span id="fillSumKQ" style="color: red" >${fillSumKQ}</span>
			<span style="color: red">/</span>
			<span id="fillSum" style="color: red">${fillSum}</span>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
var id=common.getQueryString("id");
var selectedDate = common.getQueryString("selectedDate");

function forSave(){
	var userName = $("input[name=userName]").val();
	var userCode = $("input[name=userCode]").val();
	var time = $("input[name=time]").val();
	var proName = $("input[name=proName]").val();
	var proType = $("input[name=proType]").val();
	var workContent = $("textarea[name=workContent]").val();
	var hours = $("input[name=hours]").val();
	var hoursOld = $("input[name=hoursOld]").val();
    var fillSum = document.getElementById("fillSum").innerHTML;
    var fillSumKQ = document.getElementById("fillSumKQ").innerHTML;
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
		//验证累计工时是否超过月度工时
		/*if(fillSumKQ == '-'){
            layer.msg("无月度工时，不可提交");
            return;
		}
		fillSum = Number(fillSum)-Number(hoursOld)+Number(hours);
		if (fillSumKQ<fillSum){
            layer.msg("填报工时已超出月度工时，请检查");
            return;
		}*/
        if (fillSumKQ<hours){
            layer.msg("投入工时已超出月度工时，请检查");
            return;
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
					parent.layer.msg(data.msg);
				}else{
					parent.layer.msg(data.msg);
				}
				forClose();
			}
		});
		
	}
	
}

/*提交*/
function workCommit(){
	var userName = $("input[name=userName]").val();
	var userCode = $("input[name=userCode]").val();
	var time = $("input[name=time]").val();
	var proName = $("input[name=proName]").val();
	var proType = $("input[name=proType]").val();
	var workContent = $("textarea[name=workContent]").val();
	var hours = $("input[name=hours]").val();
    var hoursOld = $("input[name=hoursOld]").val();
    var fillSum = document.getElementById("fillSum").innerHTML;
    var fillSumKQ = document.getElementById("fillSumKQ").innerHTML;
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
        //验证累计工时是否超过月度工时
        if(fillSumKQ == '-'){
            layer.msg("无月度工时，不可提交");
            return;
        }
        debugger;
        //fillSum = Number(fillSum)-Number(hoursOld)+Number(hours);
       // fillSum = Number(fillSum)+Number(hours);

        if (fillSumKQ<hours){
            layer.msg("填报工时已超出月度工时，请检查");
            return;
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
					 parent.layer.msg(data.msg);
				}else{
					 parent.layer.msg(data.msg);
				}
				forClose();
			}
		});
		 
	}
	
	 
	

}

function checkNumberFormat(workHour){
	var result = {};
	var reg=/^([1-9]\d*|[1-9]\d*\.[05]|0\.5)$/;
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
			var dateStr = data.items[0].WORK_TIME_BEGIN.split('-');
            var WORK_TIME = dateStr[0]+"-"+dateStr[1];
			$("input[name=userName]").val(data.items[0].WORKERS);
			$("input[name=userCode]").val(data.items[0].EMPLOYEENUMBER);
			$("input[name=time]").val(WORK_TIME);
			$("input[name=proName]").val(data.items[0].PROJECT_NAME);
			$("input[name=proType]").val(data.items[0].CATEGORY);
			$("textarea[name=workContent]").val(data.items[0].JOB_CONTENT);
			$("input[name=hours]").val(data.items[0].WORKING_HOUR);
			$("input[name=hoursOld]").val(data.items[0].WORKING_HOUR);
		}
	});
}
/*获取指定月份的工时*/
function workingHours() {
    var ran = Math.random()*100000000;
    var fillSumKQ;
    var fillSum;
    $.ajax({
        type: 'POST',
        url:'<%=request.getContextPath()%>/staffWorkbench/workingHoursStatistics?ran='+ran,
        data:{selectedDate:selectedDate},
        dataType:'json',
        success : function(data) {
            if(data.fillSumKQ==0){
                fillSumKQ='-'
            }else {
                fillSumKQ = data.fillSumKQ;
            }
            if (data.fillSum==0){
                fillSum='-'
            }else {
                fillSum=data.fillSum;
            }
            document.getElementById("fillSumKQ").innerText=fillSumKQ;
            document.getElementById("fillSum").innerText=fillSum;
        }
    });
}

checkUniqueness();
workingHours();

function forClose(){
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	