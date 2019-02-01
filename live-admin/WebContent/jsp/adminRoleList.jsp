<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../header.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>角色管理以及权限分配</title>
	<script type="text/javascript" src="js/admin/usergroup.js"></script> 
  </head>
	<body style="visibility:hidden">
		<table id="group-table" >  
		</table>
		<div id="userGroup-window" title="角色管理以及权限分配"
			style="top:120px;width: 450px; height: 180px;">
			<div style="padding: 20px 20px 40px 20px;">
				<form  method="post">
					<table>
						<tr style="display:none">
							<td>
								id：
							</td>
							<td>
								<input  name="role_id"></input>
							</td>
						</tr>
						<tr>
							<td>
								名称：
							</td>
							<td>
								<input size="31" name="role_name" class="easyui-validatebox" required="true" validType="length[2,16]"></input>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div style="text-align: center; padding: 5px;">
				<a href="javascript:void(0)" onclick="savegroup()" class="easyui-linkbutton"
					icon="icon-save">保存</a>
				<a href="javascript:void(0)" onclick="closeWindow()" class="easyui-linkbutton"
					icon="icon-cancel">取消</a>
			</div>
		</div>
		
	    <div id="rule-window" title="角色权限分配"
			style="width: 450px; height: 380px;">
			<div style="height:270px;overflow: auto;padding: 20px 20px 10px 20px;">
				<form  method="post">
					<label for="rules" class="required"><font size="2" color="blue"><b>选择权限：</b></font></label>		
					<br />			
					<ul id="rules"></ul>
					<br />	
				</form>
			</div>
			<div style="text-align: center; padding: 5px;">
				<a href="javascript:void(0)" onclick="savegrouprules()" class="easyui-linkbutton"
				icon="icon-save">保存</a>
			    <a href="javascript:void(0)" onclick="closeruleWindow()" class="easyui-linkbutton"
				icon="icon-cancel">取消</a>
			</div>
		</div>
	</body>
</html>
