package com.krave.kraveapp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.krave.kraveapp.Activity_Home.GetAllLikeDude;
import com.krave.kraveapp.FragmentChatMatches.DeleteChatHistoryOfParticular;
import com.krave.kraveapp.FragmentSetting.DeleteAccountAsyncTask;
import com.ps.adapters.ChatListViewAdapter;
import com.ps.adapters.ChatMatchesAdapter;
import com.ps.adapters.FindDudesAdapter.GetOnline;

import com.ps.loader.TransparentProgressDialog;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.LatLongDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.JSONParser;
import com.ps.utill.KraveDAO;
import com.ps.utill.MyXMLHandler;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentChatFriend extends Fragment implements OnClickListener {
	/* resources */
	private SessionManager sessionManager;
	private ImageLoaderCircle imageLoaderCircle;
	private Activity_Home context;
	ChatMatchesAdapter chatMatchesAdapter;
	/* Layout Views */
	private ImageView backButton, plusButton;
	private TextView totaldude;
	private ListView friendListView;
	private List<UserDTO> searchDudeList = new ArrayList<UserDTO>();
	private RelativeLayout menuLayout;
	private LinearLayout delteAllHistoryLayout, cancleLayout;

	private ImageView onlineDudesFilter;
	// public static ChatDetailsDTO chatDetailsDTO;
	/* dto variables initialization */
	private LinearLayout onlyOnlineDudeLayout;
	private EditText searchFriends;
	private ImageView clearSearchResult;
	private boolean filterIsOnline = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat_friend, container,
				false);
		context = (Activity_Home) getActivity();
		sessionManager = new SessionManager(context);
		setLayout(view);
		setListeners();
		setTypeFace(view);
		context.mainview.setVisibility(View.GONE);
		if (searchDudeList.size() == 0) {
			if (WebServiceConstants.isOnline(context)) {
				new GetAllLikeDude().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_ALL_LIKE_DUDE);
			}
		} else {
			chatMatchesAdapter.notifyDataSetChanged();
			// count.setText("" + searchDudeList.size() + " Friend");
		}

		return view;
	}

	private void setTypeFace(View view) {
		TextView textView1, textView5, text1, text2;

		textView1 = (TextView) view.findViewById(R.id.dudeName);
		textView5 = (TextView) view.findViewById(R.id.textView1);
		text1 = (TextView) view.findViewById(R.id.text1);
		text2 = (TextView) view.findViewById(R.id.text2);
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);
		textView1.setTypeface(typeface);
		textView5.setTypeface(typeface);
		text1.setTypeface(typeface);
		text2.setTypeface(typeface);
		searchFriends.setTypeface(typeface);
		// count.setTypeface(typeface);

	}

	private void setLayout(View view) {

		LinearLayout mainview = (LinearLayout) view.findViewById(R.id.mainview);
		mainview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		backButton = (ImageView) view.findViewById(R.id.backButton);
		plusButton = (ImageView) view.findViewById(R.id.plusbutton);
		// count = (TextView) view.findViewById(R.id.count);
		friendListView = (ListView) view.findViewById(R.id.chatListView);
		menuLayout = (RelativeLayout) view.findViewById(R.id.menu);

		onlineDudesFilter = (ImageView) view.findViewById(R.id.filter);
		onlyOnlineDudeLayout = (LinearLayout) view
				.findViewById(R.id.filterLayout);

		searchFriends = (EditText) view.findViewById(R.id.serchCity);
		clearSearchResult = (ImageView) view
				.findViewById(R.id.clearSearchResult);
		delteAllHistoryLayout = (LinearLayout) view
				.findViewById(R.id.deleteAllHistory);
		cancleLayout = (LinearLayout) view.findViewById(R.id.cancleLayout);

		chatMatchesAdapter = new ChatMatchesAdapter(context, searchDudeList, 0);
		friendListView.setAdapter(chatMatchesAdapter);

		// to scroll the list view to bottom on data change

	}

	private void setListeners() {
		backButton.setOnClickListener(this);
		plusButton.setOnClickListener(this);
		delteAllHistoryLayout.setOnClickListener(this);
		cancleLayout.setOnClickListener(this);
		onlineDudesFilter.setOnClickListener(this);
		clearSearchResult.setOnClickListener(this);

		friendListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				FragmentChatMatches.backButtonComeFrom = true;
				FragmentChatMatches.userDTO = (UserDTO) chatMatchesAdapter
						.getItem(arg2);
				context.Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);

			}
		});
		// searchFriends.setOnEditorActionListener(new OnEditorActionListener()
		// {
		// @Override
		// public boolean onEditorAction(TextView v, int actionId,
		// KeyEvent event) {
		// // city = searchFriends.getText().toString();
		// // TODO Auto-generated method stub
		// if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		// if (searchFriends.getText().toString().trim().length() == 0) {
		// Toast.makeText(context, "Enter some text..",
		// Toast.LENGTH_SHORT).show();
		// } else {
		//
		// InputMethodManager imm = (InputMethodManager) context
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(
		// searchFriends.getWindowToken(), 0);
		//
		// }
		// }
		//
		// return false;
		// }
		// });

		searchFriends.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (filterIsOnline) {
					chatMatchesAdapter.getFilter().filter(
							s.toString() + "/" + AppConstants.ONLINE);
				} else {
					chatMatchesAdapter.getFilter().filter(
							s.toString() + "/" + AppConstants.OFFLINE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backButton:
		//	context.onClick(context.layoutFindDudes);
			context.selectedItem = 1;
			context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
			break;
		case R.id.plusbutton:
			menuLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.cancleLayout:
			menuLayout.setVisibility(View.GONE);

			break;
		case R.id.deleteAllHistory:
			menuLayout.setVisibility(View.GONE);
			openDailog();
			break;
		case R.id.filter:
			if (filterIsOnline) {
				onlineDudesFilter.setBackgroundResource(R.drawable.btn_offline);
				// findDudesAdapter.getFilter().filter(AppConstants.ALL_DUDE);
				filterIsOnline = false;
				// calculatePages(userDTOs.size());
				// viewPager.setCurrentItem(0);
				chatMatchesAdapter.getFilter().filter(
						searchFriends.getText().toString() + "/"
								+ AppConstants.OFFLINE);

			} else {
				onlineDudesFilter.setBackgroundResource(R.drawable.btn_online);
				// findDudesAdapter.getFilter().filter(AppConstants.ONLINE);
				filterIsOnline = true;
				chatMatchesAdapter.getFilter().filter(
						searchFriends.getText().toString() + "/"
								+ AppConstants.ONLINE);
				// viewPager.setCurrentItem(0);
			}
			break;
		case R.id.clearSearchResult:
			searchFriends.setText("");
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == context.RESULT_OK && requestCode == 1) {
			context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		System.gc();
		context.mainview.setVisibility(View.VISIBLE);
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();

	}

	public void openDailog() {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_logout);
		// Do you realy want to delete your account ?
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText("Do you really want to delete chat history ?");
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		title.setTypeface(typeface);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DeleteChatHistory().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.DELETE_CHAT);

				// KraveDAO dataBaseHelper = new KraveDAO(context);
				// dataBaseHelper.clearDataBase();
				dialog.dismiss();
				// Toast.makeText(context, "Chat history successfully deleted",
				// Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}

	class DeleteChatHistory extends AsyncTask<String, Void, JSONArray> {
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

		protected JSONArray doInBackground(String... args) {

			// http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("request_id", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("delete chat response", "" + json);
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
					KraveDAO dataBaseHelper = new KraveDAO(context);
					dataBaseHelper.clearDataBase();

					Toast.makeText(context,
							"Chat history successfully deleted",
							Toast.LENGTH_SHORT).show();
					for (int i = 0; i < searchDudeList.size(); i++) {
						UserDTO dto = searchDudeList.get(0);
						dto.setUserLastMsg("");

					}
					chatMatchesAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(context,
							"Chat history not  deleted,try again",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context, "Chat history not  deleted,try again",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	class GetAllLikeDude extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		// http://parkhya.org/Android/krave_app/index.php?action=getfriends&user_id=107
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			// //
			// http://parkhya.org/Android/krave_app/index.php?action=user_login&email=test2@gmail.com&password=123456
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", sessionManager
					.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			Log.d("like user response :", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			searchDudeList.clear();

			System.out.print("like user response :" + jsonArray);
			// System.out.print("search user response :" + jsonArray.length());
			try {

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject Object = jsonArray.getJSONObject(i);

					searchDudeList.add(parseUserData(Object));
				}
				chatMatchesAdapter.notifyDataSetChanged();
				// count.setText("" + searchDudeList.size() + " Friend");
				if (searchDudeList.size() == 0) {
					Toast.makeText(context, "No Guy", Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private UserDTO parseUserData(JSONObject mJsonObject) throws JSONException {
		UserDTO userDTO = new UserDTO();
		// AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		LatLongDTO latLongDTO = new LatLongDTO();
		JSONObject MainObject = mJsonObject.getJSONObject("userinfo");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		// System.out.println(MainObject);
		// userDTO.setLanguage(MainObject.getString("user_language"));
		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));

		userDTO.setUnreadMsg(MainObject.getString("unread_msg"));

		if (!MainObject.getString("user_message").equals("null")) {
			if (MainObject.getString("user_message").contains(
					AppConstants.BASE_IMAGE_PATH_1)) {
				userDTO.setUserLastMsg("PHOTO");

			} else {
				userDTO.setUserLastMsg(MainObject.getString("user_message"));

			}
		}
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}

		if (MainObject.getString("isonline").equals("available")) {
			userDTO.setIsOnline(AppConstants.ONLINE);

		} else if (MainObject.getString("isonline").equals("away")) {
			userDTO.setIsOnline(AppConstants.ABSENT);
		} else {
			userDTO.setIsOnline(AppConstants.OFFLINE);
		}
		// userDTO.setCommonFriends(MainObject.getString("mutualfriends"));
		whatAreYouDTO.setFeet(MainObject.getString("user_height"));
		whatAreYouDTO.setInches(MainObject.getString("user_inches"));
		whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
		whatAreYouDTO.setHight(MainObject.getString("user_height"));
		whatAreYouDTO.setAge(MainObject.getString("user_age"));
		whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
		whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
		whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));
		// whatAreYouDTO.setAge(MainObject.getString("user_note"));
		whatAreYouDTO.setRelationshipStatus(MainObject
				.getString("user_relationshipStatus"));
		whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
		whatAreYouDTO.setWhatAreYouName(MainObject
				.getString("user_whatAreYou_name"));
		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrests_id"));
			interestsDTO.setInterestName(interestJsonObject
					.getString("intrests_name"));
			interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
					+ interestJsonObject.getString("intrests_image"));
			interestsDTOs.add(interestsDTO);

		}
		for (int i = 0; i < jsonArrayImages.length(); i++) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));
			userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
					+ imagesJsonObject.getString("image_path"));
			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}
			userProfileImageDTOs.add(userProfileImageDTO);

		}

		for (int k = 0; k < userProfileImageDTOs.size(); k++) {
			UserProfileImageDTO dto = userProfileImageDTOs.get(k);
			if (dto.getIsImageActive()) {
				userProfileImageDTOs.remove(dto);
				userProfileImageDTOs.add(0, dto);
				break;
			}
		}
		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}

		for (int i = 0; i < jsonArrayLatLong.length(); i++) {
			JSONObject latLongJsonObject = jsonArrayLatLong.getJSONObject(i);
			LatLongDTO mDto = new LatLongDTO();
			mDto.setUser_id(latLongJsonObject.getString("User_id"));
			mDto.setLatitude(latLongJsonObject.getString("latitude"));
			mDto.setLongitude(latLongJsonObject.getString("longitude"));
			userDTO.setStreet(latLongJsonObject.getString("ll_street"));
			userDTO.setCity(latLongJsonObject.getString("ll_city"));
			userDTO.setState(latLongJsonObject.getString("ll_state"));
			userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
			userDTO.setCountry(latLongJsonObject.getString("ll_country"));
			latLongDTO = mDto;

		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

		}

		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		// sessionManager.setUserDetail(userDTO);
		return userDTO;

	}

	/*
	 * private void parseUserDataAndSetInSession(JSONObject mJsonObject) throws
	 * JSONException { UserDTO userDTO = new UserDTO(); LatLongDTO latLongDTO =
	 * new LatLongDTO(); WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
	 * List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	 * List<UserProfileImageDTO> userProfileImageDTOs = new
	 * ArrayList<UserProfileImageDTO>();
	 * 
	 * JSONObject MainObject = mJsonObject.getJSONObject("user");
	 * 
	 * JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
	 * JSONArray jsonArrayImages = mJsonObject.getJSONArray("images"); JSONArray
	 * jsonArrayLatLong = mJsonObject.getJSONArray("latlong"); //
	 * System.out.println(MainObject);
	 * 
	 * userDTO.setUserID(MainObject.getString("user_id"));
	 * userDTO.setEmail(MainObject.getString("user_email"));
	 * userDTO.setFirstName(MainObject.getString("user_fname"));
	 * userDTO.setLastName(MainObject.getString("user_lname"));
	 * userDTO.setProfileImage(MainObject.getString("user_image"));
	 * userDTO.setMobile(MainObject.getString("user_mobile"));
	 * userDTO.setAboutMe(MainObject.getString("user_note"));
	 * userDTO.setIsFirstTime(MainObject.getString("isFirstTime")); if
	 * (MainObject.getString("isonline").equals("available")) {
	 * userDTO.setIsOnline(AppConstants.ONLINE);
	 * 
	 * } else if (MainObject.getString("isonline").equals("away")) {
	 * userDTO.setIsOnline(AppConstants.ABSENT); } else {
	 * userDTO.setIsOnline(AppConstants.OFFLINE); }
	 * userDTO.setCommonFriends(MainObject.getString("mutualfriends"));
	 * 
	 * whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
	 * whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	 * whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	 * whatAreYouDTO.setHight(MainObject.getString("user_height"));
	 * whatAreYouDTO.setAge(MainObject.getString("user_age"));
	 * whatAreYouDTO.setWeight(MainObject.getString("user_weight")); //
	 * whatAreYouDTO.setAge(MainObject.getString("user_note"));
	 * whatAreYouDTO.setRelationshipStatus(MainObject
	 * .getString("user_relationshipStatus"));
	 * whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
	 * whatAreYouDTO.setWhatDoYouKrave(MainObject
	 * .getString("user_whatDoYouKrave"));
	 * 
	 * for (int i = 0; i < jsonArrayInterest.length(); i++) { JSONObject
	 * interestJsonObject = jsonArrayInterest.getJSONObject(i); InterestsDTO
	 * interestsDTO = new InterestsDTO();
	 * interestsDTO.setInterestId(interestJsonObject .getString("intrests_id"));
	 * interestsDTO.setInterestName(interestJsonObject
	 * .getString("intrests_name"));
	 * interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1 +
	 * interestJsonObject.getString("intrests_image"));
	 * interestsDTOs.add(interestsDTO);
	 * 
	 * } for (int i = 0; i < jsonArrayImages.length(); i++) { JSONObject
	 * imagesJsonObject = jsonArrayImages.getJSONObject(i); UserProfileImageDTO
	 * userProfileImageDTO = new UserProfileImageDTO();
	 * userProfileImageDTO.setImageId(imagesJsonObject .getString("image_id"));
	 * userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1 +
	 * imagesJsonObject.getString("image_path"));
	 * 
	 * userProfileImageDTOs.add(userProfileImageDTO);
	 * 
	 * } for (int i = 0; i < jsonArrayLatLong.length(); i++) { JSONObject
	 * latLongJsonObject = jsonArrayLatLong.getJSONObject(i); LatLongDTO mDto =
	 * new LatLongDTO();
	 * mDto.setUser_id(latLongJsonObject.getString("User_id"));
	 * mDto.setLatitude(latLongJsonObject.getString("latitude"));
	 * mDto.setLongitude(latLongJsonObject.getString("longitude"));
	 * userDTO.setStreet(latLongJsonObject.getString("ll_street"));
	 * userDTO.setCity(latLongJsonObject.getString("ll_city"));
	 * userDTO.setState(latLongJsonObject.getString("ll_state"));
	 * userDTO.setPostalCode(latLongJsonObject.getString("ll_zip"));
	 * userDTO.setCountry(latLongJsonObject.getString("ll_country")); latLongDTO
	 * = mDto;
	 * 
	 * } if (!"url".equals(userDTO.getProfileImage())) { Log.d("",
	 * "facebook image at first position in list"); UserProfileImageDTO
	 * userProfileImageDTO = new UserProfileImageDTO();
	 * userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
	 * userProfileImageDTO.setImagePath(userDTO.getProfileImage());
	 * userProfileImageDTOs.add(0, userProfileImageDTO); }
	 * userDTO.setWhatAreYouDTO(whatAreYouDTO);
	 * userDTO.setLatLongDTO(latLongDTO);
	 * userDTO.setInterestList(interestsDTOs);
	 * userDTO.setUserProfileImageDTOs(userProfileImageDTOs); // int online =
	 * contex.getUserAvailability(userDTO.getUserID()); // Log.d("",
	 * "is online id " + userDTO.getUserID() + " '" + online); //
	 * userDTO.setIsOnline("" + online); userDTOs.add(userDTO);
	 * 
	 * }
	 */
}
