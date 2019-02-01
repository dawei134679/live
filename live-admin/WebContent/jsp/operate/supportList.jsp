<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>扶持用户列表</title>
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
		dataGrid = $("#supportUserList").datagrid({
			url : '../../support/getSupportUserList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '扶持用户列表',
			pagination : true,
			rownumbers : true,
			striped : true,
			singleSelect : true,
			fit : true,
			fitColumns : true,
			pageSize : 20,
			pageList : [ 20, 50, 100 ],
			toolbar : '#tb',
			columns : [ [ {
				field : 'uid',
				title : '扶持用户ID',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
				}
			}, {
				field : 'notes',
				title : '备注',
				align : 'center',
				width : 50,
				sortable : false
				
			},  {
				field : 'status',
				title : '状态',
				align : 'center',
				width : 20,
				sortable : false,
				formatter : function(data) {
					if (data == 1) {
						return "启用";
					} else {
						return "<font color='red'>禁用</font>";
					}
				}
			},{
				field : 'createTime',
				title : '创建时间',
				align : 'center',
				width : 60,
				sortable : false,
				formatter:function(data,row){
					return data ? new Date(data).Format("yyyy-MM-dd HH:mm:ss") : "";
				}
			},{
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
				width : 80,
				sortable : false,
				formatter:function(data,row){
					return data+'<br>'+row.agentUserContactsName+","+row.agentUserContactsPhone
				}
			}, {
				field : 'promotersName',
				title : '所属铂金公会',
				align : 'center',
				width : 80,
				sortable : false,
				formatter:function(data,row){
					return data+'<br>'+row.promotersContactsName+","+row.promotersContactsPhone
				}
			}, {
				field : 'extensionCenterName',
				title : '所属钻石公会',
				align : 'center',
				width : 80,
				sortable : false,
				formatter:function(data,row){
					return data+'<br>'+row.extensionCenterContactsName+","+row.extensionCenterContactsPhone
				}
			},
			{field : 'strategicPartnerName',title : '所属星耀公会',align : 'center',width : 80,sortable : false,formatter:function(data,row){
				return data+"<br/>"+row.strategicPartnerContactsName+","+row.strategicPartnerContactsPhone;
			}}
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
	});

	function addSuserDialog() {
		url = "../../support/saveSupportUser";
		$('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增');
		$("#fm").form("clear");
	}

	function updateSuserDialog() {
		$("#fm").form("clear");
		url = "../../support/updateSupportUser";
		var rowSel = $("#supportUserList").datagrid('getSelected');
		if (rowSel == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改");
		$("#fm").form("load", rowSel);
	}

	function searchSupportUser() {
		$('#supportUserList').datagrid('load', {
			uid : $('#s_uid').textbox("getValue"),
			status : $('#s_status').combobox("getValue"),
			startRegisterTime : $("#s_startRegisterTime").datebox('getValue'),
			endRegisterTime : $("#s_endRegisterTime").datebox('getValue'),
			strategicPartnerId:$("#s_strategicPartnerId").textbox('getValue'),
			extensionCenterId:$("#s_extensionCenterId").textbox('getValue'),
			promotersId:$("#s_promotersId").textbox('getValue'),
			agentUserId:$("#s_agentUserId").textbox('getValue'),
			salesmanId:$("#s_salesmanId").textbox('getValue')
		});
	}

	function saveSuser() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("(" + data + ")");
				if (!result.result) {
					$.messager.alert("系统提示", result.msg);
					return;
				} else {
					$.messager.alert("系统提示", result.msg);
					$("#fm").form("clear");
					$("#dlg").dialog("close");
					$("#supportUserList").datagrid('reload');
				}
			},
			error : function(result) {
				alert(result);
			}
		});
	}

	
	function doValid() {
		var row = $("#supportUserList").datagrid('getSelected');
		if (row == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		var status = row.status;
		var parmas;
		if (status == 1) {
			params = {
				"uid" : row.uid,
				"id" : row.id,
				"status" : 0
			};
		}
		if (status == 0) {
			params = {
				"uid" : row.uid,
				"id" : row.id,
				"status" : 1
			};
		}
		var url = '../../support/doValid';
		$.post(url, params, function(result) {
			var data = eval("(" + result + ")");
			if (data.row == 1) {
				$.messager.alert("系统提示", "修改成功");
			} else {
				$.messager.alert("系统提示", "修改失败");
			}
			$("#supportUserList").datagrid('reload');
		})
	}
	
	function memberDetail(uid){
		 var iframe = "<iframe src='../user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg1').html(iframe);
		 $('#dlg1').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}

	//导出数据
	function exportData(){
		$("#ff").submit();
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="supportUserList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<a href="javascript:addSuserDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" id="btn-save">新增</a><!--  <a
				href="javascript:updateSuserDialog()" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" id="btn-update">修改</a> -->
				<a href="javascript:doValid()" class="easyui-linkbutton invalid"
				iconCls="icon-remove" plain="true">启用/禁用</a>
		</div>
		<div>
			<form id="ff" method="post" action="../../support/expExcel" target="ifr">
			用户UID:&nbsp;<input id="s_uid" name="uid" class="easyui-numberbox" style="width:100px"/>
			家族助理ID:&nbsp;<input id="s_salesmanId" name="salesmanId" class="easyui-textbox" style="width:100px"/>
			黄金公会ID:&nbsp;<input id="s_agentUserId" name="agentUserId" class="easyui-textbox" style="width:100px"/>
			铂金公会ID:&nbsp;<input id="s_promotersId" name="promotersId" class="easyui-textbox" style="width:100px"/>
			钻石公会ID:&nbsp;<input id="s_extensionCenterId" name="extensionCenterId" class="easyui-textbox" style="width:100px"/>
			星耀公会ID:&nbsp;<input id="s_strategicPartnerId" name="strategicPartnerId" class="easyui-textbox" style="width:100px"/>
			开始时间：&nbsp;&nbsp;<input class="easyui-datebox" style="width:100px;height:24px" name="startRegisterTime" id="s_startRegisterTime" size="10" />
			结束时间：&nbsp;&nbsp;<input class="easyui-datebox" style="width:100px;height:24px" name="endRegisterTime"  id="s_endRegisterTime" size="10" />
			状态：&nbsp;<select id="s_status" class="easyui-combobox" editable="false" name="status" panelHeight="auto" style="width:100px">
				<option value="-1">--全部--</option>
				<option value="1">启用</option>
				<option value="0">禁用</option>
			</select>
				&nbsp;&nbsp;<a href="javascript:searchSupportUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
				&nbsp;&nbsp;<a href="javascript:exportData()"class="easyui-linkbutton" iconCls="icon-excel" plain="true">导出</a>
			</form>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog"
		style="width: 570px; height: 450px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellspacing="5px;">
				<tr>
					<td>扶持用户ID:</td>
					<td>
					<input id="id" name="id" type="hidden"/> 
					<input id="uid" name="uid" class="easyui-numberbox" max="100000000" required="true"/></td>
				</tr>
				<tr>
					<td>用户昵称:</td>
					<td>
						<input id="nickname" name="nickname" class="easyui-textbox"  data-options="readonly:true"/>
					</td>
				</tr>
				<tr>
						<td>备注:</td>
						<td>
							<input class="easyui-textbox" name="notes" id="notes"  data-options="disabled:false"  data-options="multiline:true" style="height:100px;width:200px" required="true" missingMessage="备注必填"/>
						</td>
				</tr>
				<!-- <tr>
					<td>状态:</td>
					<td><select id="status" name="status" class="easyui-combobox" onClick required="true" missingMessage="状态必填"
						style="width: 170px;">
							<option value="1">启动</option>
							<option value="0">禁用</option>
					</select></td>
				</tr> -->
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveSuser()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a>
	</div>
	<div id="dlg1" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>