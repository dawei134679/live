<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>礼物列表</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	
	$(function() {
		dataGrid = $("#giftList").datagrid(
				{
					url : '../giftInfo?method=getList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '礼物列表',
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'cb',
								checkbox : 'true',
								align : 'center',
								sortable : false
							},
							{
								field : 'gid',
								title : '礼物gid',
								align : 'center',
								sortable : false
							},
							{
								field : 'gname',
								title : '礼物名称',
								align : 'center',
								sortable : false
							},
							{
								field : 'type',
								title : '礼物大类',
								align : 'center',
								sortable : false,
								formatter:function(data){
									if(data == 1){
										return "易耗品";
									}else if(data == 2){
										return "房间内|背包";
									}else if(data == 3){
										return "时效道具";
									}else{
										return "未知";
									}
								}
							},
							{
								field : 'subtype',
								title : '礼物小类',
								align : 'center',
								sortable : false,
								formatter:function(data){
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
								}
							},
							{
								field : 'gprice',
								title : '礼物价值',
								align : 'center',
								sortable : false
							},
							{
								field : 'gpriceaudit',
								title : '审核礼物价值',
								align : 'center',
								sortable : false
							},
							{
								field : 'wealth',
								title : '财富值',
								align : 'center',
								sortable : false
							},
							{
								field : 'credit',
								title : '魅力值',
								align : 'center',
								sortable : false
							},
							{
								field : 'charm',
								title : '声援值',
								align : 'center',
								sortable : false
							},
							{
								field : 'gcover',
								title : '封面名称',
								align : 'center',
								sortable : false
							}, {
								field : 'gtype',
								title : 'App效果类型',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									if(data == 0){
										return "其他";
									}else if(data == 1){
										return "礼物消息连";
									}else if(data == 2){
										return "半屏带离子效果";
									}else if(data == 3){
										return "半屏无离子效果";
									}else if(data == 4){
										return "动画效果(全屏)";
									}else if(data == 5){
										return "半屏带文字效果";
									}else{
										return "未知";
									}
								}
							} , {
								field : 'gpctype',
								title : 'PC效果类型',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									if(data == 0){
										return "其他";
									}else if(data == 1){
										return "静态";
									}else if(data == 2){
										return "SWF";
									}else{
										return "未知";
									}
								}
							} , {
								field : 'gframeurl',
								title : '小动画zip地址',
								align : 'center',
								sortable : false
							} , {
								field : 'gframeurlios',
								title : 'IOS小动画zip地址',
								align : 'center',
								sortable : false
							}, {
								field : 'simgs',
								title : '小动画数',
								align : 'center',
								sortable : false
							}, {
								field : 'bimgs',
								title : '大动画数',
								align : 'center',
								sortable : false
							}, {
								field : 'pimgs',
								title : '离子数',
								align : 'center',
								sortable : false
							}, {
								field : 'gnumtype',
								title : '礼物数组',
								align : 'center',
								sortable : false
							}, {
								field : 'gduration',
								title : '展示时间',
								align : 'center',
								sortable : false
							}, {
								field : 'gver',
								title : '礼物版本',
								align : 'center',
								sortable : false
							}, {
								field : 'sver',
								title : '资源版本',
								align : 'center',
								sortable : false
							}, {
								field : 'icon',
								title : '礼物图标',
								align : 'center',
								sortable : false
							}, {
								field : 'useDuration',
								title : '时长(天)',
								align : 'center',
								sortable : false
							}, {
								field : 'category',
								title : 'PC分类',
								align : 'center',
								sortable : false,
								formatter:function(data){
									if(data == 0){
										return "其他";
									}else if(data == 1){
										return "热门";
									}else if(data == 2){
										return "豪华";
									}else if(data == 3){
										return "活动";
									}else if(data == 4){
										return "专属";
									}else{
										return "未知";
									}
								}
							}, {
								field : 'skin',
								title : '资源指向gid',
								align : 'center',
								sortable : false
							},{
								field : 'isshow',
								title : '显示',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									if(data){
										return "是";
									}else{
										return "否";
									}
								}
							}, {
								field : 'isvalid',
								title : '有效',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									if(data){
										return "是";
									}else{
										return "否";
									}
								}
							}, {
								field : 'gsort',
								title : '排序',
								align : 'center',
								sortable : false
							}, {
								field : 'createAt',
								title : '创建时间',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							}] ]
				})
				
		$("#type").combobox({
			onChange: function () {
				var type = $('#type').combobox('getValue');
				if(type==2){
					$("#charm").removeAttr("readonly");
				}else{
					$("#charm").attr("readonly","readonly");
				} 
			}
		});
	});

	function closeManageDialog(){
		$("#fm").form("clear");
		$("#dlg").dialog("close");
	}
	
	function openGiftAddDialog() {
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "添加新礼物");
		url = "../giftInfo?method=add";
		$("#type").combobox({
			onChange: function () {
				var type = $('#type').combobox('getValue');
				if(type==2){
					$("#charm").removeAttr("readonly");
				}else{
					$("#charm").attr("readonly","readonly");
				} 
			}
		});
	}
	
	function saveManage() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(result) {
				var result = eval("("+result+")");
				var	errorMsg = result.errorMsg;
				if (errorMsg) {
					$.messager.alert("系统提示", errorMsg);
					return;
				} else {
					$.messager.alert("系统提示", "保存成功");
					$("#dlg").dialog("close");
					$("#giftList").datagrid("reload");
				}
			}
		});
	}
	
	function openGiftModifyDialog() {
		var selectedRows = $("#giftList").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改礼物信息");
		if(row.isshow){row.isshow = 1} else{row.isshow = 0}
		if(row.isvalid){row.isvalid = 1} else {row.isvalid = 0}
		$("#fm").form("load", row);
		url = "../giftInfo?method=update&gid=" + row.gid;
	}
	
	function searchGift(){
		$("#giftList").datagrid("load",{
			gname:$("#gName").val(),
			type:$("#gType").val(),
			isvalid:$("#isvalid").val(),
			isshow:$("#isshow").val()
		});
	}
