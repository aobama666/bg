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
    debugger
      //var zNodes  =$("#itemList").val();

    var zNodes  = [{"t":"test03","name":"test03","pId":"1","id":"B8552BB1B5C5428CA24D7024F826D3CE"},
                   {"t":"科技项目","name":"科技项目","pId":"1","id":"8FE842A398C98F05E0536C3C550AC1B4"},
                   {"t":"人力资源","name":"人力资源","pId":"1","id":"8FE842A398CA8F05E0536C3C550AC1B4"},
                   {"t":"testo3","name":"testo3","pId":"B8552BB1B5C5428CA24D7024F826D3CE","id":"75A15290B5AB40CB9659EAFC22E6BFB8"},
                   {"t":"合同","name":"合同","pId":"8FE842A398C98F05E0536C3C550AC1B4","id":"8FE842A398CE8F05E0536C3C550AC1B4"},
                   {"t":"付款","name":"付款","pId":"8FE842A398C98F05E0536C3C550AC1B4","id":"3F83C3FB361947029719628BC00D19D2"},
                   {"t":"人资","name":"人资","pId":"8FE842A398CA8F05E0536C3C550AC1B4","id":"8FE842A398D08F05E0536C3C550AC1B4"}];
    var  className = "dark";
    function beforeClick(treeId, treeNode, clickFlag) {
        className = (className === "dark" ? "":"dark");
        return (treeNode.click != false);
    }
    function onClick(event, treeId, treeNode, clickFlag) {
        alert(treeNode.name +"--"+treeNode.id);
        window.parent.location.reload();
        var closeIndex = parent.layer.getFrameIndex(window.name);
        parent.layer.close(closeIndex);
    }
    $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    });
});








 
