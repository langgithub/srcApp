package com.lang.scrapp;

import java.util.List;

import com.amap.api.services.cloud.CloudImage;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.core.PoiItem;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.lang.acrapp.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowPlaceActivity extends Activity {

	private Button bt_back;
	private TextView et_showPlaceTitle;
	private TextView et_showPlaceAddr;
	private NetworkImageView iv_showPlacePhoto;
	private List<CloudImage> mImageitem;
	private CloudItem cloudItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_place);
		
		initView();
		initData();
		setListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		bt_back=(Button) findViewById(R.id.bt_back);
		et_showPlaceTitle=(TextView) findViewById(R.id.et_showPlaceTitle);
		et_showPlaceAddr=(TextView) findViewById(R.id.et_showPlaceAddr);
		iv_showPlacePhoto=(NetworkImageView) findViewById(R.id.iv_showPlacePhoto);
	}
	private void initData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		String str=intent.getStringExtra("tag");
		ToastUtil.print(ShowPlaceActivity.this, str, 5000);
		if(str.equals("poi")){
			PoiItem poiItem=(PoiItem) intent.getParcelableExtra("poiItem");
			//String city=poiItem.getCityName();
			et_showPlaceTitle.setText(poiItem.getTitle());
			et_showPlaceAddr.setText(poiItem.getSnippet());
		}else{
			cloudItem=(CloudItem) intent.getParcelableExtra("cloudItem");
			et_showPlaceTitle.setText(cloudItem.getTitle());
			et_showPlaceAddr.setText(cloudItem.getSnippet());
			ImageLoader mImageLoader = new ImageLoader(Volley.newRequestQueue(ShowPlaceActivity.this),
					new AMApCloudImageCache());
			cloudItem.getCloudImage().get(0).getUrl();
			iv_showPlacePhoto.setImageUrl(cloudItem.getCloudImage().get(0).getPreurl(),mImageLoader);
			mImageitem = cloudItem.getCloudImage();// ªÒ»°Õº∆¨
		}
		
	}

	private void setListener() {
		// TODO Auto-generated method stub
		bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bt_back.setBackgroundResource(R.drawable.offline_back);
				Intent intent=new Intent(ShowPlaceActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
		iv_showPlacePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowImage("0");
				ToastUtil.print(ShowPlaceActivity.this, "go", 5000);
			}
		});
	}
	private void ShowImage(String position) {
		if (mImageitem.size() != 0) {
			Intent intent = new Intent(ShowPlaceActivity.this,
					PhotoShowActivity.class);
			Bundle bundle=new Bundle();
			bundle.putParcelable("clouditem", cloudItem);
			bundle.putString("position", position);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

}
