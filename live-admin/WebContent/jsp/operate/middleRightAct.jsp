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
<title>直播间中靠右边活动页面</title>
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
		
		dataGrid = $("#middleRightAct").datagrid(
				{
					url : '../../operat/getMiddleRight',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '上一次活动',
					rownumbers : true,
					singleSelect : true,
					toolbar : '#tb',
					columns : [ [
							{
								field : 'actName',
								title : '活动名称',
								align : 'center',
								width : 200,
								sortable : false
							},
							{
								field : 'url',
								title : '地址',
								align : 'center',
								width : 400,
								sortable : false
							},{
								field : 'stime',
								title : '开始时间',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'etime',
								title : '结束时间',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							}
							] ]
				})
	})
	
	function addMiddleRigthActOpen(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加活动');
        $('#fm').form('clear');
        url = '../../operat/addMiddleRight';
    }

	function saveMiddleRigthAct() {

		$.messager.confirm("系统提示","确认推送?",function(r){
			if(r){
				$("#fm").form("submit", {
					url : url,
					onSubmit : function() {
						if ($('#actName').val().trim() == "") {
							$.messager.alert("系统提示", "请输入活动名称");
							return false;
						}
						if ($('#url').val().trim() == "") {
							$.messager.alert("系统提示", "请输入活动地址");
							return false;
						}

			            return $(this).form('validate');
					},
					success : function(data) {
						var result = eval("("+data+")");
						if (result.success == 200) {
							$.messager.alert("系统提示", "保存成功");
							$("#dlg").dialog("close");
							$("#sysNoticelist").datagrid("reload");
						} else if(result.success == 400){
							top.location.href = "../../index.jsp"
						}else {
							$.messager.alert("系统提示", result.errorMsg);
							return;
						}
					}
				});
			}
		})
	}

	function closeDlg(){
		$("#dlg").dialog("close");
	}
	
</script>
</head>
<body>
	<div>
		<table id="middleRightAct"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addMiddleRigthActOpen()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			</div>
		</div>

		<div id="dlg" class="easyui-dialog" style="width: 570px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>活动名称：</td>
						<td>
							<input class="easyui-textbox" name="actName" id="actName" style="width:200px" data-options="disabled:false"/>
						</td>
					</tr>
					<tr>
						<td>页面地址：</td>
						<td>
							<input class="easyui-textbox" style="width:300px;height:24px" name="url" id="url" size="300"/>
						</td>
					</tr>
					<tr>
						<td>开始时间</td>
						<td>
							<input class="easyui-datetimebox" name="stime" id="stime" style="width:150px;height:24px">
						</td>
					</tr>
					<tr>
						<td>结束时间</td>
						<td>
							<input class="easyui-datetimebox" name="etime" id="etime" style="width:150px;height:24px">
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveMiddleRigthAct()" class="easyui-linkbutton" iconCls="icon-ok">保存并推送</a> 
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:closeDlg()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
