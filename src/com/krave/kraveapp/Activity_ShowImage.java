package com.krave.kraveapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ps.models.ChatDetailsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.SessionManager;
import com.ps.utill.TouchImageView;

//SELECT * FROM `krave_users` WHERE `user_id`IN('4','110','46','59','64','108','109')
public class Activity_ShowImage extends Activity {
	private static Activity_ShowImage activityObject = null;

	public static Activity_ShowImage getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_ShowImage activityObject) {
		Activity_ShowImage.activityObject = activityObject;
	}

	/* resources */
	private SessionManager sessionManager;
	private ImageLoader imageLoader;

	/* Layout Views */
	private TouchImageView showImage;

	private LinearLayout cancelLayout;
	private TextView dateTime;
	ImageButton rotateClockWise, rotateAntiClockWise;
	Bitmap bitmap;
	int angle = 0;
	/* dto variables initialization */

	public static ChatDetailsDTO chatMessageObj;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_show_image);
		activityObject = this;
		sessionManager = new SessionManager(Activity_ShowImage.this);
		imageLoader = new ImageLoader(Activity_ShowImage.this);

		setLayout();
		setListeners();
		loadData();
		// setTypeFace();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		activityObject = null;
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}

	}

	private void setTypeFace() {
		TextView textView1, textView2;
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);

		Typeface typeface = FontStyle.getFont(Activity_ShowImage.this,
				AppConstants.HelveticaNeueLTStd_Lt);
		textView1.setTypeface(typeface);

		textView2.setTypeface(typeface);

		// cancel.setTypeface(typeface);
		// ok.setTypeface(typeface);

	}

	private void loadData() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd MMM yyyy hh:mm aa");
		Date date = new Date(Long.valueOf(chatMessageObj.getTime()));

		dateTime.setText(dateFormat.format(date));

		// byte windowImageString[] = Base64.decode(chatMessageObj.getMessage(),
		// Base64.DEFAULT);
		// bitmap = BitmapFactory.decodeByteArray(windowImageString, 0,
		// windowImageString.length);
		//
		// showImage.setImageBitmap(bitmap);
		imageLoader.DisplayImage(chatMessageObj.getMessage(), showImage);
	}

	private void setLayout() {

		cancelLayout = (LinearLayout) findViewById(R.id.cancleLayout);

		showImage = (TouchImageView) findViewById(R.id.showImage);
		dateTime = (TextView) findViewById(R.id.dateTime);
		rotateClockWise = (ImageButton) findViewById(R.id.clock);
		rotateAntiClockWise = (ImageButton) findViewById(R.id.anticlock);
	}

	private void setListeners() {
		bitmap = imageLoader.getBitmap(chatMessageObj.getMessage());
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 5;
		// bitmap.
		rotateClockWise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				angle = angle + 90;

				if (bitmap != null) {
					RotateBitmap(bitmap, angle % 360);
				}

			}
		});
		rotateAntiClockWise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				angle = angle - 90;
				if (bitmap != null) {
					RotateBitmap(bitmap, angle % 360);
				}

			}
		});
		cancelLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

	}

	public Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		showImage.setImageBitmap(Bitmap.createBitmap(source, 0, 0,
				source.getWidth(), source.getHeight(), matrix, true));
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
	}
}
