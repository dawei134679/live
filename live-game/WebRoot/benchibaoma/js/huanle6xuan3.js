'use strict';
/*************************************************
** 定义公共变量、方法
*************************************************/
var Config = {
	baseUrl : '',
	token : '',
	aesKey : '',
	aesIv : '',
	roomId:'',
	anchorId:''
};
function log(msg){
	console.info(msg);
}
var isIos = function(){
	return navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/) ? true : false;
}
function show(msg){
	try{
		webToast((typeof msg == 'object' ? JSON.stringify(msg) : msg),"middle",1000);
	}catch(e){
		webToast('打印报错了',"middle",1000);
	}
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
	//游戏启动标识
	if(!window.flag_loadchebiao){
		loadTuBiao();
		setTimeout(function(){
			checkPageStatus();
		},5000);
		return false;
	}
	//读条标识
	if(!window.flag_loadtime){
		loadTimeAndPeriods();
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

/*************************************************
** 定义功能函数
*************************************************/
//接收app推送过来的消息
var imMsg = function(datastr){
	//$('body').html(datastr);
	var data = eval('('+datastr+')');
	for(var key in data){
		$(".mary").filter('[did='+key+']').html(data[key]);
	}
}
//游戏说明按钮点击事件
var bindClickImgBtnYxsm = function(){
	$("#imgBtnYxsm").bind('click',function(){
		window.gameLockStatus = 1;//记录游戏说明锁定状态
		var that = $(this);
		$(".spak").show();
		$(".top").hide();
		$(".centent").hide();
		$(".bottom").hide();
		//$(".centent-two").hide();
		$(".centent-two").css({opacity:0});
		$(".open").hide();
		$(".webl").hide();
		$('.back').css({background:'url(img/big_bg.png)',backgroundSize:'100% 100%'});
	});
}
//游戏记录详情页返回按钮
var bindClickWeblBtnBack = function(){
	$("#weblBtnBack").bind('click',function(){
		var that = $(this);
		window.gameLockStatus = -1;
		
		var shengyu_time = parseInt($("#speedPro").attr('times'));
		$("#speedPro").stop().css('margin-left',(0 - (100 - shengyu_time / 40 * 100))+'%');
		
		$(".top").show();
		$(".bottom").show();

		$(".spak").hide();
		if(window.gameStatus == 1){//游戏界面
			$(".centent").show();
			$('.back').css({background:'url(img/bg_all.png)',backgroundSize:'100% 100%'});
		}else if(window.gameStatus == 2){//摇奖界面
			//$(".centent-two").show();
			$(".centent-two").css({opacity:1});
			$('.back').css({background:'url(img/kj_bg.png)',backgroundSize:'100% 100%'});
		}else if(window.gameStatus == 3){//开奖界面
			$(".open").show();
			$('.back').css({background:'url(img/big_bg.png)',backgroundSize:'100% 100%'});
		}
		
		$(".webl").hide();
	});
}
//游戏说明详情返回按钮点击事件
var bindClickSpakBtnBack = function(){
	$("#spakBtnBack").bind('click',function(){
		var that = $(this);
		window.gameLockStatus = -1;

		var shengyu_time = parseInt($("#speedPro").attr('times'));
		$("#speedPro").stop().css('margin-left',(0 - (100 - shengyu_time / 40 * 100))+'%');
		
		$(".top").show();
		$(".bottom").show();

		$(".spak").hide();
		if(window.gameStatus == 1){//游戏界面
			$(".centent").show();
			$('.back').css({background:'url(img/bg_all.png)',backgroundSize:'100% 100%'});
		}else if(window.gameStatus == 2){//摇奖界面
			//$(".centent-two").show();
			$(".centent-two").css({opacity:1});
			$('.back').css({background:'url(img/kj_bg.png)',backgroundSize:'100% 100%'});
		}else if(window.gameStatus == 3){//开奖界面
			$(".open").show();
			$('.back').css({background:'url(img/big_bg.png)',backgroundSize:'100% 100%'});
		}
		
		$(".webl").hide();
	});
}
//游戏记录按钮点击事件
var bindClickImgBtnYxjl = function(){
	$("#imgBtnYxjl").bind('click',function(){
		window.gameLockStatus = 2;//记录游戏记录锁定状态
		var that = $(this);
		$(".spak").hide();
		$(".top").hide();
		$(".centent").hide();
		$(".bottom").hide();
		//$(".centent-two").hide();
		$(".centent-two").css({opacity:0});
		$(".open").hide();
		$(".webl").show();
		$('.back').css({background:'url(img/big_bg.png)',backgroundSize:'100% 100%'});
		loadYzjl(0);
		loadKjjl(0);
	});
}

/*加载开奖记录*/
var loadKjjl = function(_pageNo){
	var pageNo = parseInt(_pageNo || 0) + 1;
	var pageSize = 10;
	ajax({
		url :'/gameCar/getLotteryRecord',
		type:'post',
		data:{pageNo:pageNo,pageSize:pageSize,anchorId:Config.anchorId},
		success:function(d){
			if(d.code == '200'){
				//show('data:'+JSON.stringify(d.data));
				var html = [];
				if(d.data.length > 0){
					for(var i=0;i<d.data.length;i++){
						var r = d.data[i];
						html.push('<tr>');
						html.push('<td style="width: 50%;">第'+r.periods+'期</td>');
						html.push('<td>');
						html.push('<div class="bj zc">');
						html.push('<img src="'+window.tubiao[r.carId1]+'" />');
						//html.push('<span style="">123456</span>');
						html.push('</div>');
						html.push('</td>');
						html.push('<td>');
						html.push('<div class="bj zc">');
						html.push('<img src="'+window.tubiao[r.carId2]+'" />');
						//html.push('<span style="">123456</span>');
						html.push('</div>');
						html.push('</td>');
						html.push('<td>');
						html.push('<div class="bj zc">');
						html.push('<img src="'+window.tubiao[r.carId3]+'" />');
						//html.push('<span style="">123456</span>');
						html.push('</div>');
						html.push('</td>');
						html.push('</tr>');
						//html.push('<div class="left">第'+r.periods+'期</div><div class="right"><img class="cb" src="'+window.chebiao[r.carId+'']+'" /></div>');
					}
					if(pageNo==1){
						$("#kaijiangtable tbody").html(html.join(''));
					}else{
						$("#kaijiangtable tbody").append(html.join(''));
					}
					if(d.data.length < pageSize){
						$("#kaijiangtable tfoot").find('tr td').html('没有更多记录').attr('onclick','');
					}else{
						$("#kaijiangtable tfoot").find('tr td').html('更多').attr('pageNo',pageNo);
					}
				}else{
					$("#kaijiangtable tfoot").find('tr td').html('没有更多记录').attr('onclick','');
				}
			}
		}
	});
}

//获取押注记录
var loadYzjl = function(_pageNo){
	var pageNo = parseInt(_pageNo || 0) + 1;
	var pageSize = 10;
	ajax({
		url :'/gameCar/getStakeRecord',
		type:'post',
		data:{pageNo:pageNo,pageSize:pageSize},
		success:function(d){
			//show("押注记录："+JSON.stringify(d));
			if(d.code == '200'){
				var html = [];
				if(d.data.length > 0){
					for(var i=0;i<d.data.length;i++){
						var r = d.data[i];
						html.push('<tr>');
						html.push('<td>第'+r.periods+'期</td>');
						var carIds = r.carId.split(',');
						var statuss = r.status.split(',');
						var moneys = r.money.split(',');
						for(var key in window.tubiao){
							html.push('<td>');
							var flag = false,zhong = false,money = 0;
							for(var k=0;k<carIds.length;k++){
								var tmpCarId = carIds[k];
								if(key == tmpCarId){
									money = moneys[k];
									if(statuss[k] == '2'){
										zhong = true;
									}
									flag = true;
									break;
								}
							}
							if(flag){
								if(zhong){
									html.push('<div class="bj dui">');
								}else{
									html.push('<div class="bj cuo">');
								}
							}else{
								html.push('<div class="bj zc">');
							}
							html.push('<img src="'+window.tubiao[key]+'" />');
							html.push('<span style="">'+money+'</span>');
							html.push('</div>');
							html.push('</td>');
						}
						html.push('</tr>');
					}
					if(pageNo==1){
						$("#yazhutable tbody").html(html.join(''));
					}else{
						$("#yazhutable tbody").append(html.join(''));
					}
					if(d.data.length < pageSize){
						$("#yazhutable tfoot").find('tr td').html('没有更多记录').attr('onclick','');
					}else{
						$("#yazhutable tfoot").find('tr td').html('更多').attr('pageNo',pageNo);
					}
				}else{
					$("#yazhutable tfoot").find('tr td').html('没有更多记录').attr('onclick','');
				}
			}
		}
	});
}

//游戏筹码点击事件
var bindClickImgMoneyCM = function(){
	$(".moey img").bind('click',function(){
		var that = $(this);
		var dval = that.attr('dval');
		that.attr('src','img/gold_'+dval+'.png').attr('dsel','1');
		for(var i=0;i<that.siblings().length;i++){
			var tmpDval = $(that.siblings()[i]).attr('dval');
			$(that.siblings()[i]).attr('src','img/gold_'+tmpDval+'_h.png').attr('dsel','0');;
		}
	});
}

//游戏记录顶部tab切换事件
var bindClickRecordTabBtn = function(){
	$(".kj-top img").bind('click',function(){
		var that = $(this);
		var dsel = that.attr('dsel');
		var dval = that.attr('dval');
		var imgname = '';
		if(dsel == '0'){
			that.attr('dsel','1');
			that.attr('src','img/'+(dval == '1' ? 'kjjl' : 'yzjl')+'_y.png');
		}
		var other = $(that.siblings()[0]);
		other.attr('dsel','0');
		other.attr('src','img/'+(dval == '2' ? 'kjjl' : 'yzjl')+'_h.png');
		
		if(dval == '1'){//开奖记录
			$("#kaijiangtable").show();
			$("#yazhutable").hide();
			loadKjjl(0);
		}else if(dval == '2'){//押注记录
			$("#kaijiangtable").hide();
			$("#yazhutable").show();
			loadYzjl(0);
		}
		
	});
}
//开始倒计时
var startDaoJiShi = function(){
	var times = parseInt($("#speedPro").attr('times') || '0');
	if(times > 0){
		times = times - 1;
		$("#speedPro").attr('times',times);
		//$("#speedPro").css('margin-left',(0 - (100 - times / 40 * 100))+'%');
		$('#speedPro').stop().animate({marginLeft : (0 - (100 - times / 40 * 100))+'%'},1500);
		var speedhtml = [];
		var speeds = (times+"").split('');
		for(var i=0;i<speeds.length;i++){
			speedhtml.push('<img src="img/'+speeds[i]+'.png">');
		}
		//speedhtml.push('<img src="img/s.png">');
		$(".speed .f").html(speedhtml.join(''));
		clearTimeout(window.daojishi_timer);
		window.daojishi_timer = setTimeout(window.startDaoJiShi,1000);
	}else{//到计时0，开奖
		$("#speedPro").attr('times','0');
		clearTimeout(window.daojishi_timer);
		setTimeout(function(){
			serverKaijiang();
		},800);
	}
}
var serverKaijiang = function(){
	ajax({
		url :'/gameCar/getLotteryByPeriods',
		data:{periods:Config.periods,anchorId:Config.anchorId},
		success:function(d){
			//show("开奖结果："+JSON.stringify(d));
			if(d.code == '200'){
				window.gameStatus = 2;//摇奖界面
				
				if(window.gameLockStatus != 1){
					$(".spak").hide();
				}
				$(".centent").hide();
				if(window.gameLockStatus <= 0){
					$(".top").show();
					$(".bottom").show();
					//$(".centent-two").show();
					$(".centent-two").css({opacity:1});
				}
				$(".open").hide();
				if(window.gameLockStatus != 2){
					$(".webl").hide();
				}
				if(window.gameLockStatus <= 0){
					$('.back').css({background:'url(img/kj_bg.png)',backgroundSize:'100% 100%'});
				}
				
				//摇奖
				var contentTwoHeight = 130;
				window.zhuanImgHeight = $(".tu img:eq(0)").height();
				
				$(".num").css('top',0);//初始全部为零
				$(".num").css('top',((contentTwoHeight-window.zhuanImgHeight)/2));
				var num_arr = [];//将生成的3位数转变为一个数组
				
				for(var i=0;i<window.tubiaoData.length;i++){
					var tb = window.tubiaoData[i];
					if(tb.id == d.data.carId1){
						num_arr[0] = i;//tb.sort;
					}
					
					if(tb.id == d.data.carId2){
						num_arr[1] = i;//tb.sort;
					}
					
					if(tb.id == d.data.carId3){
						num_arr[2] = i;//tb.sort;
					}
				}
				
				$(".num").each(function(index){
					var _num = $(this);
					//setTimeout(function(){
						//if(index == 2){
						//	show("转："+JSON.stringify(num_arr)+"结果:"+d.data.carId1+","+d.data.carId2+","+d.data.carId3);
						//}
					_num.animate({
						//top :  (0 - (window.zhuanImgHeight * 6 * 3) - ((parseInt(num_arr[index])) * window.zhuanImgHeight) + ((contentTwoHeight-window.zhuanImgHeight)/2)) + 'px'
						top :  (0 - (window.zhuanImgHeight * 6 * 4) + ((contentTwoHeight-window.zhuanImgHeight)/2)) + 'px'
					},{
						speed:'fast',
						duration: 4000,//运动持续时间
						easing: "linear"//运动类型 easeInOutCirc linear
					});
					
					setTimeout(function(){
						_num.stop();
						_num.animate({
							top :  (0 - (window.zhuanImgHeight * 6 * 4) - ((parseInt(num_arr[index])) * window.zhuanImgHeight) + ((contentTwoHeight-window.zhuanImgHeight)/2)) + 'px'
						},{
							speed:'1500',
							duration: 2500 + (500 * index),//运动持续时间
							easing: "linear",//运动类型 easeInOutCirc linear
							complete: function(){
								if(index==2){//运动结束
									setTimeout(function(){
										window.gameStatus = 3;//开奖界面
										
										$(".num").css('top',((contentTwoHeight-window.zhuanImgHeight)/2));//初始全部为零
										
										$(".open .op1 img").attr("src",window.tubiao[d.data.carId1]);
										$(".open .op2 img").attr("src",window.tubiao[d.data.carId2]);
										$(".open .op3 img").attr("src",window.tubiao[d.data.carId3]);
										
										$("#open_tb").html("投币:"+d.data.totalStakeMoney);
										$("#open_zj").html("中奖:"+d.data.totalDeservedMoney);
										
										if(window.gameLockStatus != 1){
											$(".spak").hide();
										}
										$(".centent").hide();
										//$(".centent-two").hide();
										$(".centent-two").css({opacity:0});
										if(window.gameLockStatus <= 0){
											$(".top").show();
											$(".bottom").show();
											$(".open").show();
										}
										if(window.gameLockStatus != 2){
											$(".webl").hide();
										}
										if(window.gameLockStatus <= 0){
											$('.back').css({background:'url(img/big_bg.png)',backgroundSize:'100% 100%'});
										}
										setTimeout(function(){
											restartGame();
										},2000);
									},1000);
								}
							}
						});
					}, 1000 + (index * 500));
					
					//},1000);//一起转 || index * 1000
				});
			}else if(d.code == '3000'){
				setTimeout(function(){window.serverKaijiang();},3000);
			}else{
				setTimeout(function(){window.serverKaijiang();},5000);
			}
		},
		error:function(){
			setTimeout(function(){window.serverKaijiang();},5000);
		}
	});
}
//加载秒数和期数
var loadTimeAndPeriods = function(){
	ajax({
		url :'/gameCar/getGameCarTime',
		data:{roomId:Config.roomId,anchorId:Config.anchorId},
		success:function(d){
			//show("加载秒期数："+JSON.stringify(d));
			if(d.code == '200'){
				window.flag_loadtime = true;
				Config.periods = d.data.periods;
				var shengyu_time =d.data.time;
				var speedhtml = [];
				var speeds = (shengyu_time+"").split('');
				for(var i=0;i<speeds.length;i++){
					speedhtml.push('<img src="img/'+speeds[i]+'.png">');
				}
				//speedhtml.push('<img src="img/s.png">');
				$(".speed .f").html(speedhtml.join(''));
				$("#speedPro").attr('times',shengyu_time);
				$("#speedPro").stop().css('margin-left',(0 - (100 - shengyu_time / 40 * 100))+'%');
				//$('#speedPro').stop().animate({marginLeft : (0 - (100 - shengyu_time / 40 * 100))+'%'},1500);
				window.daojishi_timer = setTimeout(window.startDaoJiShi,1000);
			}
		},
		error:function(){
			setTimeout(function(){
				checkPageStatus();
			},5000);
			//show("getGameCarTime error："+JSON.stringify(arguments));
		}
	});
}
var setMoney = function(money){
	if(money || parseInt(money) >= 0){
		$('.brown').html(money);
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
			$('.brown').html(_money);
		}
	}catch(e){
		//show(JSON.stringify(e));
	}
}
//重启游戏
var restartGame = function(){
	window.flag_loadchebiao = true;
	window.gameStatus = 1;//游戏界面
	
	if(window.gameLockStatus != 1){
		$(".spak").hide();
	}
	if(window.gameLockStatus <= 0){
		$(".top").show();
		$(".centent").show();
		$(".bottom").show();
	}
	//$(".centent-two").hide();
	$(".centent-two").css({opacity:0});
	$(".open").hide();
	if(window.gameLockStatus != 2){
		$(".webl").hide();
	}
	if(window.gameLockStatus <= 0){
		$('.back').css({background:'url(img/bg_all.png)',backgroundSize:'100% 100%'});
	}
	$(".mary").html('0');
	$(".but span").html('0');
	
	$("#speedPro").stop().css('margin-left',0);
	
	loadTimeAndPeriods();
	refreshMoney();
}
//加载图标
var loadTuBiao = function(){
	ajax({
		url :'/gameCar/getGameCarConfig',
		success:function(d){
			if(d.code == '200'){
				//show("getGameCarConfig返回数据："+JSON.stringify(d.data));
				window.tubiao = {};
				d.data.sort(function(a,b){
					return a.sort - b.sort;
				});
				window.tubiaoData = d.data;
				
				for(var i=0;i<d.data.length;i++){
					var key = d.data[i].id;
					var val = d.data[i].img;
					window.tubiao[key] = val;
				}
				
				//开奖滚动图片列表
				var gthtml = [];
				//押注图片列表
				var html = [];
				if(d.data.length  >= 6){
					var i = 1;
					while(i < 7){
						//押注页面图片html拼接
						html.push('<div class="fruits'+i+'">');
						html.push('<img src="'+d.data[i-1].img+'" style="width:92%;" />');
						html.push('<span class="a">总压注额</span>');
						html.push('<div class="mary" did="'+d.data[i-1].id+'">0</div>');
						html.push('<div class="but" did="'+d.data[i-1].id+'" dval="0" onclick="clickXiaZhuBtn(this);">');
						html.push('<span>0</span>');
						html.push('</div>');
						html.push('</div>');
						
						//开奖滚动页面html拼接
						gthtml.push('<img src="'+d.data[i-1].img+'">');
						
						i++;
					}
				}
				$(".centent").html(html.join(''));
				
				var randomSort = function(a, b) {
					return Math.random() > 0.5 ? -1 : 1;
				}
				
				var luangthtml = $.extend(true, [], gthtml);
				
				var onehtml = luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						gthtml.join('') +
						gthtml.join('');
				
				var twohtml = luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						gthtml.join('') +
						gthtml.join('');
				
				var threehtml = luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						luangthtml.sort(randomSort).join('') +
						gthtml.join('') +
						gthtml.join('');
				
				$(".tu").html('').append(onehtml);
				$(".tu2").html('').append(twohtml);
				$(".tu3").html('').append(threehtml);
				
				//储存滚动图片高度
				window.zhuanImgHeight = $(".tu img:eq(0)").height();
				
				//开始游戏
				restartGame();
			}else{
				//加载游戏失败
			}
		},
		error:function(){
			//加载游戏失败
		}
	});
}
//控制押注按钮点击时间
var canFastClick = function(){
	var nowClickTime = new Date().getTime();
	if (Math.round(nowClickTime - window.lastClickTime) > 1000) {
		window.lastClickTime = nowClickTime;
		window.fastClickCount = 1;
		return true;
	}else{
		window.fastClickCount = (window.fastClickCount||0)+1;
		return false;
	}
}
//下注按钮点击事件
var clickXiaZhuBtn = function(t){
	var that = $(t);
	var dmoney = parseInt($(".moey img").filter('[dsel=1]').attr('dval'));
	var did = that.attr('did');
	var refreshMoneyFlag = canFastClick();
	var tmpFastClickCount = window.fastClickCount; 
	//押注
	ajax({
		url :'/gameCar/stake',
		type:'post',
		data:{roomId:Config.roomId,anchorId:Config.anchorId,carId:did,money:dmoney,periods:Config.periods},
		success:function(d){
			if(d.code == '200'){
				$('.brown').html(parseInt($('.brown').html()||'0')-dmoney);
				//var stime = new Date().getTime();
				
				$(t).find('span').html(parseInt($(t).find('span').html()) + dmoney);
				
				//var etime = new Date().getTime();
				
				//show((etime-stime));
				
				if(refreshMoneyFlag){
					refreshMoney();
				}else{
					setTimeout(function(){
						if(window.fastClickCount == tmpFastClickCount){
							refreshMoney();
						}
					},1000);
				}
			}else if(d.code == '4000'){
				try{
					if(isIos()){
						window.webkit.messageHandlers.rechargeTip.postMessage(null);
					}else{
						(window.yangtuo || yangtuo).rechargeTip();
					}
				}catch(e){}
			}else{
				show(d.msg);
			}
		},
		error:function(){
			//show("stake error："+JSON.stringify(arguments));
		}
	});
}
var bindClickRecharge = function(){
	$("#recharge").bind('click',function(){
	/*
	var d = {};
	d.data = {carId1:1,carId2:2,carId3:1};
	
	window.yangtuo = {getCfg:function(){return 1;}};
	
	$(".spak").hide();
	$(".top").show();
	$(".centent").hide();
	$(".bottom").show();
	$(".centent-two").show();
	$(".open").hide();
	$(".webl").hide();
	$('.back').css({background:'url(img/kj_bg.png)',backgroundSize:'100% 100%'});
	
	//摇奖
	var contentTwoHeight = 130;
	window.zhuanImgHeight = $(".tu img:eq(0)").height();
	
	//show(window.zhuanImgHeight+"_"+((contentTwoHeight-window.zhuanImgHeight)/2));
	$(".num").css('top',((contentTwoHeight-window.zhuanImgHeight)/2));//初始全部为零
	var num_arr = [1,4,3];//将生成的3位数转变为一个数组
	
	$(".num").each(function(index){
		var _num = $(this);
		_num.append(_num.html());
		_num.append(_num.html());
		_num.append(_num.html());
		//setTimeout(function(){
			//if(index == 2)
			//show("2:"+JSON.stringify(num_arr));// * window.zhuanImgHeight
			//show("3:"+((contentTwoHeight-window.zhuanImgHeight)/2));
			//show("4:"+(0 - (window.zhuanImgHeight * 6 * 3) - ((parseInt(num_arr[index])) * window.zhuanImgHeight) - ((contentTwoHeight-window.zhuanImgHeight)/2)));
			
			_num.animate({
				//top :  (0 - (window.zhuanImgHeight * 6 * 3) - ((parseInt(num_arr[index])) * window.zhuanImgHeight) + ((contentTwoHeight-window.zhuanImgHeight)/2)) + 'px'
				top :  (0 - (window.zhuanImgHeight * 6 * 4) + ((contentTwoHeight-window.zhuanImgHeight)/2)) + 'px'
			},{
				speed:'fast',
				duration: 4000,//运动持续时间
				easing: "linear"//运动类型 easeInOutCirc linear
			});
			
			setTimeout(function(){
				_num.stop();
				_num.animate({
					top :  (0 - (window.zhuanImgHeight * 6 * 4) - ((parseInt(num_arr[index])) * window.zhuanImgHeight) + ((contentTwoHeight-window.zhuanImgHeight)/2)) + 'px'
				},{
					speed:'1500',
					duration: 2500 + (500 * index),//运动持续时间
					easing: "linear",//运动类型 easeInOutCirc linear
					complete: function(){
						if(index==2){//运动结束
							show('运动结束'+index);
						}
					}
				});
			}, 1000 + (index * 500));
			
		//}, 1000);
	});
	*/

		try{
			if(isIos()){
				window.webkit.messageHandlers.rechargeMoney.postMessage(null);
			}else{
				(window.yangtuo || yangtuo).rechargeMoney();
			}
		}catch(e){
			log(e);
		}
	});
}

$(function(){
	FastClick.attach(document.body);
	
	bindClickSpakBtnBack();//游戏说明详情页返回按钮
	bindClickWeblBtnBack();//游戏记录详情页返回按钮
	bindClickImgBtnYxsm();//游戏说明按钮
	bindClickImgBtnYxjl();//游戏记录按钮
	bindClickImgMoneyCM();//筹码点击事件
	bindClickRecordTabBtn();//游戏记录顶部tab切换事件
	bindClickRecharge();//左下角充值按钮
	
	
	//延迟100毫秒
	setTimeout(function(){
		window.flag_loadtime = false;//读条标识
		window.gameStatus = 1;//游戏界面
		window.gameLockStatus = 0;//锁定界面
		getAppConfig();
		refreshMoney();
		loadTuBiao();
		setTimeout(function(){
			checkPageStatus();
		},5000);
	},200);
	
});
