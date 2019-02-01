<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>直播间游戏配置</title>
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	$(function() {
		doGetGameCarSettingList();
		doGetGameDollSetting();
		doGetSmashedEggConfig();
	});

	function doGetGameCarSettingList() {
		var url = '../../gcs/gameCarSettingList';
		$.post(url, function(result) {
			var data = eval("(" + result + ")");
			if (data[0]) {
				$("#cid").val(data[0].id);
				$("#probability1").val(data[0].probability1);
				$("#probability2").val(data[0].probability2);
				$("#probability3").val(data[0].probability3);
				$("#gameCommission").val(data[0].gameCommission);
				$("#roomInformMoney").val(data[0].roomInformMoney);
				$("#platformInformMoney").val(data[0].platformInformMoney);
			} else {
				$.messager.alert("系统提示", "请填写汽车押注游戏配置");
			}
		})
	}

	function saveGameCarConfig() {
		var url;
		var params = {
			"probability1" : $("#probability1").val(),
			"probability2" : $("#probability2").val(),
			"probability3" : $("#probability3").val(),
			"gameCommission" : $("#gameCommission").val(),
			"roomInformMoney" : $("#roomInformMoney").val(),
			"platformInformMoney":$("#platformInformMoney").val()
		}
		if ($("#cid").val()) {
			url = '../../gcs/updateGameCarSetting';
			params.id = $("#cid").val()
		} else {
			url = '../../gcs/saveGameCarSetting';
		}
		$.post(url, params, function(result) {
			var data = eval("(" + result + ")");
			if (data.result) {
				$.messager.alert("系统提示", data.msg);
			} else {
				$.messager.alert("系统提示", data.msg);
			}
		})
	}

	function reGameCarSettingRedis() {
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			if (r) {
				$.post("../../gcs/reGameCarSettingRedis", {}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "更新缓存成功");
					} else {
						$.messager.alert('系统提示', "更新缓存失败");
					}
				}, "json");
			}
		});
	}
	
	function doGetGameDollSetting() {
		var url = '../../gds/getGameDollSetting';
		$.post(url, function(result) {
			var data = eval("(" + result + ")");
			if (data) {
				$("#gid").val(data.id);
				$("#claw1").val(data.claw1);
				$("#claw2").val(data.claw2);
				$("#claw3").val(data.claw3);
				$("#groomInformMoney").val(data.roomInformMoney);
				$("#gplatformInformMoney").val(data.platformInformMoney);
			} else {
				$.messager.alert("系统提示", "请填写抓娃娃游戏配置");
			}
		})
	}
	
	function saveGameDollConfig() {
		var url;
		var params = {
			"claw1" : $("#claw1").val(),
			"claw2" : $("#claw2").val(),
			"claw3" : $("#claw3").val(),
			"roomInformMoney" : $("#groomInformMoney").val(),
			"platformInformMoney":$("#gplatformInformMoney").val()
		}
		if ($("#gid").val()) {
			url = '../../gds/updateGameDollSetting';
			params.id = $("#gid").val()
		} else {
			url = '../../gds/saveGameDollSetting';
		}
		$.post(url, params, function(result) {
			var data = eval("(" + result + ")");
			if (data.result) {
				$.messager.alert("系统提示", data.msg);
			} else {
				$.messager.alert("系统提示", data.msg);
			}
		})
	}
	
	function reGameDollSettingRedis() {
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			if (r) {
				$.post("../../gds/reGameDollSettingRedis", {}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "更新缓存成功");
					} else {
						$.messager.alert('系统提示', "更新缓存失败");
					}
				}, "json");
			}
		});
	}
	
	function doGetSmashedEggConfig(){
		var url = '../../smashedEggConfig/getSmashedEggConfig';
		$.post(url, function(result) {
			var data = eval("(" + result + ")");
			if (data[0]) {
				$("#eggid").val(data[0].id);
				$("#smashed1money").val(data[0].smashed1money);
				$("#smashed2money").val(data[0].smashed2money);
				$("#smashed3money").val(data[0].smashed3money);
			} else {
				$.messager.alert("系统提示", "请填写汽车押注游戏配置");
			}
		})
	}
	
	function reGameEggConfigRedis(){
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			if (r) {
				$.post("../../smashedEggConfig/reSmashedEggConfigRedis", {}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "更新缓存成功");
					} else {
						$.messager.alert('系统提示', "更新缓存失败");
					}
				}, "json");
			}
		});
	}
	function saveEggConfig(){
		var url;
		var params = {
			"smashed1money" : $("#smashed1money").val(),
			"smashed2money" : $("#smashed2money").val(),
			"smashed3money" : $("#smashed3money").val()
		}
		if ($("#eggid").val()) {
			params.id = $("#cid").val();
		}
		$.post("../../smashedEggConfig/saveSmashedEggConfig", params, function(result) {
			var data = eval("(" + result + ")");
			if (data.result) {
				doGetSmashedEggConfig();
				$.messager.alert("系统提示", data.msg);
			} else {
				$.messager.alert("系统提示", data.msg);
			}
		})
	}
