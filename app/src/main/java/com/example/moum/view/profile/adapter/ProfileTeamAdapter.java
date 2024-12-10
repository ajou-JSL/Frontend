package com.example.moum.view.profile.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Team;
import com.example.moum.view.profile.MemberProfileFragment;

import java.util.ArrayList;

public class ProfileTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Team> teams;
    private Context context;
    private MemberProfileFragment memberProfileFragment;

    public void setTeams(ArrayList<Team> teams, Context context, MemberProfileFragment memberProfileFragment) {
        this.teams = teams;
        this.context = context;
        this.memberProfileFragment = memberProfileFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_team, parent, false);
        return new ProfileTeamAdapter.ProfileTeamViewHolder(view, context, memberProfileFragment);
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

    static class ProfileTeamViewHolder extends RecyclerView.ViewHolder {
        private Team team;
        private TextView teamName;
        private ImageView teamProfile;
        private Context context;
        private MemberProfileFragment memberProfileFragment;

        public ProfileTeamViewHolder(@NonNull View itemView, Context context, MemberProfileFragment memberProfileFragment) {
            super(itemView);
            teamName = itemView.findViewById(R.id.team_name);
            teamProfile = itemView.findViewById(R.id.team_profile);
            this.context = context;
            this.memberProfileFragment = memberProfileFragment;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Team team) {
            this.team = team;
            teamName.setText(team.getTeamName());
            teamProfile.setClipToOutline(true);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_more_rounded_gray_size_fit)
                            .error(R.drawable.background_more_rounded_gray_size_fit))
                    .load(team.getFileUrl()).into(teamProfile);
            teamProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    memberProfileFragment.onProfileTeamClicked(team.getTeamId());
                }
            });
        }
    }
}
