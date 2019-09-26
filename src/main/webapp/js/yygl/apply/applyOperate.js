//用印申请，新增，修改操作

var applyOperate = {};
var dataItems = new Array();

$(function () {
})


//弹出用印种类选择框
applyOperate.checkKind = function () {
    var useSealKindCode = $("#useSealKindCode").val();
    var elseKind = $("#elseKind").val();
    var url = "/bg/yygl/apply/toCheckKind?useSealKindCode="+useSealKindCode+"&elseKind="+elseKind;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印种类选择</h4>',
        area:['30%','60%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
        }
    });
}

//用印种类保存,多选内容加其他，赋值到父页面的用印种类输入框
applyOperate.kindSave = function () {
    var checkCode = '';
    var checkValue = '';
    var elseKind = $("#elseKind").val();
    if(elseKind.length>50){
        parent.layer.msg('其他种类长度不能超过50个汉字！');
        return;
    }
    $("input[name='kind']:checked").each(function () {
        if($(this).attr("checked")){
            if(checkCode !== ''){
                checkCode += ',';
            }
            if(checkValue !== ''){
                checkValue += ';';
            }
            var splitMessage = $(this).attr('value').split(",");
            checkCode += splitMessage[0];
            checkValue += splitMessage[1];
        }
    })
    if(elseKind !== null && '' !== elseKind){
        if(checkValue !== ''){
            checkValue += ';';
        }
        checkValue += elseKind;
    }
    parent.applyOperate.assignment(checkCode,checkValue,elseKind);
    parent.layer.closeAll();
}

//种类保存赋值到页面
applyOperate.assignment = function (checkCode,checkValue,elseKind) {
    $("#useSealKindCode").val(checkCode);
    $("#useSealKindValue").val(checkValue);
    $("#elseKind").val(elseKind);
}

//新增用印申请
applyOperate.applyAdd = function () {
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //验证申请日期大于当前时间
    var useSealDate = $("#useSealDate").val();
    useSealDate = new Date(useSealDate+' 23:59:59');
    if(useSealDate<new Date()){
        layer.msg('申请日期不能早于当前时间');
        return;
    }
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    layer.confirm('确认保存该数据吗',{
            btn:['确定','取消'],icon:0,title:'保存提示'
        },function () {
            $.ajax({
                url: "/bg/yygl/apply/applyAdd",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: JSON.stringify(paperDetailFormData),
                success: function (data) {
                    if(data.success=="true"){
                        $("#uuid").attr("value",data.data.applyUuid);
                        window.location.href = "/bg/yygl/apply/toApplyUpdate?checkedId="+data.data.applyUuid;
                        layer.alert(data.msg,{icon:1,title:'信息提示'});//都跳到修改页面了还有啥提示，老实呆着吧，人家就是要添加完再去修改，你有什么办法。
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                        return;
                    }
                }
            });
        }
    )
}


//修改用印申请
applyOperate.applyUpdate = function () {
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //验证申请日期大于当前时间
    var useSealDate = $("#useSealDate").val();
    useSealDate = new Date(useSealDate+' 23:59:59');
    if(useSealDate<new Date()){
        layer.msg('申请日期不能早于当前时间');
        return;
    }
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    layer.confirm('确认保存该数据吗',{
            btn:['确定','取消'],icon:0,title:'保存提示'
        },function () {
            $.ajax({
                url: "/bg/yygl/apply/applyUpdate",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: JSON.stringify(paperDetailFormData),
                success: function (data) {
                    if(data.success=="true"){
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                        return;
                    }
                }
            });
        }
    )

}



/**
 * 提交——新增修改页面
 */
applyOperate.toSubmit = function () {
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //验证申请日期大于当前时间
    var useSealDate = $("#useSealDate").val();
    useSealDate = new Date(useSealDate+' 23:59:59');
    if(useSealDate<new Date()){
        layer.msg('申请日期不能早于当前时间');
        return;
    }

    //获取申请id
    var checkedIds = $("#uuid").val();
    if('' === checkedIds || undefined === checkedIds){
        layer.msg("请保存基本信息后再提交");
        return;
    }
    //先保存，在验证该条申请能否提交，再去弹出提交窗口
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    $.ajax({
        url: "/bg/yygl/apply/applyUpdate",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(paperDetailFormData),
        success: function (data) {
            if(data.success=="true"){
                $.ajax({
                    url: "/bg/yygl/apply/ifSubmit?checkedId="+checkedIds,
                    type: "post",
                    dataType:"json",
                    success: function (data) {
                        if(data.success=='true'){
                            var url = "/bg/yygl/apply/toApplySubmit?checkedIds="+checkedIds;
                            layer.open({
                                type:2,
                                title:'<h4 style="font-size: 18px;padding-top: 10px">选择下一环节审批人</h4>',
                                area:['40%','50%'],
                                fixed:false,//不固定
                                maxmin:true,
                                content:url,
                                end: function () {
                                    parent.layer.closeAll();
                                }
                            });
                        }else{
                            layer.msg(data.msg);
                        }
                    }
                });
            }
        }
    });


}

//提交用印申请
applyOperate.applySubmit = function () {
    //发送后台请求
    var principalUser = $("input[name='principal']:checked").val();
    var checkedIds = $("#checkedIds").val();
    if(principalUser==='' || principalUser === undefined){
        layer.msg("请选择下一环节审批人！");
        return;
    }
    $.ajax({
        url: "/bg/yygl/apply/applySubmit",
        type: "post",
        dataType:"json",
        data: {'principalUser':principalUser,'checkedIds':checkedIds},
        success: function (data) {
            if(data.success=="true"){
                parent.apply.closeAndOpen(data.msg);
            }
        }
    });
}


//返回按钮
applyOperate.returnClose = function () {
    parent.layer.closeAll();
}


/*关闭页面后弹出信息*/
applyOperate.closeAndOpen = function (message) {
    layer.closeAll();
    layer.msg(message);
};
/*用印管理-事项弹框 */
applyOperate.forItemInfo = function (){
    var url = "/bg/yyComprehensive/itemIndex";
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:27px;">用印事项</h4>',
        area:['300px','350px'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}
//提交后的反馈
/*关闭页面后弹出信息*/
var apply = {};
apply.closeAndOpen = function (message) {
    layer.closeAll();
    layer.msg(message);
};