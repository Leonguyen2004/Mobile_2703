package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseDAO - Lớp cha cho tất cả DAO, cung cấp CRUD cơ bản.
 *
 * HOW TO USE:
 *   Tạo DAO mới kế thừa BaseDAO<T> và implement các abstract method.
 *   Xem UserDAO.java hoặc ProductDAO.java làm ví dụ.
 *
 * @param <T> Model class tương ứng với bảng (vd: User, Product)
 */
public abstract class BaseDAO<T> {

    private static final String TAG = "BaseDAO";

    protected final DatabaseHelper dbHelper;
    protected final String tableName;

    public BaseDAO(Context context, String tableName) {
        this.dbHelper  = DatabaseHelper.getInstance(context);
        this.tableName = tableName;
    }

    // =========================================================
    // ABSTRACT METHODS - Subclass bắt buộc implement
    // =========================================================

    /**
     * Chuyển object T thành ContentValues để insert/update vào SQLite.
     */
    protected abstract ContentValues toContentValues(T item);

    /**
     * Chuyển một hàng Cursor thành object T.
     */
    protected abstract T fromCursor(Cursor cursor);

    // =========================================================
    // CRUD OPERATIONS
    // =========================================================

    /**
     * Thêm một bản ghi mới.
     * @return id của bản ghi vừa thêm, hoặc -1 nếu thất bại.
     */
    public long insert(T item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;
        try {
            id = db.insert(tableName, null, toContentValues(item));
            if (id == -1) {
                Log.e(TAG, "Insert failed for table: " + tableName);
            }
        } catch (Exception e) {
            Log.e(TAG, "Insert error: " + e.getMessage());
        }
        return id;
    }

    /**
     * Cập nhật bản ghi theo id.
     * @return số hàng bị ảnh hưởng (1 = thành công, 0 = thất bại).
     */
    public int update(int id, T item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            rows = db.update(tableName, toContentValues(item), "id = ?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "Update error: " + e.getMessage());
        }
        return rows;
    }

    /**
     * Xóa bản ghi theo id.
     * @return số hàng bị xóa.
     */
    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            rows = db.delete(tableName, "id = ?", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "Delete error: " + e.getMessage());
        }
        return rows;
    }

    /**
     * Lấy bản ghi theo id.
     * @return object T hoặc null nếu không tìm thấy.
     */
    public T getById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        T item = null;
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, null, "id = ?",
                    new String[]{String.valueOf(id)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                item = fromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "GetById error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return item;
    }

    /**
     * Lấy tất cả bản ghi trong bảng.
     * @return danh sách các object T.
     */
    public List<T> getAll() {
        return getAll("id", true);
    }

    /**
     * Lấy tất cả bản ghi với sắp xếp tùy chỉnh.
     * @param orderByColumn tên cột để sắp xếp.
     * @param ascending true = ASC, false = DESC.
     */
    public List<T> getAll(String orderByColumn, boolean ascending) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<T> list = new ArrayList<>();
        Cursor cursor = null;
        String orderBy = orderByColumn + (ascending ? " ASC" : " DESC");
        try {
            cursor = db.query(tableName, null, null, null, null, null, orderBy);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "GetAll error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Đếm tổng số bản ghi trong bảng.
     */
    public int count() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int count = 0;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Count error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return count;
    }

    /**
     * Xóa tất cả dữ liệu trong bảng.
     */
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(tableName, null, null);
        } catch (Exception e) {
            Log.e(TAG, "DeleteAll error: " + e.getMessage());
        }
    }

    // =========================================================
    // HELPER METHODS - Tiện ích đọc Cursor an toàn
    // =========================================================

    protected String getString(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        return idx >= 0 ? cursor.getString(idx) : "";
    }

    protected int getInt(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        return idx >= 0 ? cursor.getInt(idx) : 0;
    }

    protected double getDouble(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        return idx >= 0 ? cursor.getDouble(idx) : 0.0;
    }

    protected long getLong(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        return idx >= 0 ? cursor.getLong(idx) : 0L;
    }

    protected boolean getBoolean(Cursor cursor, String columnName) {
        int idx = cursor.getColumnIndex(columnName);
        return idx >= 0 && cursor.getInt(idx) == 1;
    }
}

