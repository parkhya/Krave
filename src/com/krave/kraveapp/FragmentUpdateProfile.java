package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ps.adapters.UpdateProfileAdapter;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.UserDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class FragmentUpdateProfile extends Fragment implements OnClickListener {

	ViewPager viewPager;
	public UpdateProfileAdapter updateProfileAdapter;
	Activity_Home context;
	int integerArrayForPagerView[] = { R.layout.activity_update_registration,
			R.layout.update_profile_details_1,
			R.layout.update_profile_details_2 };
	List<WhatAreYouDataDTO> whatAreYouDTOs;
	View viewLayout;
	SessionManager sessionManager;
	UserDTO userDTO;
	private Typeface typefaceHeader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		viewLayout = inflater.inflate(R.layout.activity_profile_details,
				container, false);
		System.gc();
		context = (Activity_Home) getActivity();

		typefaceHeader = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);

		context.headerIcon.setVisibility(View.GONE);
		context.title.setVisibility(View.VISIBLE);
		context.title.setText("UPDATE INFO");
		context.title.setTypeface(typefaceHeader);

		context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_UPDATE_PROFILE;
		context.left_button.setImageResource(R.drawable.btn_back);

		sessionManager = new SessionManager(context);
		userDTO = sessionManager.getUserDetail();
		if (WebServiceConstants.isOnline(context)) {
			new GetAllDataAsynchTask().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_ALL_INTEREST_AND_WHAT_ARE_YOU);
		}
		return viewLayout;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		context.headerIcon.setVisibility(View.VISIBLE);
		context.title.setVisibility(View.GONE);
		context.setLeftDrawer = 0;
		context.left_button.setImageResource(R.drawable.nav);
		System.gc();
	}

	private void setLayout(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		updateProfileAdapter = new UpdateProfileAdapter(context,
				integerArrayForPagerView, whatAreYouDTOs);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					context.title.setText("UPDATE INFO");
					break;
				case 1:
					context.title.setText("WHAT ARE YOU?");
					break;
				case 2:
					context.title.setText("WHAT ARE YOU?");
					break;
				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		viewPager.setAdapter(updateProfileAdapter);

	}

	class GetAllDataAsynchTask extends AsyncTask<String, Void, JSONArray> {
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

			params.add(new BasicNameValuePair("", ""));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			// Log.d("login response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();

				whatAreYouDTOs = new ArrayList<WhatAreYouDataDTO>();
				System.out.print("" + jsonArray);

				JSONObject mJsonObject1 = jsonArray.getJSONObject(0);

				JSONObject mJsonObject2 = jsonArray.getJSONObject(1);
				System.out.print("mJsonObject1" + mJsonObject1);
				System.out.print("mJsonObject1" + mJsonObject1);

				// vStatus = mJsonObject.getString("status");
				JSONArray jsonArray1 = mJsonObject1.getJSONArray("Intrast");
				JSONArray jsonArray2 = mJsonObject2.getJSONArray("wru");
				System.out.print("jsonArray1" + jsonArray1);
				System.out.print("jsonArray2" + jsonArray2);

				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject mJsonObject = jsonArray2.getJSONObject(i);
					WhatAreYouDataDTO whatAreYouDataDTO = new WhatAreYouDataDTO();
					whatAreYouDataDTO.setId(mJsonObject.getString("id"));
					whatAreYouDataDTO.setName(mJsonObject.getString("name"));
					whatAreYouDataDTO.setSelected(false);
					if (userDTO.getWhatAreYouDTO().getWhatAreYou()
							.equals(mJsonObject.getString("id"))) {
						whatAreYouDataDTO.setSelected(true);

					}
					whatAreYouDTOs.add(whatAreYouDataDTO);
				}

				setLayout(viewLayout);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public Dialog onCreateDialog(int id) {
		return new DatePickerDialog(context,
				updateProfileAdapter.datePickerListener,
				updateProfileAdapter.year, updateProfileAdapter.month,
				updateProfileAdapter.day);
	}

	public void setBackButtonFunction() {

		if (viewPager.getCurrentItem() == 0) {
			updateProfileAdapter.updateProfile();

		} else if (viewPager.getCurrentItem() == 1) {
			viewPager.setCurrentItem(0);
		} else if (viewPager.getCurrentItem() == 2) {
			viewPager.setCurrentItem(1);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
