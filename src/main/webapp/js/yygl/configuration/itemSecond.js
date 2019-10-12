var itemSecondInfo = {};
itemSecondInfo.saveBtnClickFlag = 0;//保存按钮点击事件
itemSecondInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
    itemSecondInfo.onchangeForIfsign("");
    var  isNochecks=itemSecondInfo.selectForDeptCode();
//    $("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'parent',checkType:'checkbox', popEvent:'pop' ,level:'1',isNocheck:isNochecks });
    //$("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'parent',checkType:'checkbox', popEvent:'pop' ,level:'1'  });
    /*
	checkType : checkbox 多选  radio 单选  //organCode,organName,root,iframe,ct,limit,level,dataSrc,func,show;
	level:显示数节点   0 分院  1 部门  2 处室
	limit:'yes' 启用个人权限管理   '' 或  'no' 不启用
	dataSrc:数据来源：dataSrc=RLZY 人资专用，其他为报工默认
	func:功能类型：func=YYGL 用印管理
	show:show=PART 部分显示
	*/
    $("#organTree").organTree({root:'41000001',organCode:'deptCode',organName:'deptName',iframe:'parent',checkType:'checkbox', popEvent:'pop' ,level:'1',isNocheck:isNochecks ,show:'PART',limit:'no',func:'',dataSrc:'',tmpType:''});
});

itemSecondInfo.selectForDeptCode  =function () {
    var  isNochecks="";
    $.ajax({
        url: "/bg/yyComprehensive/selectForDeptCode",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        async:false,
       // data: JSON.stringify(roomDetailFormData),
        success: function (data) {
            if(data.success=="true"){
                var  deptList=data.data.deptList;
                var  deptIds="";
                for(i=0;i<deptList.length;i++){
                    var deptId = deptList[i].DEPTID;
                    deptIds +=deptId+",";
                }
                isNochecks=deptIds;
            }else{
                isNochecks="";
            }
        }
    });
    return  isNochecks;
}


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
    // alert("回传ids："+ids);
    // alert("回传codes："+codes);
    // alert("回传names："+names);
    // alert("回传pId："+pId);
    // alert("回传level："+level);
    debugger;
    var   deptidArr=ids.split(',');
    if(deptidArr.length>3){
        $("#deptName").val("");
        messager.tip("业务主管部门不能超过3个",5000);
        return;
    }else{
        $("#deptId").val(ids);
        itemSecondInfo.onchangeForIfsign(ids);
    }
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















 
