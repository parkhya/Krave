package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ps.adapters.FindDudesAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentHome extends Fragment implements OnClickListener,
		OnPageChangeListener {

	private ImageView nextButton, previousButton, settingButton,
			onlineDudesFilter;
	private ViewPager viewPager;
	private FindDudesAdapter findDudesAdapter;

	private Activity_Home contex;
	private GPSTracker mGpsTracker;
	// private int count;
	private LinearLayout onlyOnlineDudeLayout, nearMeLayout;

	private RelativeLayout swipeLayout, refreshButtonLayout;
	private TextView noGuysText;
	// private boolean nearMe = false;
	private ImageView clearSearchResult, nearMeImageView;
	private EditText searchCity;

	private final int DUDE_BY_FILTER = 0;
	private final int DUDE_BY_CITY = 1;
	private int comeFrom;
	private SessionManager sessionManager;
	public static boolean Refresh_Data = false;
	public static List<UserDTO> userDTOs = new ArrayList<UserDTO>();
	public static boolean filterIsOnline = false;
	public static int previousCount = 0;
	public static int pageNumber = 0;
	public static int filterId = 0;
	public static int currentSelectedPage = 0;
	public static String city = "";
	public static boolean clearData = false;
	BroadcastReceiver broadcastReceiver1;
	BroadcastReceiver refreshHomeFragment;

	// boolean onPage = true;

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

		viewPager = null;

		findDudesAdapter = null;

		System.gc();
		if (clearData) {
			clearData = false;
		} else {
			setBackButtonFunction();
		}
		contex.unregisterReceiver(refreshHomeFragment);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		contex = (Activity_Home) getActivity();

		mGpsTracker = new GPSTracker(contex);

		sessionManager = new SessionManager(contex);
		setLayout(view);
		setListener();
		setPager();
		setTypeFace(view);

		if (contex.COME_FROM == AppConstants.FRAGMENT_GET_MATCHES) {
			contex.COME_FROM = 0;
			comeFrom = AppConstants.FRAGMENT_GET_MATCHES;
			if (WebServiceConstants.isOnline(contex)) {
				// new GetDudesAsynchTask()
				// .execute("http://parkhya.org/Android/krave_app/index.php?action=searchByUserIntrestWAU&userid=6");
			}
		} else {
			Log.d("", "filter id=" + filterId);
			if (userDTOs.size() == 0 || Refresh_Data) {
				if (WebServiceConstants.isOnline(contex)) {
					Refresh_Data = false;
					setWebServiceData(filterId);
					setFilterUi();
				}
			} else {

				setFilterUi();
				findDudesAdapter.notifyDataSetChanged();
				viewPager.setCurrentItem(currentSelectedPage);
				handler.postDelayed(runnable, 5000);
			}
		}

		// else {
		// comeFrom = AppConstants.FRAGMENT_HOME;
		// new GetDudesAsynchTask().execute(WebServiceConstants.BASE_URL
		// + WebServiceConstants.GET_DUDES_FROM_PARTICULAR_RANGE);
		// }

		// broadcastReceiver1 = new BroadcastReceiver() {
		//
		// @Override
		// public void onReceive(Context context, Intent intent) {
		// // Toast.makeText(context, "SMS SENT!!", Toast.LENGTH_SHORT)
		// // .show();
		// onPageSelected(0);
		// }
		// };
		// IntentFilter filterSend = new IntentFilter();
		// filterSend.addAction("onpage");
		// contex.registerReceiver(broadcastReceiver1, filterSend);

		refreshHomeFragment = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Log.d("", "onReceive" + refreshHomeFragment);
				setWebServiceData(filterId);
			}
		};

		IntentFilter closeActivity = new IntentFilter();
		closeActivity.addAction("refresh");
		contex.registerReceiver(refreshHomeFragment, closeActivity);

		return view;
	}

	private void checkGpsEnabledOrnot() {
		LocationManager MLocationManager = (LocationManager) contex
				.getSystemService(Context.LOCATION_SERVICE);
		// getting GPS status
		boolean isGPSEnabled = MLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isGPSEnabled) {
			Intent intent = new Intent(contex, Activity_Gps_Alert.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intent);
		}
	}

	public static void setBackButtonFunction() {
		userDTOs.clear();
		filterIsOnline = false;
		previousCount = 0;
		pageNumber = 0;
		filterId = 0;
		city = "";
	}

	private void setFilterUi() {
		switch (filterId) {
		case DUDE_BY_FILTER:
			searchCity.setText(city);
			nearMeImageView.setBackgroundResource(R.drawable.near_check);
			// nearMe = true;
			break;
		case DUDE_BY_CITY:
			searchCity.setText(city);
			break;

		default:
			break;
		}

		if (filterIsOnline) {
			onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
		} else {
			onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
		}

	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			swipeLayout.setVisibility(View.GONE);
			refreshButtonLayout.setVisibility(View.VISIBLE);

		}
	};

	private void setTypeFace(View view) {
		TextView textView1, textView2, textView3, textView4, textView5, textView6;
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		textView3 = (TextView) view.findViewById(R.id.textView3);
		textView4 = (TextView) view.findViewById(R.id.textView4);
		textView5 = (TextView) view.findViewById(R.id.textView5);
		textView6 = (TextView) view.findViewById(R.id.textView6);

		Typeface typeface = FontStyle.getFont(contex,
				AppConstants.HelveticaNeueLTStd_Roman);
		textView1.setTypeface(typeface);

		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		noGuysText.setTypeface(typeface);
		searchCity.setTypeface(typeface);
		// Typeface typeface1 = FontStyle.getFont(contex,
		// AppConstants.HelveticaNeueLTStsd_Bd);
		textView2.setTypeface(typeface);

	}

	private void setListener() {
		nextButton.setOnClickListener(this);
		previousButton.setOnClickListener(this);
		settingButton.setOnClickListener(this);
		onlyOnlineDudeLayout.setOnClickListener(this);
		clearSearchResult.setOnClickListener(this);
		// nearMeLayout.setOnClickListener(this);
		refreshButtonLayout.setOnClickListener(this);

		searchCity.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				city = searchCity.getText().toString();
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if (searchCity.getText().toString().trim().length() == 0) {
						Toast.makeText(contex, "Enter city name..",
								Toast.LENGTH_SHORT).show();
					} else {

						InputMethodManager imm = (InputMethodManager) contex
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								searchCity.getWindowToken(), 0);
						nearMeImageView
								.setBackgroundResource(R.drawable.near_uncheck);
						// onPage = false;
						setWebServiceData(DUDE_BY_CITY);

					}
				}

				return false;
			}
		});

		searchCity.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.toString().trim().length() == 0
						&& filterId == DUDE_BY_CITY) {
					Log.d("", "addTextChangedListener");
					city = "";
					setWebServiceData(DUDE_BY_FILTER);
					setFilterUi();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void setLayout(View view) {

		RelativeLayout mainview = (RelativeLayout) view
				.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		noGuysText = (TextView) view.findViewById(R.id.no_guys_text);
		nextButton = (ImageView) view.findViewById(R.id.next1);
		previousButton = (ImageView) view.findViewById(R.id.back1);
		settingButton = (ImageView) view.findViewById(R.id.setting);
		onlineDudesFilter = (ImageView) view.findViewById(R.id.filter);
		onlyOnlineDudeLayout = (LinearLayout) view
				.findViewById(R.id.linearLayout1);

		searchCity = (EditText) view.findViewById(R.id.serchCity);
		clearSearchResult = (ImageView) view
				.findViewById(R.id.clearSearchResult);
		nearMeImageView = (ImageView) view.findViewById(R.id.nearMe);
		nearMeLayout = (LinearLayout) view.findViewById(R.id.linearLayout2);
		swipeLayout = (RelativeLayout) view.findViewById(R.id.swipeLayout);
		refreshButtonLayout = (RelativeLayout) view
				.findViewById(R.id.refreshButton);

		viewPager = (ViewPager) view.findViewById(R.id.viewpager);

	}

	private void setPager() {
		findDudesAdapter = new FindDudesAdapter(getActivity(),
				(ArrayList<UserDTO>) userDTOs);
		viewPager.setOnPageChangeListener(this);
		// viewPager.setOnTouchListener(new OnSwipeTouchListener() {
		//
		// @Override
		// public void onSwipeBottom() {
		// // TODO Auto-generated method stub
		// super.onSwipeBottom();
		//
		// previousCount = 1;
		//
		// pageNumber = 0;
		// userDTOs.clear();
		// findDudesAdapter.notifyDataSetChanged();
		// if (WebServiceConstants.isOnline(contex))
		// new GetDudesAsynchTask().execute();
		//
		// }
		// });
		viewPager.setAdapter(findDudesAdapter);
		findDudesAdapter.notifyDataSetChanged();

	}

	/*
	 * private void calculatePages(int size) { // int size = userDTOs.size();
	 * count = (size) / 6; if (0 < size % 6) { count++; } }
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearLayout1:
			if (filterIsOnline) {
				onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
				// findDudesAdapter.getFilter().filter(AppConstants.ALL_DUDE);
				filterIsOnline = false;
				// calculatePages(userDTOs.size());
				// viewPager.setCurrentItem(0);

			} else {
				onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
				// findDudesAdapter.getFilter().filter(AppConstants.ONLINE);
				filterIsOnline = true;

				// viewPager.setCurrentItem(0);
			}
			// onPage = false;
			setWebServiceData(filterId);
			// calculatePages(findDudesAdapter.filterListSize);
			// onPageSelected(0);
			break;
		case R.id.linearLayout2:
			// if (nearMe) {
			// nearMeImageView.setBackgroundResource(R.drawable.near_uncheck);
			//
			// // findDudesAdapter.getFilter().filter(AppConstants.ALL_DUDE);
			// nearMe = false;
			// setWebServiceData(0);
			//
			// } else {
			// searchCity.setText("");
			// nearMeImageView.setBackgroundResource(R.drawable.near_check);
			// // findDudesAdapter.getFilter().filter(AppConstants.ONLINE);
			// nearMe = true;
			//
			// setWebServiceData(2);
			// }
			// calculatePages(findDudesAdapter.filterListSize);
			// onPageSelected(0);
			break;
		case R.id.clearSearchResult:
			if (filterId == DUDE_BY_CITY) {
				city = "";
				// onPage = false;
				setWebServiceData(DUDE_BY_FILTER);
				setFilterUi();
			} else {
				searchCity.setText("");
			}
			break;
		case R.id.setting:

			if (WebServiceConstants.isOnline(contex)) {
				FragmentSetting.comeFrom = true;
				filterId = DUDE_BY_FILTER;
				previousCount = 0;
				filterIsOnline = false;
				pageNumber = 0;
				userDTOs.clear();
				city = "";
				contex.Attach_Fragment(AppConstants.FRAGMENT_SETTING);
			}
			break;
		case R.id.refreshButton:
			// onPage = false;
			setWebServiceData(filterId);
			break;
		case R.id.back1:
			int position = viewPager.getCurrentItem();
			if ((position - 1) >= 0) {
				viewPager.setCurrentItem(position - 1);
			}

			break;
		case R.id.next1:
			int positio = viewPager.getCurrentItem();
			if ((positio + 1) < findDudesAdapter.getCount()) {
				viewPager.setCurrentItem(positio + 1);
			}
			break;

		default:
			break;
		}

	}

	private void setWebServiceData(int id) {
		filterId = id;
		previousCount = 0;

		pageNumber = 0;
		userDTOs.clear();
		findDudesAdapter.notifyDataSetChanged();
		if (WebServiceConstants.isOnline(contex))
			new GetDudesAsynchTask().execute();

	}

	class GetDudesAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(contex);
			// dialog.setMessage("Please Wait...");
			dialog.setCancelable(false);
			dialog.show();
			Log.d("", "lat long =" + mGpsTracker.getLatitude() + " , "
					+ mGpsTracker.getLongitude());
		}

		protected JSONArray doInBackground(String... args) {
			// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&city=indore&userid=1&records=12&status=online
			// (12:20:46 IST) Yogesh:
			// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&nearme=yes&userid=1&records=12&status=online

			// http://198.12.150.189/~simssoe/krave/index.php?action=searchbysetting&userid=&page=&records=;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// Log.d("", "page number");
			pageNumber = pageNumber + 1;
			Log.d("", "page number=" + pageNumber);
			String url = "";
			// params.add(new BasicNameValuePair("action", "login_user"));
			/*
			 * if (comeFrom == AppConstants.FRAGMENT_GET_MATCHES) { Log.d("",
			 * "get matches url"); // params.add(new
			 * BasicNameValuePair("userid", "" // +
			 * sessionManager.getUserDetail().getUserID())); } else {
			 * params.add(new BasicNameValuePair("lat", "" +
			 * mGpsTracker.getLatitude())); params.add(new
			 * BasicNameValuePair("long", "" + mGpsTracker.getLongitude()));
			 * params.add(new BasicNameValuePair("distance", "" + 5000)); }
			 */

			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("page", "" + (pageNumber)));
			params.add(new BasicNameValuePair("records", "" + 12));
			if (filterIsOnline) {
				params.add(new BasicNameValuePair("status", "online"));
			} else {
				params.add(new BasicNameValuePair("status", "all"));
			}
			if (filterId == 0) {
				url = WebServiceConstants.BASE_URL
						+ WebServiceConstants.SEARCH_BY_SETTING;
				Log.d("", "dude by filter");
			} else if (filterId == 1) {
				Log.d("", "dude by city");
				url = WebServiceConstants.BASE_URL
						+ WebServiceConstants.SEARCH_BY_CITY_OR_NEAR_ME;
				params.add(new BasicNameValuePair("city", searchCity.getText()
						.toString()));
			}

			// else if (filterId == 2) {
			// Log.d("", "dude by near me");
			// url = WebServiceConstants.BASE_URL
			// + WebServiceConstants.SEARCH_BY_CITY_OR_NEAR_ME;
			// params.add(new BasicNameValuePair("nearme", "yes"));
			// }

			JSONArray json = new JSONParser().makeHttpRequest(url, "POST",
					params);
			// Log.d("check user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			try {
				dialog.dismiss();
			} catch (Exception e) {

			}
			// 05-26 15:55:59.639: D/check user response(1676):
			// [{"status":"nofound"}]

			// List<UserDTO> tempList = new ArrayList<UserDTO>();
			try {
				// JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("status");
				// System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				// System.out.print("get dude response : " + jsonArray);
				// userDTOs.clear();

				for (int i = 0; i < jsonArray.length(); i++) {
					parseUserDataAndSetInSession(jsonArray.getJSONObject(i));
				}

				if (userDTOs.size() == 0 && pageNumber == 1) {
					// Toast.makeText(contex, "Dudes Not Found",
					// Toast.LENGTH_SHORT).show();
					noGuysText.setVisibility(View.VISIBLE);
				} else {
					noGuysText.setVisibility(View.GONE);
				}
				if (userDTOs.size() == 0) {
					pageNumber = pageNumber - 1;
				}

				// findDudesAdapter.notifyDataSetChanged();
				Log.d("previousCount", "previousCount=" + previousCount);
				// viewPager.setCurrentItem(previousCount);
				findDudesAdapter.notifyDataSetChanged();

			} catch (JSONException e) {
				e.printStackTrace();
				if (userDTOs.size() == 0 && pageNumber == 1) {
					// Toast.makeText(contex, "Dudes Not Found",
					// Toast.LENGTH_SHORT).show();
					noGuysText.setVisibility(View.VISIBLE);
				} else {
					noGuysText.setVisibility(View.GONE);
				}
				pageNumber = pageNumber - 1;

				// Toast.makeText(contex, "Dudes Not Found", Toast.LENGTH_SHORT)
				// .show();
			} catch (NullPointerException ew) {

			}
			handler.postDelayed(runnable, 5000);
			// Toast.makeText(contex, "Page number=" + pageNumber,
			// Toast.LENGTH_SHORT).show();
			contex.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			contex.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			checkGpsEnabledOrnot();
		}

	}

	// class GetDudesByCityAsynchTask extends AsyncTask<String, Void, JSONArray>
	// {
	// String vStatus;
	// TransparentProgressDialog dialog;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new TransparentProgressDialog(contex);
	// // dialog.setMessage("Please Wait...");
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// Log.d("", "lat long =" + mGpsTracker.getLatitude() + " , "
	// + mGpsTracker.getLongitude());
	// }
	//
	// protected JSONArray doInBackground(String... args) {
	// //
	// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&city=indore&userid=1&records=12&status=online
	// // (12:20:46 IST) Yogesh:
	// //
	// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&nearme=yes&userid=1&records=12&status=online
	//
	// //
	// http://198.12.150.189/~simssoe/krave/index.php?action=searchbysetting&userid=&page=&records=;
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// Log.d("", "page number");
	// pageNumber = pageNumber + 1;
	// Log.d("", "page number=" + pageNumber);
	// // params.add(new BasicNameValuePair("action", "login_user"));
	// /*
	// * if (comeFrom == AppConstants.FRAGMENT_GET_MATCHES) { Log.d("",
	// * "get matches url"); // params.add(new
	// * BasicNameValuePair("userid", "" // +
	// * sessionManager.getUserDetail().getUserID())); } else {
	// * params.add(new BasicNameValuePair("lat", "" +
	// * mGpsTracker.getLatitude())); params.add(new
	// * BasicNameValuePair("long", "" + mGpsTracker.getLongitude()));
	// * params.add(new BasicNameValuePair("distance", "" + 5000)); }
	// */
	// params.add(new BasicNameValuePair("userid", ""
	// + sessionManager.getUserDetail().getUserID()));
	// params.add(new BasicNameValuePair("page", "" + (pageNumber)));
	// params.add(new BasicNameValuePair("records", "" + 12));
	// params.add(new BasicNameValuePair("city", searchCity.getText()
	// .toString()));
	// params.add(new BasicNameValuePair("online", "all"));
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	// Log.d("check user response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// dialog.dismiss();
	// // 05-26 15:55:59.639: D/check user response(1676):
	// // [{"status":"nofound"}]
	// List<UserDTO> tempList = new ArrayList<UserDTO>();
	// try {
	// JSONObject mJsonObject = jsonArray.getJSONObject(0);
	// // vStatus = mJsonObject.getString("status");
	// System.out.print("" + jsonArray);
	// // if (vStatus.equals("success")) {
	// System.out.print("get dude response : " + jsonArray);
	// // userDTOs.clear();
	//
	// for (int i = 0; i < jsonArray.length(); i++) {
	// parseUserDataAndSetInSession(jsonArray.getJSONObject(i));
	// }
	// // calculatePages(userDTOs.size());
	// findDudesAdapter.notifyDataSetChanged();
	// viewPager.setCurrentItem(previousCount - 1);
	//
	// // setPager();
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// pageNumber = pageNumber - 1;
	// // Toast.makeText(contex, "Dudes Not Found", Toast.LENGTH_SHORT)
	// // .show();
	// }
	// handler.postDelayed(runnable, 0);
	// }
	// }
	//
	// class GetDudesByNearMeAsynchTask extends AsyncTask<String, Void,
	// JSONArray> {
	// String vStatus;
	// TransparentProgressDialog dialog;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new TransparentProgressDialog(contex);
	// // dialog.setMessage("Please Wait...");
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// Log.d("", "lat long =" + mGpsTracker.getLatitude() + " , "
	// + mGpsTracker.getLongitude());
	// }
	//
	// protected JSONArray doInBackground(String... args) {
	// //
	// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&city=indore&userid=1&records=12&status=online
	// // (12:20:46 IST) Yogesh:
	// //
	// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&nearme=yes&userid=1&records=12&status=online
	//
	// //
	// http://198.12.150.189/~simssoe/krave/index.php?action=searchbysetting&userid=&page=&records=;
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// Log.d("", "page number");
	// pageNumber = pageNumber + 1;
	// Log.d("", "page number=" + pageNumber);
	// // params.add(new BasicNameValuePair("action", "login_user"));
	// /*
	// * if (comeFrom == AppConstants.FRAGMENT_GET_MATCHES) { Log.d("",
	// * "get matches url"); // params.add(new
	// * BasicNameValuePair("userid", "" // +
	// * sessionManager.getUserDetail().getUserID())); } else {
	// * params.add(new BasicNameValuePair("lat", "" +
	// * mGpsTracker.getLatitude())); params.add(new
	// * BasicNameValuePair("long", "" + mGpsTracker.getLongitude()));
	// * params.add(new BasicNameValuePair("distance", "" + 5000)); }
	// */
	// params.add(new BasicNameValuePair("userid", ""
	// + sessionManager.getUserDetail().getUserID()));
	// params.add(new BasicNameValuePair("page", "" + (pageNumber)));
	// params.add(new BasicNameValuePair("records", "" + 12));
	// params.add(new BasicNameValuePair("nearme", "yes"));
	// params.add(new BasicNameValuePair("online", "all"));
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	// Log.d("check user response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// dialog.dismiss();
	// // 05-26 15:55:59.639: D/check user response(1676):
	// // [{"status":"nofound"}]
	// List<UserDTO> tempList = new ArrayList<UserDTO>();
	// try {
	// JSONObject mJsonObject = jsonArray.getJSONObject(0);
	// // vStatus = mJsonObject.getString("status");
	// System.out.print("" + jsonArray);
	// // if (vStatus.equals("success")) {
	// System.out.print("get dude response : " + jsonArray);
	// // userDTOs.clear();
	//
	// for (int i = 0; i < jsonArray.length(); i++) {
	// parseUserDataAndSetInSession(jsonArray.getJSONObject(i));
	// }
	// // calculatePages(userDTOs.size());
	// findDudesAdapter.notifyDataSetChanged();
	// viewPager.setCurrentItem(previousCount - 1);
	//
	// // setPager();
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// pageNumber = pageNumber - 1;
	// // Toast.makeText(contex, "Dudes Not Found", Toast.LENGTH_SHORT)
	// // .show();
	// }
	// handler.postDelayed(runnable, 0);
	// }
	// }

	private void parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();

		JSONObject MainObject = mJsonObject.getJSONObject("user");

		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		// System.out.println(MainObject);

		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));

		if (MainObject.getString("isonline").equals("available")) {
			userDTO.setIsOnline(AppConstants.ONLINE);

		} else if (MainObject.getString("isonline").equals("away")) {
			userDTO.setIsOnline(AppConstants.ABSENT);
		} else {
			userDTO.setIsOnline(AppConstants.OFFLINE);
		}

		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}

		userDTO.setCommonFriends(MainObject.getString("mutualfriends"));

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
		Boolean check = false;
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
		// int online = contex.getUserAvailability(userDTO.getUserID());
		// Log.d("", "is online id " + userDTO.getUserID() + " '" + online);
		// userDTO.setIsOnline("" + online);
		userDTOs.add(userDTO);
		findDudesAdapter.notifyDataSetChanged();

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		Log.d("", "on page selected" + arg0);
		currentSelectedPage = arg0;
		if (arg0 != 0) {
			previousCount = findDudesAdapter.getCount();

			// int temp=viewPager.getCurrentItem()/2;
			if (findDudesAdapter.getCount() == arg0 + 1) {
				if (WebServiceConstants.isOnline(contex))
					new GetDudesAsynchTask().execute();
			}
			// else {
			// onPage = true;
			// }
		}
		// else{
		// onPage = true;
		// }
	}
}
