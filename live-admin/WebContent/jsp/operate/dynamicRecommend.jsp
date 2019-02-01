<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>动态-主播推荐</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;
	var url;
	var datetimes;
	var hammer;
	$(function() {
		dataGrid = $("#recommendList").datagrid(
				{
					url : '../../operat/getRecommendList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '动态推荐主播列表',
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 30,
					pageList : [ 30, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'id',
								title : 'ID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'uid',
								title : '主播UID',
								align : 'center',
								width : 90,
								sortable : false
							},{
								field : 'nickname',
								title : '主播昵称',
								align : 'center',
								width : 250,
								sortable : false
							},
							{
								field : 'unionid',
								title : '工会ID',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'uname',
								title : '公会名称',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'adminname',
								title : '归属人名',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'anchorLevel',
								title : '主播等级',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'recommend',
								title : '房间类型',
								align : 'center',
								width : 120,
								sortable : false,
								formatter : function(data) {
									if(data == 0){ return "普通" }
									else if(data == 1){ return "最新" }
									else if(data == 2){ return "热门" }
									else { return "未知" }
								}
							},
							{
								field : 'sort',
								title : '排序',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'isvalid',
								title : '有效',
								align : 'center',
								width : 60,
								sortable : false,
								formatter : function(data){
									if(data == 1){
										return "是";
									}else{
										return "否";
									}
								}
							},
							{
								field : 'operatname',
								title : '操作人',
								align : 'center',
								width : 100,
								sortable : false
							}
							] ]
				})
	})

	function addRecommend(){
		$.messager.confirm("系统提示","确认要添加?",function(r){
			if(r){
				var uid = $('#uid').val().trim();
				var sort = $('#sort').val().trim();
				
				if (uid == "") {
					$.messager.alert("系统提示", "请输入主播UID");
					return false;
				}
				if (sort == "") {
					$.messager.alert("系统提示", "请输入排序值");
					return false;
				}
				
				$.ajax({
					
		             type: "post",
		             dataType: "json",
		             url: "../../operat/addRecommend",
		             data: { uid:uid, sort:sort},
		             
		             success: function(data) {
		            	 
		             	if(data.success == 200){
		             		$('#uid').val("");
		            		$("#sort").val("");
		             		$("#recommendList").datagrid("reload");
		             	}else{
							$.messager.alert("系统提示", data.errorMsg);
		             	}
		             }
		       	});
			}
		})
	}
	
	function delRecommend(){
		$.messager.confirm("系统提示","确认要删除?",function(r){
			if(r){
				
				var selectedRows = $("#recommendList").datagrid('getSelections');
				if (selectedRows.length != 1) {
					$.messager.alert("系统提示", "请选择一条数据！");
					return;
				}
				var row = selectedRows[0];
				
				$.ajax({
					
		             type: "post",
		             dataType: "json",
		             url: "../../operat/delRecommend",
		             data: { id:row.id},
		             
		             success: function(data) {

		             	if(data.success == 200){
		             		$("#recommendList").datagrid("reload");
		             	}else{
							$.messager.alert("系统提示", data.errorMsg);
		             	}
		             }
		       	});
			}
		})
	}
</script>
</head>
<body>
	<div>
		<table id="recommendList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
			 &nbsp;&nbsp;推荐主播UID&nbsp;<input type="text" name="uid" id="uid" class="easyui-validatebox" required="true" MaxLength="12"/>
			 &nbsp;&nbsp;排序值&nbsp;<input type="text" name="sort" id="sort" class="easyui-validatebox" required="true" MaxLength="2"/>&nbsp;(越大越靠前，最大99)
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:addRecommend()" class="easyui-linkbutton" iconCls="icon-search">添加</a>
			&nbsp;&nbsp;&nbsp;<a href="javascript:delRecommend()" class="easyui-linkbutton" iconCls="icon-search">删除</a>
			</div>
		</div>
	</div>
</body>
</html>