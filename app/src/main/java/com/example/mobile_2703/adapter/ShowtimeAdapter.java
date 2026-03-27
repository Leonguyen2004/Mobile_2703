package com.example.mobile_2703.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.model.Showtime;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * ShowtimeAdapter - Adapter cho RecyclerView hiển thị danh sách suất chiếu.
 */
public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private final List<Showtime> showtimeList;
    private OnShowtimeClickListener listener;

    public interface OnShowtimeClickListener {
        void onShowtimeClick(Showtime showtime);
    }

    public ShowtimeAdapter(List<Showtime> showtimeList, OnShowtimeClickListener listener) {
        this.showtimeList = showtimeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimeList.get(position);
        holder.bind(showtime);
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    class ShowtimeViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvSubtitle;
        private final TextView tvDate;
        private final TextView tvTime;
        private final TextView tvPrice;
        private final TextView tvSeats;

        ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle    = itemView.findViewById(R.id.tv_showtime_title);
            tvSubtitle = itemView.findViewById(R.id.tv_showtime_subtitle);
            tvDate     = itemView.findViewById(R.id.tv_showtime_date);
            tvTime     = itemView.findViewById(R.id.tv_showtime_time);
            tvPrice    = itemView.findViewById(R.id.tv_showtime_price);
            tvSeats    = itemView.findViewById(R.id.tv_showtime_seats);
        }

        void bind(Showtime showtime) {
            // Hiển thị tên phim / tên rạp
            String title = showtime.getMovieTitle();
            String subtitle = showtime.getTheaterName();
            if (title == null || title.isEmpty()) {
                title = "Phim #" + showtime.getMovieId();
            }
            if (subtitle == null || subtitle.isEmpty()) {
                subtitle = "Rạp #" + showtime.getTheaterId();
            }

            tvTitle.setText(title);
            tvSubtitle.setText(subtitle);
            tvDate.setText("📅 " + showtime.getShowDate());
            tvTime.setText("🕐 " + showtime.getShowTime());

            // Format giá tiền
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvPrice.setText(nf.format(showtime.getPrice()) + " đ");

            tvSeats.setText("Còn " + showtime.getAvailableSeats() + " ghế");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onShowtimeClick(showtime);
                }
            });
        }
    }
}
