var common = {
		getAjax:function(url,method,param,success,error){
			var ran = common.getRan();
			$.ajax({
		        url: url +"?ran="+ ran,
		        type: method,
		        data:param,
		        jsonType:'json',
		        success: function (data) {
		        	success(data);
		        },
		        error:function(err){
		        	error(err)
		        }
		    });
		},
		getRan:function(){
			var ran = Math.random()*10000;
			return ran;
		},
		getQueryString:function(name){
	    	var reg=new RegExp("(^|&)"+name+"=([^&]*)(&|$)","i");
	    	var r=window.location.search.substr(1).match(reg);
	    	if(r !=null){
	    		return unescape(r[2]);
	    	}else{
	    		return null;
	    	}
	    },
	    getWeek:function(n){
	    	//查询本周的开始时间和结束时间    n=0查询本周开始时间   n=-6查询本周结束时间
	    	var now = new Date();
	    	var year = now.getFullYear();
	    	var month = now.getMonth+1;
	    	var date = now.getDate();
	    	var day = now.getDay();
	    	if(n==0){
	    		if(day!=0){
	    			n=day-1;
		    	}else{
		    		n=day+6;
		    	}
	    	}else{
	    		if(day!=0){
	    			n=day-7;
		    	}else{
		    		n=day;
		    	}
	    	}
	    	if(month>1){
	    		month = month;
	    	}else{
	    		month=12;
	    		year=year-1;
	    	}
	    	now.setDate(now.getDate()-n);
	    	year = now.getFullYear();
	    	month = now.getMonth()+1;
	    	date = now.getDate();
	    	var s = year +"-"+(month<10?('0'+month):month)+"-"+(date<10?('0'+date):date);
	    	return s;
	    },
	    getMouthAndJD:[
	                   {"name":"一月","value":"M1","selected":"false","type":"month","num":1},
	                   {"name":"二月","value":"M2","selected":"false","type":"month","num":2},
	                   {"name":"三月","value":"M3","selected":"false","type":"month","num":3},
	                   {"name":"四月","value":"M4","selected":"false","type":"month","num":4},
	                   {"name":"五月","value":"M5","selected":"false","type":"month","num":5},
	                   {"name":"六月","value":"M6","selected":"false","type":"month","num":6},
	                   {"name":"七月","value":"M7","selected":"false","type":"month","num":7},
	                   {"name":"八月","value":"M8","selected":"false","type":"month","num":8},
	                   {"name":"九月","value":"M9","selected":"false","type":"month","num":9},
	                   {"name":"十月","value":"M10","selected":"false","type":"month","num":10},
	                   {"name":"十一月","value":"M11","selected":"false","type":"month","num":11},
	                   {"name":"十二月","value":"M12","selected":"false","type":"month","num":12},
	                   {"name":"第一季度","value":"S1","selected":"false","type":"quarter","num":30},
	                   {"name":"第二季度","value":"S2","selected":"false","type":"quarter","num":60},
	                   {"name":"第三季度","value":"S3","selected":"false","type":"quarter","num":90},
	                   {"name":"第四季度","value":"S4","selected":"false","type":"quarter","num":120}
	                ],
	     getQuarterOrMonth:function(type){
	    	 var date = new Date();
	    	 var getMouths = date.getMonth()+1;
	    	 if(type=="quarter"){
	    		 if(getMouths==1||getMouths==2||getMouths==3){
	    			 getMouths=30;
	    		 }else if(getMouths==4||getMouths==5||getMouths==6){
	    			 getMouths=60;
	    		 }else if(getMouths==7||getMouths==8||getMouths==9){
	    			 getMouths=90;
	    		 }else if(getMouths==10||getMouths==11||getMouths==12){
	    			 getMouths=120;
	    		 }
	    	 }
	    	 var html = "";
	    	 var list=common.getMouthAndJD;
	    	 for(var i=0;i<list.length;i++){
	    		 if(list[i]["type"]==type){
	    			 if(list[i]["num"]==getMouths){
	    				 html += "<option selected value='"+list[i]["value"]+"'>"+list[i]["name"]+"</option>";
	    			 }else{
	    				 html += "<option value='"+list[i]["value"]+"'>"+list[i]["name"]+"</option>";
	    			 }
	    			 
	    		 }
	    	 }
	    	 return html;
	     }
}
