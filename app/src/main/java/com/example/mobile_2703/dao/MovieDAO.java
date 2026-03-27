// FILE: app/src/main/java/com/example/mobile_2703/dao/MovieDAO.java
package com.example.mobile_2703.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDAO extends BaseDAO<Movie> {

    private static final String TAG = "MovieDAO";

    public MovieDAO(Context context) {
        super(context, DBConstants.Table.MOVIE);
    }

    // =========================================================
    // ABSTRACT METHOD IMPLEMENTATIONS
    // =========================================================

    @Override
    protected ContentValues toContentValues(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Movie.COL_TITLE,        movie.getTitle());
        cv.put(DBConstants.Movie.COL_GENRE,        movie.getGenre());
        cv.put(DBConstants.Movie.COL_DURATION_MIN, movie.getDuration());
        cv.put(DBConstants.Movie.COL_DESCRIPTION,  movie.getDescription());
        cv.put(DBConstants.Movie.COL_POSTER_URL,   movie.getPosterUrl());
        cv.put(DBConstants.Movie.COL_RATING,       movie.getRating());
        return cv;
    }

    @Override
    protected Movie fromCursor(Cursor cursor) {
        long   id          = getLong(cursor,   DBConstants.Movie.COL_ID);
        String title       = getString(cursor, DBConstants.Movie.COL_TITLE);
        String genre       = getString(cursor, DBConstants.Movie.COL_GENRE);
        int    duration    = getInt(cursor,    DBConstants.Movie.COL_DURATION_MIN);
        String description = getString(cursor, DBConstants.Movie.COL_DESCRIPTION);
        String posterUrl   = getString(cursor, DBConstants.Movie.COL_POSTER_URL);
        double rating      = getDouble(cursor, DBConstants.Movie.COL_RATING);
        return new Movie(id, title, genre, duration, description, posterUrl, rating);
    }

    // =========================================================
    // OVERRIDE getAll — sắp xếp theo tên
    // =========================================================

    @Override
    public List<Movie> getAll() {
        return getAll(DBConstants.Movie.COL_TITLE, true);
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /** Tìm phim theo id. Trả về null nếu không tìm thấy. */
    public Movie findById(long id) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        Cursor         cursor = null;
        try {
            cursor = db.query(
                    tableName,
                    null,
                    DBConstants.Movie.COL_ID + " = ?",
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

    /** Tìm kiếm phim theo từ khoá trong tên phim (LIKE %keyword%). */
    public List<Movie> searchByTitle(String keyword) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        List<Movie>    list   = new ArrayList<>();
        Cursor         cursor = null;
        try {
            cursor = db.query(
                    tableName,
                    null,
                    DBConstants.Movie.COL_TITLE + " LIKE ?",
                    new String[]{"%" + keyword + "%"},
                    null, null,
                    DBConstants.Movie.COL_TITLE + " ASC"
            );
            if (cursor != null && cursor.moveToFirst()) {
                do { list.add(fromCursor(cursor)); } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "searchByTitle error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Lấy danh sách phim đang chiếu tại một rạp cụ thể (qua bảng showtimes).
     * SELECT DISTINCT m.* FROM movies m
     * INNER JOIN showtimes s ON s.movie_id = m.id
     * WHERE s.theater_id = ?
     */
    public List<Movie> getByTheaterId(int theaterId) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        List<Movie>    list   = new ArrayList<>();
        Cursor         cursor = null;
        String query =
                "SELECT DISTINCT m.* FROM " + DBConstants.Table.MOVIE + " m"
                + " INNER JOIN " + DBConstants.Table.SHOWTIME + " s"
                + " ON s." + DBConstants.Showtime.COL_MOVIE_ID + " = m." + DBConstants.Movie.COL_ID
                + " WHERE s." + DBConstants.Showtime.COL_THEATER_ID + " = ?"
                + " ORDER BY m." + DBConstants.Movie.COL_TITLE + " ASC";
        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(theaterId)});
            if (cursor != null && cursor.moveToFirst()) {
                do { list.add(fromCursor(cursor)); } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getByTheaterId error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /** Lấy danh sách phim theo thể loại. */
    public List<Movie> getByGenre(String genre) {
        SQLiteDatabase db     = dbHelper.getReadableDatabase();
        List<Movie>    list   = new ArrayList<>();
        Cursor         cursor = null;
        try {
            cursor = db.query(
                    tableName,
                    null,
                    DBConstants.Movie.COL_GENRE + " LIKE ?",
                    new String[]{"%" + genre + "%"},
                    null, null,
                    DBConstants.Movie.COL_TITLE + " ASC"
            );
            if (cursor != null && cursor.moveToFirst()) {
                do { list.add(fromCursor(cursor)); } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getByGenre error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}
