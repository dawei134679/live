<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date,com.tinypig.admin.util.DateUtil" %>
<html>
<head>
<title>游戏历史</title>
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
		dataGrid = $("#gameRecordList").datagrid({
				url : '../../gameRecordSta/gameRecordList',
				width : getWidth(0.97),
				height : getHeight(0.97),
				title : '游戏历史',
				queryParams: {},
				pagination : true,
				rownumbers : true,
				striped : true,
				singleSelect : true,
				fit : true,
				fitColumns : true,
				pageSize : 20,
				pageList : [ 20, 50, 100 ],
				toolbar : '#tb',
				columns : [[
					{field : 'uid',title : '用户UID',align : 'center',width : 80,sortable : false,formatter : function(data) {
						if('合计'==data){
							return '<span class="subtotal">合计</span>';
						}
						return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
					}},
					{field : 'roomId',title : '主播ID',align : 'center',width : 150,sortable : false,formatter : function(data) {
						return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
					}},
					{field : 'type',title : '游戏种类',width : 150,align : 'center',sortable : false,formatter : function(data) {
						var ary = ['','开心敲敲乐','欢乐6选3','抓娃娃'];
						return ary[data];
					}},
					{field : 'money',title : '游戏金额',align : 'center',width : 150,sortable : false},
					{field : 'profit',title : '平台盈亏情况',width : 150,align : 'center',sortable : false,formatter : function(data) {
						var hm = "";
						if(data > 0){
							hm = '<font color="red">'+data+'</font>';
						} else{
							hm = '<font color="green">'+data+'</font>';
						}
						return hm;
					}},
					{field : 'ctime',title : '游戏时间',width : 150,align : 'center',sortable : false,formatter : function(data) {
						return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
					}}
				]],
				onLoadSuccess:function(data){
					$.ajax({
						url : '../../gameRecordSta/getGameRecordTotal',
						data:{
							uid : $('#searchUid').textbox("getValue"),
							roomId : $('#roomId').textbox("getValue"),
							gtype : $('#gtype').combobox("getValue"),
							gstype:$('#gstype').combobox("getValue"),
							gsid : $('#gsid').textbox("getValue"),
							startDate : $("#startDate").textbox('getValue'),
							endDate : $("#endDate").textbox('getValue')
						},
						success:function(result){
							if(result==null){
								return;
							}
							var d = result;
							$('#gameRecordList').datagrid('appendRow', {
								uid: '合计',
								roomId: '<span class="subtotal"></span>',
								type: '<span class="subtotal"></span>',
								profit: '<span class="subtotal">'+d[0].profitTotal+'</span>',
								money: '<span class="subtotal">'+d[0].priceTotal+'</span>',
								ctime: null
					        });
				            $("#gameRecordList").datagrid('mergeCells',{ 
				      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
								field: 'uid',                	//合并单元格的区域，就是clomun中的filed对应的列
								colspan: 2                		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
							});
						}
					});
				},
			});
	})


	function doQuery() {
		$("#gameRecordList").datagrid('load', {
			uid : $('#searchUid').textbox("getValue"),
			roomId : $('#roomId').textbox("getValue"),
			gtype : $('#gtype').combobox("getValue"),
			gstype:$('#gstype').combobox("getValue"),
			gsid : $('#gsid').textbox("getValue"),
			startDate : $("#startDate").textbox('getValue'),
			endDate : $("#endDate").textbox('getValue')
		});
	}
	
	function memberDetail(uid){
		 var iframe = "<iframe src='../user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg1').html(iframe);
		 $('#dlg1').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}
	
	function exportExcel(){
		$("#ff").submit();
	}
</script>
</head>
<body>
	<table id="gameRecordList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<form id="ff" method="post" action="../../gameRecordSta/expExcel" target="ifr">
			<div>
				用户UID：&nbsp;<input id="searchUid" name="searchUid" class="easyui-numberbox"/>
				主播UID：&nbsp;<input id="roomId" name="roomId" class="easyui-numberbox"/>
				&nbsp;
				游戏类型：&nbsp;
				<select id="gtype" name="gtype" class="easyui-combobox" >
					<option value="">--全部--</option>
					<option value="1">开心敲敲乐</option>
					<option value="2">欢乐6选3</option>
					<option value="3">抓娃娃</option>
				</select> &nbsp;&nbsp;&nbsp;
				归属类型：&nbsp;
				<select id="gstype" name="gstype" class="easyui-combobox" >
					<option value="">--全部--</option>
					<option value="1">星耀公会</option>
					<option value="2">钻石公会</option>
					<option value="3">铂金公会</option>
					<option value="4">黄金公会</option>
					<option value="5">家族助理</option>
				</select> 
				归属UID:&nbsp;<input id=gsid name="gsid" class="easyui-numberbox"/>
				&nbsp;
				&nbsp;
				开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startDate" id="startDate" size="10" />
				结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="endDate"  id="endDate" size="10" />
				&nbsp;&nbsp;<a href="javascript:doQuery()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:exportExcel()" class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</div>
		</form>
	</div>
	<div id="dlg1" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>
