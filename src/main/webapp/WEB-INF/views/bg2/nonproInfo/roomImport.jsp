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
    <link href="<%=request.getContextPath()%>/js/plugins/bootstrap/css/bootstrap.min.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <!-- newPage、item.css 页面css-->
    <link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/newPage.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/item.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath()%>/js/plugins/datagrid/css/datagrid.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
    <!-- 树菜单.css -->
    <link href="<%=request.getContextPath()%>/js/stylePage/dtree/css/dtree.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
    <!-- 自动补全css -->
    <link href="<%=request.getContextPath()%>/js/stylePage/inputall/css/main.css?verNo=<%=VersionUtils.verNo%>" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/ky_reward/index.css"/>
	<!-- newPage、item.css 页面css-->
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
<div>
	<input type="hidden" id="onceCode"/>
	<form id="excelForm" enctype="multipart/form-data" style="display:none">
		<input type="file" id="excelFile" name='file' onchange="fileChangeEvent(event)" style="float: left"/><div class='btn left importButton' id="importButton" onclick="importExcel()">上传</div>
	</form>
</div>
<br/>
<div class = "basic_info_box">
		<div  id = "basic_info_form" >
			<div class='btn right' onclick = "back()">返回</div>
			<div class='btn right' onclick = "importEvent()">导入</div>
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
<script src="<%=request.getContextPath()%>/js/plugins/datagrid/js/jquery-tool.datagrid.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- datagrid表格.js   -->
<script src="<%=request.getContextPath()%>/js/json2.js?verNo=<%=VersionUtils.verNo%>"></script>  <!-- IE支持 JSON格式   -->
<script src="<%=request.getContextPath()%>/js/plugins/bootstrap/js/bootstrap.min.js?verNo=<%=VersionUtils.verNo%>"></script>
<script src="<%=request.getContextPath()%>/js/stylePage/layer/layer.js?verNo=<%=VersionUtils.verNo%>"></script>    <!-- 弹框.js  -->
<script src="<%=request.getContextPath()%>/js/plugins/datebox/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/common/common.js?verNo=<%=VersionUtils.verNo%>"></script>

<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript">
$("#datagrid").datagrid({
    url: '/newtygl/laboratory/getInfoBaseMaintainImportList?rnd='+(Math.random()*99999999),
    type: 'POST',
    form: '#queryForm',
    showIndex:true,
    dataIndex:false,
    pageSize:999,
    tablepage:$(".tablepage1"),
    columns: [
        {name: '实验室名称',style:{width:"30%"}, data: 'LBNAME'},
		{name: '实验室英文名称',style:{width:"20%"}, data: 'LBENNAME'},
		{name: '实验室类型',style:{width:"10%"}, data: 'LBTYPE'},
		{name: '建设年份', style:{width:"10%"},data: 'LBYEAR'},
		{name: '实验室代码', style:{width:"10%"},data: 'LBCODE'},
		{name: '研究方向',style:{width:"15%"},data: 'LBDIRECTION'},
		{name: '实验室主任',style:{width:"10%"},data: 'LBLEADER'}
    ]
});
function checkFileEvent(){
	$("#excelFile").click();
}
function fileChangeEvent(e){
	$("#fileName").val(e.currentTarget.files[0].name);
}
function importEvent(e){
	$("#importButton").click();
}
function importExcel(){
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
          url:'/newtygl/laboratory/roomInfoImport',
          success:function(dataStr){
        	 var data=JSON.parse(dataStr);
        	 if(data.retcode!='success'){
        		 alert("导入错误："+data.errmsg);
        		 return;
        	 }else{
        		 if(data.isErr=="true"){
              	     alert("导入数据有错误，请确认!");
              	     $("#isErr").val(data.isErr);
              	     $("#oncecode").val("");
              	     var errHtml="";
              	     errHtml+="<table class='datagrid table table-bordered'><tr><th>序号</th><th>错误信息</th></tr>";
              	     for(var i=0;i<data.errData.length;i++){
              	    	errHtml+="<tr><td>"+(i+1)+"</td><td>"+data.errData[i].errmsg+"</td></tr>"; 
              	     }
              	     errHtml+="</table>";
              	     $("#datagrid").html(errHtml);
                 }else{
                	 alert("导入成功！");
     				 parent.layer.close(parent.layer.getFrameIndex(window.name));
                	/*  $("#isErr").val("");
                	 $("#oncecode").val(data.oncecode);
                	 $("#datagrid").datagrid({
                		    url: '/newtygl/infoBaseMaintain/getInfoBaseMaintainImportList?rnd='+(Math.random()*99999999),
                		    type: 'POST',
                		    form: '#queryForm',
                		    showIndex:true,
                		    dataIndex:false,
                		    pageSize:999,
                		    tablepage:$(".tablepage1"),
                		    columns: [
                		        {name: '标委会名称', data: 'SAC_NAME',style:{width:"200px"}},
                		        {name: '标委会类型', data: 'SAC_TYPE'},
                		        {name: '编号', data: 'SAC_NO'}
                		    ]
                		}); */
                	 //$("#datagrid").datagrid("seach");
                 }
        	 }
          },
          error:function(errmsg){
          	
          }
      });
}

function saveData(){
	var oncecode=$("#oncecode").val();
	var isErr=$("#isErr").val();
	if(isErr=='true'){
		alert("导入数据包含错误，无法保存！");
		return;
	}
	if(oncecode==null||oncecode==""){
		alert("没有导入数据，或数据为空，无法保存！");
		return;
	}
	$.ajax({
		url:"/newtygl/infoBaseMaintain/infoBaseMaintainImportSave",
		type:"POST",
		data:{oncecode:oncecode},
		success:function(data){
			if(data.retcode=='success'){
				alert("保存成功！");
				parent.layer.close(parent.layer.getFrameIndex(window.name));
			}else{
				alert("保存失败！");
			}
		},
		error:function(data2){
			alert("ERROR:保存失败！");
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