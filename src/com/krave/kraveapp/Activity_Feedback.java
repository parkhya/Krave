package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ps.adapters.PagerViewAdapter;

import com.ps.loader.TransparentProgressDialog;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Feedback extends Activity {
	private static Activity_Feedback activityObject = null;

	public static Activity_Feedback getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_Feedback activityObject) {
		Activity_Feedback.activityObject = activityObject;
	}

	private EditText feedBackEditText;

	private Button backButton, sendButton;
	private LinearLayout saveButton;
	private SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activityObject = this;
		setContentView(R.layout.activity_feedback);
		sessionManager = new SessionManager(Activity_Feedback.this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
		TextView textView1, textView2;
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.saveTextView);

		Typeface typeface = FontStyle.getFont(Activity_Feedback.this,
				AppConstants.HelveticaNeueLTStd_Lt);

		textView2.setTypeface(typeface);
		textView1.setTypeface(typeface);
		feedBackEditText.setTypeface(typeface);
	}

	private void setLayout() {
		feedBackEditText = (EditText) findViewById(R.id.oldPassword);
		feedBackEditText.setText(sessionManager.getFeedback());
		saveButton = (LinearLayout) findViewById(R.id.save);

		backButton = (Button) findViewById(R.id.back_button);
		sendButton = (Button) findViewById(R.id.sendButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (0 == feedBackEditText.getText().toString().length()) {
					finish();
				} else {
					openDailog();
				}

			}
		});
		// saveButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (0 == feedBackEditText.getText().toString().length()) {
		// Toast.makeText(Activity_Feedback.this, "Enter some text",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// sessionManager.setFeedback(feedBackEditText.getText()
		// .toString());
		// finish();
		// }
		//
		// }
		// });
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (0 == feedBackEditText.getText().toString().length()) {
					Toast.makeText(Activity_Feedback.this, "Enter some text",
							Toast.LENGTH_SHORT).show();
				} else {
					new SendFeedbackAsynchTask()
							.execute(WebServiceConstants.BASE_URL
									+ WebServiceConstants.SEND_FEEDBACK);

				}

			}
		});

	}

	public void openDailog() {
		final Dialog dialog = new Dialog(Activity_Feedback.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_about_me);
		// Do you realy want to delete your account ?
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText("Do you want to save feedback ?");
		Typeface typeface = FontStyle.getFont(Activity_Feedback.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		title.setTypeface(typeface);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

				sessionManager.setFeedback(feedBackEditText.getText()
						.toString());

				finish();

			}
		});
		dialog.show();
	}

	class SendFeedbackAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// http://198.12.150.189/~simssoe/index.php?action=send_feedback&feedback=hi&user_id=10
			dialog = new TransparentProgressDialog(Activity_Feedback.this);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			params.add(new BasicNameValuePair("user_id", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("feedback", ""
					+ feedBackEditText.getText().toString()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("check user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					System.out.print("feedback response : " + jsonArray);
					sessionManager.setFeedback("");
					Toast.makeText(Activity_Feedback.this,
							"Your feedback sent successfully.",
							Toast.LENGTH_SHORT).show();
					sessionManager.setFeedback("");
					finish();

				} else if (vStatus.equals("failure")) {
					Toast.makeText(Activity_Feedback.this,
							"Feedback not send, try again  ",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
}