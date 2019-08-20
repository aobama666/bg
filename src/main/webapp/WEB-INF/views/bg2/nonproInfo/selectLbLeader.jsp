<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员选择</title>
 	<link href="<%=request.getContextPath()%>/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/css/ky_reward/ky_reward_c_recommoned/recommonedList.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/ky_reward/common/reset.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/ky_reward/common/style.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/ky_reward/common/index.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/pa/leaderplantask/jquery.min.js?verNo=<%=VersionUtils.verNo%>"></script>	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/myhint/js/jquery-myhint.js?verNo=<%=VersionUtils.verNo%>"></script>
	<script src="<%=request.getContextPath()%>/js/stylePage/layer/layer.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/datagrid/js/jquery-tool.datagrid.js"></script> 
	<script src="<%=request.getContextPath()%>/js/ky_reward/common/common.js"></script> 
</head>
<body>
	<div>
		<div id="confirmok" class=" right bnt">确定</div>
	</div>
	<div class="table">
		<form id="searchForm">
			<input id="INFOID"  name="infoId" type="hidden"  class = "inputQuery changeQuery" value="${INFOID}"/>
			<label>姓名</label>
			<input id="USERALIAS"  name="userName" type="text"  class = "inputQuery changeQuery" />
			<label>单位</label>
			<input id="SEND_ORG" name="deptName" type="text"  />
			<div id = "queryButton" class = "btn query" >搜索</div>
		</form>	
	</div>
	<!-- start 列表展示 -->
	<div class="tabbable" >
		<div id="datagrid"></div>
		<div class="tablepage"></div>
	</div>
<script src="<%=request.getContextPath()%>/js/laboratory/selectLbLeader.js?rnd = <%=VersionUtils.verNo %>"></script> 
</body>
</html>