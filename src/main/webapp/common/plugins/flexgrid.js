/***********************************************************************************/
/****   GRID控件前台JS类   ***********************************************************/
/*******************************************************************************
 * ** 主要功能：插入，删除，隐藏一行；加亮显示一行；键盘移动焦点；拖动单元格宽度等×××××××××××/ /**** 作者：戴林涛
 ******************************************************************************/
// 类名：GRID
var flexgrid_msg_1 = "发生错误！";				//function moveFocusUd(obj,par)		337
var flexgrid_msg_2 = "增加一行失败！"	;			//function addLine()				531
var flexgrid_msg_3 = "增加高度失败！"	;			//function addHeight()				548
var flexgrid_msg_4 = "删除一行失败！";			//this.delLine=function()			579
var flexgrid_msg_5 = "您拖动的区域无效!";			//function domouseup()				773
var flexgrid_msg_6 = "定位焦点失败！";			//function focus(row,col)			910
var flexgrid_msg_7 = "为合计区赋值失败！";			//function setSumValue()			1029
var flexgrid_msg_8 = "列名无效！";				//getIndexByName				1232
var flexgrid_msg_9 = "请关闭所有展开列，再排序！";   //function doTabTouClick()      1529
var flexgrid_msg_10 = "全选";                //function buildBodyHtml(gdObj, rowNum, cxtPath)    154
var flexgrid_msg_11 = "打印";               //function print()             2798
var flexgrid_msg_12 = "请输入gridName";      //function exportMultiPageExcel(gridName, fromPage, toPage)   2880
var flexgrid_msg_13 = "非法的页号输入";      //function exportMultiPageExcel(gridName, fromPage, toPage)   2880
var flexgrid_msg_14 = "合计";                //327
var flexgrid_msg_15 = "定位到首行"           //348
var flexgrid_msg_16 = "定位到尾行"           //348
function GRID() {
	// 私有变量
	var gridObj = null; // grid的json对象
	var treeObj = null; // treeGrid结点的json对象
	var divAll = null; // 外面的总的DIV
	var divTou = null; // 表头的DIV
	var divTi = null; // 表体的DIV
	var divSum = null; // 合计的DIV
	var divTiLeftCol = null; // 表体左侧固定列
	var divTouLeftCol = null; // 表头左侧固定列
	var divSumLeftCol = null; // 合计左侧固定列
	var inputSort = null; // 标识排序类型的隐藏域
	var inputSortName = null; // 标识排序列名的隐藏域
	var inputPk = null; // 标识主键的隐藏域
	var ctxMenu = null; // 右键菜单
	var tabTou = null; // 表头的TABLE
	var tabTi = null; // 表体的TABLE
	var tabRowLen= null; // 表体行数
	var tabSum = null; // 合计的TABLE
	var tableArray = new Array(); // 表体的数据集合
	var sortCol = 0; // 要排序的列
	var currentLine = null; // 当前操作的行
	var isMouDown = false; // 标识鼠标按下的状态
	var isCanSort = true; // 标识鼠标按下是否要执行排序
	var OldPlace = null; // 原来的位置
	var nowPlace = null; // 新位置
	var curTd = null; // 当前操作的TD,调整列宽
	var curHr = null; // 当前GRID的竖线
	var fix = 0; // 表头拖动竖线的修正值
	var primaryKeyCol = -1; // 主键列
	var deleteCol = -1; // 隐藏列
	var indexCol = -1; // 序号列
	var lastSortTd = null;// 上次排序的列
	var isSubmit = true; // 标识是否后台提交排序
	var isMultOrder = false;//标识多列排序 tiant
	var outCanSort = true;// 是否能排序，带树列时不能
	// 树状列表
	var paddingLeft = 0;// 缩进
	var xmlroot = null;// XML对象
	var OpenImg = "open.gif"; // 打开图片
	var CloseImg = "close.gif";// 关闭图片
	var sortType = "1"; // 表示数字类型的排序规则,ASCII或数字大小,默认为ASCII
	var orderType = "1"; // 表示排序的顺序,默认为升序
	this.format = ""; // 数字类型格式化
	this.columnNames = null;
	var increaseIndex = "false";
	var scroll_btn1 = "scrollbar_btn1_vert.gif";// 自定义滚动条上图标
	var scroll_btn2 = "scrollbar_btn2_vert.gif"; // 自定义滚动条下图标
	var oldTrBGColor = "";
	var isShiftKeyDown=false;// 是否shift按下
	var lastClk=null;// 最后点击的checkbox对象
	var lastCheck=false;// 最后选择的checkbox的状态
	var isMultiSel   =   false;// 是否处于多选状态
	var startIndex   =   -1;// 记录上次多选状态起始index
	var endIndex   =   -1;// 记录上次多选状态结束index
	// 公共变量
	this.name = null; // GRID的名称
	
	// 创建xmlHttp对象

	// 方法
	// 根据json结构输出html 采用模板技术
	this.outHtml = outHtml;
	function outHtml(jsonstr, cxtPath, gridname) {
		this.name = gridname;
		this.cxtPath = cxtPath.substring(0, cxtPath.lastIndexOf("/"));
		gridObj = jsonstr;
		// 添加支持高度为百分比
		var gridheight = gridObj.GRIDINFO.height;
		if (gridheight.indexOf('%') != -1) {
			var percent = parseInt(gridheight) * 0.01;
			gridObj.GRIDINFO.height = window.screen.height * percent;
		}
		columnNames = gridObj.ColumnNames;
		var xmlHttp_Js = null;
		var xmlHttp_Template = null;
		if (window.ActiveXObject) {
			xmlHttp_Js = new ActiveXObject("Microsoft.XMLHTTP");
			xmlHttp_Template = new ActiveXObject("Microsoft.XMLHTTP");
		} else if (window.XMLHttpRequest) {
			xmlHttp_Js = new XMLHttpRequest();
			xmlHttp_Template = new XMLHttpRequest();
		}
		xmlHttp_Js.onreadystatechange = function() {
			if (xmlHttp_Js.readyState == 4)
				if (xmlHttp_Js.status == 200) {
					if(window.execScript) {
						window.execScript(xmlHttp_Js.responseText, "JavaScript");
					}else {
						window.eval(xmlHttp_Js.responseText, "JavaScript");
					}
					
					delete xmlHttp_Js;
					xmlHttp_Js = null;
					//CollectGarbage();
				}
		};
		xmlHttp_Js.open("GET", cxtPath + "/template.js", false);
		xmlHttp_Js.send();
		xmlHttp_Template.onreadystatechange = function() {
			if (xmlHttp_Template.readyState == 4)
				if (xmlHttp_Template.status == 200) {
					// document.write(xmlHttp_Template.responseText);
					// document.write(TrimPath.processDOMTemplate("gridinfo_template",
					// gridObj));
					var template = TrimPath.parseTemplate(
							xmlHttp_Template.responseText, null);
					delete xmlHttp_Template;
					xmlHttp_Template = null;
					delete TrimPath;
					document.write(template.process(gridObj, null));
					template = null;
					
					// CollectGarbage();
				}
		};
		//截取字符串到第四个'/'之前
		//该段代码是截取应用的访问路径。根据“/”的个数截取，没有考虑应用上下文中“/”个数的问题，以及没有应用上下文的问题。
		/*var cnt = 0;
		for( var indexCurrent = 0 ; indexCurrent < cxtPath.length; indexCurrent++) {
			if('/' == cxtPath.charAt(indexCurrent)) {
				cnt = cnt + 1;
				if(cnt == 4) {
					cnt = indexCurrent;
					break;
				}
			}
		}
		cxtPath1 = cxtPath.substring(0, cnt);
		cxtPath1= cxtPath1 + "/skins/default/js";
		
		xmlHttp_Template
				.open("GET", cxtPath1 + "/templates/flexgrid.jsp", false);
				*/
		//modified by 曹圣泉，2011-10-28。之前代码是根据cxtPath截取访问根路径，没有考虑应用上下文不存在的情况。
		//出现这个问题的原因是，skins包作为js等样式相关的包，其他模块在映射为虚拟资源时，不会映射jsp的资源。此处提供直接访问skins包的方式访问资源。
		//修改方式：去掉cxtpath内的业务模块的模块上下文。
		//这里有2个约定：
		//1:模块上下文中没有字符“/”
		//2：skins模块内的jsp不会直接访问flexgrid。
		var defaultIndex = cxtPath.indexOf("/default/js");
		var cxtPathPrivous = cxtPath.substring(0,defaultIndex);
		var skinsIndex = cxtPathPrivous.lastIndexOf("/");//skins上下文的Index以防用户更改skins上下文
		var busIndex = cxtPathPrivous.lastIndexOf("/",skinsIndex-1);//业务模块上下文的 Index
		var cxtPath1 = cxtPath.substring(0,busIndex)+cxtPath.substring(skinsIndex);
		xmlHttp_Template.open("GET", cxtPath1 + "/templates/flexgrid.jsp", false);
		xmlHttp_Template.send();
	}
	// 输出表格html for 大数据量 edit by zhangjijie
	this.outHtmlForLarge = outHtmlForLarge;
	function outHtmlForLarge(jsonstr, cxtPath, gridname, rno) {
		this.name = gridname;
		__cxtPath = cxtPath;
		grid_jsonstr = jsonstr;
		grid_obj = this;
		var path = cxtPath.substring(0, cxtPath.lastIndexOf("/"));
		gridObj = jsonstr;
		// 添加支持高度为百分比
		var gridheight = gridObj.GRIDINFO.height;
		if (gridheight.indexOf('%') != -1) {
			var percent = parseInt(gridheight) * 0.01;
			gridObj.GRIDINFO.height = window.screen.height * percent;
		}
		columnNames = gridObj.ColumnNames;
		var gridHtml = buildBodyHtml(gridObj, rno, path);
		var moveDivHtml = buildMoveDiv(gridObj, path);
		var dobj = document.getElementById(gridname + "__tb__").rows[0];
		dobj.cells[0].innerHTML = gridHtml;
		dobj.cells[1].innerHTML = moveDivHtml;
	}
	// 组织flexgrid表格内容 for 大数据量 edit by zhangjijie
	this.buildBodyHtml = buildBodyHtml;
	function buildBodyHtml(gdObj, rowNum, cxtPath) {
		var __ginfo = gdObj.GRIDINFO;
		var __nm = __ginfo.name;
		var __ght = __ginfo.height;
		var gstr = [ '<div id="' + __nm + '" hassum="' + __ginfo.hasSum
				+ '"  hasleft="' + __ginfo.hasLeft
				+ '" style="overflow:hidden;width:100%; height:' + __ght
				+ 'px;" class="divAllcss">' ];
		var hrowCnt = gdObj.TabHead.rows.length;
		var thh = hrowCnt * 22;
		gstr
				.push('<div style="overflow:hidden;width:100%;height:'
						+ thh
						+ ';position:absolute;z-index:1"><div style="overflow:hidden;width:100%;height:'
						+ thh
						+ ';position:absolute;z-index:1" class="divToucss"><table border="0" width="100%" height='
						+ thh
						+ ' cellspacing="0" cellpadding="0"><colgroup><col width="40px">');
		var tabHeadColObj = gdObj.TabHead.rows[hrowCnt - 1].cols;
		for ( var col = 0; col < tabHeadColObj.length; col++) {
			gstr.push('<col  width="' + tabHeadColObj[col].width + '" style="'
					+ tabHeadColObj[col].style + '">');
		}
		gstr.push('<col></colgroup>');
		// 组织标题行
		for ( var row = 0; row < hrowCnt; row++) {
			var rowObj = gdObj.TabHead.rows[row];
			gstr.push(' <tr><td nowrap width="60px"></td>');
			for ( var colno = 0; colno < rowObj.cols.length; colno++) {
				var __colObj = rowObj.cols[colno];
				gstr.push('<td nowrap colspan="' + __colObj.colspan + '">');
				if (__colObj.text == flexgrid_msg_10)
					gstr
							.push('<span unselectable=on sortName="'
									+ __colObj.sortName
									+ '" sortType="'
									+ __colObj.sortType
									+ '" cansort="'
									+ __colObj.canSort
									+ '" enableCheckAll="'
									+ __colObj.enableCheckAll
									+ '" format="'
									+ __colObj.sortType
									+ '">'
									+ __colObj.text
									+ '<label disabled style="display:none">6</label></span></td>');
				else if (__colObj.text != flexgrid_msg_10)
					gstr
							.push('<span unselectable=on sortName="'
									+ __colObj.sortName
									+ '" sortType="'
									+ __colObj.sortType
									+ '" cansort="'
									+ __colObj.canSort
									+ '" enableCheckAll="'
									+ __colObj.enableCheckAll
									+ '" format="'
									+ __colObj.sortType
									+ '">'
									+ __colObj.text
									+ '<label disabled style="display:none">6</label></span></td>');
			}
			gstr
					.push('<td nowrap style="display:blank"><span unselectable=on cansort="true" format=""></span></td></tr>');
		}
		gstr
				.push('</table><iframe height="100%" width="100%" frameborder="0" style="top:-1;left-1;position:absolute;z-index:-1;" scrolling=no></iframe></div>');
		gstr
				.push('<div style="width:41px;height:' + thh + ';position:absolute;top:0;left:0;z-index:2;" class="divToucss"><table width="100%" height="100%" border="0" cellspacing="0"  cellpadding="0"><tr><td nowrap width="41px"><span></span></td></tr></table></div></div>');
		var ticss = "divTiSkin";
		if (__ginfo.isWrap == "true")
			ticss = "divTiSkinWrap";
		var headheight = (hrowCnt - 1) * 23 - 1;
		if (__ginfo.isPrint == "true")
			gstr.push('<div style="width:100%;height:' + __ght
					+ 'px;position:absolute;padding-top:' + headheight
					+ ';" class="' + ticss + '">');
		// 隐藏滚动条
		else
			gstr
					.push('<div style="overflow-x:scroll;overflow-y:hidden;width:100%;height:'
							+ __ght
							+ 'px;position:absolute;padding-top:'
							+ headheight + ';" class="' + ticss + '">');
		gstr
				.push('<table border="0" width="100%"  cellspacing="0" cellpadding="0">');

		// 总记录gridObj.TabTi.rows.length
		var rowCnt = gdObj.TabTi.rows.length;
		var pr = 1;// 减去标题行
		if (__ginfo.hasSum == "true")
			pr = gdObj.TabSum.rows.length + pr;
		var rl = Math.floor(__ght / 20) - pr - 1; // 根据整体grid的高度计算得出能够显示的行数
		var i = 0;
		if (rowNum != null && rowCnt >= rl) { // 计算开始行数
			if (rowNum > rl && rowNum < (rowCnt - rl))
				i = rowNum - Math.round(rl / 2);
			else if (rowNum >= (rowCnt - rl) && rowNum <= rowCnt)
				i = gdObj.TabTi.rows.length - rl - 1;
			else
				i = 0;
		}
		var __tmp = 0;
		if (rowCnt > rl)
			__tmp = i + rl + 1;
		else
			__tmp = rowCnt;
		for ( var opt = i; opt < __tmp; opt++) {
			var titr = gdObj.TabTi.rows[opt];
			var firsttr = gdObj.TabTi.rows[0].cols;
			//edit by duyong 修改隔行换色功能丢失问题
			if ( opt%2==0 ) {
				gstr.push('<tr  style="background:#DFF4F1"><td width="40px"><span></span></td>');
			} else {
				gstr.push('<tr  style="background:#FFFFFF"><td width="40px"><span>test</span></td>');
			}
			//gstr.push('<tr><td width="40px"><span></span></td>');
			for ( var vtdopt = 0; vtdopt < titr.cols.length; vtdopt++) {
				var vtd = titr.cols[vtdopt];
				var titd = firsttr[vtdopt];
				gstr.push('<td width="' + titd.width + '" style="' + titd.style
						+ '" index="' + titd.index + '" ' + titd.eventString
						+ '>');
				if (titd.coltype == "flex")
					gstr.push('<span type="' + titd.type + '" title="'
							+ vtd.title + '" style="' + titd.style
							+ '" class="' + titd.styleClass + '">' + vtd.value
							+ '</span></td>');
				else if (titd.coltype == "checkbox") {
					gstr.push('<input type="checkbox" style="' + titd.style
							+ ';background:this.parentNode.background" name="' + titd.name + '" ');

					if (titd.disabled == "true")
						gstr.push(' disabled');
					gstr.push(' value="' + vtd.value + '" ' + vtd.checked
							+ '></td>');
				} else if (titd.coltype == "radio") {
					gstr.push('<input type="radio" style="' + titd.style
							+ '" name="' + titd.name + '"');
					if (titd.disabled == "true")
						gstr.push(' disabled');
					gstr.push(' value=' + vtd.value + ' ' + vtd.checked
							+ '></td>');
				} else if (titd.coltype == "tree") {
					gstr.push('<div align="left"><img src="' + __ginfo.openFlag
							+ '" nodeid="' + vtd.nodeid + '"></div></td>');
				}

			}
			gstr
					.push('<td style="display:block;border-right:0;"><span></span></td></tr>');
		}
		gstr
				.push('</table><div style="overflow:hidden;width:100%;height:20px;position:absolute;"></div></div>');
		gstr
				.push('<div style="overflow:hidden;width:100%;height:21px;position:absolute;top:0;z-index:2;display:none;">');
		gstr
				.push('<div id="' + __nm + '_sumti" style="overflow:hidden;width:100%;height:21px;" class="divSumcss"><table border=0 width="100%" height=20 cellpadding=0 cellspacing=0>');
		var sumtr = gdObj.TabSum.rows;
		for ( var sumtropt = 0; sumtropt < sumtr.length; sumtropt++) {
			gstr.push('<tr><td nowrap width="40px"></td>');
			var sumcol = sumtr[sumtropt].cols;
			for ( var sumcolopt = 0; sumcolopt < sumcol.length; sumcolopt++) {
				gstr.push('<td width="' + sumcol[sumcolopt].width
						+ '" nowrap style="' + sumcol[sumcolopt].style + '" >');
				gstr.push('<span type="' + sumcol[sumcolopt].stype
						+ '" unselectable=on format="'
						+ sumcol[sumcolopt].format + '" style="'
						+ sumcol[sumcolopt].style + '">'
						+ sumcol[sumcolopt].text + '</span></td>');
			}
			gstr
					.push('<td style="display:block;border-right:0;"><span></span></td></tr>');
		}
		gstr
				.push('</table></div><div id="' + __nm + '_sumleft" style="width:41px;position:absolute;top:0;left:0;z-index:2;height:21px;" class="divSumcss"><table width="100%" border=0  cellspacing=0  cellpadding=0><tr><td nowrap width="41px"><span>' + flexgrid_msg_14 + '</span></td></tr></table></div></div>');
		var infoheight = __ght - 22;
		gstr
				.push('<div  style="width:41px;position:absolute;height:'
						+ infoheight
						+ 'px;overflow:hidden;padding-top:'
						+ headheight
						+ ';"  class="divLeftCss"><table border=0 width="100%" cellspacing=0 cellpadding=0>');
		for ( var topt = i; topt < __tmp; topt++) {
			gstr
					.push('<tr><td nowrap width="41px"><span unselectable=on cansort="false">' + topt + '</span></td></tr>');
		}
		gstr.push('</table></div>');
		var hidtxt = __ginfo.hidden;
		for ( var hopt = 0; hopt < hidtxt.length; hopt++) {
			gstr.push('<input type="hidden" name="' + hidtxt[hopt].name
					+ '" value="' + hidtxt[hopt].value + '"/>');
		}
		gstr
				.push('<div style="display:none;" class="ctxMenu"><table CELLSPACING=1><tr><td><span>' + flexgrid_msg_15 + '</span></td></tr><tr><td><span>' + flexgrid_msg_16 + '</span></td></tr></table><iframe frameborder=0 style="width:100%;height:100%;top:0;left:0;position:absolute;z-index:-1;"></iframe></div></div>');
		return gstr.join("");
	}
	this.buildMoveDiv = buildMoveDiv;
	function buildMoveDiv(gdObj) {
		var __ginfo = gdObj.GRIDINFO;
		var __nm = __ginfo.name;
		var __ght = __ginfo.height;
		var __bh = 16;// 滑动块最小高度
		var __rht = __ght - mdivBottom - 2 * mdivTop;// 滑动块可移动范围
		var pr = 1;// 减去标题行
		var rc = gdObj.TabTi.rows.length - 1;
		if (__ginfo.hasSum == "true")
			pr = gdObj.TabSum.rows.length + pr;
		var rl = Math.floor(__ght / 20) - pr - 1; // 根据整体grid的高度计算得出能够显示的行数
		var gstr = [];
		gstr
				.push('<DIV id="' + __nm + '_rbar" style="width:12px;position:absolute;">');
		gstr.push('<TABLE  id="' + __nm
				+ '_controlTb" style="width:12px; height:' + __ght
				+ 'px;" cellSpacing="0" cellPadding="0" border="0"><TBODY>');
		var path = __cxtPath.substring(0, __cxtPath.lastIndexOf("/"));
		var __btn1 = path + "/images/" + scroll_btn1;
		var __btn2 = path + "/images/" + scroll_btn2;
		gstr.push('<TR style="height:' + mdivTop
				+ 'px"><TD style="background:#CCCCCC"><IMG  src="' + __btn1
				+ '" /></TD></TR>');
		gstr
				.push('<TR style="background:#CCCCCC"><TD style="WIDTH: 100%;" vAlign="top">');
		var mdivH = 0;
		if (rc > rl)
			mdivH = Math.floor(rl * __rht / rc);
		else
			mdivH = __rht
		if (mdivH < __bh)
			mdivH = __bh;
		moveDivHeight = mdivH;
		var display = "block";
		if (rc <= rl)
			display = "none";
		gstr
				.push('<div id="'
						+ __nm
						+ '_MoveDiv"  style="position:absolute;width:15px;top:'
						+ mdivTop
						+ 'px;height:'
						+ mdivH
						+ 'px;background:#C0C0C0;display:'
						+ display
						+ ';border:1px ridge #808080" onmousedown="MDown('
						+ __nm
						+ '_MoveDiv,'
						+ rc
						+ ')"></div><div id="'
						+ __nm
						+ '_SCS" style="position:absolute;width:60;height:12px;background:yellow;right:15px;display:none"></div>');
		gstr.push('</TD></TR>');
		gstr.push('<TR style="height:' + mdivTop
				+ 'px;"><TD style="background:#CCCCCC;"><IMG  src="' + __btn2
				+ '"/></TD></TR><TR style="background:#CCCCCC;height:'
				+ mdivBottom + 'px;cellSpacing:spacing"><TD></TD></TR>');
		gstr.push('</TBODY></TABLE>');
		gstr.push('</DIV>');
		return gstr.join("");
	}

	// 初始化,参数列表为外面DIV的ID,序号列，主键列，隐藏列，单元格的单击事件，双击事件
	this.init = init;
	function init(odivid, Index, primaryKey, Delete, clickEvent, dblclickEvent) {
		this.name = odivid;
		divAll = document.all(odivid);
		if (divAll != null && divAll.tagName == "DIV") {
			divTou = divAll.children[0].children[0];
			divTi = divAll.children[1];
			divSum = divAll.children[2].children[0];
			divTouLeftCol = divAll.children[0].children[1];
			divSumLeftCol = divAll.children[2].children[1];
			divTiLeftCol = divAll.children[3];
			inputSort = divAll.children[4];
			inputSortName = divAll.children[5];
			inputPk = divAll.children[6];
			ctxMenu = divAll.children[8];
			if (divTou != null)
				tabTou = divTou.children[0];
			if (divTi != null){	
				tabTi = divTi.children[0];
				tabRowLen= tabTi.rows.length;
			}
			if (divSum != null)
				tabSum = divSum.children[0];
// for ( var i = 1; i < tabTi.rows.length; i++) {
// if (i % 2 != 0)
// tabTi.rows[i].className = "lightSkin1";
// else
// tabTi.rows[i].className = "lightSkin2";
// }
			// 为divSum定位
			// 这三个定位操作可以通过触发一个onresize事件来做
			// divSum.parentElement.style.top=parseInt(divTi.offsetTop)+parseInt(divTi.clientHeight)-(parseInt(divSum.clientHeight))+2;

			// 由于出现了滚动条,重新给宽度赋值
			// divTiLeftCol.style.height=divTi.clientHeight-21;
			// divTou.style.width=divTi.clientWidth;
			// divSum.style.width=divTi.clientWidth;
			// 处理可能的隐藏列
			var touLast = $(tabTou).find('tr').length - 1
			// var touLast = tabTou.rows(tabTou.rows.length - 1);
			var tiFirst = tabTi.rows(0);  //$(tabTou).find('tr').eq(0)
			if (touLast.cells.length == tiFirst.cells.length) {
				var sumFirst = tabSum.rows(0);
				var len = tiFirst.cells.length - 1;
				var cols = tabTou.getElementsByTagName("COLGROUP")[0];
				var r = 1;
				for ( var i = 1; i < len; i++) {
					if (tiFirst.cells(i).style.display == "none") {
						touLast.cells(r).removeNode(true);
						cols.children[r].removeNode(true);
						sumFirst.cells(r).removeNode(true);
						r--;
					}
					r++;
				}
			}
			// 添加事件
			tabTi.attachEvent("onmousedown", this.doclick);
			tabTi.attachEvent("onkeydown", this.dokeydownTab);
			tabTi.attachEvent("onkeyup", this.dokeyupTab);
			divTi.attachEvent("onscroll", this.doscroll);
			tabTou.attachEvent("onmousedown", this.domousedown);
			tabTou.attachEvent("onmouseup", this.domouseup);
			tabTou.attachEvent("onmousemove", this.domousemove);
			tabTou.attachEvent("onmouseout", this.domouseout);
			tabTou.attachEvent("onclick", this.doTabTouClick);
			divAll.attachEvent("onresize", this.doresize);
			divAll.attachEvent("oncontextmenu", this.showCtxMenu);
			ctxMenu.attachEvent("onmousedown", this.doCtxClick);
			if (clickEvent != "" && clickEvent != null)
				tabTi.attachEvent("onclick", clickEvent);
			// if(dblclickEvent!="" && dblclickEvent!=null)
			tabTi.attachEvent("ondblclick", this.dblclickEvent);
			// 合计,后台计算合计
			// setSumValue();
			// 显示排序箭头
			var sort = inputSort.value.toString();
			var sortname = inputSortName.value.toString();
			var tou = tabTou.tBodies[0].lastChild;
			for ( var i = 1; i < tou.cells.length - 1; i++) {
				var obj = tou.cells[i];
				obj = obj.children[0];
				if (obj.tagName == "SPAN") {
					var attname = obj.getAttribute("sortName");
					if (attname == sortname) {
						lastSortTd = obj;
						if (sort.toUpperCase() == "DESC") {
							obj.children[0].innerText = "6";
							obj.children[0].style.display = "inline";
						} else {
							obj.children[0].innerText = "5";
							obj.children[0].style.display = "inline";
						}
					}
				}
			}
			// 处理合计区和左侧列的显示和隐藏
			this.showSum(gridObj.GRIDINFO.hasSum);
			// 如果有sum,将sum显示出来
			divAll.children[2].style.display = "block";
			this.showLeft(gridObj.GRIDINFO.hasLeft);
			this.changeLeftIndex(gridObj.GRIDINFO.increaseIndex);
			// 为页面添加一个竖线
			addHr();
			// 实现定位
			divAll.fireEvent("onresize");
		}

	}
	// 显示\隐藏合计行
	this.showSum = showSum;
	function showSum(isShow) {
		var dis = null;
		if (isShow == "true") {
			dis = "";
			divTiLeftCol.style.height = divTi.clientHeight - 21;
		} else if (isShow == "false") {
			dis = "none";
			divTiLeftCol.style.height = divTi.clientHeight;
		} else
			return;
		divSum.style.display = dis;
		divSumLeftCol.style.display = dis;
		tabTi.nextSibling.style.display = dis;
	}
	// 显示\隐藏左侧序号列
	this.showLeft = showLeft;
	function showLeft(isShow) {
		var dis = null;
		if (isShow == "true" && divTouLeftCol.style.display == "none") {
			dis = "";
			var col = document.createElement("COL");
			tabTou.children[0].insertAdjacentElement("afterBegin", col);
			col.width = "40px";
			divTiLeftCol.style.display = "";
			if (divSum.style.display != "none") {
				divSumLeftCol.style.display = "";
				tabSum.rows[0].cells[0].style.display = "";
			}
		} else if (isShow == "false" && divTouLeftCol.style.display != "none") {
			dis = "none";
			tabTou.children[0].children[0].removeNode();
			divSumLeftCol.style.display = "none";
			divTiLeftCol.style.display = "none";
			tabSum.rows[0].cells[0].style.display = "none";
		} else
			return;
		divTouLeftCol.style.display = dis;
		var i = 0;
		var tou = null;
		var ti = null;
		while (i < tabTou.rows.length && (tou = tabTou.rows[i++].cells[0]))
			tou.style.display = dis;
		i = 0;
		while (i < tabTi.rows.length && (ti = tabTi.rows[i++].cells[0]))
			ti.style.display = dis;
	}
	// 为页面添加一个竖线，调整列用
	this.addHr = addHr;
	function addHr() {
		var ohr = document.createElement("DIV");
		ohr.className = "divHr";
		ohr.style.display = "none";
		divAll.appendChild(ohr);
		curHr = ohr;
	}
	// 显示右键菜单
	this.showCtxMenu = showCtxMenu;
	function showCtxMenu() {
		var dtop = 0;
		var dleft = 0;
		var odiv = divAll;
		while (odiv.tagName != "BODY") {
			dtop += odiv.offsetTop;
			dleft += odiv.offsetLeft;
			odiv = odiv.offsetParent;
		}
		var xx = event.clientX;
		var yy = event.clientY;
		ctxMenu.style.display = "block";
		if (xx + ctxMenu.offsetWidth > document.body.offsetWidth)
			xx -= ctxMenu.offsetWidth;
		if (yy + ctxMenu.offsetHeight > document.body.offsetHeight)
			yy -= ctxMenu.offsetHeight;
		xx += document.body.scrollLeft - dleft;
		yy += document.body.scrollTop - dtop;
		if (xx + ctxMenu.offsetWidth > divAll.offsetWidth)
			xx -= ctxMenu.offsetWidth;
		if (yy + ctxMenu.offsetHeight > divAll.offsetHeight)
			yy -= ctxMenu.offsetHeight;
		ctxMenu.style.left = xx;
		ctxMenu.style.top = yy;
		ctxMenu.setCapture(false);
		window.event.cancelBubble = true;
		window.event.returnValue = false;
	}
	// 右键菜单点击事件
	this.doCtxClick = doCtxClick;
	function doCtxClick() {
		var eEl = event.srcElement;
		var eTr = eEl;
		var rIndex = null;
		if (eEl.tagName == "SPAN" && ctxMenu.contains(eEl))
			while (eTr != null && (eTr = eTr.parentElement).tagName != "TR")
				;
		if (eTr != null)
			rIndex = eTr.rowIndex;
		switch (rIndex) {
		// case 0:
		// // 固定一列
		// break;
		// case 1:
		// //显示明细
		// break;
		case 0:
			// 定位到首行
			moveToFirstRow();
			break;
		case 1:
			// 定位到尾行
			moveToLastRow();
			break;
		default:
			;
		}
		ctxMenu.style.display = "none";
		ctxMenu.releaseCapture();
		window.event.cancelBubble = false;
		window.event.returnValue = true;
	}
	// 定位到第一行
	this.moveToFirstRow = moveToFirstRow;
	function moveToFirstRow() {
		if (tabTi.rows.length > 1) {
			if (currentLine != null)
				delightLine(currentLine);
			currentLine = tabTi.rows[1];
			lightLine(currentLine);
			divTi.scrollTop = 0;
		}
	}
	// 定位到最后一行
	this.moveToLastRow = moveToLastRow;
	function moveToLastRow() {
		if (tabTi.rows.length > 1) {
			if (currentLine != null)
				delightLine(currentLine);
			currentLine = tabTi.rows[tabTi.rows.length - 1];
			lightLine(currentLine);
			if (treeObj != null && currentLine.style.display == "none")
				OpenNode(null,
						currentLine.cells[1].children[0].children[0].nodeid);
			divTi.scrollTop = divTi.scrollHeight - divTi.clientHeight;
		}
	}
	this.moveToRowByIndex = moveToRowByIndex;
	function moveToRowByIndex(index) {
		if (tabTi.rows.length > 1) {
			if (currentLine != null)
				delightLine(currentLine);
			if (index > 0 && index < tabTi.rows.length)
				currentLine = tabTi.rows[index];
			lightLine(currentLine);
			if (treeObj != null && currentLine.style.display == "none")
				OpenNode(null,
						currentLine.cells[1].children[0].children[0].nodeid);
			divTi.scrollTop = divTi.scrollHeight - divTi.clientHeight;
		}
	}
	// 显示当前行的明细信息
	this.showDetail = showDetail;
	function showDetail() {

	}
	// 加亮显示一行,参数为要加亮的行
	this.lightLine = lightLine;
	function lightLine(otr) {
		if (otr.rowIndex == 0)
			return;
		if (otr != null && otr.tagName == "TR") {
			try {
				/*
				 * for(var i=0;i<otr.cells.length;i++) { var otd=otr.cells[i];
				 * otd.className="lightSkin"; }
				 */
				// 给checkbox变色
				// 增加对radio的判断
// for ( var i = 0; i < otr.cells.length; i++) {
// if (otr.childNodes[i].childNodes[0].type == "checkbox"
// || otr.childNodes[i].childNodes[0].type == "radio")
// otr.childNodes[i].childNodes[0].style.backgroundColor = "#BEE3E4";
// }
				oldTrBGColor = otr.style.backgroundColor;
				// 给正行变色
				otr.style.backgroundColor = "#9ae3e2";
			} catch (ex) {
				return;
			}
		}
	}

	// 还原加亮的行，参数为要恢复的行
	this.delightLine = delightLine;
	function delightLine(otr) {
		if (otr != null && otr.tagName == "TR") {
			try {
				/*
				 * for(var i=0;i<otr.cells.length;i++) { var otd=otr.cells[i];
				 * // otd.className=""; }
				 */
				// 增加对checkbox和radio的判断
// for ( var i = 0; i < otr.cells.length; i++) {
// if (otr.childNodes[i].childNodes[0].type == "checkbox"
// || otr.childNodes[i].childNodes[0].type == "radio")
// otr.childNodes[i].childNodes[0].style.backgroundColor = oldTrBGColor;
// }

				otr.style.backgroundColor = oldTrBGColor;
			} catch (ex) {
				return;
			}
		}
	}

	// 单击事件，选择当前行
	this.doclick = doclick;
	function doclick() {
		var obj = window.event.srcElement;
		if (obj == null)
			return;
		var oTr = null;

		switch (obj.tagName) {
		case "TR":
			oTr = obj;
			break;
		case "TD":
			oTr = obj.parentElement;
			break;
		case "SPAN":
			oTr = obj.parentElement.parentElement;
			break;
		case "INPUT":
			oTr = obj.parentElement.parentElement;
			if (obj.type == "checkbox" || obj.type == "radio")
				doCheck(obj);
			break;
		case "SELECT":
			oTr = obj.parentElement.parentElement;
			break;
		case "DIV":
			oTr = obj.parentElement.parentElement;
			break;
		case "IMG":
			oTr = obj.parentElement.parentElement.parentElement;
			break;
		default:
			return;
		}
		try {
			if (currentLine != null)
				delightLine(currentLine);
			if (oTr != null)
				lightLine(oTr);
			currentLine = oTr;
		} catch (ex) {
			// alert(ex.description);
		}
		doclickAfter(currentLine);
	}
	this.chkClk = function(chkObj) {
		if (chkObj.name == "")
			return;
		// 取到所有checkBox对象
		var chks = document.all(chkObj.name);
		if (isShiftKeyDown) {
			if (!lastClk) {
				lastClk = chks[0];
			}
			if (isMultiSel) {
				for ( var i = startIndex; i <= endIndex; i++) {
					chks[i].checked = !lastCheck;
				}
			}
			var startChk = lastClk;
			var endChk = chkObj;
			if (lastClk.sourceIndex > chkObj.sourceIndex) {
				var temp = startChk;
				startChk = endChk;
				endChk = temp;
			}
			for ( var i = 0; i < chks.length; i++) {
				if (chks[i] == startChk) {
					startIndex = i;
				}
				if (chks[i] == endChk) {
					endIndex = i;
					break;
				}
			}
			for ( var i = startIndex; i <= endIndex; i++) {
				chks[i].checked = lastCheck;
			}
			isMultiSel = true;
		} else {
			lastClk = chkObj;
			isMultiSel = false;
		}
		lastCheck = chkObj.checked;
	}   
	this.dokeyupTab = function (){

		var obj = window.event.srcElement;
		var otd = obj.parentElement;
		if (otd == null || otd.tagName != "TD")
			return false;
		var otr = otd.parentElement;
		if (otr == null || otr.tagName != "TR")
			return false;
		var tab = otr.parentElement.parentElement;
		if (tab == null || tab.tagName != "TABLE")
			return;
		if (obj == null)
			return;
		try {
			if (window.event.keyCode == 16 && tab == tabTi) {
			  isShiftKeyDown=false; 
			}

		} catch (ex) {
		}
	 
	}
	// 键盘事件，回车键换行，上下键上下移动
	this.dokeydownTab = function() {
		var obj = window.event.srcElement;
		var otd = obj.parentElement;
		if (otd == null || otd.tagName != "TD")
			return false;
		var otr = otd.parentElement;
		if (otr == null || otr.tagName != "TR")
			return false;
		var tab = otr.parentElement.parentElement;
		if (tab == null || tab.tagName != "TABLE")
			return;
		if (obj == null)
			return;
		try {
			// if(window.event.keyCode=="13")
			// {
			// movetToRight(obj);
			// return;
			// if(IsLast(obj))
			// {
			// addRow();
			// var nobj=otr.nextSibling.children[0].children[0];
			// if(nobj!=null)
			// nobj.focus();
			// }
			// else
			// {
			// if(otd==otr.cells[otr.cells.length-1])
			// {
			// var nobj=otr.nextSibling.children[0].children[0];
			//						
			// //if(nobj!=null)
			// //nobj.focus();
			// }
			// else
			// {
			// var ntd=otd.nextSibling.children[0];
			// if(ntd!=null)
			// {
			// ntd.focus();
			// }
			// }
			// }
			// }
			if (window.event.keyCode == 38 && tab == tabTi)
				moveFocus(obj, -1);
			if (window.event.keyCode == 40 && tab == tabTi)
				moveFocus(obj, 1);
			if (window.event.keyCode == 37 && tab == tabTi) {
				event.returnValue = false;
				event.cancelBubble = true;
			}
			// movetToLeft(obj)
			if (window.event.keyCode == 39 && tab == tabTi) {
				event.returnValue = false;
				event.cancelBubble = true;
			}
			if (window.event.keyCode == 16 && tab == tabTi) {
				isShiftKeyDown=true;  
			}
			// movetToRight(obj)

		} catch (ex) {
		}
	};

	// 内部方法，判断当前控件是否是最后第二行的最后一个
	this.IsLast = IsLast;
	function IsLast(obj) {
		if (obj == null)
			return;
		var otd = obj.parentElement;
		if (otd == null || otd.tagName != "TD")
			return false;
		var otr = tabTi.rows[tabTi.rows.length - 2];
		if (otr == null || otr.tagName != "TR")
			return false;
		if (otr.cells[otr.cells.length - 1] == otd)
			return true;
		else
			return false;
	}

	// 左右键移动焦点
	this.moveFocusUd = moveFocusUd;
	function moveFocusUd(obj, par) {
		if (obj == null)
			return;
		var otd = obj.parentElement;
		if (otd == null || otd.tagName != "TD")
			return false;
		var otr = otd.parentElement;
		if (otr == null || otr.tagName != "TR")
			return false;
		var trindex = otr.rowIndex;
		var tdindex = countOT(otd);
		try {
			if (tdindex + par > otr.cells.length - 1 || tdindex + par < 0)
				return;
			var ntd = otr.cells[tdindex + par];
			if (ntd.children[0] != null)
				ntd.children[0].focus();
			// delightLine(currentLine);
			// lightLine(ntr);
			// currentLine=otr;
		} catch (ex) {
			alert(flexgrid_msg_1 + ex.description);
		}
	}
	// add by yao
	this.movetToRight = movetToRight;
	function movetToRight(obj) {
		if (obj == null)
			return;
		var otd = obj.parentElement;
		if (otd == null || otd.tagName != "TD")
			return false;
		var otr = otd.parentElement;
		if (otr == null || otr.tagName != "TR")
			return false;
		var index = parseInt(otd.getAttribute("index")) + parseInt(1);
		var objNext = null;// otr.cells[i]
		var tdindex = countOT(otd);
		var trindex = otr.rowIndex;
		var i;
		for (i = tdindex; i < otr.cells.length; i++) {
			var objTemp = otr.cells[i];
			if (objTemp.display != "none"
					&& objTemp.getAttribute("index") == index) {
				objNext = otr.cells[i];
				break;
			}
		}
		// alert(otd==otr.cells[otr.cells.length-1]);
		if (otd == otr.cells[otr.cells.length - 2]) {
			if (trindex == tabTi.rows.length - 1) {
				// 增加行
				addRow();
			}

			var nobj = findFTd(otr.nextSibling).children[0];
			if (nobj != null)
				nobj.focus();
			return;
		}

		objNext.children[0].focus();
	}
	this.movetToLeft = movetToLeft;
	function movetToLeft(obj) {

		if (obj == null)
			return;
		var otd = obj.parentElement;
		if (otd == null || otd.tagName != "TD")
			return false;
		var otr = otd.parentElement;
		if (otr == null || otr.tagName != "TR")
			return false;
		var index = parseInt(otd.getAttribute("index")) - parseInt(1);
		var objNext = otr.cells[i];
		var tdindex = countOT(otd);
		var trindex = otr.rowIndex;
		var i;

		for (i = tdindex; i >= 0; i--) {
			var objTemp = otr.cells[i];
			if (parseInt(objTemp.getAttribute("index")) <= index) {
				objNext = otr.cells[i];
				break;
			}
		}
		if (i < 0) {
			if (trindex == 1) {
				// 在第一行不移动
				return;
			}
			var nobjtr = otr.parentElement.rows[trindex - 1];
			var nobj = nobjtr.children[nobjtr.cells.length - 1].children[0];
			if (nobj != null)
				nobj.focus();
			return;
		}
		// alert(objNext.index)
		objNext.children[0].focus();
		// alert("")
	}
	// 上下键移动焦点
	this.moveFocus = moveFocus;
	function moveFocus(obj, par) {

		if (obj == null)
			return;
		var otd = obj.parentElement;
		if (otd == null || otd.tagName != "TD")
			return false;
		var otr = otd.parentElement;
		if (otr == null || otr.tagName != "TR")
			return false;
		var trindex = otr.rowIndex;
		// var tdindex=countOT(otd);
		var tdindex = otd.cellIndex;// countOT(otd);
		try {
			if (trindex + par >= tabTi.rows.length)
				return;
			var ntr = tabTi.rows[trindex + par];
			var ntd = ntr.cells[tdindex];
			if (trindex == 1 && par == -1) {
				// alert(tabTi.rows[1].cells[tdindex].children[0].type);
				tabTi.rows[1].cells[tdindex].children[0].focus();
				tabTi.rows[1].cells[tdindex].children[0].checked = true;
				delightLine(currentLine);
				lightLine(tabTi.rows[1]);
				currentLine = tabTi.rows[1];
				return;
			}
			delightLine(currentLine);
			lightLine(ntr);
			currentLine = ntr;
			ntd.children[0].focus();
		} catch (ex) {
			// alert(flexgrid_msg_1+ex.description);
		}
	}
	// 返回TD是当前行的第几个，从0开始，防止隐藏列的问题
	this.countOT = countOT;
	function countOT(otd) {
		if (otd == null || otd.tagName != "TD")
			return;
		for ( var i = 0; i < otd.parentElement.cells.length; i++) {
			var td = otd.parentElement.cells[i];
			if (td == otd)
				return i;
		}

	}
	// 返回TR的第 一个可获得焦点的td，从0开始，防止隐藏列的问题
	this.findFTd = findFTd;
	function findFTd(otr) {
		if (otr == null || otr.tagName != "TR")
			return;
		for ( var i = 0; i < otr.cells.length; i++) {
			var td = otr.cells[i];
			if (td == null)
				continue;
			if (td.style.display == "none")
				continue;
			else
				return td;
		}

	}
	// 为表体增加一行
	this.addLine = addLine;
	function addLine() {
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		var tr = tabTi.rows[0];
		if (tr != null) {
			var indexBase = getBaseIndex();
			try {
				tabTi.rows[0].style.display = "block";
				var ntr = tabTi.insertRow();
				ntr.className = tabTi.rows[0].className;
				// var otd=ntr.insertCell();
				// otd.innerHTML=tabTi.rows[0].cells[0].innerHTML;
				// otd.width=tabTi.rows[0].cells[0].width;
				// otd.className=tabTi.rows[0].cells[0].className;
				// otd.style.display=tabTi.rows[0].cells[0].style.display;
				// otd.setAttribute("style",tabTi.rows[0].cells[0].getAttribute("style"));
				for ( var i = 0; i < tabTi.rows[0].cells.length; i++) {
					var otd = ntr.insertCell();
					otd.innerHTML = tabTi.rows[0].cells[i].innerHTML;
					otd.width = tabTi.rows[0].cells[i].width;
					otd.index = tabTi.rows[0].cells[i].index;
					otd.style.display = tabTi.rows[0].cells[i].style.display;
					otd.setAttribute("style", tabTi.rows[0].cells[i]
							.getAttribute("style"));
				}
				// 最右一列没有右边线
				ntr.lastChild.style.borderRight = "0";
				// 为左边固定列增加一行
				if (divTiLeftCol != null && divTiLeftCol.children[0] != null) {
					var tableft = divTiLeftCol.children[0];
					var row = tableft.insertRow();
					var otd = row.insertCell();
					var rowindex = row.rowIndex;
					otd.innerHTML = "<span>" + (rowindex + indexBase)
							+ "</span>";
				}
				if (currentLine != null)
					delightLine(currentLine);
				// currentLine=ntr;
				currentLine = tabTi.rows[tabTi.rows.length - 1];
				latd2 = tabTi.rows[tabTi.rows.length - 1];// 倒算第二行
				lightLine(latd2);
				var fixView = currentLine.offsetTop + currentLine.offsetHeight
						- divTi.scrollTop - divTi.clientHeight
						+ divSum.offsetHeight;
				if (!isNaN(parseInt(divTi.style.paddingTop)))
					fixView += parseInt(divTi.style.paddingTop);
				if (fixView > 0)
					divTi.scrollTop += fixView;
				// tabTi.rows[0].setAttribute("IsHidden","1");
				divAll.scrollTop = divAll.scrollHeight;
				// 重新计算divSum的top
				// divSum.style.top=parseInt(divTi.offsetTop)+parseInt(divTi.offsetHeight)-(parseInt(divTi.style.height)-divTi.clientHeight)-curTrHeight;
			} catch (ex) {
				alert(flexgrid_msg_2 + ex.description);
				return;
			}
		}
	}
	this.insertRow=insertRow;
	function insertRow(rowIndex){
		if(rowIndex<=0||rowIndex>tabTi.rows.length)return;
		if(tabTi==null  || tabTi.tagName!="TABLE") return;
		var tr=tabTi.rows[0];
		if(tr!=null){
			try{
				tabTi.rows[0].style.display="block";
				// 在点击图片的下一行添加新行
				var ntr=tabTi.insertRow(rowIndex);
				// 判断是否有输入height，如果没有输入，默认是60高度
				if(gridObj.GRIDINFO.openHeight==undefined){
					tabTi.rows[rowIndex].height="60";
				}
				else{
					tabTi.rows[rowIndex].height=gridObj.GRIDINFO.openHeight;
				}
				//在新行里添入td
				var newTd=tabTi.rows[rowIndex].insertCell();
				// 当flexCol中有加样式display:none时，累加列
				var cellLen=tabTi.rows[0].cells.length;
				var nodisplay=0;
				for(var i=2;i<cellLen;i++){
					if(tabTi.rows[0].cells[i].style.display=="none"){
						nodisplay++;
					}
				}
				//如果有左侧，colspan比没有左侧多1，colSpan减去display：none
				if(gridObj.GRIDINFO.hasLeft=="true"){
					newTd.colSpan=columnNames.length-nodisplay+2;
				}
				else{	
					newTd.colSpan=columnNames.length-nodisplay+1;
				}
				//创建div
				var divObj=document.createElement("div");
				var rowObj=tabTi.rows[rowIndex-1];
				// 在td中添入div
				newTd.appendChild(divObj);
				divObj.innerText="自定义内容";
				// 取得div对象和当前行的对象，给页面回调函数作为参数
				overrideDiv(divObj,rowObj);
				// 在增加表体的同时，增加左侧固定列的高度
				if (divTiLeftCol != null && divTiLeftCol.children[0] != null) {
					//取得左侧固定列的table对象
					var tableft = divTiLeftCol.children[0];
					// 增加tr
					var row = tableft.insertRow(rowIndex);
					// 增加行的高度
					row.style.height=gridObj.GRIDINFO.openHeight;
					// 加入td
					var otd = row.insertCell();
					// 增加td高度把tr的高度撑开
					otd.style.height=gridObj.GRIDINFO.openHeight;
					// 与普通左侧列的dom内容保持一致
					otd.innerHTML = "<span></span>";
				}
			}
			catch(ex)
			{
				alert(flexgrid_msg_2+ex.description);
				return;
			}
		}
		
	}
	// 为表体增加高度
	this.addHeight = addHeight;
	function addHeight() {
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		try {
			// divTi.style.height=parseInt(curDivHeight)+parseInt(curTrHeight)+"px";
			curDivHeight = parseInt(curDivHeight) + parseInt(curTrHeight);
		} catch (ex) {
			// alert(flexgrid_msg_3);
			return;
		}
	}
	// 为表体删除一行，不高亮当前行
	this.delLine = function() {
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		try {
			if (currentLine != null) {
				var oldTr = currentLine.rowIndex;
				var isdel = currentLine.removeNode(true);
				// var otr=this.findCurrentLine(oldTr);
				// var isdel=currentLine.removeNode(true);
				// var isdel=otr.removeNode(true);
				currentLine = null;
				// 为左边固定列删除一行
				// arrangeRows();
				if (divTiLeftCol != null && divTiLeftCol.children[0] != null) {
					var tableft = divTiLeftCol.children[0];
					if (tableft.rows.length > 0)
						tableft.rows[tableft.rows.length - 1].removeNode(true);
				}
				divTou.style.width = divTi.clientWidth;
				divSum.style.width = divTi.clientWidth;
			}
		} catch (ex) {
			// alert(flexgrid_msg_4 + ex.description);
			return;
		}
	};
	// 为当前的表体各行排左侧的序号
	this.arrangeRows = arrangeRows;
	function arrangeRows() {
		var j = 1;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			var trow = tabTi.rows(i);
			if (trow.style.display != "none")
				trow.cells[0].children[0].innerText = j++;
		}
	}
	// 接口，添加行
	this.addRow = addRow;
	function addRow() {
		addLine();
		// 由于出现了滚动条,重新给宽度赋值
		divTiLeftCol.style.height = divTi.clientHeight - 21;
		divTou.style.width = divTi.clientWidth;
		divSum.style.width = divTi.clientWidth;
	}
	// 删除行
	this.delRow = delRow;
	function delRow() {
		// 比较deleteCol的值
		try {
			if (tabTi == null || tabTi.tagName != "TABLE")
				return;
			if (tabTi.rows.length == 1)
				return;

			if (currentLine == null)
				return;
			//
			this.delLine();

		} catch (ex) {
		}
		// 若表体不出现滚动条，则表头的宽度变大
		divTou.style.width = divTi.clientWidth;
		divSum.style.width = divTi.clientWidth;
	}

	// 左右滚动表格体，表格头移动
	this.doscroll = doscroll;
	function doscroll() {
		if (divTi == null)
			return;
		if (divTou == null)
			return;
		if (divSum == null)
			return;
		var disWid = divTi.scrollLeft;
		divTou.scrollLeft = disWid;
		divTiLeftCol.scrollTop = divTi.scrollTop;
		divSum.scrollLeft = disWid;
	}
	// 表格头鼠标按下事件
	this.domousedown = domousedown;
	function domousedown() {
		var obj = window.event.srcElement;
		var nextTd = null;
		if (obj == null || (obj.tagName != "SPAN" && obj.tagName != "TD"))
			return;
		if (obj.tagName == "SPAN")
			obj = obj.parentElement;
		nextTd = obj.previousSibling;// 前一个TD
		while (nextTd != null && nextTd.style.display == "none")
			nextTd = nextTd.previousSibling;// 前一个TD nextTd有可能为null
		var absLeft = 0;
		var odiv = divAll;
		while (odiv != document.body) {
			absLeft += odiv.offsetLeft;
			odiv = odiv.offsetParent;
		}
		// alert(absLeft+"**");
		if (window.event.clientX + document.body.scrollLeft + divTou.scrollLeft > absLeft
				+ obj.offsetLeft + obj.offsetWidth - 13) {
			curTd = obj;
			// 最后一个单元格不能拖动
			if (curTd.nextSibling == null)
				return;
			tabTou.setCapture(false);
			isMouDown = true;
			isCanSort = false;
			OldPlace = window.event.clientX;
		} else if (window.event.x + document.body.scrollLeft
				+ divTou.scrollLeft >= absLeft + obj.offsetLeft
				&& window.event.x + document.body.scrollLeft
						+ divTou.scrollLeft < absLeft + obj.offsetLeft + 13) {
			if (nextTd == null)
				return;
			else if (nextTd.cellIndex == 0
					&& divTouLeftCol.style.display != "none")
				return;
			tabTou.setCapture(false);
			curTd = nextTd;
			isMouDown = true;
			isCanSort = false;
			OldPlace = window.event.clientX;
		} else
			isCanSort = true;
		if (isMouDown) {
			var otp = curTd;
			var otl = curTd.offsetWidth;
			while (otp != divAll) {
				otl += otp.offsetLeft;
				otp = otp.offsetParent;
			}
			var odiv = divAll;
			var odl = 0;
			while (odiv.tagName != "BODY") {
				odl += odiv.offsetLeft;
				odiv = odiv.offsetParent;
			}
			fix = event.clientX + document.body.scrollLeft - odl - otl;
			curHr.style.height = divTi.clientHeight;
			curHr.style.left = otl - divTou.scrollLeft;
			curHr.style.display = "block";
		}
	}

	// 表格头鼠标移动事件
	this.domousemove = domousemove;
	function domousemove() {
		// document.selection.empty();
		var absLeft = 0;
		var odiv = divAll;
		while (odiv != document.body) {
			absLeft += odiv.offsetLeft;
			odiv = odiv.offsetParent;
		}
		if (isMouDown && curHr.style.display == "block") {
			var hrPos = event.clientX + document.body.scrollLeft - absLeft
					- fix - divTou.scrollLeft;
			if (hrPos > divTouLeftCol.offsetWidth)
				curHr.style.left = hrPos;
		}
		var obj = window.event.srcElement;
		if (obj == null || !tabTou.contains(obj)
				|| (obj.tagName != "SPAN" && obj.tagName != "TD"))
			return;
		if (obj.tagName == "SPAN") {
			obj = obj.parentElement;
		}
		if ((window.event.clientX + document.body.scrollLeft
				+ divTou.scrollLeft > absLeft + obj.offsetLeft
				+ obj.offsetWidth - 13 && obj.nextSibling != null)
				|| (window.event.clientX + document.body.scrollLeft
						+ divTou.scrollLeft >= absLeft + obj.offsetLeft && window.event.clientX
						+ document.body.scrollLeft + divTou.scrollLeft < absLeft
						+ obj.offsetLeft + 13) || isMouDown) {
			try {
				// 最后一格单元格不能拖动
				if ((!(divTouLeftCol.style.display != "none" && obj.cellIndex == 1))
						&& (!(divTouLeftCol.style.display == "none" && obj.cellIndex == 0)))
					divTou.style.cursor = "col-resize";
			} catch (ex) {
				divTou.style.cursor = "move";
			}
		} else {
			// 最下一行可排序，为手型
			if ((obj.parentElement.rowIndex == tabTou.rows.length - 1)
					&& obj.nextSibling != null)
				divTou.style.cursor = "hand";
			else
				divTou.style.cursor = "default";
		}
	}
	// 表格头鼠标事件
	this.domouseup = domouseup;
	function domouseup() {
		divTou.style.cursor = "default";
		if (isMouDown) {
			try {
				nowPlace = window.event.clientX;
				if (isNaN(OldPlace - nowPlace)) {
					isMouDown = false;
					return;
				}
				var ctr = curTd.parentElement;
				for ( var i = 0, cel = ctr.cells[0].colSpan - 1; i < ctr.cells.length
						&& ctr.cells[i] != curTd; cel += ctr.cells[++i].colSpan)
					;
				if (divTouLeftCol.style.display == "none")
					--cel;
				// 改变相应的COL的宽度
				var tCol = tabTou.children[0].all(cel);
				var tWidth = tCol.offsetWidth - (OldPlace - nowPlace);
				if (tWidth < 1)
					tWidth = "1px";
				tCol.style.width = "";
				tCol.width = tWidth;
				isMouDown = false;
				// 由于存在隐藏列，所以需要获取实际要变宽的那一列，即j列
				var firstRow = tabTi.rows[0];
				for ( var j = 0; firstRow.cells[j].cellIndex != cel; j++)
					;
				// 表体变化时对每行的相应td赋值，而不是计算，否则会有异常
				for ( var i = 0; i < tabTi.rows.length; i++) {
					var otd1 = tabTi.rows[i].cells[j];
					if (otd1 != null) {
						otd1.width = tWidth;
						otd1.style.width = "";
					}
				}
				// 合计区只有一行，所以可以不用循环
				var otd = tabSum.rows[0].cells[0];
				while (otd.cellIndex != cel)
					otd = otd.nextSibling;
				if (otd != null) {
					otd.width = tWidth;
					otd.style.width = "";
				}
				// 合计区多行的情况还需要进一步设计
				// for(var i=0;i<tabSum.rows.length;i++)
				// {
				// var otd=tabSum.rows[i].cells[cel];
				// if(otd!=null)
				// otd.width=tWidth;
				// }
				// 用触发resize事件来替代原来的调整
				divAll.fireEvent("onresize");
				curHr.style.display = "none";
				isMouDown = false;
				tabTou.releaseCapture();
			} catch (ex) {
				isMouDown = false;
				tabTou.releaseCapture();
				// alert(ex.description);
				alert(flexgrid_msg_5);
			}
		}
	}
	// 表格头鼠标离开事件
	this.domouseout = domouseout;
	function domouseout() {
		document.body.style.cursor = "default";
		// isMouDown=false;
	}
	// isSubmit的SET方法
	this.setSubmit = setSubmit;
	function setSubmit(val) {
		isSubmit = val;
	}
	
	//isMultOrder的SET方法
	this.setMultOrder = setMultOrder;
	function setMultOrder(val){
		isMultOrder = val;
	}
	
	// 表格头单击事件
	this.doTabTouClick = doTabTouClick;
	function doTabTouClick() {
		var obj = window.event.srcElement;
		// alert(obj.tagName);
		if (obj.tagName != "SPAN" && obj.tagName != "INPUT")
			return;
		if (obj.parentElement.parentElement.rowIndex != tabTou.rows.length - 1)
			return;
		if (obj.tagName != "INPUT")
			if (obj.innerText == "")
				return;
		var objcheckAll = obj.getAttribute("enableCheckAll");
		// 如果是数值类型可能有逗号之类的分割符号
		this.format = obj.getAttribute("format");
		if (obj.tagName == "INPUT") {
			if (obj.checked)
				doCheckAllLine(true, obj.parentElement.cellIndex);
			else
				doCheckAllLine(false, obj.parentElement.cellIndex);
		} else if (objcheckAll == "true") {
			if (obj.children[0].innerText == "6") {
				doCheckAllLine(true, obj.parentElement.cellIndex);
				obj.children[0].innerText = "5";
				obj.children[0].style.display = "block";
			} else {
				doCheckAllLine(false, obj.parentElement.cellIndex);
				obj.children[0].innerText = "6";
				obj.children[0].style.display = "block";
			}
		} else {
			if(tabRowLen<tabTi.rows.length){
				alert(flexgrid_msg_9);
				return;
			}
			if (outCanSort != true)
				return;
			var objcansort = obj.getAttribute("CanSort");
			if (objcansort == "false")
				return;
			if(isMultOrder)//如果isMultOrder为true，表示当前进行多列排序，点击列表头不会进行那个排序。
				return;
			if (isSubmit) {
				if (!isCanSort)
					return;
				if (inputSort != null && inputSortName != null) {
					var sort = inputSort.value;
					var sortname = inputSortName.value;
					if (obj.tagName == "SPAN") {
						var attname = obj.getAttribute("sortName");
						if (attname == sortname) {
							if (sort.toUpperCase() == "DESC") {
								inputSort.value = "ASC";
								obj.children[0].innerText == "6";
								obj.children[0].style.display = "block";
							} else {
								inputSort.value = "DESC";
								obj.children[0].innerText == "5";
								obj.children[0].style.display = "block";
							}
						} else {
							inputSortName.value = attname;
							inputSort.value = "ASC";
							obj.children[0].innerText == "5";
							obj.children[0].style.display = "block";
						}
					}
					if (document.forms[0] != null)
						document.forms[0].submit();
				}
			} else {
				if (!isCanSort)
					return;
				if (obj.tagName == "SPAN") {
					if (lastSortTd != null)
						if(lastSortTd.children[0]!=null)
						lastSortTd.children[0].style.display = "none";

					if (obj.children[0].innerText == "6") {
						obj.children[0].innerText = "5";
						obj.children[0].style.display = "inline";
						orderType = "0";
					} else {
						obj.children[0].innerText = "6";
						obj.children[0].style.display = "inline";
						orderType = "1";
					}
					lastSortTd = obj;
					obj = obj.parentElement;
				}
				if (obj.tagName == "LABEL") {
					if (obj.innerText == "6") {
						obj.innerText = "5";
						orderType = "0";
					} else {
						obj.innerText = "6";
						orderType = "1";
					}
					obj = obj.parentElement.parentElement;
				}
				if (obj.tagName != "TD")
					return;
				var colindex = countOT(obj);// .cellIndex;
				var firstRow = tabTi.rows[0];
				for ( var j = 0; firstRow.cells[j].cellIndex != colindex; j++)
					;
				if (tableArray[0] == null)
					makePlanar();
				sortArray(j - 1);
				fillData();
				// 大数据量异步解析清除缓存数据
				if(gridObj.GRIDINFO.isLarge=="true")
					tableArray=new Array();
				this.format = "";
				// 前台排序需要重新编排序号
				arrangeRows();
			}
		}
	}

	// 调整页面大小
	this.doresize = doresize;
	function doresize() {
		
		//解决当divTi的滚动条为auto时，当出现滚动条时，合计行会覆盖最后一行数据的问题，在table下增加一个div
		//当没有合计行时，div的宽度为0
		
		if(divSum.style.display=="none"){
			document.getElementById("divLast").style.width=0;
			divTiLeftCol.style.height=divTi.clientHeight;
		}else{
			//当有合计行时，设置div的宽度，使之占据一行的位置，这时出现滚动条后就会覆盖这个div。 
			document.getElementById("divLast").style.width=1;
			divTiLeftCol.style.height=divTi.clientHeight-21;
		}
		
		
		// 调整页面大小带来的头，sum表格的宽度的变化
		divTi.style.height = divAll.clientHeight;
		divTou.style.width = parseInt(divTi.clientWidth, 10);
		divSum.style.width = parseInt(divTi.clientWidth, 10);
		if (gridObj.GRIDINFO.isWrap == "true") {
			var leftTable = divTiLeftCol.children[0];
			for ( var i = 1; i < leftTable.rows.length; i++) {
				leftTable.rows[i].cells[0].style.height = tabTi.rows[i].cells[0].clientHeight + 1;
			}
		}
		// divTiLeftCol.style.height=divTi.clientHeight-21;
		// alert("divTi.offsetTop:"+divTi.offsetTop+"divTi.clientHeight:"+divTi.clientHeight+"divSum.clientHeight:"+divSum.clientHeight);
		var fixt = 1;
		if (tabTi.scrollWidth == divTi.clientWidth)
			fixt = 2;
		// 高度调整已经在init中执行
		/* divTiLeftCol.style.height=divTi.clientHeight-20; */
		// divTiLeftCol.style.height=divTi.clientHeight;
		divSum.parentElement.style.top = parseInt(divTi.offsetTop)
				+ parseInt(divTi.clientHeight)
				- (parseInt(divSum.clientHeight)) + 1;

		// divSum.parentElement.style.top=parseInt(divTi.offsetTop)+parseInt(divTi.clientHeight)-(parseInt(divSum.clientHeight))+fixt;
		// 横向滚动条会比较帅一些
		// divSum.parentElement.style.top=parseInt(divTi.offsetHeight,10)-parseInt(divSum.parentElement.offsetHeight,10)-(divTi.offsetWidth>=divTi.scrollWidth?0:17);
	}
	// 表格的双击事件。
	this.dblclickEvent = dblclickEvent;
	function dblclickEvent() {

	}
	//
	// 焦点到第row行，第col列
	this.focus = focus;
	function focus(row, col) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		delightLine(currentLine);
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		var otr = tabTi.rows[row];
		if (otr == null)
			return;
		if (otr.style.display != "none" && otr.rowIndex != 0
				&& otr.rowIndex != tabTi.rows.length) {
			lightLine(otr);
			currentLine = otr;
			try {
				var otd = otr.cells[col];
				if (otd != null && otd.children[0] != null)
					otd.children[0].focus();
			} catch (ex) {
				// alert(flexgrid_msg_6);
			}
		}
	}
	// 通过列名获取index
	this.getIndexByName = getIndexByName;
	function getIndexByName(name) {
		if (name == null || name == "") {
			// alert(flexgrid_msg_8);
			return -1;
		}
		for ( var i = 0; i < columnNames.length; i++) {
			if (name == columnNames[i]) {
				return i + 1;
			}
		}
		// alert(flexgrid_msg_8);
		return -1;
	}
	// 设置第row行，第col列值
	this.setCellValue = setCellValue;
	function setCellValue(row, col, value) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		var otr = tabTi.rows[row];
		if (otr == null)
			return;
		if (otr.style.display != "none") {
			try {
				var index = 0;
				for ( var i = 1; i < otr.cells.length; i++) {
					var otd = otr.cells[i];
					// if(otd.style.display!="none")
					// index++;
					if (i == col) {
						if (otd.children[0] != null
								&& otd.children[0].tagName == "INPUT")
							otd.children[0].value = value;
						else
							otd.innerText = value;
					}
				}

			} catch (ex) {
				return null;
			}
		}
	}
	// 取得第row行，第col列值
	//edit by duyong 2010-5-31  增加参数hasTree，以解决当表格中有树时getCellValue方法不能使用的问题
	this.getCellValue = getCellValue;
	function getCellValue(row, col, hasTree) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		var otr = tabTi.rows[row];
		if (otr == null)
			return;
		if (hasTree || otr.style.display != "none") {
			try {
				var index = 0;
				for ( var i = 1; i < otr.cells.length; i++) {
					var otd = otr.cells[i];
					// if(otd.style.display!="none")
					// index++;
					if (i == col) {
						if (otd.children[0] != null
								&& otd.children[0].tagName == "INPUT")
							return otd.children[0].value;
						return otd.innerText;
					}
				}

			} catch (ex) {
				return null;
			}
		}
	}
	// 取得第row行，第col列的TD
	//edit by duyong 2010-5-31  增加参数hasTree，以解决当表格中有树时getCell方法不能使用的问题
	this.getCell = getCell;
	function getCell(row, col, hasTree) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		var otr = tabTi.rows[row];
		if (otr == null)
			return;
		if (hasTree || otr.style.display != "none") {
			try {
				var index = 0;
				for ( var i = 1; i < otr.cells.length; i++) {
					var otd = otr.cells[i];
					// if(otd.style.display!="none")
					// index++;
					if (i == col) {
						return otd;
					}
				}

			} catch (ex) {
				return null;
			}
		}

	}

	// 返回TabTi的总行数
	this.returnRowCount = returnRowCount;
	function returnRowCount() {
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		return tabTi.rows.length - 1;

	}
	// 返回某列的合计值
	this.sum = sum;
	function sum(col) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (col == "")
			return;
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		var sum = 0;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			if (tabTi.rows[i].style.display == "none")
				continue;
			var otd = tabTi.rows[i].cells[col];
			if (otd != null && otd.children[0] != null) {
				var val = parseFloat(otd.children[0].innerText);
				if (isNaN(val))
					val = 0;
				sum += val;
			}
		}
		return sum;
	}

	// 设置合计区域单元格的值
	this.setSumValue = setSumValue;
	function setSumValue() {
		if (tabSum == null && tabSum.tagName != "TABLE")
			return;
		if (tabTi == null || tabTi.tagName != "TABLE")
			return;
		try {
			for ( var i = 1; i < tabTi.rows[0].cells.length; i++) {
				var sum = 0;
				for ( var j = 1; j < tabTi.rows.length; j++) {
					var col = tabTi.rows[j].cells[i];
					if (col == null)
						continue;
					var val = col.innerText;
					// 如果数字有分割符号的话，应做相应处理
					var head_Cell = tabTou.rows[0].cells[i];
					if (head_Cell && head_Cell.children[0]
							&& head_Cell.children[0].format) {
						var num_foramt = head_Cell.children[0].format;
						// 排序前去除分割符号
						if (num_foramt != "" && num_foramt.indexOf("#") != -1) {
							var num_separator = num_foramt.substring(num_foramt
									.indexOf("#") + 1);
							var reg_Exp = new RegExp(num_separator, "g");
							val = val.replace(reg_Exp, "");
						}
					}
					if (isNaN(val) || val == "")
						continue;
					// sum+=parseFloat(val);
					sum += val.valueOf;
				}
				if (sum != 0 && !isNaN(sum))
					tabSum.rows[0].cells[i].innerHTML = "<span>" + sum
							+ "</span>";
			}
		} catch (ex) {
			alert(flexgrid_msg_7);
		}

	}

	// 获得合计区域单元格的值
	this.getSumFieldValue = getSumFieldValue;
	function getSumFieldValue(col) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		col--;
		if (col > 0 && col < gridObj.TabSum.rows[0].cols.length)
			return gridObj.TabSum.rows[0].cols[col].text;
	}

	this.getCurrentLine = getCurrentLine;
	function getCurrentLine() {
		if (currentLine == null)
			return null;
		return currentLine.rowIndex;
	}
	// 点击checkbox时
	this.doCheck = doCheck;
	function doCheck(obj) {
		if (obj != null && obj.type == "checkbox") {
			if (inputPk == null)
				return;
			var str = inputPk.value;
			if (obj.checked) {
				if (str.indexOf(obj.value) > -1) {
					restr = new RegExp("," + obj.value);
					str = str.replace(restr, "");
				}
			} else {
				str += "," + obj.value;
			}
			inputPk.value = str;
		}
		if (obj != null && obj.type == "radio") {
			if (inputPk == null)
				return;
			inputPk.value = obj.value;
		}

	}
	// 得到选中的行的复选框的value,用","分割
	this.getCheckLine = getCheckLine;
	function getCheckLine(flag) {
		if (flag == null || flag == "")
			flag = ",";
		if (tabTi == null || tabTi.tagName != "TABLE")
			return "";
		try {
			var col = null;
			for ( var i = 0; i < tabTi.rows[1].cells.length; i++) {
				var otd = tabTi.rows[1].cells[i].children[0];
				if (otd != null
						&& (otd.type == "checkbox" || otd.type == "radio")) {
					col = i;
					break;
				}
			}
			if (col == null || isNaN(col))
				return "";
			var str = "";
			for ( var j = 1; j < tabTi.rows.length; j++) {
				if(tabTi.rows[j].cells.length==1)continue;
				if (tabTi.rows[j].cells[col].children[0].tagName == "INPUT") {
					if (tabTi.rows[j].cells[col].children[0].checked)
						str += tabTi.rows[j].cells[col].children[0].value
								+ flag;
				}
			}

			return str.substring(0, str.length - 1);
		} catch (ex) { // alert(ex.description);
			return "";
		}
	}
	// 得到选中的行所有列的值 ，列之间使用fleg1分割 ，行之间使用flag2分割，includeHidCol 判断是否包含隐藏列 ，可以执行开始和结束列号
	this.getCheckAll = getCheckAll;
	function getCheckAll(flag1, flag2, includeHidCol, beginColIndex,
			endColIndex) {
		if (flag1 == null || flag1 == "")
			flag1 = ",";
		if (flag2 == null || flag2 == "")
			flag2 = "|";
		if (tabTi == null || tabTi.tagName != "TABLE")
			return "";
		try {
			var col = null;
			for ( var i = 0; i < tabTi.rows[1].cells.length; i++) {
				var otd = tabTi.rows[1].cells[i].children[0];
				if (otd != null
						&& (otd.type == "checkbox" || otd.type == "radio")) {
					col = i;
					break;
				}
			}
			if (col == null || isNaN(col))
				return "";
			var str = "";
			for ( var j = 1; j < tabTi.rows.length; j++) {
				if (tabTi.rows[j].cells[col].children[0].tagName == "INPUT") {
					if (tabTi.rows[j].cells[col].children[0].checked) {
						var k = 1;
						var endlp = tabTi.rows[j].cells.length;
						if (beginColIndex != null && endColIndex != null
								&& beginColIndex > 0
								&& endColIndex >= beginColIndex
								&& endColIndex < endlp) {
							k = beginColIndex;// 包含开始列
							endlp = endColIndex + 1;// 包含结束列
						}
						for (k; k < endlp; k++) {
							var ootd = tabTi.rows[j].cells[k];
							if (includeHidCol != null && !includeHidCol) {
								var isDisplay = ootd.children[0].style.display;
								if (isDisplay != null
										&& isDisplay.toUpperCase() == "NONE")
									continue;
							}
							if (ootd.children[0] != null) {
								var val = "";
								if (ootd.children[0].tagName == "INPUT")
									val = ootd.children[0].value;
								else
									val = ootd.children[0].innerText;
								if (val == null)
									val = "";
								str += val + flag1;
							} else {
								var val = ootd.innerText;
								if (val == null) {
									val = "";
								}
								str += val + flag1;
							}
						}
						str += flag2;
					}
				}
			}
			return str.substring(0, str.length - 1);
		} catch (ex) { // alert(ex.description);
			return "";
		}
	}

	// 得到选中的行的行号,用","分割
	this.getCheckLineNo = getCheckLineNo;
	function getCheckLineNo() {
		if (tabTi == null || tabTi.tagName != "TABLE")
			return "";
		try {
			var col = null;
			for ( var i = 0; i < tabTi.rows[1].cells.length; i++) {
				var otd = tabTi.rows[1].cells[i].children[0];
				if (otd != null
						&& (otd.type == "checkbox" || otd.type == "radio")) {
					col = i;
					break;
				}
			}
			if (col == null || isNaN(col))
				return "";
			var str = "";
			for ( var j = 1; j < tabTi.rows.length; j++) {
				if (tabTi.rows[j].cells[col].children[0].checked)
					str += j + ",";
			}

			return str.substring(0, str.length - 1);
		} catch (ex) { // alert(ex.description);
			return "";
		}
	}
	// 选中当前所有行
	this.doCheckAllLine = doCheckAllLine;
	function doCheckAllLine(isCheck, colIndex) {
		var col = -1;
		if (tabTi == null || tabTi.tagName != "TABLE")
			return "";
		try {
			for ( var i = 0; i < tabTi.rows[1].cells.length; i++) {
				var otd = tabTi.rows[1].cells[i];
				if (otd.style.display != "none") {
					col++;
				}
				if (col == colIndex) {
					col = i;
					break;
				}
			}
			if (col == null || isNaN(col))
				return false;
			var str = "";
			if (isCheck) {
				for ( var j = 1; j < tabTi.rows.length; j++) {
					if(tabTi.rows[j].cells.length==1)
						continue;
					if (tabTi.rows[j].cells[col].children[0].disabled != true)
						tabTi.rows[j].cells[col].children[0].checked = true;
				}

				return true;
			} else {
				for ( var j = 1; j < tabTi.rows.length; j++) {
					if(tabTi.rows[j].cells.length==1)
						continue;
					if (tabTi.rows[j].cells[col].children[0].disabled != true)
						tabTi.rows[j].cells[col].children[0].checked = false;
				}

				return true;
			}
		}

		catch (ex) { // alert(ex.description);
			return false;
		}
	}
	// 生成一个二维数组,排序使用
	this.makePlanar = makePlanar;
	function makePlanar() {
		if (tabTi == null)
			return;
		if (tableArray == null)
			tableArray = new Array();
		var rowArray;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			rowArray = new Array();
			for ( var j = 0; j < tabTi.rows[0].cells.length; j++) {
				var otd = tabTi.rows[i].cells[j];
				var otxt = "";
				/*
				 * if(otd.children[0]!=null && otd.children[0].tagName!="SPAN")
				 * otxt=otd.innerHTML; else if(otd.innerText) {
				 * otxt=otd.innerText; } else{ otxt=otd.innerHTML; }
				 */
				// rowArray[j]=otd.innerText;
				rowArray.push( {
					text : otd.innerText,
					html : otd.innerHTML
				});
				// if(j<2)
				// alert(typeof(rowArray[j]));
			}
			tableArray[i - 1] = rowArray;
		}

	}

	// 二维数组排序,index为1时升序,为0时降序
	this.sortArray = sortArray;
	function sortArray(col) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tableArray[0] == null)
			return;
		if (typeof (col) != "number")
			return;
		if (col >= tableArray[0].length)
			return;
		sortCol = col;
		tableArray.sort(sortFun);
		// alert(orderType);
		if (orderType == "1")
			tableArray.reverse();
	}

	// 向表格填充数据
	this.fillData = fillData;
	function fillData() {
		if (tableArray == null)
			return;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			for ( var j = 0; j < tabTi.rows[0].cells.length; j++) {
				if (tableArray[i - 1][j] != null && tableArray[i - 1][j] != "")
					// if(tabTi.rows[i].cells[j].children[0]!=null &&
					// tabTi.rows[i].cells[j].children[0].tagName!="INPUT")
					// tabTi.rows[i].cells[j].innerHTML="<SPAN>"+tableArray[i-1][j]+"</SPAN>"
					// else
					tabTi.rows[i].cells[j].innerHTML = tableArray[i - 1][j].html;
			}
		}

	}
	// 二维数组排序规则
	this.sortFun = sortFun;
	function sortFun(par1, par2) {
		if (typeof (par1) == "object" && typeof (par1) == "object") {
			par1 = par1[sortCol + 1];
			par2 = par2[sortCol + 1];
		}
		par1 = par1.text;
		par2 = par2.text;
		// alert(par1+","+par2);
		if (sortType == "1") {// alert(par1+""+par2);
			// 排序前去除分割符号
			if (this.format && this.format != ""
					&& this.format.indexOf("#") != -1) {
				var num_format = format.substring(format.indexOf("#") + 1);
				var reg_Exp = new RegExp(num_format, "g");
				par1 = par1.replace(reg_Exp, "");
				par2 = par2.replace(reg_Exp, "");
			}

			if (!isNaN(par1) && !isNaN(par2)) {
				if (parseFloat(par1) > parseFloat(par2))
					return 1;
				else if (parseFloat(par1) < parseFloat(par2))
					return -1;
				if (parseFloat(par1) == parseFloat(par2))
					return 0;
			}
			if (typeof (par1) == "string" && typeof (par1) == "string") {
				// 货币列
				if (par1.substring(0, 1) == "￥" && par2.substring(0, 1) == "￥") {
					if (parseFloat(par1.substring(1, par1.length)) > parseFloat(par2
							.substring(1, par2.length)))
						return 1;
					else if (parseFloat(par1.substring(1, par1.length)) < parseFloat(par2
							.substring(1, par2.length)))
						return -1;
					if (parseFloat(par1.substring(1, par1.length)) == parseFloat(par2
							.substring(1, par2.length)))
						return 0;
				}
				// alert(par1.substr(par1.length-1,1)+","+par2.substr(par2.length-1,1));
				if (par1.substr(par1.length - 1, 1) == "%"
						&& par2.substr(par2.length - 1, 1) == "%") {
					if (parseFloat(par1.substring(0, par1.length - 1)) > parseFloat(par2
							.substring(0, par2.length - 1)))
						return 1;
					else if (parseFloat(par1.substring(0, par1.length - 1)) < parseFloat(par2
							.substring(0, par2.length - 1)))
						return -1;
					if (parseFloat(par1.substring(0, par1.length - 1)) == parseFloat(par2
							.substring(0, par2.length - 1)))
						return 0;
				}
				par1 = par1.toString();
				par2 = par2.toString();
				if (par1 > par2)
					return 1;
				if (par1 < par2)
					return -1;
				if (par1 == par2)
					return 0;
			}
		} else {
			par1 = par1.toString();
			par2 = par2.toString();
			if (par1 > par2)
				return 1;
			if (par1 < par2)
				return -1;
			if (par1 == par2)
				return 0;
		}
		return 0;
	}
	// 外部set方法isCanSort
	this.setCanSort = setCanSort;
	function setCanSort(val) {
		outCanSort = val;
	}
	// 外部set方法setTouText(col,val)
	this.setTouText = setTouText;
	function setTouText(col, val) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabTou == null)
			return;
		var otd = tabTou.rows[0].cells[col];
		var ospan = otd.children[0];
		var olabel = ospan.children[0];
		ospan.innerHTML = val + olabel.outerHTML;
	}
	// 外部set方法setTouAlign(col,val)
	this.setTouAlign = setTouAlign;
	function setTouAlign(col, val) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabTou == null)
			return;
		if (col < 1 || col >= tabTou.rows[0].cells.length - 1)
			return;
		var otd = tabTou.rows[0].cells[col];
		var ospan = otd.children[0];
		ospan.style.textAlign = val;
	}
	// 设置合计区域单元格的值
	this.setSumFieldValue = setSumFieldValue;
	function setSumFieldValue(col, value) {
		if (col == null || col == "")
			return;
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabSum == null && tabSum.tagName != "TABLE")
			return;
		var otr = tabSum.rows[0];
		if (otr == null)
			return;
		if (otr.style.display != "none") {
			try {
				var otd = otr.cells[col];
				if (otd != null) {
					if (otd.children[0] != null) {
						otd.children[0].innerText = value;
					} else
						otd.innerHTML = value;
				}

			} catch (ex) {
				alert(grid_msg_9);
			}
		}

	}
	// 外部set方法setSumAlign(col,val)
	this.setSumAlign = setSumAlign;
	function setSumAlign(col, val) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabSum == null)
			return;
		var otd = tabSum.rows[0].cells[col];
		var ospan = otd.children[0];
		ospan.style.textAlign = val;
	}

	// 外部set方法setSumValueCellText(col,val),设置合计区单元格的显示
	this.setSumCellText = setSumCellText;
	function setSumCellText(col, val) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabSum == null)
			return;
		var otd = tabSum.rows[0].cells[col];
		var ospan = otd.children[0];
		ospan.innerHTML = val;
	}

	this.setTiValue = setTiValue;
	function setTiValue(row, col, val) {
		if (typeof (col) == "string") {
			col = this.getIndexByName(col);
			if (col == -1)
				return;
		}
		if (tabTi == null)
			return;
		var otdc = tabTi.rows[row].cells[col].children[0];
		if (otdc.tagName == "SPAN")
			otdc.innerText = val;
		else if (otdc.tagName == "INPUT")
			otdc.value = val;
		else
			return;
	}

	// 外部set方法setSumText(col,val),只改变"合计"
	this.setSumText = setSumText;
	function setSumText(val) {
		if (divSum == null)
			return;
		var divsumnext = divSum.nextSibling.children[0];
		if (divsumnext == null)
			return;
		divsumnext.children[0].rows[0].cells[0].innerText = val;

	}
	// 外部set方法setTabTiClick
	this.setTabTiClick = setTabTiClick;
	function setTabTiClick(evevtstr) {
		if (tabTi == null)
			return;
		if (evevtstr == null || evevtstr == "")
			return;
		tabTi.attachEvent("onclick", evevtstr);
	}
	// 点击展开下级的＋－图片
	this.doOpenChildImgClick = doOpenChildImgClick;
	function doOpenChildImgClick() {
		OpenImg = gridObj.GRIDINFO.openFlag;
		CloseImg = gridObj.GRIDINFO.closeFlag;
		var img = window.event.srcElement;
		if (img.tagName != "IMG")
			return;
		var nodeid = img.nodeid;
		if (img.open != "1") {
			var rowIndex=0;
			// 根据当前点击的图片，找到当前行对象
			if(img.parentElement.parentElement.parentElement.tagName=="TR"){
				rowIndex=img.parentElement.parentElement.parentElement.rowIndex;
			}
			//在当前行的下一行增加行
			insertRow(rowIndex+1);
			ChangeImg1(img);
		} else {
			//隐藏行
			hiddenDetail(img);
			ChangeImg2(img);
		}
		// 处理由关闭展开引发的滚动条问题
		divAll.fireEvent("onresize");
	}
	// 展开下级改变为－图片
	this.hiddenDetail =hiddenDetail;
	function hiddenDetail(img){
		//取当前行的下一行，也就是展开行对象
		var nextObj=img.parentElement.parentElement.parentElement.nextSibling;
		if(nextObj.tagName=="TR"){
			//移除展开行对象
			nextObj.removeNode(true);
		}else{
			return;
		}
		var currentRowIndex=0;
		// 取到当前行索引
		if(img.parentElement.parentElement.parentElement.tagName=="TR"){
			currentRowIndex=img.parentElement.parentElement.parentElement.rowIndex;
		}
		if (divTiLeftCol != null && divTiLeftCol.children[0] != null) {
			var tableft = divTiLeftCol.children[0];
			// 移除展开行的左侧固定列对象
			tableft.rows[currentRowIndex+1].removeNode(true);			
		}
	}
	// 树状列表
	// 初始化
	this.TreeInit = TreeInit;
	function TreeInit(jsonStr) {
		if (jsonStr == null || jsonStr == "")
			return;
		OpenImg = gridObj.GRIDINFO.openFlag;
		CloseImg = gridObj.GRIDINFO.closeFlag;
		treeObj = eval(jsonStr);
		for ( var i = 0; i < treeObj.length; i++) {
			var onode = treeObj[i];
			paddingLeft = 0;
			InitTreeNode(onode);
		}
		AddImgEvent();
		arrangeRows();
		// 处理由关闭展开引发的滚动条问题
		divAll.fireEvent("onresize");
	}
	// 初始化HTML节点，隐藏，缩进
	this.InitTreeNode = InitTreeNode;
	function InitTreeNode(node) {
		if (node == null)
			return;
		paddingLeft += 15;
		for ( var i = 0; i < node.branch.length; i++) {
			var onode = node.branch[i];
			var onodeid = onode.nodeId;
			PaddingRow(onodeid);// 缩进
			HidRow(onodeid);// 隐藏
			if (onode.branch.length > 0)
				InitTreeNode(onode);
			else {
				closeImg(onodeid);
			}
		}
		paddingLeft -= 15;
	}
	// 给图片添加事件
	this.AddImgEvent = AddImgEvent;
	function AddImgEvent() {
		if (tabTi == null)
			return;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			var otr = tabTi.rows[i];
			var otd = otr.cells[1];
			var odiv = otd.children[0];
			var oimg = odiv.children[0];
			if (oimg != null && oimg.tagName == "IMG")
				oimg.attachEvent("onclick", doImgClick);
		}
	}
	// 重新排列左边固定列，显示最上面一个隐藏的行
	this.ShowLeft = ShowLeft;
	function ShowLeft() {
		var tabLeft = divTiLeftCol.children[0];
		var Ltr = null;
		for ( var j = 0; j < tabLeft.rows.length; j++) {
			var otr = tabLeft.rows[j];
			if (otr.style.display == "none") {
				Ltr = otr;
				break;
			}
		}
		if (Ltr != null)
			Ltr.style.display = "block";
	}
	// 重新排列左边固定列,隐藏最下面一个显示的行
	this.HidLeft = HidLeft;
	function HidLeft() {
		var tabLeft = divTiLeftCol.children[0];
		var Ltr = null;
		for ( var j = tabLeft.rows.length - 1; j >= 0; j--) {
			var otr = tabLeft.rows[j];
			if (otr.style.display != "none") {
				Ltr = otr;
				break;
			}
		}
		if (Ltr != null)
			Ltr.style.display = "none";
	}
	// 根据id使列表隐藏
	this.HidRow = HidRow;
	function HidRow(id) {
		if (tabTi == null)
			return;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			var otr = tabTi.rows[i];
			var otd = otr.cells[1];
			var odiv = otd.children[0];
			var oimg = odiv.children[0];
			if (oimg.nodeid == id) {
				if (otr.style.display != "none") {
					otr.style.display = "none";
					HidLeft();
				}
			}
		}
	}
	// 根据id初始化没有下级的图片
	this.closeImg = closeImg;
	function closeImg(id) {
		if (tabTi == null)
			return;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			var otr = tabTi.rows[i];
			var otd = otr.cells[1];
			var odiv = otd.children[0];
			var oimg = odiv.children[0];
			if (oimg.nodeid == id) {
				oimg.isLast = "1";
				oimg.src = CloseImg;
			}
		}
	}

	// 根据id使列表显示
	this.ShowRow = ShowRow;
	function ShowRow(id) {
		if (tabTi == null)
			return;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			var otr = tabTi.rows[i];
			var otd = otr.cells[1];
			var odiv = otd.children[0];
			var oimg = odiv.children[0];
			if (oimg.nodeid == id) {
				otr.style.display = "block";
				if (odiv.offsetWidth > otd.offsetWidth + 3)
					addWidth(1, odiv.offsetWidth);
				ShowLeft();
				ChangeImg2(oimg);
			}
		}
	}

	// 根据id使列表缩进
	this.PaddingRow = PaddingRow;
	function PaddingRow(id) {
		if (tabTi == null)
			return;
		for ( var i = 1; i < tabTi.rows.length; i++) {
			var otr = tabTi.rows[i];
			var otd = otr.cells[1];
			var odiv = otd.children[0];
			var oimg = odiv.children[0];
			if (oimg.nodeid == id) {
				var width1 = odiv.offsetWidth;
				var width2 = paddingLeft + oimg.offsetWidth;
				if (width1 < width2)
					addWidth(1, width2);
				odiv.style.paddingLeft = paddingLeft;

			}
		}
	}
	// 根据缩进改变列宽
	this.addWidth = addWidth;
	function addWidth(col, wid) {
		// 改变表头体列宽
		tabTou.rows[0].cells[col].width = wid + 5;
		// 改变合计区列宽
		tabSum.rows[0].cells[col].width = wid + 5;
		// 改变表格体列宽
		for ( var i = 0; i < tabTi.rows.length; i++) {
			var otd = tabTi.rows[i].cells[col];
			if (otd != null) {
				otd.width = wid + 5;
			}
		}
	}

	// 点击＋－图片
	this.doImgClick = doImgClick;
	function doImgClick() {
		var img = window.event.srcElement;
		if (img.tagName != "IMG")
			return;
		var nodeid = img.nodeid;
		if (img.open != "1") {
			OpenNode(null, nodeid);
			ChangeImg1(img);
		} else {
			CloseNode(null, nodeid);
			ChangeImg2(img);
		}
		// 处理由关闭展开引发的滚动条问题
		divAll.fireEvent("onresize");
	}
	// 改变为－图片
	this.ChangeImg1 = ChangeImg1;
	function ChangeImg1(img) {
		if (img != null && img.tagName == "IMG") {
			if (img.isLast == "1")
				return;
			img.src = CloseImg;
			img.open = "1";
		}
	}
	// 改变为＋图片
	this.ChangeImg2 = ChangeImg2;
	function ChangeImg2(img) {
		if (img != null && img.tagName == "IMG") {
			if (img.isLast == "1")
				return;
			img.src = OpenImg;
			img.open = "0";
		}
	}

	// 展开节点
	this.OpenNode = OpenNode;
	function OpenNode(node, nodeid) {
		if (treeObj == null)
			return;
		if (node == null)
			node = treeObj;
		else
			node = node.branch;
		for ( var i = 0; i < node.length; i++) {
			var onode = node[i];
			if (onode.nodeId == nodeid) {
				ZhanKai(onode);
				arrangeRows();
				return;
			}
			if (onode.branch.length > 0) {
				OpenNode(onode, nodeid);
				arrangeRows();
			}

		}
	}
	// 关闭节点
	this.CloseNode = CloseNode;
	function CloseNode(node, nodeid) {
		if (treeObj == null)
			return;
		if (node == null)
			node = treeObj;
		else
			node = node.branch;
		for ( var i = 0; i < node.length; i++) {
			var onode = node[i];
			if (onode.nodeId == nodeid) {
				GuanBi(onode);
				arrangeRows();
				return;
			}
			if (onode.branch.length > 0) {
				CloseNode(onode, nodeid);
				arrangeRows();
			}
		}
	}
	// 展开下一层节点
	this.ZhanKai = ZhanKai;
	function ZhanKai(onode) {
		// ShowRow(onode.nodeId);
		for ( var i = 0; i < onode.branch.length; i++) {
			var oonode = onode.branch[i];
			var ooid = oonode.nodeId;
			ShowRow(ooid);
		}
	}
	// 关闭所有下级节点
	this.GuanBi = GuanBi;
	function GuanBi(onode) {
		for ( var i = 0; i < onode.branch.length; i++) {
			var oonode = onode.branch[i];
			var ooid = oonode.nodeId;
			HidRow(ooid);
			if (oonode.branch.length > 0)
				GuanBi(oonode);
		}
	}
	// 根据ID串展开,参数用,号分开
	this.OpenNodeById = OpenNodeById;
	function OpenNodeById(ids) {
		if (ids == null || ids == "")
			return;
		var arrid = ids.split(",");
		var imgs = divAll.getElementsByTagName("img");
		for ( var i = 0; i < arrid.length; i++) {
			if (arrid[i] == null || arrid[i] == "")
				continue;
			for ( var j = 0; j < imgs.length; j++) {
				var oimg = imgs[j];
				if (oimg.nodeid == arrid[i]) {
					oimg.click();
				}
			}
		}
	}
	// 外部函数，翻页所需，resetPageIndex隐藏域
	this.resetPageIndex = resetPageIndex;
	function resetPageIndex() {
		document.all('org.sotower.web.taglib.util.GRIDINFOPRIMARY').value = null;
		document.all("org.sotower.web.taglib.util.RESETPAGEINDEX").value = "true";
	}
	// 固定一列
	this.pinCol = pinCol;
	function pinCol(col) {

	}
	// 取消列固定
	this.cancelPinCol = cancelPinCol;
	function cancelPinCol(col) {
	}
	// 固定一行
	this.pinRow = pinRow;
	function pinRow(row) {

	}
	// 取消行固定
	this.cancelPinRow = cancelPinRow;
	function cancelPinRow(row) {

	}
	// 显示明细
	this.showDetail = showDetail;
	function showDetail() {
	}
	// 隐藏明细
	this.hideDetail = hideDetail;
	function hideDetail() {
	}
	this.changeLeftIndex = changeLeftIndex;
	function changeLeftIndex(val) {
		if (val == "true" && divTouLeftCol.style.display != "none") {
			increaseIndex = "true";
			var indexBase = getBaseIndex();
			if (tabTi == null || tabTi.tagName != "TABLE")
				return;
			try {
				// 为左边固定列增加一行
				if (divTiLeftCol != null && divTiLeftCol.children[0] != null) {
					var tableft = divTiLeftCol.children[0];
					for ( var i = 1; i < tableft.rows.length; i++) {
						var row = tableft.rows[i].cells[0];
						row.innerHTML = "<span>" + (indexBase + i) + "</span>";
					}
				}
			} catch (ex) {
				return;
			}
		} else if (val == "false" && divTouLeftCol.style.display == "none") {
			return;
		} else
			return;
	}
	this.getBaseIndex = getBaseIndex;
	function getBaseIndex() {
		var baseIndex = 0;
		if ((document
				.all('org.sotower.web.taglib.util.PAGEPOLITCURRENTPAGEINDEX') != null && document
				.all('org.sotower.web.taglib.util.PAGEPOLITPAGESIZE') != null)
				&& increaseIndex == "true") {
			var pagenow = document
					.all('org.sotower.web.taglib.util.PAGEPOLITCURRENTPAGEINDEX').value;
			var pagesize = document
					.all('org.sotower.web.taglib.util.PAGEPOLITPAGESIZE').value;
			baseIndex = (pagenow - 1) * pagesize;
			return baseIndex;
		} else
			return baseIndex;
	}
	// 定位到某一行
	// lightLine

	// 打印功能
	this.print = print;
	function print(newWindow, height) {
		var printWindow;
		if (newWindow) {
			printWindow = newWindow;
		} else {
			printWindow = window
					.open(
							"",
							flexgrid_msg_11,
							"height=768,width=1024,top=0,left=0,toolbar=no,menubar=yes,scrollbars=yes, resizable=yes,location=no, status=no");
		}

		printWindow.document.writeln('<style media="print">');
		printWindow.document.writeln('.noprint{display:none}');
		printWindow.document.writeln('</style>');

		var cssCxtHiddenId = 'print_' + this.name + '_cssCxt';
		var cssCxt = document.getElementById(cssCxtHiddenId).value;
		printWindow.document
				.writeln('<link rel="stylesheet" type="text/css" href="' + cssCxt + '/skin.css"/>');

		var jsCxtHiddenId = 'print_' + this.name + '_jsCxt';
		var jsCxt = document.getElementById(jsCxtHiddenId).value;
		// 需要指定charset，或者不能成功加载flexgrid.js
		printWindow.document
				.writeln('<script language="javascript" charset="GBK" src="' + jsCxt + '/flexgrid.js"></script>');

		printWindow.document
				.writeln('<div style="float:left;margin-top:20px">');
		printWindow.document
				.writeln('<div><div style="float:left;margin-left:20px"></div><div style="float:left">');
		printWindow.document
				.writeln('<input type="button" value="' + flexgrid_msg_11 + '" class="noprint" onclick="javascript:window.print();"/>');

		printWindow.document.writeln('<SCRIPT>');
		printWindow.document.writeln('var ' + this.name + ' = new GRID();');
		printWindow.document
				.writeln('var jsonstr = window.opener.' + this.name + '_jsonstr;');
		printWindow.document.writeln('jsonstr.GRIDINFO.isPrint = \"true\";');

		if (height) {
			printWindow.document
					.writeln('jsonstr.GRIDINFO.height = \"' + height + '\";');
		}

		printWindow.document.writeln(this.name + '.outHtml(jsonstr, \"' + jsCxt
				+ '\", \"' + this.name + '\");');
		printWindow.document
				.writeln('function showGrid(){this.init(\"' + this.name + '\");}');
		printWindow.document.writeln(this.name + '.show = showGrid;');
		printWindow.document.writeln(this.name + '.show();');
		printWindow.document.writeln('</' + 'SCRIPT>');

		printWindow.document.writeln('</div></div>');

		printWindow.document.close();

	}
	

}
// 外部函数，翻页所需，resetPageIndex隐藏域
function resetPageIndex() {
	var oField = document.all('org.sotower.web.taglib.util.GRIDINFOPRIMARY');
	if (oField != null)
		oField.value = null;
	oField = document.all("org.sotower.web.taglib.util.RESETPAGEINDEX");
	if (oField != null)
		oField.value = "true";
}

