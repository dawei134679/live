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
<title>渠道列表</title>
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
		dataGrid = $("#channellist").datagrid(
				{
					url : '../../operat/getChannel',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '渠道列表',
					queryParams: {
						searchStatus:$('#searchStatus').combobox('getValue'),
						searchPlatform : $('#searchPlatform').combobox('getValue'),
						searchChannelName : $('#searchChannelName').textbox('getValue')
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
								title : 'ID',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'channelCode',
								title : '渠道号',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'channelName',
								title : '渠道名称',
								align : 'center',
								width : 120,
								sortable : false
							},
							{
								field : 'platform',
								title : '平台',
								align : 'center',
								width : 120,
								sortable : false,
								formatter:function(data){
									if(data == 0){
										return '小猪直播';
									}else if(data == 1){
										return '暴风秀场';
									}
								}
							},
							{
								field : 'loginport',
								title : '登录端',
								align : 'center',
								width : 120,
								sortable : false,
								formatter:function(data){
									if(data == 0){
										return '安卓';
									}else if(data==1){
										return 'IOS';
									}else if(data==2){
										return 'web端';
									}else if(data==3){
										return '安卓H5';
									}else if(data==4){
										return 'IOSH5';
									}
								}
							},
							{
								field : 'addtime',
								title : '添加时间',
								align : 'center',
								width : 150,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'edittime',
								title : '修改时间',
								align : 'center',
								width : 120,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'isvalid',
								title : '状态',
								align : 'center',
								width : 60,
								sortable : false,
								formatter:function(data){
									if(data == 1){
										return '启用';
									}else{
										return '禁用';
									}
								}
							},
							{
								field : 'username',
								title : '操作人',
								align : 'center',
								width : 100,
								sortable : false
							}
							] ]
				})
	})
	
	function addChannelDialog(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加渠道');
        $("#fm").form("clear");
        url = '../../operat/addChannel';
	}

	function editChannelDialog() {
		var selectedRows = $("#channellist").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "编辑渠道");
		$("#fm").form("clear");
		$("#fm").form("load", row);
		url = "../../operat/editChannel";
	}
	
	function searchChannel() {
		$('#channellist').datagrid('load', {
			searchStatus:$('#searchStatus').combobox('getValue'),
			searchPlatform : $('#searchPlatform').combobox('getValue'),
			searchChannelName : $('#searchChannelName').textbox('getValue')
		});
	}
	
	function saveChannel() {

		$.messager.confirm("系统提示","确认添加?",function(r){
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
							$("#fm").form("clear");
							$("#dlg").dialog("close");
							$("#channellist").datagrid("reload");
						}
					}
				});
			}
		})
	}
	//清空查询搜索条件
	function clear(){
		$("#searchChannelName").textbox("setValue","");
		$("#searchStatus").combobox("setValue","1");
		$("#searchPlatform").combobox("setValue","0");
		
	}
	function cancleChannel(){
		$("#dlg").dialog("close");
	}
</script>
</head>
<body style="margin: 5px;">
		<table id="channellist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addChannelDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:editChannelDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			</div>
			
		<div>
		&nbsp;<!-- 选择渠道:<input id=searchChannelCode name="searchChannelCode" class="easyui-combobox" required="true"
						data-options="valueField: 'channelCode',
   									textField: 'channelName',
   									url: '../../operat/getChannelForSelect'"
   						/> -->
   						状态:<select id="searchStatus" class="easyui-combobox" name="status" style="width:200px;">
							    <option value="1">启用</option>
							    <option value="0">禁用</option>
							</select>
						平台:<select id="searchPlatform" class="easyui-combobox" name="platform" style="width:200px;">
							   <option value="0">小猪直播</option>
							   <option value="1">暴风秀场</option>
							</select>
						渠道名:<input id="searchChannelName" name="channelName" class="easyui-textbox"/>
		&nbsp;&nbsp;<a href="javascript:searchChannel()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:clear()" class="easyui-linkbutton" iconCls="icon-search" plain="true">清空</a>
		</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<input type="hidden" id="id" name="id"/>
					<tr>
						<td>渠道号:</td>
						<td>
							<input class="easyui-textbox" name="channelCode" id="channelCode" required="true"/>
						</td>
					</tr>
					<tr>
						<td>渠道名称:</td>
						<td>
							<input id="channelName" name="channelName" class="easyui-textbox" required="true"/>
						</td>
					</tr>
					<tr>
						<td>平台:</td>
						<td><select id="platform" class="easyui-combobox" editable="true" name="platform" panelHeight="auto" style="width: 145px">
								<option value="0">小猪直播</option>
								<option value="1">暴风秀场</option>
						</select></td>
					</tr>
					<tr>
						<td>登录端:</td>
						<td><select id="loginport" class="easyui-combobox" editable="true" name="loginport" panelHeight="auto" style="width: 145px">
								<option value="0">安卓</option>
								<option value="1">IOS</option>
								<option value="2">web端</option>
								<option value="3">安卓H5</option>
								<option value="4">IOSH5</option>
						</select></td>
					</tr>
					<tr>
						<td>状态:</td>
						<td><select id="isvalid" class="easyui-combobox" editable="true" name="isvalid" panelHeight="auto" style="width: 145px">
								<option value="1">启用</option>
								<option value="0">禁用</option>
						</select></td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveChannel()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
			<a href="javascript:cancleChannel()" class="easyui-linkbutton" iconCls="icon-cancle">关闭</a>
		</div>
</body>
</html>
