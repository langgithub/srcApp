package com.lang.scrapp.po;

import java.io.Serializable;

import com.amap.api.services.core.LatLonPoint;

public class SearchedItemDetail implements Serializable{

	private int id;//ͼƬid
	private String poiId;
	private String title;
	private String address;
	private int id_goto;
	
	
	private LatLonPoint lp ;

	public SearchedItemDetail(int id, String poiId, String title,
			String address, int id_goto, LatLonPoint lp) {
		super();
		this.id = id;
		this.poiId = poiId;
		this.title = title;
		this.address = address;
		this.id_goto = id_goto;
		this.lp = lp;
	}


	public LatLonPoint getLp() {
		return lp;
	}


	public void setLp(LatLonPoint lp) {
		this.lp = lp;
	}


	public int getId_goto() {
		return id_goto;
	}


	public void setId_goto(int id_goto) {
		this.id_goto = id_goto;
	}


	public String getPoiId() {
		return poiId;
	}
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public SearchedItemDetail(int id, String title, String address) {
		super();
		this.id = id;
		this.title = title;
		this.address = address;
	}
	
}
