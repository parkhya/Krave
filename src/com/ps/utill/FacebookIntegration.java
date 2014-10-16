package com.ps.utill;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.Activity_Login;
import com.krave.kraveapp.Activity_ProfileDetails;
import com.ps.adapters.PagerViewAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.SettingDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.services.GPSTracker;

public class FacebookIntegration {
	private Context context;
	private GraphUser graphUser;
	private List<FacebookLikesDTO> facebookLikesDTOs;
	private SessionManager sessionManager;
	private int check;
	OnfacebookDone onfacebookDone;
	private GPSTracker gpsTracker;

	public FacebookIntegration(Context context, int check, OnfacebookDone done) {
		super();
		this.context = context;
		this.check = check;
		onfacebookDone = done;
		sessionManager = new SessionManager(context);
		gpsTracker = new GPSTracker(context);
	}

	// public GraphUser getGraphUser() {
	// return graphUser;
	// }
	//
	// public List<FacebookLikesDTO> getFacebookLikes() {
	// return facebookLikesDTOs;
	// }
	/*
	 * public void facebookIntegration2() {
	 * 
	 * Session session = Session.getActiveSession();
	 * 
	 * if (session != null && session.isOpened()) {
	 * Request.executeMeRequestAsync(session, new GraphUserCallback() {
	 * 
	 * @Override public void onCompleted(GraphUser user, Response response) { if
	 * (user != null) { graphUser = user; calculateFacebookLikes(response); if
	 * (check == AppConstants.COME_FROM_LOGIN) { try { String email = user
	 * .getProperty( "email") .toString(); Log.d("Facebook Email ID..", email);
	 * new CheckUserAvailabilityAsynchTask()
	 * .execute(WebServiceConstants.BASE_URL +
	 * WebServiceConstants.CHECK_FACEBOOK_LOGIN_STATUS);
	 * 
	 * } catch (Exception e) { Toast.makeText( context,
	 * "facebook id is not verified", Toast.LENGTH_SHORT) .show(); } }
	 * 
	 * } } }); } else{ Session.openActiveSession((Activity)context, true, new
	 * Session.StatusCallback() {
	 * 
	 * @Override public void call(Session session, SessionState state, Exception
	 * exception) { // TODO Auto-generated method stub if(session.isOpened()){
	 * Request.executeMeRequestAsync(session, new GraphUserCallback() {
	 * 
	 * @Override public void onCompleted(GraphUser user, Response response) { if
	 * (user != null) { graphUser = user; calculateFacebookLikes(response); if
	 * (check == AppConstants.COME_FROM_LOGIN) { try { String email = user
	 * .getProperty( "email") .toString(); Log.d("Facebook Email ID..", email);
	 * new CheckUserAvailabilityAsynchTask()
	 * .execute(WebServiceConstants.BASE_URL +
	 * WebServiceConstants.CHECK_FACEBOOK_LOGIN_STATUS);
	 * 
	 * } catch (Exception e) { Toast.makeText( context,
	 * "facebook id is not verified", Toast.LENGTH_SHORT) .show(); } }
	 * 
	 * } } }); } } }); } }
	 */
	public void facebookIntegration() {

		Session currentSession = Session.getActiveSession();
		if (currentSession == null || currentSession.getState().isClosed()) {
			Session session = new Session.Builder(context).build();
			Session.setActiveSession(session);
			currentSession = session;
		}
		if (currentSession.isOpened()) {
			// Do whatever u want. User has logged in
			Session.openActiveSession((Activity) context, true,
					new Session.StatusCallback() {
						// callback when session changes state
						@Override
						public void call(final Session session,
								SessionState state, Exception exception) {
							if (session.isOpened()) {
								// make request to the /me API
								com.facebook.Request
										.executeMeRequestAsync(
												session,
												new com.facebook.Request.GraphUserCallback() {
													// callback after
													// Graph API
													// response with
													// user object
													@Override
													public void onCompleted(
															GraphUser user,
															com.facebook.Response response) {
														if (user != null) {
															Log.d("Response..",
																	String.format(
																			"Name: %s",
																			response.toString()));
															graphUser = user;
															if (check == AppConstants.COME_FROM_LOGIN) {
																try {
																	String email = user
																			.getProperty(
																					"email")
																			.toString();
																	Log.d("Facebook Email ID..",
																			email);
																	new CheckUserAvailabilityAsynchTask()
																			.execute(WebServiceConstants.BASE_URL
																					+ WebServiceConstants.CHECK_FACEBOOK_LOGIN_STATUS);

																} catch (Exception e) {
																	Toast.makeText(
																			context,
																			"facebook id is not verified",
																			Toast.LENGTH_SHORT)
																			.show();
																}
															}

															getFriends();
															calculateFacebookLikes(
																	response,
																	session);

														}
													}
												});
							}
						}
					});
		} else if (!currentSession.isOpened()) {
			// Ask for username and password
			Session.OpenRequest op = new Session.OpenRequest((Activity) context);
			op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
			op.setCallback(null);
			List<String> permissions = new ArrayList<String>();
			// permissions.add("publish_stream");
			permissions.add("user_likes");
			permissions.add("email");
			permissions.add("user_birthday");
			permissions.add("user_interests");

			permissions.add("read_friendlists");
			// permissions.add("publish_stream");
			op.setPermissions(permissions);
			Session session = new Session.Builder(context).build();
			Session.setActiveSession(session);
			session.openForRead(op);// (op);
		}
	}

