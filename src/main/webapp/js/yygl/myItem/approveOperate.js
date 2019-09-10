//审批操作，通过审批 or 拒绝通过

var approve = {};


/*
同意
 */
approve.agree =function () {
    var applyId;
    var applyUserId;
    var approveOpinion;
    $.ajax({
        url: "/bg/yygl/my_item/agree",
        type: "post",
        data: {"applyId":applyId,"applyUserId":applyUserId,"approveOpinion":approveOpinion},
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