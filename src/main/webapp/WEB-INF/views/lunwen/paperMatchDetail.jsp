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
    <title>专家匹配详情</title>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/js/plugins/layui/css/layui.css" rel="stylesheet" type="text/css">
<body>
<div>
    <div class="col-lg-2"></div>
    <div class="col-lg-8">
        <h3>已匹配专家信息</h3>
        <table class="layui-table" style="height: 200px!important;">
            <thead>
            <tr>
                <td>专家姓名</td>
                <td>领域</td>
                <td>研究方向</td>
                <td>打分状态</td>
            </tr>
            </thead>
            <c:forEach var="msl" items="${matchSpecialists}">
                <tr>
                    <td>${msl.name}</td>
                    <td>${msl.field}</td>
                    <td>${msl.researchDirection}</td>
                    <td>${msl.scoreStatus}</td>
                </tr>
            </c:forEach>
        </table>
        <div style="text-align: center">
            <button type="button" class="layui-btn" id="return" onclick="returnButton()">返回</button>
        </div>
    </div>
    <div class="col-lg-2"></div>
</div>


</body>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/html5.min.js"></script><!-- 兼容ie8的layui栅栏样式 -->
<script src="<%=request.getContextPath()%>/js/plugins/layui/respond.min.js"></script><!-- 兼容ie8的layui栅栏样式 -->
<script>
    function returnButton() {
        parent.layer.closeAll();
    }
</script>
</html>
