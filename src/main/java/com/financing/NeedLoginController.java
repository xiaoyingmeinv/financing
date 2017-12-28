package com.financing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.SpringLayout.Constraints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.financing.Constants.UrlMap;
import com.financing.util.Msg;
import com.financing.util.Util;
@Controller
public class NeedLoginController {

	public String needLogin(HttpServletRequest request) throws Exception{
		try {
			HttpSession session =request.getSession();
			String openId = (String) session.getAttribute("openId");
			if(openId == null){
				String code = request.getParameter("code");
			    if(code==null){
			    	return "1002";
				}
				openId = Util.getOpenId(code);
				if(openId ==null||"".equals(openId)){
					return "1001";
				}else{
					request.getSession().setAttribute("openId", openId);
				}
			}
			UrlMap urlMap = UrlMap.checkRegister;
			String requestUrl = Constants.BASE_URL.concat(urlMap.getUrl());
			Map<String, Map<String, String>> reqeustMap = new HashMap<String, Map<String,String>>();
			Map<String, String> head = Util.createHead(urlMap.getBusinessCode());
			reqeustMap.put("head", head);
			Map<String, String> body = new HashMap<String, String>();
			reqeustMap.put("body", body);
			body.put("openId", openId);
			JSONObject json = (JSONObject)JSON.toJSON(reqeustMap);
			String resp = HttpConnect.post(requestUrl, json);
			Map<String, Object> map = Util.json2Map(resp);
			
			Map<String, String> headMap = (Map<String, String>) map.get("head");
			if("000000".equals(headMap.get("errorCode"))){
				String action = request.getParameter("action");
				return "0000";
			}else{
				return "1005";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value="/user.do")
	@ResponseBody
	public ModelAndView user(HttpServletRequest request) throws Exception{
		try{
			String result = needLogin(request);
			if("0000".equals(result)){
				UrlMap urlMap = UrlMap.getCustomerInfo;
				Map<String, Map<String, String>> reqeustMap = new HashMap<String, Map<String,String>>();
				Map<String, String> head = Util.createHead(urlMap.getBusinessCode());
				reqeustMap.put("head", head);
				Map<String, String> body = new HashMap<String, String>();
				reqeustMap.put("body", body);
				body.put("openId", (String)request.getSession().getAttribute("openId"));
				JSONObject json = (JSONObject)JSON.toJSON(reqeustMap);
				String requestUrl = Constants.BASE_URL.concat(urlMap.getUrl());
				String  resp = HttpConnect.post(requestUrl, json);
				Map<String, Object> map = Util.json2Map(resp);
				Map<String, String> header = (Map<String, String>)map.get("head");
				Map<String, String> bodyer = (Map<String, String>)map.get("body");
				String addTime = bodyer.get("addTime");
				bodyer.put("addTime", Util.getDate(addTime));
				String birthday = bodyer.get("birthday");
				bodyer.put("birthday", Util.getDate(birthday));
				if("000000".equals(header.get("errorCode"))){
					return new ModelAndView("user.html", bodyer);
				}else{
					return new ModelAndView("login_error.html");
				}
			}else if("1005".equals(result)){
				Map<String,String> map = new HashMap<String, String>();
				map.put("target", "user");
				return new ModelAndView("login.html",map);
			}else{
				return new ModelAndView("login_error.html");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("login_error.html");
		}		
	}
	@RequestMapping(value="/appoint.do")
	@ResponseBody
	public ModelAndView appoint(HttpServletRequest request) throws Exception{
		try{
			String result = needLogin(request);
			if("0000".equals(result)){
				return new ModelAndView("gift.html");
			}else if("1005".equals(result)){
				Map<String,String> map = new HashMap<String, String>();
				map.put("target", "appoint");
				return new ModelAndView("login.html",map);
			}else{
				return new ModelAndView("login_error.html");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("login_error.html");
		} 
	}
	@RequestMapping(value="/record.do")
	@ResponseBody
	public ModelAndView record(HttpServletRequest request) throws Exception{
		try{
			String result = needLogin(request);
			if("0000".equals(result)){
				UrlMap urlMap = UrlMap.getInvestmentList;
				Map<String, Map<String, String>> reqeustMap = new HashMap<String, Map<String,String>>();
				Map<String, String> head = Util.createHead(urlMap.getBusinessCode());
				reqeustMap.put("head", head);
				Map<String, String> body = new HashMap<String, String>();
				reqeustMap.put("body", body);
				body.put("openId", (String)request.getSession().getAttribute("openId"));
				String limitIndex= request.getParameter("limitIndex");
				if(limitIndex==null){
					limitIndex = "1";
				}
				String limitSize = Constants.LIMIT_SIZE;
				
				body.put("limitIndex", limitIndex);
				body.put("limitSize", limitSize);
				JSONObject json = (JSONObject)JSON.toJSON(reqeustMap);
				String requestUrl = Constants.BASE_URL.concat(urlMap.getUrl());
				String  resp = HttpConnect.post(requestUrl, json);
				Map<String, Object> map = Util.json2Map(resp);
				Map<String, String> header = (Map<String, String>)map.get("head");
				Map<String, Object> bodyer = (Map<String, Object>)map.get("body");
				List<Map<String,Object>> l = (List<Map<String, Object>>) bodyer.get("list");
				if(l!=null&&l.size()>0){
					for(int i =0;i<l.size();i++){
						String loanLimit = (String)l.get(i).get("loanLimit");
						l.get(i).put("loanLimit", Util.getDate(loanLimit));
						Object amount = (Object)l.get(i).get("amount");
						l.get(i).put("amount", Util.getAmount(amount));
					}
				}
				if("000000".equals(header.get("errorCode"))){
					return new ModelAndView("history_record.html", bodyer);
				}else{
					return new ModelAndView("login_error.html");
				}
			}else if("1005".equals(result)){
				Map<String,String> map = new HashMap<String, String>();
				map.put("target", "record");
				return new ModelAndView("login.html",map);
			}else{
				return new ModelAndView("login_error.html");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("login_error.html");
		} 
	}
	
	@RequestMapping(value="/monthRecord.do")
	@ResponseBody
	public ModelAndView monthRecord(HttpServletRequest request) throws Exception{
		try{
			String yearMonth1 = request.getParameter("yearMonth");
			String result = needLogin(request);
			if("0000".equals(result)){
				UrlMap urlMap = UrlMap.getBillList;
				Map<String, Map<String, String>> reqeustMap = new HashMap<String, Map<String,String>>();
				Map<String, String> head = Util.createHead(urlMap.getBusinessCode());
				reqeustMap.put("head", head);
				Map<String, String> body = new HashMap<String, String>();
				reqeustMap.put("body", body);
				body.put("openId", (String)request.getSession().getAttribute("openId"));
				body.put("yearMonth",yearMonth1);
				JSONObject json = (JSONObject)JSON.toJSON(reqeustMap);
				String requestUrl = Constants.BASE_URL.concat(urlMap.getUrl());
				String  resp = HttpConnect.post(requestUrl, json);
				Map<String, Object> map = Util.json2Map(resp);
				Map<String, String> header = (Map<String, String>)map.get("head");
				Map<String, Object> bodyer = (Map<String, Object>)map.get("body");
				
				if("000000".equals(header.get("errorCode"))){
					List<Map<String, String>> list = (List<Map<String, String>>) bodyer.get("list");
					TreeMap dataMap = new TreeMap(new Comparator() {
						public int compare(Object o1, Object o2) {
		                      //如果有空值，直接返回0
		                      if (o1 == null || o2 == null)
		                          return 0; 
		                     return String.valueOf(o2).compareTo(String.valueOf(o1));
		               }
					});
					if(list!=null&&list.size()>0){
						for(int i =0;i<list.size();i++){
							Map<String, String> recordMap = list.get(i);
							String billDate = Util.getDate((String)recordMap.get("billDate"));
							recordMap.put("billDate",billDate);
							String yearMonth = (String)recordMap.get("yearMonth");
							if(Util.getNowYearAndMonth().equals(yearMonth)){
								yearMonth = "本月";
							}else{
								String year = yearMonth.substring(0,4);
								if(year.equals(Util.getNowYear())){
									yearMonth =yearMonth.substring(4)+"月";
								}else{
									yearMonth = year+"年"+yearMonth.substring(4)+"月";
								}
							}
							recordMap.put("yearMonth", yearMonth);
							String amount = recordMap.get("amount");
							recordMap.put("amount1", Util.getAmountString(amount));
							String sumMoney = recordMap.get("sumMoney");
							recordMap.put("sumMoney", Util.getAmountString(sumMoney));
							List<Map<String, String>> l = (List<Map<String, String>>) dataMap.get(yearMonth);
							if(l==null){
								l = new ArrayList<Map<String,String>>();
								dataMap.put(yearMonth, l);
							}
							l.add(recordMap);
						}
					}
					Map returnMap = new HashMap();
					returnMap.put("data", dataMap);
					return new ModelAndView("month_record.html", returnMap);
				}else{
					return new ModelAndView("login_error.html");
				}
			}else if("1005".equals(result)){
				Map<String,String> map = new HashMap<String, String>();
				map.put("target", "monthRecord");
				return new ModelAndView("login.html");
			}else{
				return new ModelAndView("login_error.html");
			}
		}catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("login_error.html");
		} 
	}
	
	
	@RequestMapping(value="/error.do",produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String error(HttpServletRequest request) throws Exception{       
	        return Msg.returnError("error", null); 
		
	}
}
