package com.example.moum.view.profile.adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import com.example.moum.data.entity.Team;
import com.example.moum.view.chat.adapter.ChatroomParticipantAdapter;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Team> teams;
    private Context context;

    public void setTeams(ArrayList<Team> teams, Context context) {
        this.teams = teams;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_team, parent, false);
        return new ProfileTeamAdapter.ProfileTeamViewHolder(view, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Team team = teams.get(position);
        ((ProfileTeamAdapter.ProfileTeamViewHolder) holder).bind(team);

    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class ProfileTeamViewHolder extends RecyclerView.ViewHolder{
        private Team team;
        private TextView teamName;
        private CircleImageView teamProfile;
        private Context context;

        public ProfileTeamViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            teamName = itemView.findViewById(R.id.team_name);
            teamProfile = itemView.findViewById(R.id.team_profile);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Team team){
            this.team = team;
            teamName.setText(team.getTeamName());
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                    .placeholder(R.drawable.background_more_rounded_gray)
                    .error(R.drawable.background_more_rounded_gray))
                    .load(team.getFileUrl()).into(teamProfile);
        }
    }
}
