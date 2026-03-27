// FILE: app/src/main/java/com/example/mobile_2703/activity/MovieListActivity.java
package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.adapter.MovieAdapter;
import com.example.mobile_2703.dao.MovieDAO;
import com.example.mobile_2703.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    /** Key truyền movie id qua Intent. */
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private RecyclerView  rvMovies;
    private MovieAdapter  movieAdapter;
    private MovieDAO      movieDAO;
    private EditText      etSearch;
    private List<Movie>   movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieDAO = new MovieDAO(this);
        rvMovies = findViewById(R.id.rvMovies);
        etSearch = findViewById(R.id.etSearch);

        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        movieAdapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(EXTRA_MOVIE_ID, movie.getId());
            startActivity(intent);
        });
        rvMovies.setAdapter(movieAdapter);

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
        String query = etSearch.getText() != null ? etSearch.getText().toString().trim() : "";
        if (query.isEmpty()) {
            loadAllMovies();
        } else {
            filterMovies(query);
        }
    }

    private void loadAllMovies() {
        movieList.clear();
        movieList.addAll(movieDAO.getAll());
        movieAdapter.notifyDataSetChanged();
    }

    private void filterMovies(String keyword) {
        List<Movie> result = keyword.isEmpty()
                ? movieDAO.getAll()
                : movieDAO.searchByTitle(keyword);
        movieAdapter.updateList(result);
    }
}
