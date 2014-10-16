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

public class Activity_Block_Dude extends Activity implements OnClickListener {
	private static Activity_Block_Dude activityObject = null;

	public static Activity_Block_Dude getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_Block_Dude activityObject) {
		Activity_Block_Dude.activityObject = activityObject;
	}

	/* resources */
	private SessionManager sessionManager;
	private ImageLoader imageLoader;

	/* Layout Views */
	private ImageView dudeProfile, dudeProfileStamp;

	private TextView dudeName;

	private Button blockButton, reportButton, cancleButton;

	/* dto variables initialization */

	private UserDTO userDTO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.activity_block_dude);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		// LinearLayout layout = new LinearLayout(context);
		// layout.setOrientation(LinearLayout.VERTICAL);
		// //layout.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		// iv = new ImageView(context);
		//
		// iv.setImageResource(resourceIdOfImage);
		// layout.addView(iv, params);
		// addContentView(layout, params);

		View view = getLayoutInflater().inflate(R.layout.activity_block_dude,
				null);

		setContentView(view, params);

		sessionManager = new SessionManager(Activity_Block_Dude.this);
		imageLoader = new ImageLoader(Activity_Block_Dude.this);
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

		Typeface typeface = FontStyle.getFont(Activity_Block_Dude.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		dudeName.setTypeface(typeface);

	}

	private void loadData() {
		userDTO = FragmentChatMatches.userDTO;
		// if (userDTO.getUserProfileImageDTOs().size() != 0
		// || userDTO.getUserProfileImageDTOs() != null) {
		// imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
		// .getImagePath(), dudeProfile);
		//
		// }

		if (userDTO.getUserProfileImageDTOs().size() > 0) {
			if (userDTO.getUserProfileImageDTOs().get(0).getImageId()
					.equals(AppConstants.FACEBOOK_IMAGE)) {
				dudeProfile.setScaleType(ScaleType.FIT_CENTER);
				imageLoader
						.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
								.getImagePath(), dudeProfile);
			} else {
				dudeProfile.setScaleType(ScaleType.FIT_CENTER);
				if (!userDTO.getUserProfileImageDTOs().get(0)
						.getIsImageActive()) {
					dudeProfile
							.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
					dudeProfileStamp.setVisibility(View.VISIBLE);
				} else {
					imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs()
							.get(0).getImagePath(), dudeProfile);
				}
			}

		} else {

			dudeProfileStamp
					.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);

			// viewPagerStamp.setVisibility(View.VISIBLE);
		}

		dudeName.setText(userDTO.getFirstName() + " " + userDTO.getLastName());
	}

	private void setLayout() {

		dudeProfile = (ImageView) findViewById(R.id.dudeProfileImage);
		dudeProfileStamp = (ImageView) findViewById(R.id.dudeProfileImageStamp);
		dudeName = (TextView) findViewById(R.id.dudeName);
		blockButton = (Button) findViewById(R.id.block);
		reportButton = (Button) findViewById(R.id.report);
		cancleButton = (Button) findViewById(R.id.cancle);

	}

	private void setListeners() {
		blockButton.setOnClickListener(this);
		reportButton.setOnClickListener(this);
		cancleButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			finish();
			break;
		case R.id.block:
			if (WebServiceConstants.isOnline(Activity_Block_Dude.this)) {
				new BlockDudeAsynchTask().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.BLOCK_DUDE);
			}
			break;
		case R.id.report:
			Intent intent = new Intent(Activity_Block_Dude.this,
					Activity_Report_Dude.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			setResult(RESULT_OK);
			finish();
		}
	}

	class BlockDudeAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		// http://parkhya.org/Android/krave_app/index.php?action=blockuser&user_id=8&friend_id=12

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(Activity_Block_Dude.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));

			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("friend_id", userDTO.getUserID()));
			params.add(new BasicNameValuePair("msg", "block"));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					Toast.makeText(Activity_Block_Dude.this,
							"Dude successfully blocked", Toast.LENGTH_SHORT)
							.show();
					setResult(RESULT_OK);
					finish();
				} else if (vStatus.equals("failure")) {
					Toast.makeText(Activity_Block_Dude.this, "Failure",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
