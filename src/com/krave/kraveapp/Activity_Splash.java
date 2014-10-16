package com.krave.kraveapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.json.JSONArray;


import com.ps.services.ChatService;
import com.ps.utill.AppConstants;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.DialerFilter;
import android.widget.Toast;

public class Activity_Splash extends Activity {
	private SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash);
		sessionManager = new SessionManager(Activity_Splash.this);

		// try {
		// PackageInfo info = getPackageManager().getPackageInfo(
		// "com.ps.krave", PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures) {
		// MessageDigest md;
		// md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// String something = new String(Base64.encode(md.digest(), 0));
		// // String something = new
		// // String(Base64.encodeBytes(md.digest()));
		// Log.e("hash key my ....", something);
		// }
		// } catch (PackageManager.NameNotFoundException e1) {
		// Log.e("name not found", e1.toString());
		// } catch (NoSuchAlgorithmException e) {
		// Log.e("no such an algorithm", e.toString());
		// } catch (Exception e) {
		// Log.e("exception", e.toString());
		// }

		// broadcastReceiver = new BroadcastReceiver() {
		//
		// @Override
		// public void onReceive(Context context, Intent intent) {
		// check();
		//
		// }
		// };
		// IntentFilter intentFilter = new IntentFilter();
		// intentFilter.addAction("splash");
		// registerReceiver(broadcastReceiver, intentFilter);
		// check();
		// Intent intent = new Intent(Activity_Splash.this, ChatService.class);
		// startService(intent);

		/*
		 * if (WebServiceConstants.isOnlineWitoutToast(Activity_Splash.this)) {
		 * new GetXmppClient().execute(""); } else {
		 * handler.postDelayed(runnable, 5000); }
		 */
		check();
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		public void run() {

			Toast.makeText(
					Activity_Splash.this,
					" Network is not enabled,please enable your device network and continue",
					Toast.LENGTH_SHORT).show();

			finish();

		}
	};

	public void check() {

		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 5 seconds
					sleep(5 * 1000);

					// After 5 seconds redirect to another intent
					if (sessionManager.isLogin()) {
						Intent i = new Intent(getBaseContext(),
								Activity_Home.class);
						// String
						// check=getIntent().getExtras().getString("come");
						// if(check!=null){
						// i.putExtra("open", AppConstants.PUSH_NOTIFICATION);
						// i.putExtra("userId",
						// getIntent().getExtras().getString("userId"));
						// }else{
						i.putExtra("open", AppConstants.FRAGMENT_HOME);
						// }

						startActivity(i);

					} else {
						Intent i = new Intent(getBaseContext(),
								Activity_Login.class);

						startActivity(i);
					}
					finish();

				} catch (Exception e) {

				}
			}
		};

		background.start();

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
			xmppClient();
			return null;
			// //

		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {

			super.onPostExecute(jsonArray);

			// if (ChatService.connection != null) {
			// if (!ChatService.connection.isConnected()) {
			// new GetXmppClient().execute("");
			// }
			// }

			// ckStatus();
			// Addcontact();
		}
	}

	public void xmppClient() {

		String host = "198.12.150.189";
		String port = "5222";
		String service = "ip-198-12-150-189.secureserver.net";

		// Create a connection

		ConnectionConfiguration connConfig = new ConnectionConfiguration(host,
				Integer.parseInt(port), service);
		connConfig.setSecurityMode(SecurityMode.disabled);

		ChatService.connection = new XMPPConnection(connConfig);

		try {
			ChatService.connection.connect();
			Log.i("XMPPClient", "[SettingsDialog] Connected to "
					+ ChatService.connection.getHost());
		} catch (XMPPException ex) {
			Log.e("XMPPClient", "[SettingsDialog] Failed to connect to "
					+ ChatService.connection.getHost());
			Log.e("XMPPClient", ex.toString());
			// setConnection(null);

		}

		check();
	}

	// public void Addcontact() {
	// try {
	// ChatService.connection.login("68", "68");
	//
	//
	// // ChatService.connection.addPacketListener(new PacketListener() {
	// //
	// // @Override
	// // public void processPacket(Packet arg0) {
	// // // TODO Auto-generated method stub
	// // Log.d("", "packet log"+arg0);
	// // }
	// // }, null);
	//
	//
	// RosterListener rl = new RosterListener() {
	// public void entriesAdded(Collection<String> addresses) {}
	// public void entriesUpdated(Collection<String> addresses) {}
	// public void entriesDeleted(Collection<String> addresses) {}
	// public void presenceChanged(Presence presence) {
	// System.out.println("presence changed!" + presence.getFrom() +
	// " "+presence.getStatus());
	// onPresence(presence);
	// }
	// };
	// if (ChatService.connection.getRoster() != null) {
	// ChatService.connection.getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all);
	//
	// System.out.println("7");
	// ChatService.connection.getRoster().addRosterListener(rl);
	// }
	//
	//
	// Presence presence=new Presence(Presence.Type.available);
	// presence.setMode(Mode.available);
	// presence.setStatus("online");
	// ChatService.connection.sendPacket(presence);
	// } catch (XMPPException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// //Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
	// Roster roster = ChatService.connection.getRoster();
	// try {
	// roster.createEntry("65@ip-198-12-150-189.secureserver.net", "65", null);
	// } catch (XMPPException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Collection<RosterEntry> entries = roster.getEntries();
	// for (RosterEntry entry : entries) {
	//
	// Log.d("XMPPChatDemoActivity", "--------------------------------------");
	// Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
	// Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
	// Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
	// Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
	// Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
	// Presence entryPresence = roster.getPresence(entry.getUser());
	//
	// Log.d("XMPPChatDemoActivity", "Presence Status: "+
	// entryPresence.getStatus());
	// Log.d("XMPPChatDemoActivity", "Presence Type: " +
	// entryPresence.getType());
	//
	// Presence.Type type = entryPresence.getType();
	// if (type == Presence.Type.available)
	// Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
	// Log.d("XMPPChatDemoActivity", "Presence : " + entryPresence);
	// }
	// }
	// private void onPresence(Presence presence) {
	// String user = presence.getFrom();
	// // if (presence.getType() == Presence.Type.available)
	// // UnityPlayer.UnitySendMessage(eventClass, "Online", user);
	// // else
	// // UnityPlayer.UnitySendMessage(eventClass, "Offline", user);
	//
	// System.out.println("Java: Presence changed, from:" +presence.getFrom() +
	// " type:"+presence.getType() + " toString:"+presence.toString());
	// }
	// public void checkStatus() {
	//
	// final Roster roster = ChatService.connection.getRoster();
	//
	// final Collection<RosterEntry> entries = roster.getEntries();
	// for (final RosterEntry entry : entries) {
	// System.out.println("User: " + entry.getUser());
	// System.out.println("Name: " + entry.getName());
	// // System.out.println("Status: " + entry.getStatus());
	//
	// final Presence p = roster.getPresence(entry.getUser());
	// System.out.println("====== Mode: " + p.getMode());
	// System.out.println("====== Status: " + p.getStatus());
	// }
	// }

}