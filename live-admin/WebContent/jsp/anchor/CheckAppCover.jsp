<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<title>主播APP封面审核</title>
<%
	String base = request.getContextPath();
%>
<link rel="stylesheet" type="text/css" href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<script type="text/javascript">
	var url;
	//条件查询
	function searchAnchor() {
		$('#dg').datagrid('load', {
			anchoruid : $('#anchoruid').val(),
			//unionid : $('#unionid').combobox("getValue"),
			status : $('#status').combobox("getValue")
		});
	}
</script>
<script type="text/javascript">
	var dataGrid;
	
	$(function () {
	
	    dataGrid = $("#dg").datagrid({
	        url: '../../anchor/getNoUnionUserCoverList',
	        width: getWidth(0.97),
	        height: getHeight(0.97),
			queryParams: {
				anchoruid : $('#anchoruid').val(),
				//unionid : $('#unionid').combobox("getValue"),
				status : $('#status').combobox("getValue")
			},
	        title: 'APP端主播封面审核',
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
	            {field: 'cb', checkbox:'true', align: 'center', sortable: false},
	            /**
	            {field: 'id', width:"80", title: '编号', align: 'center', sortable: true},
	            {field: 'headimage', width:"80", title: '头像', align: 'center',sortable: true,formatter:function (data) {
	        		return "<img src='"+data+"' width='80px' height='80px'/>";
	        	}},
	            {field: 'unionid', width:"200", hidden:true, title: '家族Id', align: 'center', sortable: false},
	            {field: 'unionname', width:"100", title: '家族', align: 'center', sortable: false},
	            **/
	            {field: 'uid', width:"90", title: 'UID', align: 'center', sortable: false},
	            {field: 'nickname', width:"120", title: '昵称', align: 'center', sortable: false},
	            /**
	            {field: 'anchorLevel', width:"70", title: '主播等级', align: 'center', sortable: false},
	            {field: 'recommend', width:"100", title: '房间级别', align: 'center', sortable: false,formatter:function(data){
            		if(data == 0){
            			return "普通";
            		}else if(data == 1){
            			return "最新";
            		}else if(data == 2){
            			return "热门";
            		}else{
            			return "未知";
            		}
	            }},
	            **/
	            {field: 'picCover', width:"150", title: '手机封面', align: 'center', sortable: false,formatter:function (data) {
	        		return "<img src='"+data+"' width='120px' height='120px'/>";
	        	}},
	        	/**
	            {field: 'picCover1', width:"150", title: 'PC封面(4:3)', align: 'center', sortable: false,formatter:function (data) {
        			return "<img src='"+data+"' width='120px' height='90px'/>";
	        	}},
	            {field: 'picCover2', width:"165", title: 'PC封面(16:9)', align: 'center', sortable: false,formatter:function (data) {
        			return "<img src='"+data+"' width='160px' height='90px'/>";
	        	}},
	        	**/
	            {field: 'status', width:"165", title: '状态', align: 'center', sortable: false,formatter:function (data) {
        			if(data == 0){
            			return "待审核";
            		}else if(data == 1){
            			return "审核通过";
            		}else if(data == 2){
            			return "驳回";
            		}
	        	}},
	            {field: 'cause', width:"165", title: '驳回原因', align: 'center', sortable: false},
	            {field: '操作', width:"165", title: '审核', align: 'center', sortable: false,formatter:function (value,row,index) {
            		if(row.status==0){
            			return "<a href='#' onclick=\"javascript:allow()\">通过</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick=\"javascript:reject("+40+")\">驳回</a>";
            		}else{
            			return "";
            		}
	        	}}
	        ]]
	    })
	})
	var _basepath = "<%=base%>";
	//审核通过
	function allow(){
		$.messager.confirm('确认',"通过吗？",function(r){
            if (r){
				//获取选中行数据
				var row = $("#dg").datagrid("getSelected");
				$.ajax({
					type: "post",
					dataType: "json",
					url: _basepath+"/anchor/allow",
					data: {id:row.id,uid:row.uid,picCover:row.picCover,picCover1:row.picCover1,picCover2:row.picCover2},		//参数列表：主播编号、封面地址、4:3封面地址、16:9封面地址
					success: function(data) {
						var result = eval("("+data+")");
						$.messager.alert("系统提示", "审核成功");
						//重新刷新表格
						$("#dg").datagrid("reload");
		           }
		       });
            }
        });
	}
	//审核驳回
	function reject(){
		$.messager.confirm('确认',"驳回吗？",function(r){
            if (r){
				//获取选中行数据
				var row = $("#dg").datagrid("getSelected");
				console.info(row);
				var remark = prompt("驳回理由：","");
				if(remark){
					$.ajax({
		                type: "post",
		                dataType: "json",
		                url: _basepath+"/anchor/reject",
		                data: {id:row.id,picCover:row.picCover,picCover1:row.picCover1,picCover2:row.picCover2,cause:remark},		//参数列表：主播编号、封面地址、4:3封面地址、16:9封面地址、驳回理由
		                success: function(data) {
			                var result = eval("("+data+")");
							$.messager.alert("系统提示", "审核驳回成功");
							//重新刷新表格
							$("#dg").datagrid("reload");
		                }
		            });
				}
            }
        });
	}
</script>
</head>
<body style="margin: 5px;">
	<table id="dg"></table>

	<div id="tb">
		<div>
			<!-- 
			&nbsp;选择公会:<input id=unionid name="unionid" class="easyui-combobox" required="true"
							data-options="valueField: 'unionid',
    									textField: 'unionname',
    									url: '../../unionAnchor/getUnionNameList'"
    						/>
			 -->
			&nbsp;主播ID：&nbsp;<input type="text" name="anchoruid" id="anchoruid" size="10" />&nbsp;&nbsp;
			&nbsp;&nbsp;审核状态:<select id="status" class="easyui-combobox" name="status" style="width:200px;">
							   <option value="0">待审核</option>
							   <option value="1">通过</option>
							   <option value="2">驳回</option>
							</select>
			&nbsp;&nbsp;<a href="javascript:searchAnchor()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
		</div>
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
                        imgObjPreview.style.width = '400px';
                        imgObjPreview.style.height = '400px';
                        //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式 
                        imgObjPreview.src = window.URL.createObjectURL(obj.files[0]);
                    }
                    else {
                        //IE下，使用滤镜 
                        obj.select();
                        var imgSrc = document.selection.createRange().text;
                        //必须设置初始大小 
                        localImagId.style.width = "400px";
                        localImagId.style.height = "400px";
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