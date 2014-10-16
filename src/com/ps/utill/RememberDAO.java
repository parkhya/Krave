package com.ps.utill;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ps.models.ChatDetailsDTO;

public class RememberDAO {
	// -------Chat data base--------
	private static final String KEY_SNO = "id";
	private static final String KEY_MESSAGE = "message";
	private static final String KEY_FROMUSER = "fromuser";
	private static final String KEY_TOUSER = "touser";
	private static final String KEY_DATE = "chatdate";
	private static final String KEY_TIME = "chattime";

	// -------Databse Name--------
	private static final String DATABASE_NAME = "KRAVEDB";
	private static final int DATABASE_VERSION = 1;

	// -------Databse Table Name--------
	private static final String DATABASE_TABLE_CHAT = "chattable";
	private static final String CREATE_TABLE_CHAT = "create table chattable(id integer primary key autoincrement,message text," +
			"fromuser text,touser text,chatdate text,chattime text)";
	private static final String DROP_TABLE_CHAT = "drop table if exists chattable";

	

	private static Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public RememberDAO(Context context) {
		this.context = context;
		DBHelper = new DatabaseHelper(this.context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// db.execSQL(CREATE_TABLE_CONTACT);
			db.execSQL(CREATE_TABLE_CHAT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// db.execSQL(DROP_TABLE_CONTACT);
			db.execSQL(CREATE_TABLE_CHAT);
			onCreate(db);
		}
	}

	public RememberDAO open() {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long getCount() {
		Cursor c = db.query(DATABASE_TABLE_CHAT,
				new String[] { KEY_SNO }, null, null, null, null,
				null);
		long count = c.getCount();
		c.close();
		return count;
	}

	public long addChat(ChatDetailsDTO chat) {
		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGE, chat.getMessage());
		values.put(KEY_FROMUSER, chat.getFromuser());
		values.put(KEY_TOUSER, chat.getTouser());
		values.put(KEY_DATE, chat.getDate());
		values.put(KEY_TIME, chat.getTime());
		
		return db.insert(DATABASE_TABLE_CHAT, null, values);
	}

//	
//	public ArrayList<BlockedUserDTO> getAllBlockedUser() {
//		ArrayList<BlockedUserDTO> arrayList = new ArrayList<BlockedUserDTO>();
//		Cursor c = db.query(DATABASE_TABLE_BLOCK_CONTACT, null, null, null,
//				null, null, null);
//		BlockedUserDTO blockedUserDTO;
//		while (c.moveToNext()) {
//			blockedUserDTO = new BlockedUserDTO();
//			blockedUserDTO.setId(c.getString(c
//					.getColumnIndex(KEY_BLOCK_AUTO_ID)));
//			blockedUserDTO.setBlock_id(c.getString(c
//					.getColumnIndex(KEY_BLOCK_ID)));
//			blockedUserDTO.setBlock_name(c.getString(c
//					.getColumnIndex(KEY_BLOCK_NAME)));
//			blockedUserDTO.setBlock_mobileno(c.getString(c
//					.getColumnIndex(KEY_BLOCK_MOBILE)));
//			blockedUserDTO.setBlock_image(c.getString(c
//					.getColumnIndex(KEY_BLOCK_IMAGE)));
//			blockedUserDTO.setBlock_status(c.getString(c
//					.getColumnIndex(KEY_BLOCK_STATUS)));
//			arrayList.add(blockedUserDTO);
//		}
//		c.close();
//		return arrayList;
//	}

	
}