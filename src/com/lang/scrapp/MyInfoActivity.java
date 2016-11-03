package com.lang.scrapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class MyInfoActivity extends Activity {

	private ImageView iv_myMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		intiView();
		setListener();
	}
	private void intiView() {
		// TODO Auto-generated method stub
		iv_myMap=(ImageView) findViewById(R.id.iv_myMap);
		
	}
	private void setListener() {
		// TODO Auto-generated method stub
		iv_myMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MyInfoActivity.this,MainActivity.class);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
