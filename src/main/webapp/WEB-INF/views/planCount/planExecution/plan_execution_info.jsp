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
	<title>计划统计-计划执行</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
	<!-- easyuicss -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/css/planCount/planInput/planInput.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.textbox{
			width: 13%!important;
			height: 35px!important;
			border: 1px solid #ddd!important;
			margin-left: -4px!important;
			vertical-align: middle!important;
			font-size: 16px!important;
			line-height: 32px!important;
			padding: 2px 0!important;
			padding-bottom: 6px!important;
			cursor: pointer!important;
			border: 1px solid #ddd!important;
		}
		body{
			overflow: hidden;
		}

	</style>
</head>
<body>

<c:choose>
<c:when test="${PRCTR==null}">
        <div class="main_div" style="margin-top: 10px;padding-left: 20px;">
			<span title = " 三年全院综合计划整体执行进度：" style="padding:0;font-size: 18px;vertical-align: top;line-height: 33px;"> 三年全院综合计划整体执行进度：</span>
			<table class="visitOperate tableStyle specialTable" style="width: auto ;display:inline-block;border: none;">
				<tr>
					<c:forEach  var="yearTotalInfo"  items="${yearTotallist}">
						<td   style="padding: 0 10px;" >
							<span title = "${yearTotalInfo.YEAR} "    > ${yearTotalInfo.YEAR}</span>
						</td>
						<td   style="padding: 0 10px;">
							<span title = " ${yearTotalInfo.ITEM_PROGRESS}%">${yearTotalInfo.ITEM_PROGRESS}%</span>
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>
</c:when>

</c:choose>

		<div class="box" style="margin-top: 12px;">
			<div class="box-top"  style="height: 300px" >
				<div   id="yearAndDevelop" class="box-top-left"></div>
				<div   id="costAndCapital" class="box-top-right" ></div>
			</div>
			<div class="sheach">
				<%--<div class='content_top'>执行数据综合维护</div>--%>
				<form id="queryForm" style="margin-bottom: 10px;">
					<input type="hidden" id="pipProgress" name="pipProgress" value="${pipProgress}">
					<input type="hidden" id="sourceOfFunds" name="sourceOfFunds" value="">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年度：</label>
					<select id="year"  name = "year"  title="年度" class = "changeQuery userlevel" style="width: 200px;margin-left: 0px"   >
						<c:forEach  var="yearInfo"  items="${yearList}">
							<option value ="${yearInfo.year}" title=" ${yearInfo.year}" > ${yearInfo.year}</option>
						</c:forEach>
					</select>
					<label  for="specialType" class="yearTitle"> 专项类别：</label>
					<select id = "specialType" name = "specialType" title="专项类别"    class = "changeQuery userlevel" style="width: 240px;margin-left: -2px" onchange="maintainInfo.forFundsSource()"    >
						<option value = ""  > </option>
						<c:forEach  var="specialInfo"  items="${specialList}">
							<option value ="${specialInfo.SPECIAL_CODE}" title=" ${specialInfo.SPECIAL_NAME}" > ${specialInfo.SPECIAL_NAME}</option>
						</c:forEach>
					</select>
					<label  for="sourceOfFundsNew" class="yearTitle"> 资金来源：</label>
					<input class="inputQuery changeQuery tree-data" style="width: 200px"   id="sourceOfFundsNew" name="sourceOfFundsNew"  data-companyLeaderName=""       title="资金来源  " />
					<c:choose>
					    <c:when test="${PRCTR==null}">
							<label  for="commitmentUnit" class="yearTitle"> 承担单位：</label>
							<select id = "commitmentUnit" name = "commitmentUnit" title="承担单位"    class = "changeQuery userlevel" style="width: 240px;margin-left: -2px">
								<option value = "">   </option>
								<c:forEach  var="commitmentUnitInfo"  items="${commitmentUnitList}">
									<option value ="${commitmentUnitInfo.PROFIT_CENTER_CODE}" title=" ${commitmentUnitInfo.PROFIT_CENTER_DEATIL}" > ${commitmentUnitInfo.PROFIT_CENTER_DEATIL}</option>
								</c:forEach>
							</select>
						</c:when>
						<c:otherwise>
							<input type="hidden" id="commitmentUnit" name="commitmentUnit"  >
						</c:otherwise>
					</c:choose>
					<!-- 查询按钮  -->
					<div id = "queryButton" class = "btn query" onclick = "executionList.query()" style="margin-left: 20px;">搜索</div> <!-- 原来引用的函数onclick = "roomList.query()" -->
					<div  class = "btn query" onclick="roomList.expEvent()" style="margin-left: 20px;">导出</div>
				</form>
			</div>

			<!-- end    查询条件 -->

			<%--<div class='btn right deleteButton' onclick="roomList.expEvent()" style="white-space: nowrap">导出</div>--%>
			<div  style="line-height: 37px">单位：万元</div>
			<!-- start 列表展示 -->
			<div class="tabbable"   >
				<div class="tab-content">
					<!-- 表格 -->
					<div class="tab-pane active" >
						<div id="datagrid"></div>
						<div class="tablepage"></div>
					</div>
				</div>
			</div>
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
<!-- 验证校验公共方法，提示框公共方法 -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
<!-- 本页面所需的js-->

<script src="<%=request.getContextPath()%>/js/echarts/echarts.min.js"></script>
<script src="<%=request.getContextPath()%>/js/planCount/planExecute/planExecute.js"></script>
<script src="<%=request.getContextPath()%>/js/planCount/planExecute/maintain.js"></script>
</html>