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

	private selectMarkListener mSelectMarkListener;//���ø���ͼƬ�ļ���
    
	public void setmSelectMarkListener(selectMarkListener mSelectMarkListener) {
		this.mSelectMarkListener = mSelectMarkListener;
	}

	public MyPoiOverlayUtil(AMap amap ,List<PoiItem> pois) {
		mamap = amap;
        mPois = pois;
	}

	
    /**
     * ���Marker����ͼ�С�
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
     * ȥ��PoiOverlay�����е�Marker��
     *
     * @since V2.1.0
     */
    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }

    /**
     * �ƶ���ͷ����ǰ���ӽǡ�
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
     * ��ȡ��Ӧ���������ݵ���ʾͼ��
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
     * ��ȡ��Ӧ�������ݵı���
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
     * ��marker�еõ�poi��list��λ�á�
     *
     * @param marker һ����ǵĶ���
     * @return ���ظ�marker��Ӧ��poi��list��λ�á�
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
     * ���ص�index��poi����Ϣ��
     * @param index �ڼ���poi��
     * @return poi����Ϣ��poi���������������ģ��Ļ������İ���com.amap.api.services.core���е��� <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core�е���">PoiItem</a></strong>��
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

