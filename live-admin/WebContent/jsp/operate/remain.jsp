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
<title>注册留存列表</title>
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
		dataGrid = $("#remainlist").datagrid(
				{
					url : '../../operat/remain',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '留存列表',
					queryParams: {
						os : $("#searchos").combobox("getValue"),
						channel:$("#channel").combobox("getValue"),
						platform:$("#platform").textbox("getValue"),
						startDate : $("#startdate").textbox("getValue"),
						endDate : $("#enddate").textbox("getValue"),
						category: $("#category").combobox("getValue"),
						type: 1
					},
					pagination : true,
					idField : "times",
					sortName : 'times',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'times',
								title : '日期',
								align : 'center',
								sortable : false
							},
							{
								field : 'channel',
								title : '渠道',
								align : 'center',
								sortable : false
							},
							{
								field : 'newUser',
								title : '新注册数',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain1',
								title : '次日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate1',
								title : '1日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain2',
								title : '2日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate2',
								title : '2日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain3',
								title : '3日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate3',
								title : '3日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain4',
								title : '4日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate4',
								title : '4日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain5',
								title : '5日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate5',
								title : '5日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain6',
								title : '6日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate6',
								title : '6日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain7',
								title : '7日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate7',
								title : '7日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain14',
								title : '14日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate14',
								title : '14日留存率(%)',
								align : 'center',
								sortable : false
							},
							{
								field : 'remain30',
								title : '30日留存',
								align : 'center',
								sortable : false
							},
							{
								field : 'Rate30',
								title : '30日留存率(%)',
								align : 'center',
								sortable : false
							}
							] ]
				})
	})

	function clearQuery() {
	$("#channel").combobox('reset');
		$("#searchos").combobox("reset");
		$("#startdate").textbox('reset');
		$("#enddate").textbox('reset');
		$("#platform").combobox("reset");
		$("#category").combobox("reset");
	}
	
	function queryRemain(){
		$("#remainlist").datagrid('reload', {
			os : $("#searchos").combobox("getValue"),
			channel:$("#channel").combobox("getValue"),
			platform:$("#platform").textbox("getValue"),
			startDate : $("#startdate").textbox("getValue"),
			endDate : $("#enddate").textbox("getValue"),
			category: $("#category").combobox("getValue"),
			type: 1
		})
	}
	
	//导出表格数据
	function listToExcel(){
		var url = "../../operat/listToExcel?searchos="+$("#searchos").combobox("getValue")+"&channel="+$("#channel").combobox("getValue")+"&platform="+$("#platform").textbox("getValue")+"&startDate"+$("#startdate").textbox('getValue')+"&endDate="+$("#startdate").textbox('getValue')+"&category="+$("#category").combobox("getValue")+"&type=1";
		window.location.href=url;
		/* $.ajax({
			url:'../../operat/listToExcel',
			type:'get',
			data:{"searchos":$("#searchos").combobox("getValue"),"channel":$("#channel").combobox("getValue"),"platform":$("#platform").textbox("getValue"),"startDate":$("#startdate").textbox('getValue'),"endDate":$("#startdate").textbox('getValue')},
			success:function(data){
			alert(data);
				if(data){
					alert("导出成功");
				}else{
					alert("导出失败");
				}
			}
		}) */
	}
</script>
</head>
<body>
	<div>
		<table id="remainlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				类别:<select id="category" class="easyui-combobox" name="category"
					style="width:90px;">
					<option value="1">以日期分类</option>
					<option value="2">以渠道分类</option>
				</select>
				<span style="display: none;">
				平台:<select id="platform" class="easyui-combobox" name="platform"
					style="width:90px;">
					<option value="0">全部</option>
					<option value="1">小猪直播</option>
					<option value="2">暴风秀场</option>
				</select>
				</span>
				登录类型:<select id="searchos" class="easyui-combobox" name="searchos"
					style="width:90px;">
					<option value="0">全部</option>
					<option value="1">android</option>
					<option value="2">apple</option>
					<option value="3">H5</option>
					<option value="5">WEB</option>
				</select> 
				&nbsp;渠道:<input id=channel name="channel" class="easyui-combobox" required="true"
						data-options="valueField: 'channelCode',
   									textField: 'channelName',
   									url: '../../operat/getChannelForSelect'"
   						/>
				&nbsp;开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
				
				<a href="javascript:queryRemain()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>
					&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:listToExcel();" class="easyui-linkbutton">导出</a>
			</div>
		</div>
	</div>
</body>
</html>
