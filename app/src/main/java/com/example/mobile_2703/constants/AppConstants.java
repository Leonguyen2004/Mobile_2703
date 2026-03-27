package com.example.mobile_2703.constants;

/**
 * AppConstants - Hằng số chung của ứng dụng.
 *
 * Dùng cho Intent extras, SharedPreferences keys, request codes, v.v.
 */
public final class AppConstants {

    private AppConstants() {}

    // =========================================================
    // INTENT EXTRAS - Keys dùng khi truyền dữ liệu qua Intent
    // =========================================================
    public static final String EXTRA_USER_ID     = "extra_user_id";
    public static final String EXTRA_PRODUCT_ID  = "extra_product_id";
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_MODE        = "extra_mode";

    // =========================================================
    // ACTIVITY MODE - Phân biệt CREATE vs EDIT
    // =========================================================
    public static final String MODE_CREATE = "create";
    public static final String MODE_EDIT   = "edit";

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
    public static final int REQUEST_ADD_ITEM    = 100;
    public static final int REQUEST_EDIT_ITEM   = 101;
    public static final int REQUEST_PICK_IMAGE  = 102;
}
