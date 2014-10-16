package com.krave.kraveapp;

import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_AboutMe extends Activity {
	private static Activity_AboutMe activityObject = null;

	public static Activity_AboutMe getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_AboutMe activityObject) {
		Activity_AboutMe.activityObject = activityObject;
	}

	private EditText aboutMe;

	private Button backButton;
	private LinearLayout saveButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activityObject = this;
		setContentView(R.layout.activity_about_me);
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

		textView2 = (TextView) findViewById(R.id.saveTextView);

		Typeface typeface = FontStyle.getFont(Activity_AboutMe.this,
				AppConstants.HelveticaNeueLTStd_Lt);

		textView2.setTypeface(typeface);
		aboutMe.setTypeface(typeface);

	}

	private void setLayout() {
		aboutMe = (EditText) findViewById(R.id.oldPassword);
		aboutMe.setText(getIntent().getExtras().getString("about"));
		saveButton = (LinearLayout) findViewById(R.id.save);

		backButton = (Button) findViewById(R.id.back_button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (0 == aboutMe.getText().toString().length()) {
					finish();
				} else {
					openDailog();
				}

			}
		});
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (0 == aboutMe.getText().toString().length()) {
					Toast.makeText(Activity_AboutMe.this, "Enter some text",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent();
					intent.putExtra("about", aboutMe.getText().toString());
					setResult(RESULT_OK, intent);
					finish();
				}

			}
		});

	}

	public void openDailog() {
		final Dialog dialog = new Dialog(Activity_AboutMe.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_about_me);
		// Do you realy want to delete your account ?
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText("Do you want to save changes ?");
		Typeface typeface = FontStyle.getFont(Activity_AboutMe.this,
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
				Intent intent = new Intent();
				intent.putExtra("about", aboutMe.getText().toString());
				setResult(RESULT_OK, intent);
				finish();

			}
		});
		dialog.show();
	}
}