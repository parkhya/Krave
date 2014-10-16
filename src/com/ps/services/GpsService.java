package com.ps.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.krave.kraveapp.Activity_ChangePassword;
import com.krave.kraveapp.Activity_Gps_Alert;
import com.krave.kraveapp.Activity_Home;
import com.ps.utill.AppConstants;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

//import com.ps.newappexecutive.SessionManagerExecutive;

public class GpsService extends Service implements LocationListener {
	// SessionManagerExecutive manager;
	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";
	HashMap<String, String> data = new HashMap<String, String>();
	public static String gcmId = "";
	public static int notificationCount = 0;
	public static int unreadMsgCount = 0;
	LocationManager MLocationManager;
	private GPSTracker gpsTracker;
	private JSONParser jsonParser = new JSONParser();
	private SessionManager sessionManager;
	private Context mContext;
	// flag for GPS status
	boolean isGPSEnabled = false;
	// flag for network status
	boolean isNetworkEnabled = false;
	// flag for GPS status
	boolean canGetLocation = false;

	Location MLocation; // location
	private double latitude; // latitude
	private double longitude; // longitude
	private static double vlat; // latitude
	private static double vlong; // longitude
	private static String userID;
	private static String repId;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1 * 60000; // 1 minutepublic
																// static String
																// BROADCAST_ACTION
																// =
																// "com.unitedcoders.android.broadcasttest.SHOWTOAST";
	private String street = "";
	private String city = "";
	private String state = "";
	private String zip = "";
	private String country = "";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {

		gpsTracker = new GPSTracker(getApplicationContext());
		sessionManager = new SessionManager(getApplicationContext());

		userID = sessionManager.getUserDetail().getUserID();

		// Toast.makeText(GpsService.this, "Service Created", 1).show();
		Log.d("gps service", "Service Created");
		super.onCreate();
		MLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// getting GPS status
		isGPSEnabled = MLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// getting network status
		isNetworkEnabled = MLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		MLocation = MLocationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		MLocation = MLocationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (!isGPSEnabled && !isNetworkEnabled) {
			// no network provider is enabled
			// Toast.makeText(getApplicationContext(),
			// "please enabled network and gps", Toast.LENGTH_SHORT)
			// .show();
			gpsAlert();

		} else {
			this.canGetLocation = true;
			if (isNetworkEnabled) {
				MLocationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("Network", "Network");
				if (MLocationManager != null) {
					// MLocation = MLocationManager
					// .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (MLocation != null) {
						// latitude = MLocation.getLatitude();
						// longitude = MLocation.getLongitude();
						latitude = gpsTracker.getLatitude();
						longitude = gpsTracker.getLongitude();
					}
				}
			}
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (MLocation == null) {
					MLocationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("GPS Enabled", "GPS Enabled");
					if (MLocationManager != null) {
						// MLocation = MLocationManager
						// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (MLocation != null) {
							// latitude = MLocation.getLatitude();
							// longitude = MLocation.getLongitude();
							latitude = gpsTracker.getLatitude();
							longitude = gpsTracker.getLongitude();
						}
					}
				}
			}
		}

	}

	private void gpsAlert() {
		// final AlertDialog.Builder builder = new AlertDialog.Builder(
		// getApplicationContext());
		// builder.setMessage(
		// "Yourr GPS seems to be disabled, do you want to enable it?")
		// .setCancelable(false)
		// .setPositiveButton("Yes",
		// new DialogInterface.OnClickListener() {
		// public void onClick(
		// @SuppressWarnings("unused") final DialogInterface dialog,
		// @SuppressWarnings("unused") final int id) {
		//
		// Intent intent = new Intent(
		// Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		//
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent);
		// sendBroadcast(getApplicationContext());
		// stopSelf();
		//
		// }
		// })
		// .setNegativeButton("No", new DialogInterface.OnClickListener() {
		// public void onClick(final DialogInterface dialog,
		// @SuppressWarnings("unused") final int id) {
		// dialog.cancel();
		// sendBroadcast(getApplicationContext());
		// stopSelf();
		//
		// }
		// });
		// final AlertDialog alert = builder.create();
		// alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// alert.show();
		if (ChatService.APPVISIBLEORNOT
				&& ChatService.ONCHAT == AppConstants.FRAGMENT_HOME) {
			Intent intent = new Intent(getBaseContext(),
					Activity_Gps_Alert.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(intent);
		}

	}

	public void sendBroadcast(Context context) {

		Intent broadcast = new Intent();

		broadcast.setAction("close");
		context.sendBroadcast(broadcast);
	}

	/*
	 * isOnline - Check if there is a NetworkConnection
	 * 
	 * @return boolean
	 */
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Log.d("gps service", "Service Started");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Log.d("gps service", "Service Destory");
	}

	@Override
	public void onLocationChanged(Location location) {

		Log.d("rep_cureent_Location_service", "locationchanged");

		gpsTracker = new GPSTracker(getApplicationContext());

		latitude = gpsTracker.getLatitude();
		longitude = gpsTracker.getLongitude();
		Log.d("location",
				"" + gpsTracker.getLatitude() + " , "
						+ gpsTracker.getLongitude());

		if (isOnline()) {
			// Intent intent1 = new Intent(getApplicationContext(),
			// ChatService.class);
			// startService(intent1);
			Log.d("", "network Enabled" + isOnline());
			getCompleteAddressString(latitude, longitude);
			new SendLatLong().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.SEND_USER_LAT_LONG);
		} else {
			Log.d("", "network Enabled" + isOnline());

		}

		// }
	}

	@Override
	public void onProviderDisabled(String provider) {
		if (provider.equals("gps")) {
			gpsAlert();
		}

	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d("onProviderEnabled", "onProviderEnabled=" + provider);
		if (provider.equals("gps")) {
			if (ChatService.ONCHAT == AppConstants.FRAGMENT_HOME) {
				Log.d("onProviderEnabled", "APPVISIBLEORNOT");
				sendBroadcastForrefresh(getApplicationContext());
			}
		}
	}

	public void sendBroadcastForrefresh(Context context) {

		Intent broadcast = new Intent();

		broadcast.setAction("refresh");
		context.sendBroadcast(broadcast);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public double getLatitude() {
		if (MLocation != null) {
			latitude = MLocation.getLatitude(); 
		}

		// return latitude
		return latitude;
	}

	public double getLongitude() {
		if (MLocation != null) {
			longitude = MLocation.getLongitude();
		}

		// return longitude
		return longitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
		String strAdd = "";

		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,
					LONGITUDE, 1);

			if (addresses.size() > 0) {
				Address obj = addresses.get(0);
				street = obj.getAddressLine(0);

				country = obj.getCountryName();

				state = obj.getAdminArea();
				zip = obj.getPostalCode();

				city = obj.getSubAdminArea();

				if (street == null) {
					street = "";
				}
				if (city == null) {
					city = "";
				}
				if (state == null) {
					state = "";
				}
				if (zip == null) {
					zip = "";
				}
				if (country == null) {
					country = "";
				}

			} else {
				Log.w("My Current loction address", "No Address returned!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("My Current loction address", "Canont get Address!");
		}
		return strAdd;
	}

	public class SendLatLong extends AsyncTask<String, Void, JSONArray> {
		@Override
		protected void onPreExecute() {

			// Toast.makeText(getApplicationContext(), "onPreExecute",
			// 1).show();
			Log.d("gps service", "onPreExecute");
			super.onPreExecute();
		}

		@Override
		protected JSONArray doInBackground(String... args) {
			// &userid=&lat=&log=&street=&city=&state=&zip=&country=&GSMid=
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("userid", userID));

			pairs.add(new BasicNameValuePair("lat", "" + latitude));
			pairs.add(new BasicNameValuePair("log", "" + longitude));
			pairs.add(new BasicNameValuePair("street", "" + street));
			pairs.add(new BasicNameValuePair("city", "" + city));
			pairs.add(new BasicNameValuePair("state", "" + state));
			pairs.add(new BasicNameValuePair("zip", "" + zip));
			pairs.add(new BasicNameValuePair("country", "" + country));
			pairs.add(new BasicNameValuePair("GSMid", "" + gcmId));
			pairs.add(new BasicNameValuePair("devicetype", "android"));
			JSONArray json = jsonParser.makeHttpRequest(args[0], "POST", pairs);
			Log.d("rep_cureent_Location_service", "requestSend");
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			super.onPostExecute(result);
			String vstatus = "";
			Log.d("result", "result=" + result);
			Log.d("gps service", "onPostExecute");
			try {
				JSONObject jsonObject = result.getJSONObject(0);
				vstatus = jsonObject.getString("status");
				if (vstatus.equals("success")) {
					JSONObject data = jsonObject.getJSONObject("data");
					data.getString("User_id");
					notificationCount = Integer.valueOf(data
							.getString("request"));
					unreadMsgCount = Integer.valueOf(data
							.getString("unread_msg"));

					if (ChatService.APPVISIBLEORNOT) {
						Log.d("", "brodacst notification");
						Intent broadcast = new Intent();
						broadcast.putExtra("come", "notification");
						broadcast.setAction(BROADCAST_ACTION);
						sendBroadcast(broadcast);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
