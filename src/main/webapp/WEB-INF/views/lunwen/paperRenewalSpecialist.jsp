<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
	<title>更换专家</title>
	<link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">   
    <!-- easyui用css -->
	<link  href="<%=request.getContextPath()%>/yszx/css/idea/easyui.css" rel="stylesheet"/>
	<!-- 本页面所需css -->
	<link href="<%=request.getContextPath()%>/yszx/css/idea/roomList.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		table{
			width: 94%;
			border: 1px solid #ccc;
			margin-bottom: 30px;
			table-layout: fixed;
			margin: 0 auto;
			text-align: center;
		}
		table td{
			border: 1px solid #ccc;
		}
		th{
			text-align: center;
			background: #d5e7e7;
		}
		html{
			overflow-x: hidden;
		}
	</style>
 </head>
<body>
    <input type = "hidden" value = "${beforeUuid}" id = "beforeUuid" name="beforeUuid">
	<div style="" class="row">
		<div class="col-md-1"></div>
		<div class="col-md-4" style="height: 200px;!important;">
			<h4 class="tableTitle">
				已匹配论文信息
			</h4>
			<div style="height: 300px;overflow: auto">
				<table>
					<tr style="">
						<th>论文题目</th>
						<th>领域</th>
						<th>作者</th>
					</tr>
					<c:forEach var="papers" items="${paperMap}">
						<tr>
							<td><span>${papers.paperName}</span></td>
							<td><span>${papers.field}</span></td>
							<td><span>${papers.author}</span></td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>

		<div class="col-md-2"></div>

		<div class="col-md-4">
			<h4 class="tableTitle">
				可匹配专家信息
			</h4>
			<div style="height: 300px;overflow: auto">
				<table>
					<tr>
						<th>选择</th>
						<th>专家姓名</th>
						<th>领域</th>
						<th>研究方向</th>
					</tr>
					<c:forEach var="sp" items="${matchingSpecialistList}">
						<tr>
							<td><input type="radio" id="uuid" name="uuid" value="${sp.uuid}"></td>
							<td><span>${sp.name}</span></td>
							<td><span>${sp.field}</span></td>
							<td><span>${sp.researchDirection}</span></td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div class="col-md-1"></div>
	</div>

		<div style="text-align: center">
			<button type="button" class="btn" onclick="submit()" >提交</button>
			<button type="button" class="btn" onclick="messageResign()">返回</button>
	</div>

	<!-- end参观详情信息-->
    <script src="<%=request.getContextPath()%>/yszx/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script> 
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	
	<script src="<%=request.getContextPath()%>/yszx/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/datagrid2.0/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
	
    <!-- 引入日期选择框 -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
	<!-- 验证校验公共方法，提示框公共方法 -->
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/common.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/recommonedCommon.js"></script>
	<script src="<%=request.getContextPath()%>/yszx/js/idea/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/layui/layer.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/layui/layui.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/layui/html5.min.js"></script><!-- 兼容ie8的layui栅栏样式 -->
	<script src="<%=request.getContextPath()%>/js/plugins/layui/respond.min.js"></script><!-- 兼容ie8的layui栅栏样式 -->

	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
	<script src="<%=request.getContextPath()%>/js/lunwen/paperSpecialistManage.js"></script>

</body>
<script type="text/javascript">
	var isAdd = 1;
	var beforeUuid = $('#beforeUuid').val();
	var nowUuid;
	$('input:radio').click(function () {
		var $radio = $(this);
		if($radio.data('waschecked') == true){
		    $radio.prop('checked',false);
		    $radio.data('waschecked',false);
		    nowUuid = null;
		    isAdd = 0;
		}else {
            $radio.prop('checked',true);
            $radio.data('waschecked',true);
            nowUuid = $("input:radio:checked").val();
            isAdd = 1;
		}
    });


    function submit() {
		if(nowUuid == null || nowUuid == ''){
            messager.tip("请选择专家",2000);
		}else {
		    $.ajax({
                url: "/bg/expert/renewal",//删除
                type: "GET",
				data:{beforeUuid:beforeUuid,nowUuid:nowUuid},
                contentType: 'application/json',
                success: function (data) {
                    parent.messager.tip(data.msg,5000);
                    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
                    var closeIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(closeIndex);
                }
			})
		}
    }

    function messageResign() {
        roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
        var closeIndex = parent.layer.getFrameIndex(window.name);
        parent.layer.close(closeIndex);
    }
</script>
</html>