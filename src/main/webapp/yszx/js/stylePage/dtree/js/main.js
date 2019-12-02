/**
 * Created by hanxifa05 on 2016-08-29.
 */
var main= {
    //获取时间函数
    configIp:"http://10.85.15.71:6789",//后台端口主地址
    configIp1:"http://10.85.15.71:6789/aa",//注销或者登陆过期返回首页的主地址  后边是文件的路径/YTHJZ/login/login.html
    configHttp:"http://10.85.61.202",
    create: function () {
        var Times = new Date();
        var year = Times.getFullYear();
        var mouth = Times.getMonth()+1;
        var date = Times.getDate();
        var week = Times.getDay().toLocaleString();
       // //console.log(week);
        var dat=JSON.parse(localStorage.getItem("loginIfo"));
        switch (week) {
            case '0':
                week = "日";
                break;
            case '1':
                week = "一";
                break;
            case '2':
                week = "二";
                break;
            case '3':
                week = "三";
                break;
            case '4':
                week = "四";
                break;
            case '5':
                week = "五";
                break;
            case '6':
                week = "六";
                break;
            case '0.00':
                week = "日";
                break;
            case '1.00':
                week = "一";
                break;
            case '2.00':
                week = "二";
                break;
            case '3.00':
                week = "三";
                break;
            case '4.00':
                week = "四";
                break;
            case '5.00':
                week = "五";
                break;
            case '6.00':
                week = "六";
                break;
        }
        return year + "年" + mouth + "月" + date + "日星期" + week + "&nbsp" + "&nbsp" + "&nbsp" + "&nbsp" + dat["data"]["user"]["userName"]+"你好,欢迎登录一体化监理系统";
    },
    
    getLocal:function(){
        var getData=localStorage.getItem("loginIfo");
        return getData;
    },
   /* getSessionId: function () {
        //获取sessionID函数
        var sessionID = JSON.parse(main.getLocal())["data"]["sessionId"];
        return sessionID;
    },*/
    //ajax请求接口
    requireAjax:function(url,text,type){//公共请求接口；请求ajax（url,参数,类型）
        ////console.log(text);
       // var sessionId=main.getSessionId();//获取存储在本地的sessionId；
        var data=null;
        $.ajax({
            url:url,
            type:type,
            data:{data:text},
            dataType:"json",
            timeout:40000,
            async:false,
            success:function(rd){
                main.isTimeOut(JSON.parse(rd));
                data=JSON.parse(rd);
                ////console.log(data);
            },
            error:function(err){
                ////console.log(err);
            },
            complete:function(XMLHttpRequest,status){
                if(status=='timeout'){
                    layer.alert("请求超时，请重新登录",function(){
                        location.reload();
                    });
                }
            }
        });
        return data;
    },
    requireData:function(url,text,type){
        //var index=layer.load(1,{shade:[0.3,"#000"]});
        //var sessionId=main.getSessionId();//获取存储在本地的sessionId；
        var data=null;
        $.ajax({
            url:url,
            type:type,
            data:{data:text},
            async:false,
            cache:false,
            timeout:40000,
            success:function(rd){
                main.isTimeOut(JSON.parse(rd));
                data=JSON.parse(rd);
                main.loginTimeout(data);
                //layer.close(index);               // //console.log(data);
            },
           /* beforeSend:function(xhr){
                xhr.setRequestHeader("sessionId",sessionId);//通过header传递sessionId;
                xhr.setRequestHeader("If-Modified-Since","0");
                xhr.setRequestHeader("Cache-Control","no-cache");
            },*/
            error:function(err){
                ////console.log(err);
            },
            complete:function(XMLHttpRequest,status){
                if(status=='timeout'){
                    layer.alert("请求超时",function(){
                        location.reload();
                    });
                }
            }
        });
        return data;
    },
    requireDataa:function(url,text,type){
   // var sessionId=main.getSessionId();//获取存储在本地的sessionId；
    var data=null;
    $.ajax({
        url:url,
        type:type,
        data:{data:text},
        //async:false,
        cache:false,
        dataType:"text",
        success:function(rd){
            data=rd;;
        },
//        beforeSend:function(xhr){
//            xhr.setRequestHeader("sessionId",sessionId);//通过header传递sessionId;
//            xhr.setRequestHeader("If-Modified-Since","0");
//            xhr.setRequestHeader("Cache-Control","no-cache");
//        },
        error:function(err){
            ////console.log(err);
        }
    });
    return data;
    },
    //判断sessionID是否超时，超时返回登录页
    isTimeOut:function(data){
        if(data.code=="1001"){
            layer.alert(data.msg,function(index){
                layer.close(index);
                window.parent.location.href=main.configIp1+"/YTHJZ/login/login.html";
            });
           // window.parent.location.href="../../../login/login.html";
        }
    },
    getWeekNum:function(time){
        var year=time.match(/\d{4}/gi)[0];
        var mouth=Number(time.match(/-\d{2}/gi)[0].replace("-",""));
        var day=Number(time.match(/-\d{2}/gi)[1].replace("-",""));
        var total=0;
        var years=new Date(year+"-01-01");
        if((year%4==0&&year%100!=0)||(year%400==0)){
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=29;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }else{
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=28;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }
        return Math.ceil((total+Number(years.getDay())-1)/7);
        // alert(total);
        //alert(Math.ceil((total+Number(years.getDay())-1)/7));
    },
    getWeekNum2:function(year,mouth,day){
        var total=0;
        var years=new Date(year+"/"+mouth+"/"+day);
        if((year%4==0&&year%100!=0)||(year%400==0)){
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=29;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }else{
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=28;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }
        var number=Number(years.getDay())-1;
        return Math.ceil((total+number)/7);
        //return Math.ceil((total+number)/7);
    },
    getWeekNum1:function(year,mouth,day){
        var total=0;
        var years=new Date(year+"/01/01");
        if((year%4==0&&year%100!=0)||(year%400==0)){
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=29;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }else{
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=28;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }
        var number=Number(years.getDay())-1;
        return Math.ceil((total+number)/7);
        //return Math.ceil((total+number)/7);
    },
    /*getWeekNum:function(time){
        var year=time.match(/\d{4}/gi)[0];
        var mouth=Number(time.match(/-\d{2}/gi)[0].replace("-",""));
        var day=Number(time.match(/-\d{2}/gi)[1].replace("-",""));
        var total=0;
        var years=new Date(year+"-01-01");
        if((year%4==0&&year%100!=0)||(year%400==0)){
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=29;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }else{
            switch (mouth){
                case 12:total+=30;
                case 11:total+=31;
                case 10:total+=30;
                case 9:total+=31;
                case 8:total+=31;
                case 7:total+=30;
                case 6:total+=31;
                case 5:total+=30;
                case 4:total+=31;
                case 3:total+=28;
                case 2:total+=31;
                case 1:total+=day;break;
            }
        }
        return Math.ceil((total+Number(years.getDay())-1)/7);
        // alert(total);
        //alert(Math.ceil((total+Number(years.getDay())-1)/7));
    },
    getWeekNum1:function(year,mouth,day){
    var total=0;
    var years=new Date(year+"/01/01");
    if((year%4==0&&year%100!=0)||(year%400==0)){
        switch (mouth){
            case 12:total+=30;
            case 11:total+=31;
            case 10:total+=30;
            case 9:total+=31;
            case 8:total+=31;
            case 7:total+=30;
            case 6:total+=31;
            case 5:total+=30;
            case 4:total+=31;
            case 3:total+=29;
            case 2:total+=31;
            case 1:total+=day;break;
        }
    }else{
        switch (mouth){
            case 12:total+=30;
            case 11:total+=31;
            case 10:total+=30;
            case 9:total+=31;
            case 8:total+=31;
            case 7:total+=30;
            case 6:total+=31;
            case 5:total+=30;
            case 4:total+=31;
            case 3:total+=28;
            case 2:total+=31;
            case 1:total+=day;break;
        }
    }
    var number=Number(years.getDay())-1;
    return Math.ceil((total+number)/7);
    //return Math.ceil((total+number)/7);
    },*/
    alertWin:function(name,cont){
        $("<div class=\"layer_big\"><div class=\"layer_big1\"><a>提示信息</a><span>X</span></div><div class=\"layer_big2\"><p class=\""+name+"\">"+cont+"</p><a class=\"sure\">确定</a></div></div>").fadeIn("fast").appendTo($("body"));
    },
    alertWinToSave:function(cont){
    $("<div class=\"layer_big\"><div class=\"layer_big1\"><a>提示信息</a><span>X</span></div><div class=\"layer_big2\"><p>"+cont+"</p><a class=\"save\">确定</a><a class=\"cancel\">取消</a></div></div>").fadeIn("fast").appendTo($("body"));
    },
    alertSave:function(cont){
        $("<div class=\"layer_big\"><div class=\"layer_big1\"><a>提示信息</a><span>X</span></div><div class=\"layer_big2\"><p>"+cont+"</p><a class=\"saveSuccess\">确定</a></div></div>").fadeIn("fast").appendTo($("body"));
    },
    contentHeight:function(){
        var height=$("body").height()-200;
        $(".content").css({height:height});
    },
    goodsContentHeight:function(){
        var height=$("body").height()-130;
        $(".content").css({height:height});
    },
    compactContentHeight:function(){
        var height=$("body").height()-160;
        $(".content").css({height:height});
       // //console.log(height);
    },
    checkContentHeight:function(){
        var height=$("body").height()-150;
        $(".content").css({height:height});
        ////console.log(height);
    },
    dataContentHeight:function(){
        var height=$("body").height()-50;
        $(".content").css({height:height});
        ////console.log(height);
    },
    goodsContentHeight:function(){
        var height=$("body").height()-100;
        $(".content").css({height:height});
        ////console.log(height);
    },
    typeContentHeight:function(){
        var height=$("body").height()-100;
        $(".content").css({height:height});
        //conosle.log(height);
    },
    DayContentHeight:function(){
        var height=$("body").height()-80;
        $(".content").css({height:height});
        ////console.log(height);
    },
    userContentHeight:function(){
        var height=$("body").height()-120;
        $(".content").css({height:height});
        //console.log(height);
    },
    projectContentHeight:function(){
        var height=$("body").height()-50;
        $(".content").css({height:height});
        //console.log(height);
    },
    stickSend:function(){
        var height=$("body").height()-150;
        $(".detailShow").css({height:height});
        //console.log(height);
    },
    trim:function(str){
        return str.match(/\S^(.)+\S$/)[0]
    },
    detailShow:function(){
        var height=$("body").height()-50;
        $(".detailShow").css({height:height});
    },
    loginTimeout:function(data){
        if(data.code=="1001"){
            layer.alert(data.msg,function(index){
                window.parent.location.href=main.configIp1+"/YTHJZ/login/login.html";
                layer.close(index);
                return;
            })
        }else if(data.code=="1000"){
            //layer.msg(data.msg);
        }
    },
    isNull:function(arr){
        for(var i=0;i<arr.length;i++){
            if(arr[i].value==""){
                arr.eq(i).focus().css({border:"1px solid #f00"});
            }
        }
    },
    isNoNull:function(arr){
        for(var i=0;i<arr.length;i++){
            arr.eq(i).css({border:"1px solid #ccc"});
        }
    },
    getTime:function(time){
        var arr=time.split("/");
        return arr[2]+"-"+arr[0]+"-"+arr[1];
    },
    publicPath:function(path){
        var changePath=main.configHttp+path.split("/YTHJZ_File")[1];
        return changePath;
    }
};
$(".goToHome").click(function() {
    window.parent.location.href=main.configIp1+"/YTHJZ/home/index.html";
})

