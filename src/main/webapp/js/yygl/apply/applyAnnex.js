//附件展示
var annex = {};
var dataItems = new Array();
var index = 0;
$(function(){
    annex.initDataGrid();
});


/* 用印申请列表-初始化列表界面  */
annex.initDataGrid = function(){
    var applyUuid = $("#uuid").val();
    $("#datagrid").datagrid({
        url: "/bg/yygl/applyStuff/selectStuff?applyUuid="+applyUuid,
        type: 'POST',
        form: '#queryForm',
        pageSize: 10,
        tablepage: $(".tablepage"),
        columns: [
            {name: '选择',style:{width:"20px"}, data: 'id',forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck" id="oneCheck"  index = "'+(index++)+'"  value="'+(row.uuid)+'"/>';
                }
            },
            {name: '用印材料',style:{width:"50px"}, data: 'useSealFileName',forMat:function(row){
                    return "<a title = '点击下载附件' style='width:250px;" +
                        " text-align:left;'id='\"+row.UUID+\"'" +
                        " href = 'javascript:void(0)' onclick = annex.downloadStuff('"+row.useSealFileUuid+"')>"+row.useSealFileName+"</a>";
                }},
            {name: '佐证材料',style:{width:"50px"}, data: 'proofFileName',
                forMat:function(row){
                    var rvalue = "<a title = '点击下载附件' style='width:250px;" +
                        " text-align:left;'id='\"+row.UUID+\"'" ;
                    if(row.proofFileName == undefined){
                        rvalue += "></a>";
                    }else{
                        rvalue += " href = 'javascript:void(0)' onclick = annex.downloadStuff('"+row.proofFileUuid+"')>"+row.proofFileName+"</a>";
                    }
                    return rvalue;
                }},
            {name: '用印文件份数',style:{width:"50px"}, data: 'useSealAmount'},
            {name: '备注',style:{width:"50px"}, data: 'remark'}
        ]
    });
    $(".paging").css("display","none");//隐藏分页信息
}



//下载选中材料
annex.downloadStuff = function (uuid) {
    $("#filePath").val(filePath);
    $("#fileName").val(fileName);
    $("#checkAnnexUuid").val(uuid);
    document.forms[0].action = "/bg/yygl/applyStuff/downloadStuff";
    document.forms[0].submit();
}

//弹出新增用印材料窗口
annex.toAddStuff = function () {
    var applyUuid = $("#uuid").val();
    if(applyUuid === ''){
        layer.msg("请先保存基本信息后再添加材料信息");
        return;
    }
    var url = "/bg/yygl/applyStuff/toStuffAdd?applyUuid="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印材料新增</h4>',
        area:['80%','70%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            annex.initDataGrid();
        }
    });
}


/**
 * 保存材料信息-ajaxsubmit
 */
annex.saveStuff = function() {
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

    layer.confirm('确认上传吗',{
            btn:['确定','取消'],icon:0,title:'上传提示'
        },function () {
            $("#form17").ajaxSubmit({
                url: "/bg/yygl/applyStuff/stuffAdd",
                type: "post",
                dataType: "json",
                success: function (data) {
                    if(data.success=="true"){
                        parent.applyOperate.closeAndOpen(data.msg);
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

//删除对应用印材料
annex.delStuff = function () {
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.msg('请选择要操作的数据');
        return;
    }
    var checkedIds = "";
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length>0) {
        for (var i = 0; i < checkedItems.length; i++) {
            checkedIds += checkedItems[i].uuid + ",";
        }
    }
    checkedIds = checkedIds.slice(0,checkedIds.length-1);

    layer.confirm('确定删除选中的申请吗?',{
            btn:['确定','取消'],icon:0,title:'自动匹配'
        },function () {
            $.ajax({
                url: "/bg/yygl/applyStuff/stuffDel?checkedIds="+checkedIds,
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                success: function (data) {
                    layer.msg(data.msg);
                    annex.initDataGrid();
                }
            });
        }
    )
}