package com.ps.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krave.kraveapp.R;
import com.ps.models.InterestsDTO;
import com.ps.utill.CircleImageView;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;

public class DudeDetailsInterestAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<InterestsDTO> interestsDTOs;
	// private ArrayList<Integer> mSelectedImages;
	// private ArrayList<Boolean> mSelections;
	// private ArrayList<String> mTexts;
	private ViewHolder mViewHolder;
	private ImageLoader imageLoader;
	private ImageLoaderCircle imageLoaderCircle;
	private boolean isFirstTime = true;

	public DudeDetailsInterestAdapter(Activity activity,
			ArrayList<InterestsDTO> list) {

		this.mActivity = activity;
		this.interestsDTOs = list;

	}

	@Override
	public int getCount() {
		return interestsDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		return interestsDTOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		imageLoader = new ImageLoader(mActivity);
		imageLoaderCircle = new ImageLoaderCircle(mActivity);
		if (convertView == null) {

			convertView = mActivity.getLayoutInflater().inflate(
					R.layout.row_interest_detail_dude_profile, null);
			mViewHolder = new ViewHolder();
			mViewHolder.itemImageView = (CircleImageView) convertView
					.findViewById(R.id.itemImageView);
			mViewHolder.circle = (ImageView) convertView
					.findViewById(R.id.circle);
			mViewHolder.selectedItemImageView = (CircleImageView) convertView
					.findViewById(R.id.itemImageViewCheck);
			mViewHolder.itemNameTextView = (TextView) convertView
					.findViewById(R.id.itemNameTextView);
			convertView.setTag(mViewHolder);
		}

		else {

			mViewHolder = (ViewHolder) convertView.getTag();
		}
		InterestsDTO interestsDTO = interestsDTOs.get(position);

		if (interestsDTO.getIsSelected()) {
			mViewHolder.itemImageView.setAlpha(0.8f);
			mViewHolder.circle.setAlpha(0.8f);

		} else {
			mViewHolder.itemImageView.setAlpha(0.2f);
			mViewHolder.circle.setAlpha(0.2f);
		}
		imageLoader.DisplayImage(interestsDTO.getInterestIcon(),
				mViewHolder.itemImageView);
		Log.d("", "interest link =" + interestsDTO.getInterestIcon());
		return convertView;
	}

	private void setAlphaForView(View v, float alpha) {
		AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
		animation.setDuration(10000);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}

	private static class ViewHolder {

		// public ImageView selectedItemImageView;
		public ImageView circle;
		public CircleImageView itemImageView,selectedItemImageView;
		public TextView itemNameTextView;
	}
}
