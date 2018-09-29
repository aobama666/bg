/**
 * 下拉联动
 * 
 * 当B修改时, 联动A的可选结果集
 * $("A").selectLinkage($("B")); 
 */
$.fn.extend({
	selectLinkage:function (properties, hasNullOption) {
		return this.each(function(){
			var flag = hasNullOption == undefined ? false : hasNullOption;
			var parentSelect = properties;
			var _this = $(this);
			var options = _this.find("option").clone();
			var optMap = {};
			options.each(function(){
				if(!optMap[$(this).attr("key")]){
					optMap[$(this).attr("key")] = new Array();
				}
				optMap[$(this).attr("key")].push($(this));
			});
			// 当前选项
			var curValue = _this.val();
			// 暂时清空下拉项
			_this.empty();
			parentSelect.on("change", function(){
				_this.empty();
				var key = parentSelect.val();
				if(flag){
					_this.append("<option value=''></option>");
				}
				_this.append(optMap[key]);
				_this.val(curValue == "" ? _this.find("option:eq(0)").attr("value") : curValue);
			});
		})
	}
});