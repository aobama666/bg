<!DOCTYPE>
<!-- authentication_add.jsp -->
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

<title>��֤�Ͽ��ļ�</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>

<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->
</head>
<body>
<div class="page-header-sl">
	<h5>��֤�Ͽ��ļ�</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="forSave()"> ����</button>
		<button type="button" class="btn btn-warning btn-xs" onclick="forClose()"> �ر�</button>
	</div>
</div>
<hr>
<div class="form-box">
	<div class="form-group col-xs-6">
		<label for="empName"><font class="glyphicon glyphicon-asterisk required"></font> ��Ա����</label>
		<div class="controls">
			<div id="stuffTree" class="input-group organ bg-white">
				<input type="text" name="empName" property="empName" readonly="true">
				<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-6">
		<label for="empCode">��Ա���</label>
		<div class="controls">
			<input type="text"  name="empCode" property="empCode" readonly="true">
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="documentName"><font class="glyphicon glyphicon-asterisk required"></font> �ļ�����</label>
		<div class="controls">
			<input type="text"  name="documentName" property="documentName">
		</div>
	</div>
	<div class="form-group col-xs-6">
		<label for="documentCode"><font class="glyphicon glyphicon-asterisk required"></font> �ļ����</label>
		<div class="controls">
			<input type="text"  name="documentCode" property="documentCode" >
		</div>
	</div>
	<div class="form-group col-xs-6">
		<label for="publishDate"><font class="glyphicon glyphicon-asterisk required"></font> ��������</label>
		<div class="controls">
			<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
				<input type="text"  name="publishDate" property="publishDate" readonly="true">
				<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
			</div>
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="classification"><font class="glyphicon glyphicon-asterisk required"></font> ����</label>
		<div class="controls">
			<select name="classification" property="classification" >
				<options collection="classList" property="label" labelProperty="value"></options>
			</select>
		</div>
	</div>
	<div class="form-group col-xs-6">
		<label for="documentType"><font class="glyphicon glyphicon-asterisk required"></font> ����</label>
		<div class="controls">
			<select name="documentType" property="documentType" >
				<options collection="typeList" property="label" labelProperty="value"></options>
			</select>
		</div>
	</div>
	<div class="form-group col-xs-6">
		<label for="sortOrder"><font class="glyphicon glyphicon-asterisk required"></font> ����</label>
		<div class="controls">
			<input type="text"  name="sortOrder" property="sortOrder" >
		</div>
	</div>
	<div class="form-group col-xs-12">
		<label for="remark">��ע</label>
		<div class="controls">
			<textarea name="remark" property="remark" rows="3" style="height: 100px;resize: none;"></textarea>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
$(function(){
	$(".form_date").datepicker({autoclose:true,todayHighlight:true,clearBtn:true,orientation:'auto'});
	$("#stuffTree").stuffTree({root:'41000001',empCode:'empCode',empName:'empName',iframe:'parent'});
});

function forSave(){
	var ran = Math.random()*1000000;
	var checkResult = $(".form-box").sotoValidate([
	                                     	      {name:'empName',vali:'required'},
	                                     	      {name:'documentName',vali:'required;length[-50]'},
	                                     	      {name:'documentCode',vali:'required;length[-30];checkUniqueness()'},
	                                     	      {name:'publishDate',vali:'required'},
	                                     	      {name:'documentType',vali:'required'},
	                                     	      {name:'classification',vali:'required'},
	                                     	      {name:'sortOrder',vali:'required;numeric;length[-5]'},
	                                     	      {name:'remark',vali:'length[-100]'}
	                                     	]);
	if(checkResult){
		var param = $(".form-box").sotoCollecter();
		$.ajax({
			type:"POST",
			url:"<%=request.getContextPath()%>/authenticationWeb/ajaxSave.so?ran="+ran,
			data:param,
			success:function(data){
				if(data == "true"){
					parent.layer.msg("����ɹ�!");
					parent.queryList("reload");
					forClose();
				}else {
					parent.layer.msg("����ʧ��!");
				}
			}
		});
	}
}

function checkUniqueness(val){
	var uuid = $("input[name=uuid]").val();
	var empCode = $("input[name=empCode]").val();
	var result = {};
	$.ajax({
		type: 'POST',
		url:'<%=request.getContextPath()%>/authenticationWeb/ajaxCheckUniqueness.so',
		async: false,
		data: {documentCode:val,empCode:empCode,uuid:uuid},
		success:function(data){
			if(data == "true"){
				result.result = true;
				result.info = "";
			}else{
				result.result = false;
				result.info = "��Ա����ϵͳ���Ѵ�����ͬ�ļ���ŵ����ݣ������ظ�¼�룻";
			}
		}
	});
	return result;
}

function forClose(){
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	