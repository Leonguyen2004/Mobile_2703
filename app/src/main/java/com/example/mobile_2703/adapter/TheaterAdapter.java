// FILE: app/src/main/java/com/example/mobile_2703/adapter/TheaterAdapter.java
package com.example.mobile_2703.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.model.Theater;

import java.util.List;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.ViewHolder> {

    public interface OnTheaterClickListener {
        void onTheaterClick(Theater theater);
    }

    private List<Theater>              theaters;
    private final OnTheaterClickListener listener;

    public TheaterAdapter(List<Theater> theaters, OnTheaterClickListener listener) {
        this.theaters = theaters;
        this.listener  = listener;
    }

    /** Thay thế toàn bộ danh sách và refresh adapter. */
    public void updateList(List<Theater> newList) {
        this.theaters = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theater, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theater theater = theaters.get(position);
        holder.bind(theater, listener);
    }

    @Override
    public int getItemCount() {
        return theaters != null ? theaters.size() : 0;
    }

    // =========================================================
    // VIEW HOLDER
    // =========================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvName;
        final TextView tvAddress;
        final TextView tvCity;
        final TextView tvSeats;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName    = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCity    = itemView.findViewById(R.id.tvCity);
            tvSeats   = itemView.findViewById(R.id.tvSeats);
        }

        public void bind(Theater theater, OnTheaterClickListener listener) {
            tvName.setText(theater.getName());
            tvAddress.setText(theater.getAddress());

            if (theater.getCity() != null && !theater.getCity().isEmpty()) {
                tvCity.setText(theater.getCity());
                tvCity.setVisibility(View.VISIBLE);
            } else {
                tvCity.setVisibility(View.GONE);
            }

            tvSeats.setText("Số ghế: " + theater.getTotalSeats());

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onTheaterClick(theater);
            });
        }
    }
}
