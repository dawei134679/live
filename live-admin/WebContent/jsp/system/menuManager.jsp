<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>菜单管理</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script>
	var treeGrid,editingId,addingId;
	function add(){
		var row = $('#tg').treegrid('getSelected');
		if (row){
			$("#fm").form("clear");
			$("#type").combobox('select',0);
			$("#show").combobox('select',1);
			$("#sort").textbox("setValue",1);
			$("#pname").textbox("setValue",row.menuname);
			$("#pid").val(row.mid);
			$('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增菜单');
		}else{
			$.messager.alert("提示","请选择一个父节点");
		}
		if (editingId != undefined){
			$('#tg').treegrid('endEdit', editingId);
			editingId = undefined;
			$("#edit_save").hide();
			$("#edit_cancel").hide();
		}
	}
	function saveMenu(){
		$("#fm").form("submit", {
			url : '../../system/adminMenu/saveMenu',
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("("+data+")");
				if(result.code == 200){
					$('#tg').treegrid('endEdit', editingId);
					editingId = undefined;
					$("#edit_save").hide();
					$("#edit_cancel").hide();
					$('#tg').treegrid('reload',$("#pid").val());
					$("#fm").form("clear");
					$("#dlg").dialog("close");
				}
			},
			error : function(result){
			}
		});
	}
	function edit(){
		if (editingId != undefined){
			$('#tg').treegrid('select', editingId);
			return;
		}
		var row = $('#tg').treegrid('getSelected');
		if (row){
			editingId = row.mid;
			$('#tg').treegrid('beginEdit', editingId);
			$("#edit_save").show();
			$("#edit_cancel").show();
		}else{
			$.messager.alert("提示","请先选中一行要修改的数据");
		}
	}
	function save(){
		if (editingId != undefined){
			var row = $('#tg').treegrid('getSelected');
			if(!row){
				$.messager.alert("提示","请刷新页面重试");
				return;
			}
			if(!row.menuname){
				$.messager.alert("提示","请输入名称");
				return;
			}
			var edit_name = $('#tg').datagrid('getEditor', {field:'menuname',index:editingId});
			var edit_url = $('#tg').datagrid('getEditor', {field:'url',index:editingId});
			var edit_show = $('#tg').datagrid('getEditor', {field:'show',index:editingId});
			var edit_type = $('#tg').datagrid('getEditor', {field:'type',index:editingId});
			var edit_sort = $('#tg').datagrid('getEditor', {field:'sort',index:editingId});
			
			row.menuname = $(edit_name.target).val();
			row.url = $(edit_url.target).val();
			row.show = $(edit_show.target).is(":checked") ? 1 : 2;
			row.type = $(edit_type.target).combobox("getValue");
			row.sort = $(edit_sort.target).val();

			$.ajax({
				url : '../../system/adminMenu/saveMenu',
				type:"POST",
				data:row,
				success:function(result){
					$.messager.alert("提示",result.msg);
					if(result.code == 200){
						$('#tg').treegrid('endEdit', editingId);
						editingId = undefined;
						$("#edit_save").hide();
						$("#edit_cancel").hide();
					}
				}
			});
		}else{
			$.messager.alert("提示","请先选择一行进行修改");
		}
	}
	function cancel(){
		if (editingId != undefined){
			$('#tg').treegrid('cancelEdit', editingId);
			editingId = undefined;
			$("#edit_save").hide();
			$("#edit_cancel").hide();
		}
	}
	function remove(){
		$("#edit_save").hide();
		$("#edit_cancel").hide();
		var row = $('#tg').treegrid('getSelected');
		if (row){
			if(row.childCount > 0){
				$.messager.alert("提示","请先删除当前节点下的子节点");
				return;
			}
			$.messager.confirm("确认",'删除【'+row.menuname+'】吗？',function(r){
                if (r){
					$.ajax({
						url : '../../system/adminMenu/removeById',
						data:{id:row.mid},
						success:function(result){
							if(result.code == 200){
								$('#tg').treegrid('reload',row.pid);
								//$('#tg').treegrid('remove',row.mid);
							}else{
								$.messager.alert("提示",result.msg);
							}
						}
					});
                }
			});
		}else{
			$.messager.alert("提示","请先选中一行要删除的数据");
		}
	}
	$(function() {
		$('#tg').treegrid({
			url: '../../system/adminMenu/listByMid',
			rownumbers: true,
			animate: true,
			collapsible: true,
			fitColumns: true,
			idField:'mid',
		    treeField:'menuname',
		    showFooter: true,
		    columns:[[
				{title:'编号',field:'mid',width:80},
				{title:'名称',field:'menuname',width:100,editor:{ type: 'text', options: { required: true } }},
				{title:'URL',field:'url',width:120,editor:{ type: 'text', options: { required: true } }},
				{title:'是否有效',field:'show',width:80,editor:{type:'checkbox',options:{on:'1',off:'2'}},formatter:function(v){var ary = ["","是","否"];return ary[v];}},
				{title:'类型',field:'type',width:80,editor:{ type: 'combobox', options: {editable:false, data: [{value:2,text:'功能'},{value:1,text:'菜单'},{value:0,text:'父菜单'}], valueField: "value", textField: "text" } },formatter:function(v){var dary=["父菜单","菜单","功能"];return dary[v];}},
				{title:'排序',field:'sort',width:30,editor:{ type: 'numberbox', options: { required: true } }}
		    ]],
		    loader:function(param,success,error){
		    	 $.ajax({
		             url:"../../system/adminMenu/listByMid",
		             data:param,
		             dataType : "html",
		             contentType : "application/x-www-form-urlencoded",
		             success: function(data){
		            	 var d = eval('('+data+')');
		            	 for(var i=0,len = d.length;i<len;i++){
		 		    		var row = d[i];
		 			    	row._parentId = row.pid;
		 			    	if(row.childCount > 0){
		 			    		row.state = 'closed';
		 			    	}
		 			    	d[i] = row;
		 		    	}
		            	success(d);
		            	if(!param.id && d.length > 0){//加载根节点
		            		$('#tg').treegrid('expand', d[0].mid);
		            	}
		             }
		         })
		    }
		});
	});
