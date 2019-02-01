<%--
  Created by IntelliJ IDEA.
  User: fangwuqing
  Date: 16/5/6
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>提现列表</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;
	var url;
	$(function() {
		dataGrid = $("#withdrawlist").datagrid(
				{
					url : '../../operat/withdraw',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '提现列表',
					pagination : true,
					idField : "times",
					sortName : 'times',
					sortOrder : 'desc',
					rownumbers : true,
					striped : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'billno',
								title : '单号',
								width : 200,
								align : 'center',
								sortable : false
							},
							{
								field : 'createAt',
								title : '提现时间',
								align : 'center',
								width : 180,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'uid',
								title : '用户UID',
								width : 150,
								align : 'center',
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户名称',
								width : 300,
								align : 'center',
								sortable : false
							},
							{
								field : 'amount',
								title : '提现金额',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'credit',
								title : '消耗声援值',
								width : 150,
								align : 'center',
								sortable : false
							},
							{
								field : 'isSecc',
								title : '提现成功',
								width : 100,
								align : 'center',
								sortable : false,
								formatter : function(data) {
									if (data == 0) return "待审核";
									if (data == 1) return "成功";
									if (data == 2) return "未通过";
								}
							}
							] ]
				})
	})

	function clearQuery() {
		$("#isSecc").combobox('reset');
		$("#uid").textbox('reset');
		$("#starttime").textbox('reset');
		$("#endtime").textbox('reset');
	}
	
	function queryWithdraw(){
		$("#withdrawlist").datagrid('reload', {
			isSecc : $("#isSecc").combobox("getValue"),
			uid : $("#uid").textbox("getValue"),
			startDate : $("#starttime").textbox('getValue'),
			endDate : $("#endtime").textbox('getValue')
		})
	}
	
</script>
</head>
<body>
	<div>
		<table id="withdrawlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
			<select id="isSecc" class="easyui-combobox" name="isSecc" style="width:90px;" required="true">
					<option value="-1" selected="selected">全部</option>
					<option value="1">成功</option>
					<option value="2">失败</option>
					<option value="0">待审核</option>
				</select>
				&nbsp;&nbsp;开始时间：<input class="easyui-datebox" style="width:150px;height:24px" name="starttime" id="starttime" required="true" size="10"/>
				&nbsp;&nbsp;结束时间：<input class="easyui-datebox" style="width:150px;height:24px" name="endtime" id="endtime" required="true" size="10"/>[不包含]
				&nbsp;&nbsp;主播UID：&nbsp;<input class="easyui-textbox" style="width: 100px" id="uid" name="uid">
				
				<a href="javascript:queryWithdraw()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
</body>
</html>
