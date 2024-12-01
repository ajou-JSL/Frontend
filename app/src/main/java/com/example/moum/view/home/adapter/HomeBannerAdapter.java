package com.example.moum.view.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.view.moum.MoumManageActivity;

import java.util.ArrayList;

public class HomeBannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Drawable> banners;
    private Context context;

    public void setBanners(ArrayList<Drawable> banners, Context context) {
        this.banners = banners;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_banner, parent, false);
        return new HomeBannerAdapter.HomeBannerViewModel(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Drawable banner = banners.get(position);
        ((HomeBannerAdapter.HomeBannerViewModel) holder).bind(banner);

    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    static class HomeBannerViewModel extends RecyclerView.ViewHolder{
        private Drawable banner;
        private ImageView imageviewBanner;
        private Context context;

        public HomeBannerViewModel(@NonNull View itemView, Context context) {
            super(itemView);
            imageviewBanner = itemView.findViewById(R.id.imageview_main_banner);
            this.context = context;
        }

        public void bind(Drawable banner) {
            Glide.with(context).load(banner).into(imageviewBanner);
        }

    }
}
