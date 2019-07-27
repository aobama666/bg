$(function(){
    /*全选按钮状态显示判断*/
    $(".tableBody").find("input[type='checkbox']").click(function(){
        /*初始化选择为TURE*/
        $(".check_")[0].checked = true;
        /*获取未选中的*/
        var nocheckedList = new Array();
        $(".tableBody").find('input:not(:checked)').each(function() {
            nocheckedList.push($(this).val());
        });

        /*状态显示*/
        if (nocheckedList.length == $(".tableBody").find('input').length) {
            $(".check_")[0].checked = false;
        }else if(nocheckedList.length ==0){
            $(".check_")[0].checked = true;
        }else if(nocheckedList.length){
            $(".check_")[0].checked = false;
        }
    });
    // 全选/取消
    $(".check_").click(function(){
        debugger;
        if ($(this).is(':checked')) {
            $(".tableBody").find('input').each(function(){
                $(this).prop("checked",true);
            });

        } else {
            $(".tableBody").find('input').each(function(){
                $(this).removeAttr("checked",false);
                // 根据官方的建议：具有 true 和 false 两个属性的属性，
                // 如 checked, selected 或者 disabled 使用prop()，其他的使用 attr()
                $(this).prop("checked",false);
            });
        }
    });
});