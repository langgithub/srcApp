package com.lang.scrapp.po;

public class SearchGridItem {

	private int iconId;
	private String text;
	public int getIconId() {
		return iconId;
	}
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public SearchGridItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SearchGridItem(int iconId, String text) {
		super();
		this.iconId = iconId;
		this.text = text;
	}
	
}
