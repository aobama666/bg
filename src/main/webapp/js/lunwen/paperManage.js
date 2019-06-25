
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
        tablepage: $(".tablepage"),
        columns: [
            {name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.ID)+'"/>';
                }
            },
            {name: '编号',style:{width:"50px"}, data: 'PAPERID'},
            {name: '论文题目',style:{width:"10%"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '点击查看论文详情' style='width:250px;" +
                        " text-align:left;'id='\"+row.UUID+\"'" +
                        // "white-space: nowrap;" +
                        // "text-overflow: ellipsis;" +
                        // "overflow: hidden;" +
                        //不确定上面的哪个因素导致不能弹出layer弹出框，有空了研究下
                        " href = 'javascript:void(0)' onclick = paperList.forDetails('"+row.UUID+"')>"+row.PAPERNAME+"</a>";

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
                    return "<a title = '点击查看匹配专家' style='width:250px;' id='"+row.UUID+"'" +
                        "href = 'javascript:void(0)' onclick = paperList.forDetails('"+row.UUID+"')>查看详情</a>";

                }},
            {name: '打分状态',style:{width:"50px"}, data: 'SCORESTATUS'},
            {name: '加权平均分',style:{width:"50px"}, data: 'DOWNLOADCOUNT'},
            {name: '去最高最低得分', style:{width:"120px"},data: 'QUOTECOUNT'}
        ]
    });
}



/*查看详情*/
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

/*新增*/
paperList.addEvent = function (){
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //获取form表单内容
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
                        if(data.success=="true"){
                            // window.parent.location.reload();
                            // var closeIndex = parent.layer.getFrameIndex(window.name);
                            // parent.layer.close(closeIndex);
                            // paperList.query();
                            parent.messager.tip(data.msg,5000);
                            $("#uuid").val(data.data.uuid);
                        }else{
                            parent.messager.tip(data.msg,5000);
                            return;
                        }
                    }
                });
            }
        }
    );
}

/*返回按钮，关闭弹出框页面*/
paperList.addClose = function () {
    parent.layer.closeAll();
    paperList.query();
}
		
/*修改弹出框*/
paperList.updateOperation = function(){
		var checkedItems = dataGrid.getCheckedItems(dataItems);
		if(checkedItems.length==0){
			messager.tip("请选择要操作的数据",1000);
			return;
		}else if(checkedItems.length>1){
			messager.tip("每次只能修改一条数据",2000);
			return;
		}
		if(checkedItems[0].SCORETABLESTATUS!="0"){
			messager.tip("选择的数据无法修改,已生成打分表",5000);
			return;
		}
		var id = dataGrid.getCheckedIds();
		var status=checkedItems[0].approveState;
		var url = "/bg/lwPaper/paperJumpUpdate?uuid="+checkedItems[0].UUID;
		layer.open({
			type:2,
			title:'<h4 style="height:42px;line-height:25px;">论文信息修改</h4>',
			area:['85%','85%'],
			fixed:false,//不固定
			maxmin:true,
			content:url, 
		});
}


/*修改*/
paperList.updateEvent = function (){
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    $.messager.confirm( "修改提示", "确认修改该数据吗",
        function(r){
            if(r){
                $.ajax({
                    url: "/bg/lwPaper/paperToUpdate",
                    type: "post",
                    dataType:"json",
                    contentType: 'application/json',
                    data: JSON.stringify(paperDetailFormData),
                    success: function (data) {
                        if(data.success=="true"){
                            window.parent.location.reload();
                            var closeIndex = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(closeIndex);
                            parent.messager.tip(data.msg,5000);
                            paperList.query();
                        }else{
                            parent.messager.tip(data.msg,5000);
                            return;
                        }


                    }
                });
            }
        }
    );
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
		//不在前台做是否能删除的判断，在后台判断，记录日志，同时比前台更safe
		$.messager.confirm( "删除提示", "确认删除选中数据吗",
			function(r){
				if(r){
					$.ajax({
					    url: "/bg/lwPaper/delLwPaper?uuid="+checkedItems[0].UUID,//删除
						type: "post",
						dataType:"json",
						contentType: 'application/json',
                        data: '',
						success: function (data) {
							if(data.success == "true"){
								messager.tip(data.msg,3000);
								paperList.query();
							}else{
								messager.tip(data.msg,3000);
								paperList.query();
							}
						}
					});
				}
			}
		);
}


