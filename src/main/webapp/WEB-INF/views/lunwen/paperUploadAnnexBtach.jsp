<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>附件上传</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
</head>
<body style="padding-top: 5%">
<div style="margin-left: 3%;margin-bottom: 1%;color:red" >
        上传的压缩包需为zip
		<br/>
        压缩包内上传文件名称格式为：论文题目-材料类别.文件类型
    	<br/>
        压缩包内上传文件类型为：Pdf/jpg/docx/rar/bmp/doc
    </div>

<form id="queryForm" method="post" enctype="multipart/form-data">
	<table class="visitOperate tableStyle specialTable">
		<tr>
			<td>
				<span title = "附件压缩包信息"><b class="mustWrite">*</b>附件压缩包信息</span>
			</td>
			<td class="addInputStyle" colspan="2">
				<div class="controls" class="form-control">
					<input id="file" type="file" name="file" class="validNull"  property="file"
						   content="附件" title="必填项" style="display:inline-block;">
				</div>
			</td>
		</tr>
	</table>
</form>

	<div class="btnContent">
		<button type="button" class="btn" onclick="uploadAnnex.addBatchEvent()">上传</button>
		<button type="button" class="btn" onclick="uploadAnnex.addClose()">返回</button>
	</div>

	<div style="margin-left: 3%">
		<h4 id="successFile" style="color: green"></h4>
		<h4 id="repeatFile" style="color: darkred"></h4>
		<h4 id="errorFile" style="color: red"></h4>
	</div>

	<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script> 
     <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	
    <script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script> 
     
 	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>  	<!-- 弹框.js  --> 
	<!-- 引入datagrid -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
    <!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>

    <!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/js/lunwen/paperManage.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
 	<script src="<%=request.getContextPath()%>/js/lunwen/paperUploadAnnex.js"></script>
</body>

</html>