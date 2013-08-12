package com.jinjiang.wxc;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.nodes.TagNode;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RankActivity extends ListActivity {
	
	public static final String EXTRA_NOVELS_LIST_URL = "novels_list_url";
	public static final String EXTRA_NOVELS_TITLE = "novels_title";
	public static final String EXTRA_RANK_TYPE = "rank_type";
	//EXTRA_RANK_TYPE_YQ �����ڡ���������񡱡������Ƶ������а�
	public static final String RANK_TYPE_YQ = "rank_type_yq"; 
	//EXTRA_RANK_TYPE_AUTHOR �����ڡ��½����߰񡱡����¶ȡ����ȡ����ꡢ�ܷ֡��������а�
	public static final String RANK_TYPE_AUTHOR = "rank_type_author";
	
	final String JJ_WXC_URL = "http://www.jjwxc.net/";
	private ListAdapter mNovelAdapter; 
	private List<HashMap<String, Object>> mNovelList;
	ProgressDialog mProgressDialog;
	String mUrl, mRankType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUrl = getIntent().getStringExtra(EXTRA_NOVELS_LIST_URL);
		mRankType = getIntent().getStringExtra(EXTRA_RANK_TYPE);
		NovelListAsyncTask task = new NovelListAsyncTask();
		task.execute(mUrl);
	}
	
	private class NovelListAsyncTask extends AsyncTask<String, Integer, Boolean> {
		
		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(RankActivity.this);
			mProgressDialog.setTitle(R.string.loading);
			mProgressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				if(mRankType != null) {
					if(mRankType.equals(RANK_TYPE_YQ)) {
						mNovelList = getNovelList(params[0]);	
					} else if(mRankType.equals(RANK_TYPE_AUTHOR)) {
						mNovelList = getNovelList2(params[0]);	
					}	
				}
			} catch (ParserException e) {
				e.printStackTrace();
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(mProgressDialog != null) {
				if(mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
			if(mRankType != null) {
				if(mRankType.equals(RANK_TYPE_YQ)) {
					mNovelAdapter = new SimpleAdapter(RankActivity.this, 
							mNovelList, R.layout.text_item, new String[]{"content"}, 
							new int[]{R.id.text_name});	
				} else if(mRankType.equals(RANK_TYPE_AUTHOR)) {
					mNovelAdapter = new SimpleAdapter(RankActivity.this, 
							mNovelList, R.layout.novel_item, new String[]{"novel", "author"}, 
							new int[]{R.id.novel_name, R.id.novel_author});
				}	
			}
			// ��ʼ�� ListView �� adapter ����� Android �����ϵ���ʾ
			
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
				 new AndFilter(new TagNameFilter("td"), new HasAttributeFilter("width", "760")));

		 if(novelTable != null && novelTable.size() > 0) { 
			 String content = "", describe = "", novelUrl = ""; 
			// ��ȡָ�� table ��ǩ���ӽڵ��е� <td> �ڵ�
			 NodeList tableItems = novelTable.elementAt(0).getChildren().extractAllNodesThatMatch
					 (new AndFilter(new TagNameFilter("table"), 
							 new HasAttributeFilter("bgcolor", "#009900")));
			 if(tableItems != null && tableItems.size() > 0) {
				 for(int i=0; i<tableItems.size(); i++) {
					 NodeList tdItems = tableItems.elementAt(i).getChildren().extractAllNodesThatMatch
							 (new TagNameFilter("td"), true);
					 if(tdItems != null && tdItems.size() >= 2) {
						 
						 for(int j=0; j<tdItems.size(); j++) {
							 HashMap map = new HashMap();
							 TagNode td = (TagNode)tdItems.elementAt(j);
							 map.put("content", Html.fromHtml(td.toHtml()));
							 mNovelList.add(map);
						 }
						 
//						 //��ȡС˵�������ߣ����߿�����ͽ�����
//						 NodeList td = trItems.elementAt(0).getChildren().extractAllNodesThatMatch(
//								 new TagNameFilter("td"), true);
//						 if(td != null) {
//							 content = "";
//							 if(td.size() == 1) {
//								 content = ((TagNode)td.elementAt(0)).toString();
//								 map.put("content", content);
//							 }
//							 for(int j=0; j<td.size(); j++) {
//								 content = content + ((TagNode)td.elementAt(0)).toString() + "\n";
//							 } 
//							 map.put("content", content);
//						 }
//
//
//						 //��ȡ����
					 }
				 }
			 }
		 }
	     return mNovelList; 
	}

	private List<HashMap<String, Object>> getNovelList2(String url) throws ParserException {

	     ArrayList<HashMap<String, Object>> mNovelList = new ArrayList<HashMap<String, Object>>();
	     
	     // ���� html parser ���󣬲�ָ��Ҫ������ҳ�� URL �ͱ����ʽ
		 Parser htmlParser = new Parser(url); 
		 htmlParser.setEncoding("gb2312");

		 // ��ȡָ���� table �ڵ㣬�� <table> ��ǩ�����Ҹñ�ǩ���������� class ֵΪ��cytable��
		 NodeList trItems = htmlParser.extractAllNodesThatMatch( 
//				 new TagNameFilter("tbody"));
				 new AndFilter(new TagNameFilter("tr"), 
						 new HasAttributeFilter("bgcolor", "#eefaee")));
		 String content = "", describe = "", novelUrl = ""; 
		 for(int i=0; i<trItems.size(); i++) {
			 NodeList LinkItems = trItems
					 .elementAt(i).getChildren().extractAllNodesThatMatch(
							 new NodeClassFilter(LinkTag.class),true);
			 HashMap map = new HashMap();
			 // ��ȡ����
			 if(LinkItems != null && LinkItems.size() >= 2) {
				 content = ((LinkTag)LinkItems.elementAt(0)).getLinkText();
				 map.put("author", content);
				 
				 content = ((LinkTag)LinkItems.elementAt(1)).getLinkText();
				 novelUrl = ((LinkTag)LinkItems.elementAt(1)).getLink();
				 map.put("novel", content);
				 map.put("url", novelUrl);
				 mNovelList.add(map);
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

			Intent intent = new Intent(RankActivity.this, NovelIndexActivity.class);
			intent.putExtra(NovelIndexActivity.EXTRA_NOVEL_URL, url);
			RankActivity.this.startActivity(intent);
		}
		super.onListItemClick(l, v, position, id);
	}

}
