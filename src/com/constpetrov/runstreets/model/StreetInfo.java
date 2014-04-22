package com.constpetrov.runstreets.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class StreetInfo implements Parcelable{
	private Street street;
	private String streetTypeName;
	private List<Area> areas;
	private List<StreetHistory> history;
	
	public static Parcelable.Creator<StreetInfo> CREATOR = new Parcelable.Creator<StreetInfo>() {

		@Override
		public StreetInfo createFromParcel(Parcel source) {
			return new StreetInfo(source);
		}

		@Override
		public StreetInfo[] newArray(int size) {
			return new StreetInfo[size];
		}
	};

	public StreetInfo(){};
	
	public StreetInfo(Parcel in){
		street = in.readParcelable(null);
		streetTypeName = in.readString();
		areas = new ArrayList<Area>();
		areas.addAll((Collection<? extends Area>) Arrays.asList(in.readParcelableArray(null)));
		history = new ArrayList<StreetHistory>();
		history.addAll((Collection<? extends StreetHistory>) Arrays.asList(in.readParcelableArray(null)));
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(street, 0);
		dest.writeString(streetTypeName);
		dest.writeParcelableArray(areas.toArray(new Area[areas.size()]), 0);
		dest.writeParcelableArray(history.toArray(new StreetHistory[history.size()]), 0);
	}
	
	public Street getStreet() {
		return street;
	}
	public void setStreet(Street street) {
		this.street = street;
	}
	public String getStreetTypeName() {
		return streetTypeName;
	}
	public void setStreetTypeName(String streetTypeName) {
		this.streetTypeName = streetTypeName;
	}
	public List<Area> getAreas() {
		return areas;
	}
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	public List<StreetHistory> getHistory() {
		return history;
	}
	public void setHistory(List<StreetHistory> history) {
		this.history = history;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

}
