package com.jinjiang.wxc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private static String JIN_JIANG_WXC = "http://www.jjwxc.net/";
	private ImageView mLogo;
	private Button mUserBtn;
	private ListView mLinksList;
	private Button mStoragesBtn, mRankBtn;
	private List mLinkData = new ArrayList();
	private Resources mResources;
	private int[] mLinkString = {R.string.yanqing_channel, R.string.danmei_channel, 
			R.string.baihe_channel, R.string.nvzun_channel};
	private String[] mLinkUrl = {
			"http://www.jjwxc.net/bookbase.php?s_typeid=1&fw=0&ycx=0&xx=1&sd=0&lx=0&fg=0&bq=-1&submit=%B2%E9%D1%AF",
			"http://www.jjwxc.net/bookbase.php?s_typeid=1&fw=0&ycx=0&xx=2&sd=0&lx=0&fg=0&bq=-1&submit=%B2%E9%D1%AF",
			"http://www.jjwxc.net/bookbase.php?s_typeid=1&fw=0&ycx=0&xx=3&sd=0&lx=0&fg=0&bq=-1&submit=%B2%E9%D1%AF",
			"http://www.jjwxc.net/bookbase.php?s_typeid=1&fw=0&ycx=0&xx=4&sd=0&lx=0&fg=0&bq=-1&submit=%B2%E9%D1%AF"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mLogo = (ImageView)findViewById(R.id.logo);
		mUserBtn = (Button)findViewById(R.id.user);
		mLinksList = (ListView)findViewById(R.id.links);
		mStoragesBtn = (Button)findViewById(R.id.storage);
		mRankBtn = (Button)findViewById(R.id.ranking_list);
		mStoragesBtn.setOnClickListener(this);
		mRankBtn.setOnClickListener(this);
		mUserBtn.setOnClickListener(this);
		mResources = getResources();
		setupLinks();
	}
	
	private void setupLinks() {
		for(int i=0; i<mLinkString.length; i++) {
			Map map = new HashMap();
			map.put("title", mResources.getString(mLinkString[i]));
			mLinkData.add(map);
		}
		
		mLinksList.setAdapter(new SimpleAdapter(this, mLinkData, R.layout.main_activity_link_item, 
    			new String[]{"title", "things"}, new int[]{R.id.link_title}));
		mLinksList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(position < mLinkUrl.length && position < mLinkString.length) {
					String url = mLinkUrl[position];
					Intent intent = new Intent(MainActivity.this, NovelsListActivity.class);
					intent.putExtra(NovelsListActivity.EXTRA_NOVELS_LIST_URL, url);
					intent.putExtra(NovelsListActivity.EXTRA_NOVELS_TITLE, mLinkString[position]);
					MainActivity.this.startActivity(intent);
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.storage:
			Intent intentStorage = new Intent(this, StorageListActivity.class);
			this.startActivity(intentStorage);
			break;
		case R.id.ranking_list:
			Intent intentRank = new Intent(this, RankListActivity.class);
			this.startActivity(intentRank);
			break;
		case R.id.user:
			Intent intentLogin = new Intent(this, TestLoginActivity.class);
			this.startActivity(intentLogin);
		}
	}

}