	public void onActivityResultForFacebook(int requestCode, int resultCode,
			Intent data) {

		{
			Session.getActiveSession().onActivityResult((Activity) context,
					requestCode, resultCode, data);
			if (Session.getActiveSession() != null)
				Session.getActiveSession().onActivityResult((Activity) context,
						requestCode, resultCode, data);

			Session currentSession = Session.getActiveSession();
			if (currentSession == null || currentSession.getState().isClosed()) {
				Session session = new Session.Builder(
						context.getApplicationContext()).build();
				Session.setActiveSession(session);
				currentSession = session;
			}
			if (currentSession.isOpened()) {
				Session.openActiveSession((Activity) context, true,
						new Session.StatusCallback() {
							@Override
							public void call(final Session session,
									SessionState state, Exception exception) {
								if (session.isOpened()) {
									// make request to the /me API
									com.facebook.Request
											.executeMeRequestAsync(
													session,
													new com.facebook.Request.GraphUserCallback() {
														// callback after
														// Graph API
														// response with
														// user object
														@Override
														public void onCompleted(
																GraphUser user,
																com.facebook.Response response) {
															if (user != null) {
																Log.d("Response..",
																		String.format(
																				"Name: %s",
																				response.toString()));
																graphUser = user;
																if (check == AppConstants.COME_FROM_LOGIN) {
																	try {
																		String email = user
																				.getProperty(
																						"email")
																				.toString();
																		Log.d("Facebook Email ID..",
																				email);
																		new CheckUserAvailabilityAsynchTask()
																				.execute(WebServiceConstants.BASE_URL
																						+ WebServiceConstants.CHECK_FACEBOOK_LOGIN_STATUS);

																	} catch (Exception e) {
																		Toast.makeText(
																				context,
																				"facebook id is not verified",
																				Toast.LENGTH_SHORT)
																				.show();
																	}
																}

																getFriends();
																calculateFacebookLikes(
																		response,
																		session);

															}
														}
													});
								}
							}
						});

			}

		}

	}

	private void getFriends() {
		Session activeSession = Session.getActiveSession();
		if (activeSession.getState().isOpened()) {
			Request friendRequest = Request.newMyFriendsRequest(activeSession,
					new GraphUserListCallback() {
						@Override
						public void onCompleted(List<GraphUser> users,
								com.facebook.Response response) {
							Log.i("INFO", response.toString());

						}
					});
			Bundle params = new Bundle();
			params.putString("fields", "id, name, friends");
			friendRequest.setParameters(params);
			friendRequest.executeAsync();
		}
	}

