<%@ page language="java" contentType="text/html; charset=utf-8"
	import="java.util.*"
	pageEncoding="utf-8"%>
<%@page import="com.tinypig.admin.model.AdminUserModel"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>麦芽管理后台</title>
<%
	// 权限验证
	int uid = 0;
	 if (session.getAttribute("currentUser") == null) {
		response.sendRedirect("index.jsp");
		return;
	 }else{
		 AdminUserModel AdminUserModel = (AdminUserModel)session.getAttribute("currentUser");
		 uid = AdminUserModel.getUid();
	 }
%>
<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
<script type="text/javascript" src="easyui/jquery.min.js"></script>
<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/md5.js"></script>
<script type="text/javascript">
	$(function() {
		getMenuTree();
		
		$(".easyui-tabs").tabs({
	        fit : true,
	        border : false,
	        //为其附加鼠标右键事件
	        onContextMenu:function(e){           
	        	 e.preventDefault();    
	            $('#rcmenu').menu('show', {    
	                left: e.pageX,    
	                top: e.pageY
	            })
	            .css({width:'150px'})
	            .next('.menu-shadow')
	            .css({width:'150px'});
	        }
        });
		$("#mm-refresh").click(function () {
            var currTab = $('#tabs').tabs('getSelected');    //获取选中的标签项
            var url = $(currTab.panel('options').content).attr('src');    //获取该选项卡中内容标签（iframe）的 src 属性 
            if(url==null||url==""){
            	return;
            }
            $('#tabs').tabs('update', {
                tab: currTab,
                options: {
                    content: '<iframe frameborder="0"  src="' + url + '"  style="height: 100%; width: 100%;" ></iframe>'//如果用herf,容易导致样式与主页面重载,导致页面奔溃.
                }
            })
        });
        $("#mm-tabclose").bind("click",function(){    
        	 var currTab = $('#tabs').tabs('getSelected');
             currTitle = currTab.panel('options').title;
             if("首页"==currTitle){
            	 return;
             }
             $('#tabs').tabs('close', currTitle);
        });
	        //关闭其他页面（先关闭右侧，再关闭左侧）    
	        $("#mm-tabcloseother").bind("click",function(){    
	            var tablist = $('#tabs').tabs('tabs');    
	            var tab = $('#tabs').tabs('getSelected');    
	            var index = $('#tabs').tabs('getTabIndex',tab);    
	            for(var i=tablist.length-1;i>index;i--){    
	                $('#tabs').tabs('close',i);    
	            }    
	            var num = index-1;    
	            if(num < 1){    
	                return;    
	            }else{    
	                for(var i=num;i>=1;i--){    
	                    $('#tabs').tabs('close',i);    
	                }    
	                $("#tabs").tabs("select", 1);    
	            }    
	        });
	      	//关闭所有标签页    
	        $("#mm-tabcloseall").bind("click",function(){    
	            var tablist =$('#tabs').tabs('tabs');  
	            for(var i=tablist.length-1;i>=1;i--){    
	                $('#tabs').tabs('close',i);    
	            }    
	        });
	      //关闭左边的选项卡  
	        $("#mm-tabcloseleft").bind("click",function(){    
	            var tablist = $('#tabs').tabs('tabs');    
	            var tab = $('#tabs').tabs('getSelected');    
	            var index = $('#tabs').tabs('getTabIndex',tab);    
	            var num = index-1;    
	            if(num < 1){    
	                return;    
	            }else{    
	                for(var i=num;i>=1;i--){    
	                    $('#tabs').tabs('close',i);    
	                }    
	                $("#tabs").tabs("select", 1);    
	            }     
	        }); 
	        //关闭右边的选项卡  
	        $("#mm-tabcloseright").bind("click",function(){    
	            var tablist = $('#tabs').tabs('tabs');    
	            var tab = $('#tabs').tabs('getSelected');    
	            var index = $('#tabs').tabs('getTabIndex',tab);    
	            for(var i=tablist.length-1;i>index;i--){    
	                $('#tabs').tabs('close',i);    
	            }    
	        });    
	});
	
	// 新增Tab
	function openTab(text, url) {
		if ($("#tabs").tabs('exists', text)) {
			$("#tabs").tabs('select', text);
		} else {
			var content = "<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%' src="
					+ url + "></iframe>";
			$("#tabs").tabs('add', {
				title : text,
				closable : true,
				content : content
			});
		}
	}
	
	//获取菜单树
	function getMenuTree(){
		// 数据
		$.ajax({
            type: "GET",
            url: "adminMenu",
            data: {uid:<%=uid%>, method:"getMenuTree"},
            dataType: "json",
            success: function(data){
            	// 实例化树菜单
        		$("#tree").tree({
        			data : data,
        			lines : true,
        			onClick : function(node) {
        				if (node.attributes) {
        					openTab(node.text, node.attributes.url);
        				}
        			}
        		});
            }
        });
	}
	
	function quitLogin(){
		$.messager.confirm("系统提示","确定退出?",function(r){
			if(r){
				$.ajax({
		             type: "post",
		             dataType: "json",
		             url: "login/quit",
		             dataType: 'json', 
		             success: function(data) {
		             	if(data.errMsg){
							$.messager.alert("系统提示", data.errMsg);
		             	}else{
		             		top.location.href = "index.jsp"
		             	}
		             }
		       	});
			}
		})
	}
	
	function editPwd(){
		$('#editpwd-dlg').dialog('open').dialog('center').dialog('setTitle','修改密码');
        $("#editpwd-fm").form("clear");
        url = 'login/editPwd';
	}
	
	function saveNewPwd(){
		$.messager.confirm("系统提示","确认提交吗？",function(r){
			if(r){
				$("#editpwd-fm").form("submit", {
					url : url,
					onSubmit : function() {
						return $(this).form("validate");
					},
					success : function(data) {
						var result = eval("("+data+")");
						if (result.success == 200) {
							$.messager.alert("系统提示", result.msg);
							$("#editpwd-fm").form("clear");
							$("#editpwd-dlg").dialog("close");
						}else {
							$.messager.alert("系统提示", result.msg);
						}
					}
				});
			}
		})
	}
