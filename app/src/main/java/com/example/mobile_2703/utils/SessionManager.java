// FILE: app/src/main/java/com/example/mobile_2703/utils/SessionManager.java
package com.example.mobile_2703.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mobile_2703.constants.AppConstants;

/**
 * SessionManager - Quản lý phiên đăng nhập bằng SharedPreferences.
 */
public class SessionManager {

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs  = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /**
     * Lưu thông tin đăng nhập.
     */
    public void saveLogin(long userId, String username) {
        editor.putLong(AppConstants.PREF_USER_ID, userId);
        editor.putString(AppConstants.PREF_USERNAME, username);
        editor.putBoolean(AppConstants.PREF_IS_LOGGED_IN, true);
        editor.apply();
    }

    /**
     * Đăng xuất: xóa toàn bộ SharedPreferences.
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }

    /**
     * Kiểm tra trạng thái đăng nhập.
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(AppConstants.PREF_IS_LOGGED_IN, false);
    }

    /**
     * Lấy userId của người dùng hiện tại.
     */
    public long getUserId() {
        return prefs.getLong(AppConstants.PREF_USER_ID, -1L);
    }

    /**
     * Lấy username của người dùng hiện tại.
     */
    public String getUsername() {
        return prefs.getString(AppConstants.PREF_USERNAME, null);
    }
}
