package com.ps.loader;


import com.krave.kraveapp.R;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TransparentProgressDialog extends Dialog {
	private ImageView iv;

	public TransparentProgressDialog(Context context) {
		super(context, R.style.TransparentProgressDialog);
		// getWindow().setBackgroundDrawableResource(R.drawable.whitetrans);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		setTitle(null);
		setCancelable(true);
		//
		setOnCancelListener(null);
		// LinearLayout layout = new LinearLayout(context);
		// layout.setOrientation(LinearLayout.VERTICAL);
		// //layout.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		// iv = new ImageView(context);
		//
		// iv.setImageResource(resourceIdOfImage);
		// layout.addView(iv, params);
		// addContentView(layout, params);

		View view = getLayoutInflater().inflate(R.layout.dialogbox, null);
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Bd);
		TextView textView = (TextView) view.findViewById(R.id.loading);
		textView.setTypeface(typeface);
		iv = (ImageView) view.findViewById(R.id.imageView1);
		addContentView(view, params);

	}

	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
				.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}

}
