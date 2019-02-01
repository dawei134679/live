<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>ckplayer播放</title>
<script type="text/javascript" src="../js/jquery/jquery-1.11.2.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../easyui/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="../css/base.css">
<script type="text/javascript" src="../js/ckplayer/ckplayer/ckplayer.js"></script>
</head>
<body>
	<div id="video" style="width: 550px; height: 600px; text-align: center;"></div>
	<script type="text/javascript">
	var player = null;
	$(function() {
		var videoUrl = "${param.videourl}";
		var videoObject = {
					container: '#video',//“#”代表容器的ID，“.”或“”代表容器的class
					variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象
					//loaded: 'loadedHandler', //当播放器加载后执行的函数
					flashplayer:false,//如果强制使用flashplayer则设置成true
					autoplay: true,
					video:{
					    file:videoUrl,//视频地址
					    type:'video/m3u8'
					}
				};
		var player=new ckplayer(videoObject);
	});
	</script>
</body>
</html>
