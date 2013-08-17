package com.jinjiang.wxc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.jinjiang.wxc.util.Manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class TestVipActivity extends Activity {
	ProgressDialog mProgressDialog;
	String mNovelContent;
	TextView mNovelTv;
	String mNovelUrl = "http://my.jjwxc.net/onebook_vip.php?novelid=1535608&chapterid=34";
	public final static String EXTRA_NOVEL_CONTENT_URL = "NOVEL_CONTENT_URL";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);
		mNovelTv = (TextView)findViewById(R.id.novel_content);
		
//		mNovelUrl = getIntent().getStringExtra(EXTRA_NOVEL_CONTENT_URL);
		if(mNovelUrl != null && !mNovelUrl.equals("")) {
			NovelInforAsyncTask novelTask = new NovelInforAsyncTask();
			novelTask.execute(mNovelUrl);
		} 
	}
	
	private class NovelInforAsyncTask extends AsyncTask<String, Integer, Boolean> {
		
		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(TestVipActivity.this);
			mProgressDialog.setTitle(R.string.loading);
			mProgressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				mNovelContent = getNovelContent(params[0]);
				return true;
			} catch (ParserException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
 
			if(mNovelContent != null) {
//				mNovelTv.setText(mNovelContent);
				mNovelTv.setText(Html.fromHtml(mNovelContent));
			}
			super.onPostExecute(result);
		}
		
	}
	
	private String getNovelContent(String url) throws ParserException { 
		String novelContent = "";
		ArrayList<HashMap<String, Object>> mNovelIndexList = new ArrayList<HashMap<String, Object>>();
	    
		URL url1;
		try {
			url1 = new URL(mNovelUrl);
	   	 	URLConnection urlConnection = url1.openConnection();
		    String cookie = Manager.getInstance().getCookie();
		    urlConnection.setRequestProperty("Cookie", cookie);
		    // 创建 html parser 对象，并指定要访问网页的 URL 和编码格式
			Parser htmlParser = new Parser(url); 
			htmlParser.setEncoding("gb2312");

			NodeList novelTable = htmlParser.extractAllNodesThatMatch(
				 new AndFilter(new TagNameFilter("div"), 
						 new HasAttributeFilter("class", "noveltext")));
			
			if(novelTable != null && novelTable.size() > 0) { 
				Div node = (Div)novelTable.elementAt(0);
				novelContent = node.toHtml();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    return novelContent; 
	}
}
