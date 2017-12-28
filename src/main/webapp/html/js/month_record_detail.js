$(function(){
	var param = getUrlParam();
	var detailId = param.detailId;
	if(detailId){
		ajax("/financing/baseNeedOpenId.do",{action:"getBillDetail",billId:detailId,code:param.code},function(result){
			if(result.state=="1"){
				var data = JSON.parse(result.data);
				var amount = data.amount;
				var amount1 = "";
				$(".amount").text(data.earnMoney);
				if(amount.indexOf(".")!=-1){
					amount1 = amount.substring(0,amount.length-7);
				}else{
					amount1 = amount.substring(0,amount.length-4);
				}
				$(".amount1").text(amount1);
				$(".startTime").text(formatTime(data.beginTime,"-"));
				$(".endTime").text(formatTime(data.endTime,"-"));
				$(".yearRate").text(data.yearRate);
				$(".principal").text(data.principal);
				$(".billDate").text(formatTime(data.billDate,"-"));
				$("#serivce").attr("href","service_center.html?managerPhone="+data.managerPhone);
			}else{
				window.location.href="/financing/html/error.html";			
			}
		})
	}else{
		window.location.href="/financing/html/error.html";
	}
});