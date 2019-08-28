<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/8/20
  Time: 16:01:20
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
    <title>选择用印种类</title>
    <link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
    <style type="text/css">
        html{
            overflow-x: hidden;
        }
    </style>
</head>
<body>
<span style="display: none" id="kindList">${kindList}</span>
<div id="kindContent" style="padding-left: 60px">
    <c:forEach  var="kindList"  items="${kindList}">
        <input type="checkbox" value="${kindList.K},${kindList.V}" name="kind"
                <c:if test="${kindList.IF=='1'}">checked="checked"</c:if>
        /> &nbsp; ${kindList.V}<br/>
    </c:forEach>
    <input type="checkbox" value="-1" name="elseCheck"/>&nbsp; 其他<input type="text" id="elseKind" value="${elseKind}">
</div>
<div class="btnContent" style="margin: 0px;padding-top: 10px">
    <button type="button" class="btn" onclick="applyOperate.kindSave()">保存</button>
    <button type="button" class="btn" onclick="applyOperate.returnClose()">返回</button>
</div>
</body>
<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/apply/applyOperate.js"></script>
</html>
