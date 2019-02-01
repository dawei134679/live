<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<title>主播封面管理</title>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>

<script type="text/javascript">

	var url;

	function searchAnchor() {
		
		$("#fm").form("clear");
		
		$('#dg').datagrid('load', {
			anchoruid : $('#anchoruid').val(),
			unionid : $('#unionid').combobox("getValue")
		});
	}

	function saveAnchor() {
		$("#fm").form("submit", {
			url : url,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(data) {
				var result = eval("("+data+")");
				if (result.errMsg) {
					$.messager.alert("系统提示", result.errMsg);
					return;
				} else {
					$.messager.alert("系统提示", "上传成功");
					$("#fm").form("clear");
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}
			},
			error : function(result){
				alert(result);
			}
		});
	}

	function closeAnchorDialog() {
		$("#fm").form("clear");
		$("#dlg").dialog("close");
	}

	function openAnchorCoverDialog() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length != 1) {
			$.messager.alert("系统提示", "请选择一条要编辑的数据！");
			return;
		}
		var row = selectedRows[0];
		$("#showlivimage").attr("src",row.livimage);
		$("#showpcimg1").attr("src",row.pcimg1);
		$("#showpcimg2").attr("src",row.pcimg2);
		row.uid=row.anchorid;
		row.showlivimage = row.livimage;
		row.showpcimg1 = row.pcimg1;
		row.showpcimg2 = row.pcimg2;
		$("#dlg").dialog("open").dialog('center').dialog("setTitle", "编辑主播封面");
		$("#fm").form("load", row);
		url = "../../anchor/editCover";
	}
	
