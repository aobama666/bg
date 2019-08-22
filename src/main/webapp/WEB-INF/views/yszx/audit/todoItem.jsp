<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>演示中心-已办列表页</title>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
    <!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
</head>
<body>
<!-- <div class='title'>任务编制（配合）</div> -->

<!-- 任务编制配合列表页搜索条件 -->
<input type="hidden" id="search_prjName" value="<%=request.getParameter("search_prjName")==null?"":request.getParameter("search_prjName")%>">
<input type="hidden" id="search_prjYear" value="<%=request.getParameter("search_prjYear")==null?"":request.getParameter("search_prjYear")%>">
<input type="hidden" id="search_prjSource" value="<%=request.getParameter("search_prjSource")==null?"":request.getParameter("search_prjSource")%>">

<div class="main_div"></div>
<div class="sheach">
	<div class='content-top'>任务编制（配合）</div>
    <div id='queryForm' style='margin-top:10px;'>
        <label>项目名称：</label><input type='text' id='projectName' name='projectName' placeholder='项目名称'
                                   style="width: 159px">
        <label>项目年度：</label>
        <select id="prjYear" name="prjYear">
        </select>
        <label>项目来源：</label>
        <select id="prjSource" name="prjSource">
            <option value="" selected>全部</option>
        </select>
       	<input type="hidden" name="unitRole" value="phbz">
       <div class='btn query'>搜索</div>

    </div>


</div>
<!-- funcBtn功能按钮行 -->
<div id="funcBtn" style="width:100%;height: 35px;">
   	<div class='btn right recordButton' onclick="recordFunc()">同意</div> 
   	<div class='btn right cancelButton' onclick="cancelSyncFunc()">退回</div>
</div>

<!--页面表格  配合datagrid把表格显示页面上-->
<div class="tabbable" >
   <!--  <ul class="nav nav-tabs">
        <li class="active"><a href="#" data-toggle="tab">项目列表</a></li>
    </ul> -->
    <!-- 选项卡相对应的内容 -->
    <div class="tab-content">
        <!-- 表格 -->
        <div class="tab-pane active" >
            <div id="datagrid"></div>
            <div class="tablepage"></div>
        </div>
    </div>

</div>


<script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
<script src="<%=request.getContextPath()%>/yszx/js/json2.js?verNo=<%=VersionUtils.verNo%>"></script>  <!-- IE支持 JSON格式   -->
<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- 弹框.js  -->
<!-- 本页面所需的js -->
<script src="<%=request.getContextPath()%>/yszx/js/idea/todoItem.js"></script>

<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script>



</script>
</body>
</html>