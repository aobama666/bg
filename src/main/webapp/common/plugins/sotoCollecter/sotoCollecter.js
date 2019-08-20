$.fn.extend({
	sotoCollecter:function () {
		var param = {};
		var thisbox = $(this);
		thisbox.find("input,select,textarea").each(function(){
			_this = $(this);
			if(_this.attr("name") != undefined){
				if(_this.attr("type") == "checkbox"){
					param[_this.attr("name")] = _this.prop("checked") ? "1":"0";
				}else{
					param[_this.attr("name")] = _this.val();
				}
			}
		});
		return param;
	},
    sotoCollecterForZH:function () {
        var param = {};
        var thisbox = $(this);
        thisbox.find("input,select,textarea").each(function(){
            _this = $(this);
            if(_this.attr("name") != undefined){
                if(_this.attr("type") == "checkbox"){
                    param[_this.attr("name")] = _this.prop("checked");
                }else{
                    param[_this.attr("name")] = _this.val();
                }
            }
        });
        return param;
    },
	sotoCollecterForOne:function (property) {
		var param;
		var thisbox = $(this);
		thisbox.find("input,select,textarea").each(function(){
			_this = $(this);
			if(_this.attr("name") == property){
				if(_this.attr("type") == "checkbox"){
					param = _this.prop("checked") ? "1":"0";
				}else{
					param = _this.val();
				}
			}
		});
		return param;
	}
});