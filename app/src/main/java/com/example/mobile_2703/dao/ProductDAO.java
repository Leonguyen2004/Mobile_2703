package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO - CRUD cho bảng "product".
 *
 * Bao gồm ví dụ JOIN với bảng category để lấy categoryName.
 */
public class ProductDAO extends BaseDAO<Product> {

    private static final String TAG = "ProductDAO";

    public ProductDAO(Context context) {
        super(context, DBConstants.Table.PRODUCT);
    }

    @Override
    protected ContentValues toContentValues(Product product) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.Product.COL_NAME,        product.getName());
        values.put(DBConstants.Product.COL_PRICE,       product.getPrice());
        values.put(DBConstants.Product.COL_DESCRIPTION, product.getDescription());
        values.put(DBConstants.Product.COL_IMAGE_URL,   product.getImageUrl());
        values.put(DBConstants.Product.COL_STOCK,       product.getStock());
        values.put(DBConstants.Product.COL_CATEGORY_ID, product.getCategoryId());
        return values;
    }

    @Override
    protected Product fromCursor(Cursor cursor) {
        Product product = new Product(
                getInt(cursor,    DBConstants.Product.COL_ID),
                getString(cursor, DBConstants.Product.COL_NAME),
                getDouble(cursor, DBConstants.Product.COL_PRICE),
                getString(cursor, DBConstants.Product.COL_DESCRIPTION),
                getString(cursor, DBConstants.Product.COL_IMAGE_URL),
                getInt(cursor,    DBConstants.Product.COL_STOCK),
                getInt(cursor,    DBConstants.Product.COL_CATEGORY_ID),
                getString(cursor, DBConstants.Product.COL_CREATED_AT)
        );
        // Nếu query có JOIN, đọc thêm categoryName
        int catNameIdx = cursor.getColumnIndex("category_name");
        if (catNameIdx >= 0) {
            product.setCategoryName(cursor.getString(catNameIdx));
        }
        return product;
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /**
     * Lấy tất cả sản phẩm kèm tên category (JOIN).
     *
     * Đây là ví dụ về cách viết rawQuery JOIN giữa 2 bảng.
     * Tham khảo pattern này khi cần JOIN các bảng khác.
     */
    public List<Product> getAllWithCategory() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> list = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT p.*, c." + DBConstants.Category.COL_NAME + " AS category_name "
                + "FROM " + DBConstants.Table.PRODUCT + " p "
                + "LEFT JOIN " + DBConstants.Table.CATEGORY + " c "
                + "ON p." + DBConstants.Product.COL_CATEGORY_ID
                + " = c." + DBConstants.Category.COL_ID
                + " ORDER BY p." + DBConstants.Product.COL_NAME + " ASC";

        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllWithCategory error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Lấy sản phẩm theo category.
     */
    public List<Product> getByCategoryId(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.Product.COL_CATEGORY_ID + " = ?",
                    new String[]{String.valueOf(categoryId)},
                    null, null,
                    DBConstants.Product.COL_NAME + " ASC"
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getByCategoryId error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Tìm kiếm sản phẩm theo tên.
     */
    public List<Product> searchByName(String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.Product.COL_NAME + " LIKE ?",
                    new String[]{"%" + keyword + "%"},
                    null, null,
                    DBConstants.Product.COL_NAME + " ASC"
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

    /**
     * Lấy sản phẩm còn hàng (stock > 0).
     */
    public List<Product> getInStockProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Product> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.Product.COL_STOCK + " > 0",
                    null, null, null,
                    DBConstants.Product.COL_NAME + " ASC"
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getInStockProducts error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}
