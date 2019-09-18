<%--
  Created by IntelliJ IDEA.
  User: mingliao
  Date: 2019/8/22
  Time: 15:40:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="<%=request.getContextPath()%>/yszx/js/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" media="print"/>
    <title>打印预览</title>
    <style media="print">
        @page{
            /*size:210mm 290mm;*/
            margin:0mm auto;
        }

    </style>
    <style>
        td{
            height: 40px;
        }
        th{
            height: 40px;
        }
    </style>
</head>
<body>
<div class="navbar">
    <button onclick="printPreview()">打印</button>
</div>
<div>
    <h2 style="text-align: center;">中国电力科学研究院有限公司用印审批单</h2>
    <h3>申请编号：${yyApplyDAO.applyCode}</h3>
    <table border="1" cellspacing="0" width="100%" style="font-size: 20px;">
        <tr>
            <th>用印部门(单位):</th>
            <td colspan="3">&nbsp;&nbsp; ${yyApplyDAO.applyDept}</td>
        </tr>
        <tr>
            <th>用印申请人:</th>
            <td>&nbsp;&nbsp; ${yyApplyDAO.applyUser}</td>
            <th>联系方式:</th>
            <td>&nbsp;&nbsp; ${yyApplyDAO.useSealPhone}</td>
        </tr>
        <tr>
            <th>用印日期:</th>
            <td>&nbsp;&nbsp; ${yyApplyDAO.useSealDate}</td>
            <th>用印事项:</th>
            <td>&nbsp;&nbsp; ${yyApplyDAO.useSealItem}</td>
        </tr>
        <tr>
            <th>用印种类:</th>
            <td colspan="3">&nbsp;&nbsp; ${yyApplyDAO.useSealKind}</td>
        </tr>
        <tr>
            <th>用印事由:</th>
            <td colspan="3">&nbsp;&nbsp; ${yyApplyDAO.useSealReason}</td>
        </tr>
        <tr>
            <th colspan="4" style="text-align: center">审批记录</th>
        </tr>
        <tr>
            <th>审查单位</th>
            <th>审批角色</th>
            <th colspan="2">审批意见</th>
        </tr>
        <%--内容查出后循环展示--%>
            <tr>
                <td rowspan="3" style="text-align: center">某部门</td>
                <td rowspan="3" style="text-align: center">用印申请人</td>
                <td style="text-align: center">审批意见:</td>
                <td>&nbsp;&nbsp; 提交</td>
            </tr>
            <tr>
                <td style="text-align: center">申请人:</td>
                <td>&nbsp;&nbsp; 申请人姓名</td>
            </tr>
            <tr>
                <td style="text-align: center">申请时间:</td>
                <td>&nbsp;&nbsp; 2019-09-06 12:45:12</td>
            </tr>
        <%--内容查出后循环展示--%>
            <tr>
                <td rowspan="3" style="text-align: center">某部门</td>
                <td rowspan="3" style="text-align: center">用印申请人</td>
                <td style="text-align: center">审批意见:</td>
                <td>&nbsp;&nbsp; 提交</td>
            </tr>
            <tr>
                <td style="text-align: center">申请人:</td>
                <td>&nbsp;&nbsp; 申请人姓名</td>
            </tr>
            <tr>
                <td style="text-align: center">申请时间:</td>
                <td>&nbsp;&nbsp; 2019-09-06 12:45:12</td>
            </tr>
    </table>
</div>
<script>
    //控件设定必须再浏览器使用
    var HKEY_Path = "HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\"; // 定义注册表位置
    // name的值可为header（页眉），footer（页脚），margin_bottom（下边距margin_left（左边距），margin_right（右边距），margin_top（上边距）。
    function PageSetup(name,value) {
        try {
            var Wsh = new ActiveXObject("WScript.Shell");
            Wsh.RegWrite(HKEY_Path+name,value); // 修改注册表值
        }catch(e) {
            // alert('需要运行ActiveX对象后，才能进行打印设置。');
        }
    }
    function printPreview() {
        PageSetup("footer","");
        PageSetup("header","");
        window.print();
    }
    function printPreview2() {
        window.print();
    }
</script>
</body>
</html>
