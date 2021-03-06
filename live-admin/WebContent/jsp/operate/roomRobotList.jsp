<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>机器人管理</title>
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
		dataGrid = $("#RoomRobotList").datagrid({
			url : '../../roomRobot/getRoomRobotList',
			width : getWidth(0.97),
			height : getHeight(0.97),
			title : '机器人列表',
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
				field : 'headimage',
				title : '用户头像',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					return "<img src='"+data+"' width='80px' height='80px'/>";
				}
			},{
				field : 'nickname',
				title : '用户昵称',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'anchorlevel',
				title : '主播等级',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'userlevel',
				title : '用户等级',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'sex',
				title : '性别',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					if (data == 1) {
						return "男";
					} else {
						return "女";
					}
				}
			}, {
				field : 'identity',
				title : '用户身份',
				align : 'center',
				width : 50,
				sortable : false,
				formatter : function(data) {
					if (data == 1) {
						return "主播";
					} else if(data == 2) {
						return "普通用户";
					}else{
						return "管理员";
					}
				}
			},  {
				field : 'province',
				title : '省份',
				align : 'center',
				width : 50,
				sortable : false
			}, {
				field : 'city',
				title : '城市',
				align : 'center',
				width : 50,
				sortable : false
			}] ]
		})
	});
	
	function addCarDialog() {
		$("#showlivimage").attr("src", "");
		url = "../../roomRobot/saveRobot";
		$('#dlg').dialog('open').dialog('center').dialog('setTitle', '新增');
		$("#fm").form("clear");
	}

	function updateRobotDialog() {
		$("#fm").form("clear");
		url = "../../roomRobot/updateRobot";
		var rowSel = $("#RoomRobotList").datagrid('getSelected');
		if (rowSel == null) {
			$.messager.alert("系统提示", "请选择数据");
			return;
		}
		var row = $.extend({}, rowSel);
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "修改");
		$("#showlivimage").attr("src", row.headimage);
		delete row['headimage'];
		$("#fm").form("load", row);
	}

	function saveRobot() {
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
					$("#RoomRobotList").datagrid('reload');
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
	
	function reAllRoomRobotRedis() {
		$.messager.confirm("系统提示", "是否更新缓存?", function(r) {
			if (r) {
				$.post("../../roomRobot/reAllRobotRedis", {}, function(result) {
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
	<table id="RoomRobotList"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<a href="javascript:addCarDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="btn-save">新增</a>
			<a href="javascript:updateRobotDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="btn-update">修改</a>
			<a href="javascript:reAllRoomRobotRedis()"class="easyui-linkbutton invalid" iconCls="icon-reload" plain="true">更新全部缓存</a>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog"
		style="width: 570px; height: 450px; padding: 10px 20px" closed="true"
		buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
			<table cellspacing="5px;">
				<tr>
					<td>图片(45*45):</td>
					<td><input type="file" name="headimage" id="headimage"
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
					<td>用户昵称:</td>
					<td><input id="uid" name="uid" type="hidden" /> <input
						id="nickname" name="nickname" class="easyui-textbox" onClick
						required="true" /></td>
				</tr>
				<tr>
					<td>主播等级:</td>
					<td><input id="anchorlevel" name="anchorlevel"
						class="easyui-numberbox" onClick required="true"
						data-options="min:1,max:2" />[1~2]</td>
				</tr>
				<tr>
					<td>用户等级:</td>
					<td><input id="userlevel" name="userlevel"
						class="easyui-numberbox" onClick required="true"
						data-options="min:1,max:2" />[1~2]</td>
				</tr>
				<tr>
					<td>性别:</td>
					<td><select id="sex" name="sex" class="easyui-combobox"
						onClick required="true" missingMessage="状态必填"
						style="width: 170px;">
							<option value="1">男</option>
							<option value="2">女</option>
					</select></td>
				</tr>
				<tr>
					<td>身份:</td>
					<td><select id="identity" name="identity" class="easyui-combobox"
						onClick required="true" missingMessage="状态必填"
						style="width: 170px;">
							<option value="1">主播</option>
							<option value="2">普通用户</option>
							<option value="3">管理员</option>
					</select></td>
				</tr>
				<tr>
					<td>省份:</td>
					<td><input
						id="province" name="province" class="easyui-textbox" onClick
						required="true" /></td>
				</tr>
				<tr>
					<td>城市:</td>
					<td><input
						id="city" name="city" class="easyui-textbox" onClick
						required="true" /></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:saveRobot()" class="easyui-linkbutton"
			iconCls="icon-ok">保存</a>
	</div>
</body>
</html>