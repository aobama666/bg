var maintainInfo = {};
maintainInfo.saveBtnClickFlag = 0;//保存按钮点击事件
maintainInfo.saveInfoFlag = true;//页面数据保存事件
/**
 * 资金来源
 */
maintainInfo.forFundsSource = function(){
  var specalType = $("#specialType option:selected").val();
    var epriCodes='';
    $.ajax({
        url: "/bg/planBase/selectForFundsSource",
        type: "post",
        dataType:"json",
        async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        data: {'specalType':specalType,'epriCodes':epriCodes},
        success: function (data) {
            if(data.success=="ture"){
                var localData = data.fundsSourceList;
                $(".tree-data").combotree({
                    data:localData,
                    multiple:true
                });
                var sourceOfFunds=$("#sourceOfFundsNew").attr("data-companyLeaderName") ;
                if(sourceOfFunds!=""){
                    $(".tree-data").combotree(
                        'setValue',sourceOfFundsNew.split(",")
                    );
                }
            }else{
                layer.open({
                    title:'提示信息',
                    content:data.msg,
                    area:'300px',
                    skin:'demo-class'
                })
            }

        }
    });

}
/**
 * 资金来源
 */
maintainInfo.forCapitalFundsSource = function(){
    var specalType = $("#specialType option:selected").val();
    var epriCodes='Y001,Y003';
    $.ajax({
        url: "/bg/planBase/selectForFundsSource",
        type: "post",
        dataType:"json",
        async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        data: {'specalType':specalType,'epriCodes':epriCodes},
        success: function (data) {
            if(data.success=="ture"){
                var localData = data.fundsSourceList;
                $(".tree-data").combotree({
                    data:localData,
                    multiple:true
                });
                var sourceOfFunds=$("#sourceOfFundsNew").attr("data-companyLeaderName") ;
                if(sourceOfFunds!=""){
                    $(".tree-data").combotree(
                        'setValue',sourceOfFundsNew.split(",")
                    );
                }
            }else{
                layer.open({
                    title:'提示信息',
                    content:data.msg,
                    area:'300px',
                    skin:'demo-class'
                })
            }

        }
    });

}


 
