package com.ps.utill;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {

	public MyRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent e) {
//		final int action = MotionEventCompat.getActionMasked(e);
//		switch (action) {
//		case MotionEvent.ACTION_MOVE:
//			Log.d("", "MotionEvent.ACTION_MOVE");
//			return true;
//		
//
//		default:
//			return super.dispatchTouchEvent(e);
//		}
//	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// final int action = MotionEventCompat.getActionMasked(ev);
	// switch (action) {
	// case MotionEvent.ACTION_MOVE:
	// Log.d("", "MotionEvent.ACTION_MOVE");
	// return true;
	// case MotionEvent.ACTION_DOWN:
	// Log.d("", "MotionEvent.ACTION_DOWN");
	// return true;
	// case MotionEvent.ACTION_UP:
	// Log.d("", "MotionEvent.ACTION_UP");
	// return false;
	//
	// default:
	// return super.onInterceptTouchEvent(ev);
	// }
	//
	// }
}
