package com.ps.utill;

import android.widget.LinearLayout;

public class AppConstants {

	/* constants for intent */
	public static final String COME_FROM = "come_from";
	public static final int COME_FROM_NORMAL_REGISTRATION = 0;
	public static final int COME_FROM_FACEBOOK = 1;
	public static final int COME_FROM_UPDATE_PROFILE = 2;
	public static final int COME_FROM_ALL_GUYS = 0;
	public static final int COME_FROM_LISTS = 1;
	public static final int PUSH_NOTIFICATION = 500;
	/* constants for IMAGE BASE PATH */
	// public static final String BASE_IMAGE_PATH =
	// "http://www.parkhya.org/Android/krave_app/krave_image/";
	// public static final String BASE_IMAGE_PATH_1 =
	// "http://www.parkhya.org/Android/krave_app/";
	// public static final String BASE_IMAGE_PATH =
	// "http://198.12.150.189/~simssoe/krave/krave/image/";s
	// public static final String BASE_IMAGE_PATH_1 =
	// "http://198.12.150.189/~simssoe/krave/";

	// http://54.176.180.241/krave_image/14077365370_.png
	public static final String BASE_IMAGE_PATH_FOR_DAILY_KRAVE = "http://54.176.180.241/admin/";
	public static final String BASE_IMAGE_PATH_FOR_INTEREST = "http://54.219.211.237";
	public static final String BASE_IMAGE_PATH_1 = "http://54.219.211.237/";
	public static final String UN_UPDATED_IMAGE = "url";
	public static final String AWATAR_IAMGE = "http://54.176.180.241/krave_image/com_facebook_profile_picture_blank_portrait.png";
	public static final int COME_FROM_LOGIN = 0;
	public static final int COME_FROM_INTEREST = 1;
	public static final String FACEBOOK_IMAGE = "facebook_image";
	public static final int NOTIFICATION_ENABLE = 1;
	public static final String COMPARE_PHOTO_STRING = "54.219.211.237";
	public static final int NOTIFICATION_DISABLE = 0;

	/* CONSTANT FOR BACK BUTTON */
	public static final int BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE = 1;
	public static final int BACK_BUTTON_FROM_SETTING = 2;
	public static final int BACK_BUTTON_FROM_DAILY_KRAVE_DETAIL = 3;
	public static final int BACK_BUTTON_FROM_DETAIL_DUDES_PROFILE_WITH_CHAT = 4;
	public static final int BACK_BUTTON_FROM_UPDATE_PROFILE = 5;
	public static final int BACK_BUTTON_FROM_UPDATE_IMAGE_PROFILE = 6;
	public static final int BACK_BUTTON_FROM_DUDES_PROFILE = 7;
	/* FRAGMENT CONSTANT */

	public static final int FRAGMENT_HOME = 1;
	public static final int FRAGMENT_PROFILE = 2;
	public static final int FRAGMENT_FIND_DUDES = 3;
	public static final int FRAGMENT_GET_MATCHES = 4;
	public static final int FRAGMENT_DAILY_KRAVE = 5;
	public static final int FRAGMENT_CHATT_MATCHES = 50;
	public static final int FRAGMENT_SETTING = 7;
	public static final int FRAGMENT_MAP = 8;
	public static final int FRAGMENT_CHAT_FRIEND = 6;
	public static final int FRAGMENT_DUDES_PROFILE = 9;

	public static final int FRAGMENT_DETAIL_DUDES_PROFILE = 11;
	public static final int FRAGMENT_DUDES_CONGRATULATION = 12;
	public static final int FRAGMENT_DAILY_KRAVE_DETAIL = 13;
	public static final int FRAGMENT_UPDATE_PROFILE = 14;

	/* LANGUAGE CONSTANT */
	public static final String ENGLISH = "0";
	public static final String SPANISH = "1";

	/* right menu constant */

