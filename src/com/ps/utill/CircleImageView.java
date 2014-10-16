package com.ps.utill;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CircleImageView  extends ImageView {

	

	public CircleImageView(Context context) {
		this(context, null);
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	//	newWidth = Integer.parseInt( attrs.getAttributeIntValue(1, "50"));
		// = Integer.parseInt(attrs.getAttributeValue(2));
	}


	@Override
	protected void onDraw(Canvas canvas) {

		Drawable d = getDrawable();

		if(d == null) {
			return;
		}

		if(d.getIntrinsicHeight() == 0 || d.getIntrinsicWidth() == 0) {
			return;
		}
		System.out.println("newWidth...."+getWidth());
		int radius = getWidth();
		Bitmap bitmap = ((BitmapDrawable )d).getBitmap();
		Bitmap resized = Bitmap.createScaledBitmap(bitmap, getWidth(), getWidth(), true);
		Bitmap b = createCroppedBitmap(radius, resized);
		canvas.drawBitmap(b, 0, 0, null);
	}

	private Bitmap createCroppedBitmap(int radius, Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(),
				bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		// paint.setColor(color);
		canvas.drawCircle(bitmap.getWidth() / 2,
				bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;

	}
}