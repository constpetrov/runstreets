package com.constpetrov.runstreets.gui;

import java.util.List;

import com.constpetrov.runstreets.R;
import com.constpetrov.runstreets.R.id;
import com.constpetrov.runstreets.R.layout;
import com.constpetrov.runstreets.db.StreetsDataSource;
import com.constpetrov.runstreets.model.StreetHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter {

	private final List<StreetHistory> history;
	private final LayoutInflater inflater;
	
	public HistoryAdapter(List<StreetHistory> h, LayoutInflater i){
		history = h;
		inflater = i;
	}
	
	@Override
	public int getCount() {
		if(history != null){
			return history.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(history != null && history.size() > position){
			return history.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup groupView) {
		View view = null;
		if(convertView == null){
			view = inflater.inflate(R.layout.result_item_rename, null);
			TextView name = (TextView)view.findViewById(R.id.rename_name);
			name.setText(history.get(position).getName());
			
			TextView year = (TextView)view.findViewById(R.id.rename_year);
			year.setText(history.get(position).getYear());
			
			TextView type = (TextView)view.findViewById(R.id.rename_type);
			type.setText(StreetsDataSource.get().getStreetTypeName(history.get(position).getType()));
		} else {
			view = convertView;
		}
		return view;
	}

}
