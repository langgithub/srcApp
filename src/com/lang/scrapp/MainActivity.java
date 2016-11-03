package com.lang.scrapp;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.cloud.CloudSearch.OnCloudSearchListener;
import com.amap.api.services.cloud.CloudSearch.SearchBound;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.lang.acrapp.resource.ComResource;
import com.lang.acrapp.util.CloudOverlay;
import com.lang.acrapp.util.ErrorInfo;
import com.lang.acrapp.util.MyPoiOverlayUtil;
import com.lang.acrapp.util.ToastUtil;
import com.lang.acrapp.util.MyPoiOverlayUtil.selectMarkListener;
import com.lang.acrapp.view.ArcMenu;
import com.lang.acrapp.view.ArcMenu.OnMenuItemClickListener;
import com.lang.acrapp.view.TopBar;
import com.lang.acrapp.view.TopBar.OnTopBarItemClickListener;
import com.lang.scrapp.adapter.SearchedItemAdapter;
import com.lang.scrapp.po.SearchedItemDetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class MainActivity extends Activity implements OnPoiSearchListener, 
OnMarkerClickListener,InfoWindowAdapter, AMapLocationListener, LocationSource ,
selectMarkListener, OnMapClickListener, OnCloudSearchListener{

	private MapView mapView=null;
	private AMap aMap=null;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
	private ArcMenu myMenu;
	private TopBar myBar;
	private Button bt_search,input_search;
	private EditText et_text,input_edit;
	private RelativeLayout mPoiDetail;
	private TextView mPoiName, mPoiAddress,tv_looklist;
	
	private static final int TO_MYINFO=1;
	private static final int TO_SEARCH=2;
	private static final int TO_MYLOCATION=3;
	private static final int TO_SCALECONTROL=4;
	
	

	/**
	 * poi查询相关参数
	 */
	private String keyWord;
	private String cityCode="成都";//搜索城市
	private int currentPage=1;//当前查询页
	private PoiSearch.Query query;//查询的sql语句
	private PoiResult poiResult;
	private ArrayList<PoiItem> poiItems;
	private LatLonPoint lp = new LatLonPoint(39.993167, 116.473274);
	private MyPoiOverlayUtil poiOverlay;
	private Marker mlastMarker;
	private Marker detailMarker;
	
	private List<SearchedItemDetail> detailItems;
	private ListView lv_showdetail;
	private SearchedItemAdapter adapter;
	private int listViewDetailClick=0;//模拟双击
	private int listViewCurrentPostion=-1;
	
	private boolean myLocationIsShow=true;
	private boolean scaleBTIsShow=true;
	private boolean inputSearchIsShow=false;
	private boolean orClickMap=false;
	
	private static final int ACTION_MYUSERINFO=1;
	private static final int ACTION_SEARCHINPUT=2;
	
	private String mCityName = "全国";//云检索设置的默认搜索城市
	private CloudSearch mCloudSearch=null;//云检索对象
	private String mTableID = "56de1863305a2a3288f2f05a";//云图tableid
	private String mKeyWord = ""; // 搜索关键字
	private CloudSearch.Query mQuery=null;//云图查询语句对象
	private List<CloudItem> mCloudItems;//CloudItem类型结果数组
	private CloudOverlay mCloudOverlay;//工具类
	private ArrayList<CloudItem> items = new ArrayList<CloudItem>();
	public static LatLonPoint myLocation =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);//初始化
        setListener();//设置监听
        initDate();
    }
    

	/************************************************************************************ */
	/*====================================关于初始化操作结束=================================== */
    
    @SuppressWarnings("null")
	private void initDate() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		String str=intent.getStringExtra("content");
		if(str!=null&&!str.equals(" ")){
			ToastUtil.print(MainActivity.this, str, 8000);
			keyWord=str;
			doSearch();
		}
	}
    /**
     * 初始化界面控件
     * @param savedInstanceState
     */
    private void initView(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	  mapView=(MapView) findViewById(R.id.map);
          mapView.onCreate(savedInstanceState);
          myMenu=(ArcMenu) findViewById(R.id.menu_my);
          myBar=(TopBar) findViewById(R.id.bar_my);
          input_search=(Button) findViewById(R.id.input_search);
          input_edit=(EditText) findViewById(R.id.input_edittext);
          
          mPoiDetail = (RelativeLayout) findViewById(R.id.poi_details);
          if(aMap==null){
          	aMap=mapView.getMap();
          	setUpMap();
          }
          lv_showdetail=(ListView) findViewById(R.id.lv_showdetail);
          tv_looklist=(TextView) findViewById(R.id.tv_looklist);
          
          
          
	}
	/**
	 * 初始化地图控件
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		//aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);//是否开启定位
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
		aMap.getUiSettings().setScaleControlsEnabled(true);//地图放大缩小
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}
	/*====================================关于初始化操作开始=================================== */
	/************************************************************************************ */
	
	
	/************************************************************************************ */
	/*====================================关于activity活动开始=============================== */
	/**
	 * activity返回操作
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(requestCode==ACTION_MYUSERINFO){
				//
				doCloudSearch();
			}
			if(requestCode==ACTION_SEARCHINPUT){
				String keystring=data.getStringExtra("content");
				ToastUtil.print(MainActivity.this, keystring, 2000);
				keyWord=keystring;
				doSearch();
			}
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
		if(null != mlocationClient){
			mlocationClient.onDestroy();
		}
	}

	/*====================================关于activity活动结束=============================== */
	/************************************************************************************ */

	
	/************************************************************************************ */
	/*====================================关于所用的监听开始=================================== */
	/**
	 * 关于界面控件的监听
	 */
	private void setListener() {
		setMenuOnItemsClickListener();
		aMap.setOnMarkerClickListener(this);//地图上标记监听
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		aMap.setOnMapClickListener(this);
		//aMap.setOnInfoWindowClickListener(this);
		setListViewOnItemClickListener();
		setLookListOnClickListener();
		setTopBarItemClickListener();

	}


	private void setLookListOnClickListener() {
		tv_looklist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RelativeLayout.LayoutParams layout=(android.widget.RelativeLayout.LayoutParams) mPoiDetail.getLayoutParams();
				layout.height=oriHeight;
				mPoiDetail.setLayoutParams(layout);
				lv_showdetail.setVisibility(View.VISIBLE);
				tv_looklist.setVisibility(View.GONE);
			}
		});
	}


	private void setListViewOnItemClickListener() {
		lv_showdetail.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//ToastUtil.print(MainActivity.this, String.valueOf(position), 3000);
				
                //更新adapter，并设置选中背景
				adapter.setGetSelectPosition(position);
				adapter.notifyDataSetChanged();
				//选中的item对应到地图上的marker
				String LeiXing="";
				PoiItem poiItem=null;
				CloudItem cloudItem=null;
				if(poiItems!=null){
					poiItem=listViewSelectPoiItemToMarkerShow(position);
					LeiXing="poiItem";
				}else if(mCloudItems!=null){
				    cloudItem=listViewSelectCloudItemToMarkerShow(position);
					LeiXing="cloudItem";
				}
				
				
				if(listViewCurrentPostion==position){
					listViewDetailClick++;
					Log.e("outprint", "alang"+String.valueOf(listViewDetailClick));
				}else{
					listViewCurrentPostion=position;
					listViewDetailClick=1;
					Log.e("outprint", "alang"+String.valueOf(position));
				}
				if(listViewDetailClick==2){
					ToastUtil.print(MainActivity.this, "点击了两次", 3000);
					listViewDetailClick=0;
					Intent intent=new Intent(MainActivity.this,ShowPlaceActivity.class);
					Bundle bundle=new Bundle();
					switch(LeiXing){
					  case "poiItem":
						  bundle.putString("tag", "poi");
						  bundle.putParcelable(LeiXing, poiItem);
						  break;
					  case "cloudItem":
						  bundle.putString("tag", "cloud");
						  bundle.putParcelable(LeiXing, cloudItem);
						  break;
					}
					
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}

			/**
			 * 将选中item对应到显示地图上marker
			 * @param position
			 */
			private CloudItem listViewSelectCloudItemToMarkerShow(int position) {
				CloudItem mCloudItem=null;
				if(mlastMarker!=null){
					resetlastmarker("cloud");
				}
				whetherToShowDetailInfo(true);
				
				List<Marker> list=mCloudOverlay.getmPoiMarks();
				for(Marker one:list){
					mCloudItem=(CloudItem) one.getObject();
					if(detailItems.get(position).getPoiId()==mCloudItem.getID()){
						detailMarker=one;
						mlastMarker=one;
						break;
					}
				}
				detailMarker.showInfoWindow();
				detailMarker.setIcon(BitmapDescriptorFactory
						.fromBitmap(BitmapFactory.decodeResource(
								getResources(),
								R.drawable.poi_marker_pressed)));
				
				return mCloudItem;
			}
			
			/**
			 * 将选中item对应到显示地图上marker
			 * @param position
			 */
			private PoiItem listViewSelectPoiItemToMarkerShow(int position) {
				PoiItem poiItem=null;
				if(mlastMarker!=null){
					resetlastmarker("poi");
				}
				whetherToShowDetailInfo(true);
				
				List<Marker> list=poiOverlay.getmPoiMarks();
				for(Marker one:list){
					poiItem=(PoiItem) one.getObject();
					if(detailItems.get(position).getPoiId()==poiItem.getPoiId()){
						detailMarker=one;
						mlastMarker=one;
						break;
					}
				}
				detailMarker.showInfoWindow();
				detailMarker.setIcon(BitmapDescriptorFactory
						.fromBitmap(BitmapFactory.decodeResource(
								getResources(),
								R.drawable.poi_marker_pressed)));
				
				return poiItem;
			}
		});
	}


	private void setTopBarItemClickListener() {
		myBar.setmOnItemClickListener(new OnTopBarItemClickListener() {
			
			@Override
			public void onClick(View v, int pos) {
				Intent intent=null;
				switch (pos) {
				case 0:
					intent=new Intent(MainActivity.this,SearchActivity.class);
					intent.putExtra("whatDoIt", "mapSearch");
					startActivityForResult(intent, ACTION_SEARCHINPUT);
					break;
				case 1:
					/*String str=input_edit.getText().toString();
					if(str==""||str.equals(" ")){
						ToastUtil.print(MainActivity.this, str, 3000);
						return;
					}else{
						keyWord=str;
						doSearch();
					}*/
				default:
					break;
				}
				
			}
		});
	}


	/**
	 * 菜单监听
	 */
	private void setMenuOnItemsClickListener() {
		myMenu.setmOnClickItemListener(new OnMenuItemClickListener() {	
			@Override
			public void onClick(View v, int pos) {
				// TODO Auto-generated method stub		
				ToastUtil.print(MainActivity.this, String.valueOf(pos), 2000);
				Intent intent=null;
				switch (pos) {
				case TO_MYINFO:
					intent=new Intent(MainActivity.this,MyInfoActivity.class);
					startActivityForResult(intent, ACTION_MYUSERINFO);
					break;
                case TO_SEARCH:
                	showSearch();
                	break;
				case TO_MYLOCATION:
					//myLocationIsShow=true;
					whetherToShowMyLocation(myLocationIsShow);
					myLocationIsShow=!myLocationIsShow;
					break;
                case TO_SCALECONTROL:
                	whetherToShowScaleControl(scaleBTIsShow);
                	scaleBTIsShow=!scaleBTIsShow;
					break;
                case 5:
                	if(myLocation==null){
                		ToastUtil.print(MainActivity.this, "无法获取您的定位", 2000);
                		return;
                	}
					intent=new Intent(MainActivity.this,RouteSearchActivity.class);
					Bundle bundle=new Bundle();
					bundle.putParcelable("startPoint", myLocation);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
					break;
				default:
					break;
				}
				
			}

			private void showSearch() {
				// TODO Auto-generated method stub
				//inputSearchIsShow=true;
				whetherToShowInputSearch(inputSearchIsShow);
				inputSearchIsShow=!inputSearchIsShow;
			}
		});
	}


	/*====================================关于所用的监听结束=================================== */
	/************************************************************************************ */

	
	/************************************************************************************ */
	/*====================================关于poi搜索，云检索开始=============================== */
	/**
     * poi检索
     */
	private void doSearch() {
		// TODO Auto-generated method stub
		query = new PoiSearch.Query(keyWord, "", cityCode);
		// keyWord表示搜索字符串，
		//第二个参数表示POI搜索类型，二者选填其一，
		//POI搜索类型共分为以下20种：汽车服务|汽车销售|
		//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
		//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
		//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
		//cityCode表示POI搜索区域的编码，是必须设置参数
		     query.setPageSize(10);// 设置每页最多返回多少条poiitem
		 query.setPageNum(currentPage);//设置查询页码
		 PoiSearch poiSearch;
		 if (lp != null) {
				poiSearch = new PoiSearch(this, query);
				poiSearch.setOnPoiSearchListener(this);
				//poiSearch.setBound(new SearchBound(lp,5000));
				// 设置搜索区域为以lp点为圆心，其周围5000米范围
				poiSearch.searchPOIAsyn();// 异步搜索
		}
		 
	}
	
    /**
     * 云检索
     */
	private void doCloudSearch() {
		// TODO Auto-generated method stub
		mCloudSearch = new CloudSearch(this);
		mCloudSearch.setOnCloudSearchListener(this);
		SearchBound bound = new SearchBound(mCityName);
		try {
			mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
			mCloudSearch.searchCloudAsyn(mQuery);
		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	/*====================================关于poi搜索，云检索结束=============================== */
	/************************************************************************************ */
	
	
	/************************************************************************************ */
	/*====================================关于map回调函数开始================================== */
	
	
	
	/**
	 * 定位成功与否 AMapLocationListener由setLocationListener设置
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
	         if (amapLocation != null&& amapLocation.getErrorCode() == 0) {
	             mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
	            // amapLocation.getLatitude();//获取纬度
	            // amapLocation.getLongitude();//获取经度
	             myLocation=new LatLonPoint(amapLocation.getLatitude(),  amapLocation.getLongitude());
	         } else {
	             String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
	             ToastUtil.print(MainActivity.this, errText, 3000);
	             closeLocation();
	         }
	     
	}
	/**
	 * 激活定位，建立定位链接，添加参数，开始定位
	 * 监听LocationSource由setLocationSource设置
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			// TODO Auto-generated method stub
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//mLocationOption.setOnceLocation(false);//只定位一次
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}
	/**
	 * 停止定位
	 * 监听LocationSource由setLocationSource设置
	 */
	@Override
	public void deactivate() {
		closeLocation();
	}

	private void closeLocation() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	
	/**
	 * 云检索setOnCloudSearchListener监听回调函数
	 */
	@Override
	public void onCloudItemDetailSearched(CloudItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 云检索setOnCloudSearchListener监听回调函数
	 */
	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		if (rCode == 1000) {
			closeLocation();//关闭定位
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(mQuery)) {
					mCloudItems = result.getClouds();
					poiItems=null;
					if (mCloudItems != null && mCloudItems.size() > 0) {
						//并还原点击marker样式
						if (mlastMarker != null) {
							resetlastmarker("cloud");
						}				
						//清理之前搜索结果的marker
						if (mCloudOverlay !=null) {
							mCloudOverlay.removeFromMap();
						}
						aMap.clear();
						mCloudOverlay = new CloudOverlay(aMap, mCloudItems);
						mCloudOverlay.removeFromMap();
						mCloudOverlay.setmSelectMarkListener(this);
						mCloudOverlay.addToMap();
						mCloudOverlay.zoomToSpan();
						whetherToShowDetailInfo(true);
						addDetailToList();//打开LIstview
						orClickMap=true;
					} else {
					//	ToastUtil.print(this, R.string.no_result,3000);
					}
			   }
		  } else {
			ToastUtil.show(MainActivity.this,R.string.no_result,2000);
		  }
	  } else {
		ToastUtil.print(this, ErrorInfo.getErrorInfo(rCode), 2000);
	  }
	}

	
	/**
	 * poi关键字查询回调
	 */
	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * poi关键字查询回调
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rcode) {
		Log.e("outprint", "alang"+rcode);
		if (rcode == 1000) {
			closeLocation();//关闭定位
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
				
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					mCloudItems=null;
					Log.e("outprint","lang"+ poiItems.size());
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {
						//并还原点击marker样式
						if (mlastMarker != null) {
							resetlastmarker("poi");
						}				
						//清理之前搜索结果的marker
						if (poiOverlay !=null) {
							poiOverlay.removeFromMap();
						}
						aMap.clear();
						poiOverlay = new MyPoiOverlayUtil(aMap, poiItems);
					    //回调根据是第几条数据库调用第几个mark标记
						poiOverlay.setmSelectMarkListener(this);
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
						whetherToShowDetailInfo(true);
						addDetailToList();//打开LIstview
						orClickMap=true;
						
						/*aMap.addMarker(new MarkerOptions()
						.anchor(0.5f, 0.5f)
						.icon(BitmapDescriptorFactory
								.fromBitmap(BitmapFactory.decodeResource(
										getResources(), R.drawable.point4)))
						.position(new LatLng(lp.getLatitude(), lp.getLongitude())));
						
						aMap.addCircle(new CircleOptions()
						.center(new LatLng(lp.getLatitude(),
								lp.getLongitude())).radius(5000)
						.strokeColor(Color.BLUE)
						.fillColor(Color.argb(50, 1, 1, 1))
						.strokeWidth(2));*/

					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(MainActivity.this,R.string.no_result,2000);
					}
				}
			} 
			
		}else {
			Log.e("outprint", "alang"+"查询失败");
			ToastUtil.print(MainActivity.this,ErrorInfo.getErrorInfo(rcode),2000);
		}
	}

	// 将之前被点击的marker置为原来的状态
	private void resetlastmarker(String str) {
		int index;
		if(str=="poi"){
			index= poiOverlay.getPoiIndex(mlastMarker);
		}else{
			
			index=mCloudOverlay.getPoiIndex(mlastMarker);
		}
		if (index < 10) {
			mlastMarker.setIcon(BitmapDescriptorFactory
					.fromBitmap(BitmapFactory.decodeResource(
							getResources(),
							ComResource.markers[index])));
		}else {
			mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
			BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight)));
		}
		mlastMarker = null;
		
	}
	
	private void whetherToShowDetailInfo(boolean isToShow) {
		if (isToShow) {
			mPoiDetail.setVisibility(View.VISIBLE);

		} else {
			mPoiDetail.setVisibility(View.GONE);

		}
	}
	private void whetherToShowMyLocation(boolean isToShow){
		if(isToShow){
			ToastUtil.print(MainActivity.this, "true", 3000);
			aMap.getUiSettings().setMyLocationButtonEnabled(isToShow);
		}else{
			aMap.getUiSettings().setMyLocationButtonEnabled(isToShow);
		}
	}
	
	private void whetherToShowScaleControl(boolean isToShow){
		if(isToShow){
			ToastUtil.print(MainActivity.this, "true", 3000);
			aMap.getUiSettings().setScaleControlsEnabled(isToShow);
		}else{
			aMap.getUiSettings().setScaleControlsEnabled(isToShow);
		}
	}
	
	private void whetherToShowInputSearch(boolean isToShow){
		if(isToShow){
			myBar.setVisibility(View.VISIBLE);
		}else{
			myBar.setVisibility(View.GONE);
		}
	}
	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.print(this, infomation,2000);

	}

	/**
	 * 点击地图上的market回调此方法
	 * OnMarkerClickListener
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		marker.showInfoWindow();
		Log.e("outprint","lang:"+"onMarkerClick");
		if (marker.getObject() != null) {
			whetherToShowDetailInfo(true);
			try {
				if(poiItems!=null){
					PoiItem mCurrentPoi = (PoiItem) marker.getObject();
					setPoiItemDisplayContent(mCurrentPoi);
				}else if(mCloudItems!=null){
					CloudItem mCurrentPoi = (CloudItem) marker.getObject();
					setPoiItemDisplayContent(mCurrentPoi);
				}		
				if (mlastMarker == null) {
					mlastMarker = marker;
				} else {
					// 将之前被点击的marker置为原来的状态
					resetlastmarker("poi");
					mlastMarker = marker;
				}
				detailMarker = marker;
				detailMarker.setIcon(BitmapDescriptorFactory
									.fromBitmap(BitmapFactory.decodeResource(
											getResources(),
											R.drawable.poi_marker_pressed)));

				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			whetherToShowDetailInfo(false);
			resetlastmarker("poi");
		}
		return true;
	}

	/**
	 * 找到回应的LIstview 行
	 * @param mCurrentPoi
	 */
	private void setPoiItemDisplayContent(final CloudItem mCurrentPoi) {
		if(mCloudItems!=null&&mCloudItems.size()>0){
			for (int item=0;item<mCloudItems.size();item++) {
				if(mCurrentPoi.getID()==mCloudItems.get(item).getID()){
					adapter.setGetSelectPosition(item);
					adapter.notifyDataSetChanged();
					//adapter.set
					lv_showdetail.setSelection(item);
				}
			}
		}
	}
	/**
	 * 找到回应的LIstview 行
	 * @param mCurrentPoi
	 */
	private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
		if(poiItems!=null&&poiItems.size()>0){
			for (int item=0;item<poiItems.size();item++) {
				if(mCurrentPoi.getPoiId()==poiItems.get(item).getPoiId()){
					Log.e("outprint","lang"+mCurrentPoi.getPoiId());
					adapter.setGetSelectPosition(item);
					adapter.notifyDataSetChanged();
					//adapter.set
					lv_showdetail.setSelection(item);
				}
			}
		}
	}
