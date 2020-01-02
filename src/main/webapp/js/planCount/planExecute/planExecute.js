var affirmInfo = {};
affirmInfo.saveBtnClickFlag = 0;//保存按钮点击事件
affirmInfo.saveInfoFlag = true;//页面数据保存事件
$(function(){
    yearAndDevelop();
    costAndCapital();


});
//近三年发展投入趋势
function yearAndDevelop(){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('yearAndDevelop'));
    var ItemType=['产业基建','电网小型基建','产业技改','产业大修','固定资产零星购置',
        '电力营销投入','电网信息化','生产辅助技改','生产辅助大修改','管理咨询',
        '研究开发','教育培训','股权投资','信息系统开发建设' ];
    var appregnum = [55.3, 53.8, 35.1,55.3, 3.8, 85.1,
        5.3, 23.8, 20.1,75.3, 29, 45.1,
        15.3, 3.8   ];
    var maxappreg = calMax(appregnum); //计划投入金额Y轴值
    var interval_left = maxappreg / 5; //左轴间隔
    // 指定图表的配置项和数据
    option = {
        title: {
            text: '2019年综合计划执行进度-分类型',
            left: 'center',
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            left: 'center',
            bottom:'bottom',
            data:[ '各专项执行进度','全院整体执行进度' ]
        },
        xAxis:
            {
                name: '各专项名称',
                type: 'category',
                boundaryGap: true,
                data: ItemType,
                axisLabel: {
                    interval:0,
                    rotate:30,
                }
            },
        yAxis:
            {
                name: '各专项执行进度',
                type: 'value',
                scale: true,
                min: 0,
                max: 100,
                splitNumber: 5,
                axisLabel: {
                    formatter: '{value}'
                }
            },
        series: [
            {
                name:'各专项执行进度',
                type:'bar',
                data:appregnum,
                barWidth : 20,//柱图宽度
                itemStyle:{
                    normal:{
                        color:'#4ad2ff'
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                markLine: {
                    itemStyle:{
                       normal:{
                           lineStyle:{
                               type:'solid',
                               color:'red',
                           },
                           label:{
                               show:true,
                               position:'end'
                           }
                       },
                    },
                    data:[
                        {
                            name:'全院整体执行进度',
                            type:'average',
                            yAxis:60,
                            lineStyle:{
                                color:'red'
                            }
                        }
                    ]
                }
            },
            {
                name:'全院整体执行进度',
                type:'line',
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

}






//资本性和成本性投入趋势
function costAndCapital(){
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('costAndCapital'));
    var ItemType=['产业基建','电网小型基建','产业技改','产业大修','固定资产零星购置',
        '电力营销投入','电网信息化','生产辅助技改','生产辅助大修改','管理咨询',
        '研究开发','教育培训','股权投资','信息系统开发建设' ];
    var appregnum = [55.3, 53.8, 35.1,55.3, 3.8, 85.1,
        5.3, 23.8, 20.1,75.3, 29, 45.1,
        15.3, 3.8   ];
    var maxappreg = calMax(appregnum); //计划投入金额Y轴值
    var interval_left = maxappreg / 5; //左轴间隔
    // 指定图表的配置项和数据
    option = {
        title: {
            text: '2019年综合计划执行进度-分单位',
            left: 'center',
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            left: 'center',
            bottom:'bottom',
            data:[ '各专项执行进度','全院整体执行进度' ]
        },
        xAxis:
            {
                name: '各专项名称',
                type: 'category',
                boundaryGap: true,
                data: ItemType,
                axisLabel: {
                    interval:0,
                    rotate:30,
                }
            },
        yAxis:
            {
                name: '各专项执行进度',
                type: 'value',
                scale: true,
                min: 0,
                max: 100,
                splitNumber: 5,
                axisLabel: {
                    formatter: '{value}'
                }
            },
        series: [
            {
                name:'各专项执行进度',
                type:'bar',
                data:appregnum,
                barWidth : 20,//柱图宽度
                itemStyle:{
                    normal:{
                        color:'#4ad2ff'
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                markLine: {
                    itemStyle:{
                        normal:{
                            lineStyle:{
                                type:'solid',
                                color:'red',
                            },
                            label:{
                                show:true,
                                position:'end'
                            }
                        },
                    },
                    data:[
                        {
                            name:'全院整体执行进度',
                            type:'average',
                            yAxis:60,
                            lineStyle:{
                                color:'red'
                            }
                        }
                    ]
                }
            },
            {
                name:'全院整体执行进度',
                type:'line',
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
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