package com.constpetrov.runstreets.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;

public class StreetHistory  implements Parcelable{
	public static Parcelable.Creator<StreetHistory> CREATOR = new Parcelable.Creator<StreetHistory>() {

		@Override
		public StreetHistory createFromParcel(Parcel source) {
			return new StreetHistory(source);
		}

		@Override
		public StreetHistory[] newArray(int size) {
			return new StreetHistory[size];
		}
	};

	private int id;
	private int streetId;
	private String name;
	private int type;
	private String doc;

	public StreetHistory(){};
	
	public StreetHistory(Parcel in){
		id = in.readInt();
		streetId = in.readInt();
		name = in.readString();
		type = in.readInt();
		doc = in.readString();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(streetId);
		dest.writeString(name);
		dest.writeInt(type);
		dest.writeString(doc);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStreetId() {
		return streetId;
	}
	public void setStreetId(int streetId) {
		this.streetId = streetId;
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
	
	public String getYear(){
		Pattern p = Pattern.compile(".*(\\d{4}).*");
		Matcher m = p.matcher(doc);
		if(m.matches()){
			return m.group(1);
		}
		return "";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	

}
