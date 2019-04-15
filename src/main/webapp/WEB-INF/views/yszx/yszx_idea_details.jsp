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
	<title>演示中心管理详情页</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
<%-- 	<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css"> --%>
	<link href="<%=request.getContextPath()%>/yszx/css/laboratory/roomList.css" rel="stylesheet" type="text/css">
	<!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/laboratory/easyui.css" rel="stylesheet"/>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.min.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<%-- <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/js/jquery-tool.datagrid.js"></script>  --%>
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/common/recommonedCommon.js"></script>
	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/yszx/js/laboratory/roomList.js"></script>
</head>
<body>
	<div class="main_div"></div>
	<input type = "hidden" value = ${filter} id = "filter" >  
	<!-- start  头部 -->
	<div class="sheach details">
		<div class='content_top'>参观设定详情</div>	 
		<button class="btn right backBtn" onclick="resignChange()">返回</button>
	</div>
	<!-- end  头部 -->
	
	<!-- start参观详情信息 -->
	
	<!-- 参观申请单位信息展示 -->
	<table class="visitOperate tableStyle">
		<tr>
			<td>申请部门（单位）</td>
			<td colspan="3">信息中心</td>
		</tr>
		<tr>
			<td class="width-one">参观开始时间</td>
			<td class="width-one">2018-10-27  09:30</td>
			<td class="width-one">参观结束时间</td>
			<td class="width-one">2018-10-27  09:30</td>
		</tr>
		<tr>
			<td>联系人</td>
			<td>王莹</td>
			<td>联系电话</td>
			<td>15543456767</td>
		</tr>
	</table>
	
	<!-- 参观人员信息展示 -->
	<h4 class="tableTitle">参观人员信息：</h4>
	<table class="visitPerson tableStyle">
		<tr>
			<td>参观单位性质</td>
			<td>企业</td>
			<td>参观人数</td>
			<td>15</td>
		</tr>
		<tr>
			<td>参观单位名称</td>
			<td colspan="3">信息中心</td>
		</tr>
	</table>
	
	<!-- 主要参观领导信息展示 -->
	<h4 class="tableTitle">主要参观领导：</h4>
		<table class="visitLeader tableStyle thTableStyle">
		<tr>
			<th class="width-three">序号</th>
			<th>姓名</th>
			<th>职务</th>
			<th>级别</th>
		</tr>
		<tr>
			<td>1</td>
			<td>某领导</td>
			<td>某职务</td>
			<td>某级别</td>
		</tr>
		<tr>
			<td>2</td>
			<td>某领导</td>
			<td>某职务</td>
			<td>某级别</td>
		</tr>
		<tr>
			<td>3</td>
			<td>某领导</td>
			<td>某职务</td>
			<td>某级别</td>
		</tr>
	</table>
	
	<!-- 院内陪同人员信息展示 -->
	<h4 class="tableTitle">院内陪同人员信息：</h4>
	<table class="visitAccompany tableStyle">
		<tr>
			<td class="width-two">院领导姓名</td>
			<td>王xx 胡xx</td>	
		</tr>
		<tr>
		<td class="width-two">陪同人数</td>
			<td>15</td>
		</tr>
	</table>
	
	<!-- 各部门（单位）陪同人员信息展示 -->
	<h4 class="tableTitle">各部门（单位）陪同人员：</h4>
	<table class="visitUnitAccompany tableStyle thTableStyle">
		<tr>
			<th class="width-three">序号</th>
			<th>姓名</th>
			<th>职务</th>
		</tr>
		<tr>
			<td>1</td>
			<td>某领导</td>
			<td>某职务</td>
		</tr>
		<tr>
			<td>2</td>
			<td>某领导</td>
			<td>某职务</td>
		</tr>
		<tr>
			<td>3</td>
			<td>某领导</td>
			<td>某职务</td>
		</tr>
	</table>
	
	<!-- 备注信息展示 -->
	<table class="visitRemarks tableStyle" style="margin-top:10px;">
		<tr>
			<td class="width-two"> 备注</td>
			<td colspan="3">填写相关信息</td>
		</tr>
	</table>
	
	<!-- end参观详情信息-->
	
	<!-- starts审批信息-->
	<div class="grid-title approveMessage">
		<h3>审批记录</h3>
	</div>
	<div style="width:98%;">
		<table class="visitApprove tableStyle thTableStyle">
		<tr>
			<th>审批人姓名</th>
			<th>审批部门/单位</th>
			<th>审批意见</th>
			<th>审批时间</th>
			<th>下一环节审批人角色</th>
			<th>下一环节审批人姓名</th>
			<th>下一环节审批人联系方式</th>
		</tr>
		<tr>
			<td>张某某</td>
			<td>财务资产部</td>
			<td>提交</td>
			<td>2018-10-27  15:36:22</td>
			<td>部门领导审批</td>
			<td>李某某</td>
			<td>010-82812222</td>
		</tr>
		<tr>
			<td>张某某</td>
			<td>财务资产部</td>
			<td>提交</td>
			<td>2018-10-27  15:36:22</td>
			<td>部门领导审批</td>
			<td>李某某</td>
			<td>010-82812222</td>
		</tr>
		<tr>
			<td>张某某</td>
			<td>财务资产部</td>
			<td>提交</td>
			<td>2018-10-27  15:36:22</td>
			<td>部门领导审批</td>
			<td>李某某</td>
			<td>010-82812222</td>
		</tr>
	</table>
	</div>
	
	<!-- end审批信息-->

</body>
</html>