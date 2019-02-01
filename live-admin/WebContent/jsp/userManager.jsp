<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>用户管理</title>
<%@ include file="../header.jsp"%>
<script>
	var dataGrid;
	var url;
	var unionid;
	$(function() {

		dataGrid = $("#userManager").datagrid(
				{
					url : '../userManager',
					width : getWidth(0.97),
					height : getHeight(0.97),
					title : '用户管理',
					pagination : true,
					idField : "creatAt",
					sortName : 'creatAt',
					sortOrder : 'desc',
					rownumbers : true,
					singleSelect : true,
					pageSize : 20,
					pageList : [ 20, 50, 100 ],
					toolbar : '#tb',
					columns : [ [
							{
								field : 'uid',
								title : '用户id',
								align : 'center',
								sortable : false
							},
							{
								field : 'nickname',
								title : '用户昵称',
								align : 'center',
								sortable : false
							},
							{
								field : 'anchorLevel',
								title : '主播等级',
								align : 'center',
								sortable : false,
							},
							{
								field : 'userLevel',
								title : '用户等级',
								align : 'center',
								sortable : false
							},
							{
								field : 'creatAt',
								title : '操作时间',
								align : 'center',
								width : "150",
								sortable : false,
								formatter : function(data) {
									return data ? new Date(data * 1000)
											.Format("yyyy-MM-dd HH:mm:ss") : ""
								}
							},
							{
								field : 'handle',
								title : '当前状态',
								align : 'center',
								sortable : false,
								formatter : function(data){
									if(data==1){
										return "禁播";
									}else if(data==2){
										return "封号";
									}else if(data==0){
										return "正常";
									}else if(data==3){
										return "封终端";
									}
								} 
							},
							{
								field : 'source',
								title : '操作平台',
								align : 'center',
								sortable : false,
								formatter : function(data){
									if(data==1){
										return "客户的";
									}else if(data==2){
										return "后台";
									}else{
										return "其他";
									}
								} 
							},{
								field : 'adminid',
								title : '处理管理员',
								align : 'center',
								sortable : false
							},{
								field : 'cause',
								title : '处理原因',
								align : 'center',
								width : "200",
								sortable : false
							},{
								field : 'edit',
								title : '操作',
								align : 'center',
								width : "70",
								sortable : false,
								formatter : function(data) {
					            	return '<a href="javascript:Updatehandle();" class="easyui-tooltip" title="查看用戶信息">操作</a>';
								}
							}
							] ]
				})
	})
	
	function Updatehandle(){
		var row = $('#userManager').datagrid('getSelected');
	    if (row){
	        $('#dlg').dialog('open').dialog('center').dialog('setTitle','管理用户');
	        $('#fm').form('clear');
	        $('#fm').form('load',row);
	    }
	}
	function searchUser(){
		var uid=$("#userid").val();
		if(uid == ""){
			alert("主播id不能为空");
			return;
		}
		$("#userManager").datagrid('reload', {
			url : '../userManager',
			method:"search",
			uid:uid
		})
		
	}
	
	
	function statusSelect(){
		var uid=document.getElementById("selectid").value;
		$("#userManager").datagrid('reload', {
			url : '../userManager',
			method:"status",
			uid:uid
		})	
	}
	function edithandle(id){
		var row = $('#userManager').datagrid('getSelected');
		var uid = row.uid;
		$('#fm').form('submit',{
	    	url:'../userManager?method=edit&id='+id+'&uid='+uid,
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
	                $('#dlg').dialog('close');        
	                $('#userManager').datagrid('reload');
	            }
	        }
	    });
	}

</script>
</head>
<body>
	<div>
		<table id="userManager"></table>
		<div id="tb" style="padding: 2px 5px; height: auto">
			<div>
				&nbsp;主播ID：&nbsp;<input type="text" name="userid" id="userid" size="10" />
				<a href="javascript:searchUser()" class="easyui-linkbutton"
					iconCls="icon-search">搜索</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select id="selectid" onChange="statusSelect();">
					<option value=0>违规名单汇总</option>
					<option value=2>封号</option>
					<option value=1>禁播</option>
				</select>
			</div>
		</div>	
	</div>
	
	<div id="dlg" class="easyui-dialog"
			style="width: 570px; height: 350px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>
							处理原因<input type="text" id="name" name="cause">
						</td>
					</tr>
					<tr>	
						<td>
							<input type="button" value="封号" id=1 onclick="edithandle(id)">  
							<input type="button" value="解封" id=2 onclick="edithandle(id)"> 
							<input type="button" value="禁播" id=5 onclick="edithandle(id)"> 
							<input type="button" value="解禁播" id=6 onclick="edithandle(id)"> 
						</td>
					</tr>
					 
				</table>
			</form>
		</div>
</body>
</html>