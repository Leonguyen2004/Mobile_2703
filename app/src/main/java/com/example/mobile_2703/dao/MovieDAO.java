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

/**
 * MovieDAO - DAO cho bảng movies.
 * Kế thừa BaseDAO, thêm: searchByTitle, getByGenre.
 */
public class MovieDAO extends BaseDAO<Movie> {

    private static final String TAG = "MovieDAO";

    public MovieDAO(Context context) {
        super(context, DBConstants.Table.MOVIE);
    }

    // =========================================================
    // IMPLEMENT ABSTRACT METHODS
    // =========================================================

    @Override
    protected ContentValues toContentValues(Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.Movie.COL_TITLE,        movie.getTitle());
        cv.put(DBConstants.Movie.COL_GENRE,        movie.getGenre());
        cv.put(DBConstants.Movie.COL_DURATION_MIN, movie.getDurationMin());
        cv.put(DBConstants.Movie.COL_DESCRIPTION,  movie.getDescription());
        cv.put(DBConstants.Movie.COL_POSTER_URL,   movie.getPosterUrl());
        cv.put(DBConstants.Movie.COL_RATING,       movie.getRating());
        cv.put(DBConstants.Movie.COL_RELEASE_DATE, movie.getReleaseDate());
        return cv;
    }

    @Override
    protected Movie fromCursor(Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(          getInt(cursor,    DBConstants.Movie.COL_ID));
        movie.setTitle(       getString(cursor, DBConstants.Movie.COL_TITLE));
        movie.setGenre(       getString(cursor, DBConstants.Movie.COL_GENRE));
        movie.setDurationMin( getInt(cursor,    DBConstants.Movie.COL_DURATION_MIN));
        movie.setDescription( getString(cursor, DBConstants.Movie.COL_DESCRIPTION));
        movie.setPosterUrl(   getString(cursor, DBConstants.Movie.COL_POSTER_URL));
        movie.setRating(      getDouble(cursor, DBConstants.Movie.COL_RATING));
        movie.setReleaseDate( getString(cursor, DBConstants.Movie.COL_RELEASE_DATE));
        return movie;
    }

    // =========================================================
    // CUSTOM QUERIES
    // =========================================================

    /**
     * Tìm phim theo từ khóa trong tên phim (LIKE search).
     */
    public List<Movie> searchByTitle(String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Movie> list = new ArrayList<>();
        Cursor cursor = null;
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
            Log.e(TAG, "SearchByTitle error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }

    /**
     * Lấy danh sách phim theo thể loại.
     */
    public List<Movie> getByGenre(String genre) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Movie> list = new ArrayList<>();
        Cursor cursor = null;
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
            Log.e(TAG, "GetByGenre error: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return list;
    }
}
