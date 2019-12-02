var approvalInfo = {};
approvalInfo.saveBtnClickFlag = 0;//保存按钮点击事件
approvalInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
    approvalInfo.changeItemFirstUpdate();
    approvalInfo.onchangeForItemName();
//    $("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'radio',popEvent:'pop'});
    /*
	checkType : checkbox 多选  radio 单选  //organCode,organName,root,iframe,ct,limit,level,dataSrc,func,show;
	level:显示数节点   0 分院  1 部门  2 处室
	limit:'yes' 启用个人权限管理   '' 或  'no' 不启用
	dataSrc:数据来源：dataSrc=RLZY 人资专用，其他为报工默认
	func:功能类型：func=YYGL 用印管理
	show:show=PART 部分显示
	*/
	$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'radio',popEvent:'pop',show:'PART',level:'',limit:'no',func:'',dataSrc:''});
});
function popEvent(ids,codes,names,pId,level){
    debugger;
    $("#popStuffTree").val(names);
    $("#approveUserName").val(ids);
    $("#approveUserCode").val(codes);
    //$("#approveUserId").val(pId);
    $.ajax({
        url: "/bg/yyConfiguration/deptInfo",
        type: "post",
        dataType:"json",
        data: {'approveUserName':ids},
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
approvalInfo.changeItemFirst = function (obj) {
    var firstCategoryId = $("#itemFirstId option:selected").val();
    $(obj).parents(".contentBox").find("#itemSecondId").empty();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            checkContent += '<option value=""  ></option>';
            for(var i=0;i<itemSecond.length;i++){
                checkContent += '<option value="' + itemSecond[i].K + '">' + itemSecond[i].V + '</option>';
            }
            $("#itemSecondId").append(checkContent)
        }
    });

}
/**
 * 联变动二级用印事项内容
 */
approvalInfo.changeItemFirstUpdate = function (obj) {
    $(obj).parents(".contentBox").find("#itemSecondIds").empty();
    var  itemSecondId=  $("#hideItemSecondId").val() ;
    var firstCategoryId = $("#itemFirstId option:selected").val();
    var checkContent = '';
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
           // document.getElementById("itemSecondIds").innerHTML = checkContent;
          //  $(obj).parents(".contentBox").find("#itemSecondId").append(checkContent);
            var itemSecond = data.data.itemSecond;
            checkContent += '<option value=""  ></option>';
            for(var i=0;i<itemSecond.length;i++){
                if(itemSecond[i].K==itemSecondId){
                    checkContent += '<option  selected  value="' + itemSecond[i].K + '">' + itemSecond[i].V + '</option>';
                }else {
                    checkContent += '<option value="' + itemSecond[i].K + '">' + itemSecond[i].V + '</option>';
                }

            }
        }
    });
     $("#itemSecondId").append(checkContent)
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
    var  itemFirst=$("#itemFirst").val();
    var  itemSecond=$("#itemSecond").val();
    var  approveNodeId=$("#approveNodeId").val();
    if(approveNodeId=="2"||approveNodeId=="4"){
        if(itemFirst==""){
            messager.tip("请选择用印事项",1000);
            return;
        }
        if(approveNodeId=="2"){
          var   businessDeptInfo = approvalInfo.selectForitemSecond(itemSecond);
          var   businessDeptCode=  businessDeptInfo.businessDeptCode;
          var   businessDeptName=  businessDeptInfo.businessDeptName;
          var   approveDeptId=$("#approveDeptId").val();
              if(!businessDeptCode.match(approveDeptId)){
                  messager.tip("请选择"+businessDeptName+"的人员",1000);
                  return;
              }
        }
        if(itemSecond==""){
            messager.tip("请选择二级用印事项",1000);
            return;
        }
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
/*用印管理-事项弹框 */
approvalInfo.forItemInfo = function (){
    var url = "/bg/yyComprehensive/itemIndex";
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:27px;font-size: 14px;">用印事项</h4>',
        area:['500px','350px'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
    });
}

approvalInfo.onchangeForItemName = function () {
       var    approveNodeId= $("#approveNodeId").val();
       if(approveNodeId=="2"||approveNodeId=="4"){
          $('#itemNameInfo').css("display","table-row");
       }else {
           $('#itemNameInfo').css("display","none");
       }
}
approvalInfo.selectForitemSecond =function (itemSecondId) {
    var businessDeptInfo="";
    $.ajax({
        url: "/bg/yyComprehensive/selectForitemSecond",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
         async:false,
        data: JSON.stringify({'itemSecond':itemSecondId}),
        success: function (data) {
            if(data.success=="true"){
                businessDeptInfo=data.data;
            }else{
                businessDeptInfo="";
            }
        }
    });
    return   businessDeptInfo;
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
    var  itemFirst=$("#itemFirst").val();
    var  itemSecond=$("#itemSecond").val();
    var  approveNodeId=$("#approveNodeId").val();
    if(approveNodeId=="2"||approveNodeId=="4"){
        if(itemFirst==""){
            messager.tip("请选择用印事项",1000);
            return;
        }
        if(itemSecond==""){
            messager.tip("请选择二级用印事项",1000);
            return;
        }
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