package com.financing;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.financing.util.DateSecret;
import com.financing.util.Msg;
import com.financing.util.Util;

@Controller
public class WeiXinController {
	
	
	@RequestMapping(value="/get_signature.do",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String getSignature(HttpServletRequest req) throws Exception{
		String url = URLDecoder.decode(req.getParameter("urlStr"),"utf-8");

		long nowTime = create_timestamp();
		String token_ticket = HttpConnect.getSuccessToken();
		
		String jsapiTicket = token_ticket.split(Constants.SPLIT_STR)[1];
		
		Map<String,String> ret = new HashMap<String, String>();
		ret = sign(jsapiTicket, url,nowTime);
		ret.put("appId",Constants.WEI_XIN_APP_ID);
		return Msg.returnSuccess(JSONObject.toJSONString(ret));
	}
	public  Map<String, String> sign(String jsapi_ticket, String url,long nowTime) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = Long.toString(nowTime);
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
	}
	
	@RequestMapping(value="/sendTemplate.do",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String sendTemplate(HttpServletRequest request){
		try{
			String accessToken = HttpConnect.getSuccessToken().split(Constants.SPLIT_STR)[0];
			String url = Constants.TEMPLATE_URL.replace("{ACCESS_TOKEN}", accessToken);
			String parameters  = request.getParameter("parameters");
			if(parameters!=null){
				String param = URLDecoder.decode(parameters,"UTF-8");
				String parameterData = DateSecret.decryptDES(param);
				Map<String, Object> json = Util.json2Map(parameterData);
				Map head = (Map) json.get("head");
				String timestamp = (String) head.get("timestamp");
				String nonce =(String)head.get("nonce");
				String signature = (String)head.get("signature");
				boolean check = Util.checkSignature(signature, timestamp, nonce);
				if(check){
					Map body = (Map)json.get("body");
					String openId = (String) body.get("openId");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("touser", openId);
					map.put("template_id",Constants.TEMPLATE_ID);
					String yearMonth = (String)body.get("yearMonth");
					String targetUrl = Constants.MONTH_RECORD_DETAIL_URL.concat(yearMonth);
					map.put("url", Constants.OPEN_URL.replace("{targetUrl}", URLEncoder.encode(targetUrl)));
					
					Map<String, Object> data = new HashMap<String, Object>();
					map.put("data", data);
					Map<String,Object> map1 = new HashMap<String,Object>();
					map1.put("value", body.get("billDate"));
					map1.put("color", Constants.TEMPLATE_COLOR);
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("value", body.get("name"));
					map2.put("color",Constants.TEMPLATE_COLOR);
					
					Map<String, Object> map3 = new HashMap<String, Object>();
					map3.put("value", Util.getAmountString((String)body.get("sumAmount"))+"万元");
					map3.put("color", Constants.TEMPLATE_COLOR);
					
					Map<String, Object> map4 = new HashMap<String, Object>();
					map4.put("value", body.get("sumMonthRate")+"元");
					map4.put("color", Constants.TEMPLATE_COLOR);
					
					Map<String, Object> map5 = new HashMap<String, Object>();
					map5.put("value", "您的账单日到啦~");
					map5.put("color", Constants.TEMPLATE_COLOR);
					
//					Map<String, Object> map6 = new HashMap<String, Object>();
//					map6.put("value", "感谢您的使用（此模板每月仅发送两次）");
//					map6.put("color", Constants.TEMPLATE_COLOR);
					
					data.put("first", map5);
					data.put("keyword1", map1);//到帐日期
					data.put("keyword2", map2);//名字
					data.put("keyword3", map3);//总额
					data.put("keyword4", map4);//总息
//					data.put("remark", map6);//
					System.out.println("------------->map:"+map);
					JSONObject resp = HttpConnect.postWx(url, map);
					System.out.println("--------->resp:"+resp);
					Map<String, Object> respMap = Util.json2Map(resp.toJSONString());
					String code =respMap.get("errcode")+"";
					if("0".equals(code)){
						return Msg.returnSuccess(resp.toJSONString());
					}else{
						return Msg.returnError(resp.toJSONString(),"");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return Msg.returnError("error","");
		
	}
	
	
	
	
	
	
	
	
	private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    private  String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private  long create_timestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
