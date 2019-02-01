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
				  <li><a href="javascript:void(0)">首页</a></li>
				  <li><a href="javascript:void(0)">运营管理</a></li>
				  <li><a href="javascript:void(0)">线上超管管理</a></li>
				</ol>
				<div class="divcx">
					<div class="cx-top-div row">
						<div class="col-md-2">
							<button type="button" class="btn btn-info" onclick="edit('add',0);">添加线上运营</button>
							</div>
						<div style="clear:both;"></div>
					</div>
				</div>
				<!---查询结束--->
				<!---table--->
				<table cellpadding="0" cellspacing="0" class="table table-bordered">
					<thead>
						<tr>
							<th class="th10">用户UID</th>
							<th class="th30">昵称</th>
							<th class="th20">添加时间</th>
							<th class="th10">添加人</th>
							<th class="th40">添加人昵称</th>
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
		"type":"${type }",
		"swi":$("#swi").val(),
		"startDate":$("#startDate").val(),
		"endDate":$("#endDate").val()
	};
	var cellFuncs = [ function(data) {
		//第一个TD
		var view = "&nbsp;";
		if(data.id!=null){
			view=data.id;
		}
		return view;
	}, function(data) {
		var view = "&nbsp;";
		if(data.picurl!=null&& data.picurl!=""){
			view="<img src='"+data.picurl+"' style='width:100%'/>";
		}
		return view;
	}, function(data) {
		var view = "&nbsp;";
		if(data.jumpurl!=null&& data.jumpurl!=""){
			view='<a href="http://'+data.jumpurl+'" class="easyui-tooltip" title="查看用戶信息">'+data.jumpurl+'</a>';
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.startDate!=null&& data.startDate!=""){
			//view=new Date(data.startshow * 1000).Format("yyyy-MM-dd HH:mm:ss");
			view = data.startDate;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.endDate!=null&& data.endDate!=""){
			//view=new Date(data.endshow * 1000).Format("yyyy-MM-dd HH:mm:ss");
			view = data.endDate;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.swi!=null){
			if(data.platform=="1"){
				view = "android";
			}else if(data.platform=="2"){
				view = "ios";
			}else if(data.platform=="3"){
				view = "所有平台";
			}
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.swi!=null){
			if(data.swi=="0"){
				view = "不显示";
			}else if(data.swi=="1"){
				view = "显示";
			}
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
	queryPageComp(_basepath+"/banner/listAll", condi, "_dataList", cellFuncs);
}
function del(id){
	 BootstrapDialog.confirm('确认删除吗?', function(result){
        if(result) {
       	 $.ajax({
                type: "post",
                dataType: "json",
                url: _basepath+"/banner/del",
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
	location.href = _basepath+"/banner/editPage?isAddOrEdit="+isAddOrEdit+"&id="+id+"&type=${type }";
}
</script>
  </body>
</html>