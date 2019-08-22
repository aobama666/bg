<%@page import="com.sgcc.bg.common.VersionUtils"%>
<%@ page import="java.util.Calendar" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8" http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>
    <title>信息库维护</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/ky_reward/index.css"/>
	<!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/newPage.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/item.css" rel="stylesheet" type="text/css"/>
	<link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/datagrid.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/js/plugins/fontface/iconfont.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/laboratory/roomDetail.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/css/laboratory/easyui.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/laboratory/common/reset.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/laboratory/common/style.css"/>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/laboratory/common/index.css"/>
    <style type="text/css">
    </style>
</head>
<body>
<form id="excelForm" enctype="multipart/form-data">
	<input type = "file" onchange = "fileChangeEvent(event)"  style = "display:none" id = "excelFile" ><div class='btn left importButton' id="importButton" onclick="importExcel()">上传</div>
</form>
<div class = "basic_info_box">
		<div  id = "basic_info_form" >
			<div class='btn right' onclick = "back()">返回</div>
			<div class='btn right' onclick = "importEvent()">上传</div>
			<div class='btn right'  onclick = "exportMoudle()">下载模板</div>
			<br>
			<br>
			<table class = "table">
				<tbody>
					<tr>
						<td colspan="2">
							<div class = "inputForm">
								<span title = "文件名称">文件名称<b class='strongStar'>*</b></span>
								<div class = "input_div">								
									<input value = "请选择文件"   name = "fileName" style = "width:60%;" class = "validNull" id = "fileName"  type="text" />
									<div class = "customBtn" onclick = "checkFileEvent()" style = "width:15%;">请选择文件</div>
								</div>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
 	<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.7.2.min.js?verNo=<%=VersionUtils.verNo%>"></script>
	<script src="<%=request.getContextPath()%>/js/jquery/jquery-form.js?verNo=<%=VersionUtils.verNo%>"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/datebox/jquery.easyui.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/plugins/datebox/locale/easyui-lang-zh_CN.js"></script>
	<script src="<%=request.getContextPath()%>/js/laboratory/common/router.js"></script>
	<script src="<%=request.getContextPath()%>/js/laboratory/common/common.js"></script><!-- 必须在techProgress.js之前 -->
	<script src="<%=request.getContextPath()%>/js/json2.js"></script>  <!-- IE支持 JSON格式   -->
	<script src="<%=request.getContextPath()%>/js/plugins/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/js/stylePage/layer/layer.js"></script>    <!-- 弹框.js  -->
	<script src="<%=request.getContextPath()%>/js/laboratory/common/roomAddInfoCommon.js?rnd=<%=VersionUtils.verNo %>"></script>
<script src="<%=request.getContextPath()%>/js/plugins/datagrid/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->

<script type="text/javascript">
function checkFileEvent(){
	$("#excelFile").click();
}
function fileChangeEvent(e){
	$("#fileName").val(e.currentTarget.files[0].name);
}
function importEvent(e){
	$("#excelFile").click;
}
function importExcel(){
	debugger;
	//获取要上传的文件名
	  var fileName=$("#excelFile").val();
	  //判断时候选择文件
	  if(fileName==null||fileName==''){
		  alert("点击上传前请选择文件！");
		  return;
	  }
	  //判断是否为xls格式
	  if(fileName.indexOf('.xls')!=fileName.length-4){
		  alert("仅支持上传xls格式文件！");
		  return;
	  }
	  if(status=='correct'||status=='error'){
		  alert("已导入数据未保存或提交，再次上传将被覆盖，确认重新上传?");
	  }
	  var params =  $("#excelForm").serializeArray();
	  $("#excelForm").ajaxSubmit({
          type:"post",
          url:'/newtygl/infoBaseMaintain/infoBaseMaintainImport',
          success:function(dataStr){
        	  debugger;
        	 var data=JSON.parse(dataStr);
        	 if(data.retcode!='success'){
        		 alert("导入错误："+data.errmsg);
        		 return;
        	 }else{
        		 if(data.isErr=="true"){
              	     alert("导入数据有错误，请确认!");
                 }else{
                	 
                 }
        	 }
          },
          error:function(errmsg){
          	
          }
      });
}


function exportMoudle(){
	if(confirm("确定下载导入模板吗？")){
		location.href="/newtygl/laboratory/downloadModul?type=sacImport&rnd="+Math.floor(Math.random()*99999999);
	}
}

function back(){
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</body>
</html>