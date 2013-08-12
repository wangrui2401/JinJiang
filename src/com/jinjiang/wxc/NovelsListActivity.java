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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NovelsListActivity extends ListActivity {

	public static final String EXTRA_NOVELS_LIST_URL = "novels_list_url";
	public static final String EXTRA_NOVELS_TITLE = "novels_title";
	final String JJ_WXC_URL = "http://www.jjwxc.net/";
	private ListAdapter mNovelAdapter; 
	private List<HashMap<String, Object>> mNovelList;
	ProgressDialog mProgressDialog;
	String mUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUrl = getIntent().getStringExtra(EXTRA_NOVELS_LIST_URL);
//		int titleId = getIntent().getIntExtra(EXTRA_NOVELS_TITLE, 0);
//		if(titleId > 0) {
//			getActionBar().setTitle(titleId);
//		}
		NovelListAsyncTask task = new NovelListAsyncTask();
		task.execute(mUrl);
	}
	
	private class NovelListAsyncTask extends AsyncTask<String, Integer, Boolean> {
		
		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(NovelsListActivity.this);
			mProgressDialog.setTitle(R.string.loading);
			mProgressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				mNovelList = getNovelList(params[0]);
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
			mNovelAdapter = new SimpleAdapter(NovelsListActivity.this, 
					mNovelList, R.layout.novel_item, new String[]{"novel", "author"}, 
					new int[]{R.id.novel_name, R.id.novel_author});
			setListAdapter(mNovelAdapter); 
			super.onPostExecute(result);
		}
		
	}
	
	private List<HashMap<String, Object>> getNovelList(String url) throws ParserException { 

	     ArrayList<HashMap<String, Object>> mNovelList = new ArrayList<HashMap<String, Object>>();
	     
	     // ���� html parser ���󣬲�ָ��Ҫ������ҳ�� URL �ͱ����ʽ
		 Parser htmlParser = new Parser(url); 
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

			Intent intent = new Intent(NovelsListActivity.this, NovelIndexActivity.class);
			intent.putExtra(NovelIndexActivity.EXTRA_NOVEL_URL, url);
			NovelsListActivity.this.startActivity(intent);
		}
		super.onListItemClick(l, v, position, id);
	}
}
