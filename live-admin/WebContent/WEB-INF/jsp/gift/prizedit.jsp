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
					action="<%=base %>/luckygift/doedit" >
					<input type="hidden" name="isAddOrEdit" value="${gift.isAddOrEdit }">
					<input type="hidden" name="id" value="${gift.id}">
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">倍数：</label>
						<div class="col-lg-7">
                            <input type="number" class="form-control" name="multiples" value="${gift.multiples }" 
                            <c:if test="${gift.isAddOrEdit=='edit'}">readonly</c:if>
                            />
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">除数：</label>
						<div class="col-lg-7">
                            <input type="number" class="form-control" name="divisor" value="${gift.divisor }"/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">被除数：</label>
						<div class="col-lg-7">
                            <input type="number" class="form-control" name="dividend" value="${gift.dividend }"/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">是否上跑道：</label> 
						<div class="col-lg-7">
							<select class="selectpicker" name="isrunway">
								<option value="1" <c:if test="${gift.isrunway==1}">selected</c:if>>上</option>
								<option value="0" <c:if test="${gift.isrunway==0}">selected</c:if>>不上</option>
							</select>
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">连发最高中奖次数：</label>
						<div class="col-lg-7">
                            <input type="number" class="form-control" name="maxcount" value="${gift.maxcount }"/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">特效礼物：</label>
						<div class="col-lg-7" id="gid">
							<select class="selectpicker" name="gid">
							</select>
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">中奖修饰语：</label>
						<div class="col-lg-7">
                            <textarea class="form-control" rows="3" maxlength="200" name="decoratedword">${gift.decoratedword }</textarea>
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
										location.href = _basepath+"/luckygift/prizeList";
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
	        url: _basepath + "/actgift/getGiftList",
	        success: function (data) {
	        	$("<option value='0'>请选择</option>").appendTo("#gid .selectpicker");
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