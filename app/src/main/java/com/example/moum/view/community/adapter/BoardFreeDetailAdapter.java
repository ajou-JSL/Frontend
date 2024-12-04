package com.example.moum.view.community.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moum.R;
import com.example.moum.data.entity.Article;
import com.example.moum.data.entity.Comment;
import com.example.moum.utils.TimeAgo;
import com.example.moum.view.community.BoardFreeDetailActivity;
import com.example.moum.view.community.BoardFreeWriteActivity;

import java.util.ArrayList;

public class BoardFreeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_COMMENT = 0;

    private Article articleItem;
    private ArrayList<Comment> commentList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Context context;

    public BoardFreeDetailAdapter(ArrayList<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return (commentList != null ? commentList.size() : 0);
    }

    // 댓글 데이터 업데이트
    public void updateComment(ArrayList<Comment> comments) {
        this.commentList = comments;
        notifyDataSetChanged();
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
        private ImageView profileImage;
        private ImageButton menu;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentWriter = itemView.findViewById(R.id.item_board_free_comment_writer);
            commentTimestamp = itemView.findViewById(R.id.item_board_free_comment_time);
            commentContent = itemView.findViewById(R.id.item_board_free_comment_content);

            profileImage = itemView.findViewById(R.id.board_free_detail_image);
            menu = itemView.findViewById(R.id.menu);

            menu.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();  // 아이템 포지션 가져오기
                if (position != RecyclerView.NO_POSITION) {
                    showPopupMenu(v, position);  // 포지션을 함께 전달
                }
            });
        }

        public void bind(Comment comment) {
            commentWriter.setText(comment.getAuthor());
            commentTimestamp.setText(TimeAgo.getTimeAgo(comment.getCreateAt()));
            commentContent.setText(comment.getContent());
        }

        private void showPopupMenu(View view, int position) {
            if (context instanceof BoardFreeDetailActivity) {  // 액티비티가 맞는지 확인
                ((BoardFreeDetailActivity) context).commentPopupMenu(view, position);
            }
        }

    }
}
