var roomList = {};
var dataItems = new Array();
var index = 0;
roomList.btn_type_flag = 0;
$(function(){
    roomList.initDataGrid();
    var year = $("#year").val();
    roomList.forSpecalType(year);
    $("#developSpecialType").change(function(e){
        roomList.yearAndDevelopQuery();
    });
    $("#costSpecialType").change(function(e){
        roomList.costAndCapitalQuery();
    });
    $(".changeQuery").change(function(e){
        roomList.yearAndItemQuery();
    });
});
roomList.query = function(){
    var specialTypeNew = $("#specialTypeNew").val();
    $("#specialType").val(specialTypeNew);
    roomList.yearAndItemQuery();
}
roomList.initDataGrid = function(){
    roomList.yearAndDevelopQuery();
    roomList.costAndCapitalQuery();
    roomList.yearAndItemQuery();
}
roomList.yearAndDevelopQuery = function(){
    var   developSpecialType= $("#developSpecialType").val();
    var   prctr= $("#prctr").val();
    var yearAndDevelopList="";
    $.ajax({
        url: "/bg/planBase/selectForYearAndDevelopListInfo",
        type: "post",
        dataType:"json",
        async:false,
        data: {'specalType':developSpecialType,'prctr':prctr},
        success: function (data) {
            yearAndDevelopList  = data.yearAndDevelopList;
        }
    });
    var year=new Array();
    var developInput=new Array();
    var developItemNumber=new Array();
    for(var i=0;i<yearAndDevelopList.length;i++){
        year.push( yearAndDevelopList[i].YEAR);
        developInput.push( yearAndDevelopList[i].TOTAL_INVEST);
        developItemNumber.push(yearAndDevelopList[i].NUMBER_OF_ITEMS);
    }
    yearAndDevelop(year,developInput,developItemNumber);
}
roomList.costAndCapitalQuery = function(){
    var    specialType= $("#costSpecialType").val();
    var    prctr= $("#prctr").val();
    var yearAndDevelopList="";
    $.ajax({
        url: "/bg/planBase/selectForCostAndCapitalInfo",
        type: "post",
        dataType:"json",
        async:false,
        data: {'specalType':specialType,'prctr':prctr},
        success: function (data) {
            yearAndDevelopList  = data.yearAndDevelopList;
        }
    });
    var year=new Array();
    var capitalMoney=new Array();//资本性
    var costmoney=new Array();//成本性
    for(var i=0;i<yearAndDevelopList.length;i++){
        year.push( yearAndDevelopList[i].YEAR);
        capitalMoney.push( yearAndDevelopList[i].CAPITAL_INVEST);
        costmoney.push(yearAndDevelopList[i].COST_INVEST);
    }
    costAndCapital(year,capitalMoney,costmoney);
}
roomList.yearAndItemQuery = function(){
    var year= $("#year").val();
    var specialType= $("#specialType").val();
    var prctr= $("#prctr").val();
    var itemList="";
    $.ajax({
        url: "/bg/planBase/selectForItemInfo",
        type: "post",
        dataType:"json",
        async:false,
        data: {'specalType':specialType,'year':year,'prctr':prctr},
        success: function (data) {
            itemList  = data.itemList;
        }
    });
    var ItemType=new Array();
    var appregnum=new Array();//资本性
    var activenum=new Array();//成本性
    for(var i=0;i<itemList.length;i++){
        ItemType.push(itemList[i].SPECIAL_NAME);
        appregnum.push(itemList[i].YEAR_INVEST);
        activenum.push(itemList[i].NUMBER_OF_ITEMS);
    }
    yearAndItem(ItemType,appregnum,activenum,year,specialType);
}