/*	@Override
	public void onMapClick(LatLng arg0) {
		whetherToShowDetailInfo(false);
		if (mlastMarker != null) {
			resetlastmarker();
		}
	}*/

	/**
	 * marker详细信息（地址，电话，评价）显示
	 * setInfoWindowAdapter
	 */
	@Override
	public View getInfoWindow(Marker marker) {
		
		View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());
		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());
		return view;
	}

	private void addDetailToList() {
	// TODO Auto-generated method stub
		Log.e("outprint", "alang"+"addDetailToList");
		detailItems=new ArrayList<SearchedItemDetail>();
		if(poiItems!=null){
			Log.e("outprint", "alang"+"poiItems");
			for (PoiItem item : poiItems) {
				Log.e("outprint", "alang"+item.getTitle()+item.getSnippet()+item.getAdName()+item.getPoiId());
				SearchedItemDetail one=new SearchedItemDetail(R.drawable.ic_launcher,
						item.getPoiId(),item.getTitle(),item.getSnippet(),R.drawable.goplace,
						item.getLatLonPoint());
				detailItems.add(one);
			}
		}else if(mCloudItems!=null){
			Log.e("outprint", "alang"+"mcloudItems");
			for (CloudItem item : mCloudItems) {
				Log.e("outprint", "alang"+item.getTitle()+item.getSnippet()+item.getCreatetime()+item.getID());
				SearchedItemDetail one=new SearchedItemDetail(R.drawable.ic_launcher,
						item.getID(),item.getTitle(),item.getSnippet(),R.drawable.goplace,
						item.getLatLonPoint());
				detailItems.add(one);
			}
		}else{
			Log.e("outprint", "alang"+"都没有添加到datailitems");
		}
	    
		adapter=new SearchedItemAdapter(MainActivity.this,detailItems);
	    lv_showdetail.setAdapter(adapter); 
    }

	/**
	 * setInfoWindowAdapter
	 */
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BitmapDescriptor selectMark(int arg0) {
		// TODO Auto-generated method stub
		//Log.e("outprint", "alang"+arg0);
		if (arg0 < 10) {
			BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
					BitmapFactory.decodeResource(getResources(),ComResource.markers[arg0]));
			return icon;
		}else {
			BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
					BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed));
			return icon;
		}	
	}

	/**
	 * 地图点击监听setOnMapClickListener
	 */
	int mapClickTime=1;
	int oriHeight=0;
	@Override
	public void onMapClick(LatLng latlng) {
		// TODO Auto-generated method stub

		
		if(orClickMap){
			RelativeLayout.LayoutParams layout=(android.widget.RelativeLayout.LayoutParams) mPoiDetail.getLayoutParams();
			if(tv_looklist.getVisibility()==View.GONE){
				oriHeight=layout.height;
				layout.height=120;
				tv_looklist.setClickable(true);
				tv_looklist.setVisibility(View.VISIBLE);
				mapClickTime++;
			}
			lv_showdetail.setVisibility(View.GONE);
			Log.e("outprint", "lang"+String.valueOf(layout.height));
			mPoiDetail.setLayoutParams(layout);
		}
	}
	/*====================================关于map回调函数结束================================== */
	/************************************************************************************ */
}
