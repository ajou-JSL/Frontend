package com.example.moum.view.moum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.moum.view.moum.MoumCreateActivity;

import java.util.ArrayList;

public class MoumCreateImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Uri> uris;
    private Context context;
    private static final int VIEW_TYPE_IMAGE_HOLDER = 1;
    private static final int VIEW_TYPE_IMAGE_SELECTOR = 2;

    public void setUris(ArrayList<Uri> uris, @NonNull Context context) {
        this.uris = uris;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position == uris.size()-1? VIEW_TYPE_IMAGE_SELECTOR : VIEW_TYPE_IMAGE_HOLDER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_IMAGE_SELECTOR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_create_image, parent, false);
            return new MoumCreateImageAdapter.ImageSelectorViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moum_create_image, parent, false);
            return new MoumCreateImageAdapter.ImageHolderViewHolder(view, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Uri uri = uris.get(position);
        if (holder instanceof MoumCreateImageAdapter.ImageSelectorViewHolder) {
            ((MoumCreateImageAdapter.ImageSelectorViewHolder) holder).bind(uri);
        } else if (holder instanceof MoumCreateImageAdapter.ImageHolderViewHolder) {
            ((MoumCreateImageAdapter.ImageHolderViewHolder) holder).bind(uri);
        }
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    static class ImageSelectorViewHolder extends RecyclerView.ViewHolder{
        private Uri uri;
        private ImageView moumCreateImageview;
        private Context context;
        private MoumCreateActivity moumCreateActivity;

        public ImageSelectorViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            moumCreateImageview = itemView.findViewById(R.id.imageview_moum_create);
            this.context = context;
            moumCreateActivity = (MoumCreateActivity) context;
        }

        public void bind(Uri uri) {
            this.uri = uri;
            moumCreateImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moumCreateActivity.onImageSelectorClicked();
                }
            });
        }

    }

    static class ImageHolderViewHolder extends RecyclerView.ViewHolder{
        private Uri uri;
        private ImageView moumCreateImageview;
        private Context context;
        private MoumCreateActivity moumCreateActivity;

        public ImageHolderViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            moumCreateImageview = itemView.findViewById(R.id.imageview_moum_create);
            this.context = context;
            moumCreateActivity = (MoumCreateActivity) context;
        }

        public void bind(Uri uri){
            this.uri = uri;
            Glide.with(context).load(uri).into(moumCreateImageview);
            moumCreateImageview.getClipToOutline();
        }
    }

}
