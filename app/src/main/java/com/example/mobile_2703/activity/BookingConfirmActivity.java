package com.example.mobile_2703.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_2703.R;
import com.example.mobile_2703.constants.AppConstants;
import com.example.mobile_2703.dao.ShowtimeDAO;
import com.example.mobile_2703.dao.TicketDAO;
import com.example.mobile_2703.database.DatabaseHelper;
import com.example.mobile_2703.model.Showtime;
import com.example.mobile_2703.model.Ticket;
import com.example.mobile_2703.utils.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * BookingConfirmActivity - Xác nhận đặt vé.
 *
 * Hiển thị tóm tắt thông tin -> xác nhận tạo Ticket + decrement seats trong transaction.
 */
public class BookingConfirmActivity extends AppCompatActivity {

    private ShowtimeDAO showtimeDAO;
    private TicketDAO ticketDAO;
    private SessionManager sessionManager;

    private int showtimeId;
    private String seatNumber;
    private double totalPrice;
    private Showtime showtime;

    private MaterialButton btnConfirm;
    private MaterialButton btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);

        showtimeDAO    = new ShowtimeDAO(this);
        ticketDAO      = new TicketDAO(this);
        sessionManager = new SessionManager(this);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        showtimeId = intent.getIntExtra(AppConstants.EXTRA_SHOWTIME_ID, -1);
        seatNumber = intent.getStringExtra(AppConstants.EXTRA_SEAT_NUMBER);
        totalPrice = intent.getDoubleExtra(AppConstants.EXTRA_TOTAL_PRICE, 0);

        if (showtimeId == -1 || seatNumber == null) {
            Toast.makeText(this, "Lỗi: thiếu thông tin đặt vé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar_confirm);
        toolbar.setNavigationOnClickListener(v -> finish());

        btnConfirm = findViewById(R.id.btn_confirm_booking);
        btnCancel  = findViewById(R.id.btn_cancel_booking);

        loadInfo();

        btnConfirm.setOnClickListener(v -> confirmBooking());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadInfo() {
        showtime = showtimeDAO.getWithDetails(showtimeId);
        if (showtime == null) {
            Toast.makeText(this, "Không tìm thấy suất chiếu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView tvMovie   = findViewById(R.id.tv_confirm_movie);
        TextView tvTheater = findViewById(R.id.tv_confirm_theater);
        TextView tvDate    = findViewById(R.id.tv_confirm_date);
        TextView tvTime    = findViewById(R.id.tv_confirm_time);
        TextView tvSeat    = findViewById(R.id.tv_confirm_seat);
        TextView tvPrice   = findViewById(R.id.tv_confirm_price);

        tvMovie.setText(showtime.getMovieTitle() != null ? showtime.getMovieTitle() : "");
        tvTheater.setText(showtime.getTheaterName() != null ? showtime.getTheaterName() : "");
        tvDate.setText(showtime.getShowDate());
        tvTime.setText(showtime.getShowTime());
        tvSeat.setText(seatNumber);

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPrice.setText(nf.format(totalPrice) + " đ");
    }

    private void confirmBooking() {
        // Disable button ngay lập tức để tránh bấm 2 lần
        btnConfirm.setEnabled(false);
        btnCancel.setEnabled(false);

        int userId = (int) sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            btnConfirm.setEnabled(true);
            btnCancel.setEnabled(true);
            return;
        }

        // Tạo Ticket
        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setShowtimeId(showtimeId);
        ticket.setSeatNumber(seatNumber);
        ticket.setTotalPrice(totalPrice);
        ticket.setStatus("confirmed");

        // Booking time hiện tại (ISO format)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        ticket.setBookingTime(sdf.format(new Date()));

        // Transaction: insert ticket + decrement seats
        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();
        db.beginTransaction();
        try {
            long ticketId = ticketDAO.insert(ticket);
            if (ticketId == -1) {
                Toast.makeText(this, "Lỗi khi tạo vé!", Toast.LENGTH_SHORT).show();
                btnConfirm.setEnabled(true);
                btnCancel.setEnabled(true);
                return;
            }

            int updated = showtimeDAO.decreaseAvailableSeats(showtimeId);
            if (updated <= 0) {
                Toast.makeText(this, "Không còn ghế trống!", Toast.LENGTH_SHORT).show();
                btnConfirm.setEnabled(true);
                btnCancel.setEnabled(true);
                return;
            }

            db.setTransactionSuccessful();
            Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();

            // Quay về HomeActivity nếu có, hoặc về MainActivity
            Intent homeIntent;
            try {
                Class<?> homeClass = Class.forName("com.example.mobile_2703.activity.HomeActivity");
                homeIntent = new Intent(this, homeClass);
            } catch (ClassNotFoundException e) {
                // Fallback về MainActivity nếu HomeActivity chưa có
                homeIntent = new Intent(this, com.example.mobile_2703.MainActivity.class);
            }
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(homeIntent);
            finish();

        } finally {
            db.endTransaction();
        }
    }
}
