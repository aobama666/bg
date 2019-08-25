<!DOCTYPE>
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

<title>报工系统</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/css/style.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/sotoCollecter/sotoCollecter.js"></script>
<!--[if lt IE 9>
	<script src="<%=request.getContextPath()%>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath()%>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">
a{
	cursor: pointer;
	text-decoration: none !important;
}
</style>
</head>
<body>
	<div class="page-header-sl">
		<div class="button-box">
			<button type="button" class="btn btn-success btn-xs"
				onclick="forAdd()">确定</button>
		</div>
	</div>
	<hr>
	<div class="query-box">
		<div class="query-box-left">
			<form name="queryBox" action=""
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-6">
					<label for="querySex">工作任务编号</label>
					<div class="controls">
						<input name="proNumber" property="proNumber">
					</div>
				</div>
				<div class="form-group col-xs-6">
					<label>工作任务名称</label>
					<div class="controls">
						<input name="proName" property="proName">
					</div>
				</div>
				<input type="hidden" name="selectedDate" value="${selectedDate}" id="fillDa">
			</form>
		</div>
		<div class="query-box-right">
			<button type="button" class="btn btn-primary btn-xs"
				onclick="forSearch()">查询</button>
		</div>
	</div>
	<div>
		<table id="mmg" class="mmg bg-white">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align: right;"></div>
	</div>
</body>
<script type="text/javascript">
var mmg;
$(function(){
	queryList();
});

function forSearch(){
	queryList("reload");
}

// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'序列', name:'ID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'类型', name:'CATEGORY', width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
						val=val==undefined?"":val;
	            		return parent.categoryObj[val];
	            	}},
	            {title:'工作任务编号', name:'PROJECT_NUMBER', width:110, sortable:false, align:'center'},
	            {title:'工作任务名称', name:'PROJECT_NAME', width:150, sortable:false, align:'left'}
	            //{title:'WBS编号', name:'WBS_NUMBER', width:100, sortable:false, align:'left'}
	    		];
	var mmGridHeight = $("body").parent().height()*0.8;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 50,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/staffWorkbench/getProjectsByDate?ran='+ran,
		fullWidthRows: true,
		multiSelect: true,
		root: 'items',
		params: function(){
			return $(".query-box").sotoCollecter();
			} 
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择");
		});
	if(load == "reload"){
		mmg.load();
	}
}

function forAdd(){
	var items = mmg.selectedRows();

    var dateStr=$("#fillDa").val();
    var date=new Date(dateStr.replace(/-/g,"/"));
    var year = date.getFullYear();
    var month = date.getMonth();
    var firstDay=new Date(year,month,1);//这个月的第一天
    var currentMonth=firstDay.getMonth(); //取得月份数
    var nextMonthFirstDay=new Date(firstDay.getFullYear(),currentMonth+1,1);//加1获取下个月第一天
    var dis=nextMonthFirstDay.getTime()-24*60*60*1000;//减去一天就是这个月的最后一天
    var lastDay=new Date(dis);
    var time =dateFtt("yyyy-MM-dd",firstDay);//格式化 //这个格式化方法要用你们自己的，也可以用本文已经贴出来的下面的Format
    var timeEnd=dateFtt("yyyy-MM-dd",lastDay);//格式化

	for(var i=0;i<items.length;i++){
		var approverHrcode = items[i].HRCODE;
		var approverName = items[i].PRINCIPAL;
		var category = items[i].CATEGORY;
		var role = items[i].ROLE;
		var editable = "true";

		var id = items[i].ID;

		//新增项目，如果是常规工作和项目前期类型,或者项目负责人为当前登录人，则审核人为按审批层级的默认审核人
		/*if('CG,BP'.indexOf(category)!=-1 || approverHrcode==parent.currentUserHrcode){
			approverHrcode=parent.approverHrcode;
			approverName=parent.approverName;
		}else{
			editable = "false";
		}*/
        if('CG,BP'.indexOf(category)!=-1 ){
            approverHrcode=parent.approverHrcode;
            approverName=parent.approverName;
        }else if (approverHrcode==parent.currentUserHrcode || role == 1) {
            approverHrcode=parent.approverHrcode;
            approverName=parent.approverName;
        }else {
                editable = "false";
		}
		 
		parent.mmg.addRow({
			"PROJECT_ID":items[i].ID,
			"WORK_TIME_BEGIN":time,
			"WORK_TIME_END":timeEnd,
			"CATEGORY":items[i].CATEGORY,
			"PROJECT_NAME":items[i].PROJECT_NAME,
			"PRINCIPAL":approverName,
			"HRCODE":approverHrcode,
			"STATUS":"0",
			"JOB_CONTENT":"",
			"WORKING_HOUR":"",
			"EDITABLE":editable
		});
	}
	forClose();
}

function forClose() {
	parent.layer.close(parent.layer.getFrameIndex(window.name));
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

</script>
</html>
