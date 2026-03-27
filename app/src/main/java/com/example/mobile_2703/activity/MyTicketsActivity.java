package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.adapter.TicketAdapter;
import com.example.mobile_2703.dao.TicketDAO;
import com.example.mobile_2703.model.Ticket;
import com.example.mobile_2703.utils.SessionManager;

import java.util.List;

/**
 * MyTicketsActivity - Hiển thị danh sách vé đã đặt của user.
 *
 * Chỉ accessible khi đã đăng nhập.
 * Dùng RecyclerView + TicketAdapter.
 */
public class MyTicketsActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TicketDAO ticketDAO;

    private RecyclerView rvTickets;
    private TextView tvEmpty;
    private TicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        sessionManager = new SessionManager(this);

        // Redirect to Login if not logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ticketDAO = new TicketDAO(this);

        // Toolbar with back button
        Toolbar toolbar = findViewById(R.id.toolbar_my_tickets);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Views
        rvTickets = findViewById(R.id.rv_tickets);
        tvEmpty = findViewById(R.id.tv_empty);

        // RecyclerView setup
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketAdapter();
        rvTickets.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTickets();
    }

    /**
     * Tải danh sách vé của user hiện tại.
     */
    private void loadTickets() {
        int userId = sessionManager.getUserId();
        List<Ticket> tickets = ticketDAO.getByUserId(userId);

        adapter.setTickets(tickets);

        if (tickets == null || tickets.isEmpty()) {
            rvTickets.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rvTickets.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
}
