package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ps.adapters.UpdateUserListAdapter;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_R_EditList extends Activity implements OnClickListener,
		OnItemClickListener {
	private static Activity_R_EditList activityObject = null;

	public static Activity_R_EditList getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_R_EditList activityObject) {
		Activity_R_EditList.activityObject = activityObject;
	}

	TextView cancleButton, okButton, title;
	EditText searchEditText;
	ListView listView;
	LinearLayout addDudeLayout, deleteDudeLayout;
	public static UserListDTO userListDTO;
	int intentValue;
	UpdateUserListAdapter updateUserListAdapter;
	public static List<UserDTO> userDTOs;
	List<UserDTO> selectedUserDTOs = new ArrayList<UserDTO>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_r_edit_list);
		intentValue = getIntent().getExtras().getInt(AppConstants.COME_FROM);
		activityObject = this;
		setLayout();
		setTypeFace();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
	}

	private void setTypeFace() {
		TextView addToList = (TextView) findViewById(R.id.addToList);
		TextView deleteFromList = (TextView) findViewById(R.id.deleteFromList);

		Typeface typeface = FontStyle.getFont(Activity_R_EditList.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		cancleButton.setTypeface(typeface);
		okButton.setTypeface(typeface);
		title.setTypeface(typeface);
		addToList.setTypeface(typeface);
		deleteFromList.setTypeface(typeface);
		searchEditText.setTypeface(typeface);

	}

	private void setLayout() {
		title = (TextView) findViewById(R.id.title);

		cancleButton = (TextView) findViewById(R.id.cancle);
		okButton = (TextView) findViewById(R.id.ok);
		searchEditText = (EditText) findViewById(R.id.search);
		listView = (ListView) findViewById(R.id.dudeList);
		addDudeLayout = (LinearLayout) findViewById(R.id.addDudeLayout);
		deleteDudeLayout = (LinearLayout) findViewById(R.id.deleteDudeLayout);

		if (intentValue == AppConstants.COME_FROM_LISTS) {
			userDTOs = userListDTO.getDudeList();
			title.setText("" + userListDTO.getListName());
			addDudeLayout.setVisibility(View.GONE);
			searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

		} else {

			title.setText("All Guys");
			deleteDudeLayout.setVisibility(View.GONE);
			/*
			 * searchEditText .setOnEditorActionListener(new
			 * OnEditorActionListener() {
			 * 
			 * @Override public boolean onEditorAction(TextView arg0, int
			 * actionId, KeyEvent arg2) { // TODO Auto-generated method stub if
			 * (actionId == EditorInfo.IME_ACTION_SEARCH) { if
			 * (searchEditText.getText().toString().trim() .length() == 0) {
			 * Toast.makeText(Activity_R_EditList.this, "Enter Search text..",
			 * Toast.LENGTH_SHORT).show(); } else { InputMethodManager imm =
			 * (InputMethodManager) Activity_R_EditList.this
			 * .getSystemService(Context.INPUT_METHOD_SERVICE);
			 * imm.hideSoftInputFromWindow( searchEditText.getWindowToken(), 0);
			 * new SearchDudeAsyncTask() .execute(WebServiceConstants.BASE_URL +
			 * WebServiceConstants.SEARCH_USER); } } return false; } });
			 */
		}
		searchEditText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				updateUserListAdapter.getFilter().filter(s.toString());
			}
		});
		updateUserListAdapter = new UpdateUserListAdapter(
				Activity_R_EditList.this, userDTOs);
		listView.setAdapter(updateUserListAdapter);
		listView.setOnItemClickListener(this);
		cancleButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
		addDudeLayout.setOnClickListener(this);
		deleteDudeLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.addDudeLayout:
			if (selectedUserDTOs.size() != 0) {
				Activity_R_AddGuys.selectedUserDTOs = selectedUserDTOs;
				Intent intent = new Intent(Activity_R_EditList.this,
						Activity_R_AddGuys.class);
				startActivityForResult(intent, AppConstants.ADD_DUDE_TO_LIST);
			} else {
				Toast.makeText(Activity_R_EditList.this,
						"Select at least one Guy", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.deleteDudeLayout:
			if (selectedUserDTOs.size() != 0) {
				if (WebServiceConstants.isOnline(Activity_R_EditList.this)) {
					new DeleteDudes().execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.DELETE_DUDES_FROM_LIST);
				}
			} else {
				Toast.makeText(Activity_R_EditList.this,
						"Select at least one Guy", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == AppConstants.ADD_DUDE_TO_LIST) {
			for (int i = 0; i < selectedUserDTOs.size(); i++) {
				selectedUserDTOs.get(i).setSelected(false);
			}
			selectedUserDTOs.clear();
			// userDTOs.removeAll(selectedUserDTOs);
			updateUserListAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		UserDTO userDTO = (UserDTO) updateUserListAdapter.getItem(position);
		if (userDTO.isSelected()) {
			selectedUserDTOs.remove(userDTO);
			userDTO.setSelected(false);
		} else {

			selectedUserDTOs.add(userDTO);
			userDTO.setSelected(true);
		}
		updateUserListAdapter.notifyDataSetChanged();
	}

	public class DeleteDudes extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_R_EditList.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// userid=1&title=test
			// list_id=1&deletedude

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("list_id", userListDTO
					.getListId()));
			for (int i = 0; i < selectedUserDTOs.size(); i++) {
				// reqEntity.addPart("interest[" + i + "][]", new
				// StringBody(String.valueOf(i + 1)));
				params.add(new BasicNameValuePair("deletedude[" + i + "][]",
						String.valueOf(selectedUserDTOs.get(i).getUserID())));

			}
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("delete dude response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			// [{"status":"success"}]
			try {

				JSONObject mainJsonObject = jsonArray.getJSONObject(0);
				vStatus = mainJsonObject.getString("status");
				if (vStatus.equals("success")) {
					Toast.makeText(Activity_R_EditList.this,
							"Guys successfully deleted", Toast.LENGTH_SHORT)
							.show();
					userDTOs.removeAll(selectedUserDTOs);
					updateUserListAdapter.notifyDataSetChanged();
					// setResult(RESULT_OK);
					// finish();
				} else {
					Toast.makeText(Activity_R_EditList.this,
							"Guys not deleted", Toast.LENGTH_SHORT).show();
				}
				// userListAdapter.notifyDataSetChanged();

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	// class SearchDudeAsyncTask extends AsyncTask<String, Void, JSONArray> {
	// String vStatus;
	// ProgressDialog dialog;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// dialog = new ProgressDialog(Activity_R_EditList.this);
	// dialog.setMessage("Please Wait...");
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// }
	//
	// protected JSONArray doInBackground(String... args) {
	// // //
	// //
	// http://parkhya.org/Android/krave_app/index.php?action=user_login&email=test2@gmail.com&password=123456
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("key", searchEditText.getText()
	// .toString()));
	//
	// JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
	// params);
	//
	// Log.d("login response", "" + json);
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray jsonArray) {
	// super.onPostExecute(jsonArray);
	// dialog.dismiss();
	// userDTOs.clear();
	// System.out.print("search user response :" + jsonArray);
	// // System.out.print("search user response :" + jsonArray.length());
	// try {
	// JSONObject mainObject = jsonArray.getJSONObject(0);
	// JSONArray userJsonArray = mainObject.getJSONArray("users");
	// for (int i = 0; i < userJsonArray.length(); i++) {
	// JSONObject Object = userJsonArray.getJSONObject(i);
	//
	// userDTOs.add(parseUserData(Object));
	// }
	// updateUserListAdapter.notifyDataSetChanged();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// }
	//
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
	// .getString("intrest_id"));
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

}
