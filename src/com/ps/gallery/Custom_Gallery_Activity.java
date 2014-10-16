package com.ps.gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.krave.kraveapp.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class Custom_Gallery_Activity extends Activity {
	GridView gridGallery;
	ImageButton back;
	Handler handler;
	GalleryAdapter adapter;
	private ImageLoader imageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery);
		back = (ImageButton) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		initImageLoader();
		init();
	}

	private void initImageLoader() {
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
					CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					getBaseContext())
					.defaultDisplayImageOptions(defaultOptions)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new WeakMemoryCache());

			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);

		} catch (Exception e) {

		}
	}

	private void init() {

		handler = new Handler();
		gridGallery = (GridView) findViewById(R.id.gridGallery);
		gridGallery.setFastScrollEnabled(true);
		adapter = new GalleryAdapter(getApplicationContext(), imageLoader);
		PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader,
				true, true);
		gridGallery.setOnScrollListener(listener);
		gridGallery.setOnItemClickListener(mItemSingleClickListener);
		gridGallery.setAdapter(adapter);
		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
					}
				});
				Looper.loop();
			};

		}.start();

	}

	AdapterView.OnItemClickListener mItemSingleClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			CustomGallery item = adapter.getItem(position);
			Intent data = new Intent().putExtra("single_path", item.sdcardPath);
			setResult(RESULT_OK, data);
			finish();
		}
	};

	private ArrayList<CustomGallery> getGalleryPhotos() {
		ArrayList<CustomGallery> galleryList = new ArrayList<CustomGallery>();

		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;

			Cursor imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);

			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					CustomGallery item = new CustomGallery();

					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);

					item.sdcardPath = imagecursor.getString(dataColumnIndex);

					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(galleryList);
		return galleryList;
	}

}
