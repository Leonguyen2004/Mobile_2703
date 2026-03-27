package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mobile_2703.R;
import com.example.mobile_2703.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;

/**
 * HomeActivity - Màn hình chính (hub điều hướng) của ứng dụng.
 *
 * Hiển thị banner chào mừng + 3 menu card (Phim, Rạp, Lịch Chiếu)
 * và card "Vé của Tôi" khi đã đăng nhập.
 */
public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    // Views
    private TextView tvGreeting;
    private TextView tvSubtitle;
    private MaterialCardView cardMyTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        // Bind views
        tvGreeting = findViewById(R.id.tv_greeting);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        cardMyTickets = findViewById(R.id.card_my_tickets);

        // Card click listeners
        MaterialCardView cardMovies = findViewById(R.id.card_movies);
        MaterialCardView cardTheaters = findViewById(R.id.card_theaters);
        MaterialCardView cardShowtimes = findViewById(R.id.card_showtimes);

        cardMovies.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MovieListActivity.class);
            startActivity(intent);
        });

        cardTheaters.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TheaterListActivity.class);
            startActivity(intent);
        });

        cardShowtimes.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ShowtimeListActivity.class);
            startActivity(intent);
        });

        cardMyTickets.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyTicketsActivity.class);
            startActivity(intent);
        });

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        invalidateOptionsMenu();
    }

    /**
     * Cập nhật greeting và hiển thị/ẩn card Vé dựa trên trạng thái đăng nhập.
     */
    private void updateUI() {
        if (sessionManager.isLoggedIn()) {
            String username = sessionManager.getUsername();
            tvGreeting.setText(getString(R.string.home_greeting_logged_in, username));
            tvSubtitle.setText(R.string.home_subtitle_logged_in);
            cardMyTickets.setVisibility(View.VISIBLE);
        } else {
            tvGreeting.setText(R.string.home_greeting_guest);
            tvSubtitle.setText(R.string.home_subtitle_guest);
            cardMyTickets.setVisibility(View.GONE);
        }
    }

    // =========================================================
    // MENU
    // =========================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean loggedIn = sessionManager.isLoggedIn();
        menu.findItem(R.id.menu_my_tickets).setVisible(loggedIn);
        menu.findItem(R.id.menu_logout).setVisible(loggedIn);
        menu.findItem(R.id.menu_login).setVisible(!loggedIn);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_my_tickets) {
            startActivity(new Intent(this, MyTicketsActivity.class));
            return true;
        } else if (id == R.id.menu_login) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            sessionManager.clearSession();
            Toast.makeText(this, R.string.logout_success, Toast.LENGTH_SHORT).show();
            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
