function getUrlParam() {
    var obj = {};
    var urlStr = window.location.href;
    if (urlStr.indexOf("?") != -1) {
        var urlStr = urlStr.substr(urlStr.indexOf("?") + 1);
        var strs = urlStr.split("&");
        for (var i = 0; i < strs.length; i++) {
            var tempArr = strs[i].split("=");
			var processedStr = window.decodeURIComponent(tempArr[1]).replace(/(\d{4})\/(\d{2})\/(\d{2})\s+(\d{2})-(\d{2})-(\d{2})/g, "$1-$2-$3 $4:$5:$6");
			obj[tempArr[0]] = processedStr;
        }
    }
    return obj;
}
function ajax(url,data,callback){
	show_loading();
	$.ajax({
		url:url,
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		cache:false,
		// type:"POST",
		data:data,
		success:function(result){
			hide_loading();
			result = JSON.parse(result);
			if(result.state=="0"){
			}
			callback(result);
		},error:function(err){
			console.info(err);
		}});
}
function ajaxCache(url,data,callback){
	show_loading();
	$.ajax({
		url:url,
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",
		cache:true,
		type:"POST",
		data:data,
		success:function(result){
			hide_loading();
			result = JSON.parse(result);
			if(result.state=="0"){
			}
			callback(result);
		}});
}

function goToPage(url){
	if(url.indexOf("http")==-1){
		url ="http://fmstest.4000077777.com.cn/"+url;
	}
	url = encodeURIComponent(url);
	window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx013cce35a95b2261&redirect_uri="+url+"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
}
function goToLogin(result){
	var url ="financing/html/login.html";
	goToPage(url);
}

function formatTime(str,format){
	var reg = /^(\d{4})(\d{2})(\d{2})$/;
	if(!format){
		return str.trim().replace(reg,"$1年$2月$3日");
	}
	var string = "$1"+format+"$2"+format+"$3"
	return str.trim().replace(reg,string);
}



function show_loading(){
    if($(".spinner-div").length>0){
        $(".spinner-div").show();
    }else{
        var div = '<div class="spinner-div"><div class="spinner"><div class="spinner-container container1"><div class="circle1"></div><div class="circle2"></div><div class="circle3"></div><div class="circle4"></div></div><div class="spinner-container container2"><div class="circle1"></div><div class="circle2"></div><div class="circle3"></div><div class="circle4"></div></div><div class="spinner-container container3"><div class="circle1"></div><div class="circle2"></div><div class="circle3"></div><div class="circle4"></div></div></div></div>';
        $("body").append(div);
    }
}

function hide_loading(){
    $(".spinner-div").hide();
}




function alertMsg(title,content,callback){
    if($(".alertMsgDiv").is("div")){
        $(".alertMsgDiv").find(".con-msg").text(content);
        return;
    }
    var div = $("<div class='alertMsgDiv'><div class='alertMsg'><div class='message'><div class='header'>"+title+"</div><div class='content'><div class='con-msg'></div></div><div class='footer'>确定</div></div></div></div>");
    div.find(".con-msg").html(content);
    div.find(".footer").on("click",function(){
        div.remove();
        if(typeof callback == "function"){
            callback();
        }
    });
    $("body").append(div);
}












