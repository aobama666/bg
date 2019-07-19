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
		}
		input{
			text-align:center;
		}
	</style>
</head>
<body>
	<h2 style="text-align:center">${paperTypeValue}论文评分表——${paperName}</h2>
	<form id="queryForm" method="post" enctype="multipart/form-data">
        <input style="display: none" value="${pmeId}" id="pmeId" name="pmeId"/>
        <input style="display: none" value="${paperUuid}" id="paperUuid" name="paperUuid"/>
        <input style="display: none" value="${paperType}" id="paperType" name="paperType"/>
        <input style="display: none" value="${scoreStatus}" id="scoreStatus" name="scoreStatus"/>
		<c:if test="${totalScoreAfter!=null}">
			<input style="display: none" value="${totalScoreAfter}" id="totalScoreAfter" name="totalScoreAfter"/>
		</c:if>
        <input style="display: none" id="scoreTableLength" name="scoreTableLength"/>
        <div id="scoreTable">
        <%--js初始化填充规则及分数内容table--%>
        </div>
    </form>
	<div class="main_div">
		<div style="text-align: center;margin-top:2%">
			<c:if test="${scoreStatus=='0'}">
				<div class='btn  deleteButton' onclick="grade.saveGrade()" >保存</div>
			</c:if>
			<c:if test="${scoreStatus=='1'}">
				<div class='btn  deleteButton' onclick="grade.saveGrade()" >修改</div>
			</c:if>
			<div class='btn  deleteButton' onclick="grade.addClose()" >返回</div>
		</div>
	</div>


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
 	<script src="<%=request.getContextPath()%>/js/lunwen/paperGradeOperation.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
</body>

</html>