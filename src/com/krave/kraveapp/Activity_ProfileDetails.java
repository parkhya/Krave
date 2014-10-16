package com.krave.kraveapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.JsonArray;
import com.ps.adapters.PagerViewAdapter;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.UserDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CustomViewPager;
import com.ps.utill.FacebookIntegration;
import com.ps.utill.JSONParser;
import com.ps.utill.NonSwipablePagerView;
import com.ps.utill.OnfacebookDone;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ProfileDetails extends Activity {
	private String gcmRegId;
	public static CustomViewPager viewPager;
	private PagerViewAdapter pagerViewAdapter;
	private List<InterestsDTO> interestsDTOs;
	private List<WhatAreYouDataDTO> whatAreYouDataDTOs1;
	private List<WhatAreYouDataDTO> whatAreYouDataDTOs2;
	private String picturePath = "";

	// private static int RESULT_LOAD_IMAGE = 1;
	// private static int RESULT_CAMERA_IMAGE = 4;
	public String picturesPathArray[] = { "a", "a", "a", "a", "a", "a" };
	public String delete[] = { "a", "a", "a", "a", "a", "a" };
	public static int imagePosition;
	public static ImageView profilePick;
	AlertDialog alertDialog = null;
	public static int intentValue;
	private SessionManager sessionManager;
	int integerArrayForPagerView[] = { R.layout.activity_registration_new,
			R.layout.profile_details_1, R.layout.profile_details_2,
			R.layout.profile_details_3, R.layout.profile_details_4,
			R.layout.profile_details_5_new };
	private UserDTO userDTO;
	private FacebookIntegration facebookIntegration;
	public static final String TAG = "MainActivity";

	public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	public static final int REQUEST_CODE_CROP_IMAGE = 103;
	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	// public static final int REQUEST_CODE_CROP_IMAGE = 0x3;

	private ImageView mImageView;
	public File mFileTemp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_profile_details);
		sessionManager = new SessionManager(Activity_ProfileDetails.this);
		// picturesPathArray[0] = "a";
		// picturesPathArray[1] = "a";
		// picturesPathArray[2] = "a";
		// picturesPathArray[3] = "a";
		// picturesPathArray[4] = "a";
		// picturesPathArray[5] = "a";

		intentValue = getIntent().getExtras().getInt(AppConstants.COME_FROM);

		// userDTO = new UserDTO();
		// userDTO.setUserID("22");
		// userDTO.setFirstName("romesh");
		// userDTO.setLastName("sahu");
		// userDTO.setEmail("romesh@gmail.com");
		// userDTO.setMobile("345345345");
		// userDTO.setPassword("er");
		// userDTO.setIsFirstTime("0");
		// userDTO.setAboutMe("hjasgdjasdasjgdjsagdsa asdasdas");
		// WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		// whatAreYouDTO.setWhatAreYou("1");
		// whatAreYouDTO.setFeet("322");
		// whatAreYouDTO.setInches("232");
		// whatAreYouDTO.setMeters("323");
		// whatAreYouDTO.setWeight("3223");
		// whatAreYouDTO.setAge("88");
		// whatAreYouDTO.setRelationshipStatus("3");
		// whatAreYouDTO.setWhatDoYouKrave("3");
		// whatAreYouDTO.setInterest("2");
		// List<InterestsDTO> dtos = new ArrayList<InterestsDTO>();
		// for (int p = 1; p < 6; p++) {
		// InterestsDTO dto = new InterestsDTO();
		// dto.setInterestId("" + p);
		// dtos.add(dto);
		// }
		// userDTO.setInterestList(dtos);
		// userDTO.setWhatAreYouDTO(whatAreYouDTO);

		if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
			userDTO = sessionManager.getUserDetail();
			// userDTO.getWhatAreYouDTO().setWhatAreYou("");
			// userDTO.getWhatAreYouDTO().setWhatDoYouKrave("");
			// userDTO.getWhatAreYouDTO().setHight("434");
			// userDTO.setAboutMe("ddsb hdf h");
			PagerViewAdapter.userDTO = userDTO;
		} else {
			userDTO = new UserDTO();
		}
		if (WebServiceConstants.isOnline(Activity_ProfileDetails.this)) {
			new GetAllDataAsynchTask().execute(WebServiceConstants.BASE_URL
					+ WebServiceConstants.GET_ALL_INTEREST_AND_WHAT_ARE_YOU);
		}

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// EasyTracker.getInstance(this).activityStart(this);
		// EasyTracker.getInstance(this).set(Fields.SCREEN_NAME,
		// "Registration Screen");
		EasyTracker.getInstance(this).set(Fields.SCREEN_NAME,
				"Registration Screen");
		EasyTracker.getInstance(this).send(MapBuilder.createAppView().build());
	}

	@Override
	public void onStop() {
		super.onStop();

		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	private void setLayout() {
		viewPager = (CustomViewPager) findViewById(R.id.viewpager);
		pagerViewAdapter = new PagerViewAdapter(Activity_ProfileDetails.this,
				integerArrayForPagerView, interestsDTOs, whatAreYouDataDTOs1,
				whatAreYouDataDTOs2);
		viewPager.setAdapter(pagerViewAdapter);
		viewPager.setPagingEnabled(false);

	}

	public void startFacebookIntegration() {

		try {
			facebookIntegration = new FacebookIntegration(
					Activity_ProfileDetails.this,
					AppConstants.COME_FROM_INTEREST, new OnfacebookDone() {

						@Override
						public void onFbcompleate() {
							// TODO Auto-generated method stub
							pagerViewAdapter.compareFacebookInterest(true);
						}
					});
			facebookIntegration.facebookIntegration();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, pagerViewAdapter.datePickerListener,
				pagerViewAdapter.year, pagerViewAdapter.month,
				pagerViewAdapter.day);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// Toast.makeText(getBaseContext(),
			// "back key"+viewPager.getCurrentItem(), 1).show();

			try {
				if (viewPager.getCurrentItem() == 1) {
					viewPager.setCurrentItem(0, true);
				} else if (viewPager.getCurrentItem() == 2) {
					viewPager.setCurrentItem(1, true);
				} else if (viewPager.getCurrentItem() == 3) {
					viewPager.setCurrentItem(2, true);
				} else if (viewPager.getCurrentItem() == 4) {
					viewPager.setCurrentItem(3, true);
				} else if (viewPager.getCurrentItem() == 5) {
					viewPager.setCurrentItem(4, true);
				} else if (viewPager.getCurrentItem() == 0) {
					finish();
				}
			} catch (Exception e) {

			}
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK
		// && null != data) {
		// try {
		//
		// InputStream inputStream = getContentResolver().openInputStream(
		// data.getData());
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
		// && resultCode == RESULT_OK) {
		//
		// startCropImage();
		// } else

		if (requestCode == REQUEST_CODE_CROP_IMAGE && resultCode == RESULT_OK) {
			if (data.getExtras().getBoolean("delete")) {
				pagerViewAdapter.deleteImage();
			} else {
				picturePath = data.getStringExtra("path");
				// if (picturePath == null) {
				//
				// return;
				// }
				picturesPathArray[imagePosition] = picturePath;
				Log.d("", "" + picturePath);
				// picturePath = data.getStringExtra(CropImage.IMAGE_PATH);
				// if (picturePath == null) {
				//
				// return;
				// }
				// picturesPathArray[imagePosition] = picturePath;
				// Log.d("", "" + picturePath);

				Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				profilePick.setImageBitmap(bitmap);
				// LoadBitmap(bitmap);
				if ((userDTO.getUserProfileImageDTOs().size() - 1) >= imagePosition) {
					delete[imagePosition] = userDTO.getUserProfileImageDTOs()
							.get(imagePosition).getImageId();
				}
			}
		} else {
			try {

				facebookIntegration.onActivityResultForFacebook(requestCode,
						resultCode, data);
			} catch (Exception e) {

			}
		}
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
	// Intent intent = new Intent(this, CropImage.class);
	// intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
	// intent.putExtra(CropImage.SCALE, true);
	//
	// intent.putExtra(CropImage.ASPECT_X, 3);
	// intent.putExtra(CropImage.ASPECT_Y, 3);
	//
	// startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	// }

	public String fromInt(int val) {
		return String.valueOf(val);
	}

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

	private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o);
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
		return BitmapFactory.decodeStream(
				getContentResolver().openInputStream(selectedImage), null, o2);
	}

	class GetAllDataAsynchTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new TransparentProgressDialog(Activity_ProfileDetails.this);
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
				whatAreYouDataDTOs1 = new ArrayList<WhatAreYouDataDTO>();
				whatAreYouDataDTOs2 = new ArrayList<WhatAreYouDataDTO>();
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
					interestsDTO.setInterestIcon(mJsonObject
							.getString("intrests_image"));
					interestsDTO.setIsSelected(false);
					String temp = mJsonObject.getString("intrests_id");
					Log.d("", "json interest id=" + temp);
					if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
						for (int k = 0; k < userDTO.getInterestList().size(); k++) {
							// InterestsDTO dto =
							// userDTO.getInterestList().get(k);
							Log.d("", "dto interest id="
									+ userDTO.getInterestList().get(k)
											.getInterestId());
							if (userDTO.getInterestList().get(k)
									.getInterestId().equals(temp)) {
								Log.d("", "interest selection =true " + temp);
								interestsDTO.setIsSelected(true);
							}
						}

					}

					interestsDTOs.add(interestsDTO);

				}
				// {"id":"1","name":"WHITE"}
				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject mJsonObject = jsonArray2.getJSONObject(i);
					WhatAreYouDataDTO whatAreYouDataDTO = new WhatAreYouDataDTO();
					whatAreYouDataDTO.setId(mJsonObject.getString("id"));
					whatAreYouDataDTO.setName(mJsonObject.getString("name"));
					whatAreYouDataDTO.setSelected(false);
					if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
							&& (userDTO.getWhatAreYouDTO().getWhatAreYou()
									.equals(mJsonObject.getString("id")))) {
						whatAreYouDataDTO.setSelected(true);

					}
					whatAreYouDataDTOs1.add(whatAreYouDataDTO);
				}
				for (int i = 0; i < jsonArray2.length(); i++) {
					JSONObject mJsonObject = jsonArray2.getJSONObject(i);
					WhatAreYouDataDTO whatAreYouDataDTO = new WhatAreYouDataDTO();
					whatAreYouDataDTO.setId(mJsonObject.getString("id"));
					whatAreYouDataDTO.setName(mJsonObject.getString("name"));
					whatAreYouDataDTO.setSelected(false);
					whatAreYouDataDTOs2.add(whatAreYouDataDTO);
					if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE
							&& (userDTO.getWhatAreYouDTO().getWhatDoYouKrave()
									.equals(mJsonObject.getString("id")))) {
						whatAreYouDataDTO.setSelected(true);

					}
				}
				setLayout();
				if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
					viewPager.setPagingEnabled(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}