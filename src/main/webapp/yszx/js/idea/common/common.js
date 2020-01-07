/**
 * Created by win10 on 2017/11/16.
 */
/**
 * Created by hanxifa05 on 2017-04-10.
 */
var IsRight={

	//电话验证
	telePhone:function(ele){
		var val=IsRight.trim($(ele).val());
		var exp=/(^1\d{10}$)|(^\d{3}-\d{8}$)|(^\d{4}-\d{7}$)/;

		if(exp.test(val)){
			$(ele).removeClass("redColor");
			$(ele).next("span").html("√");
		}else{
			$(ele).addClass("redColor");
			$(ele).next("span").html("*手机号或者xxx-xxxxxxxx或者xxxx-xxxxxxx");
		}
	},
	//
     trim:function(ele){
		        return ele.replace(/(^\s*)|(\s*$)/g,"");
		    },
    //不能为空
   checkLength:function(ele,len){
	  
		var val = IsRight.trim($(ele).val());
		if (len>val.length) {
			 $(ele).removeClass("validRefuse");
	         return true;
		 } else {
			 $(ele).addClass("validRefuse");
	         return false;
		 }
	},  
	//不能为空
	notNull:function(ele){
		var val = IsRight.trim($(ele).val());
		if (!val=="") {
			 $(ele).removeClass("validRefuse");
	         return true;
		 } else {
			 $(ele).addClass("validRefuse");
	         return false;
		 }
	},  
    //电话验证
    telePhone:function(ele){
    	 
        var val=IsRight.trim($(ele).val());
        var exp=/(^1\d{10}$)|(^\d{3}-\d{8}$)|(^\d{4}-\d{7}$)/;
        if(exp.test(val)){
            $(ele).removeClass("validRefuse");
            return true;
        }else{
        	$(ele).addClass("validRefuse");
            return false;
        }
    },
    
    //只能含有中文或者英文
    onlyTwo:function(ele){
    	 
        var val=IsRight.trim($(ele).val());
        var exp1=/^[\u4e00-\u9fa5a-zA-Z]{1,50}$/;
        if(exp1.test(val)){
            $(ele).removeClass("validRefuse");
            return true;
        }else{
        	$(ele).addClass("validRefuse");
            return false;
        }
    },
    
    
    //只能含有中文、中划线、下划线、数字、字母且不能以中划线或下划线开头或结尾
    onlyMore:function(ele){
        var val=IsRight.trim($(ele).val());
        var exp1=/^[[\u4e00-\u9fa5\-_0-9a-zA-Z]+$/;
        var exp=/^(?!_)(?!.*?_$)[\u4e00-\u9fa5\\-_a-zA-Z0-9]+$/;

        if(exp1.test(val)){
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        }else{
            $(ele).addClass("redColor");
            $(ele).next("span").html("*只能含有中文、中划线、下划线、数字、字母且不能以中划线或下划线开头或结尾");
        }
    },
   
  
    //只能是中文
    onlyChinese:function(ele) {
        var val = IsRight.trim($(ele).val());
        var exp1 = /^[\u4e00-\u9fa5]+$/;

        if (exp1.test(val)) {
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        } else {
            $(ele).addClass("redColor");
            $(ele).next("span").html("*必须为中文");
        }
    },
    //只能是英文或数字
    onlyEnglishNum:function(ele){
        var val = IsRight.trim($(ele).val());
        var exp1 = /^[a-zA-Z0-9]+$/;

        if (exp1.test(val)) {
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        } else {
            $(ele).addClass("redColor");
            $(ele).next("span").html("*只能为数字或字母");
        }
    },
    //只能是数字
    onlyNum:function(ele){
        var val = IsRight.trim($(ele).val());
        var exp1 = /^[0-9]+$/;

        if (exp1.test(val)) {
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        } else {
            $(ele).addClass("redColor");
            $(ele).next("span").html("*必须为数字");
        }
    }, //只能是1位数字
    onlyOneNum:function(ele){
        var val = IsRight.trim($(ele).val());
        var exp1 = /^[0-9]+$/;

        if (exp1.test(val)) {
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        } else {
            $(ele).addClass("redColor");
            $(ele).next("span").html("*必须为一位数字");
        }
    },
    //只能是三位数字
    onlyThreeNum:function(ele){
        var val = IsRight.trim($(ele).val());
        var exp1 = /^[0-9]{3}$/;

        if (exp1.test(val)) {
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        } else {
            $(ele).addClass("redColor");
            $(ele).next("span").html("*必须是3位数字");
        }
    },
    //只能是小数，保留两位小数
    onlyThreeNum:function(ele){
        var val = IsRight.trim($(ele).val());
        var exp1 = /^[0-9]{3}$/;

        if (exp1.test(val)) {
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        } else {
            $(ele).addClass("redColor");
            $(ele).next("span").html("*必须是3位数字");
        }
    },



    notSpecial:function(ele){
        var val = IsRight.trim($(ele).val());
        var exp1 = /^[\u4e00-\u9fa50-9a-zA-Z_]+$/;

        if (exp1.test(val)) {
            $(ele).removeClass("redColor");
            $(ele).next("span").html("√");
        } else {
            $(ele).addClass("redColor");
            $(ele).next("span").html("*不能含有特殊符号");
        }
    }
};
var messager={
    tip:function(text,time){
    	if($(".myTips").size()){
    		return false;
    	}
        var html="<div class='myTips' style='border-radius:5px;opacity:0.8;color:#fff;font-size:14px;padding:8px 15px;background:#000;z-index:99999999;display: inline-block;position: fixed;top:49%;left:47%;'>"+text+"</div>";
        $("body").append(html);
        $('.myTips').css({"left":($(".myTips").parent().outerWidth(true)-$(".myTips").width())/2});
        var outTime = 1000;
        if(time){
        	outTime = time;
        }
        var times=setTimeout(function(){
            $(".myTips").remove();
        },outTime);
    },
    loading:function(tips,eleme){
    	$("<div class='datagrid-mask'></div>").css({display:'block',width:'100%',height:$(eleme).parent().height()+10}).appendTo(eleme);
    	$("<div class='datagrid-mask-msg'></div>").html(tips).appendTo(eleme).css({display:"block",height:"40px",left:($(eleme).parent().outerWidth(true)-110)/2,top:($(eleme).parent().height()-25)/2});
    },
    disLoad:function(){
    	$(".datagrid-mask").remove();
    	$(".datagrid-mask-msg").remove();
    },
    loadings:function(tips,eleme){
    	$("<div class='datagrid-mask'></div>").css({display:'block',width:'100%',height:$(eleme).parent().height()}).appendTo(eleme).parent().css({position:"relative"});
    	$("<div class='datagrid-mask-msg'></div>").html(tips).appendTo(eleme).css({display:"block",height:"40px",left:($(eleme).parent().outerWidth(true)-110)/2,top:($(eleme).parent().height()-25)/2});
    },
    disLoads:function(){
    	$(".datagrid-mask").remove();
    	$(".datagrid-mask-msg").remove();
    },
    getQueryString:function(name){
    	var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)","i");
    	var r=window.location.search.substr(1).match(reg);
    	if(r !=null){
    		return unescape(r[2]);
    	}else{
    		return null;
    	}
    }
};

