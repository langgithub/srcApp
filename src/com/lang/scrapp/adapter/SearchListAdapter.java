package com.lang.scrapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lang.scrapp.R;
import com.lang.scrapp.po.SearchItemInfo;
import com.lang.scrapp.po.SearchList;

public class SearchListAdapter extends BaseAdapter{

	private Context context;
	private List<SearchItemInfo>  searchingList;
		
	public SearchListAdapter(Context context,
			List<SearchItemInfo> searchingList) {
		super();
		this.context = context;
		this.searchingList = searchingList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return searchingList.size();
	}

	@Override
	public SearchItemInfo getItem(int position) {
		// TODO Auto-generated method stub
		return searchingList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub\
		SearchList sl=null;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.searching_item, null);
			sl=new SearchList();
			sl.setIcon((ImageView) convertView.findViewById(R.id.searching_icon));
			sl.setTitle((TextView) convertView.findViewById(R.id.searching_title));
			sl.setAddress((TextView)convertView.findViewById(R.id.searching_address));
			convertView.setTag(sl);
		}else{
			sl=(SearchList) convertView.getTag();
		}
		SearchItemInfo item=searchingList.get(position);
		sl.getIcon().setImageResource(item.getId());
		sl.getTitle().setText(item.getTitle());
		sl.getAddress().setText(item.getAddress());
		return convertView;
	}
	
}

