<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>dtree树</title>
    <link href="<%=request.getContextPath()%>/js/stylePage/dtree/css/dtree.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="dtree_box">
<p><a href="javascript:  d.closeAll();">合并所有</a>| <a href="javascript: d.openAll();">展开全部</a></p>
    <div class="dtree"></div>
</div>

<script src="<%=request.getContextPath()%>/js/stylePage/dtree/js/dtree.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.7.2.min.js"></script>
<script src="<%=request.getContextPath()%>/js/stylePage/dtree/js/main.js"></script>
<script>
    d = new dTree('d');//new对象
    d.add(0,-1,'树树树菜单');
    d.check = true;
    //设置树的节点及其相关属性

   /* d.add(1,0,'Node 1','example01.html');
    d.add(2,0,'Node 2','example01.html');
    d.add(3,1,'Node 1.1','example01.html');
    d.add(4,0,'Node 3','example01.html');
    d.add(5,3,'Node 1.1.1','example01.html');
    d.add(6,5,'Node 1.1.1.1','example01.html');
    d.add(7,0,'Node 4','example01.html');
    d.add(8,1,'Node 1.2','example01.html');
    d.add(9,0,'My Pictures','example01.html','Pictures I\'ve taken over the years','','');
    d.add(10,9,'The trip to Iceland','example01.html','Pictures of Gullfoss and Geysir');
    d.add(11,9,'Mom\'s birthday','example01.html');
    d.add(12,0,'Recycle Bin','example01.html','','');*/
    //config配置，设置文件夹不能被链接，即有子节点的不能被链接。

    d.add(0,-1,'Tree example','javascript: void(0);');
    d.add(1, 0,'Node 1','javascript:void(0);');
    d.add(2, 1,'Node 2','javascript:void(0);');
    d.add(3, 1,'Node 3','javascript:void(0);');
    d.add(4, 0,'Node 4','javascript:void(0);');
    d.add(5, 4,'Node 5','javascript:void(0);');
    d.add(6, 4,'Node 6','javascript:void(0);');
    d.add(7, 2,'Node 7','javascript:void(0);');
    d.add(8, 6,'Node 8','javascript:void(0);');
    d.add(9, 1,'Node 9','javascript:void(0);');
    d.add(10, 2,'Node 10','javascript:void(0);');
    d.add(11, 8,'Node 11','javascript:void(0);');
    d.add(12, 2,'Node 12','javascript:void(0);');
    d.add(13, 9,'Node 13','javascript:void(0);');
    d.add(14, 4,'Node 14','javascript:void(0);');
    d.add(15, 2,'Node 15','javascript:void(0);');
    d.add(16, 1,'Node 16','javascript:void(0);');
    d.add(17, 4,'Node 17','javascript:void(0);');
    d.add(18, 6,'Node 18','javascript:void(0);');
    d.add(19, 7,'Node 19','javascript:void(0);');

    /*$.ajax({
        type:'POST',//请求方式：post
        dataType:'json',//数据传输格式：json
        url:"datd.json",//请求的action路径
        error:function(){
            //请求失败处理函数
            alert('亲，请求失败！');
        },
        success:function(data){
            //console.log(data);
            //请求成功后处理函数
            d = new dTree('d');//new一个树对象
            d.add(0,-1,'管理');
            d.check = true;
            for(var i=0;i<datd.length;i++){
                d.add(i+1,data[i]["pid"],data[i]["name"],"example01.html");
                //console.log(proIdData1);
            }
            d.config.folderLinks=false;

        }
    });
*/
 
   /* for(var i=0;i<datad.data["list"].length;i++){
        d.add(i+1,0,'authority',datad.data["list"][i]["menuId"],datad.data["list"][i]["menuName"]);
    }*/
   d.config.folderLinks=false;
    document.write(d);
	//IE 图片没有地址时，会出现边框占位置
   if( $(".dtree img[src='']")){
	   $(".dtree img[src='']").css({"opacity":0,"width":0,"height":0});
		   
     }
</script>
</body>
</html>