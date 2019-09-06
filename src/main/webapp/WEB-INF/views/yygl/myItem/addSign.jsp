<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/9/3
  Time: 9:49
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
    <title>增加业务主管部门会签</title>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <!-- newPage、item.css 页面css-->
    <%--<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>--%>
    <%--<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>--%>
    <%--<link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">--%>
    <!-- easyuicss -->
    <%--<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet" />--%>
    <!-- 本页面所需css -->
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
    <%--<link href="<%=request.getContextPath()%>/js/plugins/layui/css/layui.css" rel="stylesheet" media="all">--%>
    <style type="text/css">
        .bg{
            background: #d5e7e7;
        }
        td{
            border: solid 1px black;
            text-align: center;
            font-size: 15px;
            height: 30px;
        }


    </style>
</head>
<body>
    <div style="padding-left: 15%;padding-right: 15%;padding-top: 10px;">
        <table style="width: 100%">
            <tr>
                <td class="bg" width="30%">已流转业务主管部门</td>
                <td>中国电科院财务资产部</td>
            </tr>
            <tr>
                <td class="bg"  width="30%">请选择会签部门</td>
                <td>
                    <select id = "useSealItemFirst" name = "useSealItemFirst" onchange=""
                            content="会签部门" class = "changeQuery changeYear validNull">
                        <option value = "">请选择</option>
                        <c:forEach  var="dept"  items="${deptList}">
                            <option value ="${dept.K}"}> ${dept.V}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <div style="padding-top: 10px;"></div>
        <div>
            <div style="float: right;">
                <span style="font-size: 17px">选择结果</span>
                <div style="border: solid 1px black;height: 200px; width: 250px;">
                    <div style="padding: 2px 2px 2px 2px">
                            <table style="width: 244px;">
                                <tr>
                                    <td class="bg">负责人姓名</td>
                                    <td class="bg">所在处室</td>
                                </tr>
                                <tr>
                                    <td>张某某</td>
                                    <td>科技项目处</td>
                                </tr>
                            </table>
                    </div>
                </div>
            </div>
            <div style="">
                <span style="font-size: 17px">请选择会签部门负责人:</span>
                <div style="border: solid 1px black;height: 200px; width: 250px;"></div>
            </div>
        </div>
        <div style="text-align: center;padding-top: 20px">
            <button type="button" class="btn" onclick="">确认</button>
            <button type="button" class="btn" onclick="">取消</button>
        </div>
    </div>

</body>
<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<%--<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>--%>
<%--<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>--%>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->

<%--<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>--%>

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
</html>
