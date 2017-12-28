package com.financing;

public class Constants {
	public static String FRONT_URL = "http://fmstest.4000077777.com.cn";
	
	public static String TIME_TYPE = "yyyyMMddHHmmss";// 日期格式
	public static String LIMIT_SIZE = "10";
	public static String WEI_XIN_APP_ID = "wx013cce35a95b2261";
	public static String WEI_XIN_SECRET = "eed0a3ba6b23e102d7cff4f3aeda70ba";
	public static String OPEN_ID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WEI_XIN_APP_ID+"&secret="+WEI_XIN_SECRET+"&code={CODE}&grant_type=authorization_code";
	
	public static String DOWN_IMG_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	public static String ACCESS_URL ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WEI_XIN_APP_ID+"&secret="+WEI_XIN_SECRET;
	public static String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
	
	public static String TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";
	public static String TEMPLATE_ID = "bpQx3YDDk04YuBzFr4FC9zPEyGWbm1IMdMx2WjKqnbI";//微信模版消息id
	public static String TEMPLATE_COLOR = "#173177";
	
	public static String OPEN_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+WEI_XIN_APP_ID+"&redirect_uri={targetUrl}&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	public static String MONTH_RECORD_DETAIL_URL = FRONT_URL+"/financing/monthRecord.do?yearMonth=";
	public static String[] GIFT={"300-1000元话费抽奖","500-2000元话费抽奖","ipadmin4 16G","ipadair2 16G","iphone6s 64G"};
	
	public static String SPLIT_STR = "____SPLIT_CODE____";
	public static long ONE_DAY = 1 * 24 * 60 * 60 * 1000;
	
	
	public static String VERSION = "v1.0.0";
//	public static String BASE_URL = "http://192.168.1.68:8080/fms/wechat/finance/";
//	public static String BASE_URL = "http://192.168.1.233:8081/fms/wechat/finance/";
//	public static String BASE_URL = "http://192.168.1.30:8080/fms/wechat/finance/";
	public static String BASE_URL = "http://127.0.0.1:8080/fms/wechat/finance/";
//	public static String BASE_URL = "http://139.196.105.191:8082/fms/wechat/finance/";
	public static final String TOKEN = "gavinknight";
	
	enum UrlMap{
		sentVerificationCode("1","sentVerificationCode","010000",null),//1.微信发送验证码
		checkIdentity("2","checkIdentity","010001",null),//2.微信客户身份验证
		getCustomerInfo("3","getCustomerInfo","010101","user.html"),//3.根据手机号码获取个人信息
		saveCustomerInfo("4","saveCustomerInfo","010102",null),//4.保存客户信息（邮寄地址和身份证）
		saveImage("5","saveImage","010003",null),//5.保存图片（身份证照片)
		getInvestmentList("6","getInvestmentList","010201","history_record.html"),//6.根据客户手机号码获取客户的投资记录列表
		getInvestmentInfo("7","getInvestmentInfo","010201",null),//7.根据手机号码和投资记录ID获取投资记录信息
		saveWxReservationInfo("8","saveWxReservationInfo","010301",null),//8.根据手机号和微信openID保存微预约信息
		saveWxGiftInfo("9","saveWxGiftInfo","010302",null),//9.	根据手机号和微信openID保存微预约奖品信息
		checkRegister("10","checkRegister","010002",null),
		goAppoint("11","goAppoint","","gift.html"),//页面跳转用
		getSuccessToken("12","getSuccessToken","010304",null),
		saveSuccessToken("13","saveSuccessToken","010303",null),
		getBillList("14","getBillList","010305","month_record.html"),
		getBillDetail("15","getBillDetail","010306",null)
		;
		private String id;
		private String url;
		private String businessCode;
		private String targetUrl;
		
		private UrlMap(String id,String url, String businessCode,String targetUrl) {
			this.id = id;
			this.url = url;
			this.businessCode = businessCode;
			this.targetUrl = targetUrl;
		}
		private UrlMap(){}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getBusinessCode() {
			return businessCode;
		}
		public void setBusinessCode(String businessCode) {
			this.businessCode = businessCode;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		public String getTargetUrl() {
			return targetUrl;
		}
		public void setTargetUrl(String targetUrl) {
			this.targetUrl = targetUrl;
		}
		public static UrlMap getUrl(String url){
			for(UrlMap u : UrlMap.values()){
				if(u.getUrl().equals(url)){
					return u;
				}
			}
			return null;
		}
		
	}
	public static void main(String[] args) {
		String s = "BRgMQRePkAAJ1PsQCEhlShyPpCPNywTzU6xrKUQTIP0XDtaubdPwBTrpR7kYNKP5eglyj2_c_hVE_j5VFbjEXA5Jg-oPvRO5CfRFY-gSdnIOZDiAAAALP$$$$kgt8ON7yVITDhtdwci0qeQz3diLEsBoR21zdG0i_p5fNbqPLoXH_JjQf4Nhl-WeO0Lx_Jhttf6jjUnGBdz11qA";
		String [] aaa = s.split(Constants.SPLIT_STR);
		System.out.println(aaa);
	}
}

