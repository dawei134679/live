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
		dataGrid = $("#versionlist").datagrid(
				{
					url : '../../operat/getVersionList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '上一版本',
					nowrap: false,
					rownumbers : true,
					singleSelect : true,
					toolbar : '#tb',
					columns : [ [
							{
								field : 'sys',
								title : '手机类型',
								align : 'center',
								width : 80,
								sortable : false,
								formatter : function(data) {
									if (data == 1) {
										return "<font color='red'>ios</font>";
									} else {
										return "android";
									}
								}
							},
							{
								field : 'ver',
								title : '版本号',
								align : 'center',
								width : 80,
								sortable : false
							},{
								field : 'isforce',
								title : '强制更新',
								align : 'center',
								width : 80,
								sortable : false,
								formatter : function(data) {
									if(data==0){
										return "否";
									}else{
										return "<font color='red'>是</font>";
									}
								}
							},{
								field : 'describtion',
								title : '更新内容',
								align : 'center',
								width : 400,
								sortable : false
							},{
								field : 'uploadUrl',
								title : '更新地址',
								align : 'center',
								width : 550,
								sortable : false
							},
							{
								field : 'updateTime',
								title : '更新时间',
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
	
	function addVersion(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加版本');
        $('#fm').form('clear');
        url = '../../operat/saveVersion';
    }
	
	function saveVersion() {
		$.messager.confirm("系统提示","确认推送?",function(r){
			if(r){
				$("#fm").form("submit", {
					url : url,
					onSubmit : function() {

						if ($('#ver').val().trim() == "") {
							$.messager.alert("系统提示", "请输入版本号");
							return false;
						}
						if ($('#describtion').val().trim() == "") {
							$.messager.alert("系统提示", "请输入更新内容");
							return false;
						}
						if ($('#uploadUrl').val().trim() == "") {
							$.messager.alert("系统提示", "请输入下载地址");
							return false;
						}
						
						if ($("#utime").textbox('getValue') == "") {
							$.messager.alert("系统提示", "请输入更新时间");
							return false;
						}
						
						return $(this).form("validate");
					},
					success : function(data) {
						var result = eval("("+data+")");
						if (result.success == 200) {
							$.messager.alert("系统提示", "保存成功");
							$("#dlg").dialog("close");
							$("#versionlist").datagrid("reload");
						} else if(result.success == 400){
							$.messager.alert("系统提示", result.errorMsg);
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
	
</script>
</head>
<body>
	<div>
		<table id="versionlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addVersion()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
			</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 670px; height: 550px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>手机大类：</td>
					</tr>
					<tr>
						<td>
							<select id="sys" class="easyui-combobox" name="sys" style="width:90px;" required="true">
								<option value="0">android</option>
								<option value="1">ios</option> 
							</select>
						</td>
					</tr>
					<tr>
						<td>版本号：(格式:x.x.x 例如 2.9.7)</td>
					</tr>
					<tr>
						<td>
							<input class="easyui-textbox" name="ver" id="ver"  data-options="disabled:false" required="true" missingMessage="版本号必填"/>
						</td>
					</tr>
					<tr>
						<td>是否强更：</td>
					</tr>
					<tr>
						<td>
							<select id="isforce" class="easyui-combobox" name="isforce" style="width:90px;" required="true">
								<option value="0">不是</option>
								<option value="1">是</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>更新内容：</td>
					</tr>
					<tr>
						<td>
							<input class="easyui-textbox" name="describtion" id="describtion" data-options="multiline:true" style="height:100px;width:500px" data-options="disabled:false" required="true" missingMessage="更新内容必填"/>
						</td>
					</tr>
					<tr>
						<td>更新地址：(需要http://开头)</td>
					</tr>
					<tr>
						<td>
							<input class="easyui-textbox" style="width:350px;height:24px" name="uploadUrl" id="uploadUrl" size="350" data-options="disabled:false" required="true" missingMessage="更新地址必填"/>
						</td>
					</tr>
					<tr>
						<td>更新时间：</td>
					</tr>
					<tr>
						<td>
							<input class="easyui-datetimebox" style="width:160px;height:24px" name="utime" id="utime" size="20"  data-options="disabled:false" required="true" missingMessage="更新时间必填"/>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveVersion()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
	</div>
</body>
</html>
