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
	 * poi��ѯ��ز���
	 */
	private String keyWord;
	private String cityCode="�ɶ�";//��������
	private int currentPage=1;//��ǰ��ѯҳ
	private PoiSearch.Query query;//��ѯ��sql���
	private PoiResult poiResult;
	private ArrayList<PoiItem> poiItems;
	private LatLonPoint lp = new LatLonPoint(39.993167, 116.473274);
	private MyPoiOverlayUtil poiOverlay;
	private Marker mlastMarker;
	private Marker detailMarker;
	
	private List<SearchedItemDetail> detailItems;
	private ListView lv_showdetail;
	private SearchedItemAdapter adapter;
	private int listViewDetailClick=0;//ģ��˫��
	private int listViewCurrentPostion=-1;
	
	private boolean myLocationIsShow=true;
	private boolean scaleBTIsShow=true;
	private boolean inputSearchIsShow=false;
	private boolean orClickMap=false;
	
	private static final int ACTION_MYUSERINFO=1;
	private static final int ACTION_SEARCHINPUT=2;
	
	private String mCityName = "ȫ��";//�Ƽ������õ�Ĭ����������
	private CloudSearch mCloudSearch=null;//�Ƽ�������
	private String mTableID = "56de1863305a2a3288f2f05a";//��ͼtableid
	private String mKeyWord = ""; // �����ؼ���
	private CloudSearch.Query mQuery=null;//��ͼ��ѯ������
	private List<CloudItem> mCloudItems;//CloudItem���ͽ������
	private CloudOverlay mCloudOverlay;//������
	private ArrayList<CloudItem> items = new ArrayList<CloudItem>();
	public static LatLonPoint myLocation =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);//��ʼ��
        setListener();//���ü���
        initDate();
    }
    

	/************************************************************************************ */
	/*====================================���ڳ�ʼ����������=================================== */
    
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
     * ��ʼ������ؼ�
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
	 * ��ʼ����ͼ�ؼ�
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// ���ö�λ����
		//aMap.getUiSettings().setMyLocationButtonEnabled(false);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);//�Ƿ�����λ
		// ���ö�λ������Ϊ��λģʽ �������ɶ�λ��������ͼ������������ת����
		aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
		aMap.getUiSettings().setScaleControlsEnabled(true);//��ͼ�Ŵ���С
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}
	/*====================================���ڳ�ʼ��������ʼ=================================== */
	/************************************************************************************ */
	
	
	/************************************************************************************ */
	/*====================================����activity���ʼ=============================== */
	/**
	 * activity���ز���
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

	/*====================================����activity�����=============================== */
	/************************************************************************************ */

	
	/************************************************************************************ */
	/*====================================�������õļ�����ʼ=================================== */
	/**
	 * ���ڽ���ؼ��ļ���
	 */
	private void setListener() {
		setMenuOnItemsClickListener();
		aMap.setOnMarkerClickListener(this);//��ͼ�ϱ�Ǽ���
		aMap.setInfoWindowAdapter(this);// �����ʾinfowindow�����¼�
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
				
                //����adapter��������ѡ�б���
				adapter.setGetSelectPosition(position);
				adapter.notifyDataSetChanged();
				//ѡ�е�item��Ӧ����ͼ�ϵ�marker
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
					ToastUtil.print(MainActivity.this, "���������", 3000);
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
			 * ��ѡ��item��Ӧ����ʾ��ͼ��marker
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
			 * ��ѡ��item��Ӧ����ʾ��ͼ��marker
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
	 * �˵�����
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
                		ToastUtil.print(MainActivity.this, "�޷���ȡ���Ķ�λ", 2000);
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


	/*====================================�������õļ�������=================================== */
	/************************************************************************************ */

	
	/************************************************************************************ */
	/*====================================����poi�������Ƽ�����ʼ=============================== */
	/**
     * poi����
     */
	private void doSearch() {
		// TODO Auto-generated method stub
		query = new PoiSearch.Query(keyWord, "", cityCode);
		// keyWord��ʾ�����ַ�����
		//�ڶ���������ʾPOI�������ͣ�����ѡ����һ��
		//POI�������͹���Ϊ����20�֣���������|��������|
		//����ά��|Ħ�г�����|��������|�������|�������|�������з���|ҽ�Ʊ�������|
		//ס�޷���|�羰��ʤ|����סլ|�����������������|�ƽ��Ļ�����|��ͨ��ʩ����|
		//���ڱ��շ���|��˾��ҵ|��·������ʩ|������ַ��Ϣ|������ʩ
		//cityCode��ʾPOI��������ı��룬�Ǳ������ò���
		     query.setPageSize(10);// ����ÿҳ��෵�ض�����poiitem
		 query.setPageNum(currentPage);//���ò�ѯҳ��
		 PoiSearch poiSearch;
		 if (lp != null) {
				poiSearch = new PoiSearch(this, query);
				poiSearch.setOnPoiSearchListener(this);
				//poiSearch.setBound(new SearchBound(lp,5000));
				// ������������Ϊ��lp��ΪԲ�ģ�����Χ5000�׷�Χ
				poiSearch.searchPOIAsyn();// �첽����
		}
		 
	}
	
    /**
     * �Ƽ���
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

	/*====================================����poi�������Ƽ�������=============================== */
	/************************************************************************************ */
	
	
	/************************************************************************************ */
	/*====================================����map�ص�������ʼ================================== */
	
	
	
	/**
	 * ��λ�ɹ���� AMapLocationListener��setLocationListener����
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
	         if (amapLocation != null&& amapLocation.getErrorCode() == 0) {
	             mListener.onLocationChanged(amapLocation);// ��ʾϵͳС����
	            // amapLocation.getLatitude();//��ȡγ��
	            // amapLocation.getLongitude();//��ȡ����
	             myLocation=new LatLonPoint(amapLocation.getLatitude(),  amapLocation.getLongitude());
	         } else {
	             String errText = "��λʧ��," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
	             ToastUtil.print(MainActivity.this, errText, 3000);
	             closeLocation();
	         }
	     
	}
	/**
	 * ���λ��������λ���ӣ���Ӳ�������ʼ��λ
	 * ����LocationSource��setLocationSource����
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			// TODO Auto-generated method stub
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//mLocationOption.setOnceLocation(false);//ֻ��λһ��
			//���ö�λ����
			mlocationClient.setLocationListener(this);
			//����Ϊ�߾��ȶ�λģʽ
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//���ö�λ����
			mlocationClient.setLocationOption(mLocationOption);
			// �˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			// ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����stopLocation()������ȡ����λ����
			// �ڶ�λ�������ں��ʵ��������ڵ���onDestroy()����
			// �ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������stopLocation()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mlocationClient.startLocation();
		}
	}
	/**
	 * ֹͣ��λ
	 * ����LocationSource��setLocationSource����
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
	 * �Ƽ���setOnCloudSearchListener�����ص�����
	 */
	@Override
	public void onCloudItemDetailSearched(CloudItemDetail arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * �Ƽ���setOnCloudSearchListener�����ص�����
	 */
	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		if (rCode == 1000) {
			closeLocation();//�رն�λ
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(mQuery)) {
					mCloudItems = result.getClouds();
					poiItems=null;
					if (mCloudItems != null && mCloudItems.size() > 0) {
						//����ԭ���marker��ʽ
						if (mlastMarker != null) {
							resetlastmarker("cloud");
						}				
						//����֮ǰ���������marker
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
						addDetailToList();//��LIstview
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
	 * poi�ؼ��ֲ�ѯ�ص�
	 */
	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * poi�ؼ��ֲ�ѯ�ص�
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rcode) {
		Log.e("outprint", "alang"+rcode);
		if (rcode == 1000) {
			closeLocation();//�رն�λ
			if (result != null && result.getQuery() != null) {// ����poi�Ľ��
				if (result.getQuery().equals(query)) {// �Ƿ���ͬһ��
				
					poiResult = result;
					poiItems = poiResult.getPois();// ȡ�õ�һҳ��poiitem���ݣ�ҳ��������0��ʼ
					mCloudItems=null;
					Log.e("outprint","lang"+ poiItems.size());
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// ����������poiitem����ʱ���᷵�غ��������ؼ��ֵĳ�����Ϣ
					if (poiItems != null && poiItems.size() > 0) {
						//����ԭ���marker��ʽ
						if (mlastMarker != null) {
							resetlastmarker("poi");
						}				
						//����֮ǰ���������marker
						if (poiOverlay !=null) {
							poiOverlay.removeFromMap();
						}
						aMap.clear();
						poiOverlay = new MyPoiOverlayUtil(aMap, poiItems);
					    //�ص������ǵڼ������ݿ���õڼ���mark���
						poiOverlay.setmSelectMarkListener(this);
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
						whetherToShowDetailInfo(true);
						addDetailToList();//��LIstview
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
			Log.e("outprint", "alang"+"��ѯʧ��");
			ToastUtil.print(MainActivity.this,ErrorInfo.getErrorInfo(rcode),2000);
		}
	}

	// ��֮ǰ�������marker��Ϊԭ����״̬
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
	 * poiû�����������ݣ�����һЩ�Ƽ����е���Ϣ
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "�Ƽ�����\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "��������:" + cities.get(i).getCityName() + "��������:"
					+ cities.get(i).getCityCode() + "���б���:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.print(this, infomation,2000);

	}

	/**
	 * �����ͼ�ϵ�market�ص��˷���
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
					// ��֮ǰ�������marker��Ϊԭ����״̬
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
	 * �ҵ���Ӧ��LIstview ��
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
	 * �ҵ���Ӧ��LIstview ��
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
	 * marker��ϸ��Ϣ����ַ���绰�����ۣ���ʾ
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
			Log.e("outprint", "alang"+"��û����ӵ�datailitems");
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
	 * ��ͼ�������setOnMapClickListener
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
	/*====================================����map�ص���������================================== */
	/************************************************************************************ */
}
