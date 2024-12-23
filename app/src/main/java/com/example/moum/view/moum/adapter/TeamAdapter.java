package com.example.moum.view.moum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moum.R;
import com.example.moum.data.entity.Moum;
import com.example.moum.data.entity.Team;
import com.example.moum.view.dialog.TeamLeaveDialog;
import com.example.moum.view.moum.MyMoumFragment;
import com.example.moum.view.moum.TeamCreateActivity;
import com.example.moum.view.moum.TeamUpdateActivity;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Team> teams;
    private ArrayList<ArrayList<Moum>> moumsOfTeams;
    private Integer myId;
    private Context context;
    private ActivityResultLauncher<Intent> launcher;
    private MyMoumFragment myMoumFragment;
    private static final int VIEW_TYPE_EXIST = 1;
    private static final int VIEW_TYPE_EMPTY = 2;
    private final String TAG = getClass().toString();

    public void setTeamsNMoums(ArrayList<Team> teams, ArrayList<ArrayList<Moum>> moumsOfTeams, Integer myId, Context context,
            ActivityResultLauncher<Intent> launcher, MyMoumFragment myMoumFragment) {
        this.teams = teams;
        this.moumsOfTeams = moumsOfTeams;
        this.myId = myId;
        this.context = context;
        this.launcher = launcher;
        this.myMoumFragment = myMoumFragment;
    }

    @Override
    public int getItemViewType(int position) {
        return position == teams.size() - 1 ? VIEW_TYPE_EMPTY : VIEW_TYPE_EXIST;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_empty, parent, false);
            return new TeamAdapter.TeamEmptyViewHolder(view, context);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
            return new TeamAdapter.TeamExistViewHolder(view, context, myId, myMoumFragment);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Log.e(TAG, "position: " + position + " team: " + teams.toString());
        Team team = teams.get(position);
        ArrayList<Moum> moums = moumsOfTeams.get(position);
        if (holder instanceof TeamAdapter.TeamEmptyViewHolder) {
            ((TeamAdapter.TeamEmptyViewHolder) holder).bind(team, moums);
        } else if (holder instanceof TeamAdapter.TeamExistViewHolder) {
            ((TeamAdapter.TeamExistViewHolder) holder).bind(team, moums, launcher);
        }
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class TeamExistViewHolder extends RecyclerView.ViewHolder {
        private Team team;
        private ArrayList<Moum> moums;
        private Integer myId;
        private ImageView teamProfile;
        private ImageView teamMembersProfile;
        private TextView teamMembers;
        private TextView teamUpdate;
        private TextView teamLeave;
        private TextView teamName;
        private TextView teamDescription;
        private RecyclerView moumRecycler;
        private Context context;
        private MyMoumFragment myMoumFragment;

        public TeamExistViewHolder(@NonNull View itemView, Context context, Integer myId, MyMoumFragment myMoumFragment) {
            super(itemView);
            this.myId = myId;
            teamProfile = itemView.findViewById(R.id.imageview_team_profile);
            teamMembersProfile = itemView.findViewById(R.id.imageview_members);
            teamMembers = itemView.findViewById(R.id.textview_team_members);
            teamUpdate = itemView.findViewById(R.id.textview_update);
            teamLeave = itemView.findViewById(R.id.textview_leave);
            teamName = itemView.findViewById(R.id.textview_team_name);
            teamDescription = itemView.findViewById(R.id.textview_team_description);
            moumRecycler = itemView.findViewById(R.id.recycler_moum);
            this.context = context;
            this.myMoumFragment = myMoumFragment;
        }

        @SuppressLint("DefaultLocale")
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Team team, ArrayList<Moum> moums, ActivityResultLauncher<Intent> launcher) {
            this.team = team;
            this.moums = moums;
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_gray)
                            .error(R.drawable.background_gray))
                    .load(team.getFileUrl()).into(teamProfile);
            Glide.with(context)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .placeholder(R.drawable.background_circle_gray_size_fit)
                            .error(R.drawable.background_circle_gray_size_fit))
                    .load(team.getMembers().get(0).getProfileImageUrl()).into(teamMembersProfile);
            if (team.getMembers().size() < 2) {
                teamMembers.setText(team.getMembers().get(0).getName());
            } else {
                teamMembers.setText(String.format("%s 외 %d명", team.getMembers().get(0).getName(), team.getMembers().size() - 1));
            }
            teamName.setText(team.getTeamName());
            teamDescription.setText(team.getDescription());

            /*모음 리사이클러뷰 연결*/
            MoumAdapter moumAdapter = new MoumAdapter();
            moumAdapter.setMoums(moums, team.getTeamId(), context, launcher);
            moumRecycler.setLayoutManager(new LinearLayoutManager(context));
            moumRecycler.setAdapter(moumAdapter);

            /*단체장이라면 수정하기 버튼 보이기*/
            if (team.getLeaderId().equals(myId)) {
                teamUpdate.setVisibility(View.VISIBLE);
                teamLeave.setVisibility(View.GONE);
                teamUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, TeamUpdateActivity.class);
                        intent.putExtra("teamId", team.getTeamId());
                        intent.putExtra("leaderId", team.getLeaderId());
                        context.startActivity(intent);
                    }
                });
            } else {
                teamUpdate.setVisibility(View.GONE);
                teamLeave.setVisibility(View.VISIBLE);
                teamLeave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TeamLeaveDialog teamLeaveDialog = new TeamLeaveDialog(context, myMoumFragment, team.getTeamName());
                        teamLeaveDialog.show();
                    }
                });
            }
        }
    }

    static class TeamEmptyViewHolder extends RecyclerView.ViewHolder {
        private Team team;
        private ArrayList<Moum> moums;
        private Context context;
        private ConstraintLayout teamCreateButton;

        public TeamEmptyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            teamCreateButton = itemView.findViewById(R.id.constraint_my_moum_header);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void bind(Team team, ArrayList<Moum> moums) {
            this.team = team;
            this.moums = moums;
            teamCreateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TeamCreateActivity.class);
                    context.startActivity(intent);
                }
            });

        }
    }
}
