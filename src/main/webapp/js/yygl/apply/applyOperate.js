//用印申请，新增，修改操作

var applyOperate = {};
var dataItems = new Array();

$(function () {
    applyOperate.setDefaultValue();
})

/* 日期查询条件 */
layui.use('laydate',function () {
    var laydate = layui.laydate;

    laydate.render({
        elem: '#useSealDate',
    });
});

//修改页面-默认选中原有值-用印事项一级二级
applyOperate.setDefaultValue = function () {
    var itemFirstIdCode = $("#itemFirstIdCode").val();
    var itemSecondIdCode = $("#itemSecondIdCode").val();
    $("#useSealItemFirst").val(itemFirstIdCode);
    $("#useSealItemSecond").val(itemSecondIdCode);
}

//弹出用印种类选择框
applyOperate.checkKind = function () {
    var useSealKindCode = $("#useSealKindCode").val();
    var elseKind = $("#elseKind").val();
    var url = "/bg/yygl/apply/toCheckKind?useSealKindCode="+useSealKindCode+"&elseKind="+elseKind;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印种类选择</h4>',
        area:['30%','60%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
        }
    });
}

//用印种类保存,多选内容加其他，赋值到父页面的用印种类输入框
applyOperate.kindSave = function () {
    var checkCode = '';
    var checkValue = '';
    var elseKind = $("#elseKind").val();
    $("input[name='kind']:checked").each(function () {
        if($(this).attr("checked")){
            if(checkCode !== ''){
                checkCode += ',';
            }
            if(checkValue !== ''){
                checkValue += ';';
            }
            var splitMessage = $(this).attr('value').split(",");
            checkCode += splitMessage[0];
            checkValue += splitMessage[1];
        }
    })
    if(elseKind !== null && '' !== elseKind){
        if(checkValue !== ''){
            checkValue += ';';
        }
        checkValue += elseKind;
    }
    parent.applyOperate.assignment(checkCode,checkValue,elseKind);
    parent.layer.closeAll();
}

//种类保存赋值到页面
applyOperate.assignment = function (checkCode,checkValue,elseKind) {
    $("#useSealKindCode").val(checkCode);
    $("#useSealKindValue").val(checkValue);
    $("#elseKind").val(elseKind);
}

//新增用印申请
applyOperate.applyAdd = function () {
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    layer.confirm('确认保存该数据吗',{
            btn:['确定','取消'],icon:0,title:'保存提示'
        },function () {
            $.ajax({
                url: "/bg/yygl/apply/applyAdd",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: JSON.stringify(paperDetailFormData),
                success: function (data) {
                    if(data.success=="true"){
                        $("#uuid").attr("value",data.data.applyUuid);
                        document.getElementById("applyAdd").setAttribute("disabled","disabled");
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                        return;
                    }
                }
            });
        }
    )
}


//修改用印申请
applyOperate.applyUpdate = function () {
    //验证必填项是否为空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //验证字符长度
    var checkLength = dataForm.checkLength();
    if(!checkLength){
        return;
    }
    //获取form表单内容
    var paperDetailFormData = roomAddInfoCommon.getFormDataInfo();
    layer.confirm('确认保存该数据吗',{
            btn:['确定','取消'],icon:0,title:'保存提示'
        },function () {
            $.ajax({
                url: "/bg/yygl/apply/applyUpdate",
                type: "post",
                dataType:"json",
                contentType: 'application/json',
                data: JSON.stringify(paperDetailFormData),
                success: function (data) {
                    if(data.success=="true"){
                        document.getElementById("applyUpdate").setAttribute("disabled","disabled");
                        layer.alert(data.msg,{icon:1,title:'信息提示'});
                    }else{
                        layer.alert(data.msg,{icon:2,title:'信息提示'});
                        return;
                    }
                }
            });
        }
    )

}


//提交用印申请
applyOperate.applySubmit = function () {
    //发送后台请求

}


//返回按钮
applyOperate.returnClose = function () {
    parent.layer.closeAll();
}


/**
 * 级联变动二级用印事项内容
 */
applyOperate.changeItemFirst = function () {
    var firstCategoryId = $("#useSealItemFirst option:selected").val();
    $.ajax({
        url: "/bg/yygl/apply/secondType",
        type: "post",
        dataType:"json",
        data: {'firstCategoryId':firstCategoryId},
        success: function (data) {
            debugger
            var itemSecond = data.data.itemSecond;
            var checkContent = '';
            document.getElementById("selectSecondItem").innerHTML = checkContent;
            var i ;
            checkContent = "" +
                "<select id = 'useSealItemSecond' name = 'useSealItemSecond' content='用印事项二级' class = 'changeQuery changeYear validNull'>" +
                "<option value=''>请选择</option>";
            for(i=0;i<itemSecond.length;i++){
                var k = itemSecond[i].K;
                var v = itemSecond[i].V;
                checkContent = checkContent+'<option value = "'+k+'">'+v+'</option>';
            }
            checkContent = checkContent + '</select>';
            document.getElementById("selectSecondItem").innerHTML = checkContent;
        }
    });
}

/*关闭页面后弹出信息*/
applyOperate.closeAndOpen = function (message) {
    layer.closeAll();
    layer.msg(message);
};