package com.example.moum.view.community.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.data.entity.BoardFreeItem;
import com.example.moum.R;
import com.example.moum.view.community.BoardFreeDetailActivity;

import java.util.ArrayList;

public class BoardFreeItemAdapter extends RecyclerView.Adapter<BoardFreeItemAdapter.CustomViewHolder> {
    private ArrayList<BoardFreeItem> itemList;

    public BoardFreeItemAdapter(ArrayList<BoardFreeItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_board_free_image;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateItemList(ArrayList<BoardFreeItem> boarditemList) {
        this.itemList = boarditemList;
        notifyDataSetChanged();
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

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            Log.d("BoardFreeItemAdapter", "Item clicked at position: " + position);

            // 클릭 시 Toast 메시지로 확인
            Toast.makeText(v.getContext(), "Item clicked at position: " + position, Toast.LENGTH_SHORT).show();

            // BoardId를 Intent에 전달
            Intent intent = new Intent(v.getContext(), BoardFreeDetailActivity.class);
            intent.putExtra("targetBoardId", itemList.get(position).getBoardId());

            // Activity 시작
            v.getContext().startActivity(intent);
        });
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView title, writer, time;
        private ImageView image;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_board_free_title);
            writer = itemView.findViewById(R.id.item_board_free_writer);
            time = itemView.findViewById(R.id.item_board_free_time);
            image = itemView.findViewById(R.id.item_board_free_image_view);
        }

        public void bind(BoardFreeItem item) {
            title.setText(item.getTitle());
            writer.setText(item.getWriter());
            time.setText(item.getTime());

            if (item.hasImage()) {
                // 이미지가 있을 때만 보이게 설정
                image.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(item.getImage())
                        .into(image);
            } else {
                // 이미지가 없으면 숨기기
                image.setVisibility(View.GONE);
            }
        }
    }
}
