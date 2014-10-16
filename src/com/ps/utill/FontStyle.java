package com.ps.utill;

import android.content.Context;
import android.graphics.Typeface;

public class FontStyle {

	public static Typeface getFont(Context context,
			int fontId) {

		Typeface custom_font = null;
		switch (fontId) {
		case AppConstants.HelveticaNeueLTStd_Bd:
			custom_font = Typeface.createFromAsset(context.getAssets(),
					"HelveticaNeueLTStd-Bd.otf");
			break;
		case AppConstants.HelveticaNeueLTStd_Lt:
			custom_font = Typeface.createFromAsset(context.getAssets(),
					"HelveticaNeueLTStd-Lt.otf");
			break;
		case AppConstants.HelveticaNeueLTStd_Md:
			custom_font = Typeface.createFromAsset(context.getAssets(),
					"HelveticaNeueLTStd-Md.otf");
			break;
		case AppConstants.HelveticaNeueLTStd_Roman:
			custom_font = Typeface.createFromAsset(context.getAssets(),
					"HelveticaNeueLTStd-Roman.otf");
			break;
		default:
			break;
		}

		return custom_font;

	}

	// public static Typeface getFontStyleHelveticaNeueLTStd_Bd(Context context)
	// {
	// Typeface custom_font = Typeface.createFromAsset(context.getAssets(),
	// "HelveticaNeueLTStd-Bd.otf");
	// return custom_font;
	//
	// }
	//
	// public static Typeface getFontStyleHelveticaNeueLTStd_Lt(Context context)
	// {
	// Typeface custom_font = Typeface.createFromAsset(context.getAssets(),
	// "HelveticaNeueLTStd-Lt.otf");
	// return custom_font;
	//
	// }
	//
	// public static Typeface getFontStyleHelveticaNeueLTStd_Md(Context context)
	// {
	// Typeface custom_font = Typeface.createFromAsset(context.getAssets(),
	// "HelveticaNeueLTStd-Md.otf");
	// return custom_font;
	//
	// }
	//
	// public static Typeface getFontStyleHelveticaNeueLTStd_Roman(Context
	// context) {
	// Typeface custom_font = Typeface.createFromAsset(context.getAssets(),
	// "HelveticaNeueLTStd-Roman.otf");
	// return custom_font;
	//
	// }

}
