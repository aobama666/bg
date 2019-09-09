$.fn.extend({
	organTree:function (properties) {
		return this.each(function () {
			var organBox = $(this);
			var organCode,organName,root,iframe,ct,limit,level,dataSrc,func,show;
			root = properties.root;
			level = properties.level == undefined ? "" : properties.level;//显示数节点   0 分院  1 部门  2 处室
			iframe = properties.iframe == undefined ? "self" : properties.iframe;
			ct = properties.checkType == undefined ? "radio" : properties.checkType;
			limit = properties.limit == undefined ? "" : properties.limit;//是否应用权限  yes 是   ，其他   否
			
			dataSrc = properties.dataSrc == undefined ? "" : properties.dataSrc;//数据来源：dataSrc=RLZY 人资专用，其他为报工默认
			func = properties.func == undefined ? "" : properties.func;//功能类型：func=YYGL 用印管理
			show = properties.show == undefined ? "" : properties.show;//show=PART 部分显示
			
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
				var iframeId = window.name;
				var height=$(window).height()*0.9;
				if(height>520) height = 520;
				var p = {
						type:2,
						title:"组织机构选择页面",
						area:['480px',height+'px'],//520px
						scrollbar:true,
						content:[basePath+'/organstufftree/initOrganTree?root='
						         +root+'&ct='+ct+'&level='+level+'&organCode='
						         +organCode+'&organName='+organName+'&limit='+limit
						         +'&popEvent='+popEvent+'&iframeId='+iframeId+'&iframe='+iframe+'&dataSrc='+dataSrc+'&func='+func+'&show='+show,'no'],
						success: function(layero, index){//弹窗加载完毕后，调整人员组织树的高度不被遮挡
					    	var iframes = layero.find("iframe");
					    	var stuffPage = iframes[0].contentWindow.document;
					    	$(stuffPage).find('.tree-box').height(height-110);
					 	}
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