var dataGrid = {
	getCheckedItems:function(dataItemsParams,checkName){
		if(!checkName){
			checkName = "oneCheck";
		}
		var checkedItems = new Array();
		var checkedBox = $("input:checkbox[name="+checkName+"]:checked");//选中的行
		for(var a = 0;a<checkedBox.length;a++){
			var index =  checkedBox.eq(a).attr("index");
			checkedItems.push(dataItemsParams[index]);//将选中的行数据放入新的array中
		}
		return checkedItems;
	},
	getCheckedIds:function(checkName){
		if(!checkName) {
			checkName = "oneCheck";
		}
		var checkedIds = new Array();
		var checkedBox = $("input:checkbox[name="+checkName+"]:checked");//选中的行
		for(var a = 0;a<checkedBox.length;a++){
			var resultId =  checkedBox.eq(a).val();
			checkedIds.push(resultId);//将选中的行id放入新的array中
		}
		return checkedIds;
	},
	getSelectionItems:function(e){//e代表编辑框所在的html
		var checkedItems = new Array();
		var divHtml = $(e).parent();//input上层 div
		var tdHtml = divHtml.parent();
		var trHtml = tdHtml.parent();
		var checkBox = trHtml.find("td").eq(1).find("input");//找到复选框
		var checkBoxIndex = checkBox.attr("index");//获取复选框中的index
		var selectionData = dataItems[checkBoxIndex];//获取该复选框代表额一行的数据
		checkedItems.push(selectionData);
		return checkedItems;
	},
	getSelectionIndex:function(e){
		var divHtml = $(e).parent();//input上层 div
		var tdHtml = divHtml.parent();
		var trHtml = tdHtml.parent();
		var checkBox = trHtml.find("td").eq(1).find("input");//找到复选框
		var checkBoxIndex = checkBox.attr("index");//获取复选框中的index
		return checkBoxIndex;
	},
	initCheckBox:function(checkBoxClassName,checkBoxName){
		if(!checkBoxClassName){
			checkBoxClassName = "check_";
		}
		if(!checkBoxName){
			checkBoxName = "oneCheck";
		}
		/*  start 全选、取消全选 */
		$("."+checkBoxClassName).change(function(){
			if(this.checked==true){
				var checkBoxs=$("input:checkbox[name="+checkBoxName+"]");
				checkBoxs.each(function(i){
					checkBoxs[i].checked=true;
				});
			}else{
				var checkBoxs=$("input:checkbox[name="+checkBoxName+"]");
				checkBoxs.each(function(i){
					checkBoxs[i].checked=false;
				});
			}
		});
		/*  end 全选、取消全选 */
	}
};

