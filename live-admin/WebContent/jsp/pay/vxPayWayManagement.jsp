<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>支付配置</title>
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
		initWxPayWay();
	});

	 function initWxPayWay(){
		var url = '../../wxp/initWxPayWay';
		$.post(url,{}, function(result) {
			 var data = eval("(" + result + ")");
			 if(data.code == 200){
				    $("input:radio[value="+data.payWay+"]").attr('checked','true');
			 }else{
				 $.messager.alert("系统提示", "请先设置微信支付方式!");
			 }
		});
	} 
	
	function reWxPayWay() {
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			var payWay = $("input[name='payWay']:checked").val();
			var param = {"payWay":payWay};
			if (r) {
				$.post("../../wxp/reWxPayWayRedis", param, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "更新缓存成功");
					} else {
						$.messager.alert('系统提示', "更新缓存失败");
					}
				}, "json");
			}
		});
	}
	
</script>
<style type="text/css">
table {
	font-size: 14px;
}
</style>
</head>
<body style="margin: 5px;">
	<div style="width: 40%; margin: 50px auto;">
		<form>
			<fieldset style="min-width: 400px;">
				<legend><b style="font-size: 20px;">微信支付通道</b></legend>
				<table style="width: 100%; text-align: center;" cellspacing="5px;">
					<tr>
						<td>
							<!-- 
							<input id="unpay" type="radio" value="unpay" name="payWay">unpay</input>
							 -->
							&nbsp; &nbsp;
							<input id="nativeh5" type="radio" value="nativeh5" name="payWay">微信官方(原生h5)</input>
							&nbsp; &nbsp;
							<input id="nativereapal" type="radio" value="nativereapal" name="payWay">融宝微信原生</input>
							<br/>
							<span style="color:red;">不要频繁改动此配置，切换的瞬间会影响正在进行支付的请求。</span>
						</td>
					</tr>
					<tr>
						<td><input type="button" value="保存" id="btn-Redis" onclick="reWxPayWay()" /></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
</body>
</html>