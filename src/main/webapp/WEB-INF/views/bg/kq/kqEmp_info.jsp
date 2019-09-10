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

<title>员工工时查询</title>
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
	<h5>员工工时查询</h5>
	<div class="button-box">
		<button type="button" class="btn btn-success btn-xs" onclick="forSyncDate()"> 数据同步</button>
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
				<label>员工姓名：</label>
				<div class="controls">
					<input name="empName" property="empName" >
				</div>
			</div>

			<div class="form-group col-xs-5" style="margin-bottom:0;">
				<label>查询日期：</label>
				<div class="controls"  data-date-format="yyyy-mm">
					<div class="input-group date form_date bg-white" id="KqdateTime"　data-date-format="yyyy-mm" >
						<input id="startTime" name="startTime" property="startTime"   type="hidden"  >
						<input id="endTime" name="endTime" property="endTime"   type="hidden"  >
						<input  id="kqTime" name="kqTime" property="kqTime"  type="text"  class="form-control form_datetime_2 input-sm bg-white"   readonly  />
						<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
					</div>
				</div>
			</div>

		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">查询</button>
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
var mmg ;
var pn = 1 ;
var limit = 30 ;

$(function(){
    Timeinit();
	init();
	queryListPro();
});

function init(){
    $("input[name=startTime]").val(common.getMonthFirstDay());
    $("input[name=endTime]").val(common.getMonthEndDay());
    var   date = new Date();
    var   month=date.getMonth()+1;
    var months;
    if(month<10){
        months="0"+month;
    }
    var   newdate=date.getFullYear()+"-"+months;
    $("#kqTime").val(newdate);
	$(".form_date").datepicker({autoclose:true,todayHighlight:true, language: 'cn',orientation:'auto'});
}

function Timeinit() {
    // 时间初始化
    $("#KqdateTime").datepicker({
        startView: 'months',  //起始选择范围
        maxViewMode:'years', //最大选择范围
        minViewMode:'months', //最小选择范围
        todayHighlight : true,// 当前时间高亮显示
        autoclose : 'true',// 选择时间后弹框自动消失
        format : 'yyyy-mm',// 时间格式
        language : 'zh-CN',// 汉化
    });
}
//获取结束时间的
function getEndD(endDate) {
    var   eDate = new Date(endDate);
    var   date=new Date(eDate.getFullYear(),eDate.getMonth()+1,0);
    var   days=date.getDate();
    return days;
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
function forSearch(){
    var  startDate =$("#kqTime").val()+"-01";
    var  endDate=$("#kqTime").val();
    var  days=getEndD(endDate);
    endDate=endDate+"-"+days;
    if(startDate==""){
        layer.msg("开始时间不能为空");
        return ;
    }
    /*if(endDate==""){
        layer.msg("结束时间不能为空");
        return ;
    }
    if((new Date(endDate.replace(/-/g,"\/")))<(new Date(startDate.replace(/-/g,"\/")))){
        layer.msg("结束时间必须大于开始时间");
        return ;
    }
    var  falg=getD(startDate, endDate);
    if(!falg){
        layer.msg("结束时间大等于开始时间的一个月的时间");
        return ;
    }*/
    $("#startTime").val(startDate);
    $("#endTime").val(endDate);
	queryListPro("reload");
}
// 初始化列表数据
function queryListPro(load){
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
        　　　　{title:'员工姓名', name:'EMP_NAME', width:100, sortable:false, align:'center'},
        　　　　{title:'人资编号', name:'EMP_CODE', width:110, sortable:false, align:' center'},
	            {title:'开始时间', name:'BEGIN_DATE', width:100, sortable:false, align:'center'},
	            {title:'结束时间', name:'END_DATE', width:100, sortable:false, align:'center'},
	            {title:'全勤时长(天)', name:'FULL_TIME', width:150, sortable:false, align:'center'},
	            {title:'加班时长(小时)', name:'OVER_TIME', width:150, sortable:false, align:'center'},
        　　　　{title:'同步时间', name:'CREATE_TIME', width:150, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height() - 220;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/kqInfo/selectForKqInfo?ran='+ran,
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

function forSyncDate(){
    var  startDate =$("#kqTime").val()+"-01";
    var  endDate=$("#kqTime").val();
    var  kqTime=$("#kqTime").val();
    var  days=getEndD(endDate);
    endDate=endDate+"-"+days;
    layer.confirm('确认同步数据吗?', {icon: 7,title:'提示',shift:-1},function(index){layer.close(index);
        var ran = Math.random()*100000000;
        $.ajax({
            type: 'POST',
            url:'<%=request.getContextPath()%>/kqInfo/SyncKqData?ran='+ran,
            data: {startDate:startDate,endDate:endDate,kqTime:kqTime},
            beforeSend:function(){
                layer.load();
            },
            success:function(data){
                if(data.code == "200"){
                    layer.msg(data.msg);
                    layer.closeAll('loading');
                    queryListPro("reload");
                }else{
                    layer.msg(data.msg);
                    layer.closeAll('loading');
                }
            }
        });
    });
}
//确认
function forConfirm(){
    var selectList = mmg.selectedRows();
    if(selectList.length>0){
        var ids = "";
        for(var i=0;i<selectList.length;i++){
            ids += selectList[i].UUID+",";
        }
        ids = ids.slice(0,ids.length-1);
        $("input[name=selectList]").val(ids);
        var ran = Math.random()*1000;
        document.forms[0].method="post";
        document.forms[0].action ="<%=request.getContextPath()%>/kqInfo/exportExcel?ran="+ran;
        document.forms[0].submit();
    }else{
        $("input[name=selectList]").val("");
        var ran = Math.random()*1000;
        document.forms[0].method="post";
        document.forms[0].action ="<%=request.getContextPath()%>/kqInfo/exportExcel?ran="+ran;
        document.forms[0].submit();
    }
}
</script>
</html>
	