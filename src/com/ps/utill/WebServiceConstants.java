package com.ps.utill;

import com.krave.kraveapp.Activity_Login;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class WebServiceConstants {
	// http://198.12.150.189/~simssoe/index.php?action=setlatlog&userid=&lat=&log=&street=&city=&state=&zip=&country=&GSMid=

	// http://parkhya.org/Android/krave_app/wl.txt
	// public static String BASE_URL =
	// "http://198.12.150.189/~simssoe/index.php?action=";
	// http://54.176.180.241/index.php?action=searchbysetting&userid=4
	public static String BASE_URL = "http://54.176.180.241/index.php?action=";
	// public static String BASE_URL_FILE =
	// "http://54.193.236.22/index.php?action=";
	public static String BASE_URL_FILE = "http://54.219.211.237/index.php?action=";

	// public static String BASE_URL =
	// "http://parkhya.org/Android/krave_app/index.php?action=";
	// http://parkhya.org/Android/krave_app/index.php?action=register_user&fname=yogesh&lname=pawar&address=indore&dob=10/05/2014&height=4.5&weight=10kg&notes=test&email=test2@gmail.com&password=123456

	// http://198.12.150.189/~simssoe/index.php?action=user_login&email=test2@gmail.com&password=123456
	// http://198.12.150.189/~simssoe/index.php?action=userinfo_by_emial&user_email=test1@gmail.com
	// http://parkhya.org/Android/krave_app/index.php?action=getAllIntrest

	// Add Lat & Log By useing user id
	// http://parkhya.org/Android/krave_app/index.php?action=setlatlog&userid=&lat=&log=&street=&city=&state=&zip=&country=&GSMid=
	// http://localhost/krave_app/index.php?action=userifo_by_name&key=p
	//
	// parkhya.org/Android/krave_app/index.php?action=changepassword&oldpassword=123&newpassword=1234&userid=1

	// http://54.176.180.241/index.php?action==showuserBydistance&lat=22.718713&long=75.888273&distance=10

	// action=searchsettingEdit&userid=1&min_val=25&max_val=45&radius=150&interest=1,2,3&notification1

	// http://parkhya.org/Android/krave_app/index.php?action=showsetting&userid=1

	// http://parkhya.org/Android/krave_app/index.php?action=ShowDailyKrave

	// http://parkhya.org/Android/krave_app/index.php?action=userifo_by_name&key=p
	// http://parkhya.org/Android/krave_app/index.php?action=searchByUserIntrestWAU&userid=6
	// http://parkhya.org/Android/krave_app/index.php?action=getUserprofile&userid=6

	// http://parkhya.org/Android/krave_app/index.php?action=deleteUser&userid=2

	// http://parkhya.org/Android/krave_app/index.php?action=sendFriendRequest&user_id=2&friend_id=1
	// http://54.176.180.241/index.php?action=getfriends&user_id=107

	// http://198.12.150.189/~simssoe/index.php?action=pendingRequest&user_id=108

	// http://parkhya.org/Android/krave_app/index.php?action=acceptfriendrequest&user_id=107&friend_id=77
	// http://parkhya.org/Android/krave_app/index.php?action=cancelrequest&user_id=2&friend_id=3
	// http://parkhya.org/Android/krave_app/index.php?action=blockuser&user_id=8&friend_id=12

	// http://parkhya.org/Android/krave_app/index.php?action=showuserByall&user_id=77&page=1
	// http://198.12.150.189/~simssoe/krave/index.php?action=searchbysetting&userid=1&page=1&records=12;
	// http://198.12.150.189:9090/plugins/presence/status?jid=
	// +"userid"
	// + @ip-198-12-150-189.secureserver.net&type=xml

	// http://198.12.150.189/~simssoe/krave/index.php?action=searchbysetting&userid=1&page=1&records=12&status=online;
	// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&city=indore&userid=1&records=12&status=online&page=1
	//
	// http://198.12.150.189/~simssoe/index.php?action=searchbyNearCity&nearme=yes&userid=1&records=12&status=online&page=1
	// http://198.12.150.189/~simssoe/index.php?action=logout&user_id=300
	// http://198.12.150.189/~simssoe/index.php?action=forgotpassword&email=parkhya.developer@gmail.com
	// http://198.12.150.189/~simssoe/index.php?action=conversation_reply&user_one=4&user_two=1&time=14526987355&&reply=hello&ip=182.72.80.18
	// http://198.12.150.189/~simssoe/index.php?action=conversationHistry&user=1
	// http://198.12.150.189/~simssoe/index.php?action=conversation_reply&user_one=2&user_two=1&time=14526987355&&reply=hello&ip=182.72.80.18&msg_type=text
	// http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002
	// 198.12.150.189/~simssoe/index.php?action=updateReadStatus&user_id=4&friend_id=108

	/* right menu web services */

	// http://parkhya.org/Android/krave_app/index.php?action=alllist&userid=1
	// http://parkhya.org/Android/krave_app/index.php?action=addlist&userid=1&title=test
	// http://198.12.150.189/~simssoe/index.php?action=updatelist&userid=1&title=testsdfsdfsdf&listid=3
	// http://198.12.150.189/~simssoe/index.php?action=deletelist&listid=2&userid=1

	// http://parkhya.org/Android/krave_app/index.php?action=adduserlist&userid=1&listid=1
	// http://parkhya.org/Android/krave_app/index.php?action=updateuserlist&userid=1&id=1&listid=2
	// http://parkhya.org/Android/krave_app/index.php?action=alluserlist&userid=1
	// Taylor@campwestagency.com
	// 123
	// add_dude_array
	// delete_dude_array

	// http://198.12.150.189/~simssoe/index.php?action=alluserlistById&userid=1
	// http://parkhya.org/Android/krave_app/index.php?action=adduserdude&list_id=1&adddude=

	// http://parkhya.org/Android/krave_app/index.php?action=deleteuserdude&list_id=1&deletedude=

	public static String USER_REGISTRATION = "register_user";
	public static String USER_LOGIN = "user_login";
	public static String FORGOT_PASSWORD = "forgotpassword";
	public static String CHANGE_PASSWORD = "changepassword";
	public static String CHECK_FACEBOOK_LOGIN_STATUS = "userinfo_by_emial";
	public static String SEND_USER_LAT_LONG = "setlatlog";
	public static String GET_ALL_INTEREST_AND_WHAT_ARE_YOU = "getAllIntrest";
	public static String USER_PROFILE_UPDATE = "updateuser";
	public static String GET_DUDES_FROM_PARTICULAR_RANGE = "showuserBydistance";
	public static String GET_SETTING = "showsetting";
	public static String UPDATE_SETTING = "searchsettingEdit";
	public static String GET_DAILY_KRAVE = "ShowDailyKrave";
	public static String SEARCH_USER = "userifo_by_name";
	public static String FIND_DUDES = "";
	public static String GET_MATCHES = "searchByUserIntrestWAU";
	public static String DELETE_ACCOUNT = "deleteUser";
	public static String GET_DUDE_BY_ID = "getUserprofile";
	public static String GET_ALL_LIKE_DUDE = "getfriends";
	public static String SEND_LIKE_REQUESTS = "sendFriendRequest";
	public static String GET_ALL_REQUEST = "pendingRequest";
	public static String ACCEPT_LIKE_REQUEST = "acceptfriendrequest";
	public static String REJECT_LIKE_REQUEST = "cancelrequest";
	public static String SEARCH_BY_SETTING = "searchbysetting";
	public static String SEARCH_BY_CITY_OR_NEAR_ME = "searchbyNearCity";
	public static String BLOCK_DUDE = "blockuser";
	public static String LOGOUT = "logout";
	public static String SEND_FEEDBACK = "send_feedback";

	/* right menu web services */

	public static String GET_DUDE_REQUIST_LIST = "";
	public static String GET_USER_LIST = "alluserlistById";
	public static String CREATE_NEW_LIST = "addlist";
	public static String UPDATE_LIST = "updatelist";
	public static String DELETE_LIST = "deletelist";

	public static String ADD_DUDES_IN_LIST = "adduserdude";
	public static String DELETE_DUDES_FROM_LIST = "deleteuserdude";
	public static String XMPP_USER_REGISTRATION = "http://54.176.180.241:9090/plugins/userService/userservice?";

	/* chat table sevice */

	public static String UPLOAD_FILE = "uploadFiles";// doc
	public static String SEND_MESSAGE_TO_SERVER = "conversation_reply";
	public static String GET_HISTORY = "conversationHistry";
	public static String DELETE_CHAT = "deleteChatHistory";
	public static String UNREAD_MESSAGE_SERVICE_OF_PARTICULAR_USER = "updateReadStatus";

	//
	// [interest] => Array
	// (
	// [0] => Array
	// (
	// [0] => 2
	// )
	// [1] => Array
	// (
	// [0] => 8
	// )
	// [2] => Array
	// (
	// [0] => 4
	// )
	// [3] => Array
	// (
	// [0] => 5
	// )
	// [4] => Array
	// (
	// [0] => 6
	// )
	// )
	// )
	// https://docs.google.com/spreadsheets/d/1f-7qt2JvZ-G0WJoxg3j-gbhdb2feiOfng2YF9A_KLNA/edit#gid=1703958302

	// http://parkhya.org/Android/krave_app/index.php?action=updateuser
	//
	// language=0,1
	// user_id
	// fname
	// lname
	// mobile
	// email
	// password
	// isFirstTime
	// user_whatAreYou
	// feet="'
	// inches=""
	// meters=""
	// height
	// weight
	// age
	// user_relationshipStatus
	// user_whatDoYouKrave
	// user_note
	// user_facebook_image
	// interest
	// delete_image
	// thumb_image
	//

	public static boolean isOnline(Context activity) {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {

			Toast.makeText(activity, "Please check network connection.",
					Toast.LENGTH_SHORT).show();

			return false;
		}

	}

	public static boolean isOnlineWitoutToast(Context activity) {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {

			return false;
		}

	}
	// registration parameters

	// language,user_type("facebook","general"),fname,lname,mobile,email,password,isFirstTime("0"),user_whatAreYou,feet,
	// inches,meters,height,weight,age,user_relationshipStatus,user_whatDoYouKrave,user_note,user_facebook_image("facebook.com/example.jpg","url")
	// interest([]),thumb_image([])

	// 1)detail functionality of find guys and get matches with screen after
	// home screen
	//
	// 2)chat screen: -
	//
	// i) what function does perform on plus icon
	// ii) what function does perform on arrow icon
	//
	// 3)report screen :- details of spam ,inappropriateand and other option and
	// how to manage this data on server;
	//
	// 4) full detail and flow of this screen
	// https://trello-attachments.s3.amazonaws.com/5396a650f5c60d6dca176bb2/539a9816316a556f0f583384/ba7b6ff0f24658964e33c3b059b0fa9b/Krave_App_Functionality_Map_DO_YOU_KRAVE.pdf
	//
	//
	// please give detail app flow with functionality of each component?
	//
	// (02:51:28 IST) <ERROR>:
	// Your pal didn't receive the packet. He or she is offline maybe.
	// (02:53:48 IST) Romesh:
	// 1)detail functionality of find guys and get matches with screen after
	// home screen
	//
	// 2)chat screen: -
	//
	// i) what function does perform on plus icon
	// ii) what function does perform on arrow icon
	//
	// 3)report screen :- details of spam ,inappropriateand and other option and
	// how to manage this data on server;
	//
	// 4) full detail and flow of this screen
	// https://trello-attachments.s3.amazonaws.com/5396a650f5c60d6dca176bb2/539a9816316a556f0f583384/ba7b6ff0f24658964e33c3b059b0fa9b/Krave_App_Functionality_Map_DO_YOU_KRAVE.pdf
	//
	//
	// please give detail app flow with functionality of each component?

	// (06:06:58 IST) Dhirendra:
	// https://198.12.150.189:2083/
	// simssoe
	// Parkhya123**
	//
	//
	//
	// 1) login:----- inflate exception
	//
	// 2) load image on alert
	//
	// 3) date hidden issue
	//
	// 4) chat profile pick
	//
	// 5) profile interest red

	// 07-25 10:03:57.620: I/System.out(25805): AsyncTask #7 calls detatch()
	// 07-25 10:03:57.680: I/System.out(25805): Responce....Admin
	// Reg.String<pre>Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [action] => test
	// 07-25 10:03:57.680: I/System.out(25805): [lat] => 22.7298595
	// 07-25 10:03:57.680: I/System.out(25805): [lon] => 75.8633164
	// 07-25 10:03:57.680: I/System.out(25805): [language] => 0
	// 07-25 10:03:57.680: I/System.out(25805): [user_type] => general
	// 07-25 10:03:57.680: I/System.out(25805): [fname] => gsgsg
	// 07-25 10:03:57.680: I/System.out(25805): [lname] => hzhs
	// 07-25 10:03:57.680: I/System.out(25805): [mobile] => 9764385824
	// 07-25 10:03:57.680: I/System.out(25805): [email] =>
	// sahu.romesh0@gmail.com
	// 07-25 10:03:57.680: I/System.out(25805): [password] => 123456
	// 07-25 10:03:57.680: I/System.out(25805): [isFirstTime] =>
	// 07-25 10:03:57.680: I/System.out(25805): [user_whatAreYou] => 3
	// 07-25 10:03:57.680: I/System.out(25805): [feet] => 3/0
	// 07-25 10:03:57.680: I/System.out(25805): [inches] =>
	// 07-25 10:03:57.680: I/System.out(25805): [meters] =>
	// 07-25 10:03:57.680: I/System.out(25805): [height] => 3/0
	// 07-25 10:03:57.680: I/System.out(25805): [heightUnit] => US
	// 07-25 10:03:57.680: I/System.out(25805): [weight] => 10/0
	// 07-25 10:03:57.680: I/System.out(25805): [weightUnit] => MT
	// 07-25 10:03:57.680: I/System.out(25805): [age] => 1/2/1990
	// 07-25 10:03:57.680: I/System.out(25805): [user_relationshipStatus] => 2
	// 07-25 10:03:57.680: I/System.out(25805): [user_whatDoYouKrave] => 3
	// 07-25 10:03:57.680: I/System.out(25805): [user_note] => cfffgfg
	// 07-25 10:03:57.680: I/System.out(25805): [user_facebook_image] => url
	// 07-25 10:03:57.680: I/System.out(25805): [interest] => Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [0] => Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [0] => 5
	// 07-25 10:03:57.680: I/System.out(25805): )
	// 07-25 10:03:57.680: I/System.out(25805): [1] => Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [0] => 4
	// 07-25 10:03:57.680: I/System.out(25805): )
	// 07-25 10:03:57.680: I/System.out(25805): [2] => Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [0] => 6
	// 07-25 10:03:57.680: I/System.out(25805): )
	// 07-25 10:03:57.680: I/System.out(25805): )
	// 07-25 10:03:57.680: I/System.out(25805): [user_facebook_Interest] => No
	// 07-25 10:03:57.680: I/System.out(25805): )
	// 07-25 10:03:57.680: I/System.out(25805): Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [thumb_image0] => Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [name] => test625201495758.png
	// 07-25 10:03:57.680: I/System.out(25805): [type] =>
	// application/octet-stream
	// 07-25 10:03:57.680: I/System.out(25805): [tmp_name] => /tmp/phpnBRqvN
	// 07-25 10:03:57.680: I/System.out(25805): [error] => 0
	// 07-25 10:03:57.680: I/System.out(25805): [size] => 48817
	// 07-25 10:03:57.680: I/System.out(25805): )
	// 07-25 10:03:57.680: I/System.out(25805): [thumb_image5] => Array
	// 07-25 10:03:57.680: I/System.out(25805): (
	// 07-25 10:03:57.680: I/System.out(25805): [name] => test625201495820.png
	// 07-25 10:03:57.680: I/System.out(25805): [type] =>
	// application/octet-stream
	// 07-25 10:03:57.680: I/System.out(25805): [tmp_name] => /tmp/phpYnliRD
	// 07-25 10:03:57.680: I/System.out(25805): [error] => 0
	// 07-25 10:03:57.680: I/System.out(25805): [size] => 32383
	// 07-25 10:03:57.680: I/System.out(25805): )
	// 07-25 10:03:57.685: I/System.out(25805): )
	// 07-25 10:03:57.685: I/System.out(25805): ArrayArray
	// 07-25 10:03:57.685: I/System.out(25805): (
	// 07-25 10:03:57.685: I/System.out(25805): [0] => 5
	// 07-25 10:03:57.685: I/System.out(25805): )
	// 07-25 10:03:57.685: I/System.out(25805): ArrayArray
	// 07-25 10:03:57.685: I/System.out(25805): (
	// 07-25 10:03:57.685: I/System.out(25805): [0] => 4
	// 07-25 10:03:57.685: I/System.out(25805): )
	// 07-25 10:03:57.685: I/System.out(25805): ArrayArray
	// 07-25 10:03:57.685: I/System.out(25805): (
	// 07-25 10:03:57.685: I/System.out(25805): [0] => 6
	// 07-25 10:03:57.685: I/System.out(25805): )

	// Professional sports team
	// Musician/band
	// Book
	// Sport

	// http://stackoverflow.com/questions/15815373/android-how-to-scale-images-without-memory-exception

	// 1. From gallery
	// 2. Two Options displayed (Complete action using : Gallery | Photos)
	// 3. Choose "Photos" option
	// 4. There I found my videos and selected one of them and app crashed
	// [4:24:56 PM] Sam Darbinyan: I did it again
	// [4:24:58 PM] Sam Darbinyan: i will send log
	// [4:25:10 PM] Sam Darbinyan: USER_COMMENT=null
	// ANDROID_VERSION=4.4.4
	// APP_VERSION_NAME=1.0
	// BRAND=google
	// PHONE_MODEL=Nexus 5
	// CUSTOM_DATA=
	// STACK_TRACE=java.lang.NullPointerException
	// at com.ps.krave.FragmentChatMatches$1.run(FragmentChatMatches.java:616)
	// at android.os.Handler.handleCallback(Handler.java:733)
	// at android.os.Handler.dispatchMessage(Handler.java:95)
	// at android.os.Looper.loop(Looper.java:136)
	// at android.app.ActivityThread.main(ActivityThread.java:5001)
	// at java.lang.reflect.Method.invokeNative(Native Method)
	// at java.lang.reflect.Method.invoke(Method.java:515)
	// at
	// com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:785)
	// at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:601)
	// at dalvik.system.NativeStart.main(Native Method)
	// [4:25:17 PM] Parkhya Business: ok
	// [4:25:40 PM] Sam Darbinyan: Also I found another bug
	// [4:25:52 PM] Sam Darbinyan: again the SEND button in CHAT screen did not
	// act, it happened
	// [4:26:33 PM] Sam Darbinyan: when app was in background for long time and
	// I opened it opened on CHAT screen, I entered text tapped on SENd nothing
	// happened, I chose image it was not sent
	// [4:26:41 PM] Sam Darbinyan: but when i killed app and reopened it works

	// 08-23 17:28:39.665: I/System.out(11721): AsyncTask #8 calls detatch()
	// 08-23 17:28:39.720: I/System.out(11721): Responce....Admin
	// Reg.String<pre>Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [action] => test
	// 08-23 17:28:39.720: I/System.out(11721): [language] => 0
	// 08-23 17:28:39.720: I/System.out(11721): [user_id] => 108
	// 08-23 17:28:39.720: I/System.out(11721): [fname] => shiv123
	// 08-23 17:28:39.720: I/System.out(11721): [lname] => sankar
	// 08-23 17:28:39.720: I/System.out(11721): [mobile] => 9824576464
	// 08-23 17:28:39.720: I/System.out(11721): [email] => shiv@gmail.com
	// 08-23 17:28:39.720: I/System.out(11721): [password] =>
	// 08-23 17:28:39.720: I/System.out(11721): [isFirstTime] => 0
	// 08-23 17:28:39.720: I/System.out(11721): [user_whatAreYou] => (null)
	// 08-23 17:28:39.720: I/System.out(11721): [feet] => 2/41
	// 08-23 17:28:39.720: I/System.out(11721): [inches] =>
	// 08-23 17:28:39.720: I/System.out(11721): [meters] =>
	// 08-23 17:28:39.720: I/System.out(11721): [height] => 2/41
	// 08-23 17:28:39.720: I/System.out(11721): [heightUnit] =>
	// 08-23 17:28:39.720: I/System.out(11721): [weight] => 197/0
	// 08-23 17:28:39.720: I/System.out(11721): [weightUnit] =>
	// 08-23 17:28:39.720: I/System.out(11721): [age] => 1/1/1990
	// 08-23 17:28:39.720: I/System.out(11721): [user_relationshipStatus] => 2
	// 08-23 17:28:39.720: I/System.out(11721): [user_whatDoYouKrave] =>
	// 08-23 17:28:39.720: I/System.out(11721): [user_note] => Asderrttuu
	// 08-23 17:28:39.720: I/System.out(11721): [user_facebook_image] => url
	// 08-23 17:28:39.720: I/System.out(11721): [interest] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 1
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [1] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 2
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [2] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 5
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [3] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 6
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [4] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 7
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [5] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 8
	// 08-23 17:28:39.720: I/System.out(11721): )updateuser
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [user_facebook_Interest] => No
	// 08-23 17:28:39.720: I/System.out(11721): [delete_image] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 429
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [1] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 545
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [thumb_image0] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [name] => test723201417282.png
	// 08-23 17:28:39.720: I/System.out(11721): [type] =>
	// application/octet-stream
	// 08-23 17:28:39.720: I/System.out(11721): [tmp_name] => /tmp/phpRraIfU
	// 08-23 17:28:39.720: I/System.out(11721): [error] => 0
	// 08-23 17:28:39.720: I/System.out(11721): [size] => 860493
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): [thumb_image1] => Array
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [name] => test7232014172722.png
	// 08-23 17:28:39.720: I/System.out(11721): [type] =>
	// application/octet-stream
	// 08-23 17:28:39.720: I/System.out(11721): [tmp_name] => /tmp/phpAHBCWa
	// 08-23 17:28:39.720: I/System.out(11721): [error] => 0
	// 08-23 17:28:39.720: I/System.out(11721): [size] => 742752
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.720: I/System.out(11721): ArrayArray
	// 08-23 17:28:39.720: I/System.out(11721): (
	// 08-23 17:28:39.720: I/System.out(11721): [0] => 1
	// 08-23 17:28:39.720: I/System.out(11721): )
	// 08-23 17:28:39.725: I/System.out(11721): ArrayArray
	// 08-23 17:28:39.725: I/System.out(11721): (
	// 08-23 17:28:39.725: I/System.out(11721): [0] => 2
	// 08-23 17:28:39.725: I/System.out(11721): )
	// 08-23 17:28:39.725: I/System.out(11721): ArrayArray
	// 08-23 17:28:39.725: I/System.out(11721): (
	// 08-23 17:28:39.725: I/System.out(11721): [0] => 5
	// 08-23 17:28:39.725: I/System.out(11721): )// 08-23 17:28:39.725:
	// I/System.out(11721): (
	// 08-23 17:28:39.725: I/System.out(11721): [0] => 2
	// 08-23 17:28:39.725: I/System.out(11721): )
	// 08-23 17:28:39.725: I/System.out(11721): ArrayArray
	// 08-23 17:28:39.725: I/System.out(11721): (
	// 08-23 17:28:39.725: I/System.out(11721): [0] => 5
	// 08-23 17:28:39.725: I/System.out(11721): )
	// 08-23 17:28:39.725: I/System.out(11721): ArrayArray
	// 08-23 17:28:39.725: I/System.out(11721): (
	// 08-23 17:28:39.725: I/System.out(11721): [0] => 6
	// 08-23 17:28:39.725: I/System.out(11721): )
	// 08-23 17:28:39.725: I/System.out(11721): ArrayArray
	// 08-23 17:28:39.725: I/System.out(11721): (
	// 08-23 17:28:39.725: I/System.out(11721): [0] => 7
	// 08-23 17:28:39.725: I/System.out(11721): )
	// 08-23 17:28:39.725: I/System.out(11721): ArrayArray
	// 08-23 17:28:39.725: I/System.out(11721): (
	// 08-23 17:28:39.725: I/System.out(11721): [0] => 8
	// 08-23 17:28:39.725: I/System.out(11721): )

	// Minakshi currently in Google Play there is Krave app, but I think if user
	// will updated it with our version then app will not work
	// [1:53:25 PM] Sam Darbinyan: so are there any way that we can ask user to
	// delete current version and only then install new one

	//
	// welcome,login,forgot password,
	// registration ,
	// find dudes,dude profile,user profile,
	// daily krave,setting,contact,
	// chat matches,chat screen ,
	// block dude,report dude
	// left panel,right panel,push Notification screens,other alerts and screens

	// (06:04:56 IST) Romesh:
	// welcome,login,forgot password, :---03/09
	// registration , find dudes, :-04/09
	// dude profile,user profile, 05
	// daily krave,setting,contact, 06
	// chat matches,chat screen ,
	// block dude,report dude :--08
	// left panel,right panel,push Notification screens,other alerts and
	// screens:-09
	// (06:06:42 IST) minakshi:
	// login screen | you have enter wrong username or password | failure
	// [5:42:46 PM] Parkhya Business: login screen | login successfully| success

	// http://54.241.85.74:9090/plugins/userService/userservice?type=add&secret=7bDFD4TP&username=555&password=555&name=yogesh%20pawar&email=ypawar979@gmail.com
	// -- Register
	//
	//
	// http://54.241.85.74:9090/plugins/presence/status?jid=109@ip-10-199-23-53&type=xml
	// --Online Status
	//
	//
	// http://54.241.85.74:9090/plugins/userService/userservice?type=delete_roster&secret=7bDFD4TP&username=555&item_jid=ypawar979@gmail.com
	// -- Delete User
	// (03:29:21 IST) Yogesh:
	// http://54.176.180.241/index.php?action=searchbysetting&userid=4
	// (03:31:01 IST) Yogesh:
	// http://54.176.180.241/krave_image/14077365370_.png

	// Google Play account details details pprogramming 
	//
	// al4alvaro@gmail.com
	//
	// Pass:Maggie651
	//
	// com.krave.kraveapp
	//
	//
	// 7eVfEkcBMS1cfYKhxq7UyRbi2L0=

	// alvaroballesteros
	//
	// 1C:F4:B9:0C:F3:72:00:06:98:4A:F6:28:82:28:1A:C3:B1:FA:04:43
	//
	//
	// 7eVfEkcBMS1cfYKhxq7UyRbi2L0=
	//
	// HPS5DPNyAAaYSvYogigaw7H6BEM=

	// http://54.176.180.241/_pma/
	// username: simssoe_krave
	// pass: krave@123

	// Android: IllegalStateException (@ViewPager:populate:962) {main}

	// Android: NullPointerException (@XMPPConnection:createPacketCollector:758)
	// {AsyncTask #1}

	// Android: NullPointerException (@XMPPConnection:createPacketCollector:758)
	// {AsyncTask #1}

	// Android: IllegalStateException (@XMPPConnection:sendPacket:665)
	// {AsyncTask #1}

	// Android: NullPointerException
	// (@PagerViewAdapter:compareFacebookInterest:1900) {main}

	// Android: NullPointerException
	// (@FragmentSetting$UpdateSetting:doInBackground:734) {AsyncTask #3}

}
