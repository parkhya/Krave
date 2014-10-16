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
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class Activity_Push_Notification extends Activity implements
		OnClickListener {
	private static Activity_Push_Notification activityObject = null;

	public static Activity_Push_Notification getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(
			Activity_Push_Notification activityObject) {
		Activity_Push_Notification.activityObject = activityObject;
	}

	/* resources */
	private SessionManager sessionManager;
	private ImageLoader imageLoader;

	/* Layout Views */
	private ImageView userProfileImage, dudeProfileImage,
			userProfileImageStamp, dudeProfileImageStamp;

	private Button cancel, ok;

	/* dto variables initnlialization */

	public static UserDTO userDTO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_push_notification_friend_request);

		sessionManager = new SessionManager(Activity_Push_Notification.this);
		imageLoader = new ImageLoader(Activity_Push_Notification.this);
		activityObject = this;
		setLayout();
		setListeners();
		setTypeFace();
		loadData();

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

		Typeface typeface = FontStyle.getFont(Activity_Push_Notification.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		textView1.setTypeface(typeface);

		textView2.setTypeface(typeface);

		cancel.setTypeface(typeface);
		ok.setTypeface(typeface);

	}

	private void loadData() {
		try {

			UserDTO sessionUserDTO = sessionManager.getUserDetail();

			// imageLoader.DisplayImage(sessionManager.getUserDetail()
			// .getUserProfileImageDTOs().get(0).getImagePath(),
			// userProfileImage);
			// Log.d("", "userProfileImage="
			// + sessionManager.getUserDetail().getUserProfileImageDTOs()
			// .get(0).getImagePath());
			//
			// Log.d("", "dudeProfileImage="
			// + userDTO.getUserProfileImageDTOs().get(0).getImagePath());
			// imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
			// .getImagePath(), dudeProfileImage);

			if (sessionUserDTO.getUserProfileImageDTOs().size() > 0) {

				if (sessionUserDTO.getUserProfileImageDTOs().get(0)
						.getIsImageActive()) {
					if (sessionUserDTO.getUserProfileImageDTOs().get(0).getImageId()
							.equals(AppConstants.FACEBOOK_IMAGE)) {
						userProfileImage.setScaleType(ScaleType.FIT_CENTER);
					}
					imageLoader.DisplayImage(sessionUserDTO
							.getUserProfileImageDTOs().get(0).getImagePath(),
							userProfileImage);
				} else {
					userProfileImageStamp.setVisibility(View.VISIBLE);
				}
			}

			if (userDTO.getUserProfileImageDTOs().size() > 0) {

				if (userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
					
					if (userDTO.getUserProfileImageDTOs().get(0).getImageId()
							.equals(AppConstants.FACEBOOK_IMAGE)) {
						dudeProfileImage.setScaleType(ScaleType.FIT_CENTER);
					}
					imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs()
							.get(0).getImagePath(), dudeProfileImage);
				} else {
					dudeProfileImageStamp.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {

		}
	}

	private void setLayout() {

		cancel = (Button) findViewById(R.id.cancle);
		ok = (Button) findViewById(R.id.ok);
		userProfileImage = (ImageView) findViewById(R.id.userProfileImage);
		dudeProfileImage = (ImageView) findViewById(R.id.dudeProfileImage);
		userProfileImageStamp = (ImageView) findViewById(R.id.userProfileImageStamp);
		dudeProfileImageStamp = (ImageView) findViewById(R.id.dudeProfileImageStamp);
	}

	private void setListeners() {
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			FragmentHome.Refresh_Data = true;
			finish();
			break;
		case R.id.ok:
			FragmentChatMatches.userDTO = userDTO;
			setResult(RESULT_OK);
			FragmentHome.Refresh_Data = true;
			finish();
			break;

		default:
			break;
		}

	}

}
