package com.image.crop;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.image.crop.gestures.MoveGestureDetector;
import com.image.crop.gestures.RotateGestureDetector;
import com.krave.kraveapp.R;

public class CropActivity extends Activity implements OnTouchListener {

	// Member fields.
	private ImageView mImg;
	private ImageView mTemplateImg;
	private int mScreenWidth;
	private int mScreenHeight;
	private CropHandler mCropHandler;
	private static ProgressDialog mProgressDialog;
	private int mSelectedVersion;

	private Matrix mMatrix = new Matrix();
	private float mScaleFactor = 1f;
	private float mRotationDegrees = 0.f;
	private float mFocusX = 0.f;
	private float mFocusY = 0.f;
	private int mImageHeight, mImageWidth;
	private ScaleGestureDetector mScaleDetector;
	private RotateGestureDetector mRotateDetector;
	private MoveGestureDetector mMoveDetector;

	private int mTemplateWidth;
	private int mTemplateHeight;

	// Constants
	public static final int MEDIA_GALLERY = 1;
	public static final int TEMPLATE_SELECTION = 2;
	public static final int DISPLAY_IMAGE = 3;

	private final static int IMG_MAX_SIZE = 1000;
	private final static int IMG_MAX_SIZE_MDPI = 400;
	private RelativeLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_crop);

		mSelectedVersion = getIntent().getExtras().getInt(
				Gallery_Activity.CROP_VERSION_SELECTED_KEY, -1);

		mImg = (ImageView) findViewById(R.id.cp_img);
		mTemplateImg = (ImageView) findViewById(R.id.cp_face_template);
		layout = (RelativeLayout) findViewById(R.id.titleBar);
		mImg.setOnTouchListener(this);

		// Get screen size in pixels.
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenHeight = metrics.heightPixels;
		mScreenWidth = metrics.widthPixels;

		mFocusX = mScreenWidth / 2;
		// mFocusX = mFocusX + 130.0f;
		// mFocusX = mFocusX + 60.0f;
		mFocusY = mScreenHeight / 2;
