<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
 <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>麦芽运营管理系统-登录</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css"/>
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css"/>
	<link rel="stylesheet" type="text/css" href="css/login.css" />
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/md5.js"></script>
	<script type="text/javascript">
		$(function(){
			$("#userName").focus();
			$('#password').keydown(function (e) {
	            if (e.keyCode == 13) {
	                login();
	            }
	        });
		});
		function login() {
			var uname = $("#userName").val();
			if(uname == ""){
				$.messager.alert("系统提示","请输入用户名");
				return ;
			}
			var pword = $("#password").val();
			if(pword == ""){
				$.messager.alert("系统提示","请输入密码");
				return ;
			}
			$("#password").val(md5($("#password").val()));
			$("#fm").form("submit", {
				onSubmit : function() {
					return $(this).form("validate");
				},
				success : function(data) {
					var result = eval("("+data+")");
					if (result.success == 200 ) {
						window.location.href = result.msg;
					}else {
						$("#password").val("");
						$.messager.alert("系统提示", result.msg);
					}
				}
			});
		}
		function resetValue(){
			$("#userName").val("");
			$("#password").val("");
		}
		 function focusNextInput(thisInput){
	          var inputs = document.getElementsByTagName("input");
	          for(var i = 0;i<inputs.length;i++){
	            // 如果是最后一个，则焦点回到第一个
	            if(i==(inputs.length-1)){
	              inputs[0].focus();
	              break;
	            }else if(thisInput == inputs[i]){
	              inputs[i+1].focus();
	              break;
	            }
	          }
	      }  
	</script>
</head>
 <body>
 	<div class="second_body">
    	<form id="fm" action="login/login" method="post">
        	<div class="logo"><img src="images/logo.png" style="height: 200px;width: 170px;" /></div>
            <div class="title-zh">麦芽运营管理系统</div>
            <div class="title-en" style="">MaiYa Operate Manage System</div>
            <div class="message"></div>
            <table border="0" style="width:300px;">
           		 <tr>
                	<td style="white-space:nowrap; padding-bottom: 5px;width:55px;font-size: 16px;">用户名：</td>
                    <td colspan="2">
                    	<input type="text" id="userName" name="userName" class="login" autocomplete="off" onkeypress="if(event.keyCode==13) focusNextInput(this);"/>
                    </td>
                </tr>
                <tr>
                    <td class="lable" style="white-space:nowrap;vertical-align: middle;font-size: 16px;">密&nbsp;&nbsp;&nbsp;码：</td>
                    <td colspan="2">
                    	<input type="password" id="password" name="password" class="login"/>
                    </td>
                </tr>
                <tr>
                	<td height="15px"></td>
                 </tr>
                <tr>
                    <td colspan="3" style="text-align:center">
                        <input type="button" value="登录" class="login_button" onclick="login()"/>
                        <input type="button" value="重置" class="reset_botton" onclick="resetValue()"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
 </body>
</html>       