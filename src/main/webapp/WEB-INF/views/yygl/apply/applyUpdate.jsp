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
    <title>用印申请修改</title>
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
    <style type="text/css">
        .tableBody{
            height: 120px!important;
        }
    </style>
</head>
<body>
<div class="tabbable active" style="text-align:right;width: 97%;padding-top: 5px;padding-bottom: 5px;">
    <button type="button" class="btn" onclick="applyOperate.applySubmit()">提交</button>
    <button id="applyUpdate" type="button" class="btn" onclick="applyOperate.applyUpdate()">保存</button>
    <button type="button" class="btn" onclick="applyOperate.returnClose()">返回</button>
</div>
<table class="visitOperate tableStyle specialTable">
    <tr>
        <td style="width: 10%">
            <span title = "用印部门"><b class="mustWrite">*</b>用印部门</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <input type = "text" style="display: none" id = "uuid"  name="uuid" value="${apply.uuid}">
            <input type = "text" style="display: none" id = "applyDeptId"  name="applyDeptId" value="${deptId}">
            <input type="text"  id="applyDept"  name="applyDept" readonly value="${deptName}"
                   class="validNull"  len="50"   content="用印部门" title="必填项"/>
        </td>
        <td style="width: 10%">
            <span title = "用印日期"><b class="mustWrite">*</b>用印日期</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <div class="layui-inline">
                <input type="text" class="layui-input validNull" content="用印日期" value="${apply.useSealDate}"  id="useSealDate" name="useSealDate" />
            </div>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印申请人"><b class="mustWrite">*</b>用印申请人</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <input type = "text" style="display: none" id = "applyUserId"  name="applyUserId" value="${userId}">
            <input type="text"  id="applyUser"  name="applyUser" readonly value="${userName}"
                   class="validNull"  len="50"   content="用印申请人" title="必填项"/>
        </td>
        <td style="width: 10%">
            <span title = "联系电话"><b class="mustWrite">*</b>联系电话</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <input type="text"  id="useSealPhone" name="useSealPhone"  class="validNull"
                   value="${apply.useSealPhone}" content="联系电话"  len="50"  title="必填项  "/>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印事项一级"><b class="mustWrite">*</b>用印事项一级</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <input hidden="none" value="${apply.itemFirstId}" id="itemFirstIdCode"/>
            <select id = "useSealItemFirst" name = "useSealItemFirst" onchange="applyOperate.changeItemFirst()"
                    content="用印事项一级" class = "changeQuery changeYear validNull">
                <option value = "">请选择</option>
                <c:forEach  var="itemFirst"  items="${itemFirst}">
                    <option value ="${itemFirst.K}"}> ${itemFirst.V}</option>
                </c:forEach>
            </select>
        </td>
        <td style="width: 10%">
            <span title = "用印事项二级"><b class="mustWrite">*</b>用印事项二级</span>
        </td>
        <td style="width: 40%" class="addInputStyle">
            <input hidden="none" value="${apply.itemSecondId}" id="itemSecondIdCode"/>
            <div id="selectSecondItem">
                <select id = "useSealItemSecond" name = "useSealItemSecond" content="用印事项二级" class = "changeQuery changeYear validNull">
                        <option value = "">请选择</option>
                        <c:forEach  var="itemSecond"  items="${itemSecond}">
                            <option value ="${itemSecond.K}"}> ${itemSecond.V}</option>
                        </c:forEach>
                </select>
            </div>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印种类"><b class="mustWrite">*</b>用印种类</span>
        </td>
        <td style="width: 90%" class="addInputStyle" colspan="3">
            <input type = "text" style="display: none" id = "useSealKindCode"  name="useSealKindCode" value="${kindCode}">
            <input type = "text" style="display: none" id = "elseKind"  name="elseKind" value="${KindValue}">
            <input type="text"  id="useSealKindValue"  name="useSealKindValue"  class="validNull"
                   onclick="applyOperate.checkKind()" content="用印种类" value="${apply.useSealKind}" title="必填项"/>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印事由"><b class="mustWrite">*</b>用印事由</span>
        </td>
        <td style="width: 90%" class="addInputStyle" colspan="3">
            <input type="text"  id="useSealReason" name="useSealReason" value="${apply.useSealReason}"  class="validNull"  content="用印事由"  len="50"  title="必填项  "/>
        </td>
    </tr>
</table>

<div class="tabbable active" style="width: 94%;margin-left: 3%;margin-top: 1%">
    <h3 style="float: left">用印材料详情</h3>
    <div style="text-align: right">
        <button type="button" class="btn" onclick="annex.toAddStuff()">新增</button>
        <button type="button" class="btn" onclick="annex.delStuff()">删除</button>
    </div>

    <div id="datagrid" style="padding-top: 3px;"></div>
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
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/apply/applyOperate.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/apply/applyAnnex.js"></script>
</body>
</html>