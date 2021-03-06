<%@page import="com.sgcc.bg.common.VersionUtils"%>
<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>计划统计-基建类执行数据维护-新增项目节点</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">

</head>
<body>
<div class="main_div"  style="height: 20px"></div>
<!-- end  头部 -->
<span  style="color:red;margin:5px 0;display: inline-block;">        </span>
<!-- 参观申请单位信息展示 -->
<input type = "hidden"   id = "applyId" name="applyId"  value="${applyId}">
<div class="contentBox   Remark">
	<div class="btnBox"  style="margin-top: 14px;     position: relative;">
		<table class="visitOperate tableStyle specialTable" style="width: 95%;">
			<tr>
				<td style="width: 120px">
					<span title = " 项目名称"> 项目名称</span>
				</td>
				<td class="addInputStyle" style="width: 250px">
					<input type="hidden" id="projectId" name="projectId" value="${projectCode}">
					<input type="hidden" id="specialType" name="specialType" value="${specialType}">
					<input type="hidden" id="year" name="year" value="${year}">
					<input type="text"  id="projectName"  name="projectName"   value="${projectName}" disabled/>
				</td>
			</tr>
			<tr>
				<td style="width: 120px">
					<span title = " 项目节点名称"> 项目节点名称</span>
				</td>
				<td class="addInputStyle" style="width: 250px">
					<input type="text"  id="nodeName"  name="nodeName"   content="项目节点名称" title="必填项，中文或英文,字段长度不能超过20个字"    class = "validNull "  len="20"   />
				</td>
			</tr>
			<tr>
				<td style="width: 120px">
					<span title = " 形象进度"> 形象进度</span>
				</td>
				<td class="addInputStyle" style="width: 250px">
					<input type="text"  id="imageProgress"  name="imageProgress"   content="形象进度" title="必填项"    class = "validNull validPer"    />
				</td>
			</tr>
		</table>
	</div>
</div>
	<div class="btnContent">
		<button type="button" class="btn" onclick="roomList.nodeSave()">确认</button>
		<button type="button" class="btn" onclick="roomList.nodeResign()">返回</button>
	</div>
</body>
<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>

<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->

<!-- 引入日期选择框 -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree1.js"></script>
<!-- 验证校验公共方法，提示框公共方法 -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
<!-- 本页面所需的js -->
<script src="<%=request.getContextPath()%>/js/planCount/planExecute/capitalVisualProgress.js"></script>
</html>
