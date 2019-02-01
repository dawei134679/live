<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>IOS版本控制开关管理</title>
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
		dataGrid = $("#iosVersionList").datagrid({
			url : '../../version/getVersionList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : 'IOS版本控制开关管理',
			pagination : true,
			rownumbers : true,
			striped : true,
			singleSelect : true,
			fit : true,
			fitColumns : true,
			pageSize : 20,
			pageList : [ 20, 50, 100 ],
			toolbar : '#tb',
			columns : [[
				{field : 'version',title : '版本号',align : 'center',width : 80,sortable : false},
				{field : 'pay',title : '支付',align : 'center',width : 80,sortable : false,formatter:function(v,d){
					var ary = ['<font color=red>已禁用</font>','<font color=green>已启用</font>'];
					return ary[v];
				}},
				{field : 'payother',title : '其他支付',align : 'center',width : 80,sortable : false,formatter:function(v,d){
					var ary = ['<font color=red>已禁用</font>','<font color=green>已启用</font>'];
					return ary[v];
				}},
				{field : 'giftshow',title : '礼物',align : 'center',width : 80,sortable : false,formatter:function(v,d){
					var ary = ['<font color=red>已禁用</font>','<font color=green>已启用</font>'];
					return ary[v];
				}},
				{field : 'tixian',title : '提现',align : 'center',width : 80,sortable : false,formatter:function(v,d){
					var ary = ['<font color=red>已禁用</font>','<font color=green>已启用</font>'];
					return ary[v];
				}},
				{field : 'gameshow',title : '游戏',align : 'center',width : 80,sortable : false,formatter:function(v,d){
					var ary = ['<font color=red>已禁用</font>','<font color=green>已启用</font>'];
					return ary[v];
				}},
				{field : 'adsshow',title : '广告',align : 'center',width : 80,sortable : false,formatter:function(v,d){
					var ary = ['<font color=red>已禁用</font>','<font color=green>已启用</font>'];
					return ary[v];
				}},
				{field : 'audit',title : '是否审核版本',align : 'center',width : 80,sortable : false,formatter:function(v,d){
					var ary = ['<font color=green>否</font>','<font color=red>是</font>'];
					return ary[v];
				}}
			]]
		})
	})
	
	function saveIosVersion() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("("+data+")");
				if (result.code == '200') {
					$("#dlg").form("clear");
					$("#dlg").dialog("close");
					$("#iosVersionList").datagrid("reload");
				}else {
					$.messager.alert("系统提示", result.msg);
				}
			}
		});
	}
	
	function addIOSVersion() {
		url = "../../version/saveIosVersion";
		$('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增');
		$("#fm").form("clear");
	}
	
	function editIOSVersion() {
		$("#fm").form("clear");
		url = "../../version/updateIosVersion";
		var rowSel = $("#iosVersionList").datagrid('getSelected');
		if (rowSel == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		$("#oldVersion").val(rowSel.version);
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改");
		$("#fm").form("load", rowSel);
	}
	
	function delIOSVersion() {
		var row = $("#iosVersionList").datagrid('getSelected');
		if (row == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		$.messager.confirm("系统提示", "是否删除?", function(r) {
			if(r){
				var params = {
						"id" : row.id,
						"version" : row.version
					};
					var url = '../../version/delIosVersion';
					$.post(url, params, function(result) {
						var data = eval("(" + result + ")");
						if (data.code == 200) {
							$.messager.alert("系统提示", "删除成功");
						} else {
							$.messager.alert("系统提示", "删除失败");
						}
						$("#iosVersionList").datagrid('reload');
					});
			}
		});
	}
	
	function refreshRedisCache() {
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			if (r) {
				$.post("../../version/refreshRedisCache", {}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "更新缓存成功");
						$("#dg").datagrid("reload");
					} else {
						$.messager.alert('系统提示', "更新缓存失败");
					}
				}, "json");
			}
		});
	}
</script>
</head>
<body style="margin: 5px;">
		<table id="iosVersionList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addIOSVersion()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
				<a href="javascript:editIOSVersion()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
				<a href="javascript:delIOSVersion()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
				<a href="javascript:refreshRedisCache()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">刷新缓存</a>
			</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 500px; height: 300px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>版本号：</td>
						<td>
							<input id="id" name="id" type="hidden" />
							<input id="oldVersion" name="oldVersion" type="hidden"/>
							<input class="easyui-textbox" name="version" id="version"  data-options="" required="true" missingMessage="版本号必填"/>(格式:x.x.x 例如 2.9.7)
						</td>
					</tr>
					<tr>
						<td>支付：</td>
						<td>
							<input type="radio" name="pay" value="1" class="btn-valid"/>启用
							&nbsp;&nbsp;
							<input type="radio" name="pay" value="0" class="btn-invalid" checked="checked"/>关闭
						</td>
					</tr>
					<tr>
						<td>其他支付：</td>
						<td>
							<input type="radio" name="payother" value="1" class="btn-valid"/>启用
							&nbsp;&nbsp;
							<input type="radio" name="payother" value="0" class="btn-invalid" checked="checked"/>关闭
						</td>
					</tr>
					<tr>
						<td>礼物：</td>
						<td>
							<input type="radio" name="giftshow" value="1" class="btn-valid"/>启用
							&nbsp;&nbsp;
							<input type="radio" name="giftshow" value="0" class="btn-invalid" checked="checked"/>关闭
						</td>
					</tr>
					<tr>
						<td>提现：</td>
						<td>
							<input type="radio" name="tixian" value="1" class="btn-valid"/>启用
							&nbsp;&nbsp;
							<input type="radio" name="tixian" value="0" class="btn-invalid" checked="checked"/>关闭
						</td>
					</tr>
					<tr>
						<td>游戏：</td>
						<td>
							<input type="radio" name="gameshow" value="1" class="btn-valid"/>启用
							&nbsp;&nbsp;
							<input type="radio" name="gameshow" value="0" class="btn-invalid" checked="checked"/>关闭
						</td>
					</tr>
					<tr>
						<td>广告：</td>
						<td>
							<input type="radio" name="adsshow" value="1" class="btn-valid"/>启用
							&nbsp;&nbsp;
							<input type="radio" name="adsshow" value="0" class="btn-invalid" checked="checked"/>关闭
						</td>
					</tr>
					<tr>
						<td>审核版本：</td>
						<td>
							<input type="radio" name="audit" value="1" class="btn-valid"/>是
							&nbsp;&nbsp;
							<input type="radio" name="audit" value="0" class="btn-invalid" checked="checked"/>否
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveIosVersion()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
</body>
</html>
