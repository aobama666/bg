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
<%
    String path = request.getContextPath();
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
    <title>增加业务主管部门会签</title>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">

    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/common/plugins/zTree/css/bootstrapStyle.css">
    <script type="text/javascript" src="<%=path %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=path %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=path %>/common/plugins/zTree/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=path %>/common/plugins/zTree/js/jquery.ztree.excheck.js"></script>
    <script type="text/javascript" src="<%=path %>/common/plugins/zTree/js/jquery.ztree.exedit.js"></script>

    <!-- 本页面所需css -->
    <link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
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
        .tree-box {
            border: 1px solid #1b9974;
            background: #ffffff;
            width: 100%;
            border-bottom-left-radius: 5px;
            border-bottom-right-radius: 5px;
            position: relative;
            float: left;
            margin-top: 0px;
            height: 250px;
            overflow-y:auto;
        }
        .tree-box li{
            padding: 3px 4px;
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
                    <select id = "signDept" name = "signDept" onchange="sign.changeDept()"
                            content="会签部门" class = "changeQuery changeYear validNull">
                        <option value = "">请选择</option>
                        <c:forEach  var="dept"  items="${deptList}">
                            <option value ="${dept.CODE}"}> ${dept.V}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
        <div style="padding-top: 10px;"></div>
        <div class="row">

            <div class="col-md-5">
                <span style="font-size: 17px">请选择会签部门负责人:</span>
                <div style="border: solid 1px black;height: 250px;">
                    <div class="tree-box">
                        <ul id="tree" class="ztree"></ul>
                    </div>
                </div>
            </div>
            <div class="col-md-2"></div>
            <div class="col-md-5">
                <span style="font-size: 17px">选择结果</span>
                <div style="border: solid 1px black;height: 250px;">
                    <div style="padding: 2px 2px 2px 2px">
                            <table style="width: 100%;">
                                <tr>
                                    <td class="bg">负责人姓名</td>
                                    <td class="bg">所在处室</td>
                                </tr>
                                <tr>
                                    <td id="checkUser">张某某</td>
                                    <td id="checkOffice">科技项目处</td>
                                </tr>
                            </table>
                    </div>
                </div>
            </div>
        </div>
        <div style="text-align: center;padding-top: 20px">
            <button type="button" class="btn" onclick="sign.addSign()">确认</button>
            <button type="button" class="btn" onclick="sign.returnClose()">取消</button>
        </div>
    </div>
<input style="display: none" id="treelist" type="text"/>
<input style="display: none" id="ct" type="text"/>
<%--<input style="display: none" id="root" type="text"/>--%>
</body>

<!-- 验证校验公共方法，提示框公共方法 -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>

<!-- 本页面所需的js -->
<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
<script src="<%=request.getContextPath()%>/js/yygl/myItem/addSign.js"></script>

</html>
