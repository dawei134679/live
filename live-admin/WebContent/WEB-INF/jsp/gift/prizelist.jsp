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
				<div class="divcx">
					<div class="cx-top-div row">
						<div class="col-md-2">
							<button type="button" class="btn btn-info" onclick="edit('add',0);">创建中奖倍数</button>
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
							<th class="th20">中奖倍数</th>
							<th class="th20">是否上跑道</th>
							<th class="th20">连发最高中奖次数</th>
							<th class="th30">基本操作</th>
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
		if(data.multiples != null){
			view=data.multiples;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.isrunway!=null&& data.isrunway!=""){
			if(data.isrunway == 0){
				view = "不上"
			}else if(data.isrunway == 1){
				view = "上";
			}
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.maxcount!=null&& data.maxcount!=""){
			view = data.maxcount;
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
	queryPageComp(_basepath+"/luckygift/listAll", condi, "_dataList", cellFuncs);
}
function del(id){
	 BootstrapDialog.confirm('确认删除吗?', function(result){
        if(result) {
       	 $.ajax({
                type: "post",
                dataType: "json",
                url: _basepath+"/luckygift/del",
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
	location.href = _basepath+"/luckygift/editPage?isAddOrEdit="+isAddOrEdit+"&id="+id;
}
</script>
  </body>
</html>
