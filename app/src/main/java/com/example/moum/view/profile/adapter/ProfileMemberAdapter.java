package com.example.moum.view.profile.adapter;

import android.content.Context;
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
import com.example.moum.view.profile.TeamProfileFragment;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Member> members;
    private Context context;
    private TeamProfileFragment teamProfileFragment;

    public void setMembers(ArrayList<Member> members, Context context, TeamProfileFragment teamProfileFragment) {
        this.members = members;
        this.context = context;
        this.teamProfileFragment = teamProfileFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_member, parent, false);
        return new ProfileMemberAdapter.ProfileMemberViewHolder(view, context, teamProfileFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Member member = members.get(position);
        ((ProfileMemberAdapter.ProfileMemberViewHolder) holder).bind(member);

    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    static class ProfileMemberViewHolder extends RecyclerView.ViewHolder {
        private Member member;
        private TextView memberName;
        private CircleImageView memberProfile;
        private Context context;
        private TeamProfileFragment teamProfileFragment;

        public ProfileMemberViewHolder(@NonNull View itemView, Context context, TeamProfileFragment teamProfileFragment) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
            memberProfile = itemView.findViewById(R.id.member_profile);
            this.context = context;
            this.teamProfileFragment = teamProfileFragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Member member) {
            this.member = member;
            memberName.setText(member.getName());
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray)
                            .error(R.drawable.background_circle_gray))
                    .load(member.getProfileImageUrl()).into(memberProfile);
            memberProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    teamProfileFragment.onProfileMemberClicked(member.getId());
                }
            });
        }
    }
}
