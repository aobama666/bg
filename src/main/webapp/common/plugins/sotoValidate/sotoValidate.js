$.fn.extend({
	sotoValidate:function (properties) {
		// 解决ie8不兼容indexOf方法
		if(!Array.indexOf){
			Array.prototype.indexOf = function(obj){
				for(var i=0;i<this.length;i++){
					if(this[i]==obj){
						return i;
					}
				}
				return -1;
			}
		}

		var rules = {
            required: [/[^\s]/, "必填项"],// 非空
            letters: [/^([a-z]+)*$/i, "只能输入字母"], // 纯字母
            tel: [/^((?:(?:0\d{2,3}[-]?[1-9]\d{6,7})|(?:[48]00[-]?[1-9]\d{6})))*$/, "电话格式不正确"], // 办公或家庭电话
            mobile: [/^(1[3-9]\d{9})*$/, "手机号格式不正确"], // 移动电话
            email: [/^((?:[a-z0-9]+[_\-+.]?)*[a-z0-9]+@(?:([a-z0-9]+-?)*[a-z0-9]+\.)+([a-z]{2,})+)*$/i, "邮箱格式不正确"],
            qq: [/^([1-9]\d{4,})*$/, "QQ号格式不正确,只能有长度大于4的数字组成"],
            date: [/^(\d{4}-\d{1,2}-\d{1,2})*$/, "请输入正确的日期,例:yyyy-mm-dd"],
            time: [/^(([01]\d|2[0-3])(:[0-5]\d){1,2})*$/, "请输入正确的时间,例14:30或4:30:00"],
            yyyy: [/^(\d{4})*$/, "请输入正确的年份,例:2015"],
            yyyyMM:[/^((\d{4})-(0\d{1}|1[0-2]))*$/, "请输入正确的年月,例:2015-01"],
            yyyyMMddhh:[/^((\d{4})-(\d{2})-(\d{2}) (\d{2}))*$/,"请输入正确的年月日时,例:2015-01-01 10"],
            yyyyMMddhhmm:[/^((\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}))*$/,"请输入正确的年月日时分,例:2015-01-01 10:10"],
            yyyyMMddhhmmss:[/^((\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}))*$/,"请输入正确的年月日时分秒,例:2015-01-01 10:10:01"],
            ID_card: [/^([1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[A-Z]))*$/, "请输入正确的身份证号码"],
            url: [/^((https?|ftp):\/\/[^\s]+)*$/i, "网址格式不正确"],
            postcode: [/^([0-9]\d{5})*$/, "邮政编码不正确"],
            chinese: [/^([\u0391-\uFFE5]+)*$/, "请输入中文"],
            username: [/^(\w{3,12})*$/, "请输入3-12位数字、字母、下划线"], // 用户名
            password: [/^([0-9a-zA-Z]{6,16})*$/, "密码由6-16位数字、字母组成"], // 密码
            passport: [/^(P\d{7}|G\d{8}|S\d{7,8}|D\d+|1[4,5]\d{7})*$/,"请输入正确的护照号码"],
            //百分比数据校验,0~100间数字,可以输入0和100,小数最多输入2位
            Percentage : [/^([0-9]\d?(\.\d{1,2})?|0.\d{1,2}|100)*$/,"请输入百分比范围内的数据"],
            //正整数
            numeric : [/^[1-9][0-9]*$/,"请输正整数"],
            // 自然数
            naturalNumber : [/^[0]|[1-9]*$/,"请输自然数"],
            //可接受的后缀名      
            accept: function(element, params) {
                if (!params) return true;
                var ext = params[0];
                return (ext === '*') ||
                    (new RegExp(".(?:" + (ext || "png|jpg|jpeg|gif") + ")$", "i")).test(element.value) ||
                    this.renderMsg("只接受{1}后缀", ext.replace('|', ','));
            }
        };
        
		var flag = true;
		var xOffset = 10; 
		var yOffset = 30;
		var thisbox = $(this);
		for(var i=0;i<properties.length;i++){
			var c = thisbox.find('[name='+properties[i].name+']');
			// 只给可见元素增加校验
			if(c.is(":visible")){
				var v = c.val() == null ? "" : c.val();
				var valiArray = properties[i].vali.split(";");
				var errMsg = "";
				for(var j=0;j<valiArray.length;j++){
					var rule = rules[valiArray[j]];
					var vali = valiArray[j];
					if(vali!=""){
						if(rule != undefined){// 存在此规则
							if(!rule[0].test(v)){
								errMsg += rule[1]+"；";
								flag = false;
							}
						}else {
							if(vali.indexOf(")") == vali.length-1){
								var ev = valiArray[j].substring(0,valiArray[j].length-2)+'("'+v+'")';
								var result = eval(ev);
								if(!result.result){
									errMsg += result.info;
									flag = false;
								}
							}else if(vali.indexOf("length") == 0){
								var length = valiArray[j].replace("length[", "").replace("]", "").split("-");
	                            if (length[0] > v.length) {
	                            	errMsg += "不能小于"+length[0]+"个字；";
	                            	flag = false;
	                            } else if (length[1] < v.length) {
	                            	errMsg += "不能大于"+length[1]+"个字；";
	                            	flag = false;
	                            }
							}
						}
					}
				}
				if(errMsg != ""){
					c.attr("errMsg",errMsg);
					c.parent("div").addClass("has-error");
				}else {
					c.removeAttr("errMsg");
					c.parent("div").removeClass("has-error");
					c.unbind('hover');
				}
				c.off("mouseenter").off("mouseleave");
				c.on('mouseenter',function(e){
					var em = $(this).attr("errMsg");
					if(em != undefined){
						$("body").append("<div id='errMsg' style='position:absolute;border-radius:5px;background:#fff;color:#a94442;padding:2px 4px 2px 8px ;border:1px solid #a94442;'>"+em+"</div>"); 
						$("#errMsg") 
						.css("top",(e.pageY - xOffset) + "px") 
						.css("left",(e.pageX + yOffset) + "px") 
						.fadeIn("slow");
					}
				}).on("mouseleave", function(){ 
					$("#errMsg").remove();
				}); 
				
				c.mousemove(function(e){ 
					$("#errMsg")
					.css("top",(e.pageY - xOffset) + "px") 
					.css("left",(e.pageX + yOffset) + "px"); 
				}); 
			}
		}
		
		return flag;
			
	}
});