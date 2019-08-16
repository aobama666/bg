<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>出勤查询</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/bootstrap/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/common/css/style.css">

<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/jQuery/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmGrid.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/mmGrid/src/mmPaginator.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/layer/layer.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/common/plugins/organ-tree/organ-tree.js"></script>
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
		<h5>项目信息维护</h5>
		<div class="button-box">
			<button type="button" class="btn btn-primary btn-xs"
				style="display: none;" name="kOne" onclick="forAdd()">数据同步</button>
			<button type="button" class="btn btn-success btn-xs"
				style="display: none;" name="kOne" onclick="forUpdate()">
				修改</button>
			<button type="button" class="btn btn-warning btn-xs"
				style="display: none;" name="kOne" onclick="forDelete()">
				删除</button>
			<button type="button" class="btn btn-info btn-xs"
				style="display: none;" name="kOne" onclick="forImport()">
				批量导入</button>
			<button type="button" class="btn btn-info btn-xs"
				onclick="forExport()">导出</button>
		</div>
	</div>
	<hr>
	<div class="query-box">
		<div class="query-box-left">
			<form name="queryBox" action=""
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-4">
					<label>项目名称：</label>
					<div class="controls">
						<input name="proName" property="proName">
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label for="querySex">项目状态：</label>
					<div class="controls">
						<select name="proStatus" property="proStatus">
							<option></option>
							<c:forEach var ="dict" items="${dictMap}">
								<option value=${dict.key}>${dict.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</form>
			<form action="" method="post">
				<input type="hidden" name="ids" value="">
			</form>
		</div>
		<div class="query-box-right">
			<button type="button" class="btn btn-primary btn-xs"
				onclick="forSearch()">查询</button>
		</div>
	</div>
	<div>
		<table id="mmg" class="mmg">
			<tr>
				<th rowspan="" colspan=""></th>
			</tr>
		</table>
		<div id="pg" style="text-align: right;"></div>
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
	$("#organTree").organTree({root:'41000001',level:'3',organCode:'queryOrganCode',organName:'queryOrganName',checkType:'checkbox'});
	var ran = Math.random()*100000000;
	<%-- $.post("<%=request.getContextPath()%>/cepriCommonWebc/getBussinessStatus?ran="+ran,{bussCode:'103'},function(data){
		if(data == "true"){
			$("button[name=kOne]").show();
		}
	}); --%>
	$("button[name=kOne]").show();
}
function forSearch(){
	pn = 1;
	queryList("reload");
}
// 初始化列表数据
function queryList(load){
	var w=$(window).width();
	var ran = Math.random()*100000000;
	var cols = [
				{title:'序列', name:'id', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'项目编号', name:'projectNumber', width:110, sortable:false, align:'center'},
	            {title:'WBS编号', name:'WBSNumber', width:100, sortable:false, align:'left'},
	            {title:'项目名称', name:'projectName',width:150, sortable:false, align:'left',
	            	renderer:function(val,item,rowIndex){
	            		return '<a href="###" title="'+val+'" onclick="forDetails(\''+item.id+'\')">'+val+'</a>';
	            	}
	            },
	            {title:'项目说明', name:'projectIntroduce',width:150, sortable:false, align:'left'},
	            {title:'项目类型', name:'category', width:100,sortable:false, align:'center'},
	            {title:'项目开始时间', name:'startDate', width:100, sortable:false, align:'center'},
	            {title:'项目结束时间', name:'endDate', width:100,sortable:false, align:'center'},
	            {title:'计划投入工时(h)', name:'planHours', width:120, sortable:false, align:'center'},
	            {title:'项目负责人', name:'principal', width:90, sortable:false, align:'center'},
	            {title:'参与人数', name:'amount', width:90,sortable:false, align:'center'},
	            {title:'项目来源', name:'src', width:90,sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		val = $.trim(val);
	            		if(val=='0' || val=='1'){
	            			return '报工系统';
	            		}else if(val=='2'){
	            			return '科研系统';
	            		}else if(val=='3'){
	            			return '横向系统';
	            		}
	            		return "";
	            	}
	            },
	            {title:'项目前期', name:'isRelated', width:90,sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		return val=='0'?'无':'有';
	            	}
	            },
	            {title:'项目状态', name:'projectStatus', width:90,sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var dict=${dictJson};
	            		return dict[val];
	            	}
	            },
	            {title:'操作', name:'handle', width:120, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		var operation;
	            		switch (parseInt(item.projectStatus)){
	            		case 0:
	            			operation='<a onclick="changeStatus(\'启动\',\'start\',\''+item.id+'\')">启动</a>';
	            		  break;
	            		case 1:
	            			operation='<a onclick="changeStatus(\'暂停\',\'pause\',\''+item.id+'\')">暂停</a>&nbsp&nbsp'+
	            			'<a onclick="changeStatus(\'结束\',\'finish\',\''+item.id+'\')">结束</a>&nbsp&nbsp'+
	            			'<a onclick="changeStatus(\'废止\',\'discard\',\''+item.id+'\')">废止</a>';
	            		  break;
	            		case 2:
	            			operation='<a onclick="changeStatus(\'启动\',\'start\',\''+item.id+'\')">启动</a>&nbsp&nbsp'+
	            			'<a onclick="changeStatus(\'结束\',\'finish\',\''+item.id+'\')">结束</a>&nbsp&nbsp'+
	            			'<a onclick="changeStatus(\'废止\',\'discard\',\''+item.id+'\')">废止</a>';
	            		  break;
	            		default:
	            			operation="";
	            		}
	            		return  operation;
	            	}
	            }
	    		];
	var mmGridHeight = $("body").parent().height() - 190;
	mmg = $('#mmg').mmGrid({
		cosEdit:"5,16",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 40,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/project/initPage?ran='+ran,
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
			pn = data.page;
			$(".checkAll").css("display","none").parent().text("选择");
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}

//详细信息
function forDetails(proId){
	var height=$(window).height()*0.9;
	if(height>570) height = 570;
	layer.open({
		type:2,
		title:"项目信息-查看",
		area:['865px', height+'px'],
		//scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/project/pro_details?proId='+proId]
	});
}

// 新增
function forAdd(){
	//var height=$(window).height()*0.9;
	//height=height>570?570:height;
	layer.open({
		type:2,
		title:"项目信息-新增",
		//area:['865px', height+'px'],
		area:['910px', '85%'],
		//scrollbar:false,
		skin:'query-box',
		content:['<%=request.getContextPath()%>/project/pro_add']
	}); 
}
// 修改
function forUpdate(){
	var rows = mmg.selectedRows();
	if(rows.length == 1){
		if(rows[0].projectStatus=="3" || rows[0].projectStatus=="4"){
			layer.msg("该项目无法修改!");
			return;
		}
		var proId = $.trim(mmg.selectedRowsByName("id"));
		var src = $.trim(mmg.selectedRowsByName("src"));
		/* var height=$(window).height()*0.9;
		if(height>560){
			height = 560;
		} */
		layer.open({
			type:2,
			title:"项目信息-修改",
			area:['910px', '85%'],
			//scrollbar:false,
		 	content:['<%=request.getContextPath()%>/project/pro_update?proId='+proId+'&src='+src]
		});
	}else{
		layer.msg("请选择一条数据!");
	}
}
// 删除
function forDelete(){
	var rows = mmg.selectedRows();
	if(rows.length > 0){
		var forbiddenRows="";
		for(var i=0;i<rows.length;i++){
			var row=rows[i];
			if(row.projectStatus!="0"){
				forbiddenRows+=row.RN+" ,";
			}
		}
		if(forbiddenRows!=""){
			layer.msg("第 "+forbiddenRows.substr(0,forbiddenRows.length-1)+" 行项目无法删除！");
			return;
		}
		layer.confirm('确认删除吗?', {icon: 7,title:'提示',shift:-1},function(index){
			layer.close(index);
			var proId = mmg.selectedRowsByName("id");
			var ran = Math.random()*100000000;
			$.post('<%=request.getContextPath()%>/project/deleteProject?ran='+ran,{proId:proId},function(data){
				if(data.result == "success"){
					layer.msg(data.msg);
				}else{
					layer.msg(data.msg);
				}
				queryList("reload");
			});
		});
	}else {
		layer.msg("请选择一条数据!");
	}
}

function changeStatus(text,operation,proId){
	if(operation=="start" || operation=="pause"){
		layer.confirm('确认'+text+'吗？', {icon: 7,title:'提示',shift:-1},
			function(index){
			layer.close(index);
			var ran = Math.random()*100000000;
			$.get("<%=request.getContextPath()%>/project/changeStatus?operation="+operation+"&proId="+proId+"&ran="+ran,
					function(data,status){
						if(data.result=="done"){
							queryList("reload");
						}else{
							layer.msg(data.result);
						}
					}
			);
		});
	}else if(operation=="finish" || operation=="discard"){
		layer.confirm('项目结束日期是否需要变动？', {icon: 7,title:'提示',shift:-1,btn: ['是', '否']},
			function(index){
				layer.close(index);
				var height=$(window).height()*0.9;
				if(height>560){
					height = 560;
				}
				layer.open({
					type:2,
					title:"项目信息-修改",
					area:['620px', height+'px'],
		 			content:['<%=request.getContextPath()%>/project/pro_update?proId='+proId]
				});
			},
			function(index){
				layer.close(index);
				var ran = Math.random()*100000000;
				$.get("<%=request.getContextPath()%>/project/changeStatus?operation="+operation+"&proId="+proId+"&ran="+ran,
					function(data,status){
						if(data.result=="done"){
							queryList("reload");
						}else{
							layer.msg(data.result);
						}
					}
				);
			}
		);
	}
}

function forExport(){
	if($("#mmg").has(".emptyRow").length>0){
		layer.msg("无可导出数据");
		return;
	}
	var ids = mmg.selectedRowsByName("id");
	//如果没有选择任何记录，则把当前条件传到后台查询所有记录
	ids==""?ids=JSON.stringify($(".query-box").sotoCollecter()):ids;
	$("input[name=ids]").val(ids);
	var ran = Math.random()*1000;
	document.forms[1].action ="<%=request.getContextPath()%>/project/exportSelectedItems?ran="+ran;
	document.forms[1].submit();
	$("input[name=ids]").val("");
}

function forImport(){
	var height=$(window).height()*0.8;
	if(height>320){
		height = 320;
	}
	layer.open({
		type:2,
		title:"导入页面",
		area:['620px', height+'px'],
		resize:false,
		scrollbar:false,
		content:['<%=request.getContextPath()%>/project/import_excel_page'],
		end: function(){
			
		}
	});
}
</script>
</html>
