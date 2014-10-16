package com.ps.adapters;

import java.util.ArrayList;

import com.krave.kraveapp.R;
import com.ps.models.InterestsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<InterestsDTO> interestsDTOs;
	// private ArrayList<Integer> mSelectedImages;
	// private ArrayList<Boolean> mSelections;
	// private ArrayList<String> mTexts;
	private ViewHolder mViewHolder;
	private ImageLoader imageLoader;
	private boolean isFirstTime = true;

	public GridViewAdapter(Activity activity, ArrayList<InterestsDTO> list) {

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
		if (convertView == null) {

			convertView = mActivity.getLayoutInflater().inflate(
					R.layout.row_grid_view, null);
			mViewHolder = new ViewHolder();
			mViewHolder.itemImageView = (ImageView) convertView
					.findViewById(R.id.itemImageView);
			mViewHolder.selectedItemImageView = (ImageView) convertView
					.findViewById(R.id.itemImageViewCheck);
			mViewHolder.itemNameTextView = (TextView) convertView
					.findViewById(R.id.itemNameTextView);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Lt);
			mViewHolder.itemNameTextView.setTypeface(typeface);

			convertView.setTag(mViewHolder);
		}

		else {

			mViewHolder = (ViewHolder) convertView.getTag();
		}
		InterestsDTO interestsDTO = interestsDTOs.get(position);

		// mViewHolder.itemImageView
		// .setBackgroundResource(R.drawable.normal_icon);
		if (isFirstTime)

		{

			imageLoader
					.DisplayImage(
							AppConstants.BASE_IMAGE_PATH_1
									+ interestsDTO.getInterestIcon(),
							mViewHolder.itemImageView);
			mViewHolder.itemNameTextView
					.setText(interestsDTO.getInterestName());
			if (position == interestsDTOs.size() - 1) {
				isFirstTime = false;
			}

		}
		Log.d("", "selected interest" + interestsDTO.getIsSelected());
		if (interestsDTO.getIsSelected()) {

			mViewHolder.selectedItemImageView.setImageDrawable(mActivity
					.getResources().getDrawable(R.drawable.mask_imgred));
			mViewHolder.itemNameTextView.setTextColor(mActivity.getResources()
					.getColor(R.color.title_bar));
			Log.d("", "selected interest" + position);
		} else {
			mViewHolder.selectedItemImageView.setImageDrawable(mActivity
					.getResources().getDrawable(R.drawable.mask_img));
			mViewHolder.itemNameTextView.setTextColor(mActivity.getResources()
					.getColor(R.color.black));
		}

		return convertView;
	}

	private static class ViewHolder {

		public ImageView itemImageView, selectedItemImageView;
		public TextView itemNameTextView;
	}
}
