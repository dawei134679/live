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
<title>公会扶持列表</title>
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
		dataGrid = $("#supportlist").datagrid(
				{
					url : '../../union/getsupport',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '工会扶持列表',
					queryParams: {
						status : $('#status').combobox("getValue"),
						unionid : $('#unionid').combobox("getValue")
					},
					pagination : true,
			        rownumbers: true,
					striped : true,
			        singleSelect: true,
			        fit:true,
					nowrap : false,
			        fitColumns:true,
			        pageSize:20,
			        pageList:[20,50,100],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'unionid',
								title : '公会ID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'unionname',
								title : '公会名称',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'uid',
								title : '扶持号UID',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'nickname',
								title : '扶持号昵称',
								align : 'center',
								width : 120,
								sortable : false
							},
							{
								field : 'amount',
								title : '扶持金额(预订)',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'remarks',
								title : '备注',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'isvalid',
								title : '有效',
								align : 'center',
								width : 50,
								sortable : false,
								formatter:function (data){
									if(data == 1){
										return "有效";
									}else{
										return "<font color='red'>无效</font>";
									}
								}
							},
							{
								field : 'addtime',
								title : '添加时间',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'updtime',
								title : '更新时间',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'username',
								title : '操作者名称',
								align : 'center',
								width : 100,
								sortable : false
							}
							] ]
				})
				
				$("#uid").textbox({
					onChange:function(data){
						var uid = $.trim(data);
						if(uid == ""){
							return;
						}
						$.ajax({
				             type: "post",
				             dataType: "json",
				             url: "../../anchor/getNickName",
				             data: { uid:uid},
				             dataType: 'json', 
				             success: function(data) {
				             	if(data.success == 200){
				             		$("#nickname").textbox("setValue",data.nickname);
				             	}else{
									$.messager.alert("系统提示", data.msg);
				             	}
				             }
				       	});
					}
				});
	})
	
	function addSupportDialog(){

        $('#dlg').dialog('open').dialog('center').dialog('setTitle','设置扶持号');
        $("#fm").form("clear");
        url = '../../union/addsupport';
	}
	
	function openSupportDialog(){
		var selectedRows = $("#supportlist").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','修改扶持号');
        $("#fm").form("load", row);
        url = '../../union/editsupport';
    }
	
	function searchSupport() {
		$('#supportlist').datagrid('load', {
			status : $('#status').combobox("getValue"),
			unionid : $('#searchUnionid').combobox("getValue")
		});
	}
	
	function saveVersion() {

		$.messager.confirm("系统提示","确认要添加扶持号吗?",function(r){
			if(r){
				$("#fm").form("submit", {
					
					url : url,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(data) {
						var result = eval("("+data+")");
						if (result.errMsg) {
							$.messager.alert("系统提示", result.errMsg);
						}else {
							$.messager.alert("系统提示", "设置成功");
							$("#dlg").form("clear");
							$("#dlg").dialog("close");
							$("#supportlist").datagrid("reload");
						}
					}
				});
			}
		})
	}
</script>
</head>
<body style="margin: 5px;">
		<table id="supportlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addSupportDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a> &nbsp;&nbsp;
				<a href="javascript:openSupportDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			</div>
			
		<div>
			状态:&nbsp;<select id="status" class="easyui-combobox" name="status" panelHeight="auto" style="width: 145px">
								<option value="9" selected="selected">全部</option>
								<option value="1">启用</option>
								<option value="0">禁用</option>
				</select>
			&nbsp;选择公会:<input id=searchUnionid name="searchUnionid" class="easyui-combobox" required="true"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../../unionAnchor/getUnionNameList'"
    						/>
			&nbsp;&nbsp;<a href="javascript:searchSupport()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 670px; height: 450px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>公会ID:</td>
						<td>
							<input id=unionid name="unionid" class="easyui-combobox" required="true"  missingMessage="公会ID必填"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../../unionAnchor/getUnionNameList'"
    						/>
						</td>
					</tr>
					<tr>
						<td>扶持号UID:</td>
						<td>
							<input class="easyui-numberbox" name="uid" id="uid"  data-options="disabled:false" required="true" missingMessage="用户UID必填"/>
						</td>
					</tr>
					<tr>
						<td>扶持号名称:</td>
						<td>
							<input class="easyui-textbox" name="nickname" id="nickname"  data-options="readonly:true" />
						</td>
					</tr>
					<tr>
						<td>扶持号金额:</td>
						<td>
							<input class="easyui-numberbox" name="amount" id="amount"  data-options="disabled:false" required="true" missingMessage="扶持金额必填"/>
						</td>
					</tr>
					<tr>
						<td>状态:</td>
						<td><select id="isvalid" class="easyui-combobox" name="isvalid" panelHeight="auto" style="width: 145px">
								<option value="1">启用</option>
								<option value="0">禁用</option>
						</select></td>
					</tr>
					<tr>
						<td>备注:</td>
						<td>
							<input class="easyui-textbox" name="remarks" id="remarks"  data-options="disabled:false"  data-options="multiline:true" style="height:100px;width:400px" required="true" missingMessage="备注必填"/>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveVersion()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
</body>
</html>
