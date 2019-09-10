<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/8/22
  Time: 14:51:05
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
    <title>用印申请新增</title>
    <link href="<%=request.getContextPath()%>/css/yygl/liucheng.css" rel="stylesheet" media="all">
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
    <link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
    <%--<link href="<%=request.getContextPath()%>/js/plugins/layui/css/layui.css" rel="stylesheet" media="all">--%>
    <style type="text/css">
        .tableBody{
            height: 120px!important;
        }
    </style>
</head>
<body>
    <h3 style="float: left;margin-left: 3%;">申请编号:${yyApplyDAO.applyCode}</h3>
<div class="tabbable active" style="text-align:right;width: 97%;padding-top: 5px;padding-bottom: 5px;">
    <button type="button" class="btn" onclick="returnClose()">返回</button>
    <button type="button" class="btn" onclick="returnClose()">撤回</button>
    <button type="button" class="btn" onclick="returnClose()">同意</button>
    <button type="button" class="btn" onclick="returnClose()">退回</button>
    <button type="button" class="btn" onclick="returnClose()">增加业务会签</button>
    <button type="button" class="btn" onclick="returnClose()">确认用印</button>
</div>
<table class="visitOperate tableStyle specialTable">
    <tr>
        <td style="width: 10%">
            <span title = "用印部门"><b class="mustWrite">*</b>用印部门</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <input type = "text" style="display: none" id = "uuid"  name="uuid" value="${yyApplyDAO.uuid}">
            <span>&nbsp;&nbsp;${yyApplyDAO.applyDept}</span>
        </td>
        <td style="width: 10%">
            <span title = "用印日期"><b class="mustWrite">*</b>用印日期</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <span>&nbsp;&nbsp;${yyApplyDAO.useSealDate}</span>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印申请人"><b class="mustWrite">*</b>用印申请人</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <span>&nbsp;&nbsp;${yyApplyDAO.applyUser}</span>
        </td>
        <td style="width: 10%">
            <span title = "联系电话"><b class="mustWrite">*</b>联系电话</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <span>&nbsp;&nbsp;${yyApplyDAO.useSealPhone}</span>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印事项"><b class="mustWrite">*</b>用印事项</span>
        </td>
        <td style="width: 90%" class="addInputStyle"colspan="3">
            <span>&nbsp;&nbsp;${yyApplyDAO.useSealItem}</span>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印种类"><b class="mustWrite">*</b>用印种类</span>
        </td>
        <td style="width: 90%" class="addInputStyle" colspan="3">
            <span>&nbsp;&nbsp;${yyApplyDAO.useSealKind}</span>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印事由"><b class="mustWrite">*</b>用印事由</span>
        </td>
        <td style="width: 90%" class="addInputStyle" colspan="3">
            <span>&nbsp;&nbsp;${yyApplyDAO.useSealReason}</span>
        </td>
    </tr>
</table>

<form action="">
    <input type = "text" style="display: none" value="" id = "filePath" name="filePath">
    <input type = "text" style="display: none" value="" id = "fileName" name="fileName">
</form>


<div class="tabbable active" style="width: 94%;margin-left: 3%;margin-top: 1%">
    <h3>用印材料详情</h3>

    <div id="datagrid" style="padding-top: 3px;"></div>
</div>

<%--<div style="padding-top: 10px"></div>--%>


<div class="mingliao" style="margin-left: 3%;width: 94%;">
    <h3>审批流程</h3>
    <table class="liuchengTable" >
        <tr>
            <td>
                <div class="start green">开始</div>
            </td>
            <td colspan="4"></td>
            <td colspan="4" style="align-text: right;">
                状态说明:
                <b class="colorIcon green"></b>已完成
                <b class="colorIcon yellow"></b>进行中
                <b class="colorIcon"></b>未开始
            </td>
        </tr>
        <tr>
            <td>
                <i class="toDown"></i>
            </td>
        </tr>
        <tr>
            <td>
                <div class="process " id="1">申请人提交</div>
            </td>
            <td>
                <i class="toRight"></i>
            </td>
            <td>
                <div class="process " id="2">业务主管部门审批</div>
            </td>
            <td>
                <i class="toRight"></i>
            </td>
            <td>
                <div class="process " id="3">党委办公室审批</div>
            </td>
            <td>
                <i class="toRight"></i>
            </td>
            <td>
                <div class="process" id="4">院领导批准</div>
            </td>
        </tr>
        <tr>
            <td colspan="4"></td>
            <td style="padding-left: 70px" colspan="2" rowspan="3">
                <i class="toDownRight"></i>
            </td>
        </tr>
        <tr>
            <td colspan="4"></td>
            <td>
                <i class="toDown"></i>
            </td>
        </tr>
        <tr>
            <td colspan="6"></td>
            <td>
                <div class="process " style="padding:0px;height:45px;" id="5">印章管理员<br/>确认用印</div>
            </td>
            <td>
                <i class="toRight"></i>
            </td>
            <td>
                <div class="start" id="6">结束</div>
            </td>
        </tr>
    </table>
</div>

</body>
<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
<script src="<%=request.getContextPath()%>/js/yygl/apply/applyAnnex.js"></script>
<script>

    $(function () {
        var x = 3;
        var i = 1;
        //设置正在进行中的
        $('#'+(x+1)).addClass('yellow');
        //设置通过的
        for(i;i<=x;i++){
            $("#"+i).addClass('green');
            $("#"+i).removeClass('yellow');
        }
        //院领导是否参与本次审批

    });

    function returnClose() {
        parent.layer.closeAll();
    }

</script>
</html>
