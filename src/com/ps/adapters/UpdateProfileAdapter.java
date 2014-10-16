package com.ps.adapters;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.facebook.model.GraphUser;
import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.Activity_Login;
import com.krave.kraveapp.Activity_ProfileDetails;
import com.krave.kraveapp.R;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.AddressDTO;
import com.ps.models.FacebookLikesDTO;
import com.ps.models.InterestsDTO;
import com.ps.models.UserDTO;
import com.ps.models.UserProfileImageDTO;
import com.ps.models.WhatAreYouDTO;
import com.ps.models.WhatAreYouDataDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.InternalStorageContentProvider;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

/**
 * Created by sys1 on 1/31/14.
 */
public class UpdateProfileAdapter extends PagerAdapter {

	private List<InterestsDTO> interestList;
	private List<WhatAreYouDataDTO> whatAreYouDataList1;
	private GridViewAdapter mGridViewAdapter;
	private Activity_Home context;
	private LayoutInflater inflater;
	private int[] integerList;
	public static UserDTO userDTO;
	private String mValue;
	private WhatAreYouDTO whatAreYouDTO;
	private SessionManager sessionManager;
	private File file;

	private ImageLoader imageLoader;
	public int year = 1990;
	public int month = 1;
	public int day = 1;
	public static final int REQUEST_CODE_GALLERY = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

	private EditText firstName, lastName, email, mobile, password,
			confirmPassword;
	private Button langaugeButton;
	public static GraphUser graphUser;

	private ImageButton submitButton;
	private ListView group1;
	private ImageButton submiButton1, submiButton2, submiButton4;
	private GridView gridViewInterest;
	private RelativeLayout feetButton, weightButton, ageButton;
	private TextView selectedHeight, selectedWeight, selectedAge;
	private RadioGroup relationshipButton;

	private ImageView profilePick1, profilePick2, profilePick3, profilePick4,
			profilePick5, profilePick6;

	private EditText aboutMe;
	// private RadioButton facebookIntegrationRadioBtn;
	private ImageButton submitButton5;
	private ImageView facebookLikesIntegration;
	private ViewGroup viewGroup;

	// Double heightValue = 0.0, weightValue = 0.0;

	public UpdateProfileAdapter(Context context, int[] list,

	List<WhatAreYouDataDTO> list3) {
		this.context = (Activity_Home) context;
		this.integerList = list;
		this.whatAreYouDataList1 = list3;
		sessionManager = new SessionManager(context);
		userDTO = sessionManager.getUserDetail();

	}

	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {

		return 3;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		viewGroup = container;
		// final View viewLayout = null;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View viewLayout = inflater.inflate(integerList[position],
				container, false);
		Log.d("", "position=" + position);

		switch (position) {
		case 0:

			setRegistrationLayout(viewLayout, container, position);
			break;
		case 1:

			seProfile1Layout(viewLayout, container, position);
			break;
		case 2:

			seProfile2Layout(viewLayout, container, position);

			break;

		default:
			break;
		}

