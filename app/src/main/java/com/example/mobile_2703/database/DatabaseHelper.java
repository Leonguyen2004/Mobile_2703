package com.example.mobile_2703.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mobile_2703.constants.DBConstants;

/**
 * DatabaseHelper - Quản lý việc tạo và nâng cấp database SQLite.
 *
 * HOW TO USE:
 *   - Thêm bảng mới: khai báo CREATE_TABLE_XXX trong class này và gọi trong onCreate()
 *   - Khi thay đổi schema: tăng DATABASE_VERSION lên, xử lý migration trong onUpgrade()
 *
 * SINGLETON PATTERN: Chỉ dùng DatabaseHelper.getInstance(context) để lấy instance.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper instance;

    // =========================================================
    // 1. TẠO BẢNG MỚI: Thêm câu SQL CREATE TABLE vào đây
    // =========================================================

    /** Bảng User */
    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + DBConstants.Table.USER + " ("
                    + DBConstants.User.COL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.User.COL_USERNAME     + " TEXT NOT NULL UNIQUE, "
                    + DBConstants.User.COL_PASSWORD     + " TEXT NOT NULL, "
                    + DBConstants.User.COL_FULL_NAME    + " TEXT, "
                    + DBConstants.User.COL_EMAIL        + " TEXT, "
                    + DBConstants.User.COL_ROLE         + " TEXT DEFAULT 'user', "
                    + DBConstants.User.COL_CREATED_AT   + " TEXT DEFAULT (datetime('now','localtime'))"
                    + ");";

    /** Bảng Category */
    private static final String CREATE_TABLE_CATEGORY =
            "CREATE TABLE " + DBConstants.Table.CATEGORY + " ("
                    + DBConstants.Category.COL_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.Category.COL_NAME     + " TEXT NOT NULL, "
                    + DBConstants.Category.COL_DESC     + " TEXT"
                    + ");";

    /** Bảng Product */
    private static final String CREATE_TABLE_PRODUCT =
            "CREATE TABLE " + DBConstants.Table.PRODUCT + " ("
                    + DBConstants.Product.COL_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DBConstants.Product.COL_NAME        + " TEXT NOT NULL, "
                    + DBConstants.Product.COL_PRICE       + " REAL NOT NULL DEFAULT 0, "
                    + DBConstants.Product.COL_DESCRIPTION + " TEXT, "
                    + DBConstants.Product.COL_IMAGE_URL   + " TEXT, "
                    + DBConstants.Product.COL_STOCK       + " INTEGER DEFAULT 0, "
                    + DBConstants.Product.COL_CATEGORY_ID + " INTEGER, "
                    + DBConstants.Product.COL_CREATED_AT  + " TEXT DEFAULT (datetime('now','localtime')), "
                    + "FOREIGN KEY (" + DBConstants.Product.COL_CATEGORY_ID + ") "
                    + "REFERENCES " + DBConstants.Table.CATEGORY + "(" + DBConstants.Category.COL_ID + ")"
                    + ");";

    // TODO: Thêm CREATE TABLE mới ở đây khi có đề bài
    // private static final String CREATE_TABLE_ORDER = ...

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

        // Bật hỗ trợ FOREIGN KEY
        db.execSQL("PRAGMA foreign_keys = ON;");

        // 2. TẠO BẢNG MỚI: Gọi execSQL cho từng bảng ở đây
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_PRODUCT);
        // db.execSQL(CREATE_TABLE_ORDER); // Thêm bảng mới vào đây

        // Seed dữ liệu mẫu
        seedData(db);

        Log.d(TAG, "Database created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from v" + oldVersion + " to v" + newVersion);

        // Khi thêm bảng mới hoặc thay đổi schema:
        // - Tăng DATABASE_VERSION trong DBConstants
        // - Xử lý migration tại đây thay vì drop toàn bộ
        //
        // Ví dụ migration:
        // if (oldVersion < 2) {
        //     db.execSQL("ALTER TABLE user ADD COLUMN phone TEXT;");
        // }
        // if (oldVersion < 3) {
        //     db.execSQL(CREATE_TABLE_ORDER);
        // }

        // Development only - xóa và tạo lại (XÓA khi release)
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.Table.USER);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // Bật FOREIGN KEY mỗi lần mở (SQLite yêu cầu bật lại sau mỗi connection)
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    // =========================================================
    // SEED DATA - Dữ liệu mẫu ban đầu
    // =========================================================
    private void seedData(SQLiteDatabase db) {
        Log.d(TAG, "Seeding initial data...");

        // Seed admin user (password: admin123 - thực tế nên hash)
        db.execSQL("INSERT INTO " + DBConstants.Table.USER
                + " (username, password, full_name, email, role) VALUES "
                + "('admin', 'admin123', 'Administrator', 'admin@example.com', 'admin');");

        // Seed categories mẫu
        db.execSQL("INSERT INTO " + DBConstants.Table.CATEGORY
                + " (name, description) VALUES "
                + "('Electronics', 'Electronic devices and accessories');");
        db.execSQL("INSERT INTO " + DBConstants.Table.CATEGORY
                + " (name, description) VALUES "
                + "('Clothing', 'Fashion and apparel');");
        db.execSQL("INSERT INTO " + DBConstants.Table.CATEGORY
                + " (name, description) VALUES "
                + "('Books', 'Books and stationery');");

        // Seed products mẫu
        db.execSQL("INSERT INTO " + DBConstants.Table.PRODUCT
                + " (name, price, description, stock, category_id) VALUES "
                + "('Smartphone XYZ', 299.99, 'Latest smartphone model', 50, 1);");
        db.execSQL("INSERT INTO " + DBConstants.Table.PRODUCT
                + " (name, price, description, stock, category_id) VALUES "
                + "('Laptop Pro', 999.99, 'High performance laptop', 20, 1);");
        db.execSQL("INSERT INTO " + DBConstants.Table.PRODUCT
                + " (name, price, description, stock, category_id) VALUES "
                + "('T-Shirt Basic', 19.99, 'Comfortable cotton t-shirt', 100, 2);");

        Log.d(TAG, "Seed data inserted successfully");
    }
}