// 外部函数，用于扩展doclick函数
function doclickAfter(currentLine) {
}
//外部函数，用于扩展当页面有展开列时，重新展开下级div的回调方法
function overrideDiv(divObj,rowObj){
	//alert(divObj);
	// alert(rowObj);
}
// 导出多页excel，比如：导出 1-3页的数据到excel
function exportMultiPageExcel(gridName, fromPage, toPage) {
	if (!gridName) {
		alert(flexgrid_msg_12);
		return false;
	}

	if (!fromPage || !toPage) {
		alert(flexgrid_msg_13);
		return false;
	}

	var re = /^[0-9]+$/;
	if (!re.test(fromPage) || !re.test(toPage)) {
		alert(flexgrid_msg_13);
		return false;
	}

	var fromPageNo = new Number(fromPage);
	var toPageNo = new Number(toPage);
	if (fromPageNo > toPageNo || fromPageNo < 1 || toPageNo < 1) {
		alert(flexgrid_msg_13);
		return false;
	}

	var pageCountHiddenId = "pagepolitPageCount" + gridName;
	var pageCount = document.getElementById(pageCountHiddenId).value;
	var pageCountNo = new Number(pageCount);
	if (toPageNo > pageCountNo || fromPageNo > pageCountNo) {
		alert(flexgrid_msg_13);
		return false;
	}

	var pageSize = document
			.getElementById("org.sotower.web.taglib.util.PAGEPOLITPAGESIZE").value;
	var excelUrlId = "org.sotower.web.taglib.util." + gridName;
	var excelUrl = document.getElementById(excelUrlId).value;
	window.location.href = excelUrl + "&org.sotower.web.taglib.util.PAGEFROM="
			+ fromPageNo + "&org.sotower.web.taglib.util.PAGETO=" + toPageNo
			+ "&org.sotower.web.taglib.util.PAGEPOLITPAGESIZE=" + pageSize;

}
// 给文本框增加键盘按下对特殊字符的监控事件
function doHtmltextValueCheck() {
	var code;
	var character;
	if (document.all) // 判断是否是IE浏览器
	{
		code = window.event.keyCode;
	} else {
		code = arguments.callee.caller.arguments[0].which;
	}
	var character = String.fromCharCode(code);
	if (__isQuoteInCheck(character)) {
		// alert("企图输入非法字符，请重新输入！");
		if (document.all) {
			window.event.returnValue = false;
		} else {
			arguments.callee.caller.arguments[0].preventDefault();
		}
	}
}
// TODO event for right scrollingbar
document.onmouseup = onMouseMoveUpEvent;
document.onmousemove = onMouseMoveEvent;
var scrollId = '';
var maxrow = 0;
var curRow = 1;
var __csd = null;
var __cxtPath = '';
var moveDivHeight = 0;
var oldStopPx = 16;
var mdivTop = 16;
var mdivBottom = 16;
var pY = 0;
var grid_jsonstr = null;
var grid_obj = null;
// TODO onmousedown event
function MDown(Object, rno) {
	scrollId = Object.id;
	maxrow = rno;
	__csd = document.getElementById(grid_jsonstr.GRIDINFO.name + "_SCS");
	var oldMousePx = __csd.style.pixelTop;
	document.all(scrollId).setCapture();
	pY = event.y - document.all(scrollId).style.pixelTop;
	showCSdiv(__csd, maxrow, true, oldStopPx);
}
// TODO show Current record and Sum(CS) number
function showCSdiv(csDivObj, conNo, flag, displayPx) {
	if (csDivObj != null) {
		if (conNo != null)
			maxrow = conNo;
		csDivObj.innerText = (curRow > 0 ? curRow : 1) + "/" + maxrow;
		csDivObj.style.pixelTop = displayPx;
		if (flag)
			csDivObj.style.display = "block";
		else
			csDivObj.style.display = "none";
	}
}
// TODO moving scrollingbar following
function moveScrollAndCSDiv(divObj, stopPx) {
	divObj.style.pixelTop = stopPx;
	var h = grid_jsonstr.GRIDINFO.height - mdivTop * 2 - mdivBottom
			- moveDivHeight;
	var stopPxT = stopPx - mdivTop;
	curRow = Math.round(stopPxT * maxrow / h);
	__csd.innerText = (curRow > 0 ? curRow : 1) + "/" + maxrow;
	__csd.style.pixelTop = stopPx;
	oldStopPx = stopPx;
}

