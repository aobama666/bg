
//定义一个
var todoItem = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
todoItem.btn_type_flag = 0;
$(function(){
	
	 
	todoItem.initDataGrid();
	var classQuery = $(".changeQuery");
	/* 输入框的change事件，在输入过程中自动查询  */
//	$(".changeQuery").change(function(e){
//		todoItem.query();
//	});
//	$(".inputQuery").on("input",function(e){
//		var valLength = e.target.value.length;
//		if(valLength>3){
//			todoItem.query();
//		}
//	});
	
	
	$("#queryButton").on("click",function(e){
		todoItem.query();
	});
	//回车键出发搜索按钮
	$("body").keydown(function () {
	    if (event.keyCode == "13") {
	    	todoItem.query();
	        return false;
	    }
	});
//	todoItem.btn_type_flag = 0;
});

/*  start  列表查询  */
todoItem.query = function(){
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 演示中心管理-初始化列表界面  */
todoItem.initDataGrid = function(){
	    $("#datagrid").datagrid({
		url: "/bg/yszx/audit/queryTodoItem?tm="+new Date().getTime(),
		type: 'POST',
		form:'#queryForm',		 
		pageSize:10,
		tablepage:$(".tablepage"),//分页组件
		successFinal:function(data){
			todoItem.resize();
		},
		callBackFunc:function(){
			todoItem.initItems();
		},
		columns: [
		/*  {name: '序号',style:{width:"2%"}, data: 'RN'},    */      
		  {name: '',style:{width:"2%"}, data: 'id',checkbox:true, forMat:function(row){
			  dataItems[index] = row;//将一行数据放在一个list中
			  return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.ID)+'"/>';
		 	}
		  },
		  {name: '申请单号',style:{width:"10%"}, data: 'applyNumber',forMat:function(row){
			  return "<a title = '"+row.applyNumber+"' style='width:250px;" + 
				  		"text-align:left;display:block;" +
				  		"white-space: nowrap;" +
				  		"text-overflow: ellipsis;" +
				  		"overflow: hidden;' id = '"+row.id+"'" +
				  		"href = 'javascript:void(0)' onclick = todoItem.forDetails('"+row.id+"')>"+row.applyNumber+"</a>";
				  		 
		  }},
		  {name: '申请时间', style:{width:"8%"},data: 'applyTime'},
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
		  
		  {name: '院内陪同人员', style:{width:"10%"},data: 'deptUserName',forMat:function(row){
			  if(row.userName){
				  	return "<span title='"+row.deptUserName+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.deptUserName+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  {name: '参观开始时间', style:{width:"8%"},data: 'stateDate'},
		  {name: '参观结束时间', style:{width:"8%"},data: 'endDate'},
		  {name: '审批状态',style:{width:"7%"},data: 'statusName'   },
		  {name: '联系人', style:{width:"6%"},data: 'contactUser'},
		  {name: '联系方式', style:{width:"8%"},data: 'contactPhone'},
		  {name: '提交时间', style:{width:"8%"},data: 'submitDate'}
		  
		]
	});

}	
	
	/*演示中心管理-查看 */	
	todoItem.forDetails = function (id){
		var url = "/bg/yszx/details?id="+id;
//基于上级窗口  弹层   不适用于统一平台集成
//			parent.layer.open({
		 	//修正  基于当前窗口弹层
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">参观中心-查看</h4>',
				area:['800px','500px'],//area:['100%','100%'],
				fixed:false,//不固定
				maxmin:true,
				content:url, 
			});
	}
	/*演示中心管理-新增 */
	todoItem.passEvent = function (){
		alert("待开发。。。");
		return;
		var url = "/bg/yszx/addPage"
			//基于上级窗口  弹层   不适用于统一平台集成
//			parent.layer.open({
		 	//修正  基于当前窗口弹层
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">参观中心-新增</h4>',
				area:['800px','500px'],//area:['100%','100%'],
				fixed:false,//不固定
				maxmin:true,
				content:url, 
			});
	}
		
	/* 演示中心管理-修改*/
	todoItem.backEvent = function(){
		alert("待开发。。。");
		return; 
		
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能修改一条数据",2000);
			return;
		}
		if(checkedItems[0].approveState!="DEPT_HEAD_CHECK" && checkedItems[0].approveState!="SAVE"){
			messager.tip("该无法修改,审批状态为：待提交,待部门领导审核才可以修改",2000);
			return;
		}
		var id = dataGrid.getCheckedIds();
		var url = "/bg/yszx/updatePage?id="+id;
		//基于上级窗口  弹层   不适用于统一平台集成
//		parent.layer.open({
	 	//修正  基于当前窗口弹层
		layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:42px;">演示中心-修改 </h4>',
			area:['800px','500px'],//area:['100%','100%'],
			fixed:false,//不固定
			maxmin:true,
			content:url, 
		});
		 
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
todoItem.initItems = function(){
	dataItems = new Array();
	index = 0;
}




/* end 删除方法*/


todoItem.resize=function(){
	var height=$("body").height()-$(".sheach").height()-$("#funcBtn").height()-65;
	$("#datagrid>div").css({"height":height});
}
$(window).resize(function(){
	todoItem.resize();
})


todoItem.openRoomDetail = function (id,type){
	var url = "/bg/nonProject2/pro_details?proId="+id;
	if(parent.layer.open){
		parent.layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:42px;">实验室信息</h4>',
			area:['90%','90%'],
			fixed:false,//不固定
			maxmin:true,
			content:url,
			cancel:function(index,layero){
				if(type=="edit"){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					var layerViewSaveFlag = iframeWin.roomDetailInfo.saveInfoFlag;
					if(!layerViewSaveFlag){
						if(confirm("数据改变还未保存，是否关闭弹框？")){
							return true;
						}else{
							return false;
						}
					}
				}
			},
			end:function(){
				dataItems = new Array();
				index = 0;
				$("#datagrid").datagrid("refresh");
			}
		});
	}else{
		layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:42px;">实验室信息</h4>',
			area:['90%','90%'],
			fixed:false,//不固定
			maxmin:true,
			content:url,
			cancel:function(index,layero){
				if(type=="edit"){
					var iframeWin = window[layero.find('iframe')[0]['name']];
					var layerViewSaveFlag = iframeWin.roomDetailInfo.saveInfoFlag;
					if(!layerViewSaveFlag){
						if(confirm("数据改变还未保存，是否关闭弹框？")){
							return true;
						}else{
							return false;
						}
					}
				}
			},
			end:function(){
				dataItems = new Array();
				index = 0;
				$("#datagrid").datagrid("refresh");
			}
		});
	}
}
