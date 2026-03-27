package com.example.mobile_2703.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mobile_2703.constants.AppConstants;

/**
 * SessionManager - Lưu trạng thái đăng nhập vào SharedPreferences.
 *
 * USAGE:
 *   SessionManager session = new SessionManager(context);
 *   session.saveSession(user);   // Sau khi đăng nhập thành công
 *   session.isLoggedIn();        // Kiểm tra đã đăng nhập chưa
 *   session.clearSession();      // Đăng xuất
 */
public class SessionManager {

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs  = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /** Lưu thông tin đăng nhập */
    public void saveSession(int userId, String username) {
        editor.putBoolean(AppConstants.PREF_IS_LOGGED_IN, true);
        editor.putInt(AppConstants.PREF_USER_ID, userId);
        editor.putString(AppConstants.PREF_USERNAME, username);
        editor.apply();
    }

    /** Xóa session (đăng xuất) */
    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(AppConstants.PREF_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(AppConstants.PREF_USER_ID, -1);
    }

    public String getUsername() {
        return prefs.getString(AppConstants.PREF_USERNAME, "");
    }
}
