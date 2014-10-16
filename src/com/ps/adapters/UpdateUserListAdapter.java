package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krave.kraveapp.R;
import com.ps.models.UserDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoaderCircle;

public class UpdateUserListAdapter extends BaseAdapter {

	Context mActivity;
	ArrayList<UserDTO> marrayList;
	ImageLoaderCircle imageLoaderCircle;
	private List<UserDTO> filteredData = null;

	private ItemFilter mFilter = new ItemFilter();

	public UpdateUserListAdapter(Context Activity, List<UserDTO> arrayList) {
		this.mActivity = Activity;
		this.marrayList = (ArrayList<UserDTO>) arrayList;
		this.filteredData = arrayList;
		imageLoaderCircle = new ImageLoaderCircle(mActivity);

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
		ImageView marker, selected;
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
			rowView = ((Activity) mActivity).getLayoutInflater().inflate(
					R.layout.row_update_user_list, null);
			viewHolder.marker = (ImageView) rowView.findViewById(R.id.marker);
			viewHolder.name = (TextView) rowView.findViewById(R.id.name);
			viewHolder.email = (TextView) rowView.findViewById(R.id.email);
			viewHolder.userimage = (CircleImageView) rowView
					.findViewById(R.id.userimage);
			viewHolder.selected = (ImageView) rowView
					.findViewById(R.id.selectDude);
			Typeface typeface = FontStyle.getFont(mActivity,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.name.setTypeface(typeface);
			viewHolder.email.setTypeface(typeface);
			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}

		viewHolder.name.setText("" + userDTO.getFirstName() + " "
				+ userDTO.getLastName());
		viewHolder.email.setText("" + userDTO.getAboutMe());
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
		if (userDTO.isSelected()) {
			viewHolder.selected.setBackgroundResource(R.drawable.selected_icon);
		} else {
			viewHolder.selected.setBackgroundResource(R.drawable.normal_icon);
		}
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
