package com.ps.adapters;

import java.util.ArrayList;

import com.krave.kraveapp.R;
import com.ps.models.InterestsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingListInterestAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<InterestsDTO> interestsDTOs;
	// private ArrayList<Integer> mSelectedImages;
	// private ArrayList<Boolean> mSelections;
	// private ArrayList<String> mTexts;
	private ViewHolder mViewHolder;
	private ImageLoader imageLoader;
	private ImageLoaderCircle imageLoaderCircle;
	private boolean isFirstTime = true;
	private int check;

	public SettingListInterestAdapter(Activity activity,
			ArrayList<InterestsDTO> list) {

		this.mActivity = activity;
		this.interestsDTOs = list;
		this.check = check;
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
					R.layout.row_interest_setting_view, null);
			mViewHolder = new ViewHolder();
			mViewHolder.itemImageView = (CircleImageView) convertView
					.findViewById(R.id.itemImageView);
			mViewHolder.selectedItemImageView = (ImageView) convertView
					.findViewById(R.id.itemImageViewCheck);
			mViewHolder.itemNameTextView = (TextView) convertView
					.findViewById(R.id.itemNameTextView);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Md);
			mViewHolder.itemNameTextView.setTypeface(typeface);

			convertView.setTag(mViewHolder);
		}

		else {

			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		InterestsDTO interestsDTO = interestsDTOs.get(position);

	//	mViewHolder.itemImageView.setBackgroundResource(R.drawable.normal_icon);
//		if (isFirstTime)
//
//		{
//
//			imageLoaderCircle.DisplayImage(interestsDTO.getInterestIcon(),
//					mViewHolder.itemImageView);
//			mViewHolder.itemNameTextView
//					.setText(interestsDTO.getInterestName());
//			if (position == interestsDTOs.size() - 1) {
//				isFirstTime = false;
//			}
//
//		}
		 imageLoaderCircle.DisplayImage(interestsDTO.getInterestIcon(),
		 mViewHolder.itemImageView);
		mViewHolder.itemNameTextView.setText(interestsDTO.getInterestName());
		Log.d("", "selected interest" + interestsDTO.getIsSelected());

		if (interestsDTO.getIsSelected()) {
			// Bitmap sourceBitmap = imageLoaderCircle.getBitmap(interestsDTO
			// .getInterestIcon());
			// Bitmap sourceBitmap = bitmap.createBitmap(bitmap);

			// mViewHolder.itemImageView.setImageBitmap(gray(sourceBitmap));
			mViewHolder.selectedItemImageView.setImageDrawable(mActivity
					.getResources().getDrawable(R.drawable.mask_imgred));
			// mViewHolder.selectedItemImageView.setAlpha(1f);
			// mViewHolder.itemNameTextView.setTextColor(mActivity.getResources()
			// .getColor(R.color.title_bar));
			Log.d("", "selected interest" + position);
		} else {
			// mViewHolder.itemImageView.setImageBitmap(imageLoaderCircle
			// .getBitmap(interestsDTO.getInterestIcon()));
			mViewHolder.selectedItemImageView.setImageDrawable(mActivity
					.getResources().getDrawable(R.drawable.mask_img));
			// mViewHolder.selectedItemImageView.setAlpha(0.3f);
			// mViewHolder.itemNameTextView.setTextColor(mActivity.getResources()
			// .getColor(R.color.black));
		}

		return convertView;
	}

	private static class ViewHolder {

		public ImageView selectedItemImageView;
		public CircleImageView itemImageView;
		public TextView itemNameTextView;
	}

	public Bitmap gray(Bitmap bmp) {
		Bitmap operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),
				bmp.getConfig());

		for (int i = 0; i < bmp.getWidth(); i++) {
			for (int j = 0; j < bmp.getHeight(); j++) {
				int p = bmp.getPixel(i, j);
				int r = Color.red(p);
				int g = Color.green(p);
				int b = Color.blue(p);

				r = (int) 255;
				g = (int) 255;
				b = (int) 255;

				operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
			}
		}
		return operation;
	}
}
