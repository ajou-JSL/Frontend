package com.example.moum.view.community.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.utils.TimeAgo;
import com.example.moum.view.community.BoardRecruitDetailActivity;

import java.util.ArrayList;

public class BoardRecruitItemAdapter extends RecyclerView.Adapter<BoardRecruitItemAdapter.CustomViewHolder> {
    private ArrayList<Article> itemList;

    public BoardRecruitItemAdapter(ArrayList<Article> itemList) {
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

    public void updateItemList(ArrayList<Article> articles) {
        this.itemList = articles;
        notifyDataSetChanged();
    }

    public void addItemList(ArrayList<Article> boardFreeItems) {
        this.itemList.addAll(boardFreeItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BoardRecruitItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new BoardRecruitItemAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.bind(itemList.get(position));

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            // BoardId를 Intent에 전달
            Intent intent = new Intent(v.getContext(), BoardRecruitDetailActivity.class);
            intent.putExtra("targetBoardId", itemList.get(position).getId());

            // Activity 시작
            v.getContext().startActivity(intent);
        });
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView title, writer, time, counts;
        private ImageView image;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_board_free_title);
            writer = itemView.findViewById(R.id.item_board_free_writer);
            time = itemView.findViewById(R.id.item_board_free_time);
            counts = itemView.findViewById(R.id.item_board_free_comments_and_views);
            image = itemView.findViewById(R.id.item_board_free_image_view);
        }

        public void bind(Article article) {
            title.setText(article.getTitle());
            writer.setText(article.getAuthor());
            time.setText(TimeAgo.getTimeAgo(article.getCreateAt()));
            String count = "[" + article.getCommentsCounts() + "] / " + article.getViewCounts() + " 회";
            counts.setText(count);

            if (article.getFileURL() != null) {
                // 이미지가 있을 때만 보이게 설정
                image.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(article.getFileURL().get(0))
                        .into(image);
            } else {
                // 이미지가 없으면 숨기기
                image.setVisibility(View.GONE);
            }
        }
    }
}
