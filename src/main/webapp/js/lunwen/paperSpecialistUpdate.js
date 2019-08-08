
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
        layer.alert('联系人只能含有中文或者英文',{icon:0,title:'信息提示'});
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证联系人电话格式 手机号或者xxx-xxxxxxxx或者xxxx-xxxxxxx */
    var  phone=IsRight.telePhone("#phone");
    if(!phone){
        layer.alert('11位数字手机号码或固定电话，固定电话格式为：xxx-xxxxxxxx或xxxx-xxxxxxx',{icon:0,title:'信息提示'});
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var email=$("#email").val();
    var exp =/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    if(!exp.test(email)){
        layer.alert('邮箱格式不正确',{icon:0,title:'信息提示'});
        roomDetailInfo.saveBtnClickFlag = 0;
        return;
    }

    var specialist = roomAddInfoCommon.getFormDataInfo();
    specialist.uuid = uuid;
    layer.confirm('确认保存该数据吗',{
            btn:['确定','取消'],icon:0,title:'保存提示'
        },function () {
            $.ajax({
                url: "/bg/expert/updateExpert",
                type: "post",
                contentType: 'application/json',
                data: JSON.stringify(specialist),
                success: function (data) {
                    if(data.success==='true'){
                        parent.queryAll.closeAndOpen(data.msg);
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                    }
                }
            });
        }
    )
}

//返回
roomDetailInfo.messageResign =function(){
    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}
