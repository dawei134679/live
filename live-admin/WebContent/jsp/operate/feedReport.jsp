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
<title>动态举报列表</title>
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
		dataGrid = $("#feedReportList").datagrid(
				{
					url : '../../reportFeed/pageList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '动态举报列表',
					queryParams:{
						status : $("#status").combobox('getValue')
					},
					pagination : true,
					rownumbers : true,
					singleSelect : true,
					hideColumn : 'id',
					pageSize : 30,
					pageList : [ 30, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{field : 'id',title : 'id',align : 'center',width : 50,sortable : false},
							{field : 'createAt',title : '举报日期',align : 'center',width : 150,sortable : false,formatter : function(data) {
								return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : ""
							}},
							{field : 'reportUid',title : '被举报人',align : 'center',width : 100,sortable : false},
							{field : 'reportNum',title : '被举报次数',align : 'center',width : 550,sortable : false},
							{field : 'status',title : '处理结果',align : 'center',width : 100,sortable : false,formatter : function(v,row) {
								var ary = ['未处理','已忽略','已删除'];
								return '<a href="javascript:void(0);" onclick="chuli('+row.id+','+v+');">'+ary[v]+'</a>';
							}}
						] ]
				})
	})
	
	function chuli(id,status){
		 var iframe = "<iframe src='reportFeedDetail.jsp?id="+id+"' style='border-width: 0px;width: 900px;height: 550px'></iframe>";
		 $('#dlg').html(iframe);
		 $('#dlg').dialog('open').dialog('center').dialog('setTitle','详情');
	}

	function clearQuery() {
		$("#status").combobox('reset');
		$("#startdate").textbox('reset');
		$("#enddate").textbox('reset');
	}
	
	function queryList(){
		$("#feedReportList").datagrid('reload', {
			status : $("#status").combobox('getValue'),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		})
	}

</script>
</head>
<body>
	<div>
		<table id="feedReportList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
			<select id="status" class="easyui-combobox" name="status" style="width:120px;" required="true">
					<option value="">请选择</option>
					<option value="0" selected="selected">待审核</option>
					<option value="1">已处理[忽略]</option>
					<option value="2">已处理[删除]</option>
				</select>
				&nbsp;&nbsp;开始时间：<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" required="true"/>
				&nbsp;&nbsp;结束时间：<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" required="true"/>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:queryList()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 920px;height: 590px;" closed="true"></div>
</body>
</html>