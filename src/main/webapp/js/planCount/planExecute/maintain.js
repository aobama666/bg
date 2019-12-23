var maintainInfo = {};
maintainInfo.saveBtnClickFlag = 0;//保存按钮点击事件
maintainInfo.saveInfoFlag = true;//页面数据保存事件

/**
 * 资金来源
 */
maintainInfo.forFundsSource = function () {
    var specalType = $("#specialType option:selected").val();
    $("#sourceOfFunds").empty() ;
    var checkContent = '';
    $.ajax({
        url: "/bg/planBase/selectForFundsSource",
        type: "post",
        dataType:"json",
        async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        data: {'specalType':specalType},
        success: function (data) {
            var fundsSourceList = data.data.fundsSourceList;
            checkContent += '<option value=""  ></option>';
            for(var i=0;i<fundsSourceList.length;i++){
                checkContent += '<option value="' + fundsSourceList[i].FUNDS_SOURCE_CODE + '">' + fundsSourceList[i].FUNDS_SOURCE_NAME + '</option>';
            }
        }
    });
     $("#sourceOfFunds").append(checkContent);


}


 
