//用印申请列表及其功能按钮点选效果
var apply = {};
var dataItems = new Array();
var index = 0;
$(function(){
    apply.initDataGrid();
});

/* 日期查询条件 */
layui.use('laydate',function () {
    var laydate = layui.laydate;

    laydate.render({
        elem: '#startTime',
    });

    laydate.render({
        elem: '#endTime'
    });
});

/*   列表查询   */
apply.query = function(){

    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    if(startTime!=='' && startTime!==null && endTime!=='' && endTime!==null && startTime > endTime){
        layer.msg("申请日期的结束时间不能大于开始时间")
        return;
    }

    $("#datagrid").datagrid("seach");
}


/*   弹出框关闭后刷新对应页码  */
apply.queryAddPage = function(){
    $("#datagrid").datagrid("refresh");
}

/* 用印申请列表-初始化列表界面  */
apply.initDataGrid = function(){
    $("#datagrid").datagrid({
        url: "/bg/yygl/apply/selectApply",
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
                        " href = 'javascript:void(0)' >"+row.APPLY_CODE+"</a>";
            }},
            {name: '用印事由',style:{width:"50px"}, data: 'USE_SEAL_REASON'},
            {name: '用印部门',style:{width:"50px"}, data: 'DEPTNAME'},
            {name: '用印申请人',style:{width:"30px"}, data: 'USERALIAS'},
            {name: '用印日期',style:{width:"30px"}, data: 'USE_SEAL_DATE'},
            {name: '申请日期',style:{width:"30px"}, data: 'CREATE_TIME'},
            {name: '用印事项',style:{width:"30px"}, data: 'SECOND_CATEGORY_NAME'},
            {name: '用印种类',style:{width:"50px"}, data: 'USE_SEAL_KIND'},
            {name: '审批状态',style:{width:"50px"}, data: 'USE_SEAL_STATUS'},
            {name: '用印审批单',style:{width:"50px"}, forMat:function (row) {
                    return "<a title = '点击查看打印预览' style='color:#0080FF'>打印预览</a>";
                }}
        ]
    });
}


/**
 * 级联变动二级用印事项内容
 */
apply.changeItemFirst = function () {
    var firstCategoryId = $("#useSealItemFirst option:selected").val();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            document.getElementById("itemSecondId").innerHTML = checkContent;
            var i ;
            checkContent = "<option selected='selected'>请选择</option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            document.getElementById("itemSecondId").innerHTML = checkContent;
        }
    });
}


/**
 * 新增弹出窗口
 */
apply.toAdd = function () {
    var url = "/bg/yygl/apply/toApplyAdd";
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">用印申请新增</h4>',
        area:['90%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            apply.query();
        }
    });
}



/**
 * 修改弹出窗口
 */
apply.toUpdate = function () {
    //获取选中框
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.alert('请选择要操作的数据',{icon:0,title:'信息提示'});
        return;
    }else if(checkedItems.length>1){
        layer.alert('每次只能修改一条数据',{icon:0,title:'信息提示'});
        return;
    }
    var checkedId = checkedItems[0].UUID;
    var url = "/bg/yygl/apply/toApplyUpdate?checkedId="+checkedId;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">用印申请修改</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            apply.query();
        }
    });
}


/**
 * 选中删除
 */
apply.del = function () {
    //获取选中框
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.alert('请选择要操作的数据',{icon:0,title:'信息提示'});
        return;
    }
}



/**
 * 用印申请详情弹窗
 */
apply.toDeatil = function (checkedId) {
    var url = "/bg/yygl/apply/toApplyDetail?checkedId="+checkedId;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">用印申请详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            apply.query();
        }
    });
}


/**
 * 提交——用印申请列表
 */
apply.submit = function () {
    //获取选中框
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.alert('请选择要操作的数据',{icon:0,title:'信息提示'});
        return;
    }else if(checkedItems.length>5){
        layer.alert('最多同时提交五条申请',{icon:0,title:'信息提示'});
        return;
    }
    var checkedId = checkedItems[0].UUID;
}


/**
 * 撤回——用印申请列表
 */
apply.withdraw = function () {
    //获取选中框
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.alert('请选择要操作的数据',{icon:0,title:'信息提示'});
        return;
    }else if(checkedItems.length>1){
        layer.alert('每次只能撤回一条数据',{icon:0,title:'信息提示'});
        return;
    }
    var checkedId = checkedItems[0].UUID;
    $.ajax({
        url: "/bg/yygl/apply/withdraw",
        type: "post",
        dataType:"json",
        data: {'checkedId':checkedId},
        success: function (data) {
            console.info(data.data);
        }
    });
}


/**
 * 导出——可点选，可全量导出，无内容不可导出
 */
apply.applyExport = function () {
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    if(startTime!=='' && startTime!==null && endTime!=='' && endTime!==null && startTime > endTime){
        layer.msg("申请日期的结束时间不能大于开始时间")
        return;
    }

    var $tr = $("#datagrid tr");
    if($tr.length == 1){
        // layer.alert('没有要导出的数据！',{icon:0,title:'信息提示'});
        layer.msg("没有要导出的数据！");
    }else {
        var ids = "";
        var checkedItems = dataGrid.getCheckedItems(dataItems);
        if(checkedItems.length>0) {
            for (var i = 0; i < checkedItems.length; i++) {
                ids += checkedItems[i].UUID + ",";
            }
        }
        ids = ids.slice(0,ids.length-1);
        $("input[name=checkList]").val(ids);
        document.forms[0].action ="/bg/yygl/apply/export";
        document.forms[0].submit();
    }
}



/*关闭页面后弹出信息*/
apply.closeAndOpen = function (message) {
    layer.alert(message,{icon:1,title:'信息提示'});
    layer.closeAll();
};