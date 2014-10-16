package com.ps.adapters;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.FragmentDetailDudesProfile;
import com.krave.kraveapp.FragmentDudesProfile;
import com.krave.kraveapp.FragmentHome;
import com.krave.kraveapp.R;
import com.ps.models.UserDTO;
import com.ps.models.UserListDTO;
import com.ps.services.GPSTracker;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.MyXMLHandler;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class FindDudesAdapter extends PagerAdapter {

	Activity_Home context;
	public int filterListSize = 0;
	View layout;
	ArrayList<UserDTO> filterList = null;
	ArrayList<UserDTO> arrayList;
	public ImageLoader imageLoader;
	private SessionManager sessionManager;
	// private ItemFilter mFilter = new ItemFilter();
	private GPSTracker gpsTracker;

	// GetOnline[] getOnlinesArray = new GetOnline[6];
	public FindDudesAdapter(Activity mActivity, ArrayList<UserDTO> mainArrayList) {
		context = (Activity_Home) mActivity;
		filterList = mainArrayList;
		arrayList = mainArrayList;

		imageLoader = new ImageLoader(context);
		gpsTracker = new GPSTracker(mActivity);
		Log.d("", "arrayList  size=" + arrayList.size());

	}

	@Override
	public int getCount() {
		// notifyDataSetChanged();
		int size = filterList.size();
		int count = (size) / 6;
		if (0 < size % 6) {
			count++;
		}
		// Log.d("", "count value=" + count);
		return count;
	}

	ImageView imageView1, imageView2, imageView3, imageView4, imageView5,
			imageView6;
	ImageView imageView1Stamp, imageView2Stamp, imageView3Stamp,
			imageView4Stamp, imageView5Stamp, imageView6Stamp;
	TextView textView1, textView2, textView3, textView4, textView5, textView6;
	TextView textView11, textView22, textView33, textView44, textView55,
			textView66;
	View viewcontainer = null;

	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		viewcontainer = container;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.row_home, null);
		imageView1 = (ImageView) layout.findViewById(R.id.imageView1);
		imageView2 = (ImageView) layout.findViewById(R.id.imageView2);
		imageView3 = (ImageView) layout.findViewById(R.id.imageView3);
		imageView4 = (ImageView) layout.findViewById(R.id.imageView4);
		imageView5 = (ImageView) layout.findViewById(R.id.imageView5);
		imageView6 = (ImageView) layout.findViewById(R.id.imageView6);
		imageView1Stamp = (ImageView) layout.findViewById(R.id.imageView1Stamp);
		imageView2Stamp = (ImageView) layout.findViewById(R.id.imageView2Stamp);
		imageView3Stamp = (ImageView) layout.findViewById(R.id.imageView3Stamp);
		imageView4Stamp = (ImageView) layout.findViewById(R.id.imageView4Stamp);
		imageView5Stamp = (ImageView) layout.findViewById(R.id.imageView5Stamp);
		imageView6Stamp = (ImageView) layout.findViewById(R.id.imageView6Stamp);
		textView1 = (TextView) layout.findViewById(R.id.textView1);
		textView11 = (TextView) layout.findViewById(R.id.textView11);
		textView2 = (TextView) layout.findViewById(R.id.textView2);
		textView22 = (TextView) layout.findViewById(R.id.textView22);
		textView3 = (TextView) layout.findViewById(R.id.textView3);
		textView33 = (TextView) layout.findViewById(R.id.textView33);
		textView4 = (TextView) layout.findViewById(R.id.textView4);
		textView44 = (TextView) layout.findViewById(R.id.textView44);
		textView5 = (TextView) layout.findViewById(R.id.textView5);
		textView55 = (TextView) layout.findViewById(R.id.textView55);
		textView6 = (TextView) layout.findViewById(R.id.textView6);
		textView66 = (TextView) layout.findViewById(R.id.textView66);
		setTypeFace(layout);

		ImageView imageViewArray[] = { imageView1, imageView2, imageView3,
				imageView4, imageView5, imageView6 };
		ImageView imageViewArrayStamp[] = { imageView1Stamp, imageView2Stamp,
				imageView3Stamp, imageView4Stamp, imageView5Stamp,
				imageView6Stamp };
		TextView textViewArrayForName[] = { textView1, textView2, textView3,
				textView4, textView5, textView6 };
		TextView textViewArrayForDistance[] = { textView11, textView22,
				textView33, textView44, textView55, textView66 };

		int j = 0;
		int temp = 6 * (position + 1);
		if (temp > filterList.size()) {
			temp = filterList.size();
		}

		// for(int d=0;d<getOnlinesArray.length;d++){
		// if(getOnlinesArray[d]!=null){
		// getOnlinesArray[d].cancel(true);
		// }
		// }
		for (int i = (6 * position); i < temp; i++) {
			final int index = i;
			UserDTO userDTO = filterList.get(i);
			if (userDTO.getUserProfileImageDTOs().size() > 0) {
				if (userDTO.getUserProfileImageDTOs().get(0).getImageId()
						.equals(AppConstants.FACEBOOK_IMAGE)) {
					imageViewArray[j].setScaleType(ScaleType.FIT_CENTER);
					imageLoader.DisplayImage(userDTO.getUserProfileImageDTOs()
							.get(0).getImagePath(), imageViewArray[j]);
				} else {
					imageViewArray[j].setScaleType(ScaleType.FIT_CENTER);
					if (!userDTO.getUserProfileImageDTOs().get(0)
							.getIsImageActive()) {
						imageViewArrayStamp[j].setVisibility(View.VISIBLE);
						imageViewArray[j]
								.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
					} else {
						imageLoader.DisplayImage(userDTO
								.getUserProfileImageDTOs().get(0)
								.getImagePath(), imageViewArray[j]);
					}
				}

			} else {
				imageViewArray[j]
						.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
			}
			String age = "";
			float distance = (float) 0.0;
			try {
				age = calculateAge(userDTO.getWhatAreYouDTO().getAge());
			} catch (Exception e) {

			}
			textViewArrayForName[j].setText(userDTO.getFirstName() + " , "
					+ age);

			try {
				gpsTracker.getLocation();
				distance = distFrom(gpsTracker.getLatitude(),
						gpsTracker.getLongitude(),
						Double.valueOf(userDTO.getLatLongDTO().getLatitude()),
						Double.valueOf(userDTO.getLatLongDTO().getLongitude()));
			} catch (Exception e) {

			}
			textViewArrayForDistance[j].setText("" + distance + " miles");

			if (AppConstants.ONLINE.equals(userDTO.getIsOnline())) {
				imageViewArray[j].setBackgroundResource(R.drawable.online_user);
			} else if (AppConstants.ABSENT.equals(userDTO.getIsOnline())) {
				imageViewArray[j].setBackgroundResource(R.drawable.away_user);
			} else {
				imageViewArray[j]
						.setBackgroundResource(R.drawable.offline_user);
			}
			if (WebServiceConstants.isOnlineWitoutToast(context)) {
				GetOnline getOnline = (GetOnline) new GetOnline().execute(
						userDTO.getUserID(), imageViewArray[j], i);
				getOnline.cancel(true);
			}

			imageViewArray[j].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					FragmentHome.clearData = true;
					FragmentDetailDudesProfile.comeFrom = AppConstants.FRAGMENT_DUDES_PROFILE;
					context.Attach_Fragment(AppConstants.FRAGMENT_DETAIL_DUDES_PROFILE);
					Activity_Home.dudeCommonList = filterList;
					Activity_Home.index = index;

				}
			});

			j++;
		}
		((ViewPager) container).addView(layout, 0);
		return layout;
	}

	private void setTypeFace(View view) {

		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Roman);
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		textView3.setTypeface(typeface);
		textView4.setTypeface(typeface);
		textView5.setTypeface(typeface);
		textView6.setTypeface(typeface);
		textView11.setTypeface(typeface);
		textView22.setTypeface(typeface);
		textView33.setTypeface(typeface);
		textView44.setTypeface(typeface);
		textView55.setTypeface(typeface);
		textView66.setTypeface(typeface);

	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	// }
	// public Filter getFilter() {
	// return mFilter;
	// }

	// private class ItemFilter extends Filter {
	// @Override
	// protected FilterResults performFiltering(CharSequence constraint) {
	// String filterString = constraint.toString().toLowerCase();
	// FilterResults results = new FilterResults();
	// // final List<UserDTO> list = arrayList;
	// if (AppConstants.ONLINE.equals(filterString)) {
	// int count = arrayList.size();
	// final ArrayList<UserDTO> nlist = new ArrayList<UserDTO>(count);
	//
	// String compayerString;
	// for (int i = 0; i < count; i++) {
	// UserDTO dto = arrayList.get(i);
	// compayerString = dto.getIsOnline().toString();
	// if (compayerString.toLowerCase().contains(filterString)) {
	// nlist.add(dto);
	// }
	// }
	// results.values = nlist;
	// results.count = nlist.size();
	// } else {
	// results.values = arrayList;
	// results.count = arrayList.size();
	// }
	// return results;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// protected void publishResults(CharSequence constraint,
	// FilterResults results) {
	// filterList = (ArrayList<UserDTO>) results.values;
	// notifyDataSetChanged();
	// filterListSize = filterList.size();
	// Intent broadcast = new Intent();
	// broadcast.setAction("onpage");
	// context.sendBroadcast(broadcast);
	//
	// Log.d("", "filter list size=" + filterList.size());
	// }
	//
	// }

	@Override
	public int getItemPosition(Object object) {
		if (arrayList.contains((View) object)) {
			return arrayList.indexOf((View) object);
		} else {
			return POSITION_NONE;
		}
	}

	// //Logic for online user////
	public class GetOnline extends AsyncTask<Object, Void, String> {
		ImageView imageView;
		MyXMLHandler myXMLHandler;
		int poss;

		@Override
		protected String doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			imageView = (ImageView) arg0[1];
			poss = (Integer) arg0[2];
			// try {

			try {
				/** Handling XML */
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				/** Send URL to parse XML Tags */
				URL sourceUrl = new URL(
						"http://198.12.150.189:9090/plugins/presence/status?jid="
								+ arg0[0]
								+ "@ip-198-12-150-189.secureserver.net&type=xml");
				/** Create handler to handle XML Tags ( extends DefaultHandler ) */
				myXMLHandler = new MyXMLHandler();
				xr.setContentHandler(myXMLHandler);
				xr.parse(new InputSource(sourceUrl.openStream()));

			} catch (Exception e) {
				System.out.println("XML Pasing Excpetion = " + e.getMessage());
			}

			// String posturl =
			// "http://198.12.150.189:9090/plugins/presence/status?jid="+arg0[0]+"@ip-198-12-150-189.secureserver.net&type=text";
			//
			// DefaultHttpClient httpclient = new DefaultHttpClient();
			// HttpGet httpget = new HttpGet(posturl);
			// HttpResponse response = httpclient.execute(httpget);
			// BufferedReader in = new BufferedReader(new InputStreamReader(
			// response.getEntity().getContent()));
			// StringBuffer sb = new StringBuffer("");
			// String line = "";
			// String NL = System.getProperty("line.separator");
			// while ((line = in.readLine()) != null) {
			// sb.append(line + NL);
			// }
			// in.close();
			// String result = sb.toString();
			// Log.v("My Response :: ", result);
			//
			// return result;
			// } catch (UnsupportedEncodingException e) {
			// // writing error to Log
			// Log.v("Message...1", e.getMessage());
			// e.printStackTrace();
			// } catch (ClientProtocolException e) {
			// Log.v("Message...2", e.getMessage());
			// // writing exception to log
			// e.printStackTrace();
			// } catch (IOException e) {
			// Log.v("Message...3", e.getMessage());
			// // writing exception to log
			// e.printStackTrace();
			// }

			return myXMLHandler.getType();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("string is " + result);
			System.out.println("string is " + result == ("null"));
			try {
				if (result.equals("")) {
					filterList.get(poss).setIsOnline(AppConstants.ONLINE);
					imageView.setBackgroundResource(R.drawable.online_user);
				} else if (result.equals("away")) {
					filterList.get(poss).setIsOnline(AppConstants.ABSENT);
					imageView.setBackgroundResource(R.drawable.away_user);
				} else if (result.equals("unavailable")) {
					filterList.get(poss).setIsOnline(AppConstants.OFFLINE);
					imageView.setBackgroundResource(R.drawable.offline_user);
				} else if (result.equals("error")) {
					filterList.get(poss).setIsOnline(AppConstants.OFFLINE);
					imageView.setBackgroundResource(R.drawable.offline_user);
				}

			} catch (Exception e) {

			}
		}
	}

	private String calculateAge(String berthDateString) {
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

		return "" + number_of_years;

	}

	public float distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;// 1609.34

		double kms = (dist);
		return (float) (Math.round(kms * 10.0) / 10.0);

	}

}
