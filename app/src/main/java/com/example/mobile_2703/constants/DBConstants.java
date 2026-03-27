package com.example.mobile_2703.constants;

/**
 * DBConstants - Hằng số cho toàn bộ database.
 *
 * Tập trung tên bảng và tên cột tại một chỗ giúp:
 *   - Tránh lỗi typo khi dùng nhiều chỗ
 *   - Dễ rename khi cần thay đổi schema
 *   - Team dễ tham khảo cấu trúc database
 */
public final class DBConstants {

    private DBConstants() {} // Prevent instantiation

    // =========================================================
    // DATABASE CONFIG
    // =========================================================
    public static final String DATABASE_NAME    = "cinema_database.db";
    public static final int    DATABASE_VERSION = 1;

    // =========================================================
    // TABLE NAMES
    // =========================================================
    public static final class Table {
        public static final String USER      = "users";
        public static final String MOVIE     = "movies";
        public static final String THEATER   = "theaters";
        public static final String SHOWTIME  = "showtimes";
        public static final String TICKET    = "tickets";
    }

    // =========================================================
    // COLUMN NAMES - mỗi bảng một inner class
    // =========================================================

    /** Cột bảng users */
    public static final class User {
        public static final String COL_ID         = "id";
        public static final String COL_USERNAME   = "username";
        public static final String COL_PASSWORD   = "password";
        public static final String COL_FULL_NAME  = "full_name";
        public static final String COL_EMAIL      = "email";
        public static final String COL_PHONE      = "phone";
        public static final String COL_ROLE       = "role";
        public static final String COL_CREATED_AT = "created_at";
    }

    /** Cột bảng movies */
    public static final class Movie {
        public static final String COL_ID           = "id";
        public static final String COL_TITLE        = "title";
        public static final String COL_GENRE        = "genre";
        public static final String COL_DURATION_MIN = "duration_min";
        public static final String COL_DESCRIPTION  = "description";
        public static final String COL_POSTER_URL   = "poster_url";
        public static final String COL_RATING       = "rating";
        public static final String COL_RELEASE_DATE = "release_date";
    }

    /** Cột bảng theaters */
    public static final class Theater {
        public static final String COL_ID          = "id";
        public static final String COL_NAME        = "name";
        public static final String COL_LOCATION    = "location";
        public static final String COL_TOTAL_SEATS = "total_seats";
    }

    /** Cột bảng showtimes */
    public static final class Showtime {
        public static final String COL_ID              = "id";
        public static final String COL_MOVIE_ID        = "movie_id";
        public static final String COL_THEATER_ID      = "theater_id";
        public static final String COL_SHOW_DATE       = "show_date";
        public static final String COL_SHOW_TIME       = "show_time";
        public static final String COL_PRICE           = "price";
        public static final String COL_AVAILABLE_SEATS = "available_seats";
    }

    /** Cột bảng tickets */
    public static final class Ticket {
        public static final String COL_ID           = "id";
        public static final String COL_USER_ID      = "user_id";
        public static final String COL_SHOWTIME_ID  = "showtime_id";
        public static final String COL_SEAT_NUMBER  = "seat_number";
        public static final String COL_TOTAL_PRICE  = "total_price";
        public static final String COL_BOOKING_TIME = "booking_time";
        public static final String COL_STATUS       = "status";
    }

    // =========================================================
    // COMMON VALUES
    // =========================================================
    public static final class Role {
        public static final String ADMIN = "admin";
        public static final String USER  = "user";
    }

    public static final class TicketStatus {
        public static final String CONFIRMED = "confirmed";
        public static final String CANCELLED = "cancelled";
    }
}
