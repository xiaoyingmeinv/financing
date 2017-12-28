$(function(){
	var param = getUrlParam();
	if(!param.investmentInfoId){
		return WeixinJSBridge.call('closeWindow');
	}
	$(".btn-txt").text("查看项目资料");
	ajax("/financing/baseNeedOpenId.do",{action:"getInvestmentInfo",investmentInfoId:param.investmentInfoId},function(result){
		if(result.state=="1"){
			var data = JSON.parse(result.data);
			for(var key in data){
				$("."+key).text(data[key]);
			}
			// var monthRate = String(data.monthRate);
			// if(monthRate.indexOf(".")!=-1){
			// 	monthRate +=".00";
			// }
			// $(".monthRate").text(monthRate);
			$("#amount").text(Math.floor(Number(data.amount)/10000));
			$("#beginTime").text(formatTime(data.beginTime,"/"));
			$("#endTime").text(formatTime(data.endTime,"/"));
			if(data.projectMsgUrl){
				$("#showPDF").attr("data-href",data.projectMsgUrl);	
			}else{
				$("#showPDF").find("span").text("无项目资料信息");
			}
			if(data.picUrl){
				$(".icon-counselor").css("background","url("+data.picUrl+")");	
			}
			$(".tel-a").attr("href","tel:"+data.phone);
			
		}
	})
	$("#showPDF").on("click",function(){
		var hrefStr = $(this).attr("data-href");
		if(!hrefStr){
			return;
		}
		if($(this).hasClass("disabled")){
			return;
		}else{
			$(this).addClass("disabled");
			$(this).find("span").text("打开中...");
			window.location.href=hrefStr;
		}
	})
});