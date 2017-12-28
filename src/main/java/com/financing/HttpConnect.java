package com.financing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.financing.Constants.UrlMap;
import com.financing.util.DateSecret;
import com.financing.util.Util;


public class HttpConnect {
	
	  private Logger log = LoggerFactory.getLogger(HttpConnect.class);
	  public static JSONObject doPost(String url,JSONObject json){
	    HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(url);
	    JSONObject response = null;
	    try {
	      String param = "parameters="+DateSecret.encryptDES(json.toJSONString());
	      System.out.println(param);
	      StringEntity s = new StringEntity(param);
	      s.setContentEncoding("UTF-8");
//	      s.setContentType("application/x-www-form-urlencoded");//发送json数据需要设置contentType
	      post.setEntity(s);
	      HttpResponse res = client.execute(post);
	      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	        HttpEntity entity = res.getEntity();
	        String result = EntityUtils.toString(res.getEntity());// 返回json格式：
	        response = JSONObject.parseObject(result);
	      }
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	    return response;
	  }
	  
	  public static String post(String url,JSONObject json) throws Exception{
		    String parameterData = DateSecret.encryptDES(json.toJSONString());
		    parameterData = "parameters="+URLEncoder.encode(parameterData,"UTF-8");
		    OutputStream outputStream = null;
	        OutputStreamWriter outputStreamWriter = null;
	        InputStream inputStream = null;
	        InputStreamReader inputStreamReader = null;
	        BufferedReader reader = null;
	        StringBuffer resultBuffer = new StringBuffer();
	        String tempLine = null;
		  try {
	        URL localURL = new URL(url);
	        URLConnection connection = localURL.openConnection();
	        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
	        
	        httpURLConnection.setDoOutput(true);
	        httpURLConnection.setRequestMethod("POST");
	        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
	        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterData.length()));
	            outputStream = httpURLConnection.getOutputStream();
	            outputStreamWriter = new OutputStreamWriter(outputStream);
	            
	            outputStreamWriter.write(parameterData.toString());
	            outputStreamWriter.flush();
	            
	            if (httpURLConnection.getResponseCode() >= 300) {
	                throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
	            }
	            
	            inputStream = httpURLConnection.getInputStream();
	            inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
	            reader = new BufferedReader(inputStreamReader);
	            
	            while ((tempLine = reader.readLine()) != null) {
	                resultBuffer.append(tempLine);
	            }
	            
	        } finally {
	            
	            if (outputStreamWriter != null) {
	                outputStreamWriter.close();
	            }
	            
	            if (outputStream != null) {
	                outputStream.close();
	            }
	            
	            if (reader != null) {
	                reader.close();
	            }
	            
	            if (inputStreamReader != null) {
	                inputStreamReader.close();
	            }
	            
	            if (inputStream != null) {
	                inputStream.close();
	            }
	            
	        }

	        return resultBuffer.toString();
	  }
	  
	  //TODO 这里有access_token
	  public static byte[] getImg(String serverId){
		  HttpURLConnection conn = null;
		  try{
				String access_token = getSuccessToken().split(Constants.SPLIT_STR)[0];
				String requestUrl = Constants.DOWN_IMG_URL.replace("ACCESS_TOKEN", access_token).replace("MEDIA_ID", serverId);
				URL url = new URL(requestUrl); 
			    conn = (HttpURLConnection) url.openConnection(); 
			    conn.setDoInput(true); 
			    conn.setRequestMethod("GET"); 
			    conn.setConnectTimeout(30000); 
			    conn.setReadTimeout(30000); 
			    BufferedInputStream bis = new BufferedInputStream( 
			        conn.getInputStream()); 
			    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
			    byte[] buff = new byte[100];  
			    int rc = 0;  
			    while ((rc = bis.read(buff, 0, 100)) > 0) {  
			      swapStream.write(buff, 0, rc);  
			    }  
			    byte[] filebyte = swapStream.toByteArray();  
			    return filebyte;
			  } catch (Exception e) { 
			    e.printStackTrace(); 
			  } finally { 
			    if(conn != null){ 
			      conn.disconnect(); 
			    } 
			}
		  return null;
	  }
	  
	  public static JSONObject postWx(String url,Map<String,Object> map){
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(url);
		    JSONObject response = null;
		    try {
		      StringEntity s = new StringEntity(JSONObject.toJSONString(map),"utf-8");
		      s.setContentType("application/json");//发送json数据需要设置contentType
		      post.setEntity(s);
		      HttpResponse res = client.execute(post);
		      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
		        HttpEntity entity = res.getEntity();
		        String result = EntityUtils.toString(res.getEntity());// 返回json格式：
		        response = JSONObject.parseObject(result);
		      }
		    } catch (Exception e) {
		      throw new RuntimeException(e);
		    }
		    return response;
		  }
	  public static String doGet(String url){
		  HttpClient client = new DefaultHttpClient();
		  HttpGet get = new HttpGet(url);
		  
		  try {
			HttpResponse res = client.execute(get);
			if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
		        HttpEntity entity = res.getEntity();
		        return EntityUtils.toString(res.getEntity());// 返回json格式：
		      }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	  }
	  
	  
	  
	  public static String getSuccessToken() throws Exception{
			UrlMap urlMap = UrlMap.getSuccessToken;
			Map<String, Map<String, String>> reqeustMap = new HashMap<String, Map<String,String>>();
			Map<String, String> head = Util.createHead(urlMap.getBusinessCode());
			reqeustMap.put("head", head);
			Map<String, String> body = new HashMap<String, String>();
			reqeustMap.put("body", body);
			body.put("openId", "123123");
			JSONObject json = (JSONObject)JSON.toJSON(reqeustMap);
			String requestUrl = Constants.BASE_URL.concat(urlMap.getUrl());
			String  resp = post(requestUrl, json);
			Map<String, Object> map = Util.json2Map(resp);
			Map<String, String> header = (Map<String, String>)map.get("head");
			Map<String, String> bodyer = (Map<String, String>)map.get("body");
			 String token = bodyer.get("successtoken");
			 if(token==null){
				String s = HttpConnect.doGet(Constants.ACCESS_URL);
				Map<String, Object>  result = Util.json2Map(s);
				String access_token = (String)result.get("access_token");
				
				String getTicketUrl = Constants.TICKET_URL.concat("&access_token=").concat(access_token);
				String ticket = HttpConnect.doGet(getTicketUrl);
				Map<String, Object> tmap = Util.json2Map(ticket);
				String jsapiTicket = (String)tmap.get("ticket");
				token = access_token.concat(Constants.SPLIT_STR).concat(jsapiTicket);
				
				urlMap = UrlMap.saveSuccessToken;
				
				head = Util.createHead(urlMap.getBusinessCode());
				reqeustMap.put("head", head);
				body.put("successToken", token);
				JSONObject setJSON = (JSONObject)JSON.toJSON(reqeustMap);
				String setUrl = Constants.BASE_URL.concat(urlMap.getUrl());
				String  resp1 = post(setUrl, setJSON);
				return token;
			 }else{
				 return token;
			 }
		}
}
	
