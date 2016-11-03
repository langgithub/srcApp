package com.lang.scrapp;


import java.util.ArrayList;
import java.util.List;

import com.lang.acrapp.view.SearchingListView;
import com.lang.scrapp.adapter.SearchListAdapter;
import com.lang.scrapp.po.SearchItemInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * ����activity
 * ��Ҫ���ܣ�
 * 1.��ʾ�����������ݲ���
 * 2.���������ݴ浵�������´�����
 * @author lang
 *
 */
public class SearchActivity extends Activity {

	/**
	 * searchingList�����������������
	 */
	List<SearchItemInfo> searchingList=null;
	SearchingListView searchingView=null;
	private Button submit;
	private EditText content;
	private String amid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		Intent intent=getIntent();
		amid=intent.getStringExtra("whatDoIt");
		initView();
		initData();
		setAdapter();
		setListener();
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str=content.getText().toString();
				if(str!=null&&amid.equals("mapSearch")){
					Intent intent=new Intent(SearchActivity.this,MainActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("content", str);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
				
				if(str!=null&&amid.equals("routeSearch")){
					Intent intent=new Intent(SearchActivity.this,MainActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("content", str);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		searchingView=(SearchingListView) findViewById(R.id.searching_lv);
		submit=(Button) findViewById(R.id.bt_search);
		content=(EditText) findViewById(R.id.et_content);
	}
	
    /**
     * ��ʼ������
     */
	private void initData() {
		searchingList=new ArrayList<SearchItemInfo>();
		for (int i = 0; i < 20; i++) {
			SearchItemInfo infos=new SearchItemInfo(R.drawable.search_input,"����","��ַ");
			searchingList.add(infos);
		}
	}
	/**
	 * ����������
	 */
	private void setAdapter() {
		// TODO Auto-generated method stub
		SearchListAdapter adapter=new SearchListAdapter(SearchActivity.this, searchingList);
		searchingView.setAdapter(adapter);
	}
}
