'use strict';
/*************************************************
** 定义公共变量、方法
*************************************************/
var Config1 = {
	baseUrl : 'http://192.168.20.19:8080/game',
	token : 'MTAwMDAwODlfQUI5NjFGMTE4MDAwMkZBNkFBRkZENjAzQTFBNTgyOTRfMV84NjY0MTYwMzc2OTk5NzNfMTUwOTY5MDIzNw==',
	aesKey : 'hkzb2017hkzb2017',
	aesIv : 'A-16-Byte-String',
	roomId:'666666',
	anchorId:'10000089'
};
var Config = {
	baseUrl : '',
	token : '',
	aesKey : '',
	aesIv : '',
	roomId:'',
	anchorId:''
};
var isIos = function(){
	return navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/) ? true : false;
}
function log(msg){
	//console.info(msg);
}
function show(msg){
	try{
		webToast((typeof msg == 'object' ? JSON.stringify(msg) : msg),"middle",1000);
	}catch(e){
		webToast('打印报错了',"middle",1000);
	}
}
function pt(msg){
	$("body").html('<div style="margin-top:30%;">'+(typeof msg == 'object' ? JSON.stringify(msg) : msg)+'</div>');
}
function checkPageStatus(){
	//配置标识
	if(!window.flag_config){
		getAppConfig();
		setTimeout(function(){
			checkPageStatus();
		},5000);
		return false;
	}
	if(!window.flag_loadwawa){
		loadWawa();
		setTimeout(function(){
			checkPageStatus();
		},5000);
		return false;
	}
}
function getAppConfig(iosdata) {
	try{
		var optstr = null;
		if(iosdata){
			optstr = JSON.stringify(iosdata);
		}else{
			if(isIos()){
				window.webkit.messageHandlers.getCfg.postMessage(null);
				setTimeout(function(){
					checkPageStatus();
				},5000);
				return;
			}else{
				optstr = (window.yangtuo || yangtuo).getCfg();
			}
		}
		var s = eval('(' + optstr + ')');
		for ( var key in s) {
			Config[key] = s[key];
		}
		window.flag_config = true;
	}catch(e){
		log(e);
	}
}

//getAppConfig();
function aesEncrypt(word) {
    var key = CryptoJS.enc.Utf8.parse(Config.aesKey); //16位
    var iv = CryptoJS.enc.Utf8.parse(Config.aesIv);
    var srcs = CryptoJS.enc.Utf8.parse(word);
    
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.ciphertext.toString(CryptoJS.enc.Base64);
}
function ajax(opt) {
	var defaults = {
		url : "",
		data : {},
		dataType : "html",
		async : true,
		headers:{},
		timeout : 5000,
		xhrFields: {
			withCredentials: true 
		},
		crossDomain: true,
		type : "GET",
		contentType : "application/x-www-form-urlencoded",
		complete : function() {
		},
		success : function() {
		},
		error : function() {
			//show("error："+JSON.stringify(arguments));
		},
		auth : true
	};
	for ( var key in opt) {
		defaults[key] = opt[key];
	}
	if (defaults.hideLoading == false) {
		//layer.load();
	}
	if (defaults.auth) {
		var token = Config.token;
		var timestamp = new Date().getTime() + '';
		var aesToken = aesEncrypt(token + "_" + CryptoJS.MD5(timestamp).toString());
		var sign = CryptoJS.MD5(token + defaults.url + timestamp).toString();
		defaults.url = Config.baseUrl + defaults.url;
		defaults.headers.token = aesToken;
		defaults.headers.sign = sign;
		defaults.headers.timestamp = timestamp;
	}
	if (defaults.url.indexOf("?") == -1) {
		defaults.url = defaults.url += "?";
	} else {
		defaults.url = defaults.url += "&";
	}
	//show("请求data:"+JSON.stringify(defaults.data));
	$.ajax({
		url : defaults.url + "t=" + new Date().getTime(),
		data : defaults.data,
		headers:defaults.headers,
		dataType : defaults.dataType,
		async : defaults.async,
		type : defaults.type,
		timeout : defaults.timeout,
		contentType : defaults.contentType,
		complete : function() {
			defaults.complete.apply(this, arguments);
		},
		success : function(d) {
			var rd = eval('(' + d + ')');
			defaults.success.call(this, rd);
		},
		error : function() {
			defaults.error.apply(this, arguments);
		}
	});
}
function accAdd(arg1, arg2){
	var r1, r2, m;
	try {r1 = arg1.toString().split(".")[1].length;}catch(e){r1 = 0;}
	try {r2 = arg2.toString().split(".")[1].length;}catch (e) {r2 = 0;}
	m = Math.pow(10, Math.max(r1, r2));
	return (arg1 * m + arg2 * m) / m;
}
function Subtr(arg1, arg2) {
	var r1, r2, m, n;
	try {r1 = arg1.toString().split(".")[1].length;}catch (e){r1 = 0;}
	try {r2 = arg2.toString().split(".")[1].length;}catch (e) {r2 = 0;}
	m = Math.pow(10, Math.max(r1, r2));
	n = (r1 >= r2) ? r1 : r2;
	return ((arg1 * m - arg2 * m) / m).toFixed(n);
}
Number.prototype.sub = function (arg) {
	return Subtr(this, arg);
};
Number.prototype.add = function (arg) {
	return accAdd(arg, this);
};
/*************************************************
** 定义功能函数
*************************************************/
//接收app推送过来的消息
var imMsg = function(datastr){
	//var data = eval('('+datastr+')');
}
var setMoney = function(money){
	if(money || parseInt(money) >= 0){
		$('#jinbi').html(money);
	}
}
//更新money
var refreshMoney = function(){
	try{
		var _money = 0;
		if(isIos()){
			window.webkit.messageHandlers.getMoney.postMessage(null);
		}else{
			_money = parseFloat((window.yangtuo||yangtuo).getMoney());
		}
		if(_money >= 0){
			$('#jinbi').html(_money);
		}
	}catch(e){
		//show(JSON.stringify(e));
	}
}

