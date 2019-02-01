<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date,com.tinypig.admin.util.DateUtil" %>
<html>
<head>
<title>开心悄悄乐统计</title>
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
		dataGrid = $("#SmashedEggList").datagrid({
				url : '../../smashedEggSta/smashedEggList',
				width : getWidth(0.97),
				height : getHeight(0.97),
				title : '开心悄悄乐统计',
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
					{field : 'uid',title : '会员ID',align : 'center',width : 80,sortable : false},
					{field : 'roomId',title : '直播间ID',align : 'center',width : 150,sortable : false},
					{field : 'createAt',title : '游戏时间',width : 150,align : 'center',sortable : false,formatter : function(data) {
						return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
					}},
					{field : 'hammerPrice',title : '游戏金额',align : 'center',width : 150,sortable : false},
					{field : 'rewardGiftTotalPrice',title : '礼物价值总金额',width : 150,align : 'center',sortable : false}
				]],
				onLoadSuccess:function(data){
					$.ajax({
						url : '../../smashedEggSta/getSmashedEggTotal',
						data:{
							uid : $('#searchUid').textbox("getValue"),
							roomId : $('#roomId').textbox("getValue"),
							startDate : $("#startdate").textbox('getValue'),
							endDate : $("#enddate").textbox('getValue')
						},
						success:function(result){
							if(result==null){
								return;
							}
							var d = result;
							$('#SmashedEggList').datagrid('appendRow', {
								uid: '<span class="subtotal">合计</span>',
								roomId: '<span class="subtotal"></span>',
								createAt: '<span class="subtotal"></span>',
								hammerPrice: '<span class="subtotal">'+d[0].priceTotal+'</span>',
								rewardGiftTotalPrice: '<span class="subtotal">'+d[0].allPriceTotal+'</span>'
					        });
				            $("#SmashedEggList").datagrid('mergeCells',{ 
				      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
								field: 'uid',                	//合并单元格的区域，就是clomun中的filed对应的列
								colspan: 3                		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
							});
						}
					});
				},
			});
	})


	function doQuery() {
		$("#SmashedEggList").datagrid('load', {
			uid : $('#searchUid').textbox("getValue"),
			roomId : $('#roomId').textbox("getValue"),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		});
	}
</script>
</head>
<body>
	<table id="SmashedEggList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			用户UID：&nbsp;<input id="searchUid" name="searchUid" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
			主播UID：&nbsp;<input id="roomId" name="roomId" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
			开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10" />
			结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10" />
			&nbsp;&nbsp;<a href="javascript:doQuery()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
		</div>
	</div>
</body>
</html>
