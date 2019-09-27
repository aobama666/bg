var sign = {};
var tree;

$(function(){
    //获取当前选中部门内容
    //后台获取对应树信息
    //初始化树
    sign.ajaxInitTree();
});

/*
增加业务部门会签，也就是确认按钮
 */
sign.addSign = function () {
    debugger
    var userId = $("#userId").val();
    if(userId==='' || userId===undefined){
        layer.msg("请选择业务会签部门下的审批人");
    }
    var applyUuid = $("#applyUuid").val();
    $.ajax({
        url: "/bg/yygl/my_item/addSign",
        type: "post",
        data: {"applyUuid":applyUuid,"userId":userId},
        success: function (data) {
            parent.myItem.closeAndOpen(data.msg);
        }
    });
}

/**
 * 返回按钮
 */
sign.returnClose = function () {
    parent.layer.closeAll();
}

/*
切换部门后触发方法
 */
sign.changeDept = function () {
    sign.ajaxInitTree();
}

//根据对应部门查询信息，初始化树内容
sign.ajaxInitTree = function () {
    var deptCode = $("#signDept").val();
    /*if(deptCode === undefined || deptCode === ''){
        deptCode = '90000110';
    }*/
    $.ajax({
        url: "/bg/yygl/my_item/initDeptTree?root="+deptCode,
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        success: function (data) {
            $("#treelist").val(data.data.modelMap.treelist);
            $("#ct").val(data.data.modelMap.ct);
            $("#root").val(data.data.modelMap.root);
            sign.initTree();
        }
    });
}

//根据内容初始化树
sign.initTree = function () {
    var ct = $("#ct").val();
    var setting = {
        view: {
            selectedMulti: false
        },
        check: {
            enable: true,
            chkStyle: ct,
            radioType: "all"
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        edit: {
            enable: false
        },
        callback: {
            beforeExpand: function(event, treeNode){
                if(!treeNode.hasOwnProperty("children")){
                    $.ajax({url:'/bg/organstufftree/queryUserTreeByOrgan',
                        type:'post',
                        data:{organId:treeNode.id,organCode:treeNode.organCode},
                        success:function(data){
                            tree.addNodes(treeNode, data);
                        }
                    })
                }
            }
        }
    };

    tree = $.fn.zTree.init($("#tree"), setting, sign.parseTree());
}

sign.parseTree = function () {
    var treelist = $("#treelist").val()
    var data = $.parseJSON(treelist);
    return data;
}

//选择对应审批用户后，修改右侧内容信息
sign.clickUser = function () {
    var valueArray = tree.getCheckedNodes(true);
    if(valueArray.length === 0){
        return;
    }
    //登陆名称
    var userName   = valueArray[0].id;
    //根据登陆名称获取主键信息、中文名称、处室信息

    $.ajax({
        url: "/bg/yygl/my_item/changeUserMessage",
        type: "post",
        data: {"userName":userName},
        success: function (data) {
            //成功反馈，修改当前页面对应信息
            $("#userId").val(data.data.user.USERID);
            $("#checkUser").html(data.data.user.USERALIAS);
            $("#checkOffice").html(data.data.user.HRDEPTNAME);
        }
    });
}