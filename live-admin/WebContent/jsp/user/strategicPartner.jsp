<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>用户-星耀公会</title>
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
		dataGrid = $("#gridList").datagrid({
			url : '../../strategicPartner/getStrategicPartnerList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '星耀公会列表',
			queryParams: {},
			pagination : true,
	        rownumbers: true,
			striped : true,
	        singleSelect: true,
	        fit:true,
	        fitColumns:true,
	        pageSize:20,
	        pageList:[20,50,100],
			toolbar : '#tb',
			columns : [[
				{field : 'id',title : '星耀公会ID',align : 'center',width : 50,sortable : false},
				{field : 'name',title : '星耀公会名称',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data + "<br/>" +
					'钻石公会<a href="javascript:void(0);" onclick="openExtensionCenter(\''+data+'\','+row.id+');">'+row.extensionCenterNum+'</a>个，'+
					'铂金公会<a href="javascript:void(0);" onclick="openPromoters(\''+data+'\','+row.id+');">'+row.promotersNum+'</a>个，'+
					'黄金公会<a href="javascript:void(0);" onclick="openAgentUser(\''+data+'\','+row.id+');">'+row.agentUserNum+'</a>个，'+
					'家族助理<a href="javascript:void(0);" onclick="openSalesman(\''+data+'\','+row.id+');">'+row.salesmanNum+'</a>个，'+
					'会员<a href="javascript:void(0);" onclick="openAnchor(\''+data+'\','+row.id+');">'+row.anchorNum+'</a>个';
				}},
				{field : 'contacts',title : '联系人',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data+"<br/>"+row.contactsPhone;
				}},
				{field : '_sta',title : '统计',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return '<a href="javascript:openTongji(\''+row.name+'\','+row.id+');">统计</a>';
				}}
			]]
		});
		
	});
	
	
	//统计界面
	function openTongji(name,strategicPartnerId){
		parent.openTab("【"+name+"】的统计数据",'jsp/sta/userTransactionHisSta.jsp?strategicPartnerId='+strategicPartnerId);
	}
	
	//打开钻石公会
	function openExtensionCenter(name,strategicPartnerId){
		parent.openTab("【"+name+"】的钻石公会",'jsp/user/extensionCenter.jsp?strategicPartnerId='+strategicPartnerId);
	}
	//打开铂金公会页面
	function openPromoters(name,strategicPartnerId){
		parent.openTab("【"+name+"】的铂金公会",'jsp/user/promoters.jsp?strategicPartnerId='+strategicPartnerId);
	}
	//打开黄金公会页面
	function openAgentUser(name,strategicPartnerId){
		parent.openTab("【"+name+"】的黄金公会",'jsp/user/agentUser.jsp?strategicPartnerId='+strategicPartnerId);
	}
	//打开家族助理页面
	function openSalesman(name,strategicPartnerId){
		parent.openTab("【"+name+"】的家族助理",'jsp/user/salesman.jsp?strategicPartnerId='+strategicPartnerId);
	}
	//打开主播页面
	function openAnchor(name,strategicPartnerId){
		parent.openTab("【"+name+"】的会员",'jsp/user/memberList.jsp?strategicPartnerId='+strategicPartnerId);
	}
	
	function addEvent(){
		$("#passwordTip").hide();
		$("#password").textbox({ required:true });
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加星耀公会');
        $("#fm").form("clear");
        url = '../../strategicPartner/saveStrategicPartner';
	}

	function editEvent(){
        var selectedRows = $("#gridList").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		$("#passwordTip").show();
		$("#password").textbox({ required:false });
		var row = selectedRows[0];
		$('#dlg').dialog('open').dialog('center').dialog('setTitle','添加星耀公会');
		$("#fm").form("clear");
		row.oldContactsPhone = row.contactsPhone;
		$("#fm").form("load", row);
        url = "../../strategicPartner/updateStrategicPartner?id="+row.id;
	}
	
	function deleteEvent() {
		var row = $("#gridList").datagrid('getSelected');
		if (row == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		$.messager.confirm("系统提示","确认删除吗？",function(r){
			if(r){
				var params = {
					"id" : row.id
				};
				var url = '../../strategicPartner/delStrategicPartner';
				$.post(url, params, function(result) {
					var data = eval("(" + result + ")");
					if (data.code == 200) {
						$.messager.alert("系统提示", "删除成功");
					} else {
						$.messager.alert("系统提示", data.msg);
					}
					$("#gridList").datagrid('reload');
				});
			}
		});
	}

	function searchEvent() {
		$('#gridList').datagrid('load', {
			id : $('#searchId').textbox("getValue"),
			name : $('#searchName').textbox("getValue"),
			contactsPhone : $('#contactsPhone').textbox("getValue"),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue'),
			order : $("#order").combobox('getValue')
		});
	}
	
	function saveEvent() {
		$.messager.confirm("系统提示","确认提交吗？",function(r){
			if(r){
				$("#fm").form("submit", {
					url : url,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(data) {
						var result = eval("("+data+")");
						if (result.code == 200) {
							$.messager.alert("系统提示", "保存成功");
							$("#fm").form("clear");
							$("#dlg").dialog("close");
							$("#gridList").datagrid("reload");
						}else {
							$.messager.alert("系统提示", result.msg);
						}
					}
				});
			}
		})
	}
	
</script>
</head>
<body style="margin: 5px;">
		<table id="gridList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<html:auth action="strategicPartner:add">
					<a href="javascript:addEvent()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
				</html:auth>
				<html:auth action="strategicPartner:edit">
					<a href="javascript:editEvent()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
				</html:auth>
				<html:auth action="strategicPartner:del">
					<a href="javascript:deleteEvent()" class="easyui-linkbutton invalid" iconCls="icon-remove" plain="true">删除</a>
				</html:auth>
			</div>
			<div>
				星耀公会ID:&nbsp;<input id="searchId" name="searchId" class="easyui-textbox" />
				星耀公会名称:&nbsp;<input id="searchName" name="searchName" class="easyui-textbox" />
				手机号:&nbsp;<input id="contactsPhone" name="contactsPhone" class="easyui-textbox" />
				&nbsp;&nbsp;开始时间：&nbsp;<input class="easyui-datetimebox" editable="false" style="width:160px;height:24px" name="startdate" id="startdate" size="10"/>
				&nbsp;&nbsp;结束时间：&nbsp;<input class="easyui-datetimebox" editable="false" style="width:160px;height:24px" name="enddate"  id="enddate" size="10"/>
				&nbsp;&nbsp;排序：&nbsp;
				<select id="order" class="easyui-combobox" editable="false" name="order" panelHeight="auto" style="width: 155px">
					<option value="" selected="">默认</option>
					<option value="anchorNum,desc">会员最多</option>
					<option value="salesmanNum,desc">家族助理最多</option>
					<option value="agentUserNum,desc">黄金公会最多</option>
					<option value="promotersNum,desc">铂金公会最多</option>
					<option value="extensionCenterNum,desc">钻石公会最多</option>
				</select>
				&nbsp;&nbsp;<a href="javascript:searchEvent()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>星耀公会名称:</td>
						<td>
							<input type="text" id="name" name="name" class="easyui-textbox" required="true" data-options="validType:'length[2,32]'" />
						</td>
					</tr>
					<tr>
						<td>联系人:</td>
						<td>
							<input type="text" id="contacts" name="contacts" class="easyui-textbox" required="true" data-options="validType:'length[0,20]'" />
						</td>
					</tr>
					<tr>
						<td>联系电话</td>
						<td>
							<input type="hidden" id="oldContactsPhone" name="oldContactsPhone" />
							<input type="text" id="contactsPhone" name="contactsPhone" class="easyui-textbox" required="true" data-options="validType:'phoneNum'" />
						</td>
					</tr>
					<tr>
						<td>登录密码</td>
						<td>
							<input type="text" id="password" name="password" class="easyui-textbox" data-options="validType:'length[0,32]'" />
							<span id="passwordTip">[不修改则不填]</span>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="dlg-buttons">
			<a href="javascript:saveEvent()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
</body>
</html>