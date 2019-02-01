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
<title>授权信息</title>
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
		$.ajax({
			url:"${pageContext.request.contextPath}/sysCopyright/getSysCopyright",
			data:{},
			success:function(result){
				if(result.code!=200){
					$.messager.alert("系统提示", result.msg);
					return;
				}
				if(result.data==null){
					return;
				}
				$("#companyName").text(result.data.companyName);
				$("#serialNumber").text(result.data.serialNumber);
				$("#version").text(result.data.version);
				$("#timeEnd").text(formatDateTime(result.data.timeEnd));
			}
		});
	});
	
	function formatDateTime(timeStamp) {   
	    var date = new Date();  
	    date.setTime(timeStamp * 1000);  
	    var y = date.getFullYear();      
	    var m = date.getMonth() + 1;      
	    m = m < 10 ? ('0' + m) : m;      
	    var d = date.getDate();      
	    d = d < 10 ? ('0' + d) : d;      
	    var h = date.getHours();    
	    h = h < 10 ? ('0' + h) : h;    
	    var minute = date.getMinutes();    
	    var second = date.getSeconds();    
	    minute = minute < 10 ? ('0' + minute) : minute;      
	    second = second < 10 ? ('0' + second) : second;     
	    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;      
	};  
	function save(){
		$("#frm1").submit();
	}
	function callBack(result){
		if(result.code!=200){
			$.messager.alert("系统提示", result.msg);
			return;
		}
		$.messager.alert("系统提示", "更新成功");
		if(result.data==null){
			return;
		}
		$("#companyName").text(result.data.companyName);
		$("#serialNumber").text(result.data.serialNumber);
		$("#version").text(result.data.version);
		$("#timeEnd").text(formatDateTime(result.data.timeEnd));
	}
	function reRedis(){
		$.ajax({
			url:"${pageContext.request.contextPath}/sysCopyright/reRedis",
			data:{},
			success:function(result){
				if(result.code!=200){
					$.messager.alert("系统提示", result.msg);
					return ;
				}
				$.messager.alert("系统提示", "更新缓存成功");
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
<body>
	<iframe id="iframe1" name="iframe1" style="display: none"></iframe>
	<div style="width: 40%; margin: 0 auto;">
		<form id="frm1" method="post" enctype="multipart/form-data" target="iframe1"
					action="${pageContext.request.contextPath}/sysCopyright/save">
			<fieldset style="min-width: 400px;">
				<legend><b style="font-size: 20px;">授权信息</b></legend>
				<table style="width: 100%;">
					<tr>
						<td style="width: 150px;text-align: right;padding-right: 20px;">授权文件</td>
						<td>
							<input type="file" name="copyright" id="copyright" onchange="upload" />
						</td>
					</tr>
					<tr>
						<td style="width: 150px;text-align: right;padding-right: 20px;">版本号</td>
						<td>
							<span id="version"></span>
						</td>
					</tr>
					<tr>
						<td style="width: 150px;text-align: right;padding-right: 20px;">授权码</td>
						<td>
							<span id="serialNumber"></span>
						</td>
					</tr>
					<tr>
						<td style="width: 150px;text-align: right;padding-right: 20px;">到期时间</td>
						<td>
							<span id="timeEnd"></span>
						</td>
					</tr>
					<tr>
						<td style="width: 150px;text-align: right;padding-right: 20px;">公司名称</td>
						<td>
							<span id="companyName"></span>
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<input type="button" value="更新"  id="btn-save" onclick="save();" /> 
							<input type="button" value="缓存" id="btn-Redis" onclick="reRedis()" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<br/>
							<div style="border-bottom:1px solid #9F9F9F;"></div>
							<br/>
							<span style="color: red;">
								温馨提示：<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;说明：以下秦皇岛汇坤电子科技开发有限公司 简称为 汇坤科技<br>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1.请在到期时间前一周联系汇坤科技更新授权信息。<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.若系统运营人员未能及时联系汇坤科技出现任何问题，汇坤科技概不负责。<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.最终解释权归汇坤科技所有。<br/>
							</span>
						</td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
</body>
</html>