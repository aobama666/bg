
//定义一个
var grade = {};
var dicts = {};
$(function(){
    grade.init();
    // messager.tip("打分成功",5000);
});

grade.init = function () {
    var paperType = $("#paperType").val();
    var pmeId = $("#pmeId").val();
    var scoreStatus = $("#scoreStatus").val();
    var totalScoreAfter = $("#totalScoreAfter").val();
    $.ajax({
        url: "/bg/lwGrade/gradeInit?paperType="+paperType+"&pmeId="+pmeId+"&scoreStatus="+scoreStatus,
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
                if(scoreStatus === '0'){//未打分
                    scoreTableContent += '<td width="10%" class="addInputStyle"><input name="score'+i+'"' +
                        ' type="text" class="validNull" content="对应分值" onkeyup="grade.ifValidNull(this)"/></td>';
                }else if(scoreStatus === '2'){//已完成
                    scoreTableContent += '<td width="10%" class="addInputStyle"><input name="score'+i+'"' +
                        ' value="'+scoreTable[i].SCORE+'" type="text" readonly/></td>';
                }else {//未提交，或者重新评审
                    scoreTableContent += '<td width="10%" class="addInputStyle"><input name="score'+i+'"' +
                        ' value="'+scoreTable[i].SCORE+'" type="text" class="validNull" content="对应分值" onkeyup="grade.ifValidNull(this)"/></td>';
                }
                scoreTableContent += '</tr>';
            }
            scoreTableContent += '<tr style="background-color: #d5e7e7;">' +
                '<th colspan="4" >总分</th>' +
                '<th  class="addInputStyle">';
            if(scoreStatus === '0') {
                scoreTableContent +='<input id="totalScore" name="totalScore" value="0" readonly/>'
            }else{
                scoreTableContent +='<input id="totalScore" name="totalScore" value="'+totalScoreAfter+'" readonly/>'
            }
            scoreTableContent +='</th></tr></table>';
            $("#scoreTable").html(scoreTableContent);
        }
    });
}


/**
 * 三种颜色轮循基础材料,rgba不兼容ie8,故使用下方对应格式编码
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
    var scoreStatus =  $("#scoreStatus").val();
    //判空
    var validNull = dataForm.validNullable();
    if(!validNull){
        return;
    }
    //判大小
    var scoreSize = dataForm.scoreSize(0,100,null);
    if(!scoreSize){
        return;
    }
    var msgTitle = '';
    var msg = '';
    if(scoreStatus === '0'){
        msgTitle = '保存提示';
        msg = '确认保存该分数信息吗?';
    }else{
        msgTitle = '修改提示';
        msg = '确认修改该分数信息吗?';
    }
    $.messager.confirm( msgTitle, msg,
        function(r){
            if(r){
                $("#queryForm").ajaxSubmit({
                    url: "/bg/lwGrade/gradeSave",
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        parent.grade.closeAndOpen(data.msg);
                    }
                });
            }
        }
    );
}



/**
 * 每次离开input都会判断，全部分数框打分之后进行加权总分计算
 */
grade.ifValidNull = function (obj) {
    //输入格式控制
    obj.value = obj.value.replace(/[^\d.]/g, "");  //清除“数字”和“.”以外的字符
    obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
    obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');//只能输入两个小数
    if (obj.value.indexOf(".") < 0 && obj.value != "") {//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
        obj.value = parseFloat(obj.value);
    }
    //大小控制，也添加了一个共有方法，参数为大小范围
    var scoreSize = dataForm.scoreSize(0,100);
    if(!scoreSize){
        return;
    }
    //添加了一个共有方法，判断form表单中的参数是否有null，如果有的话返回一个false，不做提示操作
    var validNull = dataForm.validNullableNoTip();
    if(!validNull){
        return;
    }else{
        //计算总分
        grade.getTotalScore();
    }
}


/**
 * 计算总分
 */
grade.getTotalScore = function () {
    $("#queryForm").ajaxSubmit({
        url: "/bg/lwGrade/getTotalScore",
        type: "post",
        dataType: "json",
        success: function (data) {
            //修改总分值
            $("#totalScore").val(data.data.totalScore);
        }
    });
}


/*返回按钮，关闭弹出框页面*/
grade.addClose = function () {
    parent.layer.closeAll();
}