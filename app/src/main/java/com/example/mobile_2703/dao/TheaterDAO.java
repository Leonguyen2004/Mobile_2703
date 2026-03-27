package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Theater;

/**
 * TheaterDAO - DAO cho bảng theaters.
 * Kế thừa toàn bộ CRUD từ BaseDAO.
 */
public class TheaterDAO extends BaseDAO<Theater> {

    public TheaterDAO(Context context) {
        super(context, DBConstants.Table.THEATER);
    }

    // =========================================================
    // IMPLEMENT ABSTRACT METHODS
    // =========================================================

    @Override
    protected ContentValues toContentValues(Theater theater) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Theater.COL_NAME,        theater.getName());
        cv.put(DBConstants.Theater.COL_LOCATION,    theater.getLocation());
        cv.put(DBConstants.Theater.COL_TOTAL_SEATS, theater.getTotalSeats());
        return cv;
    }

    @Override
    protected Theater fromCursor(Cursor cursor) {
        Theater theater = new Theater();
        theater.setId(         getInt(cursor,    DBConstants.Theater.COL_ID));
        theater.setName(       getString(cursor, DBConstants.Theater.COL_NAME));
        theater.setLocation(   getString(cursor, DBConstants.Theater.COL_LOCATION));
        theater.setTotalSeats( getInt(cursor,    DBConstants.Theater.COL_TOTAL_SEATS));
        return theater;
    }
}
