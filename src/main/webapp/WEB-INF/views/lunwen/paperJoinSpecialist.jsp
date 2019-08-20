<!DOCTYPE>
<!-- authentication_import_excel_page.jsp -->
<%--<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>--%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%--<%@page import="java.util.List"%>--%>
<%--<%@page import="java.util.Map"%>--%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>

<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>批量录入</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>

	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.css">
	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">

	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoValidate/sotoValidate.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>

</head>
<body style="background-color: white">
<div class="page-header-sl">
	<h5>上传Excel</h5>
	<div class="button-box">
		<button type="button" class="btn btn-danger btn-xs" style="display:none;background-color:#c9302c" id="ErrInfoButton" onclick="downLoadErr(this)"> 专家导入错误信息下载</button>
		<button type="button" class="btn btn-primary btn-xs" onclick="downLoadTemp()"> 下载模板</button>
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
<div style="float:left;padding:10px;width: 100%;">
	<span>说明：下载模板，按照模板要求填入信息，再导入数据</span>
</div>
</body>
<script type="text/javascript" charset="utf-8">

$(function(){
});

/*导入*/
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
        document.forms[0].action ="<%=request.getContextPath() %>/expert/joinExcel?&ran="+ran;
        document.forms[0].submit();
        loadPage("close");
    }
}
/*错误信息下载*/
function downLoadErr(_this){
    loadPage("open");
    var ran = Math.random()*1000;
    var uuid = $(_this).attr("uuid");
    var fileName = uuid + ".xls";
    $("#fileName").val(fileName);
    document.forms[0].action = "<%=request.getContextPath() %>/expert/specialistErrExecl?ran="+ran;
    document.forms[0].submit();
    loadPage("close");
}

/*模板下载*/
function downLoadTemp(fileName){
    var ran = Math.random()*1000;
    $("#fileName").val(fileName);
    //var fileName = "项目信息模板.xls";
    document.forms[0].action = "<%=request.getContextPath() %>/expert/downloadExcelTemp?ran="+ran;
    document.forms[0].submit();
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

function queryList(){
	parent.mmg.load();
}
function forClose(){
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	