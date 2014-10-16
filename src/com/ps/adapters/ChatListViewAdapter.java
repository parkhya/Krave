package com.ps.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.krave.kraveapp.Activity_Block_Dude;
import com.krave.kraveapp.Activity_ShowImage;
import com.krave.kraveapp.FragmentChatMatches;
import com.krave.kraveapp.R;
import com.ps.models.ChatDetailsDTO;
import com.ps.utill.AppConstants;
import com.ps.utill.CircleImageView;
import com.ps.utill.FontStyle;
import com.ps.utill.ImageLoader;
import com.ps.utill.ImageLoaderCircle;
import com.ps.utill.SessionManager;

public class ChatListViewAdapter extends ArrayAdapter {

	// private TextView chatText;
	private List<ChatDetailsDTO> chatMessageList = new ArrayList();
	// private LinearLayout singleMessageContainer;
	private Context context;
	private SessionManager sessionManager;
	ImageLoaderCircle imageLoaderCircle;
	String previousDate = "";
	ImageLoader imageLoader;

	// @Override
	// public void add(ChatDetailsDTO object) {
	// chatMessageList.add(object);
	// super.add(object);
	// }

	public ChatListViewAdapter(Context context, ArrayList<ChatDetailsDTO> list) {
		super(context, 0);
		this.chatMessageList = list;
		this.context = context;
		sessionManager = new SessionManager(context);
		imageLoaderCircle = new ImageLoaderCircle(context);
		imageLoader = new ImageLoader(context);

	}

	public int getCount() {
		return this.chatMessageList.size();
	}

	public ChatDetailsDTO getItem(int index) {
		return this.chatMessageList.get(index);
	}

	class ViewHolder {
		public TextView chatText, dateTime;
		public CircleImageView profilePick;
		public ImageView onlineImage, sendImageView;
		public LinearLayout singleMessageContainer;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder = null;
		final ChatDetailsDTO chatMessageObj = getItem(position);
		Log.d("", "chat message id=" + chatMessageObj.getId());
		// if (position != 0) {
		// if (getItem(position - 1).getId().equals("-1")) {
		// row = null;
		// }
		// }
		if (!chatMessageObj.getId().equals("-1") && position != 0) {
			// if (row == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_chat_item, parent, false);
			viewHolder.singleMessageContainer = (LinearLayout) row
					.findViewById(R.id.singleMessageContainer);
			viewHolder.chatText = (TextView) row.findViewById(R.id.message);
			viewHolder.profilePick = (CircleImageView) row
					.findViewById(R.id.userimage);
			viewHolder.onlineImage = (ImageView) row
					.findViewById(R.id.onlineImage);
			viewHolder.dateTime = (TextView) row.findViewById(R.id.dateTime);
			viewHolder.sendImageView = (ImageView) row
					.findViewById(R.id.imageTransfer);

			Typeface typeface = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Md);
			viewHolder.chatText.setTypeface(typeface);
			viewHolder.dateTime.setTypeface(typeface);

			row.setTag(viewHolder);
			// } else {
			// viewHolder = (ViewHolder) row.getTag();
			// }

			// SimpleDateFormat dateFormat = new
			// SimpleDateFormat("dd-MMM-yyyy hh:mm");
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
			Date date = new Date(Long.valueOf(chatMessageObj.getTime()));

