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
	<script type="text/javascript" src="<%=request.getContextPath() %>/common/plugins/bootstrap-datepicker-master/dist/locales/bootstrap-datepicker.zh-CN.min.js"></script>

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
					 
					    <%--<option value="0">日报</option>--%>
						<%--<option value="1" selected='selected'>周报</option>--%>
						<option value="2"  selected='selected' >月报</option>
						<option value="3">季报</option>
						<option value="4">年报</option>
						<option value="5">自定义</option>
						 
					</select>
				</div>
			</div>
			<div class="form-group col-xs-5" style="margin-bottom:0;">
				<label>统计日期：</label>
				<%--<div class="controls"  data-date-format="yyyy-mm-dd">
					<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
						<input name="startTime"  id="startTime" property="startTime"  readonly="true" placeholder='开始时间'>
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<div class="floatLeft">--</div>
					<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
						<input name="endTime" property="endTime"  id="endTime" readonly="true" placeholder='结束时间'>
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>--%>
				<div class="controls"  data-date-format="yyyy-mm">
					<div class="input-group date form_date bg-white" id="startdateTime"　data-date-format="yyyy-mm" >
						<input id="startTime" name="startTime" property="startTime"   type="hidden"  >
						<input  id="startTimes" name="startTimes" property="startTimes"  type="text"  class="form-control form_datetime_2 input-sm bg-white"   readonly  />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
					<div class="floatLeft">--</div>
					<div class="input-group date form_date bg-white" id="enddateTime"　data-date-format="yyyy-mm" >
						<input id="endTime" name="endTime" property="endTime"  type="hidden"  >
						<input  id="endTimes" name="endTimes" property="endTimes" type="text"  class="form-control form_datetime_2 input-sm bg-white"  readonly    />
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
			<div class="form-group col-xs-4"  >
				<!-- <label>数据显示：</label> -->
				<div class="datashow" style="margin-left:33px">
					<div class='showcheck'><input type="checkbox" name="bpShow" checked="checked" value="1" /></div> 
					<div class="showText">项目前期计入项目工时</div>
				</div>	    
			</div>
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
    Timeinit();
    var   date = new Date();
    var   month=date.getMonth()+1;
    var months;
    if(month<10){
        months="0"+month;
    }else {
        months=month;
	}
    var   newdate=date.getFullYear()+"-"+months;
    $("#startTimes").val(newdate);
    $("#endTimes").val(newdate);
	$(".form_date").datepicker({autoclose:true,todayHighlight:true, language: 'cn',orientation:'auto'});
}

function Timeinit() {
    // 时间初始化
    $("#startdateTime").datepicker({
        startView: 'months',  //起始选择范围
        maxViewMode:'years', //最大选择范围
        minViewMode:'months', //最小选择范围
        todayHighlight : true,// 当前时间高亮显示
        autoclose : 'true',// 选择时间后弹框自动消失
        format : 'yyyy-mm',// 时间格式
        language : 'zh-CN',// 汉化
        // todayBtn:"linked",//显示今天 按钮
        //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
    });
    $("#enddateTime").datepicker({
        startView: 'months',  //起始选择范围
        maxViewMode:'years', //最大选择范围
        minViewMode:'months', //最小选择范围
        todayHighlight : true,// 当前时间高亮显示
        autoclose : 'true',// 选择时间后弹框自动消失
        format : 'yyyy-mm',// 时间格式
        language : 'zh-CN',// 汉化
        // todayBtn:"linked",//显示今天 按钮
        //clearBtn : true,// 清除按钮，和今天 按钮只能显示一个
    });
}
//获取结束时间的
function getEndD(endDate) {
    var   eDate = new Date(endDate);
    //alert(endDate);
    var   date=new Date(eDate.getFullYear(),eDate.getMonth()+1,0);
    var   days=date.getDate();
    return days;
}

//获得开始时间
function timeBegin (dateStr) {
    var dateStr=dateStr+"-01";
    var date=new Date(dateStr.replace(/-/g,"\/"));
    var year = date.getFullYear();
    var month = date.getMonth();
    var firstDay=new Date(year,month,1);//这个月的第一天
    var currentMonth=firstDay.getMonth(); //取得月份数
    var nextMonthFirstDay=new Date(firstDay.getFullYear(),currentMonth+1,1);//加1获取下个月第一天
    var dis=nextMonthFirstDay.getTime()-24*60*60*1000;//减去一天就是这个月的最后一天
    var lastDay=new Date(dis);
    var time =dateFtt("yyyy-MM-dd",firstDay);//格式化 //这个格式化方法要用你们自己的，也可以用本文已经贴出来的下面的Format
    var timeEnd=dateFtt("yyyy-MM-dd",lastDay);//格式化
    return time;
}

//获得结束时间
function timeEnd(dateStr) {
    var dateStr=dateStr+"-01";
    var date=new Date(dateStr.replace(/-/g,"\/"));
    var year = date.getFullYear();
    var month = date.getMonth();
    var firstDay=new Date(year,month,1);//这个月的第一天
    var currentMonth=firstDay.getMonth(); //取得月份数
    var nextMonthFirstDay=new Date(firstDay.getFullYear(),currentMonth+1,1);//加1获取下个月第一天
    var dis=nextMonthFirstDay.getTime()-24*60*60*1000;//减去一天就是这个月的最后一天
    var lastDay=new Date(dis);
    var time =dateFtt("yyyy-MM-dd",firstDay);//格式化 //这个格式化方法要用你们自己的，也可以用本文已经贴出来的下面的Format
    var timeEnd=dateFtt("yyyy-MM-dd",lastDay);//格式化
    return timeEnd;
}