/**
 * 常用正则表达式
 */
var regStr = {
		numRegStr:/^[0-9]*$/,//数字
		numByteRegStr:/^\d{2}$/,//验证n位的数字
		minNumByteRegStr:/^\d{2,}$/ ,//至少n位数字
		intRegStr:/^\d+$/,//正整数
       // perNumStr: /^(\d|[1-9]\d|100)(\.\d{1,2})?%$/,
	    perNumStr: /^(((\d{1,2})[.]((\d{1,2})?))|100|(?:0|[1-9][0-9]?)|100.00|100.0)?%$/,
		towByteFloatRegStr:/^[0-9]\.[0-9][0-9]$/,//两位小数的小数
		intOrFloatRegStr:/^[0-9]+(\.[0-9]{2})$/, //正整数或者保留两位小数的小数
		notZeroInt:/^\+?[1-9][0-9]*$/ , //非零正整数
		number: /^(\-|\+)?\d+(\.\d+)?$/,//整数、小数、负数
		email:/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/,//邮箱
		phone:/^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$/,//电话
		telPhone:/^[1][3|4|5|7|8][0-9]{9}$/ //手机号

}


 
 
 
var dictFunc = {
		/**
		 * 初始化年度下拉框
		 */
		initDate:function(){
			var currentYear = $("#currentYear").val();//获取当前年份
			var yearDict = $("#yearDict").val();//获取年份下拉选择数组
			var yeararry = JSON.parse(yearDict);
			var optiontxt = "";
			for(var i=0;i<yeararry.length;i++){
				if(currentYear==yeararry[i]){
					optiontxt+="<option value='"+yeararry[i]+"' selected>"+yeararry[i]+"</option>";
				}else{
					optiontxt+="<option value='"+yeararry[i]+"'>"+yeararry[i]+"</option>";
				}
			}
			var techProgress_yearobj = $("#year");//select对象
			techProgress_yearobj.append(optiontxt);
		},
		/**
		 * 初始化下拉框中的数据
		 * @param dictJSONobj 后台传来的经过jsonobj.tojsonString()方法格式化的字符串
		 * @param jDom		  需要初始化的select 经过jquery包装的元素
		 */
		initSelectDict:function(dictListVal,jDom){
			var dictObj = JSON.parse(dictListVal);
			var optiontxt = "<option value = ''>全部</option>"
			for(var a = 0;a<dictObj.length;a++){
				var dictKey = dictObj[a].KEY;
				var dictValue = dictObj[a].VALUE;
				optiontxt+="<option value='"+dictKey+"'>"+dictValue+"</option>";
			}
			jDom.append(optiontxt);
		}
};

