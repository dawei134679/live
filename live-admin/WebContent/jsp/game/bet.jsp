<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>押注中奖信息</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
    <script>

        var dataGrid;

        $(function () {

            dataGrid = $("#orderList").datagrid({
                url: '../../game/betwinlist',
                width: getWidth(0.97),
                height: getHeight(0.97),
                title: '押注列表',
                pagination: true,
                rownumbers: true,
                singleSelect: true,
                showFooter:true,
                pageSize:20,
                pageList:[20,50,100],
                toolbar: '#tb',
                columns: [[
                    {field: 'add_time', title: '押注时间', align: 'center',width : 200, sortable: false},
                    {field: 'nid7', title: '马(1.5倍)', align: 'center', width : 100,sortable: false},
                    {field: 'nid11', title: '狗(1.5倍)', align: 'center',width : 100, sortable: false},
                    {field: 'nid8', title: '羊(5倍)', align: 'center', width : 100,sortable: false},
                    {field: 'nid2', title: '牛(10倍)', align: 'center', width : 100,sortable: false},
                    {field: 'nid3', title: '虎(20倍)', align: 'center', width : 100,sortable: false},
                    {field: 'nid5', title: '龙(50倍)', align: 'center', width : 100,sortable: false},
                    {field: 'bets', title: '押注次数', align: 'center', width : 100,sortable: false},
                    {field: 'wins', title: '中奖次数', align: 'center', width : 100,sortable: false},
                    {field: 'rate', title: '中奖率(%)', align: 'center', width : 100,sortable: true},
                    {field: 'persons', title: '押注人(不重复)', align: 'center', width : 150,sortable: false},
                    {field: 'deduct_money', title: '押注金额', align: 'center', width : 100,sortable: false},
                    {field: 'add_price', title: '中奖金额', align: 'center', width : 100,sortable: false},
                    {field: 'profit', title: '营收', align: 'center', width : 100,sortable: false}
                ]]
            })
        })

        function queryOrder() {
            dataGrid.datagrid('reload', {
                uid: $("#uid").textbox('getValue'),
                s_time:$('#s_time').datebox("getValue"),
                e_time:$('#e_time').datebox("getValue")
            });
        }

        function clearQuery() {
            $("#uid").textbox('reset');
            $("#b_status").combobox('clear');
        }

    </script>
</head>
<body>
<div>
    <table id="orderList"></table>
    <div id="tb" style="padding:2px 5px;height:auto">
        <div>
            <label for="uid">押注人UID：</label><input class="easyui-textbox" style="width:90px" id="uid">
&nbsp;<input class="easyui-datebox" name="s_time" id="s_time" editable="false" size="20"/>-><input class="easyui-datebox" name="e_time" id="e_time" editable="false" size="20"/>
            &nbsp;&nbsp;<a href="javascript:queryOrder()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
            &nbsp;&nbsp;<a href="javascript:clearQuery()" class="easyui-linkbutton">清空</a>
        </div>
    </div>
</div>
</body>
</html>
