package com.constpetrov.runstreets;

import java.util.List;

import com.constpetrov.runstreets.ExpandableCheckboxAdapter.ViewHolder;
import com.constpetrov.runstreets.model.Street;
import com.constpetrov.runstreets.model.StreetHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ExpandableResListAdapter extends BaseExpandableListAdapter {

	private final List<Street> streets;
	private final List<List<StreetHistory>> history;
	private final LayoutInflater inflater;
	
	public ExpandableResListAdapter(LayoutInflater inflater, List<Street> streets, List<List<StreetHistory>> history){
		this.streets = streets;
		this.history = history;
		this.inflater = inflater;
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		if(history == null || history.size() < arg0){
			return null;
		}
		if(history.get(arg0) == null || history.get(arg0).size() < arg1){
			return null;
		}
		return history.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(final int groupPosition,
            final int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {
		View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.result_subitem, null);
            
            
        } else {
            view = convertView;         
        }
        
        
        return view;
	}

	@Override
	public int getChildrenCount(int arg0) {
		if (history != null && history.size() > arg0
                && history.get(arg0) != null)
            return history.get(arg0).size();

        return 0;
	}

	@Override
	public Object getGroup(int arg0) {
		if (streets != null && streets.size() > arg0)
            return streets.get(arg0);

        return null;
	}

	@Override
	public int getGroupCount() {
		if (streets != null)
            return streets.size();

        return 0;
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
		View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.result_item, null);
            TextView nameView = (TextView)view.findViewById(R.id.street_name);
            TextView typeView = (TextView)view.findViewById(R.id.street_type);
            
            nameView.setText(streets.get(groupPosition).toString());
            
        } else {
            view = convertView;         
        }
        
        
        return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
