<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date,com.tinypig.admin.util.DateUtil" %>
<html>
<head>
<title>送礼历史统计</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<style type="text/css">
    .subtotal { font-weight: bold; }/*合计单元格样式*/
</style>
<script>
	var dataGrid;
	var url;
	$(function() {
		dataGrid = $("#giftHistoryList").datagrid({
				url : '../../giftSta/giftHistorySta',
				width : getWidth(0.97),
				height : getHeight(0.97),
				title : '送礼历史统计',
				queryParams: {
					startTime : $("#s_startTime").datebox('getValue'),
					endTime : $("#s_endTime").datebox('getValue')
				},
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
					{field : 'srcuid',title : '送礼人ID',align : 'center',width : 80,sortable : false,formatter : function(data) {
						if('合计'==data){
							return '<span class="subtotal">合计</span>';
						}
						return data;
						//return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
					}},
					{field : 'srcnickname',title : '送礼人昵称',align : 'center',width : 150,sortable : false},
					{field : 'dstuid',title : '收礼人ID',align : 'center',width : 150,sortable : false,formatter : function(data) {
						return data;
						//return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
					}},
					{field : 'dstnickname',title : '收礼人昵称',width : 150,align : 'center',sortable : false},
					{field : 'gflag',title : '礼物类型',width : 150,align : 'center',sortable : false,formatter:function(d,r){
						var lx = ["非背包","背包","非背包（pk）","背包（pk）"];
						return lx[d];
					}},
					{field : 'addtime',title : '送礼时间',width : 150,align : 'center',sortable : false,formatter : function(data) {
						return data ? new Date(data * 1000).Format("yyyy-MM-dd HH:mm:ss") : "";
					}},
					//{field : 'gid',title : '礼物ID',width : 150,align : 'center',sortable : false},
					{field : 'gname',title : '礼物名称',width : 150,align : 'center',sortable : false},
					{field : 'price',title : '礼物价值',width : 150,align : 'center',sortable : false},
					{field : 'count',title : '礼物个数',width : 150,align : 'center',sortable : false},
					{field : 'supportUserFlag',title : '是否为虚拟号',width : 150,align : 'center',sortable : false,formatter:function(data){
						var ary = ['否','是'];
						return ary[data];
					}}
				]],
				onLoadSuccess:function(data){
					$.ajax({
						url : '../../giftSta/getGiftTotal',
						data:{
							startTime : $("#s_startTime").datebox('getValue'),
							endTime : $("#s_endTime").datebox('getValue'),
							gflag:$("#s_gflag").combobox('getValue'),
							supportUserFlag : $("#s_supportUserFlag").combobox('getValue'),
							srcuid : $("#s_srcuid").textbox('getValue'),
							dstuid : $("#s_dstuid").textbox('getValue'),
							gname : $("#s_gname").textbox('getValue')
						},
						success:function(result){
							if(result==null){
								return;
							}
							var d = result;
							if(d && d.length > 0 && d[0]){
							 	$('#giftHistoryList').datagrid('appendRow', {
								 srcuid: '合计',
								 srcnickname: '<span class="subtotal"></span>',
								 dstuid: '<span class="subtotal"></span>',
								 dstnickname: '<span class="subtotal"></span>',
								 gflag: '<span class="subtotal"></span>',
								 addtime: '<span class="subtotal"></span>',
								 //gid: '<span class="subtotal"></span>',
								 gname: '<span class="subtotal"></span>',
								 price: '<span class="subtotal">'+d[0].priceTotal+'</span>',
								 count: '<span class="subtotal">'+d[0].countTotal+'</span>',
								 supportUserFlag:'<span class="subtotal"></span>'
					            });
					            $("#giftHistoryList").datagrid('mergeCells',{ 
					      			index: data.rows.length-1,                 //datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
									field: 'srcuid',                 //合并单元格的区域，就是clomun中的filed对应的列
									colspan:7              //纵向合并的格数，如果想要横向合并，就使用colspan：mark
								});
							}
						}
					});
				},
			});
	})


	function doQuery() {
		$("#giftHistoryList").datagrid('load', {
			startTime : $("#s_startTime").datebox('getValue'),
			endTime : $("#s_endTime").datebox('getValue'),
			gflag:$("#s_gflag").combobox('getValue'),
			supportUserFlag : $("#s_supportUserFlag").combobox('getValue'),
			srcuid : $("#s_srcuid").textbox('getValue'),
			dstuid : $("#s_dstuid").textbox('getValue'),
			gname : $("#s_gname").textbox('getValue')
		});
	}
	
	function memberDetail(uid){
		 var iframe = "<iframe src='../user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg').html(iframe);
		 $('#dlg').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}
	//导出数据
	function doExport(){
		$("#ff").submit();
	}
</script>
</head>
<body>
	<table id="giftHistoryList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<form id="ff" method="post" action="../../giftSta/expExcel" target="ifr">
			送礼人ID:&nbsp;<input id="s_srcuid" name="srcuid" class="easyui-numberbox" style="width:100px"/>
			收礼人ID:&nbsp;<input id="s_dstuid" name="dstuid" class="easyui-numberbox" style="width:100px"/>
			礼物名称:&nbsp;<input id="s_gname" name="gname" class="easyui-textbox" style="width:100px"/>
			开始时间：&nbsp;<input class="easyui-datebox" style="width:100px;height:24px" name="startTime" id="s_startTime" value="<%=DateUtil.formatDate(new Date(), "yyyy-MM-dd")%>" size="10" />
			结束时间：&nbsp;<input class="easyui-datebox" style="width:100px;height:24px" name="endTime"  id="s_endTime" value="<%=DateUtil.formatDate(new Date(), "yyyy-MM-dd")%>" size="10" />
			礼物类型：&nbsp;<select id="s_gflag" class="easyui-combobox" editable="false" name="gflag" panelHeight="auto" style="width: 155px">
				<option value="" selected="">全部</option>
				<option value="0">非背包</option>
				<option value="1">背包</option>
			</select>
			是否虚拟号送出：&nbsp;
			<select id="s_supportUserFlag" class="easyui-combobox" editable="false" name="supportUserFlag" panelHeight="auto" style="width:60px">
				<option value="">全部</option><option value="1">是</option><option value="0">否</option>
			</select>
			&nbsp;&nbsp;<a href="javascript:doQuery()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
			&nbsp;&nbsp;<a href="javascript:doExport()" class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</form>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>