</script>
</head>
<body style="margin: 5px;">
	<div class="easyui-panel" title="菜单管理" style="width:100%;height:100%;">
		<div id="where" style="padding:5px 0;">
			<div>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="add()">增加</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="remove()">删除</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" onclick="edit()">修改</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="save()" id="edit_save" style="display:none;">保存</a>
				<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-no" onclick="cancel()" id="edit_cancel" style="display:none;">取消</a>
			</div>
		</div>
		<div>
			<table id="tg" class="easyui-treegrid"></table>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog"
		style="width: 570px; height: 450px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellspacing="5px;">
				<tr>
					<td>上级菜单:</td>
					<td>
						<input type="hidden" id="pid" name="pid"/>
						<input type="text" class="easyui-textbox" id="pname" disabled/>
					</td>
				</tr>
				<tr>
					<td>名称:</td>
					<td>
						<input id="menuname" name="menuname" class="easyui-textbox" onClick required="true" />
					</td>
				</tr>
				<tr>
					<td>URL:</td>
					<td>
						<input id="url" name="url" class="easyui-textbox" onClick required="true" />
					</td>
				</tr>
				<tr>
					<td>类型:</td>
					<td>
						<select id="type" class="easyui-combobox" name="type" style="width:170px" editable="false">
							<option value="0" selected="selected">父菜单</option>
							<option value="1">菜单</option>
							<option value="2">功能</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>是否显示:</td>
					<td>
						<select id="show" class="easyui-combobox" name="show" style="width:170px" data-options="{editable:false,value:1}">
							<option value="1" selected="selected">是</option>
							<option value="2">否</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>排序:</td>
					<td>
						<input id="sort" name="sort" class="easyui-numberbox" min="1" required="true" />
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveMenu()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
	</div>
</body>
</html>