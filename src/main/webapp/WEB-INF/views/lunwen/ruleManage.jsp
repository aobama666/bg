<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--<html style="overflow:hidden">--%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title></title>
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
		th{
			 text-align:center;
			 height:45px;
		 }
		td{
			text-align:center;
			height:45px;
		}
		input{
			text-align:center;
		}
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
	</style>
</head>
<body>
	<div class='content_top'>评分规则</div>
	<div id="funcBtn" style="float:right;margin-top:10px">
		<div class='btn right deleteButton' onclick="rule.downLoadTempLate()" >修改</div>
		<div class='btn right deleteButton' onclick="rule.downLoadTempLate()" >保存</div>
	</div>
	<!-- 学术技术综述分类按钮 -->
	<div class="paperTypeTab">
		<ul>
			<li style="border-left: 1px solid #ccc" class="checkkk" onclick="rule.updatePaperType(1)">学术类</li>
			<li onclick="rule.updatePaperType(2)">技术类</li>
			<li onclick="rule.updatePaperType(3)">综述类</li>
		</ul>
	</div>
	<form id="queryForm" method="post" enctype="multipart/form-data">
        <input style="display: none" value="1" id="paperType" name="paperType"/>
        <div id="ruleTable">
        <%--js初始化填充规则及分数内容table--%>
        </div>
    </form>


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
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>

	<!-- 本页面所需的js -->
 	<script src="<%=request.getContextPath()%>/js/lunwen/ruleManager.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
</body>

</html>