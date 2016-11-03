package com.lang.acrapp.view;

import java.util.ArrayList;
import java.util.List;

import com.lang.scrapp.R;
import com.lang.scrapp.adapter.SearchGridAdapter;
import com.lang.scrapp.po.SearchGridItem;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.GridView;
import android.widget.ListView;


public class SearchingListView extends ListView {


    View header=null;
    GridView gv_list=null;
    List<SearchGridItem> gridList=null;
	public SearchingListView(Context context) {
		this(context,null);
	}
	
	public SearchingListView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	public SearchingListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
		this.addHeaderView(header);
	}
	
	private void initView(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=LayoutInflater.from(context);
		header=inflater.inflate(R.layout.searchinglisthead, null);
		gv_list=(GridView) header.findViewById(R.id.gv_searching);
		text();
		initInfo();
		intitAdapter(context);
	}

	private void text() {
		// TODO Auto-generated method stub
		int width=gv_list.getMeasuredWidth();
		int height=gv_list.getMeasuredHeight();
		Log.e("outprint", "TAGG"+String.valueOf(width)+":"+height);
	}

	public void initInfo() {
		gridList=new ArrayList<SearchGridItem>();
		for(int i=0;i<12;i++){
			SearchGridItem item=new SearchGridItem(R.drawable.ic_launcher, "╪ссму╬");
			gridList.add(item);
		}
	}
	public void intitAdapter(Context context) {
		// TODO Auto-generated method stub
		SearchGridAdapter adapter=new SearchGridAdapter(context, gridList);
		gv_list.setAdapter(adapter);
	}
}
