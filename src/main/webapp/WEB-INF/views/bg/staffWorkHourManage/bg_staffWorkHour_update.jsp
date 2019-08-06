<!DOCTYPE>
<!-- authentication_add.jsp -->
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>修改项目信息</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css"
	media="screen">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath()%>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->
<style type="text/css">
.white_bg{
	background-color: #fff !important;
}
</style>
</head>
<body>
	<input type="hidden" id="whId" value="${ID}"/>
	<input type="hidden" id="approver" value="${APPROVER}"/>
	<%-- <c:out value="${ID}"></c:out> --%>
	<div class="page-header-sl">
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs"
				onclick="forSave()">保存</button>
			<button type="button" class="btn btn-success btn-xs"
				onclick="forSubmit()">提交</button>
		</div>
	</div>
	<hr>
	<div class="form-box">
		<%-- <c:out value="${proUsers}"></c:out> --%>
		<div class="form-group col-xs-12">
			<label for="empName">人员姓名：</label>
			<div class="controls">
				<input type="text" disabled name="empName" property="empName" value="${EMPNAME}">
			</div>
		</div>
		<div class="form-group col-xs-12">
			<label for="hrCode">人员编号：</label>
			<div class="controls">
				<input type="text" id="hrCode" disabled name="hrCode" property="hrCode" value="${HRCODE}">
			</div>
		</div>
		<div class="form-group col-xs-12">
			<label for="date">日期：</label>
			<div class="controls">
				<input type="text" disabled id="date" name="date" property="date" value="${WORK_DATE}">
			</div>
		</div>
		<div class="form-group col-xs-12">
			<label for="projectName">类型：</label>
			<div class="controls">
				<input type="text" disabled name="category" property="category" value="${CATEGORY}">
			</div>
		</div>
		<div class="form-group col-xs-12">
			<label for="projectName">任务名称：</label>
			<div class="controls">
				<c:choose>
					<c:when test="${CATEGORY=='非项目工作'}">
						<input type="text" id="projectName" name="projectName" property="projectName" value="${PROJECT_NAME}">
					</c:when>
					<c:otherwise>
						<input type="text" id="projectName" disabled name="projectName" property="projectName" value="${PROJECT_NAME}">
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="form-group col-xs-12">
			<label for="jobContent">工作内容简述：</label>
			<div class="controls">
				<textarea name="jobContent" class="" id="jobContent" onblur="checkInput(this)" property="jobContent" style="height:75px">${JOB_CONTENT}</textarea>
			</div>
		</div>
		<div class="form-group col-xs-12">
			<div style="float: left">
				<label for="workHour">投入工时(h)：</label>
				<div class="controls">
					<input type="text" style="width: 150px" id="workHour" name="workHour" onblur="checkInput(this)" property="workHour" value="${WORKING_HOUR}">
					<input type="hidden" id="workHourOld" name="workHourOld" property="workHourOld" value="${WORKING_HOUR}">
				</div>
			</div>
			<div style="padding-left: 290px">
				<span style="line-height: 26px">月度工时/已填报工时（h）：</span>
				<span id="fillSumKQ">${fillSumKQ}</span>
				<span>/</span>
				<span id="fillSum">${fillSum}</span>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		$(".form_date").datepicker({
			autoclose:true,
			orientation:'auto',
			language: 'cn',
			format: 'yyyy-mm-dd',
			todayHighlight:true,
			startDate:new Date()});		
		$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'self',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'});
	}); 
	
	//输入正确性校验
	function checkInput(_this){
		var checkResult =$(_this).parent().sotoValidate([
	                         	      {name:'jobContent',vali:'length[-200]'},
	                         	      {name:'workHour',vali:'checkNumberFormat();'}
	                              ]);
		if(!checkResult){
			//TODO
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

	//保存
	function forSave(){
		var ran = Math.random()*100000000;
		//校验逻辑
		var checkResult;
		checkResult =$(".form-box").sotoValidate([
									  {name:'projectName',vali:'length[-50]'},
                               	      {name:'jobContent',vali:'length[-200]'},//required;暂不做工作内容必填校验
                               	      {name:'workHour',vali:'required;checkNumberFormat()'}
	                               ]);
		
		if(!checkResult){
			layer.msg("您的填写有误，请检查");
			return;
		}

        //验证累计工时是否超过月度工时
        var fillSum = document.getElementById("fillSum").innerHTML;
        var fillSumKQ = document.getElementById("fillSumKQ").innerHTML;
        var workHour = $("input[name=workHour]").val();
        var workHourOld = $("input[name=workHourOld]").val();
        fillSum = Number(fillSum)-Number(workHourOld)+Number(workHour);
        if (fillSumKQ<fillSum){
            layer.msg("填报工时已超出月度工时，请检查");
            return;
        }

		var params={};
		params["id"] =  $.trim($("#whId").val());
		params["projectName"] =  $.trim($("#projectName").val());
		params["workHour"] =  $.trim($("#workHour").val());
		params["jobContent"] =  $.trim($("#jobContent").val());
		
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath() %>/staffWorkingHourManage/saveWorkinghourInfo?ran="+ran,
			data:params,
			dataType:'json',
			success:function(data){
				if(data.result == "success"){
					parent.layer.msg(data.hint);
					parent.mmg.load();
					forClose();
				}else{
					 layer.msg(data.hint);
				}
			}
		});
			
	}
	
	//提交
	function forSubmit(){
		parent.layer.confirm('确认提交吗？', {icon: 7,title:'提示',shift:-1}, function(index){
			parent.layer.close(index);
			var ran = Math.random()*100000000;
			//校验逻辑
			var paramArr =new Array();
			var checkResult;
			checkResult =$(".form-box").sotoValidate([
										  {name:'projectName',vali:'length[-50]'},
	                               	      {name:'jobContent',vali:'length[-200]'},//required;暂不做工作内容必填校验
	                               	      {name:'workHour',vali:'required;checkNumberFormat()'}
		                               ]);
			
			if(!checkResult){
				//layer.msg("您的填写有误，请检查");
				return;
			}

            //验证累计工时是否超过月度工时
            var fillSum = document.getElementById("fillSum").innerHTML;
            var fillSumKQ = document.getElementById("fillSumKQ").innerHTML;
            var workHour = $("input[name=workHour]").val();
            var workHourOld = $("input[name=workHourOld]").val();
            fillSum = Number(fillSum)-Number(workHourOld)+Number(workHour);
            if (fillSumKQ<fillSum){
                layer.msg("填报工时已超出月度工时，请检查");
                return;
            }

			var paramArr =new Array();
			var params={};
			params["id"] =  $.trim($("#whId").val());
			params["hrCode"] =  $.trim($("#hrCode").val());
			params["approver"] =  $.trim($("#approver").val());
			params["date"] =  $.trim($("#date").val());
			params["projectName"] =  $.trim($("#projectName").val());
			params["workHour"] =  $.trim($("#workHour").val());
			params["jobContent"] =  $.trim($("#jobContent").val());
			
			paramArr.push(JSON.stringify(params));
			//校验通过，保存
			$.ajax({
				type: 'POST',
				url:'<%=request.getContextPath()%>/staffWorkingHourManage/submitWorkHourInfo?ran='+ran,
				data:{jsonStr:"["+paramArr.toString()+"]"},
				dataType:'json',
				success : function(data) {
					if(data.hint=='success'){
						parent.layer.msg("提交成功！");
						parent.mmg.load();
						forClose();
					}else{
						layer.msg(data.hint);
					}
				}
			});
		});
	}
 	
	function forClose() {
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}
	
	
</script>
</html>
