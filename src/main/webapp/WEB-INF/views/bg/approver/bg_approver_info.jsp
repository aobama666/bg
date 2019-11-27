<!-- http://localhost/bg/organstufftree/demo -->
<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> --%>
<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="IE=10; IE=9; IE=8; IE=EDGE; Chrome=1"/>

<title>审批人配置</title>
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
<style type="text/css">
a{
	cursor: pointer;
	text-decoration: none !important;
}
#show{
	border: 1px solid #1b9974;
	margin:10px;
	width:666px;
}
#show td{
	border: solid rgb(228, 228, 228) 1px;
	padding: 5px;
	text-align: center;
	font-size: 14px;
}
.even{
	background: #f9f9f9;
}
</style>

</head>
<body>
	<div class="page-header-sl">
		<h5>审批人配置</h5>

		<div class="button-box">
			<button type="button" class="btn btn-primary btn-xs"
				 name="kOne" onclick="forAdd()">新增</button>
			<button type="button" class="btn btn-success btn-xs"
					name="kOne" onclick="forDelete()">删除</button>
			<%--<button type="button" class="btn btn-warning btn-xs" name="kOne" onclick="forImport()">--%>
				<%--批量导入</button>--%>
			<button type="button" class="btn btn-info btn-xs"
				onclick="forExport()">导出</button>
		</div>
	</div>
	<hr>
	<div class="query-box">
		<div class="query-box-left">
			<form name="queryBox" action="" method="post"
				style="width: 100%; padding-left: 10px">
				<div class="form-group col-xs-4">
					<label for="username"> 姓名：</label>
					<div class="controls">
						<input name="username" id="username"/>	
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label for="roleCode">审核人类别：</label>
					<div class="controls">
						<select id="roleCode" name="roleCode" property="roleCode">
							<option></option>
							<c:forEach  var="pcodeList"  items="${pcodeList}">
								<option value ="${pcodeList.CODE}" title=" ${pcodeList.NAME}" > ${pcodeList.NAME}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group col-xs-4">
					<label for="deptName"> 组织名称：</label>
					<div class="controls">
						<div id="organTree" class="input-group organ bg-white">
							<input type="text" name="deptName" id="deptName" readonly="readonly">
							<input type="hidden" name="deptCode" id="deptCode">
							<span class="input-group-addon"><span class="glyphicon glyphicon-th-list"></span></span>
						</div>
					</div>
				</div>

				<input type="hidden" name="index" value="">
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
	$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'self',checkType:'radio',popEvent:'pop',level:'1'});
}
function queryList(load){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'UUID', name:'UUID', width:0, sortable:false, align:'center', hidden: true, lockDisplay: true},
	            {title:'姓名',name:'USERALIAS', width:100, sortable:false, align:'center'},
	            {title:'审核人类别', name:'SUBNAME', width:100, sortable:false, align:'center'},
	            {title:'组织名称', name:'DEPTNAME', width:120,sortable:false, align:'center'},
		        {title:'组织等级', name:'TYPE', width:120,sortable:false, align:'center'},
	            {title:'审核优先级', name:'PRIORITY', width:80,sortable:false, align:'center'},
	            {title:'操作', name:'handle', width:50, sortable:false, align:'center',
	            	renderer:function(val,item,rowIndex){
	            		return '  <a onclick="forUpdata(\''+item.UUID+'\')">修改</a>       ';
	            	}
	            }
	    		];
	var mmGridHeight = $("body").parent().height()-190;
	mmg = $('#mmg').mmGrid({
		cosEdit:"9",//声明需要编辑，取消点击选中的列
		indexCol: true,
		indexColWidth: 40,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/approver/listApprovers?ran='+ran,
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

//查看审批负责和部门类型
function show(target){
	var url = '<%=request.getContextPath()%>/approver/getInfoForShow?ran='+Math.random()*100000000;
	$.get(url,{target:target},
			function(data){
				var title;
				var content = '<table id="show">';
				if(target=='organType'){
					title = '组织类型';
					content += '<tr style="background-color: rgb(143, 230, 198);font-weight: bold;"><td>组织编号</td><td>组织名称</td><td>组织类型</td></tr>';
					$.each(data,function(i,obj){
						var clazz = i%2==0?"even":"";
						content += '<tr class="'+clazz+'"><td>'+obj.DEPTCODE+'</td>'+'<td>'+obj.ORGAN+'</td>'+'<td>'+obj.TYPE+'</td></tr>';
					});
				}else{
					title = '审批规则';
					content += '<tr style="background-color: rgb(143, 230, 198);font-weight: bold;"><td>组织类型</td><td>角色</td><td>审批角色</td></tr>';
					$.each(data,function(i,obj){
						var clazz = i%2==0?"even":"";
						content += '<tr class="'+clazz+'"><td>'+obj.ORGANTYPE+'</td>'+'<td>'+obj.ROLE+'</td>'+'<td>'+obj.APPROVEROLE+'</td></tr>';
					});
				}
				
				content += '</table>';
				index = layer.open({
					type:1,
					title:title,
					area:['700px', '600px'],
					resize:false,
					scrollbar:false,
					content:content,
					end: function(){
						queryList("reload");
					}
				}); 
	});
}

//查询
function forSearch(){
	pn = 1;
	queryList("reload");
}

//新增
function forAdd(){
	layer.open({
		type:2,
		title:"审批权限-新增",
		area:['620px', 　'300px'],
		skin:'query-box',
		content:['<%=request.getContextPath()%>/approver/addPage']
	}); 
}
//修改
function forUpdata(id){
    layer.open({
        type:2,
        title:"审批权限-修改",
        area:['620px', 　'300px'],
        skin:'query-box',
		content:['<%=request.getContextPath()%>/approver/updataPage?id='+id]
    });
}


//删除
function forDelete(){

    var rows = mmg.selectedRows();
    var ids="";
    if(rows.length > 0) {
        for (var i = 0; i < rows.length; i++) {
            var row = rows[i];
            ids += row.UUID + ","
        }

        layer.confirm('确认删除吗？', {icon: 7,title:'提示',shift:-1},
            function(index){
                layer.close(index);
                var ran = Math.random()*1000000;
                $.post("<%=request.getContextPath()%>/approver/deleteDuty?ran="+ran,
                    { id: ids},
                    function(data){
                        if(data.success=="true"){
                            layer.msg(data.msg);
                        }else{
                            layer.msg(data.msg);
						}
                        queryList("reload");
                    }
                );
            });
    }else {
        layer.msg("请选择一条数据!");
	}

}





function forImport(){
	var height=$(window).height()*0.8;
	if(height>300){
		height = 300;
	}
	layer.open({
		type:2,
		title:"导入页面",
		area:['620px', height+'px'],
		resize:false,
		scrollbar:false,
		content:['<%=request.getContextPath()%>/approver/importExcelPage'],
		end: function(){
			
		}
	});
}

function forExport(){
	if($("#mmg").has(".emptyRow").length>0){
		layer.msg("无可导出数据");
		return;
	}
	var index = mmg.selectedRowsIndex();
	//如果没有选择任何记录，则把当前条件传到后台查询所有记录
	//index = index==""?JSON.stringify($(".query-box").sotoCollecter()):index;
	$("input[name=index]").val(index);
	document.forms[0].action ="<%=request.getContextPath()%>/approver/exportSelectedItems";
	document.forms[0].submit();
	$("input[name=index]").val("");
}

 
function forClose(){
	layer.msg("自定义关闭！");
	parent.layer.close(parent.layer.getFrameIndex(window.name));
}

function popEvent(ids,codes,texts,pId,level){
	//alert(level);
}
//回车键提交事件
$("body").keydown(function(){
    if(event.keyCode=="13"){
    	forSearch();
    }
})
</script>
</html>
	