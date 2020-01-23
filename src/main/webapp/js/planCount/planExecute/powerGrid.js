//定义一个 计划统计--电网信息化执行数据维护
var roomList = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
    roomList.initDataGrid();
    /* 输入框的change事件，在输入过程中自动查询  */
    $(".changeQuery").change(function(e){
        roomList.query();
    });
    $(".inputQuery").on("input",function(e){
        var valLength = e.target.value.length;
        if(valLength>0){
            roomList.query();
        }
    });
    //回车键出发搜索按钮
    $("body").keydown(function () {
        if (event.keyCode == "13") {
            dataItems = new Array();
            index = 0;
            $("#datagrid").datagrid("seach");
            return false;
        }
    });
    roomList.btn_type_flag = 0;
});
/*  start  列表查询  */
roomList.query = function(){


    /* 检索条件的验证 */
    dataItems = new Array();
    index = 0;
    $("#datagrid").datagrid("seach");
}
/*  end  列表查询  */

/* 执行数据综合维护-初始化列表界面  */
roomList.initDataGrid = function(){
    $("#datagrid").datagrid({
        url: "/bg/planExecution/selectForBaseInfo",
        type: 'POST',
        form:'#queryForm',
        pageSize:10,
        showFooter:true,
        tablepage:$(".tablepage"),//分页组件
        columns: [
            {name: '序号',style:{width:"50px"}, data: 'ROWNO'},
            {name: '项目名称', style:{width:"100px"},data: 'POST1' },
            {name: '国网编码', style:{width:"100px"}, data: 'POSID'   },
            {name: '总投入', style:{width:"150px"},data: 'ZGSZTZ'},
            {name: '当年投资', style:{width:"150px"},data: 'WERT1'},
            {name: '承担单位',style:{width:"200px"},data: 'KTEXT'   },
            {name: '采购申请', style:{width:"100px"},data: 'ZSQJE' },
            {name: '招标采购完成进度',style:{width:"100px"}, data: 'BIDDING_PROGRESS',forMat:function(row){
                    return "<a title = '"+row.BIDDING_PROGRESS+"%' style='width:100px;" +
                        "color: blue;" +
                        "white-space: nowrap;" +
                        "text-overflow: ellipsis;" +
                        "overflow: hidden;'  projectId ='"+row.PSPID+"' " +
                        "href = 'javascript:void(0)' onclick = roomList.forCG('"+row.PSPID+"','"+row.PTIME+"')>"+row.BIDDING_PROGRESS+"%</a>";
                }},
            {name: '采购合同', style:{width:"100px"},data: 'ZDDJE' },
            {name: '物资到货/系统开发进度',style:{width:"150px"}, data: 'SYSTEM_DEV_PROGRESS',forMat:function(row){
                    return "<a title = '"+row.SYSTEM_DEV_PROGRESS+"%' style='width:150px;" +
                        "color: blue;" +
                        "white-space: nowrap;" +
                        "text-overflow: ellipsis;" +
                        "overflow: hidden;'  projectId ='"+row.PSPID+"' " +
                        "href = 'javascript:void(0)' onclick = roomList.forXT('"+row.PSPID+"','"+row.PTIME+"')>"+row.SYSTEM_DEV_PROGRESS+"%</a>";
                }},
            {name: '发票入账', style:{width:"100px"},data: 'ZFPRZ' },
            {name: '形象进度',style:{width:"100px"}, data: 'IMAGE_PROGRESS',forMat:function(row){
                    return row.IMAGE_PROGRESS+"%";
                }},
            {name: '计划完成数', style:{width:"80px"},data: 'PLANNED_COMPLETION' }
        ]
    });

}

/**
 * 招标采购完成进度
 */
roomList.forCG = function (projectId,year) {
    var url = "/bg/planExecution/powerGridPurchase?projectId="+projectId+"&year="+year;
    layer.open({
                type:2,
                title:'<h4 style="text-align: center;margin: 2px;font-size: 18px;padding-top: 10px">招标采购完成进度维护</h4>',
                area:['45%','33%'],
                fixed:false,//不固定
                maxmin:true,
                content:url
    });
}

roomList.purchaseForUpdata= function(){
    /* 验证形象进度保留两位小数的小数*/
    var  checkPercent=dataForm.validPercent();
    if(!checkPercent){
        roomList.saveBtnClickFlag = 0;
        return false;
    }
    var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
    $.ajax({
        url: "/bg/planExecution/updateForBiddingProgress",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(affirmFormData),
        success: function (data) {
            roomList.saveBtnClickFlag = 0;//保存按钮点击事件
            if(data.success=="true"){
                messager.tip("保存成功",1000);
                roomList.saveInfoFlag = true;//页面数据保存事件
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




/**
 * 物资到货/系统开发进度
 */
roomList.forXT = function (projectId,year) {
    var url = "/bg/planExecution/powerGridMaterial?projectId="+projectId+"&year="+year;
    layer.open({
        type:2,
        title:'<h4 style="text-align: center;margin: 2px;font-size: 18px;padding-top: 10px">物资到货/系统开发进度维护</h4>',
        area:['48%','33%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}
roomList.marginForUpdata= function(){
    var  checkPercent=dataForm.validPercent();
    if(!checkPercent){
        roomList.saveBtnClickFlag = 0;
        return false;
    }
    var   affirmFormData = roomAddInfoCommon.getFormDataInfo();
    $.ajax({
        url: "/bg/planExecution/updateForSystemDevProgress",
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        data: JSON.stringify(affirmFormData),
        success: function (data) {
            roomList.saveBtnClickFlag = 0;//保存按钮点击事件
            if(data.success=="true"){
                messager.tip("保存成功",1000);
                roomList.saveInfoFlag = true;//页面数据保存事件
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
roomList.resign= function(){
    roomList.saveInfoFlag = true;//页面数据保存事件
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}

/**
 * 计划统计--执行数据综合维护
 */
roomList.expEvent = function(){
    var $tr = $("#datagrid tr");
    if($tr.length == 1){
        alert("没有要导出的数据！");
    }else{
        var ran = Math.random()*1000;
        document.forms[0].action ="/bg/planExecution/selectForPowerGridBaseExl?ran="+ran;
        document.forms[0].submit();
    }

}





