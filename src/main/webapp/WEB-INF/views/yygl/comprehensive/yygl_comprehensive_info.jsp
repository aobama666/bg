<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>综合查询</title>
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
	<div class="sheach">
<%--		<div class='content_top'>综合查询</div>	 --%>
		<form id="queryForm" style="margin-bottom: 10px;">
	    	 <input type = "hidden"   id = "applyId" name="applyId">
			<input type = "hidden"   id = "type" name="type" value="${type}">
			<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;申请编码：</label>
			<input type = "text" id = "applyCode" name = "applyCode" style="width: 200px" class = "inputQuery changeQuery"   title="申请编码">
			<label  for="stateDate" class="yearTitle">用印日期：</label>
			<input id="stateDate" name="stateDate"
				   onclick=" WdatePicker({dateFmt:'yyyy-MM-dd',enableInputMask:false})" readonly="true"
				   type="text"
				   class="inputQuery changeQuery Wdate"
				   title="用印日开始期（格式：yyyy-MM-dd）"
				   content="用印开始日期"
				   style="width:150px;"
			/>
			<label >至 </label>
			<input id="endDate" name="endDate"
				   onclick=" WdatePicker({dateFmt:'yyyy-MM-dd',enableInputMask:false})" readonly="true"
				   type="text"
				   class="inputQuery changeQuery Wdate"
				   title="用印结束日期（格式：yyyy-MM-dd）"
				   content="用印结束日期"
				   style="width:150px;"
			/>
			<label  for="itemName" class="yearTitle">用印事项：</label>
				<input readonly="true" type = "text" id = "itemName" name = "itemName" style="  width: 239px"   class = "inputQuery changeQuery"   title="用印事项"   onclick="roomList.forItemInfo()"  >
				<span class="input-itemName-addon"  onclick="roomList.forItemInfo()" ><span class="glyphicon glyphicon-th-list"></span></span>
				<input type = "hidden" id = "itemSecond" name = "itemSecond"   class = "inputQuery changeQuery"   title="用印事项"   >
				<input type = "hidden" id = "itemFirst" name = "itemFirst"   class = "inputQuery changeQuery"   title="用印事项"    >







		<%--<select id = "itemFirst" name = "itemFirst"  title="用印事项一级"  class = "changeQuery userlevel" style="width: 240px"  onchange="changeItemFirst()"   >--%>
				<%--<option value = "">   </option>--%>
				<%--<c:forEach  var="itemFirstList"  items="${itemFirstList}">--%>
					        <%--<option value ="${itemFirstList.K}" title=" ${itemFirstList.V}" > ${itemFirstList.V}</option>--%>
				<%--</c:forEach>--%>
			<%--</select>--%>
			<%--<label  for="itemSecond" class="yearTitle">用印事项二级：</label>--%>
			<%--<select id = "itemSecond" name = "itemSecond" title="用印事项二级"   class = "changeQuery userlevel" style="width: 240px">--%>
				<%--<option value = "">   </option>--%>
			<%--</select>--%>
			</br>
			<label >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;申请单位：</label>
			<select id="deptId"  name = "deptId"  title="申请单位" class = "changeQuery userlevel" style="width: 200px;margin-left: 0px"  >
						<option value=""  ></option>
				<c:forEach  var="deptInfo"  items="${deptInfoList}">
					<option value ="${deptInfo.DEPT_ID}" title=" ${deptInfo.DEPTNAME}" > ${deptInfo.DEPTNAME}</option>
				</c:forEach>

			        	</select>
			<label style="margin-left:20px;">用印事由：</label>
			<input type = "text" id = "useSealReason" title="用印事由" name = "useSealReason" style="width: 323px;" class = "inputQuery changeQuery" >
			<label  for="useSealStatus" class="yearTitle"> 审批状态：</label>
			<select id = "useSealStatus" name = "useSealStatus" title="审批状态"    class = "changeQuery userlevel" style="width: 240px;margin-left: -2px">
				<option value = "">   </option>
				<c:forEach  var="statusInfo"  items="${statusInfoList}">
					<option value ="${statusInfo.CODE}" title=" ${statusInfo.NAME}" > ${statusInfo.NAME}</option>
				</c:forEach>
			</select>
			<!-- 查询按钮  -->
			<div id = "queryButton" class = "btn query" onclick = "roomList.query()" style="margin-left: 20px;">搜索</div> <!-- 原来引用的函数onclick = "roomList.query()" -->
		</form>
	</div>

	<!-- end    查询条件 -->

	<div class='btn right deleteButton' onclick="roomList.expEvent()" style="white-space: nowrap">导出</div>
	<div    style="line-height: 37px">&nbsp;</div>
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
	<!-- end参观详情信息-->
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
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree1.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/js/yygl/comprehensive/comprehensive.js"></script>
	<script src="<%=request.getContextPath()%>/js/lunwen/allCheckBox.js"></script>
</body>
</html>