		// ((ViewPager) container)
		// .setOnPageChangeListener(new OnPageChangeListener() {
		//
		// @Override
		// public void onPageSelected(int arg0) {
		// Log.d("", "onPageSelected " + arg0);
		//
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		// Log.d("", "onPageScrolled " + arg0);
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int id) {
		// Log.d("", "onPageScrollStateChanged " + id);
		// switch (position) {
		// case 0:
		// userDTO.setFirstName(firstName.getText().toString());
		// userDTO.setLastName(lastName.getText().toString());
		// userDTO.setEmail(email.getText().toString());
		// userDTO.setMobile(mobile.getText().toString());
		// if (intentValue != AppConstants.COME_FROM_UPDATE_PROFILE) {
		// userDTO.setPassword(password.getText()
		// .toString());
		// userDTO.setConfirmPassword(confirmPassword
		// .getText().toString());
		// }
		// break;
		//
		// default:
		// break;
		// }
		//
		// }
		// });

		((ViewPager) container).addView(viewLayout);

		return viewLayout;

	}

	private void seProfile2Layout(View viewLayout, final ViewGroup container,
			final int position) {

		feetButton = (RelativeLayout) viewLayout.findViewById(R.id.hightLayout);

		ageButton = (RelativeLayout) viewLayout.findViewById(R.id.ageLayout);
		weightButton = (RelativeLayout) viewLayout
				.findViewById(R.id.weightLayout);
		selectedHeight = (TextView) viewLayout.findViewById(R.id.selectedHight);
		selectedWeight = (TextView) viewLayout
				.findViewById(R.id.selectedWeight);
		selectedAge = (TextView) viewLayout.findViewById(R.id.selectedAge);
		relationshipButton = (RadioGroup) viewLayout
				.findViewById(R.id.radioGroup1);
		submiButton2 = (ImageButton) viewLayout
				.findViewById(R.id.submitButton2);

		TextView heightTextView = (TextView) viewLayout.findViewById(R.id.feet);
		TextView weightTextView = (TextView) viewLayout
				.findViewById(R.id.weight);
		TextView ageTextView = (TextView) viewLayout.findViewById(R.id.age);
		TextView relationShipStausText = (TextView) viewLayout
				.findViewById(R.id.relationShipStausText);
		RadioButton radio00 = (RadioButton) viewLayout
				.findViewById(R.id.radio0);
		RadioButton radio11 = (RadioButton) viewLayout
				.findViewById(R.id.radio1);
		RadioButton radio22 = (RadioButton) viewLayout
				.findViewById(R.id.radio2);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		heightTextView.setTypeface(typeface);
		weightTextView.setTypeface(typeface);
		ageTextView.setTypeface(typeface);
		selectedHeight.setTypeface(typeface);
		selectedWeight.setTypeface(typeface);
		selectedAge.setTypeface(typeface);

		radio00.setTypeface(typeface);
		radio11.setTypeface(typeface);
		radio22.setTypeface(typeface);
		Typeface typeface1 = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Md);
		relationShipStausText.setTypeface(typeface1);

		// if (intentValue == AppConstants.COME_FROM_UPDATE_PROFILE) {
		String h = whatAreYouDTO.getHight();
		String w = whatAreYouDTO.getWeight();
		String a = whatAreYouDTO.getAge();
		if (h.length() != 0) {
			// if (whatAreYouDTO.getHeightUnit().equals(
			// AppConstants.HEIGHT_IN_FEET)) {
			// selectedHeight.setText(h.split("/")[0] + " Ft,"
			// + h.split("/")[1] + " In");
			// } else {
			// selectedHeight.setText(h.split("/")[0] + " Mts,"
			// + h.split("/")[1] + " Cm");
			// }
			if (whatAreYouDTO.getHeightUnit().equals(
					AppConstants.HEIGHT_IN_FEET)) {
				selectedHeight.setText(h.split("/")[0] + " '" + h.split("/")[1]
						+ " \"");
			} else {
				selectedHeight.setText(h.split("/")[0] + " m,"
						+ h.split("/")[1] + " cm");
			}
		}
		if (w.length() != 0) {
			// if (whatAreYouDTO.getWeightUnit().equals(
			// AppConstants.WEIGHT_IN_KILOGRAM)) {
			// selectedWeight.setText(w.split("/")[0] + "K," + w.split("/")[1]
			// + "Gms");
			// } else {
			// selectedWeight.setText(w.split("/")[0] + "Lb,"
			// + w.split("/")[1] + "oz");
			// }
			if (whatAreYouDTO.getWeightUnit().equals(
					AppConstants.WEIGHT_IN_KILOGRAM)) {
				selectedWeight.setText(w.split("/")[0] + " kg");
			} else {
				selectedWeight.setText(w.split("/")[0] + " lb");
			}
		}
		// selectedAge.setText(a);
		if (a.length() != 0) {
			year = Integer.valueOf(a.split("/")[2]);
			month = Integer.valueOf(a.split("/")[1]) - 1;
			day = Integer.valueOf(a.split("/")[0]);
			calculateAge(whatAreYouDTO.getAge());
		}

		// }

		feetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDialogForHight();

			}
		});
		weightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openDialogForWeight();

			}
		});
		ageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					context.showDialog(0);
				} catch (Exception exp) {

				}

			}
		});

		RadioButton radio1, radio2, radio3;
		switch (Integer.parseInt(whatAreYouDTO.getRelationshipStatus())) {

		case 1:
			radio1 = (RadioButton) viewLayout.findViewById(R.id.radio0);
			radio1.setChecked(true);

			break;
		case 2:
			radio2 = (RadioButton) viewLayout.findViewById(R.id.radio1);
			radio2.setChecked(true);
			break;
		case 3:
			radio3 = (RadioButton) viewLayout.findViewById(R.id.radio2);
			radio3.setChecked(true);
			break;

		default:
			break;
		}

		relationshipButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radio0:
							whatAreYouDTO.setRelationshipStatus("" + 1);

							break;
						case R.id.radio1:
							whatAreYouDTO.setRelationshipStatus("" + 2);

							break;
						case R.id.radio2:
							whatAreYouDTO.setRelationshipStatus("" + 3);

							break;

						}
					}
				});
		submiButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateProfile();
			}
		});

	}

	public void updateProfile() {
		userDTO.setFirstName(firstName.getText().toString());
		userDTO.setLastName(lastName.getText().toString());
		userDTO.setEmail(email.getText().toString());
		userDTO.setMobile(mobile.getText().toString());
		if (!"Enter".equals(checkValidation())) {
			Toast.makeText(context, checkValidation(), Toast.LENGTH_SHORT)
					.show();
		} else {
			if (WebServiceConstants.isOnline(context)) {
				new UpdateProfileAsynchTask()
						.execute(WebServiceConstants.BASE_URL_FILE
								+ WebServiceConstants.USER_PROFILE_UPDATE);

			}
			context.Attach_Fragment(AppConstants.FRAGMENT_SETTING);
		}

	}

	private Boolean calculateAge(String berthDateString) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date berthDate = null;
		try {
			berthDate = format.parse(berthDateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date currentDate = new Date();

		long time = currentDate.getTime() - berthDate.getTime();

		long diffInDays = (long) time / (1000 * 60 * 60 * 24);

		long number_of_years = (long) diffInDays / 365;
		long number_of_months = (long) (diffInDays % (365)) / 30;
		if (currentDate.before(berthDate)) {
			Toast.makeText(context, "Please select the currect birth date",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (number_of_years < 18) {
			Toast.makeText(context, "You must be 18 year old or above.",
					Toast.LENGTH_SHORT).show();
			return false;
		} else {
			// selectedAge.setText("" + number_of_years + "Years,"
			// + number_of_months + "Months");
			selectedAge.setText("" + number_of_years + "Years");
			return true;
		}
	}

	public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth + 1;
			day = selectedDay;
			Boolean check = calculateAge(selectedDay + "/"
					+ (selectedMonth + 1) + "/" + selectedYear);
			if (check) {
				whatAreYouDTO.setAge(selectedDay + "/" + (selectedMonth + 1)
						+ "/" + selectedYear);
			}

		}
	};

	public void openDialogForHight() {
		String unit[] = { "METRIC", "U.S." };
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_enter_height);

		final NumberPicker feetWheel = (NumberPicker) dialog
				.findViewById(R.id.feetWheel);
		final NumberPicker inchesWheel = (NumberPicker) dialog
				.findViewById(R.id.inchesWheel);
		final NumberPicker unitWheel = (NumberPicker) dialog
				.findViewById(R.id.unitWheel);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		Button cancle = (Button) dialog.findViewById(R.id.cancle);

		final TextView feetText = (TextView) dialog.findViewById(R.id.feet);
		final TextView inchesText = (TextView) dialog.findViewById(R.id.inches);
		final TextView setValue = (TextView) dialog.findViewById(R.id.setValue);
		// title.setText(titleString);
		// text.setText(titleString);

		unitWheel.setMaxValue(1);
		unitWheel.setMinValue(0);
		unitWheel.setDisplayedValues(unit);
		setValue.setText("HIEGHT");
		if (whatAreYouDTO.getHeightUnit().equals(AppConstants.HEIGHT_IN_FEET)) {
			unitWheel.setValue(1);

			feetWheel.setMaxValue(7);
			feetWheel.setMinValue(3);

			inchesWheel.setMaxValue(11);
			inchesWheel.setMinValue(0);
			feetText.setText("Ft");
			inchesText.setText("In");

			String h = whatAreYouDTO.getHight();
			// setValue.setText(h.split("/")[0] + "Feet" + h.split("/")[1]
			// + "Inches");
			feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
			inchesWheel.setValue(Integer.valueOf(h.split("/")[1]));

			setValue.setText("" + feetWheel.getValue() + " feet,"
					+ inchesWheel.getValue() + " inches");

		} else {
			unitWheel.setValue(0);
			feetWheel.setMaxValue(2);
			feetWheel.setMinValue(1);

			inchesWheel.setMaxValue(99);
			inchesWheel.setMinValue(0);
			// inchesWheel.setDisplayedValues(grams);
			feetText.setText("Mts");
			inchesText.setText("CM");

			String h = whatAreYouDTO.getHight();
			// setValue.setText(w.split("/")[0] + "Kilo" + w.split("/")[1]
			// + "Grams");
			feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
			inchesWheel.setValue((Integer.valueOf(h.split("/")[1])));

			setValue.setText("" + feetWheel.getValue() + " meter,"
					+ inchesWheel.getValue() + " centi meter");
		}

		unitWheel.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (newVal == 1) {
					whatAreYouDTO.setHeightUnit(AppConstants.HEIGHT_IN_FEET);
					Double meter = (double) (feetWheel.getValue() + ((inchesWheel
							.getValue()) / 100));
					Log.d("", "meter=" + meter);
					int inches = getMeterToInches(meter);
					Log.d("", "inches=" + inches);
					feetWheel.setMaxValue(7);
					feetWheel.setMinValue(3);

					inchesWheel.setMaxValue(11);
					inchesWheel.setMinValue(0);
					feetText.setText("Ft");
					inchesText.setText("In");

					feetWheel.setValue(inches / 12);
					inchesWheel.setValue(inches % 12);

					setValue.setText("" + feetWheel.getValue() + " feet,"
							+ inchesWheel.getValue() + " inches");
				} else {
					whatAreYouDTO.setHeightUnit(AppConstants.HEIGHT_IN_METER);
					int in = ((feetWheel.getValue()) * 12)
							+ inchesWheel.getValue();
					int cm = (int) Math.round(in * 2.54);
					Log.d("", "inches=" + in);
					Log.d("", "cm=" + cm);
					feetWheel.setMaxValue(2);
					feetWheel.setMinValue(1);

					inchesWheel.setMaxValue(99);
					inchesWheel.setMinValue(0);
					// inchesWheel.setDisplayedValues(grams);
					feetText.setText("Mts");
					inchesText.setText("CM");

					feetWheel.setValue(cm / 100);
					inchesWheel.setValue((cm % 100));
					setValue.setText("" + feetWheel.getValue() + " meter,"
							+ inchesWheel.getValue() + " centi meter");
				}

			}

		});

		OnValueChangeListener scrolledListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				Log.d("on valiue change", "oldVal=" + oldVal + " newVal="
						+ newVal);
				if (whatAreYouDTO.getHeightUnit().equals(
						AppConstants.HEIGHT_IN_FEET)) {
					setValue.setText("" + feetWheel.getValue() + " feet,"
							+ inchesWheel.getValue() + " inches");
				} else {
					setValue.setText("" + feetWheel.getValue() + " meter,"
							+ inchesWheel.getValue() + " centi meter");
				}

			}
		};
		feetWheel.setOnValueChangedListener(scrolledListener);
		inchesWheel.setOnValueChangedListener(scrolledListener);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				whatAreYouDTO.setHight("" + feetWheel.getValue() + "/"
						+ inchesWheel.getValue());
				if (whatAreYouDTO.getHeightUnit().equals(
						AppConstants.HEIGHT_IN_FEET)) {
					selectedHeight.setText("" + feetWheel.getValue() + "'"
							+ inchesWheel.getValue() + "\"");
				} else {
					selectedHeight.setText("" + feetWheel.getValue() + " m,"
							+ inchesWheel.getValue() + " cm");

				}

				Log.d("", "hight=" + whatAreYouDTO.getHight() + " "
						+ whatAreYouDTO.getHeightUnit());

				dialog.dismiss();
			}
		});

		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();

	}

	public void openDialogForWeight() {
		final String[] grams = new String[10];
		for (int i = 0; i < 10; i++) {

			grams[i] = ("" + (i * 100));

		}
		String unit[] = { "METRIC", "U.S." };
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_enter_weight);

		final NumberPicker feetWheel = (NumberPicker) dialog
				.findViewById(R.id.feetWheel);
		final NumberPicker inchesWheel = (NumberPicker) dialog
				.findViewById(R.id.inchesWheel);
		final NumberPicker unitWheel = (NumberPicker) dialog
				.findViewById(R.id.unitWheel);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		Button cancle = (Button) dialog.findViewById(R.id.cancle);

		final TextView feetText = (TextView) dialog.findViewById(R.id.feet);
		final TextView inchesText = (TextView) dialog.findViewById(R.id.inches);
		final TextView setValue = (TextView) dialog.findViewById(R.id.setValue);
		// title.setText(titleString);
		// text.setText(titleString);

		unitWheel.setMaxValue(1);
		unitWheel.setMinValue(0);
		unitWheel.setDisplayedValues(unit);
		setValue.setText("WEIGHT");
		if (whatAreYouDTO.getWeightUnit().equals(
				AppConstants.WEIGHT_IN_KILOGRAM)) {
			unitWheel.setValue(0);
			feetWheel.setMaxValue(199);
			feetWheel.setMinValue(10);

			inchesWheel.setMaxValue(9);
			inchesWheel.setMinValue(0);
			inchesWheel.setDisplayedValues(grams);
			feetText.setText("kilo");
			inchesText.setText("gms");

			String w = whatAreYouDTO.getWeight();
			// setValue.setText(h.split("/")[0] + "Feet" + h.split("/")[1]
			// + "Inches");
			feetWheel.setValue(Integer.valueOf(w.split("/")[0]));
			inchesWheel.setValue((Integer.valueOf(w.split("/")[1])) / 100);

			// setValue.setText("" + feetWheel.getValue() + " kilo,"
			// + grams[inchesWheel.getValue()] + " grams");
			setValue.setText("" + feetWheel.getValue() + " kilo");

		} else {
			unitWheel.setValue(1);
			feetWheel.setMaxValue(439);
			feetWheel.setMinValue(22);

			inchesWheel.setMaxValue(9);
			inchesWheel.setMinValue(0);
			inchesWheel.setDisplayedValues(grams);
			feetText.setText("lb");
			inchesText.setText("oz");

			String h = whatAreYouDTO.getWeight();
			// setValue.setText(w.split("/")[0] + "Kilo" + w.split("/")[1]
			// + "Grams");
			feetWheel.setValue(Integer.valueOf(h.split("/")[0]));
			inchesWheel.setValue((Integer.valueOf(h.split("/")[1])) / 100);

			// setValue.setText("" + feetWheel.getValue() + " pound,"
			// + grams[inchesWheel.getValue()] + " ounce");

			setValue.setText("" + feetWheel.getValue() + " pound");
		}

		unitWheel.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (newVal == 0) {
					whatAreYouDTO
							.setWeightUnit(AppConstants.WEIGHT_IN_KILOGRAM);

					int gms = (int) ((feetWheel.getValue() * 454) + (inchesWheel
							.getValue() * 28.35));
					feetWheel.setMaxValue(199);
					feetWheel.setMinValue(10);

					inchesWheel.setMaxValue(9);
					inchesWheel.setMinValue(0);
					inchesWheel.setDisplayedValues(grams);
					feetText.setText("kilo");
					inchesText.setText("gms");

					feetWheel.setValue(gms / 1000);
					inchesWheel.setValue(Math.round((gms % 1000) / 100));

					// setValue.setText("" + feetWheel.getValue() + " kilo,"
					// + inchesWheel.getValue() + " grams");
					setValue.setText("" + feetWheel.getValue() + " kilo");
				} else {
					whatAreYouDTO.setWeightUnit(AppConstants.WEIGHT_IN_POUND);
					int oz = (int) (((feetWheel.getValue() * 1000) + (inchesWheel
							.getValue())) * 0.0353);
					feetWheel.setMaxValue(439);
					feetWheel.setMinValue(22);

					inchesWheel.setMaxValue(9);
					inchesWheel.setMinValue(0);
					inchesWheel.setDisplayedValues(grams);
					feetText.setText("lb");
					inchesText.setText("oz");

					feetWheel.setValue(oz / 16);
					inchesWheel.setValue(Math.round((oz % 16) / 100));
					// setValue.setText("" + feetWheel.getValue() + " pound,"
					// + inchesWheel.getValue() + " ounce");

					setValue.setText("" + feetWheel.getValue() + " pound");
				}

			}

		});

		OnValueChangeListener scrolledListener = new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				if (whatAreYouDTO.getWeightUnit().equals(
						AppConstants.WEIGHT_IN_KILOGRAM)) {
					// setValue.setText("" + feetWheel.getValue() + " kilo,"
					// + grams[inchesWheel.getValue()] + " grams");
					setValue.setText("" + feetWheel.getValue() + " kilo");
				} else {
					// setValue.setText("" + feetWheel.getValue() + " pound,"
					// + grams[inchesWheel.getValue()] + " ounce");

					setValue.setText("" + feetWheel.getValue() + " pound");
				}

			}
		};
		feetWheel.setOnValueChangedListener(scrolledListener);
		inchesWheel.setOnValueChangedListener(scrolledListener);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				whatAreYouDTO.setWeight("" + feetWheel.getValue() + "/"
						+ grams[inchesWheel.getValue()]);

				if (whatAreYouDTO.getWeightUnit().equals(
						AppConstants.WEIGHT_IN_KILOGRAM)) {
					selectedWeight.setText("" + feetWheel.getValue() + " kg");
				} else {
					selectedWeight.setText("" + feetWheel.getValue() + " lb");

				}
				Log.d("", "weight=" + whatAreYouDTO.getWeight() + " "
						+ whatAreYouDTO.getWeightUnit());

				dialog.dismiss();
			}
		});

		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();

	}

	//

	private int getMeterToInches(Double meter) {
		Double inches = Double.valueOf(meter) * 39.3701;
		// value = value*12;

		return (int) Math.round(inches);

	}

	private void seProfile1Layout(View viewLayout, final ViewGroup container,
			final int position) {

		group1 = (ListView) viewLayout.findViewById(R.id.list1);
		submiButton1 = (ImageButton) viewLayout
				.findViewById(R.id.submitButton1);

		final WhatAreYouListAdapter myAdapter = new WhatAreYouListAdapter(
				context, whatAreYouDataList1, 0);
		group1.setAdapter(myAdapter);

		group1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				for (int i = 0; i < whatAreYouDataList1.size(); i++) {
					if (i == pos) {
						whatAreYouDataList1.get(i).setSelected(true);

					} else {
						whatAreYouDataList1.get(i).setSelected(false);

					}
				}
				Log.d("", "position=" + pos);
				whatAreYouDTO.setWhatAreYou(""
						+ whatAreYouDataList1.get(pos).getId());
				myAdapter.notifyDataSetChanged();

			}

		});

		submiButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("".equals(whatAreYouDTO.getWhatAreYou())) {
					Toast.makeText(context,
							"please select at least one choice",
							Toast.LENGTH_SHORT).show();
				} else {
					((ViewPager) container).setCurrentItem(position + 1);
				}

			}
		});

	}

	private void setRegistrationLayout(View viewLayout,
			final ViewGroup container, final int position) {

		// backButton = (ImageView) viewLayout.findViewById(R.id.back_button);
		langaugeButton = (Button) viewLayout.findViewById(R.id.selectLanguage);
		submitButton = (ImageButton) viewLayout.findViewById(R.id.done_button);
		firstName = (EditText) viewLayout.findViewById(R.id.first_name);
		lastName = (EditText) viewLayout.findViewById(R.id.last_name);
		email = (EditText) viewLayout.findViewById(R.id.email);
		mobile = (EditText) viewLayout.findViewById(R.id.mobile_no);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(10);
		mobile.setFilters(filterArray);
		password = (EditText) viewLayout.findViewById(R.id.password);
		confirmPassword = (EditText) viewLayout
				.findViewById(R.id.confirmPassword);

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		firstName.setTypeface(typeface);
		lastName.setTypeface(typeface);
		mobile.setTypeface(typeface);
		email.setTypeface(typeface);
		password.setTypeface(typeface);
		confirmPassword.setTypeface(typeface);

		whatAreYouDTO = userDTO.getWhatAreYouDTO();
		if (AppConstants.ENGLISH == userDTO.getLanguage()) {
			langaugeButton.setBackgroundResource(R.drawable.eng_select);
		} else {
			langaugeButton.setBackgroundResource(R.drawable.spn_select);
		}

		firstName.setText(userDTO.getFirstName());

		lastName.setText(userDTO.getLastName());
		email.setText(userDTO.getEmail());
		email.setEnabled(false);
		mobile.setText(userDTO.getMobile());
		password.setText("aa");
		confirmPassword.setText("aa");
		password.setVisibility(View.INVISIBLE);
		confirmPassword.setVisibility(View.INVISIBLE);

		langaugeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AppConstants.ENGLISH == userDTO.getLanguage()) {
					userDTO.setLanguage(AppConstants.SPANISH);
					langaugeButton.setBackgroundResource(R.drawable.spn_select);
				} else {
					userDTO.setLanguage(AppConstants.ENGLISH);
					langaugeButton.setBackgroundResource(R.drawable.eng_select);
				}
				Log.d("", "language=" + userDTO.getLanguage());
			}

		});

		confirmPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int actionId,
					KeyEvent arg2) {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				startRegistrationProcess(container, position);
				return false;
			}
		});

		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startRegistrationProcess(container, position);

			}
		});

	}

	private void startRegistrationProcess(ViewGroup container, int position) {

		if (0 == firstName.getText().toString().trim().length()) {
			Toast.makeText(context, "Enter User name", Toast.LENGTH_SHORT)
					.show();
		}
		// else if (0 == lastName.getText().toString().trim().length()) {
		// Toast.makeText(context, "Enter Last name", Toast.LENGTH_SHORT)
		// .show();
		// }

		else if ((email.getText().toString().trim().length() == 0)) {
			Toast.makeText(context, "Enter email id", Toast.LENGTH_SHORT)
					.show();
		} else if (!checkEmailCorrect(email.getText().toString().trim())) {
			Toast.makeText(context, "Enter correct email id",
					Toast.LENGTH_SHORT).show();
		}
		// else if (0 == mobile.getText().toString().trim().length()) {
		// Toast.makeText(context, "Enter mobile number", Toast.LENGTH_SHORT)
		// .show();
		// } else if (mobile.getText().toString().trim().length() < 10) {
		// Toast.makeText(context,
		// "Please enter at least 10 digit mobile number",
		// Toast.LENGTH_SHORT).show();
		// }
		else {
			// setUserDto();
			userDTO.setFirstName(firstName.getText().toString());
			userDTO.setLastName(lastName.getText().toString());
			userDTO.setEmail(email.getText().toString());
			userDTO.setMobile(mobile.getText().toString());

			((ViewPager) container).setCurrentItem(position + 1);
		}

	}

	private String checkValidation() {

		String validationCheck = "Enter";

		if (0 == userDTO.getFirstName().toString().trim().length()) {
			validationCheck = validationCheck + " User name,";
		}
		// if (0 == userDTO.getLastName().toString().trim().length()) {
		// validationCheck = validationCheck + " Last name,";
		// }
		// if (0 == mobile.getText().toString().trim().length()) {
		// validationCheck = validationCheck + " mobile number,";
		// } else if (mobile.getText().toString().trim().length() < 10) {
		// validationCheck = validationCheck + " mobile number not correct,";
		// }
		// else
		// if (mobile.getText().toString().trim().length()<10) {
		// validationCheck = validationCheck + "mobi,";
		// }
		if ((email.getText().toString().trim().length() == 0)) {
			validationCheck = validationCheck + " email id,";
		} else

		if (!checkEmailCorrect(userDTO.getEmail().toString().trim())) {
			validationCheck = validationCheck + " Correct email id,";
		}

		if ("".equals(whatAreYouDTO.getWhatAreYou())) {
			validationCheck = validationCheck + " What are you,";
		}
		if ("".equals(whatAreYouDTO.getHight())) {
			validationCheck = validationCheck + " Height,";
		}
		if ("".equals(whatAreYouDTO.getWeight())) {
			validationCheck = validationCheck + " Weight,";
		}
		if ("".equals(whatAreYouDTO.getAge())) {
			validationCheck = validationCheck + " Age,";
		}

		return validationCheck;

	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	private UserDTO parseUserDataAndSetInSession(JSONObject mJsonObject)
			throws JSONException {
		UserDTO userDTO = new UserDTO();
		AddressDTO addressDTO = new AddressDTO();
		WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO();
		List<InterestsDTO> interestsDTOs = new ArrayList<InterestsDTO>();
		List<UserProfileImageDTO> userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
		List<FacebookLikesDTO> facebookLikesDTOs = new ArrayList<FacebookLikesDTO>();

		JSONObject MainObject = mJsonObject.getJSONObject("user");
		JSONArray jsonArrayInterest = mJsonObject.getJSONArray("intrest");
		JSONArray jsonArrayImages = mJsonObject.getJSONArray("images");
		JSONArray jsonArrayFacebookLikes = mJsonObject
				.getJSONArray("fbintrast");
		System.out.println(MainObject);
		userDTO.setLanguage(MainObject.getString("user_language"));
		userDTO.setUserID(MainObject.getString("user_id"));
		userDTO.setEmail(MainObject.getString("user_email"));
		userDTO.setFirstName(MainObject.getString("user_fname"));
		userDTO.setLastName(MainObject.getString("user_lname"));
		userDTO.setProfileImage(MainObject.getString("user_image"));
		userDTO.setMobile(MainObject.getString("user_mobile"));
		userDTO.setAboutMe(MainObject.getString("user_note"));
		userDTO.setIsFirstTime(MainObject.getString("isFirstTime"));
		if (MainObject.getString("user_facebook_Interest").equals(
				AppConstants.FACEBOOK_LIKE_ENABLE)) {
			userDTO.setFacebookLikeEnable(true);
		} else {
			userDTO.setFacebookLikeEnable(false);
		}
		this.userDTO.setUserID(MainObject.getString("user_id"));
		whatAreYouDTO.setFeet(MainObject.getString("user_height"));
		whatAreYouDTO.setInches(MainObject.getString("user_inches"));
		whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
		whatAreYouDTO.setHight(MainObject.getString("user_height"));
		whatAreYouDTO.setAge(MainObject.getString("user_age"));
		whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
		whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
		whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));

		// whatAreYouDTO.setAge(MainObject.getString("user_note"));
		whatAreYouDTO.setRelationshipStatus(MainObject
				.getString("user_relationshipStatus"));
		whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
		whatAreYouDTO.setWhatDoYouKrave(MainObject
				.getString("user_whatDoYouKrave"));

		for (int i = 0; i < jsonArrayInterest.length(); i++) {
			JSONObject interestJsonObject = jsonArrayInterest.getJSONObject(i);
			InterestsDTO interestsDTO = new InterestsDTO();
			interestsDTO.setInterestId(interestJsonObject
					.getString("intrest_id"));
			interestsDTOs.add(interestsDTO);

		}

		for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) {
			JSONObject facebookJsonObject = jsonArrayFacebookLikes
					.getJSONObject(i);
			FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
			facebookLikesDTO.setLikeId(facebookJsonObject
					.getString("intrest_id"));

			facebookLikesDTOs.add(facebookLikesDTO);

		}
		for (int i = 0; i < jsonArrayImages.length(); i++) {
			JSONObject imagesJsonObject = jsonArrayImages.getJSONObject(i);
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(imagesJsonObject
					.getString("image_id"));
			userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1
					+ imagesJsonObject.getString("image_path"));
			userProfileImageDTO.setImagePosition(imagesJsonObject
					.getString("image_position"));

			if (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
					.getString("user_img_status"))) {
				userProfileImageDTO.setIsImageActive(true);
			}

			userProfileImageDTOs.add(userProfileImageDTO);

		}
		if (!"url".equals(userDTO.getProfileImage())) {
			Log.d("", "facebook image at first position in list");
			UserProfileImageDTO userProfileImageDTO = new UserProfileImageDTO();
			userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
			userProfileImageDTO.setImagePath(userDTO.getProfileImage());
			userProfileImageDTO.setIsImageActive(true);
			userProfileImageDTOs.add(0, userProfileImageDTO);
		}
		userDTO.setWhatAreYouDTO(whatAreYouDTO);
		userDTO.setInterestList(interestsDTOs);
		userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
		userDTO.setFacebookLikesDTOs(facebookLikesDTOs);
		return userDTO;
	}

	/*
	 * private UserDTO parseUserDataAndSetInSession(JSONObject mJsonObject)
	 * throws JSONException { UserDTO userDTO = new UserDTO(); new AddressDTO();
	 * WhatAreYouDTO whatAreYouDTO = new WhatAreYouDTO(); List<InterestsDTO>
	 * interestsDTOs = new ArrayList<InterestsDTO>(); List<UserProfileImageDTO>
	 * userProfileImageDTOs = new ArrayList<UserProfileImageDTO>();
	 * List<FacebookLikesDTO> facebookLikesDTOs = new
	 * ArrayList<FacebookLikesDTO>();
	 * 
	 * JSONObject MainObject = mJsonObject.getJSONObject("user"); JSONArray
	 * jsonArrayInterest = mJsonObject.getJSONArray("intrest"); JSONArray
	 * jsonArrayImages = mJsonObject.getJSONArray("images"); JSONArray
	 * jsonArrayFacebookLikes = mJsonObject .getJSONArray("fbintrast");
	 * System.out.println(MainObject);
	 * userDTO.setLanguage(MainObject.getString("user_language"));
	 * userDTO.setUserID(MainObject.getString("user_id"));
	 * userDTO.setEmail(MainObject.getString("user_email"));
	 * userDTO.setFirstName(MainObject.getString("user_fname"));
	 * userDTO.setLastName(MainObject.getString("user_lname"));
	 * userDTO.setProfileImage(MainObject.getString("user_image"));
	 * userDTO.setMobile(MainObject.getString("user_mobile"));
	 * userDTO.setAboutMe(MainObject.getString("user_note"));
	 * userDTO.setIsFirstTime(MainObject.getString("isFirstTime")); if
	 * (MainObject.getString("user_facebook_Interest").equals(
	 * AppConstants.FACEBOOK_LIKE_ENABLE)) {
	 * userDTO.setFacebookLikeEnable(true); } else {
	 * userDTO.setFacebookLikeEnable(false); }
	 * this.userDTO.setUserID(MainObject.getString("user_id"));
	 * whatAreYouDTO.setFeet(MainObject.getString("user_height"));
	 * whatAreYouDTO.setInches(MainObject.getString("user_inches"));
	 * whatAreYouDTO.setMeters(MainObject.getString("user_meters"));
	 * whatAreYouDTO.setHight(MainObject.getString("user_height"));
	 * whatAreYouDTO.setAge(MainObject.getString("user_age"));
	 * whatAreYouDTO.setWeight(MainObject.getString("user_weight"));
	 * whatAreYouDTO.setHeightUnit(MainObject.getString("user_heightUnit"));
	 * whatAreYouDTO.setWeightUnit(MainObject.getString("user_weightUnit"));
	 * 
	 * // whatAreYouDTO.setAge(MainObject.getString("user_note"));
	 * whatAreYouDTO.setRelationshipStatus(MainObject
	 * .getString("user_relationshipStatus"));
	 * whatAreYouDTO.setWhatAreYou(MainObject.getString("user_whatAreYou"));
	 * whatAreYouDTO.setWhatDoYouKrave(MainObject
	 * .getString("user_whatDoYouKrave"));
	 * 
	 * for (int i = 0; i < jsonArrayInterest.length(); i++) { JSONObject
	 * interestJsonObject = jsonArrayInterest.getJSONObject(i); InterestsDTO
	 * interestsDTO = new InterestsDTO();
	 * interestsDTO.setInterestId(interestJsonObject .getString("intrest_id"));
	 * interestsDTOs.add(interestsDTO);
	 * 
	 * }
	 * 
	 * for (int i = 0; i < jsonArrayFacebookLikes.length(); i++) { JSONObject
	 * facebookJsonObject = jsonArrayFacebookLikes .getJSONObject(i);
	 * FacebookLikesDTO facebookLikesDTO = new FacebookLikesDTO();
	 * facebookLikesDTO.setLikeId(facebookJsonObject .getString("intrest_id"));
	 * 
	 * facebookLikesDTOs.add(facebookLikesDTO);
	 * 
	 * } for (int i = 0; i < jsonArrayImages.length(); i++) { JSONObject
	 * imagesJsonObject = jsonArrayImages.getJSONObject(i); UserProfileImageDTO
	 * userProfileImageDTO = new UserProfileImageDTO();
	 * userProfileImageDTO.setImageId(imagesJsonObject .getString("image_id"));
	 * userProfileImageDTO.setImagePath(AppConstants.BASE_IMAGE_PATH_1 +
	 * imagesJsonObject.getString("image_path")); if
	 * (AppConstants.IMAGE_ACTIVE.equals(imagesJsonObject
	 * .getString("user_img_status"))) {
	 * userProfileImageDTO.setIsImageActive(true); }
	 * userProfileImageDTOs.add(userProfileImageDTO);
	 * 
	 * } if (!"url".equals(userDTO.getProfileImage())) { Log.d("",
	 * "facebook image at first position in list"); UserProfileImageDTO
	 * userProfileImageDTO = new UserProfileImageDTO();
	 * userProfileImageDTO.setImageId(AppConstants.FACEBOOK_IMAGE);
	 * userProfileImageDTO.setImagePath(userDTO.getProfileImage());
	 * userProfileImageDTO.setIsImageActive(true); userProfileImageDTOs.add(0,
	 * userProfileImageDTO); } userDTO.setWhatAreYouDTO(whatAreYouDTO);
	 * userDTO.setInterestList(interestsDTOs);
	 * userDTO.setUserProfileImageDTOs(userProfileImageDTOs);
	 * userDTO.setFacebookLikesDTOs(facebookLikesDTOs); return userDTO; }
	 */

	class UpdateProfileAsynchTask extends AsyncTask<String, Void, String> {
		private JSONArray jsonArray;
		private JSONObject jsonObject;
		private String vStatus;

		// private TransparentProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog = new TransparentProgressDialog(context);
			// // dialog.setMessage("Please Wait...");
			// dialog.setCanceledOnTouchOutside(false);
			// dialog.show();
		}

		protected String doInBackground(String... args) {
			String vResult = "";
			try {

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(args[0]);

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				Log.d("", "language=" + userDTO.getLanguage());
				reqEntity.addPart("language",
						new StringBody(userDTO.getLanguage()));
				reqEntity.addPart("user_id", new StringBody(userDTO.getUserID()
						.toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("fname", new StringBody(userDTO
						.getFirstName().toString()));
				reqEntity.addPart("lname", new StringBody(userDTO.getLastName()
						.toString()));
				reqEntity.addPart("mobile", new StringBody(userDTO.getMobile()
						.toString()));
				reqEntity.addPart("email", new StringBody(userDTO.getEmail()
						.toString()));
				reqEntity.addPart("password", new StringBody(userDTO
						.getPassword().toString()));
				reqEntity.addPart("isFirstTime", new StringBody(userDTO
						.getIsFirstTime().toString()));
				reqEntity.addPart("user_whatAreYou", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatAreYou().toString()));
				reqEntity.addPart("feet", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));

				reqEntity.addPart("inches", new StringBody(userDTO
						.getWhatAreYouDTO().getInches().toString()));
				reqEntity.addPart("meters", new StringBody(userDTO
						.getWhatAreYouDTO().getMeters().toString()));
				reqEntity.addPart("height", new StringBody(userDTO
						.getWhatAreYouDTO().getHight().toString()));
				reqEntity.addPart("heightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getHeightUnit().toString()));
				reqEntity.addPart("weight", new StringBody(userDTO
						.getWhatAreYouDTO().getWeight().toString()));
				reqEntity.addPart("weightUnit", new StringBody(userDTO
						.getWhatAreYouDTO().getWeightUnit().toString()));
				reqEntity.addPart("age", new StringBody(userDTO
						.getWhatAreYouDTO().getAge().toString()));
				reqEntity.addPart("user_relationshipStatus", new StringBody(
						userDTO.getWhatAreYouDTO().getRelationshipStatus()
								.toString()));
				reqEntity.addPart("user_whatDoYouKrave", new StringBody(userDTO
						.getWhatAreYouDTO().getWhatDoYouKrave().toString()));

				reqEntity.addPart("user_note", new StringBody(userDTO
						.getAboutMe().toString()));

				reqEntity.addPart("user_facebook_image",
						new StringBody(userDTO.getProfileImage()));

				for (int i = 0; i < userDTO.getInterestList().size(); i++) {
					// reqEntity.addPart("interest[" + i + "][]", new
					// StringBody(String.valueOf(i + 1)));
					reqEntity
							.addPart(
									"interest[" + i + "][]",
									new StringBody(String.valueOf(userDTO
											.getInterestList().get(i)
											.getInterestId())));

				}
				if (userDTO.isFacebookLikeEnable()) {
					if (userDTO.getFacebookLikesDTOs() != null) {
						Log.d("",
								"isFacebookLikesEnable="
										+ userDTO.isFacebookLikeEnable());
						Log.d("", "size="
								+ userDTO.getFacebookLikesDTOs().size());
						reqEntity.addPart("user_facebook_Interest",
								new StringBody(
										AppConstants.FACEBOOK_LIKE_ENABLE));
						for (int i = 0; i < userDTO.getFacebookLikesDTOs()
								.size(); i++) {
							// reqEntity.addPart("interest[" + i + "][]", new
							// StringBody(String.valueOf(i + 1)));
							reqEntity.addPart(
									"fbintrast[" + i + "][]",
									new StringBody(String.valueOf(userDTO
											.getFacebookLikesDTOs().get(i)
											.getImagePath())));

						}
					}
				} else {
					reqEntity.addPart("user_facebook_Interest", new StringBody(
							AppConstants.FACEBOOK_LIKE_DISABLE));
				}

				post.setEntity(reqEntity);
				HttpResponse response = client.execute(post);
				StringBuffer stringBuffer = new StringBuffer("");
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				String LineSeparator = System.getProperty("line.separator");
				while ((line = rd.readLine()) != null) {
					stringBuffer.append(line + LineSeparator);
				}
				vResult = stringBuffer.toString();

			} catch (UnsupportedEncodingException e) {
				// writing error to Log
				Log.v("Message...1", e.getMessage());
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Log.v("Message...2", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			} catch (IOException e) {
				Log.v("Message...3", e.getMessage());
				// writing exception to log
				e.printStackTrace();
			}
			return vResult;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// dialog.dismiss();
			System.out.println("Responce....Admin Reg.String" + result);
			// [{"status":"Success","admin_unique_id":"admin4"}]
			try {
				jsonArray = new JSONArray(result);

				jsonObject = jsonArray.getJSONObject(0);
				vStatus = jsonObject.getString("status");
				if (vStatus.equals("success")) {
					// Toast.makeText(context, "Profile Updated Successfully",
					// Toast.LENGTH_SHORT).show();

					sessionManager
							.setUserDetail(parseUserDataAndSetInSession(jsonObject));

				} else if (vStatus.equals("failure")) {
					// Toast.makeText(context, "Profile Not Updated",
					// Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	// validation to check mail id
	boolean checkEmailCorrect(String Email) {

		String pttn = "[a-zA-Z0-9._-]+@[a-z_-]+\\.+[a-z]+";
		Pattern p = Pattern.compile(pttn);
		Matcher m = p.matcher(Email);

		if (m.matches()) {
			return true;
		}
		return false;
	}
}