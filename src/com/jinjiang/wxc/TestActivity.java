package com.jinjiang.wxc;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
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

/**
 * 这是一个成功解析晋江言情小说列表的范例，保留
 * @author echo
 *
 */
public class TestActivity extends ListActivity {
	private ArrayAdapter<String> adapter; 
	private List<String> postTitleList = new ArrayList<String>(); 
	
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
	 super.onCreate(savedInstanceState); 
	 //setContentView(R.layout.test); 
	 parseHtmlAsyncTask task = new parseHtmlAsyncTask();
	 task.execute("");
	 } 
	
	private class parseHtmlAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try { 
		          // 通过 HTMLParser 获取推荐文章题目列表，存放于 postTitleList 中
			 postTitleList = parserDwPost(); 
		 } catch (ParserException e) { 
		 e.printStackTrace(); 
		     } 
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {

			 // 初始化 ListView 的 adapter 完成在 Android 界面上的显示
			 adapter = new ArrayAdapter<String>(TestActivity.this, R.layout.dw_post_item); 
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
	
	private List<String> parserDwPost() throws ParserException{ 
	     final String DW_HOME_PAGE_URL =
	    		 "http://my.jjwxc.net/backend/userinfo.php?jsid=3101461.1375504233";//基本信息
	    		 //"http://my.jjwxc.net/backend/logininfo.php?jsid=3101461-0.2075700170826167";//我的晋江
	    		 //"http://www.jjwxc.net/bookbase.php?s_typeid=1&fw=0&ycx=0&xx=1&sd=0&lx=0&fg=0&bq=-1&submit=%B2%E9%D1%AF";//言情小说列表
	    		 //"http://www.ibm.com/developerworks/cn"; 
	 ArrayList<String> pTitleList = new ArrayList<String>(); 
	     // 创建 html parser 对象，并指定要访问网页的 URL 和编码格式
	 Parser htmlParser = new Parser(DW_HOME_PAGE_URL); 
	 //htmlParser.setEncoding("UTF-8"); //言情小说列表
	 htmlParser.setEncoding("GBK");//我的晋江
	 
	     String postTitle = ""; 
	 // 获取指定的 div 节点，即 <div> 标签，并且该标签包含有属性 id 值为“tab1”
	     NodeList divOfTab1 = htmlParser.extractAllNodesThatMatch( 
	    new AndFilter(new TagNameFilter("table"), new HasAttributeFilter("width", "984")));//基本信息
	    //new AndFilter(new TagNameFilter("table"), new HasAttributeFilter("class", "cytable")));//言情小说列表
	    //new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "myjquerymenu")));//我的晋江
	    
	     if(divOfTab1 != null && divOfTab1.size() > 0) { 
	 // 获取指定 div 标签的子节点中的 <li> 节点
	 NodeList itemLiList = divOfTab1.elementAt(0).getChildren().extractAllNodesThatMatch
			 (new TagNameFilter("td"), true);//我的晋江
			 //(new TagNameFilter("li"), true);//我的晋江
	 //(new TagNameFilter("td"), true);//言情小说列表

	           if(itemLiList != null && itemLiList.size() > 0) { 
	                for(int i = 0; i < itemLiList.size(); ++i) { 
	 // 在 <li> 节点的子节点中获取 Link 节点
	 NodeList linkItem 
	 = itemLiList.elementAt(i).getChildren().extractAllNodesThatMatch 
	                          (new NodeClassFilter(LinkTag.class),true); 
	                     if(linkItem != null && linkItem.size() > 0) { 
	 // 获取 Link 节点的 Text，即为要获取的推荐文章的题目文字
	 postTitle = ((LinkTag)linkItem.elementAt(0)).getLinkText(); 
	 System.out.println(postTitle); 
	                          pTitleList.add(postTitle); 
	 } 
	 } 
	            } 
	         } 
	         return pTitleList; 
	  } 
	
}
