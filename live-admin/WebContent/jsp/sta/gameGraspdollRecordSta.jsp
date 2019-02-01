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
<style type="text/css">
      .subtotal { font-weight: bold; }/*合计单元格样式*/
</style>
<script>
	var dataGrid;
	var url;

	$(function() {
		dataGrid = $("#betlist").datagrid({
			url : '../../gameGraspdollRecord/getGameGraspdollRecordPage',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '抓娃娃游戏统计列表',
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
					url : '../../gameGraspdollRecord/getGameGraspdollRecordTotal',
					data:{anchorId : $('#anchorId').val(),
						uid:$('#uid').val(),
						startTime : $("#startTime").datebox('getValue'),
						endTime : $("#endTime").datebox('getValue')},
					success:function(result){
						if(result==null){
							return;
						}
						var d = eval("(" + result + ")");;
						 $('#betlist').datagrid('appendRow', {
				                uid: '<span class="subtotal">合计</span>',
				                anchorId: '<span class="subtotal"></span>',
				                periods: '<span class="subtotal"></span>',
				                createAt: '<span class="subtotal"></span>',
				                pawsPrice: '<span class="subtotal">'+d.pawsPrice+'</span>',
				                totalPrice: '<span class="subtotal">'+d.totalPrice+'</span>'
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
			},{
				field : 'createAt',
				title : '时间',
				align : 'center',
				width : 50,
				sortable : false,
				formatter:function(data,row){
					if(data==null||data==0){
						return "";
					}
					var date = new Date();  
				    date.setTime(data);  
				    var y = date.getFullYear();      
				    var m = date.getMonth() + 1;      
				    m = m < 10 ? ('0' + m) : m;      
				    var d = date.getDate();      
				    d = d < 10 ? ('0' + d) : d;      
				    var h = date.getHours();    
				    h = h < 10 ? ('0' + h) : h;    
				    var minute = date.getMinutes();    
				    var second = date.getSeconds();    
				    minute = minute < 10 ? ('0' + minute) : minute;      
				    second = second < 10 ? ('0' + second) : second;     
				    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;        
				}
			},{
				field : 'pawsPrice',
				title : '爪子价格',
				align : 'center',
				width : 50,
				sortable : false
			},{
				field : 'totalPrice',
				title : '中奖总金额',
				align : 'center',
				width : 50,
				sortable : false
			}] ]
		})
	});
	
	function searchBetList() {
		$('#betlist').datagrid('load', {
			anchorId : $('#anchorId').val(),
			uid:$('#uid').val(),
			startTime : $("#startTime").datebox('getValue'),
			endTime : $("#endTime").datebox('getValue')
		});
	}

</script>
</head>
<body style="margin: 5px;">
	<table id="betlist"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			主播ID：&nbsp;<input id="anchorId" />
			&nbsp;&nbsp;用戶ID：&nbsp;<input id="uid" />
			&nbsp;&nbsp;开始时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="startTime" id="startTime" size="10" />
			&nbsp;&nbsp;结束时间：&nbsp;<input class="easyui-datebox" style="width:160px;height:24px" name="endTime"  id="endTime" size="10" />
			<a href="javascript:searchBetList()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			<a href="javascript:expExcel()" class="easyui-linkbutton" iconCls="icon-save" plain="true">导出</a>
		</div>
	</div>
</body>
</html>