</script>
<script type="text/javascript">
	var dataGrid;
	
	$(function () {
	
	    dataGrid = $("#dg").datagrid({
	        url: '../../anchor/getCover',
	        width: getWidth(0.97),
	        height: getHeight(0.97),
			queryParams: {
				anchoruid : $('#anchoruid').val(),
				unionid : $('#unionid').combobox("getValue")
			},
	        title: '主播封面列表',
			pagination : true,
	        rownumbers: true,
			striped : true,
	        singleSelect: true,
	        fit:true,
	        fitColumns:true,
	        pageSize:20,
	        pageList:[20,50,100],
	        toolbar: '#tb',
	        columns: [[
	            {
	            	field: 'cb', 
	            	checkbox:'true', 
	            	align: 'center', 
	            	sortable: false
	            },
	            {
	            	field: 'headimage', 
	            	width:"80", 
	            	title: '头像', 
	            	align: 'center', 
	            	sortable: true,
	            	formatter:function (data) {
	        			return "<img src='"+data+"' width='80px' height='80px'/>";
	        		}
	            },
	            {
	            	field: 'unionid', 
	            	width:"200", 
	            	hidden:true, 
	            	title: '家族Id', 
	            	align: 'center', 
	            	sortable: false
	            },
	            {
	            	field: 'unionname', 
	            	width:"100", 
	            	title: '家族', 
	            	align: 'center', 
	            	sortable: false
	            },
	            {
	            	field: 'anchorid', 
	            	width:"90", 
	            	title: 'UID', 
	            	align: 'center', 
	            	sortable: false
	            },
	            {
	            	field: 'nickname', 
	            	width:"120", 
	            	title: '昵称', 
	            	align: 'center', 
	            	sortable: false
	            },
	            {
	            	field: 'anchorLevel', 
	            	width:"70", 
	            	title: '主播等级', 
	            	align: 'center', 
	            	sortable: false
	            },
	            {
	            	field: 'recommend', 
	            	width:"100", 
	            	title: '房间级别', 
	            	align: 'center', 
	            	sortable: false,
	            	formatter:function(data){
	            		if(data == 0){
	            			return "普通";
	            		}else if(data == 1){
	            			return "最新";
	            		}else if(data == 2){
	            			return "热门";
	            		}else{
	            			return "未知";
	            		}
	            	}
	            },
	            {
	            	field: 'livimage', 
	            	width:"150", 
	            	title: '手机封面', 
	            	align: 'center', 
	            	sortable: false,
	            	formatter:function (data) {
	        			return "<img src='"+data+"' width='120px' height='120px'/>";
	        		}
	            },
	            {
	            	field: 'pcimg1', 
	            	width:"150", 
	            	title: 'PC封面(4:3)', 
	            	align: 'center', 
	            	sortable: false,
	            	formatter:function (data) {
	        			return "<img src='"+data+"' width='120px' height='90px'/>";
	        		}
	            },
	            {
	            	field: 'pcimg2', 
	            	width:"165", 
	            	title: 'PC封面(16:9)', 
	            	align: 'center', 
	            	sortable: false,
	            	formatter:function (data) {
	        			return "<img src='"+data+"' width='160px' height='90px'/>";
	        		}
	            }
	        ]]
	    })
	})
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>

	<div id="tb">
		<div>
			<a href="javascript:openAnchorCoverDialog()"
				class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a> 
		</div>
		<div>
			&nbsp;选择公会:<input id=unionid name="unionid" class="easyui-combobox" required="true"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../../unionAnchor/getUnionNameList'"
    						/>)
			&nbsp;主播ID：&nbsp;<input type="text" name="anchoruid" id="anchoruid" size="10" />&nbsp;&nbsp;(若不填主播UID,则只在工会下查主播信息)
			&nbsp;&nbsp;<a href="javascript:searchAnchor()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		</div>
	</div>

	<div id="dlg" class="easyui-dialog"
		style="width: 670px; height: 550px; padding: 10px 20px" closed="true" buttons="#dlg-buttons">
		<form id="fm" method="post" enctype="multipart/form-data">
					<input type="hidden" name="livimage" value="">
					<input type="hidden" name="pcimg1" value="">
					<input type="hidden" name="pcimg2" value="">
					<input type="hidden" name="uid" value="">
			<table cellspacing="5px;">
				<tr>
					<td>UID：</td>
					<td><input type="text" name="anchorid" id="anchorid"
						class="easyui-validatebox" required="true" data-options="disabled:true" value=""/></td>
					<td>昵称：</td>
					<td><input type="text" name="nickname" id="nickname"
						class="easyui-validatebox" required="true" data-options="disabled:true" value=""/></td>
				</tr>
				<tr>
					<td>家族：</td>
					<td><input type="text" name="unionname" id="unionname"
						class="easyui-validatebox" required="true" data-options="disabled:true"/></td>
					<td>主播等级：</td>
					<td><input type="text" name="anchorLevel" id="anchorLevel"
						class="easyui-validatebox" required="true" data-options="disabled:true"/></td>
				</tr>
				<tr>
					<td colspan="4">手机封面(1:1)</td>
				</tr>
				<tr>
					<td colspan="4"><input id="imgApp" name="imgApp" onchange="javascript:setImagePreview(this,localImag1,showlivimage);" type="file" /></td>
				</tr>
				<tr>
					<td colspan="4"><div  id="localImag1"><img id="showlivimage" alt="预览图片" src="" width='120px' height='120px'/></div></td>
				</tr>
				<tr>
					<td colspan="4">PC封面(4:3)</td>
				</tr>
				<tr>
					<td colspan="4"><input type="file" name="imgPc1" id="imgPc1" onchange="javascript:setImagePreview(this,localImag2,showpcimg1);"></td>
				</tr>
				<tr>
					<td colspan="4"><div  id="localImag2"><img id="showpcimg1" alt="预览图片" src="" width='120px' height='90px'/></div></td>
				</tr>
				<tr>
					<td colspan="4">PC封面(16:9)</td>
				</tr>
				<tr>
					<td colspan="4"><input type="file" name="imgPc2" id="imgPc2" onchange="javascript:setImagePreview(this,localImag3,showpcimg2);"></td>
				</tr>
				<tr>
					<td colspan="4"><div  id="localImag3"><img id="showpcimg2" name="pcimg2" alt="预览图片" src="" width='160px' height='90px'/></div></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="dlg-buttons">
		<a href="javascript:saveAnchor()" class="easyui-linkbutton" iconCls="icon-ok">保存</a> 
		<a href="javascript:closeAnchorDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
	</div>
</body>
<script>
    //检查图片的格式是否正确,同时实现预览
    function setImagePreview(obj, localImagId, imgObjPreview) {
        var array = new Array('gif', 'jpeg', 'png', 'jpg', 'bmp'); //可以上传的文件类型
        if (obj.value == '') {
            $.messager.alert("让选择要上传的图片!");
            return false;
        }
        else {
            var fileContentType = obj.value.match(/^(.*)(\.)(.{1,8})$/)[3]; //这个文件类型正则很有用 
            ////布尔型变量
            var isExists = false;
            //循环判断图片的格式是否正确
            for (var i in array) {
                if (fileContentType.toLowerCase() == array[i].toLowerCase()) {
                    //图片格式正确之后，根据浏览器的不同设置图片的大小
                    if (obj.files && obj.files[0]) {
                        //火狐下，直接设img属性 
                        imgObjPreview.style.display = 'block';
//                         imgObjPreview.style.width = '400px';
//                         imgObjPreview.style.height = '400px';
                        //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式 
                        imgObjPreview.src = window.URL.createObjectURL(obj.files[0]);
                    }
                    else {
                        //IE下，使用滤镜 
                        obj.select();
                        var imgSrc = document.selection.createRange().text;
                        //必须设置初始大小 
//                         localImagId.style.width = "400px";
//                         localImagId.style.height = "400px";
                        //图片异常的捕捉，防止用户修改后缀来伪造图片 
                        try {
                            localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
                            localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader=").src = imgSrc;
                        }
                        catch (e) {
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
</script>
</html>