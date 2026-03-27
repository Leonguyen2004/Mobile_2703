// FILE: app/src/main/java/com/example/mobile_2703/SplashActivity.java
package com.example.mobile_2703;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_2703.utils.SessionManager;

/**
 * SplashActivity - Màn hình khởi động.
 * Hiển thị logo/tên app trong 1.5 giây rồi chuyển sang HomeActivity.
 */
public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SessionManager sessionManager = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Home không yêu cầu login; login chỉ cần khi đặt vé.
            // Dù logged in hay không, đều vào HomeActivity.
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY_MS);
    }
}
