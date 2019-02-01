<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>押注统计</title>
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var dataGrid;
	var url;

	$(function() {
		dataGrid = $("#betlist").datagrid({
			url : '../../bl/getBetList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '欢乐六选三游戏统计列表',
			pagination : true,
			rownumbers : true,
			striped : true,
			singleSelect : true,
			fit : true,
			fitColumns : true,
			pageSize : 20,
			pageList : [ 20, 50, 100 ],
			toolbar : '#tb',
			onLoadSuccess:function(data){
				$.ajax({
					url : '../../bl/getBetTotal',
					data:{periods : $('#periods').val(),
						  anchorId : $('#anchorId').val(),
						  uid:$('#uid').val()},
					success:function(result){
						if(result==null){
							return;
						}
						var d = eval("(" + result + ")");;
						 $('#betlist').datagrid('appendRow', {
				                uid: '<span class="subtotal">合计</span>',
				                anchorId: '<span class="subtotal"></span>',
				                periods: '<span class="subtotal"></span>',
				                betTotal: '<span class="subtotal">'+d.betTotal+'</span>',
				                capitalTotal: '<span class="subtotal">'+d.capitalTotal+'</span>',
				                awTotal: '<span class="subtotal">'+d.awTotal+'</span>',
				                deservedTotal: '<span class="subtotal">'+d.deservedTotal+'</span>',
				                commissionTotal: '<span class="subtotal">'+d.commissionTotal+'</span>',
				            });
				            $("#betlist").datagrid('mergeCells',{ 
				      			index: data.rows.length-1,                 //datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
								field: 'uid',                 //合并单元格的区域，就是clomun中的filed对应的列
								colspan:3                //纵向合并的格数，如果想要横向合并，就使用colspan：mark
							}); 
					}
				});
			},
			columns : [[{
				field : 'uid',
				title : '用戶ID',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'anchorId',
				title : '主播ID',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'periods',
				title : '押注期数',
				align : 'center',
				width : 50,
				sortable : false,
			}, {
				field : 'betTotal',
				title : '押注总筹码',
				align : 'center',
				width : 50,
				sortable : false
			},{
				field : 'capitalTotal',
				title : '押注中奖总筹码',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'awTotal',
				title : '应返总筹码',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'deservedTotal',
				title : '实返总筹码',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'commissionTotal',
				title : '平台抽成总筹码',
				align : 'center',
				width : 50,
				sortable : false
			} ] ]
		})
	});
	
	function searchBetList() {
		$('#betlist').datagrid('load', {
			periods : $('#periods').val(),
			anchorId : $('#anchorId').val(),
			uid:$('#uid').val()
		});
	}

</script>
</head>
<body style="margin: 5px;">
	<table id="betlist"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			期数:&nbsp;<input id="periods" />
			&nbsp;&nbsp;主播ID：&nbsp;<input id="anchorId" />
			用戶ID：&nbsp;<input id="uid" />
			<a href="javascript:searchBetList()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			<a href="javascript:expExcel()" class="easyui-linkbutton" iconCls="icon-save" plain="true">导出</a>
		</div>
	</div>
</body>
</html>