var wawaData = [];

var RunRight = function(){
	if(window.currTuo == wawaData.length){
		window.currTuo = 0;
	}
	
	//给第一个礼物托绑定动画结束事件
	$(".turndown").find(".bigchild").eq(3).one('transitionend',function(e){
		e.stopPropagation();
		$(".turndown").find(".bigchild:first").stop().remove();
		setTimeout(function(){
			RunRight();
		},0);
	});
	
	//追加一个新的礼物托
	var htmls = [];
	var bean = wawaData[window.currTuo++];
	htmls.push('<div class="bigchild" graspdollId="'+bean.id+'" multiple="'+bean.multiple+'">');
	htmls.push('<div class="bj">');
	htmls.push('<img src="'+bean.imageUrl+'">');
	htmls.push('</div>');
	htmls.push('<div class="gs">X'+bean.multiple+'</div>');
	htmls.push('</div>');
//	var tmpHtml = $('<div class="bigchild">'+data[window.currTuo++]+'</div>');	
	var tmpHtml = $(htmls.join(""));
	tmpHtml.css({width : window.tuoWidth+'px',right:window.winWidth+"px"});
	$(".turndown").append(tmpHtml);
	
	//每个礼物托向右移动一格
	$(".turndown").find(".bigchild").each(function(idx){
		var that = $(this);
		//var thatRight = that.css('right').replace("px","");
		var thatWidth = that.width();
		var translateX = parseInt(that.attr("translateX") || 0);
		that.attr("translateX",(translateX+window.tuoWidth));
		that.css({
			width : tuoWidth+'px',
			transform : 'translateX('+(translateX+window.tuoWidth)+'px)'
			//right : (thatRight - window.tuoWidth)+ "px"
		});
		/*that.animate({
			width : tuoWidth+'px',
			right : (thatRight - window.tuoWidth)+ "px"
		},{
			duration:2000,
			easing: "linear",
			complete: function(){
				if(idx == 4){
					$(".turndown").find(".bigchild:first").stop().remove();
					RunRight();
				}
			}
		});*/
	});
}

