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
<title>礼物促销活动列表</title>
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
		
		dataGrid = $("#promotlist").datagrid(
				{
					url : '../../operat/promotlist',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '促销活动列表',
					pagination : true,
					idField : "endtime",
					sortName : 'endtime',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					hideColumn : 'id',
					pageSize : 30,
					pageList : [ 30 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'id',
								title : '序号',
								align : 'center',
								width : 50,
								sortable : false
							},{
								field : 'promotionName',
								title : '促销名称',
								align : 'center',
								width : 250,
								sortable : false
							},{
								field : 'gid',
								title : '礼物Gid',
								align : 'center',
								width : 80,
								sortable : false
							},
							{
								field : 'gname',
								title : '礼物名称',
								width : 200,
								align : 'center',
								sortable : false
							},
							{
								field : 'gprice',
								title : '原价',
								width : 80,
								align : 'center',
								sortable : false
							},
							{
								field : 'discount',
								title : '折扣率[%]',
								width : 80,
								align : 'center',
								sortable : false
							},
							{
								field : 'disPrice',
								title : '折扣价',
								width : 80,
								align : 'center',
								sortable : false
							},
							{
								field : 'isvalid',
								title : '是否有效',
								width : 80,
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data == "1" ? "有效" : "无效";
								}
							},
							{
								field : 'starttime',
								title : '开始时间',
								width : 200,
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'endtime',
								title : '结束时间',
								width : 200,
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'adminName',
								title : '操作人',
								width : 150,
								align : 'center',
								sortable : false
							}
							] ]
				})
				
		$("#gid").combobox({
			valueField: 'gid',
		    textField: 'gname',
			url:'../../operat/getMallGift',
			editable:false,
			onSelect:function(){
				
				var gname = $('#gid').combobox('getText');
				console.log("gname:"+gname);
				
				if(gname != "" && gname.indexOf(",") >= 0){
					var array = gname.split(",");
					$("#gprice").val(array[1]);
					var discount = $("#discount").val();
					if(discount != ""){
						$("#disPrice").val(Math.floor(array[1]*discount/100));
					}
				} 
	         }
		}); 
		
		$("#discount").numberbox({
			onChange:function(){
				var gprice = $("#gprice").val();
				var discount = $("#discount").val();
				$("#disPrice").val(Math.floor(gprice*discount/100));
	         }
		}); 
	})
	
	function openDiscountAddDialog() {
		$("#dlg").form("clear");
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "添加促销");
		url = "../../operat/promotAdd";
	}

	function openDiscountModifyDialog() {
		$("#dlg").form("clear");
		var selectedRows = $("#promotlist").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		row.starttime = myformatter(new Date(row.starttime * 1000));
		row.endtime = myformatter(new Date(row.endtime * 1000));
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改促销");
		$("#fm").form("load", row);
		url = "../../operat/promotEdit";
	}

	function closePromotDialog(){
		$("#dlg").form("clear");
		$("#dlg").dialog("close");
	}
	
	function savePromot() {

		$(".icon-ok").attr("disabled","true"); 
		setTimeout($(".icon-ok:disabled").removeAttr("disabled"),10000);
		
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("("+data+")");
				if (result.errorCode) {
					$.messager.alert("系统提示", "保存成功");
					$("#dlg").dialog("close");
					$("#promotlist").datagrid("reload");
				} else {
					$.messager.alert("系统提示", result.errorMsg);
					return;
				}
			}
		});
	}
	
	function myformatter(date){  
		var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
		var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
		+ (date.getMonth() + 1);
		var hor = date.getHours();
		var min = date.getMinutes();
		var sec = date.getSeconds();
		return date.getFullYear() + '-' + month + '-' + day+" "+hor+":"+min+":"+sec; 
	}
	
</script>
</head>
<body>
	<div>
		<table id="promotlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:openDiscountAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a> 
				<a href="javascript:openDiscountModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
			</div>
		</div>
		
		<div id="dlg" class="easyui-dialog" style="width: 570px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>活动名称：</td>
						<td><input type="text" name="promotionName" id="promotionName" class="easyui-validatebox" required="true" MaxLength="25"/></td>
					</tr>
					<tr>
						<td>商品选择：</td>
						<td><input id=gid name="gid" class="easyui-combobox" required="true" /></td>
					</tr>
					<tr>
						<td>原价：</td>
						<td><input type="text" name="gprice" id="gprice" class="easyui-validatebox" readonly="true"/></td>
					</tr>
					<tr>
						<td>折扣率[%]：</td>
						<td><input type="text" name="discount" id="discount" class="easyui-numberbox" min="80" max="100" required="true"/>[填整数 80~100]</td>
					</tr>
					<tr>
						<td>折扣价：</td>
						<td><input type="text" name="disPrice" id="disPrice" class="easyui-validatebox" readonly="true"/></td>
					</tr>
					<tr>
						<td>有效：</td>
						<td><select id="isvalid" class="easyui-combobox" editable="false" name="isvalid" panelHeight="auto" style="width: 145px">
								<option value="1">有效</option>
								<option value="0">无效</option>
						</select></td>
					</tr>
					<tr>
						<td>开始时间：</td>
						<td><input class="easyui-datetimebox" style="width:180px;height:24px" name="starttime" id="starttime" size="20" /></td>
					</tr>
					<tr>
						<td>结束时间：</td>
						<td><input class="easyui-datetimebox" style="width:180px;height:24px" name="endtime" id="endtime" size="20" /></td>
					</tr>
				</table>
				<input type="hidden" name="id" id="id" />
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:savePromot()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:closePromotDialog()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
