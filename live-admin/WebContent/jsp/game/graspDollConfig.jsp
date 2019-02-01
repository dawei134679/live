<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>抓娃娃游戏配置</title>
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
		dataGrid = $("#dollConfiglist").datagrid({
			url : '../../ggd/getDollList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '游戏娃娃列表',
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
				field : 'name',
				title : '名称',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'imageUrl',
				title : '娃娃图片',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					return "<img src='"+data+"' width='80px' height='80px'/>";
				}
			}, {
				field : 'probability',
				title : '概率',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'multiple',
				title : '倍数',
				align : 'center',
				width : 50,
				sortable : false
			},{
				field : 'sort',
				title : '显示顺序',
				align : 'center',
				width : 20,
				sortable : false
			}, {
				field : 'status',
				title : '状态',
				align : 'center',
				width : 20,
				sortable : false,
				formatter : function(data) {
					if (data == 1) {
						return "启用";
					} else {
						return "<font color='red'>禁用</font>";
					}
				}
			} ] ]
		})
	});

	function addDollDialog() {
		$("#showlivimage").attr("src", "");
		url = "../../ggd/saveDoll";
		$('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增');
		$("#fm").form("clear");
	}

	function updateCarDialog() {
		$("#fm").form("clear");
		url = "../../ggd/updateDoll";
		var rowSel = $("#dollConfiglist").datagrid('getSelected');
		if (rowSel == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		var row = $.extend({}, rowSel);
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改");
		$("#showlivimage").attr("src", row.imageUrl);
		delete row['imageUrl'];
		$("#fm").form("load", row);
	}

	function doValid() {
		var row = $("#dollConfiglist").datagrid('getSelected');
		if (row == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		var status = row.status;
		var parmas;
		if (status == 1) {
			params = {
				"id" : row.id,
				"status" : 2
			};
		}
		if (status == 2) {
			params = {
				"id" : row.id,
				"status" : 1
			};
		}
		var url = '../../ggd/doValid';
		$.post(url, params, function(result) {
			var data = eval("(" + result + ")");
			if (data.row == 1) {
				$.messager.alert("系统提示", "修改成功");
			} else {
				$.messager.alert("系统提示", "修改失败");
			}
			$("#dollConfiglist").datagrid('reload');
		})

	}

	function searchDoll() {
		$('#dollConfiglist').datagrid('load', {
			name : $('#DollName').val(),
			status : $("#DollStatus").val(),
		});
	}

	function saveDoll() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("(" + data + ")");
				if (!result.result) {
					$.messager.alert("系统提示", result.msg);
					return;
				} else {
					$.messager.alert("系统提示", result.msg);
					$("#fm").form("clear");
					$("#dlg").dialog("close");
					$("#dollConfiglist").datagrid('reload');
				}
			},
			error : function(result) {
				alert(result);
			}
		});
	}

	//检查图片的格式是否正确,同时实现预览
	function setImagePreview(obj, localImagId, imgObjPreview) {
		var array = new Array('gif', 'jpeg', 'png', 'jpg', 'bmp'); //可以上传的文件类型
		if (obj.value == '') {
			$.messager.alert("让选择要上传的图片!");
			return false;
		} else {
			var fileContentType = obj.value.match(/^(.*)(\.)(.{1,8})$/)[3]; //这个文件类型正则很有用 
			////布尔型变量
			var isExists = false;
			//循环判断图片的格式是否正确
			for ( var i in array) {
				if (fileContentType.toLowerCase() == array[i].toLowerCase()) {
					//图片格式正确之后，根据浏览器的不同设置图片的大小
					if (obj.files && obj.files[0]) {
						//火狐下，直接设img属性 
						imgObjPreview.style.display = 'block';
						//                         imgObjPreview.style.width = '400px';
						//                         imgObjPreview.style.height = '400px';
						//火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式 
						imgObjPreview.src = window.URL
								.createObjectURL(obj.files[0]);
					} else {
						//IE下，使用滤镜 
						obj.select();
						var imgSrc = document.selection.createRange().text;
						//必须设置初始大小 
						//                         localImagId.style.width = "400px";
						//                         localImagId.style.height = "400px";
						//图片异常的捕捉，防止用户修改后缀来伪造图片 
						try {
							localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
							localImagId.filters
									.item("DXImageTransform.Microsoft.AlphaImageLoader=").src = imgSrc;
						} catch (e) {
							$.messager.alert("您上传的图片格式不正确，请重新选择!");
							return false;
						}
						imgObjPreview.style.display = 'none';
						document.selection.empty();
					}
					isExists = true;
					return true;
				}
			}
			if (isExists == false) {
				$.messager.alert("上传图片类型不正确!");
				return false;
			}
			return false;
		}
	}
	
	function closeDollDialog(){
		$("#fm").form("clear");
		$("#dlg").dialog("close");
	}
	
	function reGameDollConfigRedis() {
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			if (r) {
				$.post("../../ggd/reGameDollConfigRedis", {}, function(result) {
					if (result.code == 200) {
						$.messager.alert("系统提示", "更新缓存成功");
						$("#dg").datagrid("reload");
					} else {
						$.messager.alert('系统提示', "更新缓存失败");
					}
				}, "json");
			}
		});
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="dollConfiglist"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<a href="javascript:addDollDialog()" class="easyui-linkbutton"
				iconCls="icon-add" plain="true" id="btn-save">新增</a> <a
				href="javascript:updateCarDialog()" class="easyui-linkbutton"
				iconCls="icon-edit" plain="true" id="btn-update">修改</a> <a
				href="javascript:doValid()" class="easyui-linkbutton invalid"
				iconCls="icon-remove" plain="true">启用/禁用</a> <a
				href="javascript:reGameDollConfigRedis()"
				class="easyui-linkbutton invalid" iconCls="icon-reload" plain="true">更新缓存</a>
		</div>
		<div>
			名称:&nbsp;<input id="DollName" /> &nbsp;&nbsp;状态：&nbsp;<select
				id="DollStatus">
				<option value="-1">--全部--</option>
				<option value="1">启用</option>
				<option value="2">禁用</option>
			</select> &nbsp;&nbsp;<a href="javascript:searchDoll()"
				class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog"
		style="width: 570px; height: 450px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellspacing="5px;">
				<tr>
					<td>名称:</td>
					<td><input id="id" name="id" type="hidden" /> <input
						id="name" name="name" class="easyui-textbox" onClick
						required="true" /></td>
				</tr>
				<tr>
					<td>图片(45*45):</td>
					<td><input type="file" name="imageUrl" id="imageUrl"
						onchange="javascript:setImagePreview(this,localImag1,showlivimage);"
						onClick required="true" /></td>
				</tr>
				<tr>
					<td></td>
					<td><div id="localImag1">
							<img id="showlivimage" alt="预览图片" src="" width='120px'
								height='120px' />
						</div>
					</td>
				</tr>
				<tr>
					<td>概率:</td>
					<td><input id="probability" name="probability"
						class="easyui-numberbox" onClick required="true"
						data-options="min:0,max:1,precision:2" />[0~1]</td>
				</tr>
				<tr>
					<td>倍数:</td>
					<td><input id="multiple" name="multiple"
						class="easyui-numberbox" onClick required="true"
						data-options="min:1,precision:2" /></td>
				</tr>
				<tr>
					<td>显示顺序:</td>
					<td><input id="sort" name="sort" class="easyui-numberbox"
						onClick required="true" min="1" /></td>
				</tr>
				<tr>
					<td>状态:</td>
					<td><select id="status" name="status" class="easyui-combobox"
						onClick required="true" missingMessage="状态必填"
						style="width: 170px;">
							<option value="1">启动</option>
							<option value="2">禁用</option>
					</select></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveDoll()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a><a href="javascript:closeDollDialog()"
			class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
</html>