package com.financing;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.financing.Constants.UrlMap;
import com.financing.util.Msg;
import com.financing.util.Util;

@Controller
public class BaseController {

	@RequestMapping(value="/baseNeedOpenId.do",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String baseNeedOpenId(HttpServletRequest request) throws Exception{
		try{

			HttpSession session =request.getSession();
			String openId = (String) session.getAttribute("openId");
			if(openId ==null){
				String code = request.getParameter("code");
			    if(code==null){
					return Msg.returnError("1002","not code");
				}
				openId = Util.getOpenId(code);
				if(openId ==null||"".equals(openId)){
					return Msg.returnError("1001", "not openId");
				}else{
					request.getSession().setAttribute("openId", openId);
				}
			}
			Map<String, String[]> param = request.getParameterMap();
			String action = param.get("action")[0];
			Map<String, Map<String, String>> reqeustMap = new HashMap<String, Map<String,String>>();
			UrlMap urlMap = UrlMap.getUrl(action);
			
			Map<String, String> head = Util.createHead(urlMap.getBusinessCode());
			reqeustMap.put("head", head);
			Map<String, String> body = new HashMap<String, String>();
			reqeustMap.put("body", body);
			for(Map.Entry<String, String[]> m : param.entrySet()){
				String value = new String((m.getValue()[0]).getBytes("iso-8859-1"),"utf-8");
				if("giftName".equals(m.getKey())){
					body.put("giftName", value);
				}else if(!"action".equals(m.getKey())&&!"code".equals(m.getKey())&&!"_".equals(m.getKey())){
					body.put(m.getKey(),value);
				}
			}
			body.put("openId", openId);
			JSONObject json = (JSONObject)JSON.toJSON(reqeustMap);
			String requestUrl = Constants.BASE_URL.concat(urlMap.getUrl());
			System.out.println("=============>json:"+json);
			String  resp = HttpConnect.post(requestUrl, json);
			System.out.println("=============>resultJSON:"+resp);
			return Util.responseString(resp);
		}catch(Exception e){
			e.printStackTrace();
			return Msg.returnError("1111", "runtime error");
		}
	}
	
	@RequestMapping(value="/uploadImage.do",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String uploadImage(HttpServletRequest request) throws Exception{
		String openId = (String) request.getSession().getAttribute("openId");
		String serverId = request.getParameter("serverId");
		String flag = request.getParameter("flag");
		byte[] imgByte = HttpConnect.getImg(serverId);
		String name = "";
		if("back".equals(flag)){
	    	 name = "back.jpg";
	     }else if("front".equals(flag)){
	    	 name ="front.jpg";
	     }else{
	    	 name ="head.jpg";
	     }
		String url = Constants.BASE_URL.concat(UrlMap.saveImage.getUrl());
		Map map = new HashMap();
		Map head = Util.createHead(UrlMap.saveImage.getBusinessCode());
		Map body = new HashMap();
		map.put("head",head);
		map.put("body", body);
		body.put("openId",openId);
		body.put("imageName", name);
		body.put("image",imgByte);
		String  resp = HttpConnect.post(url, (JSONObject)JSON.toJSON(map));
		Map respMap = Util.json2Map(resp);
		Map header = (Map) respMap.get("head");
		if("000000".equals(header.get("errorCode"))){
			Map bodyer = (Map) respMap.get("body");
			String imgUrl = (String) bodyer.get("imageUrl");
			String img = imgUrl.substring(imgUrl.indexOf("group"));
			Map map2 = new HashMap();
			
			String url2 = Constants.BASE_URL.concat(UrlMap.saveCustomerInfo.getUrl());
			head = Util.createHead(UrlMap.saveCustomerInfo.getBusinessCode());
			body = new HashMap();
			body.put("openId",openId);
			if("back".equals(flag)){
		    	 body.put("idPicBack", img);
		     }else{
		    	 body.put("idPicFront", img);
		    }
			map2.put("head", head);
			map2.put("body", body);
			String  resp2 = HttpConnect.post(url2, (JSONObject)JSON.toJSON(map2));
		}
		return Msg.returnSuccess("ok");
	}
	
	
	
	
	@RequestMapping(value="/test.do",produces="text/html;charset=UTF-8")
	@ResponseBody
	public ModelAndView test(HttpServletRequest request) throws Exception{
	        ModelAndView view = new ModelAndView("login.html");
	        return view; 
		
	}
}
