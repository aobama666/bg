<!DOCTYPE>
<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>同步数据</title>
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css"
          media="screen">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/common/css/style.css">

    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/stuff-tree/stuff-tree.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/sotoValidate/sotoValidate.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
</head>
<body>
<div class="tab-pane fade in active" id="proInfo">
    <div class="page-header-sl">
        <div class="button-box">
            <button type="button" class="btn btn-success btn-xs" onclick="forSave()">同步</button>
            <!-- <button type="button" class="btn btn-warning btn-xs"
                        onclick="forClose()">关闭</button> -->
        </div>
    </div>
    <hr>
    <div class="form-box">
        <div class="form-group col-xs-11">
            <label>数据分类</label>
            <div class="controls">
                <select id="category" name="category" property="category">
                    <options collection="typeList" property="label" labelProperty="value">
                        <c:forEach  var="type"  items="${map}">
                            <option value ="${type.key}"}> ${type.value}</option>
                        </c:forEach>
                    </options>
                </select>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    function forSave() {
        var category=$("select[name='category']").val();
        parent.forSave(category);
        parent.queryList('reload');
        forClose()
    }

    function forClose(){
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }

</script>

</body>
</html>
