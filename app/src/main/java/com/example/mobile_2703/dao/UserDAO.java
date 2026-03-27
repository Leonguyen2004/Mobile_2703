package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.User;

import java.util.List;

/**
 * UserDAO - Xử lý toàn bộ thao tác CRUD cho bảng "user".
 *
 * Kế thừa CRUD cơ bản từ BaseDAO, thêm các query đặc thù cho User.
 *
 * USAGE:
 *   UserDAO userDAO = new UserDAO(context);
 *   User user = userDAO.login("admin", "admin123");
 *   List<User> users = userDAO.getAll();
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
        ContentValues values = new ContentValues();
        values.put(DBConstants.User.COL_USERNAME,  user.getUsername());
        values.put(DBConstants.User.COL_PASSWORD,  user.getPassword());
        values.put(DBConstants.User.COL_FULL_NAME, user.getFullName());
        values.put(DBConstants.User.COL_EMAIL,     user.getEmail());
        values.put(DBConstants.User.COL_ROLE,      user.getRole());
        return values;
    }

    @Override
    protected User fromCursor(Cursor cursor) {
        return new User(
                getInt(cursor,    DBConstants.User.COL_ID),
                getString(cursor, DBConstants.User.COL_USERNAME),
                getString(cursor, DBConstants.User.COL_PASSWORD),
                getString(cursor, DBConstants.User.COL_FULL_NAME),
                getString(cursor, DBConstants.User.COL_EMAIL),
                getString(cursor, DBConstants.User.COL_ROLE),
                getString(cursor, DBConstants.User.COL_CREATED_AT)
        );
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /**
     * Đăng nhập: tìm user theo username và password.
     * @return User nếu khớp, null nếu sai thông tin.
     */
    public User login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.User.COL_USERNAME + " = ? AND "
                            + DBConstants.User.COL_PASSWORD + " = ?",
                    new String[]{username, password},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                user = fromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "Login error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return user;
    }

    /**
     * Kiểm tra username đã tồn tại chưa.
     */
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;
        try {
            cursor = db.query(
                    tableName,
                    new String[]{DBConstants.User.COL_ID},
                    DBConstants.User.COL_USERNAME + " = ?",
                    new String[]{username},
                    null, null, null
            );
            exists = cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "isUsernameExists error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return exists;
    }

    /**
     * Tìm user theo username.
     */
    public User getByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.User.COL_USERNAME + " = ?",
                    new String[]{username},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                user = fromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "getByUsername error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return user;
    }

    /**
     * Lấy danh sách tất cả admin.
     */
    public List<User> getAllAdmins() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        java.util.List<User> list = new java.util.ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.User.COL_ROLE + " = ?",
                    new String[]{DBConstants.Role.ADMIN},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllAdmins error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}
