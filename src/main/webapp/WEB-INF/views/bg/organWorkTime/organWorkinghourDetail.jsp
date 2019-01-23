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

<title>员工工时明细</title>
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
	.page-header-sl{
		overflow:hidden;	
		height:22px;
		margin:0;
		margin-bottom:10px;
	}
</style>
</head>
<body>
<div class="page-header-sl">
	<div class="button-box">
		<button type="button" class="btn btn-info btn-xs" onclick="forExport()"  > 导出</button>
	</div>
</div>
<div>
   <form name="queryBox" action="" style="width:100%;padding-left:10px" method="post" >
   		<input name="ran" type="hidden"/>
   		<input name="deptid" type="hidden"/>
   		<input name="labid" type="hidden"/>
   		<input name="StartData" type="hidden"/>
   		<input name="EndData" type="hidden"/>
   		<input name="type" type="hidden"/>
   		<input name="dataShow" type="hidden"/>
   		<input name="ids" type="hidden"/>
   </form>
	<table id="mmg" class="mmg">
		<tr>
			<th rowspan="" colspan=""></th>
		</tr>
	</table>
	
	<div id="pg" style="text-align:right;"></div>
</div>
</body>
<script type="text/javascript">
var deptid=common.getQueryString("deptid");
var labid=common.getQueryString("labid");
var StartData=common.getQueryString("StartData");
var EndData=common.getQueryString("EndData");
var type=common.getQueryString("type");
var dataShow=common.getQueryString("dataShow");
 
ran = Math.random()*1000;
$("input[name=ran]").val(ran);
$("input[name=deptid]").val(deptid);
$("input[name=labid]").val(labid);
$("input[name=StartData]").val(StartData);
$("input[name=EndData]").val(EndData);
$("input[name=type]").val(type);
$("input[name=dataShow]").val(dataShow);

var workType = '';
var nameType = '';
if(type==1){
	workType = 'TotalHoursNum';
	nameType = '投入总工时(h)';
}else if(type==2){
	workType = 'ProjectTotalHoursNum';
	nameType = '项目投入工时(h)';
}else if(type==3){
	workType = 'NoProjectTotalHoursNum';
	nameType = '非项目投入工时(h)';
}
var mmg;
var pn = 1;
var limit = 30;
var cols=[];
var params={
	ran:ran,
	deptid:deptid,
	labid:labid,
	StartData:StartData,
	EndData:EndData,
	type:type,
	dataShow:dataShow,
	page:1,
	limit:30
}

$(function(){
	init();
});

function init(){
	$.post('<%=request.getContextPath()%>/BgWorkinghourInfo/selectFororganAndUser', params,
			   function(data){
					cols = [
					            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
					            {title:'统计周期', name:'StartAndEndData', width:150, sortable:false, align:'center'},
					            {title:'部门', name:'PDEPTNAME', width:100, sortable:false, align:'left'},
					            {title:'处室', name:'DEPTNAME', width:100, sortable:false, align:'left'},
					            {title:'人员编号', name:'HRCODE', width:70, sortable:false, align:'center'},
					            {title:'人员姓名', name:'useralias', width:70, sortable:false, align:'center'}
					            //{title:nameType, name:workType, width:100, sortable:false, align:'center'}
					    		];
					var title=data.title;
					$.each(title,function(i,n){
						cols.push({title:n, name:i, width:100, sortable:false, align:'center'});
					});
					
					//TODO
					/* {title:'非项目',   width:200, sortable:false, align:'center' ,cols:[
                     {title:'项目前期投入工时（h）', name:'BPHoursNum', width:200, sortable:false, align:'center'},
  	                 {title:'常规工作投入工时（h）', name:'NP_CGHoursNum', width:200, sortable:false, align:'center'},
                     {title:'工时占比（%）', name:'NoProjectTotalHoursNumBF', width:200, sortable:false, align:'center' }
                   ]}; */
                   
                   
					/* delete title['NP000'];
					if(type==1){
						$.each(title,function(i,n){
							cols.push({title:n, name:i, width:100, sortable:false, align:'center'});
						});
						cols.push({title:"非项目工作", name:"NP000", width:100, sortable:false, align:'center',
							renderer:function(val,item,rowIndex){
			            		return val==undefined?'0':val;;
			            	}	
						});
					}else if(type==2){
						$.each(title,function(i,n){
							cols.push({title:n, name:i, width:100, sortable:false, align:'center'});
						});
					}else if(type==3){
						cols.push({title:"非项目工作", name:"NP000", width:100, sortable:false, align:'center',
							renderer:function(val,item,rowIndex){
			            		return val==undefined?'0':val;;
			            	}	
						});
					} */
					queryList();

			 	});
}

function forSearch(){
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var mmGridHeight = $("body").parent().height() - 100;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		//items:[],
		url: '<%=request.getContextPath()%>/BgWorkinghourInfo/selectFororganAndUser',
		root: 'items',
		fullWidthRows: true,
		multiSelect: true,
		params:params, 
		plugins: [
			$("#pg").mmPaginator({page:pn, limit:limit, totalCountName:'totalCount'})
				]
		}).on('loadSuccess', function(e, data){
			$(".checkAll").css("display","none").parent().text("选择"); 
			pn = data.page;
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}
}
//导出
function forExport(){
	var ids="";
	var selectList = mmg.selectedRows();
	for(var i=0;i<selectList.length;i++){
		ids += (parseInt(selectList[i].Count))+",";
	}	
	$("input[name=ids]").val(ids);
	document.forms[0].action ="<%=request.getContextPath()%>/BgWorkinghourInfo/selectFororganAndUserExport";
	document.forms[0].submit();
	$("input[name=uuid]").val("");
}
 
function reject(){
	//关闭弹出框
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}
</script>
</html>
	