package com.jinjiang.wxc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.jinjiang.wxc.util.Manager;

public class TestCookieActivity extends ListActivity {
	private ArrayAdapter<String> adapter; 
	private List<String> postTitleList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ParseHtmlAsyncTask userInfoTask = new ParseHtmlAsyncTask();
		userInfoTask.execute("");
	}
	
	private class ParseHtmlAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				postTitleList = getMyJJInfo();
				return true;
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// 初始化 ListView 的 adapter 完成在 Android 界面上的显示
			adapter = new ArrayAdapter<String>(TestCookieActivity.this, R.layout.dw_post_item); 
			if(postTitleList != null && postTitleList.size() > 0) { 
				for(String title : postTitleList) { 
					// 将 postTitleList 里面的内容显示在 listview 中
					adapter.add(title); 
				} 
			} 
			setListAdapter(adapter); 
			super.onPostExecute(result);
		}
		
	}
	
	private List<String> getMyJJInfo() throws ParserException { 
	     final String USER_INFO_PAGE_URL =
	    		 "http://my.jjwxc.net/backend/userinfo.php?jsid=13560991.1375675542";//基本信息
	     ArrayList<String> pTitleList = new ArrayList<String>();
	     
	     try {
	    	 URL url = new URL(USER_INFO_PAGE_URL);
	    	 URLConnection urlConnection = url.openConnection();
		     String cookie = Manager.getInstance().getCookie();
		     urlConnection.setRequestProperty("Cookie", cookie);
		      
		     // 创建 html parser 对象，并指定要访问网页的 URL 和编码格式
		     Parser htmlParser = new Parser(urlConnection); 
		     String encoding = urlConnection.getContentEncoding();
		     htmlParser.setEncoding("gb2312");//基本信息
		 
		     String postTitle = ""; 
		     // 获取指定的 div 节点，即 <div> 标签，并且该标签包含有属性 id 值为“tab1”
		     NodeList divOfTab1 = htmlParser.extractAllNodesThatMatch( 
		    		 new AndFilter(new TagNameFilter("table"), new HasAttributeFilter("width", "984")));//基本信息
		    
		     if(divOfTab1 != null && divOfTab1.size() > 0) { 
		    	 // 获取指定 div 标签的子节点中的 <li> 节点
		    	 NodeList itemLiList = divOfTab1.elementAt(0).getChildren().extractAllNodesThatMatch
		    			 (new TagNameFilter("b"), true);//基本信息

		    	 if(itemLiList != null && itemLiList.size() > 0) { 
		    		 for(int i = 0; i < itemLiList.size(); ++i) { 
		    			 // 在 <li> 节点的子节点中获取 Link 节点
		    			 NodeList linkItem = itemLiList
		    					 .elementAt(i).getChildren().extractAllNodesThatMatch(
		    							 new NodeClassFilter(LinkTag.class),true); 
		    			 if(linkItem != null && linkItem.size() > 0) { 
		    				 // 获取 Link 节点的 Text，即为要获取的推荐文章的题目文字
		    				 postTitle = ((LinkTag)linkItem.elementAt(0)).getLinkText(); 
		    				 System.out.println(postTitle); 
		    				 pTitleList.add(postTitle); 
		    			 } 
		    		 } 
		    	 } 
		     } 
	     } catch (MalformedURLException e) {
	    	 e.printStackTrace();
	     } catch (IOException e) {
			e.printStackTrace();
	     }
	     return pTitleList; 
	}
	
	private String getUrlString(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();    
        String line = null; 
        try {
			while((line = br.readLine()) != null) {
				sb.append(line + "/n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return sb.toString();
	}
	
}
