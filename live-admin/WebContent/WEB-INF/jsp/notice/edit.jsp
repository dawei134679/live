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
				<ol class="breadcrumb">
				  <li><a href="#">首页</a></li>
				  <li><a href="#">公告管理</a></li>
				  <li><a href="#">系统公告</a></li>
				  <li class="active">编辑</li>
				  <li class="returnbutton"><a href="javascript:history.back(-1)"><button type="button" class="btn btn-success btn-xs" id="resetBtn">返&nbsp;&nbsp;回</button></a></li>
				</ol>
				<form class="form-inline form-horizontal col-md-offset-1" id="validatorForm" method="post" 
					action="<%=base %>/notice/system/doedit" >
					<input type="hidden" name="isAddOrEdit" value="${notice.isAddOrEdit }">
					<input type="hidden" name="id" value="${notice.id}">
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">公告内容(限制50字)：</label>
						<div class="col-lg-7">
                            <textarea class="form-control" rows="3" maxlength="50" name="content">${notice.content }</textarea>
                        </div>
					</div>
					<div class="form-group divlable ">
						<div class="col-lg-6">
							<button type="submit" class="btn btn-primary btn-lg btn-block">提&nbsp;交</button>
						</div>
					</div>
				</form>
			</div>
			<!---右侧结束--->
		</div>
		<%@ include file="../common/footer.jsp"%>
	</div>
	<script type="text/javascript" charset="UTF-8">
	$(document).ready(function(){
		var options = {
				type : "post",
				dataType : "json",
				success : function(data) {
					console.log(data);
					//成功后处理
					if (data.result) {
						BootstrapDialog.alert(
									data.msg,
									function() {
										location.href = _basepath+"/notice/system/noticeList";
									});
					} else {
						BootstrapDialog
								.alert(data.msg);
					}
				},
				error : function(data){
					console.log(data);
				}
			};
			$("#validatorForm").ajaxForm(options);
		 //重置
	    $('#resetBtn').click(function() {
	        $('#validatorForm').data('bootstrapValidator').resetForm(true);
	    });
	});

</script>
</body>