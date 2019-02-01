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
<title>主播列表</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {
		
		dataGrid = $("#anchorList").datagrid(
				{
					url : '../unionAnchor/getAnchorList',

					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '公会主播列表',
					pagination : true,
					striped : true,
					queryParams:{
						unionid : $("#searchUnionid").combobox('getValue'),
						anchorid : $("#searchAnchorid").textbox('getValue')
					},
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'anchorid',
								title : '主播UID',
								align : 'center',
								sortable : false
							},
							{
								field : 'nickname',
								title : '主播昵称',
								align : 'center',
								sortable : false
							},
							{
								field : 'anchorLevel',
								title : '主播等级',
								align : 'center',
								sortable : false
							},
							{
								field : 'unionid',
								title : '公会ID',
								align : 'center',
								sortable : false
							},
							{
								field : 'unionname',
								title : '所属公会',
								align : 'center',
								sortable : false
							},
							{
								field : 'adminname',
								title : '归属人',
								align : 'center',
								sortable : false
							},
							{
								field : 'recommend',
								title : '房间级别',
								align : 'center',
								sortable : false,
								formatter : function(data){
									var view = "普通";
									if(data == 1){
										view = "最新";
									}else if(data == 2){
										view = "热门";
									}
									return view;
								}
							},
							{
								field : 'credit',
								title : '剩余声援',
								align : 'center',
								sortable : false
							},
							{
								field : 'creditTotal',
								title : '累计声援',
								align : 'center',
								sortable : false
							},
							{
								field : 'phone',
								title : '绑定手机',
								align : 'center',
								sortable : false
							},
							{
								field : 'registtime',
								title : '注册时间',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'opentime',
								title : '最后开播',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'rate',
								title : '分成比率',
								align : 'center',
								sortable : true
							},
							{
								field : 'salary',
								title : '定薪',
								align : 'center',
								sortable : false
							},
							{
								field : 'remarks',
								title : '考核备注',
								align : 'center',
								sortable : false
							},
							{
								field : 'createtime',
								title : '添加时间',
								align : 'center',
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'identity',
								title : '主播状态',
								align : 'center',
								sortable : false,
								formatter : function(data){
									var view = "正常";
									if(data == 1){
										view = "封号";
									}else if(data == 2){
										view = "禁播";
									}
									return view;
								}
							}
							] ]
				})
	})
	
	function saveAnchor(){
		$('#fmAnchor').form('submit',{
            url: url,
            onSubmit: function(){
                return $(this).form('validate');
            },
            success: function(result){
                var result = eval('('+result+')');
                if (result.errorMsg){
                    $.messager.show({
                        title: 'Error',
                        msg: result.errorMsg
                    });
                } else {
                	$('#fmAnchor').form('clear');
                    $('#dlg').dialog('close');        
                    $('#anchorList').datagrid('reload');
                }
            }
        });
	}
	
	function queryAnchor(){
		$("#anchorList").datagrid('reload', {
			unionid : $("#searchUnionid").combobox('getValue'),
			anchorid : $("#searchAnchorid").textbox('getValue')
		})
	}
	
	function closeDlg(){
		$('#fmAnchor').form('clear');
		$('#dlg').dialog('close');
	}
	
	function editAnchor(){
		var selectedRows = $("#anchorList").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}

   		var row = selectedRows[0];
        $('#dlg').dialog('open').dialog('center').dialog('setTitle','编辑公会主播');
        $('#fmAnchor').form('clear');
        $('#fmAnchor').form('load',row);
        url = '../unionAnchor/editAnchor';
	}
</script>
</head>
<body>
	<div>
		<table id="anchorList"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				<a href="javascript:editAnchor()"
					class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改主播</a> 
			</div>
			<div>
				选择公会:<input id=searchUnionid name="searchUnionid" class="easyui-combobox" required="true"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../unionAnchor/getUnionNameList'"
    						/>
				<label>主播id</label>
				<input class="easyui-textbox" style="width: 150px" id="searchAnchorid" name="searchAnchorid">
				<a href="javascript:queryAnchor();" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
			</div>
		</div>
		<div id="dlg" class="easyui-dialog"
			style="width: 500px; height: 400px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons-add-anchor">
			<form id="fmAnchor" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>主播uid：</td>
						<td><input type="text" name="anchorid" id="anchorid" class="easyui-validatebox" required="true" data-options="readonly:true" /></td>
					</tr>
					<tr>
						<td>公会ID:</td>
						<td>
							<input type="text" name="unionid" id="unionid" class="easyui-validatebox" required="true" data-options="readonly:true" />
						</td>
					</tr>
					<tr>
						<td>公会名称:</td>
						<td>
							<input type="text" name="unionname" id="unionname" class="easyui-validatebox" required="true" data-options="readonly:true" />
						</td>
					</tr>
					<tr>
						<td>分成比率(0.01~1)：</td>
						<td><input type="text" name="rate" id="rate" class="easyui-numberbox" min="0.01" max="1" precision="2"/></td>
					</tr>
					<tr>
						<td>定薪(0~10000)：</td>
						<td><input type="text" name="salary" id="salary" class="easyui-numberbox" min="0" max="10000"/></td>
					</tr>
					<tr>
						<td>备注说明：</td>
						<td><input type="text" name="remarks" id="remarks" class="easyui-textbox" data-options="multiline:true" style="width:200px;height:100px"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div id="dlg-buttons-add-anchor">
			<a href="javascript:saveAnchor()" class="easyui-linkbutton" iconCls="icon-ok">保存</a> <a href="javascript:closeDlg()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
	</div>
</body>
</html>
