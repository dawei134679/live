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
<title>商城购买查询</title>
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
		dataGrid = $("#searchlist").datagrid(
				{
					url : '../../operat/mallSearch',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '商城购买查询',
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'gid',
								title : '礼物ID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'gname',
								title : '礼物名称',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'srcuid',
								title : '购买者ID',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'srcnickname',
								title : '购买者名称',
								width : 200,
								align : 'center',
								sortable : false
							},
							{
								field : 'dstuid',
								title : '收益者ID',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'dstnickname',
								title : '收益者名称',
								width : 200,
								align : 'center',
								sortable : false
							},
							{
								field : 'pricetotal',
								title : '原价',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'realpricetotal',
								title : '实际支付',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'createAt',
								title : '购买时间',
								width : 150,
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'endtime',
								title : '过期时间',
								width : 150,
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							}
							] ]
				})
	})

	function clearQuery() {
		
		$("#content").textbox('reset');
		$("#condition").combobox('reset');
		
		$("#searchos").combobox('reset');
		$("#startdate").textbox('reset');
		$("#enddate").textbox("reset");
	}
	
	function querySearch(){
		$("#searchlist").datagrid('reload', {
			
			content : $("#content").textbox('getValue'),
			condition : $("#condition").combobox('getValue'),
			
			searchos : $("#searchos").combobox("getValue"),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		})
	}
</script>
</head>
<body>
	<div>
		<table id="searchlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<select id="condition" class="easyui-combobox" name="condition"
					style="width:120px;">
					<option value="1">道具ID</option>
					<option value="2">购买者ID</option>
				</select><input class="easyui-textbox" style="width: 100px" id="content">
				<select id="searchos" class="easyui-combobox" name="searchos"
					style="width:90px;">
					<option value="0">全部</option>
					<option value="1">守护</option>
					<option value="2">VIP</option>
					<option value="3">商城道具</option>
				</select>
				&nbsp;开始时间：&nbsp;<input class="easyui-datebox" style="width:120px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:120px;height:24px" name="enddate"  id="enddate" size="10" />
				&nbsp;&nbsp;
				<a href="javascript:querySearch()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
</body>
</html>
