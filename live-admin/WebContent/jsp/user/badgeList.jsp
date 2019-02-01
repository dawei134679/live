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
<title>徽章列表</title>
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
		dataGrid = $("#badgelist").datagrid(
				{
					url : '../../gift/getUsedBadgeList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '礼物添加列表',
					queryParams: {
						uid : $('#searchUid').textbox("getValue")
					},
					pagination : true,
			        rownumbers: true,
					striped : true,
			        singleSelect: true,
			        fit:true,
			        fitColumns:true,
			        pageSize:20,
			        pageList:[20,50,100],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'uid',
								title : '用户UID',
								align : 'center',
								width : 90,
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户昵称',
								align : 'center',
								width : 110,
								sortable : false
							},
							{
								field : 'gid',
								title : '礼物ID',
								align : 'center',
								width : 70,
								sortable : false
							},
							{
								field : 'gname',
								title : '礼物名称',
								align : 'center',
								width : 120,
								sortable : false
							},
							{
								field : 'num',
								title : '数量',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'starttime',
								title : '开始时间',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'endtime',
								title : '结束时间',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							}
							] ]
				})

				
				$("#uid").textbox({
					onChange:function(data){
						var uid = $.trim(data);
						if(uid == ""){
							return;
						}
						$.ajax({
				             type: "post",
				             dataType: "json",
				             url: "../../anchor/getNickName",
				             data: { uid:uid},
				             dataType: 'json', 
				             success: function(data) {
				             	if(data.success == 200){
				             		$("#nickname").textbox("setValue",data.nickname);
				             	}else{
									$.messager.alert("系统提示", data.msg);
				             	}
				             }
				       	});
					}
				});
	})
	
	function addBadgeDialog(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加徽章');
        $("#fm").form("clear");
        url = '../../gift/addUserBadge';
	}
	
	function searchBadge() {
		$('#badgelist').datagrid('load', {
			uid : $('#searchUid').textbox("getValue")
		});
	}
	
	function saveBadge() {
		
		var gname = $('#gid').combobox('getText');
		var msg = "确认给"+$('#nickname').textbox('getValue') + " 添加 " + $('#num').textbox('getValue') + "个 "+ gname + "？";
		
		$.messager.confirm("系统提示",msg,function(r){
			if(r){
				$("#fm").form("submit", {
					url : url,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(data) {
						var result = eval("("+data+")");
						if (result.success == 200 ) {
							$.messager.alert("系统提示", "添加成功");
							$("#fm").form("clear");
							$("#dlg").dialog("close");
							$("#badgelist").datagrid("reload");
						}else {
							$.messager.alert("系统提示", result.msg);
						}
					}
				});
			}
		})
	}
	
</script>
</head>
<body style="margin: 5px;">
		<table id="badgelist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addBadgeDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
			</div>
			<div>
				用户UID:&nbsp;<input id=searchUid name="searchUid" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
				&nbsp;&nbsp;<a href="javascript:searchBadge()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>用户UID:</td>
						<td>
							<input id=uid name="uid" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
						</td>
					</tr>
					<tr>
						<td>用户昵称:</td>
						<td>
							<input id="nickname" name="nickname" class="easyui-textbox"  data-options="readonly:true"/>
						</td>
					</tr>
					<tr>
						<td>选择徽章</td>
						<td>
							<input id="gid" name="gid" class="easyui-combobox" required="true"  missingMessage="徽章必填"
							data-options="valueField: 'gid',
    									textField: 'gname',
    									url: '../../gift/forSelectBySubtype?subtype=6'"
    						/>
						</td>
					</tr>
					<tr>
						<td>开始时间:</td>
						<td>
							<input class="easyui-datetimebox" style="width:160px;height:24px" name="startdate" id="startdate" size="15" />
						</td>
					</tr>
					<tr>
						<td>天数:</td>
						<td>
							<input class="easyui-numberbox" name="num" id="num"  data-options="disabled:false" required="true" min="0" max="365"/>[1~365]
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td>
							<input class="easyui-textbox" name="descrip" id="descrip"  data-options="disabled:false"  data-options="multiline:true" style="height:100px;width:200px" required="true" missingMessage="备注必填"/>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveBadge()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
</body>
</html>