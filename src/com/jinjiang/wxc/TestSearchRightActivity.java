package com.jinjiang.wxc;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class TestSearchRightActivity extends ListActivity {
	private ArrayAdapter<String> adapter; 
	private List<String> postTitleList = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SearchConditionAsyncTask searchTask = new SearchConditionAsyncTask();
		searchTask.execute("");
	}
	
	private class SearchConditionAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				postTitleList = getSearchCondition();
				return true;
			} catch (ParserException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// ��ʼ�� ListView �� adapter ����� Android �����ϵ���ʾ
			adapter = new ArrayAdapter<String>(TestSearchRightActivity.this, R.layout.dw_post_item); 
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
	
	private List<String> getSearchCondition() throws ParserException { 
	     final String USER_INFO_PAGE_URL =
	    		 "http://www.jjwxc.net/fenzhan/yq/";//������Ϣ
	     ArrayList<String> pTitleList = new ArrayList<String>();
	     
	     // ���� html parser ���󣬲�ָ��Ҫ������ҳ�� URL �ͱ����ʽ
		 Parser htmlParser = new Parser(USER_INFO_PAGE_URL); 
		 htmlParser.setEncoding("gb18030");//������Ϣ
 
		 String postTitle = ""; 
		 // ��ȡָ���� div �ڵ㣬�� <div> ��ǩ�����Ҹñ�ǩ���������� id ֵΪ��tab1��
		 NodeList divOfTab1 = htmlParser.extractAllNodesThatMatch( 
				 new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "search_right")));//������Ϣ
		
		 if(divOfTab1 != null && divOfTab1.size() > 0) { 
			 // ��ȡָ�� div ��ǩ���ӽڵ��е� <li> �ڵ�
			 NodeList itemLiList = divOfTab1.elementAt(0).getChildren().extractAllNodesThatMatch
					 (new TagNameFilter("select"), true);

			 if(itemLiList != null && itemLiList.size() > 0) { 
				 for(int i = 0; i < itemLiList.size(); ++i) { 
					 // �� <li> �ڵ���ӽڵ��л�ȡ Link �ڵ�
					 NodeList linkItem = itemLiList
							 .elementAt(i).getChildren().extractAllNodesThatMatch(
									 new TagNameFilter("option"), true); 
					 for(int j = 0; j < linkItem.size(); j++) { 
						 // ��ȡ Link �ڵ�� Text����ΪҪ��ȡ���Ƽ����µ���Ŀ����
						 postTitle = ((OptionTag)linkItem.elementAt(j)).getOptionText();//((LinkTag)linkItem.elementAt(0)).getLinkText(); 
						 System.out.println(postTitle); 
						 pTitleList.add(postTitle); 
					 } 
				 } 
			 } 
		 }
	     return pTitleList; 
	}
}
