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
<title>用户守护列表</title>
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
		dataGrid = $("#guardlist").datagrid({
					url : '../../gift/getUsedGuardList',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '用户守护列表',
					queryParams: {
						uid : $('#uid').textbox("getValue"),
						anchorId : $("#anchorId").textbox('getValue'),
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
					columns : [ [
							{
								field : 'uid',
								title : '用户UID',
								align : 'center',
								width : 85,
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户昵称',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'gid',
								title : '礼物ID',
								align : 'center',
								width : 50,
								sortable : false
							},
							{
								field : 'gname',
								title : '礼物名称',
								align : 'center',
								width : 100,
								sortable : false
							},{
								field : 'anchoruid',
								title : '主播UID',
								align : 'center',
								width : 85,
								sortable : false
							},
							{
								field : 'anchorname',
								title : '主播昵称',
								align : 'center',
								width : 100,
								sortable : false
							},
							{
								field : 'level',
								title : '等级',
								align : 'center',
								width : 30,
								sortable : false
							},
							{
								field : 'isdel',
								title : '有效',
								align : 'center',
								width : 40,
								sortable : false,
								formatter:function(data){
									if(data == 0){
										return "是";
									}else{
										return "<font color='red'>否</font>";
									}
								}
							},
							{
								field : 'starttime',
								title : '开始时间',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'endtime',
								title : '结束时间',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							},
							{
								field : 'cushiontime',
								title : '缓存期',
								align : 'center',
								width : 130,
								sortable : false,
								formatter:function(data){
									return data ? new Date(data * 1000)
									.Format("yyyy-MM-dd HH:mm:ss") : "";
								}
							}
							] ]
				})
	})
	
	function searchGuard() {
		$('#guardlist').datagrid('load', {
			uid : $('#searchUid').textbox("getValue"),
			anchorId : $('#anchorId').textbox("getValue")
		});
	}
 	function myformatterStart(date){
          var y = date.getFullYear();
          var m = date.getMonth()+1;
          return y+'-'+(m<10?('0'+m):m)+'-01';
      }
 	function myformatterEnd(date){
         var y = date.getFullYear();
         var m = date.getMonth()+1;
         var d = date.getDate();
         return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
     }
</script>
</head>
<body style="margin: 5px;">
		<table id="guardlist"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				用户UID:&nbsp;<input id=uid name="searchUid" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
				主播UID:&nbsp;<input id=anchorId name="anchorId" class="easyui-numberbox" min="1000000" max="100000000" required="true"/>
				&nbsp;&nbsp;<a href="javascript:searchGuard()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
			</div>
		</div>
		<div id="dlg" class="easyui-dialog" style="width: 470px; height: 350px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>用户UID:</td>
						<td>
							<input id=uid name="uid" class="easyui-numberbox" min="1000000" max="100000000" onClick required="true"/>
						</td>
					</tr>
					<tr>
						<td>用户昵称:</td>
						<td>
							<input id="nickname" name="nickname" class="easyui-textbox"  data-options="readonly:true"/>
						</td>
					</tr>
					<tr>
						<td>守护类型</td>
						<td>
							<input id="gid" name="gid" class="easyui-combobox" required="true"  missingMessage="守护必填"
							data-options="valueField: 'gid',
    									textField: 'gname',
    									url: '../../gift/forSelectBySubtype?subtype=7'"
    						/>
						</td>
					</tr>
					<tr>
						<td>天数:</td>
						<td>
							<input class="easyui-numberbox" name="num" id="num"  data-options="disabled:false" required="true" min="0" max="365"/>[1~365]
						</td>
					</tr>
					<tr>
						<td>守护主播UID:</td>
						<td>
							<input id="anchoruid" name="anchoruid" class="easyui-numberbox" min="1000000" max="100000000" onClick required="true"/>
						</td>
					</tr>
					<tr>
						<td>用户昵称:</td>
						<td>
							<input id="anchorname" name="anchorname" class="easyui-textbox"  data-options="readonly:true"/>
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
			<a href="javascript:saveGuard()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
</body>
</html>