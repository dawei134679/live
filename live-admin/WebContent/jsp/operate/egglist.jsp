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
<title>砸蛋列表</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;
	var url;
	var datetimes;
	var hammer;
	$(function() {
		dataGrid = $("#egglist").datagrid(
				{
					url : '../../operat/egglist',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '砸蛋列表',
					pagination : true,
					idField : "times",
					sortName : 'times',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 30,
					pageList : [ 30, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'datetimes',
								title : '日期',
								align : 'center',
								width : 100,
								sortable : false
							},{
								field : 'times',
								title : '砸蛋次数',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'consume',
								title : '消耗金币',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'gid1',
								title : '礼物一',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(1);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gid2',
								title : '礼物二',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(2);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gid3',
								title : '礼物三',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(3);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gid4',
								title : '礼物四',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(4);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gid5',
								title : '礼物五',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(5);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gid6',
								title : '礼物六',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(6);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gid7',
								title : '礼物七',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(7);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gid8',
								title : '礼物八',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return '<a href="javascript:getUserlist(8);" class="easyui-tooltip" title="查看中奖详细">'+data+'</a>';
								}
							},
							{
								field : 'gets',
								title : '中奖金币',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'profit',
								title : '收益',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'odds',
								title : '赔率[%]',
								align : 'center',
								width : 150,
								sortable : false
							}
							] ]
				})
	})

	function clearQuery() {
		$("#condition").combobox('reset');
		$("#startdate").textbox('reset');
		$("#enddate").textbox('reset');
	}
	
	function queryEggList(){
		hammer = $("#condition").combobox('getValue');
		$("#egglist").datagrid('reload', {
			condition : $("#condition").combobox('getValue'),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		})
	}
	
	function getUserlist(param){
		$('#dlgUserList').form("clear");
		var row = $('#egglist').datagrid('getSelected');
		datetimes = row.datetimes;
		$('#dlgUserList').dialog('open').dialog('center').dialog('setTitle','砸蛋明细');
		dataGrid = $("#userList").datagrid(
				{
					url : '../../operat/egglist/userlist?type='+param+"&datetimes="+datetimes+"&hammer="+hammer,
					width : getWidth(0.97),
					height : getHeight(0.97),
					pagination : false,
					rownumbers : true,
					singleSelect : true,
					columns : [ [
							{
								field : 'createAt',
								title : '时间',
								align : 'center',
								width : 150,
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'uid',
								title : '用户UID',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户昵称',
								align : 'center',
								width : 200,
								sortable : false
							},
							{
								field : 'userLevel',
								title : '用户等级',
								width : 100,
								align : 'center',
								sortable : true
							},
							{
								field : 'roomId',
								title : '房间ID',
								align : 'center',
								sortable : false
							},
							{
								field : 'roomName',
								title : '房间名称',
								width : 200,
								align : 'center',
								sortable : false
							},
							{
								field : 'anchorLevel',
								title : '房间等级',
								align : 'center',
								sortable : true
							},
							{
								field : 'reward_gift_name',
								title : '礼物名称',
								align : 'center',
								width : 200,
								sortable : false
							}
							] ]
				})
	}
</script>
</head>
<body>
	<div>
		<table id="egglist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
			<select id="condition" class="easyui-combobox" name="condition"
					style="width:120px;" required="true" missingMessage="类型必填">
					<option value="1" selected="selected">铜锤[10金币]</option>
					<option value="2">金锤[25金币]</option>
					<option value="3">紫锤[50金币]</option>
				</select>
				开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
				&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
				&nbsp;&nbsp; 
				<a href="javascript:queryEggList()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:clearQuery();" class="easyui-linkbutton">清空</a> (<font color="red">包含开始时间和结束时间</font>)
			</div>
			<div>
			 *铜锤 10金币[礼物排序&nbsp;&nbsp;&nbsp;&nbsp;gid1:小色猪*5&nbsp;&nbsp;&nbsp;&nbsp;gid2:小色猪*7&nbsp;&nbsp;&nbsp;&nbsp;gid3:小黄鸡*2&nbsp;&nbsp;&nbsp;&nbsp;gid4:小黄鸡*3&nbsp;&nbsp;&nbsp;&nbsp;gid5:亲亲*3&nbsp;&nbsp;&nbsp;&nbsp;gid6:亲亲*6&nbsp;&nbsp;&nbsp;&nbsp;gid7:金砖&nbsp;&nbsp;&nbsp;&nbsp;gid8:跑车]
			</div>
			<div>
			 *金锤 25金币[礼物排序&nbsp;&nbsp;&nbsp;&nbsp;gid1:小色猪*10&nbsp;&nbsp;&nbsp;&nbsp;gid2:小色猪*15&nbsp;&nbsp;&nbsp;&nbsp;gid3:小黄鸡*5&nbsp;&nbsp;&nbsp;&nbsp;gid4:小黄鸡*7&nbsp;&nbsp;&nbsp;&nbsp;gid5:亲亲*7&nbsp;&nbsp;&nbsp;&nbsp;gid6:亲亲*10&nbsp;&nbsp;&nbsp;&nbsp;gid7:甲壳虫&nbsp;&nbsp;&nbsp;&nbsp;gid8:兰博基尼]
			 </div>
			<div>
			 *紫锤 50金币[礼物排序&nbsp;&nbsp;&nbsp;&nbsp;gid1:小色猪*15&nbsp;&nbsp;&nbsp;&nbsp;gid2:小色猪*20&nbsp;&nbsp;&nbsp;&nbsp;gid3:小黄鸡*8&nbsp;&nbsp;&nbsp;&nbsp;gid4:小黄鸡*10&nbsp;&nbsp;&nbsp;&nbsp;gid5:亲亲*10&nbsp;&nbsp;&nbsp;&nbsp;gid6:亲亲*15&nbsp;&nbsp;&nbsp;&nbsp;gid7:兰博基尼&nbsp;&nbsp;&nbsp;&nbsp;gid8:飞机]
			</div>
		</div>
	</div>
	
	<div id="dlgUserList" class="easyui-dialog" style="width:80%;height:80%" closed="true" maximizable="true" >
		<table id="userList"></table>
	</div>
</body>
</html>