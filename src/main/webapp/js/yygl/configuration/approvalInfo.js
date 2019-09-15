var approvalInfo = {};
approvalInfo.saveBtnClickFlag = 0;//保存按钮点击事件
approvalInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
    newchangeItemFirst()
    $("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'radio',popEvent:'pop'});
});
function popEvent(ids,codes,names,pId,level){
    $("#popStuffTree").val(names);
    $("#approveUserName").val(ids);
    $("#approveUserCode").val(codes);
    $("#approveUserId").val(pId);
    $.ajax({
        url: "/bg/yyConfiguration/deptInfo",
        type: "post",
        dataType:"json",
        data: {'approveUserCode':codes},
        success: function (data) {
            var deptName = data.data.deptName;
            var deptId = data.data.deptId;
            $("#approveDeptId").val(deptId);
            $("#approveDeptName").val(deptName);
        }
    });

}
/**
 * 级联变动二级用印事项内容
 */
changeItemFirst = function () {
    debugger;
    var firstCategoryId = $("#itemFirstId option:selected").val();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            document.getElementById("itemSecondId").innerHTML = checkContent;
            var i ;
            checkContent = "<option selected='selected'></option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            document.getElementById("itemSecondId").innerHTML = checkContent;
        }
    });
}
/**
 * 联变动二级用印事项内容
 */
newchangeItemFirst = function () {
    var  itemSecondId=  $("#hideItemSecondId").val() ;
    var firstCategoryId = $("#itemFirstId option:selected").val();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            document.getElementById("itemSecondId").innerHTML = checkContent;
            var i ;
            checkContent = "<option selected='selected'></option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                if(k==itemSecondId){
                    checkContent = checkContent+'<option value = "'+k+'" selected>'+v+'</option>';
                }else {
                    checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
                }

            }
            document.getElementById("itemSecondId").innerHTML = checkContent;
        }
    });
}
approvalInfo.approvalForSave =function () {
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
               saveForApprival(roomDetailFormData);
            }
        }
    );
}

function saveForApprival(roomDetailFormData) {
    $.ajax({
        url: "/bg/yyConfiguration/saveForApproval",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(roomDetailFormData),
        success: function (data) {
            if(data.success=="true"){
                messager.tip("保存成功",1000);
                approvalInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                approvalInfo.saveInfoFlag = true;//页面数据保存事件
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
approvalInfo.approvalForUpdate =function () {
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
    $.messager.confirm( "修改提示", "确认修改该数据吗",
        function(r){
            if(r){
                UpdateForApprival(roomDetailFormData);
            }
        }
    );
}
function UpdateForApprival(roomDetailFormData) {
    $.ajax({
        url: "/bg/yyConfiguration/updateForApproval",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(roomDetailFormData),
        success: function (data) {
            if(data.success=="true"){
                messager.tip("修改成功",1000);
                approvalInfo.saveBtnClickFlag = 0;//保存按钮点击事件
                approvalInfo.saveInfoFlag = true;//页面数据保存事件
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
approvalInfo.approvalForResign=function (){
    approvalInfo.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}