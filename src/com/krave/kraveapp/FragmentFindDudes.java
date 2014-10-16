package com.krave.kraveapp;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FragmentFindDudes extends Fragment {
	
	String about = "home";
	TextView text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		System.gc();
//		text = (TextView) view.findViewById(R.id.textView1);
//		text.setText(Html.fromHtml(about));
		LinearLayout mainview = (LinearLayout) view.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		return view;
	}

}
