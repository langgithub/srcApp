package com.lang.scrapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lang.scrapp.R;
import com.lang.scrapp.po.SearchGridItem;
import com.lang.scrapp.po.SearchingGridItemC;

public class SearchGridAdapter extends BaseAdapter{

		Context context=null;
		List<SearchGridItem> gridList=null;
		
		public SearchGridAdapter(Context context, List<SearchGridItem> gridList) {
			super();
			this.context = context;
			this.gridList = gridList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gridList.size();
		}

		@Override
		public SearchGridItem getItem(int position) {
			// TODO Auto-generated method stub
			return gridList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            SearchingGridItemC c=null;
			if(convertView==null){
				convertView=View.inflate(context, R.layout.searching_griditem, null);
				c=new SearchingGridItemC();
				c.setText((TextView) convertView.findViewById(R.id.tv_griditem));
				//c.setImgv((ImageView)convertView.findViewById(R.id.iv_griditem));
				convertView.setTag(c);
			}else{
				c=(SearchingGridItemC) convertView.getTag();
			}
			SearchGridItem item=gridList.get(position);
			System.out.println(item.getIconId()+item.getText());
		//	c.getImgv().setImageResource(item.getIconId());
			c.getText().setText(item.getText());
			return convertView;
		}
		
	}
	
