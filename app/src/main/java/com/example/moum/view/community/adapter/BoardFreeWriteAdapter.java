package com.example.moum.view.community.adapter;



import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moum.R;

import java.util.ArrayList;

public class BoardFreeWriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_ADD_BUTTON = 1;

    private final ArrayList<String> imageList;
    private final Context context;
    private final OnImageClickListener onImageClickListener;

    public interface OnImageClickListener {
        void onAddImageClick();
        void onImageClick(int position);
        void onImageLongClick(int position);
    }

    public BoardFreeWriteAdapter(Context context, ArrayList<String> imageList, OnImageClickListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.onImageClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        // 마지막 아이템은 사진 추가
        return position == imageList.size() ? TYPE_ADD_BUTTON : TYPE_IMAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_board_image_content, parent, false);
            return new ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_board_create_image, parent, false);
            return new AddButtonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_IMAGE) {
            ((ImageViewHolder) holder).bind(imageList.get(position), position);
        } else {
            ((AddButtonViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        // 아이템 수는 이미지 리스트 크기 + 1 ("사진 추가 버튼")
        return imageList.size() + 1;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_board_content);
        }

        public void bind(String imageUri, int position) {
            // 이미지 로드
            Glide.with(context)
                    .load(Uri.parse(imageUri))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);

            Log.e("imageURL","이미지 로드완료 URL : " + imageUri);
            // 이미지 클릭 이벤트 처리
            imageView.setOnClickListener(v -> onImageClickListener.onImageClick(position));

            // 이미지 길게 클릭 이벤트 처리
            imageView.setOnLongClickListener(v -> {
                onImageClickListener.onImageLongClick(position);
                return true;
            });
        }
    }

    class AddButtonViewHolder extends RecyclerView.ViewHolder {
        private final ImageView addButton;

        public AddButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            addButton = itemView.findViewById(R.id.imageview_board_create);
        }

        public void bind() {
            // 사진 추가 버튼 클릭 이벤트 처리
            addButton.setOnClickListener(v -> onImageClickListener.onAddImageClick());
        }
    }
}