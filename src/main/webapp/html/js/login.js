$(function(){
	var param = getUrlParam();
	var code = param.code;
	var target = $("#target").val();
	var timeout;
	$("#sendCode").on("click",function(){
		if($(this).hasClass("disabled")){
			return;
		}
		$(this).addClass("disabled")
		if(timeout){//timeout存在
			return alertMsg("温馨提示","已经发送了");
		}
		var $tel = $("#telphone");
		var telphone = $tel.val();
		if(!checkTel(telphone)){
			return;
		}
		ajax("/financing/baseNeedOpenId.do",{code:code,phoneNum:telphone,action:"sentVerificationCode"},function(result){
			if(result.state=="1"){
				waitCode();
			}else{
				$(".msg").css("visibility","visible").text("手机号不存在");
			}
		});		
	});
	$("#loginBtn").on("click",function(){
		var $tel = $("#telphone");
		var telphone = $tel.val();
		if(!checkTel(telphone)){
			return;
		}
		var $code = $("#verificationCode");
		var verificationCode = $code.val();
		if(!verificationCode){
			$(".msg").css("visibility","visible").text("验证码不能为空");
			return ;
		}
		ajax("/financing/baseNeedOpenId.do",{action:"checkIdentity",phoneNum:telphone,openId:window.sessionStorage.getItem("openId"),verificationCode:verificationCode},function(result){
			if(result.state==1){
				if(target=="user"){
					window.location.href="/financing/user.do";	
				}else if(target=="appoint"){
					window.location.href="/financing/appoint.do";
				}else if(target=="record"){
					window.location.href="/financing/record.do";
				}else if(target=="monthRecord"){
					window.location.href="/financing/monthRecord.do";
				}else{
					window.location.href="html/error.html";
				}

				
			}else{//验证失败
				$(".msg").css("visibility","visible").text("验证码不正确,请重新输入");
			}
		});
	});
	$("#telphone").on("input",function(){
		var value = $(this).val();
		if(value.length>=11){
			$(this).val(value.substring(0,11));
		}
		$("#sendCode").removeClass("disabled");
		if(timeout){
			clearTimeout(timeout);
			timeout = null;
			$("#sendCode").text("发送验证码");	
		}
	});
	$("#verificationCode").on("input",function(){
		var value = $(this).val();
		if(value.length>=4){
			$(this).val(value.substring(0,4));
		}
	})
	function checkTel(tel){
		var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(14[0-9]{1})|(17[0-9]{1}))+\d{8})$/; 
		if(!myreg.test(tel)){//失败
			$(".msg").css("visibility","visible").text("手机号格式不正确");
			return false;
		}
		$(".msg").css("visibility","hidden");
		return true;
	}
	function waitCode(){
		var time = 60;
		settime(time);
	}
	function settime(time){
		timeout = setTimeout(function(){
			if(time<=0){
				$("#sendCode").removeClass("disabled");
				$("#sendCode").text("重发验证码");
				clearTimeout(timeout);
				timeout=null;
			}else{
				$("#sendCode").text(time+"s");
				settime(time-1);	
			}
		},1000);
	}
	
});