$(function(){
    $("#scroller").mobiscroll().date();
	var currYear = (new Date()).getFullYear();  
  	//初始化日期控件
	var opt = {
		preset: 'date', 
		theme: 'default',
		display: 'modal',
		mode: 'scroller',
		lang:'zh',
		dateFormat: 'yyyymmdd',
		setText: '确定',
		cancelText: '取消',
		dateOrder: 'yyyymmdd',
		dayText: '日', monthText: '月', yearText: '年',
		showNow: false,
   		nowText: "今", 
   		minDate: new Date(),
    	startYear:currYear,
    	endYear:currYear + 100
	};
	$("#scroller").mobiscroll(opt);
	$("#ex15").on("input",function(){
		$(".msg").css("visibility","hidden");
	});
	$("#appointBtn").on("click",function(){
		var resAmount = $("#ex15").val();
		if(!resAmount){
			$(".msg").css("visibility","visible").text("请输入金额");
			return;
		}
		if(Number(resAmount)<10||Number(resAmount)>1000){
			$(".msg").css("visibility","visible").text("请输入范围在10万-1000万");
			return;	
		}
		var value = resAmount*10000;
		var resDate = $("#scroller").val();
		if(!resDate||!resDate.trim()){
			$(".msg").css("visibility","visible").text("请选择日期");
			return;
		}
		ajax("/financing/baseNeedOpenId.do",{action:"saveWxReservationInfo",resAmount:value,resDate:resDate},function(result){
			if(result.state=="1"){
				var data = JSON.parse(result.data);
				if(resAmount<30){
					window.location.href="/financing/html/appoint_success.html?flag=1";
				}else{
					window.location.href="/financing/html/check_gift.html?wxResId="+data.wxResId+"&amount="+value;	
				}
			}
		})
	});

	$("#scroller").on("change",function(){
		$(".msg").css("visibility","hidden").text("1");
		var value = $("#scroller").val();
		if(value){
			var year = value.substring(2,4);
			var month = value.substring(4,6);
			var day  = value.substring(6,8);
			$(".year").text(year);
			$(".month").text(month);
			$(".day").text(day);
		}
	});
	var scro = $("#scroller").val();
	if(scro){
		$("#scroller").trigger("change");
	}
});