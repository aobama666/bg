
var roomDetailInfo = {};
roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
var  VisitunitLevelData='';

/*修改保存*/
roomDetailInfo.messageSubmit= function(approvalUserd){

    var uuid=$("#uuid").val();
    /* 验证必填项   */
    var validNull = dataForm.validNullable();
    if(!validNull){
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证字段超长 */
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证字段为正整数*/
    var  checkteleUser=dataForm.validNumber(0);
    if(!checkteleUser){
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证联系人只能含有中文或者英文  */
    var  name=IsRight.onlyTwo("#name");
    if(!name){
        messager.tip("联系人只能含有中文或者英文",2000);
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证联系人电话格式 手机号或者xxx-xxxxxxxx或者xxxx-xxxxxxx */
    var  phone=IsRight.telePhone("#phone");
    if(!phone){
        messager.tip("11位数字手机号码或固定电话，固定电话格式为：xxx-xxxxxxxx或xxxx-xxxxxxx",2000);
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var email=$("#email").val();
    var exp =/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(!exp.test(email)){
        messager.tip("邮箱格式不正确");
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var specialist = roomAddInfoCommon.getFormDataInfo();
    specialist.uuid = uuid;
    $.messager.confirm( "保存提示", "确认保存该数据吗",
            function(r){
                if (r) {
                    $.ajax({
                        url: "/bg/expert/updateExpert",
                        type: "post",
                        contentType: 'application/json',
                        data: JSON.stringify(specialist),
                        success: function (data) {
                            roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                            if (data.success == "true") {
                                messager.tip("保存成功", 1000);
                                roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
                                window.parent.location.reload();
                                var closeIndex = parent.layer.getFrameIndex(window.name);
                                parent.layer.close(closeIndex);
                            } else {
                                messager.tip("保存失败", 5000);
                                return;
                            }
                        }
                    });
                }
            }
        );
}

//返回
roomDetailInfo.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}
