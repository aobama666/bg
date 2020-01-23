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

	<title>计划统计-计划投入</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/css/planCount/planInput/planInput.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.textbox{
			width: 20%!important;
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

	</style>
</head>
<body>

<div class="sheach">
		<div class="box">
			<div class="box-top" style="height:45%;">
				<div   class="box-top-left" style=" background-color: #D5E7E7;">
					<table  style="width:100%;height: 100%; ">
						<tr style="width:100%;height: 10%; ">
							<td>

										<label style="font-size: 20px;"> 近三年发展投入趋势</label ><br>
										<label > 综合计划发展投入：</label>
										<select id = "developSpecialType" name = "developSpecialType" title="专项类别"    class = " userlevel" style="width: 240px;margin-left: -2px"    >
											<option value = ""  >发展总投入</option>
											<c:forEach  var="specialInfo"  items="${specialList}">
												<option value ="${specialInfo.SPECIAL_CODE}" title=" ${specialInfo.SPECIAL_NAME}" > ${specialInfo.SPECIAL_NAME}</option>
											</c:forEach>
										</select>

							</td>
						</tr>
						<tr  style="width:100%;height: 90%;">
							<td style=" background: #fcfdfd !important;height: 80%;">
								<div id="yearAndDevelop"   style="width:100%;height: 90%;"></div>
							</td>
						</tr>
					</table>
				</div>
				<div     class="box-top-right" style=" background-color: #D5E7E7;">
					<table  style="width:100%;height: 100%; ">
						<tr style="width:100%;height: 10%; ">
							<td>
								<form id="queryForm" style="padding-top: 0px;">
										<label style="font-size: 20px;"> 各专项年度投入情况</label ><br>
										<label > 年份：</label>
										<select id="year"  name = "year"  title="年度" class = "changeQuery userlevel" style="width: 200px;margin-left: 0px"   onclick="roomList.selectforYearToSpecialType()" >
											<c:forEach  var="yearInfo"  items="${yearList}">
												<option value ="${yearInfo.year}" title=" ${yearInfo.year}" > ${yearInfo.year}</option>
											</c:forEach>
										</select>
										<label   class="yearTitle"> 专项类别：</label>
									     <input type="hidden" id="specialType" name="specialType" value=""　   　>
								    	 <input class="inputQuery changeQuery tree-data" style="width: 300px"   id="specialTypeNew" name="specialTypeNew"  data-companyLeaderName=""  />
									     <div id = "queryButton" class = "btn query" onclick = "roomList.query()" style="margin-left: 20px;">搜索</div>
								</form>
							</td>
						</tr>
						<tr  style="width:100%;height: 90%; " >
							<td style=" background: #fcfdfd !important;height: 80%;">
								<div id="yearAndItem"   style="width:100%;height: 90%;"></div>
							</td>
						</tr>
					</table>

				</div>
			</div>
			<div class="box-center" style="height:45%;">
				<div  class="box-center-left" style=" background-color: #D5E7E7;">
					<table  style="width:100%;height: 100%; ">
						<tr style="width:100%;height: 10%; ">
							<td>
										<label style="font-size: 20px;"> 资本性与成本性投入趋势</label ><br>
										<label >  </label>
										<%--<select id = "costSpecialType" name = "costSpecialType" title="专项类别"    class = "  userlevel" style="width: 240px;margin-left: -2px" onchange="maintainInfo.forFundsSource()"    >--%>
											<%--<option value = ""  > </option>--%>
											<%--<c:forEach  var="specialInfo"  items="${specialList}">--%>
												<%--<option value ="${specialInfo.SPECIAL_CODE}" title=" ${specialInfo.SPECIAL_NAME}" > ${specialInfo.SPECIAL_NAME}</option>--%>
											<%--</c:forEach>--%>
										<%--</select>--%>
							</td>
						</tr>
						<tr  style="width:100%;height: 90%;">
							<td style=" background: #fcfdfd !important;height: 80%;">
								<div id="costAndCapital"   style="width:100%;height: 90%;"></div>
							</td>
						</tr>
					</table>
				</div>
				<div class="box-center-right  " style=" background-color: #D5E7E7;">
					<table  style="width:100%;height: 100%; ">
						<tr style="width:100%;height: 10%; ">
							<td>
								<label style="font-size: 20px;"> 各单位投资效率效益情况</label ><br>
								<label >（图表目前展示2019实际数据，具体功能待建设）  </label>
							</td>
						</tr>
						<tr  style="width:100%;height: 90%;">
							<td style=" background: #fcfdfd !important;height: 80%;">
								<div id="unitAndefficiency"   style="width:100%;height: 90%;"></div>
							</td>
						</tr>
					</table>

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
<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree1.js"></script>
<!-- 验证校验公共方法，提示框公共方法 -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
<script src="<%=request.getContextPath()%>/js/echarts/echarts3.0.js"></script>
<script src="<%=request.getContextPath()%>/js/planCount/planInput/planInput.js"></script>
</html>