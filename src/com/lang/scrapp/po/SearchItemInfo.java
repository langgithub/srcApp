package com.lang.scrapp.po;

public class SearchItemInfo {

	private int id;
	private String title;
	private String address;
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
	public SearchItemInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SearchItemInfo(int id, String title, String address) {
		super();
		this.id = id;
		this.title = title;
		this.address = address;
	}
	
}
