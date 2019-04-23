
var roomDetailInfo = {};
roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
 
$(function(){
	//‘新增’页面，院领导姓名多选下拉框
	roomDetailInfo.initSelectForVisitunitType();//默认查询框
	roomDetailInfo.initSelectForVisitunitLevel();//默认查询框
	roomDetailInfo.initSelectForLeader();
});
//获取当前时间
function getNowFormatDate() {    
    var date = new Date();    
    var seperator1 = "-";    
    var seperator2 = ":";    
    var month = date.getMonth() + 1;    
    var strDate = date.getDate();
    var hours = date.getHours();
    var minutes = date.getMinutes();
    if (month >= 1 && month <= 9) {        
        month = "0" + month;    
    }    
    if (strDate >= 0 && strDate <= 9) {        
    	strDate = "0" + strDate;    
    } 
    if (hours >= 0 && hours <= 9) {        
    	hours = "0" + hours;    
    } 
    if (minutes >= 0 && minutes <= 9) {        
    	minutes = "0" + minutes;    
    } 
    if(strDate >= 0 && strDate <= 9){}
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + hours + seperator2 + minutes;    
    return currentdate;
}
//验证参观开始时间和参观结束时间
function checkDate(stateDate,endDate){
    	var startTmp=stateDate.replace(" ",":").replace(/\:/g,"-").split("-");
        var endTmp=endDate.replace(" ",":").replace(/\:/g,"-").split("-");
        var sd=new Date(startTmp[0],startTmp[1],startTmp[2],startTmp[3],startTmp[4]);
        var ed=new Date(endTmp[0],endTmp[1],endTmp[2],endTmp[3],endTmp[4]);
        //参观获取当前时间
        var loaclDate=getNowFormatDate();
        var loaclTmp=loaclDate.replace(" ",":").replace(/\:/g,"-").split("-");
        var ld=new Date(loaclTmp[0],loaclTmp[1],loaclTmp[2],loaclTmp[3],loaclTmp[4]);
        //参观开始时间和当前时间比较  
        if(sd.getTime()<ld.getTime()){
            messager.tip("参观开始日期不能早于当前日期！",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
        }
        //参观开始时间和参观结束时间的比较
        if(sd.getTime()>ed.getTime()){
            messager.tip("参观结束日期不能早于参观开始日期！",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
        }
    return true;
}
/* 保存信息库信息 */
roomDetailInfo.messageSave= function(){
		/* 验证必填项   */
		var validNull = dataForm.validNullable();
		if(!validNull){
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
		}  
		/* 验证字段超长 */
		var checkLength = dataForm.checkLength();
		if(!checkLength){
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
		} 
		/* 验证字段为正整数*/
		var  checkteleUser=dataForm.validNumber(0);
		if(!checkteleUser){
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
		} 
		/* 验证联系人只能含有中文或者英文  */
		var  checkteleUser=IsRight.onlyTwo("#contactUser");
		if(!checkteleUser){
			messager.tip("联系人只能含有中文或者英文",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
		} 
		/* 验证联系人电话格式 手机号或者xxx-xxxxxxxx或者xxxx-xxxxxxx */
		var  checktelePhone=IsRight.telePhone("#contactPhone");
		if(!checktelePhone){
			messager.tip("联系人电话格式：手机号或者xxx-xxxxxxxx或者xxxx-xxxxxxx",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
		}  
		var stateDate =$("#stateDate").val();
	    var endDate = $("#endDate").val() ; 
	    //验证参观开始时间和参观结束时间的
	    checkDate(stateDate,endDate);
	   
	    //参观人数
	    var visitorNumber=$("#visitorNumber").val();
	    //参观单位名称
	    var visitUnitName=$("#visitUnitName").val();
	    //验证主要参观领导
	    //说明: 类，名的调用 用 . 
	    visitinfo=[];
	    var  sortId=0;
		 $(".visitLeader tr:gt(0)").each(function(){
		 
			 //姓名
			 var  checkteleUser=IsRight.onlyTwo("#visitUserName");
				if(!checkteleUser){
					messager.tip("参观领导姓名只能含有中文或者英文",2000);
					roomDetailInfo.saveBtnClickFlag = 0;
					return;
				} 
			 //职务
			 var checkLength = dataForm.checkLength("#visitPosition");
				if(!checkLength){
					roomDetailInfo.saveBtnClickFlag = 0;
					return;
				} 
				// 级别
				 var userLevel=$("#userLevel option:selected");
				//姓名
				 var visitUserName=$("#visitUserName").val();
				//职务
				 var visitPosition=$("#visitPosition").val();
				 //
				 sortId++;
				var visit={"userLevel":userLevel.val(),"visitUserName":visitUserName,"visitPosition":visitPosition,"sortId":sortId}
				
				 
				 visitinfo.push(visit);
			 
		 });
		
	    // 院内陪同人员信息
	    //院领导姓名
	    var companyLeaderName=$("#companyLeaderName").val();
	    //陪同人数
	    var companyUserNumber=$("#companyUserNumber").val();
	    //备注
	    var remark=$("#remark").val();
	 var roomDetailFormData = roomAddInfoCommon.getFormDataInfo();
	 roomDetailFormData.visitLevel="save";
	 roomDetailFormData.visitinfo=visitinfo;
	 //验证参观单位性质
	 var visitUnitType=$("#visitUnitType option:selected");
	 roomDetailFormData.visitUnitType=visitUnitType.val();
	 /* 保存方法 */
		$.ajax({
		    url: "/bg/IdeaInfo/addIdeaInfo",
			type: "post",
			dataType:"json",
			contentType: 'application/json',
			data: JSON.stringify(roomDetailFormData),
			success: function (p_content) {
				var p_data = p_content.P_DATA;
				roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
				if(p_data=="SUCCESS"){
					alert("保存成功");
					roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
					var closeIndex = parent.layer.getFrameIndex(window.name);
					parent.layer.close(closeIndex);
				}else{
					messager.tip("保存失败");
				}
			}
		});
	 
	 
}

roomDetailInfo.initSelectForVisitunitType = function(details){
	debugger;
	var pcode='visitunit_type';
	var optionSelectd = $(".select-person").attr("data-visitUnitType")
	/* start 查询数据字典集合  */
	$.ajax({
	    url: "/bg/DataDictionary/selectForDataDictionary?pcode="+pcode,//获取申报界面数据字典
		type: "post",
		success: function (data) {
			debugger;
			if(data.success){
				var dictData = data.dictData;
				var visitUnitType = '';
				    visitUnitType += '<option value="">请选择参观单位性质</option>';
				for (var i = 0; i < dictData.length; i++) {
					if(dictData[i].K==optionSelectd){
						visitUnitType += '<option value="' + dictData[i].K +       '"    selected >' + dictData[i].V + '</option>';
					}else{
						visitUnitType += '<option value="' + dictData[i].K +       '" >' + dictData[i].V + '</option>';
					}
				
				}
				$('#visitUnitType').html(visitUnitType); 
			
				
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
	if(details == 'details' ){
		
	}
	/* start 查询数据字典集合  */
}
/*//详情预览页面
roomDetailInfo.initSelectForVisitunitType = function(){
	var 
	$("#visitUnitType option").each(function(){
		var optionText = $(this).text();
		
	}) 
}*/

roomDetailInfo.initSelectForVisitunitLevel = function(){
	var pcode='visitunit_levle';
	var optionSelectd = $(".userLevel").attr("data-userLevel")
	/* start 查询数据字典集合  */
	$.ajax({
	    url: "/bg/DataDictionary/selectForDataDictionary?pcode="+pcode,//获取申报界面数据字典
		type: "post",
		success: function (data) {
			if(data.success){
				var dictData = data.dictData;
				var visitUnitLevel = '';
				visitUnitLevel += '<option value="">请选择参观领导级别</option>';
				for (var i = 0; i < dictData.length; i++) {
					if(dictData[i].K==optionSelectd){
						visitUnitLevel += '<option value="' + dictData[i].K + '"   selected >' + dictData[i].V + '</option>';
					}else{
						visitUnitLevel += '<option value="' + dictData[i].K + '">' + dictData[i].V + '</option>';
					}
					
				}
				$('.userLevel').html(visitUnitLevel); 
				
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
	/* start 查询数据字典集合  */
}

roomDetailInfo.initSelectForLeader = function(){
	var pcode='visitunit_levle';
	/* start 查询数据字典集合  */
	$.ajax({
	    url: "/bg/IdeaInfo/selectForLeader" ,//获取申报界面数据字典
		type: "post",
		success: function (data) {
			if(data.success){
				var leaderData = data.leaderData;
				var localData =  leaderData;
				$(".tree-data").combotree({
					data:localData,
					multiple:true
				});
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
	/* start 查询数据字典集合  */
}

/* 提交信息库信息 */
roomDetailInfo.messageSubmit= function(){
	alert(stateDate); 
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

/*2019.04.11演示中心页面新增*/
//点击‘返回’按钮，弹出提示层
function resignChange(){
    layer.open({
        title:'提示信息',
        content:'确定要返回吗？',
        area:'300px',
        btn:['确定','取消'],
        skin:'demo-class',
        yes:function(index,layero){
            layer.close(index);
            
        }
    });
}
//新增页面，主要参观领导的新增
function addLeader(obj){
 
	var html = '';
	html +='<tr>'+
				'<td>'+
					'<input type="checkbox"/>'+
				'</td>'+
				'<td class="addInputStyle">'+
					'<input type="text"/>'+
				'</td>'+
				'<td class="addInputStyle">'+
					'<input type="text"/>'+
				'</td>'+
				'<td class="addInputStyle">'+
					'<select id=""  name = "userLevel"  class = "changeQuery userLevel">'+
						'<option>请选择</option>'+
					'</select>'+
				'</td>'+
			'</tr>'
				$(obj).parents(".contentBox").find(".visitLeader tr:last-child").after(html);
	roomDetailInfo.initSelectForVisitunitLevel();
}
//新增页面，主要参观领导的删除

/*调用选中的列表数据*/
function delLeader(obj){
	var checkedNumber = $(obj).parents(".contentBox").find("input[type=checkbox]:checked").length;
    if(checkedNumber == 0){
    	 layer.open({
    	        title:'提示信息',
    	        content:'请选中需要删除的数据',
    	        area:'300px',
    	        skin:'demo-class'
    	    })
    }else if(checkedNumber > 0){
    	layer.open({
            title:'提示信息',
            content:'确定要删除选中的行吗？',
            area:'300px',
            btn:['确定','取消'],
            skin:'demo-class',
            yes:function(index,layero){
                layer.close(index);
                $(obj).parents(".contentBox").find("input[type=checkbox]:checked").parent().parent("tr").remove();
            }
        });
    }
}
function messageSubmit(){
	layer.confirm('<table class="visitUnitAccompany tableStyle thTableStyle">'+
			'<tr>'+
				'<th>选择</th>'+
				'<th>审批人</th>'+
				'<th>审批部门</th>'+
			'</tr>'+
			'<tr>'+
				'<td>'+
					'<input type="checkbox" style="width:16px;"/>'+
				'</td>'+
				'<td class="addInputStyle">'+
					'王平'+
				'</td>'+
				'<td class="addInputStyle">'+
					'信息中心'+
				'</td>'+
			'</tr>'+
		
		'</table>',{title:'请选择审批人', skin:'demo-class'},function(index){


        })
}

//新增页面，各部门陪同人信息
function AccompanyLeader(obj){
 		/*
 			iframe:self 作用域：当前窗口   parent 作用域：父类窗口
 		*/
 		$("#stuffTree2").stuffTree({root:'41000001',empCode:'empCode2',empName:'empName2',iframe:'self',checkType:'checkbox',popEvent:'pop'});
 		 
}
 
