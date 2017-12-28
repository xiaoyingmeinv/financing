
$(function(){
	var index = 0;
	var gift1 = '<div class="swiper-slide" gift-data="300-1000元话费抽奖">'+
			        	'<span class="gift-name">300-1000元话费抽奖</span>'+
			        	'<img class="img" src="images/huafei1.png"/>'+
			        '</div>';
	var gift2= '<div class="swiper-slide" gift-data="500-2000元话费抽奖">'+
			        	'<span class="gift-name">500-2000元话费抽奖</span>'+
			        	'<img class="img" src="images/huafei2.png"/>'+
			        '</div>';
	var gift3 = '<div class="swiper-slide" gift-data="ipad min4 16G">'+
			        	'<span class="gift-name">ipadmini4 16G</span>'+
			        	'<img class="img" src="images/ipadmini4.png"/>'+
			        '</div>';
	var gift4 = '<div class="swiper-slide" gift-data="ipadair2 16G">'+
			        	'<span class="gift-name">ipadair2 16G</span>'+
			        	'<img class="img" src="images/ipad.png"/>'+
			        '</div>';
	var gift5 = '<div class="swiper-slide" gift-data="iphone6s 64G">'+
			        	'<span class="gift-name">iphone6s 64G</span>'+
			        	'<img class="img" src="images/yuyue_02.png"/>'+
			        '</div>';

	var param = getUrlParam();
	var amount = param.amount;
	if(amount){
		var value = Number(amount)/10000;
		var div = "";
		if(value>=30&&value<50){
			index =1;
			div =gift1;
		}
		if(value>=50&&value<100){
			index =2;
			div=gift1+gift2;
		}
		if(value>=100&&value<200){
			index =3;
			div=gift1+gift2+gift3;
		}
		if(value>=200&&value<500){
			index=4;
			div=gift1+gift2+gift3+gift4;	
		}
		if(value>=500){
			index=5;
			div=gift1+gift2+gift3+gift4+gift5;	
		}
		$(".swiper-wrapper").append(div);
		var mySwiper = new Swiper ('.swiper-container', {direction: 'horizontal',loop: true,pagination: '.swiper-pagination',nextButton: '.swiper-button-next',prevButton: '.swiper-button-prev',});
	}else{
		var div = '<div class="swiper-slide" gift-data="没有预约金额">'+
			        	'<span class="gift-name">没有预约金额</span>'+
			        	'<img class="img" src="images/jtk.png"/>'+
			        '</div>';
		$(".swiper-wrapper").append(div);
		return;
	}		
	var gift=["300-1000元话费抽奖","500-2000元话费抽奖","ipadmini4 16G","ipadair2 16G","iphone6s 64G"];//,800元交通卡,ipadmin4 16G,ipadair2 16G,iphone6s 64G
	$("#checkGiftBtn").on("click",function(){
		var i = mySwiper.activeIndex-1;
		if(i < 0){
			i = i+index;
		}
		var giftName = gift[i];
		ajax("/financing/baseNeedOpenId.do",{action:"saveWxGiftInfo",wxResId:param.wxResId,giftName:giftName},function(result){
			if(result.state=="1"){
				window.location.href="/financing/html/appoint_success.html";
			}else{
				window.location.href="/financing/html/error.html"
			}
		})
	});
})