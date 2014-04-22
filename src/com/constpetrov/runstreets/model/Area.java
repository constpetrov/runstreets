package com.constpetrov.runstreets.model;

import java.util.StringTokenizer;

import android.os.Parcel;
import android.os.Parcelable;

public class Area implements Parcelable{
	public static Parcelable.Creator<Area> CREATOR = new Parcelable.Creator<Area>() {

		@Override
		public Area createFromParcel(Parcel source) {
			return new Area(source);
		}

		@Override
		public Area[] newArray(int size) {
			return new Area[size];
		}
	};
	private int id;
	private String code;
	private int parentId;
	private int type;
	private String name;
	private String doc;
	
	public Area(){};
	
	public Area(Parcel in){
		id = in.readInt();
		code = in.readString();
		parentId = in.readInt();
		type = in.readInt();
		name = in.readString();
		doc = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(code);
		dest.writeInt(parentId);
		dest.writeInt(type);
		dest.writeString(name);
		dest.writeString(doc);
	}

	@Override
	public String toString(){
		return getAbbr();
	}
	
	private String getAbbr() {
		if(getName().startsWith("Зелен")){
			return "Зеленоград";
		}
		StringTokenizer tokenizer = new StringTokenizer(getName(), " -");
		StringBuilder b = new StringBuilder();
		while (tokenizer.hasMoreTokens()){
			b.append(tokenizer.nextToken().substring(0,1).toUpperCase());
		}
		return b.toString();
	}

	public String getDistrictName(){
		return getName().replace("Район ", "").replace(" район", "");
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
}
