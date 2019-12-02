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
        var paperUuid = $("#uuid").val();
        if(paperUuid === ''){
            //防止无参数导致的无数据导致的组件关闭不掉
            paperUuid = 'epri';
        }
	    $("#datagrid").datagrid({
            url: "/bg/lwPaper/selectPaperAnnex?uuid="+paperUuid,
            type: 'POST',
            form: '#queryForm',
            columns: [
                {name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                        dataItems[index] = row;//将一行数据放在一个list中
                        return '<input type="checkbox" name="oneCheck"  index = "'+(index++)+'"  value="'+(row.UUID)+'"/>';
                    }
                },
                {name: '附件名称',style:{width:"120px"}, data: 'FILENAME',forMat:function(row){
                        return "<a title = '点击下载附件' style='width:250px;" +
                            " text-align:left;'id='\"+row.UUID+\"'" +
                            " href = 'javascript:void(0)' onclick = paperList.downloadAnnex('"+row.UUID+"')>"+row.FILENAME+"</a>";
                }},
                {name: '文件格式',style:{width:"50px"}, data: 'FILEEXTNAME'},
                {name: '文件大小', style:{width:"50px"},data: 'FILESIZE'},
                {name: '上传时间', style:{width:"120px"},data: 'CREATETIME'}
            ],
           /* calculateHeadWidth:function(target){
                $(".tableBody").css("height","100px");
            },*/
        });
        $(".paging").css("display","none");
}


/*弹出添加附件框 */
uploadAnnex.addOperation = function (){

    var paperUuid = $("#uuid").val();
    if(!paperUuid){
        layer.alert('请保存基本信息后再添加附件',{icon:0,title:'信息提示'});
        return;
    }
    var url = "/bg/lwPaper/paperJumpUploadAnnex?paperUuid="+paperUuid;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">上传附件信息</h4>',
        area:['65%','40%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function(){
            uploadAnnex.initDataGrid();
        }
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
    var loadIndex = '';//加载条
    layer.confirm('确认上传吗',{
            btn:['确定','取消'],icon:0,title:'上传提示'
        },function () {
            loadIndex = layer.load(1,{
               shade: [0.5,'#fff']
            });
            $("#queryForm").ajaxSubmit({
                url: "/bg/lwPaper/paperAddAnnex",
                type: "post",
                dataType: "json",
                success: function (data) {
                    layer.close(loadIndex);
                    if(data.success=="true"){
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                        parent.layer.closeAll();
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                        return;
                    }
                }
            });
        },function () {
            layer.close(index);
        }
    )
}

/**
 * 批量上传触发方法
 */
uploadAnnex.addBatchEvent = function(){
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
    //判断是否为zip文件和限制文件大小100mb以内----------不兼容ie8，瞬间被摒弃
    /*var uploadFile = document.getElementById('file').files[0];
    var uploadFileSize = uploadFile.size;
    var uploadFileName = uploadFile.name;
    uploadFileName = uploadFileName.substr(uploadFileName.lastIndexOf('.'),uploadFileName.length);
    if('.zip' !== uploadFileName){
        layer.alert('上传的压缩包需为zip',{icon:0,title:'信息提示'});
        return;
    }
    if(uploadFileSize > 104857600){
        layer.alert('zip大小不能超过100MB',{icon:0,title:'信息提示'});
        return;
    }*/

    //判断文件类型
    var uploadFile = document.getElementById('file');
    var uploadName = uploadFile.value;
    var ifzip = false;
    if(uploadName.indexOf('.') !== -1){
        uploadName = uploadName.substr(uploadName.lastIndexOf('.'),uploadName.length);
        if('.zip' !== uploadName){
            ifzip = true;
        }
    }else{
        ifzip = true;
    }
    if(ifzip){
        layer.alert('上传的压缩包需为zip',{icon:0,title:'信息提示'});
        return;
    }

    var loadIndex = '';
    layer.confirm('确认上传吗',{
            btn:['确定','取消'],icon:0,title:'上传提示'
        },function () {
            loadIndex = layer.load(1,{
                shade: [0.5,'#fff']
            });
            $("#queryForm").ajaxSubmit({
                url: "/bg/lwPaper/btachUpload",
                type: "post",
                dataType: "json",
                    success: function (data) {
                    layer.close(loadIndex);
                    $("#errorFile").html('');
                    if(data.success === 'true'){
                        parent.paperList.closeAndOpen(data.msg);
                    }else{
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                        var errorMessage = data.data.errorMessage;
                        var errorFile = '<table>' +
                            '<tr>' +
                            '<th>序号</th>' +
                            '<th>文件名称</th>' +
                            '<th>上传错误原因</th>' +
                            '</tr>';
                        for(i=0;i<errorMessage.length;i++){
                            errorFile += '<tr>' +
                                '<td>'+(i+1)+'</td>' +
                                '<td>'+errorMessage[i].fileName+'</td>' +
                                '<td>'+errorMessage[i].errorReason+'</td>' +
                                '</tr>';
                        }
                        errorFile += '</table>';
                        $("#errorFile").html(errorFile);
                    }
                    return;
                }
            });
        },function () {
            layer.close(index);
        }
    )
};

/*返回按钮，关闭弹出框页面*/
uploadAnnex.addClose = function () {
    parent.layer.closeAll();
}

/*删除方法*/
uploadAnnex.delEvent = function(){
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.alert('请选择要删除的数据',{icon:0,title:'信息提示'});
        return;
    }
    var uuids = "";
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length>0) {
        for (var i = 0; i < checkedItems.length; i++) {
            uuids += checkedItems[i].UUID + ",";
        }
    }
    uuids = uuids.slice(0,uuids.length-1);
    layer.confirm('确认删除选中数据吗',{
            btn:['确定','取消'],icon:0,title:'删除提示'
        },function () {
            $.ajax({
                url: "/bg/lwPaper/paperDelAnnex?uuids="+uuids,//删除
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: '',
                success: function (data) {
                    if(data.success == "true"){
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                        uploadAnnex.query();
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                        uploadAnnex.query();
                    }
                }
            });
        },function () {
            layer.close(index);
        }
    )
}


