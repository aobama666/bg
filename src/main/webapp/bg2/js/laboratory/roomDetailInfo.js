var roomDetailInfo = {};
roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
 
/* 保存信息库信息 */
roomDetailInfo.forSave_pro= function(){
	/* 验证必填项     */
	var validNull = dataForm.validNullable();
	if(!validNull){
		roomDetailInfo.saveBtnClickFlag = 0;
		return;
	}
	var checkLength = dataForm.checkLength();
	if(!checkLength){
		roomDetailInfo.saveBtnClickFlag = 0;
		return;
	}
	 
}



roomDetailInfo.initSelected = function(){
	/* start 查询数据字典集合  */
	$.ajax({
	    url: "/bg/DataDictionary/selectForDataDictionary",//获取 演示中心界面数据字典
		type: "post",
		success: function (data) {
			if(data.success){
				dicts = data[0];
		    	var lbType = data[0].lbType;//成果类型数据字典
				var lbTypeOption = '';
				lbTypeOption += '<option value="">全部</option>';
				for (var i = 0; i < lbType.length; i++) {
					lbTypeOption += '<option value="' + lbType[i].KEY + '">' + lbType[i].VALUE + '</option>';
				}
				$('#lbType').html(lbTypeOption);
			}
			roomList.initDataGrid();
		}
	});
	/* start 查询数据字典集合  */
}








function checkStartDate(startDate){
	var result = {};
	var currentYear=new Date().getFullYear();
	if($("select[name=category]").val()=="JS" && startDate.substr(0,4)!=currentYear){
		result.result = false;
		result.info = "技术服务非项目不能跨年；";
	}else{
		result.result = true;
		result.info = "";
	}
	return result;
}

function checkEndDate(endDate){
	var result = {};
	var currentYear=new Date().getFullYear();
	var startDate = $("input[name=startDate]").val();
	if($("select[name=category]").val()=="JS" && endDate.substr(0,4)!=currentYear){
		result.result = false;
		result.info = "技术服务非项目不能跨年；";
		return result;
	}
	if((new Date(endDate.replace(/-/g,"\/")))>(new Date(startDate.replace(/-/g,"\/")))){
		result.result = true;
		result.info = "";
	}else{
		result.result = false;
		result.info = "非项目结束时间必须大于非项目开始时间；";
	}
	return result;
}

function checkNumberFormat(planHours){
	var result = {};
	var reg=/^([0-9]|[1-9][0-9]{0,7})$/;
	if($.trim(planHours)!="" && !reg.test(planHours)){
		result.result = false;
		result.info = "必须为8位以内整数；";
	}else{
		result.result = true;
		result.info = "";
	}
	return result;
}

function checkDateRange(date){
	var result = {};
	if(getDate($("input[name='startDate']").val())<=getDate(date) && getDate($("input[name='endDate']").val())>=getDate(date)){
		result.result = true;
		result.info = "";
	}else{
		result.result = false;
		result.info = "日期超范围；";
	}
	return result;
}
function checkOrganFormat(deptName){
	var result = {};
	if($.trim(deptName) =="中国电力科学研究院有限公司"  ){
		result.result = false;
		result.info = "组织信息不能为'中国电力科学研究院有限公司'";
	}else{
		result.result = true;
		result.info = "";
	}
	return result;
}


/*时间控件change事件*/
function datePackerChange(dp, el) {
    var oldDate = dp.cal.getDateStr();
    var newDate = dp.cal.getNewDateStr();
    if (oldDate != newDate) {
        $(el).removeClass("validRefuse").addClass("validPass");
    }
}
/*时间控件change事件*/
roomDetailInfo.datePackerChange = function(dp, el) {
    var oldDate = dp.cal.getDateStr();
    var newDate = dp.cal.getNewDateStr();
    if (oldDate != newDate) {
        $(el).removeClass("validRefuse").addClass("validPass");
    }
}



