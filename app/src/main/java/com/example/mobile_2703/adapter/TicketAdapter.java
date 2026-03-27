package com.example.mobile_2703.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.constants.DBConstants;
import com.example.mobile_2703.model.Ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * TicketAdapter - RecyclerView.Adapter cho danh sách vé.
 *
 * Hiển thị thông tin vé: phim, rạp, ngày, giờ, ghế, giá, trạng thái.
 * Status badge: "ĐÃ XÁC NHẬN" (xanh) / "ĐÃ HỦY" (đỏ).
 */
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> tickets = new ArrayList<>();

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets != null ? tickets : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.bind(ticket);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    // =========================================================
    // ViewHolder
    // =========================================================

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvMovieTitle;
        private final TextView tvTheaterName;
        private final TextView tvShowDate;
        private final TextView tvShowTime;
        private final TextView tvSeatNumber;
        private final TextView tvTotalPrice;
        private final TextView tvStatus;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle  = itemView.findViewById(R.id.tv_movie_title);
            tvTheaterName = itemView.findViewById(R.id.tv_theater_name);
            tvShowDate    = itemView.findViewById(R.id.tv_show_date);
            tvShowTime    = itemView.findViewById(R.id.tv_show_time);
            tvSeatNumber  = itemView.findViewById(R.id.tv_seat_number);
            tvTotalPrice  = itemView.findViewById(R.id.tv_total_price);
            tvStatus      = itemView.findViewById(R.id.tv_status);
        }

        void bind(Ticket ticket) {
            tvMovieTitle.setText(ticket.getMovieTitle());
            tvTheaterName.setText(ticket.getTheaterName());
            tvShowDate.setText(ticket.getShowDate());
            tvShowTime.setText(ticket.getShowTime());
            tvSeatNumber.setText(ticket.getSeatNumber());
            tvTotalPrice.setText(String.format(
                    itemView.getContext().getString(R.string.ticket_price_format),
                    ticket.getTotalPrice()));

            // Status badge
            String status = ticket.getStatus();
            if (DBConstants.TicketStatus.CONFIRMED.equals(status)) {
                tvStatus.setText(R.string.ticket_status_confirmed);
                setStatusBadgeColor(R.color.colorStatusConfirmed);
            } else {
                tvStatus.setText(R.string.ticket_status_cancelled);
                setStatusBadgeColor(R.color.colorStatusCancelled);
            }
        }

        private void setStatusBadgeColor(int colorRes) {
            GradientDrawable bg = new GradientDrawable();
            bg.setColor(ContextCompat.getColor(itemView.getContext(), colorRes));
            bg.setCornerRadius(
                    itemView.getContext().getResources()
                            .getDimension(R.dimen.card_corner_radius));
            tvStatus.setBackground(bg);
        }
    }
}
