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
<title>轮播公告</title>
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
		dataGrid = $("#roomChatlist").datagrid(
				{
					url : '../../sysnotice/getRoomChat',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '轮播列表',
					pagination : true,
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
								title : '轮播内容',
								align : 'center',
								width : 500,
								sortable : false
							},{
								field : 'isvalid',
								title : '有效',
								align : 'center',
								width : 150,
								sortable : false,
								formatter:function(data){
									if(data) return "有效";
									else return "无效";
								}
							},{
								field : 'interval',
								title : '间隔时间(分)',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'starttime',
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
								field : 'endtime',
								title : '结束时间',
								align : 'center',
								width : 150,
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
								field : 'username',
								title : '操作人',
								align : 'center',
								width : 150,
								sortable : false
							}
							] ]
				})
	})
	
	function addRoomChat(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','新增轮播公告');
        $('#fm').form('clear');
        url = '../../sysnotice/addRoomChat';
    }

	function editRoomChat(){

		var selectedRows = $("#roomChatlist").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		
		row.starttime = myformatter(new Date(row.starttime * 1000));
		row.endtime = myformatter(new Date(row.endtime * 1000));
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改轮播公告");
		$("#fm").form("load", row);
	    url = '../../sysnotice/editRoomChat';
    }
	
	function removeRoomChat(){
		var selectedRows = $("#roomChatlist").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		if(row.isvalid == 0){
			alert("不能操作无效的数据");
			return;
		}
		$.messager.confirm("系统提示","确认作废?",function(r){
			if(r){
				$.ajax({
		             type: "post",
		             dataType: "json",
		             url: "../../sysnotice/removeRoomChat",
		             data: { id:row.id},
		             dataType: 'json', 
		             success: function(data) {
		             	if(data.success == 200){
							$.messager.alert("系统提示", "作废成功");
							$("#dlg").dialog("close");
		             		$("#roomChatlist").datagrid("reload");
		             	}else{
							$.messager.alert("系统提示", data.errorMsg);
		             	}
		             }
		       	});
			}
		})
	}

	function saveRoomChat() {
		$(".icon-ok").attr("disabled","true");
		setTimeout($(".icon-ok:disabled").removeAttr("disabled"),10000); 
		
		$.messager.confirm("系统提示","认真检查啦?",function(r){
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
							$("#roomChatlist").datagrid("reload");
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
		$("#dlg").form("clear");
		$("#dlg").dialog("close");
	}
	
	function myformatter(date){  
		var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
		var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
		+ (date.getMonth() + 1);
		var hor = date.getHours();
		var min = date.getMinutes();
		var sec = date.getSeconds();
		return date.getFullYear() + '-' + month + '-' + day+" "+hor+":"+min+":"+sec; 
	}
	
</script>
</head>
<body>
	<div>
		<table id="roomChatlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addRoomChat()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:editRoomChat()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:removeRoomChat()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">无效</a>
			</div>
		</div>

		<div id="dlg" class="easyui-dialog" style="width: 570px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>轮播信息：</td>
						<td>
							<input class="easyui-textbox" name="content" id="content" data-options="multiline:true" style="height:120px" data-options="disabled:false"/>
						</td>
					</tr>
					<tr>
						<td>开始时间：</td>
						<td><input class="easyui-datetimebox" style="width:180px;height:24px" name="starttime" id="starttime" size="20" /></td>
					</tr>
					<tr>
						<td>结束时间：</td>
						<td><input class="easyui-datetimebox" style="width:180px;height:24px" name="endtime" id="endtime" size="20" /></td>
					</tr>
					<tr>
						<td>有效：</td>
						<td><select id="isvalid" class="easyui-combobox" name="isvalid" panelHeight="auto" style="width: 145px">
								<option value="1">有效</option>
								<option value="0">无效</option>
						</select></td>
					</tr>
				</table>
				<input type="hidden" name="id" id="id" />
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveRoomChat()" class="easyui-linkbutton" iconCls="icon-ok">保存</a> 
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:closeDlg()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
