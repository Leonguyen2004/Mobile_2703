// FILE: app/src/main/java/com/example/mobile_2703/activity/TheaterListActivity.java
package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.adapter.TheaterAdapter;
import com.example.mobile_2703.constants.AppConstants;
import com.example.mobile_2703.dao.TheaterDAO;
import com.example.mobile_2703.model.Theater;

import java.util.ArrayList;
import java.util.List;

public class TheaterListActivity extends AppCompatActivity {

    private RecyclerView   rvTheaters;
    private TheaterAdapter theaterAdapter;
    private TheaterDAO     theaterDAO;
    private List<Theater>  theaterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        theaterDAO = new TheaterDAO(this);
        rvTheaters = findViewById(R.id.rvTheaters);
        rvTheaters.setLayoutManager(new LinearLayoutManager(this));

        theaterAdapter = new TheaterAdapter(theaterList, theater -> {
            // Click rạp → hiển thị danh sách phim đang chiếu tại rạp đó
            Intent intent = new Intent(TheaterListActivity.this, MovieListActivity.class);
            intent.putExtra(AppConstants.EXTRA_THEATER_ID, (int) theater.getId());
            startActivity(intent);
        });
        rvTheaters.setAdapter(theaterAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTheaters();
    }

    private void loadTheaters() {
        theaterList.clear();
        theaterList.addAll(theaterDAO.getAll());
        theaterAdapter.notifyDataSetChanged();
    }
}
