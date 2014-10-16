package com.ps.adapters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.krave.kraveapp.Activity_Home;
import com.krave.kraveapp.R;
import com.ps.loader.TransparentProgressDialog;
import com.ps.models.UserListDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.FontStyle;
import com.ps.utill.JSONParser;
import com.ps.utill.SessionManager;
import com.ps.utill.WebServiceConstants;

public class UserListAdapter extends BaseAdapter {

	private List<UserListDTO> list;
	private Activity context;
	private LayoutInflater layoutInflater;
	private List<UserListDTO> filteredData = null;

	private ItemFilter mFilter = new ItemFilter();
	private int check;
	SessionManager sessionManager;

	public UserListAdapter(List<UserListDTO> list, Activity context, int check) {
		super();
		this.list = list;
		this.context = context;
		this.layoutInflater = context.getLayoutInflater();
		this.filteredData = list;
		this.check = check;
		sessionManager = new SessionManager(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filteredData.size();
	}

	@Override
	public UserListDTO getItem(int position) {
		// TODO Auto-generated method stub
		return filteredData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = null;
		ViewHolder viewHolder = null;
		final UserListDTO userListDTO = getItem(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			if (check == 0) {
				rowView = context.getLayoutInflater().inflate(
						R.layout.row_list, null);
			} else {
				rowView = context.getLayoutInflater().inflate(
						R.layout.row_update_delete_list, null);
				viewHolder.editList = (ImageView) rowView
						.findViewById(R.id.editListName);
				viewHolder.deleteList = (ImageView) rowView
						.findViewById(R.id.deleteList);
			}

			viewHolder.mTextView = (TextView) rowView
					.findViewById(R.id.textView);

			Typeface typeface = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Roman);
			viewHolder.mTextView.setTypeface(typeface);

			rowView.setTag(viewHolder);
		} else {
			rowView = convertView;
			viewHolder = (ViewHolder) rowView.getTag();
		}

		if (check == 1) {
			viewHolder.editList.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openDailogForEditName(userListDTO);

				}
			});

			viewHolder.deleteList.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openDailog(userListDTO);

				}
			});
		}
		viewHolder.mTextView.setText(userListDTO.getListName());

		return rowView;
	}

	// static class for object
	public static class ViewHolder {
		ImageView mImageView, editList, deleteList;
		TextView mTextView;

	}

	// // getview for single row
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// View rowView = null;
	// ViewHolder viewHolder = new ViewHolder();
	// if (convertView == null) {
	// rowView = ((Activity) mActivity).getLayoutInflater().inflate(
	// R.layout.row_what_are_you, null);
	// viewHolder.mImageView = (ImageView) rowView
	// .findViewById(R.id.imageView1);
	// viewHolder.mTextView = (TextView) rowView
	// .findViewById(R.id.textView);
	// rowView.setTag(viewHolder);
	// } else {
	// rowView = convertView;
	// viewHolder = (ViewHolder) rowView.getTag();
	// }
	// if (marrayList.get(position).isSelected() == true) {
	// ;
	// viewHolder.mImageView.setImageDrawable(mActivity.getResources()
	// .getDrawable(R.drawable.selected_icon));
	// } else {
	// viewHolder.mImageView.setImageDrawable(mActivity.getResources()
	// .getDrawable(R.drawable.normal_icon));
	//
	// }
	//
	// viewHolder.mTextView.setText(marrayList.get(position).getName());
	// return rowView;
	// }
	// }

	public Filter getFilter() {
		return mFilter;
	}

	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			String filterString = constraint.toString().toLowerCase();
			FilterResults results = new FilterResults();
			// final List<UserDTO> list = arrayList;

			int count = list.size();
			final ArrayList<UserListDTO> nlist = new ArrayList<UserListDTO>(
					count);

			String compayerString;
			for (int i = 0; i < count; i++) {
				UserListDTO dto = list.get(i);
				compayerString = dto.getListName().toString();
				if (compayerString.toLowerCase().contains(filterString)) {
					nlist.add(dto);
				}
			}
			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filteredData = (ArrayList<UserListDTO>) results.values;
			notifyDataSetChanged();
		}

	}

	public void openDailog(final UserListDTO dto) {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.dialog_logout);

		Button cancle = (Button) dialog.findViewById(R.id.cancle);
		Button ok = (Button) dialog.findViewById(R.id.ok);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText("Do you really want to delete this list");
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);
		title.setTypeface(typeface);

		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

				new ListDeleteAsyncTask().execute(WebServiceConstants.BASE_URL
						+ WebServiceConstants.DELETE_LIST, dto.getListId());

			}
		});
		dialog.show();
	}

	public void openDailogForEditName(final UserListDTO dto) {
		final Dialog dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.activity_r_update_list_name);

		TextView cancleButton = (TextView) dialog
				.findViewById(R.id.cancleButton);
		TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
		TextView title = (TextView) dialog.findViewById(R.id.title);
		final EditText listName = (EditText) dialog
				.findViewById(R.id.newListFields);
		Typeface typeface = FontStyle.getFont(context,
				AppConstants.HelveticaNeueLTStd_Lt);

		cancleButton.setTypeface(typeface);
		okButton.setTypeface(typeface);
		title.setTypeface(typeface);
		title.setTypeface(typeface);
		listName.setText("" + dto.getListName());

		cancleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

				new UpdateListNameAsyncTask().execute(
						WebServiceConstants.BASE_URL
								+ WebServiceConstants.UPDATE_LIST,
						dto.getListId(), listName.getText().toString());

			}
		});
		dialog.show();
	}

	class ListDeleteAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String listId;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// http://198.12.150.189/~simssoe/index.php?action=deletelist&listid=2&userid=1
			listId = args[1];
			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("listid", "" + args[1]));

			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					Toast.makeText(context, "List deleted successfully",
							Toast.LENGTH_SHORT).show();
					for (int i = 0; i < filteredData.size(); i++) {
						UserListDTO userListDTO = filteredData.get(i);
						if (userListDTO.getListId().equals(listId)) {
							filteredData.remove(userListDTO);
							list.remove(userListDTO);
						}
					}
					notifyDataSetChanged();
				} else {
					Toast.makeText(context,
							"List not edited, please try again",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}

	class UpdateListNameAsyncTask extends AsyncTask<String, Void, JSONArray> {
		String vStatus;
		TransparentProgressDialog dialog;
		String listId;
		String title;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// http://198.12.150.189/~simssoe/index.php?action=updatelist&userid=1&title=testsdfsdfsdf&listid=3
			dialog = new TransparentProgressDialog(context);
			// dialog.setMessage("Please Wait...");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		protected JSONArray doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// http://198.12.150.189/~simssoe/index.php?action=deletelist&listid=2&userid=1
			listId = args[1];
			title = args[2];
			params.add(new BasicNameValuePair("userid", ""
					+ sessionManager.getUserDetail().getUserID()));
			params.add(new BasicNameValuePair("listid", "" + args[1]));
			params.add(new BasicNameValuePair("title", "" + args[2]));
			JSONArray json = new JSONParser().makeHttpRequest(args[0], "POST",
					params);
			Log.d("get user response", "" + json);
			return json;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {
			super.onPostExecute(jsonArray);
			dialog.dismiss();
			try {
				JSONObject mJsonObject = jsonArray.getJSONObject(0);
				vStatus = mJsonObject.getString("status");
				System.out.print("" + jsonArray);
				if (vStatus.equals("success")) {
					Toast.makeText(context, "List edited successfully",
							Toast.LENGTH_SHORT).show();
					for (int i = 0; i < filteredData.size(); i++) {
						UserListDTO userListDTO = filteredData.get(i);
						if (userListDTO.getListId().equals(listId)) {
							userListDTO.setListName(title);
						}
					}

					notifyDataSetChanged();

				} else {
					Toast.makeText(context,
							"List not edited, please try again",
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();

			}

		}
	}
}
