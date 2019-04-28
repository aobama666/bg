
var roomDetailInfo = {};
roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
var  VisitunitLevelData='';
$(function(){
	//‘新增’页面，院领导姓名多选下拉框
	roomDetailInfo.initSelectForLeader();
	//初始化人员选择树
	$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'self',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'}); 
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
	   debugger;
	   /* 主ID  */
	    var id=$("#id").val();
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
	    var visitinfo=[];
	    var  sortId=0;
		 $(".visitLeader tr:gt(0)").each(function(){
			 var visitId = $(this).find(".visitid").val()//姓名
			  //验证名称，职务，级别是否为空
			 var username = $(this).find(".visitUsername").val()//姓名
			 var position = $(this).find(".visitposition").val()//职务
			 //var userLevel = $(this).find(".userlevel").val()//级别
			 var userLevel =$(this).find("#userLevel").val();//级别
			 if(username == ''){
				 messager.tip("参观领导名称不能为空",2000);
				 $(this).find(".visitUsername").addClass("validRefuse");
				 roomDetailInfo.saveBtnClickFlag = 0;
				 return;
			 }else if(position == ''){
				 messager.tip("参观领导职务不能为空",2000);
				 $(this).find(".visitposition").addClass("validRefuse");
				 roomDetailInfo.saveBtnClickFlag = 0;
				 return;
			 }else if(userLevel == ''){
				 messager.tip("参观领导级别不能为空",2000)
				 $(this).find(".userlevel").addClass("validRefuse");
				 roomDetailInfo.saveBtnClickFlag = 0;
				 return;
			 }else{
				   $(this).find(".visitUsername").removeClass("validRefuse");
				   $(this).find(".visitposition").removeClass("validRefuse");
				   $(this).find(".userlevel").removeClass("validRefuse");
			 }
			//序号
			sortId++;
			var visit={"visitId":visitId,"userLevel":userLevel,"visitUserName":username,"visitPosition":position,"sortId":sortId}
		    visitinfo.push(visit);
		 });
		var companyUserInfo=[];
		//部门id的获取
		 $(".visitUnitAccompany tr:gt(0)").each(function(){
			 var userId = $(this).find(".userId").val()//用户id
			 var companyId = $(this).find(".companyId").val()//陪同主ID
			 var userInfo={"userId":userId,"companyId":companyId}
			 companyUserInfo.push(userInfo);
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
	  roomDetailFormData.companyUserInfo=companyUserInfo ;
	  roomDetailFormData.id=id;
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
			success: function (data) {
				 
				roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
				if(data.success=="ture"){
					alert("保存成功");
					roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
					var closeIndex = parent.layer.getFrameIndex(window.name);
					parent.layer.close(closeIndex);
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

roomDetailInfo.initSelectForLeader = function(){
	debugger;
	var pcode='visitunit_levle';
	/* start 查询数据字典集合  */
	$.ajax({
	    url: "/bg/IdeaInfo/selectForLeader" ,//获取申报界面数据字典
		type: "post",
		success: function (data) {
			if(data.success=="ture"){
				var leaderData = data.leaderData;
				var localData =  leaderData;
				$(".tree-data").combotree({
					data:localData,
					multiple:true
				});
				var companyLeaderName=$("#companyLeaderName").attr("data-companyLeaderName") ;
				if(companyLeaderName!=""){
					$(".tree-data").combotree(
							'setValue',companyLeaderName.split(",")
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
	var visitInfoData='';
	    visitInfoData+='<tr>' 
	    visitInfoData+='<td>' 
	    		visitInfoData+='<input type="checkbox"   id="visitId"  name = "visitId"  class="visitId"  value = ""  />' 
	    visitInfoData+='</td>'
	    	
	    visitInfoData+='<td  class="addInputStyle">' 
	    		visitInfoData+='<input type="text"    id="visitUserName"  name = "visitUserName"  class="visitUsername"  title="必填项 ,中文或英文 " />' 
	    visitInfoData+='</td>'
	    	
	    visitInfoData+='<td class="addInputStyle">' 
	    		visitInfoData+='<input type="text" id="visitPosition"  name = "visitPosition"  class="visitposition"  title="必填项,字段长度不能超过 150" />'
	    visitInfoData+='</td>'
	    visitInfoData+='<td class="addInputStyle">' 
	    	   visitInfoData+='<select name = "userLevel" id="userLevel"  class = "changeQuery userLevel"  title="必填项  "  >'
	    	   visitInfoData+=SelectForVisitunitLevel();
	    	   visitInfoData+='</select>'
	    visitInfoData+='</td>'	 	
	    visitInfoData+='</tr>'
	$(obj).parents(".contentBox").find(".visitLeader tr:last-child").after(visitInfoData);
}
//新增页面，主要参观领导级别的查询
function SelectForVisitunitLevel(){
	var pcode='visitunit_levle';
	var visitUnitLevel = '';
	var optionSelectd = $(".userLevel").attr("data-userLevel")
	$.ajax({
	    url: "/bg/DataDictionary/selectForDataDictionary?pcode="+pcode,//获取申报界面数据字典
		type: "post",
		dataType: "json",
		async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
		success: function (data) {
			if(data.success=="ture"){
				var dictData = data.dictData;
				visitUnitLevel += '<option value=""  >请选择参观领导级别</option>';
				for (var i = 0; i < dictData.length; i++) {
					if(dictData[i].K==optionSelectd){
						visitUnitLevel += '<option value="' + dictData[i].K + '"   selected >' + dictData[i].V + '</option>';
					}else{
						visitUnitLevel += '<option value="' + dictData[i].K + '">' + dictData[i].V + '</option>';
					}
					
				} 
			} else{
				layer.open({
		    	        title:'提示信息',
		    	        content:data.msg,
		    	        area:'300px',
		    	        skin:'demo-class'
		    	    }) 
			}
		}
	});
    return visitUnitLevel;
}
 

 

/*参观领导删除*/
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
            	delLeaderInfo(obj);  
            }
        });
    }
}
/*参观领导删除逻辑*/
function delLeaderInfo(obj){
	debugger;
	     $(obj).parents(".contentBox").find("input[class=visitid]:checked").each(function(){
         var  visitId= $(this).val();
         if(visitId==""){
        	 $(obj).parents(".contentBox").find("input[class=visitid]:checked").parent().parent("tr").remove();
         }else{
        	 $.ajax({
        		    url: "/bg/IdeaInfo/deleteVisitInfo?visitId="+visitId,//获取申报界面数据字典
        			type: "post",
        			dataType: "json",
        			async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        			success: function (data) {
        				if(data.success=="true"){
        					 $(obj).parents(".contentBox").find("input[class=visitid]:checked").parent().parent("tr").remove();
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
        
    });	
}

/*陪同人员删除*/
function delUser(obj){
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
                delUserInfo(obj);
                
                
            }
        });
    }
}
/*陪同人员信息删除逻辑*/
function delUserInfo(obj){
	debugger;
	 $(obj).parents(".contentBox").find("input[type=checkbox]:checked").each(function(){
		  var companyId=$(this).siblings(".companyId").val();
          if(companyId==""){
         	 $(obj).parents(".contentBox").find("input[type=checkbox]:checked").parent().parent("tr").remove();
          }else{
        	  $.ajax({
      		    url: "/bg/IdeaInfo/deleteCompanyUserInfo?companyId="+companyId,//获取申报界面数据字典
      			type: "post",
      			dataType: "json",
      			async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
      			success: function (data) {
      				if(data.success=="true"){
      					 $(obj).parents(".contentBox").find("input[type=checkbox]:checked").parent().parent("tr").remove();
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
     });
	
	
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
/* 各部门（单位）陪同人员信息 查询  */
function popEvent(ids,codes,names,userId){
	roomDetailInfo.SelectForUserId(userId);
}
roomDetailInfo.SelectForUserId = function(userId){
	$.ajax({
	    url: "/bg/IdeaInfo/selectForuserName?userId="+userId,//获取申报界面数据字典
		type: "post",
		success: function (data) {
			if(data.success=="ture"){
				var userData = data.userInfo;
				var userInfoData = '';
				for (var i = 0; i < userData.length; i++) {
					userInfoData+='<tr >' 
				    userInfoData+='<td>' 
				    	 userInfoData+='<input type="checkbox"   id = "userId"  name="userId" class="userId"   value="' + userData[i].userId + '" />' 
				    	 userInfoData+='<input type="hidden"   id = "companyId"  name="companyId" class="companyId" value=""/>' 
				    	 userInfoData+='</td>'
				    userInfoData+='<td  class="addInputStyle">' 
						  userInfoData+='<input type="text" disabled    id="UserName" name="UserName"  class="UserName"  value="' + userData[i].userAlias + '" />' 
				    userInfoData+='</td>'
				    userInfoData+='<td class="addInputStyle">' 
						  userInfoData+='<input type="text" disabled    id="Position" name="Position" class="Position"   value="' + userData[i].postName + '" />'
					 userInfoData+='</td>'
				    userInfoData+='</tr>' 	
				}
				$(".model_tr_userInfo").remove();
				$(".visitUnitAccompany tr:last-child").after(userInfoData);
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
