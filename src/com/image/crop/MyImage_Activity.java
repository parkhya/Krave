package com.image.crop;




import com.krave.kraveapp.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MyImage_Activity extends Activity 
{
	Button button;
	String path;
	ImageView view;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_image);
		button = (Button) findViewById(R.id.button1);
		view = (ImageView) findViewById(R.id.imageView1);
		view.setImageURI(Uri.parse(getIntent().getExtras().getString("image")));
		path = getIntent().getExtras().getString("image");
		button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				Home_Activity.imagePath=getIntent().getExtras().getString("image");
				Intent intent = getIntent();
				intent.putExtra("imagepath", getIntent().getExtras().getString("image"));
				setResult(300,intent);
				finish();
			}
		});
	}
}
