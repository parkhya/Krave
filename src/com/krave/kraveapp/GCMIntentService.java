package com.krave.kraveapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.krave.kraveapp.Activity_Splash;

import com.ps.services.ChatService;
import com.ps.services.GpsService;
import com.ps.utill.AppConstants;

public class GCMIntentService extends GCMBaseIntentService {
	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";
	private static final String TAG = "GCMIntentService";
	public static String userId = "-1";

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		CommonUtilities.displayMessage(context,
				"Your device registred with GCM");
		// Log.d("NAME", MainActivity.name);
		// ServerUtilities.register(context, MainActivity.name,
		// MainActivity.email, registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_unregistered));
		// ServerUtilities.unregister(context, registrationId);

	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		String message = intent.getExtras().getString("data");
		Log.d(TAG, "Received message"+message);
		CommonUtilities.displayMessage(context, message);
		// notifies user
		try {
			/* notification for accept friend request */
			if (message.contains("Your request accepted by")) {
				if (ChatService.APPVISIBLEORNOT) {

					sendBroadcast(context, message.split("#")[1]);
				} else {
					userId = message.split("#")[1];
					generateNotification(context, message);
				}

				/* get friend request */
			} else if (message.contains("You have new Friend request")) {

				// GpsService.notification = true;
				GpsService.notificationCount ++;
				if (ChatService.APPVISIBLEORNOT) {
					generateNotification(context, message);
					sendBroadcast();
				} else {
					generateNotification(context, message);

				}

			}

			else {
				generateNotification(context, message);
			}
		} catch (Exception e) {

		}
	}

	private void sendBroadcast() {

		Log.d("", "brodacst notification");
		Intent broadcast = new Intent();
		broadcast.putExtra("come", "notification");
		broadcast.setAction(BROADCAST_ACTION);
		sendBroadcast(broadcast);

	}

	public void sendBroadcast(Context context, String userId) {

		Intent broadcast = new Intent();
		broadcast.putExtra("come", "push_notification");
		broadcast.putExtra("userId", userId);
		broadcast.setAction(BROADCAST_ACTION);
		context.sendBroadcast(broadcast);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		CommonUtilities.displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		CommonUtilities.displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */

	private static void generateNotification(Context context, String message) {

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, Activity_Splash.class);

		notificationIntent.setAction("android.intent.action.MAIN");
		notificationIntent.addCategory("android.intent.category.LAUNCHER");
		// set intent so it does not start a new activity
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, title, message, intent);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		

		notificationManager.notify(0, notification);

	}
}
