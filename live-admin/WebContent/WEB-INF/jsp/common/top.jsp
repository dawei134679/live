<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<style type="text/css">
	.caretNew {
	  display: inline-block;
	  width: 0;
	  height: 0;
	  color:#000000;
	  margin-left: 2px;
	  vertical-align: middle;
	  border-top: 4px dashed;
	  border-top: 4px solid \9;
	  border-right: 4px solid transparent;
	  border-left: 4px solid transparent;
	}
</style><%--
<!-----头部----->
<div class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<!---头部-left-logo--->
		<div class="navbar-header">
			<a class="navbar-brand" href="#"><img
				src="<%=base%>/static/images/logo.png" /> </a>
		</div>
		<!---头部-left-logo结束--->
		<!---头部-right--->
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="collapse navbar-collapse nav navbar-nav top-menu navbar-right">
				<li><a class="ajax-link" href="#" data-toggle="dropdown">
						<span style="color:#fff;"><shiro:principal/></span> &nbsp;<i class="fa fa-angle-down">
						<img src="<%=base%>/static/images/jt.png" /> </i> </a>
					 <ul class="dropdown-menu" role="menu">
						<li><a href="<%=base %>/updatePwd"><i class="fa"></i>修改密码</a>
						</li>
						<li class="divider"></li>
						<li><a href="<%=base %>/logout" class="text-danger"><i
								class="fa fa-sign-out fa-fw"></i>退出</a></li>
					</ul> <!-- /.dropdown-user -->
					
				</li>
				<!-- /.dropdown -->
			</ul>
			<div class="topcommunitydiv">
		    			<h5 class="">123</h5>
		    </div>
		</div>
		<!---头部-right结束--->
	</div>
</div>
<!-----头部end----->
--%>