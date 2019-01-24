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

<title>项目工时统计</title>
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
	
</style>
</head>
<body>
<div class="page-header-sl">
	<h5>项目工时统计</h5>
	<div class="button-box">
		<button type="button" class="btn btn-info btn-xs" onclick="forConfirm()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px"   method="post" >
			<hidden name="uuid" property="uuid"></hidden>
			<input type="hidden" name="selectList"/>
			<div class="form-group col-xs-3">
				<label>统计报表：</label>
				<div class="controls"  data-date-format="yyyy-mm-dd">
					<select name="type">
					 
					    <option value="0">日报</option>
						<option value="1" selected='selected'>周报</option>
						<option value="2">月报</option>
						<option value="3">季报</option>
						<option value="4">年报</option>
						<option value="5">自定义</option>
						 
					</select>
				</div>
			</div>
			<div class="form-group col-xs-5" style="margin-bottom:0;">
				<label>统计日期：</label>
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
			<div class="form-group col-xs-3">
				<label>统计维度：</label>
				<div class="controls">
					<select name="status">
						<option value="0" selected='selected'>项目</option>
						<option value="1">人员</option>
					</select>
				</div>
			</div>
			<div class="form-group col-xs-3">
				<label>项目名称：</label>
				<div class="controls">
					<input name="projectName" property="projectName" >
				</div>
			</div>
			
			<div class="form-group col-xs-5 hidden"   id="username">
				<label>人员姓名：</label>
				<div class="controls" >
					<input name="userName" property="userName" >
				</div>
			</div>
				<!-- 年前不上，把计前期取消 -->
			<!-- <div class="form-group col-xs-4"  >
				<label>数据显示：</label>
				<div class="controls datashow">
					<div class='showcheck'><input type="checkbox" name="bpShow" value="1" /></div>checked="checked" 
					<div class="showText">项目计入项目前期</div>
				</div>	    
			</div> -->
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">统计</button>
	</div>
</div>
<div>
	<div class='table1'>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align:right;"></div>
	</div>
	<div class='table2' style='display:none'>
		<table id="mmg2" class="mmg2">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg2" style="text-align:right;"></div>
	</div>
</div>
</body>
<script type="text/javascript">

$("select[name=status]").change(function(){
	var status = $("select[name=status]").val();
 
	if(status==0){
		$("#username").addClass("hidden");
	}else{
		$("#username").removeClass("hidden");
	}
})
$("input[name=startTime]").val(common.getMonthFirstDay());
$("input[name=endTime]").val(common.getMonthEndDay());
var mmg,mmg2;
var pn = 1,pn2 = 1;
var limit = 30,limit2 = 30;
$(function(){
	init();
	queryListPro();
});

