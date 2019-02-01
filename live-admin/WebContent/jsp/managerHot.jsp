<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>热门管理</title>
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
					title : '热门管理',
					pagination : true,
					idField : "rowId",
					sortName : 'rowId',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'rowId',
								title : '推荐序号位',
								align : 'center',
								sortable : false
							},
							{
								field : 'nickname',
								title : '主播名',
								align : 'center',
								sortable : false
							},
							{
								field : 'uid',
								title : '主播id',
								align : 'center',
								sortable : false
							},
							{
								field : 'totalperson',
								title : '观看人数',
								align : 'center',
								sortable : false
							},
							{
								field : 'roomperson',
								title : '房间人数',
								align : 'center',
								sortable : false,
							},
							{
								field : 'pig',
								title : '直播间金币消耗量',
								align : 'center',
								sortable : false
							},
							{
								field : 'rank',
								title : '排位值',
								align : 'center',
								sortable : false
							},
							{field: 'edit', width:"90", title: '操作', align: 'center', sortable: false,formatter : function(data) {
				            	return '<a href="javascript:UpdateAdv();" class="easyui-tooltip" title="查看用戶信息">操作</a>';
							}}
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
				&nbsp;主播ID：&nbsp;<input type="text" name="userid" id="userid" size="10" />
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