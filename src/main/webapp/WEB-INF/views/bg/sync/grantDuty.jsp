<!-- http://localhost/bg/organstufftree/demo -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>

<title>专责授权</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
</head>
<body>
<div class="page-header-sl">
	<h5>专责授权</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="forSubmit()"> 提交</button>
	</div>
</div>
<hr>
<div class="form-box">
	<div class="form-group col-xs-12">
		<label for="empName"><font class="glyphicon glyphicon-asterisk required"></font>员工</label>
		<div class="controls">
			<div id="stuffTree" class="input-group organ bg-white">
				<input name="empName" id="empName" readonly="readonly"/>
				<input name="empCode" id="empCode" type="hidden"/>
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>	
	<div class="form-group col-xs-12">
		<label for="deptName"><font class="glyphicon glyphicon-asterisk required"></font>组织</label>
		<div class="controls">
			<div id="organTree" class="input-group organ bg-white">
				<input type="text" name="deptName" id="deptName" readonly="readonly">
				<input type="hidden" name="deptCode" id="deptCode">
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="roleCode"><font class="glyphicon glyphicon-asterisk required"></font>角色</label>
		<div class="controls">
			<div class="input-group organ bg-white">
				<select id="roleCode" name="roleCode" property="roleCode">
					<option><option/>
					<option value="MANAGER_UNIT">院专责</option>
					<option value="MANAGER_DEPT">部门专责</option>
					<option value="MANAGER_LAB">处室专责</option>
					<option value="MANAGER_KJB">科技部专责</option>
				</select>
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
$(function(){	
	$("#stuffTree").stuffTree({root:'41000001',empCode:'empCode',empName:'empName',iframe:'self',checkType:'radio'});
	$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio'});
});
function forSubmit(){
	var ran = Math.random()*1000000;
	$.ajax({
		type:"POST",
		url:"<%=request.getContextPath()%>/sync/addDuty?ran="+ran,
		data:{empCode:$("#empCode").val(),deptCode:$("#deptCode").val(),roleCode:$("#roleCode").val()},
		dataType:'text',
		success:function(data){
			layer.msg(data);
		}
	});
}
 
function forClose(){
	layer.msg("自定义关闭！");
	return ;
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	