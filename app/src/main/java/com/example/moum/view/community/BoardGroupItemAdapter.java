package com.example.moum.view.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.BoardGroupItem;

import java.util.ArrayList;

public class BoardGroupItemAdapter extends RecyclerView.Adapter<BoardGroupItemAdapter.CustomViewHolder> {
    private ArrayList<BoardGroupItem> itemList;

    public BoardGroupItemAdapter(ArrayList<BoardGroupItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_board_group;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public void updateItemList(ArrayList<BoardGroupItem> boarditemList) {
        this.itemList = boarditemList;
        notifyDataSetChanged();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView content, writer;
        private ImageView image;

        public CustomViewHolder(View itemView) {
            super(itemView);
            writer = itemView.findViewById(R.id.item_board_group_writer);
            content = itemView.findViewById(R.id.item_board_group_content);
            image = itemView.findViewById(R.id.item_board_group_image_view);
        }

        public void bind(BoardGroupItem item) {
            content.setText(item.getContent());
            writer.setText(item.getWriter());

            if (item.hasImage() && image != null) {
                Glide.with(itemView.getContext())
                        .load(item.getImage())
                        .into(image);
            } else {
                image.setVisibility(View.GONE);
            }
        }

    }
}
