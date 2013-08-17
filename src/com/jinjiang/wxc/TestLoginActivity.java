package com.jinjiang.wxc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.jinjiang.wxc.util.CookieUtil;
import com.jinjiang.wxc.util.Manager;

public class TestLoginActivity extends Activity {
	
	//主要是记录用户会话过程中的一些用户的基本信息
	private HashMap<String, String> session =new HashMap<String, String>();
	List<Cookie> cookies;
	String jsid;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		LoginAsyncTask task = new LoginAsyncTask();
		task.execute("");
	} 
	
	private class LoginAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			return login();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result) {
//				Intent intent = new Intent(TestLoginActivity.this, TestCookieActivity.class);
				Intent intent = new Intent(TestLoginActivity.this, TestVipActivity.class);
				TestLoginActivity.this.startActivity(intent);
			}
			super.onPostExecute(result);
		}
		
	}
	
	private boolean login() {
    	DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpPost mPost = new HttpPost("http://my.jjwxc.net/login.php?action=login");
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
//        nvps.add(new BasicNameValuePair("loginname", "443912790@qq.com"));
		nvps.add(new BasicNameValuePair("loginname", "mokena@live.cn"));
        nvps.add(new BasicNameValuePair("loginpassword", "2008010299"));
        try {
			mPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
    			
		try {
			HttpResponse response = mHttpClient.execute(mPost);
			int res = response.getStatusLine().getStatusCode();
			
			if (res == 200) {
				cookies = mHttpClient.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
		            Log.i("jinjiang", "None");
		        } else {
		            for (int i = 0; i < cookies.size(); i++) {
		            	Log.i("jinjiang", "- " + cookies.get(i).toString());
		            }
		            StringBuffer sb = new StringBuffer();
		            for (int i = 0; i < cookies.size(); i++) {
		                Cookie cookie = cookies.get(i);
		                String cookieName = cookie.getName();
		                String cookieValue = cookie.getValue();
		                if(cookieName.equals("token")) {
		                	String[] tokens = CookieUtil.decodeToken(cookieValue);
		                	if(tokens.length > 0) {
		                		jsid = tokens[0];	
		                		Manager.getInstance().setJsid(jsid);
		                	}
		                	 
		                	Log.i("jinjiang", "jsid is " + jsid);
		                }
		                if (!TextUtils.isEmpty(cookieName)
		                         && !TextUtils.isEmpty(cookieValue)) {
		                    sb.append(cookieName + "=" );
		                    sb.append(cookieValue + ";" );
		               }
		           }
		           Manager.getInstance().setCookie(sb.toString());
		           mHttpClient.getConnectionManager().shutdown();
		           return true;
		        }		
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mHttpClient.getConnectionManager().shutdown();
    	return false;
	}
	
}
