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
        url: "/bg/yygl/apply/selectApply",
        // url: "/bg/yygl/my_item/selectComingSoon",
        type: 'POST',
        form: '#queryForm',
        pageSize: 10,
        tablepage: $(".tablepage"),
        columns: [
            {name: '',style:{width:"2px"}, data: 'id',checkbox:true, forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck" id="oneCheck"  index = "'+(index++)+'"  value="'+(row.UUID)+'"/>';
                }
            },
            {name: '申请编号',style:{width:"50px"}, data: 'PAPERNAME',forMat:function(row){
                    return "<a title = '点击查看用印详情' style='color: #0080FF;" +
                        " text-align:left;'id='\"+row.UUID+\"'" +
                        " onclick=myItem.toDeatil('"+row.UUID+"')>"+row.APPLY_CODE+"</a>";
                }},
            {name: '用印事由',style:{width:"50px"}, data: 'USE_SEAL_REASON'},
            {name: '用印部门',style:{width:"50px"}, data: 'DEPTNAME'},
            {name: '用印申请人',style:{width:"30px"}, data: 'USERALIAS'},
            {name: '用印日期',style:{width:"30px"}, data: 'USE_SEAL_DATE'},
            {name: '用印事项',style:{width:"50px"}, data: 'USESEALITEM'},
            {name: '用印种类',style:{width:"50px"}, data: 'USE_SEAL_KIND'}
        ]
    });
}


/**
 * 用印申请详情弹窗
 */
myItem.toDeatil = function (applyUuid) {
    var url = "/bg/yygl/apply/toApplyDetail?applyUuid="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印申请详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}


/*
弹框转到——增加业务主管部门会签
 */
myItem.toAddSign = function () {
    
}


/*
弹框转到——同意选择下一个审批人
 */
myItem.toAgree = function () {
    
}


/*
弹框转到——退回选择下一个审批人
 */
myItem.toSendBack = function () {
    
}