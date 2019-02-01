<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户审核</title>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
	var url;
	
	

	function searchAnchor(){
		$("#dg").datagrid('reload', {
			url : '../CheckUserList',
			method:"search",
	        uid: $("#c_uid").val(),
	        cardId: $("#c_cardId").val()
		})
	}
	function waitCheck(){
		$("#dg").datagrid('reload', {
			url : '../CheckUserList',
			method:"wait"
		});
		$("#dg").datagrid("getPanel").panel('setTitle', '待审核列表');
	}
	function alreadyCheck(){
		$("#dg").datagrid('reload', {
			url : '../CheckUserList',
			method:"already"
		});
		$("#dg").datagrid("getPanel").panel('setTitle', '已审核列表');
	}
</script>
<script type="text/javascript">
var dataGrid;

$(function () {
    dataGrid = $("#dg").datagrid({
        url: '../CheckUserList',
        width: getWidth(0.97),
        height: getHeight(0.97),
		queryParams: {
		method:"wait",
        uid: $("#c_uid").val()
		},// 传参
        title: '待审核列表',
        pagination: true,
        rownumbers: true,
        singleSelect: true,
        fit:true,
        fitColumns:true,
        pageSize:20,
        pageList:[20,50,100],
        toolbar: '#tb',
        columns: [[
            {field: 'createAt', width:"120", title: '创建时间', align: 'center', sortable: false,formatter : function(data) {
				return data ? new Date(data * 1000)
				.Format("yyyy-MM-dd HH:mm:ss") : ""
				}},
            {field: 'uid', width:"200", title: '用户Id', align: 'center', sortable: false},
            {field: 'realName', width:"200", title: '主播真名', align: 'center', sortable: false},
            {field: 'cardId', width:"200", title: '身份证号', align: 'center', sortable: false},
            {field: 'auditStatus', width:"90", title: '状态', align: 'center', sortable: false,formatter : function(data) {
            	if(data==3){return '<a href="javascript:getCheckUser();" class="easyui-tooltip" title="查看用戶信息">已通过</a>'}
            	else if(data==1){return '<a href="javascript:getCheckUser();"class="easyui-tooltip" title="查看用戶信息">待审核</a>'}
            	else{return '<a href="javascript:getCheckUser();" class="easyui-tooltip" title="查看用戶信息">驳回</a>'}
				;
			}},
           
        ]]
    })
})
 
/* function memberDetail(uid){
		 var iframe = "<iframe src='../user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg').html(iframe);
		 $('#dlg').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	} */
function getCheckUser(){
	var row = $('#dg').datagrid('getSelected');
	var uid = row.uid;
	var iframe = "<iframe src='CheckInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 1000px;height: 700px'></iframe>";
	 $('#dlg').html(iframe);
	 $('#dlg').dialog('open').dialog('center').dialog('setTitle',uid+'-详情')
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>
	<div id="tb">
		<div>
			<input type="button" value="待审核列表" onclick="waitCheck()">
			<input type="button" value="已审核列表" onclick="alreadyCheck()">
		</div>
		<div>
			&nbsp;用户ID：&nbsp;<input type="text" name="c_uid" id="c_uid" size="10" />
			&nbsp;身份证号：&nbsp;<input type="text" name="c_cardId" id="c_cardId" size="10" />  
			<a href="javascript:searchAnchor()" class="easyui-linkbutton"
				iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 1000px;height: 700px;" closed="true"></div>
</body>
</html>