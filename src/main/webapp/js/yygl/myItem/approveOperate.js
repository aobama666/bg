//审批操作，通过审批 or 拒绝通过

var approve = {};


/*
同意
 */
approve.agree =function () {
    var useSealAdmin = $("#useSealAdmin").val();
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
    var applyUuid = $('#applyUuid').val();
    $.ajax({
        url: "/bg/yygl/my_item/agree",
        type: "post",
        data: {"applyUuid":applyUuid,"approveOpinion":approveOpinion,"toDoerId":toDoerId},
        success: function (data) {
            parent.myItem.closeAndOpen(data.msg);
        }
    });
}


/*
退回
 */
approve.sendBack = function () {
    alert("审核完成，已拒绝");
}


/**
 * 返回按钮
 */
approve.returnClose = function () {
    parent.layer.closeAll();
}