<!DOCTYPE>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<div class="tab-pane fade" id="beforePro">
		<div class="page-header-sl">
			<div class="button-box">
				<button type="button" class="btn btn-primary btn-xs" name="kOne" onclick="forAddBeforePro()">添加</button>
				<button type="button" class="btn btn-warning btn-xs" name="kOne" onclick="forDeleteBeforePro()">删除</button>
				<button type="button" class="btn btn-success btn-xs" name="kOne" onclick="forSaveBeforePro()">保存</button>
			</div>
		</div>
		<hr>
		<div>
			<table id="mmg_p" class="mmg">
				<tr>
					<th rowspan="" colspan=""></th>
				</tr>
			</table>
		</div>
	</div>
	
<!-- 项目前期维护javascrip代码 -->
<script type="text/javascript">
var mmg_p;
var allRelatedRows;//所有已关联的项目前期

// 初始化项目前期列表
function queryList_beforePro(){
	var ran = Math.random()*100000000;
	var cols = [
				{title:'工作任务编号', name:'projectNumber',width:145,sortable:false, align:'center'},
				{title:'工作任务名称', name:'projectName',width:170,sortable:false, align:'center'},
				{title:'开始日期', name:'startDate', width:145, sortable:false, align:'center'},
				{title:'结束日期', name:'endDate', width:145,sortable:false, align:'center'},
				{title:'已投入工时(h)', name:'workTime', width:145, sortable:false, align:'center'}
	    		];
	var mmGridHeight = $("body").parent().height() - 180;
	mmg_p = $('#mmg_p').mmGrid({
		noDataText:"",
		indexCol: true,
		indexColWidth:30,
		checkCol: true,
		checkColWidth:50,
		height: mmGridHeight,
		cols: cols,
		nowrap: true,
		url: '<%=request.getContextPath()%>/project/getBeforePro?isRelated=y&relProId=${id}&ran='+ran,
		multiSelect: true,
		root: 'items'
	}).on('loadSuccess', function(e, data){
		allRelatedRows = data.items;
	});
}

//添加项目前期
function forAddBeforePro(){
	parent.layer.open({
		type:2,
		title:"项目前期选择",
		area:['865px', '80%'],
		resize:true,
		scrollbar:true,
		content:['<%=request.getContextPath()%>/project/beforeProSelectPage'],
		end: function(){
			//document.execCommand("Refresh");
		}
	});
	//forClose();
}

function forDeleteBeforePro(){
	var index = mmg_p.selectedRowsIndex();
	if(index.length==0){
		layer.msg("请至少选择一条数据！");
		return;
	}  
	
	mmg_p.removeRow(index);
	sortIndex("mmg_p");
}

function forSaveBeforePro(){
	var proId = $("#proId").val()
	if(!proId) {
		layer.msg("请先保存项目!");
		//TODO 调试完成后放开
		//return;
	}
	
	var items = mmg_p.rows();
	var idsArr = [];
	
	if($('#mmg_p tr:first').hasClass('emptyRow') || items.length==0){
		layer.msg("无可保存内容!");
		//return;
	}else{
		$.each( items, function(i, n){
			idsArr.push(items[i].id);
		});
	}
	
	
	var ids = idsArr.toString();
	$.post('<%=request.getContextPath()%>/project/saveBeforePro',{proId:proId,ids:ids},function(data){
		if(data.success == "true"){
			layer.msg("保存成功!");
			allRelatedRows = mmg_p.rows();//保存成功后更新关联项目前期
			parent.queryList("reload");
		}else{
			layer.msg("保存失败!");
		}
		//queryList("reload");
	}); 
}

</script>