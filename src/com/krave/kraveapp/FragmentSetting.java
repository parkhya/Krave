package com.krave.kraveapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ps.adapters.DudeDetailsInterestAdapter;
import com.ps.adapters.GridViewAdapter;
import com.ps.adapters.SettingListInterestAdapter;
import com.ps.adapters.WhatAreYouListAdapter;
import com.ps.horizontal_listview.HorizontalListView;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.InterestsDTO;
import com.ps.models.SettingDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.RangeSeekBar;
import com.ps.utill.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentSetting extends Fragment implements OnClickListener {
	SettingDTO settingDTO = new SettingDTO();
	private List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	private List<WhatAreYouDataDTO> whatAreYouDTOs = new ArrayList<WhatAreYouDataDTO>();
	private TextView minyearText, maxyearText, mailesText;
	private HorizontalListView gridView, ethinicityHorizontalListView;
	private ImageView enableNotificationImageView, scrollLeftButton,
			scrollRightButton, scrollRightButtonEthinicity,
			scrollLeftButtonEthinicity;;
	private RelativeLayout mainview;
	private LinearLayout layoutChangePassword, layoutDeleteAccount,
			layoutUpdateInfo, layoutHideAndShow, seekBarLayoutForAge;
	private SeekBar radiusSeekBar;
	private int radius = 50;
	private int MAX_VALUE = 2000;
	private int isNotificationEnable;
	SessionManager sessionManager;
	private String userId;
	Activity_Home context;
	private int minAge;
	private int maxAge;
	private RangeSeekBar<Integer> seekBar;
	private SettingListInterestAdapter settingListInterestAdapter;
	private WhatAreYouListAdapter whatAreYouListAdapter;
	private List<InterestsDTO> selectedInterest = new ArrayList<InterestsDTO>();
	private boolean deleteAcoount = true;
	// private List<WhatAreYouDataDTO> selectedWhatAreYouDTOs = new
	// ArrayList<WhatAreYouDataDTO>();
	public static boolean comeFrom = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		System.gc();
		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);
		userId = sessionManager.getUserDetail().getUserID();
		setLayout(view);
		setListener();
		setTypeFace(view);
		if (comeFrom) {
			context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_SETTING;
			context.left_button.setImageResource(R.drawable.btn_back);
			// layoutHideAndShow.setVisibility(View.INVISIBLE);
			// comeFrom = false;
		}

		new GetSetting().execute(WebServiceConstants.BASE_URL
				+ WebServiceConstants.GET_SETTING);

		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

		if (WebServiceConstants.isOnline(context) && deleteAcoount) {
			sessionManager.setSettingDetail(settingDTO);
			new UpdateSetting().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.UPDATE_SETTING);
		}
		System.gc();
		context.left_button.setImageResource(R.drawable.nav);
	}

	private void setTypeFace(View view) {
		TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8;
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		textView3 = (TextView) view.findViewById(R.id.textView3);
		textView4 = (TextView) view.findViewById(R.id.textView4);
		textView5 = (TextView) view.findViewById(R.id.textView5);
		textView6 = (TextView) view.findViewById(R.id.textView6);
		textView7 = (TextView) view.findViewById(R.id.textView7);
		textView8 = (TextView) view.findViewById(R.id.textView8);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView7.setTypeface(typeface);
		textView8.setTypeface(typeface);

		Typeface typeface1 = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		minyearText.setTypeface(typeface1);
		maxyearText.setTypeface(typeface1);
		mailesText.setTypeface(typeface1);

	}

	private void setValuesFromSession() {
		// settingDTO = sessionManager.getSettingDetail();
		minAge = Integer.valueOf(settingDTO.getMinAge());
		maxAge = Integer.valueOf(settingDTO.getMaxAge());
		radius = Integer.valueOf(settingDTO.getSearchRadius());
		isNotificationEnable = Integer.valueOf(settingDTO
				.getIsNotificationEnable());
		seekBar.setSelectedMaxValue(maxAge);
		seekBar.setSelectedMinValue(minAge);
		radiusSeekBar.setProgress((radius));
		minyearText.setText(minAge + " Year old");
		maxyearText.setText(maxAge + " Year old");
		mailesText.setText(radius + " Miles");
		if (AppConstants.NOTIFICATION_ENABLE == isNotificationEnable) {
			enableNotificationImageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.select_notification));

		} else {
			enableNotificationImageView.setImageDrawable(context.getResources()
					.getDrawable(R.drawable.unselect_notofication));

		}

		settingListInterestAdapter.notifyDataSetChanged();
		whatAreYouListAdapter.notifyDataSetChanged();

	}

	private void setListener() {
		layoutChangePassword.setOnClickListener(this);
		layoutDeleteAccount.setOnClickListener(this);
		layoutUpdateInfo.setOnClickListener(this);
		enableNotificationImageView.setOnClickListener(this);
		scrollLeftButton.setOnClickListener(this);
		scrollRightButton.setOnClickListener(this);
		scrollLeftButtonEthinicity.setOnClickListener(this);
		scrollRightButtonEthinicity.setOnClickListener(this);
		radiusSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// progress = progress + 1;
				radius = progress;
				mailesText.setText("" + radius + " Miles");
				settingDTO.setSearchRadius("" + radius);

			}
		});

	}

	private void setLayout(View view) {
		mainview = (RelativeLayout) view.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		seekBarLayoutForAge = (LinearLayout) view.findViewById(R.id.seekbar2);
		minyearText = (TextView) view.findViewById(R.id.year1);
		maxyearText = (TextView) view.findViewById(R.id.year2);
		mailesText = (TextView) view.findViewById(R.id.miles);
		radiusSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
		gridView = (HorizontalListView) view.findViewById(R.id.interestList);
		ethinicityHorizontalListView = (HorizontalListView) view
				.findViewById(R.id.ethinicity);
		scrollLeftButton = (ImageView) view.findViewById(R.id.backimage);
		scrollRightButton = (ImageView) view.findViewById(R.id.nextimage);
		scrollLeftButtonEthinicity = (ImageView) view
				.findViewById(R.id.backimageethinicity);
		scrollRightButtonEthinicity = (ImageView) view
				.findViewById(R.id.nextimageethinicity);

		layoutHideAndShow = (LinearLayout) view.findViewById(R.id.footer);
		enableNotificationImageView = (ImageView) view
				.findViewById(R.id.notification);
		layoutUpdateInfo = (LinearLayout) view.findViewById(R.id.updateInfo);
		layoutChangePassword = (LinearLayout) view
				.findViewById(R.id.changePassword);
		layoutDeleteAccount = (LinearLayout) view
				.findViewById(R.id.deleteAccount);

		radiusSeekBar.setMax(MAX_VALUE);
		setSeekBarForAge();
		settingListInterestAdapter = new SettingListInterestAdapter(context,
				(ArrayList<InterestsDTO>) interestsDTOs);
		gridView.setAdapter(settingListInterestAdapter);

		whatAreYouListAdapter = new WhatAreYouListAdapter(context,
				whatAreYouDTOs, 1);
		ethinicityHorizontalListView.setAdapter(whatAreYouListAdapter);
		ethinicityHorizontalListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {

						whatAreYouDTOs.get(pos).setSelected(
								!whatAreYouDTOs.get(pos).isSelected());

						Log.d("", "position=" + pos);

						whatAreYouListAdapter.notifyDataSetChanged();

					}

				});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				InterestsDTO interestsDTO = interestsDTOs.get(position);

				if (interestsDTO.getIsSelected()) {
					selectedInterest.remove(interestsDTO);
					interestsDTO.setIsSelected(false);
				} else {
					interestsDTO.setIsSelected(true);
					selectedInterest.add(interestsDTO);

				}
				settingListInterestAdapter.notifyDataSetChanged();
				// gridViewAdapter = new GridViewAdapter(context,
				// (ArrayList<InterestsDTO>) interestsDTOs, 1);
				// horizontalListView.setAdapter(gridViewAdapter);
			}
		});

	}

	private void setSeekBarForAge() {

		seekBar = new RangeSeekBar<Integer>(18, 75, getActivity());
		seekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {
			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Integer minValue, Integer maxValue) {
				// handle changed range values
				// Log.i(TAG, "User selected new range values: MIN=" + minValue
				// + ", MAX=" + maxValue);
				minAge = minValue;
				maxAge = maxValue;
				settingDTO.setMaxAge("" + maxAge);
				settingDTO.setMinAge("" + minAge);
				minyearText.setText(minValue + " Year old");
				maxyearText.setText(maxValue + " Year old");
			}
		});

		// add RangeSeekBar to pre-defined layout

		seekBarLayoutForAge.addView(seekBar);
		seekBar.setSelectedMinValue(10);
		seekBar.setSelectedMaxValue(60);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.changePassword:
			Intent intent = new Intent(context, Activity_ChangePassword.class);
			startActivity(intent);
			break;
		case R.id.deleteAccount:
			openDailog();
			break;
		case R.id.updateInfo:
			// if (WebServiceConstants.isOnline(context)) {
			// Intent intent1 = new Intent(context,
			// Activity_ProfileDetails.class);
			// intent1.putExtra(AppConstants.COME_FROM,
			// AppConstants.COME_FROM_UPDATE_PROFILE);
			// startActivity(intent1);
			// }
			context.Attach_Fragment(AppConstants.FRAGMENT_UPDATE_PROFILE);
			break;
		case R.id.notification:
			if (AppConstants.NOTIFICATION_ENABLE == isNotificationEnable) {
				enableNotificationImageView.setImageDrawable(context
						.getResources().getDrawable(
								R.drawable.unselect_notofication));
				isNotificationEnable = AppConstants.NOTIFICATION_DISABLE;

			} else {
				enableNotificationImageView.setImageDrawable(context
						.getResources().getDrawable(
								R.drawable.select_notification));
				isNotificationEnable = AppConstants.NOTIFICATION_ENABLE;

			}
			settingDTO.setIsNotificationEnable("" + isNotificationEnable);
			break;
		case R.id.backimage:
			int index = gridView.Getposition();

			gridView.scrollTo(index + 50);
			break;
		case R.id.nextimage:
			int index1 = gridView.Getposition();

			gridView.scrollTo(index1 - 50);

			break;
		case R.id.backimageethinicity:
			int index2 = ethinicityHorizontalListView.Getposition();

			ethinicityHorizontalListView.scrollTo(index2 + 50);
			break;
		case R.id.nextimageethinicity:
			int index3 = ethinicityHorizontalListView.Getposition();

			ethinicityHorizontalListView.scrollTo(index3 - 50);
			break;
		default:
			break;
		}

	}

	class GetSetting extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			// http://parkhya.org/Android/krave_app/index.php?action=showsetting&userid=1
			// http://parkhya.org/Android/krave_app/index.php?action=searchsettingEdit&userid=1&min_val=25&max_val=45&radius=150&interest=1,2,3&notification1
			params.add(new BasicNameValuePair("userid", "" + userId));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("Get setting response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			// [{"s_no":"1","user_id":"1","min_value":"25","max_value":"45","radius":"150","interest":"1,2,3","notification":"0"}]
			try {
				List<String> interestsStrings = new ArrayList<String>();
				List<String> wruStrings = new ArrayList<String>();
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("status");
				// System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("get setting response" + jsonArray);
				JSONArray jsonArray1 = mJsonObject.getJSONArray("setting");
				JSONArray jsonArray2 = mJsonObject.getJSONArray("intarest");
				JSONArray jsonArray3 = mJsonObject.getJSONArray("wru");

				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject mmJsonObject = jsonArray1.getJSONObject(i);
					settingDTO = new SettingDTO();
					settingDTO.setUserID(mmJsonObject.getString("user_id"));
					settingDTO.setMinAge(mmJsonObject.getString("min_value"));
					settingDTO.setMaxAge(mmJsonObject.getString("max_value"));
					settingDTO
							.setSearchRadius(mmJsonObject.getString("radius"));
					settingDTO.setIsNotificationEnable(mmJsonObject
							.getString("notification"));
					String interest[] = mmJsonObject.getString("interest")
							.split(",");

					for (int k = 0; k < interest.length; k++) {

						interestsStrings.add(interest[k]);
					}
					String wru[] = mmJsonObject.getString("ethencity").split(
							",");

					for (int k = 0; k < wru.length; k++) {

						wruStrings.add(wru[k]);
					}

				}

				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject mmJsonObject = jsonArray2.getJSONObject(i);
					InterestsDTO interestsDTO = new InterestsDTO();
					interestsDTO.setInterestId(mmJsonObject
							.getString("intrests_id"));
					interestsDTO.setInterestName(mmJsonObject
							.getString("intrests_name"));
					interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
							+ mmJsonObject.getString("intrests_image"));
					interestsDTO.setIsSelected(false);
					String temp = mmJsonObject.getString("intrests_id");
					Log.d("", "json interest id=" + temp);

					for (int k = 0; k < interestsStrings.size(); k++) {
						// InterestsDTO dto =
						// userDTO.getInterestList().get(k);
						Log.d("", "dto interest id=" + interestsStrings.get(k));
						if (interestsStrings.get(k).equals(temp)) {
							Log.d("", "interest selection =true " + temp);
							interestsDTO.setIsSelected(true);
							selectedInterest.add(interestsDTO);
						}

					}

					interestsDTOs.add(interestsDTO);

				}

				for (int i = 0; i < jsonArray3.length(); i++) {
					JSONObject object = jsonArray3.getJSONObject(i);
					WhatAreYouDataDTO whatAreYouDataDTO = new WhatAreYouDataDTO();
					whatAreYouDataDTO.setId(object.getString("id"));
					whatAreYouDataDTO.setName(object.getString("name"));
					whatAreYouDataDTO.setSelected(false);
					String temp = object.getString("id");

					for (int k = 0; k < wruStrings.size(); k++) {

						if (wruStrings.get(k).equals(temp)) {
							Log.d("", "interest selection =true " + temp);
							whatAreYouDataDTO.setSelected(true);

						}

					}
					whatAreYouDTOs.add(whatAreYouDataDTO);

				}

				settingDTO.setSelectedInterestList(selectedInterest);
				settingDTO.setWhatAreYouDataDTOs(whatAreYouDTOs);
				Log.d("", "interestsDTOs=" + interestsDTOs.size());
				setValuesFromSession();

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public void openDailog() {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_logout);
		// Do you realy want to delete your account ?
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText("Do you really want to delete your account ?");
		Typeface typeface = FontStyle.getFont(context,
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
				Intent intent = new Intent(context, GpsService.class);
				context.stopService(intent);
				Intent inten1 = new Intent(context, ChatService.class);
				context.stopService(inten1);
				new DeleteAccountAsyncTask()
						.execute(WebServiceConstants.BASE_URL
								+ WebServiceConstants.DELETE_ACCOUNT);

			}
		});
		dialog.show();
	}

	class DeleteAccountAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", "" + userId));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("DELETE ACCOUNT RESPONSE", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {
				List<String> interestsStrings = new ArrayList<String>();
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					deleteAcoount = false;
					new XmppDeleteUserAsynchTask().execute(
							WebServiceConstants.XMPP_USER_REGISTRATION, userId);
					sessionManager.Logout();
					context.finish();
					Intent intent = new Intent(context, Activity_Login.class);
					context.startActivity(intent);
					Toast.makeText(context, "Account successfully deleted",
							Toast.LENGTH_SHORT).show();
				} else if (vStatus.equals("failure")) {
					Intent intent = new Intent(context, GpsService.class);
					context.startService(intent);
					Intent inten1 = new Intent(context, ChatService.class);
					context.startService(inten1);
					Toast.makeText(context, "Fail to delete account",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Intent intent = new Intent(context, GpsService.class);
				context.startService(intent);
			}

		}
	}

	class XmppDeleteUserAsynchTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected String doInBackground(String... args) {
			Log.d("", "xmpp registration start");
			try {

			//	http://54.241.85.74:9090/plugins/userService/userservice?type=delete_roster&secret=7bDFD4TP&username=555&item_jid=ypawar979@gmail.com
				
				String posturl = "http://54.241.85.74:9090/plugins/userService/userservice?type=delete_roster&secret=7bDFD4TP&username="
						+ args[1];

				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(posturl);
				HttpResponse response = httpclient.execute(httpget);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String result = sb.toString();
				Log.v("My Response :: ", result);

				// HttpClient client = new DefaultHttpClient();
				// HttpPost post = new HttpPost(args[0]);
				//
				// MultipartEntity reqEntity = new MultipartEntity(
				// HttpMultipartMode.BROWSER_COMPATIBLE);
				// reqEntity.addPart("type", new StringBody("add"));
				// reqEntity.addPart("secret", new StringBody("7bDFD4TP"));
				// reqEntity.addPart("username",
				// new StringBody(userDTO.getUserID()));
				// reqEntity.addPart("password",
				// new StringBody(userDTO.getPassword()));
				// reqEntity.addPart("name", new
				// StringBody(userDTO.getFirstName()
				// + " " + userDTO.getLastName()));
				// reqEntity.addPart("email", new
				// StringBody(userDTO.getEmail()));
				//
				// post.setEntity(reqEntity);
				// HttpResponse response = client.execute(post);
				// Log.d("", "" + response.toString());
				return null;
			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				Log.v("Message...1", e.getMessage());
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Log.v("Message...2", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("Message...3", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			Log.d("", "xmpp registration successfull");

		}
	}

	public class UpdateSetting extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("update setting request", "update setting request");
			// dialog = new ProgressDialog(Activity_Home.this);
			// dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			// SettingDTO settingDTO = FragmentSetting.settingDTO;
			String interest = "";
			for (int i = 0; i < settingDTO.getSelectedInterestList().size(); i++) {
				interest = interest
						+ settingDTO.getSelectedInterestList().get(i)
								.getInterestId() + ",";
			}

			String ethnicity = "";
			for (int i = 0; i < settingDTO.getWhatAreYouDataDTOs().size(); i++) {
				if (settingDTO.getWhatAreYouDataDTOs().get(i).isSelected()) {
					ethnicity = ethnicity
							+ settingDTO.getWhatAreYouDataDTOs().get(i).getId()
							+ ",";
				}
			}
			Log.d("", "interest=" + interest);
			Log.d("", "ethnicity=" + ethnicity);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			// http://parkhya.org/Android/krave_app/index.php?action=showsetting&userid=1

			params.add(new BasicNameValuePair("userid", "" + userId));
			params.add(new BasicNameValuePair("min_val", ""
					+ settingDTO.getMinAge()));
			params.add(new BasicNameValuePair("max_val", ""
					+ settingDTO.getMaxAge()));
			params.add(new BasicNameValuePair("radius", ""
					+ settingDTO.getSearchRadius()));
			params.add(new BasicNameValuePair("interest", "" + interest));
			params.add(new BasicNameValuePair("ethencity", "" + ethnicity));
			Log.d("", "notification" + settingDTO.getIsNotificationEnable());
			params.add(new BasicNameValuePair("notification", ""
					+ settingDTO.getIsNotificationEnable()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("update setting response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("update setting response" + jsonArray);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
