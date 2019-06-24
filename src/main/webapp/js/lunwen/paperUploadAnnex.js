//定义
var uploadAnnex = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
uploadAnnex.btn_type_flag = 0;
$(function(){
    uploadAnnex.initDataGrid();
	/* 输入框的change事件，在输入过程中自动查询  */
	$(".changeQuery").change(function(e){
        uploadAnnex.query();
	});
	$(".inputQuery").on("input",function(e){
		var valLength = e.target.value.length;
		if(valLength>0){
            uploadAnnex.query();
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
    uploadAnnex.btn_type_flag = 0;
});

/*  start  列表查询   */
uploadAnnex.query = function(){
	dataItems = new Array();
	index = 0;
	$("#datagrid").datagrid("seach");
}
/*  end  列表查询  */


/* 附件信息-初始化列表界面  */
uploadAnnex.initDataGrid = function(){
	    $("#datagrid").datagrid({
        url: "/bg/lwPaper/selectPaperAnnex",
        type: 'POST',
        form: '#queryForm',
        // pageSize: 10,
        // tablepage: $(".tablepage"),
        //取消分页
        pagination: false,
        showHeader: false,
        columns: [
            {name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.ID)+'"/>';
                }
            },
            {name: '附件名称',style:{width:"50px"}, data: 'DOWNLOADCOUNT'},
            {name: '文件扩展名',style:{width:"50px"}, data: 'DOWNLOADCOUNT'},
            {name: 'FTP路径', style:{width:"120px"},data: 'QUOTECOUNT'},
            {name: '上传时间', style:{width:"120px"},data: 'QUOTECOUNT'}
        ]
    });
}


/*弹出新增框 */
uploadAnnex.addOperation = function (){
    var url = "/bg/lwPaper/paperJumpUploadAnnex"
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">上传附件信息</h4>',
        area:['65%','40%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}

/*新增*/
uploadAnnex.addEvent = function (){
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
    var formData = new FormData($("#queryForm")[0]);
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    $.messager.confirm( "保存提示", "确认保存该数据吗",
        function(r){
            if(r){
                $.ajax({
                    url: "/bg/lwPaper/paperAddAnnex",
                    type: "post",
                    async: false,
                    cache: false,
                    processData: false,
                    contentType: false,
                    data: formData,
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

/*返回按钮，关闭弹出框页面*/
uploadAnnex.addClose = function () {
    parent.layer.closeAll();
}

/*删除方法*/
uploadAnnex.delEvent = function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        messager.tip("请选择要操作的数据",1000);
        return;
    }else if(checkedItems.length>1){
        messager.tip("每次只能删除一条数据",2000);
        return;
    }
    /*if(checkedItems[0].SCORETABLESTATUS!="0"){
        messager.tip("选择的数据无法删除,已生成打分表",5000);
        return;
    }*/
    // var checkedIds = dataGrid.getCheckedIds();
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
