<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
</head>
<body>
	<form id="fm" method="post">
		<div style="margin: 20px 0;"></div>
		<div class="easyui-panel" title="修改密码"
			style="width: 400px; padding: 30px 60px">
			<div style="margin-bottom: 20px">
				<div>旧密码:</div>
				<input class="easyui-textbox" style="width: 100%; height: 32px">
			</div>
			<div style="margin-bottom: 20px">
				<div>新密码:</div>
				<input id="password" name="password" validType="length[6,15]"
					class="easyui-validatebox" required="true" type="password" value=""
					style="width: 100%; height: 32px" />
			</div>
			<div style="margin-bottom: 20px">
				<div>确认密码:</div>
				<input type="password" name="repassword" id="repassword"
					required="true" class="easyui-validatebox"
					validType="equalTo['#password']" invalidMessage="两次输入密码不匹配" /> <input
					class="easyui-textbox" style="width: 100%; height: 32px">
			</div>

			<div>
				<a href="javascript:saveManage()" class="easyui-linkbutton" iconCls="icon-ok"
					style="width: 100%; height: 32px">修改</a>
			</div>
		</div>
	</form>
</body>
</html>