var file = {
		/**
		 * ky_common附件上传(这个方法是形审和奖励推荐专用方法  公共方法请调用以下方法)
		 * @param fileData  fileData.busId = $("#OPTION_ID").val();//主键
							fileData.code = ["10008002"];
							fileData.pCode = "10008";
							fileData.busTable = "ky_repawa_techProgess_check";
							fileData.busModule = "reward";
		 * @param callBackFunc  回调函数
		 */
		toUploadFile:function(fileData,callBackFunc){
			fileData = JSON.stringify(fileData);
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:42px;">附件上传</h4>',
				area:['650px','400px'],
				fixed:false,//不固定
				maxmin:true,
				content:encodeURI('/newtygl/recommoned/fileUpload?fileData='+fileData),
				end:callBackFunc
			});
		},
		/**
		 * sys_common附件上传 b_common_file
		 * @param fileData
		 * 	fileData.busId = $("#OPTION_ID").val();//主键
			fileData.code = ["10008002"];
			fileData.pCode = "10008";
			fileData.busTable = "KY_REPAWA_AWARD_INFO";
			fileData.colName = "FILE_NAME";
			fileData.validExt = true;  //是否要验证附件类型  默认为false不验证，如果要验证附件类型传值为true;fileData.validExtValue必须传
			fileData.validExtValue = ".docx"
		 * @param callBackFunc     
		 */
		toSysUploadFile:function(fileData,callBackFunc){
			fileData = JSON.stringify(fileData);
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:42px;">附件上传</h4>',
				area:['650px','400px'],
				fixed:false,//不固定
				maxmin:true,
				content:encodeURI('/newtygl/kyOrSysFileUpload/sys/toSysFileUploadView?fileData='+fileData),
				end:callBackFunc
			});
		},
		/**
		 * ky_common附件上传公共方法 ky_common_file ky_common_doc
		 * @param fileData
		 * 	fileData.busId = $("#OPTION_ID").val();//主键
			fileData.code = ["10008002"];
			fileData.pCode = "10008";
			fileData.busTable = "ky_repawa_techProgess_check";
			fileData.busModule = "reward";
			fileData.validExt = true;  //是否要验证附件类型  默认为false不验证，如果要验证附件类型传值为true;fileData.validExtValue必须传
			fileData.validExtValue = ".docx,.xls,.doc"
		 * @param callBackFunc
		 */
		toKyUploadFile:function(fileData,callBackFunc){
			fileData = JSON.stringify(fileData);
			layer.open({
				type:2,
				title:'<h4 style="height:42px;line-height:42px;">附件上传</h4>',
				area:['650px','400px'],
				fixed:false,//不固定
				maxmin:true,
				content:encodeURI('/newtygl/kyOrSysFileUpload/ky/toKyFileUploadView?fileData='+fileData),
				end:callBackFunc
			});
		}
}

 

