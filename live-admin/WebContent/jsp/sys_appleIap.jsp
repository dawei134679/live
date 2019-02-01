<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
	var url;
	// 解封号
	function unblockAnchor() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要关闭房间的主播！");
			return;
		}
		var uid = selectedRows[0].uid;
		url = "../monitor";
		$.messager.confirm("系统提示", "您确认要解封这<font color=red>"
				+ selectedRows.length + "</font>个房间吗？", function(r) {
			if (r) {
				$.post(url, {
					uid : uid,
					method:"block",
					status:"1"
				}, function(result) {
					if (result.code == 200 ) {
						$.messager.alert("系统提示", "您已成功解封<font color=red>"
								+ selectedRows.length + "</font>个房间！");
						$("#dg").datagrid("reload");
					} else {
						$.messager.alert('系统提示', result.msg);
					}
				}, "json");
			}
		});
	}
	// 解禁播
	function unbanAnchor() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert("系统提示", "请选择要关闭房间的主播！");
			return;
		}
		var uid = selectedRows[0].uid;
		url = "../monitor";
		$.messager.confirm("系统提示", "您确认要解禁播这<font color=red>"
				+ selectedRows.length + "</font>个房间吗？", function(r) {
			if (r) {
				$.post(url, {
					uid : uid,
					method:"ban",
					status:"1"
				}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "您已成功解禁播<font color=red>"
								+ selectedRows.length + "</font>个房间！");
						$("#dg").datagrid("reload");
					} else {
						$.messager.alert('系统提示', result.msg);
					}
				}, "json");
			}
		});
	}

	function searchAnchor() {
		$('#dg').datagrid('load', {
			method:'unhandle',
			uid : $('#a_uid').val()
		});
	}

	function imgFormatter(v) {
		return "<img src="+v+" width='50px' height='50px'/>";
	}
</script>
<script type="text/javascript">
var dataGrid;

$(function () {
    dataGrid = $("#dg").datagrid({
        url: '../monitor',
        width: getWidth(0.97),
        height: getHeight(0.97),
		queryParams: {
		method:"unhandle",
        uid: $("#a_uid").val()
		},// 传参
        title: '处理用户',
        //pagination: true,
        rownumbers: true,
        singleSelect: true,
        fit:true,
        fitColumns:true,
        //pageSize:20,
        //pageList:[20,50,100],
        toolbar: '#tb',
        columns: [[
            {field: 'cb', checkbox:'true', align: 'center', sortable: false},
            {field: 'uid', width:"120", title: 'UID', align: 'center', sortable: false},
            {field: 'familyId', width:"200", hidden:true, title: '家族Id', align: 'center', sortable: false},
            {field: 'familyName', width:"200", title: '家族', align: 'center', sortable: false},
            {field: 'nickname', width:"200", title: '昵称', align: 'center', sortable: false},
            {field: 'userLevel', width:"90", title: '用户等级', align: 'center', sortable: false},
            {field: 'anchorLevel', width:"90", title: '主播等级', align: 'center', sortable: false},
            {field: 'recommend', width:"90", title: '房间级别', align: 'center', sortable: false,formatter:function(data){
            	if(data == 0) return "普通主播";
            	else if(data == 1) return "最新主播";
            	else if(data == 2) return "热门主播";
            	else return "未知主播";
            }},
            {field: 'identity', width:"90", title: '用户身份', align: 'center', sortable: false,formatter:function(data){
            	if(data == 1) return "主播";
            	else if(data == 2) return "观众";
            	else if(data == 3) return "超管员";
            	else return "未知";
            }},
            {field: 'status', width:"90", title: '状态', align: 'center', sortable: false,formatter:function(data){
            	if(data == 1) return "正常";
            	else if(data == 0) return "封号";
            	else return "未知";
            }},
            {field: 'cause', width:"150", title: '原因', align: 'center', sortable: false},
            {field: 'source', width:"50", title: '平台', align: 'center', sortable: false,formatter:function(data){
            	if(data == 1) return "客户端";else if(data == 2) return "后台"; else return "未知";
            }},
            {field: 'operator', width:"150", title: '操作人', align: 'center', sortable: false}
        ]]
    })
})
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>
	<div id="tb">
		<div>
			<a href="javascript:unbanAnchor()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">解禁播</a>
			<a href="javascript:unblockAnchor()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">解封号</a>
		</div>
		<div>
			&nbsp;用户ID：&nbsp;<input type="text" name="a_uid" id="a_uid" size="10" />  <a href="javascript:searchAnchor()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>
</body>
</html>