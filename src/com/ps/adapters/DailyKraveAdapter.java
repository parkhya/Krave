package com.ps.adapters;

import java.util.ArrayList;

import com.krave.kraveapp.R;
import com.ps.models.DailyKraveDTO;
import com.ps.utill.ImageLoader;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DailyKraveAdapter extends BaseAdapter {
	private Activity mActivity;
	private ArrayList<DailyKraveDTO> arrayList;
	private ViewHolder mViewHolder;
	private ImageLoader imageLoader;

	public DailyKraveAdapter(Activity activity, ArrayList<DailyKraveDTO> list) {
		this.mActivity = activity;
		this.arrayList = list;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
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
					R.layout.row_dailykrave, null);
			mViewHolder = new ViewHolder();
			mViewHolder.ImageView = (ImageView) convertView
					.findViewById(R.id.imageView);
			mViewHolder.TextView = (TextView) convertView
					.findViewById(R.id.textView);
			convertView.setTag(mViewHolder);
		}

		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.TextView
				.setText("" + arrayList.get(position).getHeadline());
		imageLoader.DisplayImage(arrayList.get(position).getImagepath(),
				mViewHolder.ImageView);

		Log.d("", "image path=" + arrayList.get(position).getImagepath());
		return convertView;
	}

	private static class ViewHolder {
		public ImageView ImageView;
		public TextView TextView;
	}
}
