package com.example.mobile_2703.constants;

/**
 * AppConstants - Hằng số chung của ứng dụng Cinema.
 *
 * Dùng cho Intent extras, SharedPreferences keys, request codes.
 */
public final class AppConstants {

    private AppConstants() {}

    // =========================================================
    // INTENT EXTRAS
    // =========================================================
    public static final String EXTRA_USER_ID      = "extra_user_id";
    public static final String EXTRA_MOVIE_ID     = "extra_movie_id";
    public static final String EXTRA_THEATER_ID   = "extra_theater_id";
    public static final String EXTRA_SHOWTIME_ID  = "extra_showtime_id";
    public static final String EXTRA_SEAT_NUMBER  = "extra_seat_number";
    public static final String EXTRA_TOTAL_PRICE  = "extra_total_price";

    // =========================================================
    // SHARED PREFERENCES
    // =========================================================
    public static final String PREF_NAME         = "app_prefs";
    public static final String PREF_USER_ID      = "pref_user_id";
    public static final String PREF_USERNAME     = "pref_username";
    public static final String PREF_IS_LOGGED_IN = "pref_is_logged_in";

    // =========================================================
    // REQUEST CODES
    // =========================================================
    public static final int REQUEST_LOGIN = 200;
}
