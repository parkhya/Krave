package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ps.adapters.DailyKraveAdapter;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.DailyKraveDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.JSONParser;
import com.ps.utill.WebServiceConstants;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("NewApi")
public class FragmentDailyKrave extends Fragment {
	private ListView mListView;
	private static ArrayList<DailyKraveDTO> arrayList = new ArrayList<DailyKraveDTO>();
	private DailyKraveAdapter mAdapter;
	public static DailyKraveDTO mDailyKraveDTO;
	private Activity_Home context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.gc();
		View view = inflater.inflate(R.layout.fragment_daily_krave, container,
				false);
		context = (Activity_Home) getActivity();
		init(view);
		if (arrayList.size() == 0) {
			if (WebServiceConstants.isOnline(context)) {
				new GetDailyKraveAsynchTask()
						.execute(WebServiceConstants.BASE_URL
								+ WebServiceConstants.GET_DAILY_KRAVE);
			}
		} else {
			mAdapter.notifyDataSetChanged();
		}
		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		System.gc();
	}

	private void init(View view) {
		LinearLayout mainview = (LinearLayout) view.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		mListView = (ListView) view.findViewById(R.id.listView);

		// DailyKraveDTO mDto1 = new DailyKraveDTO();
		// mDto1.setId("1");
		// mDto1.setHeadline("Love u Janu");
		// mDto1.setName("Devendra");
		// mDto1.setSubtitle("Please Kiss me");
		// mDto1.setImagepath("Image path");
		// arrayList.add(mDto1);
		//
		// DailyKraveDTO mDto2 = new DailyKraveDTO();
		// mDto2.setId("1");
		// mDto2.setHeadline("Love u Janu");
		// mDto2.setName("Devendra");
		// mDto2.setSubtitle("Please Kiss me");
		// mDto2.setImagepath("Image path");
		// arrayList.add(mDto2);
		//
		// DailyKraveDTO mDto3 = new DailyKraveDTO();
		// mDto3.setId("1");
		// mDto3.setHeadline("Love u Janu");
		// mDto3.setName("Devendra");
		// mDto3.setSubtitle("Please Kiss me");
		// mDto3.setImagepath("Image path");
		// arrayList.add(mDto3);
		//
		// DailyKraveDTO mDto4 = new DailyKraveDTO();
		// mDto4.setId("1");
		// mDto4.setHeadline("Love u Janu");
		// mDto4.setName("Devendra");
		// mDto4.setSubtitle("Please Kiss me");
		// mDto4.setImagepath("Image path");
		// arrayList.add(mDto4);
		//
		// DailyKraveDTO mDto5 = new DailyKraveDTO();
		// mDto5.setId("1");
		// mDto5.setHeadline("Love u Janu");
		// mDto5.setName("Devendra");
		// mDto5.setSubtitle("Please Kiss me");
		// mDto5.setImagepath("Image path");
		// arrayList.add(mDto5);

		mAdapter = new DailyKraveAdapter(getActivity(), arrayList);
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				FragmentDetailDailyKrave.dailyKraveDTO = arrayList.get(arg2);
				context.Attach_Fragment(AppConstants.FRAGMENT_DAILY_KRAVE_DETAIL);

			}

		});

	}

	class GetDailyKraveAsynchTask extends AsyncTask<String, Void, JSONArray> {
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
			Log.d("daily dude response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			// {"news_id":"5","news_date":"2014-05-18 00:00:00","news_img":"krave_image/140100136201850422625_icocal.png","news_title":"test title5","news_header":"test header5","news_details":"test details5"}
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);

				// vStatus = mJsonObject.getString("status");
				arrayList.clear();
				// if (vStatus.equals("success")) {
				JSONArray jsonArray2 = mJsonObject.getJSONArray("data");

				System.out.print("daily dude response : " + jsonArray);

				for (int i = 0; i < jsonArray2.length(); i++) {
					DailyKraveDTO dailyKraveDTO = new DailyKraveDTO();
					JSONObject MainObject = jsonArray2.getJSONObject(i);
					dailyKraveDTO.setId(MainObject.getString("news_id"));
					dailyKraveDTO.setName(MainObject.getString("news_title"));
					dailyKraveDTO.setHeadline(MainObject
							.getString("news_header"));
					dailyKraveDTO.setSubtitle(MainObject
							.getString("news_details"));
					dailyKraveDTO
							.setImagepath(AppConstants.BASE_IMAGE_PATH_FOR_DAILY_KRAVE
									+ MainObject.getString("news_img"));

					// }

					// } else {
					// Toast.makeText(context, "No daily krave found",
					// Toast.LENGTH_SHORT).show();
					arrayList.add(dailyKraveDTO);

					Log.d("", "arrayList size=" + dailyKraveDTO.getImagepath());
				}
				if (arrayList.size() == 0) {
					Toast.makeText(context, "No daily krave found",
							Toast.LENGTH_SHORT).show();
				}
				Log.d("", "arrayList size=" + arrayList.size());
				mAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();

			}
			// 05-27 01:21:30.899: D/(1425): arrayList
			// size=http://www.parkhya.org/Android/krave_app/krave_image/140100136201850422625_icocal.png

		}
	}

}
