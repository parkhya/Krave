package com.image.crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.krave.kraveapp.R;
import com.ps.gallery.Custom_Gallery_Activity;

import eu.janmuller.android.simplecropimage.CropImage;

public class Gallery_Activity extends Activity {
	// priyank.deolekar@gmail.com
	Button Gallery, Camera;
	LinearLayout cancleLayout, deleteLayout;
	private String picturePath;
	public static final String CROP_VERSION_SELECTED_KEY = "crop";
	public static final int VERSION_2 = 2;
	public static final int VERSION_1 = 1;
	public static Bitmap pictureObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_new_01);
		Gallery = (Button) findViewById(R.id.choose_photo);
		Camera = (Button) findViewById(R.id.launch_editor);
		cancleLayout = (LinearLayout) findViewById(R.id.cancleLayout);
		deleteLayout = (LinearLayout) findViewById(R.id.deleteLayout);
		cancleLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		deleteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("delete", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		Gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent();
				// intent.setType("image/*");
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// startActivityForResult(
				// Intent.createChooser(intent, "Select Picture"), 200);
				Intent i = new Intent(Gallery_Activity.this,
						Custom_Gallery_Activity.class);
				startActivityForResult(i, 200);
			}
		});

		Camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dispatchTakePictureIntent();
				// Intent intent = new Intent(
				// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				// startActivityForResult(intent, 400);
			}
		});
		if (getIntent().getExtras().getBoolean("delete")) {
			deleteLayout.setVisibility(View.VISIBLE);
		} else {
			deleteLayout.setVisibility(View.GONE);
		}
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
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
				startActivityForResult(takePictureIntent, 400);
			}
		}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 200 && resultCode == Activity.RESULT_OK
				&& data != null) {
			picturePath = data.getStringExtra("single_path");
			startCropImage();
			// Uri _uri = data.getData();
			// // User had pick an image.
			// Cursor cursor = getContentResolver()
			// .query(_uri,
			// new String[] {
			// android.provider.MediaStore.Images.ImageColumns.DATA },
			// null, null, null);
			// cursor.moveToFirst();
			//
			// // Link to the image
			// picturePath = cursor.getString(0);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 1;
			// pictureObject = BitmapFactory.decodeFile(picturePath);
			// if (pictureObject != null) {
			// // Toast.makeText(getApplicationContext(),
			// // pictureObject.toString(), Toast.LENGTH_LONG).show();
			// }
			// Toast.makeText(getApplicationContext(), imageFilePath,
			// Toast.LENGTH_LONG).show();
			// Intent intent = new Intent(Gallery_Activity.this,
			// CropActivity.class);
			// intent.putExtra(CROP_VERSION_SELECTED_KEY, VERSION_2);
			// intent.putExtra("image", picturePath);
			// startActivityForResult(intent, 300);
			// cursor.close();
		} else if (requestCode == 400 && resultCode == RESULT_OK) {

			// pictureObject = (Bitmap) data.getExtras().get("data");
			// LoadBitmap(pictureObject);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			// pictureObject = BitmapFactory.decodeFile(picturePath, options);
			imageOreintationValidator(
					BitmapFactory.decodeFile(picturePath, options), picturePath);
			startCropImage();
			// Intent intent = new Intent(Gallery_Activity.this,
			// CropActivity.class);
			// intent.putExtra(CROP_VERSION_SELECTED_KEY, VERSION_2);
			// intent.putExtra("image", picturePath);
			// startActivityForResult(intent, 300);

		} else

		if (requestCode == 300 && data != null) {
			// Toast.makeText(getApplicationContext(),
			// "" + data.getExtras().getString("imagepath"),
			// Toast.LENGTH_LONG).show();
			Intent intent = getIntent();
			intent.putExtra("delete", false);
			intent.putExtra("path", picturePath);
			setResult(RESULT_OK, intent);
			finish();
			// if (pictureObject != null) {
			// pictureObject.recycle();
			// pictureObject = null;
			// }
			// ;
		}

	}

	private void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, picturePath);
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, 300);
	}

	private void LoadBitmap(Bitmap bitmap) {

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(picturePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);

			fos.flush();
			fos.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
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
		// LoadBitmap(bitmap);
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

}
