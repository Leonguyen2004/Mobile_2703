package com.example.mobile_2703;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_2703.R;
import com.example.mobile_2703.dao.CategoryDAO;
import com.example.mobile_2703.dao.ProductDAO;
import com.example.mobile_2703.dao.UserDAO;
import com.example.mobile_2703.database.DatabaseHelper;
import com.example.mobile_2703.model.Category;
import com.example.mobile_2703.model.Product;
import com.example.mobile_2703.model.User;
import com.example.mobile_2703.utils.SessionManager;

import java.util.List;

/**
 * MainActivity - Màn hình chính để test database.
 *
 * Đây chỉ là activity mẫu để kiểm tra database hoạt động đúng.
 * Team có thể xóa / thay thế bằng activity thực tế khi có đề bài.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private UserDAO     userDAO;
    private CategoryDAO categoryDAO;
    private ProductDAO  productDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo DAOs
        userDAO      = new UserDAO(this);
        categoryDAO  = new CategoryDAO(this);
        productDAO   = new ProductDAO(this);
        sessionManager = new SessionManager(this);

        // Khởi tạo database (tạo bảng + seed data nếu chưa có)
        DatabaseHelper.getInstance(this);

        setupButtons();
        testDatabase(); // Chạy test khi mở app để verify database
    }

    private void setupButtons() {
        // TODO: Thêm button và navigation đến các màn hình của team
        Button btnTest = findViewById(R.id.btn_test_db);
        if (btnTest != null) {
            btnTest.setOnClickListener(v -> testDatabase());
        }
    }

    /**
     * Hàm test database - kiểm tra các thao tác CRUD cơ bản.
     * Xem Log để verify kết quả.
     */
    private void testDatabase() {
        Log.d(TAG, "=== DATABASE TEST START ===");

        // Test Users
        List<User> users = userDAO.getAll();
        Log.d(TAG, "Total users: " + users.size());
        for (User u : users) Log.d(TAG, "  " + u);

        // Test login
        User admin = userDAO.login("admin", "admin123");
        Log.d(TAG, "Login test - admin: " + (admin != null ? "SUCCESS" : "FAIL"));

        // Test Categories
        List<Category> categories = categoryDAO.getAll();
        Log.d(TAG, "Total categories: " + categories.size());
        for (Category c : categories) Log.d(TAG, "  " + c);

        // Test Products với JOIN
        List<Product> products = productDAO.getAllWithCategory();
        Log.d(TAG, "Total products: " + products.size());
        for (Product p : products) {
            Log.d(TAG, "  " + p + " | Category: " + p.getCategoryName());
        }

        Log.d(TAG, "=== DATABASE TEST END ===");
        Toast.makeText(this, "DB OK - " + users.size() + " users, "
                + categories.size() + " categories, "
                + products.size() + " products", Toast.LENGTH_SHORT).show();
    }
}