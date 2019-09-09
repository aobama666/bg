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
    alert("增加会签成功");
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
    if(deptCode === undefined || deptCode === ''){
        deptCode = '90000110';
    }
    $.ajax({
        url: "/bg/yygl/my_item/initDeptTree?root="+deptCode,
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        success: function (data) {
            debugger
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
    debugger;
    var treelist = $("#treelist").val()
    var data = $.parseJSON(treelist);
    return data;
}