	public static final int CREATE_NEW_LIST = 1;
	public static final int DELETE_DUDE_FROM_LIST = 2;
	public static final int ADD_DUDE_TO_LIST = 3;
	public static final int EDIT_LIST = 4;
	public static final int PUSH_NOTIFICATION_ACTIVITY = 5;
	public static final int FRIEND_NOTIFICATION_ACTIVITY = 7;
	public static final int FRIEND_NOTIFICATION_RESULT1 = 8;
	public static final int FRIEND_NOTIFICATION_RESULT2 = 9;

	/* font style id */
	public static final int HelveticaNeueLTStd_Bd = 1;
	public static final int HelveticaNeueLTStd_Lt = 2;
	public static final int HelveticaNeueLTStd_Md = 3;
	public static final int HelveticaNeueLTStd_Roman = 4;

	/* user availability constant */
	public static final String OFFLINE = "0";
	public static final String ONLINE = "1";
	public static final String ABSENT = "2";
	public static final String ALL_DUDE = "all";

	/* hight Weight unit constant */
	public static final String HEIGHT_IN_METER = "MT";
	public static final String HEIGHT_IN_FEET = "US";

	public static final String WEIGHT_IN_KILOGRAM = "MT";
	public static final String WEIGHT_IN_POUND = "US";
	/* chat notification constant */

	// http://54.241.85.74:9090/plugins/presence/status?jid=109@ip-10-199-23-53&type=xml

	public static final String CHAT_SERVER_ID = "@ip-10-199-23-53";
	public static final String CHAT_NOTIFICATION_TYPE = "notification_type";
	public static final String CHAT_NOTIFICATION_STRING = "notification_string";
	public static final int CHAT_APP_DOES_NOT_VISIBLE = 1;
	public static final int CHAT_NOT_IN_CHAT_FRAGMENT = 2;
	public static final int CHAT_OTHER_DUDE_NOTIFICATION = 3;

	public static final String CHAT_FROM = "0";
	public static final String CHAT_TO = "1";

	public static final String MESSAGE_TYPE_TEXT = "<text>";
	public static final String MESSAGE_TYPE_IMAGE = "<image>";

	/* facebook like enable or disbale */
	public static final String FACEBOOK_LIKE_ENABLE = "Yes";
	public static final String FACEBOOK_LIKE_DISABLE = "No";

	/* image active or not */
	public static final String IMAGE_ACTIVE = "1";
	public static final String IMAGE_IN_ACTIVE = "0";

	/* crop image */

	// What happens in Vegas, Stays in Vegas! The app that keeps your Vegas
	// adventures a secret!
	// This application basically design for tourist and las vegas people.
	// All users that are located in Las Vegas will be able to share pictures or
	// videos and view content posted by other users.
	// User must be located in Las Vegas, NV to login. If user are not in Las
	// Vegas, NV, user will not be able to view or post to the system.
	// All content will remain in the system for up to 30 days and then be
	// automatically deleted from the system unless user login within 30 days
	// from Las Vegas, NV. If user attempt to login from an area other than Las
	// Vegas, NV, then his account and content will be deleted automatically.
	// website link:- http://whathappensinvegasapp.com
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	// Krave is a location-based app for curious guys to meet and interact.
	// Krave is great place to get to know guys to date, search for a
	// relationship, friendships, socializing and networking. Krave provides the
	// ability to upload multiple profile photos, send pictures and text
	// messages (chatting)and send your location to other members. User can
	// reveal your exact location on the Krave map or you can be discrete.
	//
	// Create account, upload your photos, and enter your profile information
	// and start searching for guys.
	//
	//
	//
	//
	//
	//
	// Trysunrise(sunrise sunset alarm clock):-
	// This is basically a alarm clock application based on sunrise sunset time.
	// User is able to add alarm according to its geo location sunrise sunset
	// time in particular week days.
	// User select location from gps or google map.
	//
	//
	//
	// Representative tracking system :-This application basically design for
	// company and enterprise

}
