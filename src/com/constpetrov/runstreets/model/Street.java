package com.constpetrov.runstreets.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Street implements Parcelable{
	private int id;
	private int code;
	private String name;
	private int type;
	private String doc;
	private String sort;
	private String sort_second;
	private String position;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getSort_second() {
		return sort_second;
	}
	public void setSort_second(String sort_second) {
		this.sort_second = sort_second;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(code);
		dest.writeString(name);
		dest.writeInt(type);
		dest.writeString(doc);
		dest.writeString(sort);
		dest.writeString(sort_second);
		dest.writeString(position);
	}
	
	private void Street(Parcel in){
		id = in.readInt();
		code = in.readInt();
		name = in.readString();
		type = in.readInt();
		doc = in.readString();
		sort = in.readString();
		sort_second = in.readString();
		position = in.readString();
	}

}
