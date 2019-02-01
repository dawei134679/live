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
<title>举报列表</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>

	var dataGrid;
	var url;
	
	$(function() {
		dataGrid = $("#reportalbumlist").datagrid(
				{
					url : '../../operat/reportalbum',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '举报列表',
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					hideColumn : 'id',
					pageSize : 30,
					pageList : [ 30, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'id',
								title : 'id',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'createAt',
								title : '举报日期',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},{
								field : 'uid',
								title : '被举报人UID',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'nickname',
								title : '被举报人昵称',
								align : 'center',
								width : 120,
								sortable : false
							},
							{
								field : 'url',
								title : '相册图片',
								align : 'center',
								width : 550,
								sortable : false,
								formatter : function(data) {
									return  '<img src="'+data+'" width=300 height=300 />';
								}
							},
							{
								field : 'status',
								title : '处理结果',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data,row) {
									if(data == 0){
										return '<a href="javascript:verifyalbum('+row.id+',1,'+row.uid+','+row.pid+');" class="easyui-tooltip" title="审核相册">忽略</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:verifyalbum('+row.id+',2,'+row.uid+','+row.pid+');" class="easyui-tooltip" title="审核相册">删除</a>';
									}else if(data == 1){
										return "忽略";
									}else if(data == 2){
										return "删除";
									}else{
										return "未知";
									}
								}
							}
							] ]
				})
	})
	
	function verifyalbum(id,status,uid,pid){
		var msg = "";
		if(status == 1){
			msg = "忽略";
		}else if(status == 2){
			msg = "删除";
		}
		$.messager.confirm("系统提示","确认"+msg+"吗？",function(r){
			if(r){
				$.ajax({
		             type: "post",
		             dataType: "json",
		             url: "../../operat/verifyAlbum",
		             data: { id:id,status:status,uid:uid,pid:pid},
		             success: function(data) {
		             	if(data.errorCode==200){
		             		$("#reportalbumlist").datagrid("reload");
		             	}else{
		             		$.messager.alert("系统提示","审核失败！");
		             	}
		             }
		       	});
			}
		});
	}

	function clearQuery() {
		$("#status").combobox('reset');
		$("#startdate").textbox('reset');
		$("#enddate").textbox('reset');
	}
	
	function queryEggList(){
		$("#reportalbumlist").datagrid('reload', {
			status : $("#status").combobox('getValue'),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		})
	}


	function closeVerifyAlbumDialog(){
		$("#dlg").form("clear");
		$("#dlg").dialog("close");
	}
</script>
</head>
<body>
	<div>
		<table id="reportalbumlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
			<select id="status" class="easyui-combobox" name="status" style="width:120px;" required="true">
					<option value="0" selected="selected">待审核</option>
					<option value="1">已处理[忽略]</option>
					<option value="2">已处理[删除]</option>
				</select>
				&nbsp;&nbsp;开始时间：<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10"/>
				&nbsp;&nbsp;结束时间：<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10"/>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:queryEggList()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
</body>
</html>