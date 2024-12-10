package com.example.moum.view.community.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.view.community.ImageDialogFragment;

import java.util.ArrayList;

public class BoardImageAdapter extends RecyclerView.Adapter<BoardImageAdapter.CustomViewHolder> {
    private ArrayList<String> imageList;
    private final Context context;
    private BoardImageAdapter.ItemClickListener itemClickListener;

    public BoardImageAdapter(Context context) {
        this.context = context;
    }

    public void updateItemList(ArrayList<String> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener{
        void onItemClick(View v, int position);
    }

    @Override
    public int getItemCount() {
        return imageList != null ? imageList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_board_image_content;
    }

    @NonNull
    @Override
    public BoardImageAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new BoardImageAdapter.CustomViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bind(imageList.get(position));

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof androidx.fragment.app.FragmentActivity) {
                String imageUrl = imageList.get(position);
                ImageDialogFragment dialogFragment = ImageDialogFragment.newInstance(imageUrl);
                dialogFragment.show(((androidx.fragment.app.FragmentActivity) context).getSupportFragmentManager(), "ImageDialog");
            }
        });
    }



    static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ItemClickListener itemClickListener;
        private ImageView image;

        public CustomViewHolder(View itemView, Context context) {
            super(itemView);
            image = itemView.findViewById(R.id.imageview_board_content);
        }

        @SuppressLint("DefaultLocale")
        public void bind(String imageUrl) {

            if (imageUrl != null) {
                Glide.with(itemView.getContext())
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.background_gray)
                                .error(R.drawable.background_gray))
                        .load(imageUrl)
                        .into(image);
                image.setClipToOutline(true);

            }
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
