package com.krave.kraveapp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ps.adapters.ChatListViewAdapter;
import com.ps.gallery.Custom_Gallery_Activity;

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
public class FragmentChatMatches extends Fragment implements OnClickListener {

	public static String BROADCAST_ACTION = "com.unitedcoders.android.broadcasttest.SHOWTOAST";
	/* resources */

	private SessionManager sessionManager;
	private ImageLoaderCircle imageLoaderCircle;
	private Activity_Home context;

	/* Layout Views */
	private ImageView backButton, addButton, onlineImageView;
	private CircleImageView circleImageView;
	private TextView dudeName;
	private RelativeLayout plusButtonLayout, imageButtonLayout;
	private LinearLayout viewDudeProfileLayout, blockDudeLayout,
			deleteHistoryLayout, cancle1Layout, takeFromCameraLayout,
			fromGalleryLayout, cancle2Layout;
	private static ListView chatListView;
	private EditText messageField;
	private Button sendButton, imageButton;
	// public static ChatDetailsDTO chatDetailsDTO;
	/* dto variables initialization */
	private ChatListViewAdapter adapter;
	private List<ChatDetailsDTO> chatList = new ArrayList<ChatDetailsDTO>();
	public static String ChatXmppUserId;
	public static boolean isOnline = false;
	public static UserDTO userDTO;
	public static boolean comeFrom = true;
	public static boolean backButtonComeFrom = false;
	KraveDAO databaseHelper;
	public static ChatDetailsDTO chatDetailsDTO;
	BroadcastReceiver messageBroadcastReceiver;
	String previousDate = "";
	private static int RESULT_LOAD_IMAGE = 5;
	private static int RESULT_CAMERA_IMAGE = 4;
	private String picturePath = "";
	private ChatDetailsDTO chatHistoryChatDetailsDTO;

	// String mCurrentPhotoPath;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.gc();
		View view = inflater.inflate(R.layout.fragment_chat_matches, container,
				false);

		context = (Activity_Home) getActivity();
		context.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		sessionManager = new SessionManager(context);
		imageLoaderCircle = new ImageLoaderCircle(context);
		databaseHelper = new KraveDAO(context);
		setLayout(view);
		setListeners();
		setTypeFace(view);
		context.mainview.setVisibility(View.GONE);

		messageBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				addNewMessage(chatDetailsDTO);

			}
		};
		IntentFilter intentFilter1 = new IntentFilter();
		intentFilter1.addAction("message");

		context.registerReceiver(this.messageBroadcastReceiver, intentFilter1);

		if (comeFrom) {
			try {
				loadData();
			} catch (Exception e) {

			}
		} else {
			if (WebServiceConstants.isOnline(context)) {
				new GetDudeAsynchTask().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.GET_DUDE_BY_ID);
			}
			viewDudeProfileLayout.setEnabled(false);
			blockDudeLayout.setEnabled(false);
			comeFrom = true;
		}

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		outState.putString("tab", "yourAwesomeFragmentsTab");

		super.onSaveInstanceState(outState);

	}

	private void setTypeFace(View view) {
		TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
		textView1 = (TextView) view.findViewById(R.id.textView1);
		textView2 = (TextView) view.findViewById(R.id.textView2);
		textView3 = (TextView) view.findViewById(R.id.textView3);
		textView4 = (TextView) view.findViewById(R.id.textView4);
		textView5 = (TextView) view.findViewById(R.id.textView5);
		textView6 = (TextView) view.findViewById(R.id.textView6);
		textView7 = (TextView) view.findViewById(R.id.textView7);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);
		dudeName.setTypeface(typeface);
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView7.setTypeface(typeface);
		messageField.setTypeface(typeface);
		// sendButton.setTypeface(typeface);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		databaseHelper.previousDate = "";
		chatList.clear();
		chatList.addAll(databaseHelper.getChat(ChatXmppUserId, sessionManager
				.getUserDetail().getUserID()));

		if (chatList.size() != 0) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
			Date date2 = new Date(Long.valueOf(chatList
					.get(chatList.size() - 1).getTime()));
			previousDate = dateFormat1.format(date2);
		}

		adapter.notifyDataSetChanged();

	}

	private void loadData() {
		handler.postDelayed(runnable, 0);
		// userDTO = Activity_Home.dudeCommonList.get(Activity_Home.index);
		if (userDTO.getUserProfileImageDTOs().size() != 0
				&& userDTO.getUserProfileImageDTOs() != null
				&& userDTO.getUserProfileImageDTOs().get(0).getIsImageActive()) {
			imageLoaderCircle.DisplayImage(userDTO.getUserProfileImageDTOs()
					.get(0).getImagePath(), circleImageView);

		}
		ChatXmppUserId = userDTO.getUserID();
		dudeName.setText(userDTO.getFirstName() + " " + userDTO.getLastName());

	}

	private void addNewMessage(ChatDetailsDTO m) {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
		Date date2 = new Date(Long.valueOf(m.getTime()));
		String newDateMessageDate = dateFormat1.format(date2);
		if (!previousDate.equals(newDateMessageDate)) {
			ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
			chatDetailsDTO.setId("-1");
			chatDetailsDTO.setDate(newDateMessageDate);
			chatList.add(chatDetailsDTO);
			previousDate = newDateMessageDate;

		}
		chatList.add(m);

		adapter.notifyDataSetChanged();
		// chatListView.post(new Runnable() {
		// @Override
		// public void run() {
		// chatListView.setSelection(adapter.getCount() - 1);
		// }
		// });
		// chatListView.scrollTo(0, chatListView.getHeight());
		// chatListView.setSelection(chatList.size() - 1);
		// scrollMyListViewToBottom();
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
		addButton = (ImageView) view.findViewById(R.id.addButton);

		circleImageView = (CircleImageView) view
				.findViewById(R.id.dudeProfilePick);
		dudeName = (TextView) view.findViewById(R.id.dudeName);
		viewDudeProfileLayout = (LinearLayout) view
				.findViewById(R.id.viewDudeProfileLayout);
		blockDudeLayout = (LinearLayout) view
				.findViewById(R.id.blockDudeLayout);

		deleteHistoryLayout = (LinearLayout) view
				.findViewById(R.id.deleteHistory);
		cancle1Layout = (LinearLayout) view.findViewById(R.id.cancleLayout1);
		cancle2Layout = (LinearLayout) view.findViewById(R.id.cancleLayout2);
		takeFromCameraLayout = (LinearLayout) view
				.findViewById(R.id.takePictureLayout);
		fromGalleryLayout = (LinearLayout) view
				.findViewById(R.id.fromGalleryLayout);
		plusButtonLayout = (RelativeLayout) view
				.findViewById(R.id.plusButtonLayout);
		imageButtonLayout = (RelativeLayout) view
				.findViewById(R.id.imageButtonLayout);
		chatListView = (ListView) view.findViewById(R.id.chatListView);
		imageButton = (Button) view.findViewById(R.id.imageButton);
		messageField = (EditText) view.findViewById(R.id.message);
		sendButton = (Button) view.findViewById(R.id.sendButton);
		onlineImageView = (ImageView) view.findViewById(R.id.onlineImage);

		adapter = new ChatListViewAdapter(context,
				(ArrayList<ChatDetailsDTO>) chatList);
		chatListView
				.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		chatListView.setStackFromBottom(true);

		// adapter.registerDataSetObserver(new DataSetObserver() {
		// @Override
		// public void onChanged() {
		// super.onChanged();
		// chatListView.setSelection(adapter.getCount() - 1);
		// }
		// });
		chatListView.setAdapter(adapter);

		// to scroll the list view to bottom on data change

	}

	private void setListeners() {
		backButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		viewDudeProfileLayout.setOnClickListener(this);
		blockDudeLayout.setOnClickListener(this);
		deleteHistoryLayout.setOnClickListener(this);
		cancle1Layout.setOnClickListener(this);
		cancle2Layout.setOnClickListener(this);
		takeFromCameraLayout.setOnClickListener(this);
		fromGalleryLayout.setOnClickListener(this);
		sendButton.setOnClickListener(this);
		imageButton.setOnClickListener(this);

		messageField.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEND) {

					if (messageField.getText().toString().trim().length() != 0)
						sendMessage();

				}
				return false;
			}
		});
		messageField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().trim().length() == 0) {
					sendButton.setBackgroundResource(R.drawable.send_gray);
				} else {
					sendButton.setBackgroundResource(R.drawable.send_red);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backButton:
			// if (backButtonComeFrom) {
			context.Attach_Fragment(AppConstants.FRAGMENT_CHAT_FRIEND);
			backButtonComeFrom = false;
			// } else {
			// context.setLeftDrawer = 0;
			//
			// context.left_button.setImageResource(R.drawable.nav);
			// context.onClick(context.layoutFindDudes);
			// }
			break;
		case R.id.addButton:
			imageButtonLayout.setVisibility(View.GONE);
			plusButtonLayout.setVisibility(View.VISIBLE);

			break;

		case R.id.viewDudeProfileLayout:
			FragmentDetailDudesProfile.comeFrom = AppConstants.FRAGMENT_CHATT_MATCHES;
			context.Attach_Fragment(AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE);
			plusButtonLayout.setVisibility(View.GONE);

			break;
		case R.id.blockDudeLayout:
			Intent intent = new Intent(context, Activity_Block_Dude.class);
			startActivityForResult(intent, 1);
			plusButtonLayout.setVisibility(View.GONE);
			break;
		case R.id.cancleLayout1:
			plusButtonLayout.setVisibility(View.GONE);
			break;
		case R.id.cancleLayout2:
			imageButtonLayout.setVisibility(View.GONE);
			break;
		case R.id.deleteHistory:
			openDailog();
			plusButtonLayout.setVisibility(View.GONE);
			break;
		case R.id.takePictureLayout:
			dispatchTakePictureIntent();
			imageButtonLayout.setVisibility(View.GONE);
			break;
		case R.id.fromGalleryLayout:
			openGallery();
			imageButtonLayout.setVisibility(View.GONE);
			break;
		case R.id.imageButton:
			plusButtonLayout.setVisibility(View.GONE);
			imageButtonLayout.setVisibility(View.VISIBLE);

			break;
		case R.id.sendButton:
			if (messageField.getText().toString().trim().length() != 0) {

				sendMessage();
			}
			break;
		default:
			break;
		}

	}

	private void startUserCurrentLocationService() {

		Intent intent1 = new Intent(context, ChatService.class);
		context.startService(intent1);

	}

	public void openGallery() {
		// TODO Auto-generated method stub
		// Intent intent = new Intent(Intent.ACTION_PICK);
		// intent.setType("image/*");
		// startActivityForResult(intent, RESULT_LOAD_IMAGE);

		Intent i = new Intent(context, Custom_Gallery_Activity.class);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	public void openCamera() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, RESULT_CAMERA_IMAGE);

	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = new File(
				android.os.Environment.getExternalStorageDirectory(),
				"krave/Temp");
		;
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		picturePath = image.getAbsolutePath();
		return image;
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				// ...
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, RESULT_CAMERA_IMAGE);
			}
		}
	}

	private void processPictureWhenReady(final String picturePath) {
		final File pictureFile = new File(picturePath);

		if (pictureFile.exists()) {
			// The picture is ready; process it.
		} else {
			// The file does not exist yet. Before starting the file observer,
			// you
			// can update your UI to let the user know that the application is
			// waiting for the picture (for example, by displaying the thumbnail
			// image and a progress indicator).

			final File parentDirectory = pictureFile.getParentFile();
			FileObserver observer = new FileObserver(parentDirectory.getPath()) {
				// Protect against additional pending events after CLOSE_WRITE
				// is
				// handled.
				private boolean isFileWritten;

				@Override
				public void onEvent(int event, String path) {
					if (!isFileWritten) {
						// For safety, make sure that the file that was created
						// in
						// the directory is actually the one that we're
						// expecting.
						File affectedFile = new File(parentDirectory, path);
						isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile
								.equals(pictureFile));

						if (isFileWritten) {
							stopWatching();
							new UploadImage()
									.execute(WebServiceConstants.BASE_URL_FILE
											+ WebServiceConstants.UPLOAD_FILE);
							// Now that the file is ready, recursively call
							// processPictureWhenReady again (on the UI thread).
							// runOnUiThread(new Runnable() {
							// @Override
							// public void run() {
							// processPictureWhenReady(picturePath);
							// }
							// });
						}
					}
				}
			};
			observer.startWatching();
		}
	}

	private void takePhoto() {
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(getTempFile(context)));
		startActivityForResult(intent, RESULT_CAMERA_IMAGE);
	}

	private File getTempFile(Context context) {
		// it will return /sdcard/image.tmp
		final File path = new File(Environment.getExternalStorageDirectory(),
				context.getPackageName());
		if (!path.exists()) {
			path.mkdir();
		}
		return new File(path, "image.jpg");
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (resultCode == RESULT_OK) {
	// switch(requestCode){
	// case TAKE_PHOTO_CODE:
	// final File file = getTempFile(this);
	// try {
	// Bitmap captureBmp = Media.getBitmap(getContentResolver(),
	// Uri.fromFile(file) );
	// // do whatever you want with the bitmap (Resize, Rename, Add To Gallery,
	// etc)
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// break;
	// }
	// }
	// }

	public void sendImage() {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (WebServiceConstants.isOnline(context)
				&& ChatService.connection.isConnected()) {
			String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
			String text = messageField.getText().toString();

			Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");
			Message msg = new Message(to, Message.Type.chat);
			msg.setProperty("name", sessionManager.getUserDetail()
					.getFirstName());

			String body = convertImageToBase64(picturePath);
			msg.setBody(body + AppConstants.MESSAGE_TYPE_IMAGE);
			ChatService.connection.sendPacket(msg);

			ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
			chatDetailsDTO.setTouser(ChatXmppUserId);
			chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
					.getUserID());
			chatDetailsDTO.setTime("" + System.currentTimeMillis());
			chatDetailsDTO.setMessage(body);
			chatDetailsDTO.setMeassageType(AppConstants.MESSAGE_TYPE_IMAGE);
			chatHistoryChatDetailsDTO = chatDetailsDTO;
			long rowId = databaseHelper.addChat(chatDetailsDTO);
			chatDetailsDTO.setId("" + rowId);
			addNewMessage(chatDetailsDTO);
			messageField.setText("");
			// new SaveChatHistory().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.SEND_MESSAGE_TO_SERVER);

			// new UploadImage().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.UPLOAD_FILE);
		}
	}

	public void sendImage(String pictureUrl) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		if (WebServiceConstants.isOnline(context)
				&& ChatService.connection.isConnected()) {
			String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
			String text = messageField.getText().toString();

			Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");
			Message msg = new Message(to, Message.Type.chat);
			msg.setProperty("name", sessionManager.getUserDetail()
					.getFirstName());

			msg.setBody(pictureUrl + AppConstants.MESSAGE_TYPE_IMAGE);
			ChatService.connection.sendPacket(msg);

			ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
			chatDetailsDTO.setTouser(ChatXmppUserId);
			chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
					.getUserID());
			chatDetailsDTO.setTime("" + System.currentTimeMillis());
			chatDetailsDTO.setMessage(pictureUrl);
			chatDetailsDTO.setMeassageType(AppConstants.MESSAGE_TYPE_IMAGE);
			chatHistoryChatDetailsDTO = chatDetailsDTO;
			long rowId = databaseHelper.addChat(chatDetailsDTO);
			chatDetailsDTO.setId("" + rowId);
			addNewMessage(chatDetailsDTO);
			messageField.setText("");
			new SaveChatHistory().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.SEND_MESSAGE_TO_SERVER);

			// new UploadImage().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.UPLOAD_FILE);
		} else {
			startUserCurrentLocationService();
		}
	}

	public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth,
			int bitmapHeight) {
		return Bitmap
				.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
	}

	private String convertImageToBase64(String picturePath) {
		// String imageInSD = "/sdcard/UserImages/" + userImageName;
		// BitmapFactory.Options options = null;
		// options = new BitmapFactory.Options();
		// options.inSampleSize = 2;
		Bitmap bitmap = convertBitmap(picturePath);
		// Bitmap resizeBitmap = getResizedBitmap(bitmap, 100, 100);
		// Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		// byte windowImageString[] = Base64.decode(
		// jObj.getString(Shared.TAG_windowimage), Base64.DEFAULT);
		//
		// byte[] bytes = cursor1.getBlob(cursor1
		// .getColumnIndex(Shared.TAG_windowimage));
		String windowImageString = null;
		if (byteArray != null) {

			windowImageString = Base64
					.encodeToString(byteArray, Base64.DEFAULT);
			Log.e("windowImage", windowImageString);
		}

		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stream = null;
		bitmap.recycle();

		// new UploadFile().execute(WebServiceConstants.BASE_URL
		// + WebServiceConstants.UPLOAD_FILE, windowImageString);
		return windowImageString;

	}

	public Bitmap convertBitmap(String path) {

		Bitmap bitmap = null;
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false; // Disable Dithering mode
		bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
		bfOptions.inInputShareable = true; // Which kind of reference will be
											// used to recover the Bitmap data
											// after being clear, when it will
											// be used in the future
		bfOptions.inTempStorage = new byte[32 * 1024];

		File file = new File(path);
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			if (fs != null) {
				bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
						bfOptions);
			}
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}

	private void LoadBitmap(Bitmap bitmap, String strMyImagePath) {
		// String strMyImagePath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/test.png";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strMyImagePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

			fos.flush();
			fos.close();
			picturePath = strMyImagePath;
			// MediaStore.Images.Media.insertImage(getContentResolver(), b,
			// "Screen", "screen");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver()
				.openInputStream(selectedImage), null, o);
		final int REQUIRED_SIZE = 200;
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(context.getContentResolver()
				.openInputStream(selectedImage), null, o2);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("", "onActivityResult");
		if (resultCode == context.RESULT_OK && requestCode == 1) {
			context.Attach_Fragment(AppConstants.FRAGMENT_HOME);
		} else

		// if (requestCode == RESULT_LOAD_IMAGE
		// && resultCode == context.RESULT_OK && null != data) {
		// Uri selectedImage = data.getData();
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		// Cursor cursor = context.getContentResolver().query(selectedImage,
		// filePathColumn, null, null, null);
		// cursor.moveToFirst();
		// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		// picturePath = cursor.getString(columnIndex);
		// cursor.close();
		if (requestCode == RESULT_LOAD_IMAGE
				&& resultCode == Activity.RESULT_OK && data != null) {
			picturePath = data.getStringExtra("single_path");
			// imageLoader.displayImage("file://" + single_path, imgSinglePick);

			new UploadImage().execute(WebServiceConstants.BASE_URL_FILE
					+ WebServiceConstants.UPLOAD_FILE);
			// sendImage();
			// try {
			// // ProfilePic.setImageBitmap(decodeUri(selectedImage));
			// } catch (FileNotFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		} else if (requestCode == RESULT_CAMERA_IMAGE
				&& resultCode == context.RESULT_OK) {
			// picturePath=(String) data.getExtras().get("data");
			// Bitmap photo = (Bitmap) data.getExtras().get("data");
			// LoadBitmap(photo);
			// processPictureWhenReady(picturePath);
			// new UploadImage().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.UPLOAD_FILE);
			// sendImage();
			// ProfilePic.setImageBitmap(photo);

			// final File file = getTempFile(context);
			// picturePath=file.getAbsolutePath();
			// try {
			// // Bitmap captureBmp =
			// Media.getBitmap(context.getContentResolver(),
			// // Uri.fromFile(file));
			// new UploadImage().execute(WebServiceConstants.BASE_URL
			// + WebServiceConstants.UPLOAD_FILE);
			// // do whatever you want with the bitmap (Resize, Rename, Add To
			// // Gallery, etc)
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 5;
			Bitmap bitmap = imageOreintationValidator(
					BitmapFactory.decodeFile(picturePath, options), picturePath);
			LoadBitmap(bitmap, picturePath);
			new UploadImage().execute(WebServiceConstants.BASE_URL_FILE
					+ WebServiceConstants.UPLOAD_FILE);
		}
	}

	private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

		ExifInterface ei;
		try {
			ei = new ExifInterface(path);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				bitmap = rotateImage(bitmap, 90);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				bitmap = rotateImage(bitmap, 180);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				bitmap = rotateImage(bitmap, 270);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	private Bitmap rotateImage(Bitmap source, float angle) {

		Bitmap bitmap = null;
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		try {
			bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), matrix, true);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}

		return bitmap;
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
		context.mainview.setVisibility(View.VISIBLE);
		handler.removeCallbacks(runnable);
		context.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		new UnreadMessageAsyncTaskr()
				.execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.UNREAD_MESSAGE_SERVICE_OF_PARTICULAR_USER);

		System.gc();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();

	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {

			Log.d("", "online thread start");

			Log.d("", "online thread stop");
			if (WebServiceConstants.isOnline(context)) {
				new GetOnline().execute(userDTO.getUserID());
			}

		}
	};

	private void sendMessage() {
		Log.d("ChatXmppUserId", ChatXmppUserId);
		try {
			if (WebServiceConstants.isOnline(context)
					&& ChatService.connection.isConnected()) {
				String to = ChatXmppUserId + AppConstants.CHAT_SERVER_ID;
				String text = messageField.getText().toString();

				Log.i("XMPPClient", "Sending text [" + text + "] to [" + to
						+ "]");
				Message msg = new Message(to, Message.Type.chat);
				msg.setProperty("name", sessionManager.getUserDetail()
						.getFirstName());
				msg.setBody(text + AppConstants.MESSAGE_TYPE_TEXT);
				ChatService.connection.sendPacket(msg);

				ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
				chatDetailsDTO.setTouser(ChatXmppUserId);
				chatDetailsDTO.setFromuser(sessionManager.getUserDetail()
						.getUserID());
				chatDetailsDTO.setTime("" + System.currentTimeMillis());
				chatDetailsDTO.setMessage(messageField.getText().toString());
				chatDetailsDTO.setMeassageType(AppConstants.MESSAGE_TYPE_TEXT);
				chatHistoryChatDetailsDTO = chatDetailsDTO;
				long rowId = databaseHelper.addChat(chatDetailsDTO);
				chatDetailsDTO.setId("" + rowId);
				addNewMessage(chatDetailsDTO);
				messageField.setText("");
				new SaveChatHistory().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.SEND_MESSAGE_TO_SERVER);
			} else {
				startUserCurrentLocationService();
			}
		} catch (Exception e) {

		}
		// InputMethodManager imm = (InputMethodManager) context
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
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
				new DeleteChatHistoryOfParticular()
						.execute(WebServiceConstants.BASE_URL
								+ WebServiceConstants.DELETE_CHAT);
				// databaseHelper.clearChatOfUser(ChatXmppUserId, sessionManager
				// .getUserDetail().getUserID());
				// onResume();
				dialog.dismiss();
				// previousDate = "";
				// Toast.makeText(context, "Chat history successfully deleted",
				// Toast.LENGTH_SHORT).show();
			}
		});
		dialog.show();
	}

	public void openDailogToSendImage() {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_logout);
		// Do you realy want to delete your account ?
		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText("Do you really want to delete your account ?");
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
				dialog.dismiss();
				Intent intent = new Intent(context, GpsService.class);
				context.stopService(intent);
				Intent inten1 = new Intent(context, ChatService.class);
				context.stopService(inten1);
				// new DeleteAccountAsyncTask()
				// .execute(WebServiceConstants.BASE_URL
				// + WebServiceConstants.DELETE_ACCOUNT);

			}
		});
		dialog.show();
	}

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
					onlineImageView.setBackgroundResource(R.drawable.online);
					isOnline = true;
				} else if (result.equals("unavailable")) {
					onlineImageView.setBackgroundResource(R.drawable.white_crl);
					// filterList.get(poss).setIsOnline(AppConstants.OFFLINE);
				} else if (result.equals("error")) {
					onlineImageView.setBackgroundResource(R.drawable.white_crl);
					// filterList.get(poss).setIsOnline(AppConstants.OFFLINE);
				}
			} catch (Exception e) {

			}
			// new GetOnline().execute(userDTO.getUserID());
			handler.postDelayed(runnable, 30000);
		}
	}

	class SaveChatHistory extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			// http://198.12.150.189/~simssoe/index.php?action=conversation_reply&user_one=4&user_two=1&time=14526987355&&reply=hello&msg_type=""&ip=182.72.80.18
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_one", ""
					+ chatHistoryChatDetailsDTO.getFromuser()));
			params.add(new BasicNameValuePair("user_two", ""
					+ chatHistoryChatDetailsDTO.getTouser()));
			params.add(new BasicNameValuePair("time", ""
					+ chatHistoryChatDetailsDTO.getTime()));
			params.add(new BasicNameValuePair("reply", ""
					+ chatHistoryChatDetailsDTO.getMessage()));
			params.add(new BasicNameValuePair("msg_type", ""
					+ chatHistoryChatDetailsDTO.getMeassageType()));

			params.add(new BasicNameValuePair("ip", ""));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("get dude response : " + jsonArray);

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}

	class UploadImage extends AsyncTask<String, Void, String> {
		// private JSONArray jsonArray;
		// private JSONObject jsonObject;
		// private String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected String doInBackground(String... args) {
			String vResult = "";
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(args[0]);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("doc", new FileBody(new File(picturePath)));
				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				StringBuffer stringBuffer = new StringBuffer("");
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				String LineSeparator = System.getProperty("line.separator");
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line + LineSeparator);
				}
				vResult = stringBuffer.toString();

			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				Log.v("Message...1", e.getMessage());
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Log.v("Message...2", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("Message...3", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			}
			return vResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.dismiss();

			// 08-04 12:32:11.412: I/System.out(8974): newWidth....150
			// 08-04 12:32:30.272: I/System.out(8974): Responce....Admin
			// Reg.String[{"status":"success","filename":"krave_image/krave_doc/1407135708_test6302014173048.png","extension":"png"}]

			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				JSONArray jsonArray = new JSONArray(result);

				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {
					String url = jsonObject.getString("filename");
					try {
						sendImage(url);
					} catch (Exception exc) {

					}
				} else {
					Toast.makeText(context, "Unsupported file",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(context, "Unsupported file", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	class UploadFile extends AsyncTask<String, Void, JSONArray> {
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

			params.add(new BasicNameValuePair("doc", "" + args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("get dude response : " + jsonArray);

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context, "Dudes data not found",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	class GetDudeAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("userid", "" + ChatXmppUserId));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);

			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				// vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				// if (vStatus.equals("success")) {
				System.out.print("get dude response : " + jsonArray);

				for (int i = 0; i < jsonArray.length(); i++) {
					userDTO = parseUserDataAndSetInSession(jsonArray
							.getJSONObject(i));
				}
				loadData();
				adapter.notifyDataSetChanged();
				viewDudeProfileLayout.setEnabled(true);
				blockDudeLayout.setEnabled(true);

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context, "Dudes data not found",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private UserDTO parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		LatLongDTO latLongDTO = new LatLongDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayLatLong = mJsonObject.getJSONArray("latlong");
		// System.out.println(MainObject);

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
		userDTO.setLatLongDTO(latLongDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);

		return userDTO;

	}

	class DeleteChatHistoryOfParticular extends
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

			// http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002http://198.12.150.189/~simssoe/index.php?action=deleteChatHistory&request_id=1001&friend_id=1002
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("request_id", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("friend_id", "" + ChatXmppUserId));

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
					databaseHelper.clearChatOfUser(ChatXmppUserId,
							sessionManager.getUserDetail().getUserID());
					onResume();

					previousDate = "";
					Toast.makeText(context,
							"Chat history successfully deleted",
							Toast.LENGTH_SHORT).show();
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

	class UnreadMessageAsyncTaskr extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected JSONArray doInBackground(String... args) {

			// 198.12.150.189/~simssoe/index.php?action=updateReadStatus&user_id=4&friend_id=108
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("user_id", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("friend_id", "" + ChatXmppUserId));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("unread message response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			// dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {

					int count = Integer.valueOf(mJsonObject
							.getString("countread"));
					GpsService.unreadMsgCount = GpsService.unreadMsgCount
							- count;
					Intent broadcast = new Intent();
					broadcast.putExtra("come", "notification");
					broadcast.setAction(BROADCAST_ACTION);
					context.sendBroadcast(broadcast);

				} else {
					// Toast.makeText(context,
					// "Chat history not  deleted,try again",
					// Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(context, "Chat history not  deleted,try again",
						Toast.LENGTH_SHORT).show();
			}

		}
	}
}
