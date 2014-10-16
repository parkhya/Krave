package com.ps.services;

import com.ps.utill.SessionManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GpsServiceBrodcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SessionManager sessionManager = new SessionManager(context);
		Log.d("gps service receiver", "Login= " + sessionManager.isLogin());

		if (sessionManager.isLogin()) {
			Intent serviceIntent = new Intent(context, GpsService.class);
			context.startService(serviceIntent);

			Intent chatService = new Intent(context, ChatService.class);
			context.startService(chatService);

			Log.d("gps service receiver", "under on receive");
		}

	}

}
