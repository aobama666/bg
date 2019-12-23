var maintainInfo = {};
maintainInfo.saveBtnClickFlag = 0;//保存按钮点击事件
maintainInfo.saveInfoFlag = true;//页面数据保存事件
function  commonCheck() {
    /* 验证必填项   */
    var validNull = dataForm.validNullable();
    if(!validNull){
        maintainInfo.saveBtnClickFlag = 0;
        return false;
    }
    /* 验证为计划投入金额保留两位小数的小数*/
    var  checkteleUser=dataForm.validNewNumber(0,2);
    if(!checkteleUser){
        maintainInfo.saveBtnClickFlag = 0;
        return false;
    }
    /* 验证计划项目数正整数*/
    var  checkteleUser=dataForm.validPosiviceNumber();
    if(!checkteleUser){
        maintainInfo.saveBtnClickFlag = 0;
        return false;
    }
    return  true;
}
//股权投资投入数据维护新增
function stockRightSave(){
    var  flag= commonCheck();
    if(flag){
        MaintainOfYearForSave();
    }
}
//股权投资投入数据维护修改
function stockRightUpdata(){
    var  flag= commonCheck();
    if(flag){
        MaintainOfYearForUpdata();
    }
}
//信息系统开发建设投入数据维护新增
function messageSave(){
    var  flag= commonCheck();
    if(flag){
         MaintainOfYearForSave();
    }
}
//股权投资投入数据维护修改
function messageUpdata(){
    var  flag= commonCheck();
    if(flag){
        MaintainOfYearForUpdata();
    }
}
//新增接口
function MaintainOfYearForSave(){
    var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
            $.ajax({
                url: "/bg/planInput/saveForMaintainOfYear",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: JSON.stringify(affirmFormData),
                success: function (data) {
                    maintainInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                    if(data.success=="true"){
                        messager.tip("保存成功",1000);
                        maintainInfo.saveInfoFlag = true;//页面数据保存事件
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

//修改接口
function MaintainOfYearForUpdata(){
    var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
    $.ajax({
        url: "/bg/planInput/updateForMaintainOfYear",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(affirmFormData),
        success: function (data) {
            maintainInfo.saveBtnClickFlag = 0;//保存按钮点击事件
            if(data.success=="true"){
                messager.tip("保存成功",1000);
                maintainInfo.saveInfoFlag = true;//页面数据保存事件
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

//返回
function Resign(){
    maintainInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}

function ImageProgressUpdata(){
    /* 验证形象进度保留两位小数的小数*/
    var  checkteleUser=dataForm.validNewNumber(0,2);
    if(!checkteleUser){
        maintainInfo.saveBtnClickFlag = 0;
        return false;
    }

    //形象进度的值
    var    imageProgress= $("#imageProgress").val();
    if(Number(imageProgress)>100){
        maintainInfo.saveBtnClickFlag = 0;
        messager.tip("形象进度不能超过100.00",5000);
        return false;
    }
    ImageProgressForUpdata();
}

//形象进度修改接口
function ImageProgressForUpdata(){
    var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
    $.ajax({
        url: "/bg/planExecution/updateForImageProgress",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(affirmFormData),
        success: function (data) {
            maintainInfo.saveBtnClickFlag = 0;//保存按钮点击事件
            if(data.success=="true"){
                messager.tip("保存成功",1000);
                maintainInfo.saveInfoFlag = true;//页面数据保存事件
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


 
