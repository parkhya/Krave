package com.krave.kraveapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gcm.GCMRegistrar;
import com.navdrawer.SimpleSideDrawer;
import com.ps.adapters.ChatMatchesAdapter;
import com.ps.adapters.RequistListAdapter;
import com.ps.adapters.SearchByAdapter;
import com.ps.adapters.UserListAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveDAO;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class Activity_Home extends Activity implements OnClickListener {
	private int fragmentIds = 0;
	private String gcmRegId = "";
	public int COME_FROM = 0;
	public int setLeftDrawer = 0;
	public boolean isOpenCongratulationLayout = false;
	public static int index;
	public static List<UserDTO> dudeCommonList;// = new ArrayList<UserDTO>();
	public ImageButton left_button, right_button, back_button;
	private ImageView notificationHeader, notificationRequiest;
	private TextView notificationHeaderTextView, notificationRequiestTextView;
	private SessionManager sessionManager;
	public TextView title;
	public ImageView headerIcon;
	public static SimpleSideDrawer slide_me;
	private static FragmentManager fragmentManager;
	// private MyApps xmppClient = new MyApps();
	// private LinearLayout layoutHome;
	private LinearLayout layoutProfile;
	public LinearLayout layoutFindDudes;
	// private LinearLayout layoutGetMatches;
	private LinearLayout layoutDailyKrave;
	private LinearLayout layoutChatMatches;
	private LinearLayout layoutSetting;
	private LinearLayout layoutContactUs;
	// private LinearLayout layoutMap;
	private LinearLayout layoutLogout;
	// private boolean LoadHomeFlag = false;
	int selectedItem = 1;
	int previousItem = 1;
	static Fragment tempFragment = null;
	ChatMatchesAdapter chatMatchesAdapter;
	// static FragmentHome fragmentHome;
	// private Presence presence;
	// public XMPPConnection connection;
	// static Fragment fragmentProfile;
	// static Fragment fragmentFindDudes;
	// static Fragment fragmentGetMatches;
	// static Fragment fragmentDailyKrave;
	// static Fragment fragmentChatMatches;
	// static Fragment fragmentSetting;
	// static Fragment fragmentMap;

	// private final int PROFILE = 2;
	// private final int FIND_DUDES = 3;
	// private final int GET_MATCHES = 4;

	// private final int SETTING = 7;

	private ListView dudeListView;
	private EditText searchDude;
	public LinearLayout mainview;

	/* right menu variable */
	public List<UserDTO> searchDudeList = new ArrayList<UserDTO>();
	public List<UserDTO> requestDudesList = new ArrayList<UserDTO>();
	List<UserListDTO> userListDTOs = new ArrayList<UserListDTO>();

	UserListAdapter userListAdapter;
	SearchByAdapter subListAdapter;
	RequistListAdapter requistListAdapter;
	ImageLoader imageLoader;
	BroadcastReceiver broadcastReceiver, closeActivtiyBroadcastReceiver;
	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		imageLoader = new ImageLoader(Activity_Home.this);
		mainview = (LinearLayout) findViewById(R.id.mainview);
		ChatService.APPACTIVITYSTATE = 0;
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		sessionManager = new SessionManager(Activity_Home.this);
		fragmentManager = getFragmentManager();

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.left_menu);
		slide_me.setRightBehindContentView(R.layout.right_menu);

		setLayout();
		setTypeFace();
		setListeners();
		registerClient();
		startUserCurrentLocationService();

		if (FragmentHome.userDTOs != null) {
			FragmentHome.setBackButtonFunction();
		}

		int intentValue = getIntent().getExtras().getInt("open",
				AppConstants.FRAGMENT_HOME);
		if (intentValue == AppConstants.FRAGMENT_HOME) {

			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			// if (!GCMIntentService.userId.equals("-1")) {
			// new GetDudeById().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.GET_DUDE_BY_ID,
			// GCMIntentService.userId);
			// }
		} else {

			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
		}
		broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// Toast.makeText(context, "SMS SENT!!", Toast.LENGTH_SHORT)
				// .show();
				if ("chat".equals(intent.getExtras().get("come"))) {
					Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
				} else if ("push_notification".equals(intent.getExtras().get(
						"come"))) {
					String userId = intent.getExtras().getString("userId");
					new GetDudeById().execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.GET_DUDE_BY_ID, userId);
				} else {
					setNotification();
				}
			}
		};
		IntentFilter filterSend = new IntentFilter();
		filterSend.addAction(BROADCAST_ACTION);
		registerReceiver(broadcastReceiver, filterSend);

		closeActivtiyBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};

		IntentFilter closeActivity = new IntentFilter();
		closeActivity.addAction("close");
		registerReceiver(closeActivtiyBroadcastReceiver, closeActivity);

		setNotification();

		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
		// "com.krave.kraveapp", PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md;
		// md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// String something = new String(Base64.encode(md.digest(), 0));
		// // String something = new
		// // String(Base64.encodeBytes(md.digest()));
		// Log.e("hash key my ....", something);
		// }
		// } catch (PackageManager.NameNotFoundException e1) {
		// Log.e("name not found", e1.toString());
		// } catch (NoSuchAlgorithmException e) {
		// Log.e("no such an algorithm", e.toString());
		// } catch (Exception e) {
		// Log.e("exception", e.toString());
		// }

	}

	private void startUserCurrentLocationService() {
		Intent intent = new Intent(Activity_Home.this, GpsService.class);
		startService(intent);
		Intent intent1 = new Intent(Activity_Home.this, ChatService.class);
		startService(intent1);

	}

	private void registerClient() {
		GCMRegistrar.checkDevice(Activity_Home.this);
		GCMRegistrar.checkManifest(Activity_Home.this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				CommonUtilities.DISPLAY_MESSAGE_ACTION));

		gcmRegId = GCMRegistrar.getRegistrationId(Activity_Home.this);
		Log.v("REG ID", gcmRegId);

		if (gcmRegId.equals("")) {
			GCMRegistrar
					.register(Activity_Home.this, CommonUtilities.SENDER_ID);
			Log.v("REG ID IN IF", gcmRegId);
		} else {
			if (GCMRegistrar.isRegisteredOnServer(Activity_Home.this)) {
				// regId = GCMRegistrar.getRegistrationId(this);
				System.out.println("GCM Register on server" + gcmRegId);
			} else {
				GCMRegistrar.setRegisteredOnServer(Activity_Home.this, true);
			}
		}

		GpsService.gcmId = gcmRegId;
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

	@Override
	protected void onResume() {

		super.onResume();
		// MyApps.activityResumed();

		ChatService.APPVISIBLEORNOT = true;
		if (ChatService.isApplicationBaground) {
			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			ChatService.APPACTIVITYSTATE = 0;
			ChatService.isApplicationBaground = false;
		}
		if (!GCMIntentService.userId.equals("-1")) {
			new GetDudeById().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_DUDE_BY_ID,
					GCMIntentService.userId);
		}
		// if (isRightMenuList) {
		// new GetUserList().execute(WebServiceConstants.BASE_URL
		// + WebServiceConstants.GET_USER_LIST);
		// isRightMenuList = false;
		// }

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ChatService.APPVISIBLEORNOT = false;
		ChatService.APPACTIVITYSTATE = 1;
		// MyApps.activityPaused();
		// unregisterReceiver(broadcastReceiver);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// EasyTracker.getInstance(this).activityStart(this);
		EasyTracker.getInstance(this).set(Fields.SCREEN_NAME, "Home Screen");
		EasyTracker.getInstance(this).send(MapBuilder.createAppView().build());
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ChatService.APPACTIVITYSTATE = 0;
		super.onDestroy();
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {

			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
	}

	private void stopUserCurrentLocationService() {
		Intent intent = new Intent(Activity_Home.this, GpsService.class);
		stopService(intent);
		Intent intent1 = new Intent(Activity_Home.this, ChatService.class);
		stopService(intent1);

	}

	private void setListeners() {
		left_button.setOnClickListener(this);
		right_button.setOnClickListener(this);
		// back_button.setOnClickListener(this);
		// layoutHome.setOnClickListener(this);
		layoutProfile.setOnClickListener(this);
		layoutFindDudes.setOnClickListener(this);
		// layoutGetMatches.setOnClickListener(this);
		layoutDailyKrave.setOnClickListener(this);
		layoutChatMatches.setOnClickListener(this);
		layoutSetting.setOnClickListener(this);
		layoutContactUs.setOnClickListener(this);
		// layoutMap.setOnClickListener(this);
		layoutLogout.setOnClickListener(this);
		// dudeListView.setOnItemClickListener(this);
	}

	FrameLayout rightContainer;

	private void setLayout() {
		// View leftView = slide_me.getLeftBehindView();
		notificationHeader = (ImageView) findViewById(R.id.notofication);
		// notificationRequiest = (ImageView) findViewById(R.id.notofication);
		notificationHeaderTextView = (TextView) findViewById(R.id.notificationCount);
		// notificationRequiestTextView = (TextView)
		// findViewById(R.id.notificationRCount);
		left_button = (ImageButton) findViewById(R.id.left_buton);
		right_button = (ImageButton) findViewById(R.id.right_buton);
		back_button = (ImageButton) findViewById(R.id.back_button);
		// layoutHome = (LinearLayout) findViewById(R.id.home);
		headerIcon = (ImageView) findViewById(R.id.icon);
		title = (TextView) findViewById(R.id.titleTextView);
		layoutProfile = (LinearLayout) findViewById(R.id.profile);
		layoutFindDudes = (LinearLayout) findViewById(R.id.findDudes);
		// layoutGetMatches = (LinearLayout) findViewById(R.id.getMatches);
		layoutDailyKrave = (LinearLayout) findViewById(R.id.dailyKrave);
		layoutChatMatches = (LinearLayout) findViewById(R.id.chatMatches);
		layoutSetting = (LinearLayout) findViewById(R.id.setting);
		layoutContactUs = (LinearLayout) findViewById(R.id.contactUs);
		// layoutMap = (LinearLayout) findViewById(R.id.map);
		layoutLogout = (LinearLayout) findViewById(R.id.logout);

		rightContainer = (FrameLayout) findViewById(R.id.rightContainer);
		settingAllGuysLayout();
	}

	private void setTypeFace() {
		TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView10, textView9;
		// textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		// textView4 = (TextView) findViewById(R.id.textView4);
		textView5 = (TextView) findViewById(R.id.textView5);
		textView6 = (TextView) findViewById(R.id.textView6);
		textView7 = (TextView) findViewById(R.id.textView7);
		// textView8 = (TextView) findViewById(R.id.textView8);
		textView9 = (TextView) findViewById(R.id.textView9);
		textView10 = (TextView) findViewById(R.id.textView10);
		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		// textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		textView3.setTypeface(typeface);
		// textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView7.setTypeface(typeface);
		// textView8.setTypeface(typeface);
		textView9.setTypeface(typeface);
		textView10.setTypeface(typeface);

	}

	private void setNotification() {
		int total = GpsService.notificationCount + GpsService.unreadMsgCount;
		if (total > 0) {
			notificationHeader.setVisibility(View.VISIBLE);
			notificationHeaderTextView.setVisibility(View.VISIBLE);
			notificationHeaderTextView.setText("" + total);
			if (GpsService.notificationCount > 0) {
				notificationRequiest.setVisibility(View.VISIBLE);

				notificationRequiestTextView.setVisibility(View.VISIBLE);

				notificationRequiestTextView.setText(""
						+ GpsService.notificationCount);
			}
		} else {
			notificationHeader.setVisibility(View.INVISIBLE);
			notificationRequiest.setVisibility(View.INVISIBLE);
			notificationHeaderTextView.setVisibility(View.INVISIBLE);
			notificationRequiestTextView.setVisibility(View.INVISIBLE);
		}

		notificationHeaderTextView.setVisibility(View.GONE);
		notificationRequiestTextView.setVisibility(View.GONE);

	}

	private void settingAllGuysLayout() {
		TextView editAllGuysButton, listsButton;
		FrameLayout requistsLayout;

		LayoutInflater inflater = getLayoutInflater();
		rightContainer.removeAllViews();
		View view = inflater.inflate(R.layout.right_menu_all_guys,
				rightContainer);
		notificationRequiest = (ImageView) view
				.findViewById(R.id.requestsNotification);
		notificationRequiestTextView = (TextView) findViewById(R.id.notificationRCount);
		editAllGuysButton = (TextView) view
				.findViewById(R.id.editAllGuysButton);
		listsButton = (TextView) view.findViewById(R.id.listsButton);
		TextView allGuys = (TextView) view.findViewById(R.id.allGuys);
		TextView requestTextView = (TextView) view
				.findViewById(R.id.requestTextView);

		dudeListView = (ListView) view.findViewById(R.id.dudeList);
		requistsLayout = (FrameLayout) view.findViewById(R.id.reuistsLayout);
		searchDude = (EditText) view.findViewById(R.id.searchDude);

		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		editAllGuysButton.setTypeface(typeface);
		listsButton.setTypeface(typeface);
		allGuys.setTypeface(typeface);
		requestTextView.setTypeface(typeface);
		searchDude.setTypeface(typeface);

		searchDudeList = new ArrayList<UserDTO>();
		chatMatchesAdapter = new ChatMatchesAdapter(Activity_Home.this,
				searchDudeList, 1);
		dudeListView.setAdapter(chatMatchesAdapter);

		if (WebServiceConstants.isOnline(Activity_Home.this)) {
			new GetAllLikeDude().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_ALL_LIKE_DUDE);
		}
		searchDude.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// chatMatchesAdapter.getFilter().filter(s.toString());
				chatMatchesAdapter.getFilter().filter(
						s.toString() + "/" + AppConstants.OFFLINE);
			}
		});

		dudeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FragmentChatMatches.userDTO = (UserDTO) chatMatchesAdapter
						.getItem(arg2);
				Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
				slide_me.close();

			}
		});

		listsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setListsLayout();

			}

		});
		editAllGuysButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity_R_EditList.userDTOs = searchDudeList;
				Intent intent2 = new Intent(Activity_Home.this,
						Activity_R_EditList.class);

				intent2.putExtra(AppConstants.COME_FROM,
						AppConstants.COME_FROM_ALL_GUYS);
				startActivityForResult(intent2, AppConstants.EDIT_LIST);

			}
		});

		requistsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setRequistsLayout();
			}
		});
		setNotification();
	}

	private void setRequistsLayout() {
		ImageView backButton;
		EditText searchDudesEditText;
		LinearLayout backLayout;
		ListView requestList;

		LayoutInflater inflater = getLayoutInflater();
		rightContainer.removeAllViews();
		View view1 = inflater.inflate(R.layout.right_menu_requst,
				rightContainer);

		backButton = (ImageView) view1.findViewById(R.id.backButton);
		searchDudesEditText = (EditText) view1.findViewById(R.id.searchDude);
		requestList = (ListView) view1.findViewById(R.id.dudelist);

		backLayout = (LinearLayout) view1.findViewById(R.id.backLayout);

		TextView requestTextView = (TextView) view1
				.findViewById(R.id.requestTextView);
		TextView listTextView = (TextView) view1
				.findViewById(R.id.listTextView);

		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		requestTextView.setTypeface(typeface);
		listTextView.setTypeface(typeface);
		searchDudesEditText.setTypeface(typeface);

		requistListAdapter = new RequistListAdapter(Activity_Home.this,
				requestDudesList);
		requestList.setAdapter(requistListAdapter);
		if (WebServiceConstants.isOnline(Activity_Home.this)) {
			new GetDudeRequistsList().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_ALL_REQUEST);
		}

		searchDudesEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				requistListAdapter.getFilter().filter(s.toString());
			}
		});
		requestList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// setListsItemLayout(position);

			}
		});

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settingAllGuysLayout();

			}
		});
		backLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settingAllGuysLayout();

			}
		});

	}

	// private Roster roster;

	private void setListsLayout() {
		ImageView backFromListImageButton;
		ListView nameList;

		LinearLayout createNewListLayout, editListName;

		EditText searchNameListEditText;

		LayoutInflater inflater = getLayoutInflater();
		rightContainer.removeAllViews();
		View view1 = inflater
				.inflate(R.layout.right_menu_lists, rightContainer);

		backFromListImageButton = (ImageView) view1
				.findViewById(R.id.backFromListName1);
		editListName = (LinearLayout) view1.findViewById(R.id.editListButton);
		searchNameListEditText = (EditText) view1
				.findViewById(R.id.searchNameEditText);
		nameList = (ListView) view1.findViewById(R.id.nameList);

		createNewListLayout = (LinearLayout) view1
				.findViewById(R.id.createNewListsLayout);
		TextView listTextView = (TextView) view1
				.findViewById(R.id.listTextView);
		TextView createNewListTextView = (TextView) view1
				.findViewById(R.id.createNewListTextView);
		TextView editListNameTextView = (TextView) view1
				.findViewById(R.id.editListButtonTextView);

		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		listTextView.setTypeface(typeface);
		createNewListTextView.setTypeface(typeface);
		searchNameListEditText.setTypeface(typeface);
		editListNameTextView.setTypeface(typeface);
		userListAdapter = new UserListAdapter(userListDTOs, Activity_Home.this,
				0);
		nameList.setAdapter(userListAdapter);
		if (WebServiceConstants.isOnline(Activity_Home.this)) {
			new GetUserList().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_USER_LIST);
		}
		searchNameListEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				userListAdapter.getFilter().filter(s.toString());
			}
		});
		nameList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				setListsItemLayout(userListAdapter.getItem(position));

			}
		});

		backFromListImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				settingAllGuysLayout();

			}
		});
		editListName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setEditListsLayout();

			}
		});
		createNewListLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Activity_Home.this,
						Activity_R_CreateNewList.class);
				startActivityForResult(intent, 1);

			}
		});
	}

	private void setEditListsLayout() {
		LayoutInflater inflater = getLayoutInflater();
		rightContainer.removeAllViews();
		LinearLayout cancelEditListButton, saveEditListButton, createNewList;
		EditText searchNameListEditText;
		ListView nameList;
		View view1 = inflater.inflate(R.layout.right_menu_lists_edit,

		rightContainer);
		cancelEditListButton = (LinearLayout) view1.findViewById(R.id.cancel);
		saveEditListButton = (LinearLayout) view1.findViewById(R.id.save);
		searchNameListEditText = (EditText) view1
				.findViewById(R.id.searchNameEditText);
		nameList = (ListView) view1.findViewById(R.id.nameList);

		createNewList = (LinearLayout) view1
				.findViewById(R.id.createNewListsLayout);
		TextView listTextView = (TextView) view1
				.findViewById(R.id.listTextView);
		TextView createNewListTextView = (TextView) view1
				.findViewById(R.id.createNewListTextView);
		TextView cancleTextView = (TextView) view1
				.findViewById(R.id.backFromListName1);
		TextView saveTextView = (TextView) view1
				.findViewById(R.id.createNewListImageButton);

		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		listTextView.setTypeface(typeface);
		createNewListTextView.setTypeface(typeface);
		searchNameListEditText.setTypeface(typeface);
		cancleTextView.setTypeface(typeface);
		saveTextView.setTypeface(typeface);

		userListAdapter = new UserListAdapter(userListDTOs, Activity_Home.this,
				1);
		nameList.setAdapter(userListAdapter);
		if (WebServiceConstants.isOnline(Activity_Home.this)) {
			new GetUserList().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_USER_LIST);
		}
		searchNameListEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				userListAdapter.getFilter().filter(s.toString());
			}
		});

		cancelEditListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setListsLayout();

			}
		});
		saveEditListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setListsLayout();

			}
		});
		createNewList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Activity_Home.this,
						Activity_R_CreateNewList.class);
				startActivityForResult(intent, 1);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (fragmentIds == AppConstants.FRAGMENT_PROFILE) {
			tempFragment.onActivityResult(requestCode, resultCode, data);
		} else if (fragmentIds == AppConstants.FRAGMENT_CHATT_MATCHES) {
			Log.d("", "onActivityResult1 chat fragmnet");
			tempFragment.onActivityResult(requestCode, resultCode, data);
		}

		if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CREATE_NEW_LIST) {
			UserListDTO userListDTO = new UserListDTO();
			userListDTO.setListId(data.getExtras().getString("listId"));
			userListDTO.setListName(data.getExtras().getString("name"));
			userListDTOs.add(userListDTO);
			userListAdapter.notifyDataSetChanged();
			Log.d("", "my new list =" + data.getExtras().getString("listId")
					+ " " + data.getExtras().getString("name"));
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.DELETE_DUDE_FROM_LIST) {

			subListAdapter.notifyDataSetChanged();
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.EDIT_LIST) {

			chatMatchesAdapter.notifyDataSetChanged();
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.PUSH_NOTIFICATION_ACTIVITY) {

			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			slide_me.close();
		} else if (requestCode == AppConstants.FRIEND_NOTIFICATION_ACTIVITY
				&& resultCode == AppConstants.FRIEND_NOTIFICATION_RESULT2) {

			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			slide_me.close();
		} else if (requestCode == AppConstants.FRIEND_NOTIFICATION_ACTIVITY
				&& resultCode == AppConstants.FRIEND_NOTIFICATION_RESULT1) {
			// LoadHomeFlag=true;
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			slide_me.close();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setListsItemLayout(final UserListDTO userListDTO) {
		ListView subList;

		EditText searchSubListEditText;
		TextView editSubListButton;

		LayoutInflater inflater = getLayoutInflater();

		View view = inflater.inflate(R.layout.right_menu_lists_item,
				rightContainer);
		TextView listName = (TextView) view.findViewById(R.id.listName);
		listName.setText("" + userListDTO.getListName());
		editSubListButton = (TextView) view.findViewById(R.id.editSubList);
		TextView lists = (TextView) view.findViewById(R.id.lists);
		searchSubListEditText = (EditText) view
				.findViewById(R.id.searchSubListEditText);

		Typeface typeface = FontStyle.getFont(Activity_Home.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		listName.setTypeface(typeface);
		editSubListButton.setTypeface(typeface);
		lists.setTypeface(typeface);
		searchSubListEditText.setTypeface(typeface);

		subList = (ListView) view.findViewById(R.id.subList);

		subListAdapter = new SearchByAdapter(Activity_Home.this,
				userListDTO.getDudeList(), 0);
		subList.setAdapter(subListAdapter);
		subList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FragmentChatMatches.userDTO = (UserDTO) subListAdapter
						.getItem(arg2);
				Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
				slide_me.close();
			}
		});

		searchSubListEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				subListAdapter.getFilter().filter(s.toString());
			}
		});

		editSubListButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (userListDTO.getDudeList().size() != 0) {
					Activity_R_EditList.userListDTO = userListDTO;
					Intent intent = new Intent(Activity_Home.this,
							Activity_R_EditList.class);
					intent.putExtra(AppConstants.COME_FROM,
							AppConstants.COME_FROM_LISTS);
					startActivityForResult(intent,
							AppConstants.DELETE_DUDE_FROM_LIST);
				} else {
					Toast.makeText(Activity_Home.this, "Empty list",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		lists.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setListsLayout();

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_buton:

			if (fragmentIds == AppConstants.FRAGMENT_PROFILE) {

				if (WebServiceConstants.isOnline(Activity_Home.this)) {
					((FragmentProfile) tempFragment).checkValidation(1);
				} else {
					slide_me.toggleLeftDrawer();
				}

			} else {
				setLeftDrawer();
			}

			break;
		case R.id.right_buton:
			if (fragmentIds == AppConstants.FRAGMENT_PROFILE) {

				if (WebServiceConstants.isOnline(Activity_Home.this)) {
					((FragmentProfile) tempFragment).checkValidation(2);
					setLeftDrawer = 0;
				} else {
					slide_me.toggleRightDrawer();
					;
				}
			} else {
				// if(LoadHomeFlag)
				// {
				// Attach_Fragment(AppConstants.FRAGMENT_HOME);
				// LoadHomeFlag=false;
				// }
				// if (mSearchByAdapter != null) {
				// mSearchByAdapter.notifyDataSetChanged();
				// }
				//
				// if (requistListAdapter != null) {
				// requistListAdapter.notifyDataSetChanged();
				// }
				settingAllGuysLayout();
				slide_me.toggleRightDrawer();

			}
			break;
		// case R.id.home:
		// // Attach_Fragment(AppConstants.FRAGMENT_HOME);
		// break;
		case R.id.profile:

			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				selectedItem = 0;
				Attach_Fragment(AppConstants.FRAGMENT_PROFILE);
				slide_me.close();
			} else {
				Toast.makeText(Activity_Home.this, "Please wait ......",
						Toast.LENGTH_SHORT).show();
			}

			// if (WebServiceConstants.isOnline(Activity_Home.this)) {
			// Intent intent = new Intent(Activity_Home.this,
			// Activity_ProfileDetails.class);
			// intent.putExtra(AppConstants.COME_FROM,
			// AppConstants.COME_FROM_UPDATE_PROFILE);
			// startActivity(intent);
			// }
			break;
		case R.id.findDudes:
			selectedItem = 1;
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			slide_me.close();
			break;
		// case R.id.getMatches:
		// // COME_FROM = AppConstants.FRAGMENT_GET_MATCHES;
		// //
		// // Attach_Fragment(AppConstants.FRAGMENT_HOME);
		// break;
		case R.id.dailyKrave:
			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				selectedItem = 2;

				Attach_Fragment(AppConstants.FRAGMENT_DAILY_KRAVE);
				slide_me.close();
			}
			break;
		case R.id.chatMatches:
			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				selectedItem = 3;
				Attach_Fragment(AppConstants.FRAGMENT_CHAT_FRIEND);
				slide_me.close();
			}
			break;
		case R.id.setting:
			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				selectedItem = 4;

				FragmentSetting.comeFrom = false;
				Attach_Fragment(AppConstants.FRAGMENT_SETTING);
				slide_me.close();
			}
			break;
		// case R.id.map:
		// Attach_Fragment(AppConstants.FRAGMENT_MAP);
		// break;

		case R.id.contactUs:
			if (WebServiceConstants.isOnline(Activity_Home.this)) {

				Intent intent = new Intent(Activity_Home.this,
						Activity_Feedback.class);
				startActivity(intent);

			}
			break;
		case R.id.logout:

			if (WebServiceConstants.isOnline(Activity_Home.this)) {
				selectedItem = 5;

				openDailog();
			}

			// Intent intent = new Intent(Activity_Home.this,
			// MainActivity.class);
			// startActivity(intent);
			// finish();
			// // connection.disconnect();// chat lagout
			// sessionManager.Logout();
			// Intent intent = new Intent(Activity_Home.this,
			// MainActivity.class);
			// startActivity(intent);

			break;

		default:
			break;
		}

	}

	public void openDailog() {
		try {
			final Dialog dialog = new Dialog(Activity_Home.this,
					android.R.style.Theme_Translucent_NoTitleBar);

			dialog.setContentView(R.layout.dialog_logout);

			Button cancle = (Button) dialog.findViewById(R.id.cancle);
			Button ok = (Button) dialog.findViewById(R.id.ok);
			TextView title = (TextView) dialog.findViewById(R.id.title);
			Typeface typeface = FontStyle.getFont(Activity_Home.this,
					AppConstants.HelveticaNeueLTStd_Lt);
			title.setTypeface(typeface);
			cancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					String userId = sessionManager.getUserDetail().getUserID();

					stopUserCurrentLocationService();
					new UserLogoutAsyncTask().execute(
							WebServiceConstants.BASE_URL
									+ WebServiceConstants.LOGOUT, userId);

				}
			});
			dialog.show();
		} catch (Exception e) {

		}
	}

	// public void openDailogForPushNotification(final UserDTO userDTO) {
	// final Dialog dialog = new Dialog(Activity_Home.this,
	// android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	//
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//
	// dialog.setContentView(R.layout.dialog_push_notification_friend_request);
	//
	// Button cancle = (Button) dialog.findViewById(R.id.cancle);
	// Button ok = (Button) dialog.findViewById(R.id.ok);
	// ImageView userProfileImage = (ImageView) dialog
	// .findViewById(R.id.userProfileImage);
	// ImageView dudeProfileImage = (ImageView) dialog
	// .findViewById(R.id.dudeProfileImage);
	//
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
	//
	// cancle.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// FragmentHome.Refresh_Data = true;
	// dialog.dismiss();
	// }
	// });
	// ok.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// FragmentChatMatches.userDTO = userDTO;
	// dialog.dismiss();
	// FragmentHome.Refresh_Data = true;
	// Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
	//
	// }
	// });
	// dialog.show();
	// }

	private void setLeftDrawer() {
		switch (setLeftDrawer) {
		case AppConstants.BACK_BUTTON_FROM_DUDES_PROFILE:
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			setLeftDrawer = 0;
			// left_button.setImageResource(R.drawable.nav);
			break;
		case AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE:
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			setLeftDrawer = 0;
			// left_button.setImageResource(R.drawable.nav);
			break;
		case AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE_WITH_CHAT:
			Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			setLeftDrawer = 0;
			// left_button.setImageResource(R.drawable.nav);
			break;
		case AppConstants.BACK_BUTTON_FROM_SETTING:
			Attach_Fragment(AppConstants.FRAGMENT_HOME);
			setLeftDrawer = 0;
			// left_button.setImageResource(R.drawable.nav);
			break;
		case AppConstants.BACK_BUTTON_FROM_DAILY_KRAVE_DETAIL:
			Attach_Fragment(AppConstants.FRAGMENT_DAILY_KRAVE);
			setLeftDrawer = 0;
			left_button.setImageResource(R.drawable.nav);
			break;
		case AppConstants.BACK_BUTTON_FROM_UPDATE_PROFILE:
			((FragmentUpdateProfile) tempFragment).setBackButtonFunction();

			break;
		// case AppConstants.BACK_BUTTON_FROM_UPDATE_IMAGE_PROFILE:
		// if (((FragmentProfile) tempFragment).checkValidation()) {
		// setLeftDrawer = 0;
		// slide_me.toggleLeftDrawer();
		// }
		//
		// break;
		default:
			slide_me.toggleLeftDrawer();
			break;
		}

	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		FragmentUpdateProfile fragmentUpdateProfile = (FragmentUpdateProfile) tempFragment;
		return new DatePickerDialog(this,
				fragmentUpdateProfile.updateProfileAdapter.datePickerListener,
				fragmentUpdateProfile.updateProfileAdapter.year,
				fragmentUpdateProfile.updateProfileAdapter.month,
				fragmentUpdateProfile.updateProfileAdapter.day);
	}

	public void Attach_Fragment(int fragmentId) {

		try {
			LinearLayout selectedLayout[] = { layoutProfile, layoutFindDudes,
					layoutDailyKrave, layoutChatMatches, layoutSetting,
					layoutLogout };
			// if (fragmentId <= 7) {
			// selectedLayout[selectedItem]
			// .setBackgroundColor(R.color.gray);
			// selectedItem = fragmentId;
			// selectedLayout[selectedItem - 1]
			// .setBackgroundColor(R.color.unselect);
			// }
			// Toast.makeText(Activity_Home.this, "" + selectedItem,
			// Toast.LENGTH_SHORT).show();
			// Toast.makeText(Activity_Home.this, "" + previousItem,
			// Toast.LENGTH_SHORT).show();

			// for (int i = 0; i < 6; i++) {
			// if ((selectedItem) == i) {
			selectedLayout[previousItem]
					.setBackgroundResource(R.color.transprent_color_code);
			selectedLayout[selectedItem].setBackgroundResource(R.color.select);
			previousItem = selectedItem;
			// } else {

			// }
			// }

			this.fragmentIds = fragmentId;
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			RemoveAll(fragmentTransaction);

			switch (fragmentId) {
			case AppConstants.FRAGMENT_HOME:
				tempFragment = new FragmentHome();

				break;
			case AppConstants.FRAGMENT_PROFILE:
				tempFragment = new FragmentProfile();

				break;
			case AppConstants.FRAGMENT_FIND_DUDES:
				// tempFragment = new FragmentFindDudes();
				tempFragment = new FragmentHome();
				break;
			case AppConstants.FRAGMENT_GET_MATCHES:
				// tempFragment = new FragmentGetMatches();
				tempFragment = new FragmentHome();
				break;
			case AppConstants.FRAGMENT_DAILY_KRAVE:
				tempFragment = new FragmentDailyKraveWithWebView();

				break;
			case AppConstants.FRAGMENT_CHATT_MATCHES:
				tempFragment = new FragmentChatMatches();

				break;
			case AppConstants.FRAGMENT_SETTING:
				tempFragment = new FragmentSetting();

				break;
			case AppConstants.FRAGMENT_MAP:
				// tempFragment = new FragmentMap();

				break;
			case AppConstants.FRAGMENT_DUDES_PROFILE:
				tempFragment = new FragmentDudesProfile();

				break;
			case AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE:
				tempFragment = new FragmentDetailDudesProfile();

				break;

			case AppConstants.FRAGMENT_CHAT_FRIEND:
				tempFragment = new FragmentChatFriend();

				break;
			// case AppConstants.FRAGMENT_DUDES_CONGRATULATION:
			// tempFragment = new FragmentDudesCongratulation();
			//
			// break;
			case AppConstants.FRAGMENT_DAILY_KRAVE_DETAIL:
				tempFragment = new FragmentDetailDailyKrave();

				break;
			case AppConstants.FRAGMENT_UPDATE_PROFILE:
				tempFragment = new FragmentUpdateProfile();

				break;
			default:
				break;
			}
			ChatService.ONCHAT = fragmentId;
			tempFragment.setRetainInstance(true);
			fragmentTransaction.replace(R.id.containerLayout, tempFragment);
			// fragmentTransaction.attach(tempFragment);

			fragmentTransaction.commitAllowingStateLoss();
			fragmentTransaction.disallowAddToBackStack();

		} catch (Exception e) {

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			switch (fragmentIds) {
			case AppConstants.FRAGMENT_HOME:
				((FragmentHome) tempFragment).setBackButtonFunction();
				finish();
				break;
			case AppConstants.FRAGMENT_DUDES_PROFILE:
				Attach_Fragment(AppConstants.FRAGMENT_HOME);
				setLeftDrawer = 0;
				// left_button.setImageResource(R.drawable.nav);
				break;
			case AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE:
				if (FragmentDetailDudesProfile.comeFrom == AppConstants.FRAGMENT_CHATT_MATCHES) {
					Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
				} else {

					Attach_Fragment(AppConstants.FRAGMENT_HOME);
				}
				setLeftDrawer = 0;

				// left_button.setImageResource(R.drawable.nav);
				break;
			case AppConstants.FRAGMENT_CHATT_MATCHES:
				// if (FragmentChatMatches.backButtonComeFrom) {
				Attach_Fragment(AppConstants.FRAGMENT_CHAT_FRIEND);
				// } else {
				//
				// setLeftDrawer = 0;
				//
				// left_button.setImageResource(R.drawable.nav);
				// onClick(layoutFindDudes);
				//
				// }
				break;
			case AppConstants.FRAGMENT_DAILY_KRAVE_DETAIL:
				setLeftDrawer = 0;

				left_button.setImageResource(R.drawable.nav);
				Attach_Fragment(AppConstants.FRAGMENT_DAILY_KRAVE);
				break;
			case AppConstants.FRAGMENT_SETTING:

				setLeftDrawer = 0;

				// left_button.setImageResource(R.drawable.nav);
				// onClick(layoutFindDudes);
				selectedItem = 1;
				Attach_Fragment(AppConstants.FRAGMENT_HOME);

				break;
			case AppConstants.FRAGMENT_UPDATE_PROFILE:
				((FragmentUpdateProfile) tempFragment).setBackButtonFunction();
				break;
			case AppConstants.FRAGMENT_PROFILE:
				((FragmentProfile) tempFragment).checkValidation(3);
				break;
			default:
				selectedItem = 1;
				Attach_Fragment(AppConstants.FRAGMENT_HOME);
				//onClick(layoutFindDudes);
				break;
			}
		}
		return false;
	}

	private static void RemoveAll(FragmentTransaction fragmentTransaction) {
		try {
			ChatService.ONCHAT = AppConstants.FRAGMENT_HOME;
			if (tempFragment != null)
				fragmentTransaction.detach(tempFragment);
			System.gc();
		} catch (Exception e) {

		}

	}

	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long
	// arg3) {
	//
	// FragmentChatMatches.userDTO = searchDudeList.get(arg2);
	// Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
	//
	// }

	class GetAllLikeDude extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		// http://parkhya.org/Android/krave_app/index.php?action=getfriends&user_id=107
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// //
			// http://parkhya.org/Android/krave_app/index.php?action=user_login&email=test2@gmail.com&password=123456
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("like user response :", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				dialog.dismiss();
			} catch (Exception e) {

			}
			searchDudeList.clear();
			System.out.print("like user response :" + jsonArray);
			// System.out.print("search user response :" + jsonArray.length());
			try {

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject Object = jsonArray.getJSONObject(i);

					searchDudeList.add(parseUserData(Object));
				}
				chatMatchesAdapter.notifyDataSetChanged();
				/*
				 * if (searchDudeList.size() == 0) {
				 * Toast.makeText(Activity_Home.this, "No Guy",
				 * Toast.LENGTH_SHORT).show(); }
				 */
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// private void parseUserDataAndSetInSession(JSONArray mjsonArray)
	// throws JSONException {
	// JSONObject MainObject;
	// for (int i = 0; i <= mjsonArray.length(); i++) {
	// MainObject = mjsonArray.getJSONObject(i);
	// UserDTO userDTO = new UserDTO();
	// AddressDTO addressDTO = new AddressDTO();
	// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	//
	// userDTO.setUserID(MainObject.getString("user_id"));
	// userDTO.setEmail(MainObject.getString("user_email"));
	// userDTO.setFirstName(MainObject.getString("user_fname"));
	// userDTO.setLastName(MainObject.getString("user_lname"));
	// userDTO.setProfileImage(MainObject.getString("user_image"));
	// userDTO.setMobile(MainObject.getString("user_mobile"));
	// userDTO.setAboutMe(MainObject.getString("user_note"));
	// userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
	// userDTO.setAboutMe(MainObject.getString("user_note"));
	//
	// whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
	// whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	// whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	// whatAreYouDTO.setHight(MainObject.getString("user_height"));
	// whatAreYouDTO.setAge(MainObject.getString("user_age"));
	// whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
	// whatAreYouDTO.setRelationshipStatus(MainObject
	// .getString("user_relationshipStatus"));
	// whatAreYouDTO
	// .setWhatAreYou(MainObject.getString("user_whatAreYou"));
	// whatAreYouDTO.setWhatDoYouKrave(MainObject
	// .getString("user_whatDoYouKrave"));
	//
	// userDTO.setWhatAreYouDTO(whatAreYouDTO);
	// searchDudeList.add(userDTO);
	//
	// }
	// }

	private UserDTO parseUserData(JSONObject mJsonObject) throws JSONException {
		UserDTO userDTO = new UserDTO();
		// AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		LatLongDTO latLongDTO = new LatLongDTO();
		JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");

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
		try {
			userDTO.setUnreadMsg(MainObject.getString("unread_msg"));

			if (!MainObject.getString("user_message").equals("null")) {
				if (MainObject.getString("user_message").contains(
						AppConstants.BASE_IMAGE_PATH_1)) {
					userDTO.setUserLastMsg("PHOTO");

				} else {
					userDTO.setUserLastMsg(MainObject.getString("user_message"));

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			if (MainObject.getString("isonline").equals("available")) {
				userDTO.setIsOnline(AppConstants.ONLINE);

			} else if (MainObject.getString("isonline").equals("away")) {
				userDTO.setIsOnline(AppConstants.ABSENT);
			} else {
				userDTO.setIsOnline(AppConstants.OFFLINE);
			}
		} catch (Exception e) {

		}
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}
		// userDTO.setCommonFriends(MainObject.getString("mutualfriends"));
		whatAreYouDTO.setFeet(MainObject.getString("user_height"));
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
		try {
			whatAreYouDTO.setWhatAreYouName(MainObject
					.getString("user_whatAreYou_name"));
		} catch (Exception e) {
			whatAreYouDTO.setWhatAreYouName("ASIAN");
		}
		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrests_id"));
			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));
			interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
					+ interestJsonObject.getString("intrests_image"));
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
			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}
			userProfileImageDTOs.add(userProfileImageDTO);

		}

		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				userProfileImageDTOs.remove(dto);
				userProfileImageDTOs.add(0, dto);
				break;
			}
		}

		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		for (int i = 0; i < jsonArrayLatLong.length(); i++) {
			JSONObject latLongJsonObject = jsonArrayLatLong.getJSONObject(i);
			LatLongDTO mDto = new LatLongDTO();
			mDto.setUser_id(latLongJsonObject.getString("User_id"));
			mDto.setLatitude(latLongJsonObject.getString("latitude"));
			mDto.setLongitude(latLongJsonObject.getString("longitude"));
			userDTO.setStreet(latLongJsonObject.getString("ll_street"));
			userDTO.setCity(latLongJsonObject.getString("ll_city"));
			userDTO.setState(latLongJsonObject.getString("ll_state"));
			userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
			userDTO.setCountry(latLongJsonObject.getString("ll_country"));
			latLongDTO = mDto;

		}

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		// sessionManager.setUserDetail(userDTO);
		return userDTO;

	}

	private UserDTO parseUserDataForSingleDude(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		// System.out.println(MainObject);

		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
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
					.getString("intrests_id"));
			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));
			interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
					+ interestJsonObject.getString("intrests_image"));
			interestsDTOs.add(interestsDTO);

		}
		for (int i = 0; i < jsonArrayImages.length(); i++) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));
			userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
					+ imagesJsonObject.getString("image_path"));
			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}
			userProfileImageDTOs.add(userProfileImageDTO);

		}
		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				userProfileImageDTOs.remove(dto);
				userProfileImageDTOs.add(0, dto);
				break;
			}
		}
		for (int i = 0; i < jsonArrayLatLong.length(); i++) {
			JSONObject latLongJsonObject = jsonArrayLatLong.getJSONObject(i);
			LatLongDTO mDto = new LatLongDTO();
			mDto.setUser_id(latLongJsonObject.getString("User_id"));
			mDto.setLatitude(latLongJsonObject.getString("latitude"));
			mDto.setLongitude(latLongJsonObject.getString("longitude"));
			userDTO.setStreet(latLongJsonObject.getString("ll_street"));
			userDTO.setCity(latLongJsonObject.getString("ll_city"));
			userDTO.setState(latLongJsonObject.getString("ll_state"));
			userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
			userDTO.setCountry(latLongJsonObject.getString("ll_country"));
			latLongDTO = mDto;

		}
		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

		}

		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);

		return userDTO;

	}

	// private UserDTO parseUserData(JSONObject mJsonObject) throws
	// JSONException {
	// UserDTO userDTO = new UserDTO();
	// // AddressDTO addressDTO = new AddressDTO();
	// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	// List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	// List<UserProfileImageDTO> userProfileImageDTOs = new
	// ArrayList<UserProfileImageDTO>();
	//
	// JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
	// JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
	// JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
	// // System.out.println(MainObject);
	// userDTO.setLanguage(MainObject.getString("user_language"));
	// userDTO.setUserID(MainObject.getString("user_id"));
	// userDTO.setEmail(MainObject.getString("user_email"));
	// userDTO.setFirstName(MainObject.getString("user_fname"));
	// userDTO.setLastName(MainObject.getString("user_lname"));
	// userDTO.setProfileImage(MainObject.getString("user_image"));
	// userDTO.setMobile(MainObject.getString("user_mobile"));
	// userDTO.setAboutMe(MainObject.getString("user_note"));
	// userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
	//
	// whatAreYouDTO.setFeet(MainObject.getString("user_height"));
	// whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	// whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	// whatAreYouDTO.setHight(MainObject.getString("user_height"));
	// whatAreYouDTO.setAge(MainObject.getString("user_age"));
	// whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
	// // whatAreYouDTO.setAge(MainObject.getString("user_note"));
	// whatAreYouDTO.setRelationshipStatus(MainObject
	// .getString("user_relationshipStatus"));
	// whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
	// whatAreYouDTO.setWhatDoYouKrave(MainObject
	// .getString("user_whatDoYouKrave"));
	//
	// for (int i = 0; i < jsonArrayInterest.length(); i++) {
	// JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
	// InterestsDTO interestsDTO = new InterestsDTO();
	// interestsDTO.setInterestId(interestJsonObject
	// .getString("intrests_id"));
	// interestsDTOs.add(interestsDTO);
	//
	// }
	// for (int i = 0; i < jsonArrayImages.length(); i++) {
	// JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
	// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
	// userProfileImageDTO.setImageId(imagesJsonObject
	// .getString("image_id"));
	// userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
	// + imagesJsonObject.getString("image_path"));
	//
	// userProfileImageDTOs.add(userProfileImageDTO);
	//
	// }
	// if (!"url".equals(userDTO.getProfileImage())) {
	// Log.d("", "facebook image at first position in list");
	// UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
	// userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
	// userProfileImageDTO.setImagePath(userDTO.getProfileImage());
	// userProfileImageDTOs.add(0, userProfileImageDTO);
	// }
	// userDTO.setWhatAreYouDTO(whatAreYouDTO);
	// userDTO.setInterestList(interestsDTOs);
	// userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
	// // sessionManager.setUserDetail(userDTO);
	// return userDTO;
	//
	// }

	private UserDTO parseUserDataforList(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		// AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		LatLongDTO latLongDTO = new LatLongDTO();
		JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONObject latLongJsonObject = mJsonObject.getJSONObject("latlong");

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
		try {
			if (MainObject.getString("isonline").equals("available")) {
				userDTO.setIsOnline(AppConstants.ONLINE);

			} else if (MainObject.getString("isonline").equals("away")) {
				userDTO.setIsOnline(AppConstants.ABSENT);
			} else {
				userDTO.setIsOnline(AppConstants.OFFLINE);
			}
		} catch (Exception e) {

		}
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}
		// userDTO.setCommonFriends(MainObject.getString("mutualfriends"));
		whatAreYouDTO.setFeet(MainObject.getString("user_height"));
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

		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrests_id"));
			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));
			interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
					+ interestJsonObject.getString("intrests_image"));
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

			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}

		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				userProfileImageDTOs.remove(dto);
				userProfileImageDTOs.add(0, dto);
				break;
			}
		}
		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		LatLongDTO mDto = new LatLongDTO();
		mDto.setUser_id(latLongJsonObject.getString("User_id"));
		mDto.setLatitude(latLongJsonObject.getString("latitude"));
		mDto.setLongitude(latLongJsonObject.getString("longitude"));
		userDTO.setStreet(latLongJsonObject.getString("ll_street"));
		userDTO.setCity(latLongJsonObject.getString("ll_city"));
		userDTO.setState(latLongJsonObject.getString("ll_state"));
		userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
		userDTO.setCountry(latLongJsonObject.getString("ll_country"));
		latLongDTO = mDto;

		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		// sessionManager.setUserDetail(userDTO);
		return userDTO;

	}

	/* right menu web service */

	public class GetUserList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user list response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			userListDTOs.clear();
			// [{"list_id":"1","list_title":"abc","user_id":"1"},{"list_id":"3","list_title":"testsdfsdfsdf","user_id":"1"},{"list_id":"4","list_title":"test","user_id":"1"}]
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject listJsonObject = jsonArray.getJSONObject(i);
					UserListDTO dto = new UserListDTO();
					dto.setListId(listJsonObject.getString("list_id"));
					dto.setListName(listJsonObject.getString("list_title"));

					JSONArray userJsonArray = listJsonObject
							.getJSONArray("users");
					List<UserDTO> userDTOs = new ArrayList<UserDTO>();
					for (int j = 0; j < userJsonArray.length(); j++) {

						JSONObject jsonObject = userJsonArray.getJSONObject(j);

						userDTOs.add(parseUserData(jsonObject));
					}

					dto.setDudeList(userDTOs);
					userListDTOs.add(dto);
				}
				userListAdapter.notifyDataSetChanged();
				if (userListDTOs.size() == 0) {
					Toast.makeText(Activity_Home.this, "No list",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public class GetDudeRequistsList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			// params.add(new BasicNameValuePair("userid", ""
			// + sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get request dude list response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			requestDudesList.clear();
			// [{"list_id":"1","list_title":"abc","user_id":"1"},{"list_id":"3","list_title":"testsdfsdfsdf","user_id":"1"},{"list_id":"4","list_title":"test","user_id":"1"}]

			try {
				// JSONObject mainObject = jsonArray.getJSONObject(0);
				// JSONArray userJsonArray = mainObject.getJSONArray("users");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject Object = jsonArray.getJSONObject(i);

					requestDudesList.add(parseUserData(Object));
				}

				requistListAdapter.notifyDataSetChanged();
				if (requestDudesList.size() == 0) {
					Toast.makeText(Activity_Home.this, "No request",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	class GetDudeById extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", "" + args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("get dude response : " + jsonArray);
				UserDTO userDTO = null;
				for (int i = 0; i < jsonArray.length(); i++) {
					userDTO = parseUserDataForSingleDude(mJsonObject);
				}
				GCMIntentService.userId = "-1";
				Activity_Push_Notification.userDTO = userDTO;
				Intent intent = new Intent(Activity_Home.this,
						Activity_Push_Notification.class);
				startActivityForResult(intent,
						AppConstants.PUSH_NOTIFICATION_ACTIVITY);
				// openDailogForPushNotification(userDTO);
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(Activity_Home.this, "Dudes data not found",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	public void ShowFriendAcceptDialog(UserDTO userDTO) {
		Activity_Friens_Request_Accept.userDTO = userDTO;
		Intent intent = new Intent(this, Activity_Friens_Request_Accept.class);
		startActivityForResult(intent,
				AppConstants.FRIEND_NOTIFICATION_ACTIVITY);
	}

	class UserLogoutAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", "" + args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
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
					// stopUserCurrentLocationService();

				} else {
					Toast.makeText(Activity_Home.this,
							"Logout fail, please try again !",
							Toast.LENGTH_SHORT).show();
				}
				finish();
				// connection.disconnect();// chat lagout
				sessionManager.Logout();
			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}

}
