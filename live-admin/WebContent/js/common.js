//datagrid自适应分辨率
function getWidth(percent) {
	return document.body.clientWidth * percent;
}

function getHeight(percent) {
	return document.body.clientHeight * percent;
}
//在对象obj上显示msg内容的遮罩层
function showloading(obj, msg) {
	$("div .panel-tool-close").click( function () { hideloading(obj); });
	$("<div class=\"datagrid-mask\"></div>").css( {
		display : "block",
		width : '100%',
		height : '100%'
	}).appendTo(obj);
	$("<div class=\"datagrid-mask-msg\"></div>").html(msg).appendTo(obj).css( {
		display : "block",
		left : "35%",
		top : "35%"
	});
}
//移除遮罩层
function hideloading(obj) {
	obj.find("div.datagrid-mask-msg").remove();
	obj.find("div.datagrid-mask").remove();
}
//格式化数字到几位小数
function formatFloat(src, pos){
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
}
Date.prototype.Format = function (fmt) {
	var o = {
		"M+": this.getMonth() + 1, //月份
		"d+": this.getDate(), //日
		"H+": this.getHours(), //小时
		"m+": this.getMinutes(), //分
		"s+": this.getSeconds(), //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
function submitIFrameForm(opt) {
	var iframe, form;
	var iName = "com_down_iframe_" + new Date().getTime();
	var method = opt.method || 'post';
	// 创建iframe和form表单
	iframe = jQuery('<iframe name="' + iName + '" style="display:none;"/>');
	form = jQuery('<form method="' + method
			+ '" style="display:none;" target="' + iName + '" action="'
			+ opt.url + '" name="form_' + iName + '" />');
	// 额外的参数
	if (opt.data) {
		for ( var key in opt.data) {
			var value = opt.data[key];
			jQuery(
					'<input type="hidden" name="' + key + '" value="' + value
							+ '"/>').appendTo(form);
		}
	}
	// 插入body
	jQuery(document.body).append(iframe).append(form);
	form.submit();// 提交表单;
	// 文件提交完后
	iframe.load(function() {
		var data = $(this).contents().text();
		form.remove();
		iframe.remove();
		if (opt.success) {
			opt.success(data);
		}
	});
}
if($.fn.validatebox){
	$.extend($.fn.validatebox.defaults.rules, {     
	    phoneNum: { //验证手机号    
	        validator: function(value, param){
	        	if(value && value.length == 11){
	        		return /^1[3-9]+\d{9}$/.test(value);  
	        	}
	        	return false;
	        },     
	        message: '请输入正确的手机号码。'    
	    },  
	     
	    telNum:{ //既验证手机号，又验证座机号  
	      validator: function(value, param){  
	          return /(^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$)|(^((\d3)|(\d{3}\-))?(1[358]\d{9})$)/.test(value);  
	         },
	         message: '请输入正确的电话号码。'  
	    },
	    
	    equals:{
	        validator: function(value,param){
	            return value == $(param[0]).val();
	        },
	        message:'输入不一致！'
	    }
	});
}