<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>主播管理列表</title>
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
		dataGrid = $("#anchorInfoList").datagrid({
			url : '../../anchoIl/getanchorInfoList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '主播管理列表',
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
				title : '主播ID',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					return data;
					//return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
				}
			}, {
				field : 'nickname',
				title : '主播昵称',
				align : 'center',
				width : 30,
				sortable : false
			},  {
				field : 'auditAtStr',
				title : '开通直播权限时间',
				align : 'center',
				width : 80,
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
			},{
				field : 'strategicPartnerName',
				title : '所属星耀公会',
				align : 'center',
				width : 80,
				sortable : false,
				formatter:function(data,row){
					return data+'<br>'+row.strategicPartnerContactsName+","+row.strategicPartnerContactsPhone
				}
			},{field: 'liveStatus',
				title: '直播状态', 
				align: 'center', 
				width:30,
				sortable: false,
				formatter : function(data) {
					if (data == 0) {
						return "未开播";
					} else {
						return "<font color='red'>直播中</font>";
					}
			}},{field: 'identity',
				title: '主播状态', 
				align: 'center', 
				width:30,
				sortable: false,
				formatter : function(data) {
					if (data == 1) {
						return "正常";
					} else if(data == 3) {
						return "<font color='blue'>封号</font>";
					}else{
						return "<font color='blue'>禁播</font>";
					}
			}},{field: 'opt',
				title: '操作', 
				align: 'center', 
				width:50,
				sortable: false,
				formatter : function(data,row) {
					var _html = [];
					if(row.identity==1){
						var href = "javascript:jinbo("+ row.uid + ");";
						_html.push('<span><a href='+ href+ '><font color="blue">禁播</font></a></span>');
					}else{
						var href1 = "javascript:unbanAnchor("+ row.uid + ");";
						_html.push('<span><a href='+ href1+ '><font color="blue">解禁</font></a></span>');
					}
					if(row.liveStatus==1){
						var href2 = "javascript:guanbi("+ row.uid + ");";
						_html.push('&nbsp;&nbsp;&nbsp;&nbsp;<span><a href='+ href2+ '><font color="blue">关闭</font></a></span>');
					}
				  return _html.join("");
			}}] ]
		})
	});
	
	function search() {
		$('#anchorInfoList').datagrid('load', {
			uid : $('#uid').textbox("getValue"),
			liveStatus:$('#liveStatus').combobox("getValue"),
			gStatus:$('#gStatus').combobox("getValue"),
			gsid : $('#gsid').textbox("getValue")
		});
	}
	
	function memberDetail(uid){
		 var iframe = "<iframe src='../user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg').html(iframe);
		 $('#dlg').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}
	
 	//封号
	function fenghao(uid){
		$.messager.confirm("系统提示","确定要封号吗？",function(r){
			if(r){
				$.get("../../monitor?method=block&uid="+uid+"&status=0",function(data){
					if(data.code == 200){
						$.messager.alert("系统提示","封号成功！");
						//$("#item_"+uid).remove();
						$("#anchorInfoList").datagrid('reload');
						return;
					}else{
						$.messager.alert("系统提示","封号失败！");
						return;
					}
				},'json')
			}
		})
	} 

	//禁播
	function jinbo(uid){
		$.messager.confirm("系统提示","确定要禁播吗？",function(r){
			if(r){
				$.get("../../monitor?method=ban&uid="+uid+"&status=2",function(data){
					console.log("data="+data.code);
					if(data.code == 200){
						$.messager.alert("系统提示","禁播成功！");
						//$("#item_"+uid).remove();
						$("#anchorInfoList").datagrid('reload');
						return;
					}else{
						$.messager.alert("系统提示","禁播失败！");
						return;
					}
				},'json')
			}
		})
	}
	
	//解禁播
	function unbanAnchor(uid) {
		url = "../../monitor";
		$.messager.confirm("系统提示", "您确认要解禁播这<font color=red>"
				+ uid + "</font>个房间吗？", function(r) {
			if (r) {
				$.post(url, {
					uid : uid,
					method:"ban",
					status:"1"
				}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "您已成功解禁播<font color=red>"
								+ uid + "</font>个房间！");
						$("#anchorInfoList").datagrid("reload");
					} else {
						$.messager.alert('系统提示', result.msg);
					}
				}, "json");
			}
		});
	}

	//关闭
	function guanbi(uid){

		$.messager.confirm("系统提示","确定要关闭吗？",function(r){
			if(r){
				$.get("../../monitor?method=close&uid="+uid+"&status=2",function(data){
					if(data.code == 200){
						$.messager.alert("系统提示","关播成功！");
						//$("#item_"+uid).remove();
						$("#anchorInfoList").datagrid('reload');
						return;
					}else{
						$.messager.alert("系统提示","关播失败！"+data.code);
						return;
					}
				},'json')
			}
		})
	};
	function exportExcel(){
		$("#ff").submit();
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="anchorInfoList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			 <form id="ff" method="post" action="../../anchoIl/expExcel" target="ifr">
				用户UID:&nbsp;<input id=uid name="uid" class="easyui-numberbox"/>
				&nbsp;
				归属类型：&nbsp;
				<select id="gStatus" name="gStatus" class="easyui-combobox" >
					<option value="1" selected="selected">星耀公会</option>
					<option value="2">钻石公会</option>
					<option value="3">铂金公会</option>
					<option value="4">黄金公会</option>
					<option value="5">家族助理</option>
				</select> 
				归属UID:&nbsp;<input id=gsid name="gsid" class="easyui-numberbox"/>
				&nbsp;
				开播状态：&nbsp;
				<select id="liveStatus" name="liveStatus" class="easyui-combobox" >
					<option value="-1">--全部--</option>
					<option value="0">未开播</option>
					<option value="1">开播</option>
				</select>
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
				<a href="javascript:exportExcel()" class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</form>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>