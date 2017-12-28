function is_weixin(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i)=="micromessenger") {
    } else {
        if(window.location.href.indexOf(".do")!=-1){
            window.location.href="html/error.html?errorMsg=请在微信中打开";
        }else{
            window.location.href="error.html?errorMsg=请在微信中打开";    
        }
        return false;
    }
}
is_weixin();
var weixin = function(param){
    wx.config({
        debug: false,
        appId: param.appId,
        timestamp: param.timestamp,
        nonceStr: param.nonceStr,
        signature: param.signature,
        jsApiList: ['chooseImage','previewImage',"uploadImage","hideOptionMenu","closeWindow"]
    });

    wx.ready(function () {
        console.log("ready");
        wx.hideOptionMenu();
    });
    wx.error(function (res) {
    	console.log(JSON.stringify(res));
    });
    
}
var url = window.location.href.split("#")[0];
ajax("/financing/get_signature.do",{urlStr:encodeURIComponent(url)},function(json){
    weixin(JSON.parse(json.data));
})
  



