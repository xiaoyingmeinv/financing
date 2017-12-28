$(function(){
	var page = {limitIndex:1,limitSize:10};
	var dropload = $('.inner').dropload({
	    domDown : {
	        domClass   : 'dropload-down',
	        domRefresh : '<div class="dropload-refresh">↑上拉加载更多</div>',
	        domUpdate  : '<div class="dropload-update">↓释放加载</div>',
	        domLoad    : '<div class="dropload-load"><span class="loading"></span>加载中...</div>'
	    },
	    loadDownFn : function(me){
	    	page.limitIndex = page.limitIndex+1;
	        init();
	        setTimeout(function(){
	            me.resetload();
	        },1000);
	    }
	});
function init (){
	ajax("/financing/baseNeedOpenId.do",{action:"getInvestmentList",limitIndex:page.limitIndex,limitSize:page.limitSize},function(res){
		var recordList = $(".record-list");
		if(res.state=="1"){
			var data = JSON.parse(res.data);
			if(data.list&&data.list.length){
				recordList.find(".record-item");
				data.list.forEach(function(item){
					var div = $(getDiv());
					div.find(".loanLimit").text(formatTime(item.loanLimit));
					div.find(".amount").text(Math.floor(Number(item.amount/10000)));
					div.find(".investStatus").text(item.investStatus);
					div.find(".goToDetail").attr("href","/financing/html/record_detail.html?investmentInfoId="+item.investmentInfoId);
					recordList.append(div);
				});
			}else{
				if(!recordList.find(".not_data")[0]){
					recordList.append("<div class='record-item not_data'><div class='not-data'>没有投资记录了</div></div>");	
				}
				page.limitIndex--;
			}
		}
	});
}
function getDiv (){
	return '<div class="record-item">'+
				'<div class="item-l">'+
					'<div>'+
						'<label class="label">出借日期:</label>'+
						'<span class="loanLimit"></span>'+
					'</div>'+
					'<div>'+
						'<label class="label">投资金额:</label>'+
						'<span><em class="amount"></em>万</span>'+
						'<label class="label">项目状态:</label>'+
						'<span class="investStatus"></span>'+
					'</div>'+
				'</div>'+
				'<div class="item-r"><a class="goToDetail"><span class="label">查看明细>></span></a></div>'+
		'</div>';
	}
});