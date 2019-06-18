
//定义一个
var roomList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
	roomList.initDataGrid();
	/* 输入框的change事件，在输入过程中自动查询  */
	$(".changeQuery").change(function(e){
		roomList.query();
	});
	$(".inputQuery").on("input",function(e){
		var valLength = e.target.value.length;
		if(valLength>0){
			roomList.query();
		}
	});
//	$(document).on("keyup",".inputQuery",function(e){
//		var valLength =$(this).val().length;
//		if(valLength>1){
//			roomList.query();
//		}
//	});
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

/*  start  列表查询   */
roomList.query = function(){
	 
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 演示中心管理-初始化列表界面  */
roomList.initDataGrid = function(){
	    $("#datagrid").datagrid({
	    url: '/bg/IdeaInfo/selectIdeaInfo',
		type: 'POST',
		form:'#queryForm',		 
		pageSize:10,
		successFinal:function(data){
			$("#datagrid").find("input[type=checkbox]").eq(0).attr("style","display:none");
        },

		columns: [
				  {name: '编号',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '论文题目',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '作者',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '作者单位',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '期刊名称',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '领域',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '推荐单位',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '被引量',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '下载量',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '专家打分',style:{width:"50px"}, data: 'ROWNO'},
				  {name: '打分状态',style:{width:"50px"}, data: 'ROWNO'}
				]
	});

}
    /*演示中心管理- 查看 */
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
	/*打分弹出框 */
	roomList.gradeOperation = function (){
		var url = "/bg/lunwen/grade_operation"
		layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:25px;">****论文</h4>',
			area:['85%','85%'],
			fixed:false,//不固定
			maxmin:true,
			content:url,
		});
	}
	/*演示中心管理-新增 */
	roomList.addEvent = function (){
		var url = "/bg/yszx/addPage"
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">参观预定申请</h4>',
				area:['85%','85%'],
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
		if(checkedItems[0].approveState!="SAVE"&& checkedItems[0].approveState!="RETURN"){
			messager.tip("该无法修改,审批状态为：待提交,被退回才可以修改",5000);
			return;
		}
		var id = dataGrid.getCheckedIds();
		var status=checkedItems[0].approveState;
		var url = "/bg/yszx/updatePage?id="+id+"&status="+status;
		layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:25px;">参观预定修改 </h4>',
			area:['85%','85%'],
			fixed:false,//不固定
			maxmin:true,
			content:url, 
		});
		 
	}
	/* 演示中心管理-删除方法*/
	roomList.delEvent = function(){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能删除一条数据",2000);
			return;
		}
		if(checkedItems[0].approveState!="SAVE"&& checkedItems[0].approveState!="RETURN"){
			messager.tip("选择的数据无法删除,审批状态为：待提交,被退回才可以删除",5000);
			return;
		}
		var checkedIds = dataGrid.getCheckedIds();
		 $.messager.confirm( "删除提示", "确认删除选中数据吗",
			function(r){
				if(r){
					$.ajax({
					    url: "/bg/IdeaInfo/deleteIdeaInfo?ideaId="+checkedIds,//删除
						type: "post",
						dataType:"json",
						contentType: 'application/json',
						success: function (data) {
							if(data.success == "true"){
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
	
	 
	
	/* 演示中心管理-提交方法*/
	roomList.submitEvent = function(){
	   debugger;
	 	var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能提交一条数据",2000);
			return;
		}
		if(checkedItems[0].approveState!="SAVE" && checkedItems[0].approveState!="RETURN"){
			messager.tip("选择的数据无法提交,审批状态为：待提交,被退回才可以提交",2000);
			return;
		}
		 var userPrivilegehtml = '';
		 $.messager.confirm("提交提示", "确认提交选中数据吗",
		 function(r){
		       if(r){
		     messageSubmit();
			}
		  });
		
		
	}
    function approveUserID(){
		var ApproveUserId = "";
		var approveState="SAVE"
		$.ajax({
		    url: "/bg/Privilege/getApproveUserByUserName?approveState="+approveState+"&type="+"submit",//获取申报界面数据字典
			type: "post",
			dataType: "json",
			async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
			success: function (data) { 
				if(data.success =='true'){
				  	var userPrivilegelist = data.data.userPrivilege;
				  	var len=userPrivilegelist.length
				  	if(len>1){
				  		ApproveUserId="";
				  	}else{
				  		ApproveUserId=userPrivilegelist[0].userId;
				  	}
				} 
			 }
		  });
		return ApproveUserId;
	}	
	/* 提交信息库信息 */
	messageSubmit= function(){
		var html=messageSubmitHtml();
		if(html =='' || html ==undefined){
			messager.tip("审批人查询失败",2000);
			return;
		}else{
			var checkedIds = dataGrid.getCheckedIds();
			var ApproveUserId=approveUserID();
			if(ApproveUserId!=""){
				  messageForSubmit(checkedIds,ApproveUserId);
			      layer.close(layer.index);
			  }else{
			      layer.confirm(
					       html,
					       {title:'请选择审批人', area:'800px',skin:'demo-class'   },
					       function(r){
					    	   if(r){
					    		   var checkedNumber = $(".userPrivilege").find("input[type=checkbox]:checked").length;
							     
					    		   var auditUserIds="";
									 if(checkedNumber == 0){
										    messager.tip("请选择要操作的数据",1000);
											return;
								     }else if (checkedNumber == 1){
								    	  auditUserIds=$(".userPrivilege").find("input[type=checkbox]:checked").siblings(".userId").val();
								     }else if(checkedNumber > 1 ){
								    	 $(".userPrivilege").find("input[type=checkbox]:checked").each(function(){
											 var auditUserId=$(this).siblings(".userId").val();
											 auditUserIds += auditUserId+","
									    });
										if(auditUserIds.length>0){
											 auditUserIds = auditUserIds.substr(0,auditUserIds.length-1);
										} 
								     }else{
								    	 messager.tip("选择审批人异常",1000);
										 return;
								     }
									 messageForSubmit(checkedIds,auditUserIds);
					    	   }
						     
		             });
			    }
		  }

	}

	
	messageForSubmit  =function (checkedIds,approvalUserd){
		 debugger;
		var   checkedItems = dataGrid.getCheckedItems(dataItems);
		var   status=checkedItems[0].approveState;
		var   approveId=checkedItems[0].approveId;
		var   stateDate=checkedItems[0].stateDate;
		var   endDate=checkedItems[0].endDate;
			  $.ajax({
				    url: "/bg/IdeaInfo/submitForStatus?ideaId="+checkedIds+"&approvalUserd="+approvalUserd+"&status="+status+"&approveId="+approveId+"&stateDate="+stateDate+"&endDate="+endDate, 
					type: "post",
					dataType:"json",
					contentType: 'application/json',
					success: function (data) {
						if(data.success == "true"){
							messager.tip("提交成功",1000);
							roomList.query();
							layer.close(layer.index);
						 }else{
							messager.tip(data.msg,2000);
							 
						  }
					 }
			   });
			   
	  }

	/* 提交信息库信息---页面拼接 */
	messageSubmitHtml=  function (){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		var approveState= "SAVE";
		var userPrivilegehtml = '';
		$.ajax({
		    url: "/bg/Privilege/getApproveUserByUserName?approveState="+approveState+"&type="+"submit",//获取申报界面数据字典
			type: "post",
			dataType: "json",
			async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
			success: function (data) {
				if(data.success =='true'){
			    	var userPrivilegelist = data.data.userPrivilege;
					userPrivilegehtml += '<table class="userPrivilege tableStyle thTableStyle">';
					userPrivilegehtml += '<tr>';
					     userPrivilegehtml += '<th>选择</th>';
					     userPrivilegehtml += '<th>审批人</th>';
					     userPrivilegehtml += '<th>部门</th>';
					userPrivilegehtml += '</tr>';
						for (var i = 0; i < userPrivilegelist.length; i++) {
							userPrivilegehtml += '<tr>';
							     userPrivilegehtml += '<td>';
							       userPrivilegehtml+='<input type="checkbox"   class="inputUserId"  />' 
							       userPrivilegehtml+='<input type="hidden"    id="userId"  name = "userId"  class="userId"  value="' + userPrivilegelist[i].userId + '"  />' 
							     userPrivilegehtml += '</td>';
							     userPrivilegehtml += '<td class="addInputStyle">  ';
							       userPrivilegehtml+='<input type="text" disabled  id="userAlias"  name = "userAlias"  class="userAlias inputChange"  value="' + userPrivilegelist[i].userAlias + '" title="审批人名称 " />' 
							     userPrivilegehtml += '</td>';
							     userPrivilegehtml += '<td class="addInputStyle">';
							       userPrivilegehtml+='<input type="text" disabled   id="deptName"   name = "deptName"   class="deptName inputChange"  value="' + userPrivilegelist[i].deptName + '" title="审批人单位" />'
							     userPrivilegehtml += '</td>';
							       
							 userPrivilegehtml += '</tr>';      
						}
					userPrivilegehtml += '</table>';
				}else{
					userPrivilegehtml ;
				}
			 
			}
		});
		return userPrivilegehtml;
	}
	
	
	
	
	/* 演示中心管理-撤销方法*/
	roomList.repealEvent = function(){
		 
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能撤销一条数据",2000);
			return;
		}
		if(checkedItems[0].approveState =="SAVE"  ){
			messager.tip("无法撤销,审批状态为：待提交不能撤销",2000);
			return;
		}
		if(checkedItems[0].approveState =="CANCEL"  ){
			messager.tip("无法撤销,审批状态为：已撤销",2000);
			return;
		}
		
		var checkedIds = dataGrid.getCheckedIds();
		$.messager.confirm( "撤销提示", "确认撤销选中数据吗",
			function(r){
				if(r){
					$.ajax({
					    url: "/bg/IdeaInfo/repealForStatus?ideaId="+checkedIds,//删除
						type: "post",
						dataType:"json",
						contentType: 'application/json',
						success: function (data) {
							if(data.success == "true"){
								messager.tip("撤销成功",1000);
								roomList.query();
							}else{
								messager.tip("撤销失败",1000);
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

