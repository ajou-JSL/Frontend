package com.example.moum.view.auth.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Genre;
import com.example.moum.data.entity.Member;
import com.example.moum.utils.ImageManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Genre> genres;
    private ArrayList<Boolean> isSelecteds;
    private Context context;

    public void setGenres(Genre[] genres, Context context) {
        this.genres = new ArrayList<>(Arrays.asList(genres));
        this.context = context;
    }

    public ArrayList<Boolean> getIsSelecteds() {
        Log.e(TAG, isSelecteds.isEmpty()? "empty" : "not");
        return isSelecteds;
    }

    public void setIsSelected(int pos, Boolean isSelected) {
        if(!isSelecteds.isEmpty()){
            isSelecteds.set(pos, isSelected);}
    }

    public Boolean getIsSelected(int pos){
        if(!isSelecteds.isEmpty()){
            return isSelecteds.get(pos);
        }
        return false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isSelecteds == null) isSelecteds = new ArrayList<>(Collections.nCopies(genres.size(), false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenreAdapter.GenreViewModel(view, context, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Genre genre = genres.get(position);
        ((GenreAdapter.GenreViewModel) holder).bind(genre);

    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    static class GenreViewModel extends RecyclerView.ViewHolder{
        private GenreAdapter adapter;
        private Genre genre;
        private TextView genreItem;
        private Context context;

        public GenreViewModel(@NonNull View itemView, Context context, GenreAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            genreItem = itemView.findViewById(R.id.genre_item);
            this.context = context;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Genre genre){
            this.genre = genre;
            int pos = getAbsoluteAdapterPosition();
            genreItem.setText(genre.name());
            if (pos != RecyclerView.NO_POSITION) {
                Boolean isSelected = adapter.getIsSelected(pos);
                if (isSelected) {
                    genreItem.setBackground(context.getDrawable(R.drawable.background_genre_item_mint));
                    genreItem.setTextColor(context.getColor(R.color.gray0));
                    adapter.setIsSelected(pos, false);
                } else {
                    genreItem.setBackground(context.getDrawable(R.drawable.background_genre_item));
                    genreItem.setTextColor(context.getColor(R.color.gray7));
                    adapter.setIsSelected(pos, true);
                }
            }
            genreItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pos != RecyclerView.NO_POSITION) {
                        Boolean isSelected = adapter.getIsSelected(pos);
                        if(isSelected){
                            genreItem.setBackground(context.getDrawable(R.drawable.background_genre_item_mint));
                            genreItem.setTextColor(context.getColor(R.color.gray0));
                            adapter.setIsSelected(pos, false);
                        }
                        else{
                            genreItem.setBackground(context.getDrawable(R.drawable.background_genre_item));
                            genreItem.setTextColor(context.getColor(R.color.gray7));
                            adapter.setIsSelected(pos, true);
                        }
                    }
                }
            });
        }
    }
}
