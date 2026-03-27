package com.example.mobile_2703.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;

/**
 * DatabaseHelper - Quản lý việc tạo và nâng cấp database SQLite.
 *
 * SINGLETON PATTERN: Chỉ dùng DatabaseHelper.getInstance(context) để lấy instance.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper instance;

    // =========================================================
    // CREATE TABLE STATEMENTS
    // =========================================================

    /** Bảng users */
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + DBConstants.Table.USER + " ("
                    + DBConstants.User.COL_ID         + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.User.COL_USERNAME   + " TEXT NOT NULL UNIQUE, "
                    + DBConstants.User.COL_PASSWORD   + " TEXT NOT NULL, "
                    + DBConstants.User.COL_FULL_NAME  + " TEXT, "
                    + DBConstants.User.COL_EMAIL      + " TEXT, "
                    + DBConstants.User.COL_PHONE      + " TEXT, "
                    + DBConstants.User.COL_ROLE       + " TEXT DEFAULT 'user', "
                    + DBConstants.User.COL_CREATED_AT + " TEXT DEFAULT (datetime('now','localtime'))"
                    + ");";

    /** Bảng movies */
    private static final String CREATE_TABLE_MOVIE =
            "CREATE TABLE " + DBConstants.Table.MOVIE + " ("
                    + DBConstants.Movie.COL_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.Movie.COL_TITLE        + " TEXT NOT NULL, "
                    + DBConstants.Movie.COL_GENRE        + " TEXT, "
                    + DBConstants.Movie.COL_DURATION_MIN + " INTEGER DEFAULT 0, "
                    + DBConstants.Movie.COL_DESCRIPTION  + " TEXT, "
                    + DBConstants.Movie.COL_POSTER_URL   + " TEXT, "
                    + DBConstants.Movie.COL_RATING       + " REAL DEFAULT 0.0, "
                    + DBConstants.Movie.COL_RELEASE_DATE + " TEXT"
                    + ");";

    /** Bảng theaters */
    private static final String CREATE_TABLE_THEATER =
            "CREATE TABLE " + DBConstants.Table.THEATER + " ("
                    + DBConstants.Theater.COL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.Theater.COL_NAME        + " TEXT NOT NULL, "
                    + DBConstants.Theater.COL_LOCATION    + " TEXT, "
                    + DBConstants.Theater.COL_TOTAL_SEATS + " INTEGER DEFAULT 100"
                    + ");";

    /** Bảng showtimes (FK: movie_id -> movies, theater_id -> theaters) */
    private static final String CREATE_TABLE_SHOWTIME =
            "CREATE TABLE " + DBConstants.Table.SHOWTIME + " ("
                    + DBConstants.Showtime.COL_ID              + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.Showtime.COL_MOVIE_ID        + " INTEGER NOT NULL, "
                    + DBConstants.Showtime.COL_THEATER_ID      + " INTEGER NOT NULL, "
                    + DBConstants.Showtime.COL_SHOW_DATE       + " TEXT NOT NULL, "
                    + DBConstants.Showtime.COL_SHOW_TIME       + " TEXT NOT NULL, "
                    + DBConstants.Showtime.COL_PRICE           + " REAL NOT NULL DEFAULT 0, "
                    + DBConstants.Showtime.COL_AVAILABLE_SEATS + " INTEGER DEFAULT 0, "
                    + "FOREIGN KEY (" + DBConstants.Showtime.COL_MOVIE_ID   + ") "
                    + "REFERENCES " + DBConstants.Table.MOVIE   + "(" + DBConstants.Movie.COL_ID   + "), "
                    + "FOREIGN KEY (" + DBConstants.Showtime.COL_THEATER_ID + ") "
                    + "REFERENCES " + DBConstants.Table.THEATER + "(" + DBConstants.Theater.COL_ID + ")"
                    + ");";

    /** Bảng tickets (FK: user_id -> users, showtime_id -> showtimes) */
    private static final String CREATE_TABLE_TICKET =
            "CREATE TABLE " + DBConstants.Table.TICKET + " ("
                    + DBConstants.Ticket.COL_ID           + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.Ticket.COL_USER_ID      + " INTEGER NOT NULL, "
                    + DBConstants.Ticket.COL_SHOWTIME_ID  + " INTEGER NOT NULL, "
                    + DBConstants.Ticket.COL_SEAT_NUMBER  + " TEXT NOT NULL, "
                    + DBConstants.Ticket.COL_TOTAL_PRICE  + " REAL NOT NULL DEFAULT 0, "
                    + DBConstants.Ticket.COL_BOOKING_TIME + " TEXT DEFAULT (datetime('now','localtime')), "
                    + DBConstants.Ticket.COL_STATUS       + " TEXT DEFAULT 'confirmed', "
                    + "FOREIGN KEY (" + DBConstants.Ticket.COL_USER_ID     + ") "
                    + "REFERENCES " + DBConstants.Table.USER     + "(" + DBConstants.User.COL_ID       + "), "
                    + "FOREIGN KEY (" + DBConstants.Ticket.COL_SHOWTIME_ID + ") "
                    + "REFERENCES " + DBConstants.Table.SHOWTIME + "(" + DBConstants.Showtime.COL_ID   + ")"
                    + ");";

    // =========================================================
    // SINGLETON
    // =========================================================
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    // =========================================================
    // LIFECYCLE
    // =========================================================
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database v" + DBConstants.DATABASE_VERSION);

        db.execSQL("PRAGMA foreign_keys = ON;");

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MOVIE);
        db.execSQL(CREATE_TABLE_THEATER);
        db.execSQL(CREATE_TABLE_SHOWTIME);
        db.execSQL(CREATE_TABLE_TICKET);

        seedData(db);

        Log.d(TAG, "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from v" + oldVersion + " to v" + newVersion);

        // Drop tables in reverse FK order
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.TICKET);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.SHOWTIME);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.THEATER);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.USER);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    // =========================================================
    // SEED DATA
    // =========================================================
    private void seedData(SQLiteDatabase db) {
        Log.d(TAG, "Seeding initial data...");

        // --- Users ---
        db.execSQL("INSERT INTO " + DBConstants.Table.USER
                + " (username, password, full_name, email, phone, role) VALUES "
                + "('admin', 'admin', 'Administrator', 'admin@cinema.com', '0900000000', 'admin');");
        db.execSQL("INSERT INTO " + DBConstants.Table.USER
                + " (username, password, full_name, email, phone, role) VALUES "
                + "('user1', '123456', 'Nguyen Van An', 'an@gmail.com', '0911111111', 'user');");
        db.execSQL("INSERT INTO " + DBConstants.Table.USER
                + " (username, password, full_name, email, phone, role) VALUES "
                + "('user2', '123456', 'Tran Thi Bich', 'bich@gmail.com', '0922222222', 'user');");

        // --- Movies ---
        db.execSQL("INSERT INTO " + DBConstants.Table.MOVIE
                + " (title, genre, duration_min, description, rating, release_date) VALUES "
                + "('Avengers: Endgame', 'Action/Sci-Fi', 181, "
                + "'The Avengers assemble once more in order to reverse Thanos'' actions.', "
                + "8.4, '2019-04-26');");
        db.execSQL("INSERT INTO " + DBConstants.Table.MOVIE
                + " (title, genre, duration_min, description, rating, release_date) VALUES "
                + "('Inception', 'Sci-Fi/Thriller', 148, "
                + "'A thief who steals corporate secrets through dream-sharing technology.', "
                + "8.8, '2010-07-16');");
        db.execSQL("INSERT INTO " + DBConstants.Table.MOVIE
                + " (title, genre, duration_min, description, rating, release_date) VALUES "
                + "('The Dark Knight', 'Action/Crime', 152, "
                + "'Batman faces the Joker, a criminal mastermind who wants to plunge Gotham into anarchy.', "
                + "9.0, '2008-07-18');");
        db.execSQL("INSERT INTO " + DBConstants.Table.MOVIE
                + " (title, genre, duration_min, description, rating, release_date) VALUES "
                + "('Interstellar', 'Sci-Fi/Drama', 169, "
                + "'A team of explorers travel through a wormhole in space.', "
                + "8.6, '2014-11-07');");

        // --- Theaters ---
        db.execSQL("INSERT INTO " + DBConstants.Table.THEATER
                + " (name, location, total_seats) VALUES "
                + "('CGV Vincom Center', '72 Le Thanh Ton, Q.1, TP.HCM', 120);");
        db.execSQL("INSERT INTO " + DBConstants.Table.THEATER
                + " (name, location, total_seats) VALUES "
                + "('Lotte Cinema Tay Ho', 'Tay Ho, Ha Noi', 100);");
        db.execSQL("INSERT INTO " + DBConstants.Table.THEATER
                + " (name, location, total_seats) VALUES "
                + "('BHD Star Bitexco', '2 Hai Trieu, Q.1, TP.HCM', 80);");

        // --- Showtimes ---
        // Movie 1 (Avengers) - Theater 1 & 2
        db.execSQL("INSERT INTO " + DBConstants.Table.SHOWTIME
                + " (movie_id, theater_id, show_date, show_time, price, available_seats) VALUES "
                + "(1, 1, '2026-03-28', '10:00', 90000, 120);");
        db.execSQL("INSERT INTO " + DBConstants.Table.SHOWTIME
                + " (movie_id, theater_id, show_date, show_time, price, available_seats) VALUES "
                + "(1, 2, '2026-03-28', '13:30', 85000, 100);");

        // Movie 2 (Inception) - Theater 1 & 3
        db.execSQL("INSERT INTO " + DBConstants.Table.SHOWTIME
                + " (movie_id, theater_id, show_date, show_time, price, available_seats) VALUES "
                + "(2, 1, '2026-03-29', '15:00', 90000, 118);");
        db.execSQL("INSERT INTO " + DBConstants.Table.SHOWTIME
                + " (movie_id, theater_id, show_date, show_time, price, available_seats) VALUES "
                + "(2, 3, '2026-03-29', '18:30', 80000, 80);");

        // Movie 3 (The Dark Knight) - Theater 2
        db.execSQL("INSERT INTO " + DBConstants.Table.SHOWTIME
                + " (movie_id, theater_id, show_date, show_time, price, available_seats) VALUES "
                + "(3, 2, '2026-03-30', '20:00', 85000, 95);");

        // Movie 4 (Interstellar) - Theater 3
        db.execSQL("INSERT INTO " + DBConstants.Table.SHOWTIME
                + " (movie_id, theater_id, show_date, show_time, price, available_seats) VALUES "
                + "(4, 3, '2026-03-30', '09:00', 80000, 78);");

        // --- Tickets (sample bookings by user1) ---
        db.execSQL("INSERT INTO " + DBConstants.Table.TICKET
                + " (user_id, showtime_id, seat_number, total_price, status) VALUES "
                + "(2, 1, 'A01', 90000, 'confirmed');");
        db.execSQL("INSERT INTO " + DBConstants.Table.TICKET
                + " (user_id, showtime_id, seat_number, total_price, status) VALUES "
                + "(2, 3, 'B05', 90000, 'confirmed');");

        Log.d(TAG, "Seed data inserted successfully");
    }
}
