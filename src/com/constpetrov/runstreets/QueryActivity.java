package com.constpetrov.runstreets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;

public class QueryActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query);
		
		
		ArrayList<OptionItem> groups = new ArrayList<OptionItem>();
	    List<List<OptionItem>> children = new ArrayList<List<OptionItem>>();

	    for (int i = 0; i < 10; i++) {
	    	OptionItem groupName = new OptionItem("", "Group " + i);
	        groups.add(groupName);
	        List<OptionItem> temp = new ArrayList<OptionItem>();
	        for (int j = 0; j < 5; j++) {
	            temp.add(new OptionItem(groupName.getChildName(), "Child " + j));
	        }
	        children.add(temp);
	    }

	    children.get(0).get(3).setSelected(true);
	    children.get(5).get(2).setSelected(true);
	    children.get(8).get(1).setSelected(true);
	    children.get(3).get(4).setSelected(true);

	    ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView1);
	    elv.setAdapter(new ExpandableAdapter(groups, children));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.query, menu);
		return true;
	}

	public static class ViewHolder {
	    protected CheckBox cb;
	}
	
	public class ExpandableAdapter extends BaseExpandableListAdapter {

	    private List<List<OptionItem>> list;
	    private ArrayList<OptionItem> groups;

	    public ExpandableAdapter(ArrayList<OptionItem> groups,
	            List<List<OptionItem>> children) {
	        this.groups = groups;
	        this.list = children;
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
	            view = getLayoutInflater().inflate(R.layout.child_row, null);
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
	                .getChildName());

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
	            view = getLayoutInflater().inflate(R.layout.group_row, null);
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
	                .getChildName());

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
	}
	
}
