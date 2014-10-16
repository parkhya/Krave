package com.ps.adapters;

import java.util.ArrayList;

import com.krave.kraveapp.FragmentChatMatches;
import com.krave.kraveapp.R;
import com.ps.models.InterestsDTO;
import com.ps.models.UserDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddDudesInListImageAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<UserDTO> userDTOs;
	// private ArrayList<Integer> mSelectedImages;
	// private ArrayList<Boolean> mSelections;
	// private ArrayList<String> mTexts;
	private ViewHolder mViewHolder;
	private ImageLoader imageLoader;
	private ImageLoaderCircle imageLoaderCircle;
	private boolean isFirstTime = true;

	public AddDudesInListImageAdapter(Activity activity, ArrayList<UserDTO> list) {

		this.mActivity = activity;
		this.userDTOs = list;

	}

	@Override
	public int getCount() {
		return userDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		return userDTOs.get(position);
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
					R.layout.row_add_dude_profile_image, null);
			mViewHolder = new ViewHolder();
			mViewHolder.itemImageView = (CircleImageView) convertView
					.findViewById(R.id.userimage);
			// mViewHolder.selectedItemImageView = (CircleImageView) convertView
			// .findViewById(R.id.itemImageViewCheck);
			// mViewHolder.itemNameTextView = (TextView) convertView
			// .findViewById(R.id.itemNameTextView);
			convertView.setTag(mViewHolder);
		}

		else {

			mViewHolder = (ViewHolder) convertView.getTag();
		}
		UserDTO userDTO = (UserDTO) getItem(position);

		if (userDTO.getUserProfileImageDTOs().size() != 0
				&& userDTO.getUserProfileImageDTOs() != null && userDTO
						.getUserProfileImageDTOs().get(0).getIsImageActive()) {
			imageLoaderCircle.DisplayImage(userDTO.getUserProfileImageDTOs()
					.get(0).getImagePath(), mViewHolder.itemImageView);
		}
		return convertView;
	}

	private static class ViewHolder {

		// public ImageView selectedItemImageView;
		public CircleImageView itemImageView, selectedItemImageView;
		public TextView itemNameTextView;
	}
}
