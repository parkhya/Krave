package com.krave.kraveapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ps.adapters.DailyKraveAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.DailyKraveDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.JSONParser;
import com.ps.utill.WebServiceConstants;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@SuppressLint("NewApi")
public class FragmentDailyKraveWithWebView extends Fragment {
	WebView webView;
	private Activity_Home context;
	TransparentProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.fragment_daily_krave_with_web_view, container, false);
		System.gc();
		context = (Activity_Home) getActivity();
		progressDialog = new TransparentProgressDialog(context);
		progressDialog.show();
		webView = (WebView) view.findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				progressDialog.show();
				Log.d("", "shouldOverrideUrlLoading");
				context.back_button.setVisibility(View.VISIBLE);
				context.left_button.setVisibility(View.GONE);

				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				Log.d("", "onPageFinished");
				progressDialog.dismiss();
				if (url.equals("http://www.kraveapp.net/blog/")) {
					context.left_button.setVisibility(View.VISIBLE);
					context.back_button.setVisibility(View.GONE);
				}
			}

		});
		context.back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webView != null) {
					if (webView.canGoBack()) {
						progressDialog.show();
						webView.goBack();
						Log.d("", "canGoBack");
					}
//					if (!webView.canGoBack()) {
//						Log.d("", "canGoBacknot");
//						context.left_button.setVisibility(View.VISIBLE);
//						context.back_button.setVisibility(View.GONE);
//					}
				}
			}
		});
		webView.loadUrl("http://www.kraveapp.net/blog/");
		return view;
	}

}