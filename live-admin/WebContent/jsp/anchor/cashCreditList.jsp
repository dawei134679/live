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
<title>兑现列表</title>
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
		
		dataGrid = $("#cashlist").datagrid(
				{
					url : '../../anchor/getCashCreditList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '兑换列表',
					queryParams: {
						uid : $('#uid').textbox("getValue"),
						status : $("#status").combobox('getValue'),
						startdate : $("#startdate").textbox('getValue'),
						enddate : $("#enddate").textbox('getValue')
					},
					pagination : true,
			        rownumbers: true,
					striped : true,
			        singleSelect: true,
			        fit:true,
			        fitColumns:true,
			        pageSize:20,
			        pageList:[20,50,100],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'id',
								title : 'id',
								align : 'center',
								width : 30,
				            	hidden:true,
								sortable : false
							},
							{
								field : 'times',
								title : '时段',
								align : 'center',
								width : 90,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd") : "";
								}
							},
							{
								field : 'unionid',
								title : '公会ID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'uname',
								title : '公会名称',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'uid',
								title : '主播UID',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'nickname',
								title : '公会ID',
								align : 'center',
								width : 70,
								sortable : false
							},
							{
								field : 'credits',
								title : '声援值提现',
								align : 'center',
								width : 70,
								sortable : false
							},
							{
								field : 'rate',
								title : '分成比率',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'amount',
								title : '礼物提成',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'operatebak',
								title : '备注说明',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'status',
								title : '操作',
								align : 'center',
								width : 150,
								sortable : false,
								formatter:function(data){
									if(data == 0){
										return "待艺管审核";
									}else if(data == 1){
										return "待运营审核";
									}else if(data == 2){
										return "<font color='red'>艺管驳回</font>";
									}else if(data == 3){
										return "运营通过";
									}else if(data == 4){
										return "<font color='red'>运营驳回</font>";
									}else{
										return "<font color='red'>异常</font>";
									}
								}
							},
							{
								field : 'addtime',
								title : '提交时间',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							}
							] ]
				})
	})
	
	function searchCash() {
		$('#cashlist').datagrid('load', {
			uid : $('#uid').textbox("getValue"),
			status : $("#status").combobox('getValue'),
			startdate : $("#startdate").textbox('getValue'),
			enddate : $("#enddate").textbox('getValue')
		});
	}
</script>
</head>
<body style="margin: 5px;">
		<table id="cashlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;用户UID:&nbsp;<input id="uid" name="uid" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
				&nbsp;开始时间:&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间:&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
				&nbsp;状态:&nbsp;<select id="status" class="easyui-combobox" name="status" panelHeight="auto" style="width: 145px">
								<option value="9" selected="selected">全部</option>
								<option value="0">待艺管审核</option>
								<option value="1">待运营审核</option>
								<option value="2">艺管驳回</option>
								<option value="3">运营通过</option>
								<option value="4">运营驳回</option>
				</select>
				&nbsp;&nbsp;<a href="javascript:searchCash()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
</body>
</html>