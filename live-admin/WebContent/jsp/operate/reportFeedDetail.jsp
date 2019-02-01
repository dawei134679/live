<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="html" uri="/WEB-INF/auth.tld"%>
<html>
<head>
<title>会员详情</title>
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../../easyui/themes/icon.css">
<script type="text/javascript" src="../../easyui/jquery.min.js"></script>
<script type="text/javascript" src="../../easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="../../easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../../js/common.js"></script>
<style type="text/css">
.table {font-size: 14px;border-collapse: collapse;border: none;width:100%;}
td{border: solid #C0C0C0 1px;padding: 5px;}
.label{background-color: #DCDCDC;width: 100px;height: 30px;}
.active_box div{width: 100px;height: 30px;line-height: 30px;color: #fff;text-align: center;float: right;cursor: pointer;border-radius: 5px;margin-left: 10px}
.adopt{background-color: #88c067;}
.adopt:hover{background-color: #4d9324;}
.reject{background-color: #ea6555;}
.reject:hover{background-color: #b03d30;}
</style>
<script>
	function formatter(data){
		var date = new Date();  
	    date.setTime(data * 1000);  
	    var y = date.getFullYear();      
	    var m = date.getMonth() + 1;      
	    m = m < 10 ? ('0' + m) : m;      
	    var d = date.getDate();      
	    d = d < 10 ? ('0' + d) : d;      
	    var h = date.getHours();    
	    h = h < 10 ? ('0' + h) : h;    
	    var minute = date.getMinutes();    
	    var second = date.getSeconds();    
	    minute = minute < 10 ? ('0' + minute) : minute;      
	    second = second < 10 ? ('0' + second) : second;     
	    return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
	}
	//处理
	function chuliFeed(status,userFeedId){
		var ary = ['',"忽略","删除"];
		$.messager.confirm("系统提示", "确定要【"+ary[status]+"】吗，数据不可恢复?", function(r) {
			if (r) {
				$.ajax({
					url:"${pageContext.request.contextPath}/reportFeed/chuliFeed",
					data:{id:'${param.id}',status:status,userFeedId:userFeedId},
					success:function(res){
						var d = eval("(" + res + ")");
						if(d.success){
							$.messager.alert("系统提示", "处理成功");
							parent.$('#dlg').dialog('close');
							parent.$("#feedReportList").datagrid("reload");
						}else{
							$.messager.alert("系统提示", d.msg);
						}
					}
				});
			}
		});
	}
	$(function() {
		$.ajax({
			url:"${pageContext.request.contextPath}/reportFeed/detail",
			data:{id:'${param.id}'},
			success:function(res){
				var d = eval("(" + res + ")");
				if(d.success){
					var reportFeed = d.reportFeed;
					var reportFeedUsers = d.reportFeedUsers;
					var userFeed = d.userFeed;
					var html = [];
					html.push('<table class="table">');
					html.push('<tr>');
						html.push('<td class="label">被举报用户：</td>');
						html.push('<td>');
							html.push('<div class="uid">'+reportFeed.reportUid+'</div>');
						html.push('</td>');
					html.push('</tr>');
					html.push('<tr>');
					html.push('<td class="label">举报时间：</td>');
						html.push('<td>');
							html.push('<div class="date">'+formatter(reportFeed.createAt)+'</div>');
						html.push('</td>');
					html.push('</tr>');
					html.push('<tr>');
						html.push('<td class="label">动态内容：</td>');
						html.push('<td>');
							html.push('<div class="content">'+userFeed.content+'</div>');
						html.push('</td>');
					html.push('</tr>');
					html.push('<tr>');
						html.push('<td class="label">动态图片：</td>');
						html.push('<td>');
							html.push('<div class="imgs">');
							if(userFeed.imgs){
								var imgs = userFeed.imgs.split(";");
								for(var i=0,len=imgs.length;i<len;i++){
									html.push('<img src="'+imgs[i]+'" width="33%"/>');
								}
							}
							html.push('</div>');
						html.push('</td>');
					html.push('</tr>');
					
					html.push('<tr>');
					html.push('<td class="label">举报用户列表：</td>');
					html.push('<td>');
					if(reportFeedUsers){
						html.push('<table class="table">');
						html.push('<tr>');
						html.push('<td class="label">举报原因</td>');
						html.push('<td class="label">举报人</td>');
						html.push('<td class="label">举报时间</td>');
						html.push('<td class="label">状态</td>');
						html.push('</tr>');
						var ary = ['未处理','忽略','删除'];
						for(var k=0,len=reportFeedUsers.length;k<len;k++){
							var reportFeedUser = reportFeedUsers[k];
							html.push('<tr>');
							html.push('<td>'+reportFeedUser.reportReason+'</td>');
							html.push('<td>'+reportFeedUser.dstuid+'</td>');
							html.push('<td>'+formatter(reportFeedUser.dstAt)+'</td>');
							html.push('<td>'+ary[reportFeedUser.status]+'</td>');
							html.push('</tr>');
						}
						html.push('</table>');
					}
					html.push('</td>');
					html.push('</tr>');
					if(reportFeed.status == 0){
						html.push('<tr>');
							html.push('<td colspan="2">');
								html.push('<div class="active_box"><div class="adopt" onclick="chuliFeed(1,'+userFeed.id+');">忽 略</div><div class="reject" onclick="chuliFeed(2,'+userFeed.id+');">删 除</div></div>');
							html.push('</td>');
						html.push('</tr>');
					}
					html.push('</table>');
					$("#center").html(html.join(''));
				}
			}
		});
	});
</script>
</head>
<body style="margin: 5px;">
	<div align="center" id="center"></div>
</body>
</html>