package com.example.moum.view.moum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
import com.example.moum.view.chat.adapter.ChatroomParticipantAdapter;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoumManageMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Member> members;
    private Context context;

    public void setMembers(ArrayList<Member> members, Context context) {
        this.members = members;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MoumManageMemberAdapter.MoumManageMemberViewModel(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Member member = members.get(position);
        ((MoumManageMemberAdapter.MoumManageMemberViewModel) holder).bind(member);

    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class MoumManageMemberViewModel extends RecyclerView.ViewHolder {
        private Member member;
        private TextView memberName;
        private CircleImageView memberProfile;
        private Context context;

        public MoumManageMemberViewModel(@NonNull View itemView, Context context) {
            super(itemView);
            memberName = itemView.findViewById(R.id.participant_name);
            memberProfile = itemView.findViewById(R.id.participant_profile);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Member member) {
            this.member = member;
            memberName.setText(member.getName());
            if (member.getProfileImageUrl() != null) {
                Glide.with(context)
                        .applyDefaultRequestOptions(new RequestOptions()
                                .placeholder(R.drawable.background_circle_gray_size_fit)
                                .error(R.drawable.background_circle_gray_size_fit))
                        .load(member.getProfileImageUrl()).into(memberProfile);
            }
        }
    }
}
