
var roomAddInfoCommon = {
		/**
		 * 获取表单填写的数据  如果传递的有e那么只取e的值
		 * @param e 经过jquery包装的元素值 <input id = "fieldName" value = "value" />
		 */
		getFormDataInfo:function(e){
			var formData = {};
			if(!e){
				e = $(".addInputStyle>input,.addInputStyle>select,.addInputStyle>textarea,.addInputStyle>*>input,.addInputStyle>*>select");
			}
			for(var a = 0;a<e.length;a++){
				var ele = $(e[a]);
				var eleValue = ele.val();
				var eleField = ele.attr("id");
				formData[eleField] = eleValue;
			}
			return formData;
		},
		bindChangeEvent:function(fn){
			$("input,select,textarea").change(fn);
		}
		
};
/**
 * form表单公共方法
 */
var dataForm = {
		/**
		 * 验证字段是否为空
		 * @param   e 可传 可不传 需要验证的字段（经过jquery包装的）
		 * @returns {Boolean}
		 */
		
		validNullable:function(e){
			 
			/* 如果传了e 那么只验证e，没传e 验证所有的 */
			if(!e){
				e = $(".validNull");
			}
			for(var a = 0;a<e.length;a++){
				var ele = $(e[a]);
				var eleTitle = ele.attr("content");
				var eleValue = ele.val();
				if(!eleValue){
					messager.tip(eleTitle+"字段不能为空",2000);
					ele.addClass("validRefuse");
					return false;
				}else{
					ele.removeClass("validRefuse");
				}
			}
			return true;
		},


		/**
		 * 验证字段是否为空，取消提示
		 * @param   e 可传 可不传 需要验证的字段（经过jquery包装的）
		 * @returns {Boolean}
		 */
		validNullableNoTip:function(e){
			/* 如果传了e 那么只验证e，没传e 验证所有的 */
			if(!e){
				e = $(".validNull");
			}
			for(var a = 0;a<e.length;a++){
				var ele = $(e[a]);
				var eleTitle = ele.parent().prev().text();
				var eleValue = ele.val();
				if(!eleValue){
					return false;
				}
			}
			return true;
		},
        /**
         * 验证输入数字是否为0-100或0-100保留两位小数的百分比
         */
        validPercent:function(e){
            if(!e){
                e = $(".validPer");
            }
            for(var a = 0;a<e.length;a++){
                var ele = $(e[a]);
                var eleTitle = ele.attr("content");
                var eleValue = ele.val();
                var reg = regStr.perNumStr;
                if(!reg.test(eleValue)){
                    messager.tip(eleTitle+"必须0-100之间的百分比,例如（50%，50.67%）",2000);
                    ele.addClass("validRefuse");
                    return  false;
                }else{

                    ele.removeClass("validRefuse");
                }
            }
            return true;
        },


		/**
		 * 验证输入数字是否为0-100或其他参数
		 * @param   e 可传 可不传 需要验证的字段（经过jquery包装的）
		 * @returns {Boolean}
		 */
		scoreSize:function(little,big,e){
			/* 如果传了e 那么只验证e，没传e 验证所有的 */
			if(!e){
				e = $(".validNull");
			}
			for(var a = 0;a<e.length;a++){
				var ele = $(e[a]);
				var eleTitle = ele.parent().prev().text();
				var eleValue = ele.val();
				if(eleValue > big || eleValue < little){
                    messager.tip("请输入"+little+"~"+big+"之间的分值，最多保留两位小数点",3000);
                    ele.addClass("validRefuse");
					return false;
				}else{
					ele.removeClass("validRefuse");
				}
			}
			return true;
		},
		
		/**
		 * 验证有没有超长 如果超长 变红提示
		 * @param e
		 */
		checkLength:function(e){
			if(!e){
				e = $(".addInputStyle>input, .addInputStyle>select, .addInputStyle>textarea");
			}
			for(var a = 0;a<e.length;a++){
				var ele = $(e[a]);
				var eleTitle = ele.attr("content");
				var eleValue = ele.val();
				var eLength = ele.attr("len");
				if(eLength&&eLength<eleValue.length){
					messager.tip(eleTitle+"字段长度不能超过"+eLength,2000);
					ele.addClass("validRefuse");
					return false;
				}else{
					ele.removeClass("validRefuse");
					 
				}
			}
			return true;
		},
    /**
     * 验证字段是否为正整数
     */
    validPosiviceNumber:function(e){
        if(!e){
            e = $(".posiviceNum");
        }
        for(var a = 0;a<e.length;a++){
            var ele = $(e[a]);
            var eleTitle = ele.attr("content");
            var eleValue = ele.val();
            var reg = regStr.numRegStr;
            if(!reg.test(eleValue)){
                messager.tip(eleTitle+"必须为正整数",2000);
                ele.addClass("validRefuse");
                return  false;
            }else{
                ele.removeClass("validRefuse");
            }
        }
        return true;
       },
		/**
		 * 验证字段是否为数字  小数  
		 * @param type	0代表可为零的正整数，1代表不可零的正整数，2代表小数 
		 * @param byte  小数点位数  可不传
		 * 
		 */
		validNumber:function(e,type,byte){
			if(!e){
				e = $(".validNum");
			}
			for(var a = 0;a<e.length;a++){
				var ele = $(e[a]);
				var eleTitle = ele.attr("content");
				var eleValue = ele.val();
				var reg = regStr.numRegStr;
				if(!reg.test(eleValue)){
					messager.tip(eleTitle+"必须为正整数",2000);
					ele.addClass("validRefuse");
					return  false;
				}else{
					ele.removeClass("validRefuse");
				}
				var eleClassName = ele.attr("class");
				if(!type){//没有传type  按照默认的验证
					if(eleClassName.indexOf("poInt")!=-1){//正整数
						reg = regStr.intRegStr;
						if(!reg.test(eleValue)){
							messager.tip(eleTitle+"必须为正整数",2000);
							ele.addClass("validRefuse");
							return  false;
						}else{
							ele.removeClass("validRefuse");
						}
					}else if(eleClassName.indexOf("float")!=-1){//正整数或者两位数字的小数
						reg = regStr.intOrFloatRegStr;
						if(!reg.test(eleValue)){
							messager.tip(eleTitle+"必须为正整数或者保留两位小数的小数",2000);
							ele.addClass("validRefuse");
							return  false;
						}else{
							ele.removeClass("validRefuse");
						}
					}
				}else{
					if(0==type){//正整数
						reg = regStr.intRegStr;
						if(!reg.test(eleValue)){
							messager.tip(eleTitle+"必须为正整数",2000);
							ele.addClass("validRefuse");
							return  false;
						}else{
							ele.removeClass("validRefuse");
						}
					}else if(1==type){//非零正整数
						reg = regStr.notZeroInt;
						if(!reg.test(eleValue)){
							messager.tip(eleTitle+"必须为非零正整数",2000);
							ele.addClass("validRefuse");
							return  false;
						}else{
							ele.removeClass("validRefuse");
						}
					}else if(2==type){//小数
						if(!byte){
							reg = regStr.intOrFloatRegStr;
							if(!reg.test(eleValue)){
								messager.tip(eleTitle+"必须为正整数或者保留两位小数的小数",2000);
								ele.addClass("validRefuse");
								return  false;
							}else{
								ele.removeClass("validRefuse");
							}
						}else{
							reg = new RegExp("^[0-9]+(\\.[0-9]{"+byte+"})$");      // /^[0-9]+(\.[0-9]{2})$/;
							if(!reg.test(eleValue)){
								messager.tip(eleTitle+"必须为正整数或者保留"+byte+"位小数的小数");
								ele.addClass("validRefuse");
								return  false;
							}else{
								ele.removeClass("validRefuse");
							}
						}
					}
				}
			}
			return true;
		},

    /**
     * 验证字段是否为数字 和两位 小数
     *
     */
    validNewNumber:function(e,type,byte){
        if(!e){
            e = $(".validNum");
        }
        for(var a = 0;a<e.length;a++){
            var ele = $(e[a]);
            var eleTitle = ele.attr("content");
            var eleValue = ele.val();
            var reg1 = regStr.notZeroInt;;
            var reg2 = regStr.intOrFloatRegStr;
            if(!reg1.test(eleValue)&&!reg2.test(eleValue)){
                messager.tip(eleTitle+"必须为正整数或者保留两位小数的小数",2000);
                ele.addClass("validRefuse");
                return  false;
            }else{
                ele.removeClass("validRefuse");
            }
        }
        return true;
    },

		initSelectEditor:function(dictList,jDom){
			var dictObj = JSON.parse(dictList);
			var optiontxt = ""
			for(var a = 0;a<dictObj.length;a++){
				var dictKey = dictObj[a].KEY;
				var dictValue = dictObj[a].VALUE;
				optiontxt+="<option value='"+dictKey+"'>"+dictValue+"</option>";
			}
			jDom.append(optiontxt);
		},
		
		
		
		initInputReadOnly:function(){
			$("input,textarea,select").attr("readonly",true);
			$("input,textarea,select").attr("rom",true);
			$(".Wdate").removeAttr("onclick").removeClass("Wdate");
			$(".detailBtn").hide();
		}	
}


