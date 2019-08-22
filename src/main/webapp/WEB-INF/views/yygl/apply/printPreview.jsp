<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/8/22
  Time: 15:40:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>打印预览</title>
    <style>
        td{
            border:1px black solid
        }
        table{
            width: 80%;
        }
    </style>
</head>
<body>
<h2>中国电力科学研究院有限公司用印审批单</h2>
<h5>申请编号：${yyApplyDAO.applyCode}</h5>
<table>
    <tr>
        <td>用印部门(单位):</td>
        <td colspan="3">${yyApplyDAO.applyDept}</td>
    </tr>
    <tr>
        <td>用印申请人:</td>
        <td>${yyApplyDAO.applyUser}</td>
        <td>联系方式:</td>
        <td>${yyApplyDAO.useSealPhone}</td>
    </tr>
    <tr>
        <td>用印日期:</td>
        <td>${yyApplyDAO.useSealDate}</td>
        <td>用印事项:</td>
        <td>${yyApplyDAO.useSealItem}</td>
    </tr>
    <tr>
        <td>用印种类:</td>
        <td colspan="3">${yyApplyDAO.useSealKind}</td>
    </tr>
    <tr>
        <td>用印事由:</td>
        <td colspan="3">${yyApplyDAO.useSealReason}</td>
    </tr>
    <tr>
        <td colspan="4">审批记录</td>
    </tr>
    <tr>
        <td>审查单位</td>
        <td>审批角色</td>
        <td colspan="2">审批意见</td>
    </tr>
</table>
</body>
</html>
