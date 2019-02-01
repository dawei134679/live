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
<title>主播结算基础数据</title>
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
		dataGrid = $("#wageslist").datagrid(
				{
					url : '../../operat/wages',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '基础列表',
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
								width : 80,
								sortable : false
							},
							{
								field : 'unionname',
								title : '工会名称',
								width : 150,
								align : 'center',
								sortable : false
							},
							{
								field : 'anchorid',
								title : '主播UID',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'validday',
								title : '有效天',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'airtime',
								title : '有效时长',
								width : 200,
								align : 'center',
								sortable : false,
								formatter : function(data) {
					            	var h = parseInt(data/3600);
					            	var hl = data%3600;
					            	var m = parseInt(hl/60);
					            	var ml = hl%60;
					            	var str = "";
					            	if(h > 0){ str = h +"小时"}
					            	if(m > 0){ str = str+m+"分钟"}
					            	if(ml > 0){str = str+ml+"秒"}
					            	return str;
								}
							},
							{
								field : 'credits',
								title : '获取声援值',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'weekstar',
								title : '周星奖励',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'activity',
								title : '活动奖励',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'exchange',
								title : '内兑',
								width : 100,
								align : 'center',
								sortable : false
							},
							{
								field : 'withdraw',
								title : '提现',
								width : 100,
								align : 'center',
								sortable : false
							}
							] ]
				})
	})

	function clearQuery() {
		$("#uid").textbox('reset');
		$("#dateYM").textbox('reset');
		$("#unionid").combobox("reset");
	}
	
	function queryWages(){
		$("#wageslist").datagrid('reload', {
			uid : $("#uid").textbox("getValue"),
			dateYM : $("#dateYM").textbox('getValue'),
			unionid : $("#unionid").combobox('getValue'),
			searchos : $("#searchos").combobox("getValue")
		})
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
<body>
	<div>
		<table id="wageslist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<select id="searchos" class="easyui-combobox" name="searchos"
					style="width:90px;">
					<option value="0">全部</option>
					<option value="1">工会</option>
					<option value="2">自由人</option>
				</select>
				选择工会：&nbsp;<input id=unionid name="unionid" class="easyui-combobox" required="true"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../../unionAnchor/getUnionNameList'"
    						/>
				&nbsp;选择年月：&nbsp;<input class="easyui-datebox" style="width:120px;height:24px" name="dateYM" id="dateYM" size="10" data-options="formatter:myformatter,parser:myParser"/>
				&nbsp;主播UID：&nbsp;<input class="easyui-textbox" style="width: 100px" id="uid" name="uid">
				
				<a href="javascript:queryWages()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>&nbsp;&nbsp;&nbsp;&nbsp; <a
					href="javascript:clearQuery();" class="easyui-linkbutton">清空</a>
			</div>
		</div>
	</div>
</body>
</html>
