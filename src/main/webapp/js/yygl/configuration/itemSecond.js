var itemSecondInfo = {};
itemSecondInfo.saveBtnClickFlag = 0;//保存按钮点击事件
itemSecondInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
    itemSecondInfo.onchangeForIfsign("");
    $("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'parent',checkType:'checkbox', popEvent:'pop' });
});



itemSecondInfo.onchangeForIfsign=function (deptIds) {
    var ifsignhtml = '';
    var  reg=RegExp(/,/);
    if(deptIds!=""){
        var  flag  = reg.test(deptIds);
        if(flag){
            ifsignhtml += '<option value="1" selected>是</option>';
        }else{
            ifsignhtml += '<option value="0" selected>否</option>';
        }
    }
    $("#ifsign").append(ifsignhtml)
}

function popEvent(ids,codes,names,pId,level){
    //人员树时：pId,level为空
    $("#deptId").val(ids);
    // alert("回传ids："+ids);
    // alert("回传codes："+codes);
    // alert("回传names："+names);
    // alert("回传pId："+pId);
    // alert("回传level："+level);
    itemSecondInfo.onchangeForIfsign(ids);
}

//保存
itemSecondInfo.itemSecondSave=function (){
    /* 验证必填项   */
    var validNull = dataForm.validNullable();
    if(!validNull){
        itemSecondInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证字段超长 */
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        itemSecondInfo.saveBtnClickFlag = 0;
        return;
    }
    var roomDetailFormData = roomAddInfoCommon.getFormDataInfo();
    $.messager.confirm( "保存提示", "确认保存该数据吗",
        function(r){
            if(r){
                saveForItemSecond(roomDetailFormData);
            }
        }
    );
}
 function saveForItemSecond(roomDetailFormData) {
     $.ajax({
         url: "/bg/yyConfiguration/saveForitemSecond",
         type: "post",
         dataType:"json",
         contentType: 'application/json',
         data: JSON.stringify(roomDetailFormData),
         success: function (data) {
             if(data.success=="true"){
                 messager.tip("保存成功",1000);
                 itemSecondInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                 itemSecondInfo.saveInfoFlag = true;//页面数据保存事件
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

//修改
itemSecondInfo.itemSecondUpdate=function (){
    /* 验证必填项   */
    var validNull = dataForm.validNullable();
    if(!validNull){
        itemSecondInfo.saveBtnClickFlag = 0;
        return;
    }
    /* 验证字段超长 */
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        itemSecondInfo.saveBtnClickFlag = 0;
        return;
    }
    var roomDetailFormData = roomAddInfoCommon.getFormDataInfo();
    $.messager.confirm( "修改提示", "确认修改该事项吗？该事项可能已使用，修改请谨慎。",
        function(r){
            if(r){
                updateForItemSecond(roomDetailFormData);
            }
        }
    );
}
function updateForItemSecond(roomDetailFormData) {
    $.ajax({
        url: "/bg/yyConfiguration/updateForitemSecond",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(roomDetailFormData),
        success: function (data) {
            if(data.success=="true"){
                messager.tip("修改成功",1000);
                itemSecondInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                itemSecondInfo.saveInfoFlag = true;//页面数据保存事件
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
itemSecondInfo.itemSecondResign=function (){
    itemSecondInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}















 
