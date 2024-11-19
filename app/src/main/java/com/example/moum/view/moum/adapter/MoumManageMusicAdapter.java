package com.example.moum.view.moum.adapter;

import android.content.Context;
import android.os.Build;
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
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Music;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoumManageMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Music> musics;
    private Context context;

    public void setMusics(ArrayList<Music> musics, Context context) {
        this.musics = musics;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_manage_music, parent, false);
        return new MoumManageMusicAdapter.MoumManageMusicViewModel(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Music music = musics.get(position);
        ((MoumManageMusicAdapter.MoumManageMusicViewModel) holder).bind(music);

    }

    @Override
    public int getItemCount() {
        return musics.size();
    }

    static class MoumManageMusicViewModel extends RecyclerView.ViewHolder{
        private Music music;
        private TextView musicName;
        private TextView artistName;
        private Context context;

        public MoumManageMusicViewModel(@NonNull View itemView, Context context) {
            super(itemView);
            musicName = itemView.findViewById(R.id.music_name);
            artistName = itemView.findViewById(R.id.artist_name);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Music music){
            this.music = music;
            musicName.setText(music.getMusicName());
            artistName.setText(music.getArtistName());
        }
    }
}
