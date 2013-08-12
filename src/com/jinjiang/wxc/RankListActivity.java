package com.jinjiang.wxc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RankListActivity extends Activity {
	
	final static int[] mStorages = new int[]{R.string.official_yanqing_rank, R.string.official_danmei_rank,
			R.string.author_rank, R.string.month_rank,
			R.string.quarter_rank, R.string.half_year_rank,
			R.string.score_rank, R.string.words_count_rank};
	final static String[] mStorageUrls = new String[] {
			"http://www.jjwxc.net/topten.php?orderstr=1",
			"http://www.jjwxc.net/topten.php?orderstr=1&t=1",
			"http://www.jjwxc.net/topten.php?orderstr=3&t=1",
			"http://www.jjwxc.net/topten.php?orderstr=5&t=1",
			"http://www.jjwxc.net/topten.php?orderstr=4&t=1",
			"http://www.jjwxc.net/topten.php?orderstr=6&t=1",
			"http://www.jjwxc.net/topten.php?orderstr=7&t=1",
			"http://www.jjwxc.net/topten.php?orderstr=8&t=1",
	};
	final static String KEY_NAME = "STORAGE_NAME";
	final static String KEY_URL = "STORAGE_URL";
	
	TextView mTitle;
	ListView mStoragesListview;
	ListAdapter mAdapter;
	ArrayList<Map<String, Object>> mStoragesList;
	Resources mRes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title_list);
		setup();
	}
	
	private void setup() {
		mRes = getResources();
		mTitle = (TextView)findViewById(R.id.title);
		mTitle.setText(R.string.novel_storage);
		mStoragesListview = (ListView)findViewById(R.id.main_list);
		mStoragesList = new ArrayList<Map<String,Object>>();
		for(int i=0; i<mStorages.length; i++) {
			if(i<mStorageUrls.length) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(KEY_NAME, mRes.getString(mStorages[i]));	
				map.put(KEY_URL, mStorageUrls[i]);
				mStoragesList.add(map);
			}
		}
		mAdapter = new SimpleAdapter(this, mStoragesList, R.layout.text_item,
				new String[]{KEY_NAME}, new int[]{R.id.text_name});
		mStoragesListview.setAdapter(mAdapter);
		mStoragesListview.setOnItemClickListener(mOnItemClickListener);
	}

	OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			if(position < mStorageUrls.length) {
				Intent intent = new Intent(RankListActivity.this, RankActivity.class);
				intent.putExtra(RankActivity.EXTRA_NOVELS_LIST_URL, mStorageUrls[position]);
				intent.putExtra(RankActivity.EXTRA_NOVELS_TITLE, mStorages[position]);
				if(position<2) {
					intent.putExtra(RankActivity.EXTRA_RANK_TYPE, RankActivity.RANK_TYPE_YQ);
				} else {
					intent.putExtra(RankActivity.EXTRA_RANK_TYPE, RankActivity.RANK_TYPE_AUTHOR);
				}
				RankListActivity.this.startActivity(intent);
			}
		}
	};
}
