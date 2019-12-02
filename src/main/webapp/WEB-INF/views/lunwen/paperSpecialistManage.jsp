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
	<title>专家管理</title>
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
</head>
<body>
	<div class="main_div"></div>

	<!-- start    查询条件 -->
	<div class="sheach">
		<div class='content_top'>专家管理</div>
		<form id="queryForm" style="margin-bottom: 10px;">

			<input type="hidden" name="selectList"/>

			<label style="margin-left: 20px">专家姓名:</label>
			<input type = "text" id = "name" name = "name" style="width: 100px" class = "inputQuery changeQuery" >

			<label style="margin-left: 20px">研究方向:</label>
			<input type = "text" id = "researchDirection" name = "researchDirection" style="width: 100px" class = "inputQuery changeQuery" >

			<label style="margin-left: 20px">领域：</label>
			<div id="fieldList" style="display: inline">
				<select id = "field" name = "field"   class = "changeQuery changeYear" style="width: 200px">
					<option value = "" selected>请选择</option>
					<c:forEach  var="fieldList"  items="${fieldList}">
						<option value ="${fieldList.FIELD}"}> ${fieldList.FIELD}</option>
					</c:forEach>
				</select>
			</div>
			<br/>
			<label style="margin-left: 20px">单位名称:</label>
			<input type = "text" id = "unitName" name = "unitName" style="width: 100px" class = "inputQuery changeQuery" >

			<label  for="matchStatus" class="yearTitle">匹配状态:</label>
			<select id = "matchStatus" name = "matchStatus"   class = "changeQuery changeYear" style="width: 100px">
				<option value="">不限</option>
				<option value = "0">未匹配</option>
				<option value = "1">已匹配</option>
				<option value = "2">已屏蔽</option>
			</select>
			<!-- 查询按钮  " -->
			<div style="float:right" id = "queryButton" class = "btn query" onclick = "queryAll.query()">搜索</div>
		</form>
	</div>
	<!-- end    查询条件 -->

	<div class="tab" role="tabplanel">
		<div id="funcBtn" style="width:100%;height: 35px;">
			<div class='btn right addButton' onclick="queryAll.addEvent()">新增</div>
			<div class='btn right updateButton' onclick="queryAll.updateEvent()">修改</div>
			<div class='btn right deleteButton' onclick="queryAll.delEvent()" >删除</div>
			<%--<div class='btn right downloadButton' onclick="queryAll.downLoadTemp()">下载模板</div>--%>
			<div class='btn right joinButton' onclick="queryAll.joinEvent()">导入</div>
			<div class='btn right outButton' onclick="queryAll.outEvent()">导出</div>
			<div class='btn right renewalButton' onclick="queryAll.renewal()" >更换专家</div>
		</div>
	</div>

	<div class="tabbable" >
		<div id="datagrid" style="height:450px;margin-bottom: 10px;"></div>
		<div class="tablepage"></div>
	</div>

	<div>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align:right;"></div>
	</div>

	<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
     <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
    <script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.js"></script>
 	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>  	<!-- 弹框.js  --> 
	<!-- 引入datagrid -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
    <!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/js/lunwen/paperSpecialistManage.js"></script>
	<script src="<%=request.getContextPath()%>/js/lunwen/allCheckBox.js"></script>

</body>
</html>