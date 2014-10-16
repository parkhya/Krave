package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.FragmentDudesProfile;
import com.krave.kraveapp.R;
import com.ps.adapters.WhatAreYouListAdapter.ViewHolder;
import com.ps.models.UserDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.SessionManager;

public class SearchByAdapter extends BaseAdapter {

	Context mActivity;
	ArrayList<UserDTO> marrayList;
	ImageLoaderCircle imageLoaderCircle;
	ArrayList<UserDTO> filteredData = null;
	Filter mFilter = new ItemFilter();
	int check;

	public SearchByAdapter(Context Activity, List<UserDTO> arrayList, int check) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = (ArrayList<UserDTO>) arrayList;
		imageLoaderCircle = new ImageLoaderCircle(mActivity);
		this.check = check;
	}

	// methed to count
	@Override
	public int getCount() {
		return filteredData.size();
	}

	@Override
	public Object getItem(int position) {
		return filteredData.get(position);// /[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// static class for object
	public static class ViewHolder {
		ImageView marker;
		TextView name, email;
		CircleImageView userimage;
	}

	// getview for single row
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = null;
		ViewHolder viewHolder = new ViewHolder();
		UserDTO userDTO = (UserDTO) getItem(position);
		if (convertView == null) {
			if (check == 0) {
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_home2, null);
			} else {
				rowView = ((Activity) mActivity).getLayoutInflater().inflate(
						R.layout.row_home3, null);
			}
			viewHolder.marker = (ImageView) rowView.findViewById(R.id.marker);
			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.email = (TextView) rowView.findViewById(R.id.about);
			viewHolder.userimage = (CircleImageView) rowView
					.findViewById(R.id.userimage);

			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.name.setTypeface(typeface);
			viewHolder.email.setTypeface(typeface);

			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}

		if (userDTO.getUserProfileImageDTOs().size() != 0
				&& userDTO.getUserProfileImageDTOs() != null) {
			if (userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
				imageLoaderCircle.DisplayImage(userDTO
						.getUserProfileImageDTOs().get(0).getImagePath(),
						viewHolder.userimage);
			} else {
				imageLoaderCircle.DisplayImage(AppConstants.AWATAR_IAMGE,
						viewHolder.userimage);
			}
		} else {
			imageLoaderCircle.DisplayImage(AppConstants.AWATAR_IAMGE,
					viewHolder.userimage);
		}
		if (check == 1) {
			if (userDTO.getIsOnline().equals(AppConstants.ONLINE)) {
				viewHolder.marker.setBackgroundResource(R.drawable.online);
			} else {
				viewHolder.marker.setBackgroundResource(R.drawable.white_crl);
			}
		}
		viewHolder.name.setText(filteredData.get(position).getFirstName() + " "
				+ filteredData.get(position).getLastName());
		viewHolder.email.setText(filteredData.get(position).getAboutMe());
		return rowView;
	}

	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			// final List<UserDTO> list = arrayList;

			int count = marrayList.size();
			final ArrayList<UserDTO> nlist = new ArrayList<UserDTO>(count);

			String compayerString;
			for (int i = 0; i < count; i++) {
				UserDTO dto = marrayList.get(i);
				compayerString = dto.getFirstName().toString()
						+ dto.getLastName().toString();
				if (compayerString.toLowerCase().contains(filterString)) {
					nlist.add(dto);
				}
			}
			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredData = (ArrayList<UserDTO>) results.values;
			notifyDataSetChanged();
		}

	}
}
