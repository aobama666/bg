
var roomDetailInfo = {};
roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
var  VisitunitLevelData='';
$(function(){
	//‘新增’页面，院领导姓名多选下拉框
	roomDetailInfo.initSelectForLeader();
	//初始化人员选择树
	$("#stuffTree").stuffTree({bindLayId:'popStuffTree',root:'41000001',iframe:'parent',empCode:'empCode',empName:'empName',checkType:'checkbox',popEvent:'pop'}); 
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
        
        var  stateday=new Date(stateDate).getDay();
        var  endday=new Date(endDate).getDay();
        //参观开始时间和当前时间比较  
        if(sd.getTime()<ld.getTime()){
        	$("#stateDate").addClass("validRefuse");
            messager.tip("参观开始时间不能早于当前时间！",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return  false;
        }else if(sd.getTime()>ed.getTime()){
        	$("#endDate").addClass("validRefuse");
            messager.tip("参观结束时间不能早于参观开始时间！",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return false;
        }else if(stateday=="1"){
  		   $("#stateDate").addClass("validRefuse");
		    messager.tip("检查参观开始时间,每周一常规检修,不接受预定！",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return  false;
	    }else if(endday=="1"){
 		   $("#endDate").addClass("validRefuse");
		    messager.tip("检查参观结束时间,每周一常规检修,不接受预定！",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return  false;
	    }else{
        	 $("#stateDate").removeClass("validRefuse");
        	 $("#endDate").removeClass("validRefuse");
        	 return true;
        } 
           
}
//
function checkLeaderInfo(){
	debugger;
	var flag=false;
	 
	 $(".visitLeader tr:gt(0)").each(function(){
		 var visitId = $(this).find(".visitid").val()//姓名
		  //验证名称，职务，级别是否为空
		 var username = $(this).find(".visitUsername").val()//姓名
		 var position = $(this).find(".visitposition").val()//职务
		 var userLevel =$(this).find("#userLevel").val();//级别
		 
		 if(username == ''){
			 messager.tip("参观领导名称不能为空",2000);
			 $(this).find(".visitUsername").addClass("validRefuse");
			 roomDetailInfo.saveBtnClickFlag = 0;
			 flag=false;
			 return  false;
		 }else{
			 var  checkVisitUser=IsRight.onlyTwo("#visitUserName");
		     if(!checkVisitUser){
				 messager.tip("参观领导名称只能含有中文或者英文",2000);
				 $(this).find(".visitUsername").addClass("validRefuse");
				 roomDetailInfo.saveBtnClickFlag = 0;
				 flag=false;
				 return false;	 
		     }else{
		    	 $(this).find(".visitUsername").removeClass("validRefuse");
		     }
			 if(username.length>20){
				 messager.tip("参观领导名称不超过20个字",2000);
				 $(this).find(".visitUsername").addClass("validRefuse");
				 roomDetailInfo.saveBtnClickFlag = 0;
				 flag=false;
				 return  false;
			 }else{
				 $(this).find(".visitUsername").removeClass("validRefuse");
			 }
		 }
	
		 if(position == ''){
			 messager.tip("参观领导职务不能为空",2000);
			 $(this).find(".visitposition").addClass("validRefuse");
			 roomDetailInfo.saveBtnClickFlag = 0;
			 flag=false;
			 return  false;
		 }else{
			 if(position.length>50){
				 messager.tip("参观领导职务不超过50个字",2000);
				 $(this).find(".visitposition").addClass("validRefuse");
				 roomDetailInfo.saveBtnClickFlag = 0;
				 flag=false;
				 return  false;
			 }else{
				 $(this).find(".visitposition").removeClass("validRefuse");
			 }
		 }	 
		 
	     if(userLevel == ''){
			 messager.tip("参观领导级别不能为空",2000)
			 $(this).find(".userlevel").addClass("validRefuse");
			 roomDetailInfo.saveBtnClickFlag = 0;
			 flag=false;
			 return  false;
		 }else{
			 $(this).find(".userlevel").removeClass("validRefuse");
		 } 
	     flag=true;
	     return flag;
	 })
	 return  flag;
}
function  accompanyUserInfo(){
	var accompanyUser="";
	$(".visitUnitAccompany tr:gt(0)").each(function(){
		 var userId = $(this).find(".userId").val()//用户id
		 if(userId=="on"){
			 messager.tip("参观领导级别/职务不能为空",2000)
			 $(this).find("#UserName").addClass("validRefuse");
			 $(this).find("#Position").addClass("validRefuse");
			 roomDetailInfo.saveBtnClickFlag = 0;
			 accompanyUser="0"
		 }else{
			 $(this).find("#UserName").removeClass("validRefuse");
			 $(this).find("#Position").removeClass("validRefuse");
			 accompanyUser="1"
		    
		 }
	 })
	 if(accompanyUser =="0"){
		 return false;
		
	 }else{
		 return  true;
	 }
}

//返回
roomDetailInfo.messageResign =function(){
	roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
	var closeIndex = parent.layer.getFrameIndex(window.name);
	parent.layer.close(closeIndex);
}
/* 保存信息库信息 */
roomDetailInfo.messageSave= function(approvalUserd){
	   debugger;
	   /* 主ID  */
	    var id=$("#id").val();
	    var approveId=$("#wlApproveId").val();
	    var approveState=$("#approveState").val();
	    approveState
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
			messager.tip("11位数字手机号码或固定电话，固定电话格式为：xxx-xxxxxxxx或xxxx-xxxxxxx",2000);
			roomDetailInfo.saveBtnClickFlag = 0;
			return;
		}  
		var stateDate =$("#stateDate").val();
	    var endDate = $("#endDate").val() ; 
	    //验证参观开始时间和参观结束时间的
	    var checkDates =  checkDate(stateDate,endDate);
	    if(!checkDates){
	    	return;
	    } 
	    //验证主要参观领导
	    var visitinfo=[];
	    var  sortId=0;
	    var visitleaderlen =$(".visitLeader tr").length;
	    if(visitleaderlen==1){
	    	 messager.tip("主要参观领导人信息不能为空",2000)
			 roomDetailInfo.saveBtnClickFlag = 0;
			 return;
	    }else{
	    	var checkLeader=checkLeaderInfo();
	 	    if(!checkLeader){
	 	    	return;
	 	    }else{
	 	    	  $(".visitLeader tr:gt(0)").each(function(){
	 				 var visitId = $(this).find(".visitid").val()//姓名
	 				  //验证名称，职务，级别是否为空
	 				 var username = $(this).find(".visitUsername").val()//姓名
	 				 var position = $(this).find(".visitposition").val()//职务
	 				 var userLevel =$(this).find("#userLevel").val();//级别
	 				//序号
	 				sortId++;
	 				var visit={"visitId":visitId,"userLevel":userLevel,"visitUserName":username,"visitPosition":position,"sortId":sortId}
	 			    visitinfo.push(visit);
	 			 });
	 	    }
	 	  
	    } 
	   
	    
	    // 院内陪同领导人员信息
	    var companyLeaderName=$("#companyLeaderName").val();
	    if(companyLeaderName==""){
	    	 messager.tip("院内陪同领导姓名不能为空",2000)
	    	 $(".textbox").addClass("validRefuse");
			 roomDetailInfo.saveBtnClickFlag = 0;
			 return;
	    }else{
	    	 $(".textbox").removeClass("validRefuses");
	    }
	    
	    
	    
	    var companyUserInfo=[];
		//陪同部门人员信息的获取
	    var unitAccompanylen =$(".visitUnitAccompany tr").length;
	    if(unitAccompanylen==1){
	    	 messager.tip("陪同部门人员信息不能为空",2000)
			 roomDetailInfo.saveBtnClickFlag = 0;
			 return;
	    }else{
	    	 var checkAccompanyUser=accompanyUserInfo();
	    	 if(!checkAccompanyUser){
	    		 return;
	    	 }else{
	    		 $(".visitUnitAccompany tr:gt(0)").each(function(){
	    			 var userId = $(this).find(".userId").val()//用户id
	    			 var companyId = $(this).find(".companyId").val()//陪同主ID
	    			 var userInfo={"userId":userId,"companyId":companyId}
	    			 companyUserInfo.push(userInfo);
	    		 });
	    	 }
	    }
	   
      var roomDetailFormData = roomAddInfoCommon.getFormDataInfo();
	  roomDetailFormData.companyUserInfo=companyUserInfo ;
	  roomDetailFormData.id=id;
	 //验证参观单位性质
	 var visitUnitType=$("#visitUnitType option:selected");
	  roomDetailFormData.visitUnitType=visitUnitType.val();
	  var msginfo="";
	  if(approvalUserd =='' || approvalUserd ==undefined){
		  roomDetailFormData.visitLevel="save";  
		  roomDetailFormData.approvalUserd="";
		  msginfo="save";
	  }else{
		  roomDetailFormData.visitLevel="submit";
		  roomDetailFormData.approvalUserd=approvalUserd;
		  msginfo="submit";
	  }
	  var remark= $('#remark').val();
	  
	  if(remark!=""){
		  if(remark.length>200){
			  $("#remark").addClass("validRefuse");
			  messager.tip("备注不超过200个字",2000)
			  roomDetailInfo.saveBtnClickFlag = 0;
			  return;
		  }else{
			  $("#remark").removeClass("validRefuses");
		  }
		
	  } 
	  roomDetailFormData.remark=remark;
	  roomDetailFormData.approveId=approveId;
	  roomDetailFormData.approveState= approveState;
	  roomDetailFormData.visitinfo=visitinfo;
	 /* 保存方法 */
	  if(msginfo=="save"){
		  $.messager.confirm( "保存提示", "确认保存该数据吗",
					function(r){
						if(r){
							$.ajax({
							    url: "/bg/IdeaInfo/addIdeaInfo",
								type: "post",
								dataType:"json",
								contentType: 'application/json',
								data: JSON.stringify(roomDetailFormData),
								success: function (data) {
									roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
									if(data.success=="true"){
										 messager.tip("保存成功",1000);
										 roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
										 window.parent.location.reload();
										 var closeIndex = parent.layer.getFrameIndex(window.name);
										 parent.layer.close(closeIndex);
									}else{ 
										 messager.tip(data.msg,5000);
										 return;  
									}
									
									 
								}
							}); 
						}
					}
				);
		  
		  
		  
	  }else if(msginfo=="submit"){
		  $.messager.confirm( "提交提示", "确认提交该数据吗",
		  function(r){
				if(r){
					$.ajax({
					    url: "/bg/IdeaInfo/addIdeaInfo",
						type: "post",
						dataType:"json",
						contentType: 'application/json',
						data: JSON.stringify(roomDetailFormData),
						success: function (data) {
							roomDetailInfo.saveBtnClickFlag = 0;//保存按钮点击事件
							if(data.success=="true"){
							    messager.tip("提交成功",1000);
							    roomDetailInfo.saveInfoFlag = true;//页面数据保存事件
							    window.parent.location.reload();
								var closeIndex = parent.layer.getFrameIndex(window.name);
								parent.layer.close(closeIndex);
								
							}else{ 
								messager.tip(data.msg,5000);
								return;  
							}
							
						}
					}); 
				}
			}
		);
		  
		  
		  
	  }
	 
	  
	  
	  
	
}
function approveUserID(){
	 
	var ApproveUserId = "";
	var approveState="SAVE"
	$.ajax({
	    url: "/bg/Privilege/getApproveUserByUserName?approveState="+approveState+"&type="+"submit",//获取申报界面数据字典
		type: "post",
		dataType: "json",
		async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
		success: function (data) { 
			if(data.success =='true'){
			  	var userPrivilegelist = data.data.userPrivilege;
			  	var len=userPrivilegelist.length
			  	if(len>1){
			  		ApproveUserId="";
			  	}else{
			  		ApproveUserId=userPrivilegelist[0].userId;
			  	}
			} 
		 }
	  });
	return ApproveUserId;
	
}
/* 提交信息库信息 */
roomDetailInfo.messageSubmit= function(){
	var html=messageSubmitHtml();
	if(html =='' || html ==undefined){
	    messager.tip("审批人查询失败",1000);
	    return;
	}else{
		var ApproveUserId=approveUserID();
		if(ApproveUserId!=""){
			  roomDetailInfo.messageSave(ApproveUserId);
		      layer.close(layer.index);
		  }else{
		      layer.confirm(
					html,
					{title:'请选择审批人', area:'800px',skin:'demo-class'   },
					function(r){
						if(r){
							
							var checkedNumber = $(".userPrivilege").find("input[type=checkbox]:checked").length;
							var userId=$(".userPrivilege").find("input[type=checkbox]:checked").siblings(".userId").val();
							if(checkedNumber == 0){
							    messager.tip("请选择要操作的数据",1000);
							    return;
							}else if(checkedNumber > 1 ){
								messager.tip("请选择一条数据",1000);
								return;  
							}else{
								roomDetailInfo.messageSave(userId);
						    }
							
						}
						
				     }); 
		    }
		
		
	    }

}

/* 提交信息库信息---页面拼接 */


function messageSubmitHtml(){
	var approveState="SAVE"
	var userPrivilegehtml = '';
	$.ajax({
	    url: "/bg/Privilege/getApproveUserByUserName?approveState="+approveState+"&type="+"submit",//获取申报界面数据字典
		type: "post",
		dataType: "json",
		async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
		success: function (data) {
			 
			if(data.success =='true'){
		    	var userPrivilegelist = data.data.userPrivilege;
				userPrivilegehtml += '<table class="userPrivilege tableStyle thTableStyle">';
				userPrivilegehtml += '<tr>';
				     userPrivilegehtml += '<th>选择</th>';
				     userPrivilegehtml += '<th>审批人</th>';
				     userPrivilegehtml += '<th>部门</th>';
				     
				userPrivilegehtml += '</tr>';
					for (var i = 0; i < userPrivilegelist.length; i++) {
						userPrivilegehtml += '<tr>';
						     userPrivilegehtml += '<td>';
						       userPrivilegehtml+='<input type="checkbox"   class="inputUserId"  />' 
						       userPrivilegehtml+='<input type="hidden"    id="userId"  name = "userId"  class="userId"  value="' + userPrivilegelist[i].userId + '"  />' 
						     userPrivilegehtml += '</td>';
						     userPrivilegehtml += '<td class="addInputStyle">  ';
						       userPrivilegehtml+='<input type="text" disabled  id="userAlias"  name = "userAlias"  class="userAlias inputChange"  value="' + userPrivilegelist[i].userAlias + '" title="审批人名称 " />' 
						     userPrivilegehtml += '</td>';
						     userPrivilegehtml += '<td class="addInputStyle">';
						       userPrivilegehtml+='<input type="text" disabled   id="deptName"   name = "deptName"   class="deptName inputChange"  value="' + userPrivilegelist[i].deptName + '" title="审批人单位" />'
						     userPrivilegehtml += '</td>';
						       
						 userPrivilegehtml += '</tr>';      
					}
				userPrivilegehtml += '</table>';
				 
				 
			}else{
				
				userPrivilegehtml ;
			}
		 
		}
	});
	return userPrivilegehtml;
}






roomDetailInfo.initSelectForLeader = function(){
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
	    		visitInfoData+='<input type="text"    id="visitUserName"  name = "visitUserName"  class="visitUsername"  title="必填项 ,中文或英文 ,字段长度不能超过 20个字" />' 
	    visitInfoData+='</td>'
	    	
	    visitInfoData+='<td class="addInputStyle">' 
	    		visitInfoData+='<input type="text" id="visitPosition"  name = "visitPosition"  class="visitposition"  title="必填项,字段长度不能超过 50个字" />'
	    visitInfoData+='</td>'
	    visitInfoData+='<td class="addInputStyle">' 
	    	   visitInfoData+='<select name = "userLevel" id="userLevel"  class = "changeQuery userlevel"  title="必填项  "  >'
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
		messager.tip("请选中需要删除的数据",2000);
		return;
    }
	else if(checkedNumber>1){
    	messager.tip("每次只能删除一条数据",2000);
    	return;
    }
	else if(checkedNumber > 0){
    	 $.messager.confirm( "删除提示", "确认删除选中数据吗",
    	 function(r){
    		if(r){
    			delLeaderInfo(obj);  
    			}
    		    layer.close(index);
			});
    }
}
/*参观领导删除逻辑*/
function delLeaderInfo(obj){
	 
	     $(obj).parents(".contentBox").find("input[type=checkbox]:checked").each(function(){
         var  visitId= $(this).val();
         if(visitId==""){
        	 $(obj).parents(".contentBox").find("input[type=checkbox]:checked").parent().parent("tr").remove();
        	 messager.tip("删除成功",1000);
         	 return;
         }else{
        	 $.ajax({
        		    url: "/bg/IdeaInfo/deleteVisitInfo?visitId="+visitId,//获取申报界面数据字典
        			type: "post",
        			dataType: "json",
        			async : false,   //要想获取ajax返回的值,async属性必须设置成同步，否则获取不到返回值
        			success: function (data) {
        				if(data.success=="true"){
        					 $(obj).parents(".contentBox").find("input[type=checkbox]:checked").parent().parent("tr").remove();
        					 messager.tip("删除成功",1000);
        		         	 return;
        				}else{
        					 messager.tip("删除失败",1000);
        		         	 return;
        					
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
		messager.tip("请选中需要删除的数据",2000);
		return;
    }else if(checkedNumber>1){
    	messager.tip("每次只能删除一条数据",2000);
    	return;
    }else if(checkedNumber > 0){
    	 $.messager.confirm( "删除提示", "确认删除选中数据吗",
    	   function(r){
    	     if(r){
    	        delUserInfo(obj);  
    	     }
    	     layer.close(index);
    	  });
    }
}
/*陪同人员信息删除逻辑*/
function delUserInfo(obj){
	 
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
      					 messager.tip("删除成功",1000);
    		         	 return;
      				}else{
      					messager.tip("删除失败",1000);
   		         	    return;
      					
      				} 
      			}
      		});
         	
          }
     });
	
	
}


/* 各部门（单位）陪同人员信息 查询  */
function popEvent(ids,codes,names,userId){
	//roomDetailInfo.SelectForUserId(userId);
	roomDetailInfo.SelectForUserId(codes);
}
roomDetailInfo.SelectForUserId = function(codes){
	 
	$.ajax({
	   // url: "/bg/IdeaInfo/selectForuserName?userId="+userId,//获取申报界面数据字典
		url: "/bg/IdeaInfo/getUserCode?codes="+codes,//获取申报界面数据字典
		type: "post",
		
		success: function (data) {
			if(data.success){
				var userData = data.userInfo;
				var userInfoData = '';
				for (var i = 0; i < userData.length; i++) {
					userInfoData+='<tr >' 
					    userInfoData+='<td>' 
					    	 userInfoData+='<input type="checkbox"   id = "userId"  name="userId" class="userId"   value="' + userData[i].userId + '" />' 
					    	 userInfoData+='<input type="hidden"   id = "companyId"  name="companyId" class="companyId" value=""/>' 
					    	 userInfoData+='</td>'
					    userInfoData+='<td  class="addInputStyle">' 
							   //userInfoData+='<input type="text" disabled    id="UserName" name="UserName"  class="UserName"  value="' + userData[i].userAlias + '" />' 
							   userInfoData+='<span class="detailsLeft">'+userData[i].userAlias+'</span>';
					    userInfoData+='</td>'
					    userInfoData+='<td class="addInputStyle">' 
							  //userInfoData+='<input type="text" disabled    id="Position" name="Position" class="Position"   value="' + userData[i].postName + '" />'
					    	  userInfoData+='<span class="detailsLeft">'+userData[i].postName+'</span>';
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
