<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/9/4
  Time: 19:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>提交-选择下一环节审批人</title>
    <link href="<%=request.getContextPath()%>/css/yygl/agree.css" rel="stylesheet" media="all">
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
    <style>
        body{
            overflow-x: hidden;
        }
    </style>
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
            <td>EPRI_LMM</td>
            <td>李某某</td>
            <td>业务主管部门负责人审批</td>
            <td>科技部</td>
        </tr>
    </table>
    </table>
    <div style="text-align: center;padding-top: 20px">
        <button type="button" class="btn" onclick="">确认</button>
        <button type="button" class="btn" onclick="">取消</button>
    </div>
</div>

</body>
</html>
