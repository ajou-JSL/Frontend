package com.example.moum.view.community.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Comment;
import com.example.moum.utils.TimeAgo;
import com.example.moum.view.community.BoardRecruitDetailActivity;
import com.example.moum.viewmodel.community.BoardRecruitDetailViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoardRecruitDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_COMMENT = 0;

    private final BoardRecruitDetailViewModel boardRecruitDetailViewModel;
    private List<Comment> commentList;
    private AdapterView.OnItemClickListener onItemClickListener;
    private final Context context;
    private com.example.moum.view.community.adapter.BoardFreeDetailAdapter.OnProfileClickListener profileClickListener;

    public Comment getCommentAt(int position) {
        if (position >= 0 && position < commentList.size()) {
            return commentList.get(position);
        }
        return null; // 범위를 벗어난 경우 null 반환
    }

    public interface OnProfileClickListener {
        void onProfileClick(int position); // 포지션이나 다른 데이터 전달
    }

    public BoardRecruitDetailAdapter(ArrayList<Comment> commentList, Context context, BoardRecruitDetailViewModel boardRecruitDetailViewModel) {
        this.commentList = commentList;
        this.context = context;
        this.boardRecruitDetailViewModel = boardRecruitDetailViewModel;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        return (commentList != null ? commentList.size() : 0);
    }


    public void updateComment(List<Comment> comments) {
        for (Comment comment : commentList) {
            Log.e("updateComment", "Comment: " + comment.toString());
        }
        int previousSize = this.commentList.size();
        this.commentList.clear();
        this.commentList.addAll(comments);
        notifyItemInserted(previousSize);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board_free_comment, parent, false);
        return new com.example.moum.view.community.adapter.BoardRecruitDetailAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        ((com.example.moum.view.community.adapter.BoardFreeDetailAdapter.CommentViewHolder) holder).bind(comment);
    }

    public void setOnProfileClickListener(com.example.moum.view.community.adapter.BoardFreeDetailAdapter.OnProfileClickListener listener) {
        this.profileClickListener = listener;
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
            profileImage = itemView.findViewById(R.id.item_board_free_comment_image);
            menu = itemView.findViewById(R.id.menu);

            menu.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    showPopupMenu(v, position);
                }
            });

            profileImage.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && profileClickListener != null) {
                    profileClickListener.onProfileClick(position);
                }
            });
        }

        public void bind(Comment comment) {
            commentWriter.setText(comment.getAuthor());
            commentTimestamp.setText(TimeAgo.getTimeAgo(comment.getCreateAt()));
            commentContent.setText(comment.getContent());

            // context가 LifecycleOwner인지 확인 후 처리
            if (context instanceof LifecycleOwner) {
                LifecycleOwner lifecycleOwner = (LifecycleOwner) context;

                // ViewModel에서 멤버 정보를 가져오고 observe를 설정
                boardRecruitDetailViewModel.getIsLoadMemberSuccess().observe(lifecycleOwner, member -> {
                    if (member != null && member.getData().getProfileImageUrl() != null) {
                        Glide.with(profileImage.getContext())
                                .load(member.getData().getProfileImageUrl())
                                .apply(new RequestOptions().circleCrop())
                                .into(profileImage);
                    } else {
                        profileImage.setImageResource(R.drawable.background_circle_darkgray);
                    }
                });
            }
            boardRecruitDetailViewModel.loadProfileImage(comment.getAuthorId());

        }

        private void showPopupMenu(View view, int position) {
            if (context instanceof BoardRecruitDetailActivity) {  // 액티비티가 맞는지 확인
                ((BoardRecruitDetailActivity) context).commentPopupMenu(view, position);
            }
        }

    }
}
