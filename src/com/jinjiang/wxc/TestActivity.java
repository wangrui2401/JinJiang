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
 * ����һ���ɹ�������������С˵�б�ķ���������
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
		          // ͨ�� HTMLParser ��ȡ�Ƽ�������Ŀ�б������ postTitleList ��
			 postTitleList = parserDwPost(); 
		 } catch (ParserException e) { 
		 e.printStackTrace(); 
		     } 
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {

			 // ��ʼ�� ListView �� adapter ����� Android �����ϵ���ʾ
			 adapter = new ArrayAdapter<String>(TestActivity.this, R.layout.dw_post_item); 
			 if(postTitleList != null && postTitleList.size() > 0) { 
			          for(String title : postTitleList) { 
			 // �� postTitleList �����������ʾ�� listview ��
			 adapter.add(title); 
			 } 
			 } 
			 setListAdapter(adapter); 
			super.onPostExecute(result);
		}
		
	}
	
	private List<String> parserDwPost() throws ParserException{ 
	     final String DW_HOME_PAGE_URL =
	    		 "http://my.jjwxc.net/backend/userinfo.php?jsid=3101461.1375504233";//������Ϣ
	    		 //"http://my.jjwxc.net/backend/logininfo.php?jsid=3101461-0.2075700170826167";//�ҵĽ���
	    		 //"http://www.jjwxc.net/bookbase.php?s_typeid=1&fw=0&ycx=0&xx=1&sd=0&lx=0&fg=0&bq=-1&submit=%B2%E9%D1%AF";//����С˵�б�
	    		 //"http://www.ibm.com/developerworks/cn"; 
	 ArrayList<String> pTitleList = new ArrayList<String>(); 
	     // ���� html parser ���󣬲�ָ��Ҫ������ҳ�� URL �ͱ����ʽ
	 Parser htmlParser = new Parser(DW_HOME_PAGE_URL); 
	 //htmlParser.setEncoding("UTF-8"); //����С˵�б�
	 htmlParser.setEncoding("GBK");//�ҵĽ���
	 
	     String postTitle = ""; 
	 // ��ȡָ���� div �ڵ㣬�� <div> ��ǩ�����Ҹñ�ǩ���������� id ֵΪ��tab1��
	     NodeList divOfTab1 = htmlParser.extractAllNodesThatMatch( 
	    new AndFilter(new TagNameFilter("table"), new HasAttributeFilter("width", "984")));//������Ϣ
	    //new AndFilter(new TagNameFilter("table"), new HasAttributeFilter("class", "cytable")));//����С˵�б�
	    //new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("id", "myjquerymenu")));//�ҵĽ���
	    
	     if(divOfTab1 != null && divOfTab1.size() > 0) { 
	 // ��ȡָ�� div ��ǩ���ӽڵ��е� <li> �ڵ�
	 NodeList itemLiList = divOfTab1.elementAt(0).getChildren().extractAllNodesThatMatch
			 (new TagNameFilter("td"), true);//�ҵĽ���
			 //(new TagNameFilter("li"), true);//�ҵĽ���
	 //(new TagNameFilter("td"), true);//����С˵�б�

	           if(itemLiList != null && itemLiList.size() > 0) { 
	                for(int i = 0; i < itemLiList.size(); ++i) { 
	 // �� <li> �ڵ���ӽڵ��л�ȡ Link �ڵ�
	 NodeList linkItem 
	 = itemLiList.elementAt(i).getChildren().extractAllNodesThatMatch 
	                          (new NodeClassFilter(LinkTag.class),true); 
	                     if(linkItem != null && linkItem.size() > 0) { 
	 // ��ȡ Link �ڵ�� Text����ΪҪ��ȡ���Ƽ����µ���Ŀ����
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
