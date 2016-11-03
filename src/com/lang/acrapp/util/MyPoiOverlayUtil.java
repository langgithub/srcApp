package com.lang.acrapp.util;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;


public class MyPoiOverlayUtil {

	private AMap mamap;
	private List<PoiItem> mPois;
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
    public ArrayList<Marker> getmPoiMarks() {
		return mPoiMarks;
	}

	private selectMarkListener mSelectMarkListener;//设置各自图片的监听
    
	public void setmSelectMarkListener(selectMarkListener mSelectMarkListener) {
		this.mSelectMarkListener = mSelectMarkListener;
	}

	public MyPoiOverlayUtil(AMap amap ,List<PoiItem> pois) {
		mamap = amap;
        mPois = pois;
	}

	
    /**
     * 添加Marker到地图中。
     * @since V2.1.0
     */
    public void addToMap() {
        for (int i = 0; i < mPois.size(); i++) {
            Marker marker = mamap.addMarker(getMarkerOptions(i));
            PoiItem item = mPois.get(i);
			marker.setObject(item);
            mPoiMarks.add(marker);
        }
    }

    /**
     * 去掉PoiOverlay上所有的Marker。
     *
     * @since V2.1.0
     */
    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }

    /**
     * 移动镜头到当前的视角。
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (mPois != null && mPois.size() > 0) {
            if (mamap == null)
                return;
            LatLngBounds bounds = getLatLngBounds();
            mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                    mPois.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int index) {
        return new MarkerOptions()
                .position(
                        new LatLng(mPois.get(index).getLatLonPoint()
                                .getLatitude(), mPois.get(index)
                                .getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .icon(getBitmapDescriptor(index));
    }

    /**
     * 获取对应是哪条数据的显示图标
     * @param index
     * @return
     */
    private BitmapDescriptor getBitmapDescriptor(int index) {
		// TODO Auto-generated method stub
    	if(mSelectMarkListener!=null){
    		return mSelectMarkListener.selectMark(index);
    	}
		return null;
	}

    /**
     * 获取对应哪条数据的标题
     * @param index
     * @return
     */
	protected String getTitle(int index) {
        return mPois.get(index).getTitle();
    }

    protected String getSnippet(int index) {
        return mPois.get(index).getSnippet();
    }

    /**
     * 从marker中得到poi在list的位置。
     *
     * @param marker 一个标记的对象。
     * @return 返回该marker对应的poi在list的位置。
     * @since V2.1.0
     */
    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回第index的poi的信息。
     * @param index 第几个poi。
     * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
     * @since V2.1.0
     */
    public PoiItem getPoiItem(int index) {
        if (index < 0 || index >= mPois.size()) {
            return null;
        }
        return mPois.get(index);
    }
   

    public interface selectMarkListener{
    	BitmapDescriptor selectMark(int index);
    }
    
}

