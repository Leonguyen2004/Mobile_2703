package com.example.mobile_2703.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile_2703.R;
import com.example.mobile_2703.activity.HomeActivity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * BookingConfirmActivity - Xác nhận đặt vé (hỗ trợ nhiều ghế).
 *
 * Nhận danh sách ghế dạng chuỗi phân cách bởi dấu phẩy (vd: "A1,A2,B3").
 * Tạo một Ticket riêng cho mỗi ghế trong cùng một transaction SQLite.
 */
public class BookingConfirmActivity extends AppCompatActivity {

    private ShowtimeDAO showtimeDAO;
    private TicketDAO   ticketDAO;
    private SessionManager sessionManager;

    private int    showtimeId;
    private String seatNumbers; // vd: "A1,A2,B3"
    private double totalPrice;
    private Showtime showtime;

    private List<String> seatList = new ArrayList<>();

    private MaterialButton btnConfirm;
    private MaterialButton btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);

        showtimeDAO    = new ShowtimeDAO(this);
        ticketDAO      = new TicketDAO(this);
        sessionManager = new SessionManager(this);

        Intent intent = getIntent();
        showtimeId  = intent.getIntExtra(AppConstants.EXTRA_SHOWTIME_ID, -1);
        seatNumbers = intent.getStringExtra(AppConstants.EXTRA_SEAT_NUMBER);
        totalPrice  = intent.getDoubleExtra(AppConstants.EXTRA_TOTAL_PRICE, 0);

        if (showtimeId == -1 || seatNumbers == null || seatNumbers.isEmpty()) {
            Toast.makeText(this, "Lỗi: thiếu thông tin đặt vé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Phân tách danh sách ghế từ chuỗi "A1,A2,B3"
        seatList = Arrays.asList(seatNumbers.split(","));

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

        TextView tvMovie    = findViewById(R.id.tv_confirm_movie);
        TextView tvTheater  = findViewById(R.id.tv_confirm_theater);
        TextView tvDate     = findViewById(R.id.tv_confirm_date);
        TextView tvTime     = findViewById(R.id.tv_confirm_time);
        TextView tvSeat     = findViewById(R.id.tv_confirm_seat);
        TextView tvSeatCount = findViewById(R.id.tv_confirm_seat_count);
        TextView tvPrice    = findViewById(R.id.tv_confirm_price);

        tvMovie.setText(showtime.getMovieTitle() != null ? showtime.getMovieTitle() : "");
        tvTheater.setText(showtime.getTheaterName() != null ? showtime.getTheaterName() : "");
        tvDate.setText(showtime.getShowDate());
        tvTime.setText(showtime.getShowTime());

        // Hiển thị danh sách ghế
        tvSeat.setText(String.join("  ·  ", seatList));
        tvSeatCount.setText(seatList.size() + " ghế");

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPrice.setText(nf.format(totalPrice) + " đ");
    }

    private void confirmBooking() {
        btnConfirm.setEnabled(false);
        btnCancel.setEnabled(false);

        int userId = (int) sessionManager.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show();
            btnConfirm.setEnabled(true);
            btnCancel.setEnabled(true);
            return;
        }

        String bookingTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        double pricePerSeat = showtime != null ? showtime.getPrice() : (totalPrice / seatList.size());

        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();
        db.beginTransaction();
        try {
            // Insert một ticket cho mỗi ghế được chọn
            for (String seat : seatList) {
                Ticket ticket = new Ticket();
                ticket.setUserId(userId);
                ticket.setShowtimeId(showtimeId);
                ticket.setSeatNumber(seat.trim());
                ticket.setTotalPrice(pricePerSeat);
                ticket.setStatus("confirmed");
                ticket.setBookingTime(bookingTime);

                long ticketId = ticketDAO.insert(ticket);
                if (ticketId == -1) {
                    Toast.makeText(this, "Lỗi khi tạo vé cho ghế " + seat, Toast.LENGTH_SHORT).show();
                    btnConfirm.setEnabled(true);
                    btnCancel.setEnabled(true);
                    return;
                }
            }

            // Giảm số ghế trống đúng số lượng ghế đã đặt
            for (int i = 0; i < seatList.size(); i++) {
                int updated = showtimeDAO.decreaseAvailableSeats(showtimeId);
                if (updated <= 0) {
                    Toast.makeText(this, "Không còn đủ ghế trống!", Toast.LENGTH_SHORT).show();
                    btnConfirm.setEnabled(true);
                    btnCancel.setEnabled(true);
                    return;
                }
            }

            db.setTransactionSuccessful();

            Toast.makeText(this,
                    "Đặt " + seatList.size() + " vé thành công!",
                    Toast.LENGTH_SHORT).show();

            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(homeIntent);
            finish();

        } finally {
            db.endTransaction();
        }
    }
}
