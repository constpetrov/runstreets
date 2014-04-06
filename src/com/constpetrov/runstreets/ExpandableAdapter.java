package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ExpandableAdapter<T> extends BaseExpandableListAdapter {

    private List<List<OptionItem<T>>> list;
    private ArrayList<OptionItem<T>> groups;
    private LayoutInflater inflater;

    public ExpandableAdapter(LayoutInflater inflater, ArrayList<OptionItem<T>> groups,
            List<List<OptionItem<T>>> children) {
        this.groups = groups;
        this.list = children;
        this.inflater = inflater;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (list != null && list.size() > groupPosition
                && list.get(groupPosition) != null) {
            if (list.get(groupPosition).size() > childPosition)
                return list.get(groupPosition).get(childPosition);
        }

        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition,
            final int childPosition, boolean isLastChild, View convertView,
            ViewGroup parent) {

        View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.child_row, null);
            final ViewHolder childHolder = new ViewHolder();
            childHolder.cb = (CheckBox) view.findViewById(R.id.checkBox_child);              
            childHolder.cb
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton button,
                                boolean isChecked) {
                            OptionItem item = (OptionItem) childHolder.cb
                                    .getTag();
                            item.setSelected(button.isChecked());
                        }
                    });

            view.setTag(childHolder);
            childHolder.cb.setTag(list.get(groupPosition).get(childPosition));
        } else {
            view = convertView;         
            ((ViewHolder) view.getTag()).cb.setTag(list.get(groupPosition).get(childPosition));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.cb.setChecked(list.get(groupPosition).get(childPosition)
                .isSelected());
        holder.cb.setText(list.get(groupPosition).get(childPosition)
                .getName());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (list != null && list.size() > groupPosition
                && list.get(groupPosition) != null)
            return list.get(groupPosition).size();

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groups != null && groups.size() > groupPosition)
            return groups.get(groupPosition);

        return null;
    }

    @Override
    public int getGroupCount() {
        if (groups != null)
            return groups.size();

        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.group_row, null);
            final ViewHolder childHolder = new ViewHolder();
            childHolder.cb = (CheckBox) view.findViewById(R.id.checkBox_group);              
            childHolder.cb
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton button,
                                boolean isChecked) {
                            OptionItem item = (OptionItem) childHolder.cb
                                    .getTag();
                            item.setSelected(button.isChecked());
                        }
                    });

            view.setTag(childHolder);
            childHolder.cb.setTag(groups.get(groupPosition));
        } else {
            view = convertView;         
            ((ViewHolder) view.getTag()).cb.setTag(groups.get(groupPosition));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.cb.setChecked(groups.get(groupPosition)
                .isSelected());
        holder.cb.setText(groups.get(groupPosition)
                .getName());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    public static class ViewHolder {
	    protected CheckBox cb;
	}
}