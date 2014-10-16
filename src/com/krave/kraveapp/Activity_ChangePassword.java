package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.SettingDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ChangePassword extends Activity {
	private static Activity_ChangePassword activityObject = null;

	public static Activity_ChangePassword getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_ChangePassword activityObject) {
		Activity_ChangePassword.activityObject = activityObject;
	}

	private EditText oldPassword, newPassword, confairmNewPassword;
	private ImageButton submitButton;
	private Button backButton;
	private SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activityObject = this;
		setContentView(R.layout.activity_change_password);
		sessionManager = new SessionManager(Activity_ChangePassword.this);
		setLayout();
		setTypeFace();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	private void setTypeFace() {
		TextView textView1;
		textView1 = (TextView) findViewById(R.id.titleTextView);

		Typeface typeface = FontStyle.getFont(Activity_ChangePassword.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		textView1.setTypeface(typeface);

		oldPassword.setTypeface(typeface);
		newPassword.setTypeface(typeface);
		confairmNewPassword.setTypeface(typeface);

	}

	private void setLayout() {
		oldPassword = (EditText) findViewById(R.id.oldPassword);
		newPassword = (EditText) findViewById(R.id.newpassword);
		confairmNewPassword = (EditText) findViewById(R.id.confirmPassword);
		submitButton = (ImageButton) findViewById(R.id.done_button);
		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (0 == oldPassword.getText().toString().length()) {
					Toast.makeText(Activity_ChangePassword.this,
							"Enter old password", Toast.LENGTH_SHORT).show();
				} else if (0 == newPassword.getText().toString().length()) {
					Toast.makeText(Activity_ChangePassword.this,
							"Enter new password", Toast.LENGTH_SHORT).show();
				} else if (newPassword.getText().toString().length() < 6) {
					Toast.makeText(Activity_ChangePassword.this,
							"New password must be 6 character long",
							Toast.LENGTH_SHORT).show();
				} else if (newPassword.getText().toString()
						.equals(confairmNewPassword.getText().toString())) {
					new ChangePasswordAsyncTask()
							.execute(WebServiceConstants.BASE_URL
									+ WebServiceConstants.CHANGE_PASSWORD);
				} else {
					Toast.makeText(
							Activity_ChangePassword.this,
							"New password and confirm new password are not same",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	class ChangePasswordAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(Activity_ChangePassword.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// parkhya.org/Android/krave_app/index.php?action=changepassword&oldpassword=123&newpassword=1234&userid=1
			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("oldpassword", oldPassword
					.getText().toString()));
			params.add(new BasicNameValuePair("newpassword", newPassword
					.getText().toString()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("change password response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				// System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					Toast.makeText(Activity_ChangePassword.this,
							"Password changed successfully", Toast.LENGTH_SHORT)
							.show();
					if (sessionManager.isRemember()) {
						sessionManager.setRememberMe(sessionManager.getEmail(),
								newPassword.getText().toString());
					}
					finish();
				} else {
					Toast.makeText(Activity_ChangePassword.this,
							"worng old password", Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
}