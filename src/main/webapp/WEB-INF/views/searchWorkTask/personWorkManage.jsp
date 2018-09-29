<!DOCTYPE>
<!-- authentication_index.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%-- <%
	String path = ToolsUtil.getContextPath(request);
%> --%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>个人工时管理</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/css/bootstrap-datepicker.min.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/common/css/style.css">

<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmGrid.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/mmGrid/src/mmPaginator.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/organ-tree/organ-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/stuff-tree/stuff-tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">
	.input-group{
		float:left;
    	width: 45%;
	}
	.floatLeft{
		float:left;
		width:10%;
		text-align:center;
	}
	.input-groups{
		width: 100%;	
	}
	.updateManage,.deleteManage{
		cursor:pointer;
		color:blue;
		padding:0 10px;
	}
	.updateManage,.backManage{
		cursor:pointer;
		color:blue;
		padding:0 10px;
	}
</style>
</head>
<body>
<div class="page-header-sl">
	<div class="button-box">
	<!-- 
	   <button type="button" class="btn btn-success btn-xs" onclick="workUpdate()"> 修改</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workDelete()"> 删除</button>
	 -->
	    
		<button type="button" class="btn btn-success btn-xs" onclick="workCommit()"> 提交</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workExport()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px"  method="post">
		<hidden name="uuid" property="uuid"></hidden>
		<input type="hidden" name="selectList"/>
		<div class="form-group col-xs-6" style="margin-bottom:0;">
			<label>查询日期：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="startTime" property="startTime"  readonly="true" placeholder='开始时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
				<div class="floatLeft">--</div>
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input name="endTime" property="endTime"  readonly="true" placeholder='结束时间'>
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		
		<div class="form-group col-xs-5">
			<label>类型：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<select name="category">
					<option></option>
					<option value="KY">科研项目</option>
					<option value="JS">技术服务项目</option>
					<option value="HX">横向项目</option>
					<option value="NP">非项目工作</option>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-6">
			<label>项目名称：</label>
			<div class="controls">
				<input name="projectName" property="projectName" >
			</div>
		</div>
		<div class="form-group col-xs-5">
			<label>状态：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<select name="status">
					<option></option>
					<option value="0">未提交</option>
					<option value="1">审批中</option>
					<option value="2">已退回</option>
					<option value="3">已通过</option>
				</select>
			</div>
		</div>
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">查询</button>
	</div>
</div>
<div>
	<table id="mmg" class="mmg">
		<tr>
			<th rowspan="" colspan=""></th>
		</tr>
	</table>
	<div id="pg" style="text-align:right;"></div>
</div>
</body>
<script type="text/javascript">
var data=new Date();
 

var year=data.getFullYear()
var month=data.getMonth()+1>=10?(data.getMonth()+1):"0"+(data.getMonth()+1);
var day=data.getDate()>=10?(data.getDate()):"0"+(data.getDate());
var newdata =year+"-"+month+"-"+day ;
$("input[name=startTime]").val(getWeek(0));
$("input[name=endTime]").val(getWeek(-6));
function getWeek(n){
	//查询本周的开始时间和结束时间    n=0查询本周开始时间   n=-6查询本周结束时间
	var now = new Date();
	var year = now.getFullYear();
	var month = now.getMonth+1;
	var date = now.getDate();
	var day = now.getDay();
	if(day!=0){
		n=n+day-1;
	}else{
		n=n+day;
	}
	if(month>1){
		month = month;
	}else{
		month=12;
		year=year-1;
	}
	now.setDate(now.getDate()-n);
	year = now.getFullYear();
	month = now.getMonth()+1;
	date = now.getDate();
	var s = year +"-"+(month<10?('0'+month):month)+"-"+(date<10?('0'+date):date);
	return s;
}
var mmg;
var pn = 1;
var limit = 30;
$(function(){
	init();
	queryList();
});

