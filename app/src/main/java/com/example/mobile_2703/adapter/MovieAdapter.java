// FILE: app/src/main/java/com/example/mobile_2703/adapter/MovieAdapter.java
package com.example.mobile_2703.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_2703.R;
import com.example.mobile_2703.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private List<Movie>            movies;
    private final OnMovieClickListener listener;

    public MovieAdapter(List<Movie> movies, OnMovieClickListener listener) {
        this.movies   = movies;
        this.listener = listener;
    }

    /** Thay thế toàn bộ danh sách và refresh adapter. */
    public void updateList(List<Movie> newList) {
        this.movies = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    // =========================================================
    // VIEW HOLDER
    // =========================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView ivPoster;
        final TextView  tvTitle;
        final TextView  tvGenre;
        final TextView  tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle  = itemView.findViewById(R.id.tvTitle);
            tvGenre  = itemView.findViewById(R.id.tvGenre);
            tvRating = itemView.findViewById(R.id.tvRating);
        }

        public void bind(Movie movie, OnMovieClickListener listener) {
            tvTitle.setText(movie.getTitle());
            tvGenre.setText(movie.getGenre());
            tvRating.setText(String.format("%.1f", movie.getRating()));

            // Không dùng Glide — giữ placeholder màu xám
            ivPoster.setImageDrawable(null);
            ivPoster.setBackgroundResource(R.drawable.ic_movie_placeholder);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMovieClick(movie);
            });
        }
    }
}
