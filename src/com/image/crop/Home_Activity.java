package com.image.crop;






import com.krave.kraveapp.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class Home_Activity extends Activity 
{
	ImageView imageView;
	public static String imagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		imageView =(ImageView) findViewById(R.id.imageView1);
		imageView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				Intent intent = new  Intent(Home_Activity.this,Gallery_Activity.class);
				startActivityForResult(intent, 100);
			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100&& data != null )
		{
			imageView.setImageURI(Uri.parse(data.getExtras().getString("imagepath")));
		}
	}
}
