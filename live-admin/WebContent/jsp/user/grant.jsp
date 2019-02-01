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
<title>金币添加列表</title>
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
		dataGrid = $("#grantlist").datagrid(
				{
					url : '../../anchor/getGrant',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '金币添加列表',
					queryParams: {
						uid : $('#searchUid').textbox("getValue"),
						/* operate_uid : $('#actUid').combobox("getValue") */
					},
					pagination : true,
			        rownumbers: true,
					striped : true,
			        singleSelect: true,
			        fit:true,
			        nowrap:false,
			        fitColumns:true,
			        pageSize:20,
			        pageList:[20,50,100],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'uid',
								title : '添加对象UID',
								align : 'center',
								width : 100,
								sortable : false,
								formatter : function(data) {
									return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
								}
							},
							{
								field : 'oldzhutou',
								title : '加前金币数',
								align : 'center',
								width : 90,
								sortable : false
							},
							{
								field : 'oldcredit',
								title : '加前声援值',
								align : 'center',
								width : 90,
								sortable : false
							},
							{
								field : 'zhutou',
								title : '新加金币数',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'credit',
								title : '新加声援值',
								align : 'center',
								width : 60,
								sortable : false
							},
							{
								field : 'descrip',
								title : '备注',
								align : 'center',
								width : 150,
								sortable : false
							},
							{
								field : 'username',
								title : '操作者',
								align : 'center',
								width : 100,
								sortable : false
							}, {
								field : 'salesmanId',
								title : '所属家族助理',
								align : 'center',
								width : 80,
								sortable : false,
								formatter:function(data,row){
									return row.salesmanName+'<br>'+row.salesmanContactsPhone
								}
							}, {
								field : 'agentUserName',
								title : '所属黄金公会',
								align : 'center',
								width : 100,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.agentUserContactsName+","+row.agentUserContactsPhone
								}
							}, {
								field : 'promotersName',
								title : '所属铂金公会',
								align : 'center',
								width : 100,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.promotersContactsName+","+row.promotersContactsPhone
								}
							}, {
								field : 'extensionCenterName',
								title : '所属钻石公会',
								align : 'center',
								width : 100,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.extensionCenterContactsName+","+row.extensionCenterContactsPhone
								}
							},{
								field : 'strategicPartnerName',
								title : '星耀公会',
								align : 'center',
								width : 100,
								sortable : false,
								formatter:function(data,row){
									return data+'<br>'+row.strategicPartnerContactsName+","+row.strategicPartnerContactsPhone
								}
							},
							{
								field : 'addtime',
								title : '添加时间',
								align : 'center',
								width : 100,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							}
							] ]
				})
				
				$("#uid").textbox({
					onChange:function(data){
						var uid = $.trim(data);
						if(uid == ""){
							return;
						}
						$.ajax({
				             type: "post",
				             dataType: "json",
				             url: "../../anchor/getNickName",
				             data: { uid:uid},
				             dataType: 'json', 
				             success: function(data) {
				             	if(data.success == 200){
				             		$("#nickname").textbox("setValue",data.nickname);
				             	}else{
									$.messager.alert("系统提示", data.msg);
				             	}
				             }
				       	});
					}
				});
	})
	
	function addGrantDialog(){
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','设置扶持号');
        $("#fm").form("clear");
        url = '../../anchor/addGrant';
	}
	
	
	function searchGrant() {
		$('#grantlist').datagrid('load', {
			uid : $('#searchUid').textbox("getValue"),
			/* operate_uid : $('#actUid').combobox("getValue"), */
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue'),
			gStatus:$('#gStatus').combobox("getValue"),
			gsid : $('#gsid').textbox("getValue")
		});
	}
	
	function saveGrant() {

		$.messager.confirm("系统提示","确认添加?",function(r){
			if(r){
				$("#fm").form("submit", {
					url : url,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(data) {
						var result = eval("("+data+")");
						if (result.errMsg) {
							$.messager.alert("系统提示", result.errMsg);
						}else {
							$.messager.alert("系统提示", "设置成功");
							$("#fm").form("clear");
							$("#dlg").dialog("close");
							$("#grantlist").datagrid("reload");
						}
					}
				});
			}
		})
	}
	
	function memberDetail(uid){
		 var iframe = "<iframe src='memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg1').html(iframe);
		 $('#dlg1').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}
	
	function exportExcel(){
		$("#ff").submit();
	}
</script>
</head>
<body style="margin: 5px;">
		<table id="grantlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:addGrantDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>[<font color="red">异常订单补金币 不要在此添加</font>]
			</div>
			
		<div>
			<form id="ff" method="post" action="../../anchor/expExcel" target="ifr">
				用户UID:&nbsp;<input id=searchUid name="searchUid" class="easyui-numberbox" min="1000000" max="100000000" />
				&nbsp;&nbsp;<!-- 操作用户:&nbsp;<input id="actUid" name="actUid" class="easyui-combobox"
								data-options="valueField: 'uid',
	    									textField: 'username',
	    									url: '../../adminoperat/forSelect'"/> -->
	    		归属类型：&nbsp;
				<select id="gStatus" name="gStatus" class="easyui-combobox" >
					<option value="">--全部--</option>
					<option value="1">星耀公会</option>
					<option value="2">钻石公会</option>
					<option value="3">铂金公会</option>
					<option value="4">黄金公会</option>
					<option value="5">家族助理</option>
				</select>	
				归属UID:&nbsp;<input id=gsid name="gsid" class="easyui-numberbox"/>
				&nbsp;&nbsp;开始时间：<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10"/>
				&nbsp;&nbsp;结束时间：<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10"/>
				&nbsp;&nbsp;<a href="javascript:searchGrant()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:exportExcel()" class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</form>
		</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>用户UID:</td>
						<td>
							<input id="uid" name="uid" class="easyui-numberbox" max="100000000" required="true"/>
						</td>
					</tr>
					<tr>
						<td>用户昵称:</td>
						<td>
							<input id="nickname" name="nickname" class="easyui-textbox"  data-options="readonly:true"/>
						</td>
					</tr>
					<tr>
						<td>添加金币数:</td>
						<td>
							<input class="easyui-numberbox" name="zhutou" id="zhutou"  data-options="disabled:false" required="true" value="0"/>[负数 则减，正数 则加]
						</td>
					</tr>
					<tr>
						<td>添加声援值:</td>
						<td>
							<input class="easyui-numberbox" name="credit" id="credit"  data-options="disabled:false" required="true"  value="0"/>[负数 则减，正数 则加]
						</td>
					</tr>
					<tr>
						<td>备注:</td>
						<td>
							<input class="easyui-textbox" name="descrip" id="descrip"  data-options="disabled:false"  data-options="multiline:true" style="height:100px;width:200px" required="true" missingMessage="备注必填"/>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveGrant()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
		<div id="dlg1" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
		<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>
