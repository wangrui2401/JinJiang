package com.jinjiang.wxc.util;

public class Manager {
	
	private static Manager mManager;
	private String cookie;
	
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
}
