package com.example.moum.view.chat.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.utils.ImageManager;
import com.example.moum.view.chat.ChatMemberListFragment;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatroomMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Member> members;
    private Context context;
    private Integer leaderId;
    private ChatMemberListFragment chatMemberListFragment;

    public void setMembers(ArrayList<Member> members, Integer leaderId, Context context, ChatMemberListFragment chatMemberListFragment) {
        this.members = members;
        this.leaderId = leaderId;
        this.context = context;
        this.chatMemberListFragment = chatMemberListFragment;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_member, parent, false);
        return new ChatroomMemberAdapter.ChatroomMemberViewHolder(view, leaderId, context, chatMemberListFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Member member = members.get(position);
        ((ChatroomMemberAdapter.ChatroomMemberViewHolder) holder).bind(member);

    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class ChatroomMemberViewHolder extends RecyclerView.ViewHolder{
        private ChatMemberListFragment chatMemberListFragment;
        private Integer leaderId;
        private Member member;
        private TextView memberName;
        private CircleImageView memberProfile;
        private Context context;

        public ChatroomMemberViewHolder(@NonNull View itemView, Integer leaderId, Context context, ChatMemberListFragment chatMemberListFragment) {
            super(itemView);
            this.leaderId = leaderId;
            memberName = itemView.findViewById(R.id.chat_member_nickname);
            memberProfile = itemView.findViewById(R.id.chat_member_profile);
            this.context = context;
            this.chatMemberListFragment = chatMemberListFragment;
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Member member){
            this.member = member;

            // 리더인 경우
            if(member.getId().equals(leaderId)){
                memberName.setText(String.format("[단체장] %s", member.getName()));
                memberName.setTextColor(Color.rgb(42, 200, 189));
                if(ImageManager.isUrlValid(member.getProfileImageUrl()))
                    Glide.with(context)
                            .load(member.getProfileImageUrl())
                            .apply(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray_size_fit)
                            .error(R.drawable.background_circle_gray_size_fit))
                            .into(memberProfile);
            }
            //리더가 아닌 경우
            else{
                memberName.setText(member.getName());
                if(ImageManager.isUrlValid(member.getProfileImageUrl()))
                    Glide.with(context)
                            .load(member.getProfileImageUrl())
                            .apply(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray_size_fit)
                            .error(R.drawable.background_circle_gray_size_fit))
                            .into(memberProfile);
            }
            memberProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatMemberListFragment.onProfileClicked(member.getId());
                }
            });
        }
    }
}
