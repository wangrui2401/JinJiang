package com.jinjiang.wxc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.jinjiang.wxc.util.StringUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NovelIndexActivity extends Activity {
	
	ProgressDialog mProgressDialog;
	String mNovelUrl;
	private List<HashMap<String, Object>> mNovelIndexList;
	private ListAdapter mNovelAdapter; 
	private String mIntroduce;
	private TextView mIntroduceTv;
	private ListView mNovelIndexLv;
	
	public final static String EXTRA_NOVEL_URL = "NOVEL_URL";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_novel_index);
		mIntroduceTv = (TextView)findViewById(R.id.novel_introduce);
		mNovelIndexLv = (ListView)findViewById(R.id.novel_index);
		
		mNovelUrl = getIntent().getStringExtra(EXTRA_NOVEL_URL);
		if(mNovelUrl != null && !mNovelUrl.equals("")) {
			NovelInforAsyncTask novelTask = new NovelInforAsyncTask();
			novelTask.execute(mNovelUrl);
		} 
	}
	
	private class NovelInforAsyncTask extends AsyncTask<String, Integer, Boolean> {
		
		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(NovelIndexActivity.this);
			mProgressDialog.setTitle(R.string.loading);
			mProgressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				mNovelIndexList = getNovelChapters(params[0]);
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
			// 初始化 ListView 的 adapter 完成在 Android 界面上的显示
			if(mNovelIndexList != null) {
				mNovelAdapter = new SimpleAdapter(NovelIndexActivity.this, 
						mNovelIndexList, R.layout.chapter_item, new String[]{"novelIndex"}, 
						new int[]{R.id.chapter_name});
				mNovelIndexLv.setAdapter(mNovelAdapter);
				mNovelIndexLv.setOnItemClickListener(mChapterItemClickListener);
			}
 
			if(mIntroduce != null) {
//				mIntroduceTv.setText(mIntroduce);
				mIntroduceTv.setText(Html.fromHtml(mIntroduce));
			}
			super.onPostExecute(result);
		}
		
	}
	
	private List<HashMap<String, Object>> getNovelChapters(String url) throws ParserException { 

	     ArrayList<HashMap<String, Object>> mNovelIndexList = new ArrayList<HashMap<String, Object>>();
	     
	     // 创建 html parser 对象，并指定要访问网页的 URL 和编码格式
		 Parser htmlParser = new Parser(url); 
		 htmlParser.setEncoding("gb2312");

		 NodeList novelTable = htmlParser.extractAllNodesThatMatch(
				 new OrFilter(new AndFilter(new TagNameFilter("div"), 
						 new HasAttributeFilter("id", "novelintro")),
						 new AndFilter(new TagNameFilter("table"), 
								 new HasAttributeFilter("id", "oneboolt")))
				 );
		
		 if(novelTable != null && novelTable.size() > 0) { 
			 for(int i=0; i<novelTable.size(); i++) {
				 TagNode node = (TagNode)novelTable.elementAt(i);
				 String idValue = node.getAttribute("id");
				 if(idValue == null) {continue;}
				 if(idValue.equals("novelintro")) {
					 //小说介绍
					 mIntroduce = ((Div)node).getStringText();
//					 mIntroduce = StringUtil.getPureString(mIntroduce);
				 } else if(idValue.equals("oneboolt")) {
					 //小说章节
					 // 获取指定 table 标签的子节点中的 <td> 节点
					 NodeList itemLiList = node.getChildren().extractAllNodesThatMatch
							 (new TagNameFilter("td"), true);
			
					 if(itemLiList != null && itemLiList.size() > 0) { 
						 String content = "", novelUrl = ""; 
						 for(int j = 0; j < itemLiList.size(); j++) { 
							 HashMap map = new HashMap();
							 // 获取小说章节名
							 NodeList novelIndex = itemLiList
									 .elementAt(j).getChildren().extractAllNodesThatMatch(
											 new NodeClassFilter(LinkTag.class),true);
							 if(novelIndex != null && novelIndex.size() > 0) {
								 content = ((LinkTag)novelIndex.elementAt(0)).getLinkText();
								 novelUrl = ((LinkTag)novelIndex.elementAt(0)).getLink();
								 
								 map.put("novelIndex", content);
								 map.put("url", novelUrl);
								 mNovelIndexList.add(map);
							 }
						 } 
					 } 
				 }
			 }
		 }
	     return mNovelIndexList; 
	}

	OnItemClickListener mChapterItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			HashMap map = (HashMap) mNovelAdapter.getItem(position);
			String url = (String) map.get("url");
			Intent intent = new Intent(NovelIndexActivity.this, ReadActivity.class);
			intent.putExtra(ReadActivity.EXTRA_NOVEL_CONTENT_URL, url);
			NovelIndexActivity.this.startActivity(intent);
		}
	};

}
