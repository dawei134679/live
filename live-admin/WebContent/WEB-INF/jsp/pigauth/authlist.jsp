<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
<%@ include file="../common/common.jsp"%>
</head>
  <body>
   <%@ include file="../common/top.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@ include file="../common/left.jsp"%>
			<!---右侧--->
			<div class="col-md-<%=defcol%> main r-pad">
				<!---查询--->
				<!-- <ol class="breadcrumb">
				  <li><a href="#">首页</a></li>
				  <li><a href="#">小猪认证</a></li>
				  	<li class="active">认证列表</li>
				</ol> -->
				<div class="divcx">
					<div class="cx-top-div row">
						<div class="form-inline col-md-2" style="float:left; margin-right:20px;">
							<div class="form-group">
								<label class="sr-only" for="uid">用户id：</label> 
								<input type="text" class="form-control" id="uid" placeholder="请输入用户id"/>
							</div>
						</div>
						<select class="col-md-2 selectpicker" id="status">
							<option value="">--全部--</option>
							<option value="1">待审核</option>
							<option value="2">已驳回</option>
							<option value="3">已通过</option>
						</select>
						<div class="col-md-3">
							<button type="button" class="btn btn-primary btn-lg btn-block cx-btn" onclick="query()">查&nbsp;询</button>
						</div>
					</div>
				</div>
				<!---查询结束--->
				<!---table--->
				<table cellpadding="0" cellspacing="0" class="table table-bordered">
					<thead>
						<tr>
						<%--th10是宽度10% th05到 th35 --%>
							<th class="th10">序号</th>
							<th class="th20">用户id</th>
							<th class="th20">认证昵称</th>
							<th class="th20">认证内容</th>
							<th class="th10">状态</th>
							<th class="th20">基本操作</th>
						</tr>
					</thead>
					<tbody id="_dataList">
					</tbody>
				</table>
				<!---table--->
				<%@ include file="../common/page.jsp"%>
			</div>
			<!---右侧结束--->
		</div>
<%@ include file="../common/footer.jsp"%>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	query();
});
function query() {
	//查询条件
	var condi = {
		"uid":$("#uid").val(),
		"status":$("#status").val()
	};
	var cellFuncs = [ function(data) {
		var view = "&nbsp;";
		if(data.id!=null){
			view=data.id;
		}
		return view;
	}, function(data) {
		var view = "&nbsp;";
		if(data.uid!=null){
			view=data.uid;
		}
		return view;
	}, function(data) {
		var view = "&nbsp;";
		if(data.nickname!=null&& data.nickname!=""){
			view=data.nickname;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.authcontent!=null&& data.authcontent!=""){
			view = data.authcontent;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.status!=null){
			if(data.status=="1"){
				view = "待审核";
			}else if(data.status=="2"){
				view = "已驳回";
			}else if(data.status=="3"){
				view = "已通过";
			}
		}
		return view;
	},function(data) {
			var caozuo = "&nbsp;";
			if(data.id == null || data.id == ""){
			}else{
				caozuo +="<ul class='tab-ul'>";
				if(data.status=="1"){
					caozuo +="<li><button type=\"button\" class=\"btn btn-warning\" onclick=\"javascript:check("+data.id+")\">查看</button>&nbsp;</li>";
					caozuo +="<li><button type=\"button\" class=\"btn btn-success\" onclick=\"javascript:approve("+data.id+")\">通过</button>&nbsp;</li>";
					caozuo +="<li><button type=\"button\" class=\"btn btn-danger\" onclick=\"javascript:reject("+data.id+")\">驳回</button>&nbsp;</li>";
				}else if(data.status=="2"){
					caozuo +="<li><button type=\"button\" class=\"btn btn-warning\" onclick=\"javascript:check("+data.id+")\">查看</button>&nbsp;</li>";
				}else if(data.status=="3"){
					caozuo +="<li><button type=\"button\" class=\"btn btn-warning\" onclick=\"javascript:check("+data.id+")\">查看</button>&nbsp;</li>";
				}
				
			}
			
			
		return "<p>"+caozuo+"</p>";
	} ];
	queryPageComp(_basepath+"/pigauth/list", condi, "_dataList", cellFuncs);
}

function approve(id){
	 BootstrapDialog.confirm('确认通过审核吗?', function(result){
        if(result) {
       	 $.ajax({
                type: "post",
                dataType: "json",
                url: _basepath+"/pigauth/approve",
                data: { id: id },
                success: function(data) {
               	 if(data.result){
               		 BootstrapDialog.alert("通过成功!", function(){
    						query();
    			        });
               	 }else{
               		 if(data.msg){
               			BootstrapDialog.alert(data.msg);
               		 }else{
               			BootstrapDialog.alert("通过失败！");	 
               		 }
               		 
               	 }
                }
            });
        }
    });
}

function check(id){
	location.href = _basepath+"/pigauth/getinfo?id="+id;
}

function reject(id) {
	
	BootstrapDialog.show({
        message: '请输入驳回理由: <input type="text" class="form-control">',
        onhide: function(dialogRef){
        },
        buttons: [{
            label: '确定',
            action: function(dialogRef) {
            	var cause = dialogRef.getModalBody().find('input').val();
                if($.trim(cause.toLowerCase()) == '') {
                    alert('理由不能为空!');
                    return false;
                }else{
                	dialogRef.close();
                	$.ajax({
                        type: "post",
                        dataType: "json",
                        url: _basepath+"/pigauth/reject",
                        data: { id: id, cause: cause },
                        success: function(data) {
                       	 if(data.result){
                       		 BootstrapDialog.alert("驳回成功!", function(){
            						query();
            			        });
                       	 }else{
                       		 BootstrapDialog.alert("驳回失败！");
                       	 }
                        }
                    });
                }
            }
        }]
    });

}
</script>
  </body>
</html>
