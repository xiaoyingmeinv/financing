package com.financing.util;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.financing.Constants;
import com.financing.HttpConnect;

public class Util {
	private static Logger log = LoggerFactory.getLogger(Util.class);
	
	public static Map<String,Object> json2Map(String json){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
		JSONObject jsonObject =JSON.parseObject(json);
		
		for(Entry<String,Object> entry:jsonObject.entrySet()){
			map.put(entry.getKey(), entry.getValue());
        }  
		}catch(Exception e){
			log.error("parse json error");
		}
		return map;
	}
	public static Map<String, String> createHead(String businessCode){
		Map<String, String> head = new HashMap<String, String>();
		
		
		long currTime =System.currentTimeMillis();
		String timestamp = String.valueOf(currTime);
		String nonce = getNonce();
		head.put("businessCode", businessCode);
		head.put("version", Constants.VERSION);
		head.put("requestTime",getRequestTime(currTime));
		head.put("timestamp", timestamp);
		head.put("nonce",nonce);
		head.put("requestId",getReqeustId());
		head.put("signature",getSignature(timestamp,nonce));
		return head;
	}
	
	public static String getNonce(){
		return String.valueOf(new Random().nextInt(1000));
	}
	public static  String getRequestTime(long currTime){
		SimpleDateFormat simple = new SimpleDateFormat(Constants.TIME_TYPE);
		return simple.format(currTime);
		
	}
	public static String getReqeustId(){
		return UUID.randomUUID().toString().replace("-","");
	}
	public static boolean isBlank(String s){
		if(s==null||"".equals(s)||"".equals(s.trim())){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 验证签名
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		if(isBlank(signature) || isBlank(timestamp) || isBlank(nonce)) {
			return false;
		}
		String[] arr = new String[] { Constants.TOKEN, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于我们的App
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}
	public static String getSignature(String timestamp,String nonce){
		String[] arr = new String[] { Constants.TOKEN, timestamp, nonce };
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		content = null;
		return tmpStr;
	}
	public static  String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}
	public static  String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}
	public static String getOpenId(String code){
		String url = Constants.OPEN_ID_URL;
		String target = url.replace("{CODE}", code);
		
		String result = HttpConnect.doGet(target);
		Map<String, Object> map = Util.json2Map(result);
		String openId = (String)map.get("openid");
		return openId;
	}
	public static String responseString(String resp){
		Map<String, Object> map = json2Map(resp);
		Map<String, String> head = (Map<String, String>)map.get("head");
		Map<String, String> body = (Map<String, String>)map.get("body");
		if("000000".equals(head.get("errorCode"))){
			return Msg.returnSuccess(JSONObject.toJSONString(body));
		}else{
			return Msg.returnError(head.get("errorMsg"), null);
		}
	}
	public static String getDate2(String date){
		if(date==null||"".equals(date)){
			return "";
		}
		Date d = new Date(Long.valueOf(date));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		return simpleDateFormat.format(d);
	}
	public static String getDate3(String yearMonth){
		if(yearMonth==null||"".equals(yearMonth)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(yearMonth.substring(0,4)).append("年").append(yearMonth.substring(4,6)).append("月");
		return sb.toString();
	}
	public static String getDate(String date){
		if(date==null||"".equals(date)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(date.substring(0,4)).append("年").append(date.substring(4,6)).append("月").append(date.subSequence(6, 8)).append("日");
		return sb.toString();
	}
	public static String getNowYearAndMonth(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		return format.format(date);
	}
	public static String getNowYear(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		return format.format(date);
	}
	public static int getAmount(Object amount){
		if(amount==null||"".equals(amount.toString())){
			return 0;
		}
		BigDecimal bd = new BigDecimal(amount.toString());
		return bd.intValue()/10000;
	}
	public static String getAmountString(String amount){
		if(amount==null||"".equals(amount)){
			return "";
		}
		if(amount.indexOf(".")!=-1){
			return amount.substring(0,amount.length()-7);
		}else{
			return amount.substring(0,amount.length()-4);
		}
	}
}
