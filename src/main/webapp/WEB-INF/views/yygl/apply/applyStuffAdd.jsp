<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/8/27
  Time: 16:17:18
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
    <title>用印材料新增</title>
</head>
<body>
<form action="" id="form17" name="form17" enctype="multipart/form-data" method="post">
<table class="visitOperate tableStyle specialTable">
    <tr>
        <td style="width: 20%">
            <span title = "用印材料">用印材料<b class="mustWrite">*</b></span>
        </td>

        <td style="width: 80%" class="addInputStyle">
            <input type = "text" style="display: none" id = "applyId"  name="applyId" value="${applyUuid}"/>
            <div class="controls" class="form-control">
                <input id="useSealFile" type="file" name="useSealFile" class="validNull"  property="file"
                       content="用印材料" title="必填项" style="display:inline-block;">
            </div>
        </td>
    </tr>
    <tr>
        <td style="width: 20%">
            <span title = "佐证材料">佐证材料</span>
        </td>
        <td style="width: 80%" class="addInputStyle">
            <div class="controls" class="form-control">
                <input id="proofFile" type="file" name="proofFile" class=""  property="file"
                       content="佐证材料" title="必填项" style="display:inline-block;">
            </div>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "用印文件份数">用印文件份数<b class="mustWrite">*</b></span>
        </td>
        <td style="width: 90%" class="addInputStyle">
            <input type="text"  id="useSealAmount"  name="useSealAmount"
                   class="validNull"  len="50"   content="用印文件份数" title="必填项"/>
        </td>
    </tr>
    <tr>
        <td style="width: 10%">
            <span title = "备注">备注</span>
        </td>
        <td style="width: 90%" class="addInputStyle">
            <input type="text"  id="remark"  name="remark"
                   class=""  len="50"   content="备注" title="必填项"/>
        </td>
    </tr>
</table>
</form>
<div style="text-align: center; padding-top: 5px">
    <button type="button" class="btn" onclick="annex.saveStuff()">保存</button>
    <button type="button" class="btn" onclick="applyOperate.returnClose()">返回</button>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoValidate/sotoValidate.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>

<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/apply/applyOperate.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/apply/applyAnnex.js"></script>
</body>
</html>
