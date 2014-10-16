package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.JsonArray;
import com.ps.loader.TransparentProgressDialog;
import com.ps.utill.JSONParser;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_ForgotPassword extends Activity implements
		OnClickListener {

	private EditText email;
	private Button sendButton, cancleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_forgot_password);
		setLayout();

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//EasyTracker.getInstance(this).activityStart(this);
		EasyTracker.getInstance(this).set(Fields.SCREEN_NAME, "Forgot Password Screen");
		EasyTracker.getInstance(this).send( MapBuilder.createAppView().build() );
	}
	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	}

	private void setLayout() {
		email = (EditText) findViewById(R.id.email);
		sendButton = (Button) findViewById(R.id.send);
		cancleButton = (Button) findViewById(R.id.cancel);

		sendButton.setOnClickListener(this);
		cancleButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			if (email.getText().toString().trim().length() == 0) {
				Toast.makeText(Activity_ForgotPassword.this,
						"Enter email id.", Toast.LENGTH_SHORT).show();
			} else {
				if (WebServiceConstants.isOnline(Activity_ForgotPassword.this)) {
					new ForgottenPassword()
							.execute(WebServiceConstants.BASE_URL
									+ WebServiceConstants.FORGOT_PASSWORD);
				} else {
					Toast.makeText(Activity_ForgotPassword.this,
							"000000.", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.cancel:
			finish();
			break;

		default:
			break;
		}

	}

	class ForgottenPassword extends AsyncTask<String, Void, JSONArray> {
		TransparentProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new TransparentProgressDialog(
					Activity_ForgotPassword.this);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();

		}
		

		@Override
		protected JSONArray doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("email", email.getText()
					.toString()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			return json;

		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			try {
				JSONObject object = result.getJSONObject(0);
				if (object.get("status").equals("success")) {
					Toast.makeText(Activity_ForgotPassword.this,
							"Email successfully send.", Toast.LENGTH_SHORT)
							.show();
					finish();
				} else {
					Toast.makeText(Activity_ForgotPassword.this,
							"email id is not correct.", Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}