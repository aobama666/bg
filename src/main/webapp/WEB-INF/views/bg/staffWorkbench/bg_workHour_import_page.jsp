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
	<h5>上传Excel</h5>
	<div class="button-box">
		<button type="button" class="btn btn-danger btn-xs" style="display:none;" id="ErrInfoButton" onclick="downLoadErr(this)"> 工时错误信息下载</button>
		<button type="button" class="btn btn-primary btn-xs" onclick="DIYTemp()"> 定制模板内容</button>
		<button type="button" class="btn btn-primary btn-xs" onclick="downLoadTemp('工时填报模板.xls')"> 模板下载</button>
		<button type="button" class="btn btn-success btn-xs" onclick="uploadProFile()"> 确定</button>
	</div>
</div>
<hr>
<div class="form-box">
	<form action="" id="form1" name="form1" encType="multipart/form-data" method="post" target="hidden_frame">
		<div style="padding: 0px 66px 5px 66px;font-size: 12px;">
			<span>请选择要导入的EXCEL文件</span>&nbsp;<code>注意：文件需为Excel 97~2003格式，后缀名为.xls</code>
		</div>
		<div class="form-group col-xs-12">
			<label for="file" >文件：</label>
			<div class="controls" style="margin-left:110px" class="form-control">
				<input id="file" type="file" name="file" property="file" style="display:inline-block;width:395px">
			</div>
		</div>
		<input type="hidden" name="fileName" id="fileName">
	</form>
	<iframe name='hidden_frame' id="hidden_frame" width="0" height="0"></iframe>
</div>
<div style="float:left;padding:5px;width: 100%;">
	<span>说明：下载模板，按照模板要求填入信息，再导入数据</span>
</div>
</body>
<script type="text/javascript" charset="utf-8">

$(function(){
});

function downLoadTemp(fileName){
	var ran = Math.random()*1000;
	$("#fileName").val(fileName);
	//var fileName = "项目信息模板.xls";
	document.forms[0].action = "<%=request.getContextPath() %>/staffWorkbench/download_excel_temp?ran="+ran;
	document.forms[0].submit();
}

function DIYTemp(){
	parent.layer.open({
		type:2,
		title:"定制模板内容",
		area:['580px', '65%'],
		resize:true,
		scrollbar:true,
		content:['<%=request.getContextPath()%>/staffWorkbench/DIY_temp_page'],
		end: function(){
			document.execCommand("Refresh");
		}
	});
	//forClose();
}

function uploadProFile() {
	var checkResult = $(".form-box").sotoValidate([
		                                     	     {name:'file',vali:'required;checkFileType()'}
		                                     	]);
	if(checkResult){
		loadPage("open");
		var ran = Math.random()*1000;
		/* var filePath=$("#file").val();
		var pos=filePath.lastIndexOf("\\");
		var fileName=filePath.substr(pos+1); */
		document.forms[0].action ="<%=request.getContextPath() %>/staffWorkbench/readWorkHourExcel?&ran="+ran;
		document.forms[0].submit();
		loadPage("close");

        //workingHours();
	}
}

function checkFileType(val){
	var result = {result:true,info:"只能导入 Microsoft Office Excel 97-2003 工作表!"};
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
function initProErrInfo(uuid){
	loadPage("close");
	$("#ErrInfoButton").show().attr("uuid",uuid);
}


function downLoadErr(_this){
	loadPage("open");
	var ran = Math.random()*1000;
	var uuid = $(_this).attr("uuid");
	var fileName = uuid + ".xls";
	$("#fileName").val(fileName);
	document.forms[0].action = "<%=request.getContextPath() %>/staffWorkbench/downloadErrExecl?ran="+ran;
	document.forms[0].submit();
	loadPage("close");
}
function queryList(){
	parent.mmg.load();
}
function forClose(){
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	