<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/9/3
  Time: 9:50
  To change this template use File | Settings | File Templates.
--%>
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
    <title>退回并填写审批意见</title>
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
    <link href="<%=request.getContextPath()%>/css/yygl/agree.css" rel="stylesheet" media="all">
</head>
<body>
<div style="padding-left: 15%;padding-right: 15%">
    <div>
        <div class="content-tag">
            选择下一环节审批人
        </div>
        <div class="content-tag-line"></div>
    </div>
    <div  style="height: 10px"></div>
    <table class="agree" style="width: 100%">
        <tr>
            <td class="bg">选择</td>
            <td class="bg">用户账号</td>
            <td class="bg">用户名称</td>
            <td class="bg">审批角色</td>
            <td class="bg">部门</td>
        </tr>
        <tr>
            <td><input type="radio"/></td>
            <td>${applyUser}</td>
            <td>${applyUserName}</td>
            <td>用印申请人</td>
            <td>${applyDept}</td>
        </tr>
    </table>
    <div style="padding-top: 20px"></div>
    <table class="agree" style="width: 100%">
        <tr>
            <td class="bg">审批人</td>
            <td id="approveUser">${loginUserName}</td>
            <td class="bg">审批时间</td>
            <td id="approveTime">${nowDate}</td>
        </tr>
        <tr>
            <td class="bg">审批意见</td>
            <td colspan="3">
                <textarea id="approveRemark" style="height: 100%;width: 100%;resize:vertical">拒绝</textarea>
            </td>
        </tr>
    </table>
    <input type="text" id="applyUserId" value="${applyUserId}" style="display: none;"/>
    <input type="text" id="applyId" value="${applyId}" style="display: none;"/>
    <div style="text-align: center;padding-top: 20px">
        <button type="button" class="btn" onclick="approve.sendBack()">确认</button>
        <button type="button" class="btn" onclick="approve.returnClose()">取消</button>
    </div>
</div>
</body>
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
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/myItem/approveOperate.js"></script>
</html>
