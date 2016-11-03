package com.lang.scrapp.po;

import android.widget.ImageView;
import android.widget.TextView;

public class SearchedItemC {

	private ImageView imgview;
	private TextView tvTitle;
	private TextView tvAddr;
	private ImageView iv_goto;
	
	public ImageView getIv_goto() {
		return iv_goto;
	}
	public void setIv_goto(ImageView iv_goto) {
		this.iv_goto = iv_goto;
	}
	public ImageView getImgview() {
		return imgview;
	}
	public void setImgview(ImageView imgview) {
		this.imgview = imgview;
	}
	public TextView getTvTitle() {
		return tvTitle;
	}
	public void setTvTitle(TextView tvTitle) {
		this.tvTitle = tvTitle;
	}
	public TextView getTvAddr() {
		return tvAddr;
	}
	public void setTvAddr(TextView tvAddr) {
		this.tvAddr = tvAddr;
	}

	public SearchedItemC(ImageView imgview, TextView tvTitle, TextView tvAddr,
			ImageView iv_goto) {
		super();
		this.imgview = imgview;
		this.tvTitle = tvTitle;
		this.tvAddr = tvAddr;
		this.iv_goto = iv_goto;
	}
	public SearchedItemC() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