var startRight = function(){
	window.winWidth = $(window).width();
	window.tuoWidth = Math.round(window.winWidth/4);
	
	var htmls = [];
	wawaData.forEach(function(bean,i){
		if(i < 4){
			htmls.push('<div class="bigchild" graspdollId="'+bean.id+'" multiple="'+bean.multiple+'">');
			htmls.push('<div class="bj">');
			htmls.push('<img src="'+bean.imageUrl+'">');
			htmls.push('</div>');
			htmls.push('<div class="gs">X'+bean.multiple+'</div>');
			htmls.push('</div>');
		}
	});
	window.currTuo = 4;//当前显示到哪个图了
	$(".turndown").html(htmls.join(''));
	
	var wws = $(".turndown").find(".bigchild");
	var wwsLen = wws.length;
	var i = 3;
	while(i > -1){
		var that = wws.eq(i);
		var css = {width : window.tuoWidth+'px'};
		css.right = (i * 25)+'%';
		that.css(css);
		i--;
	}
	
	RunRight();
}

/**********************************************
 ** 向左转start
 **********************************************/
var RunLeft = function(){
	if(window.currTuo2 == wawaData.length){
		window.currTuo2 = 0;
	}
	
	//给第一个礼物托绑定动画结束事件
	$(".turntop").find(".littlechild").eq(4).one('transitionend',function(e){
		e.stopPropagation();
		$(".turntop").find(".littlechild:first").stop().remove();
		setTimeout(function(){
			RunLeft();
		},0);
	});
	
	//追加一个新的礼物托

	var htmls = [];
	var bean = wawaData[window.currTuo2++];
	htmls.push('<div class="littlechild" graspdollId="'+bean.id+'" multiple="'+bean.multiple+'">');
	htmls.push('<div class="bj">');
	htmls.push('<img src="'+bean.imageUrl+'">');
	htmls.push('</div>');
	htmls.push('<div class="gs">X'+bean.multiple+'</div>');
	htmls.push('</div>');
	var tmpHtml = $(htmls.join(""));
	tmpHtml.css({width : window.tuoWidth2+'px',left:window.winWidth2+"px"});
	$(".turntop").append(tmpHtml);
	
	//每个礼物托向左移动一格
	$(".turntop").find(".littlechild").each(function(idx){
		var that = $(this);
//		var thatLeft = that.css('left').replace("px","");
		var thatWidth = that.width();
		var translateX = parseInt(that.attr("translateX") || 0);
		that.attr("translateX",(translateX-window.tuoWidth2));
		that.css({
			width : window.tuoWidth2+'px',
			transform : 'translateX('+(translateX-window.tuoWidth2)+'px)'
		});		
		/*that.animate({
			width : window.tuoWidth2+'px',
			left : (thatLeft - window.tuoWidth2)+ "px"
		},{
			duration:2000,
			easing: "linear",
			complete: function(){
				if(idx == 5){
					$(".turntop").find(".littlechild:first").stop().remove();
					RunLeft();
				}
			}
		});*/
		
	});
}


var startLeft = function(){
	window.winWidth2 = $(window).width();
	window.tuoWidth2 = Math.round(window.winWidth2/5);
	
	var htmls = [];
	wawaData.forEach(function(bean,i){
		if(i < 5){
			htmls.push('<div class="littlechild" graspdollId="'+bean.id+'" multiple="'+bean.multiple+'">');
			htmls.push('<div class="bj">');
			htmls.push('<img src="'+bean.imageUrl+'">');
			htmls.push('</div>');
			htmls.push('<div class="gs">X'+bean.multiple+'</div>');
			htmls.push('</div>');
		}
	});
	window.currTuo2 = 5;//当前显示到哪个图了
	$(".turntop").html(htmls.join(''));
	
	var wws = $(".turntop").find(".littlechild");
	var wwsLen = wws.length;
	var i = 4;
	while(i > -1){
		var that = wws.eq(i);
		var css = {width : window.tuoWidth2+'px'};
		css.left = (i * 20)+'%';
		that.css(css);
		i--;
	}
	
	RunLeft();
}

/**********************************************
 ** 抓娃娃
 **********************************************/
