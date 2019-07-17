
//定义一个
var grade = {};
var dicts = {};
$(function(){
    grade.init();
    // messager.tip("打分成功",5000);
});

grade.init = function () {
    var paperType = $("#paperType").val();
    $.ajax({
        url: "/bg/lwGrade/gradeInit?paperType="+paperType,
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        success: function (data) {
            var scoreTable = data.data.scoreTable;
            var firstIndexs = data.data.firstIndexs;
            var scoreTableLength = scoreTable.length;
            $("#scoreTableLength").val(scoreTableLength);
            var scoreTableContent = '<table border="1px" align="center" style="text-align:center;width:100%;height:450px">' +
                '<tr style="background-color: #d5e7e7;">' +
                    '<th>一级指标</th>' +
                    '<th>二级指标</th>' +
                    '<th>参考要求</th>' +
                    '<th>分值</th>' +
                    '<th>打分</th>' +
                '</tr>';
            /*
                一级指标纵向合并，首行使用第一个一级指标，全盘计算k值如果符合条件将add标示改为true，for循环下一圈添加对应一级指标内容，赋值顺序就是执行逻辑
                js轮循三种颜色，黄兰粉，从一级指标开始，如果add为true，执行轮训方法，反之使用之前的颜色
                算法拙劣，欢迎在保持原有功能下重构
            */
            var j = 0;//一级指标数量数组第几位
            var k = 0;//当前位于某个一级指标的第几个二级指标
            var add = true;//是否允许添加一级指标
            var backColor;//不同
            for(i = 0;i<scoreTable.length;i++){
                if(add){
                    backColor = randomColor();
                }
                scoreTableContent += '<tr style="background-color:'+backColor+'">';
                scoreTableContent += '<td style="display: none;">'+'<input id="secondIndexId'+i+'" value="'+scoreTable[i].UUID+'"/></td>';
                if(add){
                    if(i!==0){
                        j++;
                    }
                    scoreTableContent += '<td width="15%" rowspan="'+firstIndexs[j]+'">'+scoreTable[i].FIRST_INDEX+'('+scoreTable[i].FWEIGHTS+'%)</td>';
                    k = 0;
                }
                k++;
                if(k===parseInt(firstIndexs[j])){
                    add = true;
                }else{
                    add = false;
                }
                scoreTableContent += '<td width="15%">'+scoreTable[i].SECOND_INDEX+'('+scoreTable[i].SWEIGHTS+'%)</td>';
                scoreTableContent += '<td width="50%" style="text-align: left">'+scoreTable[i].REQUIRE+'</td>';
                scoreTableContent += '<td width="10%">0~100</td>';
                scoreTableContent += '<td width="10%" class="addInputStyle"><input id="score'+i+'" type="text" class="validNull"/></td>';
                scoreTableContent += '</tr>';
            }
            scoreTableContent += '<tr style="background-color: #d5e7e7;">' +
                '<th colspan="4" >总分</th>' +
                '<th>0</th>' +
                '</tr>' +
                '</table>';
            $("#scoreTable").html(scoreTableContent);
        }
    });
}


/**
 * 三种颜色轮循基础材料,rgba不兼容ie8,故使用下方对应编码
 */
// var colors = ['rgba(255, 255, 204, 1)','rgba(204, 255, 255, 1)','rgba(255, 230, 255, 1)'];
var colors = ['#ffffcc','#ccffff','#ffe6ff'];
var randomColor = iteratorColor(colors);
function iteratorColor(array) {
    var i =0;
    return function () {
        if(i<array.length-1){
            return array[i++];
        }else{
            i=0;
            return array[array.length-1];
        }
    }
}


/**
 * 触发保存操作
 */
grade.saveGrade = function () {

}


/*返回按钮，关闭弹出框页面*/
grade.addClose = function () {
    parent.layer.closeAll();
}