// TODO 考虑加小延迟 onmousemove event
function onMouseMoveEvent() {
	if (scrollId != '') {
		var mousePx = event.y - pY;
		var mvObj = document.getElementById(scrollId);
		var bottomPx = grid_jsonstr.GRIDINFO.height - moveDivHeight
				- mdivBottom - mdivTop;
		if (mvObj != null) {
			if (mousePx <= mdivTop)
				moveScrollAndCSDiv(mvObj, mdivTop);
			else if (mousePx >= bottomPx)
				moveScrollAndCSDiv(mvObj, bottomPx);
			else
				moveScrollAndCSDiv(mvObj, mousePx);
		}
	}
}
// TODO onmouseup event
function onMouseMoveUpEvent() {
	if (scrollId != '') {
		__csd.style.display = "none";
		document.all(scrollId).releaseCapture();
		// var grid = new GRID();
		var nm = grid_jsonstr.GRIDINFO.name;
		var gridHtml = grid_obj.outHtmlForLarge(grid_jsonstr, __cxtPath, nm,
				curRow);
		grid_obj.setSubmit(grid_jsonstr.GRIDINFO.isSubmit);
		//tiant
		grid_obj.setMultOrder(grid_jsonstr.GRIDINFO.isMultOrder);
		//end
		grid_obj.init(nm);
		document.all(scrollId).style.pixelTop = oldStopPx;
		__csd.style.pixelTop = oldStopPx;
		scrollId = '';
	}
}