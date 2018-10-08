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

<title>认证认可文件</title>
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
<!--[if lt IE 9>
	<script src="<%=request.getContextPath() %>/common/plugins/html5shiv/html5shiv.min.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/respond/respond.js"></script>
	<script src="<%=request.getContextPath() %>/common/plugins/pseudo/jquery.pseudo.js"></script>
<![endif]-->

<style type="text/css">

</style>
</head>
<body>
<div class="page-header-sl">
	<h5> 认证认可文件</h5>
	<div class="button-box">
		<button type="button" class="btn btn-primary btn-xs" style="display:none;" name="kOne" onclick="forAdd()"> 新增</button>
		<button type="button" class="btn btn-success btn-xs" style="display:none;" name="kOne" onclick="forUpdate()"> 修改</button>
		<button type="button" class="btn btn-warning btn-xs" style="display:none;" name="kOne" onclick="forDelete()"> 删除</button>
		<button type="button" class="btn btn-info btn-xs" style="display:none;" name="kOne" onclick="forImport()"> 批量导入</button>
		<button type="button" class="btn btn-info btn-xs" onclick="forExport()"> 导出</button>
	</div>
</div>
<hr>
<div class="query-box">
	<div class="query-box-left">
		<form name="queryBox" action="" style="width:100%;padding-left:10px">
		<hidden name="uuid" property="uuid"></hidden>
		<div class="form-group col-xs-4">
			<label for="empCode">组织机构</label>
			<div class="controls">
				<div id="organTree" class="input-group organ bg-white">
					<hidden name="queryOrganCode" property="queryOrganCode" ></hidden>
					<!-- <input name="queryOrganName" property="queryOrganName" readonly="true"> -->
					<input type="text" name="queryOrganName" value="" readonly="readonly">
					<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
				</div>
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>人员姓名</label>
			<div class="controls">
				<input name="queryEmpName" property="queryEmpName" >
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>文件名称</label>
			<div class="controls">
				<input name="queryDocumentName" property="queryDocumentName" >
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>文件编号</label>
			<div class="controls">
				<input name="queryDocumentCode" property="queryDocumentCode" >
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label>类型</label>
			<div class="controls">
				<select name="queryDocumentType" property="queryDocumentType" >
					<options collection="typeList" property="label" labelProperty="value"></options>
				</select>
			</div>
		</div>
		<div class="form-group col-xs-4">
			<label for="querySex">发布年份</label>
			<div class="controls">
				<select name="queryPublishDate" property="queryPublishDate" >
					<options collection="years" property="label" labelProperty="value"></options>
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
var mmg;
var pn = 1;
var limit = 30;
$(function(){

	init();
	
	queryList();
	
});

function init(){

	$("#queryPublishDateEx").datepicker({autoclose:true,todayHighlight:true,clearBtn:true});

	$("#organTree").organTree({root:'41000001',level:'3',organCode:'queryOrganCode',organName:'queryOrganName',checkType:'checkbox'});
	var ran = Math.random()*100000000;
	<%-- $.post("<%=request.getContextPath()%>/cepriCommonWebc/getBussinessStatus.so?ran="+ran,{bussCode:'103'},function(data){
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
	var ran = Math.random()*100000000;
	var cols = [
	            {title:'序列', name:'hex2', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'部门（单位）', name:'label', width:100, sortable:false, align:'left'},
	            {title:'处室', name:'rgb', width:100, sortable:false, align:'left'},
	            {title:'人员编号', name:'phone', width:100, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height() - 280;
	mmg = $('#mmg').mmGrid({
		indexCol: true,
		indexColWidth: 30,
		checkCol: true,
		checkColWidth: 130,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/demo/indexPage?ran='+ran,
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
		});
	if(load == "reload"){
		mmg.load({page:pn});
	}

}
// 新增
function forAdd(){
	layer.open({
		type:2,
		title:"新增页面",
		area:['620px', '500px'],
		scrollbar:false,
		content:['http://localhost/bg/demo/authentication_add','no']
	});
}
// 修改
function forUpdate(){
	var rows = mmg.selectedRows();
	if(rows.length == 1){
		var uuid = mmg.selectedRowsByName("hex2");
		//alert(uuid);
		 layer.open({
			type:2,
			title:"修改页面",
			area:['620px', '500px'],
			scrollbar:false,
			<%-- content:['<%=request.getContextPath()%>/authenticationWeb/updatePage.so?uuid='+uuid,'no'] --%>
		 	content:['http://localhost/bg/demo/authentication_update','no']
		});
	}else{
		layer.msg("请选择一条数据!");
	}
}
// 删除
function forDelete(){
	var rows = mmg.selectedRows();
	if(rows.length > 0){
		layer.confirm('确认删除吗?', function(index){
			var uuids = mmg.selectedRowsByName("uuid");
			var ran = Math.random()*100000000;
			$.post('<%=request.getContextPath()%>/authenticationWeb/forDelete.so?ran='+ran,{uuids:uuids},function(data){
				if(data == "true"){
					layer.msg("删除成功!");
				}else{
					layer.msg("删除失败!");
				}
				queryList("reload");
			});
			layer.close(index);
		});
	}else {
		layer.msg("请选择一条数据!");
	}
}

function forExport(){
	var uuids = mmg.selectedRowsByName("uuid");
	$("input[name=uuid]").val(uuids);
	var ran = Math.random()*1000;
	document.forms[0].action ="<%=request.getContextPath()%>/authenticationWeb/exportExcel.so?ran="+ran;
	document.forms[0].submit();
	$("input[name=uuid]").val("");
}

function forImport(){
	layer.open({
		type:2,
		title:"导入页面",
		area:['620px', '230px'],
		resize:false,
		scrollbar:false,
		content:['http://localhost/bg/demo/authentication_import_excel_page','no'],
		end: function(){
			
		}
	});
}
</script>
</html>
	