</script>
</head>
<body>
	<div>
		<table id="giftList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:openGiftAddDialog()" class="easyui-linkbutton"
					iconCls="icon-add" plain="true">添加</a> <a
					href="javascript:openGiftModifyDialog()" class="easyui-linkbutton"
					iconCls="icon-edit" plain="true">修改</a>
			</div>
			<div>
				礼物名称:&nbsp;
				<input id="gName" /> &nbsp;&nbsp;
				礼物大类：&nbsp;
				<select id="gType">
					<option value="">--全部--</option>
					<option value="1">易耗品</option>
					<option value="2">房间内背包</option>
					<option value="3">时效道具</option>
				</select>&nbsp;
				是否显示：&nbsp;
				<select id="isshow">
					<option value="">--全部--</option>
					<option value="1">显示</option>
					<option value="0">不显示</option>
				</select>&nbsp;
				是否有效：&nbsp;
				<select id="isvalid">
					<option value="">--全部--</option>
					<option value="1">有效</option>
					<option value="0">无效</option>
				</select>
				 &nbsp;&nbsp;<a href="javascript:searchGift()"
					class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>

		<div id="dlg" class="easyui-dialog"
			style="width: 670px; height: 700px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td align="right">礼物名称：</td>
						<td><input type="text" name="gname" id="gname"
							class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">礼物大类：</td>
						<td><select id="type" class="easyui-combobox" editable="false" name="type" panelHeight="auto" style="width: 155px" required="true">
								<option value="0">请选择...</option>
								<option value="1">易耗品(弹幕、喇叭)</option>
								<option value="2">房间内背包(可送出)</option>
								<option value="3">时效道具</option>
						</select></td>
					</tr>
					<tr>
						<td align="right">礼物小类：</td>
						<td><select id="subtype" class="easyui-combobox" editable="false" name="subtype" panelHeight="auto" style="width: 155px" required="true">
								<option value="-1">请选择...</option>
								<option value="0">普通礼物</option>
								<option value="1">弹幕</option>
								<option value="2">喇叭</option>
								<option value="3">VIP</option>
								<option value="4">贵族</option>
								<option value="5">座驾</option>
								<option value="6">徽章</option>
								<option value="7">守护</option>
								<option value="8">商城道具</option>
								<option value="9">背包</option>
						</select>
						</td>
					</tr>
					<tr>
						<td align="right">礼物价格：</td>
						<td><input type="text" name="gprice" id="gprice" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">审核礼物价格：</td>
						<td><input type="text" name="gpriceaudit" id="gpriceaudit" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">财富值：</td>
						<td><input type="text" name="wealth" id="wealth" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">魅力值：</td>
						<td><input type="text" name="credit" id="credit" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">声援值:（<font color ="red">房间内背包礼物必填</font>）：</td>
						<td><input type="text" name="charm" id="charm" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td align="right">封面名称（<font color ="red">有资源必填</font>）：</td>
						<td><input type="text" name="gcover" id="gcover" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td align="right">App效果类型[gtype]：</td>
						<td><select id="gtype" class="easyui-combobox" editable="false" name="gtype" panelHeight="auto" style="width: 155px">
								<option value="">请选择...</option>
								<option value="1">礼物消息连 </option>
								<option value="2">半屏带离子效果</option>
								<option value="3">半屏无离子效果</option>
								<option value="4">动画效果(全屏)</option>
								<option value="5">半屏带文字效果</option>
								<option value="0">其他</option>
						</select></td>
					</tr>
					<tr>
						<td align="right">PC效果类型[gpctype]：</td>
						<td><select id="gpctype" class="easyui-combobox" editable="false" name="gpctype" panelHeight="auto" style="width: 155px">
								<option value="">请选择...</option>
								<option value="1">静态 </option>
								<option value="2">SWF</option>
								<option value="0">其他</option>
						</select></td>
					</tr>
					<tr>
						<td align="right">PC分类[category]：</td>
						<td><select id="category" class="easyui-combobox" editable="false" name="category" panelHeight="auto" style="width: 155px">
								<option value="">请选择...</option>
								<option value="1">热门</option>
								<option value="2">豪华</option>
								<option value="3">活动</option>
								<option value="4">专属</option>
								<option value="0">其他</option>
						</select></td>
					</tr>
					<tr>
						<td align="right">zip地址：</td>
						<td><input type="text" name="gframeurl" id="gframeurl" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td align="right">ios zip地址：</td>
						<td><input type="text" name="gframeurlios" id="gframeurlios" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td align="right">小动画数量：</td>
						<td><input type="text" name="simgs" id="simgs" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">大动画数量：</td>
						<td><input type="text" name="bimgs" id="bimgs" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">离子数量：</td>
						<td><input type="text" name="pimgs" id="pimgs" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">礼物数组：</td>
						<td><input type="text" name="gnumtype" id="gnumtype" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">展示时间(单位：秒)：</td>
						<td><input type="text" name="gduration" id="gduration" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td align="right">排序(越小越靠前)：</td>
						<td><input type="text" name="gsort" id="gsort" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">资源指向gid[skin]：</td>
						<td><input type="text" name="skin" id="skin" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td align="right">礼物图标[Icon]：</td>
						<td><input type="text" name="icon" id="icon" class="easyui-validatebox"/></td>
					</tr>
					<tr>
						<td align="right">礼物有效时长[useDuration]：</td>
						<td><input type="text" name="useDuration" id="useDuration" class="easyui-numberbox" value="0"/></td>
					</tr>
					<tr>
						<td align="right">是否显示：</td>
						<td><select id="isshow" class="easyui-combobox" editable="false" name="isshow" panelHeight="auto" style="width: 155px">
								<option value="">请选择...</option>
								<option value="1">有效</option>
								<option value="0">无效</option>
						</select></td>
					</tr>
					<tr>
						<td align="right">是否有效：</td>
						<td><select id="isvalid" class="easyui-combobox" editable="false" name="isvalid" panelHeight="auto" style="width: 155px">
								<option value="">请选择...</option>
								<option value="1">有效</option>
								<option value="0">无效</option>
						</select></td>
					</tr>
				</table>
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