function zhuawawa(idx){
	idx = idx || 1;
	if(idx<5){
		//第一步到第二步的时候张开爪子
		if(idx == 1){
			$(".lefts").css("transform",'rotate(30deg)');
			$(".rights").css("transform",'rotate(-30deg)');
		}
		
		var winHeight = $(window).height();
		var tuoHeight = Math.round(winHeight/4);
		$(".linewarp").animate({
			top:(-68 + ((68/4) * idx)) + "%"
		},{
			duration:500,
			easing: "linear"
		});
		$(".hookwarp").animate({
			top:(13 + (68 / 4 * idx)) + '%'
		},{
			duration:500,
			easing: "linear",
			complete: function(){
				if(idx == 1){
					log('开始：1/4');
				}else if(idx == 2){
					log('开始：2/4');
				}else if(idx == 3){
					log('开始：3/4');
					//第三步到第四步的时候张合一点爪子
					$(".lefts").css("transform",'rotate(15deg)');
					$(".rights").css("transform",'rotate(-15deg)');
				}else if(idx == 4){
					log('开始：4/4');
					//抓娃娃
					smashChild();
				}
				zhuawawa(++idx);
			}
		});
	}
}
//抓娃娃
function smashChild(){
	//zhua
	var result = checkZhua();
	var _pawsPrice = window.pawsPrice || ($('.sdzhu').eq(0).attr('price') || '0');
	var graspdollId = 0;
	var multiple = 0;
	if(result.success){
		$(".jiangpin").html(result.that);
		//算概率
		graspdollId = result.that.attr("graspdollId");
		multiple = result.that.get(0).getAttribute("multiple");
	}
	ajax({
		url :'/gameDoll/grab',
		type : 'POST',
		data : {roomId:Config.roomId,anchorId:Config.anchorId,pawsPrice:_pawsPrice,graspdollId:graspdollId,multiple:multiple},
		success : function(d){
			if(d.code == '200'){
				window.zwwFlag = true;
				$("#resuldjinbi").html(d.data.totalPrice);
				Config.recordId = d.data.recordId;
			}else if(d.code == '4000'){
				try{
					if(isIos()){
						window.webkit.messageHandlers.rechargeTip.postMessage(null);
					}else{
						(window.yangtuo || yangtuo).rechargeTip();
					}
				}catch(e){}
			}
		}
	});
	//回去
	hookback(1);
}

//up
var hookback = function(idx){
	if(idx<5){
		var winHeight = $(window).height();
		var tuoHeight = Math.round(winHeight/4);
		$(".linewarp").animate({
			top:(0 - ((68/4) * idx)) + "%"
		},{
			duration:1000,
			easing: "linear"
		});
		$(".hookwarp").animate({
			top:(13 + (68 / 4 * (4-idx))) + '%'
		},{
			duration:1000,
			easing: "linear",
			complete: function(){
				if(idx == 1){
					log('返回：1/4');
				}else if(idx == 2){
					log('返回：2/4');
					if(!window.zwwFlag){
						if(Math.random() > 0.5){
							$(".jiangpin").animate({
								top:(450)+'%'
							},{
								duration:1000,
								easing: "linear",
								complete: function(){
									$(".jiangpin").hide();
								}
							});
						}
					}
				}else if(idx == 3){
					log('返回：3/4');
					if(!window.zwwFlag){
						if($(".jiangpin").css('display') != 'none'){
							$(".jiangpin").animate({
								top:(550)+'%'
							},{
								duration:1000,
								easing: "linear",
								complete: function(){
									$(".jiangpin").hide();
								}
							});
						}
					}
				}else if(idx == 4){
					
					window.startUnLock = true;//返回顶部解除锁定
					log('返回：4/4');
					
					if(window.zwwFlag){
						$(".resuld").show();
						//推送中奖消息
						ajax({
							url :'/gameDoll/pushDollMsg',
							type : 'POST',
							data : {recordId:Config.recordId},
							success : function(d){
								log(d.msg);
							}
						});
					}

					refreshMoney();
					
					window.zwwFlag = false;
					$(".jiangpin").removeAttr("style");
					$(".jiangpin").empty();
					//第四步结束的时候合爪子
					$(".lefts").css("transform",'rotate(0deg)');
					$(".rights").css("transform",'rotate(0deg)');
				}
				hookback(++idx);
			}
		});
	}
}

