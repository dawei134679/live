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
<title>后台操作列表</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;

	$(function() {

 		var curr_time = new Date();   
		$("#startdate").datebox("setValue",myformatterStart(curr_time));
		$("#enddate").datebox("setValue",myformatterEnd(curr_time));
		
		dataGrid = $("#loglist").datagrid(
				{
					url : '../../adminoperat/operat/logs',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '后台操作列表',
					queryParams: {
						action : $('#action').combobox("getValue"),
						uid : $('#uid').combobox("getValue"),
						startDate : $("#startdate").textbox('getValue'),
						endDate : $("#enddate").textbox('getValue')
					},
					pagination : true,
			        rownumbers: true,
					striped : true,
			        singleSelect: true,
			        fit:true,
					nowrap : false,
			        fitColumns:true,
			        pageSize:20,
			        height : 'auto',
			        pageList:[20,50,100],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'dbname',
								title : '表名',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'db_id',
								title : '表key',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'operation_note',
								title : '操作方式',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'previous_version',
								title : '操作之前',
								align : 'center',
								width : 280,
								sortable : false
							},
							{
								field : 'current_version',
								title : '操作之后',
								align : 'center',
								width : 280,
								sortable : false
							},
							{
								field : 'username',
								title : '操作者',
								align : 'center',
								width : 90,
								sortable : false
							},
							{
								field : 'operation_time',
								title : '添加时间',
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
	})
	
	 function myformatterStart(date){
           var y = date.getFullYear();
           var m = date.getMonth()+1;
           return y+'-'+(m<10?('0'+m):m)+'-01';
       }

	 function myformatterEnd(date){
          var y = date.getFullYear();
          var m = date.getMonth()+1;
          var d = date.getDate();
          return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
      }
	
	function searchLoginLog() {
		$('#loglist').datagrid('load', {
			action : $('#action').combobox("getValue"),
			uid : $('#uid').combobox("getValue"),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		});
	}
</script>
</head>
<body style="margin: 5px;">
		<table id="loglist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<select id="action" class="easyui-combobox" name="action" style="width:70px;">
					<option value="0" selected="selected">全部</option>
					<option value="1">新增</option>
					<option value="2">修改</option>
				</select>
				选择用户:<input id="uid" name="uid" class="easyui-combobox"
							data-options="valueField: 'uid',
    									textField: 'username',
    									url: '../../adminoperat/forSelect'"
    						/>
				&nbsp;&nbsp;开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
				&nbsp;&nbsp; 
				<a href="javascript:searchLoginLog()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
</body>
</html>