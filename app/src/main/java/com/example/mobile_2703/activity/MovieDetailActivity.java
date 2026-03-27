// FILE: app/src/main/java/com/example/mobile_2703/activity/MovieDetailActivity.java
package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mobile_2703.R;
import com.example.mobile_2703.dao.MovieDAO;
import com.example.mobile_2703.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Toolbar với nút Back
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        long movieId = getIntent().getLongExtra(MovieListActivity.EXTRA_MOVIE_ID, -1);
        if (movieId == -1) {
            Toast.makeText(this, "Không tìm thấy phim", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        MovieDAO movieDAO = new MovieDAO(this);
        Movie    movie    = movieDAO.findById(movieId);
        if (movie == null) {
            Toast.makeText(this, "Không tìm thấy phim", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindMovie(movie);

        // Nút Xem Lịch Chiếu — dùng setClassName để tránh lỗi compile khi
        // ShowtimeListActivity chưa được tạo bởi member khác
        Button btnShowtime = findViewById(R.id.btnShowtime);
        btnShowtime.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClassName(
                    getPackageName(),
                    getPackageName() + ".activity.ShowtimeListActivity"
            );
            intent.putExtra(MovieListActivity.EXTRA_MOVIE_ID, movieId);
            startActivity(intent);
        });
    }

    private void bindMovie(Movie movie) {
        ImageView ivPoster      = findViewById(R.id.ivPoster);
        TextView  tvTitle       = findViewById(R.id.tvTitle);
        TextView  tvGenre       = findViewById(R.id.tvGenre);
        TextView  tvDuration    = findViewById(R.id.tvDuration);
        TextView  tvRating      = findViewById(R.id.tvRating);
        TextView  tvDescription = findViewById(R.id.tvDescription);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(movie.getTitle());
        }

        // Placeholder màu xám — không dùng Glide/Picasso
        ivPoster.setImageDrawable(null);
        ivPoster.setBackgroundResource(R.drawable.ic_movie_placeholder);

        tvTitle.setText(movie.getTitle());
        tvGenre.setText(movie.getGenre());
        tvDuration.setText(movie.getDuration() + " phút");
        tvRating.setText(String.format("%.1f/10", movie.getRating()));
        tvDescription.setText(movie.getDescription());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
