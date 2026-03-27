package com.example.mobile_2703;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity - Điểm khởi đầu của ứng dụng.
 * Chuyển hướng ngay đến SplashActivity để bắt đầu luồng chính.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
