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

<title>个人工时统计</title>
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
	.popup{
		cursor:pointer;
	} 
</style>
</head>
<body>
<div class="page-header-sl">
	<h5> 个人工时统计</h5>
	<div class="button-box">
		<button type="button" class="btn btn-info btn-xs" onclick="forConfirm()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px"  method="post">
		<hidden name="uuid" property="uuid"></hidden>
		<div class="form-group col-xs-3">
			<label>统计报表</label>
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
		<div class="form-group col-xs-5">
			<label>统计日期：</label>
			<div class="controls"  data-date-format="yyyy-mm-dd">
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input id="startDate" name="startTime" property="startTime"  readonly="true" placeholder='开始时间'  >
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
				<div class="floatLeft">--</div>
				<div class="input-group date form_date bg-white" data-date-format="yyyy-mm-dd">
					<input id="endDate" name="endTime" property="endTime"  readonly="true" placeholder='结束时间'  >
					<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
				</div>
			</div>
		</div>
		</form>
	</div>
	<div class="query-box-right">
		<button type="button" class="btn btn-primary btn-xs" onclick="forSearch()">统计</button>
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
	init();
	queryList();
});

function init(){
	$(".form_date").datepicker({autoclose:true,todayHighlight:true,language: 'cn' ,orientation:'auto'});
}
function forSearch(){
	var startDate =$("#startDate").val();
	var endDate=$("#endDate").val();
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
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var ran = Math.random()*100000000;
    var fixed2 = function(val){
    	return val;
    }
	var cols = [
	            {title:'序列', name:'Count', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'统计周期', name:'StartAndEndData' , width:100, sortable:false, align:'center'},
	            {title:'投入总工时（h）', name:'TotalHoursNum', width:150, sortable:false, align:'center',  renderer: function(val,row){
	            	if(val>0){
	            		 return ' <span Style="Color: #00b"  class="popup" startTime="'+row.StartData+'"  endTime="'+row.EndData+'" type="0">' +val + '</span> ';
	            	 }else{
	            		 return '<span  >' +val + '</span>';
	            	 }
	            }},
	            {title:'项目工作投入工时统计',  width:200, sortable:false, align:'center' ,cols:[
                 {title:'项目投入总工时（h）', name:'ProjectTotalHoursNum', width:200, sortable:false, align:'center',  renderer: function(val,row){
                	 if(val>0){
	            		 return '<span  Style="Color: #00b"  class="popup" startTime="'+row.StartData+'"  endTime="'+row.EndData+'" type="1">' +val + '</span>';
	            	 }else{
	            		 return '<span  >' +val + '</span>';
	            	 }
	            }},
                 {title:'工时占比（%）', name:'ProjectTotalHoursNumBF', width:200, sortable:false, align:'center'}
                 ]}, 
                 {title:'非项目工作投入工时统计',   width:200, sortable:false, align:'center' ,cols:[
                   {title:'非项目投入总工时（h）', name:'NoProjectTotalHoursNum', width:200, sortable:false, align:'center',  renderer: function(val,row){
	            	 if(val>0){
	            		 return '<span   Style="Color: #00b" class="popup" startTime="'+row.StartData+'"  endTime="'+row.EndData+'" type="2">' +val + '</span>';
	            	 }else{
	            		 return '<span  >' +val + '</span>';
	            	 }
	             }},
                   {title:'工时占比（%）', name:'NoProjectTotalHoursNumBF', width:200, sortable:false, align:'center' }
                 ]}, 
 	            {title:'标准工时', name:'StandartHoursNum', width:100, sortable:false, align:'center'},
 	            {title:'工作饱和度', name:'StandartHoursNumBF', width:100, sortable:false, align:'center' ,renderer: function(val,row){
 	            	 if(row.StandartHoursNumBJ>0){
 	            		 return '<span   Style="Color: #b00"   type="3">' +val + '</span>';
 	            	 }else{
 	            		 return '<span   >' +val + '</span>';
 	            	 }
 	            }}
	    		];
	var mmGridHeight = $("body").parent().height() - 190;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 40,
	 
		height: mmGridHeight,
		cols: cols,
		checkColWidth: 50,
		checkCol: true,
		nowrap: true,
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectForTimeAndPagebgWorkinghourInfo?ran='+ran,
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
		}) ;
	if(load == "reload"){
		mmg.load({page:pn});
	}

}

 


function reject(){
	var selectList = mmg.selectedRows();
	if(selectList.length==0){
		layer.alert("请勾选");
		return false;
	}
	
}
$("body").on("click",".popup",function(){
	var type=$(this).attr("type");
	var title="";
	if(type==0){
		title = "投入总工时统计详情";
	}else if(type==1){
		title = "项目投入总工时统计详情";
	}else{
		title = "非项目投入总工时统计详情";
	}
	var startTime = $(this).attr("startTime");
	var endTime = $(this).attr("endTime");
	index = layer.open({
		type:2,
		title:title,
		area:['80%', '80%'],
		resize:false,
		scrollbar:false,
		content:['/bg/BgWorkinghourInfo/Workinghour_detail?type='+type+"&startTime="+startTime+"&endTime="+endTime,'no'],
		end: function(){
			//queryList("reload");
		}
	})
})

$("body").on("click",".reject",function(){
	var ids="";
	var selectList = mmg.selectedRows();
	var reason = $(".reason").val();
	for(var i=0;i<selectList.length;i++){
		ids += selectList[i].ID+",";
	}
	ids = ids.slice(0,ids.length-1);
	common.getAjax("<%=request.getContextPath()%>/searchWorkTask/confirmExamine","post",{"type":"2","ids":ids,"reason":reason},
		function(data){
			layer.close(layer.getFrameIndex(window.name));
		},function(err){
			alert(err);
	})
})
//导出
function forConfirm(){
	var ids="";
	var selectList = mmg.selectedRows();
	var reason = $(".reason").val();
	for(var i=0;i<selectList.length;i++){
		ids += selectList[i].Count+",";
	}
   
	var ran = Math.random()*1000;
	document.forms[0].action ="<%=request.getContextPath()%>/BgWorkinghourInfo/exportExcelForTime?ran="+ran+"&id="+ids;
	document.forms[0].submit();
	$("input[name=uuid]").val("");
}
 
 
 
 
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
 
$(function(){
    $("#startDate").val(dateRangeUtil.getMonthFirstDay());
	$("#endDate").val( dateRangeUtil.getMonthEndDay());
})


</script>
</html>
	