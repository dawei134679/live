<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>苹果更新</title>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
var dataGrid;

$(function () {
    dataGrid = $("#dg").datagrid({
        url: '../',
        width: getWidth(0.97),
        height: getHeight(0.97),
		queryParams: {
		method:"list",
		},// 传参
        title: '更新',
        //pagination: true,
        rownumbers: true,
        singleSelect: true,
        fit:true,
        fitColumns:true,
        pageSize:20,
        pageList:[20,50,100],
        toolbar: '#tb',
		pagination : true,
		idField : "created",
		sortName : 'created',
		sortOrder : 'desc',
        columns: [[
            {field: 'created', width:"120", title: '创建日期', align: 'center', sortable: false,formatter : function(data) {
			return data ? new Date(data * 1000)
			.Format("yyyy-MM-dd HH:mm:ss") : ""
			}},
            {field: 'type', width:"200", title: '版本类型', align: 'center', sortable: false},
            {field: 'updateAdds', width:"200", title: '更新地址', align: 'center', sortable: false},
            {field: 'typeNum', width:"200", title: '最新版本号', align: 'center', sortable: false},
            {field: 'pubDate', width:"120", title: '发布日期', align: 'center', sortable: false,formatter : function(data) {
    			return data ? new Date(data * 1000)
    			.Format("yyyy-MM-dd HH:mm:ss") : ""
    		}},
    		{field: 'content', width:"200", title: '更新内容', align: 'center', sortable: false},
            {field: 'status', width:"90", title: '状态', align: 'center', sortable: false,formatter : function(data) {
            	return '<a href="javascript:getCheckUser();" class="easyui-tooltip" title="状态">操作</a>';
			}},
           
        ]]
    })
})
 
 function createAdv(){
	 $('#dlg').dialog('open').dialog('center').dialog('setTitle','新增广告');
     $('#fm').form('clear');
     url = '../unionAdd';
}



function saveUnion(){
    $('#fm').form('submit',{
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
                $('#dlg').dialog('close');        
                $('#dg').datagrid('reload');
            }
        }
    });
}

function out(){
	$('#dlg').dialog('close');        
    $('#dg').datagrid('reload');
}
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<input type="button" value="添加版本" onclick="createAdv()">
		</div>
	</div>
	
	<div id="dlg" class="easyui-dialog"
			style="width: 570px; height: 350px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>版本类型</td>
						<td><input type="text" name="type" id="type"
							class="easyui-textbox" required="true"
							data-options="disabled:false" /></td>
						<td>更新跳转地址</td>
						<td><input type="text" name="updateAdds" id="updateAdds"
							class="easyui-textbox" required="true"
							data-options="disabled:false" /></td>
					</tr>
					<tr>
						<td>最新版本号</td>
						<td><input type="text" name="typeNum" id="typeNum"
							class="easyui-textbox" 
							data-options="disabled:false" /></td>
						<td>发布时间</td>
						<td>
							<input class="easyui-datetimebox" id="pubDate" style="width:150px;height:24px" data-options="disabled:false">
						</td>
					</tr>
					<tr>
						<td>内容</td>
						<td>
							<input class="easyui-textbox" name="content" data-options="multiline:true" style="height:60px" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="dlg-buttons">
			<a href="javascript:saveUnion()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:out()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
</body>
</html>