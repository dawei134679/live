<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
				action="<%=base %>/banner/doedit" enctype="multipart/form-data">
					<input type="hidden" name="isAddOrEdit" value="${banner.isAddOrEdit }">
					<%-- 1是轮播图 --%>
					<input type="hidden" name="type" value="${type}">
					<input type="hidden" name="id" value="${banner.id}">
					<input type="hidden" name="picurl" value="${banner.picurl }">
					<input type="hidden" name="webPicUrl" value="${banner.webPicUrl }">
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">活动名：</label>
						<div class="col-lg-7">
                            <input type="text" class="form-control" name="name" value="${banner.name }"/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">平台：</label> 
						<div class="col-lg-7">
							<select class="selectpicker" name="platform">
								<option value="9" <c:if test="${banner.platform==9}">selected</c:if>>所有平台</option>
								<option value="2" <c:if test="${banner.platform==2}">selected</c:if>>ios</option>
								<option value="1" <c:if test="${banner.platform==1}">selected</c:if>>android</option>
								<option value="5" <c:if test="${banner.platform==5}">selected</c:if>>WEB</option>
							</select>
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">开始时间：</label>
						<div class="col-lg-7">
                            <input type="text" class="form-control form_date_ss" name="startDate" value="${fn:substring(banner.startDate, 0, 10)}"/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">结束时间：</label>
						<div class="col-lg-7">
                            <input type="text" class="form-control form_date_ss" name="endDate" value="${fn:substring(banner.endDate, 0, 10)}"/>
                        </div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">跳转地址：<font color="red">如跳转H5页，请输入，其他则不要输入</font></label>
						<div class="col-lg-7">
							<input type="input" class="form-control" name="jumpurl" value="${banner.jumpurl }"/>
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">跳转直播间：<font color="red">如跳转直播间，请输入，其他则不要输入</font></label>
						<div class="col-lg-7">
							<input type="input" class="form-control" name=roomId value="${banner.roomId }"/>
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">状态：</label> 
						<div class="col-lg-7">
							<select class="selectpicker" name="swi">
								<option value="1" <c:if test="${banner.swi==1}">selected</c:if>>显示</option>
								<option value="0" <c:if test="${banner.swi==0}">selected</c:if>>不显示</option>
							</select>
						</div>
					</div>
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">排序(越大越靠前，最大99)：</label> 
						<div class="col-lg-7">
							<input type="input" class="form-control" name="sort" value="${banner.sort }"/>
						</div>
					</div>
					
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">app图片：</label>
						<div class="col-lg-7">
							<input type="file" name="fileapp" onchange="previewImage(this);">
						</div>
					</div>
					<div class="form-group divlable ">
						<div class="col-lg-6" id="preview">
						<c:if test='${banner.picurl !=null }'>
						<img src="${banner.picurl }" alt="" class="img-rounded">
						</c:if>
						<c:if test='${banner.picurl == null }'>
						<img src="<%=base %>/static/images/banner123.png" alt="上传图片" class="img-rounded">
						</c:if>
						</div>
					</div>
					
					<div class="form-group divlable">
						<label class="col-lg-4 control-label">WEB图片：</label>
						<div class="col-lg-7">
							<input type="file" name="fileweb" onchange="previewImage2(this);">
						</div>
					</div>
					<div class="form-group divlable ">
						<div class="col-lg-6" id="preview2">
						<c:if test='${banner.webPicUrl != "" }'>
						<img src="${banner.webPicUrl }" alt="" class="img-rounded">
						</c:if>
						<c:if test='${banner.webPicUrl == "" }'>
						<img src="<%=base %>/static/images/banner123.png" alt="上传图片" class="img-rounded">
						</c:if>
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
										location.href = _basepath+"/banner/bannerList?type=${type}";
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
	function previewImage(file)
	{
	  var MAXWIDTH  = 260; 
	  var MAXHEIGHT = 180;
	  var div = document.getElementById('preview');
	  if (file.files && file.files[0])
	  {
	      div.innerHTML ='<img id="imghead" class="img-rounded">';
	      var img = document.getElementById('imghead');
	      img.onload = function(){
	        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	        img.width  =  rect.width;
	        img.height =  rect.height;
	//         img.style.marginLeft = rect.left+'px';
	        img.style.marginTop = rect.top+'px';
	      }
	      var reader = new FileReader();
	      reader.onload = function(evt){img.src = evt.target.result;}
	      reader.readAsDataURL(file.files[0]);
	  }
	  else //兼容IE
	  {
	    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
	    file.select();
	    var src = document.selection.createRange().text;
	    div.innerHTML = '<img id=imghead>';
	    var img = document.getElementById('imghead');
	    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
	    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
	    div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
	  }
	}
	

	function previewImage2(file)
	{
	  var MAXWIDTH  = 260; 
	  var MAXHEIGHT = 180;
	  var div = document.getElementById('preview2');
	  if (file.files && file.files[0])
	  {
	      div.innerHTML ='<img id="imghead1" class="img-rounded">';
	      var img = document.getElementById('imghead1');
	      img.onload = function(){
	        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	        img.width  =  rect.width;
	        img.height =  rect.height;
	//         img.style.marginLeft = rect.left+'px';
	        img.style.marginTop = rect.top+'px';
	      }
	      var reader = new FileReader();
	      reader.onload = function(evt){img.src = evt.target.result;}
	      reader.readAsDataURL(file.files[0]);
	  }
	  else //兼容IE
	  {
	    var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
	    file.select();
	    var src = document.selection.createRange().text;
	    div.innerHTML = '<img id=imghead1>';
	    var img = document.getElementById('imghead1');
	    img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
	    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
	    status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
	    div.innerHTML = "<div id=divhead1 style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
	  }
	}
	
	function clacImgZoomParam( maxWidth, maxHeight, width, height ){
	    var param = {top:0, left:0, width:width, height:height};
	    if( width>maxWidth || height>maxHeight )
	    {
	        rateWidth = width / maxWidth;
	        rateHeight = height / maxHeight;
	         
	        if( rateWidth > rateHeight )
	        {
	            param.width =  maxWidth;
	            param.height = Math.round(height / rateWidth);
	        }else
	        {
	            param.width = Math.round(width / rateHeight);
	            param.height = maxHeight;
	        }
	    }
	     
	    param.left = Math.round((maxWidth - param.width) / 2);
	    param.top = Math.round((maxHeight - param.height) / 2);
	    return param;
	}
</script>
</body>