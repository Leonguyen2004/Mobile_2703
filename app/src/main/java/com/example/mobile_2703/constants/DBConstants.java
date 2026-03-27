package com.example.mobile_2703.constants;

/**
 * DBConstants - Hằng số cho toàn bộ database.
 *
 * Tập trung tên bảng và tên cột tại một chỗ giúp:
 *   - Tránh lỗi typo khi dùng nhiều chỗ
 *   - Dễ rename khi cần thay đổi schema
 *   - Team dễ tham khảo cấu trúc database
 *
 * HOW TO ADD NEW TABLE:
 *   1. Thêm tên bảng vào class Table
 *   2. Tạo inner class chứa tên các cột
 *   3. Khai báo CREATE TABLE trong DatabaseHelper
 */
public final class DBConstants {

    private DBConstants() {} // Prevent instantiation

    // =========================================================
    // DATABASE CONFIG
    // =========================================================
    public static final String DATABASE_NAME    = "app_database.db";
    public static final int    DATABASE_VERSION = 1;
    // Tăng DATABASE_VERSION mỗi khi thay đổi schema

    // =========================================================
    // TABLE NAMES
    // =========================================================
    public static final class Table {
        public static final String USER     = "user";
        public static final String CATEGORY = "category";
        public static final String PRODUCT  = "product";
        // TODO: Thêm tên bảng mới ở đây
        // public static final String ORDER = "order_table";
    }

    // =========================================================
    // COLUMN NAMES - mỗi bảng một inner class
    // =========================================================

    /** Cột bảng user */
    public static final class User {
        public static final String COL_ID         = "id";
        public static final String COL_USERNAME   = "username";
        public static final String COL_PASSWORD   = "password";
        public static final String COL_FULL_NAME  = "full_name";
        public static final String COL_EMAIL      = "email";
        public static final String COL_ROLE       = "role";
        public static final String COL_CREATED_AT = "created_at";
    }

    /** Cột bảng category */
    public static final class Category {
        public static final String COL_ID   = "id";
        public static final String COL_NAME = "name";
        public static final String COL_DESC = "description";
    }

    /** Cột bảng product */
    public static final class Product {
        public static final String COL_ID          = "id";
        public static final String COL_NAME        = "name";
        public static final String COL_PRICE       = "price";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL   = "image_url";
        public static final String COL_STOCK       = "stock";
        public static final String COL_CATEGORY_ID = "category_id";
        public static final String COL_CREATED_AT  = "created_at";
    }

    // TODO: Thêm inner class cột cho bảng mới
    // public static final class Order { ... }

    // =========================================================
    // COMMON VALUES
    // =========================================================
    public static final class Role {
        public static final String ADMIN = "admin";
        public static final String USER  = "user";
    }
}
