<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<title>主播信息管理</title>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
	var url;

	function searchAnchor() {
		$('#dg').datagrid('load', {
			type:'1',
			uid : $('#a_uid').val(),
			recommend : $('#a_recommend').combobox("getValue")
		});
	}

	function saveAnchor() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				if ($('#recommend').combobox("getValue") == "") {
					$.messager.alert("系统提示", "请选择所属类别");
					return false;
				}
				var rq = $('#contrRq').val();
				if (rq == "" || rq < 0 || rq.length > 7) {
					$.messager.alert("系统提示", "请填入正确机器人倍数（必须为数字且不可大于7位数）");
					return false;
				}
				return $(this).form("validate");
			},
			success : function(result) {
				if (result.errorMsg) {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				} else {
					var json = jQuery.parseJSON(result);
					$.messager.alert("系统提示", json.msg);
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}
			},
			error : function(result){
				alert(result);
			}
		});
	}

	
	function resetValue() {
		$("#stuNo").val("");
		$("#stuName").val("");
		$("#sex").combobox("setValue", "");
		$("#birthday").datebox("setValue", "");
		$("#gradeId").combobox("setValue", "");
		$("#email").val("");
		$("#stuDesc").val("");
	}

	function closeAnchorDialog() {
		$("#dlg").dialog("close");
		resetValue();
	}

	function openAnchorModifyDialog() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "编辑主播信息");
		$("#fm").form("load", row);
		url = "../anchorlist?uid=" + row.uid + "&type=2";
	}
	
	function deleteAnchor(){
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var uid = selectedRows[0].uid;
		$.messager.confirm("系统提示","确定要关闭吗？",function(r){
			if(r){
				$.get("../monitor?method=close&uid="+uid+"&status=2",function(data){
					if(data.code == 200){
						$.messager.alert("系统提示","关播成功！","info",function(){
							$('#dg').datagrid('reload');
						});
						return;
					}else{
						$.messager.alert("系统提示","关播失败！"+data.code);
						return;
					}
				},'json')
			}
		});
	}
	
	function imgFormatter(v) {
		return "<img src="+v+" width='50px' height='50px'/>";
	}
	

	function memberDetail(uid){
		 var iframe = "<iframe src='user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg1').html(iframe);
		 $('#dlg1').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}
	

var dataGrid;

$(function () {

    dataGrid = $("#dg").datagrid({
        url: '../anchorlist',
        width: getWidth(0.97),
        height: getHeight(0.97),
		queryParams: {
        type: '1'
		},// 传参
        title: '开播列表',
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
            {field: 'headimage', width:"80", title: '头像', align: 'center', sortable: true,formatter:function (data) {
        		return "<img src="+data+" width='60px' height='60px'/>";
        	}},
            {field: 'familyId', width:"200", hidden:true, title: '家族Id', align: 'center', sortable: false},
            {field: 'familyName', width:"200", title: '家族', align: 'center', sortable: false},
            {field: 'uid', width:"120", title: 'UID', align: 'center', sortable: false,  formatter : function(data) {
				return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";}},
            {field: 'nickname', width:"200", title: '昵称', align: 'center', sortable: false},
            {field: 'userLevel', width:"90", title: '用户等级', align: 'center', sortable: false},
            {field: 'anchorLevel', width:"90", title: '主播等级', align: 'center', sortable: false},
            {field: 'contrRq', width:"80", title: '机器人倍数', align: 'center', sortable: false},
            {field: 'recommend', width:"100", hidden:true, title: '房间级别id', align: 'center', sortable: false},
            {field: 'recommendName', width:"90", title: '房间级别', align: 'center', sortable: false},
            {field: 'hms', width:"100", title: '时长', align: 'center', sortable: false},
            {field: 'verified_reason', width:"150", title: '认证', align: 'center', sortable: false},
            {field: 'grade', width:"150", title: '评级', align: 'center', sortable: false}
        ]]
    })
})
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>

	<div id="tb">
		<div>
			<a href="javascript:openAnchorModifyDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
			<a href="javascript:deleteAnchor()" class="easyui-linkbutton"
				iconCls="icon-remove" plain="true">关房</a>
		</div>
		<div>
			&nbsp;用户ID：&nbsp;<input type="text" name="a_uid" id="a_uid" size="10" />
			&nbsp;类别：&nbsp;<select class="easyui-combobox" id="a_recommend" name="a_recommend" editable="false" panelHeight="auto">
				<option value="">请选择...</option>
				<option value="3">头牌</option>
				<option value="2">热门</option>
				<option value="1">最新</option>
				<option value="0">普通</option>
			</select> <a href="javascript:searchAnchor()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 570px; height: 350px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellspacing="5px;">
				<tr>
					<td>UID：</td>
					<td><input type="text" name="uid" id="uid"
						class="easyui-validatebox" required="true" data-options="disabled:true"/></td>
					<td>昵称：</td>
					<td><input type="text" name="nickname" id="nickname"
						class="easyui-validatebox" required="true"  data-options="disabled:true"/></td>
				</tr>
				<tr>
					<td>家族：</td>
					<td><input type="text" name="familyName" id="familyName"
						class="easyui-validatebox" required="true"  data-options="disabled:true"/></td>
					<td>主播等级：</td>
					<td><input type="text" name="anchorLevel" id="anchorLevel"
						class="easyui-validatebox" required="true" data-options="disabled:true"/></td>
				</tr>
				<tr>
					<td>类别：</td> 
					<td><select id="recommend" class="easyui-combobox" editable="false"
						name="recommend" panelHeight="auto"
						style="width: 155px">
							<option value="">请选择...</option>
							<option value="3">头牌</option>
							<option value="2">热门</option>
							<option value="1">最新</option>
							<option value="0">普通</option>
					</select></td>
					<td>机器人倍数：</td>
					<td><input type="text" name="contrRq" id="contrRq" class="easyui-validatebox" required="true"
						data-options="required:true,validType:'number'"/>
						</td>
				</tr>
				<tr>
					<td>评级：</td> 
					<td><select id="grade" class="easyui-combobox" editable="false"
						name="grade" panelHeight="auto"
						style="width: 155px">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
							<option value="6">6</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:saveAnchor()" class="easyui-linkbutton" iconCls="icon-ok">保存</a> 
		<a href="javascript:closeAnchorDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
	<div id="dlg1" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
</body>
</html>