//		Toast.makeText(getApplicationContext(), "" + mScreenHeight,
//				Toast.LENGTH_LONG).show();
//		Toast.makeText(getApplicationContext(), "" + mScreenWidth,
//				Toast.LENGTH_LONG).show();

		Bitmap faceTemplate = BitmapFactory.decodeResource(getResources(),
				R.drawable.shape_rectangular);
		mTemplateWidth = faceTemplate.getWidth();
		mTemplateHeight = faceTemplate.getHeight();

		// Set template image accordingly to device screen size.
		// if (mScreenWidth == 1080 && mScreenHeight == 1920) {
		// mTemplateWidth = 1000;
		// mTemplateHeight = 1000;
		// mFocusX = mFocusX + 130.0f;
		//
		// faceTemplate = Bitmap.createScaledBitmap(faceTemplate,
		// mTemplateWidth, mTemplateHeight, true);
		// mTemplateImg.setImageBitmap(faceTemplate);
		// }
		// if (mScreenWidth == 480 && mScreenHeight == 800) {
		// mTemplateWidth = 400;
		// mTemplateHeight = 400;
		// mFocusX = mFocusX + 60.0f;
		//
		// faceTemplate = Bitmap.createScaledBitmap(faceTemplate,
		// mTemplateWidth, mTemplateHeight, true);
		// mTemplateImg.setImageBitmap(faceTemplate);
		// }
		// if (mScreenWidth == 240 && mScreenHeight == 400) {
		// mTemplateWidth = 200;
		// mTemplateHeight = 200;
		// mFocusX = mFocusX + 40.0f;
		//
		// faceTemplate = Bitmap.createScaledBitmap(faceTemplate,
		// mTemplateWidth, mTemplateHeight, true);
		// mTemplateImg.setImageBitmap(faceTemplate);
		// }
		// if (mScreenWidth == 720 && mScreenHeight == 1280) {
		// mTemplateWidth = 700;
		// mTemplateHeight = 700;
		// mFocusX = mFocusX + 100.0f;
		// faceTemplate = Bitmap.createScaledBitmap(faceTemplate,
		// mTemplateWidth, mTemplateHeight, true);
		// mTemplateImg.setImageBitmap(faceTemplate);
		// }
		// if (mScreenWidth == 540 && mScreenHeight == 960) {
		mTemplateWidth = mScreenWidth;
		mTemplateHeight = mScreenWidth;
		mFocusX = mFocusX + 100.0f;
		faceTemplate = Bitmap.createScaledBitmap(faceTemplate, mTemplateWidth,
				mTemplateHeight, true);
		mTemplateImg.setImageBitmap(faceTemplate);
		// }

		// Load temp image.
		// Bitmap photoImg = BitmapFactory.decodeResource(getResources(),
		// R.drawable.temp_image);

		// Toast.makeText(getApplicationContext(),
		// getIntent().getExtras().getString("image"),
		// Toast.LENGTH_LONG).show();
		try {
			Bitmap photoImg = Gallery_Activity.pictureObject;
			mImageHeight = photoImg.getHeight();
			mImageWidth = photoImg.getWidth();
			float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2;
			float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2;
			mMatrix.reset();
			mMatrix.postScale(mScaleFactor, mScaleFactor);
			mMatrix.postRotate(mRotationDegrees, scaledImageCenterX,
					scaledImageCenterY);
			mMatrix.postTranslate((mScreenWidth - mImageWidth) / 2, mFocusY
					- scaledImageCenterY);
			mImg.setImageBitmap(photoImg);
			// mImg.setImageBitmap(Bitmap.createScaledBitmap(photoImg, 1024,
			// 1024,
			// true));
		} catch (Exception e) {
			// TODO: handle exception
		}
		// mImg.setImageBitmap(photoImg);
		// mImageHeight = photoImg.getHeight();
		// mImageWidth = photoImg.getWidth();

		// View is scaled by matrix, so scale initially
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		mImg.setImageMatrix(mMatrix);

		// Setup Gesture Detectors
		mScaleDetector = new ScaleGestureDetector(getApplicationContext(),
				new ScaleListener());
		mRotateDetector = new RotateGestureDetector(getApplicationContext(),
				new RotateListener());
		mMoveDetector = new MoveGestureDetector(getApplicationContext(),
				new MoveListener());

		// Instantiate Thread Handler.
		mCropHandler = new CropHandler(this);
	}

	public void onCancle(View v) {
		finish();
	}

	public void onCancleButton(View v) {
		finish();
	}

	public void onCropImageButton(View v) {
		// Create progress dialog and display it.
		mProgressDialog = new ProgressDialog(v.getContext());
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Cropping Image\nPlease Wait.....");
		mProgressDialog.show();

		// Setting values so that we can retrive the image from
		// ImageView multiple times.
		mImg.buildDrawingCache(true);
		mImg.setDrawingCacheEnabled(true);
		mTemplateImg.buildDrawingCache(true);
		mTemplateImg.setDrawingCacheEnabled(true);

		// Create new thread to crop.
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Crop image using the correct template size.
				Bitmap croppedImg = null;
				if (mScreenWidth == 480 && mScreenHeight == 800) {
					if (mSelectedVersion == Gallery_Activity.VERSION_1) {
						croppedImg = ImageProcess.cropImage(
								mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true),
								mTemplateWidth, mTemplateHeight);
					} else {
						croppedImg = ImageProcess.cropImageVer2(
								mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true),
								mTemplateWidth, mTemplateHeight);
					}
				} else {
					if (mSelectedVersion == Gallery_Activity.VERSION_1) {
						croppedImg = ImageProcess.cropImage(
								mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true),
								mTemplateWidth, mTemplateHeight);
					} else {
						croppedImg = ImageProcess.cropImageVer2(
								mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true),
								mTemplateWidth, mTemplateHeight);
					}
				}
				mImg.setDrawingCacheEnabled(false);
				mTemplateImg.setDrawingCacheEnabled(false);

				// Send a message to the Handler indicating the Thread has
				// finished.
				mCropHandler.obtainMessage(DISPLAY_IMAGE, -1, -1, croppedImg)
						.sendToTarget();
			}
		}).start();
	}

	public void onChangeTemplateButton(View v) {
		Intent intent = new Intent(this, TemplateSelectDialog.class);
		startActivityForResult(intent, TEMPLATE_SELECTION);
	}

	public void onChangeImageButton(View v) {
		// Start Gallery App.
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, MEDIA_GALLERY);
	}

	/*
	 * Adjust the size of bitmap before loading it to memory. This will help the
	 * phone by not taking up a lot memory.
	 */
	private void setSelectedImage(String path) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		if (mScreenWidth == 320 && mScreenHeight == 480) {
			options.inSampleSize = calculateImageSize(options,
					IMG_MAX_SIZE_MDPI);
		} else {
			options.inSampleSize = calculateImageSize(options, IMG_MAX_SIZE);
		}

		options.inJustDecodeBounds = false;
		Bitmap photoImg = BitmapFactory.decodeFile(path, options);
		mImageHeight = photoImg.getHeight();
		mImageWidth = photoImg.getWidth();
		mImg.setImageBitmap(photoImg);
	}

	/*
	 * Retrieves the path to the selected image from the Gallery app.
	 */
	private String getGalleryImagePath(Intent data) {
		Uri imgUri = data.getData();
		String filePath = "";
		if (data.getType() == null) {
			// For getting images from gallery.
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(imgUri, filePathColumn,
					null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath = cursor.getString(columnIndex);
			cursor.close();
		}
		return filePath;
	}

	/*
	 * Calculation used to determine by what factor images need to be reduced
	 * by. Images with its longest side below the threshold will not be resized.
	 */
	private int calculateImageSize(BitmapFactory.Options opts, int threshold) {
		int scaleFactor = 1;
		final int height = opts.outHeight;
		final int width = opts.outWidth;

		if (width >= height) {
			scaleFactor = Math.round((float) width / threshold);
		} else {
			scaleFactor = Math.round((float) height / threshold);
		}
		return scaleFactor;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == MEDIA_GALLERY) {
				String path = getGalleryImagePath(data);
				setSelectedImage(path);
			} else if (requestCode == TEMPLATE_SELECTION) {
				int pos = data.getExtras()
						.getInt(TemplateSelectDialog.POSITION);
				Bitmap templateImg = null;

				// Change template according to what the user has selected.
				switch (pos) {
				case 0:
					templateImg = BitmapFactory.decodeResource(getResources(),
							R.drawable.shape_rectangular);
					break;
				case 1:
					templateImg = BitmapFactory.decodeResource(getResources(),
							R.drawable.face_oblong);
					break;
				case 2:
					templateImg = BitmapFactory.decodeResource(getResources(),
							R.drawable.face_oval);
					break;
				case 3:
					templateImg = BitmapFactory.decodeResource(getResources(),
							R.drawable.face_round);
					break;
				case 4:
					templateImg = BitmapFactory.decodeResource(getResources(),
							R.drawable.face_square);
					break;
				case 5:
					templateImg = BitmapFactory.decodeResource(getResources(),
							R.drawable.face_triangular);
					break;
				}

				mTemplateWidth = templateImg.getWidth();
				mTemplateHeight = templateImg.getHeight();

				// Resize template if necessary.
				if (mScreenWidth == 320 && mScreenHeight == 480) {
					mTemplateWidth = 218;
					mTemplateHeight = 300;
					templateImg = Bitmap.createScaledBitmap(templateImg,
							mTemplateWidth, mTemplateHeight, true);
				}
				mTemplateImg.setImageBitmap(templateImg);
			}
		}
	}

	private static class CropHandler extends Handler {
		WeakReference<CropActivity> mThisCA;

		CropHandler(CropActivity ca) {
			mThisCA = new WeakReference<CropActivity>(ca);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CropActivity ca = mThisCA.get();
			if (msg.what == DISPLAY_IMAGE) {
				mProgressDialog.dismiss();
				Bitmap cropImg = (Bitmap) msg.obj;

				// Setup an AlertDialog to display cropped image.
				AlertDialog.Builder builder = new AlertDialog.Builder(ca);
				builder.setTitle("Final Cropped Image");
				builder.setIcon(new BitmapDrawable(ca.getResources(), cropImg));
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog dialog = builder.create();

				String root = Environment.getExternalStorageDirectory()
						.toString();
				File myDir = new File(root + "/Saved_Images");
				myDir.mkdirs();
				Random generator = new Random();
				int n = 10000;
				n = generator.nextInt(n);
				String fname = "Image-" + n + ".jpg";
				File file = new File(myDir, fname);
				if (file.exists())
					file.delete();
				try {
					FileOutputStream out = new FileOutputStream(file);
					cropImg.compress(Bitmap.CompressFormat.JPEG, 90, out);
					out.flush();
					out.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
				// Toast.makeText(ca, file.toString(),
				// Toast.LENGTH_LONG).show();

				Intent intent = ca.getIntent();
				intent.putExtra("imagepath", file.toString());
				ca.setResult(300, intent);
				ca.finish();
				// dialog.show();
			}
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		// mRotateDetector.onTouchEvent(event);
		mMoveDetector.onTouchEvent(event);

		float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2;
		float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2;

		mMatrix.reset();
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		mMatrix.postRotate(mRotationDegrees, scaledImageCenterX,
				scaledImageCenterY);
		mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY
				- scaledImageCenterY);

		ImageView view = (ImageView) v;
		view.setImageMatrix(mMatrix);
		return true;
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();
			mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 10.0f));

			return true;
		}
	}

	private class RotateListener extends
			RotateGestureDetector.SimpleOnRotateGestureListener {
		@Override
		public boolean onRotate(RotateGestureDetector detector) {
			mRotationDegrees -= detector.getRotationDegreesDelta();
			return true;
		}
	}

	private class MoveListener extends
			MoveGestureDetector.SimpleOnMoveGestureListener {
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			PointF d = detector.getFocusDelta();
			mFocusX += d.x;
			mFocusY += d.y;

			return true;
		}
	}
}