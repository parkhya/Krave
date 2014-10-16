package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.krave.kraveapp.Activity_Home.GetUserList;
import com.ps.adapters.AddDudesInListImageAdapter;
import com.ps.adapters.UserListAdapter;
import com.ps.horizontal_listview.HorizontalListView;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_R_AddGuys extends Activity implements OnClickListener {
	private static Activity_R_AddGuys activityObject = null;

	public static Activity_R_AddGuys getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_R_AddGuys activityObject) {
		Activity_R_AddGuys.activityObject = activityObject;
	}

	TextView cancleButton, title, createNewList;
	ImageView createNewListImageButton;
	LinearLayout createNewListLayout, dudeProfilrPickLayout;
	ListView listView;
	List<UserListDTO> userListDTOs = new ArrayList<UserListDTO>();
	UserListAdapter userListAdapter;
	AddDudesInListImageAdapter addDudesInListImageAdapter;
	SessionManager sessionManager;
	UserListDTO userListDTO;
	HorizontalListView horizontalListView;
	public static List<UserDTO> selectedUserDTOs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_r_add_guys);
		sessionManager = new SessionManager(Activity_R_AddGuys.this);
		activityObject = this;
		setLayout();
		setTypeFace();
		if (WebServiceConstants.isOnline(Activity_R_AddGuys.this)) {
			new GetUserList().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_USER_LIST);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
	}

	private void setTypeFace() {
		createNewList = (TextView) findViewById(R.id.createNewList);
		title = (TextView) findViewById(R.id.title);

		Typeface typeface = FontStyle.getFont(Activity_R_AddGuys.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		cancleButton.setTypeface(typeface);
		createNewList.setTypeface(typeface);
		title.setTypeface(typeface);

	}

	private void setLayout() {
		cancleButton = (TextView) findViewById(R.id.cancle);
		createNewListImageButton = (ImageView) findViewById(R.id.createNewListImageButton);
		dudeProfilrPickLayout = (LinearLayout) findViewById(R.id.profilePickLayout1);
		horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);

		listView = (ListView) findViewById(R.id.list);
		createNewListLayout = (LinearLayout) findViewById(R.id.createNewListsLayout);
		userListAdapter = new UserListAdapter(userListDTOs,
				Activity_R_AddGuys.this,0);
		listView.setAdapter(userListAdapter);
		addDudesInListImageAdapter = new AddDudesInListImageAdapter(
				Activity_R_AddGuys.this, (ArrayList<UserDTO>) selectedUserDTOs);
		horizontalListView.setAdapter(addDudesInListImageAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				userListDTO = userListDTOs.get(position);
				if (WebServiceConstants.isOnline(Activity_R_AddGuys.this)) {
					new AddDudes().execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.ADD_DUDES_IN_LIST);
				}

			}
		});
		cancleButton.setOnClickListener(this);
		createNewListImageButton.setOnClickListener(this);
		createNewListLayout.setOnClickListener(this);

	}

	// @Override
	// protected void onResume() {
	// // TODO Auto-generated method stub
	// super.onResume();
	// new GetUserList().execute(WebServiceConstants.BASE_URL
	// + WebServiceConstants.GET_USER_LIST);
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancle:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.createNewListImageButton:

		case R.id.createNewListsLayout:
			Intent intent = new Intent(Activity_R_AddGuys.this,
					Activity_R_CreateNewList.class);
			startActivityForResult(intent, AppConstants.CREATE_NEW_LIST);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CREATE_NEW_LIST) {
			UserListDTO userListDTO = new UserListDTO();
			userListDTO.setListId(data.getExtras().getString("listId"));
			userListDTO.setListName(data.getExtras().getString("name"));
			userListDTOs.add(userListDTO);
			Log.d("", "new list name id="
					+ data.getExtras().getString("listId") + " , "
					+ data.getExtras().getString("name"));

			userListAdapter.notifyDataSetChanged();
		}
	}

	public class AddDudes extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_R_AddGuys.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// userid=1&title=test
			// list_id=1&adddude
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("list_id", userListDTO
					.getListId()));
			for (int i = 0; i < selectedUserDTOs.size(); i++) {
				// reqEntity.addPart("interest[" + i + "][]", new
				// StringBody(String.valueOf(i + 1)));
				params.add(new BasicNameValuePair("adddude[" + i + "][]",
						String.valueOf(selectedUserDTOs.get(i).getUserID())));

			}
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("add dude response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {

				JSONObject mainJsonObject = jsonArray.getJSONObject(0);
				vStatus = mainJsonObject.getString("status");
				if (vStatus.equals("success")) {
					Toast.makeText(Activity_R_AddGuys.this,
							"Guys successfully added", Toast.LENGTH_SHORT)
							.show();
					setResult(RESULT_OK);
					finish();
				} else {
					Toast.makeText(Activity_R_AddGuys.this, "Guys not added",
							Toast.LENGTH_SHORT).show();
				}
				// for (int i = 0; i < jsonArray.length(); i++) {
				// JSONObject mJsonObject = jsonArray.getJSONObject(i);
				// UserListDTO dto = new UserListDTO();
				// dto.setListId(mJsonObject.getString("list_id"));
				// dto.setListName(mJsonObject.getString("list_title"));
				//
				// }
				// userListAdapter.notifyDataSetChanged();

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	public class GetUserList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(Activity_R_AddGuys.this);
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
					JSONObject mJsonObject = jsonArray.getJSONObject(i);
					UserListDTO dto = new UserListDTO();
					dto.setListId(mJsonObject.getString("list_id"));
					dto.setListName(mJsonObject.getString("list_title"));
					userListDTOs.add(dto);
				}
				userListAdapter.notifyDataSetChanged();

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
