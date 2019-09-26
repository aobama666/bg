//待办列表查询及其对应功能，已办列表查询
var myItem = {};
var dataItems = new Array();
var index = 0;
$(function(){
    myItem.initDataGrid();
});

/*   列表查询   */
myItem.query = function(){
    $("#datagrid").datagrid("seach");
}

/*   弹出框关闭后刷新对应页码  */
myItem.queryAddPage = function(){
    $("#datagrid").datagrid("refresh");
}


/**
 * 查询结果列表
 */
myItem.initDataGrid = function(){
    $("#datagrid").datagrid({
        url: "/bg/yygl/my_item/selectMyItem",
        type: 'POST',
        form: '#queryForm',
        pageSize: 10,
        tablepage: $(".tablepage"),
        columns: [
            {name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck" id="oneCheck"  index = "'+(index++)+'"  value="'+(row.uuid)+'"/>';
                }
            },
            {name: '申请编号',style:{width:"50px"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '点击查看用印详情' style='color: #0080FF;" +
                        " text-align:left;'id='\"+row.UUID+\"'" +
                        " onclick=myItem.toDeatil('"+row.uuid+"')>"+row.applyCode+"</a>";
                }},
            {name: '用印事由',style:{width:"50px"}, data: 'useSealReason'},
            {name: '用印部门',style:{width:"50px"}, data: 'applyDept'},
            {name: '用印申请人',style:{width:"30px"}, data: 'applyUser'},
            {name: '用印日期',style:{width:"30px"}, data: 'useSealDate'},
            {name: '用印事项',style:{width:"50px"}, data: 'useSealItem'},
            {name: '用印种类',style:{width:"50px"}, data: 'useSealKind'}
        ]
    });
}


/**
 * 用印申请详情弹窗
 */
myItem.toDeatil = function (applyUuid) {
    var url = "/bg/yygl/apply/toApplyDetail?accessType=0&applyUuid="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印申请详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            myItem.queryAddPage();
        }
    });
}


/*
弹框转到——增加业务主管部门会签
 */
myItem.toAddSign = function () {
    //获取选中框
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.msg('请选择要操作的数据');
        return;
    }else if(checkedItems.length>1){
        layer.msg('每次只能修改一条数据');
        return;
    }
    var checkedId = checkedItems[0].uuid;

    $.ajax({
        url: "/bg/yygl/my_item/ifAddSign",
        type: "post",
        data: {"checkedId":checkedId},
        success: function (data) {
            if(data.success === 'true'){
                var url = "/bg/yygl/my_item/toAddSign?checkedId="+checkedId;
                layer.open({
                    type:2,
                    title:'<h4 style="font-size: 18px;padding-top: 10px">增加业务主管部门会签</h4>',
                    area:['90%','60%'],
                    fixed:false,//不固定
                    maxmin:true,
                    content:url,
                    end: function () {
                        myItem.queryAddPage();
                    }
                });
            }else{
                layer.msg("当前没有此权限");
            }
        }
    });

}


/*
弹框转到——同意选择下一个审批人
 */
myItem.toAgree = function () {
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.msg('请选择要操作的数据');
        return;
    }else if(checkedItems.length>5){
        layer.msg('最多批量操作5条数据');
        return;
    }
    //获取选中框
    var checkedIds = "";
    if(checkedItems.length>0) {
        for (var i = 0; i < checkedItems.length; i++) {
            checkedIds += checkedItems[i].uuid + ",";
        }
    }
    checkedIds = checkedIds.slice(0,checkedIds.length-1);

    // var checkedId = checkedItems[0].uuid;
    var url = "/bg/yygl/my_item/toAgree?checkedIds="+checkedIds;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">同意并填写审批意见</h4>',
        area:['70%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            myItem.queryAddPage();
        }
    });
}


/*
弹框转到——退回选择下一个审批人
 */
myItem.toSendBack = function () {
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.msg('请选择要操作的数据');
        return;
    }else if(checkedItems.length>5){
        layer.msg('最多批量操作5条数据');
        return;
    }
    //获取选中框
    var checkedIds = "";
    if(checkedItems.length>0) {
        for (var i = 0; i < checkedItems.length; i++) {
            checkedIds += checkedItems[i].uuid + ",";
        }
    }
    checkedIds = checkedIds.slice(0,checkedIds.length-1);
    var url = "/bg/yygl/my_item/toSendBack?checkedIds="+checkedIds;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">退回并填写审批意见</h4>',
        area:['70%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            myItem.queryAddPage();
        }
    });
}

myItem.closeAndOpen = function (message) {
    layer.closeAll();
    layer.msg(message);
}