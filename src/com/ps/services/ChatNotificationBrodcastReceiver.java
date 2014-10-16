package com.ps.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.krave.kraveapp.Activity_Block_Dude;
import com.krave.kraveapp.Activity_ChangePassword;
import com.krave.kraveapp.Activity_DudeGallery;
import com.krave.kraveapp.Activity_Friens_Request_Accept;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.Activity_Push_Notification;
import com.krave.kraveapp.Activity_R_AddGuys;
import com.krave.kraveapp.Activity_R_CreateNewList;
import com.krave.kraveapp.Activity_R_EditList;
import com.krave.kraveapp.Activity_Report_Dude;
import com.krave.kraveapp.Activity_ShowImage;
import com.krave.kraveapp.FragmentChatMatches;
import com.krave.kraveapp.FragmentHome;
import com.krave.kraveapp.MyApps;
import com.ps.utill.AppConstants;
import com.ps.utill.SessionManager;

public class ChatNotificationBrodcastReceiver extends BroadcastReceiver {
	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";

	@Override
	public void onReceive(Context context, Intent intent) {
		SessionManager sessionManager = new SessionManager(context);
		// int notificationType = intent.getExtras().getInt(
		// AppConstants.CHAT_NOTIFICATION_TYPE);
		// String message = intent.getExtras().getString(
		// AppConstants.CHAT_NOTIFICATION_STRING);
		if (Activity_Push_Notification.getActivityObject() != null) {
			Activity_Push_Notification.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}

		if (Activity_Friens_Request_Accept.getActivityObject() != null) {
			Activity_Friens_Request_Accept.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_ChangePassword.getActivityObject() != null) {
			Activity_ChangePassword.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_DudeGallery.getActivityObject() != null) {
			Activity_DudeGallery.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_Report_Dude.getActivityObject() != null) {
			Activity_Report_Dude.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_Block_Dude.getActivityObject() != null) {
			Activity_Block_Dude.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}

		if (Activity_R_CreateNewList.getActivityObject() != null) {
			Activity_R_CreateNewList.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_R_AddGuys.getActivityObject() != null) {
			Activity_R_AddGuys.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_R_EditList.getActivityObject() != null) {
			Activity_R_EditList.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (Activity_ShowImage.getActivityObject() != null) {
			Activity_ShowImage.getActivityObject().finish();
			ChatService.APPVISIBLEORNOT = true;
		}
		if (ChatService.APPVISIBLEORNOT)// app open
		{

			if (ChatService.ONCHAT == AppConstants.FRAGMENT_CHATT_MATCHES)// on
			// chat
			// screen
			{
				if (!FragmentChatMatches.ChatXmppUserId.equals(intent
						.getExtras().getString("id"))) {
					FragmentChatMatches.ChatXmppUserId = intent.getExtras()
							.getString("id");
					FragmentChatMatches.comeFrom = false;
					Log.d("", "send brodcast to home activity");
					sendBroadcast(context);

				}
			} else {
				FragmentChatMatches.ChatXmppUserId = intent.getExtras()
						.getString("id");
				FragmentChatMatches.comeFrom = false;
				Log.d("", "send brodcast to home activity");
				sendBroadcast(context);
			}
		} else {
			if (ChatService.APPACTIVITYSTATE == 1) {
				ChatService.isApplicationBaground = true;
			}
			FragmentChatMatches.ChatXmppUserId = intent.getExtras().getString(
					"id");
			FragmentChatMatches.comeFrom = false;
			Intent notificationIntent = new Intent(context, Activity_Home.class);
			notificationIntent.putExtra("open",
					AppConstants.FRAGMENT_CHATT_MATCHES);
			// notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Intent.FLAG_ACTIVITY_SINGLE_TOP, Intent.FLAG_ACTIVITY_CLEAR_TOP,
			// Intent.FLAG_ACTIVITY_REORDER_TO_FRONT and
			// Intent.FLAG_ACTIVITY_NEW_TASK
			context.startActivity(notificationIntent);

		}

		// Log.d("chat notification receiver", "type" + notificationType);
		// switch (notificationType) {
		// case AppConstants.CHAT_APP_DOES_NOT_VISIBLE:
		// Intent notificationIntent = new Intent(context, Activity_Home.class);
		// notificationIntent.putExtra("open",
		// AppConstants.FRAGMENT_CHATT_MATCHES);
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(notificationIntent);
		// break;
		// case AppConstants.CHAT_NOT_IN_CHAT_FRAGMENT:
		// // Activity_Home activity_Home = new Activity_Home();
		// //
		// Activity_Home.Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
		// sendBroadcast(context);
		// break;
		// case AppConstants.CHAT_OTHER_DUDE_NOTIFICATION:
		// // Activity_Home activity_Home1 = new Activity_Home();
		// //
		// Activity_Home.Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
		// sendBroadcast(context);
		// break;
		//
		// default:
		// break;
		// }

	}

	public void sendBroadcast(Context context) {

		Intent broadcast = new Intent();
		broadcast.putExtra("come", "chat");
		broadcast.setAction(BROADCAST_ACTION);
		context.sendBroadcast(broadcast);
	}
	// private static void generateNotification(Context context, String message,
	// int notificationType) {
	// int icon = R.drawable.ic_launcher;
	// long when = System.currentTimeMillis();
	// NotificationManager notificationManager = (NotificationManager) context
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Notification notification = new Notification(icon, message, when);
	//
	// String title = context.getString(R.string.app_name);
	//
	// Intent notificationIntent = new Intent(context,
	// Activity_Home.class);
	// notificationIntent.putExtra("open",
	// AppConstants.FRAGMENT_CHATT_MATCHES);
	//
	//
	// PendingIntent intent = PendingIntent.getBroadcast(context, 0,
	// notificationIntent, 0);
	// ((Activity_Home)(context)).Attach_Fragment(AppConstants.FRAGMENT_CHATT_MATCHES);
	// notification.setLatestEventInfo(context, title, message, intent);
	//
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	//
	// // Play default notification sound
	// notification.defaults |= Notification.DEFAULT_SOUND;
	//
	// // notification.sound = Uri.parse("android.resource://" +
	// // context.getPackageName() + "your_sound_file_name.mp3");
	//
	// // Vibrate if vibrate is enabled
	// notification.defaults |= Notification.DEFAULT_VIBRATE;
	// notificationManager.notify(0, notification);
	//
	// }
}
