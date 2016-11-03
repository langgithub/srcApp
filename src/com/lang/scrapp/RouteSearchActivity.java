package com.lang.scrapp;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.lang.acrapp.util.ErrorInfo;
import com.lang.acrapp.util.ToastUtil;
import com.lang.acrapp.view.OnBusFragment;
import com.lang.acrapp.view.OndriveFragment;
import com.lang.acrapp.view.OnwalkFragment;
import com.lang.scrapp.po.SearchedItemDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class RouteSearchActivity extends FragmentActivity implements OnGeocodeSearchListener {

	private ViewPager vp;
	private List<Fragment> fgList;
	private FragmentPagerAdapter adapter;
	private ImageView iv_onbus,iv_ondrive,iv_onwalk;
	private Button bt_search;
	private int way=0;
	private GeocodeSearch geocoderSearch;
	private LatLonPoint startPoint;
	private LatLonPoint endPoint;
	
	private TextView busEnd;
	private TextView driverEnd;
	private TextView walkEnd;
	
	private OnBusFragment busf;
	private OndriveFragment drivef;
	private OnwalkFragment walkf;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_search);
		Intent intent=getIntent();
		Parcelable[] points=intent.getParcelableArrayExtra("point");
		String address=(String) intent.getStringExtra("address");
		if(points!=null&&points.length>0){
			ToastUtil.print(RouteSearchActivity.this, address,3000);
			//end.setText(address);
			//busEnd=(TextView) fgList.get(0).getView().findViewById(R.id.et_myendPosition_bus);
			//busEnd.setText(address);
			//walkEnd.setText(address);
			Intent intent2=new Intent(RouteSearchActivity.this,RouteActivity.class);
			Bundle bundle=new Bundle();
			bundle.putParcelableArray("point", points);
			intent2.putExtras(bundle);
			startActivity(intent2);
		}
		intiView();
		setListener();
	}
	@SuppressWarnings("deprecation")
	private void setListener() {
		// TODO Auto-generated method stub
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		onPagechangeListener();
		onClickListener();
		
		//setOnBusClickListener();
		//setOnDriveClickListener();
		//setOnWalkClickListener();
	}
	/*private void setOnBusClickListener() {
		busf.tv_bus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(RouteSearchActivity.this,SearchActivity.class);
				intent1.putExtra("whatDoIt", "routeSearch");
				startActivity(intent1);
			}
		});
	}
	private void setOnWalkClickListener() {
		walkf.tv_walk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(RouteSearchActivity.this,SearchActivity.class);
				intent1.putExtra("whatDoIt", "routeSearch");
				startActivity(intent1);
			}
		});
	}
	private void setOnDriveClickListener() {
		drivef.tv_drive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(RouteSearchActivity.this,SearchActivity.class);
				intent1.putExtra("whatDoIt", "routeSearch");
				startActivity(intent1);
			}
		});
	}*/
	
	private void onClickListener() {
		bt_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToastUtil.print(RouteSearchActivity.this, "in", 3000);
				switch (way) {

				case 0:
					busEnd=(TextView) fgList.get(0).getView().findViewById(R.id.et_myendPosition_bus);
					String str=busEnd.getText().toString();
				    ToastUtil.print(RouteSearchActivity.this, str, 3000);
					break;
				case 1:
					/*getEndPoint(0);
					if(endPoint!=null){
						Intent intent1=new Intent(RouteSearchActivity.this,RouteActivity.class);
						Bundle bundle1=new Bundle();
						bundle1.putParcelable("startPoint", startPoint);
						bundle1.putParcelable("endPoint", endPoint);
						intent1.putExtras(bundle1);
						startActivity(intent1);
					}*/
					Intent intent1=new Intent(RouteSearchActivity.this,SearchActivity.class);
					intent1.putExtra("whatDoIt", "routeSearch");
					startActivity(intent1);
					
					
				    
				    //driverEnd=(TextView)fgList.get(1).getView().findViewById(R.id.et_myendPosition_drive);
				    break;
				case 2:
				   // walkEnd=(TextView)fgList.get(2).getView().findViewById(R.id.et_myendPosition_walk);
					break;
				default:
					break;
				}
			}
		});
	}
	@SuppressWarnings("deprecation")
	private void onPagechangeListener() {
	//	vp.setCurrentItem(0);
		
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				resetImagView();
				way=arg0;
				switch (arg0) {
				case 0:
					way=0;
					iv_ondrive.setBackgroundResource(R.drawable.route_drive_select);
					break;
				case 1:
					way=1;
					iv_onbus.setBackgroundResource(R.drawable.route_bus_select);
					break;
				case 2:
					way=2;
					iv_onwalk.setBackgroundResource(R.drawable.route_walk_select);
					//setchecked(true)
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void getEndPoint(int postion) {
		// TODO Auto-generated method stub
		
	  /*  String str=end.getText().toString();
	    ToastUtil.print(RouteSearchActivity.this, str, 3000);
	    getLatlon(str);*/
		
	}
	private void resetImagView() {
		// TODO Auto-generated method stub
		iv_onbus.setBackgroundResource(R.drawable.route_bus_normal);
		iv_ondrive.setBackgroundResource(R.drawable.route_drive_normal);
		iv_onwalk.setBackgroundResource(R.drawable.route_walk_normal);
	}
	private void intiView() {
		// TODO Auto-generated method stub
		iv_onbus=(ImageView) findViewById(R.id.route_bus);
		iv_ondrive=(ImageView) findViewById(R.id.route_drive);
		iv_ondrive.setBackgroundResource(R.drawable.route_drive_select);
		iv_onwalk=(ImageView) findViewById(R.id.route_walk);
		vp=(ViewPager) findViewById(R.id.vp_routeWay);
		bt_search=(Button) findViewById(R.id.bt_search);
		
		
		fgList=new ArrayList<Fragment>();
		
		busf=new OnBusFragment();
		drivef=new OndriveFragment();
		walkf=new OnwalkFragment();
		
		fgList.add(busf);
		fgList.add(drivef);
		fgList.add(walkf);
		
		adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fgList.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fgList.get(arg0);
			}
		};
		
		vp.setAdapter(adapter);
		
	
		//无法使用
		//busEnd=(TextView) fgList.get(0).getView().findViewById(R.id.et_myendPosition_bus);
		//driverEnd=(TextView)fgList.get(1).getView().findViewById(R.id.et_myendPosition);
		//walkEnd=(TextView)fgList.get(2).getView().findViewById(R.id.et_myendPosition);
	}
	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		//showDialog();
		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				ToastUtil.print(RouteSearchActivity.this, "终点"+address.getLatLonPoint().toString(), 2000);
				endPoint=address.getLatLonPoint();
			} else {
				ToastUtil.show(RouteSearchActivity.this, R.string.no_result,3000);
			}
		} else {
			ToastUtil.print(this, ErrorInfo.getErrorInfo(rCode),3000);
		}
	}
	@Override
	public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
