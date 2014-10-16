package com.krave.kraveapp;

import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.ps.adapters.PagerViewAdapter;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.UserDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.JSONParser;
import com.ps.utill.OnSwipeTouchListener;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentDudesProfile extends Fragment implements OnClickListener {
	private RelativeLayout swipeLayout, dudesCongratulationLayout;
	private UserDTO userDTO;
	private ImageView dudeProfileImage, dudeCongratulationProfileImage,
			dudeCongratulationProfileImageStamp, userProfileImage,
			userProfileImageStamp, cancelButton, addButton, doneButton,
			settingButton, userStatusImage;
	private TextView name, address, commonFriends, userStatusTexts;
	private Activity_Home context;
	private ImageLoader imageLoader;
	private Button sendMessageButton, keepSearchingButton;
	private SessionManager sessionManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home2, container, false);
		System.gc();
		context = (Activity_Home) getActivity();
		imageLoader = new ImageLoader(context);
		sessionManager = new SessionManager(context);
		setLayout(view);
		setListener();
		setTypeFace(view);
		loadData(Activity_Home.index);
		context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_DUDES_PROFILE;
		context.left_button.setImageResource(R.drawable.btn_back);
		if (context.isOpenCongratulationLayout) {
			// context.setLeftDrawer = 0;
			// context.left_button.setBackgroundResource(R.drawable.nav);
			OpenDudesCongratulationLayout();
			context.isOpenCongratulationLayout = false;
			// if (WebServiceConstants.isOnline(context)) {
			// new SendLikeRequestAsynchTask()
			// .execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.SEND_LIKE_REQUESTS);
			// }
		}
		return view;
	}

	private void setListener() {
		dudeProfileImage.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		doneButton.setOnClickListener(this);
		settingButton.setOnClickListener(this);
		sendMessageButton.setOnClickListener(this);
		keepSearchingButton.setOnClickListener(this);
		dudesCongratulationLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		swipeLayout.setOnTouchListener(new OnSwipeTouchListener() {
			// public void onSwipeTop() {
			// settingSkipButton();
			// }
			//
			// public void onpress() {
			// LongClick();
			// }

			public void onSwipeRight() {

				// Toast.makeText(context, "swipe right", 1).show();
				cancelButtonEvent();
			}

			public void onSwipeLeft() {

				// .makeText(context, "swipe left", 1).show();
				doneButtonEvent();
			}

			// public void onSwipeBottom() {
			// settingToggleButton();
			// }
		});

	}

	private void setTypeFace(View view) {
		TextView textView1, textView11, textView2;
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView11 = (TextView) view.findViewById(R.id.textView11);
		textView2 = (TextView) view.findViewById(R.id.textView2);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		name.setTypeface(typeface);
		address.setTypeface(typeface);
		commonFriends.setTypeface(typeface);

		textView1.setTypeface(typeface);

		textView11.setTypeface(typeface);

		textView2.setTypeface(typeface);

		sendMessageButton.setTypeface(typeface);
		keepSearchingButton.setTypeface(typeface);
	}

	private void setLayout(View view) {
		RelativeLayout mainview = (RelativeLayout) view
				.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		swipeLayout = (RelativeLayout) view.findViewById(R.id.swipeLayout);

		settingButton = (ImageView) view.findViewById(R.id.setting);

		dudeProfileImage = (ImageView) view.findViewById(R.id.userProfileImage);
		cancelButton = (ImageView) view.findViewById(R.id.cancel);
		addButton = (ImageView) view.findViewById(R.id.add);
		doneButton = (ImageView) view.findViewById(R.id.done);
		userStatusImage = (ImageView) view.findViewById(R.id.imageOnOff);
		userStatusTexts = (TextView) view.findViewById(R.id.textOnOff);
		name = (TextView) view.findViewById(R.id.name);
		address = (TextView) view.findViewById(R.id.address);
		commonFriends = (TextView) view.findViewById(R.id.commonFriends);

		/* congratulation Layout */
		dudesCongratulationLayout = (RelativeLayout) view
				.findViewById(R.id.congratulationLayout);
		userProfileImage = (ImageView) view
				.findViewById(R.id.userImageViewcongratulation);
		dudeCongratulationProfileImage = (ImageView) view
				.findViewById(R.id.dudeImageView);
		userProfileImageStamp = (ImageView) view
				.findViewById(R.id.userImageViewcongratulationStamp);
		dudeCongratulationProfileImageStamp = (ImageView) view
				.findViewById(R.id.dudeImageViewStamp);
		sendMessageButton = (Button) view.findViewById(R.id.sendmsgbtn);
		keepSearchingButton = (Button) view
				.findViewById(R.id.keepSearchingButton);

	}

	private void loadData(int index) {
		userDTO = Activity_Home.dudeCommonList.get(index);
		// if ((Activity_Home.dudeCommonList.size() - 1) == index) {
		// cancelButton.setVisibility(View.GONE);
		// }
		if (userDTO.getUserProfileImageDTOs().size() > 0) {
			if (userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
				imageLoader
						.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
								.getImagePath(), dudeProfileImage);
			}
		}
		String age = "";
		try {
			age = calculateAge(userDTO.getWhatAreYouDTO().getAge());
		} catch (Exception e) {

		}
		name.setText(userDTO.getFirstName() + " | " + age);
		String addressString = userDTO.getCity() + ", " + userDTO.getCountry();
		address.setText(addressString);
		commonFriends.setText("" + userDTO.getCommonFriends()
				+ " Friends in common");

		if (userDTO.getIsOnline().equals(AppConstants.ONLINE)) {
			userStatusImage.setBackgroundResource(R.drawable.online);
			userStatusTexts.setText("ONLINE");
		} else {
			userStatusImage.setBackgroundResource(R.drawable.red_crcl);
			userStatusTexts.setText("OFFLINE");
		}

		// handler.postDelayed(runnable, 0);
	}

	private String calculateAge(String berthDateString) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date berthDate = null;
		try {
			berthDate = format.parse(berthDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date currentDate = new Date();

		long time = currentDate.getTime() - berthDate.getTime();

		long diffInDays = (long) time / (1000 * 60 * 60 * 24);

		long number_of_years = (long) diffInDays / 365;
		long number_of_months = (long) (diffInDays % (365)) / 30;

		return "" + number_of_years;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userProfileImage:
			dudeProfileImage.setEnabled(false);
			swipeLayout.setVisibility(View.VISIBLE);

			handler.postDelayed(runnable, 5000);

			break;
		case R.id.cancel:
			cancelButtonEvent();
			break;
		case R.id.add:
			FragmentDetailDudesProfile.comeFrom = AppConstants.FRAGMENT_DUDES_PROFILE;
			context.Attach_Fragment(AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE);
			break;
		case R.id.done:
			// OpenDudesCongratulationLayout();
			doneButtonEvent();

			break;
		case R.id.setting:
			FragmentSetting.comeFrom = true;
			FragmentHome.filterId = 0;
			FragmentHome.previousCount = 0;
			FragmentHome.filterIsOnline = false;
			FragmentHome.pageNumber = 0;
			FragmentHome.userDTOs.clear();
			FragmentHome.city = "";
			context.Attach_Fragment(AppConstants.FRAGMENT_SETTING);
			break;
		case R.id.sendmsgbtn:
			FragmentHome.Refresh_Data = true;
			FragmentChatMatches.userDTO = Activity_Home.dudeCommonList
					.get(Activity_Home.index);
			context.Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
			// Activity_Chat.userDTO = userDTO;
			// Intent intent = new Intent(context, Activity_Chat.class);
			// startActivity(intent);
			break;
		case R.id.keepSearchingButton:
			// context.Attach_Fragment(AppConstants.FRAGMENT_DUDES_PROFILE);
			FragmentHome.Refresh_Data = true;
			context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
			context.setLeftDrawer = 0;
			context.left_button.setImageResource(R.drawable.nav);
			break;

		default:
			break;
		}

	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();

		//Activity_Home.dudeCommonList.remove(Activity_Home.index);
		System.gc();
		context.left_button.setImageResource(R.drawable.nav);
	}

	private void cancelButtonEvent() {
		if (Activity_Home.index < Activity_Home.dudeCommonList.size() - 1) {
			Activity_Home.index = Activity_Home.index + 1;
			loadData(Activity_Home.index);
		}

	}

	private void doneButtonEvent() {
		if (WebServiceConstants.isOnline(context)) {
			new SendLikeRequestAsynchTask()
					.execute(WebServiceConstants.BASE_URL
							+ WebServiceConstants.SEND_LIKE_REQUESTS);
		}

	}

	private void OpenDudesCongratulationLayout() {
		/* add dude in friend list */
		UserDTO dto = Activity_Home.dudeCommonList.get(Activity_Home.index);

		context.searchDudeList.add(dto);
		/* ...../////..........////............///........ */

		/* remove dude from requist list */
		String id = dto.getUserID();
		for (int i = 0; i < context.requestDudesList.size(); i++) {
			UserDTO userDTO = context.requestDudesList.get(i);

			if (id.equals(userDTO.getUserID())) {

				context.requestDudesList.remove(userDTO);
			}
		}
		/* ...../////..........////............///........ */

		dudesCongratulationLayout.setVisibility(View.VISIBLE);
		if (userDTO.getUserProfileImageDTOs().get(0).getImageId()
				.equals(AppConstants.FACEBOOK_IMAGE)) {
			dudeCongratulationProfileImage.setScaleType(ScaleType.FIT_CENTER);
		} else {
			dudeCongratulationProfileImage.setScaleType(ScaleType.FIT_XY);
		}

		// imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
		// .getImagePath(), dudeCongratulationProfileImage);

		if (sessionManager.getUserDetail().getUserProfileImageDTOs().get(0)
				.getImageId().equals(AppConstants.FACEBOOK_IMAGE)) {
			userProfileImage.setScaleType(ScaleType.FIT_CENTER);
		} else {
			userProfileImage.setScaleType(ScaleType.FIT_XY);
		}

		// imageLoader.DisplayImage(sessionManager.getUserDetail()
		// .getUserProfileImageDTOs().get(0).getImagePath(),
		// userProfileImage);

		try {

			// UserDTO sessionUserDTO = sessionManager.getUserDetail();

			if (sessionManager.getUserDetail().getUserProfileImageDTOs().size() > 0) {

				if (sessionManager.getUserDetail().getUserProfileImageDTOs()
						.get(0).getIsImageActive()) {
					imageLoader.DisplayImage(sessionManager.getUserDetail()
							.getUserProfileImageDTOs().get(0).getImagePath(),
							userProfileImage);
				} else {
					userProfileImageStamp.setVisibility(View.VISIBLE);
				}
			}

			if (userDTO.getUserProfileImageDTOs().size() > 0) {

				if (userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
					imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs()
							.get(0).getImagePath(),
							dudeCongratulationProfileImage);
				} else {
					userProfileImageStamp.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {

		}
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			Log.d("", "swipe thread start");

			Log.d("", "swipe thread stop");
			swipeLayout.setVisibility(View.GONE);
			dudeProfileImage.setEnabled(true);

		}
	};

	class SendLikeRequestAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		// user_id=2&friend_id=1
		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("friend_id", userDTO.getUserID()));
			// params.add(new BasicNameValuePair("user_email",
			// userDTO.getEmail()));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get Like response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {

					if (context.isOpenCongratulationLayout) {
						context.setLeftDrawer = 0;
						context.left_button.setImageResource(R.drawable.nav);
						// OpenDudesCongratulationLayout();
						context.isOpenCongratulationLayout = false;
					}
					OpenDudesCongratulationLayout();

				} else if (vStatus.equals("send")) {
					Toast.makeText(context, "Like request successfully send.",
							Toast.LENGTH_SHORT).show();
					if (Activity_Home.index < Activity_Home.dudeCommonList
							.size() - 1) {
						Activity_Home.index = Activity_Home.index + 1;
						loadData(Activity_Home.index);
					} else {
						context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
					}
					// cancelButtonEvent();

				} else if (vStatus.equals("allready")) {
					Toast.makeText(context, "You allready send an request.",
							Toast.LENGTH_SHORT).show();
					if (Activity_Home.index < Activity_Home.dudeCommonList
							.size() - 1) {
						Activity_Home.index = Activity_Home.index + 1;
						loadData(Activity_Home.index);
					} else {
						context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
					}
				} else if (vStatus.equals("failure")) {
					Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
}
