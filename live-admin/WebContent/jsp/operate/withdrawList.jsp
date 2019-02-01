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
<title>提现审核列表</title>
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
					title : '提现审核列表',
					pagination : true,
					rownumbers : true,
					striped: true,
					singleSelect : true,
					pageSize : 30,
					pageList : [ 30, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'id',
								title : 'ID',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'billno',
								title : '提现单',
								align : 'center',
								width : 200,
								sortable : false
							},{
								field : 'uid',
								title : '提现人UID',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'nickname',
								title : '提现人昵称',
								align : 'center',
								width : 120,
								sortable : false
							},
							{
								field : 'amount',
								title : '提现金额',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'credit',
								title : '消耗声援值',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'openid',
								title : 'openid',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'createAt',
								title : '申请日期',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'sendTime',
								title : '发放日期',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'isSecc',
								title : '审核',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data,row,index) {
									if(data == 0){
										return '<a href="javascript:verifyWithdraw(1,'+index+');" class="easyui-tooltip" title="提现审核通过"> 通过 </a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:verifyWithdraw(2,'+index+');" class="easyui-tooltip" title="提现审核不通过"> 不通过 </a>';
									}else if(data == 1){
										return "已提现";
									}else if(data == 2){
										return "未通过";
									}else{
										return "未知";
									}
								}
							}
							] ]
				})
	})
	
	function verifyWithdraw(result,index){
		$.messager.confirm("系统提示","确认操作?",function(r){
			if(r){
				$('#withdrawlist').datagrid('selectRow',index);
				var selectedRows = $('#withdrawlist').datagrid('getSelections');
				if (selectedRows.length != 1) {
					$.messager.alert("系统提示", "请选择一条要编辑的数据！");
					return;
				}

				var row = selectedRows[0];
		     	$.ajax({
		             type: "post",
		             dataType: "json",
		             url: "../../operat/verifyWithdraw",
		             data: { id:row.id, billno:row.billno,result:result},
		             success: function(data) {

		             	if(data.success == 200){
		             		$("#withdrawlist").datagrid("reload");
		             	}else{
							$.messager.alert("系统提示", data.errorMsg);
		             	}
		             }
		       	});
			}
		})
	}

	function clearQuery() {
		$("#startdate").textbox('reset');
		$("#enddate").textbox('reset');
	}
	
	function queryWithdrawList(){
		$("#withdrawlist").datagrid('reload', {
			isSecc : '0',
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		})
	}
</script>
</head>
<body>
	<div>
		<table id="withdrawlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				开始时间：<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" required="true"/>
				&nbsp;&nbsp;结束时间：<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" required="true"/>[不包含结束时间]
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:queryWithdrawList()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
</body>
</html>