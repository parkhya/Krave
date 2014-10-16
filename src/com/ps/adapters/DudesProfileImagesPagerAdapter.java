package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.krave.kraveapp.Activity_DudeGallery;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.R;
import com.ps.models.UserProfileImageDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.ImageLoader;
import com.ps.utill.SessionManager;

public class DudesProfileImagesPagerAdapter extends PagerAdapter {

	Activity_DudeGallery context;
	View layout;
	ArrayList<UserProfileImageDTO> arrayList;
	private ImageLoader imageLoader;
	private SessionManager sessionManager;

	public DudesProfileImagesPagerAdapter(Activity mActivity,
			List<UserProfileImageDTO> mainArrayList) {
		context = (Activity_DudeGallery) mActivity;
		arrayList = (ArrayList<UserProfileImageDTO>) mainArrayList;
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {

		return arrayList.size();
	}

	ImageView imageView1;
	ImageView imageView1Stamp;

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.row_user_profile_image_pager_view,
				null);
		imageView1 = (ImageView) layout.findViewById(R.id.imageView1);
		imageView1Stamp = (ImageView) layout.findViewById(R.id.imageView1Stamp);
		if (arrayList.size() > 0) {
			if (arrayList.get(position).getImageId()
					.equals(AppConstants.FACEBOOK_IMAGE)) {
				imageView1.setScaleType(ScaleType.FIT_CENTER);
				imageLoader.DisplayImage(
						arrayList.get(position).getImagePath(), imageView1);
			} else {
				imageView1.setScaleType(ScaleType.FIT_CENTER);
				if (!arrayList.get(position).getIsImageActive()) {
					imageView1Stamp.setVisibility(View.VISIBLE);
				} else {
					imageLoader.DisplayImage(arrayList.get(position)
							.getImagePath(), imageView1);
				}
			}

		}
		((ViewPager) container).addView(layout, 0);
		return layout;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	// }

}
