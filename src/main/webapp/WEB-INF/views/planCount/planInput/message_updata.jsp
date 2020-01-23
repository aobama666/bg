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
	<title>计划统计-股权投资投入数据维护-维护</title>
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

<div class="contentBox   Remark">
	<div class="btnBox"  style="margin-top: 14px;     position: relative;">
		<table class="visitOperate tableStyle specialTable" style="width: 90%;">
			<tr>
				<td style="width: 145px">
					<span title = " 年度"> 年度</span>
				</td>
				<td class="addInputStyle" style="width: 200px">
					<input type = "hidden"   id = "id" name="id"  value="${ID}">
					<input type="text"  id="year"  name="year"   value="${YEAR}"  disabled />
				</td>
			</tr>
			<tr>
				<td style="width: 145px">
					<span title = " 计划投入金额(万元)"> 计划投入金额(万元)</span>
				</td>
				<td class="addInputStyle" style="width: 200px">
					<input type="text"  id="planAmount"  name="planAmount"   value="${PLAN_AMOUNT}"  content="计划投入金额" title="必填项"    class = "validNull validNum"  />
				</td>
			</tr>
			<tr>
				<td style="width: 145px">
					<span title = " 计划项目数"> 计划项目数</span>
				</td>
				<td class="addInputStyle" style="width: 200px">
					<input type="text"  id="itemNumber"  name="itemNumber"   value="${ITEM_NUMBER}"  content="计划项目数" title="必填项"   class = "validNull posiviceNum"   />
				</td>
			</tr>
		</table>
	</div>
</div>
<div class="btnContent">
	<button type="button" class="btn" onclick="messageUpdata()">确认</button>
	<button type="button" class="btn" onclick="Resign()">取消</button>
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
<script src="<%=request.getContextPath()%>/js/planCount/planInput/maintain.js"></script>
</html>
