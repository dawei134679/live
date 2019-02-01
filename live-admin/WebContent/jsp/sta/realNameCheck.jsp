<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>实名认证统计</title>
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
		dataGrid = $("#realNameList").datagrid({
			url : '../../rCheck/getRealNameList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '实名认证统计',
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
				title : '用户ID',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					return "<a href='javascript:memberDetail("+data+")' >"+data+"</a>";
				}
			}, {
				field : 'realName',
				title : '真实名字',
				align : 'center',
				width : 30,
				sortable : false
			},  {
				field : 'cardID',
				title : '身份证ID',
				align : 'center',
				width : 60,
				sortable : false
			}, {
				field : 'createTime',
				title : '提交审核时间',
				align : 'center',
				width : 70,
				sortable : false,
				formatter:function(data,row){
					if(data==null||data==0){
						return "";
					}
					var date = new Date();  
				    date.setTime(data * 1000);  
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
			},  {
				field : 'passTime',
				title : '审核时间',
				align : 'center',
				width : 70,
				sortable : false,
				formatter:function(data,row){
					if(data==null||data==0){
						return "";
					}
					var date = new Date();  
				    date.setTime(data * 1000);  
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
				title : '星耀公会',
				align : 'center',
				width : 80,
				sortable : false,
				formatter:function(data,row){
					return data+'<br>'+row.strategicPartnerContactsName+","+row.strategicPartnerContactsPhone
				}
			},{field: 'auditStatus',
				title: '状态', 
				align: 'center', 
				width:30,
				sortable: false,
				formatter : function(data) {
            		if(data==3){return '<a href="javascript:getCheckUser();" class="easyui-tooltip" title="查看用戶信息">已通过</a>'}
            		else if(data==1){return '<a href="javascript:getCheckUser();"class="easyui-tooltip" title="查看用戶信息">待审核</a>'}
            		else{return '<a href="javascript:getCheckUser();" class="easyui-tooltip" title="查看用戶信息">驳回</a>'}
				;
			}}] ]
		})
	});

	function getCheckUser(){
		var row = $('#realNameList').datagrid('getSelected');
		var uid = row.uid;
		var iframe = "<iframe src='../CheckInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 800px;height: 400px'></iframe>";
		 $('#dlg1').html(iframe);
		 $('#dlg1').dialog('open').dialog('center').dialog('setTitle',uid+'-详情')
		}
	
	function search() {
		$('#realNameList').datagrid('load', {
			uid : $('#uid').textbox("getValue"),
			realName : $('#realName').textbox("getValue"),
			cardID : $('#cardID').textbox("getValue"),
			auditStatus:$('#aStatus').combobox("getValue"),
			dateStatus:$('#dateStatus').combobox("getValue"),
			gStatus:$('#gStatus').combobox("getValue"),
			gsid : $('#gsid').textbox("getValue"),
			startDate : $("#startdate").textbox('getValue'),
			endDate : $("#enddate").textbox('getValue')
		});
	}
	function memberDetail(uid){
		 var iframe = "<iframe src='../user/memberInfo.jsp?uid="+uid+"' style='border-width: 0px;width: 560px;height: 320px'></iframe>";
		 $('#dlg').html(iframe);
		 $('#dlg').dialog('open').dialog('center').dialog('setTitle',uid+'-详情');
	}
	
	function exportExcel(){
		$("#ff").submit();
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="realNameList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
		    <form id="ff" method="post" action="../../rCheck/expExcel" target="ifr">
				用户UID:&nbsp;<input id="uid" name="uid" class="easyui-numberbox"/>
				&nbsp;
				真实姓名:&nbsp;<input id="realName" name="realName" class="easyui-textbox"/>
				&nbsp;
				身份证ID:&nbsp;<input id="cardID" name="cardID" class="easyui-textbox"/>
				&nbsp;
				归属类型：&nbsp;
				<select id="gStatus" name="gStatus" class="easyui-combobox" >
					<option value="-1">--全部--</option>
					<option value="1">星耀公会</option>
					<option value="2">钻石公会</option>
					<option value="3">铂金公会</option>
					<option value="4">黄金公会</option>
					<option value="5">家族助理</option>
				</select> 
				归属UID:&nbsp;<input id=gsid name="gsid" class="easyui-numberbox"/>
				&nbsp;
				状态：&nbsp;
				<select id="aStatus" name="aStatus" class="easyui-combobox" >
					<option value="-1">--全部--</option>
					<option value="1">待审核</option>
					<option value="2">驳回</option>
					<option value="3">已通过</option>
				</select> &nbsp;&nbsp;&nbsp;
				日期状态：&nbsp;
				<select id="dateStatus" name="dateStatus" class="easyui-combobox" >
					<option value="-1">--全部--</option>
					<option value="1">提交审核时间</option>
					<option value="2">审核时间</option>
				</select> 
				&nbsp;&nbsp;开始时间：<input class="easyui-datebox" style="width:160px;height:24px" name="startdate" id="startdate" size="10"/>
				&nbsp;&nbsp;结束时间：<input class="easyui-datebox" style="width:160px;height:24px" name="enddate"  id="enddate" size="10"/>
				&nbsp;&nbsp;&nbsp;&nbsp; 
				<a href="javascript:search()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
				&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:exportExcel()" class="easyui-linkbutton" iconCls="icon-excel">导出</a>
			</form>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 576px;height: 360px;" closed="true"></div>
	<div id="dlg1" class="easyui-dialog" style="width: 800px;height: 400px;" closed="true"></div>
	<iframe id="ifr" name="ifr" style="display: none;"></iframe>
</body>
</html>