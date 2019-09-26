/**
 * 用印申请详情中的功能按钮
 */
var detail = {};
$(function(){

});

//撤回
detail.withdraw = function () {
    var applyUuid = $("#uuid").val();
    layer.confirm('确定撤回选中的申请吗?',{
        btn:['确定','取消'],icon:0,title:'撤回申请'
    },function () {
        $.ajax({
            url: "/bg/yygl/apply/applyWithdraw?applyUuid="+applyUuid,
            type: "post",
            dataType:"json",
            success: function (data) {
                layer.msg(data.msg);
                window.location.reload();
            }
        });
    })
}

//同意
detail.agree = function () {
    var applyUuid = $("#uuid").val();
    var url = "/bg/yygl/my_item/toAgree?checkedIds="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">同意并填写审批意见</h4>',
        area:['70%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            window.location.reload();
        }
    });
}

//退回=拒绝
detail.refuse = function () {
    var applyUuid = $("#uuid").val();
    var url = "/bg/yygl/my_item/toSendBack?checkedIds="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">退回并填写审批意见</h4>',
        area:['70%','50%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            window.location.reload();
        }
    });
}

//增加会签
detail.addSign = function () {
    var applyUuid = $("#uuid").val();
    var url = "/bg/yygl/my_item/toAddSign?checkedId="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">增加业务主管部门会签</h4>',
        area:['90%','60%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            window.location.reload();
        }
    });
}

//确认用印
detail.completeSeal = function () {
    var applyUuid = $("#uuid").val();
    var applyUserId = $("#applyUserId").val();
    var url = "/bg/yyComprehensive/affirmIndex?applyId="+applyUuid+"&applyUserId="+applyUserId;
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">确认用印</h4>',
        area:['600px','240px'],
        content:url,
    });
}

//刷新详情页面，考虑关闭窗口后重新打开,功能弹出窗口关闭时触发该方法
detail.refresh = function () {
    window.location.reload();
}


detail.returnItem = function () {
    var auditType=$("#auditType").val();
    if(auditType=="finish"){
        window.location.href = "/TYGLPT/index/waithaddo/index.jsp??tm="+new Date().getTime();

    }else{
        window.location.href = "/TYGLPT/index/waittodo/index.jsp?tm="+new Date().getTime();
    }
}


//部分流程操作后关闭窗口时依赖的方法
var myItem = {};
myItem.closeAndOpen = function (message) {
    layer.closeAll();
    layer.msg(message);
}