/********************抓到哪个娃娃******************/
//确定抓哪个娃娃
var checkZhua = function(){
	var winWidth = $(window).width();
	var winTopCenter = Math.round(winWidth/2);
	var tuoWidth = Math.round(winWidth/4);
	var tuoTopCenter = Math.round(tuoWidth/2);
	var minDistanceChild = [];
	$('.turndown').find('.bigchild').each(function(){
		var that = $(this);
		//var a = window.getComputedStyle(that.get(0)).getPropertyValue("transform");
		var leftCenter = that.offset().left.add(tuoTopCenter);
		//log((leftCenter+"-"+winTopCenter+"=")+(Number(leftCenter.sub(winTopCenter)).toFixed(2)));
		var distance = Math.abs(Number(Number(leftCenter.sub(winTopCenter)).toFixed(2)));
		if(distance < Math.round(tuoTopCenter/2)){
			minDistanceChild.push({that:that,distance:distance});
		}
	});
	if(minDistanceChild.length > 0){
		minDistanceChild.sort(function(a,b){
			return a.distance > b.distance ? 1 : -1;
		});
		
		var that = minDistanceChild[0].that;
		var thatDistance = minDistanceChild[0].distance;
		if(minDistanceChild.length > 1){
			var thatNext = minDistanceChild[1].that;
			var thatNextDistance = minDistanceChild[1].distance;
			if(thatDistance == thatNextDistance){
				return {success:false};
			}
		}
		
		var thatClone = $(that.prop("outerHTML"));
		thatClone.removeAttr("style");
		thatClone.css({'opacity':0});
		that.replaceWith(thatClone);

		var baseClone = $(thatClone.prop("outerHTML"));
		baseClone.css({'opacity':1});
		return {that:baseClone,success:true};
	}
	return {success:false};
}

/********************爪子切换******************/
var bindZhuaClick = function(){
	$('.sdzhu').bind('click',function() {
		if(window.startUnLock){
			var that = $(this);
			window.pawsPrice = that.attr("price") || '0'; 
			var i = that.index();
			for(var k=1;k<4;k++){
				if(i != k){
					$(".topwarp").removeClass("topwarp"+k);
					$(".linewarp").removeClass("linewarp"+k);
					$(".hookwarp").removeClass("hookwarp"+k);
					$(".lefts").removeClass("donghua");
					$(".rights").removeClass("donghua");
				}
			}
		  	$(".topwarp").addClass("topwarp"+i);
		  	$(".linewarp").addClass("linewarp"+i);
		  	$(".hookwarp").addClass("hookwarp"+i);
			setTimeout(function(){
				$(".lefts").addClass("donghua");
				$(".rights").addClass("donghua");
			},10);
		}
	});
}
/********************开始抓娃娃******************/
var bindGoStart = function(){
	$(".go").bind('click',function(){
		if(window.startUnLock){
			window.startUnLock = false; 
			var jinbi = parseInt(parseInt($("#jinbi").html()));
			var jianqian = parseInt(window.pawsPrice || $('.sdzhu').eq(0).attr('price'));
			if(jinbi >= jianqian){
				$("#jinbi").html(jinbi.sub(jianqian));
				zhuawawa();
			}else{
				window.startUnLock = true; 
				try{
					if(isIos()){
						window.webkit.messageHandlers.rechargeTip.postMessage(null);
					}else{
						(window.yangtuo || yangtuo).rechargeTip();
					}
				}catch(e){}
			}
		}
	});
}
var loadWawa = function(){
	ajax({
		url :'/gameDoll/getGameDollMoney',
		data : {}, 
		success : function(d){
			if(d.code == '200'){
				$(".sdzhu").each(function(idx){
					$(this).attr("price",(d.data['claw'+(idx+1)] || 0));
					$(this).find("span").html(d.data['claw'+(idx+1)] || 0);
				});
			}
		}
	});
	ajax({
		url :'/gameDoll/getGameDollConfig',
		data:{roomId:Config.roomId,anchorId:Config.anchorId,},
		success:function(d){
			if(d.code == '200'){
				wawaData = d.data;
				
				window.flag_loadwawa = true;
				window.startUnLock = true;
				bindZhuaClick();
				startRight();//向右转
				startLeft();//向左转
				bindGoStart();//绑定开始按钮抓娃娃
				
			}else{
				show(d.msg);
			}
		}
	});
}
var bindResuldClick = function(){
	$(".resuld").click(function(){
		$(this).hide();
	});
}

$(function(){
	FastClick.attach(document.body);
	window.flag_config = true;//配置是否已经加载好
	window.zwwFlag = false;//是否抓到娃娃
	window.flag_loadwawa = false;//是否已经加载娃娃
	setTimeout(function(){
		getAppConfig();
		refreshMoney();
		loadWawa();
		bindResuldClick();
		setTimeout(function(){
			checkPageStatus();
		},3000);
	},200);
});