			viewHolder.dateTime.setText(dateFormat.format(date));
			/* check Messge type */
			if (chatMessageObj.getMeassageType().equals(
					AppConstants.MESSAGE_TYPE_TEXT)) {
				viewHolder.chatText.setVisibility(View.VISIBLE);
				viewHolder.sendImageView.setVisibility(View.GONE);
				viewHolder.chatText.setText(chatMessageObj.getMessage());
			} else {
				viewHolder.chatText.setVisibility(View.GONE);
				viewHolder.sendImageView.setVisibility(View.VISIBLE);

				// byte windowImageString[] = Base64.decode(
				// chatMessageObj.getMessage(), Base64.DEFAULT);
				//
				// Bitmap bitmap = BitmapFactory.decodeByteArray(
				// windowImageString, 0, windowImageString.length);
				//
				// viewHolder.sendImageView.setImageBitmap(getResizedBitmap(
				// bitmap, 100, 100));
				// bitmap.recycle();
				imageLoader.DisplayImage(chatMessageObj.getMessage(),
						viewHolder.sendImageView);
			}
			viewHolder.sendImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Activity_ShowImage.chatMessageObj = chatMessageObj;
					Intent intent = new Intent(context,
							Activity_ShowImage.class);
					context.startActivity(intent);

				}
			});

			/* set message row */
			if (sessionManager.getUserDetail().getUserID()
					.equals(chatMessageObj.getFromuser())) {
				viewHolder.chatText
						.setBackgroundResource(R.drawable.chat_small1);
				viewHolder.sendImageView
						.setBackgroundResource(R.drawable.chat_small1);
				viewHolder.singleMessageContainer.setGravity(Gravity.RIGHT);
				viewHolder.profilePick.setVisibility(View.GONE);
				viewHolder.onlineImage.setVisibility(View.GONE);

			} else {
				viewHolder.profilePick.setVisibility(View.VISIBLE);
				if (FragmentChatMatches.isOnline) {
					viewHolder.onlineImage.setVisibility(View.VISIBLE);
				} else {
					viewHolder.onlineImage.setVisibility(View.GONE);
				}
				try {
					if (FragmentChatMatches.userDTO != null
							&& FragmentChatMatches.userDTO
									.getUserProfileImageDTOs().size() > 0
							&& FragmentChatMatches.userDTO
									.getUserProfileImageDTOs().get(0)
									.getIsImageActive()) {
						imageLoaderCircle
								.DisplayImage(FragmentChatMatches.userDTO
										.getUserProfileImageDTOs().get(0)
										.getImagePath(), viewHolder.profilePick);
					}
				} catch (Exception e) {

				}
				viewHolder.chatText
						.setBackgroundResource(R.drawable.chat_small2);
				viewHolder.sendImageView
						.setBackgroundResource(R.drawable.chat_small2);
				viewHolder.singleMessageContainer.setGravity(Gravity.LEFT);

			}
			// SimpleDateFormat dateFormat1 = new
			// SimpleDateFormat("dd-MMM-yyyy");
			// Date date2 = new Date(Long.valueOf(chatMessageObj.getTime()));
			//
			// if (!previousDate.equals(dateFormat1.format(date2))) {
			// ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
			// chatDetailsDTO.setId("-1");
			// chatDetailsDTO.setDate(dateFormat1.format(date2));
			// chatMessageList.add(position, chatDetailsDTO);
			// notifyDataSetChanged();
			// }
			((ListView) parent).setSelection(position);
		} else {

			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.row_chat_date_header_item, parent,
					false);
			TextView dateTime = (TextView) row.findViewById(R.id.dateTime);
			Typeface typeface = FontStyle.getFont(context,
					AppConstants.HelveticaNeueLTStd_Bd);

			dateTime.setTypeface(typeface);
			dateTime.setText(chatMessageObj.getDate());
		}

		return row;
	}

	public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth,
			int bitmapHeight) {
		return Bitmap
				.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
	}

	// public Bitmap getThumbnail(ContentResolver cr, String path)
	// throws Exception {
	//
	// Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	// new String[] { MediaStore.MediaColumns._ID },
	// MediaStore.MediaColumns.DATA + "=?", new String[] { path },
	// null);
	// if (ca != null && ca.moveToFirst()) {
	// int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
	// ca.close();
	// return MediaStore.Images.Thumbnails.getThumbnail(cr, id,
	// MediaStore.Images.Thumbnails.MICRO_KIND, null);
	// }
	//
	// ca.close();
	// return null;
	//
	// }
	//
	// public Bitmap decodeToBitmap(byte[] decodedByte) {
	// return BitmapFactory
	// .decodeByteArray(decodedByte, 0, decodedByte.length);
	// }

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();

	}
	// private void setMeassge(ChatDetailsDTO chatMessageObj, ViewHolder
	// viewHolder) {
	// if (chatMessageObj.getMeassageType().equals(
	// AppConstants.MESSAGE_TYPE_TEXT)) {
	// viewHolder.chatText.setText(chatMessageObj.getMessage());
	// viewHolder.chatText.setBackgroundResource(R.drawable.chat_small1);
	// viewHolder.sendImageView.setVisibility(View.GONE);
	// } else {
	// Bitmap myBitmap = BitmapFactory.decodeFile(chatMessageObj
	// .getMessage());
	//
	// viewHolder.sendImageView.setImageBitmap(myBitmap);
	// viewHolder.chatText.setVisibility(View.GONE);
	// }
	// }
}