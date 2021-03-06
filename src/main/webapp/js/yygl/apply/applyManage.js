//用印申请列表及其功能按钮点选效果
var apply = {};
var dataItems = new Array();
var index = 0;
$(function(){
    apply.initDataGrid();
});

/*   列表查询   */
apply.query = function(){

    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    if(startTime!=='' && startTime!==null && endTime!=='' && endTime!==null && startTime > endTime){
        layer.msg("申请日期的结束时间不能早于开始时间")
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
                        " onclick=apply.toDeatil('"+row.UUID+"')>"+row.APPLY_CODE+"</a>";
            }},
            {name: '用印事由',style:{width:"50px"}, data: 'USE_SEAL_REASON'},
            {name: '用印部门',style:{width:"50px"}, data: 'DEPTNAME'},
            {name: '用印申请人',style:{width:"30px"}, data: 'USERALIAS'},
            {name: '用印日期',style:{width:"30px"}, data: 'USE_SEAL_DATE'},
            {name: '申请日期',style:{width:"30px"}, data: 'CREATE_TIME'},
            {name: '用印事项',style:{width:"50px"}, data: 'USESEALITEM'},
            {name: '用印种类',style:{width:"50px"}, data: 'USE_SEAL_KIND'},
            {name: '审批状态',style:{width:"50px"}, data: 'USE_SEAL_STATUS'},
            {name: '用印审批单',style:{width:"50px"}, forMat:function (row) {
                    // if(row.USE_SEAL_STATUS_CODE=='8'){ //之前是只有已完成（待用印）才能行，现在是所有的都能行
                        return "<a title = '点击查看打印预览' style='color:#0080FF'" +
                        " onclick=apply.printPreview('"+row.UUID+"')>打印预览</a>";
                    // }
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
            document.getElementById("selectSecondItem").innerHTML = checkContent;
            var i ;
            checkContent = "" +
                "<select id = 'itemSecondId' name = 'itemSecondId'   class = 'changeQuery changeYear'>" +
                "<option value='' selected='selected'>请选择</option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            checkContent = checkContent + '</select>';
            document.getElementById("selectSecondItem").innerHTML = checkContent;
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
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印申请新增</h4>',
        area:['90%','80%'],
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
        layer.msg('请选择要修改的申请');
        return;
    }else if(checkedItems.length>1){
        layer.msg('每次只能修改一条申请');
        return;
    }
    var checkedId = checkedItems[0].UUID;
    var status = checkedItems[0].USE_SEAL_STATUS_CODE;
    if('1'!==status && '2'!==status && '3'!==status){
        //如果是未提交，已撤回，被退回的状态，可以进行修改操作，否则提示“当前申请处于审批流程,不可修改”
        layer.msg("当前申请处于审批流程中,不可修改");
        return;
    }
    var url = "/bg/yygl/apply/toApplyUpdate?checkedId="+checkedId;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印申请修改</h4>',
        area:['90%','80%'],
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
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.msg('请选择要操作的申请');
        return;
    }
    var checkedIds = "";
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length>0) {
        for (var i = 0; i < checkedItems.length; i++) {
            checkedIds += checkedItems[i].UUID + ",";
        }
    }
    checkedIds = checkedIds.slice(0,checkedIds.length-1);

    layer.confirm('确定删除选中的申请吗?',{
            btn:['确定','取消'],icon:0,title:'删除申请'
        },function () {
            $.ajax({
                url: "/bg/yygl/apply/del?checkedContent="+checkedIds,
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                success: function (data) {
                    layer.msg(data.msg);
                    apply.queryAddPage();
                }
            });
        }
    )
}



/**
 * 用印申请详情弹窗
 */
apply.toDeatil = function (applyUuid) {
    var url = "/bg/yygl/apply/toApplyDetail?accessType=4&applyUuid="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印申请详情</h4>',
        area:['85%','85%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}


/**
 * 打印预览
 */
apply.printPreview = function (applyUuid) {
    var url = "/bg/yygl/apply/toPrintPreview?applyUuid="+applyUuid;
    window.open(url);
}




/**
 * 提交——用印申请列表
 */
apply.submit = function () {
    //获取选中框
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.msg('请选择要操作的申请');
        return;
    }else if(checkedItems.length>5){
        layer.msg('最多同时提交五条申请');
        return;
    }
    var checkedIds = "";
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length>0) {
        for (var i = 0; i < checkedItems.length; i++) {
            checkedIds += checkedItems[i].UUID + ",";
        }
    }
    checkedIds = checkedIds.slice(0,checkedIds.length-1);
    //判断五个申请的事项是否一致,后期取消了这个判断条件，不用判断该条件
    /*for(var i = 0; i <checkedItems.length; i++){
        if(checkedItems[i].USESEALITEM !== checkedItems[0].USESEALITEM){
            layer.msg("批量提交时用印事项必须一致");
            return;
        }
    }*/
    $.ajax({
        url: "/bg/yygl/apply/ifSubmit?checkedId="+checkedItems[0].UUID,
        type: "post",
        dataType:"json",
        success: function (data) {
            if(data.success=='true'){
                var url = "/bg/yygl/apply/toApplySubmit?checkedIds="+checkedIds;
                layer.open({
                    type:2,
                    title:'<h4 style="font-size: 18px;padding-top: 10px">选择下一环节审批人</h4>',
                    area:['60%','40%'],
                    fixed:false,//不固定
                    maxmin:true,
                    content:url,
                    end: function () {
                        apply.queryAddPage();
                    }
                });
            }else{
                layer.msg(data.msg);
            }
        }
    });

}


/**
 * 撤回——用印申请列表
 */
apply.withdraw = function () {
    //获取选中框
    var checkedItems = dataGrid.getCheckedItems(dataItems);
    if(checkedItems.length==0){
        layer.msg('请选择要操作的申请');
        return;
    }else if(checkedItems.length>1){
        layer.msg('每次只能撤回一条申请');
        return;
    }
    var checkedId = checkedItems[0].UUID;
    var useSealStatus = checkedItems[0].USE_SEAL_STATUS_CODE;
    if(useSealStatus==='1' || useSealStatus==='2' || useSealStatus==='3'){
        layer.msg("该申请未提交无需撤回");
        return;
    }
    if(useSealStatus==='9'){
        layer.msg("该申请已确认用印，无法撤回操作");
        return;
    }

    layer.confirm('确定撤回选中的申请吗?',{
        btn:['确定','取消'],icon:0,title:'撤回申请'
    },function () {
        $.ajax({
            url: "/bg/yygl/apply/applyWithdraw?applyUuid="+checkedId,
            type: "post",
            dataType:"json",
            success: function (data) {
                layer.msg(data.msg);
                apply.queryAddPage();
            }
        });
    })
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
    layer.closeAll();
    layer.msg(message);
};


/*用印管理-事项弹框 */
apply.forItemInfo = function (){
    var url = "/bg/yyComprehensive/itemIndex";
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:27px;">用印事项</h4>',
        area:['500px','350px'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}