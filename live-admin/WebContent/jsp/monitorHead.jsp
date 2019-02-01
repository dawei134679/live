<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.tinypig.admin.util.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<title>直播监控-头牌</title>
	<script type="text/javascript" src="../js/jquery/jquery-1.11.2.min.js"></script>
	<link rel="stylesheet" type="text/css" href="../easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../easyui/themes/icon.css">
	<script type="text/javascript" src="../easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="../easyui/locale/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" href="../css/base.css">

</head>
<body>
	<div class="container">
	</div>
	<div id="dlg" class="easyui-dialog" style="width: 575px;height: 650px;" closed="true">
	</div>
	<script type="text/javascript">
		//获取直播用户列表
		var imgCount;
		function get_monitor_list(){
			$.get('../monitor?method=monitor&recommend=3',function(data){
				var index = Math.random();
				var htmlStr = "";
				if(data.length>0){
					imgCount = data.length;
					for(var i=0;i<data.length;i++){
						htmlStr += '<div class="item" id="item_'+data[i].uid+'">';
						htmlStr += '	<div class="img_box" >';
						htmlStr += '		<img id="img_'+i+'" src="'+data[i].videopic+'?v='+index+'" onclick="play_video('+data[i].uid+')">';
						htmlStr += '		<div class="user_info">';
						htmlStr += '			<span class="nickname">['+data[i].anchorlevel+'] '+data[i].nickname+'</span>';
						htmlStr += '			<span class="usetId">ID:'+data[i].uid+'</span>';
						htmlStr += '		</div>';
						htmlStr += '		<div class="active_box">';
						htmlStr += '			<div class="fenghao" onclick="fenghao('+data[i].uid+')">封号</div>';
						htmlStr += '			<div class="jinbo" onclick="jinbo('+data[i].uid+')">禁播</div>';
						htmlStr += '			<div class="guanbi" onclick="guanbi('+data[i].uid+')">关闭</div>';
						htmlStr += '		</div>';
						htmlStr += '	</div>';
						if(data[i].verified==true){
							htmlStr += '	<div class="status star"><span></span>认证</div>';
						}
						if(parseInt(data[i].report)>0){
							htmlStr += '	<div class="status jubao">举报 <count>'+data[i].report+'</count></div>';
						}
						htmlStr += '</div>';
					}
					$(".container").html(htmlStr);
					/* setInterval(reload_img,20000); */
				}
			},'json');
		}
		
		get_monitor_list();
		
		setInterval(get_monitor_list,10000);
		//截图刷新
		function reload_img(){
			var index = Math.random();
			var src;
			for(var i=0;i<imgCount;i++){
				src = $("#img_"+i).attr("src");
				src = src.substring(0,src.indexOf("?"))+"?v="+index;
				//console.log(src);
				$("#img_"+i).attr("src",src);
			}
			//console.log("---------------------");
		}

		//封号
		function fenghao(uid){

			$.messager.confirm("系统提示","确定要封号吗？",function(r){
				if(r){
					$.get("../monitor?method=block&uid="+uid+"&status=0",function(data){
						if(data.code == 200){
							$.messager.alert("系统提示","封号成功！");
		
							$("#item_"+uid).remove();
							return;
						}else{
							$.messager.alert("系统提示","封号失败！");
							return;
						}
					},'json')
				}
			})
		}

		//禁播
		function jinbo(uid){
			$.messager.confirm("系统提示","确定要禁播吗？",function(r){
				if(r){
					$.get("../monitor?method=ban&uid="+uid+"&status=2",function(data){
						console.log("data="+data.code);
						if(data.code == 200){
							$.messager.alert("系统提示","禁播成功！");
							$("#item_"+uid).remove();
							return;
						}else{
							$.messager.alert("系统提示","禁播失败！");
							return;
						}
					},'json')
				}
			})
		}

		//关闭
		function guanbi(uid){

			$.messager.confirm("系统提示","确定要关闭吗？",function(r){
				if(r){
					$.get("../monitor?method=close&uid="+uid+"&status=2",function(data){
						if(data.code == 200){
							$.messager.alert("系统提示","关播成功！");
							$("#item_"+uid).remove();
							return;
						}else{
							$.messager.alert("系统提示","关播失败！"+data.code);
							return;
						}
					},'json')
				}
			})
		};

		//打开视频
		function play_video(id){
			var hls = "<%=com.tinypig.admin.util.Constant.qn_liveCover_bucket_hls%>";
			var videourl = hls+"/"+id+".m3u8";
			var iframe = "<iframe src='ckPlayer.jsp?videourl="+videourl+"' style='border-width: 0px;width: 560px;height: 610px'></iframe>";
			 $('#dlg').html(iframe);
			$("#dlg").dialog("open").dialog('center').dialog("setTitle", "热门主播监控");;
		}

		$(document).on("mouseenter",".img_box",function(){
			$(this).find(".active_box").show();
		})
		$(document).on("mouseleave",".img_box",function(){
			$(this).find(".active_box").hide();
		})
	</script>
</body>
</html>