</script>
</head>
<body class="easyui-layout">
	<div region="north" style="height: 80px; background-color: #E0EDFF">
		<div align="left" style="width: 80%; float: left">
			<img src="images/main.jpg">
		</div>
		<div style="padding-top: 50px; padding-right: 20px;">
			当前用户：&nbsp;<font color="red">${currentUser.username }</font>&nbsp;&nbsp;
			<a href="javascript:editPwd()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改密码</a>
			&nbsp;&nbsp;
			<a href="javascript:quitLogin()" class="easyui-linkbutton" iconCls="icon-search" plain="true">退出登录</a>
		</div>
		<!-- <div style="padding-top: 50px; padding-right: 20px;">
		</div> -->
	</div>
	<div region="center">
		<div class="easyui-tabs" fit="true" border="false" id="tabs">
			<div title="首页">
				<div align="center" style="padding-top: 100px;">
					<font color="red" size="10">欢迎使用</font>
				</div>
			</div>
		</div>
	</div>
	<div region="west" style="width: 150px;" title="导航菜单" split="true">
		<ul id="tree"></ul>
	</div>
	<div region="south" style="height: 25px;" align="center">
		Copyright © 版权所有 汇坤科技
	</div>
	
	<!-- 修改密码 -->
	<div id="editpwd-dlg" class="easyui-dialog" style="width:350px; height: 190px; padding: 10px 20px" closed="true" buttons="#editpwd-dlg-buttons">
			<form id="editpwd-fm" method="post">
				<table cellspacing="5px;">
					<tr>
						<td>旧密码</td>
						<td>
							<input type="hidden" value="" id="passwordOld_cache"/>
							<input type="hidden" value="" id="passwordNew_cache"/>
							<input type="hidden" value="" id="passwordNew2_cache"/>
							<input type="password" id="passwordOld" name="passwordOld" class="easyui-textbox" required="true" data-options="validType:'length[0,32]'" />
							<span style="color:red;">*</span>
						</td>
					</tr>
					<tr>
						<td>新密码</td>
						<td>
							<input type="password" id="passwordNew" name="passwordNew" class="easyui-textbox" required="true" data-options="validType:'length[0,32]',missingMessage:'新密码0-32位'"/>
							<span style="color:red;">*</span>
						</td>
					</tr>
					<tr>
						<td>确认新密码</td>
						<td>
							<input type="password" id="passwordNew2" name="passwordNew2" class="easyui-textbox" required="true" data-options="validType:'equals[\'#passwordNew\']',missingMessage:'与新密码一致'"/>
							<span style="color:red;">*</span>
						</td>
					</tr>
				</table>
			</form>
		</div>

		<div id="editpwd-dlg-buttons">
			<a href="javascript:saveNewPwd()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
		</div>
	
	
	<!-- 菜单右键 -->
    <div id="rcmenu" class="easyui-menu">
    	<div id="mm-refresh">刷新页面</div>
        <div id="mm-tabclose">关闭当前</div>
        <div id="mm-tabcloseother">关闭其它</div>
        <div id="mm-tabcloseall">全部关闭</div>
        <div class="menu-sep"></div>
        <div id="mm-tabcloseleft">当前页左侧全部关闭</div> 
        <div id="mm-tabcloseright">当前页右侧全部关闭</div>
    </div>
</body>
</html>
