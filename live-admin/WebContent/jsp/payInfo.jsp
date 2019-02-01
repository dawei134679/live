<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>充值信息</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {
		//设置时间
 		var curr_time = new Date();   
		$("#startTime").datetimebox("setValue",todayformatterStart(curr_time));
		$("#endTime").datetimebox("setValue",todayformatterEnd(curr_time));
		
		var status = $("#status").combobox("getValue")
		var srcuid =  $("#srcuid").textbox("getValue");
		var startTime = $('#startTime').datetimebox('getValue');
		var endTime = $('#endTime').datetimebox('getValue');
		
		dataGrid = $("#payInfo").datagrid(
				{
					url : '../payOrderSta/pageList',
					width : getWidth(0.99),
					height : getHeight(0.98),
					title : '充值信息',
					pagination : true,
					showFooter : true,
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					queryParams: {
						srcuid: srcuid,
				        startTime:startTime,
				        endTime:endTime,
				        status:status,
				        gstype:$('#gstype').combobox("getValue"),
						gsid : $('#gsid').textbox("getValue"),
						paytype:$("#paytype").combobox("getValue")
					},
					columns : [ [
							{
								field : 'srcuid',
								title : '用户UID',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'srcnickname',
								title : '用户昵称',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'orderId',
								title : '交易号',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'amount',
								title : '金额',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'status',
								title : '订单状态',
								align : 'center',
								width : 80,
								sortable : false,
								formatter : function(data){
									if(data==0){
										return "生成订单";
									}else if(data==1){
										return "等待支付";
									}else if(data==2){
										return "已支付"
									}else if(data==3){
										return "取消"
									}
								}
							},
							{
								field : 'dataTypeStr',
								title : '数据类型',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'inpourNo',
								title : '第三方交易号',
								align : 'center',
								width : 250,
								sortable : false
							},
							{
								field : 'paytype',
								title : '充值方式',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'creatAt',
								title : '创建时间',
								align : 'center',
								sortable : false,
								width : 150,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'paytime',
								title : '充值时间',
								align : 'center',
								sortable : false,
								width : 150,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},{
								field : 'registtime',
								title : '注册时间',
								align : 'center',
								sortable : false,
								width : 150,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							}, {
								field : 'salesmanId',
								title : '所属家族助理',
								align : 'center',
								width : 250,
								sortable : false,
								formatter:function(data,row){
									return row.salesmanName+'<br>'+row.salesmanContactsPhone
								}
							}, {
								field : 'agentUserName',
								title : '所属黄金公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.agentUserContactsName+","+row.agentUserContactsPhone
								}
							}, {
								field : 'promotersName',
								title : '所属铂金公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.promotersContactsName+","+row.promotersContactsPhone
								}
							}, {
								field : 'extensionCenterName',
								title : '所属钻石公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.extensionCenterContactsName+","+row.extensionCenterContactsPhone
								}
							},{
								field : 'strategicPartnerName',
								title : '星耀公会',
								align : 'center',
								width : 250,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.strategicPartnerContactsName+","+row.strategicPartnerContactsPhone
								}
							},{
								field : 'dataType',
								title : '操作',
								align : 'center',
								width : 50,
								sortable : false,
								formatter:function(data,row){
									if(data==2){
										return "<a href='javascript:del(\""+row.orderId+"\")' >删除</a>";										
									}
									return "-";
								}
							}
							]],onLoadSuccess:function(data){
								$.ajax({
									url : '../payOrderSta/getPayOrderTotalAmount',
									type: 'POST',
									data:{
										srcuid:$("#srcuid").textbox("getValue"),
								        startTime:$('#startTime').datetimebox('getValue'),
								        endTime:$('#endTime').datetimebox('getValue'),
								        status:$("#status").combobox("getValue"),
								        paytype:$("#paytype").combobox("getValue"),
								        gstype:$('#gstype').combobox("getValue"),
										gsid : $('#gsid').textbox("getValue"),
										dataType:$("#dataType").combobox("getValue")
									},
									success:function(result){
										if(result==null){
											return;
										}
										var d = result;
										$('#payInfo').datagrid('appendRow', {
											srcuid: '<span class="subtotal">合计</span>',
											srcnickname: '<span></span>',
											orderId: '<span></span>',
											amount:  '<span class="subtotal">'+result.totalAmount+'</span>',
											status:  '<span></span>'
										
								        });
							            $("#payInfo").datagrid('mergeCells',{ 
							      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
											field: 'srcuid',                	//合并单元格的区域，就是clomun中的filed对应的列
											colspan: 3                		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
										});
							            
							            $("#payInfo").datagrid('mergeCells',{ 
							      			index: data.rows.length-1,		//datagrid的index，表示从第几行开始合并；紫色的内容需是最精髓的，就是记住最开始需要合并的位置
											field: 'status',                	//合并单元格的区域，就是clomun中的filed对应的列
											colspan: 13               		//纵向合并的格数，如果想要横向合并，就使用colspan：mark
										});
									}
								});
							},
				})
				
				$("#fm #uid").textbox({
					onChange:function(data){
						var uid = $.trim(data);
						if(uid == ""){
							return;
						}
						$.ajax({
				             type: "post",
				             dataType: "json",
				             url: "../anchor/getNickName",
				             data: { uid:uid},
				             dataType: 'json', 
				             success: function(data) {
				             	if(data.success == 200){
				             		$("#fm #nickname").textbox("setValue",data.nickname);
				             	}else{
									$.messager.alert("系统提示", data.msg);
				             	}
				             }
				       	});
					}
				});
		
		 $(window).resize(function () {
	            $('#payInfo').datagrid('resize', {
	            	width : getWidth(0.99),
					height : getHeight(0.98)
	            });
	        });
	})
	
	 function myformatterStart(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            return y+'-'+(m<10?('0'+m):m)+'-01 00:00:00';
        }

	 function myformatterEnd(date){
           var y = date.getFullYear();
           var m = date.getMonth()+1;
           var d = date.getDate();
           return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' 23:59:59';
       }
	 
	 function todayformatterStart(date){
         var y = date.getFullYear();
         var m = date.getMonth()+1;
         var d = date.getDate();
         return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' 00:00:00';
     }

	 function todayformatterEnd(date){
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' 23:59:59';
    }
	
	function searchUser(){
		var srcuid = $("#srcuid").textbox("getValue");
		var startTime = $('#startTime').datetimebox('getValue');
		var endTime = $('#endTime').datetimebox('getValue');
		var status = $("#status").combobox("getValue");
		var paytype = $("#paytype").combobox("getValue");
		
		
		$("#payInfo").datagrid({
			url : '../payOrderSta/pageList',
			queryParams: {
		        srcuid:srcuid,
		        startTime:startTime,
		        endTime:endTime,
		        status:status,
		        paytype:paytype,
		        gstype:$('#gstype').combobox("getValue"),
				gsid : $('#gsid').textbox("getValue"),
				dataType:$("#dataType").combobox("getValue")
			}
		})
	}
	
	function exportExcel(){
		$("#ff").submit();
	}
	function add(){
		$('#dlg').dialog('open').dialog('center').dialog('setTitle', '创建虚拟充值信息');
	}
	function save(){
		$("#fm").form("submit", {
			url : "../payOrder/savePayOrder",
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("(" + data + ")");
				if (result.code!=200) {
					$.messager.alert("系统提示", result.msg);
					return;
				} else {
					$.messager.alert("系统提示", result.msg);
					$("#fm").form("clear");
					$("#dlg").dialog("close");
					$("#payInfo").datagrid('reload');
				}
			},
			error : function(result) {
				alert(result);
			}
		});
	}
	
	function del(orderId){
		$.messager.confirm("系统提示", "是否删除该虚拟交易?", function(r) {
			if (r) {
				$.post("../payOrder/delPayOrder", {"orderId":orderId}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "删除成功");
						$("#payInfo").datagrid("reload");
					} else {
						$.messager.alert('系统提示', result.msg);
					}
				}, "json");
			}
		});
	}
