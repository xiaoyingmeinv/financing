$(function(){
	var param = getUrlParam();
	if(param.address){
		$("textarea").val(param.address);
	}
	$("#saveAddress").on("click",function(){
		var value = $("textarea").val();
		if(value){
			value = value.trim();
		}
		ajax("/financing/baseNeedOpenId.do",{action:"saveCustomerInfo",registerAddress:value},function(result){
			if(result.state==1){
				window.localStorage.setItem("address",value);
				alertMsg("温馨提示","修改成功",function(){
					window.history.go(-1);	
				});
			}
		});
	});
});