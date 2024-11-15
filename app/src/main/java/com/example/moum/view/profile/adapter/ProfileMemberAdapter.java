package com.example.moum.view.profile.adapter;

import android.content.Context;
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
import com.example.moum.R;
import com.example.moum.data.entity.Member;
import com.example.moum.data.entity.Team;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Member> members;
    private Context context;

    public void setMembers(ArrayList<Member> members, Context context) {
        this.members = members;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_team, parent, false);
        return new ProfileMemberAdapter.ProfileMemberViewHolder(view, context);
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

    static class ProfileMemberViewHolder extends RecyclerView.ViewHolder{
        private Member member;
        private TextView memberName;
        private CircleImageView memberProfile;
        private Context context;
        private final String TAG = getClass().toString();


        public ProfileMemberViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            memberName = itemView.findViewById(R.id.team_name);
            memberProfile = itemView.findViewById(R.id.team_profile);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Member member){
            this.member = member;
            memberName.setText(member.getName());
            Glide.with(context).load(member.getProfileImageUrl()).into(memberProfile);
        }
    }
}
