<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>轮播公告</title>
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
		})
	}
	function alreadyCheck(){
		$("#dg").datagrid('reload', {
			url : '../CheckUserList',
			method:"already"
		})
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
		method:"list",
        uid: $("#c_uid").val()
		},// 传参
        title: '公告',
        //pagination: true,
        rownumbers: true,
        singleSelect: true,
        fit:true,
        fitColumns:true,
        pagination : true,
        pageSize:20,
        pageList:[20,50,100],
        toolbar: '#tb',
        columns: [[
            {field: 'createAt', width:"150", title: '创建时间', align: 'center', sortable: false,formatter : function(data) {
				return data ? new Date(data * 1000)
				.Format("yyyy-MM-dd HH:mm:ss") : ""
				}},
            {field: 'createdBy', width:"50", title: '创建人', align: 'center', sortable: false},
            {field: 'platForm', width:"70", title: '平台', align: 'center', sortable: false},
            {field: 'startDate', width:"150", title: '开始时间', align: 'center', sortable: false,formatter : function(data) {
				return data ? new Date(data * 1000)
				.Format("yyyy-MM-dd HH:mm:ss") : ""
				}},
			{field: 'endDate', width:"150", title: '结束时间', align: 'center', sortable: false,formatter : function(data) {
					return data ? new Date(data * 1000)
					.Format("yyyy-MM-dd HH:mm:ss") : ""
				}},
			{field: 'time', width:"100", title: '间隔', align: 'center', sortable: false},
			{field: 'content', width:"200", title: '内容', align: 'center', sortable: false},
            {field: 'status', width:"90", title: '状态', align: 'center', sortable: false,formatter : function(data) {
            	return '<a href="javascript:getCheckUser();" class="easyui-tooltip" title="查看用戶信息">'+data+'</a>';
			}},
           
        ]]
    })
})
 
function send(){
	var arr = document.getElementsByName("radio");
	var ar=document.getElementsByName("check");
	for(i=0;i<arr.length;i++){
		if(arr[i].checked){
		var radio = arr[i].value;
		}
	}
	for(j=0;j<ar.length;j++){
		if(ar[j].checked){
		var check =ar[j].value;	
		}
	}
	if(check==null){
		
	}else{
		
	}
	
}
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>
	<div id="tb">
		<div>
			<input type="radio" name="radio" value="1">苹果&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="radio" value="2">安卓&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="radio" value="3">All&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div>
			<input class="easyui-textbox" name="content" data-options="multiline:true" style="height:100px;width:310px" />
		</div>
		<div>
			<input type="checkbox" name="check" value="1">
			<input class="easyui-datetimebox" id="startDate" style="width:100px;height:24px" data-options="disabled:false">
			<input class="easyui-datetimebox" id="endDate" style="width:100px;height:24px" data-options="disabled:false">
			<select id="time">
				<option value=0>间隔</option>
				<option value=1>5min</option>
				<option value=2>10min</option>
				<option value=3>15min</option>
				<option value=4>30min</option>
				<option value=5>1h</option>
			</select>
		</div>
		<input type="button" name="submit" id="submit" value="发送" onclick="send()">
	</div>
	
</body>
</html>