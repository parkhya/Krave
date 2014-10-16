package com.krave.kraveapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.style.LineHeightSpan.WithDensity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.image.crop.Gallery_Activity;
import com.ps.adapters.DudeDetailsInterestAdapter;
import com.ps.adapters.SettingListInterestAdapter;
import com.ps.horizontal_listview.HorizontalListView;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FacebookIntegration;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderForProfile;
import com.ps.utill.InternalStorageContentProvider;
import com.ps.utill.JSONParser;
import com.ps.utill.OnfacebookDone;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

@SuppressLint("NewApi")
public class FragmentProfile extends Fragment implements OnClickListener,
		OnItemClickListener {

	RelativeLayout mainLayout;
	ImageView profilePick1, profilePick2, profilePick3, profilePick4,
			profilePick5, profilePick6, backInterest, nextInterest,
			facebookLikesIntegration;

	ImageView profilePick1Stamp, profilePick2Stamp, profilePick3Stamp,
			profilePick4Stamp, profilePick5Stamp, profilePick6Stamp;

	Button galleryView, cameraView;
	LinearLayout cancleLayout, deleteLayout, imageLayout;
	RelativeLayout pictureLayout;
	private FacebookIntegration facebookIntegrationObject;
	ImageView imageViewArray[] = new ImageView[6];
	ImageView imageViewArrayStampArray[] = new ImageView[6];
	HorizontalListView horizontalListView;
	EditText aboutMe;
	int checkId = 0;

	SessionManager sessionManager;
	ImageLoaderForProfile imageLoader;
	Activity_Home context;
	UserDTO userDTO;
	public static List<FacebookLikesDTO> facebookLikesDTOs;
	private File file;
	ArrayList<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
	ArrayList<InterestsDTO> selectedInterest = new ArrayList<InterestsDTO>();

	SettingListInterestAdapter adapter;
	public static final String TAG = "MainActivity";

	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

	public static final int REQUEST_CODE_GALLERY = 100;
	public static final int REQUEST_CODE_TAKE_PICTURE = 102;
	public static final int REQUEST_CODE_CROP_IMAGE = 100;

	private ImageView mImageView;
	public File mFileTemp;
	int imagePosition;
	private String picturePath = "";
	DudeDetailsInterestAdapter dudeDetailsInterestAdapter;
	// private static int RESULT_LOAD_IMAGE = 1;
	// private static int RESULT_CAMERA_IMAGE = 4;
	public String picturesPathArray[] = { "a", "a", "a", "a", "a", "a" };
	public String delete[] = { "a", "a", "a", "a", "a", "a" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, container,
				false);
		System.gc();
		context = (Activity_Home) getActivity();
		context.setLeftDrawer = AppConstants.BACK_BUTTON_FROM_UPDATE_IMAGE_PROFILE;
		sessionManager = new SessionManager(context);
		imageLoader = new ImageLoaderForProfile(context);
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(context.getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}
		setLayout(view);
		setListener();
		setTypeFace(view);
		if (WebServiceConstants.isOnline(context)) {
			new GetAllDataAsynchTask().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_ALL_INTEREST_AND_WHAT_ARE_YOU);
		}
		setData();
		return view;
	}

	private void loadProfileImagesFromURl(ImageView[] profileImageViewArray) {

		// for (int j = i; j < 6; j++) {
		// context.picturesPathArray[j] = "a";
		// }
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		System.gc();
	}

	public void setData() {
		userDTO = sessionManager.getUserDetail();

		int i = 0;
		for (i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {

			if (AppConstants.FACEBOOK_IMAGE.equals(userDTO
					.getUserProfileImageDTOs().get(i).getImageId())
					&& (i == 0)) {
				imageViewArray[i].setScaleType(ScaleType.FIT_CENTER);
				imageLoader
						.DisplayImage(userDTO.getUserProfileImageDTOs().get(i)
								.getImagePath(), imageViewArray[i]);
				picturesPathArray[i] = AppConstants.FACEBOOK_IMAGE;

			} else {

				int poss = 0;
				try {
					poss = Integer.valueOf(userDTO.getUserProfileImageDTOs()
							.get(i).getImagePosition());
				} catch (Exception e) {

				}
				imageLoader
						.DisplayImage(userDTO.getUserProfileImageDTOs().get(i)
								.getImagePath(), imageViewArray[poss]);
				// imageViewArray[poss].setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.btn_cross));
				picturesPathArray[poss] = AppConstants.UN_UPDATED_IMAGE;
				if (!userDTO.getUserProfileImageDTOs().get(i)
						.getIsImageActive()) {
					imageViewArrayStampArray[poss].setVisibility(View.VISIBLE);
				}
			}

		}
		if (userDTO.getUserProfileImageDTOs().size() == 0) {
			imageViewArray[0]
					.setBackgroundResource(R.drawable.com_facebook_profile_picture_blank_square);
		}
		aboutMe.setText(userDTO.getAboutMe());

		if (userDTO.isFacebookLikeEnable()) {
			facebookLikesIntegration.setBackgroundDrawable(context
					.getResources().getDrawable(R.drawable.check));
		} else {
			facebookLikesIntegration.setBackgroundDrawable(context
					.getResources().getDrawable(R.drawable.uncheck));
		}

		facebookLikesIntegration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (userDTO.isFacebookLikeEnable()) {
					facebookLikesIntegration.setBackgroundDrawable(context
							.getResources().getDrawable(R.drawable.uncheck));
					userDTO.setFacebookLikeEnable(false);
				} else {
					facebookLikesIntegration.setBackgroundDrawable(context
							.getResources().getDrawable(R.drawable.check));
					userDTO.setFacebookLikeEnable(true);
					// if (intentValue != AppConstants.COME_FROM_FACEBOOK) {
					//
					startFacebookIntegration();
					// }

				}
				// notifyDataSetChanged();

			}
		});
	}

	public void startFacebookIntegration() {
		facebookIntegrationObject = new FacebookIntegration(context,
				AppConstants.COME_FROM_INTEREST, new OnfacebookDone() {

					@Override
					public void onFbcompleate() {
						// TODO Auto-generated method stub

					}
				});
		facebookIntegrationObject.facebookIntegration();
	}

	private void setListener() {
		// backInterest.setOnTouchListener(new View.OnTouchListener() {
		//
		// private Handler mHandler;
		// private long mInitialDelay = 300;
		// private long mRepeatDelay = 100;
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// if (mHandler != null)
		// return true;
		// mHandler = new Handler();
		// mHandler.postDelayed(mAction, mInitialDelay);
		// break;
		// case MotionEvent.ACTION_UP:
		// if (mHandler == null)
		// return true;
		// mHandler.removeCallbacks(mAction);
		// mHandler = null;
		// break;
		// }
		// return false;
		// }
		//
		// Runnable mAction = new Runnable() {
		// @Override
		// public void run() {
		// horizontalListView.scrollTo((int) horizontalListView.getScrollX() +
		// 10, (int) horizontalListView.getScrollY());
		// mHandler.postDelayed(mAction, mRepeatDelay);
		// }
		// };
		// });
		//
		mainLayout.setOnClickListener(this);
		backInterest.setOnClickListener(this);
		nextInterest.setOnClickListener(this);
		cancleLayout.setOnClickListener(this);
		galleryView.setOnClickListener(this);
		cameraView.setOnClickListener(this);
		deleteLayout.setOnClickListener(this);
		horizontalListView.setOnItemClickListener(this);
		for (int i = 0; i < imageViewArray.length; i++) {
			final int poss = i;
			imageViewArray[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, Gallery_Activity.class);
					// startActivity(intent);

					// pictureLayout.setVisibility(View.VISIBLE);
					// for (int i = 0; i < userDTO.getUserProfileImageDTOs()
					// .size(); i++) {
					// if(userDTO.getUserProfileImageDTOs()
					// .get(i).getImagePosition().equals(""+imagePosition)){
					//
					// }
					//
					// }
					imagePosition = poss;
					Log.d("", "picturesPathArray[poss]="
							+ picturesPathArray[poss]);
					if (picturesPathArray[imagePosition].equals("a")) {
						// deleteLayout.setVisibility(View.GONE);

						//

						intent.putExtra("delete", false);
					} else {

						intent.putExtra("delete", true);
						deleteLayout.setVisibility(View.VISIBLE);
					}

					startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
				}
			});
		}

	}

	private void setLayout(View viewLayout) {
		mainLayout = (RelativeLayout) viewLayout.findViewById(R.id.mainView);
		profilePick1 = (ImageView) viewLayout.findViewById(R.id.profileImage1);
		profilePick2 = (ImageView) viewLayout.findViewById(R.id.profileImage2);
		profilePick3 = (ImageView) viewLayout.findViewById(R.id.profileImage3);
		profilePick4 = (ImageView) viewLayout.findViewById(R.id.profileImage4);
		profilePick5 = (ImageView) viewLayout.findViewById(R.id.profileImage5);
		profilePick6 = (ImageView) viewLayout.findViewById(R.id.profileImage6);

		profilePick1Stamp = (ImageView) viewLayout
				.findViewById(R.id.profileImage1StampImageView);
		profilePick2Stamp = (ImageView) viewLayout
				.findViewById(R.id.profileImage2StampImageView);
		profilePick3Stamp = (ImageView) viewLayout
				.findViewById(R.id.profileImage3StampImageView);
		profilePick4Stamp = (ImageView) viewLayout
				.findViewById(R.id.profileImage4StampImageView);
		profilePick5Stamp = (ImageView) viewLayout
				.findViewById(R.id.profileImage5StampImageView);
		profilePick6Stamp = (ImageView) viewLayout
				.findViewById(R.id.profileImage6StampImageView);

		galleryView = (Button) viewLayout.findViewById(R.id.gallery);
		cameraView = (Button) viewLayout.findViewById(R.id.camera);
		cancleLayout = (LinearLayout) viewLayout
				.findViewById(R.id.cancleLayout);
		deleteLayout = (LinearLayout) viewLayout
				.findViewById(R.id.deleteLayout);
		pictureLayout = (RelativeLayout) viewLayout
				.findViewById(R.id.imageLayout);
		imageViewArray[0] = profilePick1;
		imageViewArray[1] = profilePick2;
		imageViewArray[2] = profilePick3;
		imageViewArray[3] = profilePick4;
		imageViewArray[4] = profilePick5;
		imageViewArray[5] = profilePick6;

		imageViewArrayStampArray[0] = profilePick1Stamp;
		imageViewArrayStampArray[1] = profilePick2Stamp;
		imageViewArrayStampArray[2] = profilePick3Stamp;
		imageViewArrayStampArray[3] = profilePick4Stamp;
		imageViewArrayStampArray[4] = profilePick5Stamp;
		imageViewArrayStampArray[5] = profilePick6Stamp;

		aboutMe = (EditText) viewLayout.findViewById(R.id.aboutMe);
		aboutMe.setActivated(false);
		aboutMe.setOnClickListener(this);
		backInterest = (ImageView) viewLayout.findViewById(R.id.backimage);
		nextInterest = (ImageView) viewLayout.findViewById(R.id.nextimage);
		horizontalListView = (HorizontalListView) viewLayout
				.findViewById(R.id.horizontalListView1);
		facebookLikesIntegration = (ImageView) viewLayout
				.findViewById(R.id.facebookLikeIntegration);
		// imageLayout = (LinearLayout) viewLayout.findViewById(R.id.image_h_w);
		// Display display = context.getWindowManager().getDefaultDisplay();
		// Point size = new Point();
		// display.getSize(size);
		// int width = size.x;
		// int height = size.y;
		// int newHeight=(width/4)*3;
		// LinearLayout.LayoutParams params = new
		// LinearLayout.LayoutParams(width,
		// newHeight);
		// imageLayout.setLayoutParams(params);

	}

	private void setTypeFace(View view) {
		TextView interestTextView, cancleTextView, addPictureTextView, deleteTextView;
		interestTextView = (TextView) view.findViewById(R.id.interestTextView);
		cancleTextView = (TextView) view.findViewById(R.id.cancleTextView);
		deleteTextView = (TextView) view.findViewById(R.id.deleteTextView);
		addPictureTextView = (TextView) view
				.findViewById(R.id.addPictureTextView);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		aboutMe.setTypeface(typeface);
		cancleTextView.setTypeface(typeface);
		deleteTextView.setTypeface(typeface);
		addPictureTextView.setTypeface(typeface);

		Typeface typeface1 = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		interestTextView.setTypeface(typeface1);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mainView:

			break;
		case R.id.imageLayout:

			break;
		case R.id.cancleLayout:
			pictureLayout.setVisibility(View.GONE);
			break;
		case R.id.deleteLayout:

			deleteImage();
			break;
		case R.id.gallery:

			openGallery();
			pictureLayout.setVisibility(View.GONE);
			break;
		case R.id.camera:

			takePicture();

			pictureLayout.setVisibility(View.GONE);
			break;
		case R.id.backimage:

			int index = horizontalListView.Getposition();

			horizontalListView.scrollTo(index + 50);

			break;
		case R.id.nextimage:
			int index1 = horizontalListView.Getposition();

			horizontalListView.scrollTo(index1 - 50);
			break;
		case R.id.aboutMe:
			Intent intent = new Intent(context, Activity_AboutMe.class);
			intent.putExtra("about", aboutMe.getText().toString());
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}

	}

	private void deleteImage() {
		// int check = 0;
		// for (int i = 0; i < picturesPathArray.length; i++) {
		// if (!picturesPathArray[i].equals("a")) {
		// check++;
		// }
		// }

		// pictureLayout.setVisibility(View.GONE);
		// if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
		// if (check > 1) {
		if (imagePosition == 0
				&& picturesPathArray[0].equals(AppConstants.FACEBOOK_IMAGE)) {
			picturesPathArray[0] = "a";
			userDTO.getUserProfileImageDTOs().remove(imagePosition);
		} else {
			if (picturesPathArray[imagePosition].equals("url")) {
				// if ((userDTO.getUserProfileImageDTOs().size() - 1) >=
				// imagePosition) {
				// delete[imagePosition] = userDTO.getUserProfileImageDTOs()
				// .get(imagePosition).getImageId();
				// }
				for (int i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {
					if (userDTO.getUserProfileImageDTOs().get(i)
							.getImagePosition().equals("" + imagePosition)) {
						delete[imagePosition] = userDTO
								.getUserProfileImageDTOs().get(i).getImageId();
					}
				}
				// userDTO.getUserProfileImageDTOs().remove(imagePosition);
			}

		}

		picturesPathArray[imagePosition] = "a";
		// }

		// else {
		//
		// picturesPathArray[imagePosition] = "a";
		// }
		imageViewArray[imagePosition].setImageResource(R.drawable.camera_200);
		imageViewArrayStampArray[imagePosition].setVisibility(View.GONE);
		// } else {
		// Toast.makeText(context, "At least one image must be compulsory",
		// Toast.LENGTH_SHORT).show();
		// }
	}

	private void takePicture() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			} else {
				/*
				 * The solution is taken from here:
				 * http://stackoverflow.com/questions
				 * /10042695/how-to-get-camera-result-as-a-uri-in-data-folder
				 */
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			context.startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

			Log.d("", "cannot take picture", e);
		}
	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		context.startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		// if (requestCode == REQUEST_CODE_GALLERY
		// && resultCode == context.RESULT_OK && null != data) {
		// try {
		//
		// InputStream inputStream = context.getContentResolver()
		// .openInputStream(data.getData());
		// FileOutputStream fileOutputStream = new FileOutputStream(
		// mFileTemp);
		// copyStream(inputStream, fileOutputStream);
		// fileOutputStream.close();
		// inputStream.close();
		//
		// startCropImage();
		//
		// } catch (Exception e) {
		//
		// Log.e(TAG, "Error while creating temp file", e);
		// }
		//
		// } else if (requestCode == REQUEST_CODE_TAKE_PICTURE
		// && resultCode == context.RESULT_OK) {
		//
		// startCropImage();
		// } else

		if (requestCode == REQUEST_CODE_CROP_IMAGE
				&& resultCode == context.RESULT_OK) {
			if (data.getExtras().getBoolean("delete")) {
				deleteImage();
			} else {
				picturePath = data.getStringExtra("path");
				// if (picturePath == null) {
				//
				// return;
				// }
				picturesPathArray[imagePosition] = picturePath;
				Log.d("", "" + picturePath);

				Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				imageViewArray[imagePosition].setImageBitmap(bitmap);
				LoadBitmap(bitmap);
				// if ((userDTO.getUserProfileImageDTOs().size() - 1) >=
				// imagePosition) {
				// delete[imagePosition] = userDTO.getUserProfileImageDTOs()
				// .get(imagePosition).getImageId();
				// }
				for (int i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {
					if (userDTO.getUserProfileImageDTOs().get(i)
							.getImagePosition().equals("" + imagePosition)) {
						delete[imagePosition] = userDTO
								.getUserProfileImageDTOs().get(i).getImageId();
					}
				}
			}
		} else if (requestCode == 1 && resultCode == context.RESULT_OK) {
			aboutMe.setText(data.getExtras().getString("about"));

		} else {
			try {

				facebookIntegrationObject.onActivityResultForFacebook(
						requestCode, resultCode, data);
			} catch (Exception e) {

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	// private void startCropImage() {
	//
	// Intent intent = new Intent(context, CropImage.class);
	// intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
	// intent.putExtra(CropImage.SCALE, true);
	//
	// intent.putExtra(CropImage.ASPECT_X, 3);
	// intent.putExtra(CropImage.ASPECT_Y, 3);
	//
	// startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	// }

	private void LoadBitmap(Bitmap bitmap) {
		Calendar c = Calendar.getInstance();
		String date = fromInt(c.get(Calendar.MONTH))
				+ fromInt(c.get(Calendar.DAY_OF_MONTH))
				+ fromInt(c.get(Calendar.YEAR))
				+ fromInt(c.get(Calendar.HOUR_OF_DAY))
				+ fromInt(c.get(Calendar.MINUTE))
				+ fromInt(c.get(Calendar.SECOND));

		String strMyImagePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/test" + date + ".png";
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strMyImagePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);

			fos.flush();
			fos.close();
			picturePath = strMyImagePath;
			picturesPathArray[imagePosition] = picturePath;
			Log.d("", "" + picturePath);
			// MediaStore.Images.Media.insertImage(getContentResolver(), b,
			// "Screen", "screen");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public String fromInt(int val) {
		return String.valueOf(val);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long arg3) {
		InterestsDTO interestsDTO = interestsDTOs.get(position);

		if (interestsDTO.getIsSelected()) {
			// if (selectedInterest.size() > 1) {
			selectedInterest.remove(interestsDTO);
			interestsDTO.setIsSelected(false);
			// } else {
			// Toast.makeText(context,
			// "At least one interest must be compulsory",
			// Toast.LENGTH_SHORT).show();
			// }
		} else {
			interestsDTO.setIsSelected(true);
			selectedInterest.add(interestsDTO);

		}
		adapter.notifyDataSetChanged();
		// gridViewAdapter = new GridViewAdapter(context,
		// (ArrayList<InterestsDTO>) interestsDTOs, 1);
		// horizontalListView.setAdapter(gridViewAdapter);
	}

	public void checkValidation(int id) {

		String validation = "Please add";
		checkId = id;
		// int check = 0;
		// for (int i = 0; i < picturesPathArray.length; i++) {
		// if (!picturesPathArray[i].equals("a")) {
		// check++;
		// }
		// }
		if (aboutMe.getText().toString().trim().length() == 0) {
			validation = validation + " About me,";
		}
		// if (check < 1) {
		// validation = validation + " Images,";
		// }
		if (picturesPathArray[0].equals("a")) {
			validation = validation + " Profile picture,";
		}
		if (selectedInterest.size() == 0) {
			validation = validation + " Interest,";
		}
		if (validation.equals("Please add")) {
			if (WebServiceConstants.isOnlineWitoutToast(context)) {
				new UpdateProfileAsynchTask()
						.execute(WebServiceConstants.BASE_URL_FILE
								+ WebServiceConstants.USER_PROFILE_UPDATE);
			}

		} else {
			Toast.makeText(context, validation, Toast.LENGTH_SHORT).show();

		}

	}

	class GetAllDataAsynchTask extends AsyncTask<String, Void, JSONArray> {
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

			params.add(new BasicNameValuePair("", ""));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);

			// Log.d("login response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
				interestsDTOs = new ArrayList<InterestsDTO>();

				System.out.print("" + jsonArray);

				JSONObject mJsonObject1 = jsonArray.getJSONObject(0);

				JSONObject mJsonObject2 = jsonArray.getJSONObject(1);
				System.out.print("mJsonObject1" + mJsonObject1);
				System.out.print("mJsonObject1" + mJsonObject1);

				// vStatus = mJsonObject.getString("status");
				JSONArray jsonArray1 = mJsonObject1.getJSONArray("Intrast");
				JSONArray jsonArray2 = mJsonObject2.getJSONArray("wru");
				System.out.print("jsonArray1" + jsonArray1);
				System.out.print("jsonArray2" + jsonArray2);
				// "intrests_id":"2","intrests_name":"DANCE","intrests_image":"2.jpeg","intrests_status":"1"
				for (int i = 0; i < jsonArray1.length(); i++) {
					JSONObject mJsonObject = jsonArray1.getJSONObject(i);
					InterestsDTO interestsDTO = new InterestsDTO();
					interestsDTO.setInterestId(mJsonObject
							.getString("intrests_id"));
					interestsDTO.setInterestName(mJsonObject
							.getString("intrests_name"));
					interestsDTO.setInterestIcon(AppConstants.BASE_IMAGE_PATH_1
							+ mJsonObject.getString("intrests_image"));
					interestsDTO.setIsSelected(false);
					String temp = mJsonObject.getString("intrests_id");
					Log.d("", "json interest id=" + temp);

					for (int k = 0; k < userDTO.getInterestList().size(); k++) {
						// InterestsDTO dto =
						// userDTO.getInterestList().get(k);
						Log.d("", "dto interest id="
								+ userDTO.getInterestList().get(k)
										.getInterestId());
						if (userDTO.getInterestList().get(k).getInterestId()
								.equals(temp)) {
							Log.d("", "interest selection =true " + temp);
							interestsDTO.setIsSelected(true);
							selectedInterest.add(interestsDTO);
						}
					}
					interestsDTOs.add(interestsDTO);

				}
			} catch (Exception e) {

			}

			adapter = new SettingListInterestAdapter(context, interestsDTOs);
			horizontalListView.setAdapter(adapter);

			// dudeDetailsInterestAdapter = new
			// DudeDetailsInterestAdapter(context,
			// (ArrayList<InterestsDTO>) interestsDTOs);
			// horizontalListView.setAdapter(dudeDetailsInterestAdapter);
		}
	}

	class UpdateProfileAsynchTask extends AsyncTask<String, Void, String> {
		private JSONArray jsonArray;
		private JSONObject jsonObject;
		private String vStatus;
		private TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected String doInBackground(String... args) {
			String vResult = "";
			try {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(args[0]);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				Log.d("", "language=" + userDTO.getLanguage());
				reqEntity.addPart("language",
						new StringBody(userDTO.getLanguage()));
				reqEntity.addPart("user_id", new StringBody(userDTO.getUserID()
						.toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("lname", new StringBody(userDTO.getLastName()
						.toString()));
				reqEntity.addPart("mobile", new StringBody(userDTO.getMobile()
						.toString()));
				reqEntity.addPart("email", new StringBody(userDTO.getEmail()
						.toString()));
				reqEntity.addPart("password", new StringBody(userDTO
						.getPassword().toString()));
				reqEntity.addPart("isFirstTime", new StringBody(userDTO
						.getIsFirstTime().toString()));
				reqEntity.addPart("user_whatAreYou", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatAreYou().toString()));
				reqEntity.addPart("feet", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));

				reqEntity.addPart("inches", new StringBody(userDTO
						.getWhatAreYouDTO().getInches().toString()));
				reqEntity.addPart("meters", new StringBody(userDTO
						.getWhatAreYouDTO().getMeters().toString()));
				reqEntity.addPart("height", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));
				reqEntity.addPart("heightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getHeightUnit().toString()));
				reqEntity.addPart("weight", new StringBody(userDTO
						.getWhatAreYouDTO().getWeight().toString()));
				reqEntity.addPart("weightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getWeightUnit().toString()));
				reqEntity.addPart("age", new StringBody(userDTO
						.getWhatAreYouDTO().getAge().toString()));
				reqEntity.addPart("user_relationshipStatus", new StringBody(
						userDTO.getWhatAreYouDTO().getRelationshipStatus()
								.toString()));
				reqEntity.addPart("user_whatDoYouKrave", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatDoYouKrave().toString()));

				reqEntity.addPart("user_note", new StringBody(aboutMe.getText()
						.toString()));

				if (picturesPathArray[0].equals(AppConstants.FACEBOOK_IMAGE)) {
					reqEntity.addPart("user_facebook_image", new StringBody(
							userDTO.getProfileImage()));
				} else {
					reqEntity.addPart("user_facebook_image", new StringBody(
							"url"));
				}

				for (int i = 0; i < selectedInterest.size(); i++) {
					// reqEntity.addPart("interest[" + i + "][]", new
					// StringBody(String.valueOf(i + 1)));
					reqEntity.addPart(
							"interest[" + i + "][]",
							new StringBody(String.valueOf(selectedInterest.get(
									i).getInterestId())));

				}
				if (userDTO.isFacebookLikeEnable()) {
					if (facebookLikesDTOs != null) {
						facebookLikesDTOs = userDTO.getFacebookLikesDTOs();
						Log.d("",
								"isFacebookLikesEnable="
										+ userDTO.isFacebookLikeEnable());
						Log.d("", "size=" + facebookLikesDTOs.size());
						reqEntity.addPart("user_facebook_Interest",
								new StringBody(
										AppConstants.FACEBOOK_LIKE_ENABLE));
						for (int i = 0; i < facebookLikesDTOs.size(); i++) {
							// reqEntity.addPart("interest[" + i + "][]", new
							// StringBody(String.valueOf(i + 1)));
							reqEntity.addPart(
									"fbintrast[" + i + "][]",
									new StringBody(String
											.valueOf(facebookLikesDTOs.get(i)
													.getImagePath())));

						}
					}
				} else {
					reqEntity.addPart("user_facebook_Interest", new StringBody(
							AppConstants.FACEBOOK_LIKE_DISABLE));
				}
				/* delete images */
				for (int i = 0; i < delete.length; i++) {
					if (!AppConstants.FACEBOOK_IMAGE.equals(delete[i])
							&& !"a".equals(delete[i])) {

						// if (!(i == 0 && AppConstants.FACEBOOK_IMAGE
						// .equals(context.picturesPathArray[i]))) {

						reqEntity.addPart("delete_image[" + i + "][]",
								new StringBody(String.valueOf(delete[i])));
						Log.d("update image image Id=" + delete[i],
								"update image image Id=" + delete[i]);

						// }
					}

				}

				for (int i = 0; i < picturesPathArray.length; i++) {
					System.out.println("AddItemActivity.arrayList.get(i)"
							+ picturesPathArray[i]);
					if (!AppConstants.UN_UPDATED_IMAGE
							.equals(picturesPathArray[i])
							&& !"a".equals(picturesPathArray[i])) {
						if (!(i == 0 && AppConstants.FACEBOOK_IMAGE
								.equals(picturesPathArray[i]))) {
							file = new File(picturesPathArray[i]);
							FileBody bin = new FileBody(file);
							reqEntity.addPart("thumb_image" + i, bin);
							Log.d("add image image Id=" + delete[i],
									"add image image path="
											+ picturesPathArray[i]
											+ " image id=" + i);
						}
					}
				}

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
			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				jsonArray = new JSONArray(result);

				jsonObject = jsonArray.getJSONObject(0);
				vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {
					// Toast.makeText(context, "Profile Updated Successfully",
					// Toast.LENGTH_SHORT).show();
					if (sessionManager.isLogin()) {
						sessionManager
								.setUserDetail(parseUserDataAndSetInSession(jsonObject));
					}

					refreshData();

					// ((Activity) context).finish();

				} else if (vStatus.equals("failure")) {
					// Toast.makeText(context, "Profile Not Updated",
					// Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			switch (checkId) {
			case 1:
				context.slide_me.toggleLeftDrawer();
				break;
			case 2:
				context.slide_me.toggleRightDrawer();

				break;
			case 3:
				context.onClick(context.layoutFindDudes);
				break;

			default:
				break;
			}

		}

	}

	public void refreshData() {

		picturesPathArray[0] = "a";
		picturesPathArray[1] = "a";
		picturesPathArray[2] = "a";
		picturesPathArray[3] = "a";
		picturesPathArray[4] = "a";
		picturesPathArray[5] = "a";
		delete[0] = "a";
		delete[1] = "a";
		delete[2] = "a";
		delete[3] = "a";
		delete[4] = "a";
		delete[5] = "a";

		userDTO = sessionManager.getUserDetail();

		int i = 0;
		for (i = 0; i < userDTO.getUserProfileImageDTOs().size(); i++) {

			if (AppConstants.FACEBOOK_IMAGE.equals(userDTO
					.getUserProfileImageDTOs().get(i).getImageId())
					&& (i == 0)) {
				// imageViewArray[i].setScaleType(ScaleType.FIT_CENTER);
				// imageLoader
				// .DisplayImage(userDTO.getUserProfileImageDTOs().get(i)
				// .getImagePath(), imageViewArray[i]);
				picturesPathArray[i] = AppConstants.FACEBOOK_IMAGE;
			} else {

				int poss = 0;
				try {
					poss = Integer.valueOf(userDTO.getUserProfileImageDTOs()
							.get(i).getImagePosition());
				} catch (Exception e) {

				}
				// imageLoader
				// .DisplayImage(userDTO.getUserProfileImageDTOs().get(i)
				// .getImagePath(), imageViewArray[poss]);
				// imageViewArray[poss].setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.btn_cross));
				picturesPathArray[poss] = AppConstants.UN_UPDATED_IMAGE;
				if (!userDTO.getUserProfileImageDTOs().get(i)
						.getIsImageActive()) {
					imageViewArrayStampArray[poss].setVisibility(View.VISIBLE);
				}
			}

		}
	}

	private UserDTO parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();

		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		System.out.println(MainObject);
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
		this.userDTO.setUserID(MainObject.getString("user_id"));
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
		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrest_id"));
			interestsDTOs.add(interestsDTO);

		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

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
		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		return userDTO;
	}

}
