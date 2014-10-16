package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.model.GraphUser;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gcm.GCMRegistrar;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.SettingDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.FacebookIntegration;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveDAO;
import com.ps.utill.OnfacebookDone;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class Activity_Login extends Activity implements OnClickListener {
	private String gcmRegId;
	private ImageButton loginButton, facebookButton;
	private TextView signUp, forgotPassword;
	private EditText userName, password;
	private SessionManager sessionManager;
	private JSONParser jsonParser = new JSONParser();
	private GraphUser graphUser;
	FacebookIntegration facebookIntegration;
	private ImageView rememberMe;
	private TextView rememberMeTextView;
	private boolean isRememberMe = false;
	private GPSTracker gpsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);

		sessionManager = new SessionManager(Activity_Login.this);
		gpsTracker = new GPSTracker(Activity_Login.this);
		setLayout();
		setListener();
		setTypeFace();
		isRememberMe = sessionManager.isRemember();
		if (isRememberMe) {
			userName.setText(sessionManager.getEmail());
			password.setText(sessionManager.getPassword());
			rememberMe.setImageResource(R.drawable.remember_checked);
		} else {
			rememberMe.setImageResource(R.drawable.remember_un_checked);
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// EasyTracker.getInstance(this).activityStart(this);
		EasyTracker.getInstance(this).set(Fields.SCREEN_NAME, "Login Screen");
		EasyTracker.getInstance(this).send(MapBuilder.createAppView().build());
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	private void setListener() {
		loginButton.setOnClickListener(this);
		facebookButton.setOnClickListener(this);
		signUp.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		rememberMe.setOnClickListener(this);

	}

	private void setLayout() {

		userName = (EditText) findViewById(R.id.userNameEditText);
		password = (EditText) findViewById(R.id.passwordEditText);
		loginButton = (ImageButton) findViewById(R.id.signInButton);
		facebookButton = (ImageButton) findViewById(R.id.connectWithFbButton);
		signUp = (TextView) findViewById(R.id.registration);
		forgotPassword = (TextView) findViewById(R.id.forgotPassword);
		rememberMe = (ImageView) findViewById(R.id.rememberMe);
		rememberMeTextView = (TextView) findViewById(R.id.rememberMeTextView);
		// userName.setText("sahu.romesh670@gmail.com");
		// password.setText("123456");

	}

	private void setTypeFace() {

		Typeface typeface = FontStyle.getFont(Activity_Login.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		userName.setTypeface(typeface);
		password.setTypeface(typeface);
		Typeface typeface1 = FontStyle.getFont(Activity_Login.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		signUp.setTypeface(typeface1);
		forgotPassword.setTypeface(typeface1);
		rememberMeTextView.setTypeface(typeface1);

	}

	private void registerClient() {
		GCMRegistrar.checkDevice(Activity_Login.this);
		GCMRegistrar.checkManifest(Activity_Login.this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				CommonUtilities.DISPLAY_MESSAGE_ACTION));

		gcmRegId = GCMRegistrar.getRegistrationId(Activity_Login.this);
		Log.v("REG ID", gcmRegId);

		if (gcmRegId.equals("")) {
			GCMRegistrar.register(Activity_Login.this,
					CommonUtilities.SENDER_ID);
			Log.v("REG ID IN IF", gcmRegId);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(Activity_Login.this)) {
				// regId = GCMRegistrar.getRegistrationId(this);
				System.out.println("GCM Register on server" + gcmRegId);
			} else {
				GCMRegistrar.setRegisteredOnServer(Activity_Login.this, true);
			}
		}

		// GpsService.gcmId = gcmRegId;
		Log.v("", "Already registered:  " + gcmRegId);
		// if (gcmRegId.length() != 0)
		//
		// //new GCMAsynchTask().execute(Webservice.GcmService);

	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					CommonUtilities.EXTRA_MESSAGE);

		}

	};

	// registration,login,map,profile,daily krave,setting,right menu
	//
	// Home:- 1)only online dudes underworking
	// 2)friends in common underworking
	// 3)distance away under working
	// 4)shared interest under working
	// 5) congratulation (you have match) send him a message button under
	// working
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {

			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
	}

	@Override
	public void onClick(View arg0) {
		if (WebServiceConstants.isOnline(Activity_Login.this)) {

			switch (arg0.getId()) {
			case R.id.signInButton:
				if (0 == userName.getText().toString().trim().length()) {
					Toast.makeText(Activity_Login.this, "Enter email id",
							Toast.LENGTH_SHORT).show();
				} else if (0 == password.getText().toString().trim().length()) {
					Toast.makeText(Activity_Login.this, "Enter password",
							Toast.LENGTH_SHORT).show();
				} else {
					registerClient();

					new LoginAsynchTask().execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.USER_LOGIN);

				}

				break;
			case R.id.connectWithFbButton:
				facebookIntegration = new FacebookIntegration(
						Activity_Login.this, AppConstants.COME_FROM_LOGIN,
						new OnfacebookDone() {

							@Override
							public void onFbcompleate() {
								// TODO Auto-generated method stub

							}
						});
				facebookIntegration.facebookIntegration();
				// graphUser = facebookIntegration.getGraphUser();

				break;
			case R.id.registration:

				Intent intent2 = new Intent(Activity_Login.this,
						Activity_ProfileDetails.class);
				intent2.putExtra(AppConstants.COME_FROM,
						AppConstants.COME_FROM_NORMAL_REGISTRATION);
				startActivity(intent2);
				break;
			case R.id.forgotPassword:
				Intent intent3 = new Intent(Activity_Login.this,
						Activity_ForgotPassword.class);
				startActivity(intent3);
				break;

			case R.id.rememberMe:
				if (isRememberMe) {
					rememberMe.setImageResource(R.drawable.remember_un_checked);
					isRememberMe = false;
				} else {
					rememberMe.setImageResource(R.drawable.remember_checked);
					isRememberMe = true;
				}
				break;
			default:
				break;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			facebookIntegration.onActivityResultForFacebook(requestCode,
					resultCode, data);
		} catch (Exception e) {

		}
	}

	class LoginAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_Login.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// //
			// devicetype
			// GCMID
			// http://parkhya.org/Android/krave_app/index.php?action=user_login&email=test2@gmail.com&password=123456
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));

			params.add(new BasicNameValuePair("lat", ""
					+ gpsTracker.getLatitude()));
			params.add(new BasicNameValuePair("log", ""
					+ gpsTracker.getLongitude()));
			params.add(new BasicNameValuePair("email", userName.getText()
					.toString()));
			params.add(new BasicNameValuePair("password", password.getText()
					.toString()));
			params.add(new BasicNameValuePair("GCMID", gcmRegId));
			params.add(new BasicNameValuePair("devicetype", "android"));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("login response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("login response :" + jsonArray);
				if (vStatus.equals("success")) {

					parseUserDataAndSetInSession(mJsonObject);
				} else if (vStatus.equals("unverified")) {
					Toast.makeText(
							Activity_Login.this,
							"Your email id is not verified ,please verify email id ",
							Toast.LENGTH_SHORT).show();
				} else if (vStatus.equals("failure")) {
					Toast.makeText(Activity_Login.this,
							"Email and/or password is not correct",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	// [{"status":"success",
	// "user":{"user_id":"48","user_email":"ankit@gmail.com","user_password":"202cb962ac59075b964b07152d234b70",
	// "user_fname":"ankit","user_lname":"ankit","user_image":null,"user_mobile":"908242000","user_address":"",
	// "user_dob":"0000-00-00","user_feet":"1","user_inches":"","user_meters":"","user_age":"1","user_weight":"1",
	// "user_height":"","user_note":"","user_relationshipStatus":"1","user_whatAreYou":"","user_whatDoYouKrave":"",
	// "isFirstTime":"0","user_type":"","user_status":"1","user_reg_date":"2014-05-22 11:58:16"},
	// "intrest":[{"ui_id":"40","intrest_id":"1","user_id":"48","ui_type":"admin"},
	// {"ui_id":"41","intrest_id":"2","user_id":"48","ui_type":"admin"},
	// {"ui_id":"42","intrest_id":"3","user_id":"48","ui_type":"admin"},
	// {"ui_id":"43","intrest_id":"4","user_id":"48","ui_type":"admin"},
	// {"ui_id":"44","intrest_id":"5","user_id":"48","ui_type":"admin"},
	// {"ui_id":"45","intrest_id":"6","user_id":"48","ui_type":"admin"},
	// {"ui_id":"46","intrest_id":"7","user_id":"48","ui_type":"admin"},
	// {"ui_id":"47","intrest_id":"8","user_id":"48","ui_type":"admin"}],
	// "images":[{"image_id":"33","User_id":"48","image_name":null,"image_path":"krave_image/14007400960_test.png"},
	// {"image_id":"34","User_id":"48","image_name":null,"image_path":"krave_image/14007400961_test.png"},
	// {"image_id":"35","User_id":"48","image_name":null,"image_path":"krave_image/14007400962_test.png"},
	// {"image_id":"36","User_id":"48","image_name":null,"image_path":"krave_image/14007400963_test.png"},
	// {"image_id":"37","User_id":"48","image_name":null,"image_path":"krave_image/14007400964_test.png"},
	// {"image_id":"38","User_id":"48","image_name":null,"image_path":"krave_image/14007400965_test.png"}]}]

	private void parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		SettingDTO settingDTO = new SettingDTO();

		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		// System.out.println(MainObject);
		userDTO.setLanguage(MainObject.getString("user_language"));
		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));

		settingDTO.setIsNotificationEnable(MainObject
				.getString("notification_status"));

		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}
		whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
		whatAreYouDTO.setInches(MainObject.getString("user_inches"));
		whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
		whatAreYouDTO.setHight(MainObject.getString("user_height"));
		whatAreYouDTO.setAge(MainObject.getString("user_age"));
		whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
		whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
		whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));
		// whatAreYouDTO.setAge(MainObject.getString("user_note"));
		whatAreYouDTO.setRelationshipStatus(MainObject
				.getString("user_relationshipStatus"));
		whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
		whatAreYouDTO.setWhatAreYouName(MainObject
				.getString("user_whatAreYou_name"));
		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrest_id"));
			interestsDTOs.add(interestsDTO);

		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

		}
		for (int i = 0; i < jsonArrayImages.length(); i++) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));
			userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
					+ imagesJsonObject.getString("image_path"));
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));
			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}
		/* set facebook image at first position in list */

		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}
		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		sessionManager.setUserDetail(userDTO);
		sessionManager.setSettingDetail(settingDTO);
		Intent mIntent = new Intent(Activity_Login.this, Activity_Home.class);
		mIntent.putExtra("open", AppConstants.FRAGMENT_HOME);
		startActivity(mIntent);

		if (isRememberMe) {
			sessionManager.setRememberMe(userName.getText().toString(),
					password.getText().toString());
		} else {
			sessionManager.clearRememberMe();
		}
		finish();
		new GetChatHistory().execute(WebServiceConstants.BASE_URL
				+ WebServiceConstants.GET_HISTORY);
	}

	class GetChatHistory extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("", "GET CHAT HISTORY");
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			KraveDAO databaseHelper = new KraveDAO(Activity_Login.this);
			try {
				Log.d("", "GET CHAT HISTORY=" + jsonArray.length());
				Log.d("", "chat history=" + jsonArray);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject mJsonObject = jsonArray.getJSONObject(i);
					ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
					chatDetailsDTO.setFromuser(mJsonObject
							.getString("from_user"));
					chatDetailsDTO.setTouser(mJsonObject.getString("to_user"));
					chatDetailsDTO.setMessage(mJsonObject.getString("message"));
					chatDetailsDTO.setMeassageType(mJsonObject
							.getString("msg_type"));
					chatDetailsDTO.setTime(mJsonObject.getString("time"));
					databaseHelper.addChat(chatDetailsDTO);
				}

			} catch (JSONException e) {

			}

		}
	}
}