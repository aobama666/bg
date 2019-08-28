//附件展示
var annex = {};
var dataItems = new Array();
var index = 0;
$(function(){
    annex.initDataGrid();
});


/* 用印申请列表-初始化列表界面  */
annex.initDataGrid = function(){
    $("#datagrid").datagrid({
        url: "/bg/yygl/applyStuff/selectApplyAnnex",
        type: 'POST',
        form: '#queryForm',
        pageSize: 10,
        tablepage: $(".tablepage"),
        columns: [
            {name: '选择',style:{width:"20px"}, data: 'id',forMat:function(row){
                    dataItems[index] = row;//将一行数据放在一个list中
                    return '<input type="checkbox" name="oneCheck" id="oneCheck"  index = "'+(index++)+'"  value="'+(row.UUID)+'"/>';
                }
            },
            {name: '用印材料',style:{width:"50px"}, data: 'USESEALITEM'},
            {name: '佐证材料',style:{width:"50px"}, data: 'USE_SEAL_KIND'},
            {name: '用印文件份数',style:{width:"50px"}, data: 'USE_SEAL_STATUS'},
            {name: '备注',style:{width:"50px"}, data: 'USE_SEAL_STATUS'}
        ]
    });
    $(".paging").css("display","none");//隐藏分页信息
}



//弹出新增用印材料窗口
annex.toAddStuff = function () {
    var applyUuid = $("#uuid").val();
    if(applyUuid === ''){
        layer.msg("请先保存基本信息后再添加材料信息");
        return;
    }
    var url = "/bg/yygl/applyStuff/toStuffAdd?applyUuid="+applyUuid;
    layer.open({
        type:2,
        title:'<h4 style="font-size: 18px;padding-top: 10px">用印材料新增</h4>',
        area:['80%','70%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
        }
    });
}

/**
 * 保存材料信息
 */
annex.addStuff = function () {
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
                url: "/bg/yygl/applyStuff/applyAdd",
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

//删除对应用印材料
annex.delStuff = function () {


}