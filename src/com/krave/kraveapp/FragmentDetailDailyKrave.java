package com.krave.kraveapp;


import com.ps.models.DailyKraveDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.ImageLoader;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentDetailDailyKrave extends Fragment {
	private ImageView imagepath;
	private TextView TextViewName, TextViewHeadLine, TextViewSubtitle;
	private Button Back;
	public static DailyKraveDTO dailyKraveDTO;
	private ImageLoader imageLoader;
	private Activity_Home context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.full_daily_krave, container,
				false);
		System.gc();
		context = (Activity_Home) getActivity();
		context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_DAILY_KRAVE_DETAIL;
		context.left_button.setImageResource(R.drawable.btn_back);
		imageLoader = new ImageLoader(context);
		init(view);
		loadData();
		return view;
	}

	private void loadData() {
		imageLoader.DisplayImage(dailyKraveDTO.getImagepath(), imagepath);
		TextViewName.setText(dailyKraveDTO.getName());
		TextViewHeadLine.setText(dailyKraveDTO.getHeadline());
		TextViewSubtitle.setText(dailyKraveDTO.getSubtitle());
		// TextViewName.setText(dailyKraveDTO.getName());
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
		imagepath = (ImageView) view.findViewById(R.id.imageView);
		TextViewName = (TextView) view.findViewById(R.id.textViewName);
		TextViewHeadLine = (TextView) view.findViewById(R.id.textViewHeadLine);
		TextViewSubtitle = (TextView) view.findViewById(R.id.textViewSubTitle);
		// TextViewName.setText(""+FragmentDailyKrave.mDailyKraveDTO.getName());
		// TextViewHeadLine.setText(""+FragmentDailyKrave.mDailyKraveDTO.getHeadline());
		// TextViewSubtitle.setText(""+FragmentDailyKrave.mDailyKraveDTO.getSubtitle());

	}

}
