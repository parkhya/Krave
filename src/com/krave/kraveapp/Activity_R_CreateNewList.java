package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.UserListDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_R_CreateNewList extends Activity implements
		OnClickListener {
	private static Activity_R_CreateNewList activityObject = null;

	public static Activity_R_CreateNewList getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_R_CreateNewList activityObject) {
		Activity_R_CreateNewList.activityObject = activityObject;
	}

	TextView cancleButton, okButton, title;
	EditText listName;
	SessionManager sessionManager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_r_create_new_list);
		sessionManager = new SessionManager(Activity_R_CreateNewList.this);
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

		Typeface typeface = FontStyle.getFont(Activity_R_CreateNewList.this,
				AppConstants.HelveticaNeueLTStd_Roman);
		cancleButton.setTypeface(typeface);
		okButton.setTypeface(typeface);
		title.setTypeface(typeface);
		listName.setTypeface(typeface);

	}

	private void setLayout() {
		cancleButton = (TextView) findViewById(R.id.cancleButton);
		okButton = (TextView) findViewById(R.id.okButton);
		title = (TextView) findViewById(R.id.title);
		listName = (EditText) findViewById(R.id.newListFields);

		cancleButton.setOnClickListener(this);
		okButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancleButton:
			finish();
			break;
		case R.id.okButton:
			if (listName.getText().toString().trim().length() != 0) {
				if (WebServiceConstants.isOnline(Activity_R_CreateNewList.this)) {
					new CreateNewList().execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.CREATE_NEW_LIST);
				}
			} else {
				Toast.makeText(Activity_R_CreateNewList.this,
						"Enter list name", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

	}

	public class CreateNewList extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(
					Activity_R_CreateNewList.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// userid=1&title=test
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("title", listName.getText()
					.toString()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("create new list response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			// [{"list_id":"5","list_title":"test23","user_id":"6"}]
			// 06-03 17:21:52.170: D/create new list response(11423):
			// [{"list_id":"13","user_id":"6","list_title":"dddddddd"}]

			try {

				JSONObject mainJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mainJsonObject.getString("success");
				// if (vStatus.equals("success")) {

				String id = mainJsonObject.getString("list_id");
				String name = mainJsonObject.getString("list_title");
				Intent intent = new Intent();
				intent.putExtra("listId", id);
				intent.putExtra("name", name);
				Log.d("", "new list created=" + id + "...... " + name);
				setResult(RESULT_OK, intent);
				// } else {
				// setResult(RESULT_CANCELED);
				// }
				finish();
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
