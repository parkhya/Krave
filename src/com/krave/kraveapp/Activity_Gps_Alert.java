package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.navdrawer.SimpleSideDrawer;
import com.ps.adapters.UserListAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.SettingDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class Activity_Gps_Alert extends Activity implements OnClickListener {
	private static Activity_Gps_Alert activityObject = null;

	public static Activity_Gps_Alert getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_Gps_Alert activityObject) {
		Activity_Gps_Alert.activityObject = activityObject;
	}

	private Button cancel, ok;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_gps_alert);

		activityObject = this;
		setLayout();
		setListeners();
		setTypeFace();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
	}

	private void setTypeFace() {
		TextView textView1, textView2;
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);

		Typeface typeface = FontStyle.getFont(Activity_Gps_Alert.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		textView1.setTypeface(typeface);

		textView2.setTypeface(typeface);

		cancel.setTypeface(typeface);
		ok.setTypeface(typeface);

	}

	private void setLayout() {

		cancel = (Button) findViewById(R.id.cancle);
		ok = (Button) findViewById(R.id.ok);

	}

	private void setListeners() {
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			// sendBroadcast(getApplicationContext());
			finish();
			// Intent intent = new Intent(Activity_Gps_Alert.this,
			// GpsService.class);
			// stopService(intent);
			break;
		case R.id.ok:
			Intent intent1 = new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS);

			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent1);
			// sendBroadcast(getApplicationContext());
			finish();
			// Intent intent2 = new Intent(Activity_Gps_Alert.this,
			// GpsService.class);
			// stopService(intent2);
			break;

		default:
			break;
		}

	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == 1 && requestCode == 1) {
//			finish();
//
//			if (ChatService.ONCHAT == AppConstants.FRAGMENT_HOME) {
//				Log.d("", "onActivityResult=AppConstants.FRAGMENT_HOME");
//				sendBroadcast(getApplicationContext());
//				Intent intent2 = new Intent(Activity_Gps_Alert.this,
//						GpsService.class);
//				stopService(intent2);
//				Intent i = new Intent(getBaseContext(), Activity_Home.class);
//				i.putExtra("open", AppConstants.FRAGMENT_HOME);
//				startActivity(i);
//			}
//		}
//	}

	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (requestCode == 1 && resultCode == 0) {
	// String provider = Settings.Secure.getString(getContentResolver(),
	// Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	// if (provider != null) {
	// Log.v("", " Location providers: " + provider);
	// finish();
	//
	// Log.d("", "onActivityResult=AppConstants.FRAGMENT_HOME");
	// sendBroadcast(getApplicationContext());
	// Intent intent2 = new Intent(Activity_Gps_Alert.this,
	// GpsService.class);
	// stopService(intent2);
	// Intent i = new Intent(getBaseContext(), Activity_Home.class);
	// i.putExtra("open", AppConstants.FRAGMENT_HOME);
	// startActivity(i);
	//
	// } else {
	// // Users did not switch on the GPS
	// }
	// }
	// }

	public void sendBroadcast(Context context) {

		Intent broadcast = new Intent();

		broadcast.setAction("close");
		context.sendBroadcast(broadcast);
	}
}
