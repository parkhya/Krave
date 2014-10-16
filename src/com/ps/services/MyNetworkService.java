/*package com.ps.rememberme;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;
import com.ps.services.SessionManager;

public class MyNetworkService extends Service {
	private SessionManager sessionManager;
	private Intent widgetIntent;
	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
//				Toast.makeText(context,
//						ConnectivityManager.CONNECTIVITY_ACTION,
//						Toast.LENGTH_SHORT).show();
				boolean noConnectivity = intent.getBooleanExtra(
						ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				String reason = intent
						.getStringExtra(ConnectivityManager.EXTRA_REASON);
				boolean isFailover = intent.getBooleanExtra(
						ConnectivityManager.EXTRA_IS_FAILOVER, false);
				NetworkInfo currentNetworkInfo = (NetworkInfo) intent
						.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				NetworkInfo otherNetworkInfo = (NetworkInfo) intent
						.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
				if (currentNetworkInfo.isConnected()) {
//					Toast.makeText(getApplicationContext(), "Connected",
//							Toast.LENGTH_LONG).show();
					try {
						sessionManager.changeNetwork(true);
						widgetIntent = new Intent(context,
								MyWidgetProvider.class);
						widgetIntent
								.setAction("android.appwidget.action.APPWIDGET_UPDATE");
						int ids[] = AppWidgetManager.getInstance(
								getApplication()).getAppWidgetIds(
								new ComponentName(getApplication(),
										MyWidgetProvider.class));
						widgetIntent.putExtra(
								AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
						sendBroadcast(widgetIntent);
					} catch (Exception exception) {

					}
				} else {
//					Toast.makeText(getApplicationContext(), "Not Connected",
//							Toast.LENGTH_LONG).show();
					try {
						sessionManager.changeNetwork(false);
						widgetIntent = new Intent(context,
								MyWidgetProvider.class);
						widgetIntent
								.setAction("android.appwidget.action.APPWIDGET_UPDATE");
						int ids[] = AppWidgetManager.getInstance(
								getApplication()).getAppWidgetIds(
								new ComponentName(getApplication(),
										MyWidgetProvider.class));
						widgetIntent.putExtra(
								AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
						sendBroadcast(widgetIntent);
					} catch (Exception exception) {

					}
				}
			} else if (intent.getAction() == Intent.ACTION_CONFIGURATION_CHANGED) {
//				Toast.makeText(context, Intent.ACTION_CONFIGURATION_CHANGED,
//						Toast.LENGTH_SHORT).show();
				if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//					Toast.makeText(context, "LANDSCAPE", Toast.LENGTH_SHORT)
//							.show();
					sessionManager.changeOrientation(true);
				} else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					sessionManager.changeOrientation(true);
//					Toast.makeText(context, "PORTRAIT", Toast.LENGTH_SHORT)
//							.show();
				}
				try {
					widgetIntent = new Intent(context, MyWidgetProvider.class);
					widgetIntent
							.setAction("android.appwidget.action.APPWIDGET_UPDATE");
					int ids[] = AppWidgetManager.getInstance(getApplication())
							.getAppWidgetIds(
									new ComponentName(getApplication(),
											MyWidgetProvider.class));
					widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
							ids);
					sendBroadcast(widgetIntent);
				} catch (Exception exception) {

				}
			}
		}
	};

	@Override
	public void onCreate() {
		sessionManager = new SessionManager(this);
		// get an instance of the receiver in your service
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(this.mConnReceiver, intentFilter);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}*/