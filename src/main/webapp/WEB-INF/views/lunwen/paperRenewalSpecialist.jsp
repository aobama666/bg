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
 </head>
<body>
    <input type = "hidden" value = "${beforeUuid}" id = "beforeUuid" name="beforeUuid">
	<div class="main_div"></div>
	<!-- start  头部 -->
	<!-- <div class="sheach details">
		<div class='content_top'>参观预定审请</div>	  
	</div> -->
	<!-- end  头部 -->
	<div style="height: 30px;width: 100%"></div>
	<div style="width: 100%; height: 400px;padding-left: 100px">
		<div class="col-lg-5" style="width: 45%; height:400px">
			<h4 class="tableTitle">
				已匹配论文信息
			</h4>
			<div class="maxBox maxLine" style="width: 100%; height: 350px ">
				<table class="visitLeader tableStyle thTableStyle">
					<tr>
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

		<div class="col-lg-2" style="width: 3%; height: 400px"></div>

		<div class="col-lg-5" style="width: 45%; height: 400px">
			<h4 class="tableTitle">
				可匹配专家信息
			</h4>
			<div class="maxBox maxLine" style="width: 100% ;height: 350px">
				<table class="visitLeader tableStyle thTableStyle">
					<tr <%--style="background-color: #0dc4c4"--%>>
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
	</div>

	<div class="btnContent">
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
	<!-- 本页面所需的js -->
	<script src="<%=request.getContextPath()%>/yszx/js/plugins/stuff-tree/stuff-tree.js"></script>
    <script src="<%=request.getContextPath()%>/yszx/js/plugins/organ-tree/organ-tree.js"></script>
 	<script src="<%=request.getContextPath()%>/js/lunwen/paperSpecialistUpdate.js"></script>

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
				success:function(data){
                    if (data.success == "true") {
                        messager.tip("更换专家成功", 3000);
                        roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
                        var closeIndex = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(closeIndex);
					}else {
                        messager.tip("更换专家失败", 3000);
					}
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