<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<title>直播监控-最新</title>
	<script type="text/javascript" src="../js/jquery/jquery-1.11.2.min.js"></script>
	<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
	<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" href="../css/base.css">
</head>
<body>
	<div>
				&nbsp;表名：&nbsp;<input type="text" name="table" id="table" size="2" />&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="javascript:get_monitor_list()" class="easyui-linkbutton"
					iconCls="icon-search">刷新</a>
			</div>
	<div class="container">
	</div>
	<script type="text/javascript">
		var sh;
		var page = 1;
		var table = 0;
		//获取直播用户列表
		var imgCount;
		function get_monitor_list(){
			var tb = $("#table").val();
			if(table != tb){
				page = 1;
			}
			table = tb;
			
			$.get('../monitor?method=test&table='+tb+'&page='+page,function(data){
				var index = Math.random();
				var htmlStr = "";
				if(data.length>0){
					imgCount = data.length;
					for(var i=0;i<data.length;i++){
						htmlStr += '<div class="item" id="item_'+data[i].uid+'">';
						htmlStr += '	<div class="img_box" >';
						htmlStr += '		<img id="img_'+i+'" src="'+data[i].headimage+'" >';
						htmlStr += '		<div class="user_info">';
						htmlStr += '			<span class="nickname">['+data[i].nickname+'</span>';
						htmlStr += '			<span class="usetId">ID:'+data[i].uid+'</span>';
						htmlStr += '		</div>';
						htmlStr += '	</div>';
						htmlStr += '</div>';
					}
					$(".container").html(htmlStr);
					if(imgCount < 100){
						
						clearInterval(sh);

						alert('table：'+$("#table").val()+' 循环结束 共 '+page+' 页');
					}
					page++;
				}
				
			},'json');
		}
		//sh = setInterval(get_monitor_list,120000);
	</script>
</body>
</html>