
//定义一个
var paperList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
paperList.btn_type_flag = 0;
$(function(){
    paperList.initDataGrid();
	/* 输入框的change事件，在输入过程中自动查询  */
	$(".changeQuery").change(function(e){
        paperList.query();
	});
	$(".inputQuery").on("input",function(e){
		var valLength = e.target.value.length;
		if(valLength>0){
            paperList.query();
		}
	});
//	$(document).on("keyup",".inputQuery",function(e){
//		var valLength =$(this).val().length;
//		if(valLength>1){
//			paperList.query();
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
    paperList.btn_type_flag = 0;
});

/*  start  列表查询   */
paperList.query = function(){
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/**
 * 切换论文类型查询
 */
paperList.updatePaperType = function(num) {
    document.getElementById("paperType").value=num;
    paperList.query();
}

/* 论文管理-初始化列表界面  */
paperList.initDataGrid = function(){
	    $("#datagrid").datagrid({
        url: "/bg/lwPaper/selectLwPaper",
        type: 'POST',
        form: '#queryForm',
        pageSize: 10,
        tablepage: $(".tablepage"),//分页组件
        successFinal:function(data){
            paperList.resize();
        },
        callBackFunc:function(){
            paperList.initItems();
        },
        columns: [
            {name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.ID)+'"/>';
                }
            },
            {name: '编号',style:{width:"50px"}, data: 'PAPERID'},
            {name: '论文题目',style:{width:"10%"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '"+row.applyNumber+"' style='width:250px;" +
                        "text-align:left;" +
                        // "white-space: nowrap;" +
                        // "text-overflow: ellipsis;" +
                        // "overflow: hidden;" +
                        //不确定上面的哪个因素导致不能弹出layer弹出框，有空了研究下
						"'id='"+row.UUID+"'" +
                        "href = 'javascript:void(0)' onclick = paperList.forDetails('"+row.UUID+"')>"+row.PAPERNAME+"</a>";

                }},
            {name: '作者',style:{width:"50px"}, data: 'AUTHOR'},
            {name: '作者单位',style:{width:"50px"}, data: 'UNIT'},
            {name: '期刊名称',style:{width:"50px"}, data: 'JOURNAL'},
            {name: '领域',style:{width:"50px"}, data: 'FIELD'},
            {name: '推荐单位',style:{width:"50px"}, data: 'RECOMMENDUNIT'},
            {name: '被引量',style:{width:"50px"}, data: 'QUOTECOUNT'},
            {name: '下载量',style:{width:"50px"}, data: 'DOWNLOADCOUNT'},
            {name: '专家信息',style:{width:"50px"}, data: 'UUID',
                forMat:function(row){
                    return "<a title = '点击查看专家信息' style='width:250px;" +
                        // "text-align:left;" +
                        "'id='"+row.UUID+"'" +
                        "href = 'javascript:void(0)' onclick = paperList.forDetails('"+row.UUID+"')>查看详情</a>";

                }},
            {name: '打分状态',style:{width:"50px"}, data: 'SCORESTATUS'},
            {name: '加权平均分',style:{width:"50px"}, data: 'DOWNLOADCOUNT'},
            {name: '去最高最低得分', style:{width:"120px"},data: 'QUOTECOUNT'}
        ]
    });
}

/*弹出新增框 */
paperList.addOperation = function (){
    var url = "/bg/lwPaper/paperJumpAdd"
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">论文信息新增</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}


/*查看*/
paperList.forDetails = function (id){
		var url = "/bg/lwPaper/detailLwPaper?uuid="+id;
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:25px;">论文信息详情</h4>',
				area:['85%','85%'],
				fixed:false,//不固定
				maxmin:true,
				content:url
			},function (index) {
				layer.close(index);
            });
	}

/*新增*/
paperList.addEvent = function (){
		var paperName = document.getElementById("paperName").value;
		var unit = document.getElementById("unit").value;
		var field = document.getElementById("field").value;
		var quoteCount = document.getElementById("quoteCount").value;
		var author = document.getElementById("author").value;
		var journal = document.getElementById("journal").value;
		var recommendUnit = document.getElementById("recommendUnit").value;
		var downloadCount = document.getElementById("downloadCount").value;
		var paperType = document.getElementById("paperType").value;

	if(null==paperName || ""==paperName){
        messager.tip("论文题目不能为空",2000);
		return;
	}
    if(null==unit || ""==unit){
        messager.tip("作者单位不能为空",2000);
        return;
    }
    if(null==field || ""==field){
        messager.tip("领域不能为空",2000);
        return;
    }
    if(null==quoteCount || ""==quoteCount){
        messager.tip("被引量不能为空",2000);
        return;
    }if(null==author || ""==author){
        messager.tip("作者不能为空",2000);
        return;
    }
    if(null==journal || ""==journal){
        messager.tip("期刊名称不能为空",2000);
        return;
    }
    if(null==recommendUnit || ""==recommendUnit){
        messager.tip("推荐单位不能为空",2000);
        return;
    }
    if(null==downloadCount || ""==downloadCount){
        messager.tip("下载量不能为空",2000);
        return;
    }
    if(null==paperType || ""==paperType){
        messager.tip("论文类型不能为空",2000);
        return;
    }

    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();

    $.messager.confirm( "保存提示", "确认保存该数据吗",
        function(r){
            if(r){
                $.ajax({
                    url: "/bg/lwPaper/paperToAdd",
                    type: "post",
                    dataType:"json",
                    contentType: 'application/json',
                    data: JSON.stringify(paperDetailFormData),
                    success: function (data) {
                        roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                        if(data.success=="true"){
                            messager.tip(data.msg,1000);
                            roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
                            window.parent.location.reload();
                            var closeIndex = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(closeIndex);
                        }else{
                            messager.tip(data.msg,5000);
                            return;
                        }


                    }
                });
            }
        }
    );
}

/*关闭新增页面*/
paperList.addClose = function () {
	parent.layer.closeAll();
}
		
	/*修改*/
paperList.updateEvent = function(){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能修改一条数据",2000);
			return;
		}
		/*if(checkedItems[0].scoreStatus!="0"&& checkedItems[0].scoreStatus!="0"){
			messager.tip("该无法修改,打分状态为：待提交,被退回才可以修改",5000);
			return;
		}*/
		var id = dataGrid.getCheckedIds();
		var status=checkedItems[0].approveState;
		var url = "/bg/lwPaper/paperJumpUpdate?uuid="+id;
		layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:25px;">论文信息修改</h4>',
			area:['85%','85%'],
			fixed:false,//不固定
			maxmin:true,
			content:url, 
		});
		 
	}
	/*删除方法*/
paperList.delEvent = function(){
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
					    url: "/bg/lwPaper/delLwPaper?uuid="+checkedIds,//删除
						type: "post",
						dataType:"json",
						contentType: 'application/json',
						success: function (data) {
							if(data.success == "true"){
								messager.tip("删除成功",1000);
								paperList.query();
							}else{
								messager.tip("删除失败",1000);
								paperList.query();
							}
						}
					});
				}
			}
		);
	}
	
	 
	
	/*提交方法*/
paperList.submitEvent = function(){
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
							paperList.query();
							layer.close(layer.index);
						 }else{
							messager.tip(data.msg,2000);
							 
						  }
					 }
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
paperList.initItems = function(){
	dataItems = new Array();
	index = 0;
}


paperList.resize=function(){
	var height=$("body").height()-$(".sheach").height()-$("#funcBtn").height()-65;
	$("#datagrid>div").css({"height":height});
}
$(window).resize(function(){
    paperList.resize();
})

