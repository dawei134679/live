var panel;
var grid;
var win;
var form;
var rulewin;
var ruleform;
var basehref = document.getElementsByTagName("base")[0].href;
var gid = -1;
var grule = '';
$(function() {
	// 生成角色数据datagrid
	grid = $('#group-table')
			.datagrid(
					{
						url : basehref + 'adminRole?method=getRoleList',
						title : '角色管理',
						width : getWidth(0.98),
						height : getHeight(1)-18,
						iconCls : 'icon-group',
						singleSelect : true,
						remoteSort : false,
						striped : true,
						nowrap : false,
						idField : "role_id",
						columns : [ [
								{
									field : 'role_id',
									title : '角色ID',
									width : getWidth(0.2),
									align : 'center',
									sortable : false
								},
								{
									field : 'role_name',
									title : '角色名称',
									width : getWidth(0.3),
									align : 'center',
									sortable : false
								},
								{
									field : 'opt',
									title : '操作',
									width : getWidth(0.5),
									align : 'center',
									formatter : function(value, rec) {
										var href = "javascript:editrule("
												+ rec.role_id + ");";
										var href1 = "javascript:editgroup("
												+ rec.role_id + ");";
										var href2 = "javascript:delgroup("
												+ rec.role_id + ");";
										return '<span><a href='
												+ href
												+ ' ><font color="fuchsia">权限设置</font></a></span>'
												+ '    '
												+ '<span><a href='
												+ href1
												+ ' ><font color="blue">编辑</font></a></span>'
												+ '    '
												+ '<span><a href='
												+ href2
												+ ' ><font color="red">删除</font></a></span>';

									}
								} ] ],
						pagination : true,
						rownumbers : true,
						toolbar : [ {
							text : '新增',
							iconCls : 'icon-add',
							handler : newgroup
						} ]
					});
	// 新建、修改角色数据的窗口
	win = $('#userGroup-window').window( {
		resizable : false,
		modal : true,
		shadow : false,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closed : true
	});
	form = win.find('form');

	form.form( {
		onBeforeLoad : function() {
			showloading($('#userGroup-window'), "正在加载数据,请稍候。。。");
		},
		onLoadSuccess : function(data) {
			hideloading($('#userGroup-window'));
		},
		onLoadError : function(data) {
			hideloading($('#userGroup-window'));
		}
	});
	// 设置权限的窗口
	rulewin = $('#rule-window').window( {
		resizable : false,
		modal : true,
		shadow : false,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closed : true
	});
	ruleform = rulewin.find('form');
	//获取数据表格窗口额panel
	panel = $('#group-table').datagrid('getPanel');
	$('body').css('visibility', 'visible');
});

// 根据角色id获取其权限树
function loadruletree(id) {
	showloading($('#rule-window'), "正在生成权限树,请稍候。。。");
	$.ajax( {
		url : basehref + 'adminRole?method=getRuleTree&rid='+id,
		type : 'post',
		contentType : 'json',
		dataType : 'json',
		success : function(data) {
			//动态生成加载树
			$('#rules').tree(
					{
						checkbox : true,
						data : data,
						onClick : function(node) {
							$(this).tree('toggle', node.target);
						},
						onLoadSuccess : function() {
							hideloading($('#rule-window'));
						},
						onLoadError : function() {
							hideloading($('#rule-window'));
						}
					});
	}
	});

}

// 获取权限树中选中的项
function getChecked() {
	var nodes = $('#rules').tree('getChecked');
	grule = '';
	for ( var i = 0; i < nodes.length; i++) {
		if (grule != '')
			grule += ',';
		grule += nodes[i].id;
	}
}

// 修改权限的角色
function savegrouprules() {
	showloading($('#rule-window'), "正在保存权限,请稍候。。。");
	getChecked();
	if(grule=='')grule=0;
	$.ajax( {
		url : basehref + 'adminRole?method=saveRuleTree&gid=' + gid + '&grule=' + grule,
		type : 'post',
		contentType : 'application/json',
		dataType : 'json',
		success : function(data) {
			hideloading($('#rule-window'));
			$.messager.alert('成功', '<br/>修改角色权限成功！', 'info');
			rulewin.window('close');
			parent.window.getMenuTree();
		},
		error : function() {
			hideloading($('#rule-window'));
		}
	});
}

// 新建角色
function newgroup() {
	form.form('clear');
	win.window('open');
	form.url = basehref + 'adminRole?method=saveRole';
}
// 修改角色
function editgroup(id) {
	form.form('clear');
	form.form('load', basehref + 'adminRole?method=loadRuleById&rid=' + id);
	win.window('open');
	form.url = basehref + 'adminRole?method=saveRole';

}
// 修改权限
function editrule(id) {
	loadruletree(id);
	rulewin.window('open');
	gid = id;
}
// 保存角色的新建、修改
function savegroup() {
	form.form('submit', {
		url : form.url,
		onSubmit : function() {
			if ($(this).form('validate'))
				showloading($(this), "正在保存中,请稍候。。。");
			else
				return false;
			;
		},
		success : function(data) {
			eval('data=' + data);
			hideloading($('#userGroup-window'));
			if (data.success) {
				grid.datagrid('reload');
				win.window('close');
			} else {
				$.messager.alert('错误', data.message, 'error');
			}
		}
	});
}

// 删除角色
function delgroup(id) {
	$.messager.confirm('系统提示', '<br/>您确定要删除该角色吗?', function(r) {
		if (r) {
			showloading(panel, "正在删除中,请稍候。。。");
			$.ajax( {
				url : basehref + 'adminRole?method=delRule&rid=' + id,
				type : 'post',
				contentType : 'application/json',
				dataType : 'json',
				success : function(data) {
					hideloading(panel);
					if (data.success) {
						grid.datagrid('reload');
						win.window('close');
					} else {
						$.messager.alert('错误', data.message, 'error');
					}
				}
			});
		}
	});

}

// 关闭窗口
function closeWindow() {
	win.window('close');
}
function closeruleWindow() {
	rulewin.window('close');
}