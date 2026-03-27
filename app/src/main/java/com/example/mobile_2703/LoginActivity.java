// FILE: app/src/main/java/com/example/mobile_2703/LoginActivity.java
package com.example.mobile_2703;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_2703.dao.UserDAO;
import com.example.mobile_2703.model.User;
import com.example.mobile_2703.utils.SessionManager;
import com.example.mobile_2703.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * LoginActivity - Màn hình đăng nhập.
 * Validate input → query UserDAO.login() → lưu session → finish.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvRegisterHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();
        setupListeners();
    }

    private void bindViews() {
        tilUsername   = findViewById(R.id.tilUsername);
        tilPassword   = findViewById(R.id.tilPassword);
        etUsername    = findViewById(R.id.etUsername);
        etPassword    = findViewById(R.id.etPassword);
        btnLogin      = findViewById(R.id.btnLogin);
        tvRegisterHint = findViewById(R.id.tvRegisterHint);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        // Lấy input
        String username = etUsername.getText() != null
                ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null
                ? etPassword.getText().toString().trim() : "";

        // Reset errors
        tilUsername.setError(null);
        tilPassword.setError(null);

        // Validate
        if (!ValidationUtils.isUsernameValid(username)) {
            tilUsername.setError("Tên đăng nhập tối thiểu 3 ký tự");
            etUsername.requestFocus();
            return;
        }
        if (!ValidationUtils.isPasswordValid(password)) {
            tilPassword.setError("Mật khẩu tối thiểu 6 ký tự");
            etPassword.requestFocus();
            return;
        }

        // Query DB
        UserDAO userDAO = new UserDAO(this);
        User user = userDAO.login(username, password);

        if (user != null) {
            // Lưu session
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.saveLogin(user.getId(), user.getUsername());

            // Nếu được gọi bằng startActivityForResult, trả kết quả
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this,
                    "Tên đăng nhập hoặc mật khẩu không đúng",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
