//审批操作，通过审批 or 拒绝通过

var approve = {};


/*
同意
 */
approve.agree =function () {
    var useSealAdmin = $("#useSealAdmin").val();
    var ifDeptEqual = $("#ifDeptEqual").val();
    var deptNum = $("#deptNum").val();
    var toDoerId = '';

    if(useSealAdmin!=='2'){//如果需要待办人
        var toDoerId = $("input[name='staffId1']:checked").val();
        if(toDoerId === undefined || toDoerId===''){
            layer.msg('请选择下一环节审批人,每部门必须选一人');
            return;
        }

        if(deptNum>1){//如果需要选择多部门
            var i =2;
            var staffId = '';
            for(i;i<=deptNum;i++){
                debugger
                var snapName = 'staffId'+i;
                staffId = $("input[name='"+snapName+"']:checked").val();
                if(staffId === undefined || staffId===''){
                    layer.msg('请选择下一环节审批人,每部门必须选一人');
                    return;
                }
                toDoerId = toDoerId+','+staffId;
            }
        }
    }
    var approveOpinion = $('#approveOpinion').val();
    if(approveOpinion.length>500){
        parent.layer.msg('审批意见不得超过500个汉字!');
        return;
    }
    var applyUuid = $('#applyUuid').val();
    //提示加载动画，调整按钮禁用
    var loadIndex = '';//加载条
    loadIndex = layer.load(1,{
        shade: [0.5,'#fff']
    });
    $("#affirm").attr("disabled","disabled");
    $.ajax({
        url: "/bg/yygl/my_item/agree",
        type: "post",
        data: {"applyUuidS":applyUuid,"approveOpinion":approveOpinion,"toDoerId":toDoerId,'ifDeptEqual':ifDeptEqual},
        success: function (data) {
            layer.close(loadIndex);
            parent.myItem.closeAndOpen(data.msg);
        }
    });
}


/*
退回
 */
approve.sendBack = function () {
    var applyId = $("#applyId").val();
    var approveRemark = $("#approveRemark").val();
    if(approveRemark.length>500){
        parent.layer.msg('审批意见不得超过500个汉字!');
        return;
    }
    $.ajax({
        url: "/bg/yygl/my_item/sendBack",
        type: "post",
        data: {"applyIdS":applyId,"approveRemark":approveRemark},
        success: function (data) {
            parent.myItem.closeAndOpen(data.msg);
        }
    });

}


/**
 * 返回按钮
 */
approve.returnClose = function () {
    parent.layer.closeAll();
}