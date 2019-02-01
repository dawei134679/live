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
				<form class="form-inline form-horizontal col-md-offset-1" id="validatorForm" method="post" 
					action="<%=base %>/actgift/doedit" >
					<input type="hidden" name="isAddOrEdit" value="${gift.isAddOrEdit }">
					<input type="hidden" name="id" value="${gift.id}">
					<input type="hidden" name="giftid" value="${gift.gid}">
					<div class="form-group divlable" >
						<label class="col-lg-4 control-label">选择礼物：</label>
						<div class="col-lg-7" id="gid">
							<select class="selectpicker" name="gid">
							</select>
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">活动类型：</label>
						<div class="col-lg-7">
							<select class="selectpicker" name="atype">
								<option value="1" <c:if test="${gift.atype==1}">selected</c:if>>周星</option>
								<%-- <option value="2" <c:if test="${gift.atype==2}">selected</c:if>>幸运</option>
								<option value="3" <c:if test="${gift.atype==3}">selected</c:if>>活动</option>
								<option value="4" <c:if test="${gift.atype==4}">selected</c:if>>新品</option>
								<option value="5" <c:if test="${gift.atype==5}">selected</c:if>>VIP</option>
								<option value="6" <c:if test="${gift.atype==6}">selected</c:if>>守护</option> --%>
							</select>(<font color="red">开始时间必须从周一开始</font>)
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">开始时间：</label>
						<div class="col-lg-7">
                            <input type="text" class="form-control form_date_ss" name="starttime" value="${gift.starttime }" required/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">结束时间：</label>
						<div class="col-lg-7">
                            <input type="text" class="form-control form_date_ss" name="endtime" value="${gift.endtime }" required/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">状态：</label>
						<div class="col-lg-7">
							<select class="selectpicker" name="isvalid">
								<option value="1" <c:if test="${gift.isvalid==true}">selected</c:if>>有效</option>
								<option value="0" <c:if test="${gift.isvalid==false}">selected</c:if>>无效</option>
							</select>
						</div>
					</div>
					<div class="form-group divlable ">
						<div class="col-lg-6">
							<button type="submit" style="width:200px;" class="btn btn-primary btn-lg btn-block">提&nbsp;交</button>
						</div>
						<div class="col-lg-6">
							<button type="button" style="width:200px;" class="btn btn-primary btn-lg btn-block" onclick="javascript:history.back(-1)">返&nbsp;回</button>
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
		loadGiftList();
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
										location.href = _basepath+"/actgift/actList";
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

	function loadGiftList(){
		$.ajax({
	        type: 'GET',
	        url: _basepath + "/actgift/getAllGiftList",
	        success: function (data) {
	            var data = eval(data);
	            $.each(data, function (i) {
	            	if("${gift.gid}" == data[i].gid){
	            		$("<option value='" + data[i].gid + "' selected >" + data[i].gname + "</option>").appendTo("#gid .selectpicker");
	            	}else{
	            		$("<option value='" + data[i].gid + "'>" + data[i].gname + "</option>").appendTo("#gid .selectpicker");
	            	}
	                    
	            });
	            
	        }
	    });
	}
</script>
</body>