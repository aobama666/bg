
//定义一个
var grade = {};
var dicts = {};
$(function(){
    grade.init();
});

grade.init = function () {
    debugger;
    var paperType = $("#paperType").val();
    $.ajax({
        url: "/bg/lwGrade/gradeInit?paperType="+paperType,
        type: "post",
        dataType:"json",
        contentType: 'application/json',
        success: function (data) {
            var scoreTable = data.data.scoreTable;
            var scoreTableContent = '<table width="100%" style="text-align: center;border: #0a73a7 1px">';
            for(i = 0;i<scoreTable.length;i++){
                scoreTableContent += '<tr>';
                scoreTableContent += '<td>'+scoreTable[i].UUID+'</td>';
                scoreTableContent += '<td>'+scoreTable[i].FIRST_INDEX+'</td>';
                scoreTableContent += '<td>'+scoreTable[i].SECOND_INDEX+'</td>';
                scoreTableContent += '<td>'+scoreTable[i].WEIGHTS+'</td>';
                scoreTableContent += '<td>0~100</td>';
                scoreTableContent += '<td><input type="text"/></td>';
                scoreTableContent += '</tr>';
            }
            scoreTableContent += +'</table>';
            $("#scoreTable").html(scoreTableContent);
        }
    });
}


/*返回按钮，关闭弹出框页面*/
grade.addClose = function () {
    parent.layer.closeAll();
}