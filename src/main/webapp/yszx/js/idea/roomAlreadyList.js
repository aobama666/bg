
//定义一个
var roomList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
	roomList.initDataGrid();
	var classQuery = $(".changeQuery");
	/* 输入框的change事件，在输入过程中自动查询  */
	$(".changeQuery").change(function(e){
		roomList.query();
	});
	$(".inputQuery").on("input",function(e){
		var valLength = e.target.value.length;
		if(valLength>1){
			roomList.query();
		}
	});
	//回车键出发搜索按钮
	$("body").keydown(function () {
	    if (event.keyCode == "13") {
	    	dataItems = new Array();
			index = 0;
	        $("#datagrid").datagrid("seach");
	        return false;
	    }
	});
	roomList.btn_type_flag = 0;
});

/*  start  列表查询  */
roomList.query = function(){
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 演示中心管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/IdeaInfo/selectForAlreadytInfo",
		type: 'POST',
		form:'#queryForm',
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
//		successFinal:function(){
//			roomList.resize();
//		},
//		callBackFunc:function(){
//			roomList.initItems();
//		},
		columns: [
		  {name: '序号',style:{width:"50px"}, data: 'ROWNO'},       
		  {name: '',style:{width:"50px"}, data: 'id',checkbox:true, forMat:function(row){
			  dataItems[index] = row;//将一行数据放在一个list中
			  return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.id)+'"/>';
		 	}
		  },
		  {name: '申请单号',style:{width:"180px"}, data: 'applyNumber',forMat:function(row){
			  return "<a title = '"+row.applyNumber+"' style='width:250px;" + 
				  		"text-align:left;display:block;" +
				  		"white-space: nowrap;" +
				  		"text-overflow: ellipsis;" +
				  		"overflow: hidden;' id = '"+row.id+"'  ,applyId ='"+row.applyId+"' " +
				  		"href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.id+"','"+row.applyId+"')>"+row.applyNumber+"</a>";
				  		 
		  }},
		  {name: '申请时间', style:{width:"85px"},data: 'createTime'},
		  {name: '申请部门（单位）',style:{width:"10%"}, data: 'applyDept',forMat:function(row){
			  if(row.applyDept){
				  	return "<span title='"+row.applyDept+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.applyDept+"</span>"
				  }else{
					  return "";
			 }
		  }},
		 
		  {name: '主要参观领导', style:{width:"10%"},data: 'visitName',forMat:function(row){
			  if(row.visitName){
				  	return "<span title='"+row.visitName+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.visitName+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  {name: '院内陪同领导', style:{width:"10%"},data: 'leaderName',forMat:function(row){
			  if(row.leaderName){
				  	return "<span title='"+row.leaderName+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.leaderName+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  
		  {name: '院内陪同人员', style:{width:"10%"},data: 'userName',forMat:function(row){
			  if(row.userName){
				  	return "<span title='"+row.userName+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.userName+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  {name: '参观开始时间', style:{width:"8%"},data: 'stateDate'},
		  {name: '参观结束时间', style:{width:"8%"},data: 'endDate'},
		  {name: '审批状态',style:{width:"7%"},data: 'status'   },
		  {name: '联系人', style:{width:"80px"},data: 'contactUser'},
		  {name: '联系方式', style:{width:"100px"},data: 'contactPhone'}
		  
		]
	});

}	
	
	/*演示中心管理-查看 */	
	roomList.forDetails = function (id,applyId){
		var url = "/bg/yszx/details?id="+id+"&applyId="+applyId;
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">参观预定详情</h4>',
				area:['85%','85%'],
				fixed:false,//不固定
				maxmin:true,
				content:url, 
			});
	}
	 
 
	/* 演示中心待办管理-撤回方法*/
	roomList.withdrawEvent = function(){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能选择一条数据",2000);
			return;
		}
		var approveId= checkedItems[0].wlApproveId;
		$.messager.confirm( "提交提示", "确认撤回选中数据吗",
			function(r){
				if(r){
					$.ajax({
					    url: "/bg/Approve/recallApprove?approveId="+approveId,//删除
						type: "post",
						dataType:"json",
						contentType: 'application/json',
						success: function (data) {
							if(data.success == "true"){
								messager.tip("撤回成功",1000);
								roomList.query();
							}else{
								messager.tip("撤回失败",1000);
								roomList.query();
							}
						}
					});
				}
			}
		);
	}

	 
	
	
	
/*  start 全选、取消全选 */
$(".check_").change(function(){
	if(this.checked==true){
		var checkBoxs=$("input:checkbox[name=oneCheck]");
		checkBoxs.each(function(i){
			checkBoxs[i].checked=true;
		});
	}else{
		var checkBoxs=$("input:checkbox[name=oneCheck]");
			checkBoxs.each(function(i){
				checkBoxs[i].checked=false;
		});
	}
});
/*  end 全选、取消全选 */

/* 初始化dataItems */
roomList.initItems = function(){
	dataItems = new Array();
	index = 0;
}




/* end 删除方法*/


roomList.resize=function(){
	var height=$("body").height()-$(".sheach").height()-$("#funcBtn").height()-65;
	$("#datagrid>div").css({"height":height});
}
$(window).resize(function(){
	roomList.resize();
})


 
