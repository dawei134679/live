<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
<link rel="stylesheet" type="text/css" href="./easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="./easyui/themes/icon.css">
<script type="text/javascript" src="./easyui/jquery.min.js"></script>
<script type="text/javascript" src="./easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="./easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="./js/common.js"></script>
    <script>

    	$(function(){
        	//获取渠道列表
    	    $("#channel_id").combobox({
   				url:"./operat/getChannelForSelect",
   				valueField:'channelCode',
   				textField:'channelName',
   				onLoadSuccess: function (data) {
   		            if (data) {
   		                $('#channel_id').combobox('setValue',"");
   		            }
   				}
    		});
    	});

        var dataGrid;

        $(function () {

            dataGrid = $("#orderList").datagrid({
                url: './payOrderList?method=getPayOrderList',
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
                    {field: 'srcUid', title: '充值操作人', align: 'center', sortable: false,formatter : function(data) {
						return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
					}},
                    {field: 'dstUid', title: '充值接收人', align: 'center', sortable: false,formatter : function(data) {
						return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
					}},
                    {field: 'amount', title: '订单金额', align: 'center', sortable: false},
                    {field: 'creatAt', title: '创建时间', align: 'center', sortable: true,formatter:function (data) {
                        return data ? new Date(data*1000).Format("yyyy-MM-dd HH:mm:ss") : ""
                    }},
                    {field: 'paytime', title: '支付时间', align: 'center', sortable: true,formatter:function (data) {
                        return data ? new Date(data*1000).Format("yyyy-MM-dd HH:mm:ss") : ""
                    }},
                    {field: 'os', title: 'APP类型', align: 'center', sortable: false,formatter:function(data){
                    	switch(data){
	                    	case 1:return "android";break;
	                    	case 2:return "IOS";break;
	                    	case 3:return "微信公众号";break;
                    	}
                    }},
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
                    {field: 'details', title: '充值描述', align: 'center', sortable: false},
                    {field: 'channel', title: '渠道来源', align: 'center', sortable: false}
                ]]
            })
        })


        function queryOrder() {
        	channels = $("#channel_id").combobox('getValues');
        	$("#channels").val(channels);
            dataGrid.datagrid('reload', {
                status: $("#b_status").combobox('getValue'),
                dstuid: $("#s_dstuid").textbox('getValue'),
                srcuid: $("#s_srcuid").textbox('getValue'),
                chnenee: $("#channels").val(),
                s_time:$('#s_time').datebox("getValue"),
                e_time:$('#e_time').datebox("getValue")
            });
        }

        function clearQuery() {
            $("#s_srcuid").textbox('reset');
            $("#s_dstuid").textbox('reset');
            $("#b_status").combobox('clear');
            $("#channel_id").combobox('clear');

        }
        
        function memberDetail(uid){
   		 var iframe = "<iframe src='jsp/user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
   		 $('#dlg1').html(iframe);
   		 $('#dlg1').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
   	}
    </script>
</head>
<body>
<div>
    <table id="orderList"></table>
    <div id="tb" style="padding:2px 5px;height:auto">
        <div>
            <input id="channels" name="channels" type="hidden">
            <label for="b_status">订单状态：</label>
            <select class="easyui-combobox" id="b_status" style="width:80px;">
                <option value="" selected="true">请选择</option>
                <option value="0">生成订单</option>
                <option value="1">等待支付</option>
                <option value="2">已支付</option>
                <option value="3">取消</option>
            </select>
            <label for="s_srcuid">充值操作人id：</label><input class="easyui-numberbox" style="width:80px" id="s_srcuid">
            <label for="s_dstuid">充值接收人id：</label><input class="easyui-numberbox" style="width:80px" id="s_dstuid">
            <label for="channel_id">渠道：</label>
            <select id="channel_id" class="easyui-combobox" style="width:120px" multiple> 
			</select>
&nbsp;<input class="easyui-datebox" name="s_time" id="s_time" editable="false" size="20"/>-><input class="easyui-datebox" name="e_time" id="e_time" editable="false" size="20"/>
            &nbsp;&nbsp;<a href="javascript:queryOrder()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
            &nbsp;&nbsp;<a href="javascript:clearQuery()" class="easyui-linkbutton">清空</a>
        </div>
    </div>
</div>
<div id="dlg1" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
</body>
</html>
