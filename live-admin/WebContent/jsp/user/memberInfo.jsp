<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>会员详情</title>
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
			url:"${pageContext.request.contextPath}/member/getMemberInfoById",
			data:{uid:'${param.uid}'},
			success:function(res){
				var d = eval("(" + res + ")");
				if(d.result){
					$("#headImage").html("<img src="+d.data.headimage+" style='width:50px;height:50px;'>");
					$("#uid").text(d.data.uid);
					$("#nickName").text(d.data.nickname);
					$("#sex").text(d.data.sex==1?"男":"女");
					$("#money").text(d.data.money);
					$("#phone").text(d.data.phone);
					$("#registTime").text(formatter(d.data.registtime));
					$("#anchorLevel").text(d.data.anchorlevel);
					$("#userLevel").text(d.data.userlevel);
					var extensionCenter = [];
					if(d.data.extensionCenterName!="undefined"&&d.data.extensionCenterName!=null&&d.data.extensionCenterName!=""){
						extensionCenter.push(d.data.extensionCenterName);
					}
					if(d.data.extensionCenterContactsName!="undefined"&&d.data.extensionCenterContactsName!=null&&d.data.extensionCenterContactsName!=""){
						extensionCenter.push(d.data.extensionCenterContactsName);
					}
					if(d.data.extensionCenterContactsPhone!="undefined"&&d.data.extensionCenterContactsPhone!=null&&d.data.extensionCenterContactsPhone!=""){
						extensionCenter.push(d.data.extensionCenterContactsPhone);
					}
					$("#extensionCenter").text(extensionCenter.join("，"));
					var promotersName = [];
					if(d.data.promotersName!="undefined"&&d.data.promotersName!=null&&d.data.promotersName!=""){
						promotersName.push(d.data.promotersName);
					}
					if(d.data.promotersContactsName!="undefined"&&d.data.promotersContactsName!=null&&d.data.promotersContactsName!=""){
						promotersName.push(d.data.promotersContactsName);
					}
					if(d.data.promotersContactsPhone!="undefined"&&d.data.promotersContactsPhone!=null&&d.data.promotersContactsPhone!=""){
						promotersName.push(d.data.promotersContactsPhone);
					}
					$("#promoters").text(promotersName.join("，"));
					var agentUser = [];
					if(d.data.agentUserName!="undefined"&&d.data.agentUserName!=null&&d.data.agentUserName!=""){
						agentUser.push(d.data.agentUserName);
					}
					if(d.data.agentUserContactsName!="undefined"&&d.data.agentUserContactsName!=null&&d.data.agentUserContactsName!=""){
						agentUser.push(d.data.agentUserContactsName);
					}
					if(d.data.agentUserContactsPhone!="undefined"&&d.data.agentUserContactsPhone!=null&&d.data.agentUserContactsPhone!=""){
						agentUser.push(d.data.agentUserContactsPhone);
					}
					$("#agentUser").text(agentUser.join("，"));
					var salesman = [];
					if(d.data.salesmanName!="undefined"&&d.data.salesmanName!=null&&d.data.salesmanName!=""){
						salesman.push(d.data.salesmanName);
					}
					if(d.data.salesmanContactsPhone!="undefined"&&d.data.salesmanContactsPhone!=null&&d.data.salesmanContactsPhone!=""){
						salesman.push(d.data.salesmanContactsPhone);
					}
					$("#salesman").text(salesman.join("，"));
				}
			}
		});
	});
	
	function formatter(data){
		var date = new Date();  
	    date.setTime(data * 1000);  
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
	}
</script>
<style type="text/css">
table {
	font-size: 14px;
	border-collapse: collapse;
    border: none;
    width: 550px;
    height: 300px;
}
td{
    border: solid #C0C0C0 1px;
    padding: 5px;
}
.td1{
	background-color: #DCDCDC;
	width: 100px;
	height: 30px;
}
</style>
</head>
<body style="margin: 5px;">
	<div align="center">
		<table>
			<tr>
				<td colspan="1" class="td1">头像:</td>
				<td>
					<span id="headImage"></span>
				</td>
				<td class="td1">UID:</td>
				<td>
					<span id="uid"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">昵称:</td>
				<td>
					<span id="nickName"></span>
				</td>
				<td class="td1">性别:</td>
				<td>
					<span id="sex"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">手机号:</td>
				<td>
					<span id="phone"></span>
				</td>
				<td class="td1">金币:</td>
				<td>
					<span id="money"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">主播等级:</td>
				<td>
					<span id="anchorLevel"></span>
				</td>
				<td class="td1">用户等级:</td>
				<td>
					<span id="userLevel"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">注册时间:</td>
				<td colspan="3">
					<span id="registTime"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">钻石公会名称:</td>
				<td colspan="3">
					<span id="extensionCenter"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">铂金公会:</td>
				<td colspan="3">
					<span id="promoters"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">黄金公会:</td>
				<td colspan="3">
					<span id="agentUser"></span>
				</td>
			</tr>
			<tr>
				<td class="td1">家族助理:</td>
				<td colspan="3">
					<span id="salesman"></span>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>