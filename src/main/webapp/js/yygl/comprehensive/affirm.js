var affirmInfo = {};
affirmInfo.saveBtnClickFlag = 0;//保存按钮点击事件
affirmInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
	$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'radio',popEvent:'pop'});
});
function popEvent(ids,codes,names,pId,level){
    $("#applyUserName").val(names);
    $("#applyUserId").val(ids);
}
//确认
function affirmSave(){
        var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
        var  applyId=  $("#applyId").val();
        var  applyUserId=$("#applyUserId").val();
        affirmFormData.applyId=applyId;
        affirmFormData.applyUserId=applyUserId;
        layer.confirm('确认保存该数据吗?',{
                btn:['确定','取消'],icon:0,title:'确认提示'
            },function () {
                $.ajax({
                    url: "/bg/yyComprehensive/affirmForSave",
                    type: "post",
                    dataType:"json",
                    contentType: 'application/json',
                    data: JSON.stringify(affirmFormData),
                    success: function (data) {
                        affirmInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                        if(data.success=="true"){
                            messager.tip("保存成功",1000);
                            affirmInfo.saveInfoFlag = true;//页面数据保存事件
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
        )
}

//返回
function affirmResign(){
    affirmInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}














 
