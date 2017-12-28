$(function(){
	var param = getUrlParam();
	/*alert(param.customerName);*/

	$(".idCard").on("click",function(){
		var $this = $(this);
		var flag = $this.attr("flag");
		wx.chooseImage({
	        count: 1, // 默认9
	        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
	        success: function (res) {
	            var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	            $this.siblings("img").attr("src",localIds[0]);
	            $this.off("click").remove();
	            wx.uploadImage({
				    localId: localIds[0], // 需要上传的图片的本地ID，由chooseImage接口获得
				    isShowProgressTips: 1, // 默认为1，显示进度提示
				    success: function (result) {
				        var serverId = result.serverId; // 返回图片的服务器端ID
				        ajax("/financing/uploadImage.do",{serverId:serverId,flag:flag},function(){});
				    }
				});
	        }
	    });
	});
});