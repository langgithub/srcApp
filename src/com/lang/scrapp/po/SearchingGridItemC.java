package com.lang.scrapp.po;

import android.widget.ImageView;
import android.widget.TextView;

public class SearchingGridItemC {

	private ImageView imgv;
	private TextView text;
	public ImageView getImgv() {
		return imgv;
	}
	public void setImgv(ImageView imgv) {
		this.imgv = imgv;
	}

	public SearchingGridItemC(ImageView imgv, TextView text) {
		super();
		this.imgv = imgv;
		this.text = text;
	}
	public TextView getText() {
		return text;
	}
	public void setText(TextView text) {
		this.text = text;
	}
	public SearchingGridItemC() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
