package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.LoginActivity;
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

        int movieId   = intent.getIntExtra(AppConstants.EXTRA_MOVIE_ID,   -1);
        int theaterId = intent.getIntExtra(AppConstants.EXTRA_THEATER_ID, -1);

        if (movieId != -1 && theaterId != -1) {
            // Lọc theo cả phim lẫn rạp (từ luồng: Rạp → Phim → Lịch chiếu)
            MovieDAO   movieDAO   = new MovieDAO(this);
            TheaterDAO theaterDAO = new TheaterDAO(this);
            Movie   movie   = movieDAO.getById(movieId);
            Theater theater = theaterDAO.getById(theaterId);

            String movieName   = movie   != null ? movie.getTitle()   : "Phim";
            String theaterName = theater != null ? theater.getName()  : "Rạp";
            toolbar.setTitle(movieName + " · " + theaterName);

            showtimes = showtimeDAO.getByMovieAndTheaterId(movieId, theaterId);

        } else if (movieId != -1) {
            // Lọc theo phim (từ MovieDetailActivity)
            MovieDAO movieDAO = new MovieDAO(this);
            Movie movie = movieDAO.getById(movieId);
            toolbar.setTitle(movie != null ? movie.getTitle() : "Lịch chiếu");

            showtimes = showtimeDAO.getByMovieId(movieId);

        } else if (theaterId != -1) {
            // Lọc theo rạp
            TheaterDAO theaterDAO = new TheaterDAO(this);
            Theater theater = theaterDAO.getById(theaterId);
            toolbar.setTitle(theater != null ? theater.getName() : "Lịch chiếu");

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
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, AppConstants.REQUEST_LOGIN);
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
