<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>审批人配置查看</title>
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
<body>
	<div class="main_div"></div>
	<!-- start    查询条件 -->
	<div class="sheach">
<%--		<div class='content_top'>审批人配置</div>--%>
		<form id="queryForm" style="margin-bottom: 10px;">

			<input type = "hidden"   id = "approveId" name="approveId">
			<label  for="approveDeptId" class="yearTitle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;部门名称：</label>
			<select id = "approveDeptId" name = "approveDeptId"   class = "changeQuery userlevel" style="width: 240px"  >
				<option value = "">   </option>
				<c:forEach  var="DeptApprovalList"  items="${DeptApprovalList}">
					<option value ="${DeptApprovalList.approveDeptId}" title=" ${DeptApprovalList.approveDeptName}" > ${DeptApprovalList.approveDeptName}</option>
				</c:forEach>
			</select>
			<label  for="approveUserName" class="yearTitle"> 员工账号：</label>
			<input type = "text" id = "approveUserName" name = "approveUserName" style="width: 237px" class = "inputQuery changeQuery" >

			<label  for="approveUserAlias" class="yearTitle">员工名称：</label>
			<input type = "text" id = "approveUserAlias" name = "approveUserAlias" style="width: 237px" class = "inputQuery changeQuery" >
			<br>
			<label  for="itemName" class="yearTitle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 用印事项：</label>
			<input type = "text" id = "itemName" name = "itemName" style="width: 239px" class = "inputQuery changeQuery"   title="用印事项"  onclick="roomList.forItemInfo()" >
			<span class="input-itemName-addon"  onclick="roomList.forItemInfo()" ><span class="glyphicon glyphicon-th-list"></span></span>
			<input type = "hidden" id = "itemSecond" name = "itemSecondId"   class = "inputQuery changeQuery"   title="用印事项"   >
			<input type = "hidden" id = "itemFirst" name = "itemFirstId"   class = "inputQuery changeQuery"   title="用印事项"    >
			<label  for="approveNodeId" class="yearTitle"> 节点类型：</label>
			<select id = "approveNodeId" name = "approveNodeId"   class = "changeQuery userlevel" style="width: 240px">
				<option value = "">   </option>
				<c:forEach  var="nodeTypeList"  items="${nodeTypeList}">
					<option value ="${nodeTypeList.CODE}" title=" ${nodeTypeList.NAME}" > ${nodeTypeList.NAME}</option>
				</c:forEach>
			</select>
			<!-- 查询按钮  -->
			<div id = "queryButton" class = "btn query" onclick = "roomList.query()" style="margin-left: 20px;">搜索</div>
		</form>
	</div>
	<!-- end    查询条件
	<div class='btn right deleteButton' onclick="roomList.approvalForDelete()" style="white-space: nowrap">删除</div>
	<div class='btn right deleteButton' onclick="roomList.approvalForUpdata()" style="white-space: nowrap">修改</div>
	<div class='btn right deleteButton' onclick="roomList.approvalForSave()" style="white-space: nowrap">新增</div>
	-->
	<div    style="line-height: 5px">&nbsp;</div>
	<!-- start 列表展示 -->
	<div class="tabbable" >
	    <div class="tab-content">
	        <!-- 表格 -->
	        <div class="tab-pane active" >
	            <div id="datagrid"></div>
	            <div class="tablepage"></div>
	        </div>
	    </div>
    </div>
	 
	<!-- end 列表展示 -->
	<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>

	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->

	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
    <!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/js/yygl/configuration/noapproval.js"></script>
</body>
</html>