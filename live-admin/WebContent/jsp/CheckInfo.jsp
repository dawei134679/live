<%@ page language="java" import="java.sql.*,com.tinypig.admin.*,com.tinypig.admin.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>身份认证审核</title>
<script type="text/javascript" src="../js/jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="../js/jquery/UrlParm.js"></script>
<style type="text/css">
	body{
		font-family:"Open Sans","Helvetica Neue",Helvetica,Arial,STHeiti,"Microsoft Yahei",sans-serif;
		font-size: 14px;
	}
	.container{
		width: 760px;
	}
	table{float: left; margin-left: 10px; box-shadow: 0px 1px 4px #b8b8b8; padding: 10px;}
	.bg_color{background: #e0ecff;}
	td{height: 30px; /*background-color: #f6f9fa;*/ margin: 0; padding: 0px; padding-left: 10px;}
	.pic{
		width: 370px;
		height: 280px;
		background-color: #fff;
		float: left;
		margin-left: 10px;
		margin-bottom: 10px;
		box-shadow: 0px 1px 4px #b8b8b8;
		overflow: hidden;
		text-align: center;
	}
	.pic img{
		width: 100%;
		height: 100%;
	}
	.active_box div,.reject_reason div{
		width: 100px;
		height: 30px;
		line-height: 30px;
		color: #fff;
		text-align: center;
		float: right;
		cursor: pointer;
		border-radius: 5px;
	}
	.reject_reason div{
		width: 70px;
	}
	.reject,.cancel{
		background-color: #ea6555;
	}
	.reject:hover,.cancel:hover{
		background-color: #b03d30;
	}
	.reject{
		margin-right: 10px;
	}
	.adopt,.confirm{
		background-color: #88c067;
	}
	.adopt:hover,.confirm:hover{
		background-color: #4d9324;
	}
	.clear{
		clear: both;
	}
	.reject_reason{
		width: 300px;
		height: 140px;
		background: #fff;
		box-shadow: 0px 1px 20px #9e9e9e;
		position: absolute;
		z-index: 100;
		display: none;

	}
	.warning{
		width: 300px;
		height: 140px;
		background: #fff;
		box-shadow: 0px 1px 20px #9e9e9e;
		position: absolute;
		z-index: 100;
		display: none;

	}
	.reject_reason *{
		margin-left: 9px;
	}
	.warning *{
		margin-left: 9px;
	}
	#reason{
		height: 30px;
		width: 280px;
		border: 1px solid #9e9e9e;
		font-family:"Open Sans","Helvetica Neue",Helvetica,Arial,STHeiti,"Microsoft Yahei",sans-serif;
	}
	h2{
		font-weight: normal;
		font-size: 15px;
	}
	.reject_reason div{
		margin-top: 15px;
	}
	.warning div{
		margin-top: 15px;
	}
	.confirm{
		margin-right: 10px;
	}
	:-moz-placeholder { /* Mozilla Firefox 4 to 18 */
	    color: #ea6555;
	}

	::-moz-placeholder { /* Mozilla Firefox 19+ */
	    color: #ea6555;
	}

	input:-ms-input-placeholder,
	textarea:-ms-input-placeholder {
	    color: #ea6555;
	}

	input::-webkit-input-placeholder,
	textarea::-webkit-input-placeholder {
	    color: #ea6555;
	}
	</style>
</head>
<body>
<div class="warning" style="width:200px;height:110px;position:absolute;left:50%;margin-left:-250px;top:50%;margin-top:-190px;">
	<h1>请选择原因</h1>
	<input type="button" onclick="out();" value="确定" >
</div>
<div class="reject_reason">
		<h2>驳回原因：</h2>
		<input name="reason" type="checkbox" id="reason1" value="所填信息与照片信息不符" />所填信息与照片信息不符<br/>
		<input name="reason" type="checkbox" id="reason2"  value="证件照与本人不符" />证件照与本人不符<br/>
		<input name="reason" type="checkbox" id="reason3"  value="证件号不正确" />证件号不正确<br/>
		<input name="reason" type="checkbox" id="reason4"  value="证件照不清晰 " />证件照不清晰 
		<div class="confirm">确定</div>
		<div class="cancel">取消</div>
	</div>
	<div class="container">
		<table width="370px" height="285px" border="0" cellspacing="0" cellpadding="0">
			<tr class="bg_color">
				<td width="30%">用户id</td>
				<td id="uid"></td>
			</tr>
			<tr>
				<td>姓名</td>
				<td id="uname"></td>
			</tr>
			<tr class="bg_color">
				<td>身份证号</td>
				<td id="idnum"></td>
			</tr>
			<tr>
				<td>手机号</td>
				<td id="phone"></td>
			</tr>
			<tr class="bg_color">
				<td>银行卡号</td>
				<td id="bank"></td>
			</tr>
			<tr>
				<td>开户行</td>
				<td id="position"></td>
			</tr>
			<tr class="bg_color">
				<td>支行信息</td>
				<td id="trunk"></td>
			</tr>
		</table>
		<div class="pic">
			<img id="id_card1" src="">
		</div>
		<div class="pic">
			<img id="id_card2" src="">
		</div>
		<div class="pic">
			<img id="id_card3" src="">
		</div>
		<div class="clear"></div>
		<div class="active_box">
			<div class="adopt">通 过</div>
			<div class="reject">驳 回</div>
		</div>
	</div>
	<script type="text/javascript">
	 function out(){
		 $(".warning").fadeOut();
	 }
		var uid = UrlParm.parm("uid");
		//用户信息初始化
		function init_user_info(){
			$.get('../CheckUserInfo',{uid:uid},function(data){
				if(data.rows.length==1){
					var user_info = data.rows[0];
					$("#id_card1").attr("src",user_info.handImage);
					$("#id_card2").attr("src",user_info.negativeImage);
					$("#id_card3").attr("src",user_info.positiveImage);
					$("#uid").text(user_info.uid);
					$("#uname").text(user_info.realName);
					$("#idnum").text(user_info.cardId);
					$("#phone").text(data.phone);
					$("#bank").text(user_info.cardNo);
					$("#position").text(user_info.bankAccount);
					$("#trunk").text(user_info.branchBank);
				}
			},'json');
		}
		init_user_info();
		
		$("#id_card1").on("click",function(){
			window.open($("#id_card1").attr("src"),"_blank","height=600,width=600");
		})
		$("#id_card2").on("click",function(){
			window.open($("#id_card2").attr("src"),"_blank","height=600,width=600");
		})
		$("#id_card3").on("click",function(){
			window.open($("#id_card3").attr("src"),"_blank","height=600,width=600");
		})

		//通过
		$(".adopt").click(function(){
			$.post('../CheckUserPass',{uid:uid},function(data){
				if(data.rows==1){
					console.log("通过");
				}
			},'json');
			parent.$('#dlg').dialog('close');
			parent.location.reload();
		})
		//驳回
		$(".reject").click(function(){
			var offset = $(this).offset();
			$(".reject_reason").css({left:offset.left-90,top:offset.top-110});
			$(".reject_reason").fadeIn();
		})
		//确定驳回
		$(".confirm").click(function(){
			var str=document.getElementsByName("reason");
			var objarray=str.length;
			var reason="";
			for (i=0;i<objarray;i++)
			{//牛图库JS特效，http://js.niutuku.com/
			  if(str[i].checked == true)
			  {
				 reason+=str[i].value+",";
			  }
			}
			if(reason==""){
				$(".warning").fadeIn();
				return;
			}
			reason=reason.substring(0,reason.length-1);
			$.post('../CheckUserReject?uid='+uid+'&reason='+encodeURI(reason),function(data){
				if(data.rows==1){
					console.log("驳回");
				}
			},'json');
			$(".reject_reason").fadeOut();
			//opener.location.reload();
			//window.close();
			parent.$('#dlg').dialog('close');
			parent.location.reload();
		})
		//取消驳回
		$(".cancel").click(function(){
			$(".warning").fadeOut();
			$(".reject_reason").fadeOut();
			$("#reason").val("");
			$("#reason").attr("placeholder","");
		})
	</script>
</body>
</html>