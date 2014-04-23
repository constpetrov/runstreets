package com.constpetrov.runstreets.gui;

import java.util.List;

import com.constpetrov.runstreets.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckboxAdapter<T> extends BaseAdapter {
	
	private final List<OptionItem<T>> elements;
	
	private final LayoutInflater inflater;
	
	public CheckboxAdapter(List<OptionItem<T>> elems, LayoutInflater inf){
		elements = elems;
		inflater = inf;
	}
	
	@Override
	public int getCount() {
		if(elements == null){
			return 0;
		}
		return elements.size();
	}

	@Override
	public Object getItem(int position) {
		if(elements != null || elements.size() > position){
			return elements.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
        if (convertView == null) {
            view = inflater.inflate(R.layout.checkbox_group_row, null);
            final ViewHolder childHolder = new ViewHolder();
            childHolder.cb = (CheckBox) view.findViewById(R.id.checkBox_group);              
            childHolder.cb
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton button,
                                boolean isChecked) {
                            @SuppressWarnings("rawtypes")
							OptionItem item = (OptionItem) childHolder.cb
                                    .getTag();
                            item.setSelected(button.isChecked());
                        }
                    });

            view.setTag(childHolder);
            childHolder.cb.setTag(elements.get(position));
        } else {
            view = convertView;         
            ((ViewHolder) view.getTag()).cb.setTag(elements.get(position));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.cb.setChecked(elements.get(position)
                .isSelected());
        holder.cb.setText(elements.get(position)
                .getName());

        return view;
	}
	public static class ViewHolder {
	    protected CheckBox cb;
	}
}