var toolUtil = {
		/**
		 * 初始化array  给array增加方法
		 */
		initArray:function(){
			/* 初始化array属性     给array增加移除和添加方法   */
			if(!Array.indexOf){
				Array.prototype.indexOf = function(el){//判断某个值在数组中的那个位置
					for(var a = 0,n=this.length;a<n;a++){
						if(this[a]===el){
							return a;
						}
					}
					return -1;
				}
			}
			if(!Array.removeByVal){
				Array.prototype.removeByVal = function(el){//通过值从数组中移除某个值
					var index = this.indexOf(el);
					if(index>-1){
						this.removeByIndex(index);
					}
				}
			}
			if(!Array.removeByIndex){
				Array.prototype.removeByIndex = function(el){//通过下标从数组中移除某个值
					if(isNaN(el)||el>this.length||el<0){
						return false;
					}
					this.splice(el,1);
				}
			}
		},
		/**
		 * 弹框关闭后获取弹框传递的值
		 */
		getLayViewResult:function(){
			if($("#layViewResult").text()){
				var layViewResult = JSON.parse($("#layViewResult").text());
				$("#layViewResult").text("");//数据取完之后将text置为空
				return layViewResult;
			}
			return false;
		}
}
toolUtil.initArray();


 

 
var checkMaxInput=function(obj,maxLen){
	if (obj == null || obj == undefined || obj == "") {
        return;
    }
    if (maxLen == null || maxLen == undefined || maxLen == "") {
        maxLen = 100;
    }
    var strResult;
    var $obj = $(obj);
    var newid = $obj.attr("id") + 'msg';
	var surplusSpan=$obj.parent().parent().find(".surplus");
	var surplusNum=Number(surplusSpan.text());
    if (obj.value.length > maxLen) { //如果输入的字数超过了限制
        obj.value = obj.value.substring(0, maxLen); //就去掉多余的字
        surplusSpan.text("0");
    }
    else {
    	surplusSpan.text(maxLen - obj.value.length);
    }
}
var checkMaxInput=function(obj,maxLen){
	if (obj == null || obj == undefined || obj == "") {
        return;
    }
    if (maxLen == null || maxLen == undefined || maxLen == "") {
        maxLen = 100;
    }
    var strResult;
    var $obj = $(obj);
    var newid = $obj.attr("id") + 'msg';
	var surplusSpan=$obj.parent().parent().find(".strongStar");
	var surplusNum=Number(surplusSpan.text());
    if(obj.value.length==0){
    	surplusSpan.text("(不能超过"+maxLen+"个汉字)*");
    }else if (obj.value.length > maxLen) { //如果输入的字数超过了限制
        obj.value = obj.value.substring(0, maxLen); //就去掉多余的字
        surplusSpan.text("(还能输入0个汉字)*");
    }
    else {
    	surplusSpan.text("(还能输入"+(maxLen - obj.value.length)+"个汉字)*");
    }
}

/* 解决js算法精度丢失问题 */
var mathUtil = {
		/**
		 * 除法
		 * @param arg1
		 * @param arg2
		 */
		accDiv:function(arg1,arg2){
			var t1 = 0,t2 = 0,r1,r2;
			try {
				t1 = arg1.toString().split(".")[1].length;
			} catch (e) {
			}
			try {
				t2 = arg2.toString().split(".")[1].length;
			} catch (e) {
			}
			with(Math){
				r1 = Number(arg1.toString().replace(".",""));
				r2 = Number(arg2.toString().replace(".",""));
				return mathUtil.accMul((r1/r2),Math.pow(10,t2-t1));
			}
		},
		/**
		 * 乘法
		 * @param arg1
		 * @param arg2
		 */
		accMul:function(arg1,arg2){
			var m =0,s1 = arg1.toString(),s2 = arg2.toString();
			try {
				m+=s1.split(".")[1].length;
			} catch (e) {
				
			}
			try {
				m+=s2.split(".")[1].length;
			} catch (e) {
				
			}
			return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
		},
		/**
		 * 加法
		 * @param arg1
		 * @param arg2
		 */
		accAdd:function(arg1,arg2){
			var r1,r2,m;
			try {
				r1 = arg1.toString().split(".")[1].length;
			} catch (e) {
				r1 = 0;
			}
			try {
				r2 = arg2.toString().split(".")[1].length;
			} catch (e) {
				r2 = 0;
			}
			m = Math.pow(10,Math.max(r1,r2));
			return (arg1*m+arg2*m)/m;
		},
		/**
		 * 减法
		 * @param arg1
		 * @param arg2
		 */
		subStr:function(arg1,arg2){
			var r1,r2,m,n;
			try {
				r1 = arg1.toString().split(".")[1].length;
			} catch (e) {
				r1 = 0;
			}
			try {
				r2 = arg2.toString().split(".")[1].length;
			} catch (e) {
				r2 = 0;
			}
			m = Math.pow(10,Math.max(r1,r2));
			n = (r1>=r2)?r1:r2;
			return ((arg1*m-arg2*m)/m).toFixed(n);		
			
		}
		
}

