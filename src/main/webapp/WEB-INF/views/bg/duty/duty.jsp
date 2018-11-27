<!-- http://localhost/bg/organstufftree/demo -->
<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> --%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>

<title>专责授权</title>
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
</head>
<body>
	<div class="page-header-sl">
		<h5>专责授权</h5>
		<div class="button-box">
			<button type="button" class="btn btn-primary btn-xs"
				style="display: none;" name="kOne" onclick="forAdd()">新增</button>
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
					<label for="username"><!-- <font class="glyphicon glyphicon-asterisk required"></font> -->员工</label>
					<div class="controls">
						<input name="username" id="username"/>	
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label for="deptName"><!-- <font class="glyphicon glyphicon-asterisk required"></font> -->组织</label>
					<div class="controls">
						<div id="organTree" class="input-group organ bg-white">
							<input type="text" name="deptName" id="deptName" readonly="readonly">
							<input type="hidden" name="deptCode" id="deptCode">
							<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label for="roleCode"><!-- <font class="glyphicon glyphicon-asterisk required"></font> -->角色</label>
					<div class="controls">
						<select id="roleCode" name="roleCode" property="roleCode">
								<option><option/>
								<option value="MANAGER_UNIT">院专责</option>
								<option value="MANAGER_DEPT">部门专责</option>
								<option value="MANAGER_LAB">处室专责</option>
								<option value="MANAGER_KJB">科技部专责</option>
						</select>
					</div>
				</div>
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
	$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio',popEvent:'pop'});
}

function queryList(load){
	var w=$(window).width();
	var ran = Math.random()*100000000;
	var cols = [
				{title:'序列', name:'id', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'人员', name:'USERALIAS', width:110, sortable:false, align:'center'},
	            {title:'角色', name:'ROLE_NAME', width:100, sortable:false, align:'center'},
	           /*  {title:'人员编号', name:'HRCODE',width:100, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		return '<a href="###" title="'+val+'" onclick="forDetails(\''+item.id+'\')">'+val+'</a>';
	            	}
	            }, */
	            {title:'人员编号', name:'HRCODE',width:100, sortable:false, align:'center'},
	            {title:'组织机构', name:'DEPTNAME', width:100,sortable:false, align:'left'},
	            {title:'组织类型', name:'TYPE', width:100, sortable:false, align:'center'},
	            {title:'组织编号', name:'DEPTCODE', width:100,sortable:false, align:'center'},
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
	var mmGridHeight = $("body").parent().height();
	console.log(mmGridHeight);
	console.log($("body").height());
	/* console.log($("body").html());
	console.log($("body").parent().html()); */
	mmg = $('#mmg').mmGrid({
		//cosEdit:"4,13",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 40,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/duty/listDuties?ran='+ran,
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

function forSearch(){
	pn = 1;
	queryList("reload");
}

function forSubmit(){
	var ran = Math.random()*1000000;
	$.ajax({
		type:"POST",
		url:"<%=request.getContextPath()%>/sync/addDuty?ran="+ran,
		data:{empCode:$("#empCode").val(),deptCode:$("#deptCode").val(),roleCode:$("#roleCode").val()},
		dataType:'text',
		success:function(data){
			layer.msg(data);
		}
	});
}
 
function forClose(){
	layer.msg("自定义关闭！");
	return ;
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}

function popEvent(ids,codes,texts,pId,level){
	//alert(level);
}
</script>
</html>
	