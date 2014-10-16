package com.krave.kraveapp;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.ps.adapters.DudeDetailsInterestAdapter;
import com.ps.adapters.DudesProfileImagesPagerAdapter;
import com.ps.horizontal_listview.HorizontalListView;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.InterestsDTO;
import com.ps.models.UserDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.JSONParser;
import com.ps.utill.MyXMLHandler;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentDetailDudesProfile extends Fragment implements
		OnClickListener {
	private Activity_Home context;
	private UserDTO userDTO;
	private ImageView cancelButton, doneButton, userStatusImage,
			showUserDetailsVisibility, backImage, nextImage;
	private TextView name, address, commonFriends, relationShipStatus,
			whatAreYou, userHightWeight, aboutDude, distanceAway,
			sharedInterest, userStatusText;
	private boolean isHide = false;
	private ImageView viewPager, viewPagerStamp;
	private HorizontalListView horizontalListView;
	private DudesProfileImagesPagerAdapter dudesProfileImagesPagerAdapter;
	// private LinearLayout setPagerIconImagesLayout;

	private ImageView imageView0, imageView1, imageView2, imageView3,
			imageView4, imageView5;
	private ImageView imageArray[] = { imageView0, imageView1, imageView2,
			imageView3, imageView4, imageView5 };
	private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			25, 25);
	private int previousPosition;
	private LinearLayout progressIcon;
	private DudeDetailsInterestAdapter dudeDetailsInterestAdapter;
	private List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	private SessionManager sessionManager;
	public static int comeFrom;
	private GPSTracker gpsTracker;
	private ImageLoader imageLoader;
	static final String logTag = "ActivitySwipeDetector";
	private Activity activity;
	static final int MIN_DISTANCE = 100;
	private float downX, downY, upX, upY;
	private RelativeLayout swipeLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home3, container, false);
		System.gc();
		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);
		imageLoader = new ImageLoader(context);
		gpsTracker = new GPSTracker(context);
		setLayout(view);
		setTypeFace(view);
		if (comeFrom == AppConstants.FRAGMENT_CHATT_MATCHES) {
			context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE_WITH_CHAT;
			context.left_button.setImageResource(R.drawable.btn_back);
			cancelButton.setVisibility(View.GONE);
			doneButton.setVisibility(View.GONE);
		} else {
			context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE;
			context.left_button.setImageResource(R.drawable.btn_back);
		}

		loadData(Activity_Home.index);

		return view;
	}

	private void setTypeFace(View view) {

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		name.setTypeface(typeface);
		address.setTypeface(typeface);
		relationShipStatus.setTypeface(typeface);
		distanceAway.setTypeface(typeface);
		userHightWeight.setTypeface(typeface);
		aboutDude.setTypeface(typeface);
		sharedInterest.setTypeface(typeface);
		whatAreYou.setTypeface(typeface);
		userStatusText.setTypeface(typeface);
	}

	// private void setPagerView() {
	// previousPosition = 0;
	// dudesProfileImagesPagerAdapter = new DudesProfileImagesPagerAdapter(
	// context, userDTO.getUserProfileImageDTOs());
	// viewPager.setAdapter(dudesProfileImagesPagerAdapter);
	// progressIcon.removeAllViews();
	// viewPager.setOnPageChangeListener(new OnPageChangeListener() {
	//
	// @Override
	// public void onPageSelected(int arg0) {
	// imageArray[previousPosition].setBackgroundDrawable(context
	// .getResources().getDrawable(R.drawable.uncheck));
	// imageArray[arg0].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.check));
	// previousPosition = arg0;
	//
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	//
	// for (int i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {
	// imageArray[i] = new ImageView(context);
	// layoutParams.setMargins(10, 5, 10, 5);
	// imageArray[i].setLayoutParams(layoutParams);
	// imageArray[i].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.uncheck));
	// if (i == 0) {
	// imageArray[i].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.check));
	// }
	//
	// final int j = i;
	// imageArray[i].setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// imageArray[previousPosition].setBackgroundDrawable(context
	// .getResources().getDrawable(R.drawable.uncheck));
	// imageArray[j].setBackgroundDrawable(context.getResources()
	// .getDrawable(R.drawable.check));
	// viewPager.setCurrentItem(j);
	// previousPosition = j;
	//
	// }
	// });
	// progressIcon.addView(imageArray[i]);
	// }
	//
	// }

	private void setLayout(View view) {
		RelativeLayout mainview = (RelativeLayout) view
				.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		viewPager = (ImageView) view.findViewById(R.id.viewpager1);
		viewPagerStamp = (ImageView) view.findViewById(R.id.viewpagerStamp);
		progressIcon = (LinearLayout) view.findViewById(R.id.layoutIcon);
		horizontalListView = (HorizontalListView) view
				.findViewById(R.id.horizontalListView1);
		// setPagerIconImagesLayout = (LinearLayout) view
		// .findViewById(R.id.layoutIcon);
		// userProfileImage = (ImageView)
		// view.findViewById(R.id.userProfileImage);
		cancelButton = (ImageView) view.findViewById(R.id.cancel);
		// addButton = (ImageView) view.findViewById(R.id.add);
		doneButton = (ImageView) view.findViewById(R.id.done);
		showUserDetailsVisibility = (ImageView) view
				.findViewById(R.id.hideShowImage);

		name = (TextView) view.findViewById(R.id.name);
		address = (TextView) view.findViewById(R.id.address);
		// commonFriends = (TextView) view.findViewById(R.id.textView5);
		relationShipStatus = (TextView) view.findViewById(R.id.relationShip);
		userHightWeight = (TextView) view.findViewById(R.id.hightWeight);
		aboutDude = (TextView) view.findViewById(R.id.aboutMe);
		distanceAway = (TextView) view.findViewById(R.id.milesAway);
		sharedInterest = (TextView) view.findViewById(R.id.sharedInterest);
		userStatusText = (TextView) view.findViewById(R.id.textOnOff);
		whatAreYou = (TextView) view.findViewById(R.id.whatareyou);
		backImage = (ImageView) view.findViewById(R.id.backimage);
		nextImage = (ImageView) view.findViewById(R.id.nimage);
		userStatusImage = (ImageView) view.findViewById(R.id.imageOnOff);
		swipeLayout = (RelativeLayout) view.findViewById(R.id.swipeLayout);
		backImage.setOnClickListener(this);
		nextImage.setOnClickListener(this);
		showUserDetailsVisibility.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		// addButton.setOnClickListener(this);
		doneButton.setOnClickListener(this);

		dudeDetailsInterestAdapter = new DudeDetailsInterestAdapter(context,
				(ArrayList<InterestsDTO>) interestsDTOs);
		horizontalListView.setAdapter(dudeDetailsInterestAdapter);
		// viewPager.setOnClickListener(this);

		// viewPager.setOnTouchListener(new OnSwipeTouchListener() {
		// @Override
		// public boolean onTouch(View view, MotionEvent motionEvent) {
		// boolean check = true;
		// if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
		// Log.d("", "down");
		// return true;
		// } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
		// Log.d("", "up");
		// if (check = true) {
		// Activity_DudeGallery.userDTO = userDTO;
		// Intent intent = new Intent(context,
		// Activity_DudeGallery.class);
		// context.startActivity(intent);
		// return true;
		// }
		//
		// } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
		// Log.d("", "move");
		// check = false;
		// }
		//
		// return true;
		// }
		//
		// // public void onSwipeTop() {
		// // settingSkipButton();
		// // }
		// //
		// // public void onpress() {
		// // LongClick();
		// // }
		//
		// public void onSwipeRight() {
		//
		// // Toast.makeText(context, "swipe right", 1).show();
		// cancelButtonEvent();
		// }
		//
		// public void onSwipeLeft() {
		//
		// // Toast.makeText(context, "swipe left", 1).show();
		// doneButtonEvent();
		// }
		//
		// // public void onSwipeBottom() {
		// // settingToggleButton();
		// // }
		// });

		viewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					downX = event.getX();
					downY = event.getY();
					if (comeFrom != AppConstants.FRAGMENT_CHATT_MATCHES) {
						swipeLayout.setVisibility(View.VISIBLE);
					}
					return true;
				}
				case MotionEvent.ACTION_UP: {
					upX = event.getX();
					upY = event.getY();

					float deltaX = downX - upX;
					float deltaY = downY - upY;
					swipeLayout.setVisibility(View.GONE);
					// swipe horizontal?
					if (Math.abs(deltaX) > Math.abs(deltaY)
							&& (comeFrom != AppConstants.FRAGMENT_CHATT_MATCHES)) {
						if (Math.abs(deltaX) > MIN_DISTANCE) {

							// handler1.postDelayed(runnable1, 5000);
							// left or right
							if (deltaX < 0) {
								doneButtonEvent();

								return true;
							}
							if (deltaX > 0) {
								cancelButtonEvent();
								return true;
							}
						}

					} else {

						openGallery();
						return false; // We don't consume the event
					}
					// // swipe vertical?
					// else {
					// if (Math.abs(deltaY) > MIN_DISTANCE) {
					// // top or down
					// if (deltaY < 0) {
					// Toast.makeText(context, "up",
					// Toast.LENGTH_SHORT).show();
					// ;
					// return true;
					// }
					// if (deltaY > 0) {
					// Toast.makeText(context, "down",
					// Toast.LENGTH_SHORT).show();
					// ;
					// return true;
					// }
					// } else {
					// Log.i(logTag,
					// "Vertical Swipe was only "
					// + Math.abs(deltaX)
					// + " long, need at least "
					// + MIN_DISTANCE);
					// Toast.makeText(context, "onclik",
					// Toast.LENGTH_SHORT).show();
					// return false; // We don't consume the event
					// }
					// }

					return true;
				}
				}
				return false;
			}
		});

	}

	Handler handler1 = new Handler();
	Runnable runnable1 = new Runnable() {

		@Override
		public void run() {

			swipeLayout.setVisibility(View.GONE);

		}
	};

	private void openGallery() {
		if (userDTO.getUserProfileImageDTOs().size() > 0) {
			Activity_DudeGallery.userDTO = userDTO;
			Intent intent = new Intent(context, Activity_DudeGallery.class);
			context.startActivity(intent);
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		handler.removeCallbacks(runnable);
		System.gc();
		context.left_button.setImageResource(R.drawable.nav);
	}

	private void loadData(int index) {
		handler.removeCallbacks(runnable);
		if (comeFrom == AppConstants.FRAGMENT_CHATT_MATCHES) {
			userDTO = FragmentChatMatches.userDTO;
		} else {
			// if ((Activity_Home.dudeCommonList.size() - 1) == index) {
			// cancelButton.setVisibility(View.GONE);
			// }
			userDTO = Activity_Home.dudeCommonList.get(index);
		}
		viewPagerStamp.setVisibility(View.GONE);
		if (userDTO.getUserProfileImageDTOs().size() > 0) {
			if (userDTO.getUserProfileImageDTOs().get(0).getImageId()
					.equals(AppConstants.FACEBOOK_IMAGE)) {
				viewPager.setScaleType(ScaleType.FIT_CENTER);
				imageLoader
						.DisplayImage(userDTO.getUserProfileImageDTOs().get(0)
								.getImagePath(), viewPager);
			} else {
				viewPager.setScaleType(ScaleType.FIT_CENTER);
				if (!userDTO.getUserProfileImageDTOs().get(0)
						.getIsImageActive()) {
					viewPager
							.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
					viewPagerStamp.setVisibility(View.VISIBLE);
				} else {
					imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs()
							.get(0).getImagePath(), viewPager);
				}
			}

		} else {

			viewPager
					.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);

			// viewPagerStamp.setVisibility(View.VISIBLE);
		}

		if (userDTO.getIsOnline().equals(AppConstants.ONLINE)) {
			userStatusImage.setBackgroundResource(R.drawable.online);
			userStatusText.setText("ONLINE");
		} else {
			userStatusImage.setBackgroundResource(R.drawable.red_crcl);
			userStatusText.setText("OFFLINE");
		}

		String age = "";
		try {
			age = calculateAge(userDTO.getWhatAreYouDTO().getAge());
		} catch (Exception e) {

		}
		name.setText(userDTO.getFirstName() + " | " + age);
		whatAreYou.setText(userDTO.getWhatAreYouDTO().getWhatAreYouName());
		String addressString = "";
		if (!(userDTO.getCity() == null)) {
			addressString = addressString + userDTO.getCity() + ", ";
		}
		if (!(userDTO.getCountry() == null)) {
			addressString = addressString + userDTO.getCountry();
		}
		// String addressString = userDTO.getCity() + ", " +
		// userDTO.getCountry();
		address.setText(addressString);
		// commonFriends.setText("");
		String hight = "";
		String weight = "";
		relationShipStatus.setText(getRelationShipStatus());
		Log.d("", "hight unit=" + userDTO.getWhatAreYouDTO().getHeightUnit());
		if (userDTO.getWhatAreYouDTO().getHeightUnit()
				.equals(AppConstants.HEIGHT_IN_FEET)) {
			hight = userDTO.getWhatAreYouDTO().getHight().split("/")[0] + "'"
					+ userDTO.getWhatAreYouDTO().getHight().split("/")[1]
					+ "\"";// + " (US)";
		} else {
			hight = userDTO.getWhatAreYouDTO().getHight().split("/")[0] + " m,"
					+ userDTO.getWhatAreYouDTO().getHight().split("/")[1]
					+ " cm";// + " (METRICs)";
		}
		if (userDTO.getWhatAreYouDTO().getWeightUnit()
				.equals(AppConstants.WEIGHT_IN_POUND)) {
			weight = userDTO.getWhatAreYouDTO().getWeight().split("/")[0]
					+ " lbs";// + " (US)";
			// + userDTO.getWhatAreYouDTO().getWeight().split("/")[1]
			// + " oz";
		} else {
			weight = userDTO.getWhatAreYouDTO().getWeight().split("/")[0]
					+ " kg";// + " (METRICs)";
			// + userDTO.getWhatAreYouDTO().getWeight().split("/")[1]
			// + " Gms";
		}
		userHightWeight.setText(hight + " / " + weight);
		aboutDude.setText(userDTO.getAboutMe());
		Float distance = (float) 0.0;
		try {
			distance = distFrom(gpsTracker.getLatitude(),
					gpsTracker.getLongitude(),
					Double.valueOf(userDTO.getLatLongDTO().getLatitude()),
					Double.valueOf(userDTO.getLatLongDTO().getLongitude()));
		} catch (Exception e) {

		}
		distanceAway.setText("" + distance + " miles away");
		interestsDTOs.clear();

		List<InterestsDTO> sesstionInterestsDTOs = sessionManager
				.getUserDetail().getInterestList();

		for (int i = 0; i < sesstionInterestsDTOs.size(); i++) {
			String temp = sesstionInterestsDTOs.get(i).getInterestId();

			Log.d("", "session interest id=" + temp);

			for (int k = 0; k < userDTO.getInterestList().size(); k++) {

				InterestsDTO dto = userDTO.getInterestList().get(k);
				Log.d("", "dto interest id=" + dto.getInterestId());
				if (dto.getInterestId().equals(temp)) {
					Log.d("", "interest selection =true " + temp);
					dto.setIsSelected(true);
				}
			}

		}

		for (int j = 0; j < userDTO.getFacebookLikesDTOs().size(); j++) {
			String id = userDTO.getFacebookLikesDTOs().get(j).getLikeId();
			InterestsDTO dto = new InterestsDTO();
			dto.setIsSelected(true);
			dto.setInterestId(id);
			dto.setInterestIcon("https://graph.facebook.com/" + id
					+ "/picture?type=large");
			userDTO.getInterestList().add(dto);

		}

		interestsDTOs.addAll(userDTO.getInterestList());
		dudeDetailsInterestAdapter.notifyDataSetChanged();
		// setPagerView();

		handler.postDelayed(runnable, 0);

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

	public float distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;// 1609.34

		// return (float) (dist * meterConversion);
		return (float) (Math.round(dist * 100.0) / 100.0);

	}

	private CharSequence getRelationShipStatus() {
		int i = 3;
		try {
			i = Integer.valueOf(userDTO.getWhatAreYouDTO()
					.getRelationshipStatus());
		} catch (Exception e) {

		}
		Log.d("", "relation ="
				+ userDTO.getWhatAreYouDTO().getRelationshipStatus());
		String relation = "";
		switch (i) {
		case 1:
			relation = "SINGLE";
			break;
		case 2:
			relation = "IN A RELATIONSHIP";
			break;
		// case 3:
		// relation = "DON'T SHOW";
		// break;

		}
		Log.d("", "relation =" + relation);
		return relation;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			cancelButtonEvent();
			break;

		case R.id.done:
			doneButtonEvent();

			break;

		case R.id.hideShowImage:
			if (isHide) {
				setVisibility(View.VISIBLE);
				showUserDetailsVisibility.setImageDrawable(context
						.getResources().getDrawable(R.drawable.arrow_down));

				isHide = false;
			} else {
				setVisibility(View.GONE);
				showUserDetailsVisibility.setImageDrawable(context
						.getResources().getDrawable(R.drawable.arrow_up));
				isHide = true;
			}
			break;
		case R.id.backimage:
			int index = horizontalListView.Getposition();

			horizontalListView.scrollTo(index + 50);
			break;
		case R.id.nimage:
			int index1 = horizontalListView.Getposition();

			horizontalListView.scrollTo(index1 - 50);
			break;
		case R.id.viewpager1:
			Activity_DudeGallery.userDTO = userDTO;
			Intent intent = new Intent(context, Activity_DudeGallery.class);
			context.startActivity(intent);
			break;
		default:
			break;
		}

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

	private void setVisibility(int id) {
		relationShipStatus.setVisibility(id);
		userHightWeight.setVisibility(id);
		aboutDude.setVisibility(id);

	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			Log.d("", "online thread");
			if (WebServiceConstants.isOnline(context)) {
				new GetOnline().execute(userDTO.getUserID());
			}

		}
	};

	// //Logic for online user////
	public class GetOnline extends AsyncTask<Object, Void, String> {
		ImageView imageView;
		MyXMLHandler myXMLHandler;

		@Override
		protected String doInBackground(Object... arg0) {
			// TODO Auto-generated method stub

			// try {

			try {
				/** Handling XML */
				Log.d("", "online/offline user ID=" + arg0[0]);
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				/** Send URL to parse XML Tags */

				// http://54.241.85.74:9090/plugins/presence/status?jid=109@ip-10-199-23-53&type=xml
				URL sourceUrl = new URL(
						"http://54.241.85.74:9090/plugins/presence/status?jid="
								+ arg0[0] + "@ip-10-199-23-53&type=xml");
				/** Create handler to handle XML Tags ( extends DefaultHandler ) */
				myXMLHandler = new MyXMLHandler();
				xr.setContentHandler(myXMLHandler);
				xr.parse(new InputSource(sourceUrl.openStream()));

			} catch (Exception e) {
				System.out.println("XML Pasing Excpetion = " + e.getMessage());
			}

			// String posturl =
			// "http://198.12.150.189:9090/plugins/presence/status?jid="+arg0[0]+"@ip-198-12-150-189.secureserver.net&type=text";
			//
			// DefaultHttpClient httpclient = new DefaultHttpClient();
			// HttpGet httpget = new HttpGet(posturl);
			// HttpResponse response = httpclient.execute(httpget);
			// BufferedReader in = new BufferedReader(new InputStreamReader(
			// response.getEntity().getContent()));
			// StringBuffer sb = new StringBuffer("");
			// String line = "";
			// String NL = System.getProperty("line.separator");
			// while ((line = in.readLine()) != null) {
			// sb.append(line + NL);
			// }
			// in.close();
			// String result = sb.toString();
			// Log.v("My Response :: ", result);
			//
			// return result;
			// } catch (UnsupportedEncodingException e) {
			// // writing error to Log
			// Log.v("Message...1", e.getMessage());
			// e.printStackTrace();
			// } catch (ClientProtocolException e) {
			// Log.v("Message...2", e.getMessage());
			// // writing exception to log
			// e.printStackTrace();
			// } catch (IOException e) {
			// Log.v("Message...3", e.getMessage());
			// // writing exception to log
			// e.printStackTrace();
			// }

			return myXMLHandler.getType();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("string is " + result);
			System.out.println("string is " + result == ("null"));
			try {
				if (result.equals("")) {
					// filterList.get(poss).setIsOnline(AppConstants.ONLINE);
					userStatusImage.setBackgroundResource(R.drawable.online);
					userStatusText.setText("ONLINE");

				} else if (result.equals("unavailable")) {
					userStatusImage.setBackgroundResource(R.drawable.red_crcl);
					userStatusText.setText("OFFLINE");
				} else if (result.equals("error")) {
					userStatusImage.setBackgroundResource(R.drawable.red_crcl);
					userStatusText.setText("OFFLINE");
				}
			} catch (Exception e) {

			}
			// new GetOnline().execute(userDTO.getUserID());
			handler.postDelayed(runnable, 15000);
		}
	}

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
					context.isOpenCongratulationLayout = true;
					context.Attach_Fragment(AppConstants.FRAGMENT_DUDES_PROFILE);

				} else if (vStatus.equals("send")) {
					Toast.makeText(context, "Like request successfully send.",
							Toast.LENGTH_SHORT).show();

					//Activity_Home.dudeCommonList.remove(Activity_Home.index);
					if (Activity_Home.index < Activity_Home.dudeCommonList
							.size() - 1) {
						// cancelButtonEvent();

						loadData(++Activity_Home.index);

					} else {
						context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
					}

				} else if (vStatus.equals("allready")) {
					Toast.makeText(context, "You allready send an request.",
							Toast.LENGTH_SHORT).show();
					//Activity_Home.dudeCommonList.remove(Activity_Home.index);
					if (Activity_Home.index < Activity_Home.dudeCommonList
							.size() - 1) {
						// cancelButtonEvent();

						loadData(++Activity_Home.index);
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
