package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDAO - CRUD cho bảng "category".
 */
public class CategoryDAO extends BaseDAO<Category> {

    private static final String TAG = "CategoryDAO";

    public CategoryDAO(Context context) {
        super(context, DBConstants.Table.CATEGORY);
    }

    @Override
    protected ContentValues toContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.Category.COL_NAME, category.getName());
        values.put(DBConstants.Category.COL_DESC, category.getDescription());
        return values;
    }

    @Override
    protected Category fromCursor(Cursor cursor) {
        return new Category(
                getInt(cursor,    DBConstants.Category.COL_ID),
                getString(cursor, DBConstants.Category.COL_NAME),
                getString(cursor, DBConstants.Category.COL_DESC)
        );
    }

    /**
     * Tìm category theo tên (tìm kiếm gần đúng).
     */
    public List<Category> searchByName(String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Category> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.Category.COL_NAME + " LIKE ?",
                    new String[]{"%" + keyword + "%"},
                    null, null,
                    DBConstants.Category.COL_NAME + " ASC"
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "searchByName error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}
