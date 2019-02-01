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
<title>系统信息公告</title>
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
		dataGrid = $("#sysNoticelist").datagrid(
				{
					url : '../../sysnotice/getSysNoticelist',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '系统信息列表',
					pagination : true,
					idField : "times",
					sortName : 'times',
					sortOrder : 'desc',
					nowrap : false,
					rownumbers : true,
					singleSelect : true,
					pageSize : 30,
					pageList : [ 30, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'id',
								title : 'ID',
								align : 'center',
								width : 50,
								sortable : false
							},{
								field : 'content',
								title : '私信内容',
								align : 'center',
								width : 500,
								sortable : false
							},
							{
								field : 'url',
								title : '跳转地址',
								align : 'center',
								width : 350,
								sortable : false
							},
							{
								field : 'sendtime',
								title : '推送时间',
								align : 'center',
								width : 120,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'addtime',
								title : '添加时间',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'adminname',
								title : '操作人',
								align : 'center',
								width : 100,
								sortable : false
							}
							] ]
				})
	})
	
	function newSysNotice(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加系统私信公告');
        $('#fm').form('clear');
        url = '../../sysnotice/addSysNoctice';
    }

	function saveSysNotice() {
		
		$(".icon-ok").attr("disabled","true");
		setTimeout($(".icon-ok:disabled").removeAttr("disabled"),10000); 
		
		$.messager.confirm("系统提示","确认推送?",function(r){
			if(r){
				$("#fm").form("submit", {
					url : url,
					onSubmit : function() {
						if ($('#content').val().trim() == "") {
							$.messager.alert("系统提示", "请输入系统信息");
							return false;
						}
						return $(this).form("validate");
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
		<table id="sysNoticelist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:newSysNotice()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			</div>
		</div>

		<div id="dlg" class="easyui-dialog" style="width: 570px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>公告信息：</td>
						<td>
							<input class="easyui-textbox" name="content" id="content" data-options="multiline:true" style="height:120px" data-options="disabled:false"/>
						</td>
					</tr>
					<tr>
						<td>跳转地址：</td>
						<td>
							<input class="easyui-textbox" style="width:160px;height:24px" name="url" id="url" size="300"/>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveSysNotice()" class="easyui-linkbutton" iconCls="icon-ok">保存并推送</a> 
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:closeDlg()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
