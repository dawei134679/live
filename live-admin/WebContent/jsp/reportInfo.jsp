<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<title>举报列表管理</title>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
	var url;

	function searchEvent() {
		$('#dg').datagrid('load', {
			uid : $('#uid').val(),
			rid : $('#rid').val(),
			status : $('#status').combobox("getValue")
		});
	}

	function saveAnchor() {
		$.messager.confirm("系统提示", "您确定要处理吗？", function(r) {
			if (r) {
				$("#fm").form("submit", {
					url : url,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(result) {
						var data = eval('('+result+')');
						$.messager.alert("系统提示", data.msg);
						if (data.success == 200) {
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					},
					error : function(result){
						alert(result);
					}
				});
			}
		});
	}


	function closeDialog() {
		$("#dlg").dialog("close");
	}

	function handler() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要处理的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "处理举报信息");
		$("#content").html(row.content);
		$("#fm").form("load", row);
		url = "../reportInfo/handler?id="+row.id;
	}
	

var dataGrid;

$(function () {

    dataGrid = $("#dg").datagrid({
        url: '../reportInfo/getReportInfoPage',
        width: getWidth(0.97),
        height: getHeight(0.97),
		queryParams: {},// 传参
        title: '举报列表',
        pagination: true,
        rownumbers: true,
        singleSelect: true,
        fit:true,
        fitColumns:true,
        pageSize:20,
        pageList:[20,50,100],
        toolbar: '#tb',
        columns: [[
            {field: 'cb', checkbox:'true', align: 'center', sortable: false},
            {field: 'uid', width:"120", title: '被举报人', align: 'center', sortable: false,  formatter : function(data) {
            	return data;
				//return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
			}},
            {field: 'rid', width:"120", title: '举报人', align: 'center', sortable: false},
            {field: 'content', width:"300", title: '举报内容', align: 'left', sortable: false},
            {field: 'createtime', width:"120", title: '举报时间', align: 'center', sortable: false,formatter:function(data){
            	return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
            }},
            {field: 'status', width:"90", title: '状态', align: 'center', sortable: false,formatter:function(data){
            	var s = ['<span style="color:red;">未处理</span>','<span style="color:green;">已处理</span>'];
            	return s[data];
            }},
            {field: 'handletime', width:"120", title: '处理时间', align: 'center', sortable: false,formatter:function(data){
            	return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
            }},
            {field: 'handlemark', width:"200", title: '处理意见', align: 'left', sortable: false}
        ]]
    })
})
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>

	<div id="tb">
		<div>
			<a href="javascript:handler()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">处理</a> 
		</div>
		<div>
			&nbsp;被举报人：&nbsp;<input type="number" name="uid" id="uid" size="10" />
			&nbsp;举报人：&nbsp;<input type="number" name="rid" id="rid" size="10" />
			&nbsp;状态：&nbsp;<select class="easyui-combobox" id="status" name="status" editable="false" panelHeight="auto">
				<option value="">请选择...</option>
				<option value="0">未处理</option>
				<option value="1">已处理</option>
			</select>
			&nbsp;<a href="javascript:searchEvent()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog" style="width:500px; height:350px; padding:5px;" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>举报内容：</td>
					<td>
						<div id="content" style="width:350px;font-size:14px;border:1px solid #ccc;padding:5px;">...</div>
					</td>
				</tr>
				<tr>
					<td>处理意见：</td>
					<td>
						<textarea name="handlemark" id="handlemark" class="textarea easyui-validatebox" required="true" placeholder="处理意见..." style="width:350px;height:110px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:saveAnchor()" class="easyui-linkbutton" iconCls="icon-ok">保存</a> 
		<a href="javascript:closeDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	<div id="dlg1" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
</body>
</html>