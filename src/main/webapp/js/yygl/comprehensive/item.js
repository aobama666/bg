var itemInfo = {};
itemInfo.saveBtnClickFlag = 0;//保存按钮点击事件
itemInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
    var setting = {
        data: {
            key: {
                title:"t"
            },
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeClick: beforeClick,
            onClick: onClick
        }
    };

    var itemList  = $("#itemList").val();
    var zNodes =JSON.parse(itemList);
    var  className = "dark";
    function beforeClick(treeId, treeNode, clickFlag) {
        className = (className === "dark" ? "":"dark");
        return (treeNode.click != false);
    }
    function onClick(event, treeId, treeNode, clickFlag) {
         var pid=treeNode.pId;
         if(pid==null){
             parent.$("#itemName").val(treeNode.name);
             parent.$("#itemFirst").val(treeNode.id);
             parent.$("#itemSecond").val("");
             //针对参数名称不同的页面
             parent.$("#useSealItemFirst").val(treeNode.id);
             parent.$("#useSealItemSecond").val("");
         }else{
             parent.$("#itemName").val(treeNode.name);
             parent.$("#itemFirst").val(treeNode.pId);
             parent.$("#itemSecond").val(treeNode.id);
             //针对参数名称不同的页面
             parent.$("#useSealItemFirst").val(treeNode.pId);
             parent.$("#useSealItemSecond").val(treeNode.id);

         }
        var closeIndex = parent.layer.getFrameIndex(window.name);
        parent.layer.close(closeIndex);

    }
    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    });
});
function clearChecked(){
    parent.$("#itemName").val("");
    parent.$("#itemFirst").val("");
    parent.$("#itemSecond").val("");
    //针对参数名称不同的页面
    parent.$("#useSealItemFirst").val("");
    parent.$("#useSealItemSecond").val("");
    var closeIndex = parent.layer.getFrameIndex(window.name);
    parent.layer.close(closeIndex);
}








 
