package com.lang.scrapp.adapter;

import java.io.ObjectOutputStream.PutField;
import java.util.List;

import com.lang.acrapp.util.ToastUtil;
import com.lang.scrapp.MainActivity;
import com.lang.scrapp.R;
import com.lang.scrapp.RouteSearchActivity;
import com.lang.scrapp.po.SearchedItemC;
import com.lang.scrapp.po.SearchedItemDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchedItemAdapter extends BaseAdapter {

	Context context;
	List<SearchedItemDetail> itemlist=null;
	private int getSelectPosition=-1;
	private boolean isUpdate=false;

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public void setGetSelectPosition(int getSelectPosition) {
		this.getSelectPosition = getSelectPosition;
	}

	public SearchedItemAdapter(Context context,
			List<SearchedItemDetail> itemlist) {
		super();
		this.context = context;
		this.itemlist = itemlist;
	}

	public SearchedItemAdapter() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemlist.size();
	}

	@Override
	public SearchedItemDetail getItem(int position) {
		// TODO Auto-generated method stub
		return itemlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SearchedItemC compant=null;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.searcheditemdetail, null);
			compant=new SearchedItemC();
			compant.setImgview((ImageView)convertView.findViewById(R.id.iv_icon));
			compant.setTvTitle((TextView)convertView.findViewById(R.id.tv_title));
			compant.setTvAddr((TextView)convertView.findViewById(R.id.tv_address));
			compant.setIv_goto((ImageView)convertView.findViewById(R.id.iv_goto));
			convertView.setTag(compant);
		}else{
			compant=(SearchedItemC) convertView.getTag();
		}
		final SearchedItemDetail item=itemlist.get(position);
		if(position==getSelectPosition){
			convertView.setBackgroundColor(Color.rgb(240, 250, 250));
			//System.out.println("alang"+position);
		}else{
			convertView.setBackgroundColor(Color.WHITE);
		}
		compant.getImgview().setImageResource(item.getId());
		compant.getTvTitle().setText(item.getTitle());
		compant.getTvAddr().setText(item.getAddress());
		compant.getIv_goto().setImageResource(item.getId_goto());
		compant.getIv_goto().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ToastUtil.print(context, item.getLp().toString(), 3000);
				Intent itent=new Intent(context,RouteSearchActivity.class);
				Bundle bundle=new Bundle();
				bundle.putParcelableArray("point", new Parcelable[]{MainActivity.myLocation,item.getLp()});
				bundle.putString("address", item.getAddress());
				itent.putExtras(bundle);
				context.startActivity(itent);
			}
		});
		return convertView;
	}
}
