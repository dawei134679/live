<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<%@ include file="../common/common.jsp"%>
<%-- 校验引入 --%>
<%@ include file="../common/validator.jsp"%>
<title>添加/修改</title>
</head>
<body>
	<%@ include file="../common/top.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/left.jsp"%>
			<!---右侧--->
			<div class="col-sm-<%=defcol%> main r-pad">
<!-- 				<ol class="breadcrumb"> -->
<!-- 				  <li><a href="#">首页</a></li> -->
<!-- 				  <li><a href="#">小猪认证</a></li> -->
<!-- 				  <li class="active">认证信息</li> -->
<!-- 				  <li class="returnbutton"><a href="javascript:history.back(-1)"><button type="button" class="btn btn-success btn-xs" id="resetBtn">返&nbsp;&nbsp;回</button></a></li> -->
<!-- 				</ol> -->
				<div class="panel panel-default">
					<div class="panel-heading" align="left">用户&nbsp${auth.uid}&nbsp的个人信息
					<li class="returnbutton"><a href="javascript:history.back(-1)"><button type="button" class="btn btn-success btn-xs" id="resetBtn">返&nbsp;&nbsp;回</button></a></li>
					</div>
					
					<table class="table">
						<tr>
							<td style='width:20%'><label>用户uid:</label></td>
							<td>${auth.uid}</td>
						</tr>
						<tr>
							<td><label>用户昵称:</label></td>
							<td>${auth.nickname}</td>
						</tr>
						<tr>
							<td><label>审核内容:</label></td>
							<td>${auth.authcontent}</td>
						</tr>
						<tr>
							<td><label>驳回原因:</label></td>
							<td>${auth.cause}</td>
						</tr>
						<tr>
							<td><label>状态:</label></td>
							<td>
								<c:if test="${auth.status==1}">待审核</c:if>
								<c:if test="${auth.status==2}">已驳回</c:if>
								<c:if test="${auth.status==3}">已通过</c:if>
							</td>
						</tr>
						<tr>
							<td><label>创建时间:</label></td>
							<td id="createat">
							</td>
						</tr>
						<tr>
							<td><label>审核时间:</label></td>
							<td id="auditat"></td>
						</tr>
						<tr>
							<td><label>认证url:</label></td>
							<td>
							    <ol>
      								<c:forEach items="${auth.authurls}" var="url">
										<li><a href="${url}" target="_blank">${url}</a></li>
									</c:forEach>
    							</ol>
							</td>
						</tr>
						<tr>
							<td><label>认证图片:</label></td>
							<td>
								<ol>
      								<c:forEach items="${auth.authpics}" var="pic">
										<li><img src="${pic}" style='width:80%'/></li>
										<br>
									</c:forEach>
    							</ol>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!---右侧结束--->
		</div>
		<%@ include file="../common/footer.jsp"%>
	</div>
	<script type="text/javascript" charset="UTF-8">
		$(document).ready(function() {
			//处理时间
			if("${auth.createat}" != 0){
				var d = new Date();
				d.setTime("${auth.createat}" * 1000);
				var year=d.getFullYear();
				var month=d.getMonth()+1;
				var day=d.getDate();
				var hour = d.getHours();
				var minute = d.getMinutes();
				var second = d.getSeconds();
				$("#createat").html(year+'-'+month+'-'+day+' '+hour+':'+minute+':'+second);
			}
			if("${auth.auditat}" != 0){
				var d = new Date();
				d.setTime("${auth.auditat}" * 1000);
				var year=d.getFullYear();
				var month=d.getMonth()+1;
				var day=d.getDate();
				var hour = d.getHours();
				var minute = d.getMinutes();
				var second = d.getSeconds();
				$("#auditat").html(year+'-'+month+'-'+day+' '+hour+':'+minute+':'+second);
			}
			
			
		});
	</script>
</body>