
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
		url: "/bg/IdeaInfo/selectComprehensiveInfo",
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
		  {name: '序号',style:{width:"2%"}, data: 'ROWNO'},         
		  {name: '',style:{width:"2%"}, data: 'id',checkbox:true, forMat:function(row){
			  dataItems[index] = row;//将一行数据放在一个list中
			  return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.id)+'"/>';
		 	}
		  },
		  {name: '申请单号',style:{width:"240px"}, data: 'applyNumber',forMat:function(row){
			  return "<a title = '"+row.applyNumber+"' style='width:250px;" + 
				  		"text-align:left;display:block;" +
				  		"white-space: nowrap;" +
				  		"text-overflow: ellipsis;" +
				  		"overflow: hidden;' id = '"+row.id+"'  ,applyId ='"+row.applyId+"' " +
				  		"href = 'javascript:void(0)' onclick = roomList.forDetails('"+row.id+"','"+row.applyId+"')>"+row.applyNumber+"</a>";
				  		 
		  }},
		  {name: '申请时间', style:{width:"8%"},data: 'createTime'},
		  {name: '申请部门（单位）',style:{width:"10%"}, data: 'applyDept',forMat:function(row){
			  if(row.applyDept){
				  	return "<span title='"+row.applyDept+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.applyDept+"</span>"
				  }else{
					  return "";
			 }
		  }},
		  {name: '参观开始时间', style:{width:"8%"},data: 'stateDate'},
		  {name: '参观结束时间', style:{width:"8%"},data: 'endDate'},
		  {name: '主要参观领导职务和级别', style:{width:"10%"},data: 'visitInfo',forMat:function(row){
			  if(row.visitInfo){
				  	return "<span title='"+row.visitInfo+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.visitInfo+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  {name: '总参观人数',style:{width:"7%"},data: 'visitorNumber'   },
		  {name: '院内陪同领导', style:{width:"10%"},data: 'leaderInfo',forMat:function(row){
			  if(row.leaderInfo){
				  	return "<span title='"+row.leaderInfo+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.leaderInfo+"</span>"
				  }else{
					  return "";
			     }
		  }},
		  
		  {name: '院内陪同人员', style:{width:"10%"},data: 'userInfo',forMat:function(row){
			  if(row.userInfo){
				  	return "<span title='"+row.userInfo+"' style='width:150px;text-align:left;display:block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;'>"+row.userInfo+"</span>"
				  }else{
					  return "";
			     }
		  }},
		 
		  {name: '总陪同人数',style:{width:"7%"},data: 'companyUserNumber'   },
		  {name: '联系人', style:{width:"6%"},data: 'contactUser'},
		  {name: '联系方式', style:{width:"8%"},data: 'contactPhone'}
		  
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
	 
		
	/* 演示中心管理-导出功能*/
 
	roomList.expEvent = function(){
		debugger;
		var $tr = $("#datagrid tr");
		if($tr.length == 1){
			alert("没有要导出的数据！");
		}else{
			var applyNumber = $("#queryForm #applyNumber").val();
			var year = $("#queryForm #year").val();
			var month = $("#queryForm #month").val();
			var applyDept = $("#queryForm #applyDept").val();
			var visitUserName = $("#queryForm #visitUserName").val();
			var visitLevel = $("#queryForm #visitLevel").val();
			var checkedIds = dataGrid.getCheckedIds();
			window.location.href = "/bg/IdeaInfo/export?ids="+checkedIds+"&applyNumber="+applyNumber+"&year="+year+"&month="+month+"&applyDept="+applyDept+"&visitUserName="+visitUserName+"&visitLevel="+visitLevel;
		}
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


 
