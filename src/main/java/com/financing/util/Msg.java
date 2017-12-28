package com.financing.util;



import java.util.Date;

import com.alibaba.fastjson.JSONObject;

public class Msg {
	private String state;
	private String msg;
	private String data;
	private Long currTime;
	
	public enum MsgState{
		error("0"),ok("1");
		private String state;
		MsgState() {}
		MsgState(String state) {
			this.state = state;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
	}
	public Msg(String state,String msg){
		this.state = state;
		this.msg = msg;
		this.currTime = new Date().getTime();
	}
	public Msg(String state,String msg,String data){
		this.state = state;
		this.msg = msg;
		this.data = data;
		this.currTime = new Date().getTime();
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public Long getCurrTime() {
		return currTime;
	}
	public void setCurrTime(Long currTime) {
		this.currTime = currTime;
	}
	/**
	 * 返回信息string
	 * @param state
	 * @param flag
	 * @param msg
	 * @return
	 */
	public static String returnError(String msg,String date){
		Msg errorMsg = new Msg(MsgState.error.getState(), msg,date);
		return JSONObject.toJSONString(errorMsg);
	}
	public static String returnSuccess(String data){
		Msg msg = new Msg(MsgState.ok.getState(),"ok",data);
		return JSONObject.toJSONString(msg);
	}
}
