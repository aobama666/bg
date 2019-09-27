<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>添加审批权限</title>
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
	src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>

<style type="text/css">
	.italic{
		color:#999;
		font-style:italic;
	}
</style>

</head>
<body>
	<div class="page-header-sl">
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs"
				onclick="forSubmit()">保存</button>
			<!-- <button type="button" class="btn btn-warning btn-xs"
				onclick="forClose()">关闭</button> -->
		</div>
	</div>
	<hr>
	<div class="form-box">
		<div class="form-group col-xs-11">
			<label for="projectName">人员</label>
			<div class="controls">
				<div id="stuffTree" class="input-group organ">
					<input type="hidden" name="empCode" id="empCode" value="">
					<input type="text" name="empName" id="empName" readonly="readonly">
					<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-11">
			<label for=""category"">组织</label>
			<div class="controls">
				<div id="organTree" class="input-group organ">
					<input type="hidden" name="deptCode" id="deptCode" value="">
					<input type="text" name="deptName" id="deptName" readonly="readonly">
					<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-11" id="projectNumber">
			<label for="projectNumber">角色</label>
			<div class="controls">
				<select id="roleCode" name="roleCode" property="roleCode">
						<option></option>
						<option value="SUT1">院领导</option>
						<option value="SUT2">分管院领导</option>
						<option value="SUT3">院助理副总师</option>
						<option value="SUT4">部门行政正职</option>
						<option value="SUT5">部门副职</option>
						<option value="SUT6">部门助理副总师</option>
						<option value="SUT7">技术专家</option>
						<option value="SUT8">处室正职</option>
						<option value="SUT9">处室副职</option>
						<option value="SUT10">专业通道员工</option>
				</select>
			</div>
		</div>	
	</div>
</body>
<script type="text/javascript">
	var level;

	$(function(){
		$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'self',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'});
		$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio',popEvent:'pop'});
	}); 

	function forSubmit(){
		var empCode = $("#empCode").val();
		var deptCode = $("#deptCode").val();
		var roleCode = $("#roleCode").val();
		var validator=[
	              	      {name:'empName',vali:'required'},
	             	      {name:'deptName',vali:'required'},
	             	      {name:'roleCode',vali:'required'}
	             	];
		var checkResult = $(".form-box").sotoValidate(validator);
		if(!checkResult){
			layer.msg("缺少必填项！");
			return;
		}
		
		/* if( (level==0 && (roleCode!='MANAGER_UNIT' && roleCode!='MANAGER_KJB')) ||
			(level==1 && roleCode!='MANAGER_DEPT') ||
			(level==2 && roleCode!='MANAGER_LAB')){
			
			layer.msg("组织类型和角色不匹配！");			
			return;
		} */

		$.post("<%=request.getContextPath()%>/approver/addApprover",
				{ empCode: empCode, deptCode: deptCode, roleCode : roleCode},
				function(data){
					if(data.success=="true"){
						parent.queryList("reload");
						forClose();
					}
					parent.layer.msg(data.msg);
				}
		);
	}
	
	//回调方法
	function popEvent(ids,codes,texts,pId,level){
		this.level = level;
	}
	
	function forClose(){
		parent.layer.close(parent.layer.getFrameIndex(window.name));
	}
</script>
</html>
