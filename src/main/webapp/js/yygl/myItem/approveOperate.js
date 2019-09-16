//审批操作，通过审批 or 拒绝通过

var approve = {};


/*
同意
 */
approve.agree =function () {
    debugger;
    var deptNum = $("#deptNum").val();
    var toDoerId = $('#staffId1').val();
    var i =2;
    for(i;i<=deptNum;i++){
        var staffId = $("#staffId"+i).val()
        toDoerId = toDoerId+','+staffId;
    }
    var applyUuid = $('#applyUuid').val();
    var approveOpinion = $('#approveOpinion').val();
    $.ajax({
        url: "/bg/yygl/my_item/agree",
        type: "post",
        data: {"applyUuid":applyUuid,"approveOpinion":approveOpinion,"toDoerId":toDoerId},
        success: function (data) {
            layer.msg(data.msg);
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