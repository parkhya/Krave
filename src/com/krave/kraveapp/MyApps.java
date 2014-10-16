package com.krave.kraveapp;

import java.util.HashMap;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

@ReportsCrashes(formKey = "", mailTo = "support@kraveapp.net")
public class MyApps extends Application {

	public static boolean APPVISIBLEORNOT;
	int ONCHAT;

	// Handler mHandler = new Handler();
	// XMPPConnection connection;
	// KraveDAO databaseHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		ACRA.init(this);

		/* tapstream code */

		// APPVISIBLEORNOT=false;
		// Ubertesters.initialize(this);
	}

	public static boolean isActivityVisible() {
		return APPVISIBLEORNOT;
	}

	public static void activityResumed() {
		APPVISIBLEORNOT = true;
	}

	public static void activityPaused() {
		APPVISIBLEORNOT = false;
	}

}
