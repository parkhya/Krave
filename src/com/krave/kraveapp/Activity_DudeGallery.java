package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.ps.adapters.DudeDetailsInterestAdapter;
import com.ps.adapters.DudesProfileImagesPagerAdapter;
import com.ps.horizontal_listview.HorizontalListView;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.InterestsDTO;
import com.ps.models.UserDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;

import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_DudeGallery extends Activity {
	private static Activity_DudeGallery activityObject = null;

	public static Activity_DudeGallery getActivityObject() {
		return activityObject;
	}

	public static void setActivityObject(Activity_DudeGallery activityObject) {
		Activity_DudeGallery.activityObject = activityObject;
	}

	public static UserDTO userDTO;
	private ViewPager viewPager;
	private DudesProfileImagesPagerAdapter dudesProfileImagesPagerAdapter;
	private ImageView imageView0, imageView1, imageView2, imageView3,
			imageView4, imageView5;
	private ImageView imageArray[] = { imageView0, imageView1, imageView2,
			imageView3, imageView4, imageView5 };

	private int previousPosition;
	private RelativeLayout layout4;
	private LinearLayout progressIcon, cancleLayout;
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			25, 25);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_dude_gallery);
		setLayout();
		activityObject = this;
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = width;

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		layout4.setLayoutParams(params);

		setPagerView();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
		viewPager.removeAllViews();
		viewPager=null;
		userDTO=null;
		dudesProfileImagesPagerAdapter=null;
		
		activityObject = null;
	}

	private void setLayout() {

		viewPager = (ViewPager) findViewById(R.id.viewpager1);
		progressIcon = (LinearLayout) findViewById(R.id.layoutIcon);
		cancleLayout = (LinearLayout) findViewById(R.id.cancleLayout);
		layout4 = (RelativeLayout) findViewById(R.id.layout4);

		cancleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	private void setPagerView() {
		previousPosition = 0;
		dudesProfileImagesPagerAdapter = new DudesProfileImagesPagerAdapter(
				Activity_DudeGallery.this, userDTO.getUserProfileImageDTOs());
		viewPager.setAdapter(dudesProfileImagesPagerAdapter);
		progressIcon.removeAllViews();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				imageArray[previousPosition]
						.setBackgroundDrawable(Activity_DudeGallery.this
								.getResources().getDrawable(R.drawable.uncheck));
				imageArray[arg0].setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.check));
				previousPosition = arg0;

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

		for (int i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {
			if (i > 5) {
				break;
			}
			imageArray[i] = new ImageView(Activity_DudeGallery.this);
			layoutParams.setMargins(10, 5, 10, 5);
			imageArray[i].setLayoutParams(layoutParams);
			imageArray[i].setBackgroundDrawable(getResources().getDrawable(
					R.drawable.uncheck));
			if (i == 0) {
				imageArray[i].setBackgroundDrawable(getResources().getDrawable(
						R.drawable.check));
			}

			final int j = i;
			imageArray[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					imageArray[previousPosition]
							.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.uncheck));
					imageArray[j].setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.check));
					viewPager.setCurrentItem(j);
					previousPosition = j;

				}
			});
			progressIcon.addView(imageArray[i]);
		}

	}

}