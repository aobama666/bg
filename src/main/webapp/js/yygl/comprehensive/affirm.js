var affirmInfo = {};
affirmInfo.saveBtnClickFlag = 0;//保存按钮点击事件
affirmInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
	//$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'radio',popEvent:'pop'});
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
    $("#applyUserAlias").val(names);
    $("#applyUserName").val(ids);
}
//确认
function affirmSave(){
        var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
        var  applyId=  $("#applyId").val();
        var  applyUserName=$("#applyUserName").val();
        affirmFormData.applyId=applyId;
        affirmFormData.applyUserName=applyUserName;
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














 
