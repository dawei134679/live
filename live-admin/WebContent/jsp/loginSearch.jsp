<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>登录信息</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {

		dataGrid = $("#loginInfo").datagrid(
				{
					url : '../loginInfo',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '登录信息',
					pagination : true,
					idField : "uid",
					sortName : 'uid',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'uid',
								title : '用户id',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户昵称',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'authkey',
								title : '登录方式',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'registchannel',
								title : '注册来源',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'address',
								title : '登陆地',
								align : 'center',
								width : "100",
								sortable : false,
							},
							{
								field : 'net',
								title : '网络',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'logintime',
								title : '登录时间',
								align : 'center',
								sortable : false,
								width : "100",
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'device',
								title : '登录设备',
								align : 'center',
								width : "100",
								sortable : false
							}
							] ]
				})
	})
	
	function searchUser(){
		var uid=$("#userid").val();
		$("#loginInfo").datagrid('reload', {
			url : '../loginInfo',
			method:"search",
			uid:uid
		})
		
	}
	
	
	function statusSelect(){
		var uid=document.getElementById("selectid").value;
		$("#loginInfo").datagrid('reload', {
			url : '../loginInfo',
			method:"status",
			uid:uid
		})	
	}

</script>
</head>
<body>
	<div>
		<table id="loginInfo"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;主播ID：&nbsp;<input  type="text" name="userid" id="userid" size="10" />
				&nbsp;开始时间：&nbsp;<input class="easyui-datetimebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datetimebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
				<a href="javascript:searchUser()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
		</div>
		
	</div>
</body>
</html>