</script>
</head>
<body>
	<table id="payInfo"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			&nbsp;<a href="javascript:add()" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="btn-add">创建虚拟充值记录</a>
		</div>
		<div>
		 <form id="ff" method="post" action="../payOrderSta/expExcel" target="ifr">
				&nbsp;状态：&nbsp;<select id="status" class="easyui-combobox" name="status">
									<option value="">全部</option>
									<option value="0">生成订单</option>
									<option value="1">未成功</option>
									<option value="2" selected="selected">成功</option>
									<option value="3">取消</option>
								</select>
				&nbsp;用户ID：&nbsp;<input  type="text" name="srcuid" id="srcuid" size="10"  class="easyui-numberbox" data-options="precision:0"/>
				&nbsp;充值方式：&nbsp;<select id="paytype" class="easyui-combobox" name="paytype">
									<option value="">全部</option>
									<option value="unpay_weixin">UNPay微信支付</option>
									<option value="wechat_h5">原生H5微信支付</option>
									<option value="wechat_native_reapal">融宝原生微信支付</option>
									<option value="alipay">支付宝支付</option>
									<option value="apple_innerpay">苹果支付</option>
								</select>
				&nbsp;数据类型：&nbsp;<select id="dataType" class="easyui-combobox" name="dataType">
									<option value="">全部</option>
									<option value="1">真实数据</option>
									<option value="2">虚拟数据</option>
								</select>
				&nbsp;归属：&nbsp;<select id="gstype" name="gstype" class="easyui-combobox">
									<option value="1" selected="selected">星耀公会</option>
									<option value="2">钻石公会</option>
									<option value="3">铂金公会</option>
									<option value="4">黄金公会</option>
									<option value="5">家族助理</option>
								</select>ID:&nbsp;<input id=gsid name="gsid" class="easyui-numberbox"/>
				&nbsp;开始时间：&nbsp;<input class="easyui-datetimebox" style="width:160px;height:24px" name="startTime" id="startTime"/>
				&nbsp;结束时间：&nbsp;<input class="easyui-datetimebox" style="width:160px;height:24px" name="endTime"  id="endTime"/>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:exportExcel()" class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</form>
		</div>
	</div>
		<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellspacing="5px;">
				<tr>
					<td>UID:</td>
					<td><input id="uid" name="uid" class="easyui-numberbox" onClick required="true"  data-options="precision:0" /></td>
				</tr>
				<tr>
					<td>昵称:</td>
					<td><input id="nickname" name="nickname" class="easyui-textbox" onClick required="true" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>金额:</td>
					<td><input id="amount" name="amount" class="easyui-numberbox" onClick required="true" data-options="min:1,precision:2"  /></td>
				</tr>
				<tr>
					<td>充值方式:</td>
					<td>
						<select id="paytype" class="easyui-combobox" name="paytype" required="true">
							<option value="unpay_weixin" selected="selected">UNPay微信支付</option>
							<option value="wechat_h5">原生H5微信支付</option>
							<option value="wechat_native_reapal">融宝原生微信支付</option>
							<option value="alipay">支付宝支付</option>
							<option value="apple_innerpay">苹果支付</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>日期:</td>
					<td><input id="date" name="date" class="easyui-datebox" onClick required="true" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:save()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	</div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>