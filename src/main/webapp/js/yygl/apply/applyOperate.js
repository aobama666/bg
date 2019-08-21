//用印申请，新增，修改操作

var applyOperate = {};

//弹出用印种类选择框
applyOperate.checkKind = function () {
    var url = "/bg/yygl/apply/toCheckKind";
    layer.open({
        type:2,
        title:'<h4 style="height:42px;line-height:25px;">用印种类选择</h4>',
        area:['30%','70%'],
        fixed:false,//不固定
        maxmin:true,
        content:url,
        end: function () {
            parent.apply.query();
        }
    });
}

//新增用印申请
applyOperate.applyAdd = function () {
    
}


//修改用印申请
applyOperate.applyUpdate = function () {
    
}