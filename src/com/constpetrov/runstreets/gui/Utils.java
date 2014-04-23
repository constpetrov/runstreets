package com.constpetrov.runstreets.gui;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {
	public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter(); 
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        Log.i("Utils", "listAdapter.getCount() = " + listAdapter.getCount());
        for (int i = 0; i < listAdapter.getCount(); i++) {
        	Log.i("Utils", "i = " + i);
            View listItem = listAdapter.getView(i, null, listView);
            Log.i("Utils", "listItem is " + (listItem == null ? "": "NOT ") + "null");
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
	
	public static String replace(final String str, final Object... data) {
        if ((str == null) || (data == null) || (data.length == 0)) {
            return str;
        }
        String result = str;
        for (int i = 0; i < data.length; i++) {
            String value = data[i].toString();

            // object can be not null but toString can be null (e.g. entityReference.getCaption())
            result = result.replace('{' + String.valueOf(i) + '}', value != null ? value : "?"); //$NON-NLS-1$
        }
        return result;
    }

}
