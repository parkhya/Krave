package com.ps.utill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ps.models.ChatDetailsDTO;
//java.lang.IllegalStateException: Couldn't read row 0, col 5 from CursorWindow.  Make sure the Cursor is initialized correctly before accessing data from it.
public class KraveDAO {
	// -------Chat data base--------
	private static final String KEY_SNO = "id";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_FROMUSER = "fromuser";
	private static final String KEY_TOUSER = "touser";
	private static final String KEY_DATE = "chatdate";
	private static final String KEY_TIME = "chattime";
	private static final String KEY_MESSAGE_TYPE = "messagetype";

	// -------Databse Name--------
	private static final String DATABASE_NAME = "KRAVEDB";
	private static final int DATABASE_VERSION = 1;

	// -------Databse Table Name--------
	private static final String DATABASE_TABLE_CHAT = "chattable";
	private static final String CREATE_TABLE_CHAT = "create table chattable(id integer primary key autoincrement,message text,fromuser text,touser text,chatdate text,chattime text,messagetype text)";
	private static final String DROP_TABLE_CHAT = "drop table if exists chattable";

	private static Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public KraveDAO(Context context) {
		this.context = context;
		DBHelper = new DatabaseHelper(this.context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.d("database", "table create");
			db.execSQL(CREATE_TABLE_CHAT);
			Log.d("database", "table create");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(DROP_TABLE_CHAT);
			onCreate(db);
		}
	}

	public KraveDAO open() {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long getCount() {
		Cursor c = db.query(DATABASE_TABLE_CHAT, new String[] { KEY_SNO },
				null, null, null, null, null);
		long count = c.getCount();
		c.close();
		return count;
	}

	public long addChat(ChatDetailsDTO chatDetailsDTO) {
		db = DBHelper.getWritableDatabase();
		Log.d("database",
				"ID=" + chatDetailsDTO.getId() + ", message="
						+ chatDetailsDTO.getMessage() + ", fromuser="
						+ chatDetailsDTO.getFromuser() + ", touser="
						+ chatDetailsDTO.getTouser() + ", time="
						+ chatDetailsDTO.getTime());

		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, chatDetailsDTO.getMessage());
		values.put(KEY_FROMUSER, chatDetailsDTO.getFromuser());
		values.put(KEY_TOUSER, chatDetailsDTO.getTouser());
		values.put(KEY_DATE, "");
		values.put(KEY_TIME, chatDetailsDTO.getTime());
		values.put(KEY_MESSAGE_TYPE, chatDetailsDTO.getMeassageType());
		long row = db.insert(DATABASE_TABLE_CHAT, null, values);
		Log.d("", "row id=" + row);
		return row;

	}

	public String previousDate = "";

	public List<ChatDetailsDTO> getChat(String fromId, String toid) {
		List<ChatDetailsDTO> list = new ArrayList<ChatDetailsDTO>();
		db = DBHelper.getReadableDatabase();
		// String selectQuery = "SELECT * FROM " + DATABASE_TABLE_CHAT +
		// " where "
		// + KEY_FROMUSER + "='" +fromId +"' and "+KEY_TOUSER+"='"+toid+"'";
		//
		// String selectQuery2 = "SELECT * FROM " + DATABASE_TABLE_CHAT +
		// " where "
		// + KEY_TOUSER + "='" +fromId +"' and "+KEY_FROMUSER+"='"+toid+"'";

		String selectQuery = "Select * from chattable where (fromuser="
				+ fromId + " and touser=" + toid + ") or (fromuser=" + toid
				+ " and touser=" + fromId + ") order by id";
		Cursor cursor = db.rawQuery(selectQuery, null);
		//c != null && c.getCount() >0 && c.moveToFirst()
		if (cursor!=null && cursor.moveToFirst() && cursor.getCount()>0) {
			do {

				SimpleDateFormat dateFormat1 = new SimpleDateFormat(
						"dd MMM yyyy");
				Date date2 = new Date(Long.valueOf(cursor.getString(5)));
				String newDateMessageDate = dateFormat1.format(date2);
				if (!previousDate.equals(newDateMessageDate)) {
					ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();
					chatDetailsDTO.setId("-1");
					chatDetailsDTO.setDate(newDateMessageDate);
					list.add(chatDetailsDTO);
					previousDate = newDateMessageDate;

				}

				ChatDetailsDTO chatDetailsDTO = new ChatDetailsDTO();

				chatDetailsDTO.setId(cursor.getString(0));
				chatDetailsDTO.setMessage(cursor.getString(1));
				chatDetailsDTO.setFromuser(cursor.getString(2));
				chatDetailsDTO.setTouser(cursor.getString(3));

				chatDetailsDTO.setTime(cursor.getString(5));
				chatDetailsDTO.setMeassageType(cursor.getString(6));
				list.add(chatDetailsDTO);
				Log.d("database", "ID=" + chatDetailsDTO.getId() + ", message="
						+ chatDetailsDTO.getMessage() + ", fromuser="
						+ chatDetailsDTO.getFromuser() + ", touser="
						+ chatDetailsDTO.getTouser() + ", time="
						+ chatDetailsDTO.getTime());
			} while (cursor.moveToNext());
		}
		Log.d("", "chat list size=" + list.size());

		return list;
	}

	public void clearDataBase() {
		db = DBHelper.getReadableDatabase();

		db.delete(DATABASE_TABLE_CHAT, null, null);

	}

	public void clearChatOfUser(String fromId, String toid) {
		db = DBHelper.getReadableDatabase();
		String deleteQuery = "(fromuser=" + fromId + " and touser="
				+ toid + ") or (fromuser=" + toid + " and touser=" + fromId
				+ ")";

		db.delete(DATABASE_TABLE_CHAT, deleteQuery, null);

	}

	
	public String getString( Cursor cursor, int columnIndex )
	{
		String value = "";
	 
	    try
	    {
	        if ( !cursor.isNull( columnIndex ) )
	        {
	            value = cursor.getString( columnIndex );
	        }
	    }
	    catch ( Throwable tr )
	    {
	       // TRLogger.innerErrorLog( "[c] - " + columnIndex, tr );
	    }
	 
	    return value;
	}
	
	
	
	//
	// public ArrayList<BlockedUserDTO> getAllBlockedUser() {
	// ArrayList<BlockedUserDTO> arrayList = new ArrayList<BlockedUserDTO>();
	// Cursor c = db.query(DATABASE_TABLE_BLOCK_CONTACT, null, null, null,
	// null, null, null);
	// BlockedUserDTO blockedUserDTO;
	// while (c.moveToNext()) {
	// blockedUserDTO = new BlockedUserDTO();
	// blockedUserDTO.setId(c.getString(c
	// .getColumnIndex(KEY_BLOCK_AUTO_ID)));
	// blockedUserDTO.setBlock_id(c.getString(c
	// .getColumnIndex(KEY_BLOCK_ID)));
	// blockedUserDTO.setBlock_name(c.getString(c
	// .getColumnIndex(KEY_BLOCK_NAME)));
	// blockedUserDTO.setBlock_mobileno(c.getString(c
	// .getColumnIndex(KEY_BLOCK_MOBILE)));
	// blockedUserDTO.setBlock_image(c.getString(c
	// .getColumnIndex(KEY_BLOCK_IMAGE)));
	// blockedUserDTO.setBlock_status(c.getString(c
	// .getColumnIndex(KEY_BLOCK_STATUS)));
	// arrayList.add(blockedUserDTO);
	// }
	// c.close();
	// return arrayList;
	// }

}