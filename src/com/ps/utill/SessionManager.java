package com.ps.utill;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.facebook.Session;
import com.google.gson.Gson;
import com.krave.kraveapp.Activity_Login;
import com.ps.models.SettingDTO;
import com.ps.models.UserDTO;
import com.ps.services.ChatService;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref, pref1;

	// Editor for Shared preferences
	Editor editor, editor1;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name

	public static final String PREF_NAME = "KraveApp";
	public static final String PREF_NAME_REMEMBER = "Remember";

	private static final String IS_LOGIN = "islogin";

	private static final String USER_DETAIL = "user_detail";
	private static final String SETTING_DETAIL = "setting_detail";
	private static final String IS_REMEMBER_ME_ON = "isRememberMeOn";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String FEEDBACK = "feedback";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		pref1 = _context.getSharedPreferences(PREF_NAME_REMEMBER, PRIVATE_MODE);
		editor1 = pref1.edit();
	}

	/**
	 * Create login session
	 * */
	public void setUserDetail(UserDTO userDTO) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref

		Gson gson = new Gson();
		String vUserDTO = gson.toJson(userDTO);
		editor.putString(USER_DETAIL, vUserDTO);
		// commit changes
		editor.commit();

	}

	public UserDTO getUserDetail() {
		Gson gson = new Gson();
		String vjson = pref.getString(USER_DETAIL, "");
		UserDTO userDTO = gson.fromJson(vjson, UserDTO.class);
		return userDTO;
	}

	public void setSettingDetail(SettingDTO settingDTO) {

		Gson gson = new Gson();
		String vUser = gson.toJson(settingDTO);
		editor.putString(SETTING_DETAIL, vUser);
		// commit changes
		editor.commit();

	}

	public SettingDTO getSettingDetail() {
		Gson gson = new Gson();
		String vjson = pref.getString(SETTING_DETAIL, "");
		SettingDTO settingDTO = gson.fromJson(vjson, SettingDTO.class);
		return settingDTO;
	}

	public boolean isLogin() {
		return pref.getBoolean(IS_LOGIN, false);
	}

	public void Logout() {

		if (ChatService.connection != null) {
			if (ChatService.connection.isConnected()) {
				ChatService.connection.disconnect();
			}
		}

		facebookLogout();
		clearDataBase();

		editor.clear();
		// commit changes
		editor.commit();
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, Activity_Login.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// i.putExtra("EXIT", true);
		// Staring Login Activity
		_context.startActivity(i);
		// System.exit(0);

	}

	private void clearDataBase() {
		KraveDAO dataBaseHelper = new KraveDAO(_context);
		dataBaseHelper.clearDataBase();

	}

	private void facebookLogout() {
		// find the active session which can only be facebook in my app
		Session session = Session.getActiveSession();

		// cache related to the Session.
		if (session != null) {
			session.closeAndClearTokenInformation();
		}
		Session.setActiveSession(null);
	}

	public void setRememberMe(String email, String password) {
		editor1.putBoolean(IS_REMEMBER_ME_ON, true);
		editor1.putString(EMAIL, email);
		editor1.putString(PASSWORD, password);
		editor1.commit();
	}

	public void clearRememberMe() {
		editor1.clear();

		editor1.commit();
	}

	public Boolean isRemember() {
		return pref1.getBoolean(IS_REMEMBER_ME_ON, false);
	}

	public String getEmail() {
		return pref1.getString(EMAIL, "");
	}

	public String getPassword() {
		return pref1.getString(PASSWORD, "");
	}

	public void setFeedback(String feedback) {

		editor.putString(FEEDBACK, feedback);
		// commit changes
		editor.commit();

	}

	public String getFeedback() {

		String feedback = pref.getString(FEEDBACK, "");

		return feedback;
	}
}