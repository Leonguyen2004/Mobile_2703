package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Showtime;

import java.util.ArrayList;
import java.util.List;

/**
 * ShowtimeDAO - DAO cho bảng showtimes.
 * Các query JOIN để nạp movieTitle và theaterName vào Showtime object.
 */
public class ShowtimeDAO extends BaseDAO<Showtime> {

    private static final String TAG = "ShowtimeDAO";

    public ShowtimeDAO(Context context) {
        super(context, DBConstants.Table.SHOWTIME);
    }

    // =========================================================
    // IMPLEMENT ABSTRACT METHODS
    // =========================================================

    @Override
    protected ContentValues toContentValues(Showtime showtime) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Showtime.COL_MOVIE_ID,        showtime.getMovieId());
        cv.put(DBConstants.Showtime.COL_THEATER_ID,      showtime.getTheaterId());
        cv.put(DBConstants.Showtime.COL_SHOW_DATE,       showtime.getShowDate());
        cv.put(DBConstants.Showtime.COL_SHOW_TIME,       showtime.getShowTime());
        cv.put(DBConstants.Showtime.COL_PRICE,           showtime.getPrice());
        cv.put(DBConstants.Showtime.COL_AVAILABLE_SEATS, showtime.getAvailableSeats());
        return cv;
    }

    @Override
    protected Showtime fromCursor(Cursor cursor) {
        Showtime s = new Showtime();
        s.setId(             getInt(cursor,    DBConstants.Showtime.COL_ID));
        s.setMovieId(        getInt(cursor,    DBConstants.Showtime.COL_MOVIE_ID));
        s.setTheaterId(      getInt(cursor,    DBConstants.Showtime.COL_THEATER_ID));
        s.setShowDate(       getString(cursor, DBConstants.Showtime.COL_SHOW_DATE));
        s.setShowTime(       getString(cursor, DBConstants.Showtime.COL_SHOW_TIME));
        s.setPrice(          getDouble(cursor, DBConstants.Showtime.COL_PRICE));
        s.setAvailableSeats( getInt(cursor,    DBConstants.Showtime.COL_AVAILABLE_SEATS));
        // Transient JOIN fields (populated by JOIN queries below)
        s.setMovieTitle(  getString(cursor, "movie_title"));
        s.setTheaterName( getString(cursor, "theater_name"));
        return s;
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /**
     * Base JOIN query: showtimes JOIN movies AND theaters.
     */
    private static final String JOIN_QUERY =
            "SELECT s.*, m." + DBConstants.Movie.COL_TITLE   + " AS movie_title, "
                    +        "t." + DBConstants.Theater.COL_NAME + " AS theater_name "
                    + "FROM " + DBConstants.Table.SHOWTIME + " s "
                    + "JOIN "  + DBConstants.Table.MOVIE   + " m ON s." + DBConstants.Showtime.COL_MOVIE_ID   + " = m." + DBConstants.Movie.COL_ID   + " "
                    + "JOIN "  + DBConstants.Table.THEATER + " t ON s." + DBConstants.Showtime.COL_THEATER_ID + " = t." + DBConstants.Theater.COL_ID;

    /**
     * Lấy tất cả suất chiếu kèm thông tin phim và rạp.
     */
    public List<Showtime> getAllWithDetails() {
        return queryWithJoin(JOIN_QUERY + " ORDER BY s." + DBConstants.Showtime.COL_SHOW_DATE
                + ", s." + DBConstants.Showtime.COL_SHOW_TIME, null);
    }

    /**
     * Lấy suất chiếu theo phim (JOIN theaters để lấy tên rạp).
     */
    public List<Showtime> getByMovieId(int movieId) {
        String sql = JOIN_QUERY
                + " WHERE s." + DBConstants.Showtime.COL_MOVIE_ID + " = ?"
                + " ORDER BY s." + DBConstants.Showtime.COL_SHOW_DATE + ", s." + DBConstants.Showtime.COL_SHOW_TIME;
        return queryWithJoin(sql, new String[]{String.valueOf(movieId)});
    }

    /**
     * Lấy suất chiếu theo rạp (JOIN movies để lấy tên phim).
     */
    public List<Showtime> getByTheaterId(int theaterId) {
        String sql = JOIN_QUERY
                + " WHERE s." + DBConstants.Showtime.COL_THEATER_ID + " = ?"
                + " ORDER BY s." + DBConstants.Showtime.COL_SHOW_DATE + ", s." + DBConstants.Showtime.COL_SHOW_TIME;
        return queryWithJoin(sql, new String[]{String.valueOf(theaterId)});
    }

    /**
     * Lấy suất chiếu theo ngày.
     */
    public List<Showtime> getByDate(String date) {
        String sql = JOIN_QUERY
                + " WHERE s." + DBConstants.Showtime.COL_SHOW_DATE + " = ?"
                + " ORDER BY s." + DBConstants.Showtime.COL_SHOW_TIME;
        return queryWithJoin(sql, new String[]{date});
    }

    /**
     * Giảm số ghế còn trống đi 1 sau khi đặt vé thành công.
     * @return số hàng cập nhật (1 = thành công, 0 = thất bại hoặc không còn ghế).
     */
    public int decreaseAvailableSeats(int showtimeId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL(
                    "UPDATE " + DBConstants.Table.SHOWTIME
                            + " SET " + DBConstants.Showtime.COL_AVAILABLE_SEATS
                            + " = " + DBConstants.Showtime.COL_AVAILABLE_SEATS + " - 1"
                            + " WHERE " + DBConstants.Showtime.COL_ID + " = " + showtimeId
                            + " AND " + DBConstants.Showtime.COL_AVAILABLE_SEATS + " > 0"
            );
            Cursor c = db.rawQuery("SELECT changes()", null);
            int changed = 0;
            if (c != null && c.moveToFirst()) {
                changed = c.getInt(0);
                c.close();
            }
            return changed;
        } catch (Exception e) {
            Log.e(TAG, "DecreaseAvailableSeats error: " + e.getMessage());
            return 0;
        }
    }

    // =========================================================
    // HELPER
    // =========================================================
    private List<Showtime> queryWithJoin(String sql, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Showtime> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, args);
            if (cursor != null && cursor.moveToFirst()) {
                do { list.add(fromCursor(cursor)); } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "QueryWithJoin error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}
