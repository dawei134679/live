<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>购买商城道具统计</title>
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
		dataGrid = $("#cvgStaList").datagrid({
				url : '../../cvgSta/getcvgStaList',
				width : getWidth(0.97),
				height : getHeight(0.97),
				title : '购买商城道具统计',
				pagination : true,
				rownumbers : true,
				striped : true,
				singleSelect : true,
				fit : true,
				fitColumns : true,
				pageSize : 20,
				pageList : [ 20, 50, 100 ],
				toolbar : '#tb',
				columns : [[
					{field : 'uid',title : '购买用户ID',align : 'center',width : 150,sortable : false,formatter : function(data) {
						return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
					}},
					{field : 'anchorid',title : '接收道具用户ID',align : 'center',width : 150,sortable : false,formatter : function(data) {
						if(data == 0){
							return "<font color='red'>后台添加</font>";
						}else{
							return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
						}
					}},
					{field : 'gid',title : '道具ID',width : 150,align : 'center',sortable : false},
					{field : 'gname',title : '道具名称',width : 150,align : 'center',sortable : false},
					{field : 'type',title : '礼物类型',width : 150,align : 'center',sortable : false,formatter:function(data,row){
						var lx = ["座驾","vip","守护"];
						return lx[data-1];
					}},
					{field : 'count',title : '购买道具时长（月）',width : 150,align : 'center',sortable : false},
					{field : 'realpricetotal',title : '购买道具总价',width : 150,align : 'center',sortable : false},
					{field : 'gstatus',title : '道具状态',width : 150,align : 'center',sortable : false,formatter:function(data,row){
						var ay = ["未过期","过期"];
						return ay[data];
					}},
					{field : 'addtime',title : '购买时间',width : 150,align : 'center',sortable : false,formatter : function(data) {
						return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
					}},
					{field : 'starttime',title : '开始时间',width : 150,align : 'center',sortable : false,formatter : function(data) {
						return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
					}},
					{field : 'endtime',title : '结束时间',width : 150,align : 'center',sortable : false,formatter : function(data) {
						return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
					}}
				 ] ]
			})
		});

	function doQuery() {
		$("#cvgStaList").datagrid('load', {
			uid : $('#s_uid').textbox("getValue"),
			anchorid : $('#s_anchorid').textbox("getValue"),
			gname : $('#s_gname').textbox("getValue"),
			gstatus : $("#gstatus").combobox('getValue'),
			type : $("#s_type").combobox('getValue'),
			startTime : $("#s_startTime").datebox('getValue'),
			endTime : $("#s_endTime").datebox('getValue')
		})
	}
	
	function memberDetail(uid){
		 var iframe = "<iframe src='../user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg').html(iframe);
		 $('#dlg').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}
	//导出数据
	function exportData(){
		$("#ff").submit();
	}
</script>
</head>
<body>
	<table id="cvgStaList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<form id="ff" method="post" action="../../cvgSta/expExcel" target="ifr">
			购买用户ID:&nbsp;<input id="s_uid" name="uid" class="easyui-numberbox" style="width:100px"/>
			接收道具用户ID:&nbsp;<input id="s_anchorid" name="anchorid" class="easyui-numberbox" style="width:100px"/>
			道具名称:&nbsp;<input id="s_gname" name="gname" class="easyui-textbox" style="width:100px"/>
			道具类型:&nbsp;<select id="s_type" class="easyui-combobox" editable="false" name="type" panelHeight="auto" style="width: 155px">
				<option value="" selected="true">全部</option>
				<option value="1">座驾</option>
				<option value="2">vip</option>
				<option value="3">守护</option>
			</select>
			道具状态:&nbsp;<select id="gstatus" class="easyui-combobox" editable="false" name="gstatus" panelHeight="auto" style="width: 155px">
				<option value="" selected="true">全部</option>
				<option value="0">未过期</option>
				<option value="1">过期</option>
			</select>
			购买开始时间:&nbsp;<input editable="false" class="easyui-datebox" type="text" name="startTime" id="s_startTime" style="width:100px;"></input>
			购买结束时间:&nbsp;<input editable="false" class="easyui-datebox" type="text" name="endTime" id="s_endTime" style="width:100px;"></input>
			&nbsp;<a href="javascript:doQuery()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
			&nbsp;<a href="javascript:exportData()"class="easyui-linkbutton" iconCls="icon-excel" plain="true">导出</a>
			</form>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>
