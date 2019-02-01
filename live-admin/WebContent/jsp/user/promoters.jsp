<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>用户-铂金公会</title>
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
			url : '../../promoters/getPromotersList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '铂金公会列表',
			queryParams: {
				strategicPartnerId:'${param.strategicPartnerId}',
				extensionCenterId:'${param.extensionCenterId}'
			},
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
				{field : 'id',title : '铂金公会ID',align : 'center',width : 50,sortable : false},
				{field : 'name',title : '铂金公会名称',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data + "<br/>" +
					'黄金公会<a href="javascript:void(0);" onclick="openAgentUser(\''+data+'\','+row.id+');">'+row.agentUserNum+'</a>个，'+
					'家族助理<a href="javascript:void(0);" onclick="openSalesman(\''+data+'\','+row.id+');">'+row.salesmanNum+'</a>个，'+
					'会员<a href="javascript:void(0);" onclick="openAnchor(\''+data+'\','+row.id+');">'+row.anchorNum+'</a>个';
				}},
				{field : 'contacts',title : '联系人',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data+"<br/>"+row.contactsPhone;
				}}
				//权限控制
				<html:auth action="promoters:list-extensionCenterName">
				,{field : 'extensionCenterName',title : '上级钻石公会',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data + "<br/>" + row.extensionCenterContacts +"," + row.extensionCenterContactsPhone ;
				}}
				</html:auth>
				//权限控制
				<html:auth action="promoters:list-strategicPartnerName">
				,{field : 'strategicPartnerName',title : '上级星耀公会',align : 'center',width : 110,sortable : false,formatter:function(data,row){
					return data+"<br/>"+row.strategicPartnerContacts+row.strategicPartnerContactsPhone;
				}}
				</html:auth>
				,{field : '_sta',title : '统计',align : 'center',width : 30,sortable : false,formatter:function(data,row){
					return '<a href="javascript:openTongji(\''+row.name+'\','+row.id+');">统计</a>';
				}}
			]]
		});
		
	});
	
	//统计界面
	function openTongji(name,promotersId){
		parent.openTab("【"+name+"】的统计数据",'jsp/sta/userTransactionHisSta.jsp?promotersId='+promotersId);
	}		
	//打开黄金公会页面
	function openAgentUser(name,promotersId){
		parent.openTab("【"+name+"】的黄金公会",'jsp/user/agentUser.jsp?promotersId='+promotersId);
	}
	//打开家族助理页面
	function openSalesman(name,promotersId){
		parent.openTab("【"+name+"】的家族助理",'jsp/user/salesman.jsp?promotersId='+promotersId);
	}
	//打开主播页面
	function openAnchor(name,promotersId){
		parent.openTab("【"+name+"】的会员",'jsp/user/memberList.jsp?promotersId='+promotersId);
	}
	
	function addEvent(){
		<html:auth action="promoters:editParentId" show="false">
		$("#editParentIdTr").hide();
		</html:auth>
		$("#passwordTip").hide();
		$("#password").textbox({ required:true });
		$.post("../../promoters/getExtensionCenter", {}, function(result) {
			var data = eval("(" + result + ")");
	        $('#dlg').dialog('open').dialog('center').dialog('setTitle','添加铂金公会');
	        $("#fm").form("clear");
	        var extensionCenterId = "${param.extensionCenterId}";
	        if(data.code == 200){
	        	if(!extensionCenterId){
	        		extensionCenterId = data.data;
	        	}
			}
	        $("#extensionCenter").textbox("setValue",extensionCenterId);
	        url = '../../promoters/savePromoters';
		});
	}

	function editEvent(){
		<html:auth action="promoters:editParentId" show="false">
		$("#editParentIdTr").hide();
		</html:auth>
        var selectedRows = $("#gridList").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		$("#passwordTip").show();
		$("#password").textbox({ required:false });
		var row = selectedRows[0];
		$('#dlg').dialog('open').dialog('center').dialog('setTitle','添加铂金公会');
		$("#fm").form("clear");
		row.oldContactsPhone = row.contactsPhone;
		$("#fm").form("load", row);
        url = "../../promoters/updatePromoters?id="+row.id;
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
				var url = '../../promoters/delPromoters';
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
		//钻石公会
		var extensionCenterId = '${param.extensionCenterId}';
		if(!extensionCenterId){
			//权限控制
			<html:auth action="promoters:list-extensionCenterName">
			extensionCenterId = $('#s_extensionCenterId').textbox("getValue");
			</html:auth>
		}
	
		//战略伙伴
		var strategicPartnerId = '${param.strategicPartnerId}';
		if(!strategicPartnerId){
			//权限控制
			<html:auth action="promoters:list-strategicPartnerName">
			strategicPartnerId = $('#s_strategicPartnerId').textbox("getValue");
			</html:auth>
		}
		
		$('#gridList').datagrid('load', {
			id : $('#s_id').textbox("getValue"),
			name : $('#s_name').textbox("getValue"),
			contactsPhone : $('#s_contactsPhone').textbox("getValue"),
			extensionCenterId:extensionCenterId,
			strategicPartnerId:strategicPartnerId,
			startDate : $("#s_startdate").textbox('getValue'),
			endDate : $("#s_enddate").textbox('getValue'),
			order : $("#s_order").combobox('getValue')
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
				<html:auth action="promoters:add">
					<a href="javascript:addEvent()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
				</html:auth>
				<html:auth action="promoters:edit">
					<a href="javascript:editEvent()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
				</html:auth>
				<html:auth action="promoters:del">
					<a href="javascript:deleteEvent()" class="easyui-linkbutton invalid" iconCls="icon-remove" plain="true">删除</a>
				</html:auth>
			</div>
			<div>
				铂金公会ID:&nbsp;<input id="s_id" name="s_id" class="easyui-textbox" />
				铂金公会名称:&nbsp;<input id="s_name" name="s_name" class="easyui-textbox" />
				铂金公会手机号:&nbsp;<input id="s_contactsPhone" name="s_contactsPhone" class="easyui-textbox" />
				<html:auth action="promoters:list-extensionCenterName">
				钻石公会ID:&nbsp;<input id="s_extensionCenterId" name="s_extensionCenterId" class="easyui-textbox" />
				</html:auth>
				<html:auth action="promoters:list-strategicPartnerName">
				星耀公会ID:&nbsp;<input id="s_strategicPartnerId" name="s_strategicPartnerId" class="easyui-textbox" />
				</html:auth>
				&nbsp;&nbsp;开始时间：&nbsp;<input class="easyui-datetimebox" editable="false" style="width:160px;height:24px" name="s_startdate" id="s_startdate" size="10"/>
				&nbsp;&nbsp;结束时间：&nbsp;<input class="easyui-datetimebox" editable="false" style="width:160px;height:24px" name="s_enddate"  id="s_enddate" size="10"/>
				&nbsp;&nbsp;排序：&nbsp;
				<select id="s_order" class="easyui-combobox" editable="false" name="s_order" panelHeight="auto" style="width: 155px">
					<option value="" selected="">默认</option>
					<option value="anchorNum,desc">会员最多</option>
					<option value="salesmanNum,desc">家族助理最多</option>
					<option value="agentUserNum,desc">黄金公会最多</option>
				</select>				
				&nbsp;&nbsp;<a href="javascript:searchEvent()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>铂金公会名称:</td>
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
					<tr id="editParentIdTr" <html:auth action="promoters:editParentId" show="false"> style="display:hidden;" </html:auth> >
						<td>钻石公会ID</td>
						<td>
							<input type="text" id="extensionCenter" name="extensionCenter" class="easyui-numberbox" required="true" min="1" max="100000000000" data-options="validType:'length[0,20]'" />
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