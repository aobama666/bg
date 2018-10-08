<!DOCTYPE>
<!-- authentication_import_excel_page.jsp -->
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

<title>�ϴ�Excel</title>
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

<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->
</head>
<body>
<div class="page-header-sl">
	<h5>�ϴ�Excel</h5>
	<div class="button-box">
		<button type="button" class="btn btn-danger btn-xs" style="display:none;" id="errInfoButton" onclick="downLoadErr(this)"> ������Ϣ����</button>
		<button type="button" class="btn btn-primary btn-xs" onclick="downLoadTemp()"> ģ������</button>
		<button type="button" class="btn btn-success btn-xs" onclick="uploadFile()"> ȷ��</button>
		<button type="button" class="btn btn-warning btn-xs" onclick="forClose()"> �ر�</button>
	</div>
</div>
<hr>
<div class="form-box">
	<form action="" id="form1" name="form1" encType="multipart/form-data" method="post" target="hidden_frame">
	<div style="padding: 0px 66px 5px 66px;font-size: 12px;">
		<span>��ѡ��Ҫ�����EXCEL�ļ�</span>&nbsp;<code>ע�⣺�ļ���ΪExcel 97~2003��ʽ����׺��Ϊ.xls</code>
	</div>
	<div class="form-group col-xs-12">
		<label for="empName" style="width:40px;margin-top:5px">�ļ�</label>
		<div class="controls" style="margin-left:50px">
			<input type="file" name="file" property="file">
		</div>
	</div>
	</form>
	<iframe name='hidden_frame' id="hidden_frame" width="0" height="0"></iframe>
</div>
<div style="float:left;padding:5px;width: 100%;">
	<span>˵��������ģ�壬����ģ��Ҫ��������Ϣ���ٵ�������</span>
</div>
</body>
<script type="text/javascript">

$(function(){
});

function downLoadTemp(){
	var ran = Math.random()*1000;
	var fileName = "��֤�Ͽ��ļ�-��Ϣ¼��ģ��.xls";
	document.forms[0].action = "<%=request.getContextPath() %>/cepriCommonWebc/downloadExcelTemp.so?fileName="+fileName+"&ran="+ran;
	document.forms[0].submit();
}

function uploadFile() {
	var checkResult = $(".form-box").sotoValidate([
		                                     	      {name:'file',vali:'required;checkFileType()'}
		                                     	]);
	if(checkResult){
		loadPage("open");
		var ran = Math.random()*1000;
		document.forms[0].action = "<%=request.getContextPath() %>/authenticationWeb/readItemsExcel.so?ran"+ran;
		document.forms[0].submit();
	}
}
function checkFileType(val){
	var result = {result:true,info:"ֻ�ܵ��� Microsoft Office Excel 97-2003 ������!"};
	var fileType=/\.[^\.]+$/.exec(val);
	if(val != "" && (fileType+"").toLowerCase() != ".xls"){
		result.result = false;
	}
	return result;
}
var loadPageIndex = 0;
function loadPage(state){
	if(state == "open"){
		loadPageIndex = layer.load(1, {shade: [0.2, '#fff']});
	}else if(state == "close"){
		layer.close(loadPageIndex);
	}
}
function initErrInfo(fileName){
	loadPage("close");
	$("#errInfoButton").show().attr("uuid",fileName);
}
function downLoadErr(_this){
	var uuid = $(_this).attr("uuid");
	var ran = Math.random()*1000;
	var fileName = uuid + ".xls";
	var excelName = "��֤�Ͽ��ļ��ϴ�������Ϣ";
	document.forms[0].action = "<%=request.getContextPath() %>/crpri_common/commImportExcelWeb/downloadErrInfo.so?fileName="+fileName+"&excelName="+excelName+"&ran="+ran;
	document.forms[0].submit();
}
function queryList(){
	parent.queryList("reload");
}
function forClose(){
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	