<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>主播麦时统计</title>
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<style type="text/css">
	td{
		text-align: center;
	}
</style>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {
		//设置时间
		var curr_time = new Date();
		$("#startTime").datetimebox("setValue", todayformatterStart(curr_time));
		$("#endTime").datetimebox("setValue", todayformatterEnd(curr_time));
		var anchorId = $("#anchorId").textbox("getValue");
		var startTime = $('#startTime').datetimebox('getValue');
		var endTime = $('#endTime').datetimebox('getValue');

		dataGrid = $("#anchorMicTime").datagrid(
				{
					url : '../../anchorTime/getAnchorMicTimeList',
					width : getWidth(0.99),
					height : getHeight(0.98),
					title : '麦时统计',
					pagination : true,
					showFooter : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					queryParams : {
						anchorId : anchorId,
						startTime : startTime,
						endTime : endTime,
						gstype : $('#gstype').combobox("getValue"),
						gsid : $('#gsid').textbox("getValue")
					},
					columns : [ [
							{
								field : 'uid',
								title : '主播ID',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'nickname',
								title : '主播昵称',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'RepDate',
								title : '开播日期',
								align : 'center',
								width : 80,
								sortable : false,
							},
							{
								field : 'openTime',
								title : '开播时长',
								align : 'center',
								sortable : false,
								width : 150,
								formatter : function(data,row) {
										return "<a href='javascript:getAnchorLiveSta("+row.uid+",\""+row.RepDate+"\")' >"+openTimeFormate(data) +"</a>";
								}
							},
							{
								field : 'salesmanId',
								title : '所属家族助理',
								align : 'center',
								width : 250,
								sortable : false,
								formatter : function(data, row) {
									return row.salesmanName + '<br>'
											+ row.salesmanContactsPhone
								}
							},
							{
								field : 'agentUserName',
								title : '所属黄金公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter : function(data, row) {
									return data + '<br>'
											+ row.agentUserContactsName + ","
											+ row.agentUserContactsPhone
								}
							},
							{
								field : 'promotersName',
								title : '所属铂金公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter : function(data, row) {
									return data + '<br>'
											+ row.promotersContactsName + ","
											+ row.promotersContactsPhone
								}
							},
							{
								field : 'extensionCenterName',
								title : '所属钻石公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter : function(data, row) {
									return data + '<br>'
											+ row.extensionCenterContactsName
											+ ","
											+ row.extensionCenterContactsPhone
								}
							},
							{
								field : 'strategicPartnerName',
								title : '星耀公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter : function(data, row) {
									return data + '<br>'
											+ row.strategicPartnerContactsName
											+ ","
											+ row.strategicPartnerContactsPhone
								}
							}
							]],onLoadSuccess:function(data){
								$.ajax({
									url : '../../anchorTime/getAllAnchorTime',
									type: 'POST',
									data:{
										anchorId : $("#anchorId").textbox("getValue"),
										startTime : $('#startTime').datetimebox('getValue'),
										endTime : $('#endTime').datetimebox('getValue'),
										gstype : $('#gstype').combobox("getValue"),
										gsid : $('#gsid').textbox("getValue")
									},
									success:function(result){
										if(result.allAnchorTime == null){
											return;
										}
										var sum = openTimeFormate(result.allAnchorTime);
										$('#anchorMicTime').datagrid('appendRow', {
											uid: '<span class="subtotal">合计</span>',
											nickname: '<span></span>',
											RepDate: '<span class="subtotal">'+sum+'</span>',
											
										});
							            $("#anchorMicTime").datagrid('mergeCells',{ 
							      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
											field: 'uid',                	//合并单元格的区域，就是clomun中的filed对应的列
											colspan: 2                		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
										});
							            
							            $("#anchorMicTime").datagrid('mergeCells',{ 
							      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
											field: 'RepDate',                	//合并单元格的区域，就是clomun中的filed对应的列
											colspan: 7               		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
										});
									}
								});
							}
				});
	})
	
	function getAnchorLiveSta(uid,addTime){
		var url = "../../anchorTime/getAnchorLiveSta";
		var params = {
				"anchorId" : uid,
				"addTime" : addTime			
		}
		$.post(url, params, function(result) {
			var data = eval("(" + result + ")");
			var message = '<table border=1 bordercolor=#ddd style="border-collapse:collapse;width:100%"><tr><th>序号</th><th>开播时间</th><th>关播时间</th><th>总时长(分)</th></tr>';
			for (var i in data) {
				var index = parseInt(i)+1;
				message = message+"<tr><td>"+index+"</td>"+"<td>"+staTimeFormate(data[i].starttime)+"</td>"
						+"<td>"+staTimeFormate(data[i].endtime)+"</td>"+"<td>"+parseInt((data[i].endtime-data[i].starttime)/60)+"</td>"
							
			}
			message = message+"</table>";
			$.messager.show({
				title:'主播开播纪录('+addTime+')',
				msg:message,
				width:450,
				height:150,
				style:{
					top:$(window).height()/2-150,
					left:$(window).width()/2-200
				},
				timeout:0
			});
		})
	}

	function staTimeFormate(data){
		var date = new Date(data*1000);
		var mon = date.getMonth()+1;
		var d = date.getDate();
		var h = date.getHours();
		var m = date.getMinutes();
		var s = date.getSeconds();
		return (mon < 10 ? ('0' + mon) : mon)+'-'+(d < 10 ? ('0' + d) : d)+' '+(h < 10 ? ('0' + h) : h) + ':' + (m < 10 ? ('0' + m) : m) + ':'+(s < 10 ? ('0' + s) : s);
	}
	
	function openTimeFormate(data){
		var sm = Math.round(data / 60);
		var h = parseInt(sm/60); 
		var m = sm%60;	
		return h+"小时"+(m < 10 ? ('0' + m) : m)+"分钟";
	}
	
	function dateFormate(data){
		var date = new Date(data);
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'+ (d < 10 ? ('0' + d) : d) ;
	}
	
	function myformatterStart(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-01 00:00:00';
	}

	function myformatterEnd(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
				+ (d < 10 ? ('0' + d) : d) + ' 23:59:59';
	}

	function todayformatterStart(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
				+ (d < 10 ? ('0' + d) : d) + ' 00:00:00';
	}

	function todayformatterEnd(date) {
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m < 10 ? ('0' + m) : m) + '-'
				+ (d < 10 ? ('0' + d) : d) + ' 23:59:59';
	}

	function doQuery() {
		var anchorId = $("#anchorId").textbox("getValue");
		var startTime = $('#startTime').datetimebox('getValue');
		var endTime = $('#endTime').datetimebox('getValue');
		$("#anchorMicTime").datagrid({
			url : '../../anchorTime/getAnchorMicTimeList',
			queryParams : {
				anchorId : anchorId,
				startTime : startTime,
				endTime : endTime,
				gstype : $('#gstype').combobox("getValue"),
				gsid : $('#gsid').textbox("getValue"),
			}
		})
	}

	function exportExcel() {
		$("#ff").submit();
	}
</script>
</head>
<body>
	<table id="anchorMicTime"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<form id="ff" method="post" action="../../anchorTime/expExcel"
				target="ifr">
				&nbsp;主播ID：&nbsp;<input type="text" name="anchorId" id="anchorId"
					size="10" class="easyui-numberbox" data-options="precision:0" />
				&nbsp;归属：&nbsp;<select id="gstype" name="gstype"
					class="easyui-combobox" style="width: 100px">
					<option value="1" selected="selected" >星耀公会</option>
					<option value="2">钻石公会</option>
					<option value="3">铂金公会</option>
					<option value="4">黄金公会</option>
					<option value="5">家族助理</option>
				</select>ID:&nbsp;<input id=gsid name="gsid" class="easyui-numberbox" />
				&nbsp;开始时间：&nbsp;<input class="easyui-datetimebox"
					style="width: 160px; height: 24px" name="startTime" id="startTime" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datetimebox"
					style="width: 160px; height: 24px" name="endTime" id="endTime" />
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:doQuery()"
					class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:exportExcel()"
					class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</form>
		</div>
	</div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>