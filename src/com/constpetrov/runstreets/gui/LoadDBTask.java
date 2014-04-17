package com.constpetrov.runstreets.gui;

import com.constpetrov.runstreets.R;
import com.constpetrov.runstreets.db.StreetsDataSource;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class LoadDBTask extends AsyncTask<Void, Void, Void> {
	
	
	
	private ProgressDialog dialog;
	
	private Context context;
	
	private CreateUIListener listener;
	
	public LoadDBTask(Context context, CreateUIListener listener){
		this.context = context;
		dialog = new ProgressDialog(context);
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute()
	{
		dialog.setTitle(R.string.db_update);
		dialog.setMessage(context.getString(R.string.please_wait));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected Void doInBackground(Void... unused)
	{
		StreetsDataSource.get(context);
		return null;
	}

	@Override
	protected void onPostExecute(final Void success)
	{
		if(dialog.isShowing()){
			dialog.dismiss();
		}
		listener.createUI();
	}

}