	public void calculateFacebookLikes(com.facebook.Response response,
			Session activeSession) {
		new Request(activeSession, "/me/likes", null, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						/* handle the result */
						Log.d("", "facebook likes=" + response);

						try {
							GraphObject responseGraphObject = response
									.getGraphObject();
							JSONObject json = responseGraphObject
									.getInnerJSONObject();
							JSONArray Like = json.getJSONArray("data");
							// System.out.println(Like);
							JSONObject mJsonObject;
							facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
							for (int j = 0; j < Like.length(); j++) {
								FacebookLikesDTO mDto = new FacebookLikesDTO();
								mJsonObject = Like.getJSONObject(j);

								mDto.setImagePath(mJsonObject.getString("id"));
								mDto.setLikeName(mJsonObject.getString("name"));
								mDto.setCategory(mJsonObject
										.getString("category"));
								facebookLikesDTOs.add(mDto);
								Log.d("",
										"" + mJsonObject.getString("category"));
							}
							// PagerViewAdapter.facebookLikesDTOs =
							// facebookLikesDTOs;
							// PagerViewAdapter.graphUser = graphUser;
							Log.d("facebook likes", "facebook likes ="
									+ facebookLikesDTOs.size());
						} catch (Exception e) {
							// TODO: handle exception
						}
						PagerViewAdapter.facebookLikesDTOs = facebookLikesDTOs;
						PagerViewAdapter.graphUser = graphUser;
						onfacebookDone.onFbcompleate();
					}
				}).executeAsync();

		Log.i("Response..", String.format("Name: %s", response.toString()));

	}

	class CheckUserAvailabilityAsynchTask extends
			AsyncTask<String, Void, JSONArray> {
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
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// params.add(new BasicNameValuePair("action", "login_user"));
			params.add(new BasicNameValuePair("lat", ""
					+ gpsTracker.getLatitude()));
			params.add(new BasicNameValuePair("log", ""
					+ gpsTracker.getLongitude()));
			params.add(new BasicNameValuePair("user_email", graphUser
					.getProperty("email").toString()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("check user response", "" + json);
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
					System.out.print("facebook response : " + jsonArray);
					parseUserDataAndSetInSession(mJsonObject);
				} else if (vStatus.equals("failure")) {
					PagerViewAdapter.graphUser = graphUser;
					Intent intent = new Intent(context,
							Activity_ProfileDetails.class);
					intent.putExtra(AppConstants.COME_FROM,
							AppConstants.COME_FROM_FACEBOOK);
					context.startActivity(intent);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private void parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		SettingDTO settingDTO = new SettingDTO();
		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		// System.out.println(MainObject);
		userDTO.setLanguage(MainObject.getString("user_language"));
		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}

		settingDTO.setIsNotificationEnable(MainObject
				.getString("notification_status"));
		whatAreYouDTO.setFeet(MainObject.getString("user_feet"));
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
			interestsDTOs.add(interestsDTO);

		}

		for (int i = 0; i < jsonArrayImages.length(); i++) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));
			userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
					+ imagesJsonObject.getString("image_path"));
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));
			
			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}
			
			userProfileImageDTOs.add(userProfileImageDTO);
			

		}
		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
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
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		sessionManager.setUserDetail(userDTO);
		sessionManager.setSettingDetail(settingDTO);
		Intent mIntent = new Intent(context, Activity_Home.class);
		mIntent.putExtra("open", AppConstants.FRAGMENT_HOME);
		context.startActivity(mIntent);
		((Activity) context).finish();
		new GetChatHistory().execute(WebServiceConstants.BASE_URL
				+ WebServiceConstants.GET_HISTORY);
	}

	class GetChatHistory extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d("", "GET CHAT HISTORY");
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user", ""
					+ sessionManager.getUserDetail().getUserID()));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			KraveDAO databaseHelper = new KraveDAO(context);
			try {
				Log.d("", "GET CHAT HISTORY=" + jsonArray.length());
				Log.d("", "chat history=" + jsonArray);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject mJsonObject = jsonArray.getJSONObject(i);
					ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
					chatDetailsDTO.setFromuser(mJsonObject
							.getString("from_user"));
					chatDetailsDTO.setTouser(mJsonObject.getString("to_user"));
					chatDetailsDTO.setMessage(mJsonObject.getString("message"));
					chatDetailsDTO.setMeassageType(mJsonObject
							.getString("msg_type"));
					chatDetailsDTO.setTime(mJsonObject.getString("time"));
					databaseHelper.addChat(chatDetailsDTO);
				}

			} catch (JSONException e) {

			}

		}
	}
}
