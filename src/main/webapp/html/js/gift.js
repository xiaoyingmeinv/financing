$(function(){
	var param = getUrlParam();
	ajax("/financing/needLogin.do",{code:param.code},function(result){
		if(result.state=="1"){

		}else{
			goToLogin(result);
		}
	});
});