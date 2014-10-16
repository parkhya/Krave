package com.ps.services;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.json.JSONArray;

import com.krave.kraveapp.Activity_Login;
import com.krave.kraveapp.FragmentChatMatches;
import com.krave.kraveapp.MyApps;
import com.krave.kraveapp.R;
import com.ps.models.ChatDetailsDTO;
import com.ps.models.UserDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.KraveDAO;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class ChatService extends Service {
	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";
	public static int ONCHAT;
	private Handler mHandler = new Handler();
	public static XMPPConnection connection;
	public SessionManager sessionManager;
	private Presence presence;
	private KraveDAO databaseHelper;
	private Roster roster;
	public static boolean APPVISIBLEORNOT;
	public static int APPACTIVITYSTATE = 0;
	public static boolean isApplicationBaground = false;

	boolean imageOrText = true;
	private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
				// Toast.makeText(context,
				// ConnectivityManager.CONNECTIVITY_ACTION,
				// Toast.LENGTH_SHORT).show();
				boolean noConnectivity = intent.getBooleanExtra(
						ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				String reason = intent
						.getStringExtra(ConnectivityManager.EXTRA_REASON);
				boolean isFailover = intent.getBooleanExtra(
						ConnectivityManager.EXTRA_IS_FAILOVER, false);
				NetworkInfo currentNetworkInfo = (NetworkInfo) intent
						.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				NetworkInfo otherNetworkInfo = (NetworkInfo) intent
						.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
				if (currentNetworkInfo.isConnected()) {
					// Toast.makeText(getApplicationContext(), "Connected",
					// Toast.LENGTH_LONG).show();
					try {
						Log.d("", "net enabled");
						if (connection != null) {
							Log.d("", "net disabled");
							/* if (connection.isConnected()) { */
							connection.disconnect();
							// }
						}
						new GetXmppClient().execute("");
					} catch (Exception exception) {

					}
				} else {
					// Toast.makeText(getApplicationContext(), "Not Connected",
					// Toast.LENGTH_LONG).show();
					try {
						if (connection != null) {
							Log.d("", "net disabled");
							/* if (connection.isConnected()) { */
							connection.disconnect();
							// }
						}
					} catch (Exception exception) {

					}
				}
			} else if (intent.getAction() == Intent.ACTION_CONFIGURATION_CHANGED) {
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {

		super.onCreate();
		Log.i("", "chat service onCreate");

		// get an instance of the receiver in your service

		// Notification notification = new Notification(R.drawable.backarrow,
		// getText(R.string.app_name), System.currentTimeMillis());
		// Intent notificationIntent = new Intent(this, Activity_Login.class);
		// PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);
		// notification.setLatestEventInfo(this, getText(R.string.app_name),
		// getText(R.string.app_name), pendingIntent);
		// startForeground(100, notification);

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		this.registerReceiver(this.mConnReceiver, intentFilter);

	}

	@Override
	public void onDestroy() {
		Log.i("", "chat service onDestroy");
		super.onDestroy();
		// Toast.makeText(getApplicationContext(), "on destroy",
		// Toast.LENGTH_SHORT).show();
		/*
		 * if (connection != null) { if (connection.isConnected()) {
		 * connection.disconnect(); } }
		 */
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i("", "chat service onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("", "chat service onStartCommand");

		sessionManager = new SessionManager(getApplicationContext());
		databaseHelper = new KraveDAO(getApplicationContext());

		// if (connection == null) {

		Log.d("", "connection start");
		if (WebServiceConstants.isOnline(getApplicationContext())) {
			new GetXmppClient().execute("");
		}
		// xmppClient();
		// }
		return START_STICKY;
	}

	class GetXmppClient extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONArray doInBackground(String... args) {
			Log.i("", "chat service doInBackground");

			try {
				xmppClient();
			} catch (Exception exp) {

			}
			return null;
			// //

		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {

			super.onPostExecute(jsonArray);

			if (connection != null) {
				if (!connection.isConnected()) {
					new GetXmppClient().execute("");
				}
			}
			// else {
			// new GetXmppClient().execute("");
			// }

		}
	}

	public void xmppClient() {
		// http://54.241.85.74:9090/plugins/presence/status?jid=109@ip-10-199-23-53&type=xml
		String host = "54.241.85.74";
		String port = "5222";
		String service = "ip-10-199-23-53";

		// Create a connection

		ConnectionConfiguration connConfig = new ConnectionConfiguration(host,
				Integer.parseInt(port), service);
		connConfig.setSecurityMode(SecurityMode.enabled);
		// connConfig.setReconnectionAllowed(true);

		if (connection == null) {
			connection = new XMPPConnection(connConfig);
		}

		if (!connection.isConnected()) {
			try {
				connection.connect();

				Log.i("XMPPClient", "[SettingsDialog] Connected to "
						+ connection.getHost());
			} catch (XMPPException ex) {
				Log.e("XMPPClient", "[SettingsDialog] Failed to connect to "
						+ connection.getHost());
				Log.e("XMPPClient", ex.toString());
				setConnection(null);

			} catch (Exception ex) {

			}
		}
		if (connection != null) {
			if (sessionManager.getUserDetail() != null
					&& connection.isConnected()) {
				if (!connection.isAuthenticated()) {
					String username = sessionManager.getUserDetail()
							.getUserID();
					String password = sessionManager.getUserDetail()
							.getUserID();
					try {
						connection.login(username, password);
						roster = connection.getRoster();

						presence = new Presence(Presence.Type.available);

						connection.sendPacket(presence);

						setConnection(connection);
					} catch (XMPPException ex) {
						Log.e("XMPPClient",
								"[SettingsDialog] Failed to log in as "
										+ username);
						Log.e("XMPPClient", ex.toString());
						setConnection(null);
					} catch (Exception ex) {

					}
				}
			}

		}
	}

	public static int retrieveState_mode(Mode userMode, boolean isOnline) {
		int userState = 0;
		/** 0 for offline, 1 for online, 2 for away,3 for busy */
		if (userMode == Mode.dnd) {
			userState = 3;
		} else if (userMode == Mode.away || userMode == Mode.xa) {
			userState = 2;
		} else if (isOnline) {
			userState = 1;
		}
		Log.d("", "userState=" + userState);
		return userState;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;

		if (connection != null) {

			connection.addConnectionListener(new ConnectionListener() {

				@Override
				public void reconnectionSuccessful() {
					// Toast.makeText(getApplicationContext(),
					// "reconnection Successful", 1).show();

				}

				@Override
				public void reconnectionFailed(Exception arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(),
					// "reconnection Failed", 1).show();
				}

				@Override
				public void reconnectingIn(int arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(getApplicationContext(),
					// "reconnecting In",
					// 1).show();
				}

				@Override
				public void connectionClosedOnError(Exception arg0) {
					// Toast.makeText(getApplicationContext(),
					// "connection ClosedOn Error", 1).show();

				}

				@Override
				public void connectionClosed() {
					// Toast.makeText(getApplicationContext(),
					// "connection Closed", 1).show();

				}
			});

			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {

						String fromName = StringUtils.parseBareAddress(message
								.getFrom());
						String name = (String) message.getProperty("name");
						Log.i("XMPPClient", "Got text [" + message.getBody()
								+ "] from [" + fromName + "]");
						sendUndreadMessageBrodcast();
						// store in database///
						ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
						chatDetailsDTO
								.setFromuser(message.getFrom().split("@")[0]);
						chatDetailsDTO.setTouser(message.getTo().split("@")[0]);
						chatDetailsDTO.setTime("" + System.currentTimeMillis());

						String body = message.getBody();
						if (body.contains(AppConstants.MESSAGE_TYPE_TEXT)) {
							chatDetailsDTO
									.setMeassageType(AppConstants.MESSAGE_TYPE_TEXT);
							chatDetailsDTO.setMessage(body.replace(
									AppConstants.MESSAGE_TYPE_TEXT, ""));
							imageOrText = true;
						} else {
							chatDetailsDTO
									.setMeassageType(AppConstants.MESSAGE_TYPE_IMAGE);
							chatDetailsDTO.setMessage(body.replace(
									AppConstants.MESSAGE_TYPE_IMAGE, ""));
							imageOrText = false;
						}

						// chatDetailsDTO.setPin(AppConstants.CHAT_TO);
						long rowID = databaseHelper.addChat(chatDetailsDTO);
						chatDetailsDTO.setId("" + rowID);
						// databaseHelper.getChat(message.getFrom().split("@")[0]);
						Log.d("", "after message delevery");

						// app state//
						if (APPVISIBLEORNOT)// app open
						{
							if (ONCHAT == AppConstants.FRAGMENT_CHATT_MATCHES)// on
																				// chat
																				// screen
							{
								// update chat screen

								if (FragmentChatMatches.ChatXmppUserId
										.equals(chatDetailsDTO.getFromuser()))// ids
																				// are
																				// same
								{
									// FragmentChatMatches.chatList
									// .add(chatDetailsDTO);
									// FragmentChatMatches.adapter
									// .notifyDataSetChanged();
									FragmentChatMatches.chatDetailsDTO = chatDetailsDTO;
									Intent broadcast = new Intent();

									broadcast.setAction("message");
									sendBroadcast(broadcast);
								} else {// ids are different
									generateNotification(
											getApplicationContext(), message,
											name);
								}

							} else // all other screen
							{
								// sent simple notification
								generateNotification(getApplicationContext(),
										message, name);
							}
						} else {
							generateNotification(getApplicationContext(),
									message, name);
						}

						// messages.add(fromName + ":");
						// messages.add(message.getBody());
						// Add the incoming message to the list view
						mHandler.post(new Runnable() {
							public void run() {
								// setListAdapter();
							}
						});
					}

				}
			}, filter);

			// FileTransferNegotiator
			// FileTransferManager manager = new
			// FileTransferManager(connection);
			// manager.addFileTransferListener(new FileTransferListener() {
			// public void fileTransferRequest(final FileTransferRequest
			// request) {
			// new Thread(){
			// @Override
			// public void run() {
			// IncomingFileTransfer transfer = request.accept();
			// File mf = Environment.getExternalStorageDirectory();
			// File file = new File(mf.getAbsoluteFile()+"/" +
			// transfer.getFileName());
			// try{
			// transfer.recieveFile(file);
			// while(!transfer.isDone()) {
			// try{
			// Thread.sleep(1000L);
			// }catch (Exception e) {
			// Log.e("", e.getMessage());
			// }
			// if(transfer.getStatus().equals(Status.error)) {
			// Log.e("ERROR!!! ", transfer.getError() + "");
			// }
			// if(transfer.getException() != null) {
			// transfer.getException().printStackTrace();
			// }
			// }
			// }catch (Exception e) {
			// Log.e("", e.getMessage());
			// }
			// };
			// }.start();
			// }
			// });
		}
	}

	protected void sendUndreadMessageBrodcast() {
		GpsService.unreadMsgCount++;

		Intent broadcast = new Intent();
		broadcast.putExtra("come", "notification");
		broadcast.setAction(BROADCAST_ACTION);
		sendBroadcast(broadcast);

	}

	private void generateNotification(Context context, Message message,
			String name) {

		/*
		 * FragmentChatMatches.ChatXmppUserId = message.getFrom().split("@")[0];
		 * FragmentChatMatches.comeFrom = false;
		 */
		Log.d("", "chat notification "
				+ sessionManager.getSettingDetail().getIsNotificationEnable());
		String not = sessionManager.getSettingDetail()
				.getIsNotificationEnable();
		if (not.equals("" + AppConstants.NOTIFICATION_ENABLE)) {

			int icon = R.drawable.ic_launcher;
			long when = System.currentTimeMillis();
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification;
			if (imageOrText) {
				notification = new Notification(icon, message.getBody()
						.replace(AppConstants.MESSAGE_TYPE_TEXT, ""), when);
			} else {
				notification = new Notification(icon, name + " sent a photo",
						when);
			}
			String title = context.getString(R.string.app_name);

			Intent notificationIntent = new Intent(context,
					ChatNotificationBrodcastReceiver.class);
			// notificationIntent.putExtra(AppConstants.CHAT_NOTIFICATION_TYPE,
			// notificationType);
			notificationIntent.putExtra("id", message.getFrom().split("@")[0]);
			// set intent so it does not start a new activity
			// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			// | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent intent = PendingIntent.getBroadcast(context,
					Integer.valueOf(message.getFrom().split("@")[0]),
					notificationIntent, PendingIntent.FLAG_ONE_SHOT);
			if (imageOrText) {
				notification.setLatestEventInfo(
						context,
						title,
						name
								+ ": "
								+ message.getBody().replace(
										AppConstants.MESSAGE_TYPE_TEXT, ""),
						intent);
			} else {
				notification.setLatestEventInfo(context, title, name
						+ " sent a photo", intent);
			}

			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			// Play default notification sound
			notification.defaults |= Notification.DEFAULT_SOUND;

			// notification.sound = Uri.parse("android.resource://" +
			// context.getPackageName() + "your_sound_file_name.mp3");

			// Vibrate if vibrate is enabled
			notification.defaults |= Notification.DEFAULT_VIBRATE;

			// if (imageOrText) {
			notificationManager.notify(
					Integer.valueOf(message.getFrom().split("@")[0]),
					notification);
			// } else {
			//
			// notificationManager.notify(Integer.valueOf("Photo"),
			// notification);
			// }

		}
	}

}
