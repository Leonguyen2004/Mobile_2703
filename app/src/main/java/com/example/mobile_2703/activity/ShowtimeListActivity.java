package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.adapter.ShowtimeAdapter;
import com.example.mobile_2703.constants.AppConstants;
import com.example.mobile_2703.dao.MovieDAO;
import com.example.mobile_2703.dao.ShowtimeDAO;
import com.example.mobile_2703.dao.TheaterDAO;
import com.example.mobile_2703.model.Movie;
import com.example.mobile_2703.model.Showtime;
import com.example.mobile_2703.model.Theater;
import com.example.mobile_2703.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

/**
 * ShowtimeListActivity - Hiển thị danh sách suất chiếu theo phim hoặc theo rạp.
 *
 * Nhận EXTRA_MOVIE_ID hoặc EXTRA_THEATER_ID từ Intent.
 * Click vào suất chiếu → kiểm tra đăng nhập → chuyển sang SeatSelectionActivity.
 */
public class ShowtimeListActivity extends AppCompatActivity
        implements ShowtimeAdapter.OnShowtimeClickListener {

    private ShowtimeDAO showtimeDAO;
    private SessionManager sessionManager;
    private RecyclerView rvShowtimes;
    private TextView tvEmpty;

    // Lưu tạm showtimeId khi cần login trước
    private int pendingShowtimeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime_list);

        showtimeDAO    = new ShowtimeDAO(this);
        sessionManager = new SessionManager(this);

        // Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar_showtime);
        toolbar.setNavigationOnClickListener(v -> finish());

        rvShowtimes = findViewById(R.id.rv_showtimes);
        tvEmpty     = findViewById(R.id.tv_empty_showtime);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));

        loadShowtimes(toolbar);
    }

    private void loadShowtimes(MaterialToolbar toolbar) {
        List<Showtime> showtimes;
        Intent intent = getIntent();

        if (intent.hasExtra(AppConstants.EXTRA_MOVIE_ID)) {
            int movieId = intent.getIntExtra(AppConstants.EXTRA_MOVIE_ID, -1);

            // Lấy tên phim để hiển thị trên Toolbar
            MovieDAO movieDAO = new MovieDAO(this);
            Movie movie = movieDAO.getById(movieId);
            if (movie != null) {
                toolbar.setTitle(movie.getTitle());
            } else {
                toolbar.setTitle("Lịch chiếu");
            }

            showtimes = showtimeDAO.getByMovieId(movieId);

        } else if (intent.hasExtra(AppConstants.EXTRA_THEATER_ID)) {
            int theaterId = intent.getIntExtra(AppConstants.EXTRA_THEATER_ID, -1);

            // Lấy tên rạp để hiển thị trên Toolbar
            TheaterDAO theaterDAO = new TheaterDAO(this);
            Theater theater = theaterDAO.getById(theaterId);
            if (theater != null) {
                toolbar.setTitle(theater.getName());
            } else {
                toolbar.setTitle("Lịch chiếu");
            }

            showtimes = showtimeDAO.getByTheaterId(theaterId);

        } else {
            // Fallback: lấy tất cả
            toolbar.setTitle("Tất cả suất chiếu");
            showtimes = showtimeDAO.getAllWithDetails();
        }

        if (showtimes.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvShowtimes.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvShowtimes.setVisibility(View.VISIBLE);
            ShowtimeAdapter adapter = new ShowtimeAdapter(showtimes, this);
            rvShowtimes.setAdapter(adapter);
        }
    }

    @Override
    public void onShowtimeClick(Showtime showtime) {
        if (showtime.getAvailableSeats() <= 0) {
            Toast.makeText(this, "Suất chiếu này đã hết ghế!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sessionManager.isLoggedIn()) {
            openSeatSelection(showtime.getId());
        } else {
            // Lưu tạm showtimeId, yêu cầu đăng nhập
            pendingShowtimeId = showtime.getId();
            try {
                // Tìm LoginActivity bằng class name
                Class<?> loginClass = Class.forName("com.example.mobile_2703.activity.LoginActivity");
                Intent loginIntent = new Intent(this, loginClass);
                startActivityForResult(loginIntent, AppConstants.REQUEST_LOGIN);
            } catch (ClassNotFoundException e) {
                Toast.makeText(this, "Vui lòng đăng nhập trước!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openSeatSelection(int showtimeId) {
        Intent intent = new Intent(this, SeatSelectionActivity.class);
        intent.putExtra(AppConstants.EXTRA_SHOWTIME_ID, showtimeId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_LOGIN && resultCode == RESULT_OK) {
            // Đăng nhập thành công → tự động mở SeatSelection
            if (pendingShowtimeId != -1) {
                openSeatSelection(pendingShowtimeId);
                pendingShowtimeId = -1;
            }
        }
    }
}
