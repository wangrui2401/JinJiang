package com.jinjiang.wxc.util;

public class Manager {
	
	private static Manager mManager;
	private String cookie;
	private String jsid;
	
	private Manager() {
		
	}
	
	public static Manager getInstance() {
		if(mManager == null) {
			mManager = new Manager();
		}
		return mManager;
	}
	
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	
	public String getCookie() {
		return cookie;
	}
	
	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
	
	public String getJsid() {
		return jsid;
	}
}
