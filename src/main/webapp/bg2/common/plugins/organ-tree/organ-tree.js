$.fn.extend({
	organTree:function (properties) {	
		return this.each(function () {
			var organBox = $(this);
			var organCode,organName,root,iframe,ct,limit,level;
			root = properties.root;
			level = properties.level == undefined ? "" : properties.level;
			iframe = properties.iframe == undefined ? "self" : properties.iframe;
			ct = properties.checkType == undefined ? "radio" : properties.checkType;
			limit = properties.limit == undefined ? "" : properties.limit;
			//limit yes 根据用户管理权限查询  no
			//自定义触发父层事件
			var popEvent = properties.popEvent == undefined ? "" : properties.popEvent;
			//自定义触发弹窗对象
			var bindLayId = properties.bindLayId == "tempAbc123" ? "" : properties.bindLayId;
			// 如果指定了organCode和organName两个文本框的name,则按照指定的组件获取;否则如果指定organCode和organName的属性,按照属性获取;否则获取可见的的文本框为organName,不可见的文本框为organCode
			if(properties.organCode != undefined && properties.organCode != "" && properties.organName != undefined && properties.organName != ""){
				organCode = properties.organCode;
				organName = properties.organName;
			}else if(organBox.attr("organcode") != undefined && organBox.attr("organcode") != "" && organBox.attr("organname") != undefined && organBox.attr("organname") != ""){
				organCode = organBox.attr("organcode");
				organName = organBox.attr("organname");
			}else{
				organCode = organBox.find("input:hidden:eq(0)").attr("name");
				organName = organBox.find("input:visible:eq(0)").attr("name");
			}
			
			var localObj = window.location;
			var contextPath = localObj.pathname.split("/")[1];
			var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
			organBox.find("#"+bindLayId+",span>span,input[name="+organName+"]").off().click(function(){
				var p = {
						type:2,
						title:"组织机构选择页面",
						area:['480px','520px'],
						scrollbar:true,
						content:[basePath+'/organstufftree2/initOrganTree2?root='+root+'&ct='+ct+'&level='+level+'&organCode='+organCode+'&organName='+organName+'&limit='+limit+'&popEvent='+popEvent,'no']
						 };
				if(iframe == "parent")
					parent.layer.open(p);
				else
					layer.open(p);
			});
			
			$("input[name="+organName+"]").dblclick(function(){
				$(this).val("");
				$("input[name="+organCode+"]").val("");
			});
		});
	}
});