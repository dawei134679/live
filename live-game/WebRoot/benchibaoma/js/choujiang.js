document.addEventListener('DOMContentLoaded',function(){
	
	window.zhuan = function(result,callback){
		result = Math.ceil(Math.random()*6)+","+Math.ceil(Math.random()*6)+","+Math.ceil(Math.random()*6);
		var u = 158;
		$(".num").css('backgroundPositionY',0);//初始全部为零
		var num_arr = (result+'').split(',');//将生成的3位数转变为一个数组
		$(".num").each(function(index){
			var _num = $(this);
			setTimeout(function(){
				_num.animate({
					backgroundPositionY: ((u * 19) - (num_arr[index] * u))
				},{
					speed:'1500',
					duration: 5000 + index * 2000,//运动持续时间
					easing: "easeInOutCirc",//运动类型 easeInOutCirc linear
					complete: function(){
						if(index==2){//运动结束
							if(callback)callback();
						}
					}
				});
			}, index * 3000);
		});
	}
	$(function(){
		$("#main").hide();
		$(".contentBox").show();
		zhuan('2,3,5');
	});

},false)
