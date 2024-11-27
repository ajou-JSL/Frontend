package com.example.moum.view.profile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Genre;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileGenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Genre> genres;
    private Context context;

    public void setGenres(ArrayList<Genre> genres, Context context) {
        this.genres = genres;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new ProfileGenreAdapter.ProfileGenreViewModel(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Genre genre = genres.get(position);
        ((ProfileGenreAdapter.ProfileGenreViewModel) holder).bind(genre);

    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    static class ProfileGenreViewModel extends RecyclerView.ViewHolder{
        private ProfileGenreAdapter adapter;
        private Genre genre;
        private TextView genreItem;
        private Context context;

        public ProfileGenreViewModel(@NonNull View itemView, Context context, ProfileGenreAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            genreItem = itemView.findViewById(R.id.genre_item);
            this.context = context;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Genre genre){
            this.genre = genre;
            genreItem.setText(genre.name());
        }
    }
}
