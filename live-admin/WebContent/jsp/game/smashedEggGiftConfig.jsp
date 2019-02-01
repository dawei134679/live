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
<title>砸蛋游戏礼物配置</title>
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
		dataGrid = $("#smashedEggGiftConfigList").datagrid({
			url : '../../smashedEggGiftConfig/smashedEggGiftConfigList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '砸蛋礼物配置列表',
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
				{field : 'giftName',title : '礼物名称',align : 'center',width : 80,sortable : false},
				{field : 'hammerType',title : '锤子类型',align : 'center',width : 50,sortable : false,formatter:function(d){
					var arg = ['','木锤','铁锤','金锤'];
					return arg[d];
				}},
				{field : 'probability',title : '中奖概率',align : 'center',width : 50,sortable : false},
				{field : 'giftNum',title : '中奖礼物数',align : 'center',width : 50,sortable : false}, 
				{field : 'isfirstprize',title : '是否头奖',align : 'center',width : 50,sortable : false,formatter:function(d){
					if(d == 1){
						return '是';
					}
					return '否';
				}}
			]]
		});
	});

	function addEggGiftDialog() {
		url = "../../smashedEggGiftConfig/saveSmashedEggGiftConfig";
		$('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增');
		$("#fm").form("clear");
		$('#hammerType').combobox('setValue',1);
		$('#isfirstprize').combobox('setValue',0);
		//$('#giftNum').textbox('setValue',1);
	}

	function updateEggGiftDialog() {
		$("#fm").form("clear");
		url = "../../smashedEggGiftConfig/updateSmashedEggGiftConfig";
		var rowSel = $("#smashedEggGiftConfigList").datagrid('getSelected');
		if (rowSel == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		var row = $.extend({}, rowSel);
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改");
		$("#fm").form("load", row);
	}

	function doSearch() {
		$('#smashedEggGiftConfigList').datagrid('load', {
			hammerType : $("#search_hammerType").val()
		});
	}

	function saveForm() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("(" + data + ")");
				if (!result.result) {
					$.messager.alert("系统提示", result.msg);
					return;
				} else {
					$.messager.alert("系统提示", result.msg);
					$("#fm").form("clear");
					$("#dlg").dialog("close");
					$("#smashedEggGiftConfigList").datagrid('reload');
				}
			},
			error : function(result) {
				alert(result);
			}
		});
	}

	function delEggGiftDialog() {
		var row = $('#smashedEggGiftConfigList').datagrid('getSelected');
		if(!row){
			$.messager.alert("系统提示", "请选择要删除的数据");
			return;
		}
		$.messager.confirm("系统提示", "确定要删除吗?", function(r) {
			if (r) {
				$.post("../../smashedEggGiftConfig/delSmashedEggGiftConfig?id="+row.id, {}, function(result) {
					if (result.result) {
						$.messager.alert("系统提示", "删除成功");
						$("#smashedEggGiftConfigList").datagrid("reload");
					} else {
						$.messager.alert('系统提示', "删除失败");
					}
				}, "json");
			}
		});
	}
	
	function reSmashedEggGiftConfigRedis() {
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			if (r) {
				$.post("../../smashedEggGiftConfig/reSmashedEggGiftConfigRedis", {}, function(result) {
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
	var giftListGrid;
	function selectGift(){
		giftListGrid = $("#giftList").datagrid(
				{
					url : '../../giftInfo?method=getList',
					width : 550,
					height : 435,
					title : '',
					queryParams: {
						isvalid:1,
						type:'2,3',
						subtype:'3,5,9'
					},
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 15,
					pageList : [ 15, 50, 100 ],
					//toolbar : '#tb',
					columns : [ [
							{field : 'cb',checkbox : 'true',align : 'center',sortable : false},
							{field : 'gid',title : '礼物gid',align : 'center',sortable : false},
							{field : 'gname',title : '礼物名称',align : 'center',sortable : false},
							{field : 'type',title : '礼物大类',align : 'center',sortable : false,formatter:function(data){
								if(data == 1){
									return "易耗品";
								}else if(data == 2){
									return "房间内|背包";
								}else if(data == 3){
									return "时效道具";
								}else{
									return "未知";
								}
							}},
							{field : 'subtype',title : '礼物小类',align : 'center',sortable : false,formatter:function(data){
								if(data == 0){
									return "普通礼物";
								}else if(data == 1){
									return "弹幕";
								}else if(data == 2){
									return "喇叭";
								}else if(data == 3){
									return "VIP";
								}else if(data == 4){
									return "贵族";
								}else if(data == 5){
									return "座驾";
								}else if(data == 6){
									return "徽章";
								}else if(data == 7){
									return "守护";
								}else if(data == 8){
									return "商城道具";
								}else if(data == 9){
									return "背包";
								}else{
									return "未知";
								}
							}},
							{field : 'gprice',title : '礼物价值',align : 'center',sortable : false},
							{field : 'wealth',title : '财富值',align : 'center',sortable : false},
							{field : 'credit',title : '魅力值',align : 'center',sortable : false}
						]]
				});
		$('#giftListdlg').dialog('open').dialog('center').dialog('setTitle', '选择礼物');
	}
	function returnSelGift(){
		var selectedRows = $("#giftList").datagrid('getSelections');
		if (selectedRows.length == 1) {
			$("#giftName").textbox('setValue',selectedRows[0].gname);
			$("#giftId").val(selectedRows[0].gid);
			$("#giftType").val(selectedRows[0].subtype);
		}
		$("#giftListdlg").dialog("close");
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="smashedEggGiftConfigList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<a href="javascript:addEggGiftDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="btn-save">新增</a> 
			<a href="javascript:updateEggGiftDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="btn-update">修改</a> 
			<a href="javascript:delEggGiftDialog()" class="easyui-linkbutton invalid" iconCls="icon-remove" plain="true">删除</a>
			<a href="javascript:reSmashedEggGiftConfigRedis()" class="easyui-linkbutton invalid" iconCls="icon-reload" plain="true">更新缓存</a>
			<a style="font-size:18px;color:red;border:0;" class="easyui-linkbutton l-btn l-btn-small l-btn-plain">
				<div style="font-size:18px;">如果删除礼物列表中的礼物，请在此列表同时删除该礼物！！！</div>
			</a>
		</div>
		<div>
			锤子类型：&nbsp;
			<select
				id="search_hammerType">
				<option value="">--全部--</option>
				<option value="1">木锤</option>
				<option value="2">铁锤</option>
				<option value="3">金锤</option>
			</select>
			&nbsp;&nbsp;
			<a href="javascript:doSearch()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog"
		style="width: 570px; height: 450px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellspacing="5px;">
				<tr>
					<td>礼物:</td>
					<td>
						<input id="id" name="id" type="hidden" />
						<input id="giftId" name="giftId" type="hidden" /> 
						<input id="giftType" name="giftType" type="hidden" /> 
						<input id="giftName" name="giftName" type="text" class="easyui-textbox" onclick="selectGift();" readonly required="true"/>
						<input type="button" value="选择" onclick="selectGift();"/>
					</td>
				</tr>
				<tr>
					<td>中奖数量:</td>
					<td>
						<input id="giftNum" name="giftNum" class="easyui-numberbox" required="true" data-options="min:1" value="1"/>
					</td>
				</tr>
				<tr>
					<td>锤子类型:</td>
					<td>
						<select id="hammerType" name="hammerType" class="easyui-combobox" style="width:100px;">
							<option value="1" selected="selected">木锤</option>
							<option value="2">铁锤</option>
							<option value="3">金锤</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>中奖概率:</td>
					<td>
						<input id="probability" name="probability" class="easyui-numberbox" onClick required="true" data-options="min:0,max:1,precision:2" />[0~1]
					</td>
				</tr>
				<tr>
					<td>是否头奖:</td>
					<td>
						<select id="isfirstprize" name="isfirstprize" class="easyui-combobox" style="width:100px;">
							<option value="0" selected="selected">否</option>
							<option value="1">是</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveForm()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	</div>
	
	<div id="giftListdlg" class="easyui-dialog"
		style="width: 570px; height:515px; padding:2px" closed="true"
		buttons="#giftList-buttons">
		<table id="giftList"></table>
	</div>
	<div id="giftList-buttons">
		<a href="javascript:returnSelGift()" class="easyui-linkbutton" iconCls="icon-ok">确定</a>
	</div>
</body>
</html>