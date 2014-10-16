package com.krave.kraveapp;
//package com.ps.krave;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.Fragment;
//import android.app.ProgressDialog;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.InflateException;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.SeekBar.OnSeekBarChangeListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.model.CircleOptions;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.maps.MapController;
//import com.ps.models.InterestsDTO;
//import com.ps.models.LatLongDTO;
//import com.ps.models.UserDTO;
//import com.ps.models.UserProfileImageDTO;
//import com.ps.models.WhatAreYouDTO;
//import com.ps.services.GPSTracker;
//import com.ps.utill.AppConstants;
//import com.ps.utill.JSONParser;
//import com.ps.utill.WebServiceConstants;
//
//@SuppressLint("NewApi")
//public class FragmentMap extends Fragment {
//	private GoogleMap mMap;
//	private SeekBar mSeekBar;
//	private ProgressDialog pDialog;
//	private JSONParser jsonParser = new JSONParser();
//	private ArrayList<UserDTO> arrayList = new ArrayList<UserDTO>();
//	private int Radius = 50;
//	private int ONE_MILE = 1609;
//	private TextView mTextView;
//	private Button dudesHomeButton;
//	private Activity_Home contex;
//	private GPSTracker mGpsTracker;
//	private static View view;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		if (view != null) {
//			ViewGroup parent = (ViewGroup) view.getParent();
//			if (parent != null)
//				parent.removeView(view);
//		}
//		try {
//
//			// view = inflater.inflate(R.layout.map, container, false);
//			View view = inflater.inflate(R.layout.fragment_map, container,
//					false);
//			FrameLayout mainview = (FrameLayout) view
//					.findViewById(R.id.mainview);
//			mainview.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//
//				}
//			});
//			contex = (Activity_Home) getActivity();
//			mGpsTracker = new GPSTracker(getActivity());
//			switch (GooglePlayServicesUtil
//					.isGooglePlayServicesAvailable(getActivity())) {
//			case ConnectionResult.SERVICE_MISSING:
//			case ConnectionResult.SERVICE_DISABLED:
//			case ConnectionResult.SERVICE_INVALID:
//				Toast.makeText(getActivity(),
//						"Unable to load Google Play Services",
//						Toast.LENGTH_LONG).show();
//				break;
//			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
//				Toast.makeText(getActivity(),
//						"Please update Google Play Services", Toast.LENGTH_LONG)
//						.show();
//				break;
//			}
//
//			dudesHomeButton = (Button) view.findViewById(R.id.dudesHomeButton);
//			dudesHomeButton.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					if (arrayList.size() != 0) {
//						Activity_Home.dudeCommonList = arrayList;
//						Activity_Home.index = 0;
//						contex.COME_FROM = AppConstants.FRAGMENT_MAP;
//						contex.Attach_Fragment(AppConstants.FRAGMENT_HOME);
//					} else {
//						Toast.makeText(contex,
//								"No dudes in " + Radius + " miles radius",
//								Toast.LENGTH_SHORT).show();
//					}
//
//				}
//			});
//			mMap = ((MapFragment) getFragmentManager().findFragmentById(
//					R.id.map)).getMap();
//			mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
//			mTextView = (TextView) view.findViewById(R.id.textView);
//			mTextView.setText("" + Radius + "miles");
//			mSeekBar.setMax(50);
//			mSeekBar.setProgress(5);
//
//			// mSeekBar.setThumb(writeOnDrawable(R.drawable.check));
//
//			mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//				@Override
//				public void onStopTrackingTouch(SeekBar arg0) {
//					new GetDudesAsynchTask()
//							.execute(WebServiceConstants.BASE_URL
//									+ WebServiceConstants.GET_DUDES_FROM_PARTICULAR_RANGE);
//				}
//
//				@Override
//				public void onStartTrackingTouch(SeekBar arg0) {
//					// TODO Auto-generated method stub
//				}
//
//				@Override
//				public void onProgressChanged(SeekBar arg0, int arg1,
//						boolean arg2) {
//					Radius = 10 * arg1;
//					// Toast.makeText(getApplicationContext(), ""+arg1,
//					// Toast.LENGTH_LONG).show();
//					mTextView.setText("" + Radius + "miles");
//					// String valueString = Radius + ""; //this is the string
//					// that
//					// will be put above the slider
//					// mSeekBar.setThumb(writeOnDrawable(R.drawable.check));
//					// mSeekBar.setEnabled(false);
//
//				}
//			});
//			this.view = view;
//			// setMyLocation();
//			// new
//			// MyLoad().execute("http://parkhya.org/Android/krave_app/index.php");
//			new GetDudesAsynchTask().execute(WebServiceConstants.BASE_URL
//					+ WebServiceConstants.GET_DUDES_FROM_PARTICULAR_RANGE);
//		} catch (InflateException e) {
//			/* map is already there, just return view as it is */
//			return this.view;
//		}
//		return this.view;
//	}
//
//	void setMyLocation() {
//		mMap.addMarker(new MarkerOptions().position(
//				new LatLng(mGpsTracker.getLatitude(), mGpsTracker
//						.getLongitude())).title("My Location"));
//		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
//				mGpsTracker.getLatitude(), mGpsTracker.getLongitude()), 10));
//
//		mMap.addCircle(new CircleOptions()
//				.center(new LatLng(mGpsTracker.getLatitude(), mGpsTracker
//						.getLongitude())).radius(ONE_MILE * Radius)
//				.strokeWidth(0f).fillColor(0x99B41F24));
//
//	}
//
//	public BitmapDrawable writeOnDrawable(int drawableId) {
//
//		Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
//				.copy(Bitmap.Config.ARGB_8888, true);
//
//		// Paint paint = new Paint();
//		// paint.setStyle(Style.FILL);
//		// paint.setColor(Color.BLACK); //Change this if you want other color of
//		// text
//		// paint.setTextSize(20); //Change this if you want bigger/smaller font
//		//
//		// Canvas canvas = new Canvas(bm);
//		// canvas.drawText(radius2+" miles", 0, bm.getHeight()+50, paint);
//		// //Change the position of the text here
//
//		return new BitmapDrawable(bm);
//	}
//
//	// class MyLoad extends
//	// android.os.AsyncTask<java.lang.String, Void, org.json.JSONArray> {
//	// @Override
//	// protected void onPreExecute() {
//	// super.onPreExecute();
//	// pDialog = new ProgressDialog(getActivity());
//	// pDialog.setMessage("Please Wait....");
//	// pDialog.setIndeterminate(false);
//	// pDialog.setCanceledOnTouchOutside(false);
//	// pDialog.setCancelable(true);
//	// pDialog.show();
//	// }
//	//
//	// @Override
//	// protected JSONArray doInBackground(String... args) {
//	// List<NameValuePair> params = new ArrayList<NameValuePair>();
//	// params.add(new BasicNameValuePair("action", "showuserslatlog"));
//	// JSONArray json = jsonParser.makeHttpRequest(args[0], "GET", params);
//	// return json;
//	// }
//	//
//	// @Override
//	// protected void onPostExecute(JSONArray result) {
//	// pDialog.dismiss();
//	// super.onPostExecute(result);
//	// try {
//	// JSONObject mJsonObject;
//	// for (int i = 0; i <= result.length(); i++) {
//	// mJsonObject = result.getJSONObject(i);
//	// LatLongDTO mDto = new LatLongDTO();
//	// mDto.setUser_id(mJsonObject.getString("User_id"));
//	// mDto.setLatitude(mJsonObject.getString("latitude"));
//	// mDto.setLongitude(mJsonObject.getString("longitude"));
//	// mDto.setStreet(mJsonObject.getString("ll_street"));
//	// mDto.setCity(mJsonObject.getString("ll_city"));
//	// mDto.setState(mJsonObject.getString("ll_state"));
//	// mDto.setPostalCode(mJsonObject.getString("ll_zip"));
//	// mDto.setCountry(mJsonObject.getString("ll_country"));
//	// arrayList.add(mDto);
//	// mMap.addMarker(new MarkerOptions().position(
//	// new LatLng(Double.valueOf(mJsonObject
//	// .getString("latitude")),
//	// Double.valueOf(mJsonObject
//	// .getString("longitude")))).title(
//	// mJsonObject.getString("User_id")));
//	// }
//	//
//	// } catch (Exception e) {
//	// // TODO: handle exception
//	// }
//	// if (arrayList != null) {
//	// Toast.makeText(getActivity(), "" + arrayList.size(),
//	// Toast.LENGTH_LONG).show();
//	// }
//	//
//	// }
//	//
//	// }
//
//	class GetDudesAsynchTask extends AsyncTask<String, Void, JSONArray> {
//		String vStatus;
//		ProgressDialog dialog;
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			dialog = new ProgressDialog(contex);
//			dialog.setMessage("Please Wait...");
//			dialog.setCanceledOnTouchOutside(false);
//			dialog.show();
//		}
//
//		protected JSONArray doInBackground(String... args) {
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			// params.add(new BasicNameValuePair("action", "login_user"));
//			params.add(new BasicNameValuePair("lat", ""
//					+ mGpsTracker.getLatitude()));
//			params.add(new BasicNameValuePair("long", ""
//					+ mGpsTracker.getLongitude()));
//			params.add(new BasicNameValuePair("distance", "" + Radius));
//
//			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
//					params);
//			Log.d("check user response", "" + json);
//			return json;
//		}
//
//		@Override
//		protected void onPostExecute(JSONArray jsonArray) {
//			super.onPostExecute(jsonArray);
//			dialog.dismiss();
//			// 05-26 15:55:59.639: D/check user response(1676):
//			// [{"status":"nofound"}]
//
//			try {
//				JSONObject mJsonObject = jsonArray.getJSONObject(0);
//				// vStatus = mJsonObject.getString("status");
//				System.out.print("" + jsonArray);
//				// if (vStatus.equals("success")) {
//				System.out.print("get dude response : " + jsonArray);
//				arrayList.clear();
//				mMap.clear();
//				setMyLocation();
//				dudesHomeButton.setText(arrayList.size() + " Dudes around");
//				;
//				for (int i = 0; i < jsonArray.length(); i++) {
//					parseUserDataAndSetInSession(jsonArray.getJSONObject(i));
//				}
//
//				dudesHomeButton.setText(arrayList.size() + " Dudes around");
//				// } else if (vStatus.equals("failure")) {
//				//
//				// }
//			} catch (JSONException e) {
//				e.printStackTrace();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//	}
//
//	private void parseUserDataAndSetInSession(JSONObject mJsonObject)
//			throws JSONException {
//		UserDTO userDTO = new UserDTO();
//		LatLongDTO latLongDTO = new LatLongDTO();
//		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
//		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
//		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
//
//		JSONObject MainObject = mJsonObject.getJSONObject("user");
//		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
//		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
//		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
//		// System.out.println(MainObject);
//
//		userDTO.setUserID(MainObject.getString("user_id"));
//		userDTO.setEmail(MainObject.getString("user_email"));
//		userDTO.setFirstName(MainObject.getString("user_fname"));
//		userDTO.setLastName(MainObject.getString("user_lname"));
//		userDTO.setProfileImage(MainObject.getString("user_image"));
//		userDTO.setMobile(MainObject.getString("user_mobile"));
//		userDTO.setAboutMe(MainObject.getString("user_note"));
//		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
//
//		whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
//		whatAreYouDTO.setInches(MainObject.getString("user_inches"));
//		whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
//		whatAreYouDTO.setHight(MainObject.getString("user_height"));
//		whatAreYouDTO.setAge(MainObject.getString("user_age"));
//		whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
//		// whatAreYouDTO.setAge(MainObject.getString("user_note"));
//		whatAreYouDTO.setRelationshipStatus(MainObject
//				.getString("user_relationshipStatus"));
//		whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
//		whatAreYouDTO.setWhatDoYouKrave(MainObject
//				.getString("user_whatDoYouKrave"));
//
//		for (int i = 0; i < jsonArrayInterest.length(); i++) {
//			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
//			InterestsDTO interestsDTO = new InterestsDTO();
//			interestsDTO.setInterestId(interestJsonObject
//					.getString("intrests_id"));
//			interestsDTO.setInterestName(interestJsonObject
//					.getString("intrests_name"));
//			interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
//					+ interestJsonObject.getString("intrests_image"));
//			interestsDTOs.add(interestsDTO);
//
//		}
//		for (int i = 0; i < jsonArrayImages.length(); i++) {
//			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
//			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
//			userProfileImageDTO.setImageId(imagesJsonObject
//					.getString("image_id"));
//			userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
//					+ imagesJsonObject.getString("image_path"));
//
//			userProfileImageDTOs.add(userProfileImageDTO);
//
//		}
//		for (int i = 0; i < jsonArrayLatLong.length(); i++) {
//			JSONObject latLongJsonObject = jsonArrayLatLong.getJSONObject(i);
//			LatLongDTO mDto = new LatLongDTO();
//			mDto.setUser_id(latLongJsonObject.getString("User_id"));
//			mDto.setLatitude(latLongJsonObject.getString("latitude"));
//			mDto.setLongitude(latLongJsonObject.getString("longitude"));
//			userDTO.setStreet(latLongJsonObject.getString("ll_street"));
//			userDTO.setCity(latLongJsonObject.getString("ll_city"));
//			userDTO.setState(latLongJsonObject.getString("ll_state"));
//			userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
//			userDTO.setCountry(latLongJsonObject.getString("ll_country"));
//			latLongDTO = mDto;
//
//			mMap.addMarker(new MarkerOptions()
//					.position(
//							new LatLng(Double.valueOf(latLongJsonObject
//									.getString("latitude")), Double
//									.valueOf(latLongJsonObject
//											.getString("longitude")))).title(
//							userDTO.getFirstName() + " "
//									+ userDTO.getLastName()));
//
//		}
//		userDTO.setWhatAreYouDTO(whatAreYouDTO);
//		userDTO.setLatLongDTO(latLongDTO);
//		userDTO.setInterestList(interestsDTOs);
//		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
//
//		arrayList.add(userDTO);
//
//	}
//
//}
