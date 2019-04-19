
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
		if(valLength>3){
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
		url: "/bg/nonProject2/initPage",
		type: 'POST',
		form:'#queryForm',
		 
		pageSize:50,
		tablepage:$(".tablepage"),//分页组件
		successFinal:function(){
			roomList.resize();
		},
		callBackFunc:function(){
			roomList.initItems();
		},
		columns: [
		/*  {name: '序号',style:{width:"2%"}, data: 'RN'},    */      
		  {name: '',style:{width:"2%"}, data: 'id',checkbox:true, forMat:function(row){
			  dataItems[index] = row;//将一行数据放在一个list中
			  return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.id)+'"/>';
		 	}
		  },
		  {name: '申请单号',style:{width:"5%"}, data: 'projectNumber',forMat:function(row){
			  return "<a title = '"+row.projectNumber+"' style='width:150px;" + 
				  		"text-align:left;display:block;" +
				  		"white-space: nowrap;" +
				  		"text-overflow: ellipsis;" +
				  		"overflow: hidden;' id = '"+row.id+"'" +
				  		"href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.id+"')>"+row.projectNumber+"</a>";
				  		 
		  }},
		  {name: '申请时间', style:{width:"8%"},data: 'startDate'},
		  {name: '申请部门（单位）',style:{width:"10%"}, data: 'projectName',forMat:function(row){
			  if(row.projectName){
				  	return "<span title='"+row.projectName+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.projectName+"</span>"
				  }else{
					  return "";
			 }
		  }},
		 
		  {name: '主要参观领导', style:{width:"10%"},data: 'projectIntroduce',forMat:function(row){
			  if(row.projectIntroduce){
				  	return "<span title='"+row.projectIntroduce+"' style='width:300px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.projectIntroduce+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  {name: '院内陪同人员', style:{width:"10%"},data: 'projectIntroduce',forMat:function(row){
			  if(row.projectIntroduce){
				  	return "<span title='"+row.projectIntroduce+"' style='width:300px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.projectIntroduce+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  {name: '参观开始时间', style:{width:"8%"},data: 'startDate'},
		  {name: '参观结束时间', style:{width:"8%"},data: 'endDate'},
		  {name: '审批状态',style:{width:"7%"},data: 'projectStatus'  ,forMat:function(row){
			   
			      if(row.projectStatus=="0"){
				  	return "未启动"
				  }else if(row.projectStatus=="1"){
					return "进行中";
			      }else if(row.projectStatus=="2"){
					return "暂停";
			      }else if(row.projectStatus=="3"){
					return "已结束";
			      }else if(row.projectStatus=="4"){
					return "废止";
			      }
		      }
		  },
		  {name: '联系人', style:{width:"6%"},data: 'planHours'},
		  {name: '联系方式', style:{width:"8%"},data: 'planHours'}
		  
		]
	});

}	
	
	/*演示中心管理-查看 */	
	roomList.forDetails = function (id){
		var url = "/bg/nonProject2/pro_details?proId="+id;
			parent.layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">项目前期工作维护-查看</h4>',
				area:['40%','60%'],
				fixed:false,//不固定
				maxmin:true,
				content:url, 
			});
	}
	/*演示中心管理-新增 */
	roomList.addEvent = function (){
		var url = "/bg/nonProject2/pro_add"
			parent.layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">项目前期工作维护-新增</h4>',
				area:['40%','60%'],
				fixed:false,//不固定
				maxmin:true,
				content:url, 
			});
	}
		
	/* 演示中心管理-修改*/
	roomList.updateEvent = function(){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能修改一条数据",2000);
			return;
		}
		if(checkedItems[0].projectStatus=="3" || checkedItems[0].projectStatus=="4"){
			messager.tip("该无法修改",2000);
			return;
		}
		var id = dataGrid.getCheckedIds();
		var url = "/bg/nonProject2/pro_update?proId="+id;
		parent.layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:42px;">项目前期工作维护-修改 </h4>',
			area:['40%','60%'],
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
roomList.initItems = function(){
	dataItems = new Array();
	index = 0;
}



/* start 删除方法*/
roomList.delEvent = function(){
	var delDataInfo = {};
	var checkedIds = dataGrid.getCheckedIds();
	delDataInfo.LBIDS = checkedIds;
	if(checkedIds.length==0){
		messager.tip("请选择要操作的数据",1000);
		return;
	}
	$.messager.confirm( "删除提示", "确认删除选中数据吗",
		function(r){
			if(r){
				$.ajax({
				    url: "/newtygl/laboratory/roomDel",//删除
					type: "post",
					dataType:"json",
					contentType: 'application/json',
					data:JSON.stringify(delDataInfo),//传递的需要删除的数据
					success: function (p_content) {
						if( p_content.P_DATA == "SUCCESS"){
							messager.tip("删除成功",1000);
							roomList.query();
						}else{
							messager.tip("删除失败",1000);
							roomList.query();
						}
					}
				});
			}
		}
	);
}
/* end 删除方法*/


roomList.resize=function(){
	var height=$("body").height()-$(".sheach").height()-$("#funcBtn").height()-65;
	$("#datagrid>div").css({"height":height});
}
$(window).resize(function(){
	roomList.resize();
})


roomList.openRoomDetail = function (id,type){
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
