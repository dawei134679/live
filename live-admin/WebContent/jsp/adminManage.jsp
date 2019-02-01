<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>管理员管理</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;

	$(function() {
		dataGrid = $("#adminList").datagrid(
				{
					url : '../manageuser?method=getList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '管理员列表',
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{field : 'cb',checkbox : 'true',align : 'center',width : 40,sortable : false},
							{field : 'uid',title : '管理员uid',align : 'center',width : 60,sortable : false},
							{field : 'username',title : '管理员帐号',align : 'center',width : 100,sortable : false},
							{field : 'type',title : '类型',align : 'center',sortable : false,width : 80,formatter : function(data) {
								var ary = ["管理员","钻石公会","铂金公会","黄金公会","家族助理","战略合作中心"];
								return ary[data];
							}}, 
							{field : 'role_name',title : '角色名称',align : 'center',width : 120,sortable : false},
							{field : 'isvalid',title : '有效',align : 'center',sortable : false,width : 50,formatter : function(data) {
								if (data) {
									return "有效";
								} else {
									return "<font color='red'>无效</font>";
								}
							}}, 
							{field : 'reg_time',title : '创建时间',align : 'center',sortable : false,width : 130,formatter : function(data) {
									return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : ""
							}},
							{field : 'login_time',title : '最后登录时间',align : 'center',sortable : false,width : 130,formatter : function(data) {
									return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : ""
							}},
							{field : 'createUid',title : '创建人uid',align : 'center',sortable : false} 
						]]
				})
	})
	function closeManageDialog(){
		$("#dlg").dialog("close");
	}
	
	function openAdminAddDialog() {
		//清空用户名和密码
		$("#username").val("");
		$("#password").val("");
		$("#usernameTip").hide();
		$("#password").textbox({ required:true });
		$("#username").removeAttr('readonly');
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "添加管理员");
		//获取角色列表
	    $("#role_id").combobox({
				url:"../adminRole?method=getAllRoleList",
				valueField:'role_id',
				textField:'role_name',
				onLoadSuccess: function (data) {
		            if (data) {
		                $('#role_id').combobox('setValue',data[0].role_id);
		            }
				}
		});
		url = "../manageuser?method=add";
	}

	function saveManage() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				if ($('#username').val().trim() == "") {
					$.messager.alert("系统提示", "请输入用户名");
					return false;
				}
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("("+data+")");
				if (result.errorCode) {
					$.messager.alert("系统提示", "保存成功");
					$("#dlg").dialog("close");
					$("#adminList").datagrid("reload");
				} else {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				}
			}
		});
	}
	function openAdminModifyDialog() {
		var selectedRows = $("#adminList").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "管理员信息");
		$("#usernameTip").show();
		$("#username").attr('readonly','readonly');
		$("#password").textbox({ required:false });
		//获取角色列表
	    $("#role_id").combobox({
				url:"../adminRole?method=getAllRoleList",
				valueField:'role_id',
				textField:'role_name'
		});
		$("#fm").form("load", row);
		$("#password").textbox('setValue','');
		url = "../manageuser?method=update&uid=" + row.uid;
	}
	
	//删除用户（不包含关联关系）
	function removeUser(){
		var row = $('#adminList').datagrid('getSelected');
		if (row){
			$.messager.confirm("确认",'删除【'+row.username+'】(不删除关联关系！)？',function(r){
                if (r){
					$.ajax({
						url : '../manageuser?method=remove',
						data:{uid:row.uid},
						success:function(data){
							var result = eval('('+data+')');
							if(result.errorCode){
								$("#adminList").datagrid("reload");
							}
							$.messager.alert("提示",result.errorMsg);
						}
					});
                }
			});
		}else{
			$.messager.alert("提示","请先选中一行要删除的数据");
		}
	}
	
	//搜索
	function searchEvent(){
		$('#adminList').datagrid('load', {
			username : $('#s_username').textbox("getValue")
		});
	}
</script>
</head>
<body>
	<div>
		<table id="adminList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;管理员账号:&nbsp;<input id="s_username" name="s_username" class="easyui-textbox" />
				&nbsp;<a href="javascript:searchEvent()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
				&nbsp;&nbsp;&nbsp;
				<a href="javascript:openAdminAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
				<a href="javascript:openAdminModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
				<a href="javascript:removeUser();" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			</div>
		</div>

		<div id="dlg" class="easyui-dialog"
			style="width: 570px; height: 350px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>用户名：</td>
						<td><input type="text" name="username" id="username"
							class="easyui-validatebox" required="true"/><label style="color:red;" id="usernameTip">用户名不能修改</label></td>
					</tr>
					<tr>
						<td>密码：</td>
						<td>
							<input type="password" name="password" id="password" class="easyui-textbox" required="true"/>
							<label style="color:red;">不修改则不填写</label>
						</td>
					</tr>
					<tr>
						<td>角色：</td>
						<td><select id="role_id" class="easyui-combobox"
							editable="false" name="role_id" panelHeight="auto"
							style="width: 155px">
						</select></td>
					</tr>
					<tr>
						<td>类型：</td>
						<td>
							<select id="type" class="easyui-combobox" editable="false" name="type" panelHeight="auto" style="width: 155px">
								<option value="0">管理员</option>
								<option value="5">战略合作中心</option>
								<option value="1">钻石公会</option>
								<option value="2">铂金公会</option>
								<option value="3">黄金公会</option>
								<option value="4">家族助理</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>有效：</td>
						<td>
							<select id="isvalid" class="easyui-combobox" editable="false" name="isvalid" panelHeight="auto" style="width: 155px">
								<option value="1">有效</option>
								<option value="0">无效</option>
							</select>
						</td>
					</tr>
				</table>
				<input type="hidden" name="uid" id="uid" />
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveManage()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closeManageDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