function init(){
	$(".form_date").datepicker({autoclose:true,language: 'cn',todayHighlight:true ,orientation:'auto'});
	$("#stuffTree").stuffTree({root:'41000001',empCode:'empCode',empName:'empName',iframe:'parent'});
}
function forSearch(){
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'日期', name:'WORK_TIME', width:100, sortable:false, align:'center'},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'项目名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
	            {title:'工作内容', name:'JOB_CONTENT', width:100, sortable:false, align:'left'},
	            {title:'投入工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center'},
	            {title:'审核人', name:'USERALIAS', width:100, sortable:false, align:'center'},
	            {title:'审核结果', name:'STATUS', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var status;
	            		switch (parseInt(val)){
	            		case 0:
	            		  status="未提交";
	            		  break;
	            		case 1:
	            		  status="审批中";
	            		  break;
	            		case 2:
	            		  status="已退回";
	            		  break;
	            		case 3:
	            		  status="已通过";
	            		  break;
	            		default:
	            		  status="";
	            		}
	            		return status;
	            	}},
	            	{title:'审核备注', name:'PROCESS_NOTE', width:100, sortable:false, align:'left'},
	            	{title:'操作', name:'aa', width:100, sortable:false, align:'center',renderer:function(title,row){
	            		if(row.STATUS==1){
	            			return "<span class='backManage' id='"+row.ID+"'>撤回</span>"
	            		}else if(row.STATUS==0){
	            			return "<span class='updateManage' id='"+row.ID+"'>修改</span><span class='deleteManage' id='"+row.ID+"'>删除</span>"
	            		}else if(row.STATUS==2){
	            			return "<span class='updateManage' id='"+row.ID+"'>修改</span><span class='deleteManage' id='"+row.ID+"'>删除</span>"
	            		}else{
	            			return ""
	            		}
	            		
	            	}},
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg = $('#mmg').mmGrid({
		cosEdit:"11",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectForbgWorkinghourInfo?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			},
		plugins: [
			$("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
		 
			$(".checkAll").css("display","none").parent().text("选择");
			
			if(data.status==201){
				layer.msg(data.res);
			} 
			pn = data.page;
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}
 

//提交
function workCommit(){
	var ids = "";
	var xhs = "";
	var noxhs = "";
	var  selectList= mmg.selectedRows();
	if(selectList.length > 0){
		for(var i=0;i<selectList.length;i++){
			var status=selectList[i].STATUS;
			if(status==0||status==2){
				ids += selectList[i].ID+",";
				xhs += selectList[i].ROW_ID+",";
			}else{
				noxhs += selectList[i].ROW_ID+",";
				 
			}
		}
	}else{
		return	layer.msg("请选择一条数据!");
	}
	
	 if(noxhs!=""){
		noxhs = noxhs.slice(0,noxhs.length-1);
		return 	layer.msg("序号 为"+noxhs+"行的审核结果不为“未提交/已退回”,审核结果为“未提交/已退回”才可以提交");
	 }
	ids = ids.slice(0,ids.length-1);
	xhs = xhs.slice(0,xhs.length-1);
	$.ajax({
		type: 'POST',
		url:'<%=request.getContextPath()%>/BgWorkinghourInfo/savebgWorkinghourInfo',
		async: false,
		data: {id:ids,xh:xhs},
		success:function(data){
			if(data.success == "true"){
				layer.msg(data.msg);
				mmg.load();  
			}else{
				 layer.msg(data.msg);
				 mmg.load();
			}
		}
	}); 
}
$("#mmg").on("click",".updateManage",function(){ 
	var id=$(this).attr("id");
	layer.open({
		type:2,
		title:"修改页面",
		area:['620px', '460px'],
		scrollbar:false,
		content:['/bg/searchWorkTask/workManageUpdate?id='+id,'no'],
	 	end:function(){
	 		mmg.load();
	 	}
	});
});
function workUpdate(){
	var rows = mmg.selectedRows();
	if(rows.length == 1){
		var id=rows[0].ID
		var status=rows[0].STATUS;
		if(status==1){
			layer.msg("无法修改审批中或已通过信息!");
		}else if(status==3){
			layer.msg("无法修改审批中或已通过信息!");
		}else{
			 layer.open({
					type:2,
					title:"修改页面",
					area:['620px', '460px'],
					scrollbar:false,
					content:['/bg/searchWorkTask/workManageUpdate?id='+id,'no'],
				});
		}
		
	}else{
		layer.msg("请选择一条数据!");
	}
}
 
$("body").on("click",".deleteManage",function(){
	var id=$(this).attr("id");
	layer.confirm("确认删除吗?",{icon:3,title:"提示"},function(index){
		$.ajax({
			type: 'POST',
			url:'<%=request.getContextPath()%>/BgWorkinghourInfo/deletebgWorkinghourInfo',
			async: false,
			data: {id:id},
			success:function(data){
				layer.msg(data.msg);
				mmg.load();
				layer.close(index);
			}
		}); 
	})
});
$("body").on("click",".backManage",function(){
	var id=$(this).attr("id");
	layer.confirm("确认撤回吗?",{icon:3,title:"提示"},function(index){
		$.ajax({
			type: 'POST',
			url:'<%=request.getContextPath()%>/BgWorkinghourInfo/backbgWorkinghourInfo',
			async: false,
			data: {id:id},
			success:function(data){
				if(data.success == "true"){
					layer.msg(data.msg);
				 	mmg.load();	  
				 	layer.close(index);
				}else{
					layer.msg(data.msg);
				 	mmg.load();
				 	layer.close(index);
				}
			}
		}); 
	})
});
//导出
function workExport(){
	var ids="";
	var selectList = mmg.selectedRows();
	var reason = $(".reason").val();
	for(var i=0;i<selectList.length;i++){
		ids += selectList[i].ID+",";
	}
		$("input[name=selectList]").val(JSON.stringify(selectList));
		var ran = Math.random()*1000;
		document.forms[0].action ="<%=request.getContextPath()%>/BgWorkinghourInfo/exportExcelForpersonWorkManage?ran="+ran+"&id="+ids;
		document.forms[0].submit();
}
</script>
</html>
	