package com.constpetrov.runstreets.gui;

import java.util.Collection;

import com.constpetrov.runstreets.R;
import com.constpetrov.runstreets.db.StreetsDataSource;
import com.constpetrov.runstreets.model.SearchParameters;
import com.constpetrov.runstreets.model.Street;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class ExecQuery extends
		AsyncTask<SearchParameters, Void, Collection<Street>> {
	
	private ProgressDialog pd;
	
	private Context context;
	
	private OnQueryListener listener;
	
	public ExecQuery(Context context, OnQueryListener listener){
		this.context = context;
		this.listener = listener;
		pd = new ProgressDialog(context);
	}
	
	@Override
	protected Collection<Street> doInBackground(SearchParameters... params) {
		Collection<Street> res = StreetsDataSource.get().findStreets(params[0]); 
		return res;
	}

	@Override
	protected void onPostExecute(final Collection<Street> result) {
		pd.dismiss();
		Toast.makeText(context, "Streets found: "+ result.size(), Toast.LENGTH_SHORT).show();
		listener.showResults(result);
	}

	@Override
	protected void onPreExecute() {
		pd.setTitle(R.string.exec_query);
		pd.setMessage(context.getString(R.string.please_wait));
		pd.setCanceledOnTouchOutside(false);
		pd.setCancelable(false);
		pd.show();
	}


}
