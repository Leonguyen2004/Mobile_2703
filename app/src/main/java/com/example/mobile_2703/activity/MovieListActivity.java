// FILE: app/src/main/java/com/example/mobile_2703/activity/MovieListActivity.java
package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.adapter.MovieAdapter;
import com.example.mobile_2703.constants.AppConstants;
import com.example.mobile_2703.dao.MovieDAO;
import com.example.mobile_2703.dao.TheaterDAO;
import com.example.mobile_2703.model.Movie;
import com.example.mobile_2703.model.Theater;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView  rvMovies;
    private MovieAdapter  movieAdapter;
    private MovieDAO      movieDAO;
    private EditText      etSearch;
    private List<Movie>   movieList = new ArrayList<>();

    /** -1 nếu không có rạp cụ thể (chế độ xem tất cả phim) */
    private int theaterId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieDAO  = new MovieDAO(this);
        rvMovies  = findViewById(R.id.rvMovies);
        etSearch  = findViewById(R.id.etSearch);

        // Kiểm tra có chạy từ rạp không
        theaterId = getIntent().getIntExtra(AppConstants.EXTRA_THEATER_ID, -1);

        if (theaterId != -1) {
            // Chế độ rạp: đặt tiêu đề toolbar = tên rạp
            TheaterDAO theaterDAO = new TheaterDAO(this);
            Theater theater = theaterDAO.getById(theaterId);
            if (theater != null && getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Phim tại " + theater.getName());
            }
            // Ẩn ô tìm kiếm vì danh sách đã được lọc theo rạp
            etSearch.setVisibility(View.GONE);
        }

        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));

        movieAdapter = new MovieAdapter(movieList, movie -> {
            if (theaterId != -1) {
                // Chế độ rạp: click phim → ShowtimeListActivity lọc theo phim + rạp
                Intent intent = new Intent(MovieListActivity.this, ShowtimeListActivity.class);
                intent.putExtra(AppConstants.EXTRA_MOVIE_ID, (int) movie.getId());
                intent.putExtra(AppConstants.EXTRA_THEATER_ID, theaterId);
                startActivity(intent);
            } else {
                // Chế độ bình thường: click phim → MovieDetailActivity
                Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
                intent.putExtra(AppConstants.EXTRA_MOVIE_ID, movie.getId());
                startActivity(intent);
            }
        });
        rvMovies.setAdapter(movieAdapter);

        // Tìm kiếm chỉ hiển thị ở chế độ bình thường
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString().trim());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (theaterId != -1) {
            loadMoviesByTheater();
        } else {
            String query = etSearch.getText() != null ? etSearch.getText().toString().trim() : "";
            if (query.isEmpty()) {
                loadAllMovies();
            } else {
                filterMovies(query);
            }
        }
    }

    private void loadAllMovies() {
        movieList.clear();
        movieList.addAll(movieDAO.getAll());
        movieAdapter.notifyDataSetChanged();
    }

    private void loadMoviesByTheater() {
        movieList.clear();
        movieList.addAll(movieDAO.getByTheaterId(theaterId));
        movieAdapter.notifyDataSetChanged();
    }

    private void filterMovies(String keyword) {
        List<Movie> result = keyword.isEmpty()
                ? movieDAO.getAll()
                : movieDAO.searchByTitle(keyword);
        movieAdapter.updateList(result);
    }
}
