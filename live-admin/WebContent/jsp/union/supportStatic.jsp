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
<title>扶持号消费列表</title>
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

		var curr_time = new Date();     
		$("#ymd").datebox("setValue",myformatter(curr_time));  
		  
		dataGrid = $("#supportlist").datagrid(
				{
					url : '../../union/getSupportConsume',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '扶持号消费列表',
					queryParams: {
						ymd : $("#ymd").textbox('getValue'),
						uid : $('#uid').val(),
						unionid : $('#unionid').combobox("getValue")
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
								title : '扶持号UID',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'nickname',
								title : '扶持号名称',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'unionid',
								title : '工会ID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'unionname',
								title : '工会名称',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'adminname',
								title : '归属人',
								align : 'center',
								width : 90,
								sortable : false
							},
							{
								field : 'supportAmount',
								title : '扶持金额(预计)',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'pay',
								title : '充值金币',
								align : 'center',
								width : 90,
								sortable : false
							},
							{
								field : 'consumes',
								title : '非扶持金币',
								align : 'center',
								width : 90,
								sortable : false
							},
							{
								field : 'amounts',
								title : '总消费金币',
								align : 'center',
								width : 90,
								sortable : false
							}
							] ]
				})
	})
	
	function searchSupport() {
		$('#supportlist').datagrid('load', {
			ymd : $("#ymd").textbox('getValue'),
			uid : $('#uid').val(),
			unionid : $('#unionid').combobox("getValue")
		});
	}
	
	function myformatter(date){
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        return y+'-'+(m<10?('0'+m):m);
    }
	
	function myParser(s){
		if (!s) return new Date();
		var ss = (s.split('-'));
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		if (!isNaN(y) && !isNaN(m)){
			return new Date(y,m-1);
		} else {
			return new Date();
		}
	}

</script>
</head>
<body style="margin: 5px;">
		<table id="supportlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;&nbsp;扶持号UID:&nbsp;<input class="easyui-textbox" name="uid" id="uid" style="width:100px" data-options="disabled:false" required="true"/>
				&nbsp;&nbsp;选择公会:&nbsp;<input id="unionid" name="unionid" class="easyui-combobox" required="true"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../../unionAnchor/getUnionNameList'"
    						/>
    			&nbsp;&nbsp;选择年月：&nbsp;<input class="easyui-datebox" style="width:100px;height:24px" name="ymd" id="ymd" size="10" data-options="formatter:myformatter,parser:myParser"/>
				&nbsp;&nbsp;<a href="javascript:searchSupport()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
</body>
</html>
