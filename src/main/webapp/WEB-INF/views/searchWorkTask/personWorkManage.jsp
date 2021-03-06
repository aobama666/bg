<!DOCTYPE>
<!-- authentication_index.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%-- <%@page import="crpri.ess.util.ToolsUtil"%>
<%@page import="crpri.ess.util.JsonUtil"%> --%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>工时管理</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>
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
	<h5>工时管理</h5>
	<div class="button-box">
	<!-- 
	   <button type="button" class="btn btn-success btn-xs" onclick="workUpdate()"> 修改</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workDelete()"> 删除</button>
	 -->
		<button type="button" class="btn btn-warning btn-xs" onclick="workDelete()">删除</button>
		<button type="button" class="btn btn-success btn-xs" onclick="workCommit()"> 提交</button>
		<button type="button" class="btn btn-info  btn-xs" onclick="workExport()"> 导出</button>
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
				<%--<div class="controls"  data-date-format="yyyy-mm-dd">
					<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
						<input name="startTime" property="startTime"  readonly="true" placeholder='开始时间'>
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<div class="floatLeft">--</div>
					<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
						<input name="endTime" property="endTime"  readonly="true" placeholder='结束时间'>
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>--%>
				<div class="controls"  data-date-format="yyyy-mm">
					<div class="input-group date form_date bg-white" id="dateTime">
						<input  name="startTime" property="startTime" type="text" class="form-control form_datetime_2 input-sm bg-white" readonly />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
					</div>
					<div class="floatLeft">--</div>
					<div class="input-group date form_date bg-white" id="dateTimeEnd">
						<input  name="endTime" property="endTime" type="text" class="form-control form_datetime_2 input-sm bg-white" readonly />
						<span class="input-group-addon">
							<span class="glyphicon glyphicon-calendar"></span>
						</span>
					</div>
				</div>
			</div>
			<div class="form-group col-xs-5">
				<label>类型：</label>
				<div class="controls"  data-date-format="yyyy-mm-dd">
					<select name="category">
						<option></option>
						<c:forEach var ="dict" items="${categoryMap}">
							<option value=${dict.key}>${dict.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form-group col-xs-6">
				<label>任务名称：</label>
				<div class="controls">
					<input name="projectName" property="projectName" >
				</div>
			</div>
			<div class="form-group col-xs-5">
				<label>状态：</label>
				<div class="controls"  data-date-format="yyyy-mm-dd">
					<select name="status">
						<option></option>
						<c:forEach var ="dict" items="${statusMap}">
							<option value=${dict.key}>${dict.value}</option>
						</c:forEach>
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

    var date = new Date();
    var startYear = date.getFullYear();
    var startMonth=date.getMonth()+1>=10?(date.getMonth()+1):"0"+(date.getMonth()+1);
    var endMonth=date.getMonth()+2>=10?(date.getMonth()+2):"0"+(date.getMonth()+2);
    var endYear = startYear;
    if(endMonth==13){
        yearEnd = parseInt(startYear)+1;
        endMonth="0"+1;
	}
    var start = startYear+"-"+startMonth;
    var end = endYear+"-"+endMonth;
    $("input[name=startTime]").val(start);
    $("input[name=endTime]").val(start);

    function Timeinit() {
        // 时间初始化
        $("#dateTime").datepicker({
            startView: 'months',  //起始选择范围
            maxViewMode:'years', //最大选择范围
            minViewMode:'months', //最小选择范围
            todayHighlight : true,// 当前时间高亮显示
            autoclose : 'true',// 选择时间后弹框自动消失
            format : 'yyyy-mm',// 时间格式
            language : 'zh-CN',// 汉化
			//todayBtn:"linked",//显示今天 按钮
            //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
        });
        $("#dateTimeEnd").datepicker({
            startView: 'months',  //起始选择范围
            maxViewMode:'years', //最大选择范围
            minViewMode:'months', //最小选择范围
            todayHighlight : true,// 当前时间高亮显示
            autoclose : 'true',// 选择时间后弹框自动消失
            format : 'yyyy-mm',// 时间格式
            language : 'zh-CN',// 汉化
            //todayBtn:"linked",//显示今天 按钮
            //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
        });
    }

/*var data=new Date();
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
}*/

var mmg;
var pn = 1;
var limit = 30;
$(function(){
	//init();
    Timeinit();
	queryList();
});

/*function init(){
	$(".form_date").datepicker({autoclose:true,language: 'cn',todayHighlight:true ,orientation:'auto'});
	$("#stuffTree").stuffTree({root:'41000001',empCode:'empCode',empName:'empName',iframe:'parent'});
}*/
function forSearch(){

    var startTime = $("input[name=startTime]").val();
    var endTime = $("input[name=endTime]").val();
    if(startTime>endTime){
        layer.msg("查询时间范围：开始时间大于结束时间！");
        return;
	}

	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            /*{title:'日期', name:'WORK_TIME', width:100, sortable:false, align:'center'},*/
	            {title:'开始日期', name:'WORK_TIME_BEGIN', width:100, sortable:false, align:'center'},
	            {title:'结束日期', name:'WORK_TIME_END', width:100, sortable:false, align:'center'},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'任务名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
	            {title:'工作内容简述', name:'JOB_CONTENT', width:100, sortable:false, align:'left'},
	            {title:'投入工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center'},
	            {title:'审核人', name:'USERALIAS', width:100, sortable:false, align:'center'},
	            {title:'审核结果', name:'STATUS', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var dict=${statusJson};
	            		return dict[val];
	            	}},
	            	{title:'审核意见', name:'PROCESS_NOTE', width:100, sortable:false, align:'left'},
	            	{title:'操作', name:'aa', width:100, sortable:false, align:'center',renderer:function(title,row){
	            		if(row.STATUS==1){
	            			return "<span class='backManage' id='"+row.ID+"'>撤回</span>"
	            		}else if(row.STATUS==0){
	            			return "<span class='updateManage' id='"+row.ID+"' date='"+row.WORK_TIME_BEGIN+"'>修改</span>"
	            		}else if(row.STATUS==2){
	            			return "<span class='updateManage' id='"+row.ID+"' date='"+row.WORK_TIME_BEGIN+"'>修改</span>"
	            		}else{
	            			return ""
	            		}
	            		/* else if(row.STATUS==0){
	            			return "<span class='updateManage' id='"+row.ID+"'>修改</span><span class='deleteManage' id='"+row.ID+"'>删除</span>"
	            		}else if(row.STATUS==2){
	            			return "<span class='updateManage' id='"+row.ID+"'>修改</span><span class='deleteManage' id='"+row.ID+"'>删除</span>"
	            		} */
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
		 
			//$(".checkAll").css("display","none").parent().text("选择");
			
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
    debugger
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
		return 	layer.msg("序号 为"+noxhs+"行的审核结果不为“未提交/已驳回”,审核结果为“未提交/已驳回”才可以提交");
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
    delayDate = $(this).attr("date");
	layer.open({
		type:2,
		title:"修改",
		area:['620px', '460px'],
		scrollbar:false,
		content:['/bg/searchWorkTask/workManageUpdate?id='+id+'&selectedDate='+delayDate,'no'],
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
 
<%-- $("body").on("click",".deleteManage",function(){
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
}); --%>

function workDelete(){
	var rows = mmg.selectedRows();
	if(rows.length == 0){
		layer.msg("请至少选择一条数据!");
		return;
	}
	
	var params = {};
	var index = [];
	
	$.each(rows,function(i,row){
		var status = row.STATUS;
		if(status=='1' || status=='3'){
			index.push(row.ROW_ID);
		}
		params[row.ID] = row.ROW_ID+'';
	});
	
	if(index.length>0){
		layer.msg("第"+index.toString()+"行已提交或通过，无法删除！");
		return;
	}
	
	var paramStr = JSON.stringify(params);
	
	$.ajax({
		type: 'POST',
		url:'<%=request.getContextPath()%>/BgWorkinghourInfo/deletebgWorkinghourInfo',
		async: false,
		data: {paramStr:paramStr},
		success:function(data){
			layer.msg(data.msg);
			mmg.load();
			layer.close(index);
		}
	}); 
	/* layer.confirm("确认删除吗?",{icon:3,title:"提示"},function(index){
	}); */
}

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
	