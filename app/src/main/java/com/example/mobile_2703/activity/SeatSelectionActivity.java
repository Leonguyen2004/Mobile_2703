package com.example.mobile_2703.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mobile_2703.R;
import com.example.mobile_2703.constants.AppConstants;
import com.example.mobile_2703.dao.ShowtimeDAO;
import com.example.mobile_2703.model.Showtime;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * SeatSelectionActivity - Hiển thị lưới ghế để user chọn ghế.
 *
 * Tạo 30 ghế cố định (A1-A5, B1-B5, ... F1-F5).
 * Ghế đã đặt: đỏ (disabled). Ghế trống: xanh lá. Ghế đang chọn: vàng.
 */
public class SeatSelectionActivity extends AppCompatActivity {

    private static final int TOTAL_ROWS = 6;
    private static final int SEATS_PER_ROW = 5;
    private static final String[] ROW_LABELS = {"A", "B", "C", "D", "E", "F"};

    private ShowtimeDAO showtimeDAO;
    private int showtimeId;
    private Showtime showtime;

    private GridLayout gridSeats;
    private TextView tvSelectedSeat;
    private MaterialButton btnConfirm;
    private TextView tvMovie, tvTheater, tvDatetime, tvPrice;

    private String selectedSeat = null;
    private TextView selectedSeatView = null;

    // Màu
    private static final int COLOR_AVAILABLE = 0xFF4CAF50; // Xanh lá
    private static final int COLOR_BOOKED    = 0xFFF44336; // Đỏ
    private static final int COLOR_SELECTED  = 0xFFFFEB3B; // Vàng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        showtimeDAO = new ShowtimeDAO(this);
        showtimeId  = getIntent().getIntExtra(AppConstants.EXTRA_SHOWTIME_ID, -1);

        if (showtimeId == -1) {
            Toast.makeText(this, "Lỗi: không tìm thấy suất chiếu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar_seat);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Bind views
        tvMovie        = findViewById(R.id.tv_seat_movie);
        tvTheater      = findViewById(R.id.tv_seat_theater);
        tvDatetime     = findViewById(R.id.tv_seat_datetime);
        tvPrice        = findViewById(R.id.tv_seat_price);
        gridSeats      = findViewById(R.id.grid_seats);
        tvSelectedSeat = findViewById(R.id.tv_selected_seat);
        btnConfirm     = findViewById(R.id.btn_confirm_seat);

        loadShowtimeInfo();
        buildSeatGrid();

        btnConfirm.setOnClickListener(v -> {
            if (selectedSeat != null && showtime != null) {
                Intent intent = new Intent(this, BookingConfirmActivity.class);
                intent.putExtra(AppConstants.EXTRA_SHOWTIME_ID, showtimeId);
                intent.putExtra(AppConstants.EXTRA_SEAT_NUMBER, selectedSeat);
                intent.putExtra(AppConstants.EXTRA_TOTAL_PRICE, showtime.getPrice());
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadShowtimeInfo() {
        showtime = showtimeDAO.getWithDetails(showtimeId);
        if (showtime == null) {
            Toast.makeText(this, "Không tìm thấy thông tin suất chiếu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvMovie.setText(showtime.getMovieTitle() != null ? showtime.getMovieTitle() : "");
        tvTheater.setText(showtime.getTheaterName() != null ? showtime.getTheaterName() : "");
        tvDatetime.setText(showtime.getShowDate() + " - " + showtime.getShowTime());

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvPrice.setText(nf.format(showtime.getPrice()) + " đ");
    }

    private void buildSeatGrid() {
        gridSeats.removeAllViews();
        gridSeats.setColumnCount(SEATS_PER_ROW);

        // Lấy danh sách ghế đã đặt
        List<String> bookedSeats = showtimeDAO.getBookedSeats(showtimeId);

        for (int row = 0; row < TOTAL_ROWS; row++) {
            for (int col = 0; col < SEATS_PER_ROW; col++) {
                String seatLabel = ROW_LABELS[row] + (col + 1);

                TextView seatView = new TextView(this);
                seatView.setText(seatLabel);
                seatView.setGravity(Gravity.CENTER);
                seatView.setTextSize(13);
                seatView.setPadding(8, 12, 8, 12);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width  = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(col, 1f);
                params.setMargins(4, 4, 4, 4);
                seatView.setLayoutParams(params);

                if (bookedSeats.contains(seatLabel)) {
                    // Ghế đã đặt
                    seatView.setBackgroundColor(COLOR_BOOKED);
                    seatView.setTextColor(0xFFFFFFFF);
                    seatView.setEnabled(false);
                    seatView.setAlpha(0.7f);
                } else {
                    // Ghế trống
                    seatView.setBackgroundColor(COLOR_AVAILABLE);
                    seatView.setTextColor(0xFFFFFFFF);
                    seatView.setOnClickListener(v -> onSeatClicked(seatView, seatLabel));
                }

                gridSeats.addView(seatView);
            }
        }
    }

    private void onSeatClicked(TextView seatView, String seatLabel) {
        // Bỏ chọn ghế cũ (nếu có)
        if (selectedSeatView != null) {
            selectedSeatView.setBackgroundColor(COLOR_AVAILABLE);
        }

        // Nếu click vào ghế đang chọn → bỏ chọn
        if (seatLabel.equals(selectedSeat)) {
            selectedSeat = null;
            selectedSeatView = null;
            tvSelectedSeat.setText("Chưa chọn ghế");
            btnConfirm.setEnabled(false);
            return;
        }

        // Chọn ghế mới
        selectedSeat = seatLabel;
        selectedSeatView = seatView;
        seatView.setBackgroundColor(COLOR_SELECTED);
        seatView.setTextColor(0xFF000000);

        tvSelectedSeat.setText("Ghế: " + seatLabel);
        btnConfirm.setEnabled(true);
    }
}
