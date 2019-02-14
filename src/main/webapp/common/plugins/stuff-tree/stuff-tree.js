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
			var empCode,empName,root,iframe,ct;
			root = properties.root;
			iframe = properties.iframe == undefined ? "self" : properties.iframe;
			ct = properties.checkType == undefined ? "radio" : properties.checkType;
			//自定义触发父层事件
			var popEvent = properties.popEvent == undefined ? "" : properties.popEvent;
			//自定义触发弹窗对象
			var bindLayId = properties.bindLayId == "tempAbc123" ? "" : properties.bindLayId;
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
				var height=$(window).height()*0.85;
				if(height>555) height = 555;
				var p = {
						type:2,// 0 content中填写显示的内容 ；1 content中为对象，弹出当前页面的一个块，如：contend:$(#id01) ；2 content中为文件地址，弹出一个文件 ，如：contend:'a.html'
						title:"人员选择页面",
						area:['480px',height+'px'],//555px
						scrollbar:true,
						content:[basePath+'/organstufftree/initStuffTree?iframe='+iframe+'&ct='+ct+'&winName='+winName+'&root='+root+'&empCode='+empCode+'&empName='+empName+'&popEvent='+popEvent,'no']
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