function dateFtt(fmt,date)
{ //author: meizz
    var o = {
        "M+" : date.getMonth()+1,     //月份
        "d+" : date.getDate(),     //日
        "h+" : date.getHours(),     //小时
        "m+" : date.getMinutes(),     //分
        "s+" : date.getSeconds(),     //秒
        "q+" : Math.floor((date.getMonth()+3)/3), //季度
        "S" : date.getMilliseconds()    //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

//统计
function forSearch(){
	var statics = $("select[name=status]").val();
    /*var  startDate =$("#startTimes").val()+"-01";
    var  endDate=$("#endTimes").val()+"-01";
    var  days=getEndD(endDate);*/

    var  startDate =timeBegin($("#startTimes").val());
    var  endDate=timeEnd($("#endTimes").val());

    if(startDate==""){
        layer.msg("开始时间不能为空");
        return ;
    }
    if(endDate==""){
        layer.msg("结束时间不能为空");
        return ;
    }
    if((new Date(endDate.replace(/-/g,"\/")))<(new Date(startDate.replace(/-/g,"\/")))){
        layer.msg("结束时间必须大于开始时间");
        return ;
    }
    /*var  falg=getD(startDate, endDate);
    if(!falg){
        layer.msg("结束时间大等于开始时间的一个月的时间");
        return ;
    }*/
    $("#startTime").val(startDate);
    $("#endTime").val(endDate);
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
//比较两个时间是否大于一个月，例如20170215--到20170315 是一个月，到20170316是大于一个月
function getD(sDate, endDate) {

    var sDate = new Date(sDate);
    var eDate = new Date(endDate);
    if (eDate.getFullYear() - sDate.getFullYear() > 1) {//先比较年
        return true;
    } else if (eDate.getMonth() - sDate.getMonth() > 1) {//再比较月
        return true;
    } else if (eDate.getMonth() - sDate.getMonth() == 1) {
        if (eDate.getDate() - sDate.getDate() >= 0) {
            return true;
        }
    }else if (eDate.getMonth() - sDate.getMonth() == 0) {
        var   date=new Date(sDate.getFullYear(),sDate.getMonth()+1,0);
        var   days=date.getDate();
        var   numdays=eDate.getDate() - sDate.getDate()+1;
        if (numdays==days) {
            return true;
        }
    } else if (eDate.getFullYear() - sDate.getFullYear() == 1) {
        if (eDate.getMonth()+12 - sDate.getMonth() > 1) {
            return true;
        }
        else if (eDate.getDate() - sDate.getDate() >= 1) {
            return true;
        }
    }
    return false;
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
	            /* {title:'投入总工时(h)', name:'PRO_HOUR', width:150, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.PRO_HOUR == 0){
	            			return '0';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetails(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+1+'\' )">'+val+'</a>';
            		}	
	            } */
	            {title:'投入总工时(h)', name:'WORKING_HOUR', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.WORKING_HOUR == 0){
	            			return '0';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetails(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+0+'\' )">'+val+'</a>';
            		}	
	            },
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
	            } 
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
	var height=$(window).height()*0.9;
	if(height>570) height = 570;
	layer.open({
		type:2,
		title:"项目信息-查看",
		area:[ '865px',height+'px'],
		scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/project/pro_details?proId='+proId]
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
		area:['50%', '80%'],
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
	            {title:'人员编号', name:'HRCODE', width:85, sortable:false, align:'center'},
	            {title:'人员姓名', name:'USERALIAS', width:85, sortable:false, align:'center'},
	            /* {title:'投入总工时(h)', name:'PRO_HOUR', width:150, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.PRO_HOUR == 0){
	            			return '0';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetailA(\''+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+1+'\' ,\''+item.HRCODE+'\',\''+item.WORKER+'\')">'+val+'</a>';
            		}	
	            }, */
	            {title:'员工项目投入工时(h)', name:'PRO_HOUR', width:150, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.PRO_HOUR == 0){
	            			return '0';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetailA(\''
            					+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+1+'\' ,\''+item.HRCODE+'\',\''+item.PROJECT_NAME+'\' ,\''+item.USERALIAS+'\',\''+item.WORKER+'\')">'+val+'</a>';
            		}	
	            },
	            {title:'员工项目前期投入工时(h)', name:'BP_HOUR', width:180, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		if(item.BP_HOUR == 0){
	            			return '0';
	            		}else if(item.BP_HOUR == '--'){
	            			return '--';
	            		}
            			return '<a href="javascript:void(0)" title="'+val+'" onclick="forHourDetailA(\''
            					+item.PROJECT_NUMBER+'\',\''+item.StartData+'\',\''+item.EndData+'\' ,\''+2+'\' ,\''+item.HRCODE+'\',\''+item.PROJECT_NAME+'\' ,\''+item.USERALIAS+'\',\''+item.WORKER+'\')">'+val+'</a>';
            		}	
	            }
	            //{title:'角色', name:'ROLE', width:100, sortable:false, align:'center'}
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
function forHourDetailA(projectNumber,StartData,EndData,type ,hrcode,projectName,userAlias,userName){
	//var userName = escape($("input[name=userName]").val());
	//var projectNames=escape(projectName);
	var projectNumber=escape(projectNumber);
	var bpShow = $('input[name="bpShow"]').prop("checked") ? "1":"0";
	var detail=escape(StartData+"至"+EndData+"　　"+projectName+"　　"+userAlias);
	
    layer.open({
		type:2,
		title:"员工工时明细",
		area:['50%', '80%'],
		scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/searchWorkTask/workinghourStaticDetails?projectNumber='
				+projectNumber+'&StartData='+StartData+'&EndData='+EndData+'&type='+type+'&userName='+userName+'&hrcode='+hrcode+'&bpShow='+bpShow+'&detail='+detail,'no']
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
	