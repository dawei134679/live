<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>轮播广告</title>
<%@ include file="../header.jsp" %>
<script type="text/javascript">
var dataGrid;

$(function () {
    dataGrid = $("#dg").datagrid({
        url: '../bannerList?method=carousel',
        width: getWidth(0.97),
        height: getHeight(0.97),
        title: '轮播广告',
        //pagination: true,
        rownumbers: true,
        singleSelect: true,
        fit:true,
        fitColumns:true,
        pageSize:20,
        pageList:[20,50,100],
        toolbar: '#tb',
		pagination : true,
		idField : "id",
		sortName : 'id',
		sortOrder : 'desc',
        columns: [[
            {field: 'id', width:"100", title: '广告Id', align: 'center', sortable: false},
            {field: 'name', width:"200", title: '活动名', align: 'center', sortable: false},
            {field: 'picUrl', width:"200", title: '图片', align: 'center', sortable: false,formatter:function(data){return '<img src="'+data+'" />';}},
            {field: 'jumpUrl', width:"190", title: '跳转地址', align: 'center', sortable: false,formatter : function(data) {
            	return '<a href="http://'+data+'" class="easyui-tooltip" title="查看用戶信息">'+data+'</a>';
			}},
            {field: 'startShow', width:"130", title: '开始时间', align: 'center', sortable: false,formatter : function(data) {
				return data ? new Date(data * 1000)
				.Format("yyyy-MM-dd HH:mm:ss") : ""
				}},
			{field: 'endShow', width:"130", title: '结束时间', align: 'center', sortable: false,formatter : function(data) {
					return data ? new Date(data * 1000)
					.Format("yyyy-MM-dd HH:mm:ss") : ""
				}},
			{field: 'swi', width:"70", title: '状态', align: 'center', sortable: false,formatter : function(data){
				if(data==1){return "显示"}else if(data==0){return "不显示"}
			}},
            {field: 'edit', width:"90", title: '操作', align: 'center', sortable: false,formatter : function(data) {
            	return '<a href="javascript:getEdit();" class="easyui-tooltip" title="查看用戶信息">编辑</a>';
			}},
           
        ]]
    })
})
 
 function createAdv(){
	 $('#dlg').dialog('open').dialog('center').dialog('setTitle','新增广告');
     $('#fm').form('clear');
}

function getEdit(){
	var row = $('#dg').datagrid('getSelected');
    if (row){
        $('#edit').dialog('open').dialog('center').dialog('setTitle','编辑广告');
        $('#fn').form('clear');
        $('#fn').form('load',row);
    }
}

function editAdv(){
	var row = $('#dg').datagrid('getSelected');
    $('#fn').form('submit',{
    	url:'../carouselAdd?method=edit&id='+row.id,
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
                $('#edit').dialog('close');        
                $('#dg').datagrid('reload');
            }
        }
    });
}

function previewImage(file)
{
  var MAXWIDTH  = 260; 
  var MAXHEIGHT = 180;
  var div = document.getElementById('preview');
  if (file.files && file.files[0])
  {
      div.innerHTML ='<img id=imghead>';
      var img = document.getElementById('imghead');
      img.onload = function(){
        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
        img.width  =  rect.width;
        img.height =  rect.height;
//         img.style.marginLeft = rect.left+'px';
        img.style.marginTop = rect.top+'px';
      }
      var reader = new FileReader();
      reader.onload = function(evt){img.src = evt.target.result;}
      reader.readAsDataURL(file.files[0]);
  }
  else //兼容IE
  {
    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
    file.select();
    var src = document.selection.createRange().text;
    div.innerHTML = '<img id=imghead>';
    var img = document.getElementById('imghead');
    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
    div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
  }
}
function clacImgZoomParam( maxWidth, maxHeight, width, height ){
    var param = {top:0, left:0, width:width, height:height};
    if( width>maxWidth || height>maxHeight )
    {
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;
         
        if( rateWidth > rateHeight )
        {
            param.width =  maxWidth;
            param.height = Math.round(height / rateWidth);
        }else
        {
            param.width = Math.round(width / rateHeight);
            param.height = maxHeight;
        }
    }
     
    param.left = Math.round((maxWidth - param.width) / 2);
    param.top = Math.round((maxHeight - param.height) / 2);
    return param;
}

function saveAdv(){
    $('#fm').form('submit',{
        url:"../carouselAdd?method=add",
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
function del(){
	var row = $('#dg').datagrid('getSelected');
	$('#fn').form('submit',{
        url:"../carouselAdd?method=del&id="+row.id,
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
                $('#edit').dialog('close');        
                $('#dg').datagrid('reload');
            }
        }
    });
}
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>
	<div id="tb" style="padding: 2px 5px; height: auto">
		<div>
			<input type="button" value="创建轮播广告" onclick="createAdv()">
		</div>
	</div>
	
	<div id="dlg" class="easyui-dialog"
			style="width: 570px; height: 350px; padding: 10px 20px" closed="true"
			buttons="#dlg-buttons">
			<form id="fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>活动名</td>
						<td><input type="text" name="name" id="ownerid"
							class="easyui-textbox" required="true"
							data-options="disabled:false" /></td>
					</tr>
					<tr>
						<td>开始时间</td>
						<td>
							<input class="easyui-datetimebox" id="StartDate" name="StartDate" style="width:150px;height:24px">
						</td>
						<td>结束时间</td>
						<td>
							<input class="easyui-datetimebox" id="EndDate" name="EndDate" style="width:150px;height:24px">
						</td>
					</tr>
					<tr>
						<td>跳转地址</td>
						<td>
							<input type="text" name="jumpUrl" id="jumpUrl" class="easyui-textbox"
							data-options="disabled:false" />
						</td>
					</tr>
					<div id="preview">
    				<img id="imghead" width=100 height=100 border=0 src='../images/u90.png'>
					</div>
     				<input type="file" name="picUrl" onchange="previewImage(this)" />   
				</table>
			</form>
		</div>
		
		<div id="dlg-buttons">
			<a href="javascript:saveAdv()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:out()"
				class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
		</div>
		
		
		<div id="edit" class="easyui-dialog"
			style="width: 570px; height: 350px; padding: 10px 20px" closed="true"
			buttons="#edit-buttons">
			<form id="fn" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>活动名</td>
						<td><input type="text" name="name" id="ownerid"
							class="easyui-textbox" required="true"
							data-options="disabled:false" /></td>
					</tr>
					<tr>
						<td>开始时间</td>
						<td>
							<input class="easyui-datetimebox" id="StartDate" name="StartDate" style="width:150px;height:24px">
						</td>
						<td>结束时间</td>
						<td>
							<input class="easyui-datetimebox" id="EndDate" name="EndDate" style="width:150px;height:24px">
						</td>
					</tr>
					<tr>
						<td>跳转地址</td>
						<td>
							<input type="text" name="jumpUrl" id="jumpUrl" class="easyui-textbox"
							data-options="disabled:false" />
						</td>
					</tr>
					<div id="preview">
    				<img id="imghead" width=100 height=100 border=0 src='../images/u90.png'>
					</div>
     				<input type="file" name="picUrl" onchange="previewImage(this)" />   
				</table>
			</form>
		</div>
		
		<div id="edit-buttons">
			<a href="javascript:editAdv()" class="easyui-linkbutton"
				iconCls="icon-ok">保存</a> <a href="javascript:del()"
				class="easyui-linkbutton" iconCls="icon-cancel">删除</a>
		</div>
</body>
</html>