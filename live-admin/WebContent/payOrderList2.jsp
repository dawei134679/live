<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@ include file="./header.jsp" %>
    <script>
        var dataGrid;

        $(function () {

            dataGrid = $("#orderList").datagrid({
                // url: '/payOrderList',
                width: getWidth(0.97),
                height: getHeight(0.97),
                title: '订单列表',
                pagination: true,
                idField: "paytime",
                sortName: 'paytime',
                sortOrder: 'DESC',
                rownumbers: true,
                singleSelect: true,
                pageSize:20,
                pageList:[20,50,100],
                toolbar: '#tb',
                columns: [[
                    {field: 'order_id', title: '订单id', align: 'center', sortable: false},
                    {field: 'srcUid', title: '充值操作人', align: 'center', sortable: false},
                    {field: 'dstUid', title: '充值接收人', align: 'center', sortable: false},
                    {field: 'amount', title: '订单金额', align: 'center', sortable: false},
                    {field: 'creatAt', title: '创建时间', align: 'center', sortable: true,formatter:function (data) {
                        return data ? new Date(data*1000).Format("yyyy-MM-dd HH:mm:ss") : ""
                    }},
                    {field: 'paytime', title: '支付时间', align: 'center', sortable: true,formatter:function (data) {
                        return data ? new Date(data*1000).Format("yyyy-MM-dd HH:mm:ss") : ""
                    }},
                    {field: 'os', title: '充值类型', align: 'center', sortable: false},
                    {field: 'paytype', title: '支付方式', align: 'center', sortable: false},
                    {field: 'status', title: '支付状态', align: 'center', sortable: true,formatter:function (data) {
                        switch (data){
                            case 0: return"生成订单";break;
                            case 1: return"等待支付";break;
                            case 2: return"已支付";break;
                            case 3: return"取消";break;
                        }
                    }},
                    {field: 'inpour_no', title: '第三方支持订单号', align: 'center', sortable: false},
                    {field: 'details', title: '充值描述', align: 'center', sortable: false}
                ]]
            })
        })


        function queryOrder() {
            dataGrid.datagrid('reload', {
                status: $("#b_status").combobox('getValue'),
                dstuid: $("#s_dstuid").textbox('getValue'),
                srcuid: $("#s_srcuid").textbox('getValue'),
                s_time:$('#s_time').datebox("getValue"),
                e_time:$('#e_time').datebox("getValue")
            })
        }

        function clearQuery() {
            $("#s_srcuid").textbox('reset')
            $("#s_dstuid").textbox('reset')
            $("#b_status").combobox('clear')

        }

    </script>
</head>
<body>
<div>
    <table id="orderList"></table>
    <div id="tb" style="padding:2px 5px;height:auto">
        <div>
            <label for="b_status">订单状态：</label>
            <select class="easyui-combobox" id="b_status" style="width:80px;">
                <option value="" selected="true">请选择</option>
                <option value="0">生成订单</option>
                <option value="1">等待支付</option>
                <option value="2">已支付</option>
                <option value="3">取消</option>
            </select>
            <label for="s_srcuid">充值操作人id：</label><input class="easyui-textbox" style="width:90px" id="s_srcuid">
            <label for="s_dstuid">充值接收人id：</label><input class="easyui-textbox" style="width:90px" id="s_dstuid">
&nbsp;<input class="easyui-datebox" name="s_time" id="s_time" editable="false" size="20"/>-><input class="easyui-datebox" name="e_time" id="e_time" editable="false" size="20"/>
            &nbsp;&nbsp;<a href="javascript:queryOrder()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
            &nbsp;&nbsp;<a href="javascript:clearQuery()" class="easyui-linkbutton">清空</a>
        </div>
    </div>
</div>
</body>
</html>
