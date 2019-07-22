
//定义一个
var rule = {};
var dicts = {};
$(function(){
    rule.init();
});

rule.init = function () {
    var paperType = $("#paperType").val();
    $.ajax({
        url: "/bg/lwRule/ruleInit?paperType="+paperType,
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
                '</tr>';
            /*
                一级指标纵向合并，首行使用第一个一级指标，全盘计算k值如果符合条件将add标示改为true，for循环下一圈添加对应一级指标内容，赋值顺序就是执行逻辑
                算法拙劣，欢迎在保持原有功能下重构
            */
            var j = 0;//一级指标数量数组第几位
            var k = 0;//当前位于某个一级指标的第几个二级指标
            var add = true;//是否允许添加一级指标
            var backColor;//不同
            for(i = 0;i<scoreTable.length;i++){
                scoreTableContent += '<tr>';
                scoreTableContent += '<td style="display: none;">'+'<input name="secondIndexId'+i+'" value="'+scoreTable[i].UUID+'"/></td>';
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
                scoreTableContent += '</tr>';
            }
            scoreTableContent +='</table>';
            $("#ruleTable").html(scoreTableContent);
        }
    });
}


rule.updatePaperType = function(num) {
    document.getElementById("paperType").value=num;
    rule.init();
}