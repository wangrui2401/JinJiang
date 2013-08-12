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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StorageListActivity extends Activity {
	
	final static int[] mStorages = new int[]{R.string.over_novel_storage, R.string.station_storage,
			R.string.vip_storage, R.string.over_count_storage,
			R.string.classic_storage, R.string.free_storage};
	final static String[] mStorageUrls = new String[] {
			"http://www.jjwxc.net/bookbase_slave.php?endstr=true&orderstr=1",
			"http://www.jjwxc.net/bookbase_slave.php?booktype=sp",
			"http://www.jjwxc.net/bookbase_slave.php?booktype=vip",
			"http://www.jjwxc.net/bookbase_slave.php?booktype=package",
			"http://www.jjwxc.net/bookbase_slave.php?booktype=scriptures",
			"http://www.jjwxc.net/bookbase_slave.php?booktype=free"
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
				Intent intent = new Intent(StorageListActivity.this, NovelsListActivity.class);
				intent.putExtra(NovelsListActivity.EXTRA_NOVELS_LIST_URL, mStorageUrls[position]);
				intent.putExtra(NovelsListActivity.EXTRA_NOVELS_TITLE, mStorages[position]);
				StorageListActivity.this.startActivity(intent);
			}
			 
		}
	};
}
