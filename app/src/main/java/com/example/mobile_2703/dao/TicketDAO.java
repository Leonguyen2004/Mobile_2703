package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * TicketDAO - DAO cho bảng tickets.
 * JOIN showtimes + movies + theaters để lấy đầy đủ thông tin vé.
 */
public class TicketDAO extends BaseDAO<Ticket> {

    private static final String TAG = "TicketDAO";

    public TicketDAO(Context context) {
        super(context, DBConstants.Table.TICKET);
    }

    // =========================================================
    // IMPLEMENT ABSTRACT METHODS
    // =========================================================

    @Override
    protected ContentValues toContentValues(Ticket ticket) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Ticket.COL_USER_ID,     ticket.getUserId());
        cv.put(DBConstants.Ticket.COL_SHOWTIME_ID, ticket.getShowtimeId());
        cv.put(DBConstants.Ticket.COL_SEAT_NUMBER, ticket.getSeatNumber());
        cv.put(DBConstants.Ticket.COL_TOTAL_PRICE, ticket.getTotalPrice());
        cv.put(DBConstants.Ticket.COL_STATUS,      ticket.getStatus());
        return cv;
    }

    @Override
    protected Ticket fromCursor(Cursor cursor) {
        Ticket ticket = new Ticket();
        ticket.setId(           getInt(cursor,    DBConstants.Ticket.COL_ID));
        ticket.setUserId(       getInt(cursor,    DBConstants.Ticket.COL_USER_ID));
        ticket.setShowtimeId(   getInt(cursor,    DBConstants.Ticket.COL_SHOWTIME_ID));
        ticket.setSeatNumber(   getString(cursor, DBConstants.Ticket.COL_SEAT_NUMBER));
        ticket.setTotalPrice(   getDouble(cursor, DBConstants.Ticket.COL_TOTAL_PRICE));
        ticket.setBookingTime(  getString(cursor, DBConstants.Ticket.COL_BOOKING_TIME));
        ticket.setStatus(       getString(cursor, DBConstants.Ticket.COL_STATUS));
        // Transient JOIN fields
        ticket.setMovieTitle(  getString(cursor, "movie_title"));
        ticket.setShowDate(    getString(cursor, "show_date_val"));
        ticket.setShowTime(    getString(cursor, "show_time_val"));
        ticket.setTheaterName( getString(cursor, "theater_name"));
        return ticket;
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /**
     * Lấy tất cả vé của một user, kèm thông tin phim + rạp + suất chiếu.
     */
    public List<Ticket> getByUserId(int userId) {
        String sql = "SELECT tk.*, "
                + "m."  + DBConstants.Movie.COL_TITLE       + " AS movie_title, "
                + "s."  + DBConstants.Showtime.COL_SHOW_DATE + " AS show_date_val, "
                + "s."  + DBConstants.Showtime.COL_SHOW_TIME + " AS show_time_val, "
                + "t."  + DBConstants.Theater.COL_NAME       + " AS theater_name "
                + "FROM " + DBConstants.Table.TICKET   + " tk "
                + "JOIN "  + DBConstants.Table.SHOWTIME + " s  ON tk." + DBConstants.Ticket.COL_SHOWTIME_ID + " = s."  + DBConstants.Showtime.COL_ID  + " "
                + "JOIN "  + DBConstants.Table.MOVIE    + " m  ON s."  + DBConstants.Showtime.COL_MOVIE_ID  + " = m."  + DBConstants.Movie.COL_ID     + " "
                + "JOIN "  + DBConstants.Table.THEATER  + " t  ON s."  + DBConstants.Showtime.COL_THEATER_ID + " = t." + DBConstants.Theater.COL_ID   + " "
                + "WHERE tk." + DBConstants.Ticket.COL_USER_ID + " = ? "
                + "ORDER BY tk." + DBConstants.Ticket.COL_BOOKING_TIME + " DESC";
        return queryWithJoin(sql, new String[]{String.valueOf(userId)});
    }

    /**
     * Lấy tất cả vé của một suất chiếu.
     */
    public List<Ticket> getByShowtimeId(int showtimeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Ticket> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName, null,
                    DBConstants.Ticket.COL_SHOWTIME_ID + " = ?",
                    new String[]{String.valueOf(showtimeId)},
                    null, null, DBConstants.Ticket.COL_SEAT_NUMBER + " ASC"
            );
            if (cursor != null && cursor.moveToFirst()) {
                do { list.add(fromCursor(cursor)); } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "GetByShowtimeId error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Kiểm tra ghế đã được đặt chưa trong một suất chiếu.
     * @return true nếu ghế đã bị đặt.
     */
    public boolean isSeatTaken(int showtimeId, String seatNumber) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    tableName,
                    new String[]{DBConstants.Ticket.COL_ID},
                    DBConstants.Ticket.COL_SHOWTIME_ID + " = ? AND "
                            + DBConstants.Ticket.COL_SEAT_NUMBER + " = ? AND "
                            + DBConstants.Ticket.COL_STATUS + " != 'cancelled'",
                    new String[]{String.valueOf(showtimeId), seatNumber},
                    null, null, null
            );
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "IsSeatTaken error: " + e.getMessage());
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    // =========================================================
    // HELPER
    // =========================================================
    private List<Ticket> queryWithJoin(String sql, String[] args) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Ticket> list = new ArrayList<>();
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
