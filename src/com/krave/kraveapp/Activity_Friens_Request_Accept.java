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
import com.ps.adapters.SearchByAdapter;
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

public class Activity_Friens_Request_Accept extends Activity implements
		OnClickListener {
	private static Activity_Friens_Request_Accept activityObject = null;

	public static Activity_Friens_Request_Accept getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(
			Activity_Friens_Request_Accept activityObject) {
		Activity_Friens_Request_Accept.activityObject = activityObject;
	}

	/* resources */
	private SessionManager sessionUserDTO;
	private ImageLoader imageLoader;

	/* Layout Views */
	private ImageView userProfileImage, dudeProfileImage,
			userProfileImageStamp, dudeProfileImageStamp;

	private Button cancel, ok;
	private SessionManager sessionManager;

	/* dto variables initialization */

	public static UserDTO userDTO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.dialog_push_notification_friend_request2);
		activityObject = this;
		sessionManager = new SessionManager(Activity_Friens_Request_Accept.this);
		imageLoader = new ImageLoader(Activity_Friens_Request_Accept.this);

		setLayout();
		setListeners();
		loadData();
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

		Typeface typeface = FontStyle.getFont(
				Activity_Friens_Request_Accept.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		textView1.setTypeface(typeface);

		textView2.setTypeface(typeface);

		cancel.setTypeface(typeface);
		ok.setTypeface(typeface);

	}

	private void loadData() {
		// imageLoader.DisplayImage(sessionManager.getUserDetail()
		// .getUserProfileImageDTOs().get(0).getImagePath(),
		// userProfileImage);
		// Log.d("",
		// "userProfileImage="
		// + sessionManager.getUserDetail()
		// .getUserProfileImageDTOs().get(0)
		// .getImagePath());
		//
		// Log.d("", "dudeProfileImage="
		// + userDTO.getUserProfileImageDTOs().get(0).getImagePath());
		// imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
		// .getImagePath(), dudeProfileImage);

		try {

			UserDTO sessionUserDTO = sessionManager.getUserDetail();

			if (sessionUserDTO.getUserProfileImageDTOs().size() > 0) {

				if (sessionUserDTO.getUserProfileImageDTOs().get(0)
						.getIsImageActive()) {
					if (sessionUserDTO.getUserProfileImageDTOs().get(0)
							.getImageId().equals(AppConstants.FACEBOOK_IMAGE)) {
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

		cancel = (Button) findViewById(R.id.keepSearchingButton);
		ok = (Button) findViewById(R.id.sendmsgbtn);
		userProfileImage = (ImageView) findViewById(R.id.userImageViewcongratulation);
		dudeProfileImage = (ImageView) findViewById(R.id.dudeImageView);
		userProfileImageStamp = (ImageView) findViewById(R.id.userImageViewcongratulationStamp);
		dudeProfileImageStamp = (ImageView) findViewById(R.id.dudeImageViewStamp);
	}

	private void setListeners() {
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentHome.Refresh_Data = true;
				setResult(AppConstants.FRIEND_NOTIFICATION_RESULT1);
				finish();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentChatMatches.userDTO = userDTO;
				setResult(AppConstants.FRIEND_NOTIFICATION_RESULT2);
				FragmentHome.Refresh_Data = true;
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cancle:

			break;
		case R.id.ok:

			break;

		default:
			break;
		}

	}

}
