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
				<%-- <ol class="breadcrumb">
				  <li><a href="#">首页</a></li>
				  <li><a href="#">广告管理</a></li>
				  <c:if test="${type==0}">
				  	<li class="active">开屏广告</li>
				  </c:if>
				  <c:if test="${type==1}">
				  	<li class="active">轮播广告</li>
				  </c:if>
				</ol> --%>
				<div class="divcx">
					<div class="cx-top-div row">
						<div class=" col-md-4 form-inline" style="float:left; margin-right:20px;">
							<%--<div class="form-group">
								<label for="exampleInputName2">活动时间：</label> 
								<input type="text"
									class="form-control form_date_ss" id="startDate" readonly="readonly">&nbsp;-&nbsp;
								<input type="text" class="form-control form_date_ss"
									id="endDate">
							</div>
							--%><div class="input-group">
						      <input type="text" class="form-control form_date_ss" placeholder="开始时间" id="startDate" readonly="readonly">
						      <span class="input-group-addon">
						        <span class="glyphicon glyphicon-remove"></span>
						      </span>
						    </div>
						    <div class="input-group">
						      <input type="text" class="form-control form_date_ss" placeholder="结束时间" id="endDate" readonly="readonly">
						      <span class="input-group-addon">
						        <span class="glyphicon glyphicon-remove"></span>
						      </span>
						    </div>
						</div>
						<select class="col-md-2 selectpicker" id="swi">
							<option value="-1">--全部--</option>
							<option value="1">显示</option>
							<option value="0">不显示</option>
						</select>
						<div class="col-md-3">
							<button type="button" class="btn btn-primary btn-lg btn-block cx-btn" onclick="query()">查&nbsp;询</button>
						</div>
						<div class="col-md-2">
							<button type="button" class="btn btn-info" onclick="edit('add',0);">创建广告</button>
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
							<th class="th05">活动名</th>
							<th class="th05">排序</th>
							<th class="th30">app图片</th>
							<th class="th30">WEB图片</th>
							<th class="th10">跳转地址</th>
							<th class="th10">跳转房间</th>
							<th class="th08">开始时间</th>
							<th class="th08">结束时间</th>
							<th class="th05">平台</th>
							<th class="th05">状态</th>
							<th class="th15" style="min-width: 110px;">基本操作</th>
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
	var cellFuncs = [function(data) {
		//第一个TD
		var view = "&nbsp;";
		if(data.name!=null){
			view=data.name;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.sort!=null){
			view=data.sort;
		}
		return view;
	}, function(data) {
		var view = "&nbsp;";
		if(data.picurl!=null&& data.picurl!=""){
			view="<img src='"+data.picurl+"' style='width:100%'/>";
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.webPicUrl!=null&& data.webPicUrl!=""){
			view="<img src='"+data.webPicUrl+"' style='width:100%'/>";
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.jumpurl!=null&& data.jumpurl!=""){
			view='<a href="'+data.jumpurl+'" class="easyui-tooltip" title="查看用戶信息">'+data.jumpurl+'</a>';
		}
		return view;
	},  function(data) {
		var view = "&nbsp;";
		if(data.roomId!=null){
			view=data.roomId;
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.startDate!=null&& data.startDate!=""){
			//view=new Date(data.startshow * 1000).Format("yyyy-MM-dd HH:mm:ss");
			view = data.startDate;
			if(view){
				view = view.substring(0,10);
			}
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.endDate!=null&& data.endDate!=""){
			//view=new Date(data.endshow * 1000).Format("yyyy-MM-dd HH:mm:ss");
			view = data.endDate;
			if(view){
				view = view.substring(0,10);
			}
		}
		return view;
	},function(data) {
		var view = "&nbsp;";
		if(data.swi!=null){
			if(data.platform=="1"){
				view = "android";
			}else if(data.platform=="2"){
				view = "ios";
			}else if(data.platform=="9"){
				view = "所有平台";
			}else if(data.platform=="5"){
				view = 'WEB';
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
