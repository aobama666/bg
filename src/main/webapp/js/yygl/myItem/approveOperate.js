//审批操作，通过审批 or 拒绝通过

var approve = {};


/*
同意
 */
approve.agree =function () {
    var useSealAdmin = $("#useSealAdmin").val();
    var ifDeptEqual = $("#ifDeptEqual").val();
    var toDoerId = '';

    if(useSealAdmin!=='2'){//如果需要待办人
        var deptNum = $("#deptNum").val();
        var toDoerId = $("input[name='staffId1']:checked").val();

        if(deptNum>1){//如果需要选择多部门
            var i =2;
            for(i;i<=deptNum;i++){
                var snapName = 'staffId'+i;
                var staffId = $("input[name='"+snapName+"']:checked").val();
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
    $.ajax({
        url: "/bg/yygl/my_item/agree",
        type: "post",
        data: {"applyUuidS":applyUuid,"approveOpinion":approveOpinion,"toDoerId":toDoerId,'ifDeptEqual':ifDeptEqual},
        success: function (data) {
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