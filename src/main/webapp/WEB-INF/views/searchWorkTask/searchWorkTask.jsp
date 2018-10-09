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

<title>工作任务查询</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
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
	<div class="button-box">
		<button type="button" class="btn btn-info btn-xs" onclick="forExport()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px">
		<hidden name="uuid" property="uuid"></hidden>
		<input type="hidden" name="selectList"/>
		<div class="form-group col-xs-5"  style="margin-bottom:0;">
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
		<div class="form-group col-xs-3">
			<label>类型：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<select name="type">
					<option value=""></option>
					<option value="KY">科研项目</option>
					<option value="HX">横向项目</option>
					<option value="JS">技术服务项目</option>
					<option value="NP">非项目工作</option>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>项目名称：</label>
			<div class="controls">
				<input name="projectName" property="projectName" >
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
var mmg;
var pn = 1;
var limit = 30;
$(function(){
	$("input[name=startTime]").val(dateRangeUtil.getMonthFirstDay());
	$("input[name=endTime]").val(dateRangeUtil.getMonthEndDay());
	init();
	queryList();
});



var dateRangeUtil = (function () {
  
    this.getCurrentDate = function () {
	var data=new Date();
        return  data;
    };
    
    this.getMonthFirstDay = function () {
       
        
        var currentDate = this.getCurrentDate();
        
        var currentMonth = currentDate.getMonth();
       
        var currentYear = currentDate.getFullYear();
        
        var firstDay = new Date(currentYear, currentMonth, 1);
	   
	    var d = new Date(firstDay);  
	    var day=d.getDate();
		if(0<day&&day<10){
	     day="0"+day ;
	    }else{
	     day=day ;
	    }
		var month=(d.getMonth() + 1);
        if(0<month&&month<10){
	     month="0"+month ;
	    }else{
	     month=month ;
	    }
		
 	    var d = new Date(firstDay);  
	    var youWant=d.getFullYear() + '-' +  month + '-' + day  ; 
       
        return youWant;
    };
  
    this.getMonthEndDay = function () {
         
        var currentDate = this.getCurrentDate();
           
        var currentMonth = currentDate.getMonth();
        
        var currentYear = currentDate.getFullYear();
       
        if (currentMonth == 11) {
            currentYear++;
            currentMonth = 0;  
        } else {
            
            currentMonth++;
        }
        
        var millisecond = 1000 * 60 * 60 * 24;
        
        var nextMonthDayOne = new Date(currentYear, currentMonth, 1);
        
        var lastDay = new Date(nextMonthDayOne.getTime() - millisecond);
 
       var d = new Date(lastDay);  
	   var month=(d.getMonth() + 1);
	   if(0<month&&month<10){
	     month="0"+month ;
	   }else{
	     month=month ;
	   }
	  
	   
	   var youWant=d.getFullYear() + '-' + month + '-' + d.getDate() ; 
         
        return youWant;

    };
  
    
    return this;
    
})();







function init(){
	$(".form_date").datepicker({autoclose:true,todayHighlight:true,language: 'cn',clearBtn:true,orientation:'auto'});
}
function forSearch(){
	var startDate =$("input[name=startTime]").val();
	var endDate=$("input[name=endTime]").val();
    if((new Date(endDate.replace(/-/g,"\/")))<(new Date(startDate.replace(/-/g,"\/")))){
	   layer.msg("结束时间必须大于开始时间");
	   return ;
    }
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'项目类型', name:'CATEGORY', width:100, sortable:false, align:'center'},
	            {title:'WBS编号/项目编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'},
	            {title:'项目名称', name:'PROJECT_NAME', width:100, sortable:false, align:'left'},
	            {title:'项目开始时间', name:'START_DATE', width:100, sortable:false, align:'center'},
	            {title:'项目结束时间', name:'END_DATE', width:100, sortable:false, align:'center'},
	            {title:'项目负责人', name:'PRINCIPAL', width:100, sortable:false, align:'center'},
	            {title:'项目状态', name:'PROJECT_STATUS', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var status;
	            		switch (parseInt(val)){
	            		case 0:
	            		  status="未启动";
	            		  break;
	            		case 1:
	            		  status="进行中";
	            		  break;
	            		case 2:
	            		  status="暂停";
	            		  break;
	            		case 3:
	            		  status="已结束";
	            		  break;
	            		case 4:
	            		  status="废止";
	            		  break;
	            		default:
	            		  status="";
	            		}
	            		return status;
	            	}},
	            {title:'本人参与开始时间', name:'PERSONSTART', width:100, sortable:false, align:'center'},
	            {title:'本人参与结束时间', name:'PERSONEND', width:100, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height() - $(".query-box").height()-$("#pg").height()-140;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/searchWorkTask/search?ran='+ran,
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
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}

function forExport(){
	var selectList = mmg.selectedRows();
	var ids = "";
	if(selectList.length>0){
		for(var i=0;i<selectList.length;i++){
			ids += selectList[i].USERID+",";
		}
		ids = ids.slice(0,ids.length-1);
	}
	$("input[name=selectList]").val(ids);
	var ran = Math.random()*1000;
	document.forms[0].method="post";
	document.forms[0].action ="<%=request.getContextPath()%>/searchWorkTask/exportExcel?ran="+ran;
	document.forms[0].submit();
}
</script>
</html>
	