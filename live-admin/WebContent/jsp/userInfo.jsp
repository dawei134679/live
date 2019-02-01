<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>用户信息</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {

		dataGrid = $("#userInfo").datagrid(
				{
					url : '../userInfo',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '用户信息',
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
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户昵称',
								align : 'center',
								sortable : false
							},
							{
								field : 'authkey',
								title : '登录方式',
								align : 'center',
								sortable : false
							},
							{
								field : 'registchannel',
								title : '注册来源',
								align : 'center',
								sortable : false
							},
							{
								field : 'anchorLevel',
								title : '主播等级',
								align : 'center',
								sortable : false,
							},
							{
								field : 'userLevel',
								title : '用户等级',
								align : 'center',
								sortable : false
							},
							{
								field : 'money',
								title : '金币持有',
								align : 'center',
								sortable : false
							},
							{
								field : 'wealth',
								title : '财富持有',
								align : 'center',
								sortable : false
							},
							{
								field : 'creditTotal',
								title : '累计声援',
								align : 'left',
								sortable : false
							},
							{
								field : 'credit',
								title : '持有声援',
								align : 'center',
								sortable : false,
							},
							{
								field : 'addtime',
								title : '注册时间',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'phone',
								title : '绑定手机',
								align : 'center',
								sortable : false
							},
							{
								field : 'handle',
								title : '账号状态',
								align : 'center',
								sortable : false,
								formatter : function(data){
									if(data==1){
										return "禁播";
									}else if(data==2){
										return "封号";
									}else if(data==0){
										return "正常";
									}else if(data==3){
										return "封终端";
									}
								} 
							}
							] ]
				})
	})
	
	function searchUser(){
		var uid=$("#userid").val();
		$("#userInfo").datagrid('reload', {
			url : '../userInfo',
			method:"search",
			uid:uid
		})
		
	}
	
	
	function statusSelect(){
		var uid=document.getElementById("selectid").value;
		$("#userInfo").datagrid('reload', {
			url : '../userInfo',
			method:"status",
			uid:uid
		})	
	}

</script>
</head>
<body>
	<div>
		<table id="userInfo"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;用户ID：&nbsp;<input type="text" name="userid" id="userid" size="10" />
				<a href="javascript:searchUser()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select id="selectid" onChange="statusSelect();">
					<option value=0>违规名单汇总</option>
					<option value=2>封号</option>
					<option value=1>禁播</option>
					<option value=3>封终端</option>
				</select>
			</div>
		</div>
		
	</div>
</body>
</html>