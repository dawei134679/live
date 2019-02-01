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
		dataGrid = $("#cashInfo").datagrid(
				{
					url : '../cashInfo',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '提现信息',
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
								field : 'time',
								title : '数据时间',
								align : 'center',
								sortable : false,
								width : "100",
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd") : ""
								}
							},
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
								field : 'anchorLevel',
								title : '主播等级',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'unionname',
								title : '所属公会',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'type',
								title : '提现方式',
								align : 'center',
								width : "100",
								sortable : false,
							},
							{
								field : 'billno',
								title : '提现单号',
								align : 'center',
								width : "150",
								sortable : false
							},
							{
								field : 'redit',
								title : '声援值',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'amount',
								title : '提现金额',
								align : 'center',
								width : "100",
								sortable : false
							},
							{
								field : 'createAt',
								title : '提现时间',
								align : 'center',
								sortable : false,
								width : "150",
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'isSecc',
								title : '订单状态',
								align : 'center',
								width : "100",
								sortable : false,
								formatter:function(data){
									if(data==0){
										return "待成功"
									}else if(data==1){
										return "成功"
									}
								}
							}
							] ]
				})
	})
	
	function searchUser(){
		var uid=$("#userid").val();
		var start=$('#startdate').datetimebox('getValue');
		var end=$('#enddate').datetimebox('getValue');
		if(uid==""){
			$("#userid").attr("placeholder","请输入用户id");
			return;
		}
		$("#cashInfo").datagrid({
			url : '../cashInfo?startdate='+start+'&enddate='+end,
			queryParams: {
				method:"search",
		        uid: uid
			}
		})
		
	}

</script>
</head>
<body>
	<div>
		<table id="cashInfo"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;用户ID：&nbsp;<input  type="text" name="userid" id="userid" size="10" />
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