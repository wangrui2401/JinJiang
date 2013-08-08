package com.jinjiang.wxc;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static String JIN_JIANG_WXC = "http://www.jjwxc.net/";
	private ImageView mLogo;
	private TextView mUser;
	private ListView mLinksList;
	private List mLinkData = new ArrayList();
	private Resources mResources;
	private int[] mLinkString = {R.string.yanqing_station, R.string.danmei_station, R.string.wanjie_station};
	private List mFunctionsData = new ArrayList();
	private int[] mFunctionsString = {R.string.read, R.string.my_jj, 
			R.string.account, R.string.write, R.string.search, R.string.money};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setupMainActivity();
	}

	private void initView() {
		mLogo = (ImageView)findViewById(R.id.logo);
		mUser = (TextView)findViewById(R.id.user);
		mLinksList = (ListView)findViewById(R.id.links);
		mResources = getResources();
		setupFunctions();
		setupLinks();
	}
	
	private void setupFunctions() {
		for(int i=0; i<mFunctionsString.length; i++) {
			Map map = new HashMap();
			map.put("title", mResources.getString(mFunctionsString[i]));
			mFunctionsData.add(map);
		}
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
				switch(position) {
				case 0:
					Intent yanqing = new Intent(MainActivity.this, YQListActivity.class);
					MainActivity.this.startActivity(yanqing);
					break;
				}
			}
		});
	}
	
	
	private void setupMainActivity() {
		try {
			URL url = new URL(JIN_JIANG_WXC);
			InputSource is = new InputSource(url.openStream());
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser;
			parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();
			MainActivityHandler handler = new MainActivityHandler();
			xmlReader.setContentHandler(handler);
			if(is != null) {
				xmlReader.parse(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class MainActivityHandler extends DefaultHandler2 {
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
		}
		
		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
		}

	}


}