</script>
<style type="text/css">
table {
	font-size: 14px;
}
</style>
</head>
<body style="margin: 5px;">
	<div style="width: 40%; margin: 0 auto;">
		<form>
			<fieldset style="min-width: 400px;">
				<legend><b style="font-size: 20px;">押注游戏配置</b></legend>
				<table style="width: 100%; text-align: left;" cellspacing="5px;">
					<tr>
						<td style="padding-left: 80px;width: 150px">一连概率:</td>
						<td><input id="cid" name="cid" type="hidden" /> <input
							type="text" id="probability1" name="probability1" /><span style="color: red;">[0~1]</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">二连概率:</td>
						<td><input type="text" id="probability2" name="probability2" /><span style="color: red;">[0~1]</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">三连概率:</td>
						<td><input type="text" id="probability3" name="probability3" /><span style="color: red;">[0~1]</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">平台抽成:</td>
						<td><input type="text" id="gameCommission"
							name="gameCommission" /><span style="color: red;">[0~1]</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">房间通知:</td>
						<td><input type="text" id="roomInformMoney" name="roomInformMoney" /><span style="color: red;">金币</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">平台通知:</td>
						<td><input type="text" id="platformInformMoney" name="platformInformMoney" /><span style="color: red;">金币</span></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="button" value="保存"  id="btn-save"
							onclick="saveGameCarConfig()" /> <input type="button" value="缓存"
							id="btn-Redis" onclick="reGameCarSettingRedis()" /></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	<div style="width: 40%; margin: 0 auto;">
		<form>
			<fieldset style="min-width: 400px;">
				<legend><b style="font-size: 20px;">抓娃娃戏配置</b></legend>
				<table style="width: 100%; text-align: left;" cellspacing="5px;">
					<tr>
						<td style="padding-left: 80px;width: 150px">一级爪子:</td>
						<td><input id="gid" name="gid" type="hidden" /> <input
							type="text" id="claw1" name="clw1" /><span style="color: red;">金币数</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">二级爪子:</td>
						<td> <input type="text" id="claw2" name="claw2" /><span style="color: red;">金币数</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">三级爪子:</td>
						<td> <input type="text" id="claw3" name="clw3" /><span style="color: red;">金币数</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">房间通知:</td>
						<td><input type="text" id="groomInformMoney" name="groomInformMoney" /><span style="color: red;">金币</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">平台通知:</td>
						<td><input type="text" id="gplatformInformMoney" name="gplatformInformMoney" /><span style="color: red;">金币</span></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="button" value="保存"  id="btn-save"
							onclick="saveGameDollConfig()" /> <input type="button" value="缓存"
							id="btn-Redis" onclick="reGameDollSettingRedis()" /></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	<div style="width: 40%; margin: 0 auto;">
		<form>
			<fieldset  style="min-width: 400px;">
				<legend><b style="font-size: 20px;">砸蛋游戏配置</b></legend>
				<table  style="width: 100%; text-align: left;" cellspacing="5px;">
					<tr>
						<td style="padding-left: 80px;width: 150px">木锤价格:</td>
						<td>
							<input id="eggid" name="eggid" type="hidden" /> 
							<input type="text" id="smashed1money" name="smashed1money" /><span style="color: red;">金币/次</span>
						</td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">铁锤价格:</td>
						<td><input type="text" id="smashed2money" name="smashed2money" /><span style="color: red;">金币/次</span></td>
					</tr>
					<tr>
						<td style="padding-left: 80px;width: 150px">金锤价格:</td>
						<td><input type="text" id="smashed3money" name="smashed3money" /><span style="color: red;">金币/次</span></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<input type="button" value="保存"  id="btn-save" onclick="saveEggConfig()" /> 
							<input type="button" value="缓存" id="btn-Redis" onclick="reGameEggConfigRedis()" />
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
	
</body>
</html>