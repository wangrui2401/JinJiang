package com.jinjiang.wxc;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class YQListActivity extends ListActivity {
	
	private ListAdapter mNovelAdapter; 
	private List<HashMap<String, Object>> mNovelList;
	ProgressDialog mProgressDialog;
	final String JJ_WXC_URL = "http://www.jjwxc.net/"; 
    final String JJ_WXC_YQ_NOVEL = "http://www.jjwxc.net/bookbase.php?s_typeid=1&fw=0&ycx=0&xx=1&sd=1&lx=0&fg=0&bq=-1&submit=%B2%E9%D1%AF";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		mNovelList = new ArrayList<HashMap<String, Object>>();
		YQListAsyncTask searchTask = new YQListAsyncTask();
		searchTask.execute("");
	}

	private class YQListAsyncTask extends AsyncTask<String, Integer, Boolean> {
	
		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(YQListActivity.this);
			mProgressDialog.setTitle(R.string.loading);
			mProgressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				mNovelList = getNovelList();
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
			// ��ʼ�� ListView �� adapter ����� Android �����ϵ���ʾ
			mNovelAdapter = new SimpleAdapter(YQListActivity.this, 
					mNovelList, R.layout.novel_item, new String[]{"novel", "author"}, 
					new int[]{R.id.novel_name, R.id.novel_author});
			setListAdapter(mNovelAdapter); 
			super.onPostExecute(result);
		}
		
	}
	
	private List<HashMap<String, Object>> getNovelList() throws ParserException { 

	     ArrayList<HashMap<String, Object>> mNovelList = new ArrayList<HashMap<String, Object>>();
	     
	     // ���� html parser ���󣬲�ָ��Ҫ������ҳ�� URL �ͱ����ʽ
		 Parser htmlParser = new Parser(JJ_WXC_YQ_NOVEL); 
		 htmlParser.setEncoding("gb18030");

		 // ��ȡָ���� table �ڵ㣬�� <table> ��ǩ�����Ҹñ�ǩ���������� class ֵΪ��cytable��
		 NodeList novelTable = htmlParser.extractAllNodesThatMatch( 
				 new AndFilter(new TagNameFilter("table"), new HasAttributeFilter("class", "cytable")));//������Ϣ
		
		 if(novelTable != null && novelTable.size() > 0) { 
			 // ��ȡָ�� table ��ǩ���ӽڵ��е� <td> �ڵ�
			 NodeList itemLiList = novelTable.elementAt(0).getChildren().extractAllNodesThatMatch
					 (new TagNameFilter("td"), true);
	
			 if(itemLiList != null && itemLiList.size() > 0) { 
				 String content = "", novelUrl = ""; 
				 for(int i = 0; i+1 < itemLiList.size(); i = i+2) { 
					 HashMap map = new HashMap();
					 // ��ȡ����
					 NodeList authorItem = itemLiList
							 .elementAt(i).getChildren().extractAllNodesThatMatch(
									 new NodeClassFilter(LinkTag.class),true);
					 if(authorItem != null && authorItem.size() > 0) {
						 content = ((LinkTag)authorItem.elementAt(0)).getLinkText();
						 map.put("author", content);
					 }
					 // ��ȡС˵��
					 NodeList novelItem = itemLiList
							 .elementAt(i+1).getChildren().extractAllNodesThatMatch(
									 new NodeClassFilter(LinkTag.class),true);
					 if(novelItem != null && novelItem.size() > 0) {
						 content = ((LinkTag)novelItem.elementAt(0)).getLinkText();
						 novelUrl = ((LinkTag)novelItem.elementAt(0)).getLink();
						 map.put("novel", content);
						 map.put("url", novelUrl);
						 mNovelList.add(map);
					 }
				 } 
			 } 
		 }
	     return mNovelList; 
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		HashMap map = mNovelList.get(position);
		String url = (String)map.get("url");
		if(url != null) {
			if(!url.contains(JJ_WXC_URL) && !url.equals("")) {
				url = JJ_WXC_URL + url; 
			}
//			Uri uri = Uri.parse(url);  
//			Intent it = new Intent(Intent.ACTION_VIEW, uri);  
//			startActivity(it);
			Intent intent = new Intent(YQListActivity.this, NovelIndexActivity.class);
			intent.putExtra(NovelIndexActivity.EXTRA_NOVEL_URL, url);
			YQListActivity.this.startActivity(intent);
		}
		super.onListItemClick(l, v, position, id);
	}
}
