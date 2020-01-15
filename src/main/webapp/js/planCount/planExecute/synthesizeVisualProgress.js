//定义一个  计划统计-执行数据综合形象维护
var roomList = {};
var dicts = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;


roomList.updataForVisualProgress= function () {
    debugger
    /* 验证形象进度保留小数的小数或正整数0-100带%的数字*/
    var  checkPer=dataForm.validPercent();
    if(!checkPer){
        roomList.saveBtnClickFlag = 0;
        return false;
    }
    var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
    $.ajax({
        url: "/bg/planExecution/updataForVisualProgress",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(affirmFormData),
        success: function (data) {
            roomList.saveBtnClickFlag = 0;//保存按钮点击事件
            if(data.success=="true"){
                messager.tip("保存成功",1000);
                roomList.saveInfoFlag = true;//页面数据保存事件
                window.parent.location.reload();
                var closeIndex = parent.layer.getFrameIndex(window.name);
                parent.layer.close(closeIndex);
            }else{
                messager.tip(data.msg,5000);
                return;
            }
        }
    });
}



roomList.Resign= function(){
    roomList.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}


