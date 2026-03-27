// FILE: app/src/main/java/com/example/mobile_2703/dao/TheaterDAO.java
package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Theater;

import java.util.ArrayList;
import java.util.List;

public class TheaterDAO extends BaseDAO<Theater> {

    private static final String TAG = "TheaterDAO";

    public TheaterDAO(Context context) {
        super(context, DBConstants.Table.THEATER);
    }

    // =========================================================
    // ABSTRACT METHOD IMPLEMENTATIONS
    // =========================================================

    @Override
    protected ContentValues toContentValues(Theater theater) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Theater.COL_NAME,        theater.getName());
        cv.put(DBConstants.Theater.COL_LOCATION,    theater.getAddress());
        cv.put(DBConstants.Theater.COL_TOTAL_SEATS, theater.getTotalSeats());
        return cv;
    }

    @Override
    protected Theater fromCursor(Cursor cursor) {
        long   id         = getLong(cursor,   DBConstants.Theater.COL_ID);
        String name       = getString(cursor, DBConstants.Theater.COL_NAME);
        String address    = getString(cursor, DBConstants.Theater.COL_LOCATION);
        int    totalSeats = getInt(cursor,    DBConstants.Theater.COL_TOTAL_SEATS);
        return new Theater(id, name, address, "", totalSeats);
    }

    // =========================================================
    // OVERRIDE getAll — sắp xếp theo tên
    // =========================================================

    @Override
    public List<Theater> getAll() {
        return getAll(DBConstants.Theater.COL_NAME, true);
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /** Tìm rạp theo id. Trả về null nếu không tìm thấy. */
    public Theater findById(long id) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = null;
        try {
            cursor = db.query(
                    tableName,
                    null,
                    DBConstants.Theater.COL_ID + " = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null
            );
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "findById error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * Lấy danh sách rạp chiếu một bộ phim cụ thể (qua bảng showtimes).
     * SELECT DISTINCT t.* FROM theaters t
     * INNER JOIN showtimes s ON s.theater_id = t.id
     * WHERE s.movie_id = ?
     */
    public List<Theater> getByMovieId(long movieId) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        List<Theater>  list   = new ArrayList<>();
        Cursor         cursor = null;
        String query =
                "SELECT DISTINCT t.* FROM " + DBConstants.Table.THEATER + " t"
                + " INNER JOIN " + DBConstants.Table.SHOWTIME + " s"
                + " ON s." + DBConstants.Showtime.COL_THEATER_ID + " = t." + DBConstants.Theater.COL_ID
                + " WHERE s." + DBConstants.Showtime.COL_MOVIE_ID + " = ?";
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(movieId)});
            if (cursor != null && cursor.moveToFirst()) {
                do { list.add(fromCursor(cursor)); } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getByMovieId error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}
