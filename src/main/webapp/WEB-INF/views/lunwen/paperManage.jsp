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
	<title>论文管理</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<style>
		.checkkk{
			color:white;
			background: #00828a;
		}
		.paperTypeTab{
			height: 50px;
		}
		.paperTypeTab li{
			float:left;
			display:block;
			list-style: none;
			height: 40px;
			width: 80px;
			text-align: center;
			font-size: 15px;
			padding-top: 7px;
			border:1px solid #ccc;
			border-bottom: 0;
			border-left: 0;
		}
        .datagrid-header-row .datagrid-cell span{
            white-space: normal !important;
            word-wrap: normal !important;
        }
	</style>
</head>
<body>
	<div class="main_div"></div>


	<!-- start    查询条件 -->
	<div class="sheach">
		<div class='content_top'>论文管理</div>
		<form id="queryForm" style="margin-bottom: 10px;">
			<input type="hidden" name="selectList"/>
					<label for="year" style="margin-left: 20px">年度：</label>
					<select id = "year" name = "year"   class = "changeQuery changeYear" style="width: 90px" onchange="paperList.changeFieldList()">
						<%--<option value = "${nowYear}"  selected>${nowYear}</option>--%>
						<c:forEach  var="yearList"  items="${yearList}">
							<option value ="${yearList.YEAR}"}> ${yearList.YEAR}</option>
						</c:forEach>
					</select>
					<label  for="allStatus">论文状态：</label>
					<select id = "allStatus" name = "allStatus"   class = "changeQuery changeYear" style="width: 100px">
						<option value = "" selected>请选择</option>
						<c:forEach  var="allStatus"  items="${allStatus}">
							<option value ="${allStatus.K}"}> ${allStatus.V}</option>
						</c:forEach>
					</select>
					<label>编号：</label>
					<input type = "text" id = "paperId" name = "paperId" style="width: 100px" class = "inputQuery changeQuery" >
					<label style="margin-left: 44px;margin-right: 4px">领域：</label>
					<div id="fieldList" style="display: inline">
						<select id = "field" name = "field"   class = "changeQuery changeYear" style="width: 200px">
							<option value = "" selected>请选择</option>
							<c:forEach  var="fieldList"  items="${fieldList}">
								<option value ="${fieldList.FIELD}"}> ${fieldList.FIELD}</option>
							</c:forEach>
						</select>
					</div>
			<br/>
				<label style="margin-left: 20px">论文题目：</label>
				<input type = "text" id = "paperName" name = "paperName" style="width: 248px" class = "inputQuery changeQuery" >
				<label style="margin-left: 14px">作者：</label>
				<input type = "text" id = "author" name = "author" style="width: 100px" class = "inputQuery changeQuery" >
				<label style="margin-left: 15px">作者单位：</label>
				<input type = "text" id = "unit" name = "unit" style="width: 100px" class = "inputQuery changeQuery" >
				<input type="text" id="paperType" name="paperType" style="display: none" value="1">
				<!-- 查询按钮  " -->
				<div style="float:right" id = "queryButton" class = "btn query" onclick = "paperList.query()">搜索</div>
		</form>
	</div>
	<!-- end    查询条件 -->

	<%--<hr/>--%>
	<!-- start 列表展示 -->

	<div>
		<!--  新增  修改  删除 功能按钮 -->
		<div id="funcBtn" style="height: 35px;float:right">
			<%--<div style="margin-left: 5px;" class='btn right ' onclick="paperList.downLoadTempLate()" >下载模板</div>--%>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.batchUploadOperation()" >附件批量导入</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.jumpImport()" >导入</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.exportExcel()" >导出</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.delEvent()" >删除</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.updateOperation()" >修改</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.addOperation()" >新增</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.automaticMatch()" >自动匹配</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.withdrawScoreTable()" >撤回打分表</div>
			<div style="margin-left: 5px;" class='btn right ' onclick="paperList.generateScoreTable()" >生成打分表</div>
		</div>
		<!-- 学术技术综述分类按钮 -->
		<div class="paperTypeTab">
			<ul>
				<li style="border-left: 1px solid #ccc" class="checkkk" onclick="paperList.updatePaperType(1)">学术类</li>
				<li onclick="paperList.updatePaperType(2)">技术类</li>
				<li onclick="paperList.updatePaperType(3)">综述类</li>
			</ul>
		</div>
	</div>
	<div class="tabbable active" >
		<div id="datagrid" style=""></div>
		<div class="tablepage"></div>
	</div>
	<!-- end 列表展示 -->

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
		<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
 	<script src="<%=request.getContextPath()%>/js/lunwen/paperManage.js"></script>
 	<script src="<%=request.getContextPath()%>/js/lunwen/allCheckBox.js"></script>
</body>

</html>