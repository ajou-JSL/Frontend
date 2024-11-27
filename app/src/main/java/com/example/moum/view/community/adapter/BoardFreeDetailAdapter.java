package com.example.moum.view.community.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;

import java.util.ArrayList;

public class BoardFreeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_COMMENT = 0;

    private Article articleItem;
    private ArrayList<Comment> commentList;
    private AdapterView.OnItemClickListener onItemClickListener;

    public BoardFreeDetailAdapter(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return 1 + (commentList != null ? commentList.size() : 0);
    }

    // 댓글 데이터 업데이트
    public void updateComment(ArrayList<Comment> comments) {
        this.commentList = comments;
        notifyDataSetChanged(); // 댓글 데이터가 갱신되면 RecyclerView 갱신
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board_free_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        ((CommentViewHolder) holder).bind(comment);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView commentContent, commentWriter, commentTimestamp;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentWriter = itemView.findViewById(R.id.item_board_free_comment_writer);
            commentTimestamp = itemView.findViewById(R.id.item_board_free_comment_time);
            commentContent = itemView.findViewById(R.id.item_board_free_comment_content);
        }

        public void bind(Comment comment) {
            commentWriter.setText(comment.getAuthor());
            commentTimestamp.setText(TimeAgo.getTimeAgo(comment.getCreateAt()));
            commentContent.setText(comment.getContent());
        }
    }
}
