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
				<ol class="breadcrumb">
				  <li><a href="#">首页</a></li>
				  <li><a href="#">公告管理</a></li>
				  <li class="active">系统公告</li>
				</ol>
				<div class="divcx">
					<div class="cx-top-div row">
						<div class="col-md-2">
							<button type="button" class="btn btn-info" onclick="edit('add',0);">创建系统公告</button>
							</div>
						<div style="clear:both;"></div>
					</div>
				</div>
				<!---查询结束--->
				<!---table--->
				<table cellpadding="0" cellspacing="0" class="table table-bordered">
					<thead>
						<tr>
						<%--th10是宽度10% th05到 th35 --%>
							<th class="th10">公告ID</th>
							<th class="th60">内容</th>
							<th class="th15">更新时间</th>
							<th class="th15">基本操作</th>
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
	$(".glyphicon-remove").click(function(){
		$(".form_date_ss").val("");
	})
});
function query() {
	//查询条件
	var condi = {
	};
	var cellFuncs = [ function(data) {
		//第一个TD
		var view = "&nbsp;";
		if(data.id!=null){
			view=data.id;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.content != null){
			view=data.content;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.utime!=null&& data.utime!=""){
			view=new Date(data.utime * 1000).Format("yyyy-MM-dd HH:mm:ss");
			//view = data.startDate;
		}
		return view;
	},function(data) {
			var caozuo = "&nbsp;";
			if(data.id == null || data.id == ""){
			}else{
				caozuo +="<ul class='tab-ul'>";
				caozuo +="<li><a href='javascript:edit(\"edit\","+data.id+");' class='icona icona1'><img src='"+_basepath+"/static/images/icona1.png'></a></li>";
				caozuo +="<li><a href='javascript:del("+data.id+");' class='icona icona2'><img src='"+_basepath+"/static/images/icona2.png'></a></li>";
			}
			
			
		return "<p>"+caozuo+"</p>";
	} ];
	queryPageComp(_basepath+"/notice/system/listAll", condi, "_dataList", cellFuncs);
}
function del(id){
	 BootstrapDialog.confirm('确认删除吗?', function(result){
        if(result) {
       	 $.ajax({
                type: "post",
                dataType: "json",
                url: _basepath+"/notice/system/del",
                data: { id: id },
                success: function(data) {
               	 if(data.result){
               		 BootstrapDialog.alert('删除成功!', function(){
    						query();
    			        });
               	 }else{
               		 BootstrapDialog.alert("删除失败！");
               	 }
                }
            });
        }
    });
}
function edit(isAddOrEdit,id){
	location.href = _basepath+"/notice/system/editPage?isAddOrEdit="+isAddOrEdit+"&id="+id+"&type=${type }";
}
</script>
  </body>
</html>
