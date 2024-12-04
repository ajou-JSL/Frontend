package com.example.moum.view.community.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.view.community.BoardRecruitWriteActivity;

import java.util.ArrayList;

public class BoardRecruitWriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_create_image, parent, false);
            return new BoardRecruitWriteAdapter.ImageSelectorViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_create_image, parent, false);
            return new BoardRecruitWriteAdapter.ImageHolderViewHolder(view, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Uri uri = uris.get(position);
        if (holder instanceof BoardRecruitWriteAdapter.ImageSelectorViewHolder) {
            ((BoardRecruitWriteAdapter.ImageSelectorViewHolder) holder).bind(uri);
        } else if (holder instanceof BoardRecruitWriteAdapter.ImageHolderViewHolder) {
            ((BoardRecruitWriteAdapter.ImageHolderViewHolder) holder).bind(uri);
        }
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    static class ImageSelectorViewHolder extends RecyclerView.ViewHolder{
        private Uri uri;
        private ImageView boardWriteImageview;
        private Context context;
        private BoardRecruitWriteActivity boardRecruitWriteActivity;

        public ImageSelectorViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            boardWriteImageview = itemView.findViewById(R.id.imageview_board_create);
            this.context = context;
            boardRecruitWriteActivity = (BoardRecruitWriteActivity) context;
        }

        public void bind(Uri uri) {
            this.uri = uri;
            boardWriteImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boardRecruitWriteActivity.onImageSelectorClicked();
                }
            });
        }

    }

    static class ImageHolderViewHolder extends RecyclerView.ViewHolder{
        private Uri uri;
        private ImageView boardWriteImageview;
        private Context context;
        private BoardRecruitWriteActivity boardRecruitWriteActivity;

        public ImageHolderViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            boardWriteImageview = itemView.findViewById(R.id.imageview_board_create);
            this.context = context;
            boardRecruitWriteActivity = (BoardRecruitWriteActivity) context;
        }

        public void bind(Uri uri){
            this.uri = uri;
            Glide.with(context).load(uri).into(boardWriteImageview);
            boardWriteImageview.setClipToOutline(true);
        }
    }
}