//近三年发展投入趋势
function yearAndDevelop(year,developInput,developItemNumber){
    var maxdevelopInput = calMax(developInput); //计划投入金额Y轴值
    var maxdevelopItemNumber = calMax(developItemNumber); //项目数Y轴值
    var interval_left = maxdevelopInput / 5; //左轴间隔
    var interval_right = maxdevelopItemNumber / 5; //右轴间隔
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('yearAndDevelop'));
    // 指定图表的配置项和数据
    var colors = ['#4ad2ff', '#d14a61' ];
    option = {
        color: colors,
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            left: 'center',
            bottom:'bottom',
            data: ['发展总投入','项目数']
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                data: year
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '发展总投入(万元)',
                min: 0,
                max:  maxdevelopInput,
                splitNumber: 5,
                interval: interval_left,
                position: 'left',
                axisLabel: {
                    formatter: '{value} '
                }
            },

            {
                type: 'value',
                name: '项目数(个)',
                min: 0,
                max:  maxdevelopItemNumber,
                splitNumber: 5,
                interval: interval_right,
                position: 'right',
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: '发展总投入',
                type: 'bar',
                data: developInput,
                barWidth : 100,//柱图宽度
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            },

            {
                name: '项目数',
                type: 'line',
                yAxisIndex: 1,
                data: developItemNumber,
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
              　},
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $(window).resize(
        function () {
            myChart.resize();
        }
    )
}
//资本性和成本性投入趋势
function costAndCapital(year,capitalMoney,costmoney){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('costAndCapital'));
    // 指定图表的配置项和数据
    option = {
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            left: 'center',
            bottom:'bottom',
            data:['资本性','成本性']
        },


        xAxis:  {
            type: 'category',
            name: '年份',
            splitLine:{show:false},
            data:year
        },
        yAxis: {
            type: 'value',
            name: '万元',
            axisLabel: {
                formatter: '{value}'
            }
        },
        series: [
            {
                name:'资本性',
                type:'line',
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                data:capitalMoney,
                itemStyle:{
                    normal:{
                        color:'#4ad2ff'
                    }
                },

            },
            {
                name:'成本性',
                type:'line',
                data:costmoney,
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },

            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $(window).resize(
        function () {
            myChart.resize();
        }
    )
    myChart.on('click', function (params) {
        switch (params.seriesIndex) {
            case 0:roomList.forCost(params.name );
                break;
            case 1:roomList.forCapital(params.name );
                break;
            default: break;
        }
    });
}
roomList.forCost =  function  (year) {
    var title=year+"年资本性项目详情";
    var  type="00001";
    var url = "/bg/planInput/planInputCostAndCapitalDetails?year="+year+"&type="+type;
    layer.open({
        type:2,
        title:'<h4 style="text-align: center;margin: 2px;font-size: 18px;padding-top: 10px">'+title+'</h4>',
        area:['60%','56.2%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}

roomList.forCapital =  function  (year) {
    var  type="00002";
    var title=year+"年成本性项目详情";
    var url = "/bg/planInput/planInputCostAndCapitalDetails?year="+year+"&type="+type;
    layer.open({
        type:2,
        title:'<h4 style="text-align: center;margin: 2px;font-size: 18px;padding-top: 10px">'+title+'</h4>',
        area:['60%','56.2%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}



//各专项年度投入情况
function yearAndItem(ItemType,appregnum,activenum,year,specialType ){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('yearAndItem'));
    var maxappreg = calMax(appregnum); //计划投入金额Y轴值
    var maxactive = calMax(activenum); //项目数Y轴值
    var interval_left = maxappreg / 5; //左轴间隔
    var interval_right = maxactive / 5; //右轴间隔
    // 指定图表的配置项和数据
    var colors = ['#4ad2ff', '#d14a61' ];
    option = {
        color: colors,
        tooltip: {
            trigger: 'axis'
        },

        legend: {
            left: 'center',
            bottom:'bottom',
            data:[ '计划投入金额','项目数' ]
        },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                triggerEvent:true,
                data: ItemType,
                axisLabel: {
                    interval:0,
                    rotate:30,
                },
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '计划投入金额（万元）',
                min: 0,
                max:  maxappreg,
                splitNumber: 5,
                interval: interval_left,
                position: 'left',
                axisLabel: {
                    formatter: '{value} '
                }
            },

            {
                type: 'value',
                name: '项目数',
                min: 0,
                max: maxactive,
                splitNumber: 5,
                interval: interval_right,
                position: 'right',
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: '计划投入金额',
                type: 'bar',
                data: appregnum,
                barWidth : 30,//柱图宽度
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            },

            {
                name: '项目数',
                type: 'line',
                yAxisIndex: 1,
                data: activenum,
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            }
        ]
    };






    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    $(window).resize(
        function () {
            myChart.resize();
        }
    )

    myChart.on('click', function (params) {
               if(params.componentType =='xAxis'){
                   if(params.value=='教育培训'||params.value=='股权投资'||params.value=='信息系统开发建设'){
                       return "";
                   }
                   roomList.forDetails(params.value, year);
               }else {
                   if(params.name=='教育培训'||params.name=='股权投资'||params.name=='信息系统开发建设'){
                       return "";
                   }
                   roomList.forDetails(params.name, year);
               }
    });
}
roomList.forDetails =  function  (name,year) {
    var title=year+name+"详情";
    var url = "/bg/planInput/planInputDetails?year="+year+"&name="+name;
    layer.open({
        type:2,
        title:'<h4 style="text-align: center;margin: 2px;font-size: 18px;padding-top: 10px">'+title+'</h4>',
        area:['60%','60%'],
        fixed:false,//不固定
        maxmin:true,
        content:url
    });
}
// 输出最大值
function calMax(arr) {
    var max = arr[0];
    for (var i = 1; i < arr.length; i++) { // 求出一组数组中的最大值
        if (max < arr[i]) {
            max = arr[i];
        }
    }
    var maxint = Math.ceil(max / 10); // 向上取整
    var maxval = maxint * 10; // 最终设置的最大值
    var maxvalStr=calmaxOfPositive(maxval);
    return maxvalStr; // 输出最大值
}
//取整数计算方法
function  calmaxOfPositive(maxval) {
    var   maxvalStr=10;
    var  maxValStr= maxval+"";
    var  maxValStrlen=maxValStr.length;
    if(maxValStrlen>=2){
        var  maxStrlen=maxValStr.length-2;
        var BS=1;
        for(var i=0; i<maxStrlen;i++){
            BS+='0';
        }
        var  maxValStrFirstNum=maxValStr.substring(0,1);
        var  maxValStrTwoNum=maxValStr.substring(1,2);
        var  maxValStrS=maxValStr.substring(0,2);
        if(maxValStrTwoNum>=5){
            //向上取整
            var num=Math.ceil(Number(maxValStrS)/10);
            maxvalStr =(Number(num)*10)*Number(BS) ;
        }else{
            maxvalStr =Number(maxValStrFirstNum+"5")*Number(BS);
        }
    }else if(maxValStrlen==1){
        if(maxval>=5){
            maxvalStr=10;
        }else {
            maxvalStr=5;
        }
    }else {
        maxvalStr=10;
    }
    return  maxvalStr;
}
roomList.selectforYearToSpecialType=function(){
    var year = $("#year").val();
    roomList.forSpecalType(year);
}
/**
 * 专项类型来源
 */
roomList.forSpecalType = function(year){

    $.ajax({
        url: "/bg/planBase/selectForItem",
        type: "post",
        dataType:"json",
        async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        data: {'year':year},
        success: function (data) {
            if(data.success=="ture"){
                var localData = data.itmeList;
                $(".tree-data").combotree({
                    data:localData,
                    multiple:true
                });
                var specialType=$("#specialTypeNew").attr("data-companyLeaderName") ;
                if(specialType!=""){
                    $(".tree-data").combotree(
                        'setValue',specialTypeNew.split(",")
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