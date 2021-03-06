$.fn.extend({
	stuffTree:function (properties) {
		return this.each(function () {
			var organBox = $(this);
			/*
			 * empCode 员工编号
			 * empName 员工姓名
			 * root    树形节点id（起始节点id）
			 * iframe  self 作用域：当前窗口   parent 作用域：父类窗口
			 * ct      树形节点选择框样式：radio，checkbox
			 */
			var empCode,empName,root,iframe,ct,limit,level,dataSrc,func,show,tmpType;
			root = properties.root;
			iframe = properties.iframe == undefined ? "self" : properties.iframe;
			ct = properties.checkType == undefined ? "radio" : properties.checkType;
			//自定义触发父层事件
			var popEvent = properties.popEvent == undefined ? "" : properties.popEvent;
			//自定义触发弹窗对象
			var bindLayId = properties.bindLayId == "tempAbc123" ? "" : properties.bindLayId;
			
			level = properties.level == undefined ? "" : properties.level;//显示数节点   0 分院  1 部门  2 处室
			limit = properties.limit == undefined ? "" : properties.limit;//是否应用权限  yes 是   ，其他   否
			dataSrc = properties.dataSrc == undefined ? "" : properties.dataSrc;//数据来源：dataSrc=RLZY 人资专用，其他为报工默认
			func = properties.func == undefined ? "" : properties.func;//功能类型：func=YYGL 用印管理
			show = properties.show == undefined ? "" : properties.show;//show=PART 部分显示
			tmpType = properties.tmpType == undefined ? "" : properties.tmpType;//tmpType=1 模板类型  1 报工  2 统一  默认为统一
			
			/* 
			 * 如果指定了empCode和empName两个文本框的name,则按照指定的组件获取;
			 * 否则如果指定empCode和empName的属性,按照属性获取;
			 * 否则获取可见的的文本框为empName,不可见的文本框为empCode
			 */
			if(properties.empCode != undefined && properties.empCode != "" && properties.empName != undefined && properties.empName != ""){
				empCode = properties.empCode;
				empName = properties.empName;
			}else if(organBox.attr("empcode") != undefined && organBox.attr("empcode") != "" && organBox.attr("empname") != undefined && organBox.attr("empname") != ""){
				empCode = organBox.attr("empcode");
				empName = organBox.attr("empname");
			}else{
				empCode = organBox.find("input:hidden:eq(0)").attr("name");
				empName = organBox.find("input:visible:eq(0)").attr("name");
			}
			var localObj = window.location;
			var contextPath = localObj.pathname.split("/")[1];
			var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
			organBox.find("#"+bindLayId+",span>span,input[name="+empName+"]").click(function(){
				//var index = iframe == "parent" ? parent.layer.getFrameIndex(window.name) : layer.getFrameIndex(window.name);
				var winName = window.name;
				var height=$(window).height()*0.9;
				if(height>555) height = 555;
				var p = {
						type:2,// 0 content中填写显示的内容 ；1 content中为对象，弹出当前页面的一个块，如：contend:$(#id01) ；2 content中为文件地址，弹出一个文件 ，如：contend:'a.html'
						title:"人员选择页面",
						area:['480px',height+'px'],//555px
						scrollbar:true,
						content:[basePath+'/organstufftree/initStuffTreeNew?iframe='
						         +iframe+'&ct='+ct+'&winName='+winName+'&root='+root+'&empCode='
						         +empCode+'&empName='+empName+'&popEvent='+popEvent
						         +'&limit='+limit+'&level='+level+'&dataSrc='+dataSrc+'&func='+func+'&show='+show+'&tmpType='+tmpType,'no'],
						success: function(layero, index){//弹窗加载完毕后，调整人员组织树的高度不被遮挡
						    	var iframes = layero.find("iframe");
						    	var stuffPage = iframes[0].contentWindow.document;
						    	$(stuffPage).find('.tree-box').height(height-155);
						 	}
				};
				if(iframe == "parent"){
					parent.layer.open(p);
				}else{
					layer.open(p);
				}
			});
			
		});
	}
});