function init(){
	$(".form_date").datepicker({autoclose:true,todayHighlight:true, language: 'cn',orientation:'auto'});
}
function forSearch(){
	var statics = $("select[name=status]").val();
	
	if(statics=="0"){
		$(".table1").show();
		$(".table2").hide();
		pn = 1;
		queryListPro("reload");
	}else{
		$(".table1").hide();
		$(".table2").show();
		pn = 1;
		if($('.table2>.mmGrid').length>0){
			queryListPer("reload");
		}else{
			queryListPer();
		}
	}
}
// 初始化列表数据
function queryListPro(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'项目编号', name:'PROJECT_NUMBER', width:110, sortable:false, align:' center'},
	            //{title:'WBS编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'},
	            {title:'项目名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left',
	            	renderer:function(val,item,rowIndex){
	            		if(!item.PROJECT_ID){
	            			return "";
	            		}
            			return '<a href="###" title="'+val+'" onclick="forDetails(\''+item.PROJECT_ID+'\')">'+val+'</a>';
            		}		
	            },
	            {title:'统计周期', name:'StartAndEndData', width:100, sortable:false, align:'center'},
	            {title:'投入总工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.WORKING_HOUR == 0){
	            			return '0';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetails(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+0+'\' )">'+val+'</a>';
            		}	
	            }/* ,
	            {title:'项目工作投入工时(h)', name:'PRO_HOUR', width:150, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.PRO_HOUR == 0){
	            			return '0';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetails(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+1+'\' )">'+val+'</a>';
            		}	
	            },
	            {title:'项目前期投入工时(h)', name:'BP_HOUR', width:150, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.BP_HOUR == 0){
	            			return '0';
	            		}else if(item.BP_HOUR == '--'){
	            			return '--';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetails(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+2+'\' )">'+val+'</a>';
            		}	
	            } */
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg = $('#mmg').mmGrid({
		cosEdit:"4,6,7,8",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectForProjectName?ran='+ran,
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
//项目详情
function forDetails(proId){
	layer.open({
		type:2,
		title:"项目信息-查看",
		area:[ '865px','530px'],
		scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/project/pro_details?proId='+proId,'no']
	});
}
//项目工时详情
function forHourDetails(projectNumber,StartData,EndData,type){
	//var userName = escape($("input[name=userName]").val());
	//var projectNames=escape(projectName);
	var bpShow = $('input[name="bpShow"]').prop("checked") ? "1":"0";
	var projectNumber=escape(projectNumber);
    layer.open({
		type:2,
		title:"项目工时明细",
		area:['600', '50%'],
		scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/searchWorkTask/workinghourStaticDetail?projectNumber='+projectNumber+'&StartData='+StartData+'&EndData='+EndData+'&type='+type+'&bpShow='+bpShow,'no']
	});
}

//初始化列表数据
function queryListPer(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'项目编号', name:'PROJECT_NUMBER', width:110, sortable:false, align:' center'},
	            //{title:'WBS编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'},
	            {title:'项目名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left',
	            	renderer:function(val,item,rowIndex){
	            		if(!item.PROJECT_ID){
	            			return "";
	            		}
            			return '<a href="###" title="'+val+'" onclick="forDetails(\''+item.PROJECT_ID+'\')">'+val+'</a>';
            		}		
	            },
	            {title:'统计周期', name:'StartAndEndData', width:100, sortable:false, align:'center'},
	            {title:'项目投入总工时(h)', name:'StandartHoursNum', width:150, sortable:false, align:'center'},
	            {title:'人员编号', name:'HRCODE', width:100, sortable:false, align:'center'},
	            {title:'人员姓名', name:'USERALIAS', width:100, sortable:false, align:'center'},
	            {title:'员工投入工时(h)', name:'PRO_HOUR', width:150, sortable:false, align:'center',/*  title:'员工项目投入工时(h)'*/
	            	renderer:function(val,item,rowIndex){
	            		if(item.PRO_HOUR == 0){
	            			return '0';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetailA(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+1+'\' ,\''+item.HRCODE+'\',\''+item.WORKER+'\')">'+val+'</a>';
            		}	
	            }/* ,
	            {title:'员工项目前期投入工时(h)', name:'BP_HOUR', width:150, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.BP_HOUR == 0){
	            			return '0';
	            		}else if(item.BP_HOUR == '--'){
	            			return '--';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetailA(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+2+'\' ,\''+item.HRCODE+'\',\''+item.WORKER+'\')">'+val+'</a>';
            		}	
	            },
	            {title:'角色', name:'ROLE', width:100, sortable:false, align:'center'} */
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg2 = $('#mmg2').mmGrid({
		cosEdit:"4,9,10",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectForProjectName?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
				return $(".query-box").sotoCollecter();
			},
		plugins: [
			$("#pg2").mmPaginator({page:pn2, limit:limit2, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
			if(data.status==201){
				layer.msg(data.res);
			} 
			pn = data.page;
		});
	if(load == "reload"){
		mmg2.load({page:pn2});
	}
}
function forHourDetailA(projectNumber,StartData,EndData,type ,hrcode,userName){
	//var userName = escape($("input[name=userName]").val());
	//var projectNames=escape(projectName);
	var projectNumber=escape(projectNumber);
	var bpShow = $('input[name="bpShow"]').prop("checked") ? "1":"0";
	 
	 
    layer.open({
		type:2,
		title:"员工工时明细",
		area:['600', '50%'],
		scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/searchWorkTask/workinghourStaticDetails?projectNumber='+projectNumber+'&StartData='+StartData+'&EndData='+EndData+'&type='+type+'&userName='+userName+'&hrcode='+hrcode+'&bpShow='+bpShow,'no']
	});
}

//确认
function forConfirm(){
	var status = $("select[name=status]").val();
	var selectList ;
	if(status == "0"){
		selectList = mmg.selectedRows();
	}else{
		selectList = mmg2.selectedRows();
	}
	var ids = "";
	for(var i=0;i<selectList.length;i++){
		ids += (parseInt(selectList[i].Count))+",";
	}
	ids = ids.slice(0,ids.length-1);
	$("input[name=selectList]").val(ids);
	var ran = Math.random()*1000;
	document.forms[0].action ="<%=request.getContextPath()%>/BgWorkinghourInfo/selectForProjectNameReport?ran="+ran;
	document.forms[0].submit();	
}

</script>
</html>
	