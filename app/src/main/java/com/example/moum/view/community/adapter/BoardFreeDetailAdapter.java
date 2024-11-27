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

    public BoardFreeDetailAdapter(Article articleItem, ArrayList<Comment> commentList) {
        this.articleItem = articleItem;
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

    // 게시글 데이터 업데이트
    public void updateArticleData(Article article) {
        this.articleItem = article;
        notifyDataSetChanged(); // 게시글 데이터가 갱신되면 RecyclerView 갱신
    }

    // 댓글 데이터 업데이트
    public void updateCommentData(ArrayList<Comment> comments) {
        this.commentList = comments;
        notifyDataSetChanged(); // 댓글 데이터가 갱신되면 RecyclerView 갱신
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ARTICLE) {
            // 게시글
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_board_free_detail, parent, false);
            return new ArticleViewHolder(view);
        } else {
            // 댓글
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_board_free_comment, parent, false);
            return new CommentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_ARTICLE) {
            ((ArticleViewHolder) holder).bind(articleItem); // 게시글 데이터 바인딩
        } else {
            Comment comment = commentList.get(position - 1); // 댓글 데이터 (position - 1)
            ((CommentViewHolder) holder).bind(comment);
        }
    }

    // 게시글 ViewHolder
    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView title, content, writer, timestamp;
        private TextView likeCounts;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            writer = itemView.findViewById(R.id.item_board_free_detail_writer);
            timestamp = itemView.findViewById(R.id.item_board_free_detail_time);
            title = itemView.findViewById(R.id.item_board_free_detail_title);
            content = itemView.findViewById(R.id.item_board_free_detail_content);
            likeCounts = itemView.findViewById(R.id.item_board_free_detail_like_count);
        }

        public void bind(Article article) {
            title.setText(article.getTitle());
            content.setText(article.getContent());
            writer.setText(article.getAuthor());
            timestamp.setText(TimeAgo.getTimeAgo(article.getCreateAt()));
            likeCounts.setText(String.valueOf(article.getLikeCounts()));
        }
    }

    // 댓글 ViewHolder
    static class CommentViewHolder extends RecyclerView.ViewHolder {
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
