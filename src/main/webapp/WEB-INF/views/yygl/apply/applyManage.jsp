<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
    <title>用印申请列表</title>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
    <!-- easyuicss -->
    <link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
    <!-- 本页面所需css -->
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/js/plugins/layui/css/layui.css" rel="stylesheet" media="all">
    <style>
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
        .datagrid-header-row .datagrid-cell span{
            white-space: normal !important;
            word-wrap: normal !important;
        }
    </style>
</head>
<body>
<div class="main_div"></div>


<!-- start    查询条件 -->
<div class="sheach">
    <div class='content_top'>用印申请列表</div>

    <form id="queryForm" style="margin-bottom: 10px;">
        <input type="hidden" name="checkList"/>
        <span style="margin-left: 20px"></span>
        <label>申请编号：</label>
        <input type = "text" id = "applyCode" name = "applyCode" style="width: 100px" class = "inputQuery changeQuery" >
        <span style="margin-left: 20px"></span>
        <label>用印事项一级：</label>
        <select id = "useSealItemFirst" name = "useSealItemFirst"  class = "changeQuery changeYear" onchange="apply.changeItemFirst()">
            <option value = "" selected>请选择</option>
            <c:forEach  var="itemFirst"  items="${itemFirst}">
                <option value ="${itemFirst.K}"}> ${itemFirst.V}</option>
            </c:forEach>
        </select>
        <span style="margin-left: 20px"></span>
        <label>用印事项二级：</label>
        <select id = "itemSecondId" name = "itemSecondId"   class = "changeQuery changeYear">
            <option value = "" selected>请选择</option>
        </select>

        <br/>
        <span style="margin-left: 20px"></span>
        <label>用印事由：</label>
        <input type = "text" id = "useSealReason" name = "useSealReason" style="width: 100px" class = "inputQuery changeQuery" >
        <span style="margin-left: 20px"></span>
        <label  for="useSealStatus">审批状态：</label>
        <span style="margin-left: 28px"></span>
        <select id = "useSealStatus" name = "useSealStatus"   class = "changeQuery changeYear">
            <option value = "" selected>请选择</option>
            <c:forEach  var="useSealStatus"  items="${useSealStatus}">
                <option value ="${useSealStatus.K}"}> ${useSealStatus.V}</option>
            </c:forEach>
        </select>
        <span style="margin-left: 20px"></span>
        <label>申请日期：</label>
        <span style="margin-left: 24px"></span>
        <div class="layui-inline">
            <input type="text" class="layui-input" id="startTime" name="startTime" style="width: 100px" />
        </div>
        <label>至</label>
        <div class="layui-inline">
            <input type="text" class="layui-input" id="endTime" name="endTime" style="width: 100px"/>
        </div>


        <div style="float:right" id = "queryButton" class = "btn query" onclick = "apply.query()">搜索</div>
    </form>
</div>
<!-- end    查询条件 -->

<%--<hr/>--%>
<!-- start 列表展示 -->

<div>
    <!--  新增  修改  删除 功能按钮 -->
    <div id="funcBtn" style="height: 35px;float:right">
        <div style="margin-left: 5px;" class='btn right ' onclick="apply.applyExport()" >导出</div>
        <div style="margin-left: 5px;" class='btn right ' onclick="apply.withdraw()" >撤回</div>
        <div style="margin-left: 5px;" class='btn right ' onclick="apply.del()" >删除</div>
        <div style="margin-left: 5px;" class='btn right ' onclick="apply.toUpdate()" >修改</div>
        <div style="margin-left: 5px;" class='btn right ' onclick="apply.toAdd()" >新增</div>
        <div style="margin-left: 5px;" class='btn right ' onclick="apply.submit()" >提交</div>
    </div>

</div>
<div class="tabbable active"  style="padding-top: 35px">
    <div id="datagrid" style=""></div>
    <div class="tablepage"></div>
</div>
<!-- end 列表展示 -->

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
<!-- 本页面所需的js -->
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/apply/applyManage.js"></script>
<script src="<%=request.getContextPath()%>/js/lunwen/allCheckBox.js"></script>
</body>

</html>