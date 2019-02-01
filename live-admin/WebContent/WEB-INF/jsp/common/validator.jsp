<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<link href="<%=base%>/static/bootstrap-3.3.5/css/bootstrapValidator.min.css" rel="stylesheet" />
<script src="<%=base%>/static/bootstrap-3.3.5/js/bootstrapValidator.js"></script>
<script src="<%=base%>/static/bootstrap-3.3.5/js/bootstrapValidator_zh_CN.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	//校验
	$('#validatorForm2').bootstrapValidator({
		message: '无效值',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        }
	});
});
</script>