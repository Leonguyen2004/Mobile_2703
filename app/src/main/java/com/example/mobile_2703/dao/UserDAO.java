package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.User;

/**
 * UserDAO - DAO cho bảng users.
 * Kế thừa BaseDAO, thêm các query đặc thù: login, findByUsername.
 */
public class UserDAO extends BaseDAO<User> {

    private static final String TAG = "UserDAO";

    public UserDAO(Context context) {
        super(context, DBConstants.Table.USER);
    }

    // =========================================================
    // IMPLEMENT ABSTRACT METHODS
    // =========================================================

    @Override
    protected ContentValues toContentValues(User user) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.User.COL_USERNAME,  user.getUsername());
        cv.put(DBConstants.User.COL_PASSWORD,  user.getPassword());
        cv.put(DBConstants.User.COL_FULL_NAME, user.getFullName());
        cv.put(DBConstants.User.COL_EMAIL,     user.getEmail());
        cv.put(DBConstants.User.COL_PHONE,     user.getPhone());
        cv.put(DBConstants.User.COL_ROLE,      user.getRole());
        return cv;
    }

    @Override
    protected User fromCursor(Cursor cursor) {
        User user = new User();
        user.setId(        getInt(cursor,    DBConstants.User.COL_ID));
        user.setUsername(  getString(cursor, DBConstants.User.COL_USERNAME));
        user.setPassword(  getString(cursor, DBConstants.User.COL_PASSWORD));
        user.setFullName(  getString(cursor, DBConstants.User.COL_FULL_NAME));
        user.setEmail(     getString(cursor, DBConstants.User.COL_EMAIL));
        user.setPhone(     getString(cursor, DBConstants.User.COL_PHONE));
        user.setRole(      getString(cursor, DBConstants.User.COL_ROLE));
        user.setCreatedAt( getString(cursor, DBConstants.User.COL_CREATED_AT));
        return user;
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /**
     * Xác thực đăng nhập.
     * @return User nếu đúng username & password, null nếu sai.
     */
    public User login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName,
                    null,
                    DBConstants.User.COL_USERNAME + " = ? AND " + DBConstants.User.COL_PASSWORD + " = ?",
                    new String[]{username, password},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "Login error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * Tìm user theo username.
     * @return User hoặc null.
     */
    public User findByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName,
                    null,
                    DBConstants.User.COL_USERNAME + " = ?",
                    new String[]{username},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "FindByUsername error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }
}
