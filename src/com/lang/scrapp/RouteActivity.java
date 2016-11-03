package com.lang.scrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.lang.acrapp.util.AMapUtil;
import com.lang.acrapp.util.ErrorInfo;
import com.lang.acrapp.util.ToastUtil;
import com.lang.scrapp.adapter.BusResultListAdapter;



public class RouteActivity extends Activity implements OnMapClickListener, 
OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener {
	private AMap aMap;
	private MapView mapView;
	private Context mContext;
	private RouteSearch mRouteSearch;
	private DriveRouteResult mDriveRouteResult;
	private BusRouteResult mBusRouteResult;
	private WalkRouteResult mWalkRouteResult;
	private LatLonPoint mStartPoint = new LatLonPoint(39.917636, 116.397743);//起点
	private LatLonPoint mEndPoint = new LatLonPoint(39.984947, 116.494689);//终点
	private String mCurrentCityName = "四川";
	private final int ROUTE_TYPE_BUS = 1;
	private final int ROUTE_TYPE_DRIVE = 2;
	private final int ROUTE_TYPE_WALK = 3;
	
	private LinearLayout mBusResultLayout;
	private RelativeLayout mBottomLayout;
	private TextView mRotueTimeDes, mRouteDetailDes;
	private ImageView mBus;
	private ImageView mDrive;
	private ImageView mWalk;
	private ListView mBusResultList;
	private ProgressDialog progDialog = null;// 鎼滅储鏃惰繘搴︽潯
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.route_activity);
		
		mContext = this.getApplicationContext();
		mapView = (MapView) findViewById(R.id.route_map);
		mapView.onCreate(bundle);
		init();
		Intent intent=getIntent();
		Parcelable[] points=intent.getParcelableArrayExtra("point");
		mStartPoint=(LatLonPoint) points[0];
		//mEndPoint=intent.getParcelableExtra("endPoint");
		mEndPoint=(LatLonPoint) points[1];
		ToastUtil.print(RouteActivity.this, "起点："+mStartPoint.toString()+"终点："+mEndPoint.toString(), 5000);
		setfromandtoMarker();
		
	}

	private void setfromandtoMarker() {
		aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mStartPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
		aMap.addMarker(new MarkerOptions()
		.position(AMapUtil.convertToLatLng(mEndPoint))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));		
	}

	/**
	 * 鍒濆鍖朅Map瀵硅薄
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();	
		}
		registerListener();
		mRouteSearch = new RouteSearch(this);//初始化RouteSearch对象
		mRouteSearch.setRouteSearchListener(this);//数据回调监听
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mBusResultLayout = (LinearLayout) findViewById(R.id.bus_result);
		mRotueTimeDes = (TextView) findViewById(R.id.firstline);
		mRouteDetailDes = (TextView) findViewById(R.id.secondline);
		mDrive = (ImageView)findViewById(R.id.route_drive);
		mBus = (ImageView)findViewById(R.id.route_bus);
		mWalk = (ImageView)findViewById(R.id.route_walk);
		mBusResultList = (ListView) findViewById(R.id.bus_result_list);
	}

	/**
	 * 娉ㄥ唽鐩戝惉
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(RouteActivity.this);
		aMap.setOnMarkerClickListener(RouteActivity.this);
		aMap.setOnInfoWindowClickListener(RouteActivity.this);
		aMap.setInfoWindowAdapter(RouteActivity.this);
		
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void onBusClick(View view) {
		searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
		mDrive.setImageResource(R.drawable.route_drive_normal);
		mBus.setImageResource(R.drawable.route_bus_select);
		mWalk.setImageResource(R.drawable.route_walk_normal);
		mapView.setVisibility(View.GONE);
		mBusResultLayout.setVisibility(View.VISIBLE);
	}
	
	public void onDriveClick(View view) {
		searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
		mDrive.setImageResource(R.drawable.route_drive_select);
		mBus.setImageResource(R.drawable.route_bus_normal);
		mWalk.setImageResource(R.drawable.route_walk_normal);
		mapView.setVisibility(View.VISIBLE);
		mBusResultLayout.setVisibility(View.GONE);
	}

	public void onWalkClick(View view) {
		searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault);
		mDrive.setImageResource(R.drawable.route_drive_normal);
		mBus.setImageResource(R.drawable.route_bus_normal);
		mWalk.setImageResource(R.drawable.route_walk_select);
		mapView.setVisibility(View.VISIBLE);
		//mBusResultLayout.setVisibility(View.GONE);
	}
	
	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(int routeType, int mode) {
		if (mStartPoint == null) {
			ToastUtil.print(mContext, "定位中，稍后再试...",3000);
			return;
		}
		if (mEndPoint == null) {
			ToastUtil.print(mContext, "终点未设置",3000);
		}
		showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				mStartPoint, mEndPoint);
		if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
			BusRouteQuery query = new BusRouteQuery(fromAndTo, mode,
					mCurrentCityName, 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
			mRouteSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
		} else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
			DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null,
					null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
			mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
		} else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
			mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		}
	}

	//公交查询回调函数
	@Override
	public void onBusRouteSearched(BusRouteResult result, int errorCode) {
		dissmissProgressDialog();
		mBottomLayout.setVisibility(View.GONE);
		aMap.clear();//  清除地图上所有覆盖物
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mBusRouteResult = result;
					BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
					mBusResultList.setAdapter(mBusResultListAdapter);		
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result,2000);
				}
			} else {
				ToastUtil.show(mContext, R.string.no_result,2000);
			}
		} else {
			ToastUtil.print(RouteActivity.this, ErrorInfo.getErrorInfo(errorCode),2000);
		}
	}

	//自驾回调函数
	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
		dissmissProgressDialog();
		aMap.clear();// 清除地图上所有覆盖物
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mDriveRouteResult = result;
					final DrivePath drivePath = mDriveRouteResult.getPaths()
							.get(0);
					DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
							this, aMap, drivePath,
							mDriveRouteResult.getStartPos(),
							mDriveRouteResult.getTargetPos());
					drivingRouteOverlay.removeFromMap();
					drivingRouteOverlay.addToMap();
					drivingRouteOverlay.zoomToSpan();
					mBottomLayout.setVisibility(View.VISIBLE);
					int dis = (int) drivePath.getDistance();
					int dur = (int) drivePath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.VISIBLE);
					int taxiCost = (int) mDriveRouteResult.getTaxiCost();
					mRouteDetailDes.setText("打车约?"+taxiCost+"元?");
					mBottomLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									DriveRouteDetailActivity.class);
							intent.putExtra("drive_path", drivePath);
							intent.putExtra("drive_result",
									mDriveRouteResult);
							startActivity(intent);
						}
					});
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result,2000);
				}

			} else {
				ToastUtil.show(mContext, R.string.no_result,2000);
			}
		} else {
			ToastUtil.show(this.getApplicationContext(), errorCode,2000);
		}
		
		
	}

	//步行路径规划回调函数
	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
		dissmissProgressDialog();
		aMap.clear();// 清空地图
		if (errorCode == 1000) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mWalkRouteResult = result;
					final WalkPath walkPath = mWalkRouteResult.getPaths()
							.get(0);
					WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
							this, aMap, walkPath,
							mWalkRouteResult.getStartPos(),
							mWalkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
					mBottomLayout.setVisibility(View.VISIBLE);
					int dis = (int) walkPath.getDistance();
					int dur = (int) walkPath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.GONE);
					mBottomLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(RouteActivity.this,
									WalkRouteDetailActivity.class);
							intent.putExtra("walk_path", walkPath);
							intent.putExtra("walk_result",
									mWalkRouteResult);
							startActivity(intent);
						}
					});
				} else if (result != null && result.getPaths() == null) {
					ToastUtil.show(mContext, R.string.no_result,2000);
				}

			} else {
				ToastUtil.show(mContext, R.string.no_result,2000);
			}
		} else {
			//ToastUtil.show(mContext, errorCode,5000);
			ToastUtil.print(RouteActivity.this, ErrorInfo.getErrorInfo(errorCode), 3000);
		}
	}
	

	/**
	 * 显示进度条
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    progDialog.setIndeterminate(false);
		    progDialog.setCancelable(true);
		    progDialog.setMessage("加载中...");
		    progDialog.show();
	    }

	/**
	